package me.chazzagram.showdown2.expansions;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.PlayerConfig;
import me.chazzagram.showdown2.files.TeamsConfig;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
        if(p == null){
            return "";
        }
        switch (params){
            case "player":
                return p.getName();
            case "team":
                String team = PlayerConfig.get().getString("players." + p.getName() + ".team");
                return Objects.requireNonNullElse(team, "No Team.");
            case "points":
                String points = PlayerConfig.get().getString("players." + p.getName() + ".points");
                return Objects.requireNonNullElse(points, "N/A");
            case "teampoints":
                String selectTeam = PlayerConfig.get().getString("players." + p.getName() + ".team");
                String teampoints = String.valueOf(TeamsConfig.get().getInt("teams." + selectTeam + ".points"));
                if (selectTeam == null){
                    return "N/A";
                } else {
                    return teampoints;
                }
            case "timer_sumo":
                if(plugin.runningTimers.containsKey("sumo")){
                    return LocalTime.of(0, plugin.runningTimers.get("sumo").getValue() / 60, plugin.runningTimers.get("sumo").getValue() % 60).format(DateTimeFormatter.ofPattern("mm:ss"));
                } else {
                    return "Waiting..";
                }
            case "timer_craftalot":
                if(plugin.runningTimers.containsKey("craftalot")){
                    return LocalTime.of(0, plugin.runningTimers.get("craftalot").getValue() / 60, plugin.runningTimers.get("craftalot").getValue() % 60).format(DateTimeFormatter.ofPattern("mm:ss"));
                } else {
                    return "Waiting..";
                }
            default:
                return null;
        }


    }
}
