package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;

import java.util.*;

public class VoteFrozenManager implements Listener {

    Showdown2 plugin;

    public VoteFrozenManager(Showdown2 plugin) {
        this.plugin = plugin;
    }

    boolean freezeCheck = false;

    List<String> frozenPlayers = new ArrayList<>();

    HashMap<String, ItemDisplay> frozenItemDisplays = new HashMap<>();

    BukkitTask runnable;

    HashMap<Location, Material> frozenBlockTypes = new HashMap<>();

    HashMap<String, List<Block>> playersFrozenBlocks = new HashMap<>();

    PotionEffect slowness = new PotionEffect(PotionEffectType.SLOWNESS, 200, 1, true, false);

    public void changeBlocksInRadius(String player, Location center, int radius, boolean ice) {
        World world = center.getWorld();

        int centerX = center.getBlockX();
        int centerY = center.getBlockY();
        int centerZ = center.getBlockZ();

        Material blockMaterial;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {

                    if (x*x + y*y + z*z <= radius*radius) {
                        Location loc = new Location(world, centerX + x, centerY + y, centerZ + z);
                        blockMaterial = world.getBlockAt(loc).getType();
                        if(ice) {
                            if (getWoolColors().contains(blockMaterial)) {
                                loc.getBlock().setType(Material.BLUE_ICE);

                                if (playersFrozenBlocks.containsKey(player)) {
                                    playersFrozenBlocks.get(player).add(loc.getBlock());
                                } else {
                                    List<Block> list = new ArrayList<>();
                                    list.add(loc.getBlock());
                                    playersFrozenBlocks.put(player, list);
                                }

                                frozenBlockTypes.put(loc, blockMaterial);
                            }
                        } else {
                            if(blockMaterial == Material.BLUE_ICE){
                                loc.getBlock().setType(frozenBlockTypes.get(loc));
                                if(playersFrozenBlocks.containsKey(player)) {

                                    playersFrozenBlocks.get(player).remove(loc.getBlock());
                                }
                                frozenBlockTypes.remove(loc);
                            }
                        }
                    }

                }
            }
        }
    }

    public void startPlayerCheck(){
        freezeCheck = true;
        for(Player player : Bukkit.getOnlinePlayers()){
            playersFrozenBlocks.put(player.getName(), new ArrayList<>());
        }
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if(freezeCheck) {
                    for(Player p : plugin.getPlayers()){
                        Block blockBelow = p.getLocation().getBlock().getRelative(BlockFace.DOWN);
                        if (blockBelow.getType() == Material.BLUE_ICE && !frozenPlayers.contains(p.getName())) {
                            if(playersFrozenBlocks.containsKey(p.getName())){
                                if(playersFrozenBlocks.get(p.getName()).contains(blockBelow)){
                                    continue;
                                }
                            }
                            frozenPlayers.add(p.getName());
                            spawnFrozenBlock(p.getLocation(), p.getName());
                            Location target = blockBelow.getLocation().add(0, 1, 0);
                            target.setX(p.getLocation().getX());
                            target.setZ(p.getLocation().getZ());
                            target.setYaw(p.getLocation().getYaw());
                            target.setPitch(p.getLocation().getPitch());
                            p.teleport(target);
                            p.addPotionEffect(slowness);
                            plugin.messagePlayer(p, "§7[§b!§7] §b§lYou are frozen!");
                            p.sendTitle("§b§lYou are frozen!", "", 0, 200, 0);
                            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_HURT_FREEZE, 1, 1);
                        }
                        if(getWoolColors().contains(blockBelow.getType()) && frozenPlayers.contains(p.getName())) {
                            frozenPlayers.remove(p.getName());
                            p.playSound(p.getLocation(), Sound.ENTITY_CREAKING_FREEZE, 1, 1);
                            p.removePotionEffect(slowness.getType());
                            Bukkit.getWorld("build").spawnParticle(Particle.SNOWFLAKE, p.getLocation().clone().add(0, 1, 0), 20,0.2, 0.5, 0.2,0);
                            p.sendTitle("", "", 0, 20, 0);
                            despawnFrozenBlock(p.getName());
                        }
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }

    public void stopPlayerCheck(){
        freezeCheck = false;
        for (Map.Entry<Location, Material> entry : new HashMap<>(frozenBlockTypes).entrySet()) {
            entry.getKey().getBlock().setType(entry.getValue());
        }
        frozenBlockTypes.clear();

        for(String player : frozenPlayers){
            despawnFrozenBlock(player);
            Player p = Bukkit.getPlayer(player);
            if(p != null) {
                p.playSound(p.getLocation(), Sound.ENTITY_CREAKING_FREEZE, 1, 1);
                Bukkit.getWorld("build").spawnParticle(Particle.SNOWFLAKE, p.getLocation().clone().add(0, 1, 0), 20, 0.2, 0.5, 0.2, 0);
                p.sendTitle("", "", 0, 20, 0);
                p.removePotionEffect(slowness.getType());
            }
        }
        frozenPlayers.clear();
    }

    public List<String> getFrozenPlayers(){
        return frozenPlayers;
    }

    public void setFrozenBlocks(String player, Block centreBlock) {

        UUID uuid = UUID.randomUUID();

        Location loc = centreBlock.getLocation();

        String name = "freeze" + uuid;

        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 32;
            @Override
            public void run() {
                if (!plugin.pausedTimers.contains(name)) {
                    timeLeft--;
                    plugin.runningTimers.get(name).setValue(timeLeft);

                    switch (timeLeft){
                        case 31,30,29,28:
                            changeBlocksInRadius(player, loc, 32-timeLeft, true);
                            break;
                        case 1:
                            changeBlocksInRadius(player, loc, 4, false);
                            break;
                        default:
                            break;
                    }
                    if (timeLeft == 0) {
                        plugin.runningTimers.remove(name);
                        cancel();
                    }
                } else {
                    cancel();
                }
            }

        }.runTaskTimer(plugin, 0L, 5L);
        plugin.runningTimers.put(name, new AbstractMap.SimpleEntry<>(task, 32));
    }

    public void despawnFrozenBlock(String player){
        ItemDisplay display = frozenItemDisplays.get(player);
        if(display != null){
            display.remove();
            frozenItemDisplays.remove(player);
        }
    }

    public void spawnFrozenBlock(Location playerLoc, String player){

        Player p = Bukkit.getPlayer(player);
        if(p != null) {
            Location loc = p.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation().clone().add(0,1,0);

            ItemStack stack = new ItemStack(Material.STICK);
            ItemMeta meta = stack.getItemMeta();
            meta.setItemModel(new NamespacedKey("amongus", "iceblock"));
            stack.setItemMeta(meta);

            loc.setX(playerLoc.getX());
            loc.setZ(playerLoc.getZ());
            loc.add(0,0.7,0);

            loc.setPitch(0);
            loc.setYaw(0);


            ItemDisplay display = (ItemDisplay) playerLoc.getWorld().spawnEntity(loc, EntityType.ITEM_DISPLAY);

            display.setItemStack(stack);

            Transformation transform = display.getTransformation();

            transform.getScale().set(1.5f, 1.5f, 1.5f);

            display.setTransformation(transform);

            frozenItemDisplays.put(player, display);
        }
    }

    private List<Material> getWoolColors() {
        return Arrays.asList(
                Material.RED_WOOL, Material.WHITE_WOOL, Material.ORANGE_WOOL, Material.LIGHT_BLUE_WOOL, Material.LIME_WOOL,
                Material.YELLOW_WOOL, Material.PURPLE_WOOL, Material.BLACK_WOOL
        );
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo() != null) {
            if(frozenPlayers.contains(event.getPlayer().getName())) {
                event.setCancelled(true);
            }
        }
    }
}
