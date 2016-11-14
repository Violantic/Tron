package violantic.tron.handler;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
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
    private Map<UUID, ArmorStand> vehicles;
    private Tron instance;

    public TrailHandler(Tron instance) {
        this.trails = new HashMap<UUID, ArrayList<Location>>();
        this.vehicles = new HashMap<UUID, ArmorStand>();
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
        if(!instance.getGameHandler().isEnabled()) return;
        handlePlayers();
        for(UUID uuid : instance.getTrailManager().getUserMap().keySet()) {
            if(!trails.containsKey(uuid)) {
                trails.put(uuid, new ArrayList<Location>());
            }
            final Player player = Bukkit.getPlayer(uuid);
            trails.get(uuid).add(player.getLocation());

            if(!vehicles.containsKey(uuid)) {
                ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
                ItemStack bike = new ItemStack(Material.WOOL, 1, instance.getData(instance.getTrailManager().getUserMap().get(uuid)));
                stand.setSmall(true);
                stand.setVisible(false);
                stand.setHelmet(bike);

                vehicles.put(uuid, stand);
            } else if(vehicles.get(uuid).getPassenger() == null) {
                vehicles.get(uuid).setPassenger(player);
            }

            Location location = player.getLocation();
            int speed = 2;
            Vector dir = location.toVector().subtract(player.getLocation().toVector()).normalize();
            vehicles.get(uuid).setVelocity(dir.multiply(speed));

            final Location block = player.getLocation().clone();
            if(player.getLocation().getBlock().getType() == Material.AIR) {
                player.getWorld().getBlockAt(player.getLocation()).setType(Material.THIN_GLASS);
                player.getWorld().getBlockAt(player.getLocation()).setData(instance.getData(instance.getTrailManager().getUserMap().get(uuid)));
            }
            for(UUID others : trails.keySet()) {
                if(others != uuid) {
                    for(Location location1 : trails.get(others)) {
                        if(player.getLocation().distanceSquared(location1) <= 0.3) {
                            instance.getServer().getPluginManager().callEvent(new PlayerCollideTrailEvent(player));
                        }
                    }
                }
            }

            new BukkitRunnable() {
                public void run() {
                    trails.get(player.getUniqueId()).remove(0);
                    player.getWorld().getBlockAt(block).setType(Material.AIR);
                }
            }.runTaskLater(instance, 20 * 5);
        }
    }
}
