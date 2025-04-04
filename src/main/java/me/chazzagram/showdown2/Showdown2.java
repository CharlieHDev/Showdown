package me.chazzagram.showdown2;
import fr.skytasul.glowingentities.GlowingEntities;
import me.chazzagram.showdown2.commands.MainCommand;
import me.chazzagram.showdown2.expansions.SpigotExpansion;
import me.chazzagram.showdown2.files.*;
import me.chazzagram.showdown2.listeners.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.bukkit.util.NumberConversions.round;

public final class Showdown2 extends JavaPlugin implements Listener {

    private static Showdown2 plugin;

    private double multiplier = 1.0;

    public boolean votingEnabled = false;

    public boolean pvpEnabled = false;

    public String currentMode = "Lobby";

    public boolean doubleJumpEnabled = false;

    public HashMap<String, Map.Entry<BukkitTask, Integer>> runningTimers = new HashMap<>();

    public ArrayList<String> pausedTimers = new ArrayList<>();

    public HashMap<Integer, Integer> slimeCheckpoints = new HashMap<>();

    public HashMap<String, String> slimeFinishers = new HashMap<>();

    public HashMap<Integer, Integer> bridgeCheckpoints = new HashMap<>();

    public HashMap<Integer, Integer> bridgeJumpCheckpoints = new HashMap<>();

    public HashMap<Integer, List<String>> bridgeJumpRegister = new HashMap<>();

    public HashMap<String, Integer> teamCheckpoints = new HashMap<>();

    public HashMap<Player, Material> playerVote = new HashMap<>();

    public HashMap<Material, String> woolModes = new HashMap<>();

    public HashMap<Material, Color> woolColors = new HashMap<>();

    public HashMap<String, Integer> modeVotes = new HashMap<>();

    public HashMap<String, Integer> colourDashCheckpoints = new HashMap<>();

    public ArrayList<String> deadPlayers = new ArrayList<>();

    public ArrayList<String> deadTeams = new ArrayList<>();

    public HashMap<String, String> lastHitPlayer = new HashMap<>();

    public HashMap<String, Integer> modeCompletions = new HashMap<>();

    public HashMap<String, Integer> modeTeamPoints = new HashMap<>();

    public HashMap<String, Integer> modePoints = new HashMap<>();

    public HashMap<String, String> itemToCraft = new HashMap<>();

    public HashMap<String, Integer> readyPlayers = new HashMap<>();

    public HashMap<String, Color> teamColors = new HashMap<>();

    public HashMap<String, Integer> gubGameKills = new HashMap<>();

    public HashMap<Material, String> woolLogos = new HashMap<>();

    public HashMap<String, ChatColor> modeColors = new HashMap<>();

    public HashMap<Integer, Integer> gubKitKills = new HashMap<>();

    public HashMap<String, ChatColor> teamGlowColors = new HashMap<>();

    public List<String> powerUpHolders = new ArrayList<>();

    public GlowingEntities glowingEntities;

    public LivingEntity chickenBall;

    public Inventory gui = Bukkit.createInventory(null, 36, "§eTeams");

    public Player slimeBallVote;

    public List<String> killRecord = new ArrayList<>();

    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;

        glowingEntities = new GlowingEntities(plugin);

        this.getCommand("mcevent").setExecutor(new MainCommand(this));

        teamColors.put("RubyRaiders", Color.RED);
        teamColors.put("AmberAmbushers", Color.ORANGE);
        teamColors.put("TopazTroopers", Color.YELLOW);
        teamColors.put("KyaniteKillers", Color.LIME);
        teamColors.put("DiamondDestroyers", Color.AQUA);
        teamColors.put("SapphireSoldiers", Color.BLUE);
        teamColors.put("SmithsoniteSlayers", Color.FUCHSIA);
        teamColors.put("CrystalCrashers", Color.WHITE);

        teamGlowColors.put("RubyRaiders", ChatColor.RED);
        teamGlowColors.put("AmberAmbushers", ChatColor.GOLD);
        teamGlowColors.put("TopazTroopers", ChatColor.YELLOW);
        teamGlowColors.put("KyaniteKillers", ChatColor.GREEN);
        teamGlowColors.put("DiamondDestroyers", ChatColor.AQUA);
        teamGlowColors.put("SapphireSoldiers", ChatColor.BLUE);
        teamGlowColors.put("SmithsoniteSlayers", ChatColor.LIGHT_PURPLE);
        teamGlowColors.put("CrystalCrashers", ChatColor.WHITE);

        woolModes.put(Material.WHITE_WOOL, "Survival Games");
        woolModes.put(Material.PURPLE_WOOL, "Gub Game");
        woolModes.put(Material.LIME_WOOL, "Slime Golf");
        woolModes.put(Material.ORANGE_WOOL, "Zoomo Go!");
        woolModes.put(Material.RED_WOOL, "Bridge Builders");
        woolModes.put(Material.LIGHT_BLUE_WOOL, "Colour Dash");
        woolModes.put(Material.YELLOW_WOOL, "Craftalot");

        woolColors.put(Material.WHITE_WOOL, Color.WHITE);
        woolColors.put(Material.PURPLE_WOOL, Color.PURPLE);
        woolColors.put(Material.LIME_WOOL, Color.LIME);
        woolColors.put(Material.ORANGE_WOOL, Color.ORANGE);
        woolColors.put(Material.RED_WOOL, Color.RED);
        woolColors.put(Material.LIGHT_BLUE_WOOL, Color.AQUA);
        woolColors.put(Material.YELLOW_WOOL, Color.YELLOW);

        modeColors.put("Survival Games", ChatColor.WHITE);
        modeColors.put("Gub Game", ChatColor.LIGHT_PURPLE);
        modeColors.put("Slime Golf", ChatColor.GREEN);
        modeColors.put("Zoomo Go!", ChatColor.GOLD);
        modeColors.put("Bridge Builders", ChatColor.RED);
        modeColors.put("Colour Dash", ChatColor.AQUA);
        modeColors.put("Craftalot", ChatColor.YELLOW);

        woolLogos.put(Material.WHITE_WOOL, "\uD83E\uDD6C");
        woolLogos.put(Material.PURPLE_WOOL, "\uD83E\uDED0");
        woolLogos.put(Material.LIME_WOOL, "\uE172");
        woolLogos.put(Material.ORANGE_WOOL, "\uD83E\uDD55");
        woolLogos.put(Material.RED_WOOL, "\uD83C\uDF45");
        woolLogos.put(Material.LIGHT_BLUE_WOOL, "\uD83E\uDD68");
        woolLogos.put(Material.YELLOW_WOOL, "\ue238");

        getServer().getPluginManager().registerEvents(new InventoryEvent(this), this);
        getServer().getPluginManager().registerEvents(new PickupItemEvent(this), this);
        getServer().getPluginManager().registerEvents(new DoubleJumpEvent(this), this);
        getServer().getPluginManager().registerEvents(new DropItemEvent(this), this);
        getServer().getPluginManager().registerEvents(new LastHitEvent(this), this);
        getServer().getPluginManager().registerEvents(new VoteWalkEvent(this), this);
        getServer().getPluginManager().registerEvents(new CraftalotEvent(this), this);
        getServer().getPluginManager().registerEvents(new ColourDashEvent(this), this);
        getServer().getPluginManager().registerEvents(new ReadyEvent(this), this);
        getServer().getPluginManager().registerEvents(new VoteSneakBombEvent(this), this);

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

        for (int i = 1; i <= 5; i++) {
            slimeCheckpoints.put(i, 1);
        }

        for (int i = 1; i <= 5; i++) {
            bridgeCheckpoints.put(i, 1);
        }

        for (int i = 1; i <= 5; i++) {
            bridgeJumpCheckpoints.put(i, 1);
        }

        for (int i = 1; i <= 5; i++) {
            List<String> list = new ArrayList<>();
            bridgeJumpRegister.put(i, list);
        }

        for (String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
            teamCheckpoints.put(team, 0);
        }

        for (String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
            modeCompletions.put(team, 0);
        }

        for (Player player : getPlayers()) {
            modePoints.put(player.getName(), 0);
        }

        for (String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
            modeTeamPoints.put(team, 0);
        }

        for (Player player : getPlayers()) {
            readyPlayers.put(player.getName(), 0);
        }

    }

    @Override
    public void onDisable() {
        glowingEntities.disable();
        // Plugin shutdown logic
        messageConsole("Plugin Unloaded.");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.getPlayer().sendTitle("§7§oLoading...", "", 0, 10, 0);
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
                for (String player : SpectatorConfig.get().getConfigurationSection("spectators").getKeys(false)) {
                    if (p.getName().equals(player)) {
                        playerFound = true;
                        break;
                    }
                }
            }
            if(!playerFound){
                SpectatorConfig.get().set("spectators." + p.getName() + ".points", 0);
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
        for(String spectator : SpectatorConfig.get().getConfigurationSection("spectators").getKeys(false)){
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
                if(runningTimers.containsKey(name)) {
                    if (!pausedTimers.contains(name)) {
                        runningTimers.get(name).setValue(timeLeft);
                        timeLeft--;
                        if (timeLeft == 0) {
                            messageConsole("Timer finished.");
                            gameEnd();
                            runningTimers.remove(name);
                            cancel();
                        } else {

                            messageConsole(timeLeft + " seconds left..");
                        }
                    }
                } else {
                    messageConsole("Timer removed by external factor.");
                    cancel();
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
        if(location != null) {
            BukkitTask task = new BukkitRunnable() {
                int timeLeft = countdown + 1;

                @Override
                public void run() {
                    if (!pausedTimers.contains("teleporttimer")) {
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
        } else {
            messageConsole("ERROR: Location not found.");
        }
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
    public void teleportSpectators(Location location, int countdown){
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = countdown+1;
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
                        for (Player player : getSpectators()) {
                            player.sendTitle("", "§7§oTeleporting in " + timeLeft + "...", 0, 20, 20);
                        }
                    }
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("teleporttimerspec", new AbstractMap.SimpleEntry<>(task, countdown+1));
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
                            teleportSpectators(TeleportConfig.get().getLocation("spectators.slimegolf"), 5);
                            resetModePoints();
                            resetSlimeCompletions();
                            break;
                        case 55:
                            try {
                                glowTeams();
                            } catch (ReflectiveOperationException e) {
                                throw new RuntimeException(e);
                            }
                            currentMode = "Slime Golf";
                            for (Player player : getPlayers()) {
                                PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 90, 1, false, false);
                                player.addPotionEffect(PotionEffect);
                                player.setGameMode(GameMode.SURVIVAL);
                            }
                            break;

                        case 50:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
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
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
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
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
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
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 0.3F*(6-timeLeft));
                            for (Player player : getPlayers()) {
                                player.sendTitle("§c§l▶ " + timeLeft + " ◀", "", 0, 20, 20);
                            }
                            break;
                        case 0:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 2);
                            for (Player player : getPlayers()) {
                                player.sendTitle("§a§l▶ GO! ◀", "", 0, 40, 0);
                            }
                            startTimer(90, "slimegolftimer");
                            startStopwatch(90, "slimegolf");
                            pvpEnabled = true;
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
                            teleportSpectators(TeleportConfig.get().getLocation("spectators.colourdash"), 5);
                            resetTeamCompletions();
                            resetModePoints();
                            break;
                        case 55:
                            try {
                                glowTeams();
                            } catch (ReflectiveOperationException e) {
                                throw new RuntimeException(e);
                            }
                            currentMode = "Colour Dash";
                            for (Player player : getPlayers()) {
                                PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 90, 1, false, false);
                                player.addPotionEffect(PotionEffect);
                                player.setGameMode(GameMode.SURVIVAL);
                            }
                            break;

                        case 50:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
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
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
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
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
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
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 0.3F*(6-timeLeft));
                            for (Player player : getPlayers()) {
                                player.sendTitle("§c§l▶ " + timeLeft + " ◀", "", 0, 20, 20);
                            }
                            break;
                        case 0:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 2);
                            pvpEnabled = true;
                            for (Player player : getPlayers()) {
                                ItemStack infiniteBlocks = new ItemStack(Material.getMaterial(TeamsConfig.get().getString("teams." + PlayerConfig.get().getString("players." + player.getName() + ".team") + ".colourname") + "_CONCRETE"));
                                infiniteBlocks.setAmount(64);
                                player.getInventory().addItem(infiniteBlocks);
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
                            teleportSpectators(TeleportConfig.get().getLocation("spectators.craftalot"), 5);
                            resetCraftalot();
                            resetModePoints();
                            break;
                        case 55:
                            try {
                                glowTeams();
                            } catch (ReflectiveOperationException e) {
                                throw new RuntimeException(e);
                            }
                            currentMode = "Craftalot";
                            for (Player player : getPlayers()) {
                                PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 90, 1, false, false);
                                player.addPotionEffect(PotionEffect);
                                player.setGameMode(GameMode.SURVIVAL);
                            }
                            break;

                        case 50:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
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
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
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
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
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
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 0.3F*(6-timeLeft));
                            for (Player player : getPlayers()) {
                                player.sendTitle("§c§l▶ " + timeLeft + " ◀", "", 0, 20, 20);
                            }
                            break;
                        case 0:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 2);
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

    public void resetCraftalot(){
        itemToCraft.clear();
        for(Player player : getPlayers()) {
            itemToCraft.put(player.getName(), "");
        }
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
                            teleportSpectators(TeleportConfig.get().getLocation("spectators.bridgebuilders"), 5);
                            resetBridgeBuilders();
                            resetModePoints();
                            break;
                        case 55:
                            try {
                                glowTeams();
                            } catch (ReflectiveOperationException e) {
                                throw new RuntimeException(e);
                            }
                            currentMode = "Bridge Builders";
                            for (Player player : getPlayers()) {
                                PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 90, 1, false, false);
                                player.addPotionEffect(PotionEffect);
                                player.setGameMode(GameMode.ADVENTURE);
                            }
                            break;

                        case 50:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
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
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
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
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
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
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 0.3F*(6-timeLeft));
                            for (Player player : getPlayers()) {
                                player.sendTitle("§c§l▶ " + timeLeft + " ◀", "", 0, 20, 20);
                            }
                            break;
                        case 0:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 2);
                            for (Player player : getPlayers()) {
                                player.setGameMode(GameMode.CREATIVE);
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

    public void resetBridgeBuilders(){
        bridgeCheckpoints.clear();
        bridgeJumpCheckpoints.clear();
        bridgeJumpRegister.clear();

        for(int i = 1; i <= 5; i++){
            bridgeCheckpoints.put(i, 1);
        }

        for(int i = 1; i <= 5; i++){
            bridgeJumpCheckpoints.put(i, 1);
        }

        for(int i = 1; i <= 5; i++){
            List<String> list = new ArrayList<>();
            bridgeJumpRegister.put(i, list);
        }
    }


    public void startZoomoGo(){
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 61;
            @Override
            public void run() {
                if(!pausedTimers.contains("zoomogostart")) {
                    timeLeft--;
                    runningTimers.get("zoomogostart").setValue(timeLeft);
                    switch (timeLeft) {
                        case 60:
                            for(Player p : getPlayers()){
                                lastHitPlayer.put(p.getName(), "");
                            }
                            teamTeleport("zoomogo", 5);
                            teleportSpectators(TeleportConfig.get().getLocation("spectators.zoomogo"), 5);
                            resetZoomoGo();
                            resetModePoints();
                            break;
                        case 55:
                            currentMode = "Zoomo Go";
                            try {
                                glowTeams();
                            } catch (ReflectiveOperationException e) {
                                throw new RuntimeException(e);
                            }
                            for (Player player : getPlayers()) {
                                PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 120, 1, false, false);
                                player.addPotionEffect(PotionEffect);
                                player.setGameMode(GameMode.ADVENTURE);
                            }
                            for (int i = 1; i <= 26; i++){
                                summonIsland(zoomoIslands(i));
                                if(i > 5){
                                    destroyIsland(zoomoIslands(i));
                                }
                                summonIsland(zoomoIslands(27));
                            }
                            break;

                        case 50:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
                            for (Player player : getPlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §8[§e§l?§8] §eWelcome to §a§lZoomo Go§e! Keep moving fast and stay on the platforms! This game takes sumo to the next level with disappearing platforms and fast-movement gameplay! Watch out for §cRed Platforms§e.
                                        §8
                                        """);
                            }
                            break;
                        case 40:
                            doubleJumpEnabled = true;
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
                            for (Player player : getPlayers()) {
                                player.sendTitle("§c§lDouble Jump", "§7is now enabled.", 0, 20, 20);
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §8[§e§l?§8] §c§lDouble Jump §ehas been §aenabled§e! Give it a go by double-tapping your space bar!
                                        §8
                                        """);
                            }
                            break;
                        case 30:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
                            for (Player player : getPlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §8[§e§l?§8] §eThere is no finish line, simply stay alive and knock other players off the platforms with your §aKnockback Stick§e! §bDouble jump §eis your ally, double-tap space bar and you will be sent flying in the direction you are facing!
                                        §8
                                        """);
                            }
                            break;
                        case 10:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
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
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 0.3F*(6-timeLeft));
                            for (Player player : getPlayers()) {
                                player.sendTitle("§c§l▶ " + timeLeft + " ◀", "", 0, 20, 20);
                            }
                            break;
                        case 0:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 2);
                            pvpEnabled = true;
                            for (Player player : getPlayers()) {
                                player.sendTitle("§a§l▶ ZOOMO GO! ◀", "", 0, 40, 0);
                            }
                            runningTimers.remove("zoomogostart");
                            cancel();
                            initiateZoomoIslands();
                            break;
                        default:
                            break;
                    }
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("zoomogostart", new AbstractMap.SimpleEntry<>(task, 61));
    }

    public void initiateZoomoIslands(){
        BukkitTask task = new BukkitRunnable() {
            int y = 72;
            int timeLeft = 141;
            @Override
            public void run() {
                if(runningTimers.containsKey("zoomogo")) {
                    if (!pausedTimers.contains("zoomogo")) {
                        timeLeft--;
                        runningTimers.get("zoomogo").setValue(timeLeft);
                        switch (timeLeft) {
                            case 140:
                                destroyIsland(zoomoIslands(27));
                                break;
                            case 128:
                                summonIsland(zoomoIslands(6));
                                break;
                            case 134:
                                summonIsland(zoomoIslands(10));
                                break;
                            case 133:
                                summonIsland(zoomoIslands(7));
                                break;
                            case 132:
                                destroyIsland(zoomoIslands(1));
                                break;
                            case 131:
                                destroyIsland(zoomoIslands(2));
                                break;
                            case 130:
                                destroyIsland(zoomoIslands(3));
                                break;
                            case 129:
                                destroyIsland(zoomoIslands(4));
                                break;
                            case 122:
                                summonIsland(zoomoIslands(8));
                                break;
                            case 121:
                                summonIsland(zoomoIslands(9));
                                break;
                            case 120:
                                summonIsland(zoomoIslands(12));
                                break;
                            case 119:
                                summonIsland(zoomoIslands(11));
                                break;
                            case 118:
                                destroyIsland(zoomoIslands(5));
                                break;
                            case 117:
                                destroyIsland(zoomoIslands(6));
                                break;
                            case 116:
                                destroyIsland(zoomoIslands(10));
                                break;
                            case 115:
                                destroyIsland(zoomoIslands(7));
                                break;
                            case 107:
                                summonIsland(zoomoIslands(13));
                                break;
                            case 106:
                                summonIsland(zoomoIslands(14));
                                break;
                            case 105:
                                summonIsland(zoomoIslands(15));
                                break;
                            case 104:
                                destroyIsland(zoomoIslands(8));
                                break;
                            case 103:
                                destroyIsland(zoomoIslands(11));
                                break;
                            case 102:
                                destroyIsland(zoomoIslands(12));
                                break;
                            case 101:
                                destroyIsland(zoomoIslands(9));
                                break;
                            case 93:
                                summonIsland(zoomoIslands(16));
                                break;
                            case 92:
                                summonIsland(zoomoIslands(17));
                                break;
                            case 91:
                                summonIsland(zoomoIslands(18));
                                break;
                            case 90:
                                destroyIsland(zoomoIslands(13));
                                break;
                            case 89:
                                destroyIsland(zoomoIslands(14));
                                break;
                            case 88:
                                destroyIsland(zoomoIslands(15));
                                break;
                            case 79:
                                summonIsland(zoomoIslands(19));
                                break;
                            case 78:
                                summonIsland(zoomoIslands(20));
                                break;
                            case 77:
                                summonIsland(zoomoIslands(22));
                                break;
                            case 76:
                                destroyIsland(zoomoIslands(16));
                                break;
                            case 75:
                                destroyIsland(zoomoIslands(17));
                                break;
                            case 74:
                                destroyIsland(zoomoIslands(18));
                                break;
                            case 65:
                                summonIsland(zoomoIslands(21));
                                break;
                            case 64:
                                summonIsland(zoomoIslands(23));
                                break;
                            case 62:
                                destroyIsland(zoomoIslands(19));
                                break;
                            case 61:
                                destroyIsland(zoomoIslands(20));
                                break;
                            case 60:
                                destroyIsland(zoomoIslands(22));
                                break;
                            case 51:
                                summonIsland(zoomoIslands(24));
                                break;
                            case 50:
                                summonIsland(zoomoIslands(25));
                                break;
                            case 49:
                                destroyIsland(zoomoIslands(21));
                                break;
                            case 48:
                                destroyIsland(zoomoIslands(23));
                                break;
                            case 37:
                                summonIsland(zoomoIslands(26));
                                break;
                            case 34:
                                destroyIsland(zoomoIslands(24));
                                break;
                            case 28:
                                destroyIsland(zoomoIslands(25));
                                break;
                            case 12:
                                destroyIsland(zoomoIslands(26));
                                break;
                            case 0:
                                runningTimers.remove("zoomogo");
                                cancel();
                                break;
                            default:
                                break;

                        }
                    }
                } else {
                    messageConsole("Timer removed by external factor.");
                    cancel();
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("zoomogo", new AbstractMap.SimpleEntry<>(task, 141));
    }

    public int[] zoomoIslands(int index){

        int[][] coords = {
                {-584, 330},
                {-612, 321},
                {-612, 339},
                {-582, 351},
                {-610, 362},
                {-596, 380},
                {-569, 394},
                {-594, 413},
                {-572, 426},
                {-614, 394},
                {-590, 440},
                {-548, 409},
                {-533, 431},
                {-519, 464},
                {-555, 457},
                {-543, 485},
                {-573, 479},
                {-599, 492},
                {-586, 514},
                {-612, 516},
                {-599, 554},
                {-569, 532},
                {-574, 559},
                {-598, 578},
                {-566, 585},
                {-575, 615},
                {-597, 340}
        };

        return coords[index-1];
    }

    public int[][] getZoomoSpawnBox(){
        return new int[][]{
            {-600, 124, 364},
            {-605, 124, 350},
            {-600, 124, 336},
            {-586, 124, 331},
            {-572, 124, 336},
            {-567, 124, 350},
            {-572, 124, 364},
            {-586, 124, 369}
        };
    }

    public void summonIsland(int[] islandCoords){
        Bukkit.getWorld("world").getBlockAt(islandCoords[0], 74, islandCoords[1]).setType(Material.REDSTONE_BLOCK);
        Bukkit.getWorld("world").getBlockAt(islandCoords[0], 74, islandCoords[1]).setType(Material.STONE);
    }

    public void destroyIsland(int[] islandCoords){
        final String name = ("islandtimer" + runningTimers.size()+1);
        Bukkit.getWorld("world").getBlockAt(islandCoords[0], 73, islandCoords[1]).setType(Material.REDSTONE_BLOCK);
        Bukkit.getWorld("world").getBlockAt(islandCoords[0], 73, islandCoords[1]).setType(Material.STONE);
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 6;
            @Override
            public void run() {
                if(runningTimers.containsKey(name)) {
                    if (!pausedTimers.contains(name)) {
                        runningTimers.get(name).setValue(timeLeft);
                        timeLeft--;
                        if (timeLeft == 0) {
                            Bukkit.getWorld("world").getBlockAt(islandCoords[0], 72, islandCoords[1]).setType(Material.REDSTONE_BLOCK);
                            Bukkit.getWorld("world").getBlockAt(islandCoords[0], 72, islandCoords[1]).setType(Material.STONE);
                            runningTimers.remove(name);
                            cancel();
                        }
                    }
                } else {
                    messageConsole("Timer removed by external factor.");
                    cancel();
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put(name, new AbstractMap.SimpleEntry<>(task, 6));
    }

    public void resetZoomoGo(){
        deadPlayers.clear();
        deadTeams.clear();
    }



    public void startGubGame(){
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 61;
            @Override
            public void run() {
                if(!pausedTimers.contains("gubgamestart")) {
                    timeLeft--;
                    runningTimers.get("gubgamestart").setValue(timeLeft);
                    switch (timeLeft) {
                        case 60:
                            for(Player p : getPlayers()){
                                lastHitPlayer.put(p.getName(), "");
                            }
                            teamTeleport("gubgame", 5);
                            teleportSpectators(TeleportConfig.get().getLocation("spectators.gubgame"), 5);
                            resetGubGame();
                            resetModePoints();
                            break;
                        case 55:
                            try {
                                glowTeams();
                            } catch (ReflectiveOperationException e) {
                                throw new RuntimeException(e);
                            }
                            currentMode = "Gub Game";
                            for (Player player : getPlayers()) {
                                PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 90, 1, false, false);
                                player.addPotionEffect(PotionEffect);
                                player.setGameMode(GameMode.ADVENTURE);
                            }
                            break;

                        case 50:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
                            for (Player player : getPlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §8[§e§l?§8] §eWelcome to §d§lGub Game§e! In this game it's all about kills, every player for themselves. Get 15 kills as quickly as possible before the time runs out.
                                        §8
                                        """);
                            }
                            break;
                        case 30:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
                            for (Player player : getPlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §8[§e§l?§8] §eEach player starts out with highly powered weapons, but with each kill, you are given harder weapons to get kills with! Until you are left with your bare fists...
                                        §8
                                        """);
                            }
                            break;
                        case 10:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
                            ItemStack firstKit = new ItemStack(Material.NETHERITE_SWORD);
                            ItemMeta meta = firstKit.getItemMeta();
                            meta.setUnbreakable(true);
                            firstKit.setItemMeta(meta);
                            for (Player player : getPlayers()) {
                                player.getInventory().addItem(firstKit);
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §8[§c§l!§8] §7Game Starting in §c§l10 seconds...
                                        §8
                                        """);
                            }
                            break;
                        case 5, 4, 3, 2, 1:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 0.3F*(6-timeLeft));
                            for (Player player : getPlayers()) {
                                player.sendTitle("§c§l▶ " + timeLeft + " ◀", "", 0, 20, 20);
                            }
                            break;
                        case 0:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 2);
                            pvpEnabled = true;
                            for (Player player : getPlayers()) {
                                player.sendTitle("§a§l▶ GUB! ◀", "", 0, 40, 0);
                            }
                            startTimer(240, "gubgame");
                            runningTimers.remove("gubgamestart");
                            cancel();
                            break;
                        default:
                            break;
                    }
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("gubgamestart", new AbstractMap.SimpleEntry<>(task, 61));
    }

    public void resetGubGame() {
        gubGameKills.clear();
        gubKitKills.clear();
        for(Player p : getPlayers()) {
            gubGameKills.put(p.getName(), 0);
        }
        for(int i = 1; i <= 15; i++){
            gubKitKills.put(i, 0);
        }
    }

    public void startSurvivalGames(){
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 61;
            @Override
            public void run() {
                if(!pausedTimers.contains("survivalgamesstart")) {
                    timeLeft--;
                    runningTimers.get("survivalgamesstart").setValue(timeLeft);
                    switch (timeLeft) {
                        case 60:
                            for(Player p : getPlayers()){
                                lastHitPlayer.put(p.getName(), "");
                            }
                            teamTeleport("sg", 5);
                            teleportSpectators(TeleportConfig.get().getLocation("spectators.survivalgames"), 5);
                            resetSurvivalGames();
                            resetModePoints();
                            break;
                        case 55:
                            currentMode = "Survival Games";
                            try {
                                glowTeams();
                            } catch (ReflectiveOperationException e) {
                                throw new RuntimeException(e);
                            }
                            for (Player player : getPlayers()) {
                                PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 90, 1, false, false);
                                player.addPotionEffect(PotionEffect);
                                player.setGameMode(GameMode.ADVENTURE);
                            }
                            break;

                        case 50:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
                            for (Player player : getPlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §8[§e§l?§8] §eWelcome to §a§lSurvival Games§e! Good old original game of last team standing. Stay alive, stay aware, stay together. Survive until the very end with your PVP skills.
                                        §8
                                        """);
                            }
                            Bukkit.getWorld("build").getWorldBorder().setCenter(-179.5, -708.5);
                            Bukkit.getWorld("build").getWorldBorder().setSize(469);
                            break;
                        case 30:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
                            for (Player player : getPlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §8[§e§l?§8] §eDid you know the border shrinks? That's right! It shrinks, and shrinks, until the very last battle has been won. Chests refill around the map every 4 minutes so keep looting!
                                        §8
                                        """);
                            }
                            break;
                        case 10:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
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
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 0.3F*(6-timeLeft));
                            for (Player player : getPlayers()) {
                                player.sendTitle("§c§l▶ " + timeLeft + " ◀", "", 0, 20, 20);
                            }
                            break;
                        case 0:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 2);
                            startGracePeriod();
                            for (Player player : getPlayers()) {
                                player.sendTitle("§a§l▶ LOOT! ◀", "", 0, 40, 0);
                            }
                            startTimer(240, "survivalgames");
                            runningTimers.remove("survivalgamesstart");
                            cancel();
                            break;
                        default:
                            break;
                    }
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("survivalgamesstart", new AbstractMap.SimpleEntry<>(task, 61));
    }

    public void startGracePeriod() {
        BukkitTask task2 = new BukkitRunnable() {
            int timeLeft = 21;
            @Override
            public void run() {
                if(!pausedTimers.contains("graceperiod")) {
                    timeLeft--;
                    runningTimers.get("graceperiod").setValue(timeLeft);
                    switch (timeLeft) {
                        case 5, 4, 3, 2, 1:
                            for (Player player : getPlayers()) {
                                messagePlayer(player, "§c§l⏱ §8| §c§lGrace period ends in: " + timeLeft);
                            }
                            break;
                        case 0:
                            for (Player player : getPlayers()) {
                                messagePlayer(player, """
                                §f
                                §f
                                §c§lGRACE PERIOD IS OVER!"
                                §f
                                """);
                            }
                            pvpEnabled = true;
                            runningTimers.remove("graceperiod");
                            cancel();
                            break;
                        default:
                            break;
                    }
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("graceperiod", new AbstractMap.SimpleEntry<>(task2, 21));
    }


    public void resetSurvivalGames(){
        deadPlayers.clear();
        deadTeams.clear();
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
            messagePlayer(player, " §f-  §e§l   ᴍᴏᴅᴇ ᴛᴇᴀᴍ ʟᴇᴀᴅᴇʀs  §f-");
            int placement = 0;
            for (String key : sortMap(modeTeamPoints).keySet()) {
                placement++;
                messagePlayer(player, String.format("%-15s%15s", placement + ". " + getTeamDisplayName(key), "§e§l\uD83D\uDCB0" + sortMap(modeTeamPoints).get(key)));
            }
            messagePlayer(player, "§f--------------------------");
        }
    }

    public void getPlayerModePoints(){
        for(Player player : getPlayers()) {
            messagePlayer(player, " §f-  §e§lᴍᴏᴅᴇ ɪɴᴅɪᴠ ʟᴇᴀᴅᴇʀs  §f-");
            List<String> players = new ArrayList<>(sortMap(modePoints).keySet());
            List<Integer> points = new ArrayList<>(sortMap(modePoints).values());
            for (int i = 0; i <= 7; i++) {
                if(players.size() >= i+1) {
                    if (players.get(i) != null && points.get(i) != null) {
                        messagePlayer(player, String.format("%-15s%15s", i+1 + ". " + getPlayerDisplayName(players.get(i)), "§e§l\uD83D\uDCB0" + points.get(i)));
                    }
                }
            }
            messagePlayer(player, "§f--------------------------");
        }
    }

    public void countVotes(){
        modeVotes.clear();
        int totalvotes = 0;
        for(int i = 109; i <= 139; i++){
            for(int j = -381; j <= -351; j++){
                totalvotes++;
                Material block = Bukkit.getServer().getWorld("world").getBlockAt(j, 122, i).getType();
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
        int totalOutOfTwenty = round((double) leaderModeVotes.getFirst() /totalvotes*20);
        float percentageFirst = ((float) leaderModeVotes.getFirst() /totalvotes)*100;
        currentMode = leaderMode.getFirst();
        playSoundAll(Sound.ENTITY_GENERIC_EXPLODE, 1.5F);
        BukkitTask task = new BukkitRunnable() {
            int percentElapsed = 0;
            @Override
            public void run() {
                percentElapsed++;
                if(percentElapsed <= totalOutOfTwenty) {
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        StringBuilder percentprogress = new StringBuilder();
                        percentprogress.append("§e|".repeat(percentElapsed));
                        percentprogress.append("§8|".repeat(20 - percentElapsed));
                        if(percentElapsed != totalOutOfTwenty) {
                            percentprogress.append(" §e").append(roundToTwoDecimalPlaces((double)percentElapsed/20*100)).append("%");
                        } else {
                            percentprogress.append(" §e").append(roundToTwoDecimalPlaces(percentageFirst)).append("%");
                        }
                        player.sendTitle("§e§l" + leaderMode.getFirst(), percentprogress.toString(), 0, 60, 40);
                    }
                } else {
                    cancel();
                }
            }

        }.runTaskTimer(plugin, 0L, 3L);
        for(Player player : Bukkit.getOnlinePlayers()) {
            messagePlayer(player, " §f-  §e§lᴠᴏᴛɪɴɢ ʀᴇsᴜʟᴛs  §f-");
            int placement = 0;
            for (String key : leaderMode) {
                placement++;
                double percentage = roundToTwoDecimalPlaces(((double) leaderModeVotes.get(placement - 1) /totalvotes)*100);
                messagePlayer(player, placement + ". " + plugin.modeColors.get(key) + key + "§7: §e§l" + leaderModeVotes.get(placement-1) + " spaces §e§o(" + percentage + "%)");
            }
            messagePlayer(player, "§f--------------------");
        }
    }

    double roundToTwoDecimalPlaces(double number) {
        double factor = Math.pow(10, 1);
        return Math.round(number * factor) / factor;
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
                            currentMode = "Voting";
                            playSoundAll(Sound.ITEM_GOAT_HORN_SOUND_0, 1);
                            for(int i = 109; i <= 139; i++){
                                for(int j = -381; j <= -351; j++){
                                    Bukkit.getServer().getWorld("world").getBlockAt(j, 122, i).setType(Material.BLACK_WOOL);
                                }
                            }
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.sendTitle("§e§lVoting Time!", "", 0, 60, 40);
                            }
                            break;
                        case 75:
                            teamTeleport("votearena", 0);
                            teleportSpectators(TeleportConfig.get().getLocation("spectators.votearena"), 0);
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §e§lVoting Time!
                                        §fWalk into your mode of choice and paint the floor with as many blocks as possible by running around! If you change your mind simply run back to the mode selection and get painting!
                                        §8
                                        """);
                            }
                            break;
                        case 74:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
                            break;
                        case 60:
                            playSoundAll(Sound.ITEM_GOAT_HORN_SOUND_1, 1);
                            votingEnabled = true;
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §6§lVoting Enabled.
                                        §fVotes will be counted in 30 seconds!
                                        §8
                                        """);
                            }
                            break;
                        case 57:
                            messageConsole("case 57 reached.");
                            try {
                                summonSlimeBall(27);
                            } catch (ReflectiveOperationException e) {
                                throw new RuntimeException(e);
                            }
                        case 55:
                            summonPowerUp(25);
                            break;
                        case 50:
                            summonPowerUp(20);
                            break;
                        case 40:
                            summonPowerUp(10);
                            break;
                        case 30:
                            playSoundAll(Sound.BLOCK_FIRE_EXTINGUISH, 1);
                            votingEnabled = false;
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §c§lVoting Period Ended.
                                        §fVotes will now be calculated!
                                        §8
                                        """);
                            }
                            powerUpHolders.clear();
                            for (Entity entity : Bukkit.getWorld("world").getEntities()) {
                                if (entity instanceof Item) {
                                    entity.remove();
                                }
                            }
                            break;
                        case 25:
                            playSoundAll(Sound.ENTITY_CREEPER_PRIMED, 1);
                            for (Player player : Bukkit.getOnlinePlayers()) {
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

    public void summonPowerUp(int time){
        Random random = new Random();
        int x = -351-random.nextInt(31);
        int z = 109+random.nextInt(31);
        ItemStack tntItemStack = new ItemStack(Material.TNT);
        Location coords = new Location(Bukkit.getWorld("world"), x,123,z);
        Item powerUp = Bukkit.getWorld("world").dropItemNaturally(coords, tntItemStack);
        powerUp.setGlowing(true);
        coords.setY(126);
        playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
        for(Player player : getPlayers()) {
            player.sendMessage("""
                    §a
                    §a
                    §a§lA POWER UP HAS SPAWNED!
                    §a
                    """);
        }
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = time+1;
            @Override
            public void run() {
                if(runningTimers.containsKey("powerup")) {
                    if (!pausedTimers.contains("powerup")) {
                        timeLeft--;
                        runningTimers.get("powerup").setValue(timeLeft);
                        if (timeLeft == 0) {
                            runningTimers.remove("powerup");
                            powerUp.remove();
                            cancel();
                        } else {
                            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.WHITE, 4);

                            coords.setX(powerUp.getLocation().getX());
                            coords.setZ(powerUp.getLocation().getZ());

                            Bukkit.getWorld("world").spawnParticle(Particle.DUST, coords, 40, 0.0, 1, 0.0, 1, dustOptions, false);
                        }
                    }
                    if (!runningTimers.containsKey("powerup")) {
                        powerUp.remove();
                        cancel();
                    }
                } else {
                    cancel();
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("powerup", new AbstractMap.SimpleEntry<>(task, time+1));
    }


    public void summonSlimeBall(int time) throws ReflectiveOperationException {
        slimeBallVote = null;
        messageConsole("summonSlimeBall started.");
        Location coords = new Location(Bukkit.getWorld("world"), -366,131,124);
        chickenBall = (LivingEntity) Bukkit.getWorld("world").spawnEntity(coords, EntityType.CHICKEN);
        messageConsole("Chicken spawned hopefully.");

        for(Player player : Bukkit.getOnlinePlayers()){
            glowingEntities.setGlowing(chickenBall, player, ChatColor.WHITE);
        }

        for(Player player : getPlayers()) {
            player.sendMessage("""
                    §a
                    §a
                    §e§lA CHICKEN BALL HAS SPAWNED!
                    §a
                    """);
        }

        BukkitTask task = new BukkitRunnable() {
            int timeLeft = time+1;
            @Override
            public void run() {
                if(runningTimers.containsKey("slimeBall")) {
                    if (!pausedTimers.contains("slimeBall")) {
                        if(chickenBall.getLocation().clone().subtract(0, 1, 0).getBlock().getType() != Material.AIR) {
                            messageConsole("Chicken ball touched ground.");
                            if (slimeBallVote != null) {
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    try {
                                        glowingEntities.unsetGlowing(chickenBall, player);
                                    } catch (ReflectiveOperationException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                                Location blockBelow = chickenBall.getLocation().clone().subtract(3, 1, 3);
                                for (int i = 0; i <= 6; i++) {
                                    for (int j = 0; j <= 6; j++) {
                                        Block currentBlock = blockBelow.clone().add(i, 0, j).getBlock();
                                        for (Material wool : getWoolColors()) {
                                            if (currentBlock.getType().equals(wool)) {
                                                currentBlock.setType(plugin.playerVote.get(slimeBallVote));
                                                break;
                                            }
                                        }
                                    }
                                }
                                Particle.DustOptions dustOptions = new Particle.DustOptions(plugin.woolColors.get(plugin.playerVote.get(slimeBallVote)), 4);

                                Bukkit.getWorld("world").spawnParticle(Particle.DUST, chickenBall.getLocation(), 300, 1.5, 0.0, 1.5, 1, dustOptions, false);
                                playSoundAll(Sound.ENTITY_CHICKEN_DEATH, 1);
                                chickenBall.remove();
                                cancel();
                                for (Player player : getPlayers()) {
                                    player.sendMessage("""
                                            §a
                                            §a
                                            §e§lTHE CHICKEN BALL HAS LANDED!
                                            §a
                                            """);
                                }
                            }
                        }
                        timeLeft--;
                        runningTimers.get("slimeBall").setValue(timeLeft);
                        if (timeLeft == 0) {
                            runningTimers.remove("slimeBall");
                            cancel();
                        }
                    }
                    if (!runningTimers.containsKey("slimeBall")) {
                        messageConsole("Not paused, timer non-existent.");
                        chickenBall.remove();
                        cancel();
                    }
                } else {
                    messageConsole("Timer does not exist.");
                    cancel();
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("slimeBall", new AbstractMap.SimpleEntry<>(task, time+1));
    }


    private Material[] getWoolColors() {
        return new Material[]{
                Material.WHITE_WOOL, Material.ORANGE_WOOL, Material.MAGENTA_WOOL,
                Material.LIGHT_BLUE_WOOL, Material.YELLOW_WOOL, Material.LIME_WOOL,
                Material.PINK_WOOL, Material.GRAY_WOOL, Material.LIGHT_GRAY_WOOL,
                Material.CYAN_WOOL, Material.PURPLE_WOOL, Material.BLUE_WOOL,
                Material.BROWN_WOOL, Material.GREEN_WOOL, Material.RED_WOOL,
                Material.BLACK_WOOL
        };
    }

    public void gameEnd(){
        runningTimers.clear();
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 61;
            @Override
            public void run() {
                if(!pausedTimers.contains("backtolobby")) {
                    timeLeft--;
                    runningTimers.get("backtolobby").setValue(timeLeft);
                    switch (timeLeft) {
                        case 60:
                            pvpEnabled = false;
                            doubleJumpEnabled = false;
//                            plugin.killRecord.clear();
                            try {
                                unGlowTeams();
                            } catch (ReflectiveOperationException e) {
                                throw new RuntimeException(e);
                            }
                            for (Player player : getPlayers()) {
                                if(currentMode.equals("Zoomo Go")){
                                    player.setAllowFlight(false);
                                }
                                player.setGameMode(GameMode.SPECTATOR);
                                Bukkit.getScheduler().runTaskLater(plugin, () -> player.setFlying(true), 1L);
                                healFeedPlayer(player);
                                Bukkit.getWorld("world").getWorldBorder().setCenter(0, 0);
                                Bukkit.getWorld("world").getWorldBorder().setSize(25000);
                                player.getInventory().clear();
                                player.sendTitle("§c§lGAME OVER!", "", 0, 60, 40);
                            }
                            break;
                        case 50:
                            if(currentMode.equals("Slime Golf")) {
                                slimeGolfTimes();
                            } else {
                                getPlayerModePoints();
                            }
                            currentMode = "Lobby";
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
                            teleportSpectators(TeleportConfig.get().getLocation("spectators.lobby"), 5);
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
            messagePlayer(p, " §f-  §e§lʜᴏʟᴇ ᴛɪᴍᴇs  §f-");
            for (String team : slimeFinishers.keySet()) {
                messagePlayer(p, "§e§l⏱§e" + slimeFinishers.get(team) + " §f- " + getTeamDisplayName(team));
            }
            for(String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
                if(!slimeFinishers.containsKey(team)) {
                    messagePlayer(p, "DNF. " + getTeamDisplayName(team));
                }
            }
            messagePlayer(p, "§f------------------");


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

    public void resetSlimeCompletions(){
        slimeFinishers.clear();
    }

    public ItemStack[] craftalotKit(){

        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        ItemStack pickaxe = new ItemStack(Material.IRON_PICKAXE);
        ItemStack axe = new ItemStack(Material.IRON_AXE);
        ItemStack shovel = new ItemStack(Material.IRON_SHOVEL);
        ItemStack trident = new ItemStack(Material.TRIDENT);
        ItemMeta tridentMeta = trident.getItemMeta();
        tridentMeta.addEnchant(Enchantment.RIPTIDE, 3, true);
        trident.setItemMeta(tridentMeta);

        return new ItemStack[]{sword, pickaxe, axe, shovel, trident};
    }

    public void healFeedPlayer(Player p) {
        p.setHealth(20);
        p.setFoodLevel(20);
    }

    public void playSoundAll(Sound sound, float pitch) {
        for(Player player : Bukkit.getOnlinePlayers()){
            player.playSound(player.getLocation(), sound, 10, pitch);
        }
    }

    public void getReadyPlayers(){
        resetReady();
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 31;
            @Override
            public void run() {
                if(!pausedTimers.contains("readytimer")) {
                    timeLeft--;
                    runningTimers.get("readytimer").setValue(timeLeft);
                    switch (timeLeft) {
                        case 30:
                            for (Player player : getPlayers()) {
                                player.sendTitle("§b§lReady to play?", "Spam Crouch!", 0, 560, 40);
                            }
                            break;
                        case 1:
                            StringBuilder notReady = new StringBuilder();
                            for (String player : readyPlayers.keySet()) {
                                if (readyPlayers.get(player) < 10) {
                                    if (notReady.isEmpty()) {
                                        notReady.append("§fNot Ready: ").append(getPlayerDisplayName(player));
                                    } else {
                                        notReady.append("§f, ").append(getPlayerDisplayName(player));
                                    }

                                }
                            }
                            if(!notReady.isEmpty()) {
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, notReady.toString());
                                    player.sendTitle("§c§lNot Ready.", "We'll try again soon.", 0, 60, 40);
                                }
                            } else {
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, "Everyone is ready!");
                                    player.sendTitle("§a§lReady!", "Hooray!", 0, 60, 40);
                                }
                            }
                        case 0:
                            runningTimers.remove("readytimer");
                            cancel();
                            break;
                        default:
                            break;
                    }
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("readytimer", new AbstractMap.SimpleEntry<>(task, 31));
    }

    public String formatDeathMessage(String player){
        Random r = new Random();
        List<String> messages = DeathMessagesConfig.get().getStringList("zoomodeaths");
        return String.format(messages.get(r.nextInt(messages.size())), getPlayerDisplayName(player));
    }

    public String formatKillMessage(String killer, String victim){
        Random r = new Random();
        List<String> messages = DeathMessagesConfig.get().getStringList("kills");
        return String.format(messages.get(r.nextInt(messages.size())), getPlayerDisplayName(victim), getPlayerDisplayName(killer));
    }

    public void resetReady(){
        readyPlayers.clear();
        for(Player player : getPlayers()){
            readyPlayers.put(player.getName(), 1);
        }
    }

    public void startEvent(){
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 91;
            @Override
            public void run() {
                if(!pausedTimers.contains("startevent")) {
                    timeLeft--;
                    runningTimers.get("startevent").setValue(timeLeft);
                    switch (timeLeft) {
                        case 90:
                            currentMode = "Start";
//                            Play music
                            PotionEffect PotionEffect = new PotionEffect(PotionEffectType.LEVITATION, 5, 1, false, false);
                            for(Player player : getPlayers()) {
                                player.addPotionEffect(PotionEffect);
                            }
                            teleportPlayers(TeleportConfig.get().getLocation("players.stage"), 5);
                            teleportSpectators(TeleportConfig.get().getLocation("spectators.stage") , 5);
                            break;
                        case 85:
                            PotionEffect PotionEffect2 = new PotionEffect(PotionEffectType.SLOW_FALLING, 5, 1, false, false);
                            for(Player player : getPlayers()) {
                                player.addPotionEffect(PotionEffect2);
                            }
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

        runningTimers.put("startevent", new AbstractMap.SimpleEntry<>(task, 91));
    }


    public void updateTeamGUI() {
        ItemStack divider1 = new ItemStack(Material.RED_CANDLE, 1);
        ItemStack divider2 = new ItemStack(Material.YELLOW_CANDLE, 1);
        ItemStack divider3 = new ItemStack(Material.LIGHT_BLUE_CANDLE, 1);
        ItemStack divider4 = new ItemStack(Material.PINK_CANDLE, 1);
        ItemStack air = new ItemStack(Material.AIR, 1);
        int index = 0;
        int playerindex;
        plugin.gui.clear();
        plugin.gui.setItem(4, divider1);
        plugin.gui.setItem(13, divider2);
        plugin.gui.setItem(22, divider3);
        plugin.gui.setItem(31, divider4);
        for(String teamname : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
            playerindex = 0;
            for (String playername : TeamsConfig.get().getStringList("teams." + teamname + ".players")) {
                playerindex++;
                ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
                SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
                meta.setDisplayName(playername);
                if (Bukkit.getPlayer(playername) != null) {
                    Player player = Bukkit.getPlayer(playername);

                    meta.setOwningPlayer(player);
                }
                playerHead.setItemMeta(meta);
                plugin.gui.setItem(index, playerHead);
                index++;
            }
            if(playerindex < 4){
                for(int i = 0; i < 4-playerindex; i++){
                    plugin.gui.setItem(index, air);
                    index++;
                }
            }
            if(index == 4 || index == 13 || index == 22 || index == 31){
                index++;
            }
        }
    }
//    public void playerMedal(Player p) {
//        ItemStack[] currentInv = p.getInventory().getContents();
//        p.getInventory().setItemInOffHand();
//        p.setHealth(0.0);
//        p.getInventory().setContents(currentInv);
//    }


    public void glowTeams() throws ReflectiveOperationException {
        for(String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)){
            for(String player : TeamsConfig.get().getStringList("teams." + team + ".players")) {
                for(String player2 : TeamsConfig.get().getStringList("teams." + team + ".players")) {
                    if(Bukkit.getPlayer(player) != null && Bukkit.getPlayer(player2) != null) {
                        glowingEntities.setGlowing(Bukkit.getPlayer(player), Bukkit.getPlayer(player2), teamGlowColors.get(team));
                    }
                }
            }
        }
    }

    public void unGlowTeams() throws ReflectiveOperationException {
        for(String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)){
            for(String player : TeamsConfig.get().getStringList("teams." + team + ".players")) {
                for(String player2 : TeamsConfig.get().getStringList("teams." + team + ".players")) {
                    if(Bukkit.getPlayer(player) != null && Bukkit.getPlayer(player2) != null) {
                        glowingEntities.unsetGlowing(Bukkit.getPlayer(player), Bukkit.getPlayer(player2));
                    }
                }
            }
        }
    }
}
