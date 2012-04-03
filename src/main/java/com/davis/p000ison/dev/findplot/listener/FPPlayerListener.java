package com.davis.p000ison.dev.findplot.listener;

import com.davis.p000ison.dev.findplot.FindPlot;
import com.davis.p000ison.dev.findplot.Util;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author p000ison
 */
public class FPPlayerListener implements Listener {

    private FindPlot plugin;
    private Util util;

    public FPPlayerListener(FindPlot plugin) {
        this.plugin = plugin;
        this.util = plugin.getUtil();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBedInteract(PlayerInteractEvent event) {
        if (plugin.hasPermission(event.getPlayer(), "plotfind.command.find")) {
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                Location loc = event.getClickedBlock().getLocation();
                if (util.checkBed(loc)) {
                    util.findPlot(event.getPlayer(), event.getPlayer().getWorld());
                }
            }
        }
    }
}
