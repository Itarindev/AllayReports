package allayplugins.stompado.inventories;

import allayplugins.stompado.AllayReports;
import allayplugins.stompado.configuration.InventoriesConfiguration;
import allayplugins.stompado.manager.ReportManager;
import allayplugins.stompado.utils.ItemBuilder;
import allayplugins.stompado.utils.ScrollerBuilder;
import lombok.val;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HistoricInventory {

    private AllayReports main;

    private final InventoriesConfiguration inventoriesConfiguration;

    private final ReportManager reportManager;

    public HistoricInventory(AllayReports main) {
        this.main = main;

        inventoriesConfiguration = main.getInventoriesConfiguration();

        reportManager = main.getReportManager();
    }

    public void open(Player player, String target) {
        val items = new ArrayList<ItemStack>();
        reportManager.getReportsAgainstPlayer(target).forEach(report -> {

            val title = inventoriesConfiguration.getIconTitle().replace("{reported}", report.getReported());

            List<String> lore = inventoriesConfiguration.getIconLore();
            lore = lore.stream().map(l -> l.replace("{victim}", report.getVictim())
                            .replace("{reported}", report.getReported())
                            .replace("{reason}", report.getReason())
                            .replace("{state}", report.getState().getColoredName())
                            .replace("{date}", report.getFormattedDate()))
                    .collect(Collectors.toList());

            val icon = new ItemBuilder(target).setName(title).setLore(lore).build();
            items.add(icon);
        });

        val inventoryName = ChatColor.translateAlternateColorCodes('&', inventoriesConfiguration.getHistoricName());

        new ScrollerBuilder().withInventoryName(inventoryName.replace("{player_name}", target)).withInventorySize(inventoriesConfiguration.getHistoricSize() * 9).withItems(items).withSlots(inventoriesConfiguration.getHistoricSlots()).open(player, 0);
    }
}