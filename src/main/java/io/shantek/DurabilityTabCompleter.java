package io.shantek;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DurabilityTabCompleter implements TabCompleter {

    private static final List<String> SUBCOMMANDS = Arrays.asList("toggle", "sound", "status", "type", "enchant", "armour", "tools");
    private static final List<String> TYPE_OPTIONS = Arrays.asList("percent", "durability");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            // Provide list of subcommands for the first argument
            List<String> completions = new ArrayList<>();
            for (String subcommand : SUBCOMMANDS) {
                if (subcommand.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(subcommand);
                }
            }
            return completions;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("type")) {
            // If the first argument is "type", provide "percent" and "durability" for the second argument
            List<String> completions = new ArrayList<>();
            for (String option : TYPE_OPTIONS) {
                if (option.toLowerCase().startsWith(args[1].toLowerCase())) {
                    completions.add(option);
                }
            }
            return completions;
        }
        return null; // No completions for other arguments
    }
}
