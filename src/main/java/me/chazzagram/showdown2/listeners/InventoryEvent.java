package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.PhilipConfig;
import me.chazzagram.showdown2.files.PlayerConfig;
import me.chazzagram.showdown2.files.SpectatorConfig;
import me.chazzagram.showdown2.files.TeamsConfig;
import org.bukkit.*;
import org.bukkit.block.Barrel;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.*;

public class InventoryEvent implements Listener {

    private static Showdown2 plugin;

    public InventoryEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }

    ItemStack air = new ItemStack(Material.AIR, 1);



    int page = 1;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(plugin.emotesEnabled && e.getView().getTitle().equalsIgnoreCase("§eEmotes")){
            e.setCancelled(true);
            Random ran = new Random();
            float x = ran.nextFloat(6.0f)-3;
            float y = ran.nextFloat(6.0f)-3;
            float z = ran.nextFloat(6.0f)-3;
            switch (e.getSlot()) {
                case 1,3,5,7:
                    e.setCancelled(true);
                    e.getWhoClicked().closeInventory();
                    Player p = (Player) e.getWhoClicked();
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1F, 2F);
                    Location emoteLocation = p.getLocation().clone().add(x, y, z);
                    TextDisplay emote;
                    World world = Bukkit.getWorld("build");
                    emote = world.spawn(emoteLocation, TextDisplay.class);
                    emote.setSeeThrough(true);
                    emote.setText(plugin.emotes.get(e.getCurrentItem().getItemMeta().getDisplayName()));
                    emote.setBillboard(Display.Billboard.CENTER);
                    emote.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                    Quaternionf quat = new Quaternionf();
                    Transformation transform = new Transformation(
                            new Vector3f(0, 0, 0),
                            quat,
                            new Vector3f(1.5f, 1.5f, 1.5f),
                            quat
                    );

                    emote.setTransformation(transform);
                    String name = p.getName() + "emote";
                    BukkitTask task = new BukkitRunnable() {
                        int timeLeft = 81;
                        Location loc;

                        @Override
                        public void run() {
                            if (plugin.runningTimers.containsKey(name)) {
                                if (!plugin.pausedTimers.contains(name)) {
                                    timeLeft--;
                                    plugin.runningTimers.get(name).setValue(timeLeft);
                                    loc = emote.getLocation();
                                    loc.add(0, 0.05F, 0);
                                    emote.teleport(loc);
                                    if (timeLeft == 0) {
                                        emote.remove();
                                        plugin.messageConsole("Pan finished.");
                                        plugin.runningTimers.remove(name);
                                        cancel();
                                    }
                                }
                            } else {
                                plugin.messageConsole("Timer removed by external factor.");
                                cancel();
                            }
                        }

                    }.runTaskTimer(plugin, 0L, 1L);

                    plugin.runningTimers.put(name, new AbstractMap.SimpleEntry<>(task, 81));
                    break;
                default:
                    break;
            }
        }
        if((plugin.currentMode.equals("Lobby") || plugin.currentMode.equals("Voting")) && PlayerConfig.get().getConfigurationSection("players").getKeys(false).contains(e.getWhoClicked().getName())) {
            if(e.getClickedInventory().getHolder() instanceof Barrel) {
                e.setCancelled(false);
            } else {
            e.setCancelled(true);
            }
        }
        if(e.getView().getTitle().equalsIgnoreCase("§eCosmetics")) {
            e.setCancelled(true);
            if(e.getCurrentItem() != null && !e.getCurrentItem().getType().equals(Material.AIR) &&  plugin.shopAllowed) {
                if (!PlayerConfig.get().getConfigurationSection("players").getKeys(false).contains(e.getWhoClicked().getName()) || PhilipConfig.get().getInt("cosmetics." + (e.getSlot() + 1) + ".cost") <= PlayerConfig.get().getInt("players." + e.getWhoClicked().getName() + ".points")) {
                    switch (e.getSlot()) {
                        case 0:
                            if(e.getWhoClicked().getInventory().contains(PhilipConfig.get().getItemStack("cosmetics.1.item"))){
                                e.getWhoClicked().getInventory().remove(PhilipConfig.get().getItemStack("cosmetics.1.item"));
                                plugin.messagePlayer((Player) e.getWhoClicked(), "§eCosmetic unequipped.");
                            } else if (e.getWhoClicked().getInventory().getItemInOffHand().equals(PhilipConfig.get().getItemStack("cosmetics.1.item"))) {
                                e.getWhoClicked().getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                                plugin.messagePlayer((Player) e.getWhoClicked(), "§eCosmetic unequipped.");
                            } else {
                                e.getWhoClicked().getInventory().setItemInOffHand(PhilipConfig.get().getItemStack("cosmetics.1.item"));
                                plugin.messagePlayer((Player) e.getWhoClicked(), "§eCosmetic equipped.");
                            }
                            break;
                        case 1:
                            if(e.getWhoClicked().getInventory().getArmorContents()[3] != null){
                                if(e.getWhoClicked().getInventory().getArmorContents()[3].equals(PhilipConfig.get().getItemStack("cosmetics.2.item"))) {
                                    ItemStack[] armour = new ItemStack[]{
                                            e.getWhoClicked().getInventory().getArmorContents()[0],
                                            e.getWhoClicked().getInventory().getArmorContents()[1],
                                            e.getWhoClicked().getInventory().getArmorContents()[2],
                                            new ItemStack(Material.AIR)
                                    };
                                    e.getWhoClicked().getInventory().setArmorContents(armour);
                                    plugin.messagePlayer((Player) e.getWhoClicked(), "§eCosmetic unequipped.");
                                } else {
                                    ItemStack[] armour = new ItemStack[]{
                                            e.getWhoClicked().getInventory().getArmorContents()[0],
                                            e.getWhoClicked().getInventory().getArmorContents()[1],
                                            e.getWhoClicked().getInventory().getArmorContents()[2],
                                            PhilipConfig.get().getItemStack("cosmetics.2.item")
                                    };
                                    e.getWhoClicked().getInventory().setArmorContents(armour);
                                    plugin.messagePlayer((Player) e.getWhoClicked(), "§eCosmetic equipped.");
                                }
                            } else {
                                ItemStack[] armour = new ItemStack[]{
                                        e.getWhoClicked().getInventory().getArmorContents()[0],
                                        e.getWhoClicked().getInventory().getArmorContents()[1],
                                        e.getWhoClicked().getInventory().getArmorContents()[2],
                                        PhilipConfig.get().getItemStack("cosmetics.2.item")
                                };
                                e.getWhoClicked().getInventory().setArmorContents(armour);
                                plugin.messagePlayer((Player) e.getWhoClicked(), "§eCosmetic equipped.");
                            }

                            break;
                        case 2:
                            if(PhilipConfig.get().getStringList("cosmetics.3.whitelist").contains(e.getWhoClicked().getName())) {
                                if (e.getWhoClicked().getInventory().getArmorContents()[3] != null) {
                                    if (e.getWhoClicked().getInventory().getArmorContents()[3].equals(PhilipConfig.get().getItemStack("cosmetics.3.item"))) {
                                        ItemStack[] armour = new ItemStack[]{
                                                e.getWhoClicked().getInventory().getArmorContents()[0],
                                                e.getWhoClicked().getInventory().getArmorContents()[1],
                                                e.getWhoClicked().getInventory().getArmorContents()[2],
                                                new ItemStack(Material.AIR)
                                        };
                                        e.getWhoClicked().getInventory().setArmorContents(armour);
                                        plugin.messagePlayer((Player) e.getWhoClicked(), "§eCosmetic unequipped.");
                                    } else {
                                        ItemStack[] armour = new ItemStack[]{
                                                e.getWhoClicked().getInventory().getArmorContents()[0],
                                                e.getWhoClicked().getInventory().getArmorContents()[1],
                                                e.getWhoClicked().getInventory().getArmorContents()[2],
                                                PhilipConfig.get().getItemStack("cosmetics.3.item")
                                        };
                                        e.getWhoClicked().getInventory().setArmorContents(armour);
                                        plugin.messagePlayer((Player) e.getWhoClicked(), "§eCosmetic equipped.");
                                    }
                                } else {
                                    ItemStack[] armour = new ItemStack[]{
                                            e.getWhoClicked().getInventory().getArmorContents()[0],
                                            e.getWhoClicked().getInventory().getArmorContents()[1],
                                            e.getWhoClicked().getInventory().getArmorContents()[2],
                                            PhilipConfig.get().getItemStack("cosmetics.3.item")
                                    };
                                    e.getWhoClicked().getInventory().setArmorContents(armour);
                                    plugin.messagePlayer((Player) e.getWhoClicked(), "§eCosmetic equipped.");
                                }
                            } else {
                                plugin.messagePlayer((Player) e.getWhoClicked(), "This item is exclusive to testers! As a thank you for their efforts and support :)");
                            }

                            break;
                        case 4:
                            if(e.getWhoClicked().getInventory().contains(PhilipConfig.get().getItemStack("cosmetics.5.item"))){
                                e.getWhoClicked().getInventory().remove(PhilipConfig.get().getItemStack("cosmetics.5.item"));
                                plugin.messagePlayer((Player) e.getWhoClicked(), "§eCosmetic unequipped.");
                            } else if (e.getWhoClicked().getInventory().getItemInOffHand().equals(PhilipConfig.get().getItemStack("cosmetics.5.item"))) {
                                e.getWhoClicked().getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                                plugin.messagePlayer((Player) e.getWhoClicked(), "§eCosmetic unequipped.");
                            } else {
                                e.getWhoClicked().getInventory().setItemInOffHand(PhilipConfig.get().getItemStack("cosmetics.5.item"));
                                plugin.messagePlayer((Player) e.getWhoClicked(), "§eCosmetic equipped.");
                            }
                            break;
                        case 6:
                            if(e.getWhoClicked().getInventory().contains(PhilipConfig.get().getItemStack("cosmetics.7.item"))){
                                e.getWhoClicked().getInventory().remove(PhilipConfig.get().getItemStack("cosmetics.7.item"));
                                plugin.messagePlayer((Player) e.getWhoClicked(), "§eCosmetic unequipped.");
                            } else if (e.getWhoClicked().getInventory().getItemInOffHand().equals(PhilipConfig.get().getItemStack("cosmetics.7.item"))) {
                                e.getWhoClicked().getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                                plugin.messagePlayer((Player) e.getWhoClicked(), "§eCosmetic unequipped.");
                            } else {
                                e.getWhoClicked().getInventory().setItemInOffHand(PhilipConfig.get().getItemStack("cosmetics.7.item"));
                                plugin.messagePlayer((Player) e.getWhoClicked(), "§eCosmetic equipped.");
                            }
                            break;
                        case 3:
                            if(e.getWhoClicked().getInventory().getArmorContents()[3] != null){
                                if(e.getWhoClicked().getInventory().getArmorContents()[3].equals(PhilipConfig.get().getItemStack("cosmetics.4.item"))) {
                                    ItemStack[] armour = new ItemStack[]{
                                            e.getWhoClicked().getInventory().getArmorContents()[0],
                                            e.getWhoClicked().getInventory().getArmorContents()[1],
                                            e.getWhoClicked().getInventory().getArmorContents()[2],
                                            new ItemStack(Material.AIR)
                                    };
                                    e.getWhoClicked().getInventory().setArmorContents(armour);
                                    plugin.messagePlayer((Player) e.getWhoClicked(), "§eCosmetic unequipped.");
                                } else {
                                    ItemStack[] armour = new ItemStack[]{
                                            e.getWhoClicked().getInventory().getArmorContents()[0],
                                            e.getWhoClicked().getInventory().getArmorContents()[1],
                                            e.getWhoClicked().getInventory().getArmorContents()[2],
                                            PhilipConfig.get().getItemStack("cosmetics.4.item")
                                    };
                                    e.getWhoClicked().getInventory().setArmorContents(armour);
                                    plugin.messagePlayer((Player) e.getWhoClicked(), "§eCosmetic equipped.");
                                }
                            } else {
                                ItemStack[] armour = new ItemStack[]{
                                        e.getWhoClicked().getInventory().getArmorContents()[0],
                                        e.getWhoClicked().getInventory().getArmorContents()[1],
                                        e.getWhoClicked().getInventory().getArmorContents()[2],
                                        PhilipConfig.get().getItemStack("cosmetics.4.item")
                                };
                                e.getWhoClicked().getInventory().setArmorContents(armour);
                                plugin.messagePlayer((Player) e.getWhoClicked(), "§eCosmetic equipped.");
                            }

                            break;
                        case 5:
                            if(e.getWhoClicked().getInventory().getArmorContents()[3] != null){
                                if(e.getWhoClicked().getInventory().getArmorContents()[3].equals(PhilipConfig.get().getItemStack("cosmetics.6.item"))) {
                                    ItemStack[] armour = new ItemStack[]{
                                            e.getWhoClicked().getInventory().getArmorContents()[0],
                                            e.getWhoClicked().getInventory().getArmorContents()[1],
                                            e.getWhoClicked().getInventory().getArmorContents()[2],
                                            new ItemStack(Material.AIR)
                                    };
                                    e.getWhoClicked().getInventory().setArmorContents(armour);
                                    plugin.messagePlayer((Player) e.getWhoClicked(), "§eCosmetic unequipped.");
                                } else {
                                    ItemStack[] armour = new ItemStack[]{
                                            e.getWhoClicked().getInventory().getArmorContents()[0],
                                            e.getWhoClicked().getInventory().getArmorContents()[1],
                                            e.getWhoClicked().getInventory().getArmorContents()[2],
                                            PhilipConfig.get().getItemStack("cosmetics.6.item")
                                    };
                                    e.getWhoClicked().getInventory().setArmorContents(armour);
                                    plugin.messagePlayer((Player) e.getWhoClicked(), "§eCosmetic equipped.");
                                }
                            } else {
                                ItemStack[] armour = new ItemStack[]{
                                        e.getWhoClicked().getInventory().getArmorContents()[0],
                                        e.getWhoClicked().getInventory().getArmorContents()[1],
                                        e.getWhoClicked().getInventory().getArmorContents()[2],
                                        PhilipConfig.get().getItemStack("cosmetics.6.item")
                                };
                                e.getWhoClicked().getInventory().setArmorContents(armour);
                                plugin.messagePlayer((Player) e.getWhoClicked(), "§eCosmetic equipped.");
                            }

                            break;
                    }
                } else {
                    plugin.messagePlayer((Player) e.getWhoClicked(), "You cannot afford this cosmetic.");
                }
            }
        }
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
                                page = 1;
                                int count = 0;
                                for (String player : PlayerConfig.get().getConfigurationSection("players").getKeys(false)) {
                                    count++;
                                    if(count <= 45) {
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
                                }
                                for (String player : SpectatorConfig.get().getConfigurationSection("spectators").getKeys(false)) {
                                    count++;
                                    if(count <= 45) {
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
                                }
                                ItemStack arrow = new ItemStack(Material.ARROW, 1);
                                arrow.getItemMeta().setDisplayName("Change Page (" + page + ")");
                                players.setItem(53, arrow);
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
                    if(e.getSlot() == 53){
                        page++;
                        int playerIndex = 0;
                        Inventory players = Bukkit.createInventory(null, 54, e.getView().getTitle());
                        int count = 0;
                        for (String player : PlayerConfig.get().getConfigurationSection("players").getKeys(false)) {
                            playerIndex++;
                            if(playerIndex > (45*page)-45) {
                                if (count < 45) {
                                    count++;
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
                            }
                        }
                        for (String player : SpectatorConfig.get().getConfigurationSection("spectators").getKeys(false)) {
                            playerIndex++;
                            if(playerIndex > (45*page)-45) {
                                if (count < 45) {
                                    count++;
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
                            }
                        }
                        if(playerIndex < (45*page)-45){
                            page = 1;
                            int playerIndex2 = 0;
                            Inventory players2 = Bukkit.createInventory(null, 54, e.getView().getTitle());
                            int count2 = 0;
                            for (String player : PlayerConfig.get().getConfigurationSection("players").getKeys(false)) {
                                playerIndex2++;
                                if(playerIndex2 >= (45*page)-45) {
                                    if (count2 < 45) {
                                        count2++;
                                        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
                                        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
                                        if (Bukkit.getServer().getPlayer(player) != null) {
                                            Player p = Bukkit.getPlayer(player);
                                            meta.setOwningPlayer(p);
                                        }
                                        meta.setDisplayName(player);
                                        playerHead.setItemMeta(meta);
                                        players2.addItem(playerHead);
                                    }
                                }
                            }
                            for (String player : SpectatorConfig.get().getConfigurationSection("spectators").getKeys(false)) {
                                playerIndex2++;
                                if(playerIndex2 > (45*page)-45) {
                                    if (count2 < 45) {
                                        count2++;
                                        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
                                        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
                                        if (Bukkit.getServer().getPlayer(player) != null) {
                                            Player p = Bukkit.getPlayer(player);
                                            meta.setOwningPlayer(p);
                                        }
                                        meta.setDisplayName(player);
                                        playerHead.setItemMeta(meta);
                                        players2.addItem(playerHead);
                                    }
                                }
                            }
                            ItemStack arrow = new ItemStack(Material.ARROW, 1);
                            arrow.getItemMeta().setDisplayName("Change Page (" + page + ")");
                            players2.setItem(53, arrow);
                            e.getWhoClicked().openInventory(players2);
                            break;
                        }
                        ItemStack arrow = new ItemStack(Material.ARROW, 1);
                        arrow.getItemMeta().setDisplayName("Change Page (" + page + ")");
                        players.setItem(53, arrow);
                        e.getWhoClicked().openInventory(players);
                        break;
                    } else if(!e.getCurrentItem().equals(air)){
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
