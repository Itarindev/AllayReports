package allayplugins.stompado.inventories;

import allayplugins.stompado.AllayReports;
import allayplugins.stompado.configuration.InventoriesConfiguration;
import allayplugins.stompado.dao.ReportDAO;
import allayplugins.stompado.utils.ItemBuilder;
import allayplugins.stompado.utils.ScrollerBuilder;
import lombok.val;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReportInventory {

    private final InventoriesConfiguration inventoriesConfiguration = AllayReports.getInstance().getInventoriesConfiguration();

    public void open(Player player) {
        val items = new ArrayList<ItemStack>();
        ReportDAO.getReports().forEach(report -> {
            List<String> lore = inventoriesConfiguration.getHeadLore();
            lore = lore.stream().map(l -> l.replace("{victim}", report.getVictim())
                    .replace("{reported}", report.getReported())
                    .replace("{reason}", report.getReason())
                    .replace("{state}", report.getState().getColoredName())
                    .replace("{date}", report.getFormattedDate()))
                    .collect(Collectors.toList());

            val head = new ItemBuilder(report.getReported()).setName(inventoriesConfiguration.getHeadTitle().replace("{reported}", report.getReported())).setLore(lore).build();

            val nmsItem = CraftItemStack.asNMSCopy(head);
            val itemCompound = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
            itemCompound.setString("AllayReports-ID", report.getId());
            nmsItem.setTag(itemCompound);

            items.add(CraftItemStack.asBukkitCopy(nmsItem));
        });

        val inventoryName = ChatColor.translateAlternateColorCodes('&', inventoriesConfiguration.getReportsName());

        new ScrollerBuilder().withInventoryName(inventoryName).withInventorySize(inventoriesConfiguration.getReportsSize() * 9).withItems(items).withSlots(inventoriesConfiguration.getReportsSlots()).open(player, 0);
    }
}