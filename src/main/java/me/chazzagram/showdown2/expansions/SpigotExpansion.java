package me.chazzagram.showdown2.expansions;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.PlayerConfig;
import me.chazzagram.showdown2.files.SpectatorConfig;
import me.chazzagram.showdown2.files.TeamsConfig;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
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
        List<String> modeteams = new ArrayList<>(plugin.sortMap(plugin.modeTeamPoints).keySet());
        List<Integer> modeteampoints = new ArrayList<>(plugin.sortMap(plugin.modeTeamPoints).values());
        StringBuilder teamplayers = new StringBuilder();
        StringBuilder teamsplayersalive = new StringBuilder();
        int count = 0;
        teamplayers.setLength(0);
        int index = 0;
        if(p == null){
            return "";
        }
        if (params.startsWith("killrecord_")) {
            try {
                int killindex = Integer.parseInt(params.split("_")[1]);

                if (killindex >= 0 && killindex < plugin.killRecord.toArray().length) {
                    return plugin.killRecord.get((plugin.killRecord.toArray().length-killindex)-1);
                } else {
                    return "§7------------";
                }
            } catch (NumberFormatException e) {
                return "Invalid number format!";
            }
        }
        switch (params) {
            case "player":
                return p.getName();
            case "playerdisplay":
                return plugin.getPlayerDisplayName(p.getName());
            case "playerprefix":
                if(PlayerConfig.get().getConfigurationSection("players").getKeys(false).contains(p.getName())) {
                    return TeamsConfig.get().get("teams." + PlayerConfig.get().getString("players." + p.getName() + ".team") + ".colour") + TeamsConfig.get().getString("teams." + PlayerConfig.get().get("players." + p.getName() + ".team") + ".icon");
                } else if (SpectatorConfig.get().getConfigurationSection("spectators").getKeys(false).contains(p.getName())) {
                    return "§7";
                } else {
                    return "§f";
                }
            case "team":
                if (SpectatorConfig.get().getConfigurationSection("spectators").getKeys(false).contains(p.getName())) {
                    return "Spectator";
                } else {
                    String team = PlayerConfig.get().getString("players." + p.getName() + ".team");
                    return Objects.requireNonNullElse(team, "No Team.");
                }
            case "displayteam":
                if (SpectatorConfig.get().getConfigurationSection("spectators").getKeys(false).contains(p.getName())) {
                    return "Spectator";
                } else {
                    String team = PlayerConfig.get().getString("players." + p.getName() + ".team");
                    return Objects.requireNonNullElse(plugin.getTeamDisplayName(team), "No Team.");
                }
            case "points":
                if (SpectatorConfig.get().getStringList("spectators").contains(p.getName())) {
                    String points = SpectatorConfig.get().getString("spectators." + p.getName() + ".points");
                    return Objects.requireNonNullElse(points, "N/A");
                } else {
                    String points = PlayerConfig.get().getString("players." + p.getName() + ".points");
                    return Objects.requireNonNullElse(points, "N/A");
                }
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
            case "teammodepoints":
                if(SpectatorConfig.get().getStringList("spectators").contains(p.getName())){
                    return "N/A";
                } else {
                    String playersTeam = PlayerConfig.get().getString("players." + p.getName() + ".team");
                    if (playersTeam == null) {
                        return "N/A";
                    } else {
                        return plugin.modeTeamPoints.get(playersTeam).toString();
                    }
                }
            case "timer_bridgebuilders":
                if (plugin.runningTimers.containsKey("bridgebuildersstart")) {
                    return plugin.getTimer("bridgebuildersstart");
                } else if (plugin.runningTimers.containsKey("bridgebuilders")){
                    return plugin.getTimer("bridgebuilders");
                } else {
                    return "Waiting..";
                }
            case "timer_colourdash":
                if (plugin.runningTimers.containsKey("colourdashstart")) {
                    return plugin.getTimer("colourdashstart");
                } else if (plugin.runningTimers.containsKey("colourdash")){
                    return plugin.getTimer("colourdash");
                } else {
                    return "Waiting..";
                }
            case "timer_craftalot":
                if (plugin.runningTimers.containsKey("craftalotstart")) {
                    return plugin.getTimer("craftalotstart");
                } else if (plugin.runningTimers.containsKey("craftalot")){
                    return plugin.getTimer("craftalot");
                } else {
                    return "Waiting..";
                }
            case "timer_gubgame":
                if (plugin.runningTimers.containsKey("gubgamestart")) {
                    return plugin.getTimer("gubgamestart");
                } else if (plugin.runningTimers.containsKey("gubgame")){
                    return plugin.getTimer("gubgame");
                } else {
                    return "Waiting..";
                }
            case "timer_slimegolf":
                if (plugin.runningTimers.containsKey("slimegolfstart")) {
                    return plugin.getTimer("slimegolfstart");
                } else if (plugin.runningTimers.containsKey("slimegolf")){
                    return plugin.getTimer("slimegolf");
                } else {
                    return "Waiting..";
                }
            case "timer_survivalgames":
                if (plugin.runningTimers.containsKey("survivalgamesstart")) {
                    return plugin.getTimer("survivalgamesstart");
                } else if (plugin.runningTimers.containsKey("survivalgames")){
                    return plugin.getTimer("survivalgames");
                } else {
                    return "Waiting..";
                }
            case "timer_voting":
                if (plugin.runningTimers.containsKey("voting")) {
                    return plugin.getTimer("voting");
                } else {
                    return "Waiting..";
                }
            case "timer_zoomogo":
                if (plugin.runningTimers.containsKey("zoomogostart")) {
                    return plugin.getTimer("zoomogostart");
                } else if (plugin.runningTimers.containsKey("zoomogo")){
                    return plugin.getTimer("zoomogo");
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
            case "modepoints_1":
                if(modeteampoints.size() > 1) {
                    if(modeteampoints.get(1) != null) {
                        return "§e§l\uD83D\uDCB0" + modeteampoints.get(1);
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "modetop_1":
                if(modeteams.size() > 1) {
                    if(modeteams.get(1) != null) {
                        return plugin.getTeamDisplayName(modeteams.get(1));
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "modepoints_2":
                if(modeteampoints.size() > 2) {
                    if(modeteampoints.get(2) != null) {
                        return "§e§l\uD83D\uDCB0" + modeteampoints.get(2);
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "modetop_2":
                if(modeteams.size() > 2) {
                    if(modeteams.get(2) != null) {
                        return plugin.getTeamDisplayName(modeteams.get(2));
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "modepoints_3":
                if(modeteampoints.size() > 3) {
                    if(modeteampoints.get(3) != null) {
                        return "§e§l\uD83D\uDCB0" + modeteampoints.get(3);
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "modetop_3":
                if(modeteams.size() > 3) {
                    if(modeteams.get(3) != null) {
                        return plugin.getTeamDisplayName(modeteams.get(3));
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "modepoints_4":
                if(modeteampoints.size() > 4) {
                    if(modeteampoints.get(4) != null) {
                        return "§e§l\uD83D\uDCB0" + modeteampoints.get(4);
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "modetop_4":
                if(modeteams.size() > 4) {
                    if(modeteams.get(4) != null) {
                        return plugin.getTeamDisplayName(modeteams.get(4));
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "playersalive":
                if(plugin.currentMode.equals("Zoomo Go")){
                    return String.valueOf(PlayerConfig.get().getConfigurationSection("players").getKeys(false).size() - plugin.deadPlayers.size());
                } else {
                    return "§8N/A";
                }
            case "checkpoints_Team1":
                return plugin.getTeamProgress("RubyRaiders").toString();


            case "teamlist_1":
                teamplayers.append("§0     ");
                for(String player : TeamsConfig.get().getStringList("teams.RubyRaiders.players")){
                    index++;
                    if(Bukkit.getPlayer(player) != null){
                        if(plugin.getPlayerDisplayName(player).length() > 13) {
                            teamplayers.append(plugin.getPlayerDisplayName(Bukkit.getPlayer(player).getName()), 0, 13).append("§0     ");
                        } else {
                            teamplayers.append(plugin.getPlayerDisplayName(Bukkit.getPlayer(player).getName())).append("§0     ");
                        }
                    } else {
                        if(player.length() > 10) {
                            teamplayers.append("§8").append(player, 0, 10).append("§0     ");
                        } else {
                            teamplayers.append("§8").append(player).append("§0     ");
                        }
                    }
                }
                teamplayers.repeat("§8NoPlayer§0     ", 4-index);
                return teamplayers.toString();

            case "teamlist_2":
                teamplayers.append("§0     ");
                for(String player : TeamsConfig.get().getStringList("teams.AmberAmbushers.players")){
                    index++;
                    if(Bukkit.getPlayer(player) != null){
                        if(plugin.getPlayerDisplayName(player).length() > 13) {
                            teamplayers.append(plugin.getPlayerDisplayName(Bukkit.getPlayer(player).getName()), 0, 13).append("§0     ");
                        } else {
                            teamplayers.append(plugin.getPlayerDisplayName(Bukkit.getPlayer(player).getName())).append("§0     ");
                        }
                    } else {
                        if(player.length() > 10) {
                            teamplayers.append("§8").append(player, 0, 10).append("§0     ");
                        } else {
                            teamplayers.append("§8").append(player).append("§0     ");
                        }
                    }
                }
                teamplayers.repeat("§8NoPlayer§0     ", 4-index);
                return teamplayers.toString();

            case "teamlist_3":
                teamplayers.append("§0     ");
                for(String player : TeamsConfig.get().getStringList("teams.TopazTroopers.players")){
                    index++;
                    if(Bukkit.getPlayer(player) != null){
                        if(plugin.getPlayerDisplayName(player).length() > 13) {
                            teamplayers.append(plugin.getPlayerDisplayName(Bukkit.getPlayer(player).getName()), 0, 13).append("§0     ");
                        } else {
                            teamplayers.append(plugin.getPlayerDisplayName(Bukkit.getPlayer(player).getName())).append("§0     ");
                        }
                    } else {
                        if(player.length() > 10) {
                            teamplayers.append("§8").append(player, 0, 10).append("§0     ");
                        } else {
                            teamplayers.append("§8").append(player).append("§0     ");
                        }
                    }
                }
                teamplayers.repeat("§8NoPlayer§0     ", 4-index);
                return teamplayers.toString();

            case "teamlist_4":
                teamplayers.append("§0     ");
                for(String player : TeamsConfig.get().getStringList("teams.KyaniteKillers.players")){
                    index++;
                    if(Bukkit.getPlayer(player) != null){
                        if(plugin.getPlayerDisplayName(player).length() > 13) {
                            teamplayers.append(plugin.getPlayerDisplayName(Bukkit.getPlayer(player).getName()), 0, 13).append("§0     ");
                        } else {
                            teamplayers.append(plugin.getPlayerDisplayName(Bukkit.getPlayer(player).getName())).append("§0     ");
                        }
                    } else {
                        if(player.length() > 10) {
                            teamplayers.append("§8").append(player, 0, 10).append("§0     ");
                        } else {
                            teamplayers.append("§8").append(player).append("§0     ");
                        }
                    }
                }
                teamplayers.repeat("§8NoPlayer§0     ", 4-index);
                return teamplayers.toString();

            case "teamlist_5":
                teamplayers.append("§0     ");
                for(String player : TeamsConfig.get().getStringList("teams.DiamondDestroyers.players")){
                    index++;
                    if(Bukkit.getPlayer(player) != null){
                        if(plugin.getPlayerDisplayName(player).length() > 13) {
                            teamplayers.append(plugin.getPlayerDisplayName(Bukkit.getPlayer(player).getName()), 0, 13).append("§0     ");
                        } else {
                            teamplayers.append(plugin.getPlayerDisplayName(Bukkit.getPlayer(player).getName())).append("§0     ");
                        }
                    } else {
                        if(player.length() > 10) {
                            teamplayers.append("§8").append(player, 0, 10).append("§0     ");
                        } else {
                            teamplayers.append("§8").append(player).append("§0     ");
                        }
                    }
                }
                teamplayers.repeat("§8NoPlayer§0     ", 4-index);
                return teamplayers.toString();

            case "teamlist_6":
                teamplayers.append("§0     ");
                for(String player : TeamsConfig.get().getStringList("teams.SapphireSoldiers.players")){
                    index++;
                    if(Bukkit.getPlayer(player) != null){
                        if(plugin.getPlayerDisplayName(player).length() > 13) {
                            teamplayers.append(plugin.getPlayerDisplayName(Bukkit.getPlayer(player).getName()), 0, 13).append("§0     ");
                        } else {
                            teamplayers.append(plugin.getPlayerDisplayName(Bukkit.getPlayer(player).getName())).append("§0     ");
                        }
                    } else {
                        if(player.length() > 10) {
                            teamplayers.append("§8").append(player, 0, 10).append("§0     ");
                        } else {
                            teamplayers.append("§8").append(player).append("§0     ");
                        }
                    }
                }
                teamplayers.repeat("§8NoPlayer§0     ", 4-index);
                return teamplayers.toString();

            case "teamlist_7":
                teamplayers.append("§0     ");
                for(String player : TeamsConfig.get().getStringList("teams.SmithsoniteSlayers.players")){
                    index++;
                    if(Bukkit.getPlayer(player) != null){
                        if(plugin.getPlayerDisplayName(player).length() > 13) {
                            teamplayers.append(plugin.getPlayerDisplayName(Bukkit.getPlayer(player).getName()), 0, 13).append("§0     ");
                        } else {
                            teamplayers.append(plugin.getPlayerDisplayName(Bukkit.getPlayer(player).getName())).append("§0     ");
                        }
                    } else {
                        if(player.length() > 10) {
                            teamplayers.append("§8").append(player, 0, 10).append("§0     ");
                        } else {
                            teamplayers.append("§8").append(player).append("§0     ");
                        }
                    }
                }
                teamplayers.repeat("§8NoPlayer§0     ", 4-index);
                return teamplayers.toString();

            case "teamlist_8":
                teamplayers.append("§0     ");
                for(String player : TeamsConfig.get().getStringList("teams.CrystalCrashers.players")){
                    index++;
                    if(Bukkit.getPlayer(player) != null){
                        if(plugin.getPlayerDisplayName(player).length() > 13) {
                            teamplayers.append(plugin.getPlayerDisplayName(Bukkit.getPlayer(player).getName()), 0, 13).append("§0     ");
                        } else {
                            teamplayers.append(plugin.getPlayerDisplayName(Bukkit.getPlayer(player).getName())).append("§0     ");
                        }
                    } else {
                        if(player.length() > 10) {
                            teamplayers.append("§8").append(player, 0, 10).append("§0     ");
                        } else {
                            teamplayers.append("§8").append(player).append("§0     ");
                        }
                    }
                }
                teamplayers.repeat("§8NoPlayer§0     ", 4-index);
                return teamplayers.toString();

            case "votedmode":
                return plugin.woolModes.getOrDefault(plugin.playerVote.get(p), "Selecting...");
            case "completions_Team1":
                return plugin.getCompletionProgress("RubyRaiders").toString();
            case "itemtocraft":
                if(plugin.itemToCraft.containsKey(p.getName())){
                    return plugin.itemToCraft.get(p.getName()).replaceAll("_", " ");
                } else {
                    return "§8Waiting..";
                }
            case "currentmode":
                return plugin.currentMode;
            default:
                return null;
        }
    }
}
