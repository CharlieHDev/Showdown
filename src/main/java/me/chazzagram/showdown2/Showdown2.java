package me.chazzagram.showdown2;

import com.sun.tools.javac.Main;
import me.chazzagram.showdown2.commands.MainCommand;
import me.chazzagram.showdown2.expansions.SpigotExpansion;
import me.chazzagram.showdown2.files.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.bukkit.util.NumberConversions.round;

public final class Showdown2 extends JavaPlugin implements Listener {

    private static Showdown2 plugin;

    private double multiplier = 1.0;

    public HashMap<String, Map.Entry<BukkitTask, Integer>> runningTimers = new HashMap<>();

    public HashMap<Integer, Integer> slimeCheckpoints = new HashMap<>();

    public HashMap<String, String> slimeFinishers = new HashMap<>();


    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;

        this.getCommand("mcevent").setExecutor(new MainCommand(this));

        TeamsConfig.setup();
        TeamsConfig.get().options().copyDefaults(true);
        TeamsConfig.save();

        PlayerConfig.setup();
        PlayerConfig.get().options().copyDefaults(true);
        PlayerConfig.save();

        SpectatorConfig.setup();
        SpectatorConfig.get().options().copyDefaults(true);
        SpectatorConfig.save();

        TeleportConfig.setup();
        TeleportConfig.get().options().copyDefaults(true);
        TeleportConfig.save();

        DeathMessagesConfig.setup();
        DeathMessagesConfig.get().options().copyDefaults(true);
        DeathMessagesConfig.save();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Bukkit.getPluginManager().registerEvents(this, this);
            new SpigotExpansion(this).register();
        }

        for(int i = 1; i <= 5; i++){
            slimeCheckpoints.put(i, 1);
        }

        messageConsole("Plugin Loaded.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        messageConsole("Plugin Unloaded.");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        boolean playerFound = false;
        Player p = e.getPlayer();
        for(String player : PlayerConfig.get().getConfigurationSection("players").getKeys(false)){
            if(p.getName().equals(player)){
                playerFound = true;
                break;
            }
        }
        if(!playerFound){
            if(!SpectatorConfig.get().getStringList("spectators").isEmpty()) {
                for (String player : SpectatorConfig.get().getStringList("spectators")) {
                    if (p.getName().equals(player)) {
                        playerFound = true;
                        break;
                    }
                }
            }
            if(!playerFound){
                List<String> spectators = SpectatorConfig.get().getStringList("spectators");
                spectators.add(p.getName());
                SpectatorConfig.get().set("spectators", spectators);
                SpectatorConfig.save();
            }
        }



    }

    public static Showdown2 getPlugin() {
        return plugin;
    }

    public void messageConsole(String message){
        System.out.println("[ME24] " + message);
    }

    public void messagePlayer(Player p, String message){
        p.sendMessage("[ME24] " + message);
    }

//    Point multiplier.
    public int multiplyPoints(int points){
        return round(points * multiplier);
    }

//    Awarding points.
    public void earnPoints(String player, int points, boolean individual){
        String playerTeam = PlayerConfig.get().getString("players." + player + ".team");
        int currentTeamPoints = TeamsConfig.get().getInt("teams." + playerTeam + ".points");
        TeamsConfig.get().set("teams." + playerTeam + ".points", currentTeamPoints+multiplyPoints(points));
        TeamsConfig.save();
        if(individual) {
            int currentPoints = PlayerConfig.get().getInt("players." + player + ".points");
            PlayerConfig.get().set("players." + player + ".points", currentPoints+multiplyPoints(points));
            PlayerConfig.save();
        }
    }

    public void earnTeamPoints(String team, int points){
        int currentTeamPoints = TeamsConfig.get().getInt("teams." + team + ".points");
        TeamsConfig.get().set("teams." + team + ".points", currentTeamPoints+multiplyPoints(points));
        TeamsConfig.save();
    }

//    Get all players
    public List<Player> getPlayers(){
        List<Player> players = new ArrayList<>();
        for(String key : PlayerConfig.get().getConfigurationSection("players").getKeys(false)){
            if(Bukkit.getServer().getPlayer(key) != null) {
                players.add(Bukkit.getPlayer(key));
            }
        }
        return players;
    }

//    Get all spectators
    public List<Player> getSpectators(){
        List<Player> spectators = new ArrayList<>();
        for(String spectator : SpectatorConfig.get().getStringList("spectators")){
            if(Bukkit.getServer().getPlayer(spectator) != null) {
                spectators.add(Bukkit.getPlayer(spectator));
            }
        }
        return spectators;
    }

//    Start a timer
    public void startTimer(int seconds, String name){
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = seconds;
            @Override
            public void run() {
                runningTimers.get(name).setValue(timeLeft);
                timeLeft--;
                if(timeLeft == 0) {
                    messageConsole("Timer finished.");
                    runningTimers.remove(name);
                    cancel();
                } else {

                    messageConsole(timeLeft + " seconds left..");
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put(name, new AbstractMap.SimpleEntry<>(task, seconds));
    }

    public void startStopwatch(int seconds, String name){
        BukkitTask task = new BukkitRunnable() {
            int timeElapsed = 0;
            @Override
            public void run() {
                runningTimers.get(name).setValue(timeElapsed);
                timeElapsed++;
                if(timeElapsed == seconds) {
                    messageConsole("Timer finished.");
                    runningTimers.remove(name);
                    cancel();
                } else {

                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put(name, new AbstractMap.SimpleEntry<>(task, 0));
    }

    public String getTimer(String timer) {
        return LocalTime.of(0, plugin.runningTimers.get(timer).getValue() / 60, plugin.runningTimers.get(timer).getValue() % 60).format(DateTimeFormatter.ofPattern("mm:ss"));
    }

//    Stop a timer
    public void stopTimer(String name){
        if(runningTimers.containsKey(name)){
            runningTimers.get(name).getKey().cancel();
            runningTimers.remove(name);
            messageConsole("Timer cancelled.");
        } else {
            messageConsole("Timer not found.");
        }

    }

//    Teleport all players
    public void teleportPlayers(Location location){
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 6;
            @Override
            public void run() {
                runningTimers.get("teleporttimer").setValue(timeLeft);
                timeLeft--;
                if(timeLeft == 0) {
                    for(Player player : getPlayers()) {
                        player.teleport(location);
                    }
                    runningTimers.remove("teleporttimer");
                    cancel();
                } else {
                    for(Player player : getPlayers()) {
                        player.sendTitle("", "§6Teleporting in §c" + timeLeft + "...", 0, 20, 20);
                    }
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("teleporttimer", new AbstractMap.SimpleEntry<>(task, 6));
    }

//    Teleport all players (teleports all players to their team teleports)
    public void teamTeleport(String location){
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 6;
            @Override
            public void run() {
                runningTimers.get("teleporttimerteam").setValue(timeLeft);
                timeLeft--;
                if(timeLeft == 0) {
                    for(Player player : getPlayers()){
                        Location tplocation = TeleportConfig.get().getLocation("teams." + PlayerConfig.get().getString("players." + player.getName() + ".team") + "." + location);
                        player.teleport(tplocation);
                    }
                    runningTimers.remove("teleporttimerteam");
                    cancel();
                } else {
                    for(Player player : getPlayers()) {
                        player.sendTitle("", "§6Teleporting in §c" + timeLeft + "...", 0, 20, 20);
                    }
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("teleporttimerteam", new AbstractMap.SimpleEntry<>(task, 6));
    }

//    Teleports all spectators
    public void teleportSpectators(Location location){
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 6;
            @Override
            public void run() {
                runningTimers.get("teleporttimerspec").setValue(timeLeft);
                timeLeft--;
                if(timeLeft == 0) {
                    for(Player player : getSpectators()) {
                        player.teleport(location);
                    }
                    runningTimers.remove("teleporttimerspec");
                    cancel();
                } else {
                    for(Player player : getPlayers()) {
                        player.sendTitle("", "§7§oTeleporting in " + timeLeft + "...", 0, 20, 20);
                    }
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("teleporttimerspec", new AbstractMap.SimpleEntry<>(task, 6));
    }

//    public void playerMedal(Player p) {
//        ItemStack[] currentInv = p.getInventory().getContents();
//        p.getInventory().setItemInOffHand();
//        p.setHealth(0.0);
//        p.getInventory().setContents(currentInv);
//    }
}
