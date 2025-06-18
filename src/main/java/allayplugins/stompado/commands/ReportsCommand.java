package allayplugins.stompado.commands;

import allayplugins.stompado.inventories.ReportInventory;
import org.bukkit.entity.Player;

public class ReportsCommand {

    @CommandBase(name = "reports", aliases = "reportes", permission = "allayreports.manager.reports", allowedConsole = false)
    public void reportsCommand(Player player) {
        new ReportInventory().open(player);
    }
}
