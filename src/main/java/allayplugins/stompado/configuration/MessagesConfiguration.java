package allayplugins.stompado.configuration;

import allayplugins.stompado.AllayReports;
import allayplugins.stompado.utils.MultipleFiles;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

@Getter
public class MessagesConfiguration {

    private AllayReports main;

    private String playerNotFound;
    private String reportedIsVictim;
    private String reportedIsStaff;
    private List<String> sendReport;

    private List<String> receivedReport;
    private List<String> changeState;
    private String teleportedThePlayer;
    private String excludedReport;
    private String changedState;

    private final FileConfiguration configuration;

    public MessagesConfiguration(AllayReports main) {
        this.main = main;

        configuration = MultipleFiles.getConfig(main, "messages");
    }

    public void loadConfigurationMessages() {
        playerNotFound = get("All", "PlayerNotFound");
        reportedIsVictim = get("All", "ReportedIsPlayer");
        reportedIsStaff = get("All", "ReportedIsStaff");
        sendReport = getList("All", "SendReport");

        receivedReport = getList("Staffs", "ReceivedReport");
        changeState = getList("Staffs", "ChangeState");
        teleportedThePlayer = get("Staffs", "TeleportedThePlayer");
        excludedReport = get("Staffs", "ExcludedReport");
        changedState = get("Staffs", "ChangedState");
    }

    private String get(String path, String key) {
        return configuration.getString("Messages." + path + "." + key);
    }

    private List<String> getList(String path, String key) {
        return configuration.getStringList("Messages." + path + "." + key);
    }
}