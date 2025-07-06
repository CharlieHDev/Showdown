package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BreakBlockEvent implements Listener {

    private static Showdown2 plugin;

    public BreakBlockEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();

        if(plugin.getPlayers().contains(p)) {
            if (plugin.currentMode.equals("Craftalot")) {
                e.setCancelled(true);
                for (Material block : getCraftalotBlocks()) {
                    if (block == e.getBlock().getType()) {
                        if(block == Material.STONE) {
                            p.getInventory().addItem(new ItemStack(Material.COBBLESTONE));
                        } else {
                            p.getInventory().addItem(new ItemStack(block));
                        }
                        e.setCancelled(false);
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
                Material.OAK_LOG, Material.BIRCH_LOG, Material.GOLD_BLOCK, Material.DIAMOND_BLOCK, Material.IRON_BLOCK, Material.SPRUCE_LOG,
                Material.ACACIA_LOG, Material.SAND, Material.GRAVEL, Material.ANDESITE, Material.GRANITE, Material.STONE, Material.DIORITE
        };
    }
}
