package com.davis.p000ison.dev.findplot;

import com.davis.p000ison.dev.findplot.manager.SettingsManager;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author p000ison
 */
public class FindPlot extends JavaPlugin {

    private static final Logger logger = Logger.getLogger("Minecraft");
    private SettingsManager SettingsManager;
    private PlotFindUtil util;
    private static Permission perms = null;

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        SettingsManager = new SettingsManager(this);
        util = new PlotFindUtil(this);

        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            setupPermissions();
        }

        try {
            MetricsLite metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException e) {
            logger.log(Level.WARNING, String.format("Could not send Plugin-Stats: %s", e.getMessage()));
        }

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
                    if (hasPermission(player, "plotfind.command.find")) {
                        util.findPlot(player, player.getWorld());
                    }
                }
                if (args.length == 1) {
                    if (args[0].equals("reload")) {
                        if (hasPermission(player, "plotfind.command.reload")) {
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

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    public static boolean hasPermission(Player player, String permission) {
        if (perms != null) {
            return perms.has(player, permission);
        }
        return player.hasPermission(permission);
    }

    public String color(String text) {
        String colourised = text.replaceAll("&(?=[0-9a-fA-FkK])", "\u00a7");
        return colourised;
    }
    
    /**
     * @return the util
     */
    public PlotFindUtil getUtil() {
        return util;
    }
}
