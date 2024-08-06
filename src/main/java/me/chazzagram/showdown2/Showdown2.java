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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
        p.sendMessage("§8" + message);
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
            int timeElapsed = -1;
            @Override
            public void run() {
                timeElapsed++;
                runningTimers.get(name).setValue(timeElapsed);
                if(timeElapsed == seconds) {
                    messageConsole("Timer finished.");
                    gameEnd();
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
    public void teleportPlayers(Location location, int countdown){
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = countdown+1;
            @Override
            public void run() {
                timeLeft--;
                runningTimers.get("teleporttimer").setValue(timeLeft);
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
    public void teamTeleport(String location, int countdown){
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = countdown+1;
            @Override
            public void run() {
                timeLeft--;
                runningTimers.get("teleporttimerteam").setValue(timeLeft);
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
                timeLeft--;
                runningTimers.get("teleporttimerspec").setValue(timeLeft);
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

    public String getTeamDisplayName(String team){

        return TeamsConfig.get().getString("teams." + team + ".colour") + TeamsConfig.get().getString("teams." + team + ".icon") + team;
    }


    public void startSlimeGolf(){
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 61;
            @Override
            public void run() {
                timeLeft--;
                runningTimers.get("slimegolfstart").setValue(timeLeft);
                switch(timeLeft){
                    case 60:
                        teamTeleport("slimegolf", 5);
                        break;
                    case 55:
                        for(Player player : getPlayers()) {
                            PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 90, 1, false, false);
                            player.addPotionEffect(PotionEffect);
                        }
                        break;

                    case 50:
                        for(Player player : getPlayers()) {
                            plugin.messagePlayer(player, """
                                    §8
                                    §8
                                    §8[§e§l?§8] §eWelcome to §a§lSlime Golf§e! The aim of the game is to hit your slimey ball into the hole at the end of the course as quickly as possible!
                                    §8
                                    """);
                        }
                        break;
                    case 30:
                        for(Player player : getPlayers()) {
                            plugin.messagePlayer(player, """
                                    §8
                                    §8
                                    §8[§e§l?§8] §eUse your §aknockback stick §eand work as a team, jump ahead and plan out your strategy, player-sized shortcuts will help you get ahead of the slime for strategic putting strategies!
                                    §8
                                    """);
                        }
                        break;
                    case 10:
                        for(Player player : getPlayers()) {
                            plugin.messagePlayer(player, """
                                    §8
                                    §8
                                    §8[§c§l!§8] §7Game Starting in §c§l10 seconds...
                                    §8
                                    """);
                        }
                        break;
                    case 5, 4, 3, 2, 1:
                        for(Player player : getPlayers()) {
                            player.sendTitle("§c§l▶ " + timeLeft + " ◀", "", 0, 20, 20);
                        }
                        break;
                    case 0:
                        for(Player player : getPlayers()) {
                            player.sendTitle("§a§l▶ GO! ◀", "", 0, 40, 0);
                        }
                        runningTimers.remove("slimegolfstart");
                        cancel();
                        break;
                    default:
                        break;
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("slimegolfstart", new AbstractMap.SimpleEntry<>(task, 61));
    }

    public LinkedHashMap<String, Integer> sortByValue() {

        HashMap<String, Integer> teamPoints = new HashMap<>();

        for (String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)){
            teamPoints.put(team, TeamsConfig.get().getInt("teams." + team + ".points"));
        }

        List<Map.Entry<String, Integer>> list = new LinkedList<>(teamPoints.entrySet());

        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public void getTeamPoints(){
        for(Player player : getPlayers()) {
            messagePlayer(player, "=== Overall Placements ===");
            int placement = 0;
            for (String key : sortByValue().keySet()) {
                placement++;
                messagePlayer(player, placement + ". " + getTeamDisplayName(key) + " §8- §e§l\uD83D\uDCB0" + sortByValue().get(key));
            }
            messagePlayer(player, "======================");
        }
    }

    public void gameEnd(){
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 61;
            @Override
            public void run() {
                timeLeft--;
                runningTimers.get("backtolobby").setValue(timeLeft);
                switch(timeLeft){
                    case 50:
                        slimeGolfTimes();
                        break;
                    case 45:
                        getTeamPoints();
                        break;
                    case 40:
                        teleportPlayers(TeleportConfig.get().getLocation("players.lobby"), 6);
                        break;
                    case 0:
                        runningTimers.remove("slimegolfstart");
                        cancel();
                        break;
                    default:
                        break;
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("backtolobby", new AbstractMap.SimpleEntry<>(task, 61));
    }


    public void slimeGolfTimes(){
        int placement = 1;
        for(Player p : getPlayers()) {
            messagePlayer(p, "=== Hole Times ===");
            for (String team : slimeFinishers.keySet()) {
                messagePlayer(p, placement + ". §e§l⏱§e" + slimeFinishers.get(team) + " §f- " + getTeamDisplayName(team));
            }
            for(String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
                if(!slimeFinishers.containsKey(team)) {
                    messagePlayer(p, "DNF. " + getTeamDisplayName(team));
                }
            }
            messagePlayer(p, "================");

        }
    }

//    public void playerMedal(Player p) {
//        ItemStack[] currentInv = p.getInventory().getContents();
//        p.getInventory().setItemInOffHand();
//        p.setHealth(0.0);
//        p.getInventory().setContents(currentInv);
//    }
}
