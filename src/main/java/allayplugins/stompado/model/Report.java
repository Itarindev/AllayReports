package allayplugins.stompado.model;

import allayplugins.stompado.enuns.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

@AllArgsConstructor
@Data
public class Report {

    private String id, victim, reported, reason;
    private State state;
    private long date;

    public Player getPlayer() {
        return Bukkit.getPlayer(reported);
    }

    public String getFormattedDate() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(date));
    }
}