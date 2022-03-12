package com.google.simonarons15.pixeljump;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class WinLoseHandler implements Listener {
    private final Pixeljump plugin;
    private final BlockPutter blockPutter;
    private final int y;

    public WinLoseHandler(Pixeljump plugin, BlockPutter blockPutter, int y) {
        this.plugin = plugin;
        this.blockPutter = blockPutter;
        this.y = y;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        if(Objects.requireNonNull(event.getTo()).getBlockY() < y)
        {
            Player player = event.getPlayer();
            plugin.playersJoined.remove(player);
            player.sendMessage(plugin.prefix + "You have fallen down!");
            CheckPlayers();
        }
    }

    public void CheckPlayers()
    {
        if(plugin.playersJoined.size() == 1)
        {
            Stop();
            blockPutter.Stop(plugin.playersJoined.get(0));
        }
        else if(plugin.playersJoined.size() == 0)
        {
            Stop();
            blockPutter.Stop();
        }
    }

    public void Stop()
    {
        HandlerList.unregisterAll(plugin);
    }
}
