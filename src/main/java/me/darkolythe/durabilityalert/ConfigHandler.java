package me.darkolythe.durabilityalert;

public class ConfigHandler {

    String lowdurability;
    String durabilityleft;
    String warningsdisabled;
    String warningsenabled;
    String invalidarguments;
    String armourset;
    String toolset;
    String mustbenumber;
    String settype;

    DurabilityAlert main;
    ConfigHandler(DurabilityAlert plugin) {
        main = plugin;

        lowdurability = main.getConfig().getString("lowdurability");
        durabilityleft = main.getConfig().getString("durabilityleft");
        warningsdisabled = main.getConfig().getString("warningsdisabled");
        warningsenabled = main.getConfig().getString("warningsenabled");
        invalidarguments = main.getConfig().getString("invalidarguments");
        armourset = main.getConfig().getString("armourset");
        toolset = main.getConfig().getString("toolset");
        mustbenumber = main.getConfig().getString("mustbenumber");
        settype = main.getConfig().getString("settype");
    }
}
