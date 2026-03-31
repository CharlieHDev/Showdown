package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.CraftalotConfig;
import me.chazzagram.showdown2.files.PhilipConfig;
import me.chazzagram.showdown2.files.PlayerConfig;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class CraftalotEvent implements Listener {


    private static Showdown2 plugin;

    public CraftalotEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void edguardInteractEvent(PlayerInteractEntityEvent e){

        Player p = e.getPlayer();

        if(plugin.ghostManager.getGhostPlayers().contains(p.getName())) return;

        EntityType entity = e.getRightClicked().getType();
        Inventory inventory = p.getInventory();

        if(plugin.finaleActive){
            if(plugin.currentMode.equals("Craftalot")){
                if(entity.equals(EntityType.VILLAGER) && p.getGameMode().equals(GameMode.SURVIVAL)){
                    if(!plugin.itemToCraft.get(p.getName()).isEmpty()){
                        List<String> items = new ArrayList<>();
                        for(ItemStack item : inventory){
                            if(item != null && item.getItemMeta() != null) {
                                items.add(item.getType().name());
                            }
                        }
                        if(items.contains(plugin.itemToCraft.get(p.getName()))){
                            String team = PlayerConfig.get().getString("players." + p.getName() + ".team");
                            plugin.teamCrafts.put(team, plugin.teamCrafts.get(team)+1);
                            if(plugin.teamCrafts.get(team) == 12 && plugin.finaleActive) {
                                plugin.finaleRoundOver(team);
                            } else {
                                plugin.craftTop.put(p.getName(), plugin.craftTop.get(p.getName()) + 1);
                                String currentItem = plugin.itemToCraft.get(p.getName()).replaceAll("_", " ");
                                List<String> craftList = plugin.finaleCraftList;
                                String itemToCraft;
                                itemToCraft = craftList.get(plugin.craftTop.get(p.getName()));
                                plugin.itemToCraft.put(p.getName(), itemToCraft);
                                plugin.craftLists.get(p.getName()).add(itemToCraft);
                                String newItem = plugin.itemToCraft.get(p.getName()).replaceAll("_", " ");
                                for (Player players : Bukkit.getOnlinePlayers()) {
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
                                for (int i = 0; i <= 3; i++) {
                                    p.getInventory().addItem(plugin.craftalotKit()[i]);
                                }
                                p.getInventory().setItemInOffHand(plugin.craftalotKit()[4]);
                            }
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
                        List<String> craftList = plugin.finaleCraftList;
                        plugin.itemToCraft.put(p.getName(), craftList.getFirst());
                        String currentItem = plugin.itemToCraft.get(p.getName()).replaceAll("_", " ");
                        plugin.messagePlayer(p, """
                                        §8
                                        §8
                                        §8[§e§l!§8] §eWelcome back, Traveller! The king has new orders..
                                        §7The first item you are required to bring me is: §e§l""" + currentItem + """
                                        §8
                                        """);
                    }
                }
            }
        } else {

            if (plugin.currentMode.equals("Craftalot")) {
                if (entity.equals(EntityType.VILLAGER) && p.getGameMode().equals(GameMode.SURVIVAL)) {
                    if (!plugin.itemsToCraft.get(p.getName()).isEmpty()) {
                        String team = PlayerConfig.get().getString("players." + p.getName() + ".team");
                        Random r = new Random();
                        int craftCount = 0;
                        int index = 0;
                        int points = 0;
                        int overallPoints = 0;
                        StringBuilder newItems = new StringBuilder();
                        boolean newItemsAdded = false;
                        for(String craftableItem : plugin.itemsToCraft.get(p.getName())) {
                            for (ItemStack item : inventory) {
                                if (item != null && item.getItemMeta() != null) {
                                    if(item.getType().name().equals(craftableItem)){
                                        craftCount++;
                                        newItemsAdded = true;
                                        item.setAmount(item.getAmount() - 1);
                                        switch (index) {
                                            case 0 -> points = 20;
                                            case 1 -> points = 30;
                                            case 2 -> points = 40;
                                        }
                                        overallPoints += points;
                                        plugin.playerCrafts.get(p.getName()).set(index, plugin.playerCrafts.get(p.getName()).get(index) + 1);
                                        plugin.earnPoints(p.getName(), points, true);
                                        String newItem = plugin.craftDifficultyLists.get(index).get(plugin.playerCrafts.get(p.getName()).get(index));
                                        plugin.itemsToCraft.get(p.getName()).set(index, newItem);
//                                        plugin.teamCrafts.put(team, plugin.teamCrafts.get(team) + 1);
                                        plugin.craftTop.put(p.getName(), plugin.craftTop.get(p.getName()) + 1);

                                        newItems.append("- ").append(plugin.toPrettyCase(newItem)).append("\n");
                                        break;
                                    }
                                }
                            }
                            index++;
                        }
                        if(newItemsAdded){
                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(plugin.getPlayerCraftlist(p.getName())));
                            plugin.messagePlayer(p, """
                                    §8
                                    §8
                                    §8[§a§l✔§8] §aITEM(S) CRAFTED.
                                    §e\uD83D\uDCB0""" + overallPoints + """
                                    §8 | §fNew items added to your craftlist:
                                    §e§l""" + newItems + """
                                    §8
                                    """);
                            for (Player players : Bukkit.getOnlinePlayers()) {
                                plugin.messagePlayer(players, "§8[§c§l!§8] " + plugin.getPlayerDisplayName(e.getPlayer().getName()) + " §7has crafted §e" + craftCount + " item(s)! (§e" + plugin.craftTop.get(p.getName()) + "§7)");
                            }
                        } else {
                            plugin.messagePlayer(p, """
                                    §8
                                    §8
                                    §8[§a§l✔§8] §cItem Missing..
                                    §7Slacking off? I gave you what the king requested! Have you double checked what you need?
                                    §8
                                    """);
                        }
//                        if (items.contains(plugin.itemToCraft.get(p.getName()))) {
//                            String team = PlayerConfig.get().getString("players." + p.getName() + ".team");
//                            plugin.teamCrafts.put(team, plugin.teamCrafts.get(team) + 1);
//                            plugin.craftTop.put(p.getName(), plugin.craftTop.get(p.getName()) + 1);
//                            String currentItem = plugin.itemToCraft.get(p.getName()).replaceAll("_", " ");
//                            plugin.earnPoints(p.getName(), 30, true);
//                            Random r = new Random();
//                            String tunnelCount;
//                            if ((plugin.craftTop.get(p.getName()) + 1) % 3 == 0) {
//                                tunnelCount = "twotunnel";
//                            } else {
//                                tunnelCount = "onetunnel";
//                            }
//                            List<String> craftList = CraftalotConfig.get().getStringList("craftlist." + tunnelCount);
//                            String itemToCraft;
//                            List<String> playerCrafts = plugin.craftLists.get(p.getName());
//                            do {
//                                itemToCraft = craftList.get(r.nextInt(craftList.size()));
//                            } while (playerCrafts.contains(itemToCraft));
//                            plugin.itemToCraft.put(p.getName(), itemToCraft);
//                            plugin.craftLists.get(p.getName()).add(itemToCraft);
//                            String newItem = plugin.itemToCraft.get(p.getName()).replaceAll("_", " ");
//                            for (Player players : Bukkit.getOnlinePlayers()) {
//                                plugin.messagePlayer(players, "§8[§c§l!§8] " + plugin.getPlayerDisplayName(e.getPlayer().getName()) + " §7has crafted an item! (§e" + currentItem + "§7)");
//                            }
//                            plugin.messagePlayer(p, """
//                                    §8
//                                    §8
//                                    §8[§a§l✔§8] §aITEM CRAFTED.
//                                    §7Next item to craft: §e§l""" + newItem + """
//                                    §8
//                                    """);
//                            p.getInventory().clear();
//                            for (int i = 0; i <= 3; i++) {
//                                p.getInventory().addItem(plugin.craftalotKit()[i]);
//                            }
//                            p.getInventory().setItemInOffHand(plugin.craftalotKit()[4]);
//                        } else {
//                            String currentItem = plugin.itemToCraft.get(p.getName()).replaceAll("_", " ");
//                            plugin.messagePlayer(p, """
//                                    §8
//                                    §8
//                                    §8[§a§l✔§8] §cItem Missing..
//                                    §7Slacking off? I told you to get me: §e§l""" + currentItem + """
//                                    §8
//                                    """);
//                        }
                    } else {
                        plugin.craftTop.put(p.getName(), 0);
                        Random r = new Random();

                        String firstItem = plugin.craftDifficultyLists.get(0).getFirst();
                        String secondItem = plugin.craftDifficultyLists.get(1).getFirst();
                        String thirdItem = plugin.craftDifficultyLists.get(2).getFirst();

                        plugin.itemsToCraft.put(p.getName(), Arrays.asList(firstItem, secondItem, thirdItem));


                        String list = "§e§l" + plugin.toPrettyCase(firstItem) + "§f, " +
                                "§e§l" + plugin.toPrettyCase(secondItem) + "§f, and " +
                                "§e§l" + plugin.toPrettyCase(thirdItem) + "§f.";
                        plugin.messagePlayer(p, """
                                §8
                                §8
                                §8[§e§l!§8] §eHello Traveller! The king has new orders..
                                §7Here are his demands: §e§l
                                §8""" + list + """
                                §8
                                """);
                    }
                }
            }
        }
        if(entity.equals(EntityType.WANDERING_TRADER)){
            WanderingTrader trader = (WanderingTrader) e.getRightClicked();
            if(trader.getCustomName().equals("Lyla")){
                e.setCancelled(true);
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
                                        §e§lLyla §f>> §a""" + lylaDialogue[r.nextInt(9)] + """
                                        
                                        §8
                                        """);
                }
            }
        }
        if(entity.equals(EntityType.VILLAGER)){
            Villager villager = (Villager) e.getRightClicked();
            if(villager.getProfession().equals(Villager.Profession.LIBRARIAN) && plugin.shopAllowed){
                e.setCancelled(true);
                Inventory gui = Bukkit.createInventory(p, 27, "§eCosmetics");
                ItemStack item;
                for(String cosmetic : PhilipConfig.get().getConfigurationSection("cosmetics").getKeys(false)){
                    item = new ItemStack(PhilipConfig.get().getItemStack("cosmetics." + cosmetic + ".item"));
                    ItemMeta itemMeta = PhilipConfig.get().getItemStack("cosmetics." + cosmetic + ".item").getItemMeta();
                    List<String> lore = new ArrayList<>(List.of());
                    if(itemMeta.getLore() != null){
                        lore.addAll(itemMeta.getLore());
                    }
                    if(PlayerConfig.get().getConfigurationSection("players").getKeys(false).contains(p.getName())) {
                        if (PlayerConfig.get().getInt("players." + p.getName() + ".points") >= PhilipConfig.get().getInt("cosmetics." + cosmetic + ".cost")) {
                            lore.add("§e§l\uD83D\uDCB0§7 | §a" + PlayerConfig.get().get("players." + p.getName() + ".points") + "/" + PhilipConfig.get().getInt("cosmetics." + cosmetic + ".cost"));
                        } else {
                            lore.add("§e§l\uD83D\uDCB0§7 | §8" + PlayerConfig.get().get("players." + p.getName() + ".points") + "/" + PhilipConfig.get().getInt("cosmetics." + cosmetic + ".cost"));
                        }
                    }
                    lore.add("§eClick to equip/unequip.");
                    itemMeta.setLore(lore);
                    item.setItemMeta(itemMeta);
                    gui.addItem(item);
                }
                p.openInventory(gui);
            }
        }
    }

    public String[] lylaDialogue = {
//            "A tale seeker. I like you! This isn’t the first location I’ve camped out, although more glamorous than any previous place I’ve been! I’ve seen the statue before… the blue one with the moustache. It was at the last place I ventured, though it looked a lot more run down and rusted. Someone clearly hadn’t taken care of it for a while...",
//            "Word spread around like old rumours of this statue’s origin; rumour of the builder who went rogue long ago. Some say he was a robot, while others say he was just a man with a flashy white hoodie. I haven’t seen him since, I don’t think so anyways. What did they call him again?",
//            "Strange stories came from that old park I visited long ago, a king of a crafting table? A factory overrun by a slime monster? A group called the Deniz Dashers? Not sure what these mean but supposedly they had a good rep though.",
//            "You know I never understood what all these monuments and locations were for… Legends of a team made of Kyanite led by an Australian Mythical Beast? Or a team made of diamond lead by a mighty leader wearing a blue hoodie. So many legendary warriors battled on these lands, no wonder it looked like a wasteland.",
//            "Construction workers soon kicked me out of my last location, talks of a sinkhole into an underground cave? I’m not sure why but every time I got close to the centre of the park, I suffered from severe lag spikes… something really wasn’t right with that park.",
//            "In my journey I encountered a space station. The S.S.A.Q, otherwise known as Space Station Alpha Quadrant. You know space is a dangerous place! I even found creepers up there… meteor showers… and worst of all… sky bases. ",
//            "I've heard many strange tales in my travels, but nothing stranger than the tale of an army. Countless warriors dressed in suits of yellow? Sounds ridiculous to me! I am usually very capable of distinguishing legendary tales, but this one just sounds like a myth to me.",
//            "8 guilds rule over the land, but they always seem to be fighting for power amongst themselves.. Surely they've heard of democracy right? It could be possible to bring back previous guilds! WitheriteWarriors, Green G- actually.. nevermind.",
//            "While I travelled through old forgotten land, it wasn't in the best of shape. I heard it was abandoned after the guilds suffered from immense lag spikes. Relocations were sporadic, I couldn't find where they went next.. rumours of a cave system? Travelling out to the stars? Neon lights? Grayscale environments?? I'm overwhelmed with information.",
//            "I realise you don't actually know who I am, I'm §e§lLyla§a! A distant relative of that guy who works for Sir Craftalot yknow, but I treat everyone I know like family because we all are! I setup shelter here a while back, but the place seems to have gained a lot of toursists like me! I'm very pleased, a man by the name of Chazzagram was kind enough to welcome me here. He seems very excited to be welcoming people here, 'it's been a while' he said.",
//            "The park has been so much more lively lately! More and more additions are being made, a bar was recently built by the lake serving.. milk? I heard this Ian guy owns the place.",
            "I can't understand where this vault has come from, it appeared near that construction site by the cave. It seems to give off this aura, and recently it's starting changing.",
            "Congratulations to the Crystal Crashers for winning the first competition! I heard they even got their own sunglasses as a reward.",
            "There's something.. off. I can't quite understand it, but I feel these tremors. Strange shakes in the park which I can't find the source of.",
            "Other travellers passing through the park always ask the same question.. 'You've seen the purple lights right?', 'Say do you know anything about a purple glow coming from around here?'. I can confidently say I have no idea what they're talking about.",
            "I'm so sad the holiday season has come and gone.. the players seemed so happy running around collecting gifts, playing games.. did that old fellow ever find all his presents?",
            "I found out recently that there's an online archive showcasing all the players valiant efforts to reach the top! Hey, even you might make it up there some day! I sure am jealous.",
            "Me and Aldo have been having such a blast seeing all the new additions to the park, both games and players alike! If I haven't said it before or you're new around here, welcome to Showdown Park! It's great to have you!",
            "A factory recently opened up near here, I wonder what they're making? If you ask me, I don't see enough ice cream stands around here.. or as a matter of fact any at all! Is it the same factory as Goopy-Droop Factory?",
            "I sure do here some amazing voices around the park, say I bet some of them could even be voice actors!",
            "I saw someone speed past the park just the other day, I wonder why they were in such a hurry. I saw them begin to make a portal to the nether, but I think they messed it up somehow and left in a fit of rage... §a§othey placed the obsidian in the wrong place. §aI mean that's what they get for being in such a rush am I right? The last thing I vaguely heard them say was 'TAS. TAS. TAS.'.. whatever that means.",
            "Did they have to build that newly renovated stage directly opposite my outpost? It's so noisy gone dark! Although, I'd be lying if I said I haven't been up on it a few times to test my vocal range. (It's very high btw)",
            "I recently rediscovered the parrot from the first event right over at main spawn, it's crazy to me but also absolutely adorable that they've found a home here in the park. Beautiful.",
    };

}
