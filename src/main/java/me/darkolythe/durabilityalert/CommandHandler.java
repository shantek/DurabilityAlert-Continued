package me.darkolythe.durabilityalert;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

    private DurabilityAlert main = DurabilityAlert.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("durabilityalert.command")) {
                if (cmd.getName().equalsIgnoreCase("durabilityalert")) {
                    if (args.length == 0) {
                        player.sendMessage(main.prefix + ChatColor.RED + main.confighandler.invalidarguments + ": /durabilityalert toggle/armour/tools");
                    } else if (args.length == 1) {
                        System.out.println(args);
                        if (args[0].equalsIgnoreCase("toggle")) {
                            main.setPlayerToggle(player);
                            if (main.getPlayerData(player).get(0) == 0) {
                                player.sendMessage(main.prefix + ChatColor.RED + main.confighandler.warningsdisabled);
                            } else {
                                player.sendMessage(main.prefix + ChatColor.GREEN + main.confighandler.warningsenabled);
                            }
                        } else {
                            player.sendMessage(main.prefix + ChatColor.RED + main.confighandler.invalidarguments + ": /durabilityalert " + args[0] + " <percent>");
                        }
                    } else if (args.length == 2) {
                        try {
                            int percent = Integer.parseInt(args[1]);
                            if (args[0].equalsIgnoreCase("armour")) {
                                main.setPlayerArmour(player, percent);
                                player.sendMessage(main.prefix + ChatColor.GREEN + main.confighandler.armourset + " " + percent);
                            } else if (args[0].equalsIgnoreCase("tools") || args[0].equalsIgnoreCase("tool")) {
                                main.setPlayerTools(player, percent);
                                player.sendMessage(main.prefix + ChatColor.GREEN + main.confighandler.toolset + " " + percent);
                            } else {
                                player.sendMessage(main.prefix + ChatColor.RED + main.confighandler.invalidarguments + ": /durabilityalert armour/tools");
                            }
                        } catch (Exception e) {
                            player.sendMessage(main.prefix + ChatColor.RED + main.confighandler.mustbenumber);
                        }
                    } else {
                        player.sendMessage(main.prefix + ChatColor.RED + main.confighandler.invalidarguments + ": /durabilityalert toggle/armour/tools");
                    }
                }
            }
        } else {
            sender.sendMessage("Only a player can use that command");
        }

        return true;
    }

}
