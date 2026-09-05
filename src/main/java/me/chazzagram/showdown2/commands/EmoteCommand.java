package me.chazzagram.showdown2.commands;

import me.chazzagram.showdown2.Showdown2;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EmoteCommand implements CommandExecutor {

    private static Showdown2 plugin;

    public EmoteCommand(Showdown2 plugin) {
        this.plugin = plugin;
    }

    Inventory emotesList = Bukkit.createInventory(null, 9, "§eEmotes");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player p)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if(plugin.emotesEnabled && !plugin.runningTimers.containsKey(p.getName() + "emote")) {
            UpdateEmotes();
            p.openInventory(emotesList);
        }


        return true;
    }

    public void UpdateEmotes(){
        ItemStack emote1 = new ItemStack(Material.PURPLE_DYE, 1);
        ItemStack emote2 = new ItemStack(Material.YELLOW_DYE, 1);
        ItemStack emote3 = new ItemStack(Material.PINK_DYE, 1);
        ItemStack emote4 = new ItemStack(Material.WHITE_DYE, 1);

        ItemMeta emoteMeta = emote1.getItemMeta();
        emoteMeta.setDisplayName("§e§l§oHYPE!");
        emote1.setItemMeta(emoteMeta);

        emoteMeta = emote2.getItemMeta();
        emoteMeta.setDisplayName("§e§l§oFIRE!");
        emote2.setItemMeta(emoteMeta);

        emoteMeta = emote3.getItemMeta();
        emoteMeta.setDisplayName("§e§l§o...");
        emote3.setItemMeta(emoteMeta);

        emoteMeta = emote4.getItemMeta();
        emoteMeta.setDisplayName("§e§l§o:O");
        emote4.setItemMeta(emoteMeta);

        emotesList.clear();
        emotesList.setItem(1, emote1);
        emotesList.setItem(3, emote2);
        emotesList.setItem(5, emote3);
        emotesList.setItem(7, emote4);
    }
}
