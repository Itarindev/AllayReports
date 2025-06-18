package allayplugins.stompado.listeners.inventories;

import allayplugins.stompado.AllayReports;
import allayplugins.stompado.configuration.InventoriesConfiguration;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class HistoricClickListener implements Listener {

    private AllayReports main;

    private final InventoriesConfiguration inventoriesConfiguration;

    public HistoricClickListener(AllayReports main) {
        this.main = main;
        Bukkit.getPluginManager().registerEvents(this, main);

        inventoriesConfiguration = main.getInventoriesConfiguration();
    }

    @EventHandler
    void onClickHistoric(InventoryClickEvent event) {
        val title = event.getView().getTitle();

        val inventoryName = ChatColor.translateAlternateColorCodes('&', inventoriesConfiguration.getHistoricName());

        val prefix = inventoryName.split("\\{player_name}")[0];

        if (!title.startsWith(prefix)) return;
        event.setCancelled(true);
    }
}