package violantic.tron.listener;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import violantic.tron.Tron;
import violantic.tron.event.PlayerCollideTrailEvent;
import violantic.tron.util.ChatUtil;
import violantic.tron.util.LocationUtil;

/**
 * Created by Ethan on 11/13/2016.
 */
public class PlayerListener implements Listener {

    private Tron instance;

    public PlayerListener(Tron instance) {
        this.instance = instance;
    }

    public Tron getInstance() {
        return instance;
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if(!instance.getCurrent().getName().equalsIgnoreCase("lobby")) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, instance.getPrefix() + ChatColor.RED + "Game in progress");
            return;
        } else if(instance.getTrailManager().getUserMap().keySet().size() >= instance.maximumPlayers()) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, instance.getPrefix() + ChatColor.RED + "Too many players are online");
        }

        instance.getTrailManager().register(event.getPlayer().getUniqueId(),
                (instance.getTrailManager().getUserMap().keySet().size() > 0)
                        ?
                        (instance.colors()[instance.getTrailManager().getUserMap().keySet().size()])
                        :
                        (instance.colors()[0]));


        Player online = event.getPlayer();
        online.sendMessage(ChatColor.DARK_GRAY + "----------------------------------------------------");
        online.sendMessage("");
        ChatUtil.sendCenteredMessage(online, Tron.getInstance().getPrefix());
        ChatUtil.sendCenteredMessage(online, ChatColor.YELLOW + "You are the " + instance.getTrailManager().getUserMap().get(online.getUniqueId()) + " player");
        online.sendMessage("");
        online.sendMessage("");
        online.sendMessage(ChatColor.DARK_GRAY + "----------------------------------------------------");
        online.playSound(online.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
    }

    @EventHandler
    public void onCollide(PlayerCollideTrailEvent event) {
        event.getPlayer().getWorld().spawnParticle(Particle.EXPLOSION_HUGE, event.getPlayer().getLocation(), 1);
        event.getPlayer().teleport(LocationUtil.getLocation("world", instance.getConfig().getString("center")).add(0, 10, 0));
        event.getPlayer().setGameMode(GameMode.SPECTATOR);
        instance.getTrailManager().getUserMap().remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if(instance.getTrailManager().getUserMap().containsKey(event.getPlayer().getUniqueId())) {
            instance.getTrailManager().getUserMap().remove(event.getPlayer().getUniqueId());
            Bukkit.broadcastMessage(instance.getPrefix() + event.getPlayer().getName() + " has left and forfeited their position!");
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if(instance.getCurrent().isCanMove()) return;

        Location from = event.getFrom();
        Location to = event.getTo();

        if((from.getX() != to.getX()) || (from.getZ() != to.getZ())) {
            event.getPlayer().teleport(from);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        event.setCancelled(!instance.getCurrent().isCanBreak());
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        event.setCancelled(!instance.getCurrent().isCanPlace());
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

}
