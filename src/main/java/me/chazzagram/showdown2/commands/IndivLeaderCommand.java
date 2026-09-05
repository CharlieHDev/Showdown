package me.chazzagram.showdown2.commands;

import me.chazzagram.showdown2.Showdown2;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class IndivLeaderCommand implements CommandExecutor {

    private static Showdown2 plugin;

    public IndivLeaderCommand(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player p)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        boolean shown2 = true;
        for(int i = 0; i <= 7; i++){
            if(!plugin.teamShown[i]){
                shown2 = false;
                break;
            }
        }
        if(shown2){
            List<String> indivNames = new ArrayList<>(plugin.getSortedIndivs().keySet());
            List<Integer> indivPoints = new ArrayList<>(plugin.getSortedIndivs().values());
            plugin.messagePlayer(p, "§6§lɪɴᴅɪᴠɪᴅᴜᴀʟ ʟᴇᴀᴅᴇʀʙᴏᴀʀᴅ");
            int index = 1;
            for(String name : indivNames){
                plugin.messagePlayer(p, plugin.formatLine(index + ". " + plugin.getPlayerDisplayName(name), "§e§l💰" + indivPoints.get(index-1), 150));
                index++;
            }
        } else {
            plugin.messagePlayer(p, "§6§lIndividual leaderboard is hidden during this segment of the event.");
        }
        return true;
    }
}
