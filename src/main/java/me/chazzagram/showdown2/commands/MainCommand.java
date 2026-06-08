package me.chazzagram.showdown2.commands;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.*;
import me.chazzagram.showdown2.listeners.CrumbleKillData;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.*;
import org.bukkit.command.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.*;
import java.util.stream.Collectors;

public class MainCommand implements CommandExecutor {

    private final Showdown2 plugin;

    public MainCommand(Showdown2 plugin) {
        this.plugin = plugin;
    }

    Location safeSpace = new Location(Bukkit.getServer().getWorld("build"), -71, 159, 581);

    Location safeSpace2 = new Location(Bukkit.getServer().getWorld("build"), -166, 161, 697);

    Location safeSpaceFinale = new Location(Bukkit.getServer().getWorld("build"), 1917, -28, 1476);

    Location ccSafeSpace = new Location(Bukkit.getServer().getWorld("build"), 66, 210, 500);

    Inventory emotesList = Bukkit.createInventory(null, 9, "§eEmotes");

    public void UpdateEmotes(){
        ItemStack emote1 = new ItemStack(Material.PURPLE_DYE, 1);
        ItemStack emote2 = new ItemStack(Material.YELLOW_DYE, 1);
        ItemStack emote3 = new ItemStack(Material.PINK_DYE, 1);
        ItemStack emote4 = new ItemStack(Material.WHITE_DYE, 1);

        ItemMeta emoteMeta = emote1.getItemMeta();
        emoteMeta.setDisplayName("§e§l§oHYPE!");
        emote1.setItemMeta(emoteMeta);

        emoteMeta = emote2.getItemMeta();
        emoteMeta.setDisplayName("§e§l§oFIRE!");
        emote2.setItemMeta(emoteMeta);

        emoteMeta = emote3.getItemMeta();
        emoteMeta.setDisplayName("§e§l§o...");
        emote3.setItemMeta(emoteMeta);

        emoteMeta = emote4.getItemMeta();
        emoteMeta.setDisplayName("§e§l§o:O");
        emote4.setItemMeta(emoteMeta);

        emotesList.clear();
        emotesList.setItem(1, emote1);
        emotesList.setItem(3, emote2);
        emotesList.setItem(5, emote3);
        emotesList.setItem(7, emote4);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

    if (commandSender instanceof ConsoleCommandSender) {
        if (args.length == 0) {
            plugin.messageConsole("Invalid command.");
        } else {
            switch (args[0].toLowerCase()) {
                case "giveelytra":
                    if(plugin.currentMode.equals("Dimension Dash")) {
                        if (args.length > 1) {
                            if (Bukkit.getPlayer(args[1]) != null) {
                                Player p = Bukkit.getPlayer(args[1]);
                                if(plugin.getPlayers().contains(p)) {
                                    ItemStack elytra = new ItemStack(Material.ELYTRA);
                                    ItemMeta meta = elytra.getItemMeta();
                                    meta.setUnbreakable(true);
                                    elytra.setItemMeta(meta);
                                    p.getInventory().setChestplate(elytra);
                                    p.sendTitle("", "§b§l+ ᴇʟʏᴛʀᴀ", 0, 40, 0);
                                }
                            }
                        }
                    }
                    break;
                case "removeelytra":
                    if(plugin.currentMode.equals("Dimension Dash")) {
                        if (args.length > 1) {
                            if (Bukkit.getPlayer(args[1]) != null) {
                                Player p = Bukkit.getPlayer(args[1]);
                                if(plugin.getPlayers().contains(p)) {
                                    ItemStack air = new ItemStack(Material.AIR);
                                    p.getInventory().setChestplate(air);
                                    p.sendTitle("", "§b§l- ᴇʟʏᴛʀᴀ", 0, 40, 0);
                                }
                            }
                        }
                    }
                    break;
                case "givetrident":
                    if(plugin.currentMode.equals("Dimension Dash")) {
                        if (args.length > 1) {
                            if (Bukkit.getPlayer(args[1]) != null) {
                                Player p = Bukkit.getPlayer(args[1]);
                                if(plugin.getPlayers().contains(p)) {
                                    ItemStack trident = new ItemStack(Material.TRIDENT);
                                    ItemMeta meta = trident.getItemMeta();
                                    meta.setUnbreakable(true);
                                    meta.addEnchant(Enchantment.RIPTIDE, 2, true);
                                    trident.setItemMeta(meta);
                                    p.getInventory().addItem(trident);
                                    p.sendTitle("", "§b§l+ ᴛʀɪᴅᴇɴᴛ", 0, 40, 0);
                                }
                            }
                        }
                    }
                    break;
                case "givetrident3":
                    if(plugin.currentMode.equals("Dimension Dash")) {
                        if (args.length > 1) {
                            if (Bukkit.getPlayer(args[1]) != null) {
                                Player p = Bukkit.getPlayer(args[1]);
                                if(plugin.getPlayers().contains(p)) {
                                    ItemStack trident = new ItemStack(Material.TRIDENT);
                                    ItemMeta meta = trident.getItemMeta();
                                    meta.setUnbreakable(true);
                                    meta.addEnchant(Enchantment.RIPTIDE, 3, true);
                                    trident.setItemMeta(meta);
                                    p.getInventory().addItem(trident);
                                    p.sendTitle("", "§b§l+ ᴛʀɪᴅᴇɴᴛ", 0, 40, 0);
                                }
                            }
                        }
                    }
                    break;
                case "taketrident":
                    if(plugin.currentMode.equals("Dimension Dash")) {
                        if (args.length > 1) {
                            if (Bukkit.getPlayer(args[1]) != null) {
                                Player p = Bukkit.getPlayer(args[1]);
                                if(plugin.getPlayers().contains(p)) {
                                    ItemStack trident = new ItemStack(Material.TRIDENT);
                                    ItemMeta meta = trident.getItemMeta();
                                    meta.setUnbreakable(true);
                                    meta.addEnchant(Enchantment.RIPTIDE, 2, true);
                                    trident.setItemMeta(meta);
                                    p.getInventory().remove(trident);
                                    p.sendTitle("", "§b§l- ᴛʀɪᴅᴇɴᴛ", 0, 40, 0);
                                }
                            }
                        }
                    }
                    break;
                case "taketrident3":
                    if(plugin.currentMode.equals("Dimension Dash")) {
                        if (args.length > 1) {
                            if (Bukkit.getPlayer(args[1]) != null) {
                                Player p = Bukkit.getPlayer(args[1]);
                                if(plugin.getPlayers().contains(p)) {
                                    ItemStack trident = new ItemStack(Material.TRIDENT);
                                    ItemMeta meta = trident.getItemMeta();
                                    meta.setUnbreakable(true);
                                    meta.addEnchant(Enchantment.RIPTIDE, 3, true);
                                    trident.setItemMeta(meta);
                                    p.getInventory().remove(trident);
                                    p.sendTitle("", "§b§l- ᴛʀɪᴅᴇɴᴛ", 0, 40, 0);
                                }
                            }
                        }
                    }
                    break;
                case "cdcp":
                    if (args.length > 2) {
                        if (Bukkit.getPlayer(args[2]) != null) {
                            if(plugin.ghostManager.getGhostPlayers().contains(args[2])) break;
                            if (plugin.getPlayers().contains(Bukkit.getPlayer(args[2]))) {
                                if (plugin.runningTimers.containsKey("dimensiondash")) {
                                    int checkpoint = Integer.parseInt(args[1]);
                                    if (plugin.currentRound == 2) {
                                        checkpoint = 7 - checkpoint;
                                    }
                                    if (!plugin.colourDashCheckpoints.containsKey(args[2])) {
                                        plugin.colourDashCheckpoints.put(args[2], 0);
                                    }
                                    if (plugin.colourDashCheckpoints.get(args[2]) < checkpoint) {
                                        int placement = 1;
                                        for (Integer checkpointNum : plugin.colourDashCheckpoints.values()) {
                                            if (checkpointNum == checkpoint) {
                                                placement++;
                                            }
                                        }
                                        int pointsEarned;
                                        switch(checkpoint) {
                                            case 2:
                                                pointsEarned = 67 - placement;
                                                plugin.earnPoints(args[2], pointsEarned, true);
                                                break;
                                            case 4:
                                                pointsEarned = 102 - (placement * 2);
                                                plugin.earnPoints(args[2], pointsEarned, true);
                                                break;
                                            default:
                                                break;
                                        }

                                        Player p = Bukkit.getServer().getPlayer(args[2]);
                                        p.sendTitle("§a[✔] \uD83C\uDFC3-" + checkpoint, "§8[§f§l⏱§8] §e§o" + plugin.getTimer("dimensiondashwatch"), 0, 100, 5);
                                        plugin.messagePlayer(p, "§a[\uD83C\uDFC3-" + checkpoint + "] Dimension crossed!");
                                        plugin.colourDashCheckpoints.put(args[2], checkpoint);
                                        for (Player player : plugin.getPlayers()) {
                                            plugin.messagePlayer(player, "§a§l⏱ §8| " + plugin.getPlayerDisplayName(p.getName()) + "§7 has crossed §aDimension #" + checkpoint + "§7!");
                                        }
                                    }
                                }
                            }
                        }
                    }

                    break;
                case "cdfinish":
                    if (args.length > 2) {
                        if (Bukkit.getPlayer(args[1]) != null) {
                            if(plugin.ghostManager.getGhostPlayers().contains(args[1])) break;
                            if (plugin.getPlayers().contains(Bukkit.getPlayer(args[1]))) {
                                if(plugin.finaleActive){
                                    if (plugin.colourDashCheckpoints.get(args[1]) < 10) {
                                        plugin.cdCompletions++;

                                        Player p = Bukkit.getServer().getPlayer(args[1]);
                                        String team = PlayerConfig.get().getString("players." + args[1] + ".team");
                                        plugin.ddFinaleTeamCompletions.put(team, (plugin.ddFinaleTeamCompletions.get(team) + 1));
                                        p.sendTitle("§aFINISH", "§8[§f§l⏱§8] §e§o" + plugin.getTimer("dimensiondashwatch"), 0, 100, 5);
                                        plugin.messagePlayer(p, "\n§a§lCourse Completed!");
                                        plugin.messagePlayer(p, "§f§l⏱ §8| §fTime Taken: §e" + plugin.getTimer("dimensiondashwatch") + "\n");
                                        plugin.colourDashCheckpoints.put(args[1], 10);
                                        for (Player player : Bukkit.getOnlinePlayers()) {
                                            plugin.messagePlayer(player, "§f\uD83D\uDC51 §8| " + plugin.getPlayerDisplayName(args[1]) + "§e was §f§l#" + plugin.cdCompletions + " §eto cross all dimensions!");
                                        }
                                        int teamSize = TeamsConfig.get().getStringList("teams." + team + ".players").size();
                                        int completions = plugin.ddFinaleTeamCompletions.get(team);


                                        // CRASHED OUT AT THIS. I put "team" instead of "teams".
//                                        plugin.messageConsole("DDF: " + completions + "/" + teamSize + " have completed the race on team: " + team);


                                        if (completions >= teamSize) {
                                            plugin.runningTimers.remove("dimensiondashwatch");
                                            plugin.finaleRoundOver(team);
                                        }

                                        plugin.ghostManager.addGhostPlayer(p.getName());
                                        plugin.revealOtherPlayers(p);
                                    }
                                } else {
                                    if (plugin.runningTimers.containsKey("dimensiondash")) {
                                        if (plugin.currentRound == Integer.parseInt(args[2])) {
                                            if (plugin.colourDashCheckpoints.get(args[1]) < 10) {
                                                plugin.cdCompletions++;
                                                int placement = 1;
                                                for (Integer checkpoint : plugin.colourDashCheckpoints.values()) {
                                                    if (checkpoint == 10) {
                                                        placement++;
                                                    }
                                                }
                                                int pointsEarned = plugin.ddPoints().get(placement - 1);
                                                plugin.earnPoints(args[1], pointsEarned, true);

                                                Player p = Bukkit.getServer().getPlayer(args[1]);
                                                String team = PlayerConfig.get().getString("players." + p.getName() + ".team");
                                                plugin.modeCompletions.put(team, (plugin.modeCompletions.get(team) + 1));
                                                p.sendTitle("§aFINISH", "§8[§f§l⏱§8] §e§o" + plugin.getTimer("dimensiondashwatch"), 0, 100, 5);
                                                plugin.messagePlayer(p, "\n§e\uD83D\uDCB0" + pointsEarned + " §8| §a§lCourse Completed!");
                                                plugin.messagePlayer(p, "§f§l⏱ §8| §fTime Taken: §e" + plugin.getTimer("dimensiondashwatch") + "\n");
                                                plugin.colourDashCheckpoints.put(args[1], 10);
                                                plugin.ghostManager.addGhostPlayer(p.getName());
                                                for (Player player : plugin.getPlayers()) {
                                                    plugin.messagePlayer(player, "§f\uD83D\uDC51 §8| " + plugin.getPlayerDisplayName(p.getName()) + "§e was §f§l#" + placement + " §eto cross all dimensions!");
                                                }

                                                plugin.dashLapData.get(p.getName()).setlap3Time();

                                                plugin.unfinishedPlayers.remove(p);

                                                p.spigot().sendMessage(
                                                        ChatMessageType.ACTION_BAR,
                                                        TextComponent.fromLegacy(
                                                                "§8[§e" + plugin.dashLapData.get(p.getName()).getFinalCompletionTimeConverted() +
                                                                        "§8] [§6" +
                                                                        plugin.formatTime(plugin.dashLapData.get(p.getName()).getLap3Timestamp() - plugin.dashLapData.get(p.getName()).getLap2Timestamp()) +
                                                                        "§8]"
                                                        )
                                                );

                                                if (plugin.cdCompletions == PlayerConfig.get().getConfigurationSection("players").getKeys(false).size()) {
                                                    plugin.runningTimers.remove("dimensiondash");
                                                    plugin.runningTimers.remove("dimensiondashwatch");
                                                    plugin.gameEnd();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                case "bbtp":
                    if(args.length > 2){
                        if (Bukkit.getPlayer(args[2]) != null) {
                            if(plugin.ghostManager.getGhostPlayers().contains(args[2])) break;
                            if (plugin.getPlayers().contains(Bukkit.getPlayer(args[2]))) {
                                if(plugin.finaleActive){

                                    List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());
                                    String firstTeam = leaderteams.getFirst();
                                    String secondTeam = leaderteams.get(1);

                                    String teamName = PlayerConfig.get().getString("players." + args[2] + ".team");
                                    if (teamName != null) {
                                        int zOffset = 38 * (Integer.parseInt(args[1]) - 1);
                                        if((plugin.bridgeTally.get(teamName) & 1) == 0){
                                            zOffset += 20;
                                        }
                                        int xOffset = 35;
                                        double x = 2053.5;
                                        int y = -37;
                                        double z = 900.5;

                                        if(teamName.equals(firstTeam)){
                                            xOffset *= 0;
                                        }
                                        if(teamName.equals(secondTeam)){
                                            xOffset *= 1;
                                        }
                                        if (Bukkit.getPlayer(args[2]) != null) {
                                            Player p = Bukkit.getPlayer(args[2]);
                                            Location tpLoc = new Location(Bukkit.getWorld("build"), (x + xOffset), y, (z - zOffset), 180, 0);
                                            p.teleport(tpLoc);
                                            plugin.messagePlayer(p, "§7§oYou fell! Teleporting you back..");
                                        }
                                    }
                                } else {
                                    if (plugin.runningTimers.containsKey("bridgebuilders")) {
                                        String teamName = PlayerConfig.get().getString("players." + args[2] + ".team");
                                        if (teamName != null) {
                                            int zOffset = 38 * (Integer.parseInt(args[1]) - 1);
                                            if ((plugin.bridgeTally.get(teamName) & 1) == 0) {
                                                zOffset += 20;
                                            }
                                            int xOffset = 35;
                                            double x = 247.5;
                                            int y = -21;
                                            double z = 661.5;
                                            switch (teamName) {
                                                case "RubyRaiders":
                                                    xOffset *= 0;
                                                    break;
                                                case "AmberAmbushers":
                                                    break;
                                                case "TopazTroopers":
                                                    xOffset *= 2;
                                                    break;
                                                case "KyaniteKillers":
                                                    xOffset *= 3;
                                                    break;
                                                case "DiamondDestroyers":
                                                    xOffset *= 4;
                                                    break;
                                                case "SapphireSoldiers":
                                                    xOffset *= 5;
                                                    break;
                                                case "SmithsoniteSlayers":
                                                    xOffset *= 6;
                                                    break;
                                                case "CrystalCrashers":
                                                    xOffset *= 7;
                                                    break;
                                                default:
                                                    break;
                                            }
                                            if (Bukkit.getPlayer(args[2]) != null) {
                                                Player p = Bukkit.getPlayer(args[2]);
                                                Location tpLoc = new Location(Bukkit.getWorld("build"), (x + xOffset), y, (z - zOffset), 180, 0);
                                                p.teleport(tpLoc);
                                                plugin.messagePlayer(p, "§7§oYou fell! Teleporting you back..");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                case "bbftp":
                    if(args.length > 2){
                        if (Bukkit.getPlayer(args[2]) != null) {
                            if(plugin.ghostManager.getGhostPlayers().contains(args[2])) break;
                            if (plugin.getPlayers().contains(Bukkit.getPlayer(args[2]))) {
                                if (plugin.runningTimers.containsKey("bridgebuilders")) {
                                    String teamName = PlayerConfig.get().getString("players." + args[2] + ".team");
                                    if (teamName != null) {
                                        int zOffset = 38 * (Integer.parseInt(args[1]) - 1);
                                        if((plugin.bridgeTally.get(teamName) & 1) == 0){
                                            zOffset += 20;
                                        }
                                        int xOffset = 35;
                                        double x = 2053.5;
                                        int y = -37;
                                        double z = 900.5;
                                        List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());
                                        if(teamName.equals(leaderteams.getFirst())){
                                            xOffset *= 0;
                                        }
                                        if (Bukkit.getPlayer(args[2]) != null) {
                                            Player p = Bukkit.getPlayer(args[2]);
                                            Location tpLoc = new Location(Bukkit.getWorld("build"), (x + xOffset), y, (z - zOffset), 180, 0);
                                            p.teleport(tpLoc);
                                            plugin.messagePlayer(p, "§7§oYou fell! Teleporting you back..");
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                case "bbjcp":
                    if (args.length > 2) {
                        if (Bukkit.getPlayer(args[2]) != null) {
                            if(plugin.ghostManager.getGhostPlayers().contains(args[2])) break;
                            if (plugin.getPlayers().contains(Bukkit.getPlayer(args[2]))) {
                                if (plugin.runningTimers.containsKey("bridgebuilders")) {
                                    if(plugin.bridgeTally.get(PlayerConfig.get().getString("players." + args[2] + ".team")) < (Integer.parseInt(args[1]) * 2)) {
                                        if (!plugin.bridgeJumpRegister.get(Integer.parseInt(args[1])).contains(args[2])) {
                                            plugin.bridgeJumpRegister.get(Integer.parseInt(args[1])).add(args[2]);
                                            plugin.summonFirework(Bukkit.getPlayer(args[2]).getLocation(), PlayerConfig.get().getString("players." + args[2] + ".team"));
                                            int register = 0;
                                            for (String player : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + args[2] + ".team") + ".players")) {
                                                if (plugin.bridgeJumpRegister.get(Integer.parseInt(args[1])).contains(player)) {
                                                    register++;
                                                    if (register == TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + args[2] + ".team") + ".players").size()) {
                                                        plugin.bridgeTally.put(PlayerConfig.get().getString("players." + args[2] + ".team"), plugin.bridgeTally.get(PlayerConfig.get().getString("players." + args[2] + ".team")) + 1);
                                                        plugin.runningTimers.remove(PlayerConfig.get().getString("players." + args[2] + ".team") + args[1]);
                                                        plugin.buildTimeStamps.put(PlayerConfig.get().getString("players." + args[2] + ".team"), plugin.runningTimers.get("bridgebuilders").getValue());
                                                        Integer placement = plugin.bridgeJumpCheckpoints.get(Integer.parseInt(args[1]));
                                                        if (placement == 1) {
                                                            for (Player p : Bukkit.getOnlinePlayers()) {
                                                                plugin.messagePlayer(p, "§e§l⏱ §8| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + args[2] + ".team")) + "§7 crossed §a\uD83C\uDF09-" + args[1] + "§7!");
                                                            }
                                                        } else {
                                                            for (Player p : Bukkit.getOnlinePlayers()) {
                                                                plugin.messagePlayer(p, "§f§l⏱ §8| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + args[2] + ".team")) + "§7 crossed §a\uD83C\uDF09-" + args[1] + "§7!");
                                                            }
                                                        }

                                                        List<Material> blocks = plugin.getBridgeBlocks(Integer.parseInt(args[1]), PlayerConfig.get().getString("players." + args[2] + ".team"));

                                                        for (String player2 : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + args[2] + ".team") + ".players")) {
                                                            if (Bukkit.getServer().getPlayer(player2) != null) {
                                                                Player p = Bukkit.getServer().getPlayer(player2);
                                                                plugin.earnPoints(player2, 5, true);
                                                                plugin.messagePlayer(p, "§e\uD83D\uDCB05 §7| §eYour entire team completed the jump! Bonus points awarded.");
                                                                p.sendTitle("§a[✔] \uD83C\uDF09-" + args[1], "§7Now build!", 0, 40, 0);
                                                                plugin.messagePlayer(p, "§a[\uD83D\uDDFB-" + args[1] + "] Jump Complete!");
                                                                plugin.messagePlayer(p, "§cBuild mode attained, get building!");
                                                                p.setGameMode(GameMode.SURVIVAL);
                                                                p.setAllowFlight(true);
                                                                Location teleportLoc = Bukkit.getServer().getPlayer(args[2]).getLocation().clone().subtract(0, 0, 2);
                                                                teleportLoc.setYaw(180);
                                                                p.teleport(teleportLoc);

                                                                for (Material block : blocks) {
                                                                    p.getInventory().addItem(new ItemStack(block, 64));
                                                                }
                                                            }
                                                        }
                                                        String team = PlayerConfig.get().getString("players." + args[2] + ".team");
                                                        for (int x = plugin.teamJump.get(team)[0] - 3; x <= plugin.teamJump.get(team)[0] + 3; x++) {
                                                            for (int y = plugin.teamJump.get(team)[1] - 2; y <= plugin.teamJump.get(team)[1] + 9; y++) {
                                                                Bukkit.getWorld("build").getBlockAt(x, y, (plugin.teamJump.get(team)[2] - (38 * (Integer.valueOf(args[1]) - 1))) - 16).setType(Material.BARRIER);
                                                            }
                                                        }
                                                        plugin.bridgeJumpCheckpoints.replace(Integer.parseInt(args[1]), placement + 1);
                                                    }
                                                }
                                            }
                                            plugin.earnPoints(args[2], 25, true);
                                            if (Bukkit.getServer().getPlayer(args[2]) != null) {
                                                Player p2 = Bukkit.getServer().getPlayer(args[2]);
                                                plugin.messagePlayer(p2, "§e\uD83D\uDCB025 §7| §eYou have completed this jump!");

                                                for(String teamPlayer : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + args[2] + ".team") + ".players")){
                                                    if(teamPlayer.equals(args[2])) continue;
                                                    if(Bukkit.getServer().getPlayer(teamPlayer) != null) {
                                                        Player p = Bukkit.getServer().getPlayer(teamPlayer);
                                                        p.showPlayer(p2);
                                                        p2.showPlayer(p);
                                                    }
                                                }

                                            }
                                            StringBuilder playerCompletions = new StringBuilder();
                                            playerCompletions.append("§a✔ ".repeat(register));
                                            for (String player2 : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + args[2] + ".team") + ".players")) {
                                                if (Bukkit.getServer().getPlayer(player2) != null) {
                                                    Player p = Bukkit.getServer().getPlayer(player2);
                                                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f + (0.2f * register));
                                                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("§eCompletions§f: " + playerCompletions));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                case "bbfjcp":
                    if (args.length > 2) {
                        if (Bukkit.getPlayer(args[2]) != null) {
                            if(plugin.ghostManager.getGhostPlayers().contains(args[2])) break;
                            if (plugin.getPlayers().contains(Bukkit.getPlayer(args[2]))) {
                                if (plugin.finaleActive) {
                                    if(plugin.bridgeTally.get(PlayerConfig.get().getString("players." + args[2] + ".team")) < (Integer.parseInt(args[1]) * 2)) {
                                        if (!plugin.bridgeJumpRegister.get(Integer.parseInt(args[1])).contains(args[2])) {
                                            plugin.bridgeJumpRegister.get(Integer.parseInt(args[1])).add(args[2]);
                                            plugin.summonFirework(Bukkit.getPlayer(args[2]).getLocation(), PlayerConfig.get().getString("players." + args[2] + ".team"));
                                            int register = 0;
                                            for (String player : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + args[2] + ".team") + ".players")) {
                                                if (plugin.bridgeJumpRegister.get(Integer.parseInt(args[1])).contains(player)) {
                                                    register++;
                                                    if (register == TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + args[2] + ".team") + ".players").size()) {
                                                        plugin.bridgeTally.put(PlayerConfig.get().getString("players." + args[2] + ".team"), plugin.bridgeTally.get(PlayerConfig.get().getString("players." + args[2] + ".team")) + 1);
                                                        Integer placement = plugin.bridgeJumpCheckpoints.get(Integer.parseInt(args[1]));
                                                        if (placement == 1) {
                                                            for (Player p : Bukkit.getOnlinePlayers()) {
                                                                if(plugin.ghostManager.getGhostPlayers().contains(p.getName())) {
                                                                plugin.messagePlayer(p, "§e§l⏱ §8| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + args[2] + ".team")) + "§7 crossed §a\uD83C\uDF09-" + args[1] + "§7!");
                                                            }
                                                                }
                                                        } else {
                                                            for (Player p : Bukkit.getOnlinePlayers()) {
                                                                if (plugin.ghostManager.getGhostPlayers().contains(p.getName())) {
                                                                    plugin.messagePlayer(p, "§f§l⏱ §8| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + args[2] + ".team")) + "§7 crossed §a\uD83C\uDF09-" + args[1] + "§7!");
                                                                }
                                                            }
                                                        }

                                                        List<Material> blocks = plugin.getBridgeBlocksFinale(Integer.parseInt(args[1]), PlayerConfig.get().getString("players." + args[2] + ".team"));

                                                        for (String player2 : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + args[2] + ".team") + ".players")) {
                                                            if (Bukkit.getServer().getPlayer(player2) != null) {
                                                                Player p = Bukkit.getServer().getPlayer(player2);
                                                                plugin.messagePlayer(p, "§eYour entire team completed the jump!");
                                                                p.sendTitle("§a[✔] \uD83C\uDF09-" + args[1], "§7Now build!", 0, 40, 0);
                                                                plugin.messagePlayer(p, "§a[\uD83D\uDDFB-" + args[1] + "] Jump Complete!");
                                                                plugin.messagePlayer(p, "§cBuild mode attained, get building!");
                                                                p.setGameMode(GameMode.SURVIVAL);
                                                                p.setAllowFlight(true);
                                                                Location teleportLoc = Bukkit.getServer().getPlayer(args[2]).getLocation().clone().subtract(0, 0, 2);
                                                                teleportLoc.setYaw(180);
                                                                p.teleport(teleportLoc);

                                                                for (Material block : blocks) {
                                                                    p.getInventory().addItem(new ItemStack(block, 64));
                                                                }
                                                            }
                                                        }
                                                        String team = PlayerConfig.get().getString("players." + args[2] + ".team");
                                                        List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());
                                                        int xC = (Objects.equals(team, leaderteams.getFirst())) ? 2053 : 2088;
                                                        for (int x = xC - 3; x <= xC + 3; x++) {
                                                            for (int y = -35 - 2; y <= -35 + 9; y++) {
                                                                Bukkit.getWorld("build").getBlockAt(x, y, 920 - (Integer.parseInt(args[1]) * 38)).setType(Material.BARRIER);
                                                            }
                                                        }
                                                        plugin.bridgeJumpCheckpoints.replace(Integer.parseInt(args[1]), placement + 1);
                                                    }
                                                }
                                            }
                                            if (Bukkit.getServer().getPlayer(args[2]) != null) {
                                                Player p2 = Bukkit.getServer().getPlayer(args[2]);
                                                plugin.messagePlayer(p2, "§eYou have completed this jump!");
                                            }
                                            StringBuilder playerCompletions = new StringBuilder();
                                            playerCompletions.append("§a✔ ".repeat(register));
                                            for (String player2 : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + args[2] + ".team") + ".players")) {
                                                if (Bukkit.getServer().getPlayer(player2) != null) {
                                                    Player p = Bukkit.getServer().getPlayer(player2);
                                                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f + (0.2f * register));
                                                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("§eCompletions§f: " + playerCompletions));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                case "bbffinish":
                    if (args.length > 1) {
                        if (Bukkit.getPlayer(args[1]) != null) {
                            if(plugin.ghostManager.getGhostPlayers().contains(args[1])) break;
                            if (plugin.getPlayers().contains(Bukkit.getPlayer(args[1]))) {
                                if (plugin.finaleActive) {
                                    if (!plugin.bridgeJumpRegister.get(plugin.bridgeJumpRegister.size()).contains(args[1])) {
                                        plugin.bridgeJumpRegister.get(plugin.bridgeJumpRegister.size()).add(args[1]);
                                        plugin.summonFirework(Bukkit.getPlayer(args[1]).getLocation(), PlayerConfig.get().getString("players." + args[1] + ".team"));
                                        int register = 0;
                                        for (String player : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + args[1] + ".team") + ".players")) {
                                            if (plugin.bridgeJumpRegister.get(plugin.bridgeJumpRegister.size()).contains(player)) {
                                                register++;
                                                if (register == TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + args[1] + ".team") + ".players").size()) {
                                                    plugin.runningTimers.remove(PlayerConfig.get().getString("players." + args[1] + ".team") + "6");
                                                    plugin.bridgeTally.put(PlayerConfig.get().getString("players." + args[1] + ".team"), plugin.bridgeTally.get(PlayerConfig.get().getString("players." + args[1] + ".team")) + 1);
                                                    Integer placement = plugin.bridgeJumpCheckpoints.get(plugin.bridgeJumpCheckpoints.size());
                                                    switch (placement) {
                                                        case 1:
                                                            for (Player p : Bukkit.getOnlinePlayers()) {
                                                                plugin.messagePlayer(p, "§8| §e\uD83D\uDC51 §8| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + args[1] + ".team")) + "§e was §e§l1st §eto finish!");
                                                            }
                                                            break;
                                                        case 2:
                                                            for (Player p : Bukkit.getOnlinePlayers()) {
                                                                plugin.messagePlayer(p, "§8| §7\uD83D\uDC51 §8| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + args[1] + ".team")) + "§e was §7§l2nd §eto finish!");
                                                            }
                                                            break;
                                                        case 3:
                                                            for (Player p : Bukkit.getOnlinePlayers()) {
                                                                plugin.messagePlayer(p, "§8| §6\uD83D\uDC51 §8| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + args[1] + ".team")) + "§e was §6§l3rd §eto finish!");
                                                            }
                                                            break;
                                                        default:
                                                            for (Player p : Bukkit.getOnlinePlayers()) {
                                                                plugin.messagePlayer(p, "§8| §f\uD83D\uDC51 §8| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + args[1] + ".team")) + "§e was §f§l" + placement + "th §eto finish!");
                                                            }
                                                            break;
                                                    }
                                                    String team = PlayerConfig.get().getString("players." + args[1] + ".team");
                                                    plugin.finaleRoundOver(team);
                                                    plugin.runningTimers.remove("finale");
                                                }
                                            }
                                        }
                                        if (Bukkit.getServer().getPlayer(args[1]) != null) {
                                            Player p2 = Bukkit.getServer().getPlayer(args[1]);
                                            plugin.messagePlayer(p2, "§eYou have completed this jump!");
                                        }

                                        StringBuilder playerCompletions = new StringBuilder();
                                        playerCompletions.append("§a✔ ".repeat(register));
                                        for (String player2 : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + args[1] + ".team") + ".players")) {
                                            if (Bukkit.getServer().getPlayer(player2) != null) {
                                                Player p = Bukkit.getServer().getPlayer(player2);
                                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f + (0.2f * register));
                                                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("§eCompletions§f: " + playerCompletions));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                case "bbfinish":
                    if (args.length > 1) {
                        if (Bukkit.getPlayer(args[1]) != null) {
                            if(plugin.ghostManager.getGhostPlayers().contains(args[1])) break;
                            if (plugin.getPlayers().contains(Bukkit.getPlayer(args[1]))) {
                                if (plugin.runningTimers.containsKey("bridgebuilders")) {
                                    if (!plugin.bridgeJumpRegister.get(plugin.bridgeJumpRegister.size()).contains(args[1])) {
                                        plugin.bridgeJumpRegister.get(plugin.bridgeJumpRegister.size()).add(args[1]);
                                        plugin.summonFirework(Bukkit.getPlayer(args[1]).getLocation(), PlayerConfig.get().getString("players." + args[1] + ".team"));
                                        int register = 0;
                                        for (String player : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + args[1] + ".team") + ".players")) {
                                            if (plugin.bridgeJumpRegister.get(plugin.bridgeJumpRegister.size()).contains(player)) {
                                                register++;
                                                if (register == TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + args[1] + ".team") + ".players").size()) {
                                                    plugin.runningTimers.remove(PlayerConfig.get().getString("players." + args[1] + ".team") + "6");
                                                    plugin.bridgeTally.put(PlayerConfig.get().getString("players." + args[1] + ".team"), plugin.bridgeTally.get(PlayerConfig.get().getString("players." + args[1] + ".team")) + 1);
                                                    Integer placement = plugin.bridgeJumpCheckpoints.get(plugin.bridgeJumpCheckpoints.size());
                                                    switch (placement) {
                                                        case 1:
                                                            for (Player p : Bukkit.getOnlinePlayers()) {
                                                                plugin.messagePlayer(p, "§8| §e\uD83D\uDC51 §8| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + args[1] + ".team")) + "§e was §e§l1st §eto finish!");
                                                            }
                                                            break;
                                                        case 2:
                                                            for (Player p : Bukkit.getOnlinePlayers()) {
                                                                plugin.messagePlayer(p, "§8| §7\uD83D\uDC51 §8| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + args[1] + ".team")) + "§e was §7§l2nd §eto finish!");
                                                            }
                                                            break;
                                                        case 3:
                                                            for (Player p : Bukkit.getOnlinePlayers()) {
                                                                plugin.messagePlayer(p, "§8| §6\uD83D\uDC51 §8| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + args[1] + ".team")) + "§e was §6§l3rd §eto finish!");
                                                            }
                                                            break;
                                                        default:
                                                            for (Player p : Bukkit.getOnlinePlayers()) {
                                                                plugin.messagePlayer(p, "§8| §f\uD83D\uDC51 §8| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + args[1] + ".team")) + "§e was §f§l" + placement + "th §eto finish!");
                                                            }
                                                            break;
                                                    }


                                                    for (String player2 : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + args[1] + ".team") + ".players")) {
                                                        if (Bukkit.getServer().getPlayer(player2) != null) {
                                                            Player p = Bukkit.getServer().getPlayer(player2);
                                                            plugin.earnPoints(player2, 5, true);
                                                            plugin.messagePlayer(p, "§e\uD83D\uDCB05 §7| §eYour entire team completed the jump! Bonus points awarded.");
                                                            p.sendTitle("§aFINISH", "", 0, 100, 5);
                                                            plugin.messagePlayer(p, "§e\uD83D\uDCB030 §8| §a§lCourse Completed!");
                                                            p.setAllowFlight(true);
                                                            plugin.ghostManager.addGhostPlayer(p.getName());
                                                        }
                                                    }
                                                    plugin.bridgeJumpCheckpoints.replace(plugin.bridgeJumpCheckpoints.size(), placement + 1);

                                                }
                                            }
                                        }
                                        plugin.earnPoints(args[1], 25, true);
                                        if (Bukkit.getServer().getPlayer(args[1]) != null) {
                                            Player p2 = Bukkit.getServer().getPlayer(args[1]);
                                            plugin.messagePlayer(p2, "§e\uD83D\uDCB025 §7| §eYou have completed this jump!");
                                        }

                                        StringBuilder playerCompletions = new StringBuilder();
                                        playerCompletions.append("§a✔ ".repeat(register));
                                        for (String player2 : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + args[1] + ".team") + ".players")) {
                                            if (Bukkit.getServer().getPlayer(player2) != null) {
                                                Player p = Bukkit.getServer().getPlayer(player2);
                                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f + (0.2f * register));
                                                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("§eCompletions§f: " + playerCompletions));
                                            }
                                        }
                                        List<String> teamList = new ArrayList<>();
                                        for(Player p : plugin.getPlayers()) {
                                            if(!teamList.contains(PlayerConfig.get().getString("players." + p.getName() + ".team"))) {
                                                teamList.add(PlayerConfig.get().getString("players." + p.getName() + ".team"));
                                            }
                                        }

                                        if(plugin.bridgeCheckpoints.get(plugin.bridgeCheckpoints.size()).equals(teamList.size())){
                                            plugin.runningTimers.remove("bridgebuilders");
                                            plugin.gameEnd();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                case "zoomodeath":
                    if(plugin.runningTimers.containsKey("zoomogo")) {
                        if (Bukkit.getPlayer(args[1]) != null) {
                            if(plugin.ghostManager.getGhostPlayers().contains(args[1])) break;
                            if (plugin.getPlayers().contains(Bukkit.getPlayer(args[1]))) {
                                if(plugin.finaleActive) {
                                    if (!plugin.deadPlayers.contains(args[1])) {
                                        Player p = Bukkit.getServer().getPlayer(args[1]);
                                        plugin.deadPlayers.add(args[1]);
                                        plugin.messagePlayer(p, "§c\uD83D\uDC80 §7| You died.");
                                        p.sendTitle("§c§lYou died.", "", 0, 20, 20);
                                        plugin.ghostManager.addGhostPlayer(p.getName());
                                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                            p.setAllowFlight(true);
                                            p.setFlying(true);
                                        }, 1L);
                                        if (plugin.lastHitPlayer.containsKey(args[1])) {
                                            if (!plugin.lastHitPlayer.get(args[1]).isEmpty()) {
                                                plugin.killRecord.add(plugin.getPlayerDisplayName(plugin.lastHitPlayer.get(args[1])) + " §c⚔ " + plugin.getPlayerDisplayName(args[1]));
                                                if (PlayerInfoConfig.get().getConfigurationSection("players").getKeys(false).contains(plugin.lastHitPlayer.get(args[1]))) {
                                                    PlayerInfoConfig.get().set("players." + plugin.lastHitPlayer.get(args[1]) + ".kills", PlayerInfoConfig.get().getInt("players." + plugin.lastHitPlayer.get(args[1]) + ".kills") + 1);
                                                    PlayerInfoConfig.save();
                                                }
                                            }
                                        }

                                        for (Player player : Bukkit.getOnlinePlayers()) {
                                            if (!plugin.deadPlayers.contains(player.getName()) && !plugin.getSpectators().contains(player)) {
                                                if (plugin.lastHitPlayer.containsKey(args[1])) {
                                                    if (!plugin.lastHitPlayer.get(args[1]).isEmpty()) {
                                                        plugin.messagePlayer(player, "§c\uD83D\uDC80 §7| " + plugin.formatKillMessage(plugin.lastHitPlayer.get(args[1]), p.getName()));
                                                    } else {
                                                        plugin.messagePlayer(player, "§c\uD83D\uDC80 §7| " + plugin.formatDeathMessage(p.getName()));
                                                    }
                                                }
                                            }
                                            if (plugin.deadPlayers.contains(player.getName()) || plugin.getSpectators().contains(player)) {
                                                if (plugin.lastHitPlayer.containsKey(args[1])) {
                                                    if (!plugin.lastHitPlayer.get(args[1]).isEmpty()) {
                                                        plugin.messagePlayer(player, "§c\uD83D\uDC80 §7| " + plugin.formatKillMessage(plugin.lastHitPlayer.get(args[1]), p.getName()));
                                                    } else {
                                                        plugin.messagePlayer(player, "§c\uD83D\uDC80 §7| " + plugin.formatDeathMessage(p.getName()));
                                                    }
                                                }
                                            }
                                        }

                                        boolean teamDead = true;
                                        for (String player : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + args[1] + ".team") + ".players")) {
                                            if (!plugin.deadPlayers.contains(player)) {
                                                teamDead = false;
                                                break;
                                            }
                                        }
                                        if (teamDead) {
                                            for (Player player2 : Bukkit.getOnlinePlayers()) {
                                                plugin.messagePlayer(player2, "\n§c§l\uD83D\uDC80 §7| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + args[1] + ".team")) + " §chave been eliminated.\n§f");
                                            }
                                            plugin.deadTeams.add(PlayerConfig.get().getString("players." + args[1] + ".team"));
                                        }

                                        List<String> teamList = new ArrayList<>(List.of());
                                        for (String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
                                            if (!TeamsConfig.get().getStringList("teams." + team + ".players").isEmpty()) {
                                                teamList.add(team);
                                            }
                                        }
                                        if (plugin.deadTeams.size() == teamList.size() - 1) {
                                            for (String team : teamList) {
                                                if (!plugin.deadTeams.contains(team)) {
                                                    plugin.winningTeam = team;
                                                    break;
                                                }
                                            }
                                            if (Objects.equals(plugin.winningTeam, "")) {
                                                plugin.winningTeam = "NO TEAM";
                                            }
                                            plugin.finaleRoundOver(plugin.winningTeam);
                                        }
                                    }
                                } else {
                                    if (!plugin.deadPlayers.contains(args[1])) {
                                        Player p = Bukkit.getServer().getPlayer(args[1]);
                                        if (plugin.zoomoLives.get(args[1]) == 2) {

                                            if (plugin.lastHitPlayer.containsKey(args[1])) {
                                                if (!plugin.lastHitPlayer.get(args[1]).isEmpty()) {
                                                    plugin.killRecord.add(plugin.getPlayerDisplayName(plugin.lastHitPlayer.get(args[1])) + " §f⚔ " + plugin.getPlayerDisplayName(args[1]));
                                                    plugin.playerKillCount.merge(plugin.lastHitPlayer.get(args[1]), 1, Integer::sum);
                                                    plugin.earnPoints(plugin.lastHitPlayer.get(args[1]), 4, true);
                                                }
                                            }

                                            for (Player player : Bukkit.getOnlinePlayers()) {
                                                if (plugin.lastHitPlayer.containsKey(args[1])) {
                                                    if (!plugin.lastHitPlayer.get(args[1]).isEmpty()) {
                                                        String killerName = plugin.lastHitPlayer.get(args[1]);
                                                        String deathMessage = "§c❤ §7| " + plugin.getPlayerDisplayName(p.getName()) + " §7lost a life to " + plugin.getPlayerDisplayName(killerName);

                                                        if (player.getName().equals(killerName)) {
                                                            plugin.messagePlayer(player, "§e\uD83D\uDCB04 §7| " + deathMessage);
                                                        } else {
                                                            plugin.messagePlayer(player, deathMessage);
                                                        }
                                                    } else {
                                                        plugin.messagePlayer(player, "§c❤ §7| " + plugin.getPlayerDisplayName(p.getName()) + " §7lost a life.");
                                                    }
                                                }
                                            }

                                            plugin.zoomoLives.replace(args[1], 1);
                                            BukkitTask task = new BukkitRunnable() {
                                                int timeLeft = 6;

                                                @Override
                                                public void run() {
                                                    if (plugin.runningTimers.containsKey(args[1] + "respawn")) {
                                                        if (!plugin.pausedTimers.contains(args[1] + "respawn")) {
                                                            plugin.runningTimers.get(args[1] + "respawn").setValue(timeLeft);
                                                            timeLeft--;
                                                            switch (timeLeft) {
                                                                case 5:
                                                                    p.sendTitle("1§c❤ §fRemaining.", "You're immune for 5 seconds.");
//                                                                    int[] island = plugin.activeIslands.getLast();
                                                                    if(plugin.zoomoMap.equals("§a§lAdrenaline Ravine")) {
                                                                        plugin.zoomoRespawn(p);
                                                                    }
                                                                    if(plugin.zoomoMap.equals("§6§lDesert")) {
                                                                        plugin.zoomoDesertRespawn(p);
                                                                    }
                                                                    p.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
                                                                    p.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
                                                                    p.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
                                                                    p.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
                                                                    break;
                                                                case 1:
                                                                    p.getInventory().setHelmet(new ItemStack(Material.AIR));
                                                                    p.getInventory().setChestplate(new ItemStack(Material.AIR));
                                                                    p.getInventory().setLeggings(new ItemStack(Material.AIR));
                                                                    p.getInventory().setBoots(new ItemStack(Material.AIR));
                                                                    break;
                                                            }
                                                            if (timeLeft == 0) {
                                                                plugin.messageConsole("Respawn timer finished.");
                                                                plugin.runningTimers.remove(args[1] + "respawn");
                                                                cancel();
                                                            }
                                                        }
                                                    } else {
                                                        plugin.messageConsole("Timer removed by external factor.");
                                                        cancel();
                                                    }
                                                }

                                            }.runTaskTimer(plugin, 0L, 20L);

                                            plugin.runningTimers.put(args[1] + "respawn", new AbstractMap.SimpleEntry<>(task, 6));
                                        } else {
                                            if(p.getInventory().getChestplate() != null) {
                                                if (p.getInventory().getChestplate().getType().equals(Material.IRON_CHESTPLATE)) {
                                                    if (plugin.zoomoMap.equals("§a§lAdrenaline Ravine")) {
                                                        plugin.zoomoRespawn(p);
                                                    }
                                                    if (plugin.zoomoMap.equals("§6§lDesert")) {
                                                        plugin.zoomoDesertRespawn(p);
                                                    }
                                                    break;
                                                }
                                            }
                                            plugin.zoomoLives.replace(args[1], 0);
                                            plugin.deadPlayers.add(args[1]);
                                            plugin.messagePlayer(p, "§c\uD83D\uDC80 §7| You died.");
                                            p.sendTitle("§c§lYou died.", "", 0, 20, 20);
                                            plugin.ghostManager.addGhostPlayer(p.getName());
                                            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                                p.setAllowFlight(true);
                                                p.setFlying(true);
                                            }, 1L);
                                            if (plugin.lastHitPlayer.containsKey(args[1])) {
                                                if (!plugin.lastHitPlayer.get(args[1]).isEmpty()) {
                                                    plugin.killRecord.add(plugin.getPlayerDisplayName(plugin.lastHitPlayer.get(args[1])) + " §c⚔ " + plugin.getPlayerDisplayName(args[1]));
                                                    plugin.earnPoints(plugin.lastHitPlayer.get(args[1]), 16, true);
                                                    if (PlayerInfoConfig.get().getConfigurationSection("players").getKeys(false).contains(plugin.lastHitPlayer.get(args[1]))) {
                                                        PlayerInfoConfig.get().set("players." + plugin.lastHitPlayer.get(args[1]) + ".kills", PlayerInfoConfig.get().getInt("players." + plugin.lastHitPlayer.get(args[1]) + ".kills") + 1);
                                                        PlayerInfoConfig.save();
                                                    }
                                                }
                                            }

                                            for (Player player : Bukkit.getOnlinePlayers()) {
                                                if (!plugin.deadPlayers.contains(player.getName()) && !plugin.getSpectators().contains(player)) {
                                                    if (plugin.lastHitPlayer.containsKey(args[1])) {
                                                        if (!plugin.lastHitPlayer.get(args[1]).isEmpty()) {
                                                            plugin.messagePlayer(player, "§e\uD83D\uDCB03 §7| " + plugin.formatKillMessage(plugin.lastHitPlayer.get(args[1]), p.getName()));
                                                        } else {
                                                            plugin.messagePlayer(player, "§e\uD83D\uDCB03 §7| " + plugin.formatDeathMessage(p.getName()));
                                                        }
                                                    }
                                                    plugin.earnPoints(player.getName(), 3, true);
                                                }
                                                if (plugin.deadPlayers.contains(player.getName()) || plugin.getSpectators().contains(player)) {
                                                    if (plugin.lastHitPlayer.containsKey(args[1])) {
                                                        if (!plugin.lastHitPlayer.get(args[1]).isEmpty()) {
                                                            plugin.messagePlayer(player, "§c\uD83D\uDC80 §7| " + plugin.formatKillMessage(plugin.lastHitPlayer.get(args[1]), p.getName()));
                                                        } else {
                                                            plugin.messagePlayer(player, "§c\uD83D\uDC80 §7| " + plugin.formatDeathMessage(p.getName()));
                                                        }
                                                    }
                                                }
                                            }

                                            boolean teamDead = true;
                                            for (String player : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + args[1] + ".team") + ".players")) {
                                                if (!plugin.deadPlayers.contains(player)) {
                                                    teamDead = false;
                                                    break;
                                                }
                                            }
                                            if (teamDead) {
                                                for (Player player2 : Bukkit.getOnlinePlayers()) {
                                                    plugin.messagePlayer(player2, "\n§c§l\uD83D\uDC80 §7| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + args[1] + ".team")) + " §chave been eliminated.\n§f");
                                                }
                                                plugin.deadTeams.add(PlayerConfig.get().getString("players." + args[1] + ".team"));
                                            }

                                            List<String> teamList = new ArrayList<>(List.of());
                                            for (String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
                                                if (!TeamsConfig.get().getStringList("teams." + team + ".players").isEmpty()) {
                                                    teamList.add(team);
                                                }
                                            }
                                            if (plugin.deadTeams.size() == teamList.size() - 1) {
                                                for (Player p2 : plugin.getPlayers()) {
                                                    if (!plugin.ghostManager.getGhostPlayers().contains(p2.getName())) {
                                                        plugin.winningTeam = PlayerConfig.get().getString("players." + p2.getName() + ".team");
                                                        break;
                                                    }
                                                }
                                                if (Objects.equals(plugin.winningTeam, "")) {
                                                    plugin.winningTeam = "NO TEAM";
                                                }
                                                plugin.deadTeams.clear();
                                                plugin.runningTimers.remove("zoomogo");
                                                plugin.gameEnd();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else if (plugin.runningTimers.containsKey("zoomogostart")) {
                        if (Bukkit.getPlayer(args[1]) != null) {
                            if (plugin.getPlayers().contains(Bukkit.getPlayer(args[1]))) {
                                if(Objects.equals(plugin.zoomoMap, "§a§lAdrenaline Ravine")) {
                                    Bukkit.getPlayer(args[1]).teleport(safeSpace);
                                }
                                if(Objects.equals(plugin.zoomoMap, "§6§lDesert")) {
                                    Bukkit.getPlayer(args[1]).teleport(safeSpace2);
                                }
                                if(plugin.finaleActive){
                                    Bukkit.getPlayer(args[1]).teleport(safeSpaceFinale);
                                }
                                plugin.messagePlayer(Bukkit.getPlayer(args[1]), "§7[§e!§7] §eYou cannot die yet! You've been saved! But grace period will end when the game starts.");
                            }
                        }
                    }
                    break;
                case "ppescape":
                    if(args.length > 2) { // /mce ppescape (left/right) (player)
                        if (plugin.runningTimers.containsKey("pushpoint")) {
                            if (Bukkit.getPlayer(args[2]) != null) {
                                Player p = Bukkit.getServer().getPlayer(args[2]);
                                if(plugin.ghostManager.getGhostPlayers().contains(args[2])) break;
                                String team = PlayerConfig.get().getString("players." + args[2] + ".team");
                                if(plugin.finalPush) {
                                    List<Material> firstBlocks = plugin.mapSides.values().stream()
                                            .filter(blocks -> blocks != null && blocks.length > 0 && blocks[0] != null)
                                            .map(blocks -> blocks[0].getType())
                                            .toList();
                                    List<Material> secondBlocks = plugin.mapSides.values().stream()
                                            .filter(blocks -> blocks != null && blocks.length > 1 && blocks[1] != null)
                                            .map(blocks -> blocks[1].getType())
                                            .toList();
                                    if ((firstBlocks.contains(plugin.teamConcrete.get(team)) && args[1].equals("left")) || (secondBlocks.contains(plugin.teamConcrete.get(team)) && args[1].equals("right"))) {
                                        if (plugin.getPlayers().contains(p) && p.getGameMode() != GameMode.SPECTATOR) {
                                            plugin.messagePlayer(p, "§aYou have escaped!");
                                            p.sendTitle("§a§lYou have escaped!", "§f§oPhew!", 0, 40, 20);
                                            p.setGameMode(GameMode.SPECTATOR);
                                            plugin.ppEscapedPlayers.computeIfAbsent(team, k -> new ArrayList<>())
                                                    .add(p);
                                        }
                                        // TODO: Add messages to indicate whether a whole team has escaped.
                                        // TODO: Add a message broadcast to all players in that map that a player has escaped.
                                    }
                                }
                            }
                        }
                    }
                    break;
                case "ddchange":
                    if(args.length > 2){
                        Player p = Bukkit.getPlayer(args[2]);
                        if(!plugin.currentMode.equals("Dimension Dash")) break;
                        if(plugin.ghostManager.getGhostPlayers().contains(args[2])) break;
                        if(p.getGameMode() != GameMode.SPECTATOR) {
                            if (Objects.equals(args[1], "back") && plugin.currentRound == 2) {

                            }
                            if (Objects.equals(args[1], "front") && plugin.currentRound == 1) {

                            }
                        }
                    }
                    break;
                case "ddsetmap":
                    if(args.length > 1){
                        if(!plugin.currentMode.equals("Dimension Dash")) break;
                        if(plugin.ghostManager.getGhostPlayers().contains(args[1])) break;
                        if(Objects.equals(plugin.ddChosenMap, "")) {
                            plugin.ddChosenMap = plugin.ddMapVotes.get(args[1]);
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                plugin.messagePlayer(p, """
                                        §8
                                        §8
                                        §f""" + plugin.getPlayerDisplayName(args[1]) + """
                                        §f has chosen to travel to..
                                        §a§l""" + plugin.ddChosenMap + """
                                        §8
                                        §8
                                        """);
                            }
                            plugin.addPlayedMap("Dimension Dash", plugin.ddChosenMap);
                        }
                    }
                    break;
                case "ddteleport":
                    if(args.length > 2){
                        Player p = Bukkit.getPlayer(args[2]);
                        if(!plugin.currentMode.equals("Dimension Dash")) break;
                        if(plugin.ghostManager.getGhostPlayers().contains(args[2])) break;
                        if(p.getGameMode() != GameMode.SPECTATOR) {
                            String name = args[2] + "teleport";
                            if((plugin.currentRound == 1 && args[1].equals("front")) || (plugin.currentRound == 2 && args[1].equals("back"))) {
                                BukkitTask task = new BukkitRunnable() {
                                    int timeLeft = 6;
                                    @Override
                                    public void run() {
                                        if (plugin.runningTimers.containsKey(name)) {
                                            if (!plugin.pausedTimers.contains(name)) {
                                                timeLeft--;
                                                plugin.runningTimers.get(name).setValue(timeLeft);
                                                switch (timeLeft) {
                                                    case 5:
                                                        p.playSound(p.getLocation(), Sound.BLOCK_PORTAL_TRIGGER, 1F, 1F);
                                                        PotionEffect darkness = new PotionEffect(PotionEffectType.DARKNESS, 100, 1, true, false);
                                                        p.addPotionEffect(darkness);
                                                        break;
                                                    case 2:
                                                        Location tpLoc = plugin.ddTeleportLocations.get(plugin.ddChosenMap).clone();
                                                        double x = 77;
                                                        float yaw = p.getLocation().getYaw();
                                                        if (Objects.equals(args[1], "front")) {
                                                            x = 77 - p.getLocation().getX();
                                                        }
                                                        if (Objects.equals(args[1], "back")) {
                                                            x = p.getLocation().getX() - 43;
                                                            yaw = yaw+180f;
                                                        }
                                                        tpLoc.subtract(x, 0, 0);
                                                        tpLoc.setYaw(yaw);
                                                        tpLoc.setPitch(p.getLocation().getPitch());
                                                        p.teleport(tpLoc);
                                                        break;
                                                    case 1:
                                                        p.getWorld().spawnParticle(
                                                                Particle.EXPLOSION,
                                                                p.getLocation(),
                                                                1
                                                        );
                                                        p.playSound(p.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 1F, 1F);
                                                        plugin.messagePlayer(p, "§a[✔] \uD83C\uDFC3-1\n§8[§f§l⏱§8] §e§o" + plugin.getTimer("dimensiondashwatch"));
                                                        for (Player player : Bukkit.getOnlinePlayers()) {
                                                            plugin.messagePlayer(player, "§a§l⏱ §8| " + plugin.getPlayerDisplayName(p.getName()) + "§7 has crossed §aDimension Lap #1§7!");
                                                        }
                                                        plugin.dashLapData.get(p.getName()).setlap1Time();
                                                        break;
                                                    default:
                                                        break;
                                                }
                                                if (timeLeft == 0) {
                                                    plugin.runningTimers.remove(name);
                                                    cancel();
                                                }
                                            }
                                        } else {
                                            plugin.messageConsole("Timer removed by external factor.");
                                            cancel();
                                        }
                                    }

                                }.runTaskTimer(plugin, 0L,  20L);

                                plugin.runningTimers.put(name, new AbstractMap.SimpleEntry<>(task, 4));
                            }
                        }
                    }
                    break;
                case "ddteleportback":
                    if(args.length > 2){
                        Player p = Bukkit.getPlayer(args[2]);
                        if(plugin.ghostManager.getGhostPlayers().contains(args[2])) break;
                        if(p.getGameMode() != GameMode.SPECTATOR) {
                            String name = args[2] + "teleport";
                                BukkitTask task = new BukkitRunnable() {
                                    int timeLeft = 6;
                                    @Override
                                    public void run() {
                                        if (plugin.runningTimers.containsKey(name)) {
                                            if (!plugin.pausedTimers.contains(name)) {
                                                timeLeft--;
                                                plugin.runningTimers.get(name).setValue(timeLeft);
                                                switch (timeLeft) {
                                                    case 5:
                                                        p.playSound(p.getLocation(), Sound.BLOCK_PORTAL_TRIGGER, 1F, 1F);
                                                        PotionEffect darkness = new PotionEffect(PotionEffectType.DARKNESS, 100, 1, true, false);
                                                        p.addPotionEffect(darkness);
                                                        break;
                                                    case 2:
                                                        Location tpLoc;
                                                        if(plugin.currentRound == 1){
                                                            tpLoc = new Location(Bukkit.getWorld("build"), 77, 140, 1127, 0, 0);
                                                        } else {
                                                            tpLoc = new Location(Bukkit.getWorld("build"), 77, 140, 1123, 180, 0);
                                                        }
                                                        double mapBaseX = 77;

                                                        switch(args[3]){
                                                            case "2023": mapBaseX = -564; break;
                                                            case "SkiResort": mapBaseX = -976; break;
                                                            case "ChaosCanyon": mapBaseX = -1199; break;
                                                        }

                                                        double playerX = p.getLocation().getX();
                                                        double distance = Math.abs(playerX) - Math.abs(mapBaseX);
                                                        double targetX;

                                                        if (plugin.currentRound == 1) {
                                                            targetX = 77 - distance;
                                                            tpLoc.setYaw(p.getLocation().getYaw());
                                                        } else {
                                                            targetX = (77 - 43) + distance;
                                                            tpLoc.setYaw(p.getLocation().getYaw() + 180f);
                                                        }

                                                        tpLoc.setX(targetX);
                                                        tpLoc.setPitch(p.getLocation().getPitch());

                                                        p.teleport(tpLoc);
                                                        break;
                                                    case 1:
                                                        p.getWorld().spawnParticle(
                                                                Particle.EXPLOSION,
                                                                p.getLocation(),
                                                                1
                                                        );
                                                        plugin.messagePlayer(p, "§a[✔] \uD83C\uDFC3-2\n§8[§f§l⏱§8] §e§o" + plugin.getTimer("dimensiondashwatch"));
                                                        for (Player player : Bukkit.getOnlinePlayers()) {
                                                            plugin.messagePlayer(player, "§a§l⏱ §8| " + plugin.getPlayerDisplayName(p.getName()) + "§7 has crossed §aDimension Lap #2§7!");
                                                        }
                                                        plugin.dashLapData.get(p.getName()).setlap2Time();
                                                        p.playSound(p.getLocation(), Sound.BLOCK_PORTAL_TRAVEL, 1F, 1F);
                                                        break;
                                                    default:
                                                        break;
                                                }
                                                if (timeLeft == 0) {
                                                    plugin.runningTimers.remove(name);
                                                    cancel();
                                                }
                                            }
                                        } else {
                                            plugin.messageConsole("Timer removed by external factor.");
                                            cancel();
                                        }
                                    }

                                }.runTaskTimer(plugin, 0L,  20L);

                                plugin.runningTimers.put(name, new AbstractMap.SimpleEntry<>(task, 4));
                            }
                    }
                    break;
                case "finalefall":
                    if(args.length > 1) {
                        if (plugin.finaleActive) {
                            List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());
                            String firstTeam = leaderteams.getFirst();
                            String secondTeam = leaderteams.get(1);
                            List<String> firstPlayers = TeamsConfig.get().getStringList("teams." + firstTeam + ".players");
                            List<String> secondPlayers = TeamsConfig.get().getStringList("teams." + secondTeam + ".players");
                            World world = Bukkit.getWorld("build");
                            Player p = Bukkit.getPlayer(args[1]);
                            if(p != null) {
                                if (plugin.getPlayers().contains(p)){
                                    if(firstPlayers.contains(p.getName()) && plugin.finaleFirstTeamRevealed){
                                        p.teleport(new Location(world, 1833.5, 157, 895.5, 180, 0));
                                    } else if(secondPlayers.contains(p.getName()) && plugin.finaleSecondTeamRevealed){
                                        p.teleport(new Location(world, 1843.5, 157, 895.5, 180, 0));
                                    } else {
                                        p.teleport(new Location(world, 1838.5, 161, 863.5, 0, 0));
                                    }
                                }
                            }
                        }
                    }
                    break;
                case "clashdeath":
                    if(plugin.currentMode.equals("Crumble Clash") && plugin.ccRoundStarted) {
                        if (Bukkit.getPlayer(args[1]) != null) {
                            if(plugin.ghostManager.getGhostPlayers().contains(args[1])) break;
                            if (plugin.getPlayers().contains(Bukkit.getPlayer(args[1]))) {
                                if(plugin.finaleActive) {
                                    if (!plugin.deadPlayers.contains(args[1])) {
                                        Player p = Bukkit.getServer().getPlayer(args[1]);
                                        plugin.deadPlayers.add(args[1]);
                                        plugin.messagePlayer(p, "§c\uD83D\uDC80 §7| You died.");
                                        p.sendTitle("§c§lYou died.", "", 0, 20, 20);
                                        plugin.ghostManager.addGhostPlayer(p.getName());
                                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                            p.setAllowFlight(true);
                                            p.setFlying(true);
                                        }, 1L);
                                        CrumbleKillData data = plugin.crumbleKillTracker.get(p.getName());

                                        if (data != null && System.currentTimeMillis() - data.time < 5000) {
                                            Player killer = Bukkit.getPlayer(data.attacker);
                                            if (killer != null && !killer.equals(p)) {
                                                String killerTeam = PlayerConfig.get().getString("player." + killer.getName() + ".team");
                                                String victimTeam = PlayerConfig.get().getString("player." + args[1] + ".team");
                                                if(!Objects.equals(killerTeam, victimTeam)) {
                                                    // Credit kill
                                                    plugin.killRecord.add(plugin.getPlayerDisplayName(data.attacker) + " §c⚔ " + plugin.getPlayerDisplayName(args[1]));
                                                    plugin.playerKillCount.putIfAbsent(data.attacker, plugin.playerKillCount.get(data.attacker) + 1);
                                                    if (PlayerInfoConfig.get().getConfigurationSection("players").getKeys(false).contains(data.attacker)) {
                                                        PlayerInfoConfig.get().set("players." + data.attacker + ".kills", PlayerInfoConfig.get().getInt("players." + data.attacker + ".kills") + 1);
                                                        PlayerInfoConfig.save();
                                                    }
                                                }
                                            }
                                        }

                                        plugin.crumbleKillTracker.remove(p.getName());

                                        for (Player player : Bukkit.getOnlinePlayers()) {
                                            if (!plugin.deadPlayers.contains(player.getName()) && !plugin.getSpectators().contains(player)) {
                                                if (data != null && System.currentTimeMillis() - data.time < 5000) {
                                                    Player killer = Bukkit.getPlayer(data.attacker);
                                                    if (killer != null && !killer.equals(p)) {
                                                        plugin.messagePlayer(player, "§c\uD83D\uDC80 §7| " + plugin.formatKillMessage(data.attacker, p.getName()));
                                                    }
                                                } else {
                                                    plugin.messagePlayer(player, "§c\uD83D\uDC80 §7| " + plugin.formatDeathMessage(p.getName()));
                                                }
                                            }
                                            if (plugin.deadPlayers.contains(player.getName()) || plugin.getSpectators().contains(player)) {
                                                if (data != null && System.currentTimeMillis() - data.time < 5000) {
                                                    Player killer = Bukkit.getPlayer(data.attacker);
                                                    if (killer != null && !killer.equals(p)) {
                                                        plugin.messagePlayer(player, "§c\uD83D\uDC80 §7| " + plugin.formatKillMessage(data.attacker, p.getName()));
                                                    }
                                                } else {
                                                    plugin.messagePlayer(player, "§c\uD83D\uDC80 §7| " + plugin.formatDeathMessage(p.getName()));
                                                }
                                            }
                                        }

                                        boolean teamDead = true;
                                        for (String player : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + args[1] + ".team") + ".players")) {
                                            if (!plugin.deadPlayers.contains(player)) {
                                                teamDead = false;
                                                break;
                                            }
                                        }
                                        if (teamDead) {
                                            for (Player player2 : Bukkit.getOnlinePlayers()) {
                                                plugin.messagePlayer(player2, "\n§c§l\uD83D\uDC80 §7| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + args[1] + ".team")) + " §chave been eliminated.\n§f");
                                            }
                                            plugin.deadTeams.add(PlayerConfig.get().getString("players." + args[1] + ".team"));
                                        }

                                        List<String> teamList = new ArrayList<>(List.of());
                                        for (String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
                                            if (!TeamsConfig.get().getStringList("teams." + team + ".players").isEmpty()) {
                                                teamList.add(team);
                                            }
                                        }
                                        if (plugin.deadTeams.size() == teamList.size() - 1) {
                                            for (String team : teamList) {
                                                if (!plugin.deadTeams.contains(team)) {
                                                    plugin.winningTeam = team;
                                                    break;
                                                }
                                            }
                                            if (Objects.equals(plugin.winningTeam, "")) {
                                                plugin.winningTeam = "NO TEAM";
                                            }
                                            plugin.finaleRoundOver(plugin.winningTeam);
                                        }
                                    }
                                } else {
                                    if (!plugin.deadPlayers.contains(args[1])) {
                                        Player p = Bukkit.getServer().getPlayer(args[1]);
                                        plugin.deadPlayers.add(args[1]);
                                        plugin.messagePlayer(p, "§c\uD83D\uDC80 §7| You died.");
                                        p.sendTitle("§c§lYou died.", "", 0, 20, 20);
                                        plugin.ghostManager.addGhostPlayer(p.getName());
                                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                            p.setAllowFlight(true);
                                            p.setFlying(true);
                                        }, 1L);
                                        CrumbleKillData data = plugin.crumbleKillTracker.get(p.getName());

                                        if (data != null && System.currentTimeMillis() - data.time < 5000) {
                                            Player killer = Bukkit.getPlayer(data.attacker);
                                            if (killer != null && !killer.equals(p)) {
                                                // Credit kill
                                                String killerTeam = PlayerConfig.get().getString("player." + killer.getName() + ".team");
                                                String victimTeam = PlayerConfig.get().getString("player." + args[1] + ".team");
                                                if(!Objects.equals(killerTeam, victimTeam)) {
                                                    plugin.killRecord.add(plugin.getPlayerDisplayName(data.attacker) + " §c⚔ " + plugin.getPlayerDisplayName(args[1]));
                                                    plugin.playerKillCount.putIfAbsent(data.attacker, plugin.playerKillCount.get(data.attacker) + 1);
                                                    plugin.earnPoints(data.attacker, 16, true);
                                                    if (PlayerInfoConfig.get().getConfigurationSection("players").getKeys(false).contains(data.attacker)) {
                                                        PlayerInfoConfig.get().set("players." + data.attacker + ".kills", PlayerInfoConfig.get().getInt("players." + data.attacker + ".kills") + 1);
                                                        PlayerInfoConfig.save();
                                                    }
                                                }
                                            }
                                        }

                                        plugin.crumbleKillTracker.remove(p.getName());

                                        for (Player player : Bukkit.getOnlinePlayers()) {
                                            if (!plugin.deadPlayers.contains(player.getName()) && !plugin.getSpectators().contains(player)) {
                                                if (data != null && System.currentTimeMillis() - data.time < 5000) {
                                                    Player killer = Bukkit.getPlayer(data.attacker);
                                                    if (killer != null && !killer.equals(p)) {
                                                        plugin.messagePlayer(player, "§e\uD83D\uDCB03 §7| " + plugin.formatKillMessage(data.attacker, p.getName()));
                                                    }
                                                } else {
                                                    plugin.messagePlayer(player, "§e\uD83D\uDCB03 §7| " + plugin.formatDeathMessage(p.getName()));
                                                }
                                                plugin.earnPoints(player.getName(), 3, true);
                                            }
                                            if (plugin.deadPlayers.contains(player.getName()) || plugin.getSpectators().contains(player)) {
                                                if (data != null && System.currentTimeMillis() - data.time < 5000) {
                                                    Player killer = Bukkit.getPlayer(data.attacker);
                                                    if (killer != null && !killer.equals(p)) {
                                                        plugin.messagePlayer(player, "§c\uD83D\uDC80 §7| " + plugin.formatKillMessage(data.attacker, p.getName()));
                                                    }
                                                } else {
                                                    plugin.messagePlayer(player, "§c\uD83D\uDC80 §7| " + plugin.formatDeathMessage(p.getName()));
                                                }
                                            }
                                        }

                                        boolean teamDead = true;
                                        for (String player : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + args[1] + ".team") + ".players")) {
                                            if (!plugin.deadPlayers.contains(player)) {
                                                teamDead = false;
                                                break;
                                            }
                                        }
                                        if (teamDead) {
                                            for (Player player2 : Bukkit.getOnlinePlayers()) {
                                                plugin.messagePlayer(player2, "\n§c§l\uD83D\uDC80 §7| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + args[1] + ".team")) + " §chave been eliminated.\n§f");
                                            }
                                            plugin.deadTeams.add(PlayerConfig.get().getString("players." + args[1] + ".team"));
                                        }

                                        List<String> teamList = new ArrayList<>(List.of());
                                        for (String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
                                            if (!TeamsConfig.get().getStringList("teams." + team + ".players").isEmpty()) {
                                                teamList.add(team);
                                            }
                                        }
                                        if (plugin.deadTeams.size() == teamList.size() - 1) {
                                            for (String team : teamList) {
                                                if (!plugin.deadTeams.contains(team)) {
                                                    plugin.winningTeam = team;
                                                    break;
                                                }
                                            }
                                            if (Objects.equals(plugin.winningTeam, "")) {
                                                plugin.winningTeam = "NO TEAM";
                                            }
                                            plugin.deadTeams.clear();
                                            plugin.runningTimers.remove("crumbleclash");
                                            plugin.gameEnd();
                                        }
                                    }
                                }
                            }
                        }
                    } else if (plugin.currentMode.equals("Crumble Clash") && plugin.runningTimers.containsKey("crumbleclashstart")) {
                        if (Bukkit.getPlayer(args[1]) != null) {
                            if (plugin.getPlayers().contains(Bukkit.getPlayer(args[1]))) {
                                Bukkit.getPlayer(args[1]).teleport(ccSafeSpace);
                                PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 120, 1, false, false);
                                Bukkit.getPlayer(args[1]).addPotionEffect(PotionEffect);
                                plugin.messagePlayer(Bukkit.getPlayer(args[1]), "§7[§e!§7] §eYou cannot die yet! You've been saved! But grace period will end when the game starts.");
                            }
                        }
                    }
                    break;

                default:
                    break;
            }
        }
    } else if (commandSender instanceof Player p) {
        if (args.length == 0) {
            plugin.messagePlayer(p, "Missing Args.");

        } else {
            switch (args[0].toLowerCase()) {
                case "help":
                    p.sendMessage("""
                            §6All Showdown Commands:§f
                            /mcevent createteam <teamname>
                            /mcevent delteam <teamname>
                            /mcevent jointeam <teamname> <player>
                            /mcevent leaveteam <player>
                            /mcevent starttimer <seconds> <timer>
                            /mcevent startstopwatch <seconds> <stopwatch>
                            /mcevent stoptimer <timer>
                            /mcevent test
                            /mcevent settp <players/spectators/teamname> <location-name>
                            /mcevent deltp <players/spectators/teamname> <location-name>
                            /mcevent tpp <location-name>
                            /mcevent tpt <location-name>
                            /mcevent slimefinishers
                            /mcevent startslimegolf
                            /mcevent startcolourdash
                            /mcevent startbridgebuilders
                            /mcevent startcraftalot
                            /mcevent countvotes
                            /mcevent readycheck
                            /mcevent pause
                            /mcevent unpause
                            /mcevent whitelist
                            /mcevent unwhitelist
                            """);
                    break;
                case "hidepoints":
                    for(int i = 0; i <= 7; i++){
                        plugin.teamShown[i] = false;
                    }
                    break;
                case "infinitecraftalot":
                    List<String> rubyTeam = new ArrayList<>();
                    for(Player player : Bukkit.getOnlinePlayers()){
                        PlayerConfig.get().set("players." + player.getName() + ".points", 0);
                        PlayerConfig.get().set("players." + player.getName() + ".team", "RubyRaiders");
                        SpectatorConfig.get().set("spectators." + player.getName() + ".spectators", null);
                        rubyTeam.add(player.getName());
                    }
                    TeamsConfig.get().set("teams.RubyRaiders.players", rubyTeam);
                    break;
                case "modeindiv":
                    List<String> modeLeaderName = new ArrayList<>(plugin.sortMap(plugin.modeFullPoints).keySet());
                    List<Integer> modeLeaderPoints = new ArrayList<>(plugin.sortMap(plugin.modeFullPoints).values());
                    plugin.messagePlayer(p, "§6§lᴘʀᴇᴠɪᴏᴜs ᴍᴏᴅᴇ ʟᴇᴀᴅᴇʀʙᴏᴀʀᴅ");
                    int placement;
                    for(int i = 0; i < modeLeaderName.size(); i++){
                        placement = i + 1;
                        plugin.messagePlayer(p, "§7" + placement + ". " + plugin.getPlayerDisplayName(modeLeaderName.get(i)) + " §7| §e§l💰" + modeLeaderPoints.get(i));
                    }
                    break;
                case "indiv":
                    boolean shown = true;
                    for(int i = 0; i <= 7; i++){
                        if(!plugin.teamShown[i]){
                            shown = false;
                            break;
                        }
                    }
                    if(shown){
                        List<String> indivNames = new ArrayList<>(plugin.getSortedIndivs().keySet());
                        List<Integer> indivPoints = new ArrayList<>(plugin.getSortedIndivs().values());
                        plugin.messagePlayer(p, "§6§lɪɴᴅɪᴠɪᴅᴜᴀʟ ʟᴇᴀᴅᴇʀʙᴏᴀʀᴅ");
                        int index = 1;
                        for(String name : indivNames){
                            plugin.messagePlayer(p, index + ". " + plugin.getPlayerDisplayName(name) + " §7| §e§l💰" + indivPoints.get(index-1));
                            index++;
                        }
                    } else {
                        plugin.messagePlayer(p, "§6§lIndividual leaderboard is hidden during this segment of the event.");
                    }
                    break;
                case "leaderboard":
                    boolean shown1 = true;
                    for(int i = 0; i <= 7; i++){
                        if(!plugin.teamShown[i]){
                            shown1 = false;
                            break;
                        }
                    }
                    if(shown1){
                        List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());
                        List<Integer> leaderteampoints = new ArrayList<>(plugin.sortByValue().values());
                        plugin.messagePlayer(p, "§6§lᴛᴇᴀᴍ ʟᴇᴀᴅᴇʀʙᴏᴀʀᴅ");
                        int index = 1;
                        for(String name : leaderteams){
                            plugin.messagePlayer(p, index + ". " + plugin.getTeamDisplayName(name) + " §7| §e§l💰" + leaderteampoints.get(index-1));
                            index++;
                        }
                    } else {
                        plugin.messagePlayer(p, "§6§lTeam leaderboard is hidden during this segment of the event.");
                    }
                    break;
                case "startwishes":
                    plugin.startWishBook();
                    break;
                case "showpoints":
                    for(int i = 0; i <= 7; i++){
                        plugin.teamShown[i] = true;
                    }
                    break;
                case "announce":
                    if(args.length > 1){
                        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                        plugin.sendAnnouncement(message);
                    } else {
                        p.sendMessage("Invalid Syntax: /mce announce <text>");
                    }
                    break;
                case "startevent":
                    plugin.startEvent();
                    break;
                case "endevent":
                    plugin.endEvent();
                    break;
                case "togglepvp":
                    plugin.pvpEnabled = !plugin.pvpEnabled;
                    plugin.messagePlayer(p, "PVP Enabled: " + plugin.pvpEnabled);
                    break;
                case "boss":
                    if(args.length > 1){
                        plugin.bossBarBgTest();
                    }
                    break;
                case "recordpan":
                    if(args.length > 1){
                        String name = args[1];
                        BukkitTask task = new BukkitRunnable() {
                            int timeLeft = 301;
                            List<Location> locs = new ArrayList<>();
                            @Override
                            public void run() {
                                if (plugin.runningTimers.containsKey(name)) {
                                    if (!plugin.pausedTimers.contains(name)) {
                                        timeLeft--;
                                        plugin.runningTimers.get(name).setValue(timeLeft);
                                        if(timeLeft == 300){
                                            p.sendTitle("3", "", 0, 40, 20);
                                        }
                                        if(timeLeft == 280){
                                            p.sendTitle("2", "", 0, 40, 20);
                                        }
                                        if(timeLeft == 260){
                                            p.sendTitle("1", "", 0, 40, 20);
                                        }
                                        if(timeLeft == 240){
                                            p.sendTitle("Recording..", "", 0, 240, 0);
                                        }
                                        if(timeLeft < 241){
                                            locs.add(p.getLocation());
                                        }
                                        if (timeLeft == 0) {
                                            p.sendTitle("Done..", "", 0, 40, 0);
                                            plugin.messageConsole("Pan finished.");
                                            PanConfig.get().set("pans." + name, locs);
                                            PanConfig.save();
                                            plugin.runningTimers.remove(name);
                                            cancel();
                                        }
                                    }
                                } else {
                                    plugin.messageConsole("Timer removed by external factor.");
                                    cancel();
                                }
                            }

                        }.runTaskTimer(plugin, 0L, 1L);

                        plugin.runningTimers.put(name, new AbstractMap.SimpleEntry<>(task, 301));
                    }
                    break;
                case "testpan":
                    if(args.length > 1){
                        String name = args[1];
                        if(!PanConfig.get().getConfigurationSection("pans").getKeys(false).contains(name)){
                            plugin.messagePlayer(p, "Not a valid pan name.");
                            break;
                        }
                        World world = Bukkit.getWorld("build");
                        List<?> rawList = PanConfig.get().getList("pans." + name);

                        List<Location> locs = new ArrayList<>();
                        if (rawList != null) {
                            for (Object obj : rawList) {
                                if (obj instanceof Location) {
                                    locs.add((Location) obj);
                                }
                            }
                        }
                        Location start = locs.getFirst();
                        ArmorStand camera = (ArmorStand) world.spawnEntity(start, EntityType.ARMOR_STAND);
                        camera.setInvisible(true);
                        camera.setMarker(true);
                        camera.setGravity(false);
                        camera.setInvulnerable(true);
                        p.setGameMode(GameMode.SPECTATOR);
                        p.teleport(start);
                        BukkitTask task = new BukkitRunnable() {
                            int timeLeft = 246;
                            int index = 0;
                            Location loc;
                            @Override
                            public void run() {
                                if (plugin.runningTimers.containsKey(name)) {
                                    if (!plugin.pausedTimers.contains(name)) {
                                        timeLeft--;
                                        plugin.runningTimers.get(name).setValue(timeLeft);
                                        camera.teleport(locs.get(index));
                                        if(timeLeft < 241) {
                                            index++;
                                            if (p.getGameMode() == GameMode.SPECTATOR && p.getSpectatorTarget() != camera) {
                                                p.setSpectatorTarget(camera);
                                            }
                                            if (timeLeft == 0) {
                                                camera.remove();
                                                plugin.messageConsole("Pan finished.");
                                                plugin.runningTimers.remove(name);
                                                cancel();
                                            }
                                        }
                                    }
                                } else {
                                    plugin.messageConsole("Timer removed by external factor.");
                                    cancel();
                                }
                            }

                        }.runTaskTimer(plugin, 0L, 1L);

                        plugin.runningTimers.put(name, new AbstractMap.SimpleEntry<>(task, 241));
                    }
                    break;
                case "border":
                    if(args.length > 1){
                        plugin.newBorderRadius = Integer.parseInt(args[1]);
                    }
                    break;
                case "setmode":
                    if(args.length > 1){
                        plugin.currentMode = args[1];
                    }
                    break;
                case "round":
                    if(args.length > 1){
                        plugin.currentRound = Integer.parseInt(args[1]);
                    }
                    break;
                case "offglow":
                    for(Player player : Bukkit.getServer().getOnlinePlayers()) {
                        player.setGlowing(false);
                    }
                    break;
                case "setmultiplier":
                    if(args.length > 1){
                        plugin.changeMultiplier(Double.parseDouble(args[1]));
                    }
                    break;
                case "setpoints":
                    if(args.length > 2){
                        if(TeamsConfig.get().getConfigurationSection("teams").getKeys(false).contains(args[1])) {
                            TeamsConfig.get().set("teams." + args[1] + ".points", Integer.parseInt(args[2]));
                            TeamsConfig.save();
                            plugin.messagePlayer(p, "§7Team " + plugin.getTeamDisplayName(args[1]) + "§7 points set to §e" + args[2] + "§7.");
                        } else {
                            plugin.messagePlayer(p, "§7Team " + args[1] + " §7is not a valid team.");
                        }
                    }
                    break;
                case "whitelist":
                    for(String key : PlayerConfig.get().getConfigurationSection("players").getKeys(false)){
                        Bukkit.getOfflinePlayer(key).setWhitelisted(true);
                    }
                    plugin.messagePlayer(p, "§ePlayers in player config have been whitelisted.");
                    break;
                case "unwhitelist":
                    for(String key : PlayerConfig.get().getConfigurationSection("players").getKeys(false)){
                        Bukkit.getOfflinePlayer(key).setWhitelisted(false);
                    }
                    plugin.messagePlayer(p, "§ePlayers in player config have been unwhitelisted.");
                    break;
                case "toggleemotes":
                    plugin.emotesEnabled = !plugin.emotesEnabled;
                    plugin.messagePlayer(p, "Emotes Enabled: " + plugin.emotesEnabled);
                    break;
                case "emotes":
                    if(plugin.emotesEnabled && !plugin.runningTimers.containsKey(p.getName() + "emote")) {
                        UpdateEmotes();
                        p.openInventory(emotesList);
                    }
                    break;
                case "teams":
                    plugin.updateTeamGUI();
                    ((Player) commandSender).openInventory(plugin.gui);
                    break;

//                    for(Player player : plugin.getPlayers()) {
//                        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
//                        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
//
//                        meta.setOwningPlayer(player);
//
//                        meta.setDisplayName(player.getDisplayName());
//                        playerHead.setItemMeta(meta);
//
//                        gui.addItem(playerHead);
//                    }
                case "createteam":
                    if(args.length > 1) {
                        if (TeamsConfig.get().getConfigurationSection("teams") == null) {
                            plugin.messagePlayer(p, "Team '§6" + args[1] + "§7' has been created.");
                            TeamsConfig.get().set("teams." + args[1] + ".name", args[1]);
                            TeamsConfig.get().set("teams." + args[1] + ".icon", "0");
                            TeamsConfig.save();
                        } else {
                            boolean teamExists = false;
                            for (String key : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
                                if (args[1].equals(key)) {
                                    plugin.messagePlayer(p, "A team with this name already exists!");
                                    teamExists = true;
                                    break;
                                }
                            }
                            if (!teamExists) {
                                plugin.messagePlayer(p, "Team '§6" + args[1] + "§7' has been created.");
                                TeamsConfig.get().set("teams." + args[1] + ".name", args[1]);
                                TeamsConfig.save();
                            }
                        }
                    }
                    break;

                case "delteam":
                    if(args.length > 1) {
                        if (TeamsConfig.get().getConfigurationSection("teams") == null) {
                            plugin.messagePlayer(p, "Currently no teams exist.");
                        } else {
                            boolean teamExists = false;
                            for (String key : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
                                if (args[1].equals(key)) {
                                    plugin.messagePlayer(p, "Team '§6" + args[1] + "§7' has been deleted.");
                                    TeamsConfig.get().set("teams." + args[1], null);
                                    TeamsConfig.save();
                                    teamExists = true;
                                    break;
                                }
                            }
                            if (!teamExists) {
                                plugin.messagePlayer(p, "A team with this name doesn't exist!");
                            }
                        }
                    }
                    break;

                case "jointeam":
                    if(args.length > 2) {
                        if (TeamsConfig.get().getConfigurationSection("teams") == null) {
                            plugin.messagePlayer(p, "Currently no teams exist.");
                        } else {
                            boolean teamExists = false;
                            for (String key : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
                                if (args[1].equals(key)) {
                                    List<String> teamPlayers = TeamsConfig.get().getStringList("teams." + args[1] + ".players");
                                    if(teamPlayers.contains(args[2])) {
                                        plugin.messagePlayer(p, "Player " + args[2] + " is already on team " + args[1] + "!");
                                    } else {
                                        plugin.messagePlayer(p, "Player " + args[2] + " has been added to team " + args[1] + "!");
                                        teamPlayers.add(args[2]);
                                        TeamsConfig.get().set("teams." + args[1] + ".players", teamPlayers);
                                        PlayerConfig.get().set("players." + args[2] + ".points", 0);
                                        PlayerConfig.get().set("players." + args[2] + ".team", TeamsConfig.get().getString("teams." + args[1] + ".name"));
                                        PlayerConfig.save();
                                        TeamsConfig.save();
                                        if(!SpectatorConfig.get().getStringList("spectators").isEmpty()) {
                                            for (String player : SpectatorConfig.get().getStringList("spectators")) {
                                                if (args[2].equals(player)) {
                                                    List<String> newSpectators = SpectatorConfig.get().getStringList("spectators");
                                                    newSpectators.remove(args[2]);
                                                    SpectatorConfig.get().set("spectators", newSpectators);
                                                    SpectatorConfig.save();
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    teamExists = true;
                                }

                            }
                            if(!teamExists) {
                                plugin.messagePlayer(p, "A team with this name doesn't exist!");
                            }
                        }
                    }
                    break;
                case "leaveteam":
                    if(args.length > 1) {
                        if (TeamsConfig.get().getConfigurationSection("teams") == null) {
                            plugin.messagePlayer(p, "Currently no teams exist.");
                        } else {
                            if(PlayerConfig.get().getConfigurationSection("players").getKeys(false).contains(args[1])){
                                String team = PlayerConfig.get().getString("players." + args[1] + ".team");
                                PlayerConfig.get().set("players." + args[1], null);
                                PlayerConfig.save();
                                List<String> teamPlayers = TeamsConfig.get().getStringList("teams." + team + ".players");
                                teamPlayers.remove(args[1]);
                                TeamsConfig.get().set("teams." + team + ".players", teamPlayers);
                                TeamsConfig.save();
                                List<String> newSpectators = SpectatorConfig.get().getStringList("spectators");
                                newSpectators.add(args[1]);
                                SpectatorConfig.get().set("spectators", newSpectators);
                                SpectatorConfig.save();
                            } else {
                                plugin.messagePlayer(p, "This player is not on a team!");
                            }
                        }
                    }
                    break;

                case "starttimer":
                    if(args.length > 2) {
                        plugin.startTimer(Integer.parseInt(args[1]), args[2]);
                    }
                    break;
                case "startstopwatch":
                    if(args.length > 2) {
                        plugin.startStopwatch(Integer.parseInt(args[1]), args[2]);
                    }
                    break;
                case "stoptimer":
                    if(args.length > 1) {
                        plugin.stopTimer(args[1]);
                    }
                    break;

                case "test":
                    for(Player players : plugin.getPlayers()){
                        plugin.messagePlayer(players, "Test.");
                    }
                    break;
                case "settp":
                    if(args.length > 2) {
                        switch (args[1]) {
                            case "players":
                                TeleportConfig.get().set("players." + args[2], p.getLocation());
                                TeleportConfig.save();
                                plugin.messagePlayer(p, "Location " + args[2] + " has been set for players!");
                                break;
                            case "spectators":
                                TeleportConfig.get().set("spectators." + args[2], p.getLocation());
                                TeleportConfig.save();
                                plugin.messagePlayer(p, "Location " + args[2] + " has been set for spectators!");
                                break;
                            default:
                                boolean teamTpFound = false;
                                for (String key : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
                                    if (args[1].equals(key)) {
                                        teamTpFound = true;
                                        TeleportConfig.get().set("teams." + args[1] + "." + args[2], p.getLocation());
                                        TeleportConfig.save();
                                        plugin.messagePlayer(p, "Location " + args[2] + " has been set for " + args[1] + "!");
                                        break;
                                    }
                                }
                                if(!teamTpFound) {
                                    plugin.messagePlayer(p, "Invalid argument (spectators/players/teamname).");
                                }
                                break;
                        }
                    }
                    break;
                case "deltp":
                    if(args.length > 2) {
                        boolean teleportFound = false;
                        switch(args[1]){
                            case "players":
                                for(String key : TeleportConfig.get().getConfigurationSection("players").getKeys(false)) {
                                    if(args[2].equals(key)) {
                                        TeleportConfig.get().set("players." + args[2], null);
                                        TeleportConfig.save();
                                        plugin.messagePlayer(p, "Location " + args[2] + " has been deleted from players!");
                                        teleportFound = true;
                                        break;
                                    }
                                }
                                break;
                            case "spectators":
                                for(String key : TeleportConfig.get().getConfigurationSection("spectators").getKeys(false)) {
                                    if(args[2].equals(key)) {
                                        TeleportConfig.get().set("spectators." + args[2], null);
                                        TeleportConfig.save();
                                        plugin.messagePlayer(p, "Location " + args[2] + " has been deleted from spectators!");
                                        teleportFound = true;
                                        break;
                                    }
                                }
                                break;
                            default:
                                boolean teamTpFound = false;
                                for(String key : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
                                    if(args[1].equals(key)) {
                                        teamTpFound = true;
                                        for(String key2 : TeleportConfig.get().getConfigurationSection("teams." + args[1]).getKeys(false)) {
                                            if(args[2].equals(key2)) {
                                                TeleportConfig.get().set("teams." + args[1] + "." + args[2], null);
                                                TeleportConfig.save();
                                                plugin.messagePlayer(p, "Location " + args[2] + " has been deleted from " + args[1] + "!");
                                                teleportFound = true;
                                                break;
                                            }
                                        }
                                        break;
                                    }
                                }
                                if (!teamTpFound) {
                                    plugin.messagePlayer(p, "Invalid argument (spectators/players/teamname).");
                                }
                                break;
                        }
                        if(!teleportFound) {
                            plugin.messagePlayer(p, "Teleport location " + args[2] + " doesn't exist!");
                        }
                    }
                    break;
                case "tpp":
                    if(args.length > 1) {
                        plugin.teleportPlayers(TeleportConfig.get().getLocation("players." + args[1]), 5);
                    }
                    break;
                case "addshopitem":
                    if(args.length > 1){
                        if(p.getInventory().getItemInMainHand().getType() != Material.AIR){
                            int key = 0;
                            if(PhilipConfig.get().getConfigurationSection("cosmetics") != null) {
                                key = PhilipConfig.get().getConfigurationSection("cosmetics").getKeys(false).size() + 1;
                            }
                            PhilipConfig.get().set("cosmetics." + key + ".item", p.getInventory().getItemInMainHand());
                            PhilipConfig.get().set("cosmetics." + key + ".cost", Integer.parseInt(args[1]));
                            PhilipConfig.save();
                        }
                    }
                    break;
                case "tpt":
                    if(args.length > 1) {
                        plugin.teamTeleport(args[1], 5);
                    }
                    break;
                case "music":
                    if(plugin.currentMode.equals("Lobby")) {
                        if (plugin.musicManager.getMusicTasks().containsKey(p.getUniqueId())) {
                            plugin.musicManager.stopMusic(p);
                        } else {
                            plugin.musicManager.startMusic(p);
                        }
                    } else {
                        plugin.messagePlayer(p, "§8[§c§l!§8] §7You cannot toggle lobby music during games!");
                    }
                    break;

                case "addgubtp":
                    if(GubTPConfig.get().getConfigurationSection("teleports") != null) {
                        int tpCount = GubTPConfig.get().getConfigurationSection("teleports").getKeys(false).size() + 1;
                        GubTPConfig.get().set("teleports.loc" + tpCount, p.getLocation());
                    } else {
                        GubTPConfig.get().set("teleports.loc1", p.getLocation());
                    }
                    GubTPConfig.save();
                    break;
                case "addpresent":
                    if(PresentsConfig.get().getConfigurationSection("presents") != null) {
                        int presentCount = PresentsConfig.get().getConfigurationSection("presents").getKeys(false).size() + 1;
                        PresentsConfig.get().set("presents.loc" + presentCount, p.getLocation());
                    } else {
                        PresentsConfig.get().set("presents.loc1", p.getLocation());
                    }
                    PresentsConfig.save();
                    break;
                case "startpresenthunt":
                    if(args.length > 1) {
                        int round = Integer.parseInt(args[1]);
                        plugin.startPresentHunt(round);
                    }
                    break;
                case "resetmodes":
                    if(args.length > 1){
                        if(args[1].equals("confirm")){
                             plugin.resetBridgeBuilders();
                             plugin.resetColourDash();
                             plugin.resetCraftalot();
                             plugin.resetGubGame();
                             plugin.resetSlimeGolf();
                             plugin.resetSurvivalGames();
                             plugin.resetZoomoGo();
                        } else {
                            plugin.messagePlayer(p, "Invalid argument /mce resetmodes confirm");
                        }
                    } else {
                        plugin.messagePlayer(p, "Invalid argument /mce resetmodes confirm");
                    }
                    break;
                case "startfinale":
                    plugin.startFinale();
                    break;
                case "slimefinishers":
                    plugin.slimeGolfTimes();
                    break;
                case "startslimegolf":
                    plugin.currentRound = 1;
                    plugin.startSlimeGolf();
                    break;
                case "startcolourdash":
                    plugin.currentRound = 1;
                    World world = Bukkit.getWorld("build");
                    for(int i = 2; i <= 4; i++) {
                        world.getBlockAt(plugin.cdWallCoords[i][0], plugin.cdWallCoords[i][1] - 1, plugin.cdWallCoords[i][2]).setType(Material.REDSTONE_BLOCK);
                        world.getBlockAt(plugin.cdWallCoords[i][0], plugin.cdWallCoords[i][1] - 1, plugin.cdWallCoords[i][2]).setType(Material.DIRT);
                    }
                    for(int i = 0; i <= 1; i++) {
                        world.getBlockAt(plugin.cdWallCoords[i][0], plugin.cdWallCoords[i][1], plugin.cdWallCoords[i][2]).setType(Material.REDSTONE_BLOCK);
                        world.getBlockAt(plugin.cdWallCoords[i][0], plugin.cdWallCoords[i][1], plugin.cdWallCoords[i][2]).setType(Material.DIRT);
                    }
                    world.getBlockAt(78, 139, 1235).setType(Material.REDSTONE_BLOCK);
                    world.getBlockAt(78, 139, 1235).setType(Material.AIR);
//                    plugin.startColourDash();
                    break;
                case "startdimensiondash":
                    plugin.currentRound = 1;
                    World world2 = Bukkit.getWorld("build");
                    for(int i = 2; i <= 4; i++) {
                        world2.getBlockAt(plugin.cdWallCoords[i][0], plugin.cdWallCoords[i][1] - 1, plugin.cdWallCoords[i][2]).setType(Material.REDSTONE_BLOCK);
                        world2.getBlockAt(plugin.cdWallCoords[i][0], plugin.cdWallCoords[i][1] - 1, plugin.cdWallCoords[i][2]).setType(Material.DIRT);
                    }
                    for(int i = 0; i <= 1; i++) {
                        world2.getBlockAt(plugin.cdWallCoords[i][0], plugin.cdWallCoords[i][1], plugin.cdWallCoords[i][2]).setType(Material.REDSTONE_BLOCK);
                        world2.getBlockAt(plugin.cdWallCoords[i][0], plugin.cdWallCoords[i][1], plugin.cdWallCoords[i][2]).setType(Material.DIRT);
                    }
                    world2.getBlockAt(78, 139, 1235).setType(Material.REDSTONE_BLOCK);
                    world2.getBlockAt(78, 139, 1235).setType(Material.AIR);

                    // Castle elytra barriers
                    world2.getBlockAt(39, 156, 1213).setType(Material.REDSTONE_BLOCK);
                    world2.getBlockAt(39, 156, 1213).setType(Material.AIR);

                    world2.getBlockAt(94, 159, 1213).setType(Material.REDSTONE_BLOCK);
                    world2.getBlockAt(94, 159, 1213).setType(Material.AIR);
                    // -------

                    world2.getBlockAt(60, 156, 1250).setType(Material.REDSTONE_BLOCK);
                    world2.getBlockAt(60, 156, 1250).setType(Material.AIR);
                    plugin.startDimensionDash();
                    break;
                case "startbridgebuilders":
                    plugin.startBridgeBuilders();
                    break;
                case "startcrumbleclash":
                    plugin.currentRound = 1;
                    plugin.startCrumbleClash();
                    break;
                case "startpushpoint":
                    plugin.currentRound = 1;
                    plugin.startPushPoint();
                    break;
                case "startcraftalot":
                    plugin.startCraftalot();
                    break;
                case "startzoomogo":
                    plugin.currentRound = 1;
                    plugin.startZoomoGo();
                    break;
                case "testitembox":
                    plugin.summonItemBox(p.getLocation());
                    plugin.messagePlayer(p, "Item box summoned.");
                    break;
//                case "shrinkplayers":
//                    for(Player player : Bukkit.getOnlinePlayers()) {
//                        player.getAttribute(Attribute.SCALE).setBaseValue(0.5F);
//                        player.sendMessage("§d§l[!] £10 has been donated to shrink you down!");
//                    }
//                    plugin.playSoundAll(Sound.BLOCK_BREWING_STAND_BREW, 2F);
//                    break;
//                case "growplayers":
//                    for(Player player : Bukkit.getOnlinePlayers()) {
//                        player.getAttribute(Attribute.SCALE).setBaseValue(1);
//                    }
//                    plugin.playSoundAll(Sound.BLOCK_BREWING_STAND_BREW, 1F);
//                    break;
                case "voteparty":
                    p.sendMessage("Vote party enabled.");
                    plugin.voteParty = true;
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        player.sendTitle("§b★ §b§lᴠᴏᴛᴇ ᴘᴀʀᴛʏ §b★", "§aᴇɴᴀʙʟᴇᴅ", 0, 60, 20);
                        player.sendMessage("§b★ §b§lVoting will now include a massive increase in power ups.");
                    }
                    break;
                case "multiplier":
                    p.sendMessage("Multiplier x2 enabled.");
                    plugin.multiplier = 2.0;
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        player.sendTitle("§e★ §e§lᴘᴏɪɴᴛ ᴍᴜʟᴛɪᴘʟɪᴇʀ §e★", "§aᴇɴᴀʙʟᴇᴅ: §62X", 0, 60, 20);
                        player.sendMessage("§e★ §e§lThe point multiplier has now been enabled! Points are now worth double.");
                    }
                    break;
                case "shrinkall":
                    p.sendMessage("Players shrunk!");
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        player.getAttribute(Attribute.SCALE).setBaseValue(0.5);
                        player.sendTitle("§a★ §a§lᴛɪɴʏ ᴘʟᴀʏᴇʀs §a★", "§cᴘᴇʀᴍᴀɴᴇɴᴛ", 0, 60, 20);
                        player.sendMessage("§a★ §a§lEveryone is TINY! But this time, you'll stay that way..");
                    }
                    break;
                case "zoomospeed":
                    p.sendMessage("Zoomo speed enabled.");
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        player.sendTitle("§d★ §d§lᴢᴏᴏᴍᴏ sᴘᴇᴇᴅ §d★", "§aᴇɴᴀʙʟᴇᴅ", 0, 60, 20);
                        player.sendMessage("§d★ §d§lDuring Zoomo Go! everyone will have a big speed boost!");
                    }
                    plugin.zoomoSpeed = true;
                    break;
                case "startgubgame":
                    plugin.startGubGame();
                    break;
                case "startsurvivalgames":
                    plugin.startSurvivalGames();
                    break;
                case "countvotes":
                    if(args.length > 1){
                        if(args[1].equals("least")){
                            plugin.leastVotes = true;
                            plugin.startVoting();
                        } else if (args[1].equals("audience")){
                            plugin.audienceVote = true;
                            plugin.startVoting();
                        } else {
                            plugin.messagePlayer(p, "Incorrect argument. (least or audience)");
                        }
                    } else {
                        plugin.startVoting();
                    }
                    break;
                case "readycheck":
                    plugin.jumpStates.clear();
                    if(args.length > 1) {
                        switch(args[1]){
                            case "jump":
                                plugin.readyType = "jump";
                                break;
                            case "snowballs":
                                plugin.readyType = "snowballs";
                                break;
                            case "sneak":
                                plugin.readyType = "sneak";
                                break;
                            case "punch":
                                plugin.readyType = "punch";
                                break;
                            case "sneakbomb":
                                plugin.readyType = "sneakbomb";
                                break;
                        }
                        plugin.getReadyPlayers();
                    } else {
                        plugin.messagePlayer(p, "Missing Args.");
                    }
                    break;
                case "pause":
                    plugin.pauseEvent();
                    break;
                case "unpause":
                    plugin.resumeEvent();
                    break;
                case "resetpoints":
                    for(String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
                        TeamsConfig.get().set("teams." + team + ".points", 0);
                    }
                    for(String player : PlayerConfig.get().getConfigurationSection("players").getKeys(false)) {
                        PlayerConfig.get().set("players." + player + ".points", 0);
                    }
                    PlayerConfig.save();
                    TeamsConfig.save();
                    break;
                default:
                    plugin.messagePlayer(p, "Missing Args.");
                    break;
            }
        }
    } else if (commandSender instanceof BlockCommandSender) {
        if (args.length == 0) {
            plugin.messageConsole("Invalid command.");
        } else {
            switch (args[0].toLowerCase()) {
                case "slimecp":
                    if (args.length > 2) {
                        if(plugin.runningTimers.containsKey("slimegolf")) {
                            Integer placement = plugin.slimeCheckpoints.get(Integer.parseInt(args[1]));

                            for (Player p : plugin.getPlayers()) {
                                plugin.messagePlayer(p, "§8| §a§l⏱ §8| " + plugin.getTeamDisplayName(args[2]) + "§7 has reached §a\uD83D\uDDFB-" + args[1] + "§7!");
                            }

                            int pointsEarned = 0;
                            int dividedPointsEarned = 0;
                            switch(Integer.parseInt(args[1])) {
                                case 2:
                                    pointsEarned = 148 - (placement * 8);
                                    dividedPointsEarned = pointsEarned / 4;
                                    for (String player : TeamsConfig.get().getStringList("teams." + args[2] + ".players")) {
                                        plugin.earnPoints(player, dividedPointsEarned, true);
                                    }
                                    break;
                                case 4:
                                    pointsEarned = 216 - (placement * 16);
                                    dividedPointsEarned = pointsEarned / 4;
                                    for (String player : TeamsConfig.get().getStringList("teams." + args[2] + ".players")) {
                                        plugin.earnPoints(player, dividedPointsEarned, true);
                                    }
                                    break;
                                default:
                                    break;
                            }
                            plugin.teamCheckpoints.put(args[2], Integer.parseInt(args[1]));

                            for (String player : TeamsConfig.get().getStringList("teams." + args[2] + ".players")) {
                                if (Bukkit.getServer().getPlayer(player) != null) {
                                    Player p = Bukkit.getServer().getPlayer(player);
                                    p.sendTitle("§a[✔] \uD83D\uDDFB-" + args[1], "§8[§f§l⏱§8] §e§o" + plugin.getTimer("slimegolf"), 0, 100, 5);
                                    plugin.messagePlayer(p, "§a[\uD83D\uDDFB-" + args[1] + "] Checkpoint reached!");
                                }
                            }
                            plugin.slimeCheckpoints.replace(Integer.parseInt(args[1]), placement + 1);
                        }
                        if(plugin.runningTimers.containsKey("finale")){
                            Integer placement = plugin.slimeCheckpoints.get(Integer.parseInt(args[1]));
                            if (placement == 1) {
                                for (Player p : plugin.getPlayers()) {
                                    plugin.messagePlayer(p, "§8| §b★ §8| " + plugin.getTeamDisplayName(args[2]) + "§e was 1st to reach §a\uD83D\uDDFB-" + args[1] + "§7!");
                                }
                            } else {
                                for (Player p : plugin.getPlayers()) {
                                    plugin.messagePlayer(p, "§8| §a§l⏱ §8| " + plugin.getTeamDisplayName(args[2]) + "§7 has reached §a\uD83D\uDDFB-" + args[1] + "§7!");
                                }
                            }

                            plugin.teamCheckpoints.put(args[2], Integer.parseInt(args[1]));

                            for (String player : TeamsConfig.get().getStringList("teams." + args[2] + ".players")) {
                                if (Bukkit.getServer().getPlayer(player) != null) {
                                    Player p = Bukkit.getServer().getPlayer(player);
                                    p.sendTitle("§a[✔] \uD83D\uDDFB-" + args[1], "§8[§f§l⏱§8] §e§o" + plugin.getTimer("finale"), 0, 100, 5);
                                    plugin.messagePlayer(p, "§a[\uD83D\uDDFB-" + args[1] + "] Checkpoint reached!");
                                }
                            }
                            plugin.slimeCheckpoints.replace(Integer.parseInt(args[1]), placement + 1);
                        }
                    }
                    break;
                case "slimefinish":
                    if (args.length > 1) {
                        if (plugin.runningTimers.containsKey("slimegolf")) {
                            Integer placement = plugin.slimeCheckpoints.get(plugin.slimeCheckpoints.size());
                            switch (placement) {
                                case 1:
                                    for (Player p : plugin.getPlayers()) {
                                        plugin.messagePlayer(p, "§8| §e\uD83D\uDC51 §8| " + plugin.getTeamDisplayName(args[1]) + "§e was §e§l1st §eto finish!");
                                    }
                                    break;
                                case 2:
                                    for (Player p : plugin.getPlayers()) {
                                        plugin.messagePlayer(p, "§8| §7\uD83D\uDC51 §8| " + plugin.getTeamDisplayName(args[1]) + "§e was §7§l2nd §eto finish!");
                                    }
                                    break;
                                case 3:
                                    for (Player p : plugin.getPlayers()) {
                                        plugin.messagePlayer(p, "§8| §6\uD83D\uDC51 §8| " + plugin.getTeamDisplayName(args[1]) + "§e was §6§l3rd §eto finish!");
                                    }
                                    break;
                                default:
                                    for (Player p : plugin.getPlayers()) {
                                        plugin.messagePlayer(p, "§8| §f\uD83D\uDC51 §8| " + plugin.getTeamDisplayName(args[1]) + "§e was §f§l" + placement + "th §eto finish!");
                                    }
                                    break;
                            }

                            int pointsEarned = 284 - (24 * placement);
                            int dividedPointsEarned = pointsEarned / 4;
                            plugin.teamCheckpoints.put(args[1], 6);
                            plugin.slimeFinishers.put(args[1], plugin.runningTimers.get("slimegolf").getValue());

                            for (String player : TeamsConfig.get().getStringList("teams." + args[1] + ".players")) {
                                if (Bukkit.getServer().getPlayer(player) != null) {
                                    Player p = Bukkit.getServer().getPlayer(player);
                                    p.sendTitle("§aFINISH", "§8[§f§l⏱§8] §e§o" + plugin.getTimer("slimegolf"), 0, 100, 5);
                                    plugin.messagePlayer(p, "§e\uD83D\uDCB0" + pointsEarned + " §8| §a§lHole Completed!");
                                    plugin.messagePlayer(p, "§f§l⏱ §8| §fTime Taken: §e" + plugin.getTimer("slimegolf"));
                                    plugin.earnPoints(player, dividedPointsEarned, true);
                                    p.setGameMode(GameMode.SPECTATOR);
                                }
                            }
                            plugin.slimeCheckpoints.replace(plugin.slimeCheckpoints.size(), placement + 1);

                            List<String> teamList = new ArrayList<>();
                            for (Player p : plugin.getPlayers()) {
                                if (!teamList.contains(PlayerConfig.get().getString("players." + p.getName() + ".team"))) {
                                    teamList.add(PlayerConfig.get().getString("players." + p.getName() + ".team"));
                                }
                            }

                            if (placement.equals(teamList.size())) {
                                plugin.runningTimers.remove("slimegolf");
                                plugin.runningTimers.remove("slimegolftimer");
                                plugin.gameEnd();
                            }
                        }
                        if (plugin.runningTimers.containsKey("finale")) {
                            Integer placement = plugin.slimeCheckpoints.get(plugin.slimeCheckpoints.size());
                            for (Player p : plugin.getPlayers()) {
                                plugin.messagePlayer(p, "§8| §e\uD83D\uDC51 §8| " + plugin.getTeamDisplayName(args[1]) + "§e was §e§l1st §eto finish!");
                            }
                            plugin.teamCheckpoints.put(args[1], 6);
                            plugin.slimeFinishers.put(args[1], plugin.runningTimers.get("finale").getValue());

                            for (String player : TeamsConfig.get().getStringList("teams." + args[1] + ".players")) {
                                if (Bukkit.getServer().getPlayer(player) != null) {
                                    Player p = Bukkit.getServer().getPlayer(player);
                                    p.sendTitle("§aFINISH", "§8[§f§l⏱§8] §e§o" + plugin.getTimer("finale"), 0, 100, 5);
                                    plugin.messagePlayer(p, "§a§lHole Completed!");
                                    plugin.messagePlayer(p, "§f§l⏱ §8| §fTime Taken: §e" + plugin.getTimer("finale"));
                                    p.setGameMode(GameMode.SPECTATOR);
                                }
                            }
                            plugin.slimeCheckpoints.replace(plugin.slimeCheckpoints.size(), placement + 1);
                            plugin.runningTimers.remove("slimegolftimer");
                            plugin.runningTimers.remove("finale");
                            plugin.finaleRoundOver(args[1]);
                        }

                    }
                    break;
                case "bbcp":
                    if (args.length > 2) {
                        if(plugin.runningTimers.containsKey("bridgebuilders")) {
                            Integer placement = plugin.bridgeCheckpoints.get(Integer.parseInt(args[1]));
                            if(!plugin.bridgeTally.containsKey(args[2])){
                                plugin.bridgeTally.put(args[2], 0);
                            }
                            plugin.bridgeTally.put(args[2], plugin.bridgeTally.get(args[2]) + 1);
                            plugin.startBridgeJump(args[2], Integer.parseInt(args[1]));
                            if (placement == 1) {
                                for (Player p : plugin.getPlayers()) {
                                    plugin.messagePlayer(p, "§e§l⏱ §8| " + plugin.getTeamDisplayName(args[2]) + "§7 built §a\uD83C\uDF09-" + args[1] + "§7!");
                                }
                            } else {
                                for (Player p : plugin.getPlayers()) {
                                    plugin.messagePlayer(p, "§f§l⏱ §8| " + plugin.getTeamDisplayName(args[2]) + "§7 built §a\uD83C\uDF09-" + args[1] + "§7!");
                                }
                            }

                            String name = args[2] + "build" + args[1];
                            List<BlockDisplay> blocks = new ArrayList<>();

                            int level = Integer.parseInt(args[1]) - 1;

                            int teamIndex = 35;

                            switch(args[2]){
                                case "RubyRaiders": teamIndex *= 0; break;
                                case "AmberAmbushers": break;
                                case "TopazTroopers": teamIndex *= 2; break;
                                case "KyaniteKillers": teamIndex *= 3; break;
                                case "DiamondDestroyers": teamIndex *= 4; break;
                                case "SapphireSoldiers": teamIndex *= 5; break;
                                case "SmithsoniteSlayers": teamIndex *= 6; break;
                                case "CrystalCrashers": teamIndex *= 7; break;
                            }

                            int x = 254 + teamIndex;
                            int z = (678 - (level * 38));

                            BukkitTask task = new BukkitRunnable() {
                                int index = 0;
                                int timeLeft = 56;
                                @Override
                                public void run() {
                                    if(plugin.runningTimers.containsKey(name)) {
                                        if (!plugin.pausedTimers.contains(name)) {
                                            timeLeft--;
                                            plugin.runningTimers.get(name).setValue(timeLeft);
                                            if(index <= 15 && timeLeft % 2 == 0){
                                                summonBridgePiece(index, args[2], blocks);
                                                index++;
                                            }
                                            if (timeLeft == 0) {
                                                for(BlockDisplay bd : blocks){
                                                    bd.remove();
                                                }
                                                Bukkit.getWorld("build").getBlockAt(x-11, -23, z-17).setType(Material.REDSTONE_BLOCK);
                                                Bukkit.getWorld("build").getBlockAt(x-11, -23, z-17).setType(Material.AIR);
                                                plugin.runningTimers.remove(name);
                                                cancel();
                                            }
                                        }
                                    } else {
                                        cancel();
                                    }
                                }

                            }.runTaskTimer(plugin, 0L, 1L);

                            plugin.runningTimers.put(name, new AbstractMap.SimpleEntry<>(task, 21));


//                            int pointsEarned = 51 - placement;
//                            plugin.earnTeamPoints(args[2], pointsEarned);
                            plugin.teamCheckpoints.put(args[2], Integer.parseInt(args[1]));

                            Location teleportLoc = new Location(Bukkit.getWorld("build"), plugin.teamJump.get(args[2])[0]+0.5, plugin.teamJump.get(args[2])[1], (plugin.teamJump.get(args[2])[2])-(38*(Integer.valueOf(args[1])-1))+3);

                            teleportLoc.setYaw(180);

                            for (String player : TeamsConfig.get().getStringList("teams." + args[2] + ".players")) {
                                if (Bukkit.getServer().getPlayer(player) != null) {
                                    Player p = Bukkit.getServer().getPlayer(player);
                                    p.getInventory().clear();
                                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_WORK_FLETCHER, 1f, 1f);
                                    plugin.summonFirework(p.getLocation(), args[2]);
                                    p.sendTitle("§a[✔] \uD83C\uDF09-" + args[1], "§7Now get ready to run!", 0, 40, 0);
                                    plugin.messagePlayer(p, "§a[\uD83D\uDDFB-" + args[1] + "] Build Complete!");
                                    plugin.messagePlayer(p, "§cBuild mode removed, move onto the next build.");
                                    p.setGameMode(GameMode.ADVENTURE);
                                    p.setAllowFlight(false);
                                    p.setFlying(false);

                                    p.teleport(teleportLoc);

                                    for (String player2 : TeamsConfig.get().getStringList("teams." + args[2] + ".players")) {
                                        if (Bukkit.getServer().getPlayer(player2) != null) {
                                            Player p2 = Bukkit.getServer().getPlayer(player2);
                                            if (p.getName().equals(p2.getName())) continue;
                                            p.hidePlayer(p2);
                                            p2.hidePlayer(p);
                                        }
                                    }
                                }
                            }
                            plugin.bridgeCheckpoints.replace(Integer.parseInt(args[1]), placement + 1);

                            int buildTime = plugin.runningTimers.get("bridgebuilders").getValue() - plugin.buildTimeStamps.get(args[2]);

                            plugin.bridgeCourseTimes.computeIfAbsent(args[2], k -> new HashMap<>()).put(Integer.parseInt(args[1]), buildTime);
                        }


                    }
                    break;
                case "bbfcp":
                    if (args.length > 2) {
                        if(plugin.finaleActive) {
                            List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());
                            String team = (Objects.equals(args[2], "leftTeam")) ? leaderteams.getFirst() : leaderteams.get(1);
                            Integer placement = plugin.bridgeCheckpoints.get(Integer.parseInt(args[1]));
                            if(!plugin.bridgeTally.containsKey(team)){
                                plugin.bridgeTally.put(team, 0);
                            }
                            plugin.bridgeTally.put(team, plugin.bridgeTally.get(team) + 1);
                            if (placement == 1) {
                                for (Player p : Bukkit.getOnlinePlayers()) {
                                    if (plugin.ghostManager.getGhostPlayers().contains(p.getName())) {
                                        plugin.messagePlayer(p, "§e§l⏱ §8| " + plugin.getTeamDisplayName(team) + "§7 built §a\uD83C\uDF09-" + args[1] + "§7!");
                                    }
                                }
                            } else {
                                for (Player p : Bukkit.getOnlinePlayers()) {
                                    if (plugin.ghostManager.getGhostPlayers().contains(p.getName())) {
                                        plugin.messagePlayer(p, "§f§l⏱ §8| " + plugin.getTeamDisplayName(team) + "§7 built §a\uD83C\uDF09-" + args[1] + "§7!");
                                    }
                                }
                            }

                            int level = Integer.parseInt(args[1]) - 1;


                            int x = (Objects.equals(args[2], "leftTeam")) ? 2049 : 2084;
                            int z = (900 - (level * 38));

                            Bukkit.getWorld("build").getBlockAt(x, -39, z).setType(Material.REDSTONE_BLOCK);
                            Bukkit.getWorld("build").getBlockAt(x, -39, z).setType(Material.AIR);

                            plugin.teamCheckpoints.put(team, Integer.parseInt(args[1]));

                            x+=4;

                            Location teleportLoc = new Location(Bukkit.getWorld("build"), x, -37, z);

                            teleportLoc.setYaw(180);

                            for (String player : TeamsConfig.get().getStringList("teams." + team + ".players")) {
                                if (Bukkit.getServer().getPlayer(player) != null) {
                                    Player p = Bukkit.getServer().getPlayer(player);
                                    p.getInventory().clear();
                                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_WORK_FLETCHER, 1f, 1f);
                                    plugin.summonFirework(p.getLocation(), team);
                                    p.sendTitle("§a[✔] \uD83C\uDF09-" + args[1], "§7The build has been copied, now run!", 0, 40, 0);
                                    plugin.messagePlayer(p, "§a[\uD83D\uDDFB-" + args[1] + "] Build Complete!");
                                    plugin.messagePlayer(p, "§cBuild mode removed, move onto the next build.");
                                    p.setGameMode(GameMode.ADVENTURE);
                                    p.setAllowFlight(false);
                                    p.setFlying(false);

                                    p.teleport(teleportLoc);
                                }
                            }
                            plugin.bridgeCheckpoints.replace(Integer.parseInt(args[1]), placement + 1);
                        }


                    }
                    break;
                default:
                    break;
                }
            }
        }
        return true;
    }


    public void summonBridgePiece(int index, String team, List<BlockDisplay> blocks) {

        World world = Bukkit.getWorld("build");

        int level = plugin.teamCheckpoints.get(team) - 1;

        int teamIndex = 35;

        switch(team){
            case "RubyRaiders": teamIndex *= 0; break;
            case "AmberAmbushers": break;
            case "TopazTroopers": teamIndex *= 2; break;
            case "KyaniteKillers": teamIndex *= 3; break;
            case "DiamondDestroyers": teamIndex *= 4; break;
            case "SapphireSoldiers": teamIndex *= 5; break;
            case "SmithsoniteSlayers": teamIndex *= 6; break;
            case "CrystalCrashers": teamIndex *= 7; break;
        }

        int x = 254 + teamIndex;
        int z = (678 - (level * 38)) - index;

        List<BlockDisplay> blockList = new ArrayList<>();
        BlockDisplay tempBD;

        for(int i = x; i <= x + 6; i++){
            for(int y = -21; y <= -13; y++){
                if(world.getBlockAt(i, y, z).getType() != Material.AIR){
                    tempBD = (BlockDisplay) world.spawnEntity(new Location(world, i-10, y, z-19), EntityType.BLOCK_DISPLAY);
                    BlockData sourceData = world.getBlockAt(i, y, z).getBlockData();
                    BlockData copiedData = sourceData.clone();
                    tempBD.setBlock(world.getBlockAt(i, y, z).getType().createBlockData());
                    if (copiedData instanceof Directional targetDir && sourceData instanceof Directional sourceDir) {
                        targetDir.setFacing(sourceDir.getFacing());
                    }

                    if (copiedData instanceof Orientable targetOrient && sourceData instanceof Orientable sourceOrient) {
                        targetOrient.setAxis(sourceOrient.getAxis());
                    }

                    if (copiedData instanceof Rotatable targetRot && sourceData instanceof Rotatable sourceRot) {
                        targetRot.setRotation(sourceRot.getRotation());
                    }

                    if (copiedData instanceof MultipleFacing target && sourceData instanceof MultipleFacing source) {
                        for (BlockFace face : target.getAllowedFaces()) {
                            target.setFace(face, source.hasFace(face));
                        }
                    }

                    tempBD.setBlock(copiedData);
                    blockList.add(tempBD);
                    blocks.add(tempBD);
                }
            }
        }

        Vector3f translation = new Vector3f(0.5F, 0.5F, 0.5F);
        Quaternionf leftRotation = new Quaternionf();
        Quaternionf rightRotation = new Quaternionf();
        Vector3f scaleVector = new Vector3f(0.0F, 0.0F, 0.0F);

        Transformation transformation = new Transformation(translation, leftRotation, scaleVector, rightRotation);
        for(BlockDisplay bd : blockList){
            bd.setTransformation(transformation);
        }

        String name = "build" + team + index;

        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 20;

            @Override
            public void run() {
                if (plugin.runningTimers.containsKey(name)) {
                    if (!plugin.pausedTimers.contains(name)) {
                        timeLeft--;
                        plugin.runningTimers.get(name).setValue(timeLeft);

                        // Read current transformation from first BlockDisplay
                        Transformation old = blockList.get(0).getTransformation();

                        // Calculate new scale & translation
                        Vector3f newScale = new Vector3f(
                                old.getScale().x() + 0.05F,
                                old.getScale().y() + 0.05F,
                                old.getScale().z() + 0.05F
                        );

                        Vector3f newTranslation = new Vector3f(
                                old.getTranslation().x() - 0.025F,
                                old.getTranslation().y() - 0.025F,
                                old.getTranslation().z() - 0.025F
                        );

                        // Create new Transformation
                        Transformation newTransformation = new Transformation(
                                newTranslation,
                                old.getLeftRotation(),   // preserve rotation
                                newScale,
                                old.getRightRotation()   // preserve rotation
                        );

                        // Apply new Transformation to all BlockDisplays
                        for (BlockDisplay bd : blockList) {
                            bd.setTransformation(newTransformation);
                        }

                        // Remove timer entry when finished
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

        plugin.runningTimers.put(name, new AbstractMap.SimpleEntry<>(task, 21));
    }

}
