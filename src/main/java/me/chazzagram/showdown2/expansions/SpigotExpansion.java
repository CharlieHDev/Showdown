package me.chazzagram.showdown2.expansions;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.PlayerConfig;
import me.chazzagram.showdown2.files.SpectatorConfig;
import me.chazzagram.showdown2.files.TeamsConfig;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SpigotExpansion extends PlaceholderExpansion {

    private final Showdown2 plugin;

    public SpigotExpansion(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "mce24";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Chazzagram";
    }

    @Override
    public @NotNull String getVersion() {
        return "0.0.0";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());
        List<Integer> leaderteampoints = new ArrayList<>(plugin.sortByValue().values());
        if(p == null){
            return "";
        }
        switch (params) {
            case "player":
                return p.getName();
            case "team":
                if (SpectatorConfig.get().getStringList("spectators").contains(p.getName())) {
                    return "Spectator";
                } else {
                    String team = PlayerConfig.get().getString("players." + p.getName() + ".team");
                    return Objects.requireNonNullElse(team, "No Team.");
                }
            case "displayteam":
                if (SpectatorConfig.get().getStringList("spectators").contains(p.getName())) {
                    return "Spectator";
                } else {
                    String team = PlayerConfig.get().getString("players." + p.getName() + ".team");
                    return Objects.requireNonNullElse(plugin.getTeamDisplayName(team), "No Team.");
                }
            case "points":
                String points = PlayerConfig.get().getString("players." + p.getName() + ".points");
                return Objects.requireNonNullElse(points, "N/A");
            case "teampoints":
                String selectTeam = PlayerConfig.get().getString("players." + p.getName() + ".team");
                String teampoints = String.valueOf(TeamsConfig.get().getInt("teams." + selectTeam + ".points"));
                if (selectTeam == null) {
                    return "N/A";
                } else {
                    return teampoints;
                }
            case "teamicon":
                if(SpectatorConfig.get().getStringList("spectators").contains(p.getName())){
                    return "SP";
                } else {
                    String playersTeam = PlayerConfig.get().getString("players." + p.getName() + ".team");
                    String teamicon = String.valueOf(TeamsConfig.get().getInt("teams." + playersTeam + ".icon"));
                    if (playersTeam == null) {
                        return "N/A";
                    } else {
                        return teamicon;
                    }
                }
            case "timer_sumo":
                if (plugin.runningTimers.containsKey("sumo")) {
                    return plugin.getTimer("sumo");
                } else {
                    return "Waiting..";
                }
            case "timer_craftalot":
                if (plugin.runningTimers.containsKey("craftalot")) {
                    return plugin.getTimer("craftalot");
                } else {
                    return "Waiting..";
                }
            case "timer_slimegolfstart":
                if (plugin.runningTimers.containsKey("slimegolfstart")) {
                    return plugin.getTimer("slimegolfstart");
                } else {
                    return "Waiting..";
                }
            case "stopwatch":
                if (plugin.runningTimers.containsKey("stopwatch")) {
                    return plugin.getTimer("stopwatch");
                } else {
                    return "Waiting..";
                }
            case "topteam_1":
                if(!leaderteams.isEmpty()) {
                    if(leaderteams.getFirst() != null) {
                        return plugin.getTeamDisplayName(leaderteams.getFirst());
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "toppoints_1":
                if(!leaderteampoints.isEmpty()) {
                    if (leaderteampoints.getFirst() != null) {
                        return "§e§l\uD83D\uDCB0" + leaderteampoints.getFirst();
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "topteam_2":
                if(leaderteams.size() > 1) {
                    if (leaderteams.get(1) != null) {
                        return plugin.getTeamDisplayName(leaderteams.get(1));
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "toppoints_2":
                if(leaderteampoints.size() > 1) {
                    if(leaderteampoints.get(1) != null) {
                        return "§e§l\uD83D\uDCB0" + leaderteampoints.get(1);
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "topteam_3":
                if(leaderteams.size() > 2) {
                    if(leaderteams.get(2) != null) {
                        return plugin.getTeamDisplayName(leaderteams.get(2));
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "toppoints_3":
                if(leaderteampoints.size() > 2) {
                    if(leaderteampoints.get(2) != null) {
                        return "§e§l\uD83D\uDCB0" + leaderteampoints.get(2);
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "topteam_4":
                if(leaderteams.size() > 3) {
                    if(leaderteams.get(3) != null) {
                        return plugin.getTeamDisplayName(leaderteams.get(3));
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "toppoints_4":
                if(leaderteampoints.size() > 3) {
                    if(leaderteampoints.get(3) != null) {
                        return "§e§l\uD83D\uDCB0" + leaderteampoints.get(3);
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "topteam_5":
                if(leaderteams.size() > 4) {
                    if(leaderteams.get(4) != null) {
                        return plugin.getTeamDisplayName(leaderteams.get(4));
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "toppoints_5":
                if(leaderteampoints.size() > 4) {
                    if(leaderteampoints.get(4) != null) {
                        return "§e§l\uD83D\uDCB0" + leaderteampoints.get(4);
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "topteam_6":
                if(leaderteams.size() > 5) {
                    if(leaderteams.get(5) != null) {
                        return plugin.getTeamDisplayName(leaderteams.get(5));
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "toppoints_6":
                if(leaderteampoints.size() > 5) {
                    if(leaderteampoints.get(5) != null) {
                        return "§e§l\uD83D\uDCB0" + leaderteampoints.get(5);
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "topteam_7":
                if(leaderteams.size() > 6) {
                    if(leaderteams.get(6) != null) {
                        return plugin.getTeamDisplayName(leaderteams.get(6));
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "toppoints_7":
                if(leaderteampoints.size() > 6) {
                    if(leaderteampoints.get(6) != null) {
                        return "§e§l\uD83D\uDCB0" + leaderteampoints.get(6);
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "topteam_8":
                if(leaderteams.size() > 7) {
                    if(leaderteams.get(7) != null) {
                        return plugin.getTeamDisplayName(leaderteams.get(7));
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "toppoints_8":
                if(leaderteampoints.size() > 7) {
                    if(leaderteampoints.get(7) != null) {
                        return "§e§l\uD83D\uDCB0" + leaderteampoints.get(7);
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "checkpoints_Team1":
                return plugin.getTeamProgress("TestTeam1").toString();
            default:
                return null;
        }
    }
}
