package me.chazzagram.showdown2.expansions;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.*;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

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
        List<String> craftPlayers = new ArrayList<>(plugin.sortMap(plugin.craftTop).keySet());
        List<Integer> craftPoints = new ArrayList<>(plugin.sortMap(plugin.craftTop).values());
        List<String> gubKillsPlayers = new ArrayList<>(plugin.sortMap(plugin.gubGameKills).keySet());
        List<Integer> gubKillsCount = new ArrayList<>(plugin.sortMap(plugin.gubGameKills).values());
        List<String> indivNames = new ArrayList<>(plugin.getSortedIndivs().keySet());
        List<Integer> indivPoints = new ArrayList<>(plugin.getSortedIndivs().values());
        StringBuilder teamplayers = new StringBuilder();
        StringBuilder teamsplayersalive = new StringBuilder();
        int position = 1;

        if(plugin.getPlayers().contains(p)) {
            int idx = leaderteams.indexOf(PlayerConfig.get().getString("players." + p.getName() + ".team"));
            if(idx >= 0) position = idx + 1;
        }
        int count = 0;
        teamplayers.setLength(0);
        int index = 0;
        if(p == null){
            return "";
        }
        if (params.startsWith("indivplayer_")) {
            try {
                int number = Integer.parseInt(params.substring("indivplayer_".length()))-1;
                int width = 126;
                if(number > 8){
                    width-=6;
                }
                if (number >= 0 && number < indivNames.size()) {
                    for(boolean truefalse : plugin.teamShown){
                        if(!truefalse){
                            return formatLine("§8§k00000000", "§e§l\uD83D\uDCB0" + "§8§k0000", width);

                        }
                    }
                    return formatLine(plugin.getPlayerDisplayName(indivNames.get(number)), "§e§l\uD83D\uDCB0" + indivPoints.get(number).toString(), width);
                } else {
                    return formatLine("§7N/A", "§7N/A", width);

                }
            } catch (NumberFormatException e) {
                return formatLine("§7I/N", "§7I/N", 126);
            }
        }
        if (params.startsWith("indivpoints_")) {
            try {
                int number = Integer.parseInt(params.substring("indivpoints_".length()))-1;
                if (number >= 0 && number < indivPoints.size()) {
                    for(boolean truefalse : plugin.teamShown){
                        if(!truefalse){
                            return "§8§k0000";
                        }
                    }
                    return "§e§l\uD83D\uDCB0" + indivPoints.get(number).toString();
                } else {
                    return "§7N/A";
                }
            } catch (NumberFormatException e) {
                return "§7I/N";
            }
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
        if(params.startsWith("placementchange_")){
            try {
                int number = Integer.parseInt(params.substring("placementchange_".length()))-1;
                if (number >= 0 && number < indivNames.size()) {
                    String name = indivNames.get(number);
                    Integer current = plugin.currentPlacements.get(name);
                    Integer previous = plugin.previousPlacements.get(name);
                    if (current != null && previous != null) {
                        for(boolean truefalse : plugin.teamShown){
                            if(!truefalse){
                                return "§8§k0";
                            }
                        }
                        if (current < previous) {
                            return "§b§l▲";
                        } else if (current > previous) {
                            return "§6§l▼";
                        } else {
                            return "§f§l-";
                        }
                    } else {
                        return "§f§l-";
                    }
                } else {
                    return "§f§l-";
                }
            } catch (NumberFormatException e) {
                return "Invalid number format!";
            }

        }
        switch (params) {
            case "player":
                return p.getName();
            case "bestgame":
                if(PlayerInfoConfig.get().getConfigurationSection("players").getKeys(false).contains(p.getName())){
                    return PlayerInfoConfig.get().getString("players."+p.getName()+".bestgame");
                } else {
                    return "N/A";
                }
            case "playersonline":
                return plugin.getPlayers().size() + "/" + PlayerConfig.get().getConfigurationSection("players").getKeys(false).size();
            case "kills":
                if(PlayerInfoConfig.get().getConfigurationSection("players").getKeys(false).contains(p.getName())){
                    return PlayerInfoConfig.get().getString("players."+p.getName()+".kills");
                } else {
                    return "N/A";
                }
            case "bestreadycheck":
                if(PlayerInfoConfig.get().getConfigurationSection("players").getKeys(false).contains(p.getName())){
                    return PlayerInfoConfig.get().getString("players."+p.getName()+".bestreadycheck");
                } else {
                    return "N/A";
                }
            case "highestplacement":
                if(PlayerInfoConfig.get().getConfigurationSection("players").getKeys(false).contains(p.getName())){
                    return PlayerInfoConfig.get().getString("players."+p.getName()+".highestplacement");
                } else {
                    return "N/A";
                }
            case "eventplacement":
                if(PlayerConfig.get().getConfigurationSection("players").getKeys(false).contains(p.getName())){
                    for(int i = 0; i < indivNames.size(); i++){
                        if(p.getName().equals(indivNames.get(i))){
                            return Integer.toString(i+1);
                        }
                    }
                    return "N/A";
                } else {
                    return "N/A";
                }
            case "playerdisplay":
                return plugin.getPlayerDisplayName(p.getName());
            case "zoomolives":
                StringBuilder livesCount = new StringBuilder();
                int lives = plugin.zoomoLives.get(p.getName());
                if(lives > 0){
                    return livesCount.append("§c❤".repeat(lives)).toString();
                } else {
                    return "§f☠";
                }
            case "playerprefix":
                if(PlayerConfig.get().getConfigurationSection("players").getKeys(false).contains(p.getName())) {
                    return TeamsConfig.get().get("teams." + PlayerConfig.get().getString("players." + p.getName() + ".team") + ".colour") + TeamsConfig.get().getString("teams." + PlayerConfig.get().get("players." + p.getName() + ".team") + ".icon");
                } else if (SpectatorConfig.get().getConfigurationSection("spectators").getKeys(false).contains(p.getName())) {
                    return "§7§l\uD83D\uDD27§7";
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
                    for(boolean truefalse : plugin.teamShown){
                        if(!truefalse){
                            return "N/A";
                        }
                    }
                    return Objects.requireNonNullElse(points, "N/A");
                }
            case "teampoints":
                String selectTeam = PlayerConfig.get().getString("players." + p.getName() + ".team");
                String teampoints = String.valueOf(TeamsConfig.get().getInt("teams." + selectTeam + ".points"));
                for(boolean truefalse : plugin.teamShown){
                    if(!truefalse){
                        return "N/A";
                    }
                }
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
            case "roundcount":
                return String.valueOf(plugin.currentRound);
            case "killcount":
                if(plugin.playerKillCount.containsKey(p.getName())) {
                    return String.valueOf(plugin.playerKillCount.get(p.getName()));
                } else {
                    return "0";
                }
            case "timer_bridgebuilders":
                if (plugin.runningTimers.containsKey("bridgebuildersstart")) {
                    return plugin.getTimer("bridgebuildersstart");
                } else if (plugin.runningTimers.containsKey("bridgebuilders")){
                    return plugin.getTimer("bridgebuilders");
                } else {
                    return "Waiting..";
                }
            case "timer_pushpoint":
                if (plugin.runningTimers.containsKey("pushpointstart")) {
                    return plugin.getTimer("pushpointstart");
                } else if (plugin.runningTimers.containsKey("pushpoint")){
                    return plugin.getTimer("pushpoint");
                } else {
                    return "Waiting..";
                }
            case "timer_crumbleclash":
                if (plugin.runningTimers.containsKey("crumbleclashstart")) {
                    return plugin.getTimer("crumbleclashstart");
                } else if (plugin.runningTimers.containsKey("crumbleclash")){
                    return plugin.getTimer("crumbleclash");
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
            case "timer_dimensiondash":
                if (plugin.runningTimers.containsKey("dimensiondashstart")) {
                    return plugin.getTimer("dimensiondashstart");
                } else if (plugin.runningTimers.containsKey("dimensiondash")){
                    return plugin.getTimer("dimensiondash");
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
                } else if (plugin.runningTimers.containsKey("slimegolftimer")){
                    return plugin.getTimer("slimegolftimer");
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
            case "spleefround":
                return plugin.currentSpleef;
            case "pp_opponent":
                if(plugin.ppTeamMatchups.containsKey(PlayerConfig.get().getString("players." + p.getName() + ".team"))){
                    return plugin.getTeamDisplayName(plugin.ppTeamMatchups.get(PlayerConfig.get().getString("players." + p.getName() + ".team")));
                } else {
                    for (Map.Entry<String, String> entry : plugin.ppTeamMatchups.entrySet()) {
                        if (entry.getValue().equals(PlayerConfig.get().getString("players." + p.getName() + ".team"))) {
                            return plugin.getTeamDisplayName(entry.getKey());
                        }
                    }
                    return "Waiting..";
                }
            case "pp_team":
                return plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + p.getName() + ".team"));
            case "pp_opponent_standings":
                if(plugin.ppTeamMatchups.isEmpty()){
                    return "§6§lᴡɪɴ§a§l 0§f§l |§c§l 0§6§l ʟᴏss";
                } else {
                    if (plugin.ppTeamMatchups.containsKey(PlayerConfig.get().getString("players." + p.getName() + ".team"))) {
                        String opponent = plugin.ppTeamMatchups.get(PlayerConfig.get().getString("players." + p.getName() + ".team"));
                        return "§6§lᴡɪɴ§a§l " + plugin.ppTeamStandings.get(opponent).getFirst() + "§f§l |§c§l " + plugin.ppTeamStandings.get(opponent).get(1) + "§6§l ʟᴏss";
                    } else {
                        for (Map.Entry<String, String> entry : plugin.ppTeamMatchups.entrySet()) {
                            if (entry.getValue().equals(PlayerConfig.get().getString("players." + p.getName() + ".team"))) {
                                return "§6§lᴡɪɴ§a§l " + plugin.ppTeamStandings.get(PlayerConfig.get().getString("players." + p.getName() + ".team")).getFirst() + "§f§l |§c§l " + plugin.ppTeamStandings.get(PlayerConfig.get().getString("players." + p.getName() + ".team")).get(1) + "§6§l ʟᴏss";
                            }
                        }
                        return "Waiting..";
                    }
                }
            case "pp_team_standings":
                if(plugin.ppTeamMatchups.isEmpty()){
                    return "§6§lᴡɪɴ§a§l 0§f§l |§c§l 0§6§l ʟᴏss";
                } else {
                    return "§6§lᴡɪɴ§a§l " + plugin.ppTeamStandings.get(PlayerConfig.get().getString("players." + p.getName() + ".team")).getFirst() + "§f§l |§c§l " + plugin.ppTeamStandings.get(PlayerConfig.get().getString("players." + p.getName() + ".team")).get(1) + "§6§l ʟᴏss";
                }
            case "topteam_1":
                return getTopTeamLine(1, position, p);

            case "topteam_2":
                return getTopTeamLine(2, position, p);

            case "topteam_3":
                return getTopTeamLine(3, position, p);

            case "topteam_4":
                return getTopTeamLine(4, position, p);
            case "topboardteam_1":
                if(!leaderteams.isEmpty() && leaderteams.getFirst() != null && plugin.teamShown[0]) {
                    return formatLine(plugin.getTeamDisplayName(leaderteams.getFirst()), "§e§l\uD83D\uDCB0" + leaderteampoints.getFirst(), 150);
                } else if(!leaderteams.isEmpty()) {
                    return formatLine("§8§k00000000000000000000", "§8§l\uD83D\uDCB0§8§k00000", 150);
                } else {
                    return "§8N/A";
                }

            case "topboardteam_2":
                if(leaderteams.size() > 1 && leaderteams.get(1) != null && plugin.teamShown[1]) {
                    return formatLine(plugin.getTeamDisplayName(leaderteams.get(1)), "§e§l\uD83D\uDCB0" + leaderteampoints.get(1), 150);
                } else if(leaderteams.size() > 1) {
                    return formatLine("§8§k00000000000000000000", "§8§l\uD83D\uDCB0§8§k00000", 150);
                } else {
                    return "§8N/A";
                }

            case "topboardteam_3":
                if(leaderteams.size() > 2 && leaderteams.get(2) != null && plugin.teamShown[2]) {
                    return formatLine(plugin.getTeamDisplayName(leaderteams.get(2)), "§e§l\uD83D\uDCB0" + leaderteampoints.get(2), 150);
                } else if(leaderteams.size() > 2) {
                    return formatLine("§8§k00000000000000000000", "§8§l\uD83D\uDCB0§8§k00000", 150);
                } else {
                    return "§8N/A";
                }

            case "topboardteam_4":
                if(leaderteams.size() > 3 && leaderteams.get(3) != null && plugin.teamShown[3]) {
                    return formatLine(plugin.getTeamDisplayName(leaderteams.get(3)), "§e§l\uD83D\uDCB0" + leaderteampoints.get(3), 150);
                } else if(leaderteams.size() > 3) {
                    return formatLine("§8§k00000000000000000000", "§8§l\uD83D\uDCB0§8§k00000", 150);
                } else {
                    return "§8N/A";
                }

            case "topboardteam_5":
                if(leaderteams.size() > 4 && leaderteams.get(4) != null && plugin.teamShown[4]) {
                    return formatLine(plugin.getTeamDisplayName(leaderteams.get(4)), "§e§l\uD83D\uDCB0" + leaderteampoints.get(4), 150);
                } else if(leaderteams.size() > 4) {
                    return formatLine("§8§k00000000000000000000", "§8§l\uD83D\uDCB0§8§k00000", 150);
                } else {
                    return "§8N/A";
                }

            case "topboardteam_6":
                if(leaderteams.size() > 5 && leaderteams.get(5) != null && plugin.teamShown[5]) {
                    return formatLine(plugin.getTeamDisplayName(leaderteams.get(5)), "§e§l\uD83D\uDCB0" + leaderteampoints.get(5), 150);
                } else if(leaderteams.size() > 5) {
                    return formatLine("§8§k00000000000000000000", "§8§l\uD83D\uDCB0§8§k00000", 150);
                } else {
                    return "§8N/A";
                }

            case "topboardteam_7":
                if(leaderteams.size() > 6 && leaderteams.get(6) != null && plugin.teamShown[6]) {
                    return formatLine(plugin.getTeamDisplayName(leaderteams.get(6)), "§e§l\uD83D\uDCB0" + leaderteampoints.get(6), 150);
                } else if(leaderteams.size() > 6) {
                    return formatLine("§8§k00000000000000000000", "§8§l\uD83D\uDCB0§8§k00000", 150);
                } else {
                    return "§8N/A";
                }

            case "topboardteam_8":
                if(leaderteams.size() > 7 && leaderteams.get(7) != null && plugin.teamShown[7]) {
                    return formatLine(plugin.getTeamDisplayName(leaderteams.get(7)), "§e§l\uD83D\uDCB0" + leaderteampoints.get(7), 150);
                } else if(leaderteams.size() > 7) {
                    return formatLine("§8§k00000000000000000000", "§8§l\uD83D\uDCB0§8§k00000", 150);
                } else {
                    return "§8N/A";
                }

            case "modepoints_1":
                if(!modeteampoints.isEmpty()) {
                    if(modeteampoints.getFirst() != null) {
                        for(boolean truefalse : plugin.teamShown){
                            if(!truefalse){
                                return "§8§l\uD83D\uDCB0§8§k0000";
                            }
                        }
                        return "§e§l\uD83D\uDCB0" + modeteampoints.getFirst();
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "modetop_1":
                if(!modeteams.isEmpty()) {
                    if(modeteams.getFirst() != null) {
                        for(boolean truefalse : plugin.teamShown){
                            if(!truefalse){
                                return "§8§k0000";
                            }
                        }
                        return plugin.getTeamDisplayName(modeteams.getFirst());
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "modepoints_2":
                if(modeteampoints.size() > 1) {
                    if(modeteampoints.get(1) != null) {
                        for(boolean truefalse : plugin.teamShown){
                            if(!truefalse){
                                return "§8§l\uD83D\uDCB0§8§k0000";
                            }
                        }
                        return "§e§l\uD83D\uDCB0" + modeteampoints.get(1);
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "modetop_2":
                if(modeteams.size() > 1) {
                    if(modeteams.get(1) != null) {
                        for(boolean truefalse : plugin.teamShown){
                            if(!truefalse){
                                return "§8§k0000";
                            }
                        }
                        return plugin.getTeamDisplayName(modeteams.get(1));
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "modepoints_3":
                if(modeteampoints.size() > 2) {
                    if(modeteampoints.get(2) != null) {
                        for(boolean truefalse : plugin.teamShown){
                            if(!truefalse){
                                return "§8§l\uD83D\uDCB0§8§k0000";
                            }
                        }
                        return "§e§l\uD83D\uDCB0" + modeteampoints.get(2);
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "modetop_3":
                if(modeteams.size() > 2) {
                    if(modeteams.get(2) != null) {
                        for(boolean truefalse : plugin.teamShown){
                            if(!truefalse){
                                return "§8§k0000";
                            }
                        }
                        return plugin.getTeamDisplayName(modeteams.get(2));
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "modepoints_4":
                if(modeteampoints.size() > 3) {
                    if(modeteampoints.get(3) != null) {
                        for(boolean truefalse : plugin.teamShown){
                            if(!truefalse){
                                return "§8§l\uD83D\uDCB0§8§k0000";
                            }
                        }
                        return "§e§l\uD83D\uDCB0" + modeteampoints.get(3);
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "modetop_4":
                if(modeteams.size() > 3) {
                    if(modeteams.get(3) != null) {
                        for(boolean truefalse : plugin.teamShown){
                            if(!truefalse){
                                return "§8§k0000";
                            }
                        }
                        return plugin.getTeamDisplayName(modeteams.get(3));
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "playersalive":
                if(plugin.currentMode.equals("Zoomo Go") || plugin.currentMode.equals("Survival Games")){
                    return String.valueOf(PlayerConfig.get().getConfigurationSection("players").getKeys(false).size() - plugin.deadPlayers.size());
                } else {
                    return "§8N/A";
                }
            case "checkpoints_Team1":
                return plugin.getTeamProgress("RubyRaiders").toString();


            case "teamlist_1":
                teamplayers.append("§r§0     ");
                for(String player : TeamsConfig.get().getStringList("teams.RubyRaiders.players")){
                    index++;
                    if(Bukkit.getPlayer(player) != null){
                        if(plugin.deadPlayers.contains(player)) {
                            teamplayers.append("§8§m" + TeamsConfig.get().getString("teams.RubyRaiders.icon") + player).append("§r§0     ");
                        } else {
                            teamplayers.append(plugin.getPlayerDisplayName(player)).append("§r§0     ");
                        }
                    } else {
                        teamplayers.append("§8").append(player).append("§r§0     ");
                    }
                }
                teamplayers.repeat("§8NoPlayer§0     ", TeamsConfig.get().getStringList("teams.RubyRaiders.players").size() - index);
                return teamplayers.toString();

            case "teamlist_2":
                teamplayers.append("§r§0     ");
                for (String player : TeamsConfig.get().getStringList("teams.AmberAmbushers.players")) {
                    index++;
                    if (Bukkit.getPlayer(player) != null) {
                        if (plugin.deadPlayers.contains(player)) {
                            teamplayers.append("§8§m" + TeamsConfig.get().getString("teams.AmberAmbushers.icon") + Bukkit.getPlayer(player).getName()).append("§r§0     ");
                        } else {
                            teamplayers.append(plugin.getPlayerDisplayName(player)).append("§r§0     ");
                        }
                    } else {
                        teamplayers.append("§8").append(player).append("§r§0     ");
                    }
                }
                teamplayers.repeat("§8NoPlayer§0     ", TeamsConfig.get().getStringList("teams.AmberAmbushers.players").size() - index);
                return teamplayers.toString();

            case "teamlist_3":
                teamplayers.append("§r§0     ");
                for (String player : TeamsConfig.get().getStringList("teams.TopazTroopers.players")) {
                    index++;
                    if (Bukkit.getPlayer(player) != null) {
                        if (plugin.deadPlayers.contains(player)) {
                            teamplayers.append("§8§m" + TeamsConfig.get().getString("teams.TopazTroopers.icon") + Bukkit.getPlayer(player).getName()).append("§r§0     ");
                        } else {
                            teamplayers.append(plugin.getPlayerDisplayName(player)).append("§r§0     ");
                        }
                    } else {
                        teamplayers.append("§8").append(player).append("§r§0     ");
                    }
                }
                teamplayers.repeat("§8NoPlayer§0     ", TeamsConfig.get().getStringList("teams.TopazTroopers.players").size() - index);
                return teamplayers.toString();

            case "teamlist_4":
                teamplayers.append("§r§0     ");
                for (String player : TeamsConfig.get().getStringList("teams.KyaniteKillers.players")) {
                    index++;
                    if (Bukkit.getPlayer(player) != null) {
                        if (plugin.deadPlayers.contains(player)) {
                            teamplayers.append("§8§m" + TeamsConfig.get().getString("teams.KyaniteKillers.icon") + Bukkit.getPlayer(player).getName()).append("§r§0     ");
                        } else {
                            teamplayers.append(plugin.getPlayerDisplayName(player)).append("§r§0     ");
                        }
                    } else {
                        teamplayers.append("§8").append(player).append("§r§0     ");
                    }
                }
                teamplayers.repeat("§8NoPlayer§0     ", TeamsConfig.get().getStringList("teams.KyaniteKillers.players").size() - index);
                return teamplayers.toString();

            case "teamlist_5":
                teamplayers.append("§r§0     ");
                for (String player : TeamsConfig.get().getStringList("teams.DiamondDestroyers.players")) {
                    index++;
                    if (Bukkit.getPlayer(player) != null) {
                            if (plugin.deadPlayers.contains(player)) {
                                teamplayers.append("§8§m" + TeamsConfig.get().getString("teams.DiamondDestroyers.icon") + Bukkit.getPlayer(player).getName()).append("§r§0     ");
                            } else {
                                teamplayers.append(plugin.getPlayerDisplayName(player)).append("§r§0     ");
                            }
                    } else {
                        teamplayers.append("§8").append(player).append("§r§0     ");
                    }
                }
                teamplayers.repeat("§8NoPlayer§0     ", TeamsConfig.get().getStringList("teams.DiamondDestroyers.players").size() - index);
                return teamplayers.toString();

            case "teamlist_6":
                teamplayers.append("§r§0     ");
                for (String player : TeamsConfig.get().getStringList("teams.SapphireSoldiers.players")) {
                    index++;
                    if (Bukkit.getPlayer(player) != null) {
                        if (plugin.deadPlayers.contains(player)) {
                            teamplayers.append("§8§m" + TeamsConfig.get().getString("teams.SapphireSoldiers.icon") + Bukkit.getPlayer(player).getName()).append("§r§0     ");
                        } else {
                            teamplayers.append(plugin.getPlayerDisplayName(player)).append("§r§0     ");
                        }
                    } else {
                        teamplayers.append("§8").append(player).append("§r§0     ");
                    }
                }
                teamplayers.repeat("§8NoPlayer§0     ", TeamsConfig.get().getStringList("teams.SapphireSoldiers.players").size() - index);
                return teamplayers.toString();

            case "teamlist_7":
                teamplayers.append("§r§0     ");
                for (String player : TeamsConfig.get().getStringList("teams.SmithsoniteSlayers.players")) {
                    index++;
                    if (Bukkit.getPlayer(player) != null) {
                        if (plugin.deadPlayers.contains(player)) {
                            teamplayers.append("§8§m" + TeamsConfig.get().getString("teams.SmithsoniteSlayers.icon") + Bukkit.getPlayer(player).getName()).append("§r§0     ");
                        } else {
                            teamplayers.append(plugin.getPlayerDisplayName(player)).append("§r§0     ");
                        }
                    } else {
                        teamplayers.append("§8").append(player).append("§r§0     ");
                    }
                }
                teamplayers.repeat("§8NoPlayer§0     ", TeamsConfig.get().getStringList("teams.SmithsoniteSlayers.players").size() - index);
                return teamplayers.toString();

            case "teamlist_8":
                teamplayers.append("§r§0     ");
                for (String player : TeamsConfig.get().getStringList("teams.CrystalCrashers.players")) {
                    index++;
                    if (Bukkit.getPlayer(player) != null) {
                        if (plugin.deadPlayers.contains(player)) {
                            teamplayers.append("§8§m" + TeamsConfig.get().getString("teams.CrystalCrashers.icon") + Bukkit.getPlayer(player).getName()).append("§r§0     ");
                        } else {
                            teamplayers.append(plugin.getPlayerDisplayName(player)).append("§r§0     ");
                        }
                    } else {
                        teamplayers.append("§8").append(player).append("§r§0     ");
                    }
                }
                teamplayers.repeat("§8NoPlayer§0     ", TeamsConfig.get().getStringList("teams.CrystalCrashers.players").size() - index);
                return teamplayers.toString();


            case "timermessage":
                return plugin.timerMessage;

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
            case "itemtocraft_easy":
                StringBuilder easyCraft = new StringBuilder();
                easyCraft.append("§a◆ ");
                if(plugin.itemsToCraft.containsKey(p.getName()) && !plugin.itemsToCraft.get(p.getName()).isEmpty()){
                    easyCraft.append(plugin.toPrettyCase(plugin.itemsToCraft.get(p.getName()).getFirst()));
                }
                return easyCraft.toString();
            case "itemtocraft_medium":
                StringBuilder mediumCraft = new StringBuilder();
                mediumCraft.append("§6◆ ");
                if(plugin.itemsToCraft.containsKey(p.getName()) && !plugin.itemsToCraft.get(p.getName()).isEmpty()){
                    mediumCraft.append(plugin.toPrettyCase(plugin.itemsToCraft.get(p.getName()).get(1)));
                }
                return mediumCraft.toString();
            case "itemtocraft_hard":
                StringBuilder hardCraft = new StringBuilder();
                hardCraft.append("§c◆ ");
                if(plugin.itemsToCraft.containsKey(p.getName()) && !plugin.itemsToCraft.get(p.getName()).isEmpty()){
                    hardCraft.append(plugin.toPrettyCase(plugin.itemsToCraft.get(p.getName()).get(2)));
                }
                return hardCraft.toString();
            case "totalcrafts":
                String crafts;
                if(plugin.playerCrafts.containsKey(p.getName())){
                    List<Integer> playerCraftScores = plugin.playerCrafts.get(p.getName());
                    crafts = "§a◆" + playerCraftScores.getFirst() + "§8 | §6◆" + playerCraftScores.get(1) + "§8 | §c◆" + playerCraftScores.get(2);
                } else {
                    crafts = "§a◆0§8 | §6◆0§8 | §c◆0";
                }
                return crafts;
            case "topcrafteritem_1":
                if(!craftPoints.isEmpty()) {
                    if(craftPoints.getFirst() != null) {
                        return String.valueOf(craftPoints.getFirst());
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "topcraftername_1":
                if(!craftPlayers.isEmpty()) {
                    if(craftPlayers.getFirst() != null) {
                        return plugin.getPlayerDisplayName(craftPlayers.getFirst());
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "topcrafteritem_2":
                if(craftPoints.size() > 1) {
                    if(craftPoints.get(1) != null) {
                        return String.valueOf(craftPoints.get(1));
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "topcraftername_2":
                if(craftPlayers.size() > 1) {
                    if(craftPlayers.get(1) != null) {
                        return plugin.getPlayerDisplayName(craftPlayers.get(1));
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "topcrafteritem_3":
                if(craftPoints.size() > 2) {
                    if(craftPoints.get(2) != null) {
                        return String.valueOf(craftPoints.get(2));
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "topcraftername_3":
                if(craftPlayers.size() > 2) {
                    if(craftPlayers.get(2) != null) {
                        return plugin.getPlayerDisplayName(craftPlayers.get(2));
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "topcrafteritem_4":
                if(craftPoints.size() > 3) {
                    if(craftPoints.get(3) != null) {
                        return String.valueOf(craftPoints.get(3));
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "topcraftername_4":
                if(craftPlayers.size() > 3) {
                    if(craftPlayers.get(3) != null) {
                        return plugin.getPlayerDisplayName(craftPlayers.get(3));
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }

            case "gubkit":
                if(plugin.currentMode.equals("Gub Game")){
                    if(plugin.gubGameKills.containsKey(p.getName())){
                        return plugin.gubGameKills.get(p.getName()).toString() + "/14";
                    } else {
                        return "0/14";
                    }
                } else {
                    return "§8N/A";
                }

            case "gubkillscount_1":
                if(!gubKillsCount.isEmpty()) {
                    if(gubKillsCount.getFirst() != null) {
                        return String.valueOf(gubKillsCount.getFirst());
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "gubkillsname_1":
                if(!gubKillsPlayers.isEmpty()) {
                    if(gubKillsPlayers.getFirst() != null) {
                        return plugin.getPlayerDisplayName(gubKillsPlayers.getFirst());
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "gubkillscount_2":
                if(gubKillsCount.size() > 1) {
                    if(gubKillsCount.get(1) != null) {
                        return String.valueOf(gubKillsCount.get(1));
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "gubkillsname_2":
                if(gubKillsPlayers.size() > 1) {
                    if(gubKillsPlayers.get(1) != null) {
                        return plugin.getPlayerDisplayName(gubKillsPlayers.get(1));
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "gubkillscount_3":
                if(gubKillsCount.size() > 2) {
                    if(gubKillsCount.get(2) != null) {
                        return String.valueOf(gubKillsCount.get(2));
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "gubkillsname_3":
                if(gubKillsPlayers.size() > 2) {
                    if(gubKillsPlayers.get(2) != null) {
                        return plugin.getPlayerDisplayName(gubKillsPlayers.get(2));
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "gubkillscount_4":
                if(gubKillsCount.size() > 3) {
                    if(gubKillsCount.get(3) != null) {
                        return String.valueOf(gubKillsCount.get(3));
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }
            case "gubkillsname_4":
                if(gubKillsPlayers.size() > 3) {
                    if(gubKillsPlayers.get(3) != null) {
                        return plugin.getPlayerDisplayName(gubKillsPlayers.get(3));
                    } else {
                        return "§8N/A";
                    }
                } else {
                    return "§8N/A";
                }

            case "bordersize":
                if(plugin.currentMode.equals("Survival Games")){
                    return String.valueOf(plugin.currentBorderRadius);
                } else {
                    return "§8N/A";
                }

            case "bridge_ruby":
                if(plugin.currentMode.equals("Bridge Builders")){
                    StringBuilder tally = new StringBuilder();
                    if(plugin.bridgeTally.containsKey("RubyRaiders")) {
                        tally.append("§c§l✔".repeat(plugin.bridgeTally.get("RubyRaiders")));
                        tally.append("§8§l✔".repeat(12 - plugin.bridgeTally.get("RubyRaiders")));
                    } else {
                        tally.append("§8§l✔".repeat(12));
                    }
                    return tally.toString();
                } else {
                    return "§8N/A";
                }
            case "bridge_amber":
                if(plugin.currentMode.equals("Bridge Builders")){
                    StringBuilder tally = new StringBuilder();
                    if(plugin.bridgeTally.containsKey("AmberAmbushers")) {
                        tally.append("§6§l✔".repeat(plugin.bridgeTally.get("AmberAmbushers")));
                        tally.append("§8§l✔".repeat(12 - plugin.bridgeTally.get("AmberAmbushers")));
                    } else {
                        tally.append("§8§l✔".repeat(12));
                    }
                    return tally.toString();
                } else {
                    return "§8N/A";
                }
            case "bridge_topaz":
                if(plugin.currentMode.equals("Bridge Builders")){
                    StringBuilder tally = new StringBuilder();
                    if(plugin.bridgeTally.containsKey("TopazTroopers")) {
                        tally.append("§e§l✔".repeat(plugin.bridgeTally.get("TopazTroopers")));
                        tally.append("§8§l✔".repeat(12 - plugin.bridgeTally.get("TopazTroopers")));
                    } else {
                        tally.append("§8§l✔".repeat(12));
                    }
                    return tally.toString();
                } else {
                    return "§8N/A";
                }
            case "bridge_kyanite":
                if(plugin.currentMode.equals("Bridge Builders")){
                    StringBuilder tally = new StringBuilder();
                    if(plugin.bridgeTally.containsKey("KyaniteKillers")) {
                        tally.append("§a§l✔".repeat(plugin.bridgeTally.get("KyaniteKillers")));
                        tally.append("§8§l✔".repeat(12 - plugin.bridgeTally.get("KyaniteKillers")));
                    } else {
                        tally.append("§8§l✔".repeat(12));
                    }
                    return tally.toString();
                } else {
                    return "§8N/A";
                }
            case "bridge_diamond":
                if(plugin.currentMode.equals("Bridge Builders")){
                    StringBuilder tally = new StringBuilder();
                    if(plugin.bridgeTally.containsKey("DiamondDestroyers")) {
                        tally.append("§b§l✔".repeat(plugin.bridgeTally.get("DiamondDestroyers")));
                        tally.append("§8§l✔".repeat(12 - plugin.bridgeTally.get("DiamondDestroyers")));
                    } else {
                        tally.append("§8§l✔".repeat(12));
                    }
                    return tally.toString();
                } else {
                    return "§8N/A";
                }
            case "bridge_sapphire":
                if(plugin.currentMode.equals("Bridge Builders")){
                    StringBuilder tally = new StringBuilder();
                    if(plugin.bridgeTally.containsKey("SapphireSoldiers")) {
                        tally.append("§9§l✔".repeat(plugin.bridgeTally.get("SapphireSoldiers")));
                        tally.append("§8§l✔".repeat(12 - plugin.bridgeTally.get("SapphireSoldiers")));
                    } else {
                        tally.append("§8§l✔".repeat(12));
                    }
                    return tally.toString();
                } else {
                    return "§8N/A";
                }
            case "bridge_smithsonite":
                if(plugin.currentMode.equals("Bridge Builders")){
                    StringBuilder tally = new StringBuilder();
                    if(plugin.bridgeTally.containsKey("SmithsoniteSlayers")) {
                        tally.append("§d§l✔".repeat(plugin.bridgeTally.get("SmithsoniteSlayers")));
                        tally.append("§8§l✔".repeat(12 - plugin.bridgeTally.get("SmithsoniteSlayers")));
                    } else {
                        tally.append("§8§l✔".repeat(12));
                    }
                    return tally.toString();
                } else {
                    return "§8N/A";
                }
            case "bridge_crystal":
                if(plugin.currentMode.equals("Bridge Builders")){
                    StringBuilder tally = new StringBuilder();
                    if(plugin.bridgeTally.containsKey("CrystalCrashers")) {
                        tally.append("§f§l✔".repeat(plugin.bridgeTally.get("CrystalCrashers")));
                        tally.append("§8§l✔".repeat(12 - plugin.bridgeTally.get("CrystalCrashers")));
                    } else {
                        tally.append("§8§l✔".repeat(12));
                    }
                    return tally.toString();
                } else {
                    return "§8N/A";
                }


            case "currentmode":
                if(plugin.finaleActive){
                    return "Finale";
                } else {
                    return plugin.currentMode;
                }
            default:
                return null;
        }
    }

    private String getTopTeamLine(int line, int position, Player player) {
        List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());
        int teamCount = leaderteams.size();

        if (line == 1) {
            return getTeamLine(0, player);
        }

        int start = Math.max(1, Math.min(position - 2, teamCount - 3));
        int index = start + (line - 2);

        return getTeamLine(index, player);
    }

    private String getTeamLine(int index, Player player) {
        List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());
        List<Integer> leaderteampoints = new ArrayList<>(plugin.sortByValue().values());

        int position = index + 1;

        if (leaderteams.size() > index && leaderteams.get(index) != null && plugin.teamShown[index]) {
            String teamName = plugin.getTeamDisplayName(leaderteams.get(index));

            String playerTeam = PlayerConfig.get().getString("players." + player.getName() + ".team");
            if (plugin.getTeamDisplayName(playerTeam).equals(teamName)) {
                teamName = TeamsConfig.get().get("teams." + leaderteams.get(index) + ".colour") + "§l► " + teamName;
            }

            return positionToText(position) + " §8| " + formatLine(
                    teamName,
                    "§e§l\uD83D\uDCB0" + leaderteampoints.get(index),
                    160
            );

        } else if (leaderteams.size() > index) {
            return "§8§k" + positionToText(position) + " §8| " + formatLine(
                    "§8§k00000000000000000000",
                    "§8§l\uD83D\uDCB0§8§k00000",
                    160
            );
        } else {
            return "§8N/A";
        }
    }

    public String positionToText(int position){
        String positionText;
        String hexString;

        switch(position){
            case 1:
                positionText = "1sᴛ";
                hexString = "#ffe045";
                break;

            case 2:
                positionText = "2ɴᴅ";
                hexString = "#b6b6b6";
                break;

            case 3:
                positionText = "3ʀᴅ";
                hexString = "#e4b338";
                break;


            default:
                positionText = position + "ᴛʜ";
                hexString = "#FFFFFF";
                break;
        }

        TextColor color = TextColor.fromHexString(hexString);

        Component message = Component.text(positionText)
                .color(color)
                .decorate(TextDecoration.BOLD);

        return LegacyComponentSerializer.legacySection().serialize(message);
    }



    public static String formatLine(String leftText, String rightText, int targetWidth) {
        // Regex for emojis and circled numbers
        String regex = "\uD83D\uDCB0|[❶-❽]";

        // Strip formatting and special characters for width calculation
        String strippedLeft = leftText.replaceAll(regex, "");
        String strippedRight = rightText.replaceAll(regex, "");

        // Measure widths using your font utility
        int leftWidth = FontUtils.getStringWidth(strippedLeft);
        int rightWidth = FontUtils.getStringWidth(strippedRight);

        // Remaining width to fill
        int remainingWidth = targetWidth - leftWidth - rightWidth;

        // If no space remains, just concatenate
        if (remainingWidth <= 0) {
            return leftText + rightText;
        }

        // Generate the exact-width space character(s)
        String space = getExactSpace(remainingWidth);

        return leftText + space + rightText;
    }

    /**
     * Returns a Minecraft resource-pack character that shifts text by the exact width.
     * Supports integer width (-8192 to 8192) and fractional width (-1.0 to 1.0) spaces.
     */
    public static String getExactSpace(double width) {
        // Use floor for positive, ceil for negative to always stay smaller
        int intWidth = width > 0 ? (int) Math.floor(width) : (int) Math.ceil(width);

        // Integer width space
        if (intWidth >= -8192 && intWidth <= 8192) {
            int charCode = 0xD0000 + intWidth;
            return new String(Character.toChars(charCode));
        }

        // Fractional widths (-1.0 to 1.0)
        if (width >= -1.0 && width <= 1.0) {
            int fracCode = 0x50000 + (int) Math.floor(width * 4800); // use floor to stay smaller
            return new String(Character.toChars(fracCode));
        }

        // Fallback: split into multiple integer-width spaces
        StringBuilder builder = new StringBuilder();
        int remaining = intWidth;
        while (remaining != 0) {
            int part = Math.max(-8192, Math.min(8192, remaining));
            builder.append(getExactSpace(part));
            remaining -= part;
        }
        return builder.toString();
    }


}
