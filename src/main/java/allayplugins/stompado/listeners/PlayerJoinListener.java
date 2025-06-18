package allayplugins.stompado.listeners;

import allayplugins.stompado.AllayReports;
import allayplugins.stompado.manager.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private AllayReports main;

    private final UserManager userManager;

    public PlayerJoinListener(AllayReports main) {
        this.main = main;
        Bukkit.getPluginManager().registerEvents(this, main);

        userManager = main.getUserManager();
    }

    @EventHandler
    void onJoin(PlayerJoinEvent event) {
        userManager.insert(event.getPlayer().getName());
    }
}