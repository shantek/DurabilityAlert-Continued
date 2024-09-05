package io.shantek;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

    private final DurabilityAlertContinued main = DurabilityAlertContinued.getInstance();

    // Define constants for command strings
    private static final String TOGGLE_COMMAND = "toggle";
    private static final String SOUND_COMMAND = "sound";
    private static final String STATUS_COMMAND = "status";
    private static final String TYPE_COMMAND = "type";
    private static final String ENCHANT_COMMAND = "enchant";
    private static final String ARMOR_COMMAND = "armour";
    private static final String TOOLS_COMMAND = "tools";
    private static final String RELOAD_COMMAND = "reload";  // New reload command

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("shantek.durabilityalert.use")) {
            player.sendMessage(main.configHandler.prefix + ChatColor.RED + "You do not have permission to use this command.");
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
            case TOGGLE_COMMAND -> handleToggleCommand(player, DurabilityAlertContinued.Setting.WARNINGS_ENABLED,
                    main.configHandler.warningsEnabled, main.configHandler.warningsDisabled);
            case SOUND_COMMAND -> handleToggleCommand(player, DurabilityAlertContinued.Setting.SOUND_ENABLED,
                    main.configHandler.soundEnabled, main.configHandler.soundDisabled);
            case STATUS_COMMAND -> printStatus(player, main);
            case TYPE_COMMAND -> handleTypeCommand(player, args);
            case ENCHANT_COMMAND -> handleToggleCommand(player, DurabilityAlertContinued.Setting.ENCHANTED_ITEMS_ONLY,
                    main.configHandler.enchantedTrue, main.configHandler.enchantedFalse);
            case ARMOR_COMMAND, TOOLS_COMMAND -> handleThresholdCommands(player, subCommand, args);
            case RELOAD_COMMAND -> handleReloadCommand(player);  // Handle the reload command
            default -> sendInvalidArgumentsMessage(player);
        }
    }

    // Handles reloading the configuration
    private void handleReloadCommand(Player player) {
        // Check if the player has the reload permission
        if (!player.hasPermission("shantek.durabilityalert.reload")) {
            player.sendMessage(main.configHandler.prefix + ChatColor.RED + "You do not have permission to reload the configuration.");
            return;
        }

        main.reloadConfig();  // Reload the configuration from the config file
        main.configHandler.loadConfig();  // Reload the values in the ConfigHandler
        player.sendMessage(main.configHandler.prefix + ChatColor.GREEN + "Configuration reloaded.");
    }

    private void handleToggleCommand(Player player, DurabilityAlertContinued.Setting setting, String messageEnabled, String messageDisabled) {
        main.togglePlayerSetting(player, setting);
        boolean isEnabled = main.getPlayerSettings(player).getSetting(setting);
        player.sendMessage(main.configHandler.prefix + (isEnabled ? messageEnabled : messageDisabled));
        main.joinListener.playerSave(player);
    }

    private void handleTypeCommand(Player player, String[] args) {
        if (args.length < 2 || (!args[1].equalsIgnoreCase("percent") && !args[1].equalsIgnoreCase("durability"))) {
            player.sendMessage(main.configHandler.prefix + ChatColor.RED + main.configHandler.invalidArguments + ": /durabilityalert type [percent/durability]");
            return;
        }

        PlayerSettings.AlertType type = args[1].equalsIgnoreCase("durability")
                ? PlayerSettings.AlertType.DURABILITY
                : PlayerSettings.AlertType.PERCENT;

        main.setPlayerAlertType(player, type);
        player.sendMessage(main.configHandler.prefix + ChatColor.GREEN + main.configHandler.setType.replaceAll("%type%", args[1]));
        main.joinListener.playerSave(player);
    }

    private void handleThresholdCommands(Player player, String command, String[] args) {
        if (args.length < 2) {
            player.sendMessage(main.configHandler.prefix + ChatColor.RED + main.configHandler.invalidArguments + ": /durabilityalert " + command + " <number>");
            return;
        }

        try {
            int threshold = Integer.parseInt(args[1]);
            if (command.equals(ARMOR_COMMAND)) {
                main.setPlayerArmorThreshold(player, threshold);
                player.sendMessage(main.configHandler.prefix + ChatColor.GREEN + main.configHandler.armourSet.replaceAll("%armour%", String.valueOf(threshold)));
            } else { // TOOLS_COMMAND
                main.setPlayerToolsThreshold(player, threshold);
                player.sendMessage(main.configHandler.prefix + ChatColor.GREEN + main.configHandler.toolSet.replaceAll("%tool%", String.valueOf(threshold)));
            }
            main.joinListener.playerSave(player);
        } catch (NumberFormatException e) {
            player.sendMessage(main.configHandler.prefix + ChatColor.RED + main.configHandler.mustBeNumber);
        }
    }

    private void sendInvalidArgumentsMessage(Player player) {
        player.sendMessage(main.configHandler.prefix + ChatColor.RED + main.configHandler.invalidArguments + ": /durabilityalert [toggle/armour/tools/type/status/enchant/sound/reload]");
    }

    public static void printStatus(Player player, DurabilityAlertContinued main) {
        PlayerSettings settings = main.getPlayerSettings(player);

        String status = ChatColor.GOLD + "Durability Alert Status:\n" +
                ChatColor.YELLOW + "Warnings: " + (settings.isWarningsEnabled() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled") + "\n" +
                ChatColor.YELLOW + "Armor Threshold: " + ChatColor.AQUA + settings.getArmorThreshold() + "\n" +
                ChatColor.YELLOW + "Tools Threshold: " + ChatColor.AQUA + settings.getToolsThreshold() + "\n" +
                ChatColor.YELLOW + "Alert Type: " + ChatColor.AQUA + settings.getAlertType().name() + "\n" +
                ChatColor.YELLOW + "Enchanted Items Only: " + (settings.isEnchantedItemsOnly() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No") + "\n" +
                ChatColor.YELLOW + "Sound: " + (settings.isSoundEnabled() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled");

        player.sendMessage(status);

    }
}
