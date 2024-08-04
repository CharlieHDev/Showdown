package me.chazzagram.showdown2.commands;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.PlayerConfig;
import me.chazzagram.showdown2.files.SpectatorConfig;
import me.chazzagram.showdown2.files.TeamsConfig;
import me.chazzagram.showdown2.files.TeleportConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MainCommand implements CommandExecutor {

    private final Showdown2 plugin;

    public MainCommand(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (commandSender instanceof Player p) {
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
                            plugin.startStopwatch(Integer.parseInt(args[1]), "stopwatch");
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
                            plugin.teleportPlayers(TeleportConfig.get().getLocation("players." + args[1]));
                        }
                        break;
                    case "tpt":
                        if(args.length > 1) {
                            plugin.teamTeleport(args[1]);
                        }
                        break;

                    default:
                        plugin.messagePlayer(p, "Missing Args.");
                        break;
                }
            }
        } else if (commandSender instanceof BlockCommandSender){
            if (args.length == 0) {
                plugin.messageConsole("Invalid command.");
            } else {
                switch (args[0].toLowerCase()) {
                    case "slimecp":
                        if (args.length > 2) {
                            Integer placement = plugin.slimeCheckpoints.get(Integer.parseInt(args[1]));
                            switch(placement) {
                                case 1:
                                    for(Player p : plugin.getPlayers()){
                                        plugin.messagePlayer(p, "Team " + args[2] + " was 1st to reach checkpoint " + args[1] + "!");
                                    }
                                    break;
                                case 2:
                                    for(Player p : plugin.getPlayers()){
                                        plugin.messagePlayer(p, "Team " + args[2] + " was 2nd to reach checkpoint " + args[1] + "!");
                                    }
                                    break;
                                case 3:
                                    for(Player p : plugin.getPlayers()){
                                        plugin.messagePlayer(p, "Team " + args[2] + " was 3rd to reach checkpoint " + args[1] + "!");
                                    }
                                    break;
                                default:
                                    for(Player p : plugin.getPlayers()){
                                        plugin.messagePlayer(p, "Team " + args[2] + " was " + placement + "th to reach checkpoint " + args[1] + "!");
                                    }
                                    break;
                            }
                            for(String player : TeamsConfig.get().getStringList("teams." + args[2] + ".players")){
                                if(Bukkit.getServer().getPlayer(player) != null) {
                                    Player p = Bukkit.getServer().getPlayer(player);
                                    p.sendTitle("§a[✔] \uD83D\uDDFB-" + args[1], "§8[§f§l⏱§8] §e§o" + plugin.getTimer("stopwatch"), 0, 100, 5);
                                    plugin.messagePlayer(p, "Checkpoint reached!");
                                }
                            }
                            plugin.slimeCheckpoints.replace(Integer.parseInt(args[1]), placement+1);
                        }
                        break;
                    case "slimefinish":
                        if(args.length > 1) {
                            Integer placement = plugin.slimeCheckpoints.get(plugin.slimeCheckpoints.size());
                            switch(placement) {
                                case 1:
                                    for(Player p : plugin.getPlayers()){
                                        plugin.messagePlayer(p, "Team " + args[1] + " was 1st to finish!");
                                    }
                                    break;
                                case 2:
                                    for(Player p : plugin.getPlayers()){
                                        plugin.messagePlayer(p, "Team " + args[1] + " was 2nd to finish!");
                                    }
                                    break;
                                case 3:
                                    for(Player p : plugin.getPlayers()){
                                        plugin.messagePlayer(p, "Team " + args[1] + " was 3rd to finish!");
                                    }
                                    break;
                                default:
                                    for(Player p : plugin.getPlayers()){
                                        plugin.messagePlayer(p, "Team " + args[1] + " was " + placement + "th to finish!");
                                    }
                                    break;
                            }
                            for(String player : TeamsConfig.get().getStringList("teams." + args[1] + ".players")){
                                if(Bukkit.getServer().getPlayer(player) != null) {
                                    Player p = Bukkit.getServer().getPlayer(player);
                                    p.sendTitle("§aFINISH", "§8[§f§l⏱§8] §e§o" + plugin.getTimer("stopwatch"), 0, 100, 5);
                                }
                            }
                            plugin.slimeCheckpoints.replace(plugin.slimeCheckpoints.size(), placement+1);
                        }
                        break;
                }
            }
        }
        return true;
    }
}
