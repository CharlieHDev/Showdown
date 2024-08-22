package me.chazzagram.showdown2;

import com.sun.tools.javac.Main;
import me.chazzagram.showdown2.commands.MainCommand;
import me.chazzagram.showdown2.expansions.SpigotExpansion;
import me.chazzagram.showdown2.files.*;
import me.chazzagram.showdown2.listeners.ColourDashEvent;
import me.chazzagram.showdown2.listeners.CraftalotEvent;
import me.chazzagram.showdown2.listeners.PlayerDeath;
import me.chazzagram.showdown2.listeners.VoteWalkEvent;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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
import org.bukkit.scoreboard.Team;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.bukkit.util.NumberConversions.round;

public final class Showdown2 extends JavaPlugin implements Listener {

    private static Showdown2 plugin;

    private double multiplier = 1.0;

    public boolean votingEnabled = false;

    public String currentMode = "Lobby";

    public HashMap<String, Map.Entry<BukkitTask, Integer>> runningTimers = new HashMap<>();

    public ArrayList<String> pausedTimers = new ArrayList<>();

    public HashMap<Integer, Integer> slimeCheckpoints = new HashMap<>();

    public HashMap<String, String> slimeFinishers = new HashMap<>();

    public HashMap<Integer, Integer> bridgeCheckpoints = new HashMap<>();

    public HashMap<String, Integer> bridgeFinishers = new HashMap<>();

    public HashMap<String, Integer> teamCheckpoints = new HashMap<>();

    public HashMap<Player, Material> playerVote = new HashMap<>();

    public HashMap<Material, String> woolModes = new HashMap<>();

    public HashMap<String, Integer> modeVotes = new HashMap<>();

    public HashMap<String, Integer> colourDashCheckpoints = new HashMap<>();

    public HashMap<String, Integer> modeCompletions = new HashMap<>();

    public HashMap<String, Integer> modeTeamPoints = new HashMap<>();

    public HashMap<String, Integer> modePoints = new HashMap<>();

    public HashMap<String, String> itemToCraft = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;

        this.getCommand("mcevent").setExecutor(new MainCommand(this));


        woolModes.put(Material.WHITE_WOOL, "Race");
        woolModes.put(Material.PURPLE_WOOL, "Gub Game");
        woolModes.put(Material.LIME_WOOL, "Slime Golf");
        woolModes.put(Material.ORANGE_WOOL, "Zoomo Go!");
        woolModes.put(Material.RED_WOOL, "Bridge Builders");
        woolModes.put(Material.LIGHT_BLUE_WOOL, "Colour Dash");
        woolModes.put(Material.YELLOW_WOOL, "Craftalot");

        getServer().getPluginManager().registerEvents(new PlayerDeath(this), this);
        getServer().getPluginManager().registerEvents(new VoteWalkEvent(this), this);
        getServer().getPluginManager().registerEvents(new CraftalotEvent(this), this);
        getServer().getPluginManager().registerEvents(new ColourDashEvent(this), this);

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

        CraftalotConfig.setup();
        CraftalotConfig.get().options().copyDefaults(true);
        CraftalotConfig.save();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Bukkit.getPluginManager().registerEvents(this, this);
            new SpigotExpansion(this).register();
        }

        for(int i = 1; i <= 5; i++){
            slimeCheckpoints.put(i, 1);
        }

        for(String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)){
            teamCheckpoints.put(team, 0);
        }

        for(String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)){
            modeCompletions.put(team, 0);
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

        modeTeamPoints.put(playerTeam, modeTeamPoints.get(playerTeam) + multiplyPoints(points));

        if(individual) {
            int currentPoints = PlayerConfig.get().getInt("players." + player + ".points");
            PlayerConfig.get().set("players." + player + ".points", currentPoints+multiplyPoints(points));
            PlayerConfig.save();

            modePoints.put(player, modePoints.get(player) + multiplyPoints(points));
        }
    }

    public void earnTeamPoints(String team, int points){
        int currentTeamPoints = TeamsConfig.get().getInt("teams." + team + ".points");
        TeamsConfig.get().set("teams." + team + ".points", currentTeamPoints+multiplyPoints(points));
        TeamsConfig.save();

        modeTeamPoints.put(team, modeTeamPoints.get(team) + multiplyPoints(points));
    }

    public void resetModePoints(){
        modePoints.clear();
        modeTeamPoints.clear();

        for(String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)){
            for(String player : TeamsConfig.get().getStringList("teams." + team + ".players")){
                modePoints.put(player, 0);
            }
            modeTeamPoints.put(team, 0);
        }
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
                if(!pausedTimers.contains(name)) {
                    runningTimers.get(name).setValue(timeLeft);
                    timeLeft--;
                    if (timeLeft == 0) {
                        messageConsole("Timer finished.");
                        runningTimers.remove(name);
                        cancel();
                    } else {

                        messageConsole(timeLeft + " seconds left..");
                    }
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
                if(!pausedTimers.contains(name)) {
                    timeElapsed++;
                    runningTimers.get(name).setValue(timeElapsed);
                    if (timeElapsed == seconds) {
                        messageConsole("Timer finished.");
                        gameEnd();
                        runningTimers.remove(name);
                        cancel();
                    }
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put(name, new AbstractMap.SimpleEntry<>(task, 0));
    }

    public String getTimer(String timer) {
        return LocalTime.of(0, runningTimers.get(timer).getValue() / 60, runningTimers.get(timer).getValue() % 60).format(DateTimeFormatter.ofPattern("mm:ss"));
    }

    public void pauseEvent(){
        for (Player player : getPlayers()) {
            player.sendTitle("", "§e§lEVENT PAUSED.", 0, 12000, 0);
            messagePlayer(player, """
                        §8
                        §8
                        §e§lEVENT PAUSED.
                        §8
                        """);
        }
        if(pausedTimers.isEmpty()) {
            pausedTimers.addAll(runningTimers.keySet());
        }
    }

    public void resumeEvent(){
        for (Player player : getPlayers()) {
            player.sendTitle("", "§a§lEVENT RESUMED.", 0, 40, 0);
            messagePlayer(player, """
                        §8
                        §8
                        §a§lEVENT RESUMED.
                        §8
                        """);
        }
        if(!pausedTimers.isEmpty()) {
            pausedTimers.clear();
        }
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
                if(!pausedTimers.contains("teleporttimer")) {
                    timeLeft--;
                    runningTimers.get("teleporttimer").setValue(timeLeft);
                    if (timeLeft == 0) {
                        for (Player player : getPlayers()) {
                            player.teleport(location);
                        }
                        runningTimers.remove("teleporttimer");
                        cancel();
                    } else {
                        for (Player player : getPlayers()) {
                            player.sendTitle("", "§6Teleporting in §c" + timeLeft + "...", 0, 20, 20);
                        }
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
                if(!pausedTimers.contains("teleporttimerteam")) {
                    timeLeft--;
                    runningTimers.get("teleporttimerteam").setValue(timeLeft);
                    if (timeLeft == 0) {
                        for (Player player : getPlayers()) {
                            Location tplocation = TeleportConfig.get().getLocation("teams." + PlayerConfig.get().getString("players." + player.getName() + ".team") + "." + location);
                            player.teleport(tplocation);
                        }
                        runningTimers.remove("teleporttimerteam");
                        cancel();
                    } else {
                        for (Player player : getPlayers()) {
                            player.sendTitle("", "§6Teleporting in §c" + timeLeft + "...", 0, 20, 20);
                        }
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
                if(!pausedTimers.contains("teleporttimerspec")) {
                    timeLeft--;
                    runningTimers.get("teleporttimerspec").setValue(timeLeft);
                    if (timeLeft == 0) {
                        for (Player player : getSpectators()) {
                            player.teleport(location);
                        }
                        runningTimers.remove("teleporttimerspec");
                        cancel();
                    } else {
                        for (Player player : getPlayers()) {
                            player.sendTitle("", "§7§oTeleporting in " + timeLeft + "...", 0, 20, 20);
                        }
                    }
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("teleporttimerspec", new AbstractMap.SimpleEntry<>(task, 6));
    }

    public String getTeamDisplayName(String team){

        return TeamsConfig.get().getString("teams." + team + ".colour") + TeamsConfig.get().getString("teams." + team + ".icon") + TeamsConfig.get().getString("teams." + team + ".colour") + "§l" + team;
    }

    public String getPlayerDisplayName(String player){
        String team = PlayerConfig.get().getString("players." + player + ".team");
        return TeamsConfig.get().getString("teams." + team + ".colour") + TeamsConfig.get().getString("teams." + team + ".icon") + player;
    }


    public void startSlimeGolf(){
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 61;
            @Override
            public void run() {
                if(!pausedTimers.contains("slimegolfstart")) {
                    timeLeft--;
                    runningTimers.get("slimegolfstart").setValue(timeLeft);
                    switch (timeLeft) {
                        case 60:
                            teamTeleport("slimegolf", 5);
                            break;
                        case 55:
                            currentMode = "Slime Golf";
                            for (Player player : getPlayers()) {
                                PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 90, 1, false, false);
                                player.addPotionEffect(PotionEffect);
                                player.setGameMode(GameMode.SURVIVAL);
                            }
                            break;

                        case 50:
                            for (Player player : getPlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §8[§e§l?§8] §eWelcome to §a§lSlime Golf§e! The aim of the game is to hit your slimey ball into the hole at the end of the course as quickly as possible!
                                        §8
                                        """);
                            }
                            break;
                        case 30:
                            for (Player player : getPlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §8[§e§l?§8] §eUse your §aknockback stick §eand work as a team, jump ahead and plan out your strategy, player-sized shortcuts will help you get ahead of the slime for strategic putting strategies!
                                        §8
                                        """);
                            }
                            break;
                        case 10:
                            ItemStack knockbackStick = new ItemStack(Material.STICK);
                            knockbackStick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);
                            for (Player player : getPlayers()) {
                                player.getInventory().addItem(knockbackStick);
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §8[§c§l!§8] §7Game Starting in §c§l10 seconds...
                                        §8
                                        """);
                            }
                            break;
                        case 5, 4, 3, 2, 1:
                            for (Player player : getPlayers()) {
                                player.sendTitle("§c§l▶ " + timeLeft + " ◀", "", 0, 20, 20);
                            }
                            break;
                        case 0:
                            for (Player player : getPlayers()) {
                                player.sendTitle("§a§l▶ GO! ◀", "", 0, 40, 0);
                            }
                            startStopwatch(90, "slimegolf");
                            runningTimers.remove("slimegolfstart");
                            cancel();
                            break;
                        default:
                            break;
                    }
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("slimegolfstart", new AbstractMap.SimpleEntry<>(task, 61));
    }

    public void startColourDash(){
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 61;
            @Override
            public void run() {
                if(!pausedTimers.contains("colourdashstart")) {
                    timeLeft--;
                    runningTimers.get("colourdashstart").setValue(timeLeft);
                    switch (timeLeft) {
                        case 60:
                            teamTeleport("colourdash", 5);
                            resetTeamCompletions();
                            break;
                        case 55:
                            currentMode = "Colour Dash";
                            for (Player player : getPlayers()) {
                                PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 90, 1, false, false);
                                player.addPotionEffect(PotionEffect);
                                player.setGameMode(GameMode.SURVIVAL);
                            }
                            break;

                        case 50:
                            for (Player player : getPlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §8[§e§l?§8] §eWelcome to §a§lColour Dash§e! This is a race to the finish, the map is bigger, and there's multiple routes for your team to take so make the right choice!
                                        §8
                                        """);
                            }
                            break;
                        case 30:
                            for (Player player : getPlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §8[§e§l?§8] §eIn this mode speed is the most important factor! The faster you reach each checkpoint, the more points you earn, so get dashing!
                                        §8
                                        """);
                            }
                            break;
                        case 10:

                            for (Player player : getPlayers()) {
                                ItemStack infiniteBlocks = new ItemStack(Material.getMaterial(TeamsConfig.get().getString("teams." + PlayerConfig.get().getString("players." + player.getName() + ".team") + ".colourname") + "_CONCRETE"));
                                infiniteBlocks.setAmount(64);
                                player.getInventory().addItem(infiniteBlocks);
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §8[§c§l!§8] §7Game Starting in §c§l10 seconds...
                                        §8
                                        """);
                            }
                            break;
                        case 5, 4, 3, 2, 1:
                            for (Player player : getPlayers()) {
                                player.sendTitle("§c§l▶ " + timeLeft + " ◀", "", 0, 20, 20);
                            }
                            break;
                        case 0:
                            for (Player player : getPlayers()) {
                                player.sendTitle("§a§l▶ DASH! ◀", "", 0, 40, 0);
                            }
                            startTimer(90, "colourdash");
                            startStopwatch(90, "colourdashwatch");
                            runningTimers.remove("colourdashstart");
                            cancel();
                            break;
                        default:
                            break;
                    }
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("colourdashstart", new AbstractMap.SimpleEntry<>(task, 61));
    }


    public void startCraftalot(){
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 61;
            @Override
            public void run() {
                if(!pausedTimers.contains("craftalotstart")) {
                    timeLeft--;
                    runningTimers.get("craftalotstart").setValue(timeLeft);
                    switch (timeLeft) {
                        case 60:
                            teleportPlayers(TeleportConfig.get().getLocation("players.craftalot"), 5);
                            resetModePoints();
                            break;
                        case 55:
                            currentMode = "Craftalot";
                            for (Player player : getPlayers()) {
                                PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 90, 1, false, false);
                                player.addPotionEffect(PotionEffect);
                                player.setGameMode(GameMode.SURVIVAL);
                            }
                            break;

                        case 50:
                            for (Player player : getPlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §8[§e§l?§8] §eWelcome to §a§lCraftalot§e! We hope you've memorised your crafting recipes! Because this mode is all about retrieving the right materials and crafting what Edguard asks to earn points!
                                        §8
                                        """);
                            }
                            break;
                        case 30:
                            for (Player player : getPlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §8[§e§l?§8] §eRun to Edguard and right-click him to get your orders, then retrieve the needed materials through the pipes marked on the map, and craft as many items as possible before the time runs out!
                                        §8
                                        """);
                            }
                            break;
                        case 10:

                            for (Player player : getPlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §8[§c§l!§8] §7Game Starting in §c§l10 seconds...
                                        §8
                                        """);
                            }
                            break;
                        case 5, 4, 3, 2, 1:
                            for (Player player : getPlayers()) {
                                player.sendTitle("§c§l▶ " + timeLeft + " ◀", "", 0, 20, 20);
                            }
                            break;
                        case 0:
                            for (Player player : getPlayers()) {
                                player.sendTitle("§a§l▶ CRAFT! ◀", "§7Speak to Edguard.", 0, 40, 0);
                                player.getInventory().clear();
                                for(int i = 0; i <= 3; i++){
                                    player.getInventory().addItem(craftalotKit()[i]);
                                }
                                player.getInventory().setItemInOffHand(craftalotKit()[4]);
                            }
                            startTimer(90, "craftalot");
                            runningTimers.remove("craftalotstart");
                            cancel();
                            break;
                        default:
                            break;
                    }
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("craftalotstart", new AbstractMap.SimpleEntry<>(task, 61));
    }

    public void startBridgeBuilders(){
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 61;
            @Override
            public void run() {
                if(!pausedTimers.contains("bridgebuildersstart")) {
                    timeLeft--;
                    runningTimers.get("bridgebuildersstart").setValue(timeLeft);
                    switch (timeLeft) {
                        case 60:
                            teamTeleport("bridgebuilders", 5);
                            resetTeamCompletions();
                            break;
                        case 55:
                            currentMode = "Bridge Builders";
                            for (Player player : getPlayers()) {
                                PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 90, 1, false, false);
                                player.addPotionEffect(PotionEffect);
                                player.setGameMode(GameMode.ADVENTURE);
                            }
                            break;

                        case 50:
                            for (Player player : getPlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §8[§e§l?§8] §eWelcome to §a§lBridge Builders§e! Creative mode is your ally! In this mode you build the course which you will race across! A mix of building and parkour skills!
                                        §8
                                        """);
                            }
                            break;
                        case 30:
                            for (Player player : getPlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §8[§e§l?§8] §eEach segment has a different set of jumps to build, replicate the build and it will construct itself on the bridge for you to complete. First to the finish wins!
                                        §8
                                        """);
                            }
                            break;
                        case 10:

                            for (Player player : getPlayers()) {
                                ItemStack infiniteBlocks = new ItemStack(Material.getMaterial(TeamsConfig.get().getString("teams." + PlayerConfig.get().getString("players." + player.getName() + ".team") + ".colourname") + "_CONCRETE"));
                                infiniteBlocks.setAmount(64);
                                player.getInventory().addItem(infiniteBlocks);
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §8[§c§l!§8] §7Game Starting in §c§l10 seconds...
                                        §8
                                        """);
                            }
                            break;
                        case 5, 4, 3, 2, 1:
                            for (Player player : getPlayers()) {
                                player.sendTitle("§c§l▶ " + timeLeft + " ◀", "", 0, 20, 20);
                            }
                            break;
                        case 0:
                            for (Player player : getPlayers()) {
                                player.sendTitle("§a§l▶ BUILD! ◀", "", 0, 40, 0);
                            }
                            startTimer(90, "bridgebuilders");
                            runningTimers.remove("bridgebuildersstart");
                            cancel();
                            break;
                        default:
                            break;
                    }
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("bridgebuildersstart", new AbstractMap.SimpleEntry<>(task, 61));
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

    public LinkedHashMap<String, Integer> sortMap(HashMap map) {

        List<Map.Entry<String, Integer>> list = new LinkedList<>(map.entrySet());

        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }


    public void getTeamModePoints(){
        for(Player player : getPlayers()) {
            messagePlayer(player, "=== Mode Team Leaderboard ===");
            int placement = 0;
            for (String key : sortMap(modeTeamPoints).keySet()) {
                placement++;
                messagePlayer(player, String.format("%-15s%15s", placement + ". " + getTeamDisplayName(key), "§e§l\uD83D\uDCB0" + sortMap(modeTeamPoints).get(key)));
            }
            messagePlayer(player, "=======================");
        }
    }

    public void getPlayerModePoints(){
        for(Player player : getPlayers()) {
            messagePlayer(player, "=== Mode Indiv Leaderboard ===");
            List<String> players = new ArrayList<>(sortMap(modePoints).keySet());
            List<Integer> points = new ArrayList<>(sortMap(modePoints).values());
            for (int i = 1; i <= 8; i++) {
                if(players.get(i) != null && points.get(i) != null) {
                    messagePlayer(player, String.format("%-15s%15s", i + ". " + getPlayerDisplayName(players.get(i)), "§e§l\uD83D\uDCB0" + points.get(i)));
                }
            }
            messagePlayer(player, "=======================");
        }
    }

    public void countVotes(){
        modeVotes.clear();
        int totalvotes = 0;
        for(int i = 356; i <= 360; i++){
            for(int j = -400; j <= -396; j++){
                totalvotes++;
                Material block = Bukkit.getServer().getWorld("world").getBlockAt(j, 62, i).getType();
                if(woolModes.containsKey(block)) {
                    if (modeVotes.containsKey(woolModes.get(block))) {
                        modeVotes.put(woolModes.get(block), modeVotes.get(woolModes.get(block)) + 1);
                    } else {
                        modeVotes.put(woolModes.get(block), 1);
                    }
                }
            }
        }
        List<String> leaderMode = new ArrayList<>(sortMap(modeVotes).keySet());
        List<Integer> leaderModeVotes = new ArrayList<>(sortMap(modeVotes).values());
        for(Player player : getPlayers()) {
            player.sendTitle("§e§l" + leaderMode.getFirst(), "", 0, 60, 40);
            messagePlayer(player, "=== Voting Results ===");
            int placement = 0;
            for (String key : leaderMode) {
                placement++;
                float percentage = ((float) leaderModeVotes.get(placement - 1) /totalvotes)*100;
                messagePlayer(player, placement + ". " + key + ": §e§l" + leaderModeVotes.get(placement-1) + " spaces §e§o(" + percentage + "%)");
            }
        }
    }

    public void startVoting(){
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 91;
            @Override
            public void run() {
                if(!pausedTimers.contains("voting")) {
                    timeLeft--;
                    runningTimers.get("voting").setValue(timeLeft);
                    switch (timeLeft) {
                        case 90:
                            for (int i = 356; i <= 360; i++) {
                                for (int j = -400; j <= -396; j++) {
                                    Bukkit.getServer().getWorld("world").getBlockAt(j, 62, i).setType(Material.BLACK_WOOL);
                                }
                            }
                            for (Player player : getPlayers()) {
                                player.sendTitle("§e§lVoting Time!", "", 0, 60, 40);
                            }
                            break;
                        case 75:
                            teleportPlayers(TeleportConfig.get().getLocation("players.votearena"), 0);
                            for (Player player : getPlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §e§lVoting Time!
                                        §fWalk into your mode of choice and paint the floor with as many blocks as possible by running around! If you change your mind simply run back to the mode selection and get painting!
                                        §8
                                        """);
                            }
                            break;
                        case 60:
                            votingEnabled = true;
                            for (Player player : getPlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §6§lVoting Enabled.
                                        §fVotes will be counted in 30 seconds!
                                        §8
                                        """);
                            }
                            break;
                        case 30:
                            votingEnabled = false;
                            for (Player player : getPlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §c§lVoting Period Ended.
                                        §fVotes will now be calculated!
                                        §8
                                        """);
                            }
                            break;
                        case 25:
                            for (Player player : getPlayers()) {
                                player.sendTitle("§7§k000000000", "", 0, 100, 40);
                            }
                            break;
                        case 20:
                            countVotes();
                            break;
                        case 0:
                            runningTimers.remove("voting");
                            cancel();
                            break;
                        default:
                            break;
                    }
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("voting", new AbstractMap.SimpleEntry<>(task, 91));
    }

    public void gameEnd(){
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 61;
            @Override
            public void run() {
                if(!pausedTimers.contains("backtolobby")) {
                    timeLeft--;
                    runningTimers.get("backtolobby").setValue(timeLeft);
                    switch (timeLeft) {
                        case 60:
                            for (Player player : getPlayers()) {
                                player.sendTitle("§c§lGAME OVER!", "", 0, 60, 40);
                            }
                            break;
                        case 50:
                            if(currentMode.equals("Slime Golf")) {
                                slimeGolfTimes();
                            } else {
                                getPlayerModePoints();
                            }
                            break;
                        case 45:
                            getTeamModePoints();
                            break;
                        case 40:
                            for (Player player : getPlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §8[§c§l🛫§8] §cTeleporting back to the lobby..
                                        §8
                                        """);
                            }
                            teleportPlayers(TeleportConfig.get().getLocation("players.lobby"), 5);
                            break;
                        case 35:
                            for (Player player : getPlayers()) {
                                player.setGameMode(GameMode.ADVENTURE);
                            }
                            break;
                        case 0:
                            runningTimers.remove("backtolobby");
                            cancel();
                            break;
                        default:
                            break;
                    }
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("backtolobby", new AbstractMap.SimpleEntry<>(task, 61));
    }


    public void slimeGolfTimes(){
        for(Player p : getPlayers()) {
            messagePlayer(p, "=== Hole Times ===");
            for (String team : slimeFinishers.keySet()) {
                messagePlayer(p, "§e§l⏱§e" + slimeFinishers.get(team) + " §f- " + getTeamDisplayName(team));
            }
            for(String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
                if(!slimeFinishers.containsKey(team)) {
                    messagePlayer(p, "DNF. " + getTeamDisplayName(team));
                }
            }
            messagePlayer(p, "================");

        }
    }

    public StringBuilder getTeamProgress(String team){
        StringBuilder progressBar = new StringBuilder();
        progressBar.append(TeamsConfig.get().getString("teams." + team + ".colour")).append(TeamsConfig.get().getString("teams." + team + ".icon"));
        if(runningTimers.containsKey("slimegolf")) {
            if (teamCheckpoints.get(team) > 5) {
                progressBar.append(TeamsConfig.get().getString("teams." + team + ".colour")).append("⬛".repeat(5)).append("§e⬛");
            } else {
                progressBar.append("⬛".repeat(Math.max(0, teamCheckpoints.get(team))));
                progressBar.append("§0⬛".repeat(6 - teamCheckpoints.get(team)));
            }
        } else {
            progressBar.append("§0⬛");
        }
        return progressBar;
    }

    public StringBuilder getCompletionProgress(String team){
        StringBuilder teamProgressBar = new StringBuilder();
        String colouredTeamIcon = TeamsConfig.get().getString("teams." + team + ".colour") + TeamsConfig.get().getString("teams." + team + ".icon");
        String teamIcon = "§0" + TeamsConfig.get().getString("teams." + team + ".icon");
        teamProgressBar.append(colouredTeamIcon.repeat(modeCompletions.get(team)));
        teamProgressBar.append(teamIcon.repeat(4-modeCompletions.get(team)));
        return teamProgressBar;
    }

    public void resetTeamCompletions(){
        modeCompletions.clear();
        for(String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)){
            modeCompletions.put(team, 0);
        }
    }

    public ItemStack[] craftalotKit(){

        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        ItemStack pickaxe = new ItemStack(Material.IRON_PICKAXE);
        ItemStack axe = new ItemStack(Material.IRON_AXE);
        ItemStack shovel = new ItemStack(Material.IRON_SHOVEL);
        ItemStack trident = new ItemStack(Material.TRIDENT);

        return new ItemStack[]{sword, pickaxe, axe, shovel, trident};
    }

//    public void playerMedal(Player p) {
//        ItemStack[] currentInv = p.getInventory().getContents();
//        p.getInventory().setItemInOffHand();
//        p.setHealth(0.0);
//        p.getInventory().setContents(currentInv);
//    }
}
