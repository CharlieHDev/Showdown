package me.chazzagram.showdown2.commands;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.PlayerConfig;
import me.chazzagram.showdown2.files.SpectatorConfig;
import me.chazzagram.showdown2.files.TeamsConfig;
import me.chazzagram.showdown2.files.TeleportConfig;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements CommandExecutor {

    private final Showdown2 plugin;

    public MainCommand(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

    if (commandSender instanceof ConsoleCommandSender) {
        if (args.length == 0) {
            plugin.messageConsole("Invalid command.");
        } else {
            switch (args[0].toLowerCase()) {
                case "cdcp":
                    if (args.length > 2) {
                        if (!plugin.colourDashCheckpoints.containsKey(args[2])) {
                            plugin.colourDashCheckpoints.put(args[2], 0);
                        }
                        if (plugin.colourDashCheckpoints.get(args[2]) < Integer.parseInt(args[1])) {
                            int placement = 1;
                            for (Integer checkpoint : plugin.colourDashCheckpoints.values()) {
                                if (checkpoint == Integer.parseInt(args[1])) {
                                    placement++;
                                }
                            }
                            int pointsEarned = 41 - placement;
                            plugin.earnPoints(args[2], pointsEarned, true);
                            Player p = Bukkit.getServer().getPlayer(args[2]);
                            p.sendTitle("§a[✔] \uD83C\uDFC3-" + args[1], "§8[§f§l⏱§8] §e§o" + plugin.getTimer("colourdashwatch"), 0, 100, 5);
                            plugin.messagePlayer(p, "§a[\uD83C\uDFC3-" + args[1] + "] Dashpoint reached!");
                            plugin.colourDashCheckpoints.put(args[2], Integer.parseInt(args[1]));
                            for (Player player : plugin.getPlayers()) {
                                plugin.messagePlayer(player, "§8| §a§l⏱ §8| " + plugin.getPlayerDisplayName(p.getName()) + "§7 has reached §a\uD83C\uDFC3-" + args[1] + "§7!");
                            }

                        }
                    }

                    break;
                case "cdfinish":
                    if (args.length > 1) {
                        if (plugin.colourDashCheckpoints.get(args[1]) < 10) {
                            int placement = 1;
                            for (Integer checkpoint : plugin.colourDashCheckpoints.values()) {
                                if (checkpoint == 10) {
                                    placement++;
                                }
                            }
                            int pointsEarned = 102 - (2 * placement);
                            plugin.earnPoints(args[1], pointsEarned, true);

                            Player p = Bukkit.getServer().getPlayer(args[1]);
                            String team = PlayerConfig.get().getString("players." + p.getName() + ".team");
                            plugin.modeCompletions.put(team, (plugin.modeCompletions.get(team) + 1));
                            p.sendTitle("§aFINISH", "§8[§f§l⏱§8] §e§o" + plugin.getTimer("colourdashwatch"), 0, 100, 5);
                            plugin.messagePlayer(p, "§e\uD83D\uDCB0" + pointsEarned + " §8| §a§lCourse Completed!");
                            plugin.messagePlayer(p, "§f§l⏱ §8| §fTime Taken: §e" + plugin.getTimer("colourdashwatch"));
                            plugin.colourDashCheckpoints.put(args[1], 10);
                            p.setGameMode(GameMode.SPECTATOR);
                            for (Player player : plugin.getPlayers()) {
                                plugin.messagePlayer(player, "§8| §f\uD83D\uDC51 §8| " + plugin.getPlayerDisplayName(p.getName()) + "§e was §f§l#" + placement + " §eto finish!");
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
                case "tpt":
                    if(args.length > 1) {
                        plugin.teamTeleport(args[1], 5);
                    }
                    break;
                case "slimefinishers":
                    plugin.slimeGolfTimes();
                    break;
                case "startslimegolf":
                    plugin.startSlimeGolf();
                    break;
                case "startcolourdash":
                    plugin.startColourDash();
                    break;
                case "startcraftalot":
                    plugin.startCraftalot();
                    break;
                case "countvotes":
                    plugin.startVoting();
                    break;
                case "pause":
                    plugin.pauseEvent();
                    break;
                case "unpause":
                    plugin.resumeEvent();
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

                        int pointsEarned = 21 - placement;
                        plugin.earnTeamPoints(args[2], pointsEarned);
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
                    break;
                case "slimefinish":
                    if (args.length > 1) {
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

                        int pointsEarned = 155 - (5 * placement);
                        plugin.earnTeamPoints(args[1], pointsEarned);
                        plugin.teamCheckpoints.put(args[1], 6);
                        plugin.slimeFinishers.put(args[1], plugin.getTimer("slimegolf"));

                        for (String player : TeamsConfig.get().getStringList("teams." + args[1] + ".players")) {
                            if (Bukkit.getServer().getPlayer(player) != null) {
                                Player p = Bukkit.getServer().getPlayer(player);
                                p.sendTitle("§aFINISH", "§8[§f§l⏱§8] §e§o" + plugin.getTimer("slimegolf"), 0, 100, 5);
                                plugin.messagePlayer(p, "§e\uD83D\uDCB0" + pointsEarned + " §8| §a§lHole Completed!");
                                plugin.messagePlayer(p, "§f§l⏱ §8| §fTime Taken: §e" + plugin.getTimer("slimegolf"));
                                p.setGameMode(GameMode.SPECTATOR);
                            }
                        }
                        plugin.slimeCheckpoints.replace(plugin.slimeCheckpoints.size(), placement + 1);
                    }
                    break;
                case "bbcp":
                    if (args.length > 2) {
                        Integer placement = plugin.bridgeCheckpoints.get(Integer.parseInt(args[1]));
                        if (placement == 1) {
                            for (Player p : plugin.getPlayers()) {
                                plugin.messagePlayer(p, "§8| §b★ §8| " + plugin.getTeamDisplayName(args[2]) + "§e was 1st to build §a\uD83C\uDF09-" + args[1] + "§7!");
                            }
                        } else {
                            for (Player p : plugin.getPlayers()) {
                                plugin.messagePlayer(p, "§8| §a§l⏱ §8| " + plugin.getTeamDisplayName(args[2]) + "§7 has built §a\uD83C\uDF09-" + args[1] + "§7!");
                            }
                        }

                        int pointsEarned = 51 - placement;
                        plugin.earnTeamPoints(args[2], pointsEarned);
                        plugin.teamCheckpoints.put(args[2], Integer.parseInt(args[1]));

                        for (String player : TeamsConfig.get().getStringList("teams." + args[2] + ".players")) {
                            if (Bukkit.getServer().getPlayer(player) != null) {
                                Player p = Bukkit.getServer().getPlayer(player);
                                p.sendTitle("§a[✔] \uD83C\uDF09-" + args[1], "§7Now get running!", 0, 40, 0);
                                plugin.messagePlayer(p, "§a[\uD83D\uDDFB-" + args[1] + "] Build Complete!");
                                plugin.messagePlayer(p, "§cCreative removed, move onto the next build.");
                                p.setGameMode(GameMode.ADVENTURE);
                            }
                        }
                        plugin.bridgeCheckpoints.replace(Integer.parseInt(args[1]), placement + 1);
                    }
                    break;
                case "bbfinish":
                    if (args.length > 1) {
                        if (plugin.bridgeFinishers.get(args[1]) < 4) {
                            plugin.bridgeFinishers.replace(args[1], plugin.bridgeFinishers.get(args[1] + 1));
                            StringBuilder playerCompletions = new StringBuilder();
                            playerCompletions.append("§a✔ ".repeat(plugin.bridgeFinishers.get(args[1])));
                            for (String player2 : TeamsConfig.get().getStringList("teams." + args[1] + ".players")) {
                                if (Bukkit.getServer().getPlayer(player2) != null) {
                                    Player p = Bukkit.getServer().getPlayer(player2);
                                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("§eCompletions§f: " + playerCompletions));
                                }
                            }
                        } else {
                            Integer placement = plugin.bridgeCheckpoints.get(plugin.bridgeCheckpoints.size());
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

                            int pointsEarned = 155 - (5 * placement);
                            plugin.earnTeamPoints(args[1], pointsEarned);
                            plugin.teamCheckpoints.put(args[1], 6);

                            for (String player : TeamsConfig.get().getStringList("teams." + args[1] + ".players")) {
                                if (Bukkit.getServer().getPlayer(player) != null) {
                                    Player p = Bukkit.getServer().getPlayer(player);
                                    p.sendTitle("§aFINISH", "", 0, 100, 5);
                                    plugin.messagePlayer(p, "§e\uD83D\uDCB0" + pointsEarned + " §8| §a§lHole Completed!");
                                    p.setGameMode(GameMode.SPECTATOR);
                                }
                            }
                            plugin.bridgeCheckpoints.replace(plugin.bridgeCheckpoints.size(), placement + 1);
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
}
