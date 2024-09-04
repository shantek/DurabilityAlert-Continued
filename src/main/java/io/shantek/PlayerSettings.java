package io.shantek;

public class PlayerSettings {

    private boolean warningsEnabled;
    private int armorThreshold;
    private int toolsThreshold;
    private AlertType alertType;
    private boolean enchantedItemsOnly;
    private boolean soundEnabled;

    // Default constructor
    public PlayerSettings() {
        this.warningsEnabled = true; // Default warnings enabled
        this.armorThreshold = 10; // Default armor threshold
        this.toolsThreshold = 10; // Default tools threshold
        this.alertType = AlertType.PERCENT; // Default alert type
        this.enchantedItemsOnly = false; // Default no enchanted items only
        this.soundEnabled = true; // Default sound enabled
    }

    // Constructor with parameters
    public PlayerSettings(boolean warningsEnabled, int armorThreshold, int toolsThreshold, AlertType alertType, boolean enchantedItemsOnly, boolean soundEnabled) {
        this.warningsEnabled = warningsEnabled;
        this.armorThreshold = armorThreshold;
        this.toolsThreshold = toolsThreshold;
        this.alertType = alertType;
        this.enchantedItemsOnly = enchantedItemsOnly;
        this.soundEnabled = soundEnabled;
    }

    // Getters and setters
    public boolean isWarningsEnabled() {
        return warningsEnabled;
    }

    public void setWarningsEnabled(boolean warningsEnabled) {
        this.warningsEnabled = warningsEnabled;
    }

    public int getArmorThreshold() {
        return armorThreshold;
    }

    public void setArmorThreshold(int armorThreshold) {
        this.armorThreshold = armorThreshold;
    }

    public int getToolsThreshold() {
        return toolsThreshold;
    }

    public void setToolsThreshold(int toolsThreshold) {
        this.toolsThreshold = toolsThreshold;
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    public boolean isEnchantedItemsOnly() {
        return enchantedItemsOnly;
    }

    public void setEnchantedItemsOnly(boolean enchantedItemsOnly) {
        this.enchantedItemsOnly = enchantedItemsOnly;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }

    // Enum for alert types
    public enum AlertType {
        PERCENT,
        DURABILITY
    }
}
