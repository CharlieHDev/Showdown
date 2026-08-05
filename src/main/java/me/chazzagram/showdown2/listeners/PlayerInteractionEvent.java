package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.PlayerConfig;
import me.chazzagram.showdown2.files.SpectatorConfig;
import me.chazzagram.showdown2.files.TeamsConfig;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class PlayerInteractionEvent implements Listener {

    private static Showdown2 plugin;

    public PlayerInteractionEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {

        if(plugin.ghostManager.getGhostPlayers().contains(event.getPlayer().getName())) { event.setCancelled(true); return; }

        event.setCancelled(false);
        if(plugin.currentMode.equals("Crumble Clash")){
            if (event.getState() != PlayerFishEvent.State.CAUGHT_ENTITY) return;

            if (!(event.getCaught() instanceof Player roddee)) return;

            Player rodder = event.getPlayer();

            plugin.crumbleKillTracker.put(
                    roddee.getName(),
                    new CrumbleKillData(rodder.getName(), System.currentTimeMillis())
            );
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) throws ReflectiveOperationException {

        if(plugin.ghostManager.getGhostPlayers().contains(e.getPlayer().getName())) {
            Player p = e.getPlayer();
            e.setCancelled(true);
            if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                if(e.getItem() != null && e.getItem().getType() == Material.COMPASS) {
                    plugin.ghostManager.populatePlayerFinder();
                    p.openInventory(plugin.ghostManager.getPlayerFinder());
                }
            }
        }

        if(plugin.currentMode.equals("Bridge Builders")) {
            if (e.getAction() == Action.LEFT_CLICK_BLOCK) {

                Block clickedBlock = e.getClickedBlock();

                if (clickedBlock != null || clickedBlock.getType() != Material.AIR) {
                    if(plugin.checkWithinBuildArea(clickedBlock, e.getPlayer().getName())) {
                        clickedBlock.setType(Material.AIR);
                        Bukkit.getWorld("build").spawnParticle(Particle.CLOUD, clickedBlock.getLocation().clone().add(0.5, 0.5, 0.5), 3, 0.0, 0.0, 0.0, 0);
                        unsetGlowing(e.getPlayer(), clickedBlock);
                    }
                }
            }
            if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                e.setCancelled(false);
            }
        } else if (plugin.currentMode.equals("Voting")) {
            if (e.getItem() != null &&
                    e.getItem().getType() == Material.BOW) {
                e.setCancelled(false);
            }
            if (e.getItem() != null) {
                if (e.getItem().getType() == Material.POTION) {

                    if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) {
                        return;
                    }

                    e.setCancelled(true);

                    PotionMeta meta = (PotionMeta) e.getItem().getItemMeta();

                    if(meta.getBasePotionType() == PotionType.SWIFTNESS){
                        PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 100, 1, false, false);
                        e.getPlayer().addPotionEffect(speed);
                    }
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1F, 1F);
                    e.getItem().setAmount(e.getItem().getAmount() - 1);
                }
            }
        }else if(plugin.currentMode.equals("Gub Game")) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (e.getItem() != null && e.getItem().getType() == Material.TRIDENT) {
                    e.setCancelled(true);
                } else if (e.getItem() != null && (e.getItem().getType() == Material.BOW || e.getItem().getType() == Material.CROSSBOW)) {
                    e.setCancelled(false);
                } else {
                    e.setCancelled(true);
                }
            }
        } else if (plugin.currentMode.equals("Survival Games")) {
            e.setCancelled(false);
        } else if (plugin.currentMode.equals("Craftalot")) {
            e.setCancelled(false);
            if (e.getItem() != null && e.getItem().getType().name().endsWith("_BOAT")) {
                e.setCancelled(true);
            }
        } else if (plugin.currentMode.equals("Dimension Dash")) {
            e.setCancelled(false);
        } else if (plugin.runningTimers.containsKey("readytimer") && plugin.readyType.equals("snowballs")) {
            Action action = e.getAction();
            if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
                return;
            }
            if (e.getItem() != null && e.getItem().getType() == Material.SNOWBALL) {
                e.setCancelled(false);
            }
        } else if (plugin.currentMode.equals("Push Point") && plugin.runningTimers.containsKey("pushpoint")) {
            e.setCancelled(false);
            Action action = e.getAction();

            if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
                return;
            }

            ItemStack item = e.getItem();
            if (item != null) {
                if (item.getType() == Material.POTION) {

                    e.setCancelled(true);

                    PotionMeta meta = (PotionMeta) item.getItemMeta();

                    if(meta.getBasePotionType() == PotionType.SWIFTNESS){
                        PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 200, 1, false, false);
                        e.getPlayer().addPotionEffect(speed);
                    }
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1F, 1F);
                    item.setAmount(item.getAmount() - 1);
                }
            }
        } else if (plugin.currentMode.equals("Crumble Clash")) {
            e.setCancelled(false);

            if(plugin.ghostManager.getGhostPlayers().contains(e.getPlayer().getName())) { e.setCancelled(true); return; }

            if(!plugin.blockBreak && !plugin.copperDecay) e.setCancelled(true);

            if (e.getItem() != null && e.getItem().getType() == Material.FISHING_ROD) {
                e.setCancelled(false);
            }
            Action action = e.getAction();

            if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
                return;
            }

            Player sender = e.getPlayer();

            ItemStack item = e.getItem();
            if (item != null) {
                if (item.getType() == Material.FIRE_CHARGE) {

                    e.setCancelled(true);

                    Vector direction = sender.getLocation().getDirection();

                    Fireball fireball = sender.getWorld().spawn(
                            sender.getEyeLocation().add(direction.multiply(1.5)),
                            Fireball.class
                    );

                    plugin.fireballSenders.put(fireball, e.getPlayer());

                    fireball.setYield(4.0f);
                    fireball.setVelocity(direction.multiply(2.0));
                    fireball.setShooter(sender);
                    fireball.setYield(2.0f);
                    fireball.setIsIncendiary(true);

                    item.setAmount(item.getAmount() - 1);
                }
                if (item.getType() == Material.POTION) {

                    e.setCancelled(true);

                    PotionMeta meta = (PotionMeta) item.getItemMeta();

                    if(meta.getBasePotionType() == PotionType.SWIFTNESS){
                        PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 200, 1, false, false);
                        e.getPlayer().addPotionEffect(speed);
                    }
                    if(meta.getBasePotionType() == PotionType.LEAPING){
                        PotionEffect leaping = new PotionEffect(PotionEffectType.JUMP_BOOST, 200, 1, false, false);
                        e.getPlayer().addPotionEffect(leaping);
                    }
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1F, 1F);
                    item.setAmount(item.getAmount() - 1);
                }
            }
        } else if (plugin.currentMode.equals("Slime Golf")) {
            if (e.getItem() != null && e.getItem().getType() == Material.FISHING_ROD) {
                e.setCancelled(false);
            }
        } else {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() != Material.BELL) {
                if(plugin.getPlayers().contains(e.getPlayer())) {
                    e.setCancelled(true);
                }
            }
            if(e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.BARREL) {
                e.setCancelled(false);
            }
        }
    }

    @EventHandler
    public void onItemDrop(ItemSpawnEvent event) {
        Item item = event.getEntity();

        if(!item.getItemStack().getType().equals(Material.PAPER)) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!item.isValid()) {
                    this.cancel();
                    return;
                }

                if (item.isOnGround()) {
                    for(Player player2 : Bukkit.getOnlinePlayers()){
                        player2.playSound(item.getLocation(), Sound.ENTITY_SLIME_JUMP, 1F, 1F);
                    }
                    Location loc = item.getLocation();
                    World world = loc.getWorld();

                    int radius = 2;


                    for (int x = -radius; x <= radius; x++) {
                        for (int y = -radius; y <= radius; y++) {
                            for (int z = -radius; z <= radius; z++) {

                                Location checkLoc = loc.clone().add(x, y, z);
                                Block block = world.getBlockAt(checkLoc);

                                if (checkLoc.distanceSquared(loc) > radius * radius) continue;

                                if (getWoolColors().contains(block.getType())) {
                                    if(plugin.voteBlasterBlasts.containsKey(item)) {
                                        Player p = plugin.voteBlasterBlasts.get(item);
                                        if (plugin.playerVote.containsKey(p)) {
                                            block.setType(plugin.playerVote.get(p));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Player p = plugin.voteBlasterBlasts.get(item);
                    Particle.DustOptions dustOptions = new Particle.DustOptions(plugin.woolColors.get(plugin.playerVote.get(p)), 4);

                    Bukkit.getWorld("build").spawnParticle(Particle.DUST, loc, 10, 1.5, 0.0, 1.5, 1, dustOptions, false);
                    item.remove();
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 1L, 1L);
    }

    private List<Material> getWoolColors() {
        return List.of(new Material[]{
                Material.RED_WOOL, Material.WHITE_WOOL, Material.ORANGE_WOOL, Material.LIGHT_BLUE_WOOL, Material.LIME_WOOL,
                Material.YELLOW_WOOL, Material.PURPLE_WOOL, Material.BLACK_WOOL
        });
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {

        if(plugin.currentMode.equals("Slime Golf")) {

            event.setCancelled(true);

            if (!(event.getEntity() instanceof Player player)) return;

            if (plugin.ghostManager.getGhostPlayers().contains(player.getName())) return;

            float force = event.getForce();

            String team = PlayerConfig.get().getString("players." + player.getName() + ".team");

            SulfurCube cube = plugin.golfTeamCubes.get(team);

            Vector direction = player.getLocation().getDirection();

            float amplify = force * 1f;

            Vector launch = direction.clone().add(new Vector(0, 0.25, 0)).normalize();

            cube.setVelocity(launch.multiply(amplify));

            String leavingPlayerTeam = PlayerConfig.get().getString("players." + player.getName() + ".team");

            plugin.golfQueues.get(leavingPlayerTeam).updateQueuePosition();
        }



        if(plugin.currentMode.equals("Voting")) {

            event.setCancelled(true);

            if (plugin.votingMode.equals("guns")) {

                if (!plugin.votingEnabled) return;

                if (!(event.getEntity() instanceof Player player)) return;

                if (plugin.voteFrozenManager.getFrozenPlayers().contains(player.getName())) return;

                if (plugin.ghostManager.getGhostPlayers().contains(player.getName())) return;

                if (plugin.playerVote.get(player) == null || !getWoolColors().contains(plugin.playerVote.get(player)))
                    return;

                if (!plugin.playerVote.containsKey(player)) return;

                float force = event.getForce();

                Vector direction = player.getLocation().getDirection().normalize();

                Location spawnLoc = player.getEyeLocation().add(direction.multiply(0.5));

                String colour = plugin.playerVote.get(player).name().replace("_WOOL", "").toLowerCase();

                ItemStack stack = new ItemStack(Material.PAPER);
                ItemMeta meta = stack.getItemMeta();
                meta.setItemModel(new NamespacedKey("amongus", "ball" + colour));
                stack.setItemMeta(meta);

                Item item = Bukkit.getWorld("build").dropItemNaturally(spawnLoc, stack);

                player.playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1F, 1F);

                item.setPickupDelay(Integer.MAX_VALUE);
                item.setGravity(true);

                double baseSpeed = 7.0;
                item.setVelocity(direction.multiply(force * baseSpeed));

                plugin.voteBlasterBlasts.put(item, player);
            }
            if (plugin.votingMode.equals("bounce")) {
                if (!(event.getEntity() instanceof Player player)) return;

                if (plugin.voteFrozenManager.getFrozenPlayers().contains(player.getName())) return;

                if (plugin.ghostManager.getGhostPlayers().contains(player.getName())) return;

                float force = event.getForce();

                Entity vehicle = player.getVehicle();
                if (vehicle == null) {
                    return;
                }

                Vector direction = player.getLocation().getDirection();

                float amplify = force * 1.5f;

                Vector launch = direction.clone().add(new Vector(0, 0.25, 0)).normalize();

                vehicle.setVelocity(launch.multiply(amplify));
            }
        }
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {

        if(!plugin.currentMode.equals("Push Point")) return;

        ThrownPotion potion = event.getEntity();

        Player shooter = (Player) potion.getShooter();

        String team = PlayerConfig.get().getString("players." + shooter.getName() + ".team");

        if (event.getPotion().getItem().hasItemMeta()) {
            if (event.getPotion().getItem().getItemMeta() instanceof PotionMeta meta) {

                PotionType type = meta.getBasePotionType();

                switch (type) {
                    case PotionType.HEALING:
                        for (Entity entity : event.getAffectedEntities()) {
                            if (entity instanceof Player player) {
                                String affectedTeam = PlayerConfig.get().getString("players." + player.getName() + ".team");
                                if (!Objects.equals(team, affectedTeam)) {

                                    event.setIntensity(player, 0.0);
                                }
                            }
                        }
                        break;
                    case PotionType.REGENERATION:
                        for (Entity entity : event.getAffectedEntities()) {
                            if (entity instanceof Player player) {
                                String affectedTeam = PlayerConfig.get().getString("players." + player.getName() + ".team");
                                if (!Objects.equals(team, affectedTeam)) {
                                    event.setIntensity(player, 0.0);
                                } else {
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 2));
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void unsetGlowing(Player player, Block block) throws ReflectiveOperationException {

        String team = PlayerConfig.get().getString("players." + player.getName() + ".team");
        List<String> teamPlayers = TeamsConfig.get().getStringList("teams." + team + ".players");

        BlockDisplay display = plugin.blockToDisplay.remove(block);
        if (display != null) {
            for (String player2 : teamPlayers) {
                Player p2 = Bukkit.getPlayer(player2);
                if (p2 != null) {
                    plugin.glowingEntities.unsetGlowing(display, p2);
                }
            }

            display.remove();
        }
    }
}
