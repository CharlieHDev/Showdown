package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class BreakBlockEvent implements Listener {

    private static Showdown2 plugin;

    public BreakBlockEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();

        if(plugin.ghostManager.getGhostPlayers().contains(p.getName())) { e.setCancelled(true); return; }
        
        if(plugin.getPlayers().contains(p)) {
            if (plugin.currentMode.equals("Craftalot")) {
                e.setCancelled(true);
                for (Material block : getCraftalotBlocks()) {
                    if (block == e.getBlock().getType()) {
                        if(block == Material.STONE) {
                            p.getInventory().addItem(new ItemStack(Material.COBBLESTONE));
                        } else if (block == Material.DIAMOND_ORE){
                            p.getInventory().addItem(new ItemStack(Material.DIAMOND));
                        } else if (block == Material.GOLD_ORE){
                            p.getInventory().addItem(new ItemStack(Material.GOLD_INGOT));
                        } else if (block == Material.IRON_ORE){
                            p.getInventory().addItem(new ItemStack(Material.IRON_INGOT));
                        } else if (block == Material.REDSTONE_ORE){
                            p.getInventory().addItem(new ItemStack(Material.REDSTONE));
                        } else if (block == Material.NETHER_QUARTZ_ORE){
                            p.getInventory().addItem(new ItemStack(Material.QUARTZ));
                        } else {
                            p.getInventory().addItem(new ItemStack(block));
                        }
                        p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1F, 1F);
                        e.setCancelled(true);
                        break;
                    }
                }
            } else if (plugin.currentMode.equals("Colour Dash")) {
                e.setCancelled(true);
                for (Material concrete : getConcreteColours()) {
                    if (concrete == e.getBlock().getType()) {
                        e.setCancelled(false);
                        break;
                    }
                }
            } else if (plugin.currentMode.equals("Crumble Clash") && plugin.blockBreak) {
                if(plugin.currentSpleef.equals("§f§lClassic Spleef")){
                    ItemStack snowball = new ItemStack(Material.SNOWBALL);
                    p.getInventory().addItem(snowball);
                    p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1F, 1F);
                }
                if(!Objects.equals(plugin.currentSpleef, "§6§lCopper Spleef")){
                    Player breaker = e.getPlayer();
                    Block block = e.getBlock();

                    for (Player target : block.getWorld().getPlayers()) {
                        if (target.equals(breaker)) continue;

                        Location feet = target.getLocation();
                        if (feet.getBlock().getRelative(BlockFace.DOWN).equals(block)) {
                            plugin.crumbleKillTracker.put(
                                    target.getName(),
                                    new CrumbleKillData(breaker.getName(), System.currentTimeMillis())
                            );
                        }
                    }
                }
                e.setCancelled(false);
            } else {
                e.setCancelled(true);
            }
        }
    }

    private Material[] getConcreteColours() {
        return new Material[]{
                Material.RED_CONCRETE, Material.ORANGE_CONCRETE, Material.YELLOW_CONCRETE, Material.LIME_CONCRETE, Material.LIGHT_BLUE_CONCRETE,
                Material.BLUE_CONCRETE, Material.MAGENTA_CONCRETE, Material.WHITE_CONCRETE, Material.BLUE_ICE

        };
    }

    private Material[] getCraftalotBlocks() {
        return new Material[]{
                Material.OAK_PLANKS, Material.BIRCH_PLANKS, Material.GOLD_ORE, Material.DIAMOND_ORE, Material.IRON_ORE, Material.SPRUCE_PLANKS,
                Material.ACACIA_PLANKS, Material.SAND, Material.GRAVEL, Material.ANDESITE, Material.REDSTONE_ORE, Material.STONE, Material.NETHER_QUARTZ_ORE
        };
    }
}
