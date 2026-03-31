package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.PlayerConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class PlayerChatEvent implements Listener {

    private final Showdown2 plugin;

    public PlayerChatEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }

//    @EventHandler
//    public void onChat(AsyncPlayerChatEvent event) {
//
//        event.setCancelled(true);
//
//        Player player = event.getPlayer();
//        String name = plugin.getPlayerDisplayName(player.getName());
//        String msg = event.getMessage();
//        String hex;
//
//        if(plugin.getPlayers().contains(player)){
//            hex = plugin.teamChatColours.get(PlayerConfig.get().getString("players." + name + ".team"));
//        } else {
//            hex = "#FFFFFF";
//        }
//        Component message = Component.text(name + ": ").append(Component.text(msg, TextColor.fromHexString(hex)));
//
//        adventure.sender(player).sendMessage(message);
//    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String name = plugin.getPlayerDisplayName(player.getName());
        String msg = event.getMessage();
        int hex;

        if(plugin.getPlayers().contains(player)){
            hex = Integer.parseInt(plugin.teamChatColours.get(PlayerConfig.get().getString("players." + player.getName() + ".team")), 16);
        } else {
            hex = Integer.parseInt("FFFFFF", 16);
        }

        // Convert hex to int
        TextColor color = TextColor.color(hex);

        Component message = Component.text(name + ": ").append(Component.text(msg, color));

        event.setCancelled(true); // cancel default chat
        String legacy = LegacyComponentSerializer.legacySection().serialize(message);
        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendMessage(legacy);
        }
    }
}
