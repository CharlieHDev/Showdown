package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.TeamsConfig;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GhostManager {

    private List<String> ghostPlayers = new ArrayList<>();

    private final Showdown2 plugin;

    private final Inventory playerFinder = Bukkit.createInventory(null, 36, "§ePlayers");

    public GhostManager(Showdown2 plugin) {
        this.plugin = plugin;
    }

    public void addGhostPlayer(String player) {

        ItemStack compass = new ItemStack(Material.COMPASS);

        ItemMeta meta = compass.getItemMeta();

        meta.setDisplayName("§ePlayer Finder");

        meta.addEnchant(Enchantment.UNBREAKING, 3, true);

        compass.setItemMeta(meta);

        if(ghostPlayers.contains(player)) return;

        ghostPlayers.add(player);
        Player ghostPlayer = Bukkit.getPlayer(player);
        if(ghostPlayer != null) {
            ghostPlayer.setGameMode(GameMode.ADVENTURE);
            for (Player p : plugin.getPlayers()) {
                if (p != null) {
                    if(!ghostPlayers.contains(p.getName())) {
                        p.hidePlayer(plugin, ghostPlayer);
                    }
                }
            }
            for(String player2 : ghostPlayers){
                Player p = Bukkit.getPlayer(player2);
                if(p != null) {
                    p.hidePlayer(plugin, ghostPlayer);
                }
            }
            ghostPlayer.setAllowFlight(true);
            ghostPlayer.setFlying(true);
            ghostPlayer.getInventory().clear();
            ghostPlayer.getInventory().addItem(compass);
        }
    }

    public void giveCompasses() {
        ItemStack compass = new ItemStack(Material.COMPASS);

        ItemMeta meta = compass.getItemMeta();

        meta.setDisplayName("§ePlayer Finder");

        meta.addEnchant(Enchantment.UNBREAKING, 3, true);

        compass.setItemMeta(meta);

        for(String player : ghostPlayers){
            Player p = Bukkit.getPlayer(player);
            if(p != null) {
                p.getInventory().clear();
                p.getInventory().addItem(compass);
            }
        }
    }

    public void removeGhostPlayer(String player) {

        if(!ghostPlayers.contains(player)) return;

        ghostPlayers.remove(player);
        Player ghostPlayer = Bukkit.getPlayer(player);
        if(ghostPlayer != null) {
            for (Player p : plugin.getPlayers()) {
                if (p != null) {
                    p.showPlayer(plugin, ghostPlayer);
                }
            }
            for(String player2 : ghostPlayers){
                Player p = Bukkit.getPlayer(player2);
                if(p != null) {
                    p.showPlayer(plugin, ghostPlayer);
                }
            }
            ghostPlayer.setAllowFlight(false);
            ghostPlayer.setFlying(false);
        }
    }

    public List<String> getGhostPlayers() {
        return ghostPlayers;
    }

    public Inventory getPlayerFinder() {
        return playerFinder;
    }

    public void populatePlayerFinder(){
        ItemStack divider1 = new ItemStack(Material.RED_CANDLE, 1);
        ItemMeta meta1 = divider1.getItemMeta();
        meta1.setDisplayName("§cRuby §f| §6Amber");
        divider1.setItemMeta(meta1);

        ItemStack divider2 = new ItemStack(Material.YELLOW_CANDLE, 1);
        ItemMeta meta2 = divider1.getItemMeta();
        meta2.setDisplayName("§eTopaz §f| §kKyanite");
        divider1.setItemMeta(meta2);

        ItemStack divider3 = new ItemStack(Material.LIGHT_BLUE_CANDLE, 1);
        ItemMeta meta3 = divider1.getItemMeta();
        meta3.setDisplayName("§bDiamond §f| §9Sapphire");
        divider1.setItemMeta(meta3);

        ItemStack divider4 = new ItemStack(Material.PINK_CANDLE, 1);
        ItemMeta meta4 = divider1.getItemMeta();
        meta4.setDisplayName("§dSmithsonite §f| §8Crystal");
        divider1.setItemMeta(meta4);

        ItemStack air = new ItemStack(Material.AIR, 1);
        int index = 0;
        int playerindex;
        playerFinder.clear();
        playerFinder.setItem(4, divider1);
        playerFinder.setItem(13, divider2);
        playerFinder.setItem(22, divider3);
        playerFinder.setItem(31, divider4);
        for(String teamname : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
            playerindex = 0;
            for (String playername : TeamsConfig.get().getStringList("teams." + teamname + ".players")) {
                playerindex++;
                if(ghostPlayers.contains(playername)) {
                    ItemStack skeletonHead = new ItemStack(Material.SKELETON_SKULL, 1);
                    SkullMeta meta = (SkullMeta) skeletonHead.getItemMeta();
                    meta.setDisplayName(playername);
                    skeletonHead.setItemMeta(meta);
                    playerFinder.setItem(index, skeletonHead);
                } else {
                    ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
                    SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
                    meta.setDisplayName(playername);
                    if (Bukkit.getPlayer(playername) != null) {
                        Player player = Bukkit.getPlayer(playername);

                        meta.setOwningPlayer(player);
                    }
                    meta.setLore(Arrays.asList("§a§uLeft-click §fto teleport", "§fto this player."));
                    playerHead.setItemMeta(meta);
                    playerFinder.setItem(index, playerHead);
                }
                index++;
            }
            if(playerindex < 4){
                for(int i = 0; i < 4-playerindex; i++){
                    playerFinder.setItem(index, air);
                    index++;
                }
            }
            if(index == 4 || index == 13 || index == 22 || index == 31){
                index++;
            }
        }
    }

}
