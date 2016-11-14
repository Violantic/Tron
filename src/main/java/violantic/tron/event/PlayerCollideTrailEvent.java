package violantic.tron.event;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import violantic.tron.Tron;
import violantic.tron.util.ChatUtil;

/**
 * Created by Ethan on 11/13/2016.
 */
public class PlayerCollideTrailEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private Player player;
    public PlayerCollideTrailEvent(Player online) {
        this.player = online;
            online.playSound(online.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
            //TitleUtil.sendTitle(online, 10, 20 * 5, 10, ChatColor.YELLOW + SurvivalGames.getInstance().getPrefix(), ChatColor.RESET + Arrays.asList(SurvivalGames.getInstance().getMap().getCreators()).toString());
            online.sendMessage(ChatColor.DARK_GRAY + "----------------------------------------------------");
            online.sendMessage("");
            ChatUtil.sendCenteredMessage(online, Tron.getInstance().getPrefix());
            ChatUtil.sendCenteredMessage(online, ChatColor.YELLOW + "You have died from hitting another players trail!");
            online.sendMessage("");
            online.sendMessage("");
            online.sendMessage(ChatColor.DARK_GRAY + "----------------------------------------------------");
    }

    public Player getPlayer() {
        return player;
    }
}
