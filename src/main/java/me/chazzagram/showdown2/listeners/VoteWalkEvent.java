package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.PlayerConfig;
import me.chazzagram.showdown2.files.TeleportConfig;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Objects;

public class VoteWalkEvent implements Listener {

    private static Showdown2 plugin;

    public VoteWalkEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) throws ReflectiveOperationException {
        if(event.getTo() != null) {
            if (plugin.getPlayers().contains(Bukkit.getPlayer(event.getPlayer().getName()))) {
                if(plugin.ghostManager.getGhostPlayers().contains(event.getPlayer().getName())) return;
                if (plugin.currentMode.equals("Gub Game") && plugin.runningTimers.containsKey("gubgamestart") && plugin.runningTimers.get("gubgamestart").getValue() <= 55) {
                    if (plugin.getPlayers().contains(Bukkit.getPlayer(event.getPlayer().getName()))) {
                        boolean onBlock = false;
                        for (Material concrete : getConcreteColours()) {
                            if (event.getTo().getBlock().getRelative(BlockFace.DOWN).getType().equals(concrete)) {
                                onBlock = true;
                            }
                        }
                        if (!onBlock) {
                            event.getPlayer().teleport(TeleportConfig.get().getLocation("teams." + PlayerConfig.get().getString("players." + event.getPlayer().getName() + ".team") + ".gubgame"));
                        }
                    }
                }
                if(plugin.currentMode.equals("Crumble Clash") && plugin.runningTimers.containsKey("crumbleclash") && plugin.copperDecay && !plugin.deadPlayers.contains(event.getPlayer().getName())){
                    if(plugin.ghostManager.getGhostPlayers().contains(event.getPlayer().getName())) return;
                    if (event.getFrom().getBlock().equals(event.getTo().getBlock())) return;
                    plugin.handleDecay(event.getPlayer());
                }
                if(plugin.currentMode.equals("Dimension Dash") && !plugin.finaleActive){
                    if(plugin.ghostManager.getGhostPlayers().contains(event.getPlayer().getName())) return;
                    Block block = event.getTo().getBlock().getRelative(BlockFace.DOWN);

                    String value = plugin.ddMapWalkableBlock.get(block.getType());

                    if (value == null) return;
                    if(!Objects.equals(plugin.ddMapVotes.get(event.getPlayer().getName()), plugin.ddMapWalkableBlock.get(block.getType()))) {
                        int x = block.getX();
                        int y = block.getY();
                        int z = block.getZ();

                        if ((x >= 36 && x <= 83 &&
                                y >= 138 && y <= 152 &&
                                z >= 1071 && z <= 1109 && plugin.currentRound == 1) ||
                                (x >= 36 && x <= 83 &&
                                        y >= 138 && y <= 155 &&
                                        z >= 1143 && z <= 1179 && plugin.currentRound == 2)) {
                            plugin.ddMapVotes.put(event.getPlayer().getName(), plugin.ddMapWalkableBlock.get(block.getType()));
                            event.getPlayer().sendTitle(plugin.ddMapWalkableBlock.get(block.getType()), "", 0, 20, 0);
                        }
                    }
                }
                if (plugin.currentMode.equals("Voting") && plugin.runningTimers.get("voting").getValue() <= 75) {
                    for (Material concrete : getConcreteColours()) {
                        if (event.getTo().getBlock().getRelative(BlockFace.DOWN).getType().equals(concrete)) {
                            String colour = concrete.toString().toUpperCase().replace("CONCRETE", "WOOL");
                            Material wool = Material.getMaterial(colour);
                            if (!wool.equals(plugin.playerVote.get(event.getPlayer()))) {
                                event.getPlayer().sendTitle(plugin.woolLogos.get(wool), "", 0, 20, 10);
                                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1.0F);
                            }
                            plugin.playerVote.put(event.getPlayer(), wool);
                            if (event.getPlayer().equals(plugin.slimeBallVote)) {
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    plugin.glowingEntities.setGlowing(plugin.chickenBall, player, plugin.modeColors.get(plugin.woolModes.get(plugin.playerVote.get(event.getPlayer()))));
                                }
                            }
                        }
                    }
                    if (plugin.votingEnabled && plugin.votingMode.equals("walk")) {
                        for (Material wool : getWoolColors()) {
                            if (event.getTo().getBlock().getRelative(BlockFace.DOWN).getType().equals(wool)) {
                                if (plugin.playerVote.containsKey(event.getPlayer())) {
                                    event.getTo().getBlock().getRelative(BlockFace.DOWN).setType(plugin.playerVote.get(event.getPlayer()));
                                }
                            }
                        }
                    }
                } else if ((plugin.currentMode.equals("Zoomo Go") || plugin.currentMode.equals("Slime Golf")) && plugin.doubleJumpEnabled && (event.getPlayer().getGameMode().equals(GameMode.ADVENTURE) || event.getPlayer().getGameMode().equals(GameMode.SURVIVAL))) {
                    Block block = event.getPlayer().getLocation().subtract(0, 0.1, 0).getBlock();
                    if(!event.getPlayer().getPassengers().isEmpty()) { event.getPlayer().setAllowFlight(false); return; }
                    if (block.getType() != Material.AIR && !plugin.runningTimers.containsKey(event.getPlayer().getName() + "dj")) {
                        event.getPlayer().setAllowFlight(true);
                        event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("§eDOUBLE JUMP READY!"));
                    }
                    if (block.getType() != Material.AIR && plugin.lifeCap && plugin.zoomoLives.get(event.getPlayer().getName()) == 2) {
                        plugin.zoomoLives.replace(event.getPlayer().getName(), 1);
                        event.getPlayer().sendTitle("", "§e⚠ §6§lLIVES CAPPED: §f1§c❤ §e⚠", 0, 30, 0);
                        plugin.earnPoints(event.getPlayer().getName(), 20, true);
                        plugin.messagePlayer(event.getPlayer(), "§e\uD83D\uDCB020 §7| §a§lBonus points awarded for keeping both lives.");
                    }
                }
            }
        }
    }


    private Material[] getWoolColors() {
        return new Material[]{
                Material.RED_WOOL, Material.WHITE_WOOL, Material.ORANGE_WOOL, Material.LIGHT_BLUE_WOOL, Material.LIME_WOOL,
                Material.YELLOW_WOOL, Material.PURPLE_WOOL, Material.BLACK_WOOL
        };
    }

    private Material[] getConcreteColours() {
        return new Material[]{
                Material.RED_CONCRETE, Material.WHITE_CONCRETE, Material.ORANGE_CONCRETE, Material.LIGHT_BLUE_CONCRETE, Material.LIME_CONCRETE,
                Material.YELLOW_CONCRETE, Material.PURPLE_CONCRETE

        };
    }
}
