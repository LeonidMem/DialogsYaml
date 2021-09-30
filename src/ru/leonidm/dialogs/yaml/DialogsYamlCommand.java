package ru.leonidm.dialogs.yaml;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

public class DialogsYamlCommand implements CommandExecutor, TabCompleter {

    private final byte[] dialogsTemplate = "text: 'Hi!'\n\nconditions:\n    beforeDialogs: '1'\n    afterDialogs: '2'\n    beforeQuests: '1'\n    afterQuests: '2'\n    whenQuests: '1'\n    ifTags: 'Tag1'\n    ifNoTags: 'Tag2'\n    ifScoreboard: 'Name > 10'\n\nactions:\n    command: 'say *nothing*'\n    console: 'give @m diamond '\n    startQuests: '4'\n\nanswers:\n  - display:\n        text: '[Hi!]'\n        color: 'green'\n    actions:\n        command: 'say Hi!'\n        openDialog: '3'\n\n  - display:\n        text: '[Bye!]'\n        color: 'red'\n    actions:\n        command: 'say Bye!'\n        console: 'say I'm server, @m...'\n\nsound: 'entity.villager.ambient'".getBytes();
    private final byte[] questTemplate = "displayName: 'I need a diamond...'\ntext: 'Thanks for the diamond!'\ninfo: 'Hover text in /quests'\nuuid: '{uuid}'\n\nconditions:\n  - dialogs: ['4']\n    items:\n      - id: 'diamond'\n        count: 1\n        nbt: '{CustomModelData:1}'\n\nrewards:\n  - console: 'say Hi, I''m server!'\n    command: 'say Hi, I''m player!'\n    items:\n      - id: 'dirt'\n        count: 1\n        nbt: '{CustomTag:2b}'\n    startQuests: '10'\n    addQuests: ['11', '12']\n    removeQuests: '13'\n    addDialogs: ['10']\n    removeDialogs: ['11', '12', '13']\n    exp: 5\n\nsound: 'entity.villager.ambient'".getBytes();
    private final byte[] npcTemplate = "uuid: '{uuid}'\nname: 'Villager'\ndialogs: ['1', '2', '3']\nsigns:\n  - text: '&l!'\n    color: 'yellow'\n    conditions:\n        beforeDialogs: '1'\n        afterDialogs: '2'\n        beforeQuests: '1'\n        afterQuests: '2'\n        whenQuests: '1'\n        ifTags: 'Tag1'\n        ifNoTags: 'Tag2'\n        ifScoreboard: 'Name > 10'".getBytes();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!sender.hasPermission("dialogs.yaml.*")) {
            sender.sendMessage("§cYou don't have enough permissions!");
            return true;
        }

        if(args.length == 0) {
            sender.sendMessage("§cUsage: §f/dyaml [dialog/quest/npc] [name]§c!");
            return true;
        }
        try {
            switch(args[0].toLowerCase()) {
                case "dialog" -> {
                    Files.write(Paths.get("plugins/DialogsM/dialogs/" + args[1] + ".yml"), dialogsTemplate,
                            StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                }
                case "quest" -> {
                    Files.write(Paths.get("plugins/DialogsM/quests/" + args[1] + ".yml"), questTemplate,
                            StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                }
                case "npc" -> {
                    Files.write(Paths.get("plugins/DialogsM/npcs/" + args[1] + ".yml"), npcTemplate,
                            StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                }
            }
        } catch(Exception e) {
            sender.sendMessage("§cIO exception occurred! Check console for more information!");
            e.printStackTrace();
        }
        return true;
    }

    private final List<String> subcommands = Arrays.asList("dialog", "quest", "npc");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 1 && sender.hasPermission("dialogs.yaml.*")) {
            return subcommands;
        }
        return null;
    }
}
