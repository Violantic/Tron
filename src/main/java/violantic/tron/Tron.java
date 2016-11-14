package violantic.tron;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import violantic.tron.game.GameState;
import violantic.tron.handler.GameHandler;
import violantic.tron.handler.TrailHandler;
import violantic.tron.manager.TrailManager;

/**
 * Created by Ethan on 11/13/2016.
 */
public class Tron extends JavaPlugin {

    private static Tron instance;
    private TrailManager trailManager;
    private TrailHandler trailHandler;
    private GameHandler gameHandler;

    private GameState current;

    public void onEnable() {
        instance = this;

        getConfig().options().copyDefaults(getConfig().contains("lobby"));
        saveConfig();

        this.trailManager = new TrailManager();
        this.trailHandler = new TrailHandler(this);
        this.gameHandler = new GameHandler(this);

        getServer().getScheduler().runTaskTimer(this, new GameHandler(this), 0l, 20l);
    }
    
    public String[] colors() {
        return new String[]{
                "RED",
                "ORANGE",
                "YELLOW",
                "GREEN",
                "NAVY",
                "BLUE",
                "FUCHSIA",
                "BLACK"
        };
    }

    public byte getData(String color) {
        if(color.equalsIgnoreCase("RED")) {
            return (byte) 14;
        } else if(color.equalsIgnoreCase("ORANGE")) {
            return (byte) 1;
        } else if(color.equalsIgnoreCase("YELLOW")) {
            return (byte) 4;
        } else if(color.equalsIgnoreCase("GREEN")) {
            return (byte) 5;
        } else if(color.equalsIgnoreCase("NAVY")) {
            return (byte) 11;
        } else if(color.equalsIgnoreCase("BLUE")) {
            return (byte) 3;
        } else if(color.equalsIgnoreCase("FUCHSIA")) {
            return (byte) 10;
        } else if(color.equalsIgnoreCase("BLACK")) {
            return (byte) 15;
        }

        return 0;
    }

    public void startTrails() {
        getServer().getScheduler().runTaskTimer(this, new TrailHandler(this), 0l, 5l);
    }

    public int minimumPlayers() {
        return 4;
    }

    public int maximumPlayers() {
        return 8;
    }

    public static Tron getInstance() {
        return instance;
    }

    public TrailManager getTrailManager() {
        return trailManager;
    }

    public GameState getCurrent() {
        return current;
    }

    public void setCurrent(GameState current) {
        this.current = current;
    }

    public TrailHandler getTrailHandler() {
        return trailHandler;
    }

    public GameHandler getGameHandler() {
        return gameHandler;
    }

    public String getPrefix() {
        return ChatColor.GRAY + "[" + ChatColor.LIGHT_PURPLE + "Mine" + ChatColor.YELLOW + "Swine" + ChatColor.GRAY + "] [" + ChatColor.AQUA + "Tron" + ChatColor.GRAY + "] ";
    }
}
