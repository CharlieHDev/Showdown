package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.GubTPConfig;
import me.chazzagram.showdown2.files.PlayerConfig;
import me.chazzagram.showdown2.files.TeamsConfig;
import me.chazzagram.showdown2.files.TeleportConfig;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;

public class LastHitEvent implements Listener {

    private static Showdown2 plugin;

    Random rand = new Random();

    public LastHitEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent e) {
        if(plugin.currentMode.equals("Voting")){
            e.setCancelled(true);
            if (plugin.runningTimers.containsKey("slimeBall") && plugin.runningTimers.get("slimeBall").getValue() > 6) {
                if (e.getDamager() instanceof Player hitter && e.getEntity() instanceof LivingEntity chicken) {
                    if (chicken.getType().equals(EntityType.CHICKEN)) {
                        plugin.slimeBallVote = hitter;
                        Vector velocity = new Vector(-1 + (rand.nextDouble() * 2), 1.0, -1 + (rand.nextDouble() * 2));
                        chicken.setVelocity(velocity);
                        hitter.playSound(hitter, Sound.ENTITY_CHICKEN_HURT, 10, 1);
                        if (plugin.chickenBall.getLocation().clone().subtract(0, 1, 0).getBlock().getType() != Material.AIR) {
                            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    try {
                                        plugin.glowingEntities.setGlowing(plugin.chickenBall, player, plugin.modeColors.get(plugin.woolModes.get(plugin.playerVote.get(hitter))));
                                    } catch (ReflectiveOperationException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                }
                            }, 20L);
                        } else {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                try {
                                    plugin.glowingEntities.setGlowing(plugin.chickenBall, player, plugin.modeColors.get(plugin.woolModes.get(plugin.playerVote.get(hitter))));
                                } catch (ReflectiveOperationException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        }
                    }
                }
            }
        } else if (plugin.currentMode.equals("Zoomo Go") && plugin.pvpEnabled) {
            if (e.getDamager() instanceof Player killer && e.getEntity() instanceof Player victim) {
                if (PlayerConfig.get().getString("players." + killer.getName() + ".team").equals(PlayerConfig.get().getString("players." + victim.getName() + ".team"))) {
                    e.setCancelled(true);
                } else {
                    plugin.lastHitPlayer.put(victim.getName(), killer.getName());
                }
            }
        } else if (plugin.currentMode.equals("Craftalot")) {
            e.setCancelled(true);
            if(e.getDamager() instanceof Player && (e.getEntity() instanceof Cow || e.getEntity() instanceof Sheep)) {
                e.setCancelled(false);
            }
        } else if (plugin.currentMode.equals("Slime Golf")) {
            if (e.getDamager() instanceof Slime && e.getEntity() instanceof Player) {
                e.setCancelled(true);
            }
        } else if (plugin.currentMode.equals("Colour Dash")  && plugin.pvpEnabled) {
            if(e.getDamager() instanceof Player attacker && e.getEntity() instanceof EnderCrystal enderCrystal){
                if(!plugin.runningTimers.containsKey(attacker.getName() + "mysterybox")) {
                    plugin.summonFirework(enderCrystal.getLocation(), PlayerConfig.get().getString("players." + attacker.getName() + ".team"));

                    Location itemBoxLoc = enderCrystal.getLocation().clone();
                    enderCrystal.remove();
                    BukkitTask task1 = new BukkitRunnable() {
                        int timeLeft = 4;

                        @Override
                        public void run() {
                            if (plugin.runningTimers.containsKey(attacker.getName() + "mysteryboxdisappear")) {
                                if (!plugin.pausedTimers.contains(attacker.getName() + "mysteryboxdisappear")) {
                                    plugin.runningTimers.get(attacker.getName() + "mysteryboxdisappear").setValue(timeLeft);
                                    timeLeft--;
                                    if (timeLeft == 0) {
                                        Bukkit.getWorld("build").spawnEntity(itemBoxLoc, EntityType.END_CRYSTAL);
                                        plugin.runningTimers.remove(attacker.getName() + "mysteryboxdisappear");
                                        cancel();
                                    }
                                }
                            } else {
                                plugin.messageConsole("Timer removed by external factor.");
                                cancel();
                            }
                        }

                    }.runTaskTimer(plugin, 0L, 20L);

                    plugin.runningTimers.put(attacker.getName() + "mysteryboxdisappear", new AbstractMap.SimpleEntry<>(task1, 4));

                    enderCrystal.remove();
                    e.setCancelled(true);


                    BukkitTask task = new BukkitRunnable() {
                        int timeLeft = 90;
                        Random rand = new Random();
                        int base;
                        int offset;
                        int unicodeChar;

                        @Override
                        public void run() {
                            if (plugin.runningTimers.containsKey(attacker.getName() + "mysterybox")) {
                                if (!plugin.pausedTimers.contains(attacker.getName() + "mysterybox")) {
                                    plugin.runningTimers.get(attacker.getName() + "mysterybox").setValue(timeLeft);
                                    timeLeft--;
                                    switch (timeLeft) {
                                        case 90, 88, 86, 84, 82, 80, 78, 76, 74, 72, 70, 65, 60, 40, 20:
                                            attacker.playSound(attacker.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 10, 1);
                                            base = 0xE000;
                                            offset = rand.nextInt(5);
                                            unicodeChar = base + offset;
                                            String character = new String(Character.toChars(unicodeChar));
                                            attacker.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(character));
                                            break;
                                        default:
                                            break;
                                    }
                                    if (timeLeft == 0) {
                                        attacker.playSound(attacker.getLocation(), Sound.BLOCK_NOTE_BLOCK_FLUTE, 10, 1);
                                        base = 0xE000;
                                        offset = rand.nextInt(5);
                                        unicodeChar = base + offset;
                                        String character = new String(Character.toChars(unicodeChar));
                                        attacker.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(character));

                                        if (offset == 1) {
                                            PotionEffect PotionEffect = new PotionEffect(PotionEffectType.JUMP_BOOST, 120, 2, false, false);
                                            attacker.addPotionEffect(PotionEffect);
                                        } else if (offset == 3) {
                                            PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SPEED, 120, 2, false, false);
                                            attacker.addPotionEffect(PotionEffect);
                                        } else if (offset == 4) {
                                            for (ItemStack item : getCDItems().get(3)) {
                                                attacker.getInventory().addItem(item);
                                            }
                                        } else if (offset == 0) {
                                            for (ItemStack item : getCDItems().get(offset)) {
                                                attacker.getInventory().addItem(item);
                                            }
                                        } else if (offset == 2) {
                                            for (ItemStack item : getCDItems().get(1)) {
                                                attacker.getInventory().addItem(item);
                                            }
                                            }
                                        attacker.sendTitle("", getCDItemNames().get(offset), 0, 20, 0);
                                        plugin.runningTimers.remove(attacker.getName() + "mysterybox");
                                        cancel();
                                    }
                                }
                            } else {
                                plugin.messageConsole("Timer removed by external factor.");
                                cancel();
                            }
                        }

                    }.runTaskTimer(plugin, 0L, 1L);

                    plugin.runningTimers.put(attacker.getName() + "mysterybox", new AbstractMap.SimpleEntry<>(task, 90));
                } else {
                    e.setCancelled(true);
                }
            }
        } else if (plugin.currentMode.equals("Lobby")) {
            e.setCancelled(true);
        } else {
            if(plugin.pvpEnabled) {
                if (e.getDamager() instanceof Player killer && e.getEntity() instanceof Player victim) {
                    handlePlayerDamage(killer, victim, e);
                } else if (e.getDamager() instanceof Projectile projectile && projectile.getShooter() instanceof Player killer) {
                    if (e.getEntity() instanceof Player victim) {
                        if (victim.getHealth() - e.getFinalDamage() <= 0) {
                            projectile.remove();
                        }
                        handlePlayerDamage(killer, victim, e);
                    }
                }
            } else {
                e.setCancelled(true);
            }
        }
    }

    public void handlePlayerDamage(Player killer, Player victim, EntityDamageByEntityEvent e) {
        if (PlayerConfig.get().getString("players." + killer.getName() + ".team").equals(PlayerConfig.get().getString("players." + victim.getName() + ".team"))) {
            e.setCancelled(true);
        } else {
            if (plugin.currentMode.equals("Gub Game")) {
                if (victim.getHealth() - e.getFinalDamage() <= 0) {
                    Bukkit.getWorld("build").spawnParticle(Particle.RAID_OMEN, victim.getLocation().clone().add(0,1,0), 20, 0.2, 0.5, 0.2, 0);
                    killer.playSound(killer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 2);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> killer.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(plugin.getPlayerDisplayName(victim.getName()) + " §7| §c♥§c§l0.0")), 1L);
                    for (Player p : plugin.getPlayers()) {
                        plugin.messagePlayer(p, "§c\uD83D\uDC80 §7| " + plugin.formatKillMessage(killer.getName(), victim.getName()));
                    }
                    plugin.killRecord.add(plugin.getPlayerDisplayName(killer.getName()) + " §c⚔ " + plugin.getPlayerDisplayName(victim.getName()));
                    plugin.messagePlayer(victim, "§c\uD83D\uDC80 §7| §cYou died to " + plugin.getPlayerDisplayName(killer.getName()));
                    e.setCancelled(true);
                    victim.setGameMode(GameMode.SPECTATOR);
                    victim.getInventory().clear();
                    for (ItemStack item : getGubKits().get(plugin.gubGameKills.get(victim.getName()))) {
                        victim.getInventory().addItem(item);
                    }
                    plugin.gubGameKills.put(killer.getName(), plugin.gubGameKills.get(killer.getName()) + 1);
                    plugin.earnPoints(killer.getName(), 40 - plugin.gubKitKills.get(plugin.gubGameKills.get(killer.getName())), true);
                    plugin.gubKitKills.put(plugin.gubGameKills.get(killer.getName()), plugin.gubKitKills.get(plugin.gubGameKills.get(killer.getName())) + 1);
                    victim.setHealth(20);
                    if (plugin.gubGameKills.get(killer.getName()).equals(14)) {
                        killer.setGameMode(GameMode.SPECTATOR);
                        killer.sendTitle("§eFINISH", "§e\uD83D\uDCB0" + (41 - plugin.gubKitKills.get(plugin.gubGameKills.get(killer.getName()))) + " §7| §c\uD83D\uDC80 " + plugin.getPlayerDisplayName(victim.getName()), 0, 20, 0);
                        for(Player p : Bukkit.getOnlinePlayers()){
                            plugin.messagePlayer(p, "§e♛ §7| " + plugin.getPlayerDisplayName(killer.getName()) + " §fhas finished the final kit!");
                        }
                    } else {
                        killer.sendTitle("", "§e\uD83D\uDCB0" + (41 - plugin.gubKitKills.get(plugin.gubGameKills.get(killer.getName()))) + " §7| §c\uD83D\uDC80 " + plugin.getPlayerDisplayName(victim.getName()), 0, 20, 0);
                        killer.getInventory().clear();
                        plugin.messagePlayer(killer, "§c\uD83D\uDC80 §7| NEXT KIT! (§e§l" + plugin.gubGameKills.get(killer.getName()) + "/14§7)");
                        for (ItemStack item : getGubKits().get(plugin.gubGameKills.get(killer.getName()))) {
                            killer.getInventory().addItem(item);
                        }
                    }
                    BukkitTask task = new BukkitRunnable() {
                        int timeLeft = 6;

                        @Override
                        public void run() {
                            if (plugin.runningTimers.containsKey(victim.getName() + "respawn")) {
                                if (!plugin.pausedTimers.contains(victim.getName() + "respawn")) {
                                    plugin.runningTimers.get(victim.getName() + "respawn").setValue(timeLeft);
                                    timeLeft--;
                                    if (timeLeft == 0) {
                                        plugin.messageConsole("Timer finished.");
                                        victim.setGameMode(GameMode.ADVENTURE);
                                        victim.sendTitle("", "", 0, 30, 0);
                                        Random rand = new Random();
                                        int index = rand.nextInt(GubTPConfig.get().getConfigurationSection("teleports").getKeys(false).size()) + 1;
                                        victim.teleport(GubTPConfig.get().getLocation("teleports.loc" + index));
                                        plugin.runningTimers.remove(victim.getName() + "respawn");
                                        cancel();
                                    } else {
                                        victim.sendTitle("§c§lYou Died.", "§6Respawning in §c" + timeLeft + "..", 0, 30, 0);
                                        plugin.messageConsole(timeLeft + " seconds left..");
                                    }
                                }
                            } else {
                                plugin.messageConsole("Timer removed by external factor.");
                                cancel();
                            }
                        }

                    }.runTaskTimer(plugin, 0L, 20L);

                    plugin.runningTimers.put(victim.getName() + "respawn", new AbstractMap.SimpleEntry<>(task, 6));
                } else {
                    Bukkit.getScheduler().runTaskLater(plugin, () -> killer.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(plugin.getPlayerDisplayName(victim.getName()) + " §7| §c♥§c§l" + PlaceholderAPI.setPlaceholders(victim, "%player_health_rounded%"))), 1L);
                }
            } else if (plugin.currentMode.equals("Survival Games")) {
                if (victim.getHealth() - e.getFinalDamage() <= 0) {
                    Bukkit.getWorld("build").spawnParticle(Particle.RAID_OMEN, victim.getLocation().clone().add(0,1,0), 20, 0.2, 0.5, 0.2, 0);
                    killer.playSound(killer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 2);
                    plugin.messagePlayer(victim, "§c\uD83D\uDC80 §7| §cYou died to " + plugin.getPlayerDisplayName(killer.getName()));
                    victim.sendTitle("§c§lYOU DIED.", "", 0, 40, 10);

                    Location location = victim.getLocation();

                    for (ItemStack item : victim.getInventory().getContents()) {
                        if (item != null && !item.getType().isAir()) {
                            victim.getWorld().dropItemNaturally(location, item);
                        }
                    }

                    victim.getInventory().clear();
                    victim.getInventory().setArmorContents(null);
                    victim.getInventory().setItemInOffHand(null);

                    e.setCancelled(true);
                    victim.setGameMode(GameMode.SPECTATOR);
                    plugin.deadPlayers.add(victim.getName());
                    plugin.earnPoints(killer.getName(), 35, true);
                    killer.sendTitle("", "§e\uD83D\uDCB035" + " §7| §c\uD83D\uDC80 " + plugin.getPlayerDisplayName(victim.getName()), 0, 20, 0);
                    for (Player p : plugin.getPlayers()) {
                        if (!p.getGameMode().equals(GameMode.SPECTATOR) && !p.getName().equals(killer.getName())) {
                            plugin.messagePlayer(p, "§e\uD83D\uDCB010 §7| " + plugin.formatKillMessage(killer.getName(), victim.getName()));
                            plugin.earnPoints(p.getName(), 10, true);
                        } else {
                            plugin.messagePlayer(p, "§c\uD83D\uDC80 §7| " + plugin.formatKillMessage(killer.getName(), victim.getName()));
                        }
                    }
                    plugin.killRecord.add(plugin.getPlayerDisplayName(killer.getName()) + " §c⚔ " + plugin.getPlayerDisplayName(victim.getName()));
                    switch (plugin.deadPlayers.size()) {
                        case 8:
                            plugin.newBorderRadius = 26;
                            for (Player p : plugin.getPlayers()) {
                                p.sendTitle("", "§e⚠ Border Shrinking ⚠", 0, 40, 10);
                            }
                            break;
                        case 16:
                            plugin.newBorderRadius = 56;
                            for (Player p : plugin.getPlayers()) {
                                p.sendTitle("", "§e⚠ Border Shrinking ⚠", 0, 40, 10);
                            }
                            break;
                        case 24:
                            plugin.newBorderRadius = 116;
                            for (Player p : plugin.getPlayers()) {
                                p.sendTitle("", "§e⚠ Border Shrinking ⚠", 0, 40, 10);
                            }
                            break;
                        case 32:
                            plugin.newBorderRadius = 176;
                            for (Player p : plugin.getPlayers()) {
                                p.sendTitle("", "§e⚠ Border Shrinking ⚠", 0, 40, 10);
                            }
                            break;
                        default:
                            break;
                    }
                    boolean teamDead = true;
                    for (String player : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + victim.getName() + ".team") + ".players")) {
                        if (!plugin.deadPlayers.contains(player)) {
                            teamDead = false;
                            break;
                        }
                    }

                    if (teamDead) {
                        for (Player player2 : Bukkit.getServer().getOnlinePlayers()) {
                            plugin.messagePlayer(player2, "\n§c§l\uD83D\uDC80 §7| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + victim.getName() + ".team")) + " §chave been eliminated.\n§f");
                        }
                        plugin.deadTeams.add(PlayerConfig.get().getString("players." + victim.getName() + ".team"));
                    }

                    Set<String> teamList = new HashSet<>();

                    for (Player player : plugin.getPlayers()) {
                        String team = PlayerConfig.get().getString("players." + player.getName() + ".team");
                        if (team != null && !team.isEmpty()) {
                            teamList.add(team);
                        }
                    }

                    if (plugin.deadTeams.size() == teamList.size() - 1) {
                        for(Player p : plugin.getPlayers()) {
                            if(!p.getGameMode().equals(GameMode.SPECTATOR)) {
                                plugin.winningTeam = PlayerConfig.get().getString("players." + p.getName() + ".team");
                                break;
                            }
                        }
                        if(Objects.equals(plugin.winningTeam, "")) {
                            plugin.winningTeam = "NO TEAM";
                        }
                        plugin.deadTeams.clear();
                        plugin.runningTimers.remove("survivalgames");
                        plugin.gameEnd();
                    }

                }
            }
        }
    }


    public ArrayList<ItemStack[]> getCDItems() {
        ArrayList<ItemStack[]> items = new ArrayList<>();

        ItemStack[] item1 = new ItemStack[1];
        item1[0] = new ItemStack(Material.BLUE_ICE);
        item1[0].setAmount(4);
        items.add(item1);

        ItemStack[] item2 = new ItemStack[1];
        item2[0] = new ItemStack(Material.STICK);
        item2[0].addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
        items.add(item2);

        ItemStack[] item3 = new ItemStack[1];
        item3[0] = new ItemStack(Material.WIND_CHARGE);
        item3[0].setAmount(3);
        items.add(item3);

        return items;
    }

    public ArrayList<String> getCDItemNames(){
        ArrayList<String> itemNames = new ArrayList<>();

        itemNames.add("§b§lIce Blocks");
        itemNames.add("§a§lJump Boost");
        itemNames.add("§d§lKnockback Stick");
        itemNames.add("§e§lSpeed");
        itemNames.add("§f§lWind Charges");

        return itemNames;
    }

    public ArrayList<ItemStack[]> getGubKits() {
        ArrayList<ItemStack[]> kits = new ArrayList<>();

        ItemStack[] kit1 = new ItemStack[1];
        kit1[0] = new ItemStack(Material.NETHERITE_SWORD);
        kits.add(kit1);

        ItemStack[] kit2 = new ItemStack[1];
        kit2[0] = new ItemStack(Material.DIAMOND_SWORD);
        kits.add(kit2);

        ItemStack[] kit3 = new ItemStack[1];
        kit3[0] = new ItemStack(Material.NETHERITE_AXE);
        kits.add(kit3);

        ItemStack[] kit4 = new ItemStack[1];
        kit4[0] = new ItemStack(Material.TRIDENT);
        kits.add(kit4);

        ItemStack[] kit5 = new ItemStack[2];
        kit5[0] = new ItemStack(Material.CROSSBOW);
        kit5[1] = new ItemStack(Material.ARROW);
        kits.add(kit5);

        ItemStack[] kit6 = new ItemStack[1];
        kit6[0] = new ItemStack(Material.DIAMOND_AXE);
        kits.add(kit6);

        ItemStack[] kit7 = new ItemStack[2];
        kit7[0] = new ItemStack(Material.BOW);
        kit7[1] = new ItemStack(Material.ARROW);
        kits.add(kit7);

        ItemStack[] kit8 = new ItemStack[1];
        kit8[0] = new ItemStack(Material.IRON_SWORD);
        kits.add(kit8);

        ItemStack[] kit9 = new ItemStack[1];
        kit9[0] = new ItemStack(Material.STONE_SWORD);
        kits.add(kit9);

        ItemStack[] kit10 = new ItemStack[1];
        kit10[0] = new ItemStack(Material.DIAMOND_SHOVEL);
        kits.add(kit10);

        ItemStack[] kit11 = new ItemStack[1];
        kit11[0] = new ItemStack(Material.IRON_PICKAXE);
        kits.add(kit11);

        ItemStack[] kit12 = new ItemStack[1];
        kit12[0] = new ItemStack(Material.STONE_SHOVEL);
        kits.add(kit12);

        ItemStack[] kit13 = new ItemStack[1];
        kit13[0] = new ItemStack(Material.WOODEN_PICKAXE);
        kits.add(kit13);

        ItemStack[] kit14 = new ItemStack[1];
        kit14[0] = new ItemStack(Material.AIR);
        kits.add(kit14);

        for(ItemStack[] kit : kits){
            for(ItemStack item : kit){
                if(item.getType() != Material.AIR) {
                    ItemMeta meta = item.getItemMeta();
                    if(item.getType() == Material.TRIDENT){
                        meta.addEnchant(Enchantment.LOYALTY, 3, true);
                    }
                    if(item.getType() == Material.CROSSBOW || item.getType() == Material.BOW){
                        meta.addEnchant(Enchantment.INFINITY, 1, true);
                        meta.addEnchant(Enchantment.POWER, 2, true);
                    }
                    if(item.getType() == Material.CROSSBOW) {
                        meta.addEnchant(Enchantment.QUICK_CHARGE, 3, true);
                    }
                    if(item.getType() == Material.BOW){
                        meta.addEnchant(Enchantment.POWER, 2, true);
                    }
                    meta.setUnbreakable(true);
                    item.setItemMeta(meta);
                }
            }
        }

        return kits;
    }
}
