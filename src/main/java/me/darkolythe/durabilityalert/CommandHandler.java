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
                        player.sendMessage(main.prefix + ChatColor.RED + main.confighandler.invalidarguments + ": /durabilityalert [toggle/armour/tools/type]");
                    } else if (args.length == 1) {
                        if (args[0].equalsIgnoreCase("toggle")) {
                            main.setPlayerToggle(player);
                            if (main.getPlayerData(player).get(0) == 0) {
                                player.sendMessage(main.prefix + ChatColor.RED + main.confighandler.warningsdisabled);
                            } else {
                                player.sendMessage(main.prefix + ChatColor.GREEN + main.confighandler.warningsenabled);
                            }
                            main.joinlistener.playerSave(player);
                        } else if (args[0].equalsIgnoreCase("status")) {
                            String type;
                            if (main.getPlayerData(player).get(3) == 0) {
                                type = "percent left: ";
                            } else {
                                type = "durability left: ";
                            }
                            player.sendMessage(main.prefix + ChatColor.WHITE + "Status for " + player.getDisplayName());
                            player.sendMessage(ChatColor.GRAY + " - Warnings: " + (main.getPlayerData(player).get(0) == 0 ? "False" : "True"));
                            player.sendMessage(ChatColor.GRAY + " - Tool " + type + main.getPlayerData(player).get(2));
                            player.sendMessage(ChatColor.GRAY + " - Armour " + type + main.getPlayerData(player).get(1));
                        } else {
                            player.sendMessage(main.prefix + ChatColor.RED + main.confighandler.invalidarguments + ": /durabilityalert " + args[0] + " <percent>");
                        }
                    } else if (args.length == 2) {
                        if (args[0].equalsIgnoreCase("type")) {
                            if (args[1].equalsIgnoreCase("durability") || args[1].equalsIgnoreCase("percent")) {
                                main.setPlayerType(player, (args[1].equalsIgnoreCase("durability") ? 1 : 0));
                                player.sendMessage(main.prefix + ChatColor.GREEN + main.confighandler.settype + " " + args[1]);
                                main.joinlistener.playerSave(player);
                            } else {
                                player.sendMessage(main.prefix + ChatColor.GREEN + main.confighandler.invalidarguments + ": /durabilityalert type [percent/durability]");
                            }
                            return true;
                        }
                        try {
                            int percent = Integer.parseInt(args[1]);
                            if (args[0].equalsIgnoreCase("armour") || args[0].equalsIgnoreCase("armor") || args[0].equalsIgnoreCase("a")) {
                                main.setPlayerArmour(player, percent);
                                player.sendMessage(main.prefix + ChatColor.GREEN + main.confighandler.armourset + " " + percent);
                                main.joinlistener.playerSave(player);
                            } else if (args[0].equalsIgnoreCase("tools") || args[0].equalsIgnoreCase("tool") || args[0].equalsIgnoreCase("t")) {
                                main.setPlayerTools(player, percent);
                                player.sendMessage(main.prefix + ChatColor.GREEN + main.confighandler.toolset + " " + percent);
                                main.joinlistener.playerSave(player);
                            } else {
                                player.sendMessage(main.prefix + ChatColor.RED + main.confighandler.invalidarguments + ": /durabilityalert [armour/tools]");
                            }
                        } catch (Exception e) {
                            player.sendMessage(main.prefix + ChatColor.RED + main.confighandler.mustbenumber);
                        }
                    } else {
                        player.sendMessage(main.prefix + ChatColor.RED + main.confighandler.invalidarguments + ": /durabilityalert [toggle/armour/tools]");
                    }
                }
            }
        } else {
            sender.sendMessage("Only a player can use that command");
        }

        return true;
    }

}
