package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class LobbyPvPManager {

    private List<String> arenaPlayers = new ArrayList<>();

    private final Showdown2 plugin;

    private ItemStack kitSword;
    private ItemStack kitChestplate;
    private ItemStack kitLeggings;
    private ItemStack kitBoots;

    private boolean arenaEnabled;

    private Location pvpArenaLocation;


    public LobbyPvPManager(Showdown2 plugin) {
        this.plugin = plugin;
        this.arenaEnabled = true;
        pvpArenaLocation = new Location(Bukkit.getServer().getWorld("build"), 168.5, 141, 682.5, -90, 0);

        kitSword = new ItemStack(Material.STONE_SWORD, 1);
        kitChestplate = new ItemStack(Material.IRON_CHESTPLATE, 1);
        kitLeggings = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
        kitBoots = new ItemStack(Material.IRON_BOOTS, 1);

        ItemMeta meta = kitSword.getItemMeta();
        meta.setUnbreakable(true);
        kitSword.setItemMeta(meta);

        meta = kitChestplate.getItemMeta();
        meta.setUnbreakable(true);
        kitChestplate.setItemMeta(meta);

        meta = kitLeggings.getItemMeta();
        meta.setUnbreakable(true);
        kitLeggings.setItemMeta(meta);

        meta = kitBoots.getItemMeta();
        meta.setUnbreakable(true);
        kitBoots.setItemMeta(meta);
    }

    public List<String> getArenaPlayers(){
        return arenaPlayers;
    }

    public void joinPvPArena(Player p){
        arenaPlayers.add(p.getName());
        givePvPKit(p);
        plugin.messagePlayer(p, "§7[⚔] You have entered the PvP practice arena.");
        p.playSound(p.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1F, 1F);

        TextComponent prefix = new TextComponent("[⚔] " + p.getName() + " has entered the PvP practice arena. ");
        prefix.setColor(ChatColor.GRAY);

        TextComponent teleportButton = new TextComponent("(Teleport)");
        teleportButton.setColor(ChatColor.YELLOW);
        teleportButton.setUnderlined(true);
        teleportButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pvparena"));
        teleportButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to teleport.")));

        prefix.addExtra(teleportButton);

        for(Player player : Bukkit.getOnlinePlayers()){
            if(player.equals(p)) continue;
            if(arenaPlayers.contains(player.getName())){
                plugin.messagePlayer(player, "§c[⚔] " + p.getName() + " has entered the PvP practice arena.");
            } else {
                player.spigot().sendMessage(prefix);
            }
        }
    }

    public void leavePvPArena(Player p){
        arenaPlayers.remove(p.getName());
        p.getInventory().clear();
        p.teleport(pvpArenaLocation);
        p.setHealth(20f);
        p.playSound(p.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1F, 0.5F);
        plugin.messagePlayer(p, "§7[⚔] You have left the PvP practice arena.");
        for(Player player : Bukkit.getOnlinePlayers()){
            if(player.equals(p)) continue;
            plugin.messagePlayer(player, "§7[⚔] " + p.getName() + " has left the PvP practice arena.");
        }
    }

    public void givePvPKit(Player p){
        PlayerInventory inv = p.getInventory();
        inv.clear();
        inv.setHelmet(null);
        inv.setChestplate(kitChestplate.clone());
        inv.setLeggings(kitLeggings.clone());
        inv.setBoots(kitBoots.clone());
        inv.addItem(kitSword.clone());
    }

    public boolean getArenaEnabled(){
        return arenaEnabled;
    }

    public void enablePvPArena(){
        arenaEnabled = true;
    }

    public void disablePvPArena(){
        arenaEnabled = false;
        for(Player p : Bukkit.getOnlinePlayers()){
            if(arenaPlayers.contains(p.getName())){
                arenaPlayers.remove(p.getName());
                p.getInventory().clear();

            }
            arenaPlayers.clear();
        }
    }

}