package me.chazzagram.showdown2.recaps;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.PlayerConfig;
import me.chazzagram.showdown2.files.TeamsConfig;
import me.chazzagram.showdown2.listeners.DashTimeData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DimensionDashRecap implements GameRecap {

    private DashTimeData raceTimes;

    private final String playerName;

    int finalPlacement;

    private static Showdown2 plugin;

    public DimensionDashRecap(Showdown2 plugin, String player) {
        this.plugin = plugin;
        this.playerName = player;
    }

    @Override
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public Showdown2 getPlugin() {
        return plugin;
    }

    @Override
    public void roundRecap() {
        Player p = Bukkit.getPlayerExact(playerName);
        if (p == null) return;

        p.sendMessage("""
                §8
                """ + plugin.hex("#FF5736") + """
                ʀᴇᴄᴀᴘ ғᴏʀ ᴛʜɪs ɢᴀᴍᴇ:
                §7§l  ⤷ §fThis round you placed §7""" + getPlacementString(finalPlacement) + """
                §7!
                §7§l  ⤷ §e§lʟᴀᴘ #1 §8[§a""" + raceTimes.getLap1TimeConverted() + """
                §8]
                §7§l  ⤷ §e§lʟᴀᴘ #2 §8[§a""" + raceTimes.getLap2TimeConverted() + """
                §8]
                §7§l  ⤷ §e§lʟᴀᴘ #3 §8[§a""" + raceTimes.getLap3TimeConverted() + """                
                §8]
                §7
                """);

    }

    public void putRaceTimes(DashTimeData raceTimes) {
        this.raceTimes = raceTimes;
    }

}
