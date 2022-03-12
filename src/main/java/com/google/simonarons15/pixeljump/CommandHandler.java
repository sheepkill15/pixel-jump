package com.google.simonarons15.pixeljump;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {
    private final Pixeljump plugin;
    private final BlockPutter blockPutter;
    public CommandHandler(Pixeljump plugin) {
        this.plugin = plugin;
        this.blockPutter = new BlockPutter(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean canJoin = false;
        boolean canStart = false;
        boolean canStop = false;
        boolean canSet = false;
        boolean canDelay = false;
        boolean canPeriod = false;
        boolean canLeave = false;
        boolean canReload = false;
        boolean canSave = false;
        Player player = null;
        if(sender instanceof Player)
        {
            player = (Player)sender;
        }
        if(player != null)
        {
            if(player.hasPermission("pixeljump.join"))
            {
                canJoin = true;
            }
            if(player.hasPermission("pixeljump.start"))
            {
                canStart = true;
            }
            if(player.hasPermission("pixeljump.stop"))
            {
                canStop = true;
            }
            if(player.hasPermission("pixeljump.set"))
            {
                canSet = true;
            }
            if(player.hasPermission("pixeljump.delay"))
            {
                canDelay = true;
            }
            if(player.hasPermission("pixeljump.period"))
            {
                canPeriod = true;
            }
            if(player.hasPermission("pixeljump.leave"))
            {
                canLeave = true;
            }
            if(player.hasPermission("pixeljump.reload"))
            {
                canReload = true;
            }
            if(player.hasPermission("pixeljump.save"))
            {
                canSave = true;
            }
        }
        else
        {
            canStart = true;
            canStop = true;
            canDelay = true;
            canPeriod = true;
            canReload = true;
            canSave = true;
        }
        if(args.length == 0)
        {
            sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Pixeljump help:\n");
            if(canJoin) sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "/pj join : " + ChatColor.RESET + "Join the pixeljump game" + '\n');
            if(canLeave) sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "/pj leave : " + ChatColor.RESET + "Leave the pixeljump game" + '\n');
            if(canStart) sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "/pj start : " + ChatColor.RESET + "Start the pixeljump game" + '\n');
            if(canStop) sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "/pj stop : " + ChatColor.RESET + "Stop the pixeljump game" + '\n');
            if(canSet) sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "/pj set <x1> <y1> <z1> <x2> <y2> <z2> : " + ChatColor.RESET + "Set the arena borders" + '\n');
            if(canDelay) sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "/pj delay <number> : " + ChatColor.RESET + "Set the starting delay" + '\n');
            if(canPeriod) sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "/pj period <number> : " + ChatColor.RESET + "Set the periodic delay" + '\n');
            if(canReload) sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "/pj reload : " + ChatColor.RESET + "Reload the plugin configuration" + '\n');
            if(canSave) sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "/pj save : " + ChatColor.RESET + "Save the plugin configuration" + '\n');
            return true;
        }
        else if(canSet && args[0].equals("set") && args.length != 7)
        {
            sender.sendMessage(plugin.prefix + ChatColor.RED + "" + ChatColor.BOLD + "Correct usage :" + ChatColor.WHITE + "/pj set <x1> <y1> <z1> <x2> <y2> <z2>" + '\n');
            return true;
        }
        else if(canSet && args[0].equals("set")) {
            try {
                Location set = new Location(player.getWorld(), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                plugin.border1 = set;
                set = new Location(player.getWorld(), Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]));
                plugin.border2 = set;
                player.sendMessage(plugin.prefix + "Successfully set the arena!");
            }
            catch (Exception e)
            {
                sender.sendMessage(plugin.prefix + ChatColor.RED + "Pass correct arguments!");
            }
            return true;
        }
        else if(canJoin && args[0].equals("join"))
        {
            if(plugin.playersJoined.contains(player.getPlayer()))
            {
                player.sendMessage(plugin.prefix + "You already joined!");
                return true;
            }
            if(plugin.border1 == null || plugin.border2 == null)
            {
                player.sendMessage(plugin.prefix + "There isn't an arena set yet!");
                return true;
            }
            if(!blockPutter.isSetup)
            {
                blockPutter.Setup();
            }
            blockPutter.InitPlayer(player);
            plugin.playersJoined.add(player);
            player.sendMessage(plugin.prefix + "You joined the game!");
            return true;
        }
        else if(canStart && args[0].equals("start"))
        {
            if(plugin.playersJoined.size() == 0)
            {
                sender.sendMessage(plugin.prefix + ChatColor.RED + "There aren't any players joined!");
            }
            else if(plugin.border1 != null && plugin.border2 != null)
            {
                blockPutter.Start();
            }
            else {
                sender.sendMessage(plugin.prefix + ChatColor.RED + "You have to set the arena first!\n");
            }
            return true;
        }
        else if(canStop && args[0].equals("stop"))
        {
            blockPutter.Stop();
            plugin.playersJoined.clear();
            return true;
        }
        else if(canDelay && args[0].equals("delay"))
        {
            try {
                plugin.Delay = Long.parseLong(args[1]);
                sender.sendMessage(plugin.prefix + ChatColor.GREEN + "Successfully changed the starting delay to " + args[1] + " ticks");
            } catch (Exception e)
            {
                sender.sendMessage(plugin.prefix + ChatColor.RED + "Pass correct arguments!");
            }
            return true;
        }
        else if(canPeriod && args[0].equals("period"))
        {
            try {
                plugin.RepeatDelay = Long.parseLong(args[1]);
                sender.sendMessage(plugin.prefix + ChatColor.GREEN + "Successfully changed the periodic delay to " + args[1] + " ticks");
            } catch (Exception e)
            {
                sender.sendMessage(plugin.prefix + ChatColor.RED + "Pass correct arguments!");
            }
            return true;
        }
        else if(canLeave && args[0].equals("leave"))
        {
            if(plugin.playersJoined.contains(player))
            {
                plugin.playersJoined.remove(player);
                int index = plugin._players.indexOf(player);
                player.teleport(plugin._playersLocation.get(index));
                plugin._playersLocation.remove(index);
                player.setGameMode(plugin._playersGameMode.get(index));
                plugin._playersGameMode.remove(index);
                plugin._players.remove(index);
                if(blockPutter.winLoseHandler != null) blockPutter.winLoseHandler.CheckPlayers();
                player.sendMessage(plugin.prefix + "You have left the game!");
            }
            else
            {
                player.sendMessage(plugin.prefix + ChatColor.RED + "You haven't joined yet!");
            }
            return true;
        }
        else if(canReload && args[0].equals("reload"))
        {
            plugin.load();
            return true;
        }
        else if(canSave && args[0].equals("save"))
        {
            plugin.save();
            return true;
        }
        return false;
    }
}
