package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.CraftalotConfig;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CraftalotEvent implements Listener {


    private static Showdown2 plugin;

    public CraftalotEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void edguardInteractEvent(PlayerInteractEntityEvent e){

        Player p = e.getPlayer();
        EntityType entity = e.getRightClicked().getType();
        Inventory inventory = p.getInventory();
        if(plugin.currentMode.equals("Craftalot")){
            if(entity.equals(EntityType.VILLAGER)){
                if(plugin.itemToCraft.containsKey(p.getName())){
                    List<String> items = new ArrayList<>();
                    for(ItemStack item : inventory){
                        if(item != null && item.getItemMeta() != null) {
                            items.add(item.getType().name());
                        }
                    }
                    if(items.contains(plugin.itemToCraft.get(p.getName()))){
                        String currentItem = plugin.itemToCraft.get(p.getName()).replaceAll("_", " ");
                        plugin.earnPoints(p.getName(), 35, true);
                        Random r = new Random();
                        List<String> craftList = CraftalotConfig.get().getStringList("craftlist");
                        plugin.itemToCraft.put(p.getName(), craftList.get(r.nextInt(craftList.size())));
                        String newItem = plugin.itemToCraft.get(p.getName()).replaceAll("_", " ");
                        for(Player players : plugin.getPlayers()){
                            plugin.messagePlayer(players, "§8[§c§l!§8] " + plugin.getPlayerDisplayName(players.getName()) + " §7has crafted an item! (§e" + currentItem + "§7)");
                        }
                        plugin.messagePlayer(p, """
                                        §8
                                        §8
                                        §8[§a§l✔§8] §aITEM CRAFTED.
                                        §7Next item to craft: §e§l""" + newItem + """
                                        §8
                                        """);
                        p.getInventory().clear();
                        for(int i = 0; i <= 3; i++){
                            p.getInventory().addItem(plugin.craftalotKit()[i]);
                        }
                        p.getInventory().setItemInOffHand(plugin.craftalotKit()[4]);
                    } else {
                        String currentItem = plugin.itemToCraft.get(p.getName()).replaceAll("_", " ");
                        plugin.messagePlayer(p, """
                                        §8
                                        §8
                                        §8[§a§l✔§8] §cItem Missing..
                                        §7Slacking off? I told you to get me: §e§l""" + currentItem + """
                                        §8
                                        """);
                    }
                } else {
                    plugin.itemToCraft.put(p.getName(), "STONE_SWORD");
                    String currentItem = plugin.itemToCraft.get(p.getName()).replaceAll("_", " ");
                    plugin.messagePlayer(p, """
                                        §8
                                        §8
                                        §8[§e§l!§8] §eHello Traveller! The king has new orders..
                                        §7The first item you are required to bring me is: §e§l""" + currentItem + """
                                        §8
                                        """);
                }
            }
        }
    }

}
