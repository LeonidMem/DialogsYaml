package ru.leonidm.dialogs.yaml;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class DialogsYaml extends JavaPlugin {

    private static DialogsYaml instance;

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(new DialogsEventsHandler(), this);
        getCommand("dyaml").setExecutor(new DialogsYaml());
        getLogger().info("Enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled!");
    }

    public static DialogsYaml getInstance() {
        return instance;
    }
}