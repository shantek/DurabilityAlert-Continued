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
        this.warningsEnabled = DurabilityAlertContinued.isEnableByDefault();
        this.armorThreshold = DurabilityAlertContinued.getDefaultValue();
        this.toolsThreshold = DurabilityAlertContinued.getDefaultValue();
        this.alertType = DurabilityAlertContinued.getDefaultType();
        this.enchantedItemsOnly = DurabilityAlertContinued.isDefaultEnchanted();
        this.soundEnabled = true;
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

    // In PlayerSettings.java

    public boolean getSetting(DurabilityAlertContinued.Setting setting) {
        switch (setting) {
            case WARNINGS_ENABLED:
                return isWarningsEnabled();
            case ENCHANTED_ITEMS_ONLY:
                return isEnchantedItemsOnly();
            case SOUND_ENABLED:
                return isSoundEnabled();
            default:
                throw new IllegalArgumentException("Unknown setting: " + setting);
        }
    }

}
