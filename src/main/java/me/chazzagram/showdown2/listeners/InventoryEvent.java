package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.PlayerConfig;
import me.chazzagram.showdown2.files.SpectatorConfig;
import me.chazzagram.showdown2.files.TeamsConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class InventoryEvent implements Listener {

    private static Showdown2 plugin;

    public InventoryEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }

    ItemStack air = new ItemStack(Material.AIR, 1);

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getView().getTitle().equalsIgnoreCase("§eTeams")) {
            e.setCancelled(true);
            switch (e.getSlot()){
                case 4,13,22,31:
                    break;
                default:
                    if(e.getSlot() >= 0 && e.getSlot() <= 35) {
                        switch (e.getClick()) {
                            case LEFT:
                                String title = "";
                                List<String> teams = new ArrayList<>(TeamsConfig.get().getConfigurationSection("teams").getKeys(false));
                                title = switch (e.getSlot()) {
                                    case 0, 1, 2, 3 -> teams.get(0);
                                    case 5, 6, 7, 8 -> teams.get(1);
                                    case 9, 10, 11, 12 -> teams.get(2);
                                    case 14, 15, 16, 17 -> teams.get(3);
                                    case 18, 19, 20, 21 -> teams.get(4);
                                    case 23, 24, 25, 26 -> teams.get(5);
                                    case 27, 28, 29, 30 -> teams.get(6);
                                    case 32, 33, 34, 35 -> teams.get(7);
                                    default -> title;
                                };
                                if (e.getCurrentItem() != null) {
                                    List<String> teamplayers = new ArrayList<>(TeamsConfig.get().getStringList("teams." + title + ".players"));
                                    teamplayers.remove(e.getCurrentItem().getItemMeta().getDisplayName());
                                    TeamsConfig.get().set("teams." + title + ".players", teamplayers);
                                    TeamsConfig.save();

                                    SpectatorConfig.get().set("spectators." + e.getCurrentItem().getItemMeta().getDisplayName() + ".points", PlayerConfig.get().getInt("players." + e.getCurrentItem().getItemMeta().getDisplayName() + ".points"));
                                    SpectatorConfig.save();

                                    PlayerConfig.get().set("players." + e.getCurrentItem().getItemMeta().getDisplayName(), null);
                                    PlayerConfig.save();
                                }
                                Inventory players = Bukkit.createInventory(null, 54, title);
                                for (String player : PlayerConfig.get().getConfigurationSection("players").getKeys(false)) {
                                    ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
                                    SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
                                    if (Bukkit.getServer().getPlayer(player) != null) {
                                        Player p = Bukkit.getPlayer(player);
                                        meta.setOwningPlayer(p);
                                    }
                                    meta.setDisplayName(player);
                                    playerHead.setItemMeta(meta);
                                    players.addItem(playerHead);
                                }
                                for (String player : SpectatorConfig.get().getConfigurationSection("spectators").getKeys(false)) {
                                    ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
                                    SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
                                    if (Bukkit.getServer().getPlayer(player) != null) {
                                        Player p = Bukkit.getPlayer(player);
                                        meta.setOwningPlayer(p);
                                    }
                                    meta.setDisplayName(player);
                                    playerHead.setItemMeta(meta);
                                    players.addItem(playerHead);
                                }
                                e.getWhoClicked().openInventory(players);
                                break;
                            case RIGHT:
                                String title2 = "";
                                List<String> teams2 = new ArrayList<>(TeamsConfig.get().getConfigurationSection("teams").getKeys(false));
                                title2 = switch (e.getSlot()) {
                                    case 0, 1, 2, 3 -> teams2.get(0);
                                    case 5, 6, 7, 8 -> teams2.get(1);
                                    case 9, 10, 11, 12 -> teams2.get(2);
                                    case 14, 15, 16, 17 -> teams2.get(3);
                                    case 18, 19, 20, 21 -> teams2.get(4);
                                    case 23, 24, 25, 26 -> teams2.get(5);
                                    case 27, 28, 29, 30 -> teams2.get(6);
                                    case 32, 33, 34, 35 -> teams2.get(7);
                                    default -> title2;
                                };
                                if (e.getCurrentItem() != null) {
                                    List<String> teamplayers = new ArrayList<>(TeamsConfig.get().getStringList("teams." + title2 + ".players"));
                                    teamplayers.remove(e.getCurrentItem().getItemMeta().getDisplayName());
                                    TeamsConfig.get().set("teams." + title2 + ".players", teamplayers);
                                    TeamsConfig.save();

                                    SpectatorConfig.get().set("spectators." + e.getCurrentItem().getItemMeta().getDisplayName() + ".points", PlayerConfig.get().getInt("players." + e.getCurrentItem().getItemMeta().getDisplayName() + ".points"));
                                    SpectatorConfig.save();

                                    PlayerConfig.get().set("players." + e.getCurrentItem().getItemMeta().getDisplayName(), null);
                                    PlayerConfig.save();//
                                    plugin.updateTeamGUI();
                                    e.getWhoClicked().openInventory(plugin.gui);
                                }
                                break;
                        }

                    }
                    break;
            }
        } else {
            for(String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)){
                if(e.getView().getTitle().equalsIgnoreCase(team)){
                    e.setCancelled(true);
                    if(!e.getCurrentItem().equals(air)){
                        String playername = e.getCurrentItem().getItemMeta().getDisplayName();
                        String teamname = e.getView().getTitle();
                        for(String teamcheck : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
                            if(TeamsConfig.get().getStringList("teams." + teamcheck + ".players").contains(playername)){
                                List<String> teamplayerlist = new ArrayList<>(TeamsConfig.get().getStringList("teams." + teamcheck + ".players"));
                                teamplayerlist.remove(e.getCurrentItem().getItemMeta().getDisplayName());
                                TeamsConfig.get().set("teams." + teamcheck + ".players", teamplayerlist);
                            }
                        }
                        TeamsConfig.save();
                        List<String> teamplayers = new ArrayList<>(TeamsConfig.get().getStringList("teams." + teamname + ".players"));
                        teamplayers.add(e.getCurrentItem().getItemMeta().getDisplayName());
                        TeamsConfig.get().set("teams." + teamname + ".players", teamplayers);
                        if(!PlayerConfig.get().getConfigurationSection("players").contains(playername)){
                            PlayerConfig.get().set("players." + playername + ".points", 0);
                        }
                        if(SpectatorConfig.get().getConfigurationSection("spectators").contains(playername)){
                            SpectatorConfig.get().set("spectators." + playername, null);
                            SpectatorConfig.save();
                        }
                        PlayerConfig.get().set("players." + playername + ".team", TeamsConfig.get().getString("teams." + teamname + ".name"));
                        PlayerConfig.save();
                        TeamsConfig.save();
                        plugin.updateTeamGUI();
                        e.getWhoClicked().openInventory(plugin.gui);
                    }
                }
            }
        }
    }
}
