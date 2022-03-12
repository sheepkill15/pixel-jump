package com.google.simonarons15.pixeljump;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Pixeljump extends JavaPlugin {
    public Location border1 = null;
    public Location border2 = null;

    private FileConfiguration config;

    public long Delay = 20;
    public long RepeatDelay = 20;

    public int szazalek = 20;

    public List<Player> playersJoined = new ArrayList<>();
    public List<GameMode> _playersGameMode = new ArrayList<>();
    public List<Location> _playersLocation = new ArrayList<>();
    public List<Player> _players = new ArrayList<>();

    public final String prefix = "[" + ChatColor.RED + "P" + ChatColor.GOLD + "J" + ChatColor.RESET + "] ";

    @Override
    public void onEnable() {
        // Plugin startup logic
        config = this.getConfig();

        config.addDefault("border1", null);
        config.addDefault("border2", null);
        config.addDefault("delay", 30);
        config.addDefault("period", 30);
        config.addDefault("percent", 20);
        config.options().copyDefaults(true);
        load();
        this.getCommand("pixeljump").setExecutor(new CommandHandler(this));
        this.getCommand("pixeljump").setTabCompleter(new TabCompleter());
        getLogger().info("Pixeljump plugin successfully initialized!");
    }

    @Override
    public void onDisable() {
        save();
    }

    public void load()
    {
        if(config.contains("border1"))
             border1 = getLocation(config.getString("border1"));
        if(config.contains("border2"))
            border2 = getLocation(config.getString("border2"));
        if(config.contains("delay")) Delay = config.getLong("delay");
        if(config.contains("period")) RepeatDelay = config.getLong("period");
        if(config.contains("percent")) szazalek = config.getInt("percent");
    }

    public void save()
    {
        if(border1 != null)
            config.set("border1", setLocation(border1));
        if(border2 != null)
            config.set("border2", setLocation(border2));
        config.set("delay", Delay);
        config.set("period", RepeatDelay);
        config.set("percent", szazalek);
        saveConfig();
    }

    private Location getLocation(String location)
    {
        String[] parts = location.split(" ");
        return new Location(getServer().getWorld(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
    }

    private String setLocation(Location location)
    {
        String oldLoc = "";
        oldLoc +=  location.getWorld().getName() + " ";
        oldLoc += location.getBlockX() + " ";
        oldLoc += location.getBlockY() + " ";
        oldLoc += location.getBlockZ();
        return oldLoc;
    }


}
