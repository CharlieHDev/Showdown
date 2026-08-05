package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.TeamsConfig;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SulfurGolfQueue {

    private List<String> playerQueue;

    private Integer queuePosition;

    private String teamColour;

    private String teamName;

    private ItemStack slingshot;

    private String currentPlayerName;

    Showdown2 plugin;

    public SulfurGolfQueue(Showdown2 plugin, String team) {
        this.playerQueue = new ArrayList<>();
        this.queuePosition = 0;
        this.teamColour = TeamsConfig.get().getString("teams." + team + ".colour");
        this.teamName = team;
        this.plugin = plugin;

        slingshot = new ItemStack(Material.BOW);
        ItemMeta meta = slingshot.getItemMeta();
        meta.setItemModel(new NamespacedKey("amongus", "slingshot"));
        meta.setDisplayName("§a§lPutter!");
        meta.addEnchant(Enchantment.INFINITY, 1, true);
        meta.setUnbreakable(true);
        slingshot.setItemMeta(meta);
    }

    public void addPlayer(String player) {
        this.playerQueue.add(player);
    }

    public void displayQueue(){
        StringBuilder queue = new StringBuilder();
        for(int i = 0; i < this.playerQueue.size(); i++){
            if(Bukkit.getPlayer(playerQueue.get(i)) == null){
                queue.append("§8§m").append(playerQueue.get(i));
            } else {
                if (i == this.queuePosition) {
                    queue.append(teamColour).append("§n").append(playerQueue.get(i));
                } else {
                    queue.append("§7").append(playerQueue.get(i));
                }
            }
            if (i < this.playerQueue.size() - 1) {
                queue.append("§7 > ");
            }
            queue.append("§7");
        }

        for(String player : this.playerQueue){
            Player p = Bukkit.getPlayer(player);
            if(p != null) {
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(queue.toString()));
            }
        }
    }

    public void startQueue(){

        this.queuePosition = 0;
        Player currentPlayer = findNextOnlinePlayer();

        this.currentPlayerName = currentPlayer.getName();
        currentPlayer.getInventory().addItem(slingshot);

        plugin.messagePlayer(currentPlayer, """
                    §8
                    §8
                    §e[§6§l!§e] §6You have obtained the putter!
                    §8
                    """);
        currentPlayer.playSound(currentPlayer.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1F, 1.5F);
    }

    public void updateQueuePosition(){
        Player currentPlayer = Bukkit.getPlayer(this.playerQueue.get(this.queuePosition));
        if (currentPlayer != null) {
            currentPlayer.getInventory().remove(slingshot);
        }

        this.queuePosition = (this.queuePosition == this.playerQueue.size() - 1) ? 0 : this.queuePosition + 1;

        currentPlayer = findNextOnlinePlayer();
        this.currentPlayerName = currentPlayer.getName();

        currentPlayer.getInventory().addItem(slingshot);

        plugin.messagePlayer(currentPlayer, """
                    §8
                    §8
                    §e[§6§l!§e] §6You have obtained the putter!
                    §8
                    """);

        for(String teamPlayer : playerQueue){
            if(Objects.equals(teamPlayer, currentPlayer.getName())) continue;
            Player p = Bukkit.getPlayer(teamPlayer);
            if(p != null) {
                plugin.messagePlayer(currentPlayer, "§e[§6§l!§e] §6" + currentPlayer.getName() + " has obtained the putter!");
            }
        }

        currentPlayer.playSound(currentPlayer.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1F, 1.5F);
        currentPlayer.setCooldown(Material.BOW, 40);

        displayQueue();
    }

    private Player findNextOnlinePlayer() {
        int startIndex = this.queuePosition;
        int index = startIndex;

        do {
            Player p = Bukkit.getPlayer(this.playerQueue.get(index));
            if (p != null) {
                this.queuePosition = index;
                return p;
            }
            index = (index == this.playerQueue.size() - 1) ? 0 : index + 1;
        } while (index != startIndex);

        return null;
    }

    public String getCurrentPlayerName(){
        return this.currentPlayerName;
    }

    public String getTeamName(){
        return this.teamName;
    }
}
