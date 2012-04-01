package com.davis.p000ison.dev.findplot;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author p000ison
 */
public class PlotFindUtil {

    private FindPlot plugin;

    public PlotFindUtil(FindPlot plugin) {
        this.plugin = plugin;
    }

    public void findPlot(Player player) {

        ArrayList listArray = new ArrayList();
        String plot = "null";
        RegionManager regionManager = plugin.getWorldGuard().getRegionManager(player.getWorld());
        for (Map.Entry<String, ProtectedRegion> regionMap : regionManager.getRegions().entrySet()) {
            String name = regionMap.getKey();
            ProtectedRegion region = regionMap.getValue();
            if (startsWith(name, plugin.getSettingsManager().getStartWithStrings()) && containsOwner(region, plugin.getSettingsManager().getAdmins())) {
                listArray.add(name);
            }

        }
        System.out.println(listArray);
        if (listArray.size() > 0) {
            plot = (String) listArray.get(0);
            player.sendMessage(String.format("Du wurdest zu dem Grundstück: %s teleportiert.", plot));
            Vector MaxVec = regionManager.getRegion(plot).getMaximumPoint();
            Vector MinVec = regionManager.getRegion(plot).getMinimumPoint();
            double X = (MinVec.getX() + MaxVec.getX()) / 2;
            double Z = (MinVec.getZ() + MaxVec.getZ()) / 2;
            System.out.println(plot);
            player.teleport(new Location(player.getWorld(), X, player.getWorld().getHighestBlockYAt((int) X, (int) Z), Z));
        } else {
            player.sendMessage(String.format("Keine Grundstücke in %s frei!", player.getWorld().getName()));
        }
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
}
