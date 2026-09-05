package me.chazzagram.showdown2.commands;

import me.chazzagram.showdown2.Showdown2;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeamLeaderCommand implements CommandExecutor {

    private static Showdown2 plugin;

    public TeamLeaderCommand(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player p)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        boolean shown1 = true;
        for(int i = 0; i <= 7; i++){
            if(!plugin.teamShown[i]){
                shown1 = false;
                break;
            }
        }
        if(shown1){
            List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());
            List<Integer> leaderteampoints = new ArrayList<>(plugin.sortByValue().values());
            plugin.messagePlayer(p, "§6§lᴛᴇᴀᴍ ʟᴇᴀᴅᴇʀʙᴏᴀʀᴅ");
            int index = 1;
            for(String name : leaderteams){
                plugin.messagePlayer(p, plugin.formatLine(index + ". " + plugin.getTeamDisplayName(name), "§e§l💰" + leaderteampoints.get(index-1), 170));
                index++;
            }
        } else {
            plugin.messagePlayer(p, "§6§lTeam leaderboard is hidden during this segment of the event.");
        }
        return true;
    }
}
