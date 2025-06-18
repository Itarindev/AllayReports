package allayplugins.stompado.enuns;

import org.bukkit.ChatColor;

public enum State {
    PENDING("Pendente", "&ePendente"),
    RESOLVED("Resolvido", "&aResolvido"),
    FALSE_REPORT("Falso", "&cFalso");

    private final String plainName;
    private final String coloredName;

    State(String plainName, String coloredName) {
        this.plainName = plainName;
        this.coloredName = coloredName;
    }

    public String getPlainName() {
        return plainName;
    }

    public String getColoredName() {
        return ChatColor.translateAlternateColorCodes('&', coloredName);
    }

    public static State fromPlainName(String name) {
        for (State state : values()) {
            if (state.plainName.equalsIgnoreCase(name)) {
                return state;
            }
        }
        return null;
    }

    public static State fromColoredName(String colored) {
        String plain = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', colored));
        return fromPlainName(plain);
    }
}