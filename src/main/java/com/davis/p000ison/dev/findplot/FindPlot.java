package com.davis.p000ison.dev.findplot;

import com.davis.p000ison.dev.findplot.manager.SettingsManager;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author p000ison
 */
public class FindPlot extends JavaPlugin {

    private static final Logger logger = Logger.getLogger("Minecraft");
    private SettingsManager SettingsManager;
    private PlotFindUtil util;

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        SettingsManager = new SettingsManager(this);
        util = new PlotFindUtil(this);
        if (getWorldGuard() == null) {
            logger.log(Level.SEVERE, "WorldGuard not found! Disabling...");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    public WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }

    /**
     * @return the SettingsManager
     */
    public SettingsManager getSettingsManager() {
        return SettingsManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("find")) {
                if (args.length == 0) {
                        util.findPlot(player, player.getWorld());
                }
                if (args.length == 1) {
                    if (args[0].equals("reload")) {
                        if (player.hasPermission("plotfind.command.reload")) {
                            player.sendMessage(ChatColor.GREEN + "Config reloaded.");
                            this.getSettingsManager().load();
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * @return the util
     */
    public PlotFindUtil getUtil() {
        return util;
    }
}
