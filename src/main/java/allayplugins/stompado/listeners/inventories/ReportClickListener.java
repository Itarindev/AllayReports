package allayplugins.stompado.listeners.inventories;

import allayplugins.stompado.AllayReports;
import allayplugins.stompado.configuration.InventoriesConfiguration;
import allayplugins.stompado.configuration.MessagesConfiguration;
import allayplugins.stompado.dao.ReportDAO;
import allayplugins.stompado.enuns.State;
import allayplugins.stompado.manager.ReportManager;
import allayplugins.stompado.utils.ChatAction;
import allayplugins.stompado.utils.PlayerUtils;
import lombok.val;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ReportClickListener implements Listener {

    private AllayReports main;

    private final MessagesConfiguration messagesConfiguration;
    private final InventoriesConfiguration inventoriesConfiguration;

    private final ReportManager reportManager;

    private final ChatAction chatAction;

    public ReportClickListener(AllayReports main) {
        this.main = main;
        Bukkit.getPluginManager().registerEvents(this, main);

        messagesConfiguration = main.getMessagesConfiguration();
        inventoriesConfiguration = main.getInventoriesConfiguration();

        reportManager = main.getReportManager();

        chatAction = main.getChatAction();
    }

    @EventHandler
    void onClick(InventoryClickEvent event) {
        val title = event.getView().getTitle();

        val inventoryName = ChatColor.translateAlternateColorCodes('&', inventoriesConfiguration.getReportsName());

        if (!title.equals(inventoryName)) return;
        event.setCancelled(true);

        val item = event.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) return;

        val nmsItem = CraftItemStack.asNMSCopy(item);
        val itemCompound = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();

        if (!itemCompound.hasKey("AllayReports-ID")) return;

        val click = event.getClick();

        val player = (Player)event.getWhoClicked();

        val report = ReportDAO.findReportById(itemCompound.getString("AllayReports-ID"));
        if (report == null) return;

        switch (click) {
            case RIGHT:
                reportManager.remove(report);
                player.closeInventory();
                PlayerUtils.sendMessage(player, messagesConfiguration.getExcludedReport());
                break;
            case DROP:
                player.closeInventory();
                for (val errorMessage : messagesConfiguration.getChangeState()) {
                    val finalMessage = ChatColor.translateAlternateColorCodes('&', errorMessage);
                    player.sendMessage(finalMessage);
                }

                chatAction.addAction(player, (sender, message) -> {
                    if (message.equalsIgnoreCase("cancelar") || message.equalsIgnoreCase("cancel")) {
                        return;
                    }

                    val newState = State.fromPlainName(message);
                    if (newState == null) {
                        player.sendMessage("§c[ ! ] Status inválido. Use: PENDENTE, RESOLVIDO ou FALSO.");
                        return;
                    }

                    report.setState(newState);
                    reportManager.updateState(report.getId(), newState);
                    ReportDAO.getReports().remove(report);
                    player.closeInventory();
                    PlayerUtils.sendMessage(player, messagesConfiguration.getChangedState().replace("{new_state}", newState.getColoredName()));
                });

                break;
            case LEFT:
                player.closeInventory();
                player.teleport(report.getPlayer());
                PlayerUtils.sendMessage(player, messagesConfiguration.getTeleportedThePlayer().replace("{reported_name}", report.getPlayer().getName()));
                break;
        }
    }
}