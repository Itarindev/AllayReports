package allayplugins.stompado.commands;

import allayplugins.stompado.AllayReports;
import allayplugins.stompado.configuration.MessagesConfiguration;
import allayplugins.stompado.dao.ReportDAO;
import allayplugins.stompado.dao.UserDAO;
import allayplugins.stompado.enuns.State;
import allayplugins.stompado.manager.ReportManager;
import allayplugins.stompado.manager.UserManager;
import allayplugins.stompado.model.Report;
import allayplugins.stompado.model.User;
import allayplugins.stompado.utils.PlayerUtils;
import lombok.val;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ReportCommand {

    private AllayReports main;

    private final MessagesConfiguration messagesConfiguration;

    private final ReportManager reportManager;
    private final UserManager userManager;

    public ReportCommand(AllayReports main) {
        this.main = main;

        messagesConfiguration = main.getMessagesConfiguration();

        reportManager = main.getReportManager();
        userManager = main.getUserManager();
    }

    @CommandBase(name = "report", aliases = "reportar", allowedConsole = false)
    public void reportCommand(Player player, String[] args) {
        if (args.length != 2) {
            player.sendMessage("§c[ ! ] Utilize /reportar (jogador) (motivo).");
            return;
        }

        val target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            PlayerUtils.sendMessage(player, messagesConfiguration.getPlayerNotFound());
            return;
        }

        if (player.equals(target)) {
            PlayerUtils.sendMessage(player, messagesConfiguration.getReportedIsVictim());
            return;
        }

        if (target.hasPermission("allayreports.bypass.reports")) {
            PlayerUtils.sendMessage(player, messagesConfiguration.getReportedIsStaff());
            return;
        }

        val targetUser = UserDAO.findUserByName(target.getName());
        if (targetUser != null) {
            targetUser.setCount(targetUser.getCount() + 1);
        }

        val id = RandomStringUtils.random(12, true, true);

        val report = new Report(id, player.getName(), target.getName(), args[1], State.PENDING, System.currentTimeMillis());
        ReportDAO.getReports().add(report);
        reportManager.insert(report);

        val user = new User(target.getUniqueId().toString(), target.getName(), 1, true);
        UserDAO.getUsers().add(user);

        messagesConfiguration.getSendReport().forEach(sendReport -> {
            player.sendMessage(sendReport.replace("{reported_name}", target.getName())
                    .replace("{report_reason}", args[1]).replace("&", "§"));
        });

        Bukkit.getOnlinePlayers().forEach(players -> {
            if (players.hasPermission("allayreports.view.reports")) {
                messagesConfiguration.getReceivedReport().forEach(receivedReport -> players.sendMessage(receivedReport.replace("&", "§")));
            }
        });
    }
}