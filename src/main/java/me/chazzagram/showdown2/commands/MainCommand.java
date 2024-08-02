package me.chazzagram.showdown2.commands;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.PlayerConfig;
import me.chazzagram.showdown2.files.SpectatorConfig;
import me.chazzagram.showdown2.files.TeamsConfig;
import org.bukkit.Bukkit;
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

                    default:
                        plugin.messagePlayer(p, "Missing Args.");
                        break;
                }
            }
        }
        return true;
    }
}
