package com.google.simonarons15.pixeljump;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class BlockPutter {
    private final Pixeljump plugin;
    private int szazalek;
    private int x1, y1, z1, x2, y2, z2;
    private World world;
    private BukkitTask task;
    private final Random random;
    public WinLoseHandler winLoseHandler;
    public boolean isSetup = false;
    private boolean doingSetup = false;
    private boolean pendingStart = false;

    private final int initialHP = 8;

    private List<PixelBlock> blocks = new ArrayList<>();

    public static final List<Material> materials = Arrays.asList(Material.AIR, Material.BLACK_WOOL, Material.PURPLE_WOOL, Material.BLUE_WOOL, Material.LIGHT_BLUE_WOOL,
            Material.GREEN_WOOL, Material.YELLOW_WOOL, Material.ORANGE_WOOL, Material.RED_WOOL);

    public BlockPutter(Pixeljump plugin) {
        this.plugin = plugin;
        random = new Random();
        szazalek = plugin.szazalek;
    }

    private void setBlocks()
    {
        blocks.removeIf(x -> x.getHp() == 0);
        for (PixelBlock block : blocks) {
            if(random.nextInt(100) < szazalek || block.getHp() == 1) {
                block.update();
            }
        }
    }

    public void Stop(Player winner)
    {
        Bukkit.getScheduler().cancelTask(task.getTaskId());
        winner.setInvulnerable(false);
        for (int i = 0; i < plugin._players.size(); i++)
        {
            plugin._players.get(i).teleport(plugin._playersLocation.get(i));
            plugin._players.get(i).setGameMode(plugin._playersGameMode.get(i));
            plugin._players.get(i).sendMessage(plugin.prefix + "The winner of the pixeljump game is: " + winner.getName());
        }
        plugin.playersJoined.clear();
        plugin._players.clear();
        plugin._playersLocation.clear();
        plugin._playersGameMode.clear();
        isSetup = false;
        blocks.clear();
    }
    public void Stop()
    {
        if(task != null)
        {
            Bukkit.getScheduler().cancelTask(task.getTaskId());
            winLoseHandler.Stop();
        }
        for(Player player: plugin.playersJoined)
        {
            player.setInvulnerable(false);
        }
        for (int i = 0; i < plugin._players.size(); i++)
        {
            plugin._players.get(i).teleport(plugin._playersLocation.get(i));
            plugin._players.get(i).setGameMode(plugin._playersGameMode.get(i));
            plugin._players.get(i).sendMessage(plugin.prefix + "The pixeljump game has ended!");
        }
        plugin._players.clear();
        plugin._playersLocation.clear();
        plugin._playersGameMode.clear();
        isSetup = false;
        blocks.clear();
    }

    public void Setup()
    {
        task = null;
        isSetup=true;
        world = plugin.border1.getWorld();
        if(world == null) return;

        // Set one corner of the cube to the given location.
        // Uses getBlockN() instead of getN() to avoid casting to an int later.
        x1 = plugin.border1.getBlockX();
        y1 = plugin.border1.getBlockY();
        z1 = plugin.border1.getBlockZ();

        // Figure out the opposite corner of the cube by taking the corner and adding length to all coordinates.
        x2 = plugin.border2.getBlockX();
        y2 = plugin.border2.getBlockY();
        z2 = plugin.border2.getBlockZ();

        if(x1 > x2)
        {
            int seged = x1;
            x1 = x2;
            x2 = seged;
        }
        if(y1 > y2)
        {
            int seged = y1;
            y1 = y2;
            y2 = seged;
        }
        if(z1 > z2)
        {
            int seged = z1;
            z1 = z2;
            z2 = seged;
        }
        blocks.clear();
        doingSetup = true;
        Set();
        doingSetup = false;
    }

    public void InitPlayer(Player player)
    {
        plugin._players.add(player);
        plugin._playersGameMode.add(player.getGameMode());
        plugin._playersLocation.add(player.getLocation());
        player.teleport(new Location(world, (x1 + x2) / 2f, y2 + 1, (z1 + z2) / 2f));
        player.setGameMode(GameMode.ADVENTURE);
        player.setInvulnerable(true);
    }

    public void Start()
    {
    //    Set();
        if(doingSetup)
        {
            pendingStart = true;
            return;
        }
        else if(pendingStart)
            pendingStart = false;
        for (Player player:plugin.playersJoined) {
            player.sendMessage(plugin.prefix + ChatColor.GREEN + "Game is starting!");
        }
        task = Bukkit.getServer().getScheduler().runTaskTimer(plugin, this::setBlocks, plugin.Delay, plugin.RepeatDelay);
        winLoseHandler = new WinLoseHandler(plugin, this, y1);
    }

    private void Set()
    {
        for (int xPoint = x1; xPoint <= x2; xPoint++) {
            for (int yPoint = y1; yPoint <= y2; yPoint++) {
                for (int zPoint = z1; zPoint <= z2; zPoint++) {
                    blocks.add(new PixelBlock(initialHP, world.getBlockAt(xPoint, yPoint, zPoint)));
                }
            }
        }

    }
}
