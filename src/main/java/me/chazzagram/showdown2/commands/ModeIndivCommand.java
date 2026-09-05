package me.chazzagram.showdown2.commands;

import me.chazzagram.showdown2.Showdown2;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ModeIndivCommand implements CommandExecutor {

    private static Showdown2 plugin;

    public ModeIndivCommand(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player p)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        boolean shown = true;
        for(int i = 0; i <= 7; i++){
            if(!plugin.teamShown[i]){
                shown = false;
                break;
            }
        }
        if(shown) {
            List<String> modeLeaderName = new ArrayList<>(plugin.sortMap(plugin.modeFullPoints).keySet());
            List<Integer> modeLeaderPoints = new ArrayList<>(plugin.sortMap(plugin.modeFullPoints).values());
            plugin.messagePlayer(p, "§6§lᴘʀᴇᴠɪᴏᴜs ᴍᴏᴅᴇ ʟᴇᴀᴅᴇʀʙᴏᴀʀᴅ");
            int placement;
            for (int i = 0; i < modeLeaderName.size(); i++) {
                placement = i + 1;
                plugin.messagePlayer(p, plugin.formatLine("§7" + placement + ". " + plugin.getPlayerDisplayName(modeLeaderName.get(i)), "§e§l💰" + modeLeaderPoints.get(i), 150));

            }
        } else {
            plugin.messagePlayer(p, "§6§lPrevious mode leaderboard is hidden during this segment of the event.");
        }
        return true;
    }
}
