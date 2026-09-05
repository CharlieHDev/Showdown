package me.chazzagram.showdown2.commands;

import me.chazzagram.showdown2.Showdown2;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyMusicCommand implements CommandExecutor {

    private static Showdown2 plugin;

    public LobbyMusicCommand(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player p)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if(plugin.currentMode.equals("Lobby")) {
            if (plugin.musicManager.getMusicTasks().containsKey(p.getUniqueId())) {
                plugin.musicManager.stopMusic(p);
            } else {
                plugin.musicManager.startMusic(p);
            }
        } else {
            plugin.messagePlayer(p, "§8[§c§l!§8] §7You cannot toggle lobby music during games!");
        }
        return true;
    }
}
