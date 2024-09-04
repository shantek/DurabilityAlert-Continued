package io.shantek;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

    private final DurabilityAlert main = DurabilityAlert.getInstance();

    // Define constants for command strings
    private static final String TOGGLE_COMMAND = "toggle";
    private static final String SOUND_COMMAND = "sound";
    private static final String STATUS_COMMAND = "status";
    private static final String TYPE_COMMAND = "type";
    private static final String ENCHANT_COMMAND = "enchant";
    private static final String ARMOR_COMMAND = "armour";
    private static final String TOOLS_COMMAND = "tools";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("shantek.durabilityalert.command")) {
            player.sendMessage(DurabilityAlert.prefix + ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("durabilityalert")) {
            if (args.length == 0) {
                sendInvalidArgumentsMessage(player);
            } else {
                handleSubCommands(player, args);
            }
        }

        return true;
    }

    private void handleSubCommands(Player player, String[] args) {
        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case TOGGLE_COMMAND -> handleToggleCommand(player);
            case SOUND_COMMAND -> handleSoundCommand(player);
            case STATUS_COMMAND -> Utility.printStatus(player, main);
            case TYPE_COMMAND -> handleTypeCommand(player, args);
            case ENCHANT_COMMAND -> handleEnchantCommand(player);
            case ARMOR_COMMAND, TOOLS_COMMAND -> handleThresholdCommands(player, subCommand, args);
            default -> sendInvalidArgumentsMessage(player);
        }
    }

    private void handleToggleCommand(Player player) {
        main.togglePlayerSetting(player, DurabilityAlert.Setting.WARNINGS_ENABLED);
        boolean enabled = main.getPlayerSettings(player).isWarningsEnabled();
        if (!enabled) {
            player.sendMessage(DurabilityAlert.prefix + ChatColor.RED + main.configHandler.warningsDisabled);
        } else {
            player.sendMessage(DurabilityAlert.prefix + ChatColor.GREEN + main.configHandler.warningsEnabled);
        }
        main.joinListener.playerSave(player);
    }

    private void handleSoundCommand(Player player) {
        main.togglePlayerSetting(player, DurabilityAlert.Setting.SOUND_ENABLED);
        boolean soundEnabled = main.getPlayerSettings(player).isSoundEnabled();
        if (!soundEnabled) {
            player.sendMessage(DurabilityAlert.prefix + ChatColor.RED + "Durability alert sound disabled.");
        } else {
            player.sendMessage(DurabilityAlert.prefix + ChatColor.GREEN + "Durability alert sound enabled.");
        }
        main.joinListener.playerSave(player);
    }

    private void handleTypeCommand(Player player, String[] args) {
        if (args.length < 2 || (!args[1].equalsIgnoreCase("percent") && !args[1].equalsIgnoreCase("durability"))) {
            player.sendMessage(DurabilityAlert.prefix + ChatColor.RED + main.configHandler.invalidArguments + ": /durabilityalert type [percent/durability]");
            return;
        }

        // Corrected: Use the right class reference for the AlertType enum
        PlayerSettings.AlertType type = args[1].equalsIgnoreCase("durability")
                ? PlayerSettings.AlertType.DURABILITY
                : PlayerSettings.AlertType.PERCENT;

        main.setPlayerAlertType(player, type);
        player.sendMessage(DurabilityAlert.prefix + ChatColor.GREEN + main.configHandler.setType.replaceAll("%type%", args[1]));
        main.joinListener.playerSave(player);
    }

    private void handleEnchantCommand(Player player) {
        main.togglePlayerSetting(player, DurabilityAlert.Setting.ENCHANTED_ITEMS_ONLY);
        boolean enchantedOnly = main.getPlayerSettings(player).isEnchantedItemsOnly();
        if (!enchantedOnly) {
            player.sendMessage(DurabilityAlert.prefix + ChatColor.RED + main.configHandler.enchantedFalse);
        } else {
            player.sendMessage(DurabilityAlert.prefix + ChatColor.GREEN + main.configHandler.enchantedTrue);
        }
        main.joinListener.playerSave(player);
    }

    private void handleThresholdCommands(Player player, String command, String[] args) {
        if (args.length < 2) {
            player.sendMessage(DurabilityAlert.prefix + ChatColor.RED + main.configHandler.invalidArguments + ": /durabilityalert " + command + " <number>");
            return;
        }

        try {
            int threshold = Integer.parseInt(args[1]);
            if (command.equals(ARMOR_COMMAND)) {
                main.setPlayerArmorThreshold(player, threshold);
                player.sendMessage(DurabilityAlert.prefix + ChatColor.GREEN + main.configHandler.armourSet.replaceAll("%armour%", String.valueOf(threshold)));
            } else { // TOOLS_COMMAND
                main.setPlayerToolsThreshold(player, threshold);
                player.sendMessage(DurabilityAlert.prefix + ChatColor.GREEN + main.configHandler.toolSet.replaceAll("%tool%", String.valueOf(threshold)));
            }
            main.joinListener.playerSave(player);
        } catch (NumberFormatException e) {
            player.sendMessage(DurabilityAlert.prefix + ChatColor.RED + main.configHandler.mustBeNumber);
        }
    }

    private void sendInvalidArgumentsMessage(Player player) {
        player.sendMessage(DurabilityAlert.prefix + ChatColor.RED + main.configHandler.invalidArguments + ": /durabilityalert [toggle/armour/tools/type/status/enchant/sound]");
    }
}
