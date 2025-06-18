package allayplugins.stompado;

import allayplugins.stompado.commands.ReportCommand;
import allayplugins.stompado.commands.HistoricCommand;
import allayplugins.stompado.commands.ReportsCommand;
import allayplugins.stompado.configuration.InventoriesConfiguration;
import allayplugins.stompado.configuration.MessagesConfiguration;
import allayplugins.stompado.connections.MySQL;
import allayplugins.stompado.connections.SQLite;
import allayplugins.stompado.connections.model.IDatabase;
import allayplugins.stompado.connections.transform.ReportTransform;
import allayplugins.stompado.connections.transform.UserTransform;
import allayplugins.stompado.dao.ReportDAO;
import allayplugins.stompado.dao.UserDAO;
import allayplugins.stompado.listeners.PlayerJoinListener;
import allayplugins.stompado.listeners.inventories.HistoricClickListener;
import allayplugins.stompado.listeners.inventories.ReportClickListener;
import allayplugins.stompado.manager.CommandManager;
import allayplugins.stompado.manager.ReportManager;
import allayplugins.stompado.manager.UserManager;
import allayplugins.stompado.utils.ChatAction;
import allayplugins.stompado.utils.MultipleFiles;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class AllayReports extends JavaPlugin {

    @Getter
    private static AllayReports instance;

    private MessagesConfiguration messagesConfiguration;
    private InventoriesConfiguration inventoriesConfiguration;

    private CommandManager commandManager;

    private IDatabase iDatabase;

    private ReportManager reportManager;
    private UserManager userManager;

    private ChatAction chatAction;

    @Override
    public void onEnable() {
        instance = this;
        registerYaml();
        registerConnections();

        chatAction = new ChatAction(this);

        registerEvents();
        registerCommands();

        sendMessage();
    }

    @Override
    public void onDisable() {
        UserDAO.getUsers().forEach(userManager::save);
        ReportDAO.getReports().forEach(reportManager::save);
        iDatabase.closeConnection();
    }

    private void registerCommands() {
        commandManager = new CommandManager(this);
        commandManager.registerCommands(new ReportCommand(this), new HistoricCommand(this), new ReportsCommand());
    }

    private void registerConnections() {
        iDatabase = getConfig().getBoolean("MySQL.Ativar") ? new MySQL() : new SQLite();

        reportManager = new ReportManager(iDatabase);
        userManager = new UserManager(iDatabase);

        new UserTransform(this).loadUsers();
        new ReportTransform(this).loadReports();
    }

    private void registerEvents() {
        new ReportClickListener(this);
        new HistoricClickListener(this);
        new PlayerJoinListener(this);
    }

    private void registerYaml() {
        MultipleFiles.createFolder(this, "cache");

        saveDefaultConfig();
        MultipleFiles.createConfig(this, "messages");
        MultipleFiles.createConfig(this, "inventories");

        messagesConfiguration = new MessagesConfiguration(this);
        messagesConfiguration.loadConfigurationMessages();

        inventoriesConfiguration = new InventoriesConfiguration(this);
        inventoriesConfiguration.loadConfigurationInventories();
    }

    private void sendMessage() {
        Bukkit.getConsoleSender().sendMessage("§e[AllayReports] §fCriado por §b[Apolo]");
        Bukkit.getConsoleSender().sendMessage("§e[AllayReports] §ao plugin foi iniciado com sucesso.");
    }
}