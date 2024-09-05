package io.shantek;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class PluginTabCompleter implements TabCompleter {

	// Store precomputed tab completions in a map
	private final Map<String, List<String>> tabCompletions = new HashMap<>();

	public PluginTabCompleter() {
		// Precompute and cache the tab completions during initialization
		tabCompletions.put("durabilityalert", new ArrayList<>(Arrays.asList("toggle", "armour", "tools", "type", "status", "enchant", "sound")));
		tabCompletions.put("type", new ArrayList<>(Arrays.asList("percent", "durability")));
		tabCompletions.put("armour", new ArrayList<>(Arrays.asList("<number>"))); // Placeholder
		tabCompletions.put("tools", new ArrayList<>(Arrays.asList("<number>")));  // Placeholder
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (command.getName().equalsIgnoreCase("durabilityalert")) {
			if (args.length == 1) {
				return filterTabCompletions(tabCompletions.get("durabilityalert"), args[0]);
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("type")) {
					return filterTabCompletions(tabCompletions.get("type"), args[1]);
				} else if (args[0].equalsIgnoreCase("armour")) {
					return tabCompletions.get("armour");
				} else if (args[0].equalsIgnoreCase("tools")) {
					return tabCompletions.get("tools");
				}
			}
		}
		return null; // Return null if no completions are found
	}

	/**
	 * Filter the list of tab completions based on the current input.
	 *
	 * @param completions The list of all possible completions.
	 * @param input The current input from the user.
	 * @return The filtered list of completions.
	 */
	private List<String> filterTabCompletions(List<String> completions, String input) {
		List<String> filtered = new ArrayList<>();
		if (completions != null) {
			for (String completion : completions) {
				if (completion.toLowerCase().startsWith(input.toLowerCase())) {
					filtered.add(completion);
				}
			}
		}
		return filtered;
	}
}
