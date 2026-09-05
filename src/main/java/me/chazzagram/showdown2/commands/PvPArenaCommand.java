package me.chazzagram.showdown2.commands;

import me.chazzagram.showdown2.Showdown2;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PvPArenaCommand implements CommandExecutor {

    private static Showdown2 plugin;

    public PvPArenaCommand(Showdown2 plugin) {
        this.plugin = plugin;
    }

    Location pvpArenaLocation = new Location(Bukkit.getServer().getWorld("build"), 168.5, 141, 682.5, -90, 0);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player p)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if(plugin.pvpArenaManager.getArenaEnabled()){
            if(!plugin.pvpArenaManager.getArenaPlayers().contains(p.getName())) {
                p.teleport(pvpArenaLocation);
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 1F);
                plugin.messagePlayer(p, "§6[§e!§6] §eTeleported to §cPvP Practice Arena§e.");
            }
        } else {
            plugin.messagePlayer(p, "§6[§e!§6] §e§cPvP Practice Arena §eis currently not open.");
        }
        return true;
    }
}
