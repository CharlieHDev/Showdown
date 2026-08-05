package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.*;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Snow;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LastHitEvent implements Listener {

    private static Showdown2 plugin;

    Random rand = new Random();

    public LastHitEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent e) {

        if(e.getDamager() instanceof Player p) {
            if (plugin.ghostManager.getGhostPlayers().contains(p.getName()))  { e.setCancelled(true); return; }
        }

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
            if(e.getDamager() instanceof Player player && e.getEntity() instanceof Interaction itemBox){
                Iterator<ItemBox> iterator = plugin.itemBoxes.iterator();

                while (iterator.hasNext()) {
                    ItemBox ib = iterator.next();

                    if (Objects.equals(itemBox, ib.getInteraction())) {

                        if(plugin.votingEnabled){
                            if(ib.getItem().equals(new ItemStack(Material.TNT))){
                                if(plugin.powerUpHolders.contains(player.getName())){
                                    player.sendTitle("", "§c§lMax capacity reached.", 0, 10, 0);
                                    plugin.messagePlayer(player, "§c[!] You already have this power-up, §ehold crouch §cto charge it. (1)");
                                } else {
                                    plugin.powerUpHolders.add(player.getName());
                                    plugin.messagePlayer(player, """
                                        §f
                                        §f
                                        §c§lPOWER-UP READY!
                                        §e§oHold shift to charge up a voting explosion!
                                        §f
                                        """);
                                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 10, 1);
                                    plugin.itemBoxCount.replace(0, plugin.itemBoxCount.get(0) - 1);
                                    ib.despawn();

                                    iterator.remove();
                                }
                            } else {
                                if (countItem(player, ib.getItem()) < 1) {
                                    givePowerUp(player, ib.getItem());
                                    plugin.itemBoxCount.replace(0, plugin.itemBoxCount.get(0) - 1);
                                    ib.despawn();

                                    iterator.remove();
                                } else {
                                    player.sendTitle("", "§c§lMax capacity reached.", 0, 10, 0);
                                    plugin.messagePlayer(player, "§c[!] You have reached the max capacity for this power-up. (1)");
                                }
                                break;
                            }
                        }
                    }
                }
            }
        } else if(plugin.currentMode.equals("Crumble Clash")){
            e.setCancelled(true);
            if (e.getDamager() instanceof Fireball fireball && e.getEntity() instanceof Player victim){
                if(plugin.fireballSenders.containsKey(fireball)){
                    if (Objects.equals(PlayerConfig.get().getString("players." + victim.getName() + ".team"), PlayerConfig.get().getString("players." + plugin.fireballSenders.get(fireball).getName() + ".team"))) return;

                    plugin.crumbleKillTracker.put(
                            victim.getName(),
                            new CrumbleKillData(plugin.fireballSenders.get(fireball).getName(), System.currentTimeMillis())
                    );
                }
            }

            if(e.getDamager() instanceof Player player && e.getEntity() instanceof Interaction itemBox){
                Iterator<ItemBox> iterator = plugin.itemBoxes.iterator();

                while (iterator.hasNext()) {
                    ItemBox ib = iterator.next();

                    if (Objects.equals(itemBox, ib.getInteraction())) {

                        if (plugin.currentMode.equals("Crumble Clash")) {
                            if (countItem(player, ib.getItem()) < 3) {
                                givePowerUp(player, ib.getItem());
                                int y = ib.getInteraction().getLocation().getBlockY();
                                if (y >= 197 && y <= 199) {
                                    plugin.itemBoxCount.replace(0, plugin.itemBoxCount.get(0) - 1);
                                }
                                if (y >= 190 && y <= 192) {
                                    plugin.itemBoxCount.replace(1, plugin.itemBoxCount.get(1) - 1);
                                }
                                if (y >= 182 && y <= 184) {
                                    plugin.itemBoxCount.replace(2, plugin.itemBoxCount.get(2) - 1);
                                }
                                ib.despawn();

                                iterator.remove();
                            } else {
                                player.sendTitle("", "§c§lMax capacity reached.", 0, 10, 0);
                                plugin.messagePlayer(player, "§c[!] You have reached the max capacity for this power-up. (3)");
                            }
                            break;
                        }
                    }
                }
            }
            if (e.getDamager() instanceof Player attacker && e.getEntity() instanceof Player victim && e.getDamageSource().getDamageType() == DamageType.MACE_SMASH) {
                e.setCancelled(false);
                e.setDamage(0);

                if(plugin.currentSpleef.equals("§6§lCopper Spleef")) {
                    plugin.crumbleKillTracker.put(
                            victim.getName(),
                            new CrumbleKillData(attacker.getName(), System.currentTimeMillis())
                    );
                }

                Location blockBelow = victim.getLocation().clone().subtract(3, 2, 3);
                for (int x = 0; x <= 6; x++) {
                    for (int z = 0; z <= 6; z++) {
                        for (int y = 0; y <= 4; y++) {
                            Block block = blockBelow.clone().add(x, y, z).getBlock();
                            Material type = block.getType();
                            switch (type) {
                                case WAXED_COPPER_BLOCK:
                                    block.setType(Material.WAXED_EXPOSED_COPPER);
                                    break;
                                case WAXED_EXPOSED_COPPER:
                                    block.setType(Material.WAXED_WEATHERED_COPPER);
                                    break;
                                case WAXED_WEATHERED_COPPER:
                                    block.setType(Material.WAXED_OXIDIZED_COPPER);
                                    break;
                                case WAXED_OXIDIZED_COPPER:
                                    block.setType(Material.AIR);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
        } else if (plugin.currentMode.equals("Zoomo Go") && plugin.pvpEnabled) {
            if (e.getDamager() instanceof Player killer &&
                    e.getEntity() instanceof Player victim) {

                ItemStack victimChestplate = victim.getInventory().getChestplate();
                ItemStack killerChestplate = killer.getInventory().getChestplate();

                // Victim is invulnerable while wearing iron chestplate
                if (victimChestplate != null &&
                        victimChestplate.getType() == Material.IRON_CHESTPLATE) {

                    e.setCancelled(true);
                    return;
                }

                // Attacker loses invulnerability if they attack another player
                if (killerChestplate != null &&
                        killerChestplate.getType() == Material.IRON_CHESTPLATE) {

                    killer.getInventory().setHelmet(null);
                    killer.getInventory().setChestplate(null);
                    killer.getInventory().setLeggings(null);
                    killer.getInventory().setBoots(null);

                    killer.sendMessage("§cInvulnerability disabled by attacking another player.");
                }

                // Prevent team damage
                if (PlayerConfig.get().getString("players." + killer.getName() + ".team")
                        .equals(PlayerConfig.get().getString("players." + victim.getName() + ".team"))) {

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
            if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
                e.setCancelled(true);
            }
        } else if (plugin.currentMode.equals("Dimension Dash")){
                e.setCancelled(true);
        } else if (plugin.currentMode.equals("Colour Dash")  && plugin.pvpEnabled) {
            if(e.getDamager() instanceof Player attacker && e.getEntity() instanceof EnderCrystal enderCrystal){
                if(!plugin.runningTimers.containsKey(attacker.getName() + "mysterybox")) {
                    plugin.summonFirework(enderCrystal.getLocation(), PlayerConfig.get().getString("players." + attacker.getName() + ".team"));

                    Location itemBoxLoc = enderCrystal.getLocation().clone();
                    enderCrystal.remove();
                    BukkitTask task1 = new BukkitRunnable() {
                        int timeLeft = 2;

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

                    plugin.runningTimers.put(attacker.getName() + "mysteryboxdisappear", new AbstractMap.SimpleEntry<>(task1, 2));

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
                                        if(attacker.getInventory().contains(getCDItems().get(2)[0]) && offset == 4){
                                            offset = 3;
                                        }
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
                                            for (ItemStack item : getCDItems().get(2)) {
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

    @EventHandler
    public void onPlayerSwing(PlayerAnimationEvent event) {

        if(plugin.ghostManager.getGhostPlayers().contains(event.getPlayer().getName())) return;

        if(plugin.currentMode.equals("Presents")) {
            if (event.getAnimationType() != PlayerAnimationType.ARM_SWING) return;

            Player player = event.getPlayer();

            // Perform ray trace to detect BlockDisplay
            Location eyeLoc = player.getEyeLocation();
            Vector direction = eyeLoc.getDirection();

            // raySize >0 needed to detect zero-hitbox BlockDisplays
            RayTraceResult result = player.getWorld().rayTraceEntities(
                    eyeLoc,
                    direction,
                    5.0,            // max distance
                    0.5,            // ray "thickness" to catch zero-size entities
                    entity -> entity instanceof BlockDisplay // only BlockDisplays
            );

            if (result == null) return;

            BlockDisplay present = (BlockDisplay) result.getHitEntity();

            // Only handle tracked displays
            if (!plugin.lobbyPresents.containsKey(present)) return;

            if (!plugin.lobbyPresents.get(present)) {
                plugin.messagePlayer(player, "§eYou just found a present!");
                plugin.earnPoints(player.getName(), 2, true);
                plugin.lobbyPresents.replace(present, true);
                if(present.isGlowing()) {
                    present.setGlowing(false);
                }
                plugin.summonFirework(present.getLocation(), PlayerConfig.get().getString("players." + player.getName() + ".team"));
            } else {
                plugin.messagePlayer(player, "§cThis present has already been found!");
            }

            if (plugin.runningTimers.containsKey("present" + present.getLocation().getX() + present.getLocation().getY())) {
                plugin.runningTimers.get("present" + present.getLocation().getX() + present.getLocation().getY()).getKey().cancel();
                plugin.runningTimers.remove("present" + present.getLocation().getX() + present.getLocation().getY());
            }

            Vector3f translation = new Vector3f(-0.5F, -0.5F, -0.5F);
            Quaternionf leftRotation = new Quaternionf();
            Quaternionf rightRotation = new Quaternionf();
            Vector3f scaleVector = new Vector3f(1F, 1F, 1F);

            Transformation transformation = new Transformation(translation, leftRotation, scaleVector, rightRotation);
            present.setTransformation(transformation);

            String name = "present" + present.getLocation().getX() + present.getLocation().getY();
            BukkitTask task = new BukkitRunnable() {
                int timeLeft = 10;

                @Override
                public void run() {
                    if (plugin.runningTimers.containsKey(name)) {
                        if (!plugin.pausedTimers.contains(name)) {
                            timeLeft--;
                            plugin.runningTimers.get(name).setValue(timeLeft);

                            if(timeLeft == 5){
                                present.setBlock(Material.COAL_BLOCK.createBlockData());
                            }
                            if (timeLeft > 4) {
                                transformation.getScale().add(0.02F, 0.02F, 0.02F);
                                transformation.getTranslation().sub(0.01F, 0.01F, 0.01F);
                            } else {
                                transformation.getScale().sub(0.02F, 0.02F, 0.02F);
                                transformation.getTranslation().add(0.01F, 0.01F, 0.01F);
                            }
                            present.setTransformation(transformation);

                            if (timeLeft == 0) {
                                plugin.runningTimers.remove(name);
                                cancel();
                            }
                        }
                    } else {
                        cancel();
                    }
                }

            }.runTaskTimer(plugin, 0L, 1L);

            plugin.runningTimers.put(name, new AbstractMap.SimpleEntry<>(task, 11));
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
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        plugin.messagePlayer(p, "§c\uD83D\uDC80 §7| " + plugin.formatKillMessage(killer.getName(), victim.getName()));
                    }
                    plugin.killRecord.add(plugin.getPlayerDisplayName(killer.getName()) + " §c⚔ " + plugin.getPlayerDisplayName(victim.getName()));
                    plugin.messagePlayer(victim, "§c\uD83D\uDC80 §7| §cYou died to " + plugin.getPlayerDisplayName(killer.getName()));
                    plugin.playerKillCount.put(killer.getName(), plugin.playerKillCount.get(killer.getName()) + 1);
                    if(PlayerInfoConfig.get().getConfigurationSection("players").getKeys(false).contains(killer.getName())) {
                        PlayerInfoConfig.get().set("players." + killer.getName() + ".kills", PlayerInfoConfig.get().getInt("players." + killer.getName() + ".kills") + 1);
                        PlayerInfoConfig.save();
                    }
                    e.setCancelled(true);
                    victim.setGameMode(GameMode.SPECTATOR);
                    victim.getInventory().clear();
                    for (ItemStack item : getGubKits().get(plugin.gubGameKills.get(victim.getName()))) {
                        victim.getInventory().addItem(item);
                    }
                    plugin.gubGameKills.put(killer.getName(), plugin.gubGameKills.get(killer.getName()) + 1);
                    victim.setHealth(20);
                    if (plugin.gubGameKills.get(killer.getName()).equals(14)) {
                        plugin.earnPoints(killer.getName(), 80 - plugin.gubKitKills.get(plugin.gubGameKills.get(killer.getName())) * 2, true);
                        killer.setGameMode(GameMode.SPECTATOR);
                        killer.sendTitle("§eFINISH", "§c\uD83D\uDC80 " + plugin.getPlayerDisplayName(victim.getName()), 0, 20, 0);
                        for(Player p : Bukkit.getOnlinePlayers()){
                            plugin.messagePlayer(p, "§e♛ §7| " + plugin.getPlayerDisplayName(killer.getName()) + " §fhas finished the final kit!");
                        }
                    } else {
                        if(plugin.gubGameKills.get(killer.getName()) > 7) {
                            plugin.earnPoints(killer.getName(), 50 - plugin.gubKitKills.get(plugin.gubGameKills.get(killer.getName())), true);
                        } else {
                            plugin.earnPoints(killer.getName(), 40 - plugin.gubKitKills.get(plugin.gubGameKills.get(killer.getName())), true);
                        }
                        killer.sendTitle("", "§c\uD83D\uDC80 " + plugin.getPlayerDisplayName(victim.getName()), 0, 20, 0);
                        killer.getInventory().clear();
                        plugin.messagePlayer(killer, "§c\uD83D\uDC80 §7| NEXT KIT! (§e§l" + plugin.gubGameKills.get(killer.getName()) + "/14§7)");
                        for (ItemStack item : getGubKits().get(plugin.gubGameKills.get(killer.getName()))) {
                            killer.getInventory().addItem(item);
                        }
                    }
                    plugin.gubKitKills.put(plugin.gubGameKills.get(killer.getName()), plugin.gubKitKills.get(plugin.gubGameKills.get(killer.getName())) + 1);
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
                plugin.lastHitPlayer.put(victim.getName(), killer.getName());
                if (victim.getHealth() - e.getFinalDamage() <= 0) {
                    Bukkit.getWorld("build").spawnParticle(Particle.RAID_OMEN, victim.getLocation().clone().add(0,1,0), 20, 0.2, 0.5, 0.2, 0);
                    killer.playSound(killer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 2);
                    plugin.messagePlayer(victim, "§c\uD83D\uDC80 §7| §cYou died to " + plugin.getPlayerDisplayName(killer.getName()));
                    victim.sendTitle("§c§lYOU DIED.", "", 0, 40, 10);
                    plugin.playerKillCount.put(killer.getName(), plugin.playerKillCount.get(killer.getName()) + 1);
                    if(PlayerInfoConfig.get().getConfigurationSection("players").getKeys(false).contains(killer.getName())){
                        PlayerInfoConfig.get().set("players." + killer.getName() + ".kills", PlayerInfoConfig.get().getInt("players." + killer.getName() + ".kills") + 1);
                        PlayerInfoConfig.save();
                    }

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
                    plugin.earnPoints(killer.getName(), 40, true);
                    killer.sendTitle("", "§e\uD83D\uDCB040" + " §7| §c\uD83D\uDC80 " + plugin.getPlayerDisplayName(victim.getName()), 0, 20, 0);
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (!p.getGameMode().equals(GameMode.SPECTATOR) && !p.getName().equals(killer.getName()) && PlayerConfig.get().getConfigurationSection("players").getKeys(false).contains(p.getName())) {
                            int survivalPoints;
                            if(plugin.deadPlayers.size() > 20) { survivalPoints = 5; }
                            else if (plugin.deadPlayers.size() > 10) { survivalPoints = 7; }
                            else { survivalPoints = 10; }

                            plugin.messagePlayer(p, "§e\uD83D\uDCB0" + survivalPoints + " §7| " + plugin.formatKillMessage(killer.getName(), victim.getName()));
                            plugin.earnPoints(p.getName(), survivalPoints, true);
                        } else {
                            plugin.messagePlayer(p, "§c\uD83D\uDC80 §7| " + plugin.formatKillMessage(killer.getName(), victim.getName()));
                        }
                    }
                    plugin.killRecord.add(plugin.getPlayerDisplayName(killer.getName()) + " §c⚔ " + plugin.getPlayerDisplayName(victim.getName()));

                    switch (plugin.deadPlayers.size()) {
                        case 16:
                            plugin.newBorderRadius = 26;
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.sendTitle("", "§e⚠ Border Shrinking ⚠", 0, 40, 10);
                            }
                            break;
                        case 12:
                            plugin.newBorderRadius = 56;
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.sendTitle("", "§e⚠ Border Shrinking ⚠", 0, 40, 10);
                            }
                            break;
                        case 8:
                            plugin.newBorderRadius = 116;
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.sendTitle("", "§e⚠ Border Shrinking ⚠", 0, 40, 10);
                            }
                            break;
                        case 4:
                            plugin.newBorderRadius = 176;
                            for (Player p : Bukkit.getOnlinePlayers()) {
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
                        for (Player player2 : Bukkit.getOnlinePlayers()) {
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
            } else if (plugin.currentMode.equals("Push Point")) {

                if (e.getDamager() instanceof Arrow arrow) {
                    if (arrow.getPersistentDataContainer().has(
                            new NamespacedKey(plugin, "crossbow_arrow"),
                            PersistentDataType.BOOLEAN)) {
                        e.setDamage(e.getDamage() * 0.75);
                    }
                }

                if (victim.getHealth() - e.getFinalDamage() <= 0) {
                    Inventory ppTeleportGUI = Bukkit.createInventory(null, 54, "§eSelect Teleport Location");
                    ppTeleportGUI.setItem(10, getRespawnLocs().getFirst());
                    ppTeleportGUI.setItem(16, getRespawnLocs().get(1));
                    ppTeleportGUI.setItem(22, getRespawnLocs().get(2));
                    ppTeleportGUI.setItem(40, getRespawnLocs().get(3));
                    Bukkit.getWorld("build").spawnParticle(Particle.RAID_OMEN, victim.getLocation().clone().add(0,1,0), 20, 0.2, 0.5, 0.2, 0);
                    killer.playSound(killer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 2);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> killer.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(plugin.getPlayerDisplayName(victim.getName()) + " §7| §c♥§c§l0.0")), 1L);

                    if(plugin.finaleActive){
                        for(Player player : Bukkit.getOnlinePlayers()) {
                            plugin.messagePlayer(player, "§c\uD83D\uDC80 §7| " + plugin.formatKillMessage(killer.getName(), victim.getName()));
                        }
                    } else {

                        String victimTeam = PlayerConfig.get().getString("players." + victim.getName() + ".team");
                        List<String> victimTeamPlayers = TeamsConfig.get().getStringList("teams." + victimTeam + ".players");

                        String killerTeam = PlayerConfig.get().getString("players." + killer.getName() + ".team");
                        List<String> killerTeamPlayers = TeamsConfig.get().getStringList("teams." + killerTeam + ".players");

                        List<Player> allPlayers = Stream.concat(victimTeamPlayers.stream(), killerTeamPlayers.stream())
                                .map(Bukkit::getPlayer)
                                .filter(Objects::nonNull)
                                .toList();

                        for(Player player : allPlayers) {
                            plugin.messagePlayer(player, "§c\uD83D\uDC80 §7| " + plugin.formatKillMessage(killer.getName(), victim.getName()));
                        }
                    }

                    plugin.messagePlayer(victim, "§c\uD83D\uDC80 §7| §cYou died to " + plugin.getPlayerDisplayName(killer.getName()));
                    victim.playSound(victim.getLocation(), Sound.ENTITY_ARMADILLO_UNROLL_START, 1F, 1F);
                    plugin.playerKillCount.put(killer.getName(), plugin.playerKillCount.get(killer.getName()) + 1);
                    if(PlayerInfoConfig.get().getConfigurationSection("players").getKeys(false).contains(killer.getName())) {
                        PlayerInfoConfig.get().set("players." + killer.getName() + ".kills", PlayerInfoConfig.get().getInt("players." + killer.getName() + ".kills") + 1);
                        PlayerInfoConfig.save();
                    }
                    e.setCancelled(true);
                    victim.setGameMode(GameMode.SPECTATOR);
                    victim.getInventory().clear();
                    givePPKits(victim);
                    victim.setHealth(20);
                    victim.setFoodLevel(20);
                    victim.setSaturation(20f);
                    for (PotionEffect effect : victim.getActivePotionEffects()) {
                        victim.removePotionEffect(effect.getType());
                    }
                    killer.sendTitle("", "§c\uD83D\uDC80 " + plugin.getPlayerDisplayName(victim.getName()), 0, 20, 0);
                    BukkitTask task = new BukkitRunnable() {
                        int timeLeft = plugin.finalPush ? 4 : 6;;
                        final int startTimeRespawn = plugin.finalPush ? 3 : 5;

                        @Override
                        public void run() {
                            if (plugin.runningTimers.containsKey(victim.getName() + "respawn")) {
                                if (!plugin.pausedTimers.contains(victim.getName() + "respawn")) {
                                    plugin.runningTimers.get(victim.getName() + "respawn").setValue(timeLeft);
                                    if(timeLeft == startTimeRespawn){
                                        victim.openInventory(ppTeleportGUI);
                                    }
                                    timeLeft--;
                                    if (timeLeft == 0) {
                                        if(plugin.playerSelectedTeleport.containsKey(victim) && plugin.runningTimers.containsKey("pushpoint") && plugin.runningTimers.get("pushpoint").getValue() > 5) {
                                            plugin.messageConsole("Timer finished.");
                                            victim.setGameMode(GameMode.ADVENTURE);
                                            victim.sendTitle("", "", 0, 30, 0);
                                            victim.teleport(plugin.playerSelectedTeleport.get(victim));
                                            plugin.playerSelectedTeleport.remove(victim);
                                        }
                                        plugin.runningTimers.remove(victim.getName() + "respawn");
                                        cancel();
                                    } else {
                                        if(plugin.playerSelectedTeleport.containsKey(victim)) {
                                            victim.sendTitle("§c§lYou Died.", "§6Respawning in §c" + timeLeft + "..", 0, 30, 0);
                                            plugin.messageConsole(timeLeft + " seconds left..");
                                        } else {
                                            victim.sendTitle("§c§lYou Died.", "§6§uSelect a respawn location.", 0, 30, 0);
                                        }
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
        item3[0].setAmount(2);
        items.add(item3);

        return items;
    }

    public List<ItemStack> getRespawnLocs() {
        List<ItemStack> items = new ArrayList<>();

        ItemStack item1 = new ItemStack(Material.NETHER_STAR);
        ItemMeta item1meta = item1.getItemMeta();
        item1meta.setDisplayName("§b§lLeft Lane");
        item1.setItemMeta(item1meta);
        items.add(item1);

        ItemStack item2 = new ItemStack(Material.NETHER_STAR);
        ItemMeta item2meta = item2.getItemMeta();
        item2meta.setDisplayName("§b§lRight Lane");
        item2.setItemMeta(item2meta);
        items.add(item2);

        ItemStack item3 = new ItemStack(Material.NETHER_STAR);
        ItemMeta item3meta = item3.getItemMeta();
        item3meta.setDisplayName("§e§lMiddle Lane");
        item3.setItemMeta(item3meta);
        items.add(item3);

        ItemStack item4 = new ItemStack(Material.NETHER_STAR);
        ItemMeta item4meta = item4.getItemMeta();
        item4meta.setDisplayName("§a§lBase");
        item4.setItemMeta(item4meta);
        items.add(item4);

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

    public void givePowerUp(Player p, ItemStack type){

                p.getInventory().addItem(type);
                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1F, 1F);
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("§7[§a+§7] §r" + type.getItemMeta().getDisplayName()));
    }

    public static int countItem(Player player, ItemStack target) {
        int total = 0;

        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.isSimilar(target)) {
                total += item.getAmount();
            }
        }

        return total;
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
                regenpotionmeta.setBasePotionType(PotionType.REGENERATION);
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
    public void onEntityDamage(EntityDamageEvent e) {
        if(plugin.currentMode.equals("Dimension Dash")
            || plugin.currentMode.equals("Bridge Builders")
            || plugin.currentMode.equals("Slime Golf")
            || plugin.currentMode.equals("Craftalot")
            || plugin.currentMode.equals("Lobby")) {
            if (e.getEntity() instanceof Player) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        if (event.getBow().getType() == Material.CROSSBOW) {
            if (event.getProjectile() instanceof Arrow arrow) {
                arrow.getPersistentDataContainer().set(
                        new NamespacedKey(plugin, "crossbow_arrow"),
                        PersistentDataType.BOOLEAN, true
                );
            }
        }
    }
}
