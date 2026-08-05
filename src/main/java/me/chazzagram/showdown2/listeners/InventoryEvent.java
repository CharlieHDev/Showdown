package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.PhilipConfig;
import me.chazzagram.showdown2.files.PlayerConfig;
import me.chazzagram.showdown2.files.SpectatorConfig;
import me.chazzagram.showdown2.files.TeamsConfig;
import org.bukkit.*;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
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

        if(plugin.ghostManager.getGhostPlayers().contains(e.getWhoClicked().getName()) && !e.getView().getTitle().equals("§ePlayers")) return;

        if(plugin.currentMode.equals("Dimension Dash")){
            e.setCancelled(true);
        }

        if(plugin.currentMode.equals("Zoomo Go")){
            if(e.getSlotType() == InventoryType.SlotType.ARMOR) {
                e.setCancelled(true);
            }
        }
        if(plugin.currentMode.equals("Push Point")){
            if(e.getView().getTitle().equalsIgnoreCase("§eSelect Teleport Location")) {
                List<Integer> pushTeleportsZ = Arrays.asList(
                        45, 45, 39, 49
                );
                List<Integer> pushTeleportsX = Arrays.asList(
                        -31, 31, 0, 0
                );
                float pitch = 0f;
                float yaw = 180f;
                int index = 0;
                World world = Bukkit.getWorld("build");
                Location baseLoc = new Location(world, 1073.5, -60, -451.5);
                double baseX = 1073.5;
                e.setCancelled(true);
                Player pushPlayer = (Player) e.getWhoClicked();
                if (PlayerConfig.get().getConfigurationSection("players").getKeys(false).contains(pushPlayer.getName())) {
                    String team = PlayerConfig.get().getString("players." + pushPlayer.getName() + ".team");
                    Map<String, Block[]> sortedMap = new TreeMap<>(plugin.mapSides);
                    for (Block[] blocks : sortedMap.values()) {
                        if (blocks[0].getType().equals(plugin.teamConcrete.get(team)) || blocks[1].getType().equals(plugin.teamConcrete.get(team))) {
                            baseLoc.setX(baseX + (index * 101));
                        }
                        if (blocks[1].getType().equals(plugin.teamConcrete.get(team))) {
                            pushTeleportsZ.replaceAll(integer -> -integer);
                            pushTeleportsX.replaceAll(integer -> -integer);
                            yaw = 0f;
                            break;
                        }
                        index++;
                    }
                    if(plugin.finaleActive){
                        baseLoc.setX(1506.5);
                    }
                }

                switch (e.getSlot()) {
                    case 10:
                        if (plugin.runningTimers.containsKey(pushPlayer.getName() + "respawn")) {
                            plugin.playerSelectedTeleport.put(pushPlayer, new Location(Bukkit.getWorld("build"), baseLoc.getX() + pushTeleportsX.getFirst(), baseLoc.getY(), baseLoc.getZ() + pushTeleportsZ.getFirst(), yaw, pitch));
                            pushPlayer.sendTitle("§c§lYou Died.", "§aLeft Lane selected.", 0, 30, 0);
                            pushPlayer.closeInventory();
                        } else {
                            if (plugin.runningTimers.containsKey("pushpoint") && plugin.runningTimers.get("pushpoint").getValue() > 5) {
                                pushPlayer.teleport(new Location(Bukkit.getWorld("build"), baseLoc.getX() + pushTeleportsX.getFirst(), baseLoc.getY(), baseLoc.getZ() + pushTeleportsZ.getFirst(), yaw, pitch));
                                plugin.playerSelectedTeleport.put(pushPlayer, new Location(Bukkit.getWorld("build"), baseLoc.getX() + pushTeleportsX.getFirst(), baseLoc.getY(), baseLoc.getZ() + pushTeleportsZ.getFirst(), yaw, pitch));
                                pushPlayer.setGameMode(GameMode.ADVENTURE);
                                pushPlayer.closeInventory();
                                plugin.playerSelectedTeleport.remove(pushPlayer);
                            }
                        }

                        break;
                    case 16:
                        if (plugin.runningTimers.containsKey(pushPlayer.getName() + "respawn")) {
                            plugin.playerSelectedTeleport.put(pushPlayer, new Location(Bukkit.getWorld("build"), baseLoc.getX() + pushTeleportsX.get(1), baseLoc.getY(), baseLoc.getZ() + pushTeleportsZ.get(1), yaw, pitch));
                            pushPlayer.sendTitle("§c§lYou Died.", "§aRight Lane selected.", 0, 30, 0);
                            pushPlayer.closeInventory();
                        } else {
                            if (plugin.runningTimers.containsKey("pushpoint") && plugin.runningTimers.get("pushpoint").getValue() > 5) {
                                pushPlayer.teleport(new Location(Bukkit.getWorld("build"), baseLoc.getX() + pushTeleportsX.get(1), baseLoc.getY(), baseLoc.getZ() + pushTeleportsZ.get(1), yaw, pitch));
                                plugin.playerSelectedTeleport.put(pushPlayer, new Location(Bukkit.getWorld("build"), baseLoc.getX() + pushTeleportsX.get(1), baseLoc.getY(), baseLoc.getZ() + pushTeleportsZ.get(1), yaw, pitch));
                                pushPlayer.setGameMode(GameMode.ADVENTURE);
                                pushPlayer.closeInventory();
                                plugin.playerSelectedTeleport.remove(pushPlayer);
                            }
                        }

                        break;
                    case 22:
                        if (plugin.runningTimers.containsKey(pushPlayer.getName() + "respawn")) {
                            plugin.playerSelectedTeleport.put(pushPlayer, new Location(Bukkit.getWorld("build"), baseLoc.getX() + pushTeleportsX.get(2), baseLoc.getY(), baseLoc.getZ() + pushTeleportsZ.get(2), yaw, pitch));
                            pushPlayer.sendTitle("§c§lYou Died.", "§aMiddle Lane selected.", 0, 30, 0);
                            pushPlayer.closeInventory();
                        } else {
                            if (plugin.runningTimers.containsKey("pushpoint") && plugin.runningTimers.get("pushpoint").getValue() > 5) {
                                pushPlayer.teleport(new Location(Bukkit.getWorld("build"), baseLoc.getX() + pushTeleportsX.get(2), baseLoc.getY(), baseLoc.getZ() + pushTeleportsZ.get(2), yaw, pitch));
                                plugin.playerSelectedTeleport.put(pushPlayer, new Location(Bukkit.getWorld("build"), baseLoc.getX() + pushTeleportsX.get(2), baseLoc.getY(), baseLoc.getZ() + pushTeleportsZ.get(2), yaw, pitch));
                                pushPlayer.setGameMode(GameMode.ADVENTURE);
                                pushPlayer.closeInventory();
                                plugin.playerSelectedTeleport.remove(pushPlayer);
                            }
                        }

                        break;
                    case 40:
                        baseLoc.setY(-51);
                        if (plugin.runningTimers.containsKey(pushPlayer.getName() + "respawn")) {
                            plugin.playerSelectedTeleport.put(pushPlayer, new Location(Bukkit.getWorld("build"), baseLoc.getX() + pushTeleportsX.get(3), baseLoc.getY(), baseLoc.getZ() + pushTeleportsZ.get(3), yaw, pitch));
                            pushPlayer.sendTitle("§c§lYou Died.", "§aBase selected.", 0, 30, 0);
                            pushPlayer.closeInventory();
                        } else {
                            if (plugin.runningTimers.containsKey("pushpoint") && plugin.runningTimers.get("pushpoint").getValue() > 5) {
                                pushPlayer.teleport(new Location(Bukkit.getWorld("build"), baseLoc.getX() + pushTeleportsX.get(3), baseLoc.getY(), baseLoc.getZ() + pushTeleportsZ.get(3), yaw, pitch));
                                plugin.playerSelectedTeleport.put(pushPlayer, new Location(Bukkit.getWorld("build"), baseLoc.getX() + pushTeleportsX.get(3), baseLoc.getY(), baseLoc.getZ() + pushTeleportsZ.get(3), yaw, pitch));
                                pushPlayer.setGameMode(GameMode.ADVENTURE);
                                pushPlayer.closeInventory();
                                plugin.playerSelectedTeleport.remove(pushPlayer);
                            }
                        }
                        break;

                    default:
                        break;
                }
            }
            if(plugin.ppTeamKitInventories.get(PlayerConfig.get().getString("players." + e.getWhoClicked().getName() + ".team")).equals(e.getClickedInventory())){
                e.setCancelled(true);
                String team = PlayerConfig.get().getString("players." + e.getWhoClicked().getName() + ".team");
                Inventory teamInventory = plugin.ppTeamKitInventories.get(team);
                Player p = (Player) e.getWhoClicked();
                boolean kitSelected = false;
                switch(e.getSlot()){
                    case 11:
                        if(plugin.ppTeamSelectedKits.get(team) != null) {
                            for (String kit : plugin.ppTeamSelectedKits.get(team).values()) {
                                if (kit.equals("Tank")) {
                                    p.sendMessage("This kit has already been selected!");
                                    kitSelected = true;
                                    break;
                                }
                            }
                        }
                        if(!kitSelected){
                            p.sendMessage("§8[§e!§8] §eYou have selected the " + e.getCurrentItem().getItemMeta().getDisplayName() + "§e kit!");
                            p.getInventory().clear();
                            if(plugin.ppTeamSelectedKits.get(team) != null) {
                                if (plugin.ppTeamSelectedKits.get(team).containsKey(p)) {
                                    replacePlayerHead(e.getInventory(), p);
                                }
                            }

                            plugin.ppTeamSelectedKits.computeIfAbsent(team, t -> new HashMap<>())
                                    .put(p, "Tank");
                            givePPKits(p);
                            ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
                            SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
                            meta.setOwningPlayer(p);
                            meta.setDisplayName(plugin.getPlayerDisplayName(p.getName()));
                            playerHead.setItemMeta(meta);
                            teamInventory.setItem(20, playerHead);
                        }
                        break;
                    case 12:
                        if(plugin.ppTeamSelectedKits.get(team) != null) {
                            for (String kit : plugin.ppTeamSelectedKits.get(team).values()) {
                                if (kit.equals("Archer")) {
                                    p.sendMessage("This kit has already been selected!");
                                    kitSelected = true;
                                    break;
                                }
                            }
                        }
                        if(!kitSelected){
                            p.sendMessage("Kit: " + e.getCurrentItem().getItemMeta().getDisplayName() + "§8 selected!");
                            p.getInventory().clear();

                            if(plugin.ppTeamSelectedKits.get(team) != null) {
                                if (plugin.ppTeamSelectedKits.get(team).containsKey(p)) {
                                    replacePlayerHead(e.getInventory(), p);
                                }
                            }

                            plugin.ppTeamSelectedKits.computeIfAbsent(team, t -> new HashMap<>())
                                    .put(p, "Archer");
                            givePPKits(p);
                            ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
                            SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
                            meta.setOwningPlayer(p);
                            meta.setDisplayName(plugin.getPlayerDisplayName(p.getName()));
                            playerHead.setItemMeta(meta);
                            teamInventory.setItem(21, playerHead);
                        }
                        break;
                    case 13:
                        if(plugin.ppTeamSelectedKits.get(team) != null) {
                            for (String kit : plugin.ppTeamSelectedKits.get(team).values()) {
                                if (kit.equals("Duelist")) {
                                    p.sendMessage("This kit has already been selected!");
                                    kitSelected = true;
                                    break;
                                }
                            }
                        }
                        if(!kitSelected){
                            p.sendMessage("Kit: " + e.getCurrentItem().getItemMeta().getDisplayName() + "§8 selected!");
                            p.getInventory().clear();

                            if(plugin.ppTeamSelectedKits.get(team) != null) {
                                if (plugin.ppTeamSelectedKits.get(team).containsKey(p)) {
                                    replacePlayerHead(e.getInventory(), p);
                                }
                            }

                            plugin.ppTeamSelectedKits.computeIfAbsent(team, t -> new HashMap<>())
                                    .put(p, "Duelist");
                            givePPKits(p);
                            ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
                            SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
                            meta.setOwningPlayer(p);
                            meta.setDisplayName(plugin.getPlayerDisplayName(p.getName()));
                            playerHead.setItemMeta(meta);
                            teamInventory.setItem(22, playerHead);
                        }
                        break;
                    case 14:
                        if(plugin.ppTeamSelectedKits.get(team) != null) {
                            for (String kit : plugin.ppTeamSelectedKits.get(team).values()) {
                                if (kit.equals("Healer")) {
                                    p.sendMessage("This kit has already been selected!");
                                    kitSelected = true;
                                    break;
                                }
                            }
                        }
                        if(!kitSelected){
                            p.sendMessage("Kit: " + e.getCurrentItem().getItemMeta().getDisplayName() + "§8 selected!");
                            p.getInventory().clear();

                            if(plugin.ppTeamSelectedKits.get(team) != null) {
                                if (plugin.ppTeamSelectedKits.get(team).containsKey(p)) {
                                    replacePlayerHead(e.getInventory(), p);
                                }
                            }

                            plugin.ppTeamSelectedKits.computeIfAbsent(team, t -> new HashMap<>())
                                    .put(p, "Healer");
                            givePPKits(p);
                            ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
                            SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
                            meta.setOwningPlayer(p);
                            meta.setDisplayName(plugin.getPlayerDisplayName(p.getName()));
                            playerHead.setItemMeta(meta);
                            teamInventory.setItem(23, playerHead);
                        }
                        break;
                    case 15:
                        if(plugin.ppTeamSelectedKits.get(team) != null) {
                            for (String kit : plugin.ppTeamSelectedKits.get(team).values()) {
                                if (kit.equals("Flanker")) {
                                    p.sendMessage("This kit has already been selected!");
                                    kitSelected = true;
                                    break;
                                }
                            }
                        }
                        if(!kitSelected){
                            p.sendMessage("Kit: " + e.getCurrentItem().getItemMeta().getDisplayName() + "§8 selected!");
                            p.getInventory().clear();

                            if(plugin.ppTeamSelectedKits.get(team) != null) {
                                if (plugin.ppTeamSelectedKits.get(team).containsKey(p)) {
                                    replacePlayerHead(e.getInventory(), p);
                                }
                            }

                            plugin.ppTeamSelectedKits.computeIfAbsent(team, t -> new HashMap<>())
                                    .put(p, "Flanker");
                            givePPKits(p);
                            ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
                            SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
                            meta.setOwningPlayer(p);
                            meta.setDisplayName(plugin.getPlayerDisplayName(p.getName()));
                            playerHead.setItemMeta(meta);
                            teamInventory.setItem(24, playerHead);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
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
        if((plugin.currentMode.equals("Lobby") || plugin.currentMode.equals("Voting") || plugin.currentMode.equals("Start")) && PlayerConfig.get().getConfigurationSection("players").getKeys(false).contains(e.getWhoClicked().getName())) {
            e.setCancelled(true);
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
                        case 7:
                            if(e.getWhoClicked().getInventory().getArmorContents()[3] != null){
                                if(e.getWhoClicked().getInventory().getArmorContents()[3].equals(PhilipConfig.get().getItemStack("cosmetics.8.item"))) {
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
                                            PhilipConfig.get().getItemStack("cosmetics.8.item")
                                    };
                                    e.getWhoClicked().getInventory().setArmorContents(armour);
                                    plugin.messagePlayer((Player) e.getWhoClicked(), "§eCosmetic equipped.");
                                }
                            } else {
                                ItemStack[] armour = new ItemStack[]{
                                        e.getWhoClicked().getInventory().getArmorContents()[0],
                                        e.getWhoClicked().getInventory().getArmorContents()[1],
                                        e.getWhoClicked().getInventory().getArmorContents()[2],
                                        PhilipConfig.get().getItemStack("cosmetics.8.item")
                                };
                                e.getWhoClicked().getInventory().setArmorContents(armour);
                                plugin.messagePlayer((Player) e.getWhoClicked(), "§eCosmetic equipped.");
                            }

                            break;
                        case 8:
                            if(e.getWhoClicked().getInventory().contains(PhilipConfig.get().getItemStack("cosmetics.9.item"))){
                                e.getWhoClicked().getInventory().remove(PhilipConfig.get().getItemStack("cosmetics.9.item"));
                                plugin.messagePlayer((Player) e.getWhoClicked(), "§eCosmetic unequipped.");
                            } else if (e.getWhoClicked().getInventory().getItemInOffHand().equals(PhilipConfig.get().getItemStack("cosmetics.9.item"))) {
                                e.getWhoClicked().getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                                plugin.messagePlayer((Player) e.getWhoClicked(), "§eCosmetic unequipped.");
                            } else {
                                e.getWhoClicked().getInventory().setItemInOffHand(PhilipConfig.get().getItemStack("cosmetics.9.item"));
                                plugin.messagePlayer((Player) e.getWhoClicked(), "§eCosmetic equipped.");
                            }
                            break;
                        case 9:
                            if(e.getWhoClicked().getInventory().contains(PhilipConfig.get().getItemStack("cosmetics.10.item"))){
                                e.getWhoClicked().getInventory().remove(PhilipConfig.get().getItemStack("cosmetics.10.item"));
                                plugin.messagePlayer((Player) e.getWhoClicked(), "§eCosmetic unequipped.");
                            } else if (e.getWhoClicked().getInventory().getItemInOffHand().equals(PhilipConfig.get().getItemStack("cosmetics.10.item"))) {
                                e.getWhoClicked().getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                                plugin.messagePlayer((Player) e.getWhoClicked(), "§eCosmetic unequipped.");
                            } else {
                                e.getWhoClicked().getInventory().setItemInOffHand(PhilipConfig.get().getItemStack("cosmetics.10.item"));
                                plugin.messagePlayer((Player) e.getWhoClicked(), "§eCosmetic equipped.");
                            }
                            break;
                    }
                } else {
                    plugin.messagePlayer((Player) e.getWhoClicked(), "You cannot afford this cosmetic.");
                }
            }
        }

        if(e.getView().getTitle().equalsIgnoreCase("§ePlayers")) {
            e.setCancelled(true);
            switch (e.getSlot()){
                case 4,13,22,31:
                    break;
                default:
                    if(e.getSlot() >= 0 && e.getSlot() <= 35) {
                        if (e.getClick() == ClickType.LEFT) {
                            if (e.getCurrentItem() != null) {
                                String name = e.getCurrentItem().getItemMeta().getDisplayName();
                                Player target = Bukkit.getPlayer(name);
                                if(target != null){
                                    e.getWhoClicked().teleport(target);
                                }
                                e.getWhoClicked().closeInventory();
                            }
                        }

                    }
                    break;
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

    public void givePPKits(Player player) {
        player.getInventory().clear();
        ItemStack arrows = new ItemStack(Material.ARROW, 12);
        ItemStack food = new ItemStack(Material.COOKED_BEEF, 16);
        ItemStack goldenapple = new ItemStack(Material.GOLDEN_APPLE);
        String team = PlayerConfig.get().getString("players." + player.getName() + ".team");
        switch(plugin.ppTeamSelectedKits.get(team).get(player)) {
            case "Tank":
                ItemStack tankChest = new ItemStack(Material.IRON_CHESTPLATE);
                ItemStack tankBoots = new ItemStack(Material.IRON_BOOTS);
                makeUnbreakable(tankChest);
                makeUnbreakable(tankBoots);
                player.getInventory().setChestplate(tankChest);
                player.getInventory().setBoots(tankBoots);

                ItemStack tanksword = new ItemStack(Material.WOODEN_SWORD);
                ItemStack tankcrossbow = new ItemStack(Material.CROSSBOW);
                tankcrossbow.addEnchantment(Enchantment.QUICK_CHARGE, 2);
                ItemStack tankarrows = new ItemStack(Material.ARROW, 8);
                makeUnbreakable(tanksword);
                makeUnbreakable(tankcrossbow);

                player.getInventory().addItem(tanksword, tankcrossbow, food, tankarrows);
                break;

            case "Archer":
                ItemStack archerChest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
                ItemStack archerBoots = new ItemStack(Material.IRON_BOOTS);
                makeUnbreakable(archerChest);
                makeUnbreakable(archerBoots);
                player.getInventory().setChestplate(archerChest);
                player.getInventory().setBoots(archerBoots);

                ItemStack archersword = new ItemStack(Material.WOODEN_SWORD);
                ItemStack archercrossbow = new ItemStack(Material.CROSSBOW);
                archercrossbow.addEnchantment(Enchantment.QUICK_CHARGE, 2);
                ItemStack archerarrows = new ItemStack(Material.ARROW, 6);
                makeUnbreakable(archersword);
                makeUnbreakable(archercrossbow);

                player.getInventory().addItem(archersword, archercrossbow, food, arrows, archerarrows);
                break;

            case "Duelist":
                ItemStack duelistChest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
                ItemStack duelistBoots = new ItemStack(Material.IRON_BOOTS);
                makeUnbreakable(duelistChest);
                makeUnbreakable(duelistBoots);
                player.getInventory().setChestplate(duelistChest);
                player.getInventory().setBoots(duelistBoots);

                ItemStack duelistsword = new ItemStack(Material.STONE_SWORD);
                ItemStack duelistbow = new ItemStack(Material.BOW);
                makeUnbreakable(duelistsword);
                makeUnbreakable(duelistbow);

                player.getInventory().addItem(duelistsword, duelistbow, food, arrows);
                break;

            case "Healer":
                ItemStack healerChest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
                ItemStack healerBoots = new ItemStack(Material.IRON_BOOTS);
                makeUnbreakable(healerChest);
                makeUnbreakable(healerBoots);
                player.getInventory().setChestplate(healerChest);
                player.getInventory().setBoots(healerBoots);

                ItemStack healersword = new ItemStack(Material.WOODEN_SWORD);
                ItemStack healerbow = new ItemStack(Material.BOW);
                makeUnbreakable(healersword);
                makeUnbreakable(healerbow);

                ItemStack healingpotion = new ItemStack(Material.SPLASH_POTION);
                PotionMeta healingpotionsmeta = (PotionMeta) healingpotion.getItemMeta();
                healingpotionsmeta.setBasePotionType(PotionType.HEALING);
                healingpotion.setItemMeta(healingpotionsmeta);

                ItemStack regenpotion = new ItemStack(Material.SPLASH_POTION);
                PotionMeta regenpotionmeta = (PotionMeta) regenpotion.getItemMeta();
                regenpotionmeta.setBasePotionType(PotionType.WATER);
                regenpotionmeta.setDisplayName("§fSplash Regeneration Potion");
                PotionEffect effect = new PotionEffect(PotionEffectType.REGENERATION, 5 * 20, 1);
                regenpotionmeta.addCustomEffect(effect, true);
                regenpotion.setItemMeta(regenpotionmeta);

                player.getInventory().addItem(healersword, healerbow, food, goldenapple, healingpotion, regenpotion, arrows);
                break;

            case "Flanker":
                ItemStack flankerChest = new ItemStack(Material.LEATHER_CHESTPLATE);
                ItemStack flankerBoots = new ItemStack(Material.IRON_BOOTS);
                makeUnbreakable(flankerChest);
                makeUnbreakable(flankerBoots);
                player.getInventory().setChestplate(flankerChest);
                player.getInventory().setBoots(flankerBoots);

                ItemStack flankersword = new ItemStack(Material.STONE_SWORD);
                ItemStack flankerbow = new ItemStack(Material.BOW);
                makeUnbreakable(flankersword);
                makeUnbreakable(flankerbow);

                ItemStack speedpotions = new ItemStack(Material.POTION, 2);
                PotionMeta speedpotionsmeta = (PotionMeta) speedpotions.getItemMeta();
                speedpotionsmeta.setBasePotionType(PotionType.SWIFTNESS);
                speedpotions.setItemMeta(speedpotionsmeta);

                player.getInventory().addItem(flankersword, flankerbow, food, speedpotions, goldenapple, arrows);
                break;

            default:
                player.sendMessage("No kit selected!");
                break;
        }
    }

    public void makeUnbreakable(ItemStack item) {
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;

        if (event.getView().getTitle().equals("§eSelect Teleport Location")) {
            if(!plugin.playerSelectedTeleport.containsKey(player) && plugin.runningTimers.containsKey("pushpoint") && plugin.runningTimers.get("pushpoint").getValue() > 5) {
                Bukkit.getScheduler().runTask(plugin, () -> player.openInventory(event.getInventory()));
            }
        }

        if(plugin.ppTeamKitInventories.containsValue(event.getInventory())){
            String team = PlayerConfig.get().getString("players." + player.getName() + ".team");
            if(plugin.ppTeamSelectedKits.get(team) != null) {
                if (!plugin.ppTeamSelectedKits.get(team).containsKey(player)) {
                    Bukkit.getScheduler().runTask(plugin, () -> player.openInventory(event.getInventory()));
                } else {
                    if (plugin.runningTimers.containsKey("pushpointstart")) {
                        player.sendMessage("§7[§6!§7] §6Press your §a§lOff-hand §6key to re-open the kits inventory before the game starts.");
                    }
                }
            } else {
                Bukkit.getScheduler().runTask(plugin, () -> player.openInventory(event.getInventory()));
            }
        }
    }

    public void replacePlayerHead(Inventory inventory, Player target) {
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);

            if (item == null || item.getType() != Material.PLAYER_HEAD) continue;
            if (!(item.getItemMeta() instanceof SkullMeta)) continue;

            SkullMeta meta = (SkullMeta) item.getItemMeta();
            if (meta.getOwningPlayer() == null) continue;

            if (meta.getOwningPlayer().getUniqueId().equals(target.getUniqueId())) {
                inventory.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS));
            }
        }
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent e) {
        Player player = e.getPlayer();

        if (player.getOpenInventory().getTopInventory().getType() != InventoryType.CRAFTING) {
            return;
        }

        if(plugin.runningTimers.containsKey("pushpointstart")){
            String team = PlayerConfig.get().getString("players." + player.getName() + ".team");
            player.openInventory(plugin.ppTeamKitInventories.get(team));
        }
    }
}
