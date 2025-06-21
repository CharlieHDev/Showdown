package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.CraftalotConfig;
import org.bukkit.Material;
import org.bukkit.entity.*;
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
                if(!plugin.itemToCraft.get(p.getName()).isEmpty()){
                    List<String> items = new ArrayList<>();
                    for(ItemStack item : inventory){
                        if(item != null && item.getItemMeta() != null) {
                            items.add(item.getType().name());
                        }
                    }
                    if(items.contains(plugin.itemToCraft.get(p.getName()))){
                        plugin.craftTop.put(p.getName(), plugin.craftTop.get(p.getName()) + 1);
                        String currentItem = plugin.itemToCraft.get(p.getName()).replaceAll("_", " ");
                        plugin.earnPoints(p.getName(), 35, true);
                        Random r = new Random();
                        List<String> craftList = CraftalotConfig.get().getStringList("craftlist");
                        plugin.itemToCraft.put(p.getName(), craftList.get(r.nextInt(craftList.size())));
                        String newItem = plugin.itemToCraft.get(p.getName()).replaceAll("_", " ");
                        for(Player players : plugin.getPlayers()){
                            plugin.messagePlayer(players, "§8[§c§l!§8] " + plugin.getPlayerDisplayName(e.getPlayer().getName()) + " §7has crafted an item! (§e" + currentItem + "§7)");
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
                    plugin.craftTop.put(p.getName(), 0);
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
        if(entity.equals(EntityType.WANDERING_TRADER)){
            WanderingTrader trader = (WanderingTrader) e.getRightClicked();
            if(trader.getCustomName().equals("Lyla")){
                if(!plugin.lylaInteractions.contains(p.getName())){
                    plugin.lylaInteractions.add(p.getName());
                    plugin.messagePlayer(p, """
                                        §8
                                        §8
                                        §e§lLyla §f>> §aHello §b""" + p.getName() + """
                                        §a! Have we met before? Not that you’d remember me per say, I am but a traveller! An observer in other words. I travelled here not long ago and set myself up here, providing stories and tales of old times to those who wish it. Come chat to me if you want to hear my tales.
                                        §8
                                        """);
                } else {
                    Random r = new Random();
                    plugin.messagePlayer(p, """
                                        §8
                                        §8
                                        §e§lLyla §f>> §a""" + lylaDialogue[r.nextInt(6)] + """
                                        
                                        §8
                                        """);
                }
            }
        }
    }

    public String[] lylaDialogue = {
            "A tale seeker. I like you! This isn’t the first location I’ve camped out, although more glamorous than any previous place I’ve been! I’ve seen the statue before… the blue one with the moustache. It was at the last place I ventured, though it looked a lot more run down and rusted. Someone clearly hadn’t taken care of it for a while...",
            "Word spread around like old rumours of this statue’s origin; rumour of the builder who went rogue long ago. Some say he was a robot, while others say he was just a man with a flashy white hoodie. I haven’t seen him since, I don’t think so anyways. What did they call him again?",
            "Strange stories came from that old park I visited long ago, a king of a crafting table? A factory overrun by a slime monster? A group called the Deniz Dashers? Not sure what these mean but supposedly they had a good rep though.",
            "You know I never understood what all these monuments and locations were for… Legends of a team made of Kyanite led by an Australian Mythical Beast? Or a team made of diamond lead by a mighty leader wearing a blue hoodie. So many legendary warriors battled on these lands, no wonder it looked like a wasteland.",
            "Construction workers soon kicked me out of my last location, talks of a sinkhole into an underground cave? I’m not sure why but every time I got close to the centre of the park, I suffered from severe lag spikes… something really wasn’t right with that park.",
            "In my journey I encountered a space station. The S.S.A.Q, otherwise known as Space Station Alpha Quadrant. You know space is a dangerous place! I even found creepers up there… meteor showers… and worst of all… sky bases. "
    };

}
