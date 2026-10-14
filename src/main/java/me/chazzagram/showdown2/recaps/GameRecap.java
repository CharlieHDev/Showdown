package me.chazzagram.showdown2.recaps;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.PlayerConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface GameRecap {

    List<String> placementToString = List.of(
            "§e§l1sᴛ", "§7§l2ɴᴅ", "§6§l3ʀᴅ", "§f§l4ᴛʜ", "§f§l5ᴛʜ", "§f§l6ᴛʜ", "§f§l7ᴛʜ", "§f§l8ᴛʜ",
            "§f§l9ᴛʜ", "§f§l10ᴛʜ", "§f§l11ᴛʜ", "§f§l12ᴛʜ", "§f§l13ᴛʜ", "§f§l14ᴛʜ", "§f§l15ᴛʜ", "§f§l16ᴛʜ",
            "§f§l17ᴛʜ", "§f§l18ᴛʜ", "§f§l19ᴛʜ", "§f§l20ᴛʜ", "§f§l21sᴛ", "§f§l22ɴᴅ", "§f§l23ʀᴅ", "§f§l24ᴛʜ",
            "§f§l25ᴛʜ", "§f§l26ᴛʜ", "§f§l27ᴛʜ", "§f§l28ᴛʜ", "§f§l29ᴛʜ", "§f§l30ᴛʜ", "§f§l31sᴛ", "§f§l32ɴᴅ"
    );

    Showdown2 getPlugin();

    String getPlayerName();

    default String getPlacementString(int placement) {
        return placementToString.get(placement);
    }

    void roundRecap();

    default void gameRecap() {
        Player p = Bukkit.getPlayer(getPlayerName());
        if (p == null) return;

        Map<String, Integer> scores = getPlugin().modeFullPoints;
        if (scores == null || scores.isEmpty()) return;

        String team = PlayerConfig.get().getString("players." + getPlayerName() + ".team");

        p.sendMessage("§8");
        p.sendMessage(getPlugin().hex("#FF5736") + "ᴛᴇᴀᴍ ᴘᴇʀғᴏʀᴍᴀɴᴄᴇ ᴛʜɪs ɢᴀᴍᴇ:");

        List<Map.Entry<String, Integer>> sorted = scores.entrySet().stream()
                .filter((score) -> Objects.equals(PlayerConfig.get().getString("players." + score.getKey() + ".team"), team))
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .toList();

        for (Map.Entry<String, Integer> entry : sorted) {
            p.sendMessage("§7  ↪ " + getPlugin().getPlayerDisplayName(entry.getKey())
                    + "§8 - §e§l💰" + entry.getValue()
                    + " §7(\uD83D\uDCB0" + Math.floor(entry.getValue() / getPlugin().multiplier) + " §7§ox" + String.format("%.2f", getPlugin().multiplier) + ")");
        }
        p.sendMessage("§8");
    }

}
