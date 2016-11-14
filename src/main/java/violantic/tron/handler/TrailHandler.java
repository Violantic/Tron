package violantic.tron.handler;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import violantic.tron.Tron;
import violantic.tron.event.PlayerCollideTrailEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Ethan on 11/13/2016.
 */
public class TrailHandler implements Runnable {

    private Map<UUID, ArrayList<Location>> trails;
    private Tron instance;

    public TrailHandler(Tron instance) {
        this.trails = new HashMap<UUID, ArrayList<Location>>();
        this.instance = instance;
    }

    public void handlePlayers() {
        for(UUID uuid : instance.getTrailManager().getUserMap().keySet()) {
            if(!trails.containsKey(uuid)) {
                trails.put(uuid, new ArrayList<Location>());
            }
            Player player = Bukkit.getPlayer(uuid);
            trails.get(uuid).add(player.getLocation());
        }
    }

    public void run() {
        handlePlayers();
        for(UUID uuid : instance.getTrailManager().getUserMap().keySet()) {
            final Player player = Bukkit.getPlayer(uuid);
            if(player.getLocation().getBlock().getType() == Material.AIR) {
                player.getWorld().getBlockAt(player.getLocation()).setType(Material.THIN_GLASS);
                player.getWorld().getBlockAt(player.getLocation()).setData(instance.getData(instance.getTrailManager().getUserMap().get(uuid)));
            }
            for(UUID others : trails.keySet()) {
                if(others != uuid) {
                    for(Location location : trails.get(others)) {
                        if(player.getLocation().distanceSquared(location) <= 0.3) {
                            instance.getServer().getPluginManager().callEvent(new PlayerCollideTrailEvent(player));
                        }
                    }
                }
            }

            new BukkitRunnable() {
                public void run() {
                    trails.get(player.getUniqueId()).remove(0);
                }
            }.runTaskLater(instance, 20 * 5);
        }
    }
}
