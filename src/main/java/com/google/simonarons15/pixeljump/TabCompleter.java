package com.google.simonarons15.pixeljump;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(!(sender instanceof Player))
            return null;
        Player player = (Player) sender;
        if(args.length == 1)
        {
            List<String> szavak = new ArrayList<>();
            if(player.hasPermission("pixeljump.join")) szavak.add("join");
            if(player.hasPermission("pixeljump.leave")) szavak.add("leave");
            if(player.hasPermission("pixeljump.start")) szavak.add("start");
            if(player.hasPermission("pixeljump.stop")) szavak.add("stop");
            if(player.hasPermission("pixeljump.delay")) szavak.add("delay");
            if(player.hasPermission("pixeljump.period")) szavak.add("period");
            if(player.hasPermission("pixeljump.set")) szavak.add("set");
            if(player.hasPermission("pixeljump.save")) szavak.add("save");
            if(player.hasPermission("pixeljump.reload")) szavak.add("reload");
            return szavak;
        }
        else if(player.hasPermission("pixeljump.set") && args[0].equals("set"))
        {
            Location playerLoc = player.getLocation();
            switch(args.length)
            {
                case 2:
                case 5:
                    return Collections.singletonList(String.valueOf(playerLoc.getBlockX()));
                case 3:
                case 6:
                    return Collections.singletonList(String.valueOf(playerLoc.getBlockY()));
                case 4:
                case 7:
                    return Collections.singletonList(String.valueOf(playerLoc.getBlockZ()));
                default:
                    return null;
            }
        }
        return null;
    }
}
