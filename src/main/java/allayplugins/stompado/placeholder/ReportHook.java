package allayplugins.stompado.placeholder;

import allayplugins.stompado.AllayReports;
import allayplugins.stompado.dao.UserDAO;
import com.sun.istack.internal.NotNull;
import lombok.val;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class ReportHook extends PlaceholderExpansion {

    protected AllayReports main;

    public ReportHook(AllayReports main) {
        this.main = main;
    }

    @Override
    public String getIdentifier() {
        return "allayreports";
    }

    @Override
    public String getAuthor() {
        return "Stompado";
    }

    @Override
    public String getVersion() {
        return main.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    public String onPlaceholderRequest(Player player, @NotNull String params) {
        val user = UserDAO.findUserByName(player.getName());
        if (params.equalsIgnoreCase("reported")) {
            if (user.isReported())
                return "&c[Reportado] ";
        }

        return "-/-";
    }
}