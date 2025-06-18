package allayplugins.stompado.configuration;

import allayplugins.stompado.AllayReports;
import allayplugins.stompado.utils.MultipleFiles;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

@Getter
public class InventoriesConfiguration {

    private AllayReports main;

    private String reportsName;
    private int reportsSize;
    private List<Integer> reportsSlots;
    private String headTitle;
    private List<String> headLore;

    private String historicName;
    private int historicSize;
    private List<Integer> historicSlots;
    private String iconTitle;
    private List<String> iconLore;

    private final FileConfiguration configuration;

    public InventoriesConfiguration(AllayReports main) {
        this.main = main;

        configuration = MultipleFiles.getConfig(main, "inventories");
    }

    public void loadConfigurationInventories() {

        reportsName = configuration.getString("Inventories.Reports.Name");
        reportsSize = configuration.getInt("Inventories.Reports.Size");
        reportsSlots = configuration.getIntegerList("Inventories.Reports.Slots");
        headTitle = configuration.getString("Inventories.Reports.Item.Title");
        headLore = configuration.getStringList("Inventories.Reports.Item.Lore");

        historicName = configuration.getString("Inventories.Historic.Name");
        historicSize = configuration.getInt("Inventories.Historic.Size");
        historicSlots = configuration.getIntegerList("Inventories.Historic.Slots");
        iconLore = configuration.getStringList("Inventories.Historic.Item.Lore");
        iconTitle = configuration.getString("Inventories.Historic.Item.Title");

    }
}