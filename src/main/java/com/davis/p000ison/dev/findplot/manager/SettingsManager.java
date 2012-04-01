package com.davis.p000ison.dev.findplot.manager;

import com.davis.p000ison.dev.findplot.FindPlot;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author p000ison
 */
public final class SettingsManager {

    private FindPlot plugin;
    private File main;
    private FileConfiguration config;
    private List<String> Admins;
    private List<String> StartWithStrings;
    private String NoSuchPlot;
    private String PlotEnter;
    private int InterX;
    private int InterY;
    private int InterZ;

    /**
     *
     */
    public SettingsManager(FindPlot plugin) {
        this.plugin = plugin;
        config = plugin.getConfig();
        main = new File(getPlugin().getDataFolder() + File.separator + "config.yml");
        load();
    }

    /**
     * Load the configuration
     */
    public void load() {
        boolean exists = (main).exists();

        if (exists) {
            try {
                getConfig().options().copyDefaults(true);
                getConfig().load(main);
            } catch (Exception ex) {
                plugin.getLogger().log(Level.WARNING, "Loading the config failed!:{0}", ex.getMessage());
            }
        } else {
            getConfig().options().copyDefaults(true);

        }

        Admins = getConfig().getStringList("Admins");
        StartWithStrings = getConfig().getStringList("StartsWithStrings");
        NoSuchPlot = getConfig().getString("Messages.NoSuchPlot");
        PlotEnter = getConfig().getString("Messages.PlotEnter");
        InterX = getConfig().getInt("InterLoc.X");
        InterY = getConfig().getInt("InterLoc.Y");
        InterZ = getConfig().getInt("InterLoc.Z");

        save();
    }

    public void save() {
        try {
            getConfig().save(main);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.WARNING, "Saving the config failed!:{0}", ex.getMessage());
        }
    }

    /**
     * @return the plugin
     */
    public FindPlot getPlugin() {
        return plugin;
    }

    /**
     * @return the config
     */
    public FileConfiguration getConfig() {
        return config;
    }

    /**
     * @return the Admins
     */
    public List<String> getAdmins() {
        return Admins;
    }

    /**
     * @return the StartWithStrings
     */
    public List<String> getStartWithStrings() {
        return StartWithStrings;
    }

    /**
     * @return the NoSuchPlot
     */
    public String getNoSuchPlot() {
        return NoSuchPlot;
    }

    /**
     * @return the PlotEnter
     */
    public String getPlotEnter() {
        return PlotEnter;
    }

    /**
     * @return the InterX
     */
    public int getInterX() {
        return InterX;
    }

    /**
     * @return the InterY
     */
    public int getInterY() {
        return InterY;
    }

    /**
     * @return the InterZ
     */
    public int getInterZ() {
        return InterZ;
    }
}