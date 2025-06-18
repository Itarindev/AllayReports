package allayplugins.stompado.commands;

import allayplugins.stompado.AllayReports;
import allayplugins.stompado.configuration.MessagesConfiguration;
import allayplugins.stompado.inventories.HistoricInventory;
import allayplugins.stompado.utils.PlayerUtils;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class HistoricCommand {

    private final AllayReports main;

    private final MessagesConfiguration messagesConfiguration;

    public HistoricCommand(AllayReports main) {
        this.main = main;

        messagesConfiguration = main.getMessagesConfiguration();
    }

    @CommandBase(name = "hreports", aliases = "hreportes", permission = "allayreports.historic.reports", allowedConsole = false)
    public void reportViewCommand(Player player, String[] args) {
        if (args.length != 1) {
            player.sendMessage("§c[ ! ] Utilize /hreportes (jogador).");
            return;
        }

        val target = Bukkit.getOfflinePlayer(args[0]);
        if (target == null) {
            PlayerUtils.sendMessage(player, messagesConfiguration.getPlayerNotFound());
            return;
        }

        new HistoricInventory(main).open(player, target.getName());
    }
}