package com.davis.p000ison.dev.findplot;

import com.davis.p000ison.dev.findplot.listener.FPPlayerListener;
import com.davis.p000ison.dev.findplot.manager.SettingsManager;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author p000ison
 */
public class FindPlot extends JavaPlugin {

    private HashMap<Player, Boolean> command = new HashMap<Player, Boolean>();
    private static final Logger logger = Logger.getLogger("Minecraft");
    private SettingsManager SettingsManager;
    private Util util;
    private static Permission perms = null;

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        SettingsManager = new SettingsManager(this);
        util = new Util(this);

        if (getServer().getPluginManager().getPlugin("Vault") != null) {
            setupPermissions();
        }

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new FPPlayerListener(this), this);

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

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("find")) {
                if (args.length == 0) {
                    if (hasPermission(player, "plotfind.command.find")) {
                        util.findPlot(player, player.getWorld());
                    }
                } else if (args.length == 1) {
                    if (args[0].equals("reload")) {
                        if (hasPermission(player, "plotfind.command.reload")) {
                            player.sendMessage(ChatColor.GREEN + "Config reloaded.");
                            this.getSettingsManager().load();
                        }
                    }
                } else if (args.length == 2) {
                    if (args[0].equals("set")) {
                        getSettingsManager().getPlugin().getConfig().set("Buttons." + args[1] + ".X", getUtil().getTarget(player).getX());
                        getSettingsManager().getPlugin().getConfig().set("Buttons." + args[1] + ".Y", getUtil().getTarget(player).getY());
                        getSettingsManager().getPlugin().getConfig().set("Buttons." + args[1] + ".Z", getUtil().getTarget(player).getZ());
                        getSettingsManager().save();
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

    public boolean hasPermission(Player player, String permission) {
        if (perms != null) {
            return perms.has(player, permission);
        }
        return player.hasPermission(permission);
    }

    /**
     * @return the util
     */
    public Util getUtil() {
        return util;
    }

    /**
     * @return the SettingsManager
     */
    public SettingsManager getSettingsManager() {
        return SettingsManager;
    }
}
