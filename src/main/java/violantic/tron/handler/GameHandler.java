package violantic.tron.handler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import violantic.tron.Tron;
import violantic.tron.game.GameState;
import violantic.tron.util.ChatUtil;

/**
 * Created by Ethan on 11/13/2016.
 */
public class GameHandler implements Runnable {

    private int second = 60;
    private boolean enabled = true;

    private Tron instance;

    public GameHandler(Tron instance) {
        this.instance = instance;
    }

    public Tron getInstance() {
        return instance;
    }

    public void setInstance(Tron instance) {
        this.instance = instance;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void pling() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1);
        }
    }

    public void handleXP() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.setLevel(second);
        }
    }

    public void handleTick() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        }
    }

    public void run() {
        if(!enabled) return;
        if(instance.getCurrent().getName().equalsIgnoreCase("Lobby")) {
            if(second > 0) {
                handleXP();
            }
            if (instance.getServer().getOnlinePlayers().size() >= instance.minimumPlayers()) {
                if(second == 15) {
                    Bukkit.broadcastMessage(instance.getPrefix() + "The game starts in " + ChatColor.YELLOW + "" + ChatColor.BOLD + second + ChatColor.RESET + "" + ChatColor.GRAY + " seconds");
                } else if(second <= 5) {
                    handleTick();
                    Bukkit.broadcastMessage(instance.getPrefix() + "The game starts in " + ChatColor.YELLOW + "" + ChatColor.BOLD + second + ChatColor.RESET + "" + ChatColor.GRAY + " seconds");
                } else if(second <= 0) {
                    GameState state = new GameState("Waiting");
                    state.setCanMove(false);
                    instance.setCurrent(state);
                    second = 10;
                }
                second--;
            }
        } else if(instance.getCurrent().getName().equalsIgnoreCase("Waiting")) {
            if(second == 10) {
                Bukkit.broadcastMessage(instance.getPrefix() + "Releasing bikers in " + ChatColor.YELLOW + "" + ChatColor.BOLD + second + ChatColor.RESET + "" + ChatColor.GRAY + " seconds");
            } else if(second <= 5) {
                handleTick();
                Bukkit.broadcastMessage(instance.getPrefix() + "Releasing bikers in " + ChatColor.YELLOW + "" + ChatColor.BOLD + second + ChatColor.RESET + "" + ChatColor.GRAY + " seconds");
            } else if(second <= 0) {
                pling();
                Bukkit.broadcastMessage(instance.getPrefix() + ChatColor.YELLOW + "" + ChatColor.BOLD + "GO!");
                GameState state = new GameState("Progress");
                state.setCanMove(true);
                state.setCanDrop(false);
                instance.setCurrent(state);
                second = 180;
            }
            second--;
        } else if(instance.getCurrent().getName().equalsIgnoreCase("Progress")) {
            if(instance.getTrailManager().getUserMap().keySet().size() == 1) {
                for (Player online : Bukkit.getOnlinePlayers()) {
                    online.sendMessage(ChatColor.DARK_GRAY + "----------------------------------------------------");
                    online.sendMessage("");
                    ChatUtil.sendCenteredMessage(online, Tron.getInstance().getPrefix());
                    String winner;
                    if(instance.getTrailManager().getUserMap().containsKey(online.getUniqueId())) {
                        winner = online.getName();
                        ChatUtil.sendCenteredMessage(online, ChatColor.YELLOW + winner + " has won the game!");
                    }
                    online.sendMessage("");
                    online.sendMessage("");
                    online.sendMessage(ChatColor.DARK_GRAY + "----------------------------------------------------");
                }
                enabled = false;
            }
        }
    }
}
