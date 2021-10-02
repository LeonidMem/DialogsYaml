package ru.leonidm.dialogs.yaml;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import ru.leonidm.dialogs.api.DialogsAPI;
import ru.leonidm.dialogs.api.events.DialogsReloadEvent;
import ru.leonidm.dialogs.api.events.NPCsReloadEvent;
import ru.leonidm.dialogs.api.events.QuestsReloadEvent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DialogsEventsHandler implements Listener {

    private Map<String, File> folderRecursion(String path) {
        if(path.endsWith("/")) path = path.substring(0, path.length() - 1);
        Map<String, File> output = new HashMap<>();
        for(File f : new File(path).listFiles()) {
            if(f.isDirectory()) for(Map.Entry<String, File> e : folderRecursion(path + "/" + f.getName()).entrySet()) output.put(e.getKey(), e.getValue());
            else if(f.getName().endsWith(".yml") || f.getName().endsWith(".yaml")) output.put(path + "/" + f.getName().replace(".json", ""), f);
        }
        return output;
    }

    private Map<String, Object> configureSectionToMap(ConfigurationSection config) {
        Map<String, Object> out = new HashMap<>();

        for(String key : config.getKeys(false)) {
            Object object = config.get(key);
            if(object instanceof ConfigurationSection section) out.put(key, configureSectionToMap(section));
            else out.put(key, config.get(key));
        }

        return out;
    }

    @EventHandler
    public void onDialogsReload(DialogsReloadEvent e) {
        for(Map.Entry<String, File> entry : folderRecursion("plugins/DialogsM/dialogs/").entrySet()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(entry.getValue());
            DialogsAPI.loadDialogFromMap(entry.getKey(), configureSectionToMap(config));
        }
    }

    @EventHandler
    public void onQuestsReload(QuestsReloadEvent e) {
        for(Map.Entry<String, File> entry : folderRecursion("plugins/DialogsM/quests/").entrySet()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(entry.getValue());
            DialogsAPI.loadQuestFromMap(entry.getKey(), configureSectionToMap(config));
        }
    }

    @EventHandler
    public void onNPCsReload(NPCsReloadEvent e) {
        for(Map.Entry<String, File> entry : folderRecursion("plugins/DialogsM/npcs/").entrySet()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(entry.getValue());
            DialogsAPI.loadNPCFromMap(entry.getKey(), configureSectionToMap(config));
        }
    }

}
