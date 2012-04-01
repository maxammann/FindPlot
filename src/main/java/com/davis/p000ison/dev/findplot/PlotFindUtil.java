package com.davis.p000ison.dev.findplot;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.World;
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

        System.out.println(listArray);
        if (listArray.size() > 0) {
            plot = (String) listArray.get(0);
            
            Vector MaxVec = regionManager.getRegion(plot).getMaximumPoint();
            Vector MinVec = regionManager.getRegion(plot).getMinimumPoint();
            double X = (MinVec.getX() + MaxVec.getX()) / 2;
            double Z = (MinVec.getZ() + MaxVec.getZ()) / 2;
            
            System.out.println(plot);
            player.teleport(new Location(player.getWorld(), X, player.getWorld().getHighestBlockYAt((int) X, (int) Z), Z));
            player.sendMessage(String.format(plugin.getSettingsManager().getPlotEnter(), plot));
        } else {
            player.sendMessage(String.format(plugin.getSettingsManager().getNoSuchPlot(), player.getWorld().getName()));
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
