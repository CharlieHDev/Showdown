package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class LastHitEvent implements Listener {

    private static Showdown2 plugin;

    public LastHitEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent e) {
        if(plugin.currentMode.equals("Zoomo Go")) {
            if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
                Player killer = (Player) e.getDamager();
                Player victim = (Player) e.getEntity();

                plugin.lastHitPlayer.put(killer.getName(), victim.getName());
            }
        }
        if (plugin.currentMode.equals("Gub Game")) {
            if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
                Player killer = (Player) e.getDamager();
                Player victim = (Player) e.getEntity();

                if (victim.getHealth() - e.getFinalDamage() <= 0) {
                    for(Player p : plugin.getPlayers()){
                        plugin.messagePlayer(p, "§c\uD83D\uDC80 §7| " + plugin.formatKillMessage(killer.getName(), victim.getName()));
                    }
                    plugin.messagePlayer(victim, "§c\uD83D\uDC80 §7| §cYou died to " + plugin.getPlayerDisplayName(killer.getName()));
                    e.setCancelled(true);
                    victim.setGameMode(GameMode.SPECTATOR);
                    plugin.gubGameKills.put(killer.getName(), plugin.gubGameKills.get(killer.getName()) + 1);

                    if (plugin.gubGameKills.get(killer.getName()).equals(15)) {
                        killer.setGameMode(GameMode.SPECTATOR);
                    } else {
                        killer.getInventory().clear();
                        plugin.messagePlayer(victim, "§c\uD83D\uDC80 §7| NEXT KIT! (§e§l" + plugin.gubGameKills.get(killer.getName()) + "/15§7)");
                        for (ItemStack item : getGubKits().get(plugin.gubGameKills.get(killer.getName()))) {
                            killer.getInventory().addItem(item);
                        }
                    }
                }
            }
        }
    }

    public ArrayList<ItemStack[]> getGubKits() {
        ArrayList<ItemStack[]> kits = new ArrayList<>();

        ItemStack[] kit15 = new ItemStack[1];
        kit15[0] = new ItemStack(Material.AIR);
        kits.add(kit15);

        ItemStack[] kit14 = new ItemStack[1];
        kit14[0] = new ItemStack(Material.WOODEN_PICKAXE);
        kits.add(kit14);

        ItemStack[] kit13 = new ItemStack[1];
        kit13[0] = new ItemStack(Material.NETHERITE_HOE);
        kits.add(kit13);

        ItemStack[] kit12 = new ItemStack[1];
        kit12[0] = new ItemStack(Material.STONE_SHOVEL);
        kits.add(kit12);

        ItemStack[] kit11 = new ItemStack[1];
        kit11[0] = new ItemStack(Material.IRON_PICKAXE);
        kits.add(kit11);

        ItemStack[] kit10 = new ItemStack[1];
        kit10[0] = new ItemStack(Material.DIAMOND_SHOVEL);
        kits.add(kit10);

        ItemStack[] kit9 = new ItemStack[1];
        kit9[0] = new ItemStack(Material.STONE_SWORD);
        kits.add(kit9);

        ItemStack[] kit8 = new ItemStack[1];
        kit8[0] = new ItemStack(Material.IRON_SWORD);
        kits.add(kit8);

        ItemStack[] kit7 = new ItemStack[2];
        kit7[0] = new ItemStack(Material.BOW);
        kit7[1] = new ItemStack(Material.ARROW);
        kits.add(kit7);

        ItemStack[] kit6 = new ItemStack[1];
        kit6[0] = new ItemStack(Material.DIAMOND_AXE);
        kits.add(kit6);

        ItemStack[] kit5 = new ItemStack[2];
        kit5[0] = new ItemStack(Material.CROSSBOW);
        kit5[1] = new ItemStack(Material.ARROW);
        kits.add(kit5);

        ItemStack[] kit4 = new ItemStack[1];
        kit4[0] = new ItemStack(Material.TRIDENT);
        kits.add(kit4);

        ItemStack[] kit3 = new ItemStack[1];
        kit3[0] = new ItemStack(Material.NETHERITE_AXE);
        kits.add(kit3);

        ItemStack[] kit2 = new ItemStack[1];
        kit2[0] = new ItemStack(Material.DIAMOND_SWORD);
        kits.add(kit2);

        ItemStack[] kit1 = new ItemStack[1];
        kit1[0] = new ItemStack(Material.NETHERITE_SWORD);
        kits.add(kit1);

        ItemMeta meta = null;
        for(ItemStack[] kit : kits){
            for(ItemStack item : kit){
                if(item.getType() != Material.AIR) {
                    meta.setUnbreakable(true);
                    item.setItemMeta(meta);
                }
            }
        }

        return kits;
    }
}
