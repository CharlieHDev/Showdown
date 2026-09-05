package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class EventPlayer {

    private final Showdown2 plugin;
    private String team;
    private String playerName;
    private Player player;

    public EventPlayer(Showdown2 plugin, String playerName, String team) {
        this.plugin = plugin;
        this.team = team;
        this.playerName = playerName;
        this.player = Bukkit.getPlayer(playerName);
    }


    public boolean isOnline() {
        return player != null && player.isOnline();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
