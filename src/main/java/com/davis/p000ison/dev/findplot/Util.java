package com.davis.p000ison.dev.findplot;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 *
 * @author p000ison
 */
public class Util {

    private FindPlot plugin;
    private static final HashSet<Byte> AIR_MATERIALS_TARGET = new HashSet<Byte>();

    static {
        AIR_MATERIALS_TARGET.add((byte) Material.AIR.getId());
    }

    public Util(FindPlot plugin) {
        this.plugin = plugin;
    }

    public void findPlot(Player player, World world) {
        ArrayList listArray = new ArrayList();
        String plot;
        RegionManager regionManager = plugin.getWorldGuard().getRegionManager(world);

        for (Map.Entry<String, ProtectedRegion> regionMap : regionManager.getRegions().entrySet()) {
            String name = regionMap.getKey();
            ProtectedRegion region = regionMap.getValue();
            
            if (startsWith(name, plugin.getSettingsManager().getStartWithStrings()) && (containsOwner(region, plugin.getSettingsManager().getAdmins()) || region.getOwners().getPlayers().isEmpty())) {
                listArray.add(name);
            }
        }
        if (listArray.size() > 0) {
            plot = (String) listArray.get(0);
            player.teleport(calcLoc(regionManager.getRegion(plot).getMinimumPoint(), regionManager.getRegion(plot).getMaximumPoint(), world));
            player.sendMessage(color(String.format(plugin.getSettingsManager().getPlotEnter(), plot)));
        } else {
            player.sendMessage(color(String.format(plugin.getSettingsManager().getNoSuchPlot(), player.getWorld().getName())));
        }
    }

    private Location calcLoc(Vector min, Vector max, World world) {
        double X = (min.getX() + max.getX()) / 2;
        double Z = (min.getZ() + max.getZ()) / 2;
        return new Location(world, X, world.getHighestBlockYAt((int) X, (int) Z), Z);
    }

    public boolean containsOwner(ProtectedRegion proRegion, List<String> list) {
        List<String> ownerlist = new LinkedList<String>();
        for (String player : proRegion.getOwners().getPlayers()) {
            ownerlist.add(player);
        }
        return ownerlist.containsAll(list);
    }

    public boolean startsWith(String string, List<String> list) {
        for (String str : list) {
            if (string.startsWith(str)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkBed(Location loc) {
        for (String str : plugin.getConfig().getConfigurationSection("Buttons").getKeys(false)) {
            if ((loc.getX() == plugin.getConfig().getDouble("Buttons." + str + ".X")
                    && loc.getY() == plugin.getConfig().getDouble("Buttons." + str + ".Y")
                    && loc.getZ() == plugin.getConfig().getDouble("Buttons." + str + ".Z"))) {
                return true;
            }
        }
        return false;
    }
    
    public Location getTarget(final Player player) {
        final Block block = player.getTargetBlock(AIR_MATERIALS_TARGET, 300);
        if (block == null) {
            player.sendMessage("Please target a valid Block!");
        }
        return block.getLocation();
    }

    public String color(String text) {
        String colourised = text.replaceAll("&(?=[0-9a-fA-FkK])", "\u00a7");
        return colourised;
    }
}
