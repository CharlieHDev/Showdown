package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.PlayerConfig;
import me.chazzagram.showdown2.files.TeamsConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ColourDashEvent implements Listener {

    private static Showdown2 plugin;

    public ColourDashEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void blockPlaceEvent(BlockPlaceEvent e) throws ReflectiveOperationException {
        Player p = e.getPlayer();

        if(plugin.ghostManager.getGhostPlayers().contains(p.getName())) return;

        Material item = e.getBlock().getType();
        Inventory inventory = p.getInventory();
        if(plugin.currentMode.equals("Colour Dash") || plugin.currentMode.equals("Bridge Builders")){
            if(!(plugin.currentMode.equals("Colour Dash") && item.equals(Material.BLUE_ICE))) {
                ItemStack sixtyfour = new ItemStack(item);
                sixtyfour.setAmount(64);
                if (!inventory.contains(sixtyfour)) {
                    if (p.getInventory().getItemInOffHand().getType().equals(item)) {
                        p.getInventory().setItemInOffHand(sixtyfour);
                    } else {
                        if(p.getInventory().getItemInMainHand().getType().equals(item)){
                            p.getInventory().setItemInMainHand(sixtyfour);
                        }
                    }
                }
                if (plugin.currentMode.equals("Bridge Builders")) {
                    if(!plugin.checkWithinBuildArea(e.getBlock(), p.getName())) {
                        e.setCancelled(true);
                    } else {
                        Block placed = e.getBlockPlaced();
                        Block other = placed.getLocation().clone().add(20,0,0).getBlock();

                        if (plugin.blockToDisplay.containsKey(placed)) {
                            BlockDisplay existingDisplay = plugin.blockToDisplay.get(placed);
                            existingDisplay.remove();
                            plugin.blockToDisplay.remove(placed);
                        }

                        if (placed.getType() != other.getType()) {
                            setGlowing(p, placed);
                            return;
                        }

                        BlockData data1 = placed.getBlockData();
                        BlockData data2 = other.getBlockData();

                        if (data1 instanceof Directional d1 && data2 instanceof Directional d2) {
                            if (d1.getFacing() != d2.getFacing()) {
                                setGlowing(p, placed);
                                return;
                            }
                        }

                        if (data1 instanceof Orientable o1 && data2 instanceof Orientable o2) {
                            if (o1.getAxis() != o2.getAxis()) {
                                setGlowing(p, placed);
                                return;
                            }
                        }

                        if (data1 instanceof Slab s1 && data2 instanceof Slab s2) {
                            if (s1.getType() != s2.getType()) {
                                setGlowing(p, placed);
                                return;
                            }
                        }

                        if (data1 instanceof Stairs st1 && data2 instanceof Stairs st2) {
                            if (st1.getHalf() != st2.getHalf()) {
                                setGlowing(p, placed);
                            }
                        }
                    }
                }
            }
        } else {
            if(plugin.getPlayers().contains(p)) {
                e.setCancelled(true);
            }
        }
    }

    public void setGlowing(Player player, Block block) throws ReflectiveOperationException {

        String team = PlayerConfig.get().getString("players." + player.getName() + ".team");
        List<String> teamPlayers = TeamsConfig.get().getStringList("teams." + team + ".players");

        Location loc = block.getLocation().add(0.01f, 0.01f, 0.01f);

        BlockDisplay display = block.getWorld().spawn(loc, BlockDisplay.class);
        display.setBlock(block.getBlockData());
        display.setPersistent(false);
        display.setGravity(false);

        Transformation transform = display.getTransformation();
        transform.getScale().set(0.98f, 0.98f, 0.98f);
        display.setTransformation(transform);

        plugin.blockToDisplay.put(block, display);

        for (String player2 : teamPlayers) {
            Player p2 = Bukkit.getPlayer(player2);
            if (p2 != null) {
                plugin.glowingEntities.setGlowing(display, p2, ChatColor.RED);
            }
        }
    }
}
