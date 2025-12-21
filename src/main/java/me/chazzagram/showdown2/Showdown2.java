package me.chazzagram.showdown2;
import fr.skytasul.glowingentities.GlowingBlocks;
import fr.skytasul.glowingentities.GlowingEntities;
import me.chazzagram.showdown2.commands.MainCommand;
import me.chazzagram.showdown2.commands.TabCompleterCMD;
import me.chazzagram.showdown2.expansions.SpigotExpansion;
import me.chazzagram.showdown2.files.*;
import me.chazzagram.showdown2.listeners.*;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;


import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.bukkit.util.NumberConversions.round;

public final class Showdown2 extends JavaPlugin implements Listener {

    private static Showdown2 plugin;

    public double multiplier = 1.0;

    public boolean votingEnabled = false;

    public boolean pvpEnabled = false;

    public boolean emotesEnabled = false;

    public String currentMode = "Lobby";

    public boolean doubleJumpEnabled = false;

    public HashMap<String, Map.Entry<BukkitTask, Integer>> runningTimers = new HashMap<>();

    public ArrayList<String> pausedTimers = new ArrayList<>();

    public HashMap<Integer, Integer> slimeCheckpoints = new HashMap<>();

    public HashMap<String, Integer> slimeFinishers = new HashMap<>();

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

    public int finaleRound = 1;

    public HashMap<String, String> lastHitPlayer = new HashMap<>();

    public HashMap<String, Integer> modeCompletions = new HashMap<>();

    public HashMap<String, Integer> modeTeamPoints = new HashMap<>();

    public HashMap<String, Integer> modePoints = new HashMap<>();

    public HashMap<String, Integer> modeFullPoints = new HashMap<>();

    public HashMap<String, String> itemToCraft = new HashMap<>();

    public HashMap<String, Integer> readyPlayers = new HashMap<>();

    public int readyPlayerCount = 0;

    public HashMap<String, Color> teamColors = new HashMap<>();
    public HashMap<String, Material> teamWool = new HashMap<>();
    public HashMap<String, Material> teamConcrete = new HashMap<>();

    public HashMap<String, Integer> gubGameKills = new HashMap<>();

    public HashMap<Material, String> woolLogos = new HashMap<>();
    public HashMap<String, String> modeLogos = new HashMap<>();

    public HashMap<String, ChatColor> modeColors = new HashMap<>();

    public HashMap<Integer, Integer> gubKitKills = new HashMap<>();

    public HashMap<String, ChatColor> teamGlowColors = new HashMap<>();

    public HashMap<String, int[]> teamJump = new HashMap<>();

    public List<String> powerUpHolders = new ArrayList<>();

    public GlowingEntities glowingEntities;

    public GlowingBlocks glowingBlocks;

    public Inventory gui = Bukkit.createInventory(null, 36, "§eTeams");

    public Player slimeBallVote;

    public List<String> killRecord = new ArrayList<>();

    TextDisplay textDisplay;

    BossBar bossBar = Bukkit.createBossBar("Initial Title", BarColor.BLUE, BarStyle.SOLID);

    public HashMap<String, BossBar> bossBars = new HashMap<>();

    public HashMap<String, List<String>> craftLists = new HashMap<>();

    public Location sgCenter = new Location(Bukkit.getWorld("build"), -180, 0, -709);

    public int newBorderRadius = 236;

    public int currentBorderRadius = 236;

    public HashMap<String, Integer> currentPlacements = new HashMap<>();
    public HashMap<String, Integer> previousPlacements = new HashMap<>();

    int[][] slimeCmdCoords = {
            {880, 41, 1362},
            {934, 40, 1317},
            {1007, 38, 1414},
            {1132, 36, 1356},
            {1221, 27, 1339},
            {1302, 23, 1348}

    };

    int[][] slimeCmdCoords2 = {
            {920, 47, 3175},
            {1019, 29, 3153},
            {1095, 56, 3173},
            {1217, 38, 3173},
            {1303, 38, 3148},
            {1357, 35, 3171}
    };

    public int[][] cdWallCoords = {
            {31, 142, 1038}, // Route 2
            {80, 143, 1009}, // Route 2
            {59, 142, 990}, // Route 1
            {46, 142, 1020}, // Route 1
            {103, 143, 1023} // Route 1
    };

    List<Slime> slimeGolfSlime = new ArrayList<>();

    public Map<String, Map<Integer, Integer>> bridgeCourseTimes = new HashMap<>();
    public Map<String, Map<Integer, Integer>> dashersCourseTimes = new HashMap<>();

    public HashMap<String, Integer> buildTimeStamps = new HashMap<>();

    public Map<Integer, List<Map.Entry<String, Integer>>> sortedTimesPerCourse = new HashMap<>();

    public List<String> lylaInteractions = new ArrayList<>();

    public int cdCompletions = 0;

    public int currentRound = 1;

    public HashMap<String, Integer> craftTop = new HashMap<>();

    public boolean leastVotes = false;
    public boolean audienceVote = false;

    public HashMap<String, Integer> bridgeTally = new HashMap<>();

    public List<Material> colourDashBlocks = Arrays.asList(Material.BLUE_ICE, Material.RED_CONCRETE, Material.ORANGE_CONCRETE, Material.YELLOW_CONCRETE, Material.LIME_CONCRETE, Material.LIGHT_BLUE_CONCRETE, Material.BLUE_CONCRETE, Material.MAGENTA_CONCRETE, Material.WHITE_CONCRETE);
    public String readyType = "sneak";

    public HashMap<Player, Boolean> jumpStates = new HashMap<>();

    public String winningTeam = "";

    public boolean shopAllowed = true;

    public boolean[] teamShown = {
        true,
        true,
        true,
        true,
        true,
        true,
        true,
        true
    };

    public HashMap<String, String> emotes = new HashMap<>();

    public List<Player> bellRingers = new ArrayList<>();

    public boolean bellRung = false;

    public List<String> craftList = new ArrayList<>();

    public int cdIndex = 2;

    public HashMap<String, Integer> playerKillCount = new HashMap<>();

    public boolean voteParty = false;

    public boolean zoomoSpeed = false;

    public LivingEntity chickenBall;

    public HashMap<String, Integer> finaleScores = new HashMap<>();

    public HashMap<BlockDisplay, Boolean> lobbyPresents = new HashMap<>();

    public Sound[] phStartSound = new Sound[]{Sound.ENTITY_ELDER_GUARDIAN_DEATH, Sound.ENTITY_ELDER_GUARDIAN_DEATH_LAND};

    public Sound[] phRoundStartSound = new Sound[]{Sound.ENTITY_GUARDIAN_DEATH, Sound.ENTITY_ELDER_GUARDIAN_CURSE};

    public Sound[] phRoundEnd = new Sound[]{Sound.BLOCK_NOTE_BLOCK_GUITAR, Sound.BLOCK_NOTE_BLOCK_BANJO, Sound.BLOCK_NOTE_BLOCK_COW_BELL};

    public Sound phHoHoHo = Sound.ENTITY_ELDER_GUARDIAN_AMBIENT;

    public Boolean blockBreak = false;

    public Boolean tntRun = false;

    public Boolean tntBow = false;

    public List<String> superAdmins = new ArrayList<>();

    public Sound[] phDialogue = new Sound[]{
            Sound.ITEM_GOAT_HORN_SOUND_2,
            Sound.ITEM_GOAT_HORN_SOUND_3,
            Sound.ITEM_GOAT_HORN_SOUND_4,
            Sound.ITEM_GOAT_HORN_SOUND_5,
            Sound.EVENT_MOB_EFFECT_BAD_OMEN,
            Sound.EVENT_MOB_EFFECT_TRIAL_OMEN,
            Sound.EVENT_MOB_EFFECT_RAID_OMEN,
            Sound.BLOCK_PORTAL_TRAVEL,
            Sound.BLOCK_PORTAL_AMBIENT,
            Sound.BLOCK_PORTAL_TRIGGER,
            Sound.ENTITY_BLAZE_DEATH,
            Sound.ENTITY_CREEPER_DEATH,
            Sound.ENTITY_ENDERMAN_DEATH
    };

    // Spawn the particle

    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;

        emotes.put("§e§l§oHYPE!", "\uE024");
        emotes.put("§e§l§oFIRE!", "\uE025");
        emotes.put("§e§l§o...", "\uE026");
        emotes.put("§e§l§o:O", "\uE027");

        superAdmins.add("Chazzagram");

        glowingEntities = new GlowingEntities(plugin);
        glowingBlocks = new GlowingBlocks(plugin);

        this.getCommand("mcevent").setExecutor(new MainCommand(this));
        this.getCommand("mcevent").setTabCompleter(new TabCompleterCMD());

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

        teamWool.put("RubyRaiders", Material.RED_WOOL);
        teamWool.put("AmberAmbushers", Material.ORANGE_WOOL);
        teamWool.put("TopazTroopers", Material.YELLOW_WOOL);
        teamWool.put("KyaniteKillers", Material.LIME_WOOL);
        teamWool.put("DiamondDestroyers", Material.LIGHT_BLUE_WOOL);
        teamWool.put("SapphireSoldiers", Material.BLUE_WOOL);
        teamWool.put("SmithsoniteSlayers", Material.MAGENTA_WOOL);
        teamWool.put("CrystalCrashers", Material.WHITE_WOOL);

        teamConcrete.put("RubyRaiders", Material.RED_CONCRETE);
        teamConcrete.put("AmberAmbushers", Material.ORANGE_CONCRETE);
        teamConcrete.put("TopazTroopers", Material.YELLOW_CONCRETE);
        teamConcrete.put("KyaniteKillers", Material.LIME_CONCRETE);
        teamConcrete.put("DiamondDestroyers", Material.LIGHT_BLUE_CONCRETE);
        teamConcrete.put("SapphireSoldiers", Material.BLUE_CONCRETE);
        teamConcrete.put("SmithsoniteSlayers", Material.MAGENTA_CONCRETE);
        teamConcrete.put("CrystalCrashers", Material.WHITE_CONCRETE);

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

        modeLogos.put("Survival Games", "\uD83E\uDD6C");
        modeLogos.put("Gub Game", "\uD83E\uDED0");
        modeLogos.put("Slime Golf", "\uE172");
        modeLogos.put("Zoomo Go", "\uD83E\uDD55");
        modeLogos.put("Bridge Builders", "\uD83C\uDF45");
        modeLogos.put("Colour Dash", "\uD83E\uDD68");
        modeLogos.put("Craftalot", "\ue238");

        teamJump.put("RubyRaiders", new int[] { 247, -19, 659 } );
        teamJump.put("AmberAmbushers", new int[] { 282, -19, 659 });
        teamJump.put("TopazTroopers", new int[] { 317, -19, 659 });
        teamJump.put("KyaniteKillers", new int[] { 352, -19, 659 });
        teamJump.put("DiamondDestroyers", new int[] { 387, -19, 659 });
        teamJump.put("SapphireSoldiers", new int[] { 422, -19, 659 });
        teamJump.put("SmithsoniteSlayers", new int[] { 457, -19, 659 });
        teamJump.put("CrystalCrashers", new int[] { 492, -19, 659 });

        getServer().getPluginManager().registerEvents(new BookSignEvent(this), this);
        getServer().getPluginManager().registerEvents(new BucketListener(this), this);
        getServer().getPluginManager().registerEvents(new BellDongEvent(this), this);
        getServer().getPluginManager().registerEvents(new RiptideEvent(this), this);
        getServer().getPluginManager().registerEvents(new ArrowDespawnEvent(this), this);
        getServer().getPluginManager().registerEvents(new BreakBlockEvent(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractionEvent(this), this);
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

        GubTPConfig.setup();
        GubTPConfig.get().options().copyDefaults(true);
        GubTPConfig.save();

        PhilipConfig.setup();
        PhilipConfig.get().options().copyDefaults(true);
        PhilipConfig.save();

        GameOrderConfig.setup();
        GameOrderConfig.get().options().copyDefaults(true);
        GameOrderConfig.save();

        PlayerInfoConfig.setup();
        PlayerInfoConfig.get().options().copyDefaults(true);
        PlayerInfoConfig.save();

        PresentsConfig.setup();
        PresentsConfig.get().options().copyDefaults(true);
        PresentsConfig.save();

        WishesConfig.setup();
        WishesConfig.get().options().copyDefaults(true);
        WishesConfig.save();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Bukkit.getPluginManager().registerEvents(this, this);
            new SpigotExpansion(this).register();
        }

        for (int i = 1; i <= 6; i++) {
            slimeCheckpoints.put(i, 1);
        }

        for (int i = 1; i <= 6; i++) {
            bridgeCheckpoints.put(i, 1);
        }

        for (int i = 1; i <= 6; i++) {
            bridgeJumpCheckpoints.put(i, 1);
        }

        for (int i = 1; i <= 6; i++) {
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

        for (Player player : getPlayers()) {
            modeFullPoints.put(player.getName(), 0);
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
        glowingBlocks.disable();
        // Plugin shutdown logic
        messageConsole("Plugin Unloaded.");
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        e.setQuitMessage("""
                    §f
                    §7  [§c§l-§7] §c§l""" + player.getName() + """
                    §f
                    """);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.getPlayer().sendTitle("§7§oLoading...", "", 0, 10, 0);

        switch(currentMode){
            case "Craftalot":
            case "Bridge Builders":
            case "Colour Dash":
                e.getPlayer().setGameMode(GameMode.SURVIVAL);
            default:
                e.getPlayer().setGameMode(GameMode.ADVENTURE);
        }

        if(PlayerInfoConfig.get().getConfigurationSection("players") == null){
            PlayerInfoConfig.get().set("players." + e.getPlayer().getName() + ".bestgame", "N/A");
            PlayerInfoConfig.get().set("players." + e.getPlayer().getName() + ".bestgameplacement", 32);
            PlayerInfoConfig.get().set("players." + e.getPlayer().getName() + ".kills", 0);
            PlayerInfoConfig.get().set("players." + e.getPlayer().getName() + ".bestreadycheck", 32);
            PlayerInfoConfig.get().set("players." + e.getPlayer().getName() + ".highestplacement", 32);
            PlayerInfoConfig.save();
        } else if (!PlayerInfoConfig.get().getConfigurationSection("players").getKeys(false).contains(e.getPlayer().getName())){
            PlayerInfoConfig.get().set("players." + e.getPlayer().getName() + ".bestgame", "N/A");
            PlayerInfoConfig.get().set("players." + e.getPlayer().getName() + ".bestgameplacement", 32);
            PlayerInfoConfig.get().set("players." + e.getPlayer().getName() + ".kills", 0);
            PlayerInfoConfig.get().set("players." + e.getPlayer().getName() + ".bestreadycheck", 32);
            PlayerInfoConfig.get().set("players." + e.getPlayer().getName() + ".highestplacement", 32);
            PlayerInfoConfig.save();
        }

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

        Player player = e.getPlayer();
        e.setJoinMessage("""
                    §f
                    §7  [§a§l+§7] §a§l""" + player.getName() + """
                    §f
                    """);


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

        if(Bukkit.getPlayer(player) != null) {
            Player p = Bukkit.getPlayer(player);
            p.playSound(p.getLocation(), Sound.ENTITY_DOLPHIN_HURT, 10, 1F);
        }

        if(!Objects.equals(currentMode, "Presents")) {
            modeTeamPoints.put(playerTeam, modeTeamPoints.get(playerTeam) + multiplyPoints(points));
        }

        if(individual) {
            int currentPoints = PlayerConfig.get().getInt("players." + player + ".points");
            PlayerConfig.get().set("players." + player + ".points", currentPoints+multiplyPoints(points));
            PlayerConfig.save();
            if(!Objects.equals(currentMode, "Presents")) {
                modePoints.put(player, modePoints.get(player) + multiplyPoints(points));
                modeFullPoints.put(player, modeFullPoints.get(player) + multiplyPoints(points));
            }
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

    public void resetModeFullPoints(){
        modeFullPoints.clear();

        for(String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)){
            for(String player : TeamsConfig.get().getStringList("teams." + team + ".players")){
                modeFullPoints.put(player, 0);
            }
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
    public void startLobbyInterval(int seconds){
        String name = "break";
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = seconds;
            @Override
            public void run() {
                if(runningTimers.containsKey(name)) {
                    if (!pausedTimers.contains(name)) {
                        timeLeft--;
                        runningTimers.get(name).setValue(timeLeft);
                        bossBarBgTest();
                        if (timeLeft == 0) {
                            messageConsole("Timer finished.");
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

        runningTimers.put(name, new AbstractMap.SimpleEntry<>(task, seconds));
    }

//    Start a timer
    public void startTimer(int seconds, String name){
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = seconds;
            @Override
            public void run() {
                if(runningTimers.containsKey(name)) {
                    if (!pausedTimers.contains(name)) {
                        timeLeft--;
                        runningTimers.get(name).setValue(timeLeft);
                        bossBarBgTest();
                        if(currentMode.equals("Survival Games")){
                            for (Player player : getPlayers()) {
                                if(player.getGameMode().equals(GameMode.SPECTATOR)) { continue; }
                                List<Player> playersInCircle = getPlayersInCircle2D(sgCenter, currentBorderRadius);
                                if(!playersInCircle.contains(player) && player.getHealth()-1 > 0) {
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("\uD83D\uDE21"));
                                    player.damage(1.0);
                                } else if (!playersInCircle.contains(player) && player.getHealth()-1 <= 0){
                                    if(!Objects.equals(plugin.lastHitPlayer.get(player.getName()), "")){
                                        Player killer = Bukkit.getPlayer(lastHitPlayer.get(player.getName()));
                                        Player victim = player;
                                        Bukkit.getWorld("build").spawnParticle(Particle.RAID_OMEN, victim.getLocation().clone().add(0,1,0), 20, 0.2, 0.5, 0.2, 0);
                                        killer.playSound(killer.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 2);
                                        plugin.messagePlayer(victim, "§c\uD83D\uDC80 §7| §cYou died to " + plugin.getPlayerDisplayName(killer.getName()));
                                        victim.sendTitle("§c§lYOU DIED.", "", 0, 40, 10);
                                        plugin.playerKillCount.put(killer.getName(), plugin.playerKillCount.get(killer.getName()) + 1);

                                        Location location = victim.getLocation();

                                        for (ItemStack item : victim.getInventory().getContents()) {
                                            if (item != null && !item.getType().isAir()) {
                                                victim.getWorld().dropItemNaturally(location, item);
                                            }
                                        }

                                        victim.getInventory().clear();
                                        victim.getInventory().setArmorContents(null);
                                        victim.getInventory().setItemInOffHand(null);

                                        victim.setGameMode(GameMode.SPECTATOR);
                                        plugin.deadPlayers.add(victim.getName());
                                        plugin.earnPoints(killer.getName(), 40, true);
                                        killer.sendTitle("", "§e\uD83D\uDCB040" + " §7| §c\uD83D\uDC80 " + plugin.getPlayerDisplayName(victim.getName()), 0, 20, 0);
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (!p.getGameMode().equals(GameMode.SPECTATOR) && !p.getName().equals(killer.getName()) && PlayerConfig.get().getConfigurationSection("players").getKeys(false).contains(p.getName())) {
                                                int survivalPoints;
                                                if(plugin.deadPlayers.size() > 20) { survivalPoints = 5; }
                                                else if (plugin.deadPlayers.size() > 10) { survivalPoints = 7; }
                                                else { survivalPoints = 10; }

                                                plugin.messagePlayer(p, "§e\uD83D\uDCB0" + survivalPoints + " §7| " + plugin.formatKillMessage(killer.getName(), victim.getName()));
                                                plugin.earnPoints(p.getName(), survivalPoints, true);
                                            } else {
                                                plugin.messagePlayer(p, "§c\uD83D\uDC80 §7| " + plugin.formatKillMessage(killer.getName(), victim.getName()));
                                            }
                                        }
                                        plugin.killRecord.add(plugin.getPlayerDisplayName(killer.getName()) + " §c⚔ " + plugin.getPlayerDisplayName(victim.getName()));
                                        switch (plugin.deadPlayers.size()) {
                                            case 24:
                                                plugin.newBorderRadius = 26;
                                                for (Player p : Bukkit.getOnlinePlayers()) {
                                                    p.sendTitle("", "§e⚠ Border Shrinking ⚠", 0, 40, 10);
                                                }
                                                break;
                                            case 16:
                                                plugin.newBorderRadius = 56;
                                                for (Player p : Bukkit.getOnlinePlayers()) {
                                                    p.sendTitle("", "§e⚠ Border Shrinking ⚠", 0, 40, 10);
                                                }
                                                break;
                                            case 8:
                                                plugin.newBorderRadius = 116;
                                                for (Player p : Bukkit.getOnlinePlayers()) {
                                                    p.sendTitle("", "§e⚠ Border Shrinking ⚠", 0, 40, 10);
                                                }
                                                break;
                                            case 4:
                                                plugin.newBorderRadius = 176;
                                                for (Player p : Bukkit.getOnlinePlayers()) {
                                                    p.sendTitle("", "§e⚠ Border Shrinking ⚠", 0, 40, 10);
                                                }
                                                break;
                                            default:
                                                break;
                                        }
                                        boolean teamDead = true;
                                        for (String player2 : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + victim.getName() + ".team") + ".players")) {
                                            if (!plugin.deadPlayers.contains(player2)) {
                                                teamDead = false;
                                                break;
                                            }
                                        }

                                        if (teamDead) {
                                            for (Player player2 : Bukkit.getOnlinePlayers()) {
                                                plugin.messagePlayer(player2, "\n§c§l\uD83D\uDC80 §7| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + victim.getName() + ".team")) + " §chave been eliminated.\n§f");
                                            }
                                            plugin.deadTeams.add(PlayerConfig.get().getString("players." + victim.getName() + ".team"));
                                        }

                                        Set<String> teamList = new HashSet<>();

                                        for (Player player3 : plugin.getPlayers()) {
                                            String team = PlayerConfig.get().getString("players." + player3.getName() + ".team");
                                            if (team != null && !team.isEmpty()) {
                                                teamList.add(team);
                                            }
                                        }

                                        if (plugin.deadTeams.size() == teamList.size() - 1) {
                                            for(Player p : plugin.getPlayers()) {
                                                if(!p.getGameMode().equals(GameMode.SPECTATOR)) {
                                                    plugin.winningTeam = PlayerConfig.get().getString("players." + p.getName() + ".team");
                                                    break;
                                                }
                                            }
                                            if(Objects.equals(plugin.winningTeam, "")) {
                                                plugin.winningTeam = "NO TEAM";
                                            }
                                            plugin.deadTeams.clear();
                                            plugin.runningTimers.remove("survivalgames");
                                            plugin.gameEnd();
                                        }
                                    } else {
                                        Player victim = player;
                                        Bukkit.getWorld("build").spawnParticle(Particle.RAID_OMEN, victim.getLocation().clone().add(0,1,0), 20, 0.2, 0.5, 0.2, 0);
                                        plugin.messagePlayer(victim, "§c\uD83D\uDC80 §7| §cYou died.");
                                        victim.sendTitle("§c§lYOU DIED.", "", 0, 40, 10);

                                        Location location = victim.getLocation();

                                        for (ItemStack item : victim.getInventory().getContents()) {
                                            if (item != null && !item.getType().isAir()) {
                                                victim.getWorld().dropItemNaturally(location, item);
                                            }
                                        }

                                        victim.getInventory().clear();
                                        victim.getInventory().setArmorContents(null);
                                        victim.getInventory().setItemInOffHand(null);

                                        victim.setGameMode(GameMode.SPECTATOR);
                                        plugin.deadPlayers.add(victim.getName());
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            if (!p.getGameMode().equals(GameMode.SPECTATOR) && PlayerConfig.get().getConfigurationSection("players").getKeys(false).contains(p.getName())) {
                                                int survivalPoints;
                                                if(plugin.deadPlayers.size() > 20) { survivalPoints = 5; }
                                                else if (plugin.deadPlayers.size() > 10) { survivalPoints = 7; }
                                                else { survivalPoints = 10; }

                                                plugin.messagePlayer(p, "§e\uD83D\uDCB0" + survivalPoints + " §7| " + plugin.formatDeathMessage(victim.getName()));
                                                plugin.earnPoints(p.getName(), survivalPoints, true);
                                            } else {
                                                plugin.messagePlayer(p, "§c\uD83D\uDC80 §7| " + plugin.formatDeathMessage(victim.getName()));
                                            }
                                        }
                                        plugin.killRecord.add(plugin.getPlayerDisplayName(victim.getName()) + " §c⚔ " + plugin.getPlayerDisplayName(victim.getName()));
                                        switch (plugin.deadPlayers.size()) {
                                            case 24:
                                                plugin.newBorderRadius = 26;
                                                for (Player p : Bukkit.getOnlinePlayers()) {
                                                    p.sendTitle("", "§e⚠ Border Shrinking ⚠", 0, 40, 10);
                                                }
                                                break;
                                            case 16:
                                                plugin.newBorderRadius = 56;
                                                for (Player p : Bukkit.getOnlinePlayers()) {
                                                    p.sendTitle("", "§e⚠ Border Shrinking ⚠", 0, 40, 10);
                                                }
                                                break;
                                            case 8:
                                                plugin.newBorderRadius = 116;
                                                for (Player p : Bukkit.getOnlinePlayers()) {
                                                    p.sendTitle("", "§e⚠ Border Shrinking ⚠", 0, 40, 10);
                                                }
                                                break;
                                            case 4:
                                                plugin.newBorderRadius = 176;
                                                for (Player p : Bukkit.getOnlinePlayers()) {
                                                    p.sendTitle("", "§e⚠ Border Shrinking ⚠", 0, 40, 10);
                                                }
                                                break;
                                            default:
                                                break;
                                        }
                                        boolean teamDead = true;
                                        for (String player2 : TeamsConfig.get().getStringList("teams." + PlayerConfig.get().getString("players." + victim.getName() + ".team") + ".players")) {
                                            if (!plugin.deadPlayers.contains(player2)) {
                                                teamDead = false;
                                                break;
                                            }
                                        }

                                        if (teamDead) {
                                            for (Player player2 : Bukkit.getOnlinePlayers()) {
                                                plugin.messagePlayer(player2, "\n§c§l\uD83D\uDC80 §7| " + plugin.getTeamDisplayName(PlayerConfig.get().getString("players." + victim.getName() + ".team")) + " §chave been eliminated.\n§f");
                                            }
                                            plugin.deadTeams.add(PlayerConfig.get().getString("players." + victim.getName() + ".team"));
                                        }

                                        Set<String> teamList = new HashSet<>();

                                        for (Player player3 : plugin.getPlayers()) {
                                            String team = PlayerConfig.get().getString("players." + player3.getName() + ".team");
                                            if (team != null && !team.isEmpty()) {
                                                teamList.add(team);
                                            }
                                        }

                                        if (plugin.deadTeams.size() == teamList.size() - 1) {
                                            for(Player p : plugin.getPlayers()) {
                                                if(!p.getGameMode().equals(GameMode.SPECTATOR)) {
                                                    plugin.winningTeam = PlayerConfig.get().getString("players." + p.getName() + ".team");
                                                    break;
                                                }
                                            }
                                            if(Objects.equals(plugin.winningTeam, "")) {
                                                plugin.winningTeam = "NO TEAM";
                                            }
                                            plugin.deadTeams.clear();
                                            plugin.runningTimers.remove("survivalgames");
                                            plugin.gameEnd();
                                        }
                                    }

                                } else {
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(""));
                                }
                            }
                            if((timeLeft % 2) == 0) {
                                if (newBorderRadius != currentBorderRadius) {
                                    currentBorderRadius-=2;
                                }
                                spawnCircleParticles(sgCenter, currentBorderRadius, (int) (currentBorderRadius * 1.1));
                            }
                        }
                        if(currentMode.equals("Slime Golf") && currentRound == 3){
                            switch(timeLeft) {
                                case 270:
                                    thunderStormEvent();
                                    break;
                                case 180:
                                    honeyIShrunkTheRosterEvent();
                                    break;
                                default:
                                    break;
                            }
                        }
                        if(timeLeft == 30){
                            for (Player player : getPlayers()) {
                                messagePlayer(player, """
                                §8
                                §8
                                §7[§c!§7] §c§l30 seconds remaining..
                                §8
                                """);
                            }
                        }
                        if (timeLeft == 0) {
                            messageConsole("Timer finished.");
                            gameEnd();
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

        runningTimers.put(name, new AbstractMap.SimpleEntry<>(task, seconds));
    }

    public List<Player> getPlayersInCircle2D(Location center, double radius) {
        return Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.getWorld().equals(Bukkit.getWorld("build")))
                .filter(player -> {
                    Location loc = player.getLocation();
                    double dx = loc.getX() - center.getX();
                    double dz = loc.getZ() - center.getZ();
                    return (dx * dx + dz * dz) <= (radius * radius);
                })
                .collect(Collectors.toList());
    }

    public void spawnCircleParticles(Location center, double radius, int points) {

        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 4.0F);
        World world = Bukkit.getWorld("build");

        Location particleLoc = center.clone();

        for (int level = 0; level < 50; level++) {
            double yOffset = level * 4;

            for (int i = 0; i < points; i++) {
                double angle = 2 * Math.PI * i / points;
                double x = center.getX() + radius * Math.cos(angle);
                double z = center.getZ() + radius * Math.sin(angle);
                double y = center.getY() + yOffset;

                particleLoc.setX(x);
                particleLoc.setY(y);
                particleLoc.setZ(z);
               world.spawnParticle(Particle.DUST, particleLoc, 1, 0, 0, 0, 0, dustOptions, false);
            }
        }
    }

    public void startStopwatch(int seconds, String name){
        if(name.equals("slimegolf")){
            Location textLoc = new Location(Bukkit.getWorld("build"), 727.9, -58, 27.5);
            textDisplay = Bukkit.getWorld("build").spawn(textLoc, TextDisplay.class);
            textDisplay.setText("00:00");
            textDisplay.setRotation(90, 0);
            textDisplay.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
            Quaternionf quat = new Quaternionf();
            Transformation transform = new Transformation(
                    new Vector3f(0, 0, 0),
                    quat,
                    new Vector3f(10.0f, 10.0f, 10.0f),
                    quat
            );

            textDisplay.setTransformation(transform);
        }
        BukkitTask task = new BukkitRunnable() {
            int timeElapsed = -1;
            @Override
            public void run() {
                if (runningTimers.containsKey(name)) {
                    if (!pausedTimers.contains(name)) {
                        timeElapsed++;
                        runningTimers.get(name).setValue(timeElapsed);
                        if (name.equals("slimegolf")) {
                            textDisplay.setText(getTimer("slimegolf"));
                        }
                        if (timeElapsed == seconds) {
                            messageConsole("Timer finished.");
                            runningTimers.remove(name);
                            cancel();
                        }
                    }
                } else {
                    messageConsole("Stopwatch removed by external factor.");
                    cancel();
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put(name, new AbstractMap.SimpleEntry<>(task, 0));
    }

    public String getTimer(String timer) {
        return LocalTime.of(0, runningTimers.get(timer).getValue() / 60, runningTimers.get(timer).getValue() % 60).format(DateTimeFormatter.ofPattern("mm:ss"));
    }

    public String getTimerValue(Integer time) {
        return LocalTime.of(0, time / 60, time % 60).format(DateTimeFormatter.ofPattern("mm:ss"));
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
                int timeDeduced;

                @Override
                public void run() {
                    if (!pausedTimers.contains("teleporttimer")) {
                        timeLeft--;
                        runningTimers.get("teleporttimer").setValue(timeLeft);
                        switch(timeLeft){
                            case 0:
                                for (Player player : getPlayers()) {
                                    player.teleport(location);
                                }
                                runningTimers.remove("teleporttimer");
                                cancel();
                                break;
                            case 5, 4, 3:
                                timeDeduced = timeLeft - 2;
                                for (Player player : getPlayers()) {
                                    player.sendTitle("", "§6Teleporting in §c" + timeDeduced + "...", 0, 20, 20);
                                }
                                break;
                            case 2:
                                for (Player player : getPlayers()) {
                                    player.sendTitle("\uE023", "", 20, 40, 20);
                                }
                                break;
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
            int timeDeduced;
            @Override
            public void run() {
                if(!pausedTimers.contains("teleporttimerteam")) {
                    timeLeft--;
                    runningTimers.get("teleporttimerteam").setValue(timeLeft);
                    switch(timeLeft){
                        case 0:
                            for (Player player : getPlayers()) {
                                Location tplocation = TeleportConfig.get().getLocation("teams." + PlayerConfig.get().getString("players." + player.getName() + ".team") + "." + location);
                                player.teleport(tplocation);
                            }
                            runningTimers.remove("teleporttimerteam");
                            cancel();
                            break;
                        case 5, 4, 3:
                            timeDeduced = timeLeft - 2;
                            for (Player player : getPlayers()) {
                                player.sendTitle("", "§6Teleporting in §c" + timeDeduced + "...", 0, 20, 20);
                            }
                            break;
                        case 2:
                            for (Player player : getPlayers()) {
                                player.sendTitle("\uE023", "", 20, 40, 20);
                            }
                            break;
                    }
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("teleporttimerteam", new AbstractMap.SimpleEntry<>(task, 6));
    }

    public void finaleTeamTeleport(String location, int countdown){
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = countdown+1;
            int timeDeduced;
            List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());
            String firstTeam = leaderteams.getFirst();
            String secondTeam = leaderteams.get(1);
            List<String> firstPlayers = TeamsConfig.get().getStringList("teams." + firstTeam + ".players");
            List<String> secondPlayers = TeamsConfig.get().getStringList("teams." + secondTeam + ".players");
            World world = Bukkit.getWorld("build");
            @Override
            public void run() {
                if(!pausedTimers.contains("teleporttimerteam")) {
                    timeLeft--;
                    runningTimers.get("teleporttimerteam").setValue(timeLeft);
                    switch(timeLeft){
                        case 0:
                            for(Player player : getPlayers()) {
                                if(!firstPlayers.contains(player.getName()) && !secondPlayers.contains(player.getName())) {
                                    Location tplocation = TeleportConfig.get().getLocation("spectators." + location);
                                    player.teleport(tplocation);
                                }
                            }
                            for(String player : firstPlayers){
                                Location tplocation = TeleportConfig.get().getLocation("teams." + PlayerConfig.get().getString("players." + player + ".team") + "." + location);
                                if(Bukkit.getPlayer(player) != null){
                                    Player p = Bukkit.getPlayer(player);
                                    p.teleport(tplocation);
                                }
                            }
                            for(String player : secondPlayers){
                                Location tplocation = TeleportConfig.get().getLocation("teams." + PlayerConfig.get().getString("players." + player + ".team") + "." + location);
                                if(Bukkit.getPlayer(player) != null){
                                    Player p = Bukkit.getPlayer(player);
                                    p.teleport(tplocation);
                                }
                            }
                            runningTimers.remove("teleporttimerteam");
                            cancel();
                            break;
                        case 5, 4, 3:
                            timeDeduced = timeLeft - 2;
                            for (Player player : getPlayers()) {
                                player.sendTitle("", "§6Teleporting in §c" + timeDeduced + "...", 0, 20, 20);
                            }
                            break;
                        case 2:
                            for (Player player : getPlayers()) {
                                player.sendTitle("\uE023", "", 20, 40, 20);
                            }
                            break;
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

    public String getPlayerDisplayName(String player) {
        if (PlayerConfig.get().getConfigurationSection("players").contains(player)) {
            String team = PlayerConfig.get().getString("players." + player + ".team");
            return TeamsConfig.get().getString("teams." + team + ".colour") + TeamsConfig.get().getString("teams." + team + ".icon") + player;
        } else {
            return "§7§l\uD83D\uDD27§7" + player;
        }
    }

    public void clearInventories(){
        PlayerInventory inv;
        ItemStack air = new ItemStack(Material.AIR);
        ItemStack[] armour = new ItemStack[]{
                air, air, air, air
        };
        for(Player player : getPlayers()){
            inv = player.getInventory();
            inv.clear();
            inv.setArmorContents(armour);
            inv.setItemInOffHand(air);
        }
    }


    public void startSlimeGolf(){
        fillVotingSpace(3);
        plugin.shopAllowed = false;
        clearInventories();
        if(currentRound == 1){
            setPreviousPlacements();
            resetModeFullPoints();
        }
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 61;
            @Override
            public void run() {
                if(!pausedTimers.contains("slimegolfstart")) {
                    timeLeft--;
                    runningTimers.get("slimegolfstart").setValue(timeLeft);
                    bossBarBgTest();
                    switch (timeLeft) {
                        case 60:
                            if(currentRound == 1) {
                                teamTeleport("slimegolf", 5);
                                teleportSpectators(TeleportConfig.get().getLocation("spectators.slimegolf"), 5);
                            } else {
                                teamTeleport("slimegolf2", 5);
                                teleportSpectators(TeleportConfig.get().getLocation("spectators.slimegolf2"), 5);
                            }
                            resetModePoints();
                            resetSlimeGolf();
                            resetSlimeCompletions();
                            break;
                        case 55:
                            try {
                                glowTeams();
                            } catch (ReflectiveOperationException e) {
                                throw new RuntimeException(e);
                            }
                            currentMode = "Slime Golf";
                            addToGameOrder(currentMode);
                            for (Player player : getPlayers()) {
                                PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 90, 1, false, false);
                                player.addPotionEffect(PotionEffect);
                                player.setGameMode(GameMode.SURVIVAL);
                            }
                            if(currentRound > 1){
                                timeLeft = 26;
                            }
                            break;

                        case 50:
                            playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §r⏳ §eWelcome to §a§lSlime Golf§e! The aim of the game is to hit your slimey ball into the hole at the end of the course as quickly as possible!
                                        §8
                                        """);
                            }
                            break;
                        case 30:
                            playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §r⏳ §eUse your §aknockback stick §eand work as a team, jump ahead and plan out your strategy, player-sized shortcuts will help you get ahead of the slime for strategic putting strategies!
                                        §8
                                        """);
                            }
                            break;
                        case 25:
                            if(currentRound == 2) {
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §a§lSlime Golf §c§lRound 2§e! Time for a change of scenery...
                                            §8
                                            """);
                                }
                            }
                            if(currentRound == 3) {
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §a§lSlime Golf§e! This is the §b§lMODIFIER ROUND§e, so watch out because anything can happen! Get your slime in the hole as fast as you can...
                                            §8
                                            """);
                                }
                            }
                            break;
                        case 10:
                            if(currentRound == 1) {
                                teamTeleport("slimegolf", 0);
                            } else {
                                teamTeleport("slimegolf2", 0);
                            }
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
                            ItemStack knockbackStick = new ItemStack(Material.STICK);
                            knockbackStick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);

                            ItemStack fishingRod = new ItemStack(Material.FISHING_ROD);
                            fishingRod.addUnsafeEnchantment(Enchantment.UNBREAKING, 3);
                            for (Player player : getPlayers()) {
                                player.getInventory().addItem(knockbackStick);
                                player.getInventory().addItem(fishingRod);
                            }
                            for(Player p : Bukkit.getOnlinePlayers()){
                                messagePlayer(p, """
                                        §8
                                        §8
                                        §8[§c§l!§8] §7Game Starting in §c§l10 seconds...
                                        §8
                                        """);
                            }
                            break;
                        case 5, 4, 3, 2, 1:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 0.3F*(6-timeLeft));
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.sendTitle("§c§l▶ " + timeLeft + " ◀", "", 0, 20, 20);
                            }
                            break;
                        case 0:
                            if(currentRound == 1) {
                                for (int[] slimeCmdCoord : slimeCmdCoords) {
                                    for (int i = 0; i <= 1540; i += 220) {
                                        Bukkit.getWorld("build").getBlockAt(slimeCmdCoord[0], slimeCmdCoord[1], slimeCmdCoord[2] + i).setType(Material.REDSTONE_BLOCK);
                                    }
                                }
                            } else {
                                for (int[] slimeCmdCoord2 : slimeCmdCoords2) {
                                    for (int i = 0; i <= 1092; i += 156) {
                                        Bukkit.getWorld("build").getBlockAt(slimeCmdCoord2[0], slimeCmdCoord2[1], slimeCmdCoord2[2] + i).setType(Material.REDSTONE_BLOCK);
                                    }
                                }
                            }
                            if(currentRound == 1) {
                                Location slimeCoords = new Location(Bukkit.getWorld("build"), 809, 59, 1369);
                                for(int z = 1369; z <= 2909; z+=220){
                                    slimeCoords.setZ(z);
                                    Slime newSlime = (Slime) Bukkit.getWorld("build").spawnEntity(slimeCoords, EntityType.SLIME);
                                    newSlime.addPotionEffect(new PotionEffect(
                                            PotionEffectType.RESISTANCE,
                                            Integer.MAX_VALUE,
                                            255,
                                            false,
                                            false,
                                            false
                                    ));
                                    newSlime.setSize(6);
                                    newSlime.setRemoveWhenFarAway(true);
                                    newSlime.setPersistent(true);
                                    slimeGolfSlime.add(newSlime);
                                }
                            } else {
                                Location slimeCoords = new Location(Bukkit.getWorld("build"), 847, 77, 3180);
                                for(int z = 3180; z <= 4272; z+=156){
                                    slimeCoords.setZ(z);
                                    Slime newSlime = (Slime) Bukkit.getWorld("build").spawnEntity(slimeCoords, EntityType.SLIME);
                                    newSlime.addPotionEffect(new PotionEffect(
                                            PotionEffectType.RESISTANCE,
                                            Integer.MAX_VALUE,
                                            255,
                                            false,
                                            false,
                                            false
                                    ));
                                    newSlime.setSize(6);
                                    newSlime.setRemoveWhenFarAway(true);
                                    newSlime.setPersistent(true);
                                    slimeGolfSlime.add(newSlime);
                                }
                            }

                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 2);
                            playMusicAll(Sound.MUSIC_DISC_CAT);
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                PotionEffect PotionEffect = new PotionEffect(PotionEffectType.RESISTANCE, 12000, 1, false, false);
                                player.addPotionEffect(PotionEffect);
                                player.sendTitle("§a§l▶ GO! ◀", "", 0, 40, 0);
                            }
                            startTimer(300, "slimegolftimer");
                            startStopwatch(300, "slimegolf");
                            pvpEnabled = true;
                            doubleJumpEnabled = true;
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


    public void startSlimeGolfFinale(){
        setPreviousPlacements();
        List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());
        String firstTeam = leaderteams.getFirst();
        String secondTeam = leaderteams.get(1);
        List<String> firstPlayers = TeamsConfig.get().getStringList("teams." + firstTeam + ".players");
        List<String> secondPlayers = TeamsConfig.get().getStringList("teams." + secondTeam + ".players");
        plugin.shopAllowed = false;
        clearInventories();
        if(currentRound == 1){
            resetModeFullPoints();
        }
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 41;
            @Override
            public void run() {
                if(!pausedTimers.contains("slimegolfstart")) {
                    timeLeft--;
                    runningTimers.get("slimegolfstart").setValue(timeLeft);
                    bossBarBgTest();
                    switch (timeLeft) {
                        case 40:
                            currentMode = "Slime Golf";
                            finaleTeamTeleport("slimegolffinale", 5);
                            teleportSpectators(TeleportConfig.get().getLocation("spectators.slimegolffinale"), 5);
                            resetModePoints();
                            resetSlimeGolf();
                            resetSlimeCompletions();
                            break;
                        case 35:
                            try {
                                glowTeams();
                            } catch (ReflectiveOperationException e) {
                                throw new RuntimeException(e);
                            }

                            for (Player player : getPlayers()) {
                                if(firstPlayers.contains(player.getName()) || secondPlayers.contains(player.getName())) {
                                    PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 90, 1, false, false);
                                    player.addPotionEffect(PotionEffect);
                                    player.setGameMode(GameMode.SURVIVAL);
                                } else {
                                    player.setGameMode(GameMode.SPECTATOR);
                                }
                            }
                            break;

                        case 30:
                            playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §r⏳ §eSlime Golf Finale! 1 round only.
                                        §8
                                        """);
                            }
                            break;
                        case 20:
                            playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §r⏳ §eFirst team to §breach the finish §ewins! Checkpoint times do not count, be as fast as possible!
                                        §8
                                        """);
                            }
                            break;
                        case 10:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
                            ItemStack knockbackStick = new ItemStack(Material.STICK);
                            knockbackStick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);

                            ItemStack fishingRod = new ItemStack(Material.FISHING_ROD);
                            fishingRod.addUnsafeEnchantment(Enchantment.UNBREAKING, 3);
                            for (Player player : getPlayers()) {
                                player.getInventory().addItem(knockbackStick);
                                player.getInventory().addItem(fishingRod);
                            }
                            for(Player p : Bukkit.getOnlinePlayers()){
                                messagePlayer(p, """
                                        §8
                                        §8
                                        §8[§c§l!§8] §7Game Starting in §c§l10 seconds...
                                        §8
                                        """);
                            }
                            break;
                        case 5, 4, 3, 2, 1:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 0.3F*(6-timeLeft));
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.sendTitle("§c§l▶ " + timeLeft + " ◀", "", 0, 20, 20);
                            }
                            break;
                        case 0:
                            for(int[] slimeCmdCoord : slimeCmdCoords){
                                for(int i = 0; i <= 1540; i+=220){
                                    Bukkit.getWorld("build").getBlockAt(slimeCmdCoord[0], slimeCmdCoord[1], slimeCmdCoord[2]+i).setType(Material.REDSTONE_BLOCK);
                                }
                            }

                            Location slimeCoords = new Location(Bukkit.getWorld("build"), 809, 59, 1369);

                            for(int z = 1369; z <= 2909; z+=220){
                                slimeCoords.setZ(z);
                                Slime newSlime = (Slime) Bukkit.getWorld("build").spawnEntity(slimeCoords, EntityType.SLIME);
                                newSlime.addPotionEffect(new PotionEffect(
                                        PotionEffectType.RESISTANCE,
                                        Integer.MAX_VALUE,
                                        255,
                                        false,
                                        false,
                                        false
                                ));
                                newSlime.setSize(6);
                                newSlime.setRemoveWhenFarAway(true);
                                newSlime.setPersistent(true);
                                slimeGolfSlime.add(newSlime);
                            }

                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 2);
                            playMusicAll(Sound.MUSIC_DISC_CAT);
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                PotionEffect PotionEffect = new PotionEffect(PotionEffectType.RESISTANCE, 12000, 1, false, false);
                                player.addPotionEffect(PotionEffect);
                                player.sendTitle("§a§l▶ GO! ◀", "", 0, 40, 0);
                            }
                            startTimer(300, "slimegolftimer");
                            startStopwatch(300, "finale");
                            pvpEnabled = true;
                            doubleJumpEnabled = true;
                            runningTimers.remove("slimegolfstart");
                            cancel();
                            break;
                        default:
                            break;
                    }
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("slimegolfstart", new AbstractMap.SimpleEntry<>(task, 41));
    }

    public void resetSlimeGolf() {
        slimeCheckpoints.clear();
        for (int i = 1; i <= 6; i++) {
            slimeCheckpoints.put(i, 1);
        }
    }

    public void resetColourDash(){
        colourDashCheckpoints.clear();
        cdCompletions = 0;
    }

    public void startColourDash(){
        fillVotingSpace(4);
        plugin.shopAllowed = false;
        clearInventories();
        if(currentRound == 1){
            setPreviousPlacements();
            resetModeFullPoints();
        }
        World world = Bukkit.getWorld("build");
        Block block;
        for(int x = 137; x >= -27; x--){
            for(int y = 195; y >= 127; y--){
                for(int z = 804; z <= 1345; z++){
                    block = world.getBlockAt(x,y,z);
                    if(colourDashBlocks.contains(block.getType())){
                        block.setType(Material.AIR);
                    }
                }
            }
        }
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 61;
            @Override
            public void run() {
                if(runningTimers.containsKey("colourdashstart")) {
                    if (!pausedTimers.contains("colourdashstart")) {
                        timeLeft--;
                        runningTimers.get("colourdashstart").setValue(timeLeft);
                        bossBarBgTest();
                        switch (timeLeft) {
                            case 60:
                                if(plugin.currentRound == 1) {
                                    Bukkit.getWorld("build").getBlockAt(121, 139, 790).setType(Material.REDSTONE_BLOCK);
                                    Bukkit.getWorld("build").getBlockAt(121, 139, 790).setType(Material.AIR);
                                    teleportPlayers(TeleportConfig.get().getLocation("players.colourdash"), 5);
                                    teleportSpectators(TeleportConfig.get().getLocation("spectators.colourdash"), 5);
                                }
                                if(plugin.currentRound == 2){
                                    Bukkit.getWorld("build").getBlockAt(75, 175, 1331).setType(Material.REDSTONE_BLOCK);
                                    Bukkit.getWorld("build").getBlockAt(75, 175, 1331).setType(Material.AIR);
                                    teleportPlayers(TeleportConfig.get().getLocation("players.colourdash2"), 5);
                                    teleportSpectators(TeleportConfig.get().getLocation("spectators.colourdash2"), 5);
                                }
                                resetColourDash();
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
                                addToGameOrder(currentMode);
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "usc setScoreboard " + p.getName() + " ColourDash");
                                }
                                for (Player player : getPlayers()) {
                                    PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 90, 1, false, false);
                                    player.addPotionEffect(PotionEffect);
                                    player.setGameMode(GameMode.SURVIVAL);
                                }
                                break;
                            case 50:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                if(plugin.currentRound == 1) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        messagePlayer(player, """
                                                §8
                                                §8
                                                §r⏳ §eWelcome to §a§lColour Dash§e! This is a race to the finish, the map is bigger, and there's multiple routes for your team to take so make the right choice!
                                                §8
                                                """);
                                    }
                                }
                                if(plugin.currentRound == 2){
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §eWelcome to §a§lColour Dash §c§lʀᴏᴜɴᴅ ᴛᴡᴏ§e! Now you know the course.. or do you? This time we're going backwards on a different route! Good luck!
                                            §8
                                            """);
                                    }
                                    timeLeft = 16;
                                }
                                break;
                            case 30:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §eIn this mode speed is the most important factor! The faster you reach each checkpoint, the more points you earn, so get dashing!
                                            §8
                                            """);
                                }
                                break;
                            case 10:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §8[§c§l!§8] §7Game Starting in §c§l10 seconds...
                                            §8
                                            """);
                                }
                                break;
                            case 5, 4, 3, 2, 1:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 0.3F * (6 - timeLeft));
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendTitle("§c§l▶ " + timeLeft + " ◀", "", 0, 20, 20);
                                }
                                break;
                            case 0:
                                if(plugin.currentRound == 1) {
                                    Bukkit.getWorld("build").getBlockAt(121, 139, 791).setType(Material.REDSTONE_BLOCK);
                                    Bukkit.getWorld("build").getBlockAt(121, 139, 791).setType(Material.AIR);
                                }
                                if(plugin.currentRound == 2){
                                    Bukkit.getWorld("build").getBlockAt(75, 174, 1331).setType(Material.REDSTONE_BLOCK);
                                    Bukkit.getWorld("build").getBlockAt(75, 174, 1331).setType(Material.AIR);
                                }
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 2);
                                playMusicAll(Sound.MUSIC_DISC_BLOCKS);
                                pvpEnabled = true;
                                for (Player player : getPlayers()) {
                                    PotionEffect PotionEffect = new PotionEffect(PotionEffectType.RESISTANCE, 12000, 1, false, false);
                                    player.addPotionEffect(PotionEffect);
                                    ItemStack infiniteBlocks = new ItemStack(Material.getMaterial(TeamsConfig.get().getString("teams." + PlayerConfig.get().getString("players." + player.getName() + ".team") + ".colourname") + "_CONCRETE"));
                                    infiniteBlocks.setAmount(64);
                                    player.getInventory().addItem(infiniteBlocks);

                                    ItemStack pickaxe = new ItemStack(Material.IRON_PICKAXE);
                                    pickaxe.addEnchantment(Enchantment.UNBREAKING, 3);
                                    pickaxe.addEnchantment(Enchantment.EFFICIENCY, 1);
                                    player.getInventory().addItem(pickaxe);
                                    player.sendTitle("§a§l▶ DASH! ◀", "", 0, 40, 0);
                                }
                                startTimer(540, "colourdash");
                                startStopwatch(540  , "colourdashwatch");
                                runningTimers.remove("colourdashstart");
                                cancel();
                                break;
                            default:
                                break;
                        }
                    }
                } else {
                    cancel();
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("colourdashstart", new AbstractMap.SimpleEntry<>(task, 61));
    }


    public void startCraftalot(){
        fillVotingSpace(2);
        setPreviousPlacements();
        plugin.shopAllowed = false;
        clearInventories();
        resetModeFullPoints();
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 61;
            Random r = new Random();
            String itemToCraft;
            @Override
            public void run() {
                if(runningTimers.containsKey("craftalotstart")) {
                    if (!pausedTimers.contains("craftalotstart")) {
                        timeLeft--;
                        runningTimers.get("craftalotstart").setValue(timeLeft);
                        bossBarBgTest();
                        switch (timeLeft){
                            case 60:
                                for(int x = 161; x <= 165; x++){
                                    for(int y = 141; y <= 146; y++){
                                        Bukkit.getWorld("build").getBlockAt(x, y, 834).setType(Material.BARRIER);
                                    }
                                }
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
                                addToGameOrder(currentMode);
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "usc setScoreboard " + p.getName() + " Craftalot");
                                }
                                for (Player player : getPlayers()) {
                                    PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 90, 1, false, false);
                                    player.addPotionEffect(PotionEffect);
                                    player.setGameMode(GameMode.SURVIVAL);
                                }
                                break;

                            case 50:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §eWelcome to §a§lCraftalot§e! We hope you've memorised your crafting recipes! Because this mode is all about retrieving the right materials and crafting what Edguard asks to earn points!
                                            §8
                                            """);
                                }
                                break;
                            case 30:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §eRun to Edguard and right-click him to get your orders, then retrieve the needed materials through the pipes marked on the map, and craft as many items as possible before the time runs out!
                                            §8
                                            """);
                                }
                                break;
                            case 17:
                                setupCraftlist();
                                break;
                            case 10:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §8[§c§l!§8] §7Game Starting in §c§l10 seconds...
                                            §8
                                            """);
                                }
                                break;
                            case 5, 4, 3, 2, 1:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 0.3F * (6 - timeLeft));
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendTitle("§c§l▶ " + timeLeft+ " ◀", "", 0, 20, 20);
                                }
                                break;
                            case 0:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 2);
                                playMusicAll(Sound.MUSIC_DISC_FAR);
                                for (Player player : getPlayers()) {
                                    player.sendTitle("§a§l▶ CRAFT! ◀", "§7Speak to Edguard.", 0, 40, 0);
                                    player.getInventory().clear();
                                    for (int i = 0; i <= 3; i++) {
                                        player.getInventory().addItem(craftalotKit()[i]);
                                    }
                                    player.getInventory().setItemInOffHand(craftalotKit()[4]);
                                    PotionEffect PotionEffect = new PotionEffect(PotionEffectType.WATER_BREATHING, 12000, 5, false, false);
                                    player.addPotionEffect(PotionEffect);
                                    PotionEffect = new PotionEffect(PotionEffectType.NIGHT_VISION, 12000, 1, false, false);
                                    player.addPotionEffect(PotionEffect);
                                }
                                for(int x = 161; x <= 165; x++){
                                    for(int y = 141; y <= 146; y++){
                                        Bukkit.getWorld("build").getBlockAt(x, y, 834).setType(Material.AIR);
                                    }
                                }
                                startTimer(600, "craftalot");
                                runningTimers.remove("craftalotstart");
                                cancel();
                                break;
                            default:
                                break;
                        }
                    }
                } else {
                    cancel();
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("craftalotstart", new AbstractMap.SimpleEntry<>(task, 61));
    }


    public void setupCraftlist(){
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 311;
            Random r = new Random();
            String itemToCraft;

            @Override
            public void run() {
                if (runningTimers.containsKey("craftlist")) {
                    if (!pausedTimers.contains("craftlist")) {
                        timeLeft--;
                        runningTimers.get("craftlist").setValue(timeLeft);
                        if (timeLeft >= 240 && timeLeft <= 260) {
                            do {
                                itemToCraft = CraftalotConfig.get().getStringList("craftlist.onetunnel").get(r.nextInt(CraftalotConfig.get().getStringList("craftlist.onetunnel").size()));
                            } while (craftList.contains(itemToCraft));
                            craftList.add(itemToCraft);
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.sendTitle("§f§lʟᴏᴀᴅɪɴɢ ᴄʀᴀғᴛs", "§b§l" + itemToCraft.replaceAll("_", " "), 0, 40, 0);
                            }
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 1F);
                        }
                        switch (timeLeft) {
                            case 310:
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendTitle("§f§lʟᴏᴀᴅɪɴɢ ᴄʀᴀғᴛs", "§7§k0000000", 0, 40, 0);
                                }
                                playSoundAll(Sound.ENTITY_CREEPER_PRIMED, 1F);
                                break;
                            case 289, 279, 269, 266, 263:
                                do {
                                    itemToCraft = CraftalotConfig.get().getStringList("craftlist.twotunnel").get(r.nextInt(CraftalotConfig.get().getStringList("craftlist.twotunnel").size()));
                                } while (craftList.contains(itemToCraft));
                                craftList.add(itemToCraft);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendTitle("§f§lʟᴏᴀᴅɪɴɢ ᴄʀᴀғᴛs", "§b§l" + itemToCraft.replaceAll("_", " "), 0, 20, 0);
                                }
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 1F);
                                break;
                            case 239:
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendTitle("§f§lᴄʀᴀғᴛʟɪsᴛ", "§a§lᴄᴏᴍᴘʟᴇᴛᴇ", 0, 60, 20);
                                }
                                playSoundAll(Sound.ENTITY_GENERIC_EXPLODE, 1F);
                            case 219:
                                playSoundAll(Sound.ENTITY_VILLAGER_YES, 1F);
                                break;
                        }
                        if(timeLeft == 219){
                            plugin.runningTimers.remove("craftlist");
                            cancel();
                        }
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(this, 0L, 1L);

        runningTimers.put("craftlist", new AbstractMap.SimpleEntry<>(task, 290));
    }

    public void resetCraftalot(){
        itemToCraft.clear();
        craftLists.clear();
        for(Player player : getPlayers()) {
            itemToCraft.put(player.getName(), "");
            craftLists.put(player.getName(), new ArrayList<>());
        }
        craftTop.clear();
    }

    public void startBridgeBuilders(){
        fillVotingSpace(0);
        setPreviousPlacements();
        plugin.shopAllowed = false;
        clearInventories();
        resetModeFullPoints();
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 61;
            @Override
            public void run() {
                if(runningTimers.containsKey("bridgebuildersstart")) {
                    if (!pausedTimers.contains("bridgebuildersstart")) {
                        timeLeft--;
                        runningTimers.get("bridgebuildersstart").setValue(timeLeft);
                        bossBarBgTest();
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
                                addToGameOrder(currentMode);
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "usc setScoreboard " + p.getName() + " BridgeBuilders");
                                }
                                for (Player player : getPlayers()) {
                                    PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 90, 1, false, false);
                                    player.addPotionEffect(PotionEffect);
                                    player.setGameMode(GameMode.ADVENTURE);
                                }
                                break;
                            case 54:
                                for (int i = 661; i >= 471; i -= 38) {
                                    for (int j = 244; j <= 489; j += 35) {
                                        Bukkit.getWorld("build").getBlockAt(j, -23, i).setType(Material.REDSTONE_BLOCK);
                                        Bukkit.getWorld("build").getBlockAt(j, -23, i).setType(Material.STONE);
                                    }
                                }
                                break;
                            case 50:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §eWelcome to §a§lBridge Builders§e! Building and speed are your ally! In this mode you build the course which you will race across! A mix of building and parkour skills!
                                            §8
                                            """);
                                }

                                World world = Bukkit.getWorld("build");
                                for(int x = 0; x < 8; x++){
                                    for(int z = 0; z < 6; z++){

                                        for(int x2 = 234+(35*x); x2 <= 240+(35*x); x2++){
                                            for(int y2 = -21; y2 <= -12; y2++){
                                                for(int z2 = 663-(38*z); z2 <= 677 - (38*z); z2++){
                                                    world.getBlockAt(x2, y2, z2).setType(Material.AIR);
                                                }
                                            }
                                        }
                                    }
                                }
                                break;
                            case 30:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §eEach segment has a different set of jumps to build, replicate the build and it will construct itself on the bridge for you to complete. First to the finish wins!
                                            §8
                                            """);
                                }
                                break;
                            case 10:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §8[§c§l!§8] §7Game Starting in §c§l10 seconds...
                                            §8
                                            """);
                                }
                                break;
                            case 5, 4, 3, 2, 1:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 0.3F * (6 - timeLeft));
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendTitle("§c§l▶ " + timeLeft + " ◀", "", 0, 20, 20);
                                }
                                break;
                            case 0:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 2);
                                playMusicAll(Sound.MUSIC_DISC_CHIRP);
                                for (Player player : getPlayers()) {
                                    player.setGameMode(GameMode.SURVIVAL);
                                    player.setAllowFlight(true);
                                    player.sendTitle("§a§l▶ BUILD! ◀", "", 0, 40, 0);
                                    for(Material block : getBridgeBlocks(0, PlayerConfig.get().getString("players." + player.getName() + ".team"))) {
                                        player.getInventory().addItem(new ItemStack(block, 64));
                                    }
                                }

                                List<String> teams = new ArrayList<>();
                                for(Player player : getPlayers()){
                                    if(!teams.contains(PlayerConfig.get().getString("players." + player.getName() + ".team"))){
                                        teams.add(PlayerConfig.get().getString("players." + player.getName() + ".team"));
                                    }
                                }
                                for(String team : teams) {
                                    plugin.buildTimeStamps.put(team, 0);
                                }
                                startTimer(390, "bridgebuilders");
                                runningTimers.remove("bridgebuildersstart");
                                cancel();
                                break;
                            default:
                                break;
                        }
                    }
                } else {
                    cancel();
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("bridgebuildersstart", new AbstractMap.SimpleEntry<>(task, 61));
    }

    public void resetBridgeBuilders(){
        bridgeTally.clear();
        bridgeCheckpoints.clear();
        bridgeJumpCheckpoints.clear();
        bridgeJumpRegister.clear();
        bridgeCourseTimes.clear();
        teamCheckpoints.clear();

        for(int i = 1; i <= 7; i++){
            bridgeCheckpoints.put(i, 1);
        }

        for(int i = 1; i <= 7; i++){
            bridgeJumpCheckpoints.put(i, 1);
        }

        for(int i = 1; i <= 7; i++){
            List<String> list = new ArrayList<>();
            bridgeJumpRegister.put(i, list);
        }

        for (String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
            teamCheckpoints.put(team, 0);
            bridgeTally.put(team, 0);
        }

    }

    public void resetCrumbleClash(){
        // Reset Stuff
    }

    public void startCrumbleClash(){
        fillVotingSpace(1);
        setPreviousPlacements();
        plugin.shopAllowed = false;
        clearInventories();
        if(currentRound == 1){
            resetModeFullPoints();
        }
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 61;
            @Override
            public void run() {
                if(runningTimers.containsKey("crumbleclashstart")) {
                    if (!pausedTimers.contains("crumbleclashstart")) {
                        timeLeft--;
                        runningTimers.get("crumbleclashstart").setValue(timeLeft);
                        bossBarBgTest();
                        switch (timeLeft) {
                            case 60:
                                for (Player p : getPlayers()) {
                                    lastHitPlayer.put(p.getName(), "");
                                }
                                teamTeleport("crumbleclash", 5);
                                teleportSpectators(TeleportConfig.get().getLocation("spectators.crumbleclash"), 5);
                                resetCrumbleClash();
                                resetModePoints();
                                break;
                            case 55:
                                currentMode = "Crumble Clash";
                                addToGameOrder(currentMode);
                                try {
                                    glowTeams();
                                } catch (ReflectiveOperationException e) {
                                    throw new RuntimeException(e);
                                }
                                for (Player player : getPlayers()) {
                                    PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 120, 1, false, false);
                                    player.addPotionEffect(PotionEffect);
                                    player.setGameMode(GameMode.SURVIVAL);
                                }
                                break;

                            case 50:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                if(currentRound == 2 || currentRound == 3) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        messagePlayer(player, """
                                                §8
                                                §8
                                                §r⏳ §eWelcome to §b§lCrumble Clash §c§lROUND """ + currentRound + """
                                                §e! Now you understand it, now §ulock in.
                                                §8
                                                """);
                                    }
                                } else {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §eWelcome to §b§lCrumble Clash§e! You and your team will have to watch your step, as you try not to fall from the platforms§e.
                                            §8
                                            """);
                                    }
                                }
                                break;
                            case 40:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §e§lAt random intervals, the entire game will switch up! At one point you could be playing spleef, and the next thing you know blocks will disappear below you as you run, and more!
                                            §8
                                            """);
                                }
                                break;
                            case 30:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §eYour individual placement will determine the game, its last team standing wins! With points being awarded depending on your entire teams placement.
                                            §8
                                            """);
                                }
                                break;
                            case 10:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    messagePlayer(p, """
                                            §8
                                            §8
                                            §8[§c§l!§8] §7Game Starting in §c§l10 seconds...
                                            §8
                                            """);
                                }
                                break;
                            case 5, 4, 3, 2, 1:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 0.3F * (6 - timeLeft));
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendTitle("§c§l▶ " + timeLeft + " ◀", "", 0, 20, 20);
                                }
                                break;
                            case 0:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 2);
                                playMusicAll(Sound.MUSIC_DISC_STAL);
                                for (Player player : getPlayers()) {
                                    PotionEffect PotionEffect = new PotionEffect(PotionEffectType.RESISTANCE, 12000, 1, false, false);
                                    player.addPotionEffect(PotionEffect);
                                    player.sendTitle("§a§l▶ CLASH! ◀", "", 0, 40, 0);
                                }
                                crumbleClashModeIterator();
                                runningTimers.remove("crumbleclashstart");
                                cancel();
                                break;
                            default:
                                break;
                        }
                    }
                } else {
                    cancel();
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("crumbleclashstart", new AbstractMap.SimpleEntry<>(task, 61));
    }

    public void crumbleClashModeIterator(){
        List<Integer> gameOrder = Arrays.asList(0,1,2);
        Collections.shuffle(gameOrder);
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 241;
            @Override
            public void run() {
                if(runningTimers.containsKey("crumbleclash")) {
                    if (!pausedTimers.contains("crumbleclash")) {
                        timeLeft--;
                        runningTimers.get("crumbleclash").setValue(timeLeft);
                        bossBarBgTest();
                        switch (timeLeft) {
                            case 240:
                                switch(gameOrder.getFirst()){
                                    case 0:
                                        ccEnableSpleef();
                                        break;
                                    case 1:
                                        ccEnableTNTRun();
                                        break;
                                    case 2:
                                        ccExplosiveBows();
                                        break;
                                }
                                break;
                            case 160:
                                switch(gameOrder.get(1)){
                                    case 0:
                                        ccEnableSpleef();
                                        break;
                                    case 1:
                                        ccEnableTNTRun();
                                        break;
                                    case 2:
                                        ccExplosiveBows();
                                        break;
                                }
                                break;
                            case 80:
                                switch(gameOrder.get(2)){
                                    case 0:
                                        ccEnableSpleef();
                                        break;
                                    case 1:
                                        ccEnableTNTRun();
                                        break;
                                    case 2:
                                        ccExplosiveBows();
                                        break;
                                }
                                break;
                            case 0:
                                runningTimers.remove("crumbleclash");
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

        runningTimers.put("crumbleclash", new AbstractMap.SimpleEntry<>(task, 141));
    }

    public void ccEnableSpleef(){
        clearInventories();
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 11;
            @Override
            public void run() {
                if(runningTimers.containsKey("ccnextmode")) {
                    if (!pausedTimers.contains("ccnextmode")) {
                        timeLeft--;
                        runningTimers.get("ccnextmode").setValue(timeLeft);
                        switch (timeLeft) {
                            case 10:
                            playSoundAll(Sound.ENTITY_TNT_PRIMED, 1F);
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle("§f§lᴍᴏᴅᴇ ᴄʜᴀɴɢᴇ", "§f§k000000", 0, 60, 20);
                            }
                            case 7:
                                playSoundAll(Sound.ITEM_BUCKET_FILL_POWDER_SNOW, 1F);
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle("§f§lᴍᴏᴅᴇ ᴄʜᴀɴɢᴇ", "§f§lSpleef", 0, 60, 20);
                                }
                            case 3,2,1:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 0.3F * (4 - timeLeft));
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendTitle("§c§l▶ " + timeLeft + " ◀", "", 0, 20, 20);
                                }
                            case 0:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 0.3F * 4);
                                ItemStack shovel = new ItemStack(Material.IRON_SHOVEL);
                                ItemMeta meta = shovel.getItemMeta();
                                meta.setUnbreakable(true);
                                meta.setDisplayName("§f§lShovellin' Shovel");
                                shovel.setItemMeta(meta);
                                for(Player p : getPlayers()){
                                    p.getInventory().addItem(shovel);
                                    messagePlayer(p, "[+] You have acquired: §f§lShovellin' Shovel");
                                }
                                blockBreak = true;
                                runningTimers.remove("ccnextmode");
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

        runningTimers.put("ccnextmode", new AbstractMap.SimpleEntry<>(task, 11));
        // Spleef Code
    }

    public void ccEnableTNTRun(){
        clearInventories();
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 11;
            @Override
            public void run() {
                if(runningTimers.containsKey("ccnextmode")) {
                    if (!pausedTimers.contains("ccnextmode")) {
                        timeLeft--;
                        runningTimers.get("ccnextmode").setValue(timeLeft);
                        switch (timeLeft) {
                            case 10:
                                playSoundAll(Sound.ENTITY_TNT_PRIMED, 1F);
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle("§f§lᴍᴏᴅᴇ ᴄʜᴀɴɢᴇ", "§f§k000000", 0, 60, 20);
                                }
                            case 7:
                                playSoundAll(Sound.BLOCK_SOUL_SAND_BREAK, 1F);
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle("§f§lᴍᴏᴅᴇ ᴄʜᴀɴɢᴇ", "§c§lDisappearing Block Run", 0, 60, 20);
                                }
                            case 3,2,1:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 0.3F * (4 - timeLeft));
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendTitle("§c§l▶ " + timeLeft + " ◀", "", 0, 20, 20);
                                }
                            case 0:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 0.3F * 4);
                                for(Player p : getPlayers()){
                                    messagePlayer(p, "[+] You have acquired: §c§lFloor Breaking Run");
                                }
                                tntRun = true;
                                runningTimers.remove("ccnextmode");
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

        runningTimers.put("ccnextmode", new AbstractMap.SimpleEntry<>(task, 11));
        // TNT Run Code
    }

    public void ccExplosiveBows(){
        clearInventories();
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 11;
            @Override
            public void run() {
                if(runningTimers.containsKey("ccnextmode")) {
                    if (!pausedTimers.contains("ccnextmode")) {
                        timeLeft--;
                        runningTimers.get("ccnextmode").setValue(timeLeft);
                        switch (timeLeft) {
                            case 10:
                                playSoundAll(Sound.ENTITY_TNT_PRIMED, 1F);
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle("§f§lᴍᴏᴅᴇ ᴄʜᴀɴɢᴇ", "§f§k000000", 0, 60, 20);
                                }
                            case 7:
                                playSoundAll(Sound.ENTITY_GENERIC_EXPLODE, 1F);
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle("§f§lᴍᴏᴅᴇ ᴄʜᴀɴɢᴇ", "§4§lBoom Bows", 0, 60, 20);
                                }
                            case 3,2,1:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 0.3F * (4 - timeLeft));
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendTitle("§c§l▶ " + timeLeft + " ◀", "", 0, 20, 20);
                                }
                            case 0:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 0.3F * 4);
                                ItemStack boombow = new ItemStack(Material.BOW);
                                boombow.addEnchantment(Enchantment.INFINITY, 1);
                                ItemMeta meta = boombow.getItemMeta();
                                meta.setUnbreakable(true);
                                meta.setDisplayName("§4§lBoom Bow");
                                boombow.setItemMeta(meta);
                                for(Player p : getPlayers()){
                                    p.getInventory().addItem(boombow);
                                    messagePlayer(p, "[+] You have acquired: §4§lBoom Bow");
                                }
                                tntBow = true;
                                runningTimers.remove("ccnextmode");
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

        runningTimers.put("ccnextmode", new AbstractMap.SimpleEntry<>(task, 11));
        // Explosive Bows Code
    }


    public void startZoomoGo(){
        fillVotingSpace(1);
        plugin.shopAllowed = false;
        clearInventories();
        if(currentRound == 1){
            setPreviousPlacements();
            resetModeFullPoints();
        }
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 61;
            @Override
            public void run() {
                if(runningTimers.containsKey("zoomogostart")) {
                    if (!pausedTimers.contains("zoomogostart")) {
                        timeLeft--;
                        runningTimers.get("zoomogostart").setValue(timeLeft);
                        bossBarBgTest();
                        switch (timeLeft) {
                            case 60:
                                for (Player p : getPlayers()) {
                                    lastHitPlayer.put(p.getName(), "");
                                }
                                teamTeleport("zoomogo", 5);
                                teleportSpectators(TeleportConfig.get().getLocation("spectators.zoomogo"), 5);
                                resetZoomoGo();
                                resetModePoints();
                                break;
                            case 55:
                                currentMode = "Zoomo Go";
                                addToGameOrder(currentMode);
                                try {
                                    glowTeams();
                                } catch (ReflectiveOperationException e) {
                                    throw new RuntimeException(e);
                                }
                                for (Player player : getPlayers()) {
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "usc setScoreboard " + player.getName() + " ZoomoGo");
                                    PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 120, 1, false, false);
                                    player.addPotionEffect(PotionEffect);
                                    player.setGameMode(GameMode.ADVENTURE);
                                }
                                for (int i = 1; i <= 26; i++) {
                                    summonIsland(zoomoIslands(i));
                                    if (i > 6) {
                                        destroyIsland(zoomoIslands(i));
                                    }
                                    summonIsland(zoomoIslands(27));
                                }
                                break;

                            case 50:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                if(currentRound == 2){
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §eWelcome to §a§lZoomo Go §c§lROUND """ + currentRound + """
                                            §e! Now you understand it, now §ulock in.
                                            §8
                                            """);
                                    }
                                } else if (currentRound == 3){
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §eWelcome to §a§lZoomo Go §c§lROUND """ + currentRound + """
                                            §e! This time try not to slip off.. as §b§lice platforms §ehave been enabled.
                                            §8
                                            """);
                                        player.sendTitle("§b§lɪᴄᴇ ᴘʟᴀᴛғᴏʀᴍs", "§8§oWatch your step!", 0, 60, 20);
                                    }
                                } else {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §eWelcome to §a§lZoomo Go§e! Keep moving fast and stay on the platforms! This game takes sumo to the next level with disappearing platforms and fast-movement gameplay! Watch out for §cRed Platforms§e.
                                            §8
                                            """);
                                    }
                                }
                                break;
                            case 40:
                                doubleJumpEnabled = true;
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendTitle("§c§lDouble Jump", "§7is now enabled.", 0, 20, 20);
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §c§lDouble Jump §ehas been §aenabled§e! Give it a go by double-tapping your space bar!
                                            §8
                                            """);
                                }
                                if(zoomoSpeed){
                                    playSoundAll(Sound.BLOCK_BREWING_STAND_BREW, 2);
                                    PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SPEED, 360, 2, false, false);
                                    for (Player player : getPlayers()) {
                                        player.addPotionEffect(PotionEffect);
                                    }
                                }
                                break;
                            case 30:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §eThere is no finish line, simply stay alive and knock other players off the platforms with your §aKnockback Stick§e! §bDouble jump §eis your ally, double-tap space bar and you will be sent flying in the direction you are facing!
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
                                }
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    messagePlayer(p, """
                                            §8
                                            §8
                                            §8[§c§l!§8] §7Game Starting in §c§l10 seconds...
                                            §8
                                            """);
                                }
                                break;
                            case 5, 4, 3, 2, 1:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 0.3F * (6 - timeLeft));
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendTitle("§c§l▶ " + timeLeft + " ◀", "", 0, 20, 20);
                                }
                                break;
                            case 0:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 2);
                                playMusicAll(Sound.MUSIC_DISC_STAL);
                                pvpEnabled = true;
                                for (Player player : getPlayers()) {
                                    PotionEffect PotionEffect = new PotionEffect(PotionEffectType.RESISTANCE, 12000, 1, false, false);
                                    player.addPotionEffect(PotionEffect);
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
                } else {
                    cancel();
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("zoomogostart", new AbstractMap.SimpleEntry<>(task, 61));
    }

    public void initiateZoomoIslands(){
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 141;
            @Override
            public void run() {
                if(runningTimers.containsKey("zoomogo")) {
                    if (!pausedTimers.contains("zoomogo")) {
                        timeLeft--;
                        runningTimers.get("zoomogo").setValue(timeLeft);
                        bossBarBgTest();
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
                {-70, 567},
                {-98, 558},
                {-98, 576},
                {-68, 588},
                {-96, 599},
                {-82, 617},
                {-55, 631},
                {-80, 650},
                {-58, 663},
                {-100, 631},
                {-76, 677},
                {-34, 646},
                {-19, 668},
                {-5, 701},
                {-41, 694},
                {-29, 722},
                {-59, 716},
                {-85, 729},
                {-72, 751},
                {-98, 753},
                {-85, 791},
                {-55, 769},
                {-60, 796},
                {-84, 815},
                {-52, 822},
                {-61, 852},
                {-83, 577}

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

//    public int[][] getCDItemBoxes(){
//        return new int[][]{
//                {79, 159, 870},
//                {76, 159, 870},
//                {73, 159, 870},
//                {70, 159, 870},
//                {46, 144, 996},
//                {52, 145, 1009},
//                {82, 141, 1003},
//                {94, 138, 1021},
//                {57, 140, 1034},
//                {82, 141, 1075},
//                {65, 141, 1071},
//                {61, 141, 1071},
//                {38, 141, 1073},
//                {73, 147, 1128},
//                {67, 147, 1128},
//                {61, 147, 1128},
//                {95, 149, 1162},
//                {68, 159, 1152},
//                {30, 149, 1152},
//                {71, 143, 1237},
//                {68, 143, 1237},
//                {65, 143, 1237},
//                {62, 143, 1237}
//        };
//    }

    public void summonIsland(int[] islandCoords){
        int y = 136;
        if(currentRound == 3){
            y = 137;
        }
        Bukkit.getWorld("build").getBlockAt(islandCoords[0], y, islandCoords[1]).setType(Material.REDSTONE_BLOCK);
        Bukkit.getWorld("build").getBlockAt(islandCoords[0], y, islandCoords[1]).setType(Material.STONE);
    }

    public void destroyIsland(int[] islandCoords){
        final String name = ("islandtimer" + runningTimers.size()+1);
        Bukkit.getWorld("build").getBlockAt(islandCoords[0], 135, islandCoords[1]).setType(Material.REDSTONE_BLOCK);
        Bukkit.getWorld("build").getBlockAt(islandCoords[0], 135, islandCoords[1]).setType(Material.STONE);
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 6;
            @Override
            public void run() {
                if(runningTimers.containsKey(name)) {
                    if (!pausedTimers.contains(name)) {
                        runningTimers.get(name).setValue(timeLeft);
                        timeLeft--;
                        if (timeLeft == 0) {
                            Bukkit.getWorld("build").getBlockAt(islandCoords[0], 134, islandCoords[1]).setType(Material.REDSTONE_BLOCK);
                            Bukkit.getWorld("build").getBlockAt(islandCoords[0], 134, islandCoords[1]).setType(Material.STONE);
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
        playerKillCount.clear();
        for(Player player : getPlayers()){
            playerKillCount.put(player.getName(), 0);
        }
    }



    public void startGubGame(){
        fillVotingSpace(5);
        setPreviousPlacements();
        plugin.shopAllowed = false;
        clearInventories();
        resetModeFullPoints();
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 61;
            @Override
            public void run() {
                if(!pausedTimers.contains("gubgamestart")) {
                    timeLeft--;
                    runningTimers.get("gubgamestart").setValue(timeLeft);
                    bossBarBgTest();
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
                            addToGameOrder(currentMode);
                            for(Player p : Bukkit.getOnlinePlayers()){
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "usc setScoreboard " + p.getName() + " GubGame");
                            }
                            for (Player player : getPlayers()) {
                                PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 90, 1, false, false);
                                player.addPotionEffect(PotionEffect);
                                player.setGameMode(GameMode.ADVENTURE);
                            }
                            break;

                        case 50:
                            playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §r⏳ §eWelcome to §d§lGub Game§e! In this game it's all about kills, every player for themselves. Get 14 kills as quickly as possible before the time runs out.
                                        §8
                                        """);
                            }
                            break;
                        case 30:
                            playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §r⏳ §eEach player starts out with highly powered weapons, but with each kill, you are given harder weapons to get kills with! Until you are left with your bare fists...
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
                            }
                            for(Player p : Bukkit.getOnlinePlayers()){
                                messagePlayer(p, """
                                        §8
                                        §8
                                        §8[§c§l!§8] §7Game Starting in §c§l10 seconds...
                                        §8
                                        """);
                            }
                            break;
                        case 5, 4, 3, 2, 1:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 0.3F*(6-timeLeft));
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.sendTitle("§c§l▶ " + timeLeft + " ◀", "", 0, 20, 20);
                            }
                            break;
                        case 0:
                            playMusicAll(Sound.MUSIC_DISC_11);
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
        playerKillCount.clear();
        for(Player p : getPlayers()) {
            gubGameKills.put(p.getName(), 0);
            playerKillCount.put(p.getName(), 0);
        }
        for(int i = 1; i <= 14; i++){
            gubKitKills.put(i, 0);
        }
    }

    public void startSurvivalGames(){
        fillVotingSpace(6);
        setPreviousPlacements();
        plugin.shopAllowed = false;
        clearInventories();
        if(currentRound == 1){
            resetModeFullPoints();
        }
        currentBorderRadius = 236;
        newBorderRadius = 236;
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 61;
            @Override
            public void run() {
                if(!pausedTimers.contains("survivalgamesstart")) {
                    timeLeft--;
                    runningTimers.get("survivalgamesstart").setValue(timeLeft);
                    bossBarBgTest();
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
                            addToGameOrder(currentMode);
                            try {
                                glowTeams();
                            } catch (ReflectiveOperationException e) {
                                throw new RuntimeException(e);
                            }
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "usc setScoreboard " + p.getName() + " SurvivalGames");
                            }
                            for(Player player : getPlayers()){
                                PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 90, 1, false, false);
                                player.addPotionEffect(PotionEffect);
                                player.setGameMode(GameMode.ADVENTURE);
                            }
                            break;

                        case 50:
                            playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §r⏳ §eWelcome to §a§lSurvival Games§e! Good old original game of last team standing. Stay alive, stay aware, stay together. Survive until the very end with your PVP skills.
                                        §8
                                        """);
                            }
                            break;
                        case 30:
                            playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §r⏳ §eDid you know the border shrinks? That's right! It shrinks, and shrinks, until the very last battle has been won. Chests refill around the map every 3 minutes so keep looting!
                                        §8
                                        """);
                            }
                            break;
                        case 10:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
                            for (Player player : Bukkit.getOnlinePlayers()) {
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
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.sendTitle("§c§l▶ " + timeLeft + " ◀", "", 0, 20, 20);
                            }
                            break;
                        case 0:
                            Bukkit.getWorld("build").getBlockAt(-200, 32, -730).setType(Material.REDSTONE_BLOCK);
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 2);
                            playMusicAll(Sound.MUSIC_DISC_MELLOHI);
                            startGracePeriod();
                            for (Player player : getPlayers()) {
                                healFeedPlayer(player);
                                player.sendTitle("§a§l▶ LOOT! ◀", "", 0, 40, 0);
                            }
                            startTimer(900, "survivalgames");
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
            int timeLeft = 61;
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
                            for (Player player : Bukkit.getOnlinePlayers()) {
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
        playerKillCount.clear();
        for(Player player : getPlayers()){
            playerKillCount.put(player.getName(), 0);
        }
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
        for(Player player : Bukkit.getOnlinePlayers()) {
            messagePlayer(player, " §f-  §e§l   ᴍᴏᴅᴇ ᴛᴇᴀᴍ ʟᴇᴀᴅᴇʀs  §f-");
            int placement = 0;
            for (String key : sortMap(modeTeamPoints).keySet()) {
                placement++;
                messagePlayer(player, String.format("%-15s%15s", placement + ". " + getTeamDisplayName(key), "§e§l\uD83D\uDCB0" + sortMap(modeTeamPoints).get(key) + " §7(\uD83D\uDCB0" + Math.floor(sortMap(modeTeamPoints).get(key) / multiplier)) + " §7§ox" + String.format("%.2f", multiplier) + ")");
            }
            messagePlayer(player, "§f--------------------------");
        }
    }

    public void getPlayerModePoints(){
        int index = 1;
        List<String> players = new ArrayList<>(sortMap(modePoints).keySet());
        List<Integer> points = new ArrayList<>(sortMap(modePoints).values());
        for(Player player : Bukkit.getOnlinePlayers()) {
            messagePlayer(player, " §f-  §e§lᴍᴏᴅᴇ ɪɴᴅɪᴠ ʟᴇᴀᴅᴇʀs  §f-");
            for (int i = 0; i <= 7; i++) {
                if(players.size() >= i+1) {
                    if (players.get(i) != null && points.get(i) != null) {
                        messagePlayer(player, String.format("%-15s%15s", i+1 + ". " + getPlayerDisplayName(players.get(i)), "§e§l\uD83D\uDCB0" + points.get(i)));
                    }
                }
            }
            messagePlayer(player, "§f--------------------------");
        }
        for(Player p : getPlayers()) {
            index = 1;
            for (String player2 : players) {
                if (p.getName().equals(player2)){
                    messagePlayer(p, String.format("%-15s%15s", index + ". " + getPlayerDisplayName(players.get(index-1)), "§e§l\uD83D\uDCB0" + points.get(index-1)));
                    break;
                }
                index++;
            }
            messagePlayer(p, "§f--------------------------");
        }
    }

    public void countVotes(){
        modeVotes.clear();
        int totalvotes = 0;
        for(int i = 187; i <= 230; i++){
            for(int j = 713; j <= 756; j++){
                totalvotes++;
                Material block = Bukkit.getServer().getWorld("build").getBlockAt(i, 139, j).getType();
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
        if(leastVotes){
            Collections.reverse(leaderMode);
            Collections.reverse(leaderModeVotes);
        }
        int totalOutOfTwenty = round((double) leaderModeVotes.getFirst() /totalvotes*20);
        float percentageFirst = ((float) leaderModeVotes.getFirst() /totalvotes)*100;
        playSoundAll(Sound.ENTITY_GENERIC_EXPLODE, 1.5F);
        BukkitTask task = new BukkitRunnable() {
            int percentElapsed = 0;
            @Override
            public void run() {
                percentElapsed++;
                if(percentElapsed <= totalOutOfTwenty || (totalOutOfTwenty < 1 && percentElapsed <= 20)) {
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

        String name = "changevotes";
        BukkitTask task2 = new BukkitRunnable() {
            int timeLeft = 86;
            int indexAdd = 0;
            int indexRemove = 0;
            BlockData data = Bukkit.createBlockData(getKeyFromValue(woolModes, leaderMode.getFirst()));
            @Override
            public void run() {
                if(plugin.runningTimers.containsKey(name)) {
                    if (!plugin.pausedTimers.contains(name)) {
                        timeLeft--;
                        plugin.runningTimers.get(name).setValue(timeLeft);
                        if(timeLeft <= 81){
                            if(indexAdd <= 43){
                                summonVotingPiece(indexAdd, data);
                                indexAdd++;
                            } else {
                                plugin.runningTimers.remove(name);
                                cancel();
                            }
                        }
                        if(indexRemove <= 43){
                            removeVotingPiece(indexRemove);
                            indexRemove++;
                        }

                        if (timeLeft == 0) {
                            plugin.runningTimers.remove(name);
                            cancel();
                        }
                    }
                } else {
                    cancel();
                }
            }

        }.runTaskTimer(plugin, 0L, 2L);

        plugin.runningTimers.put(name, new AbstractMap.SimpleEntry<>(task2, 86));
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

    public <K, V> K getKeyFromValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (Objects.equals(entry.getValue(), value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    double roundToTwoDecimalPlaces(double number) {
        double factor = Math.pow(10, 1);
        return Math.round(number * factor) / factor;
    }

    public int[][] voteSpaceLocations = {
            { 233, 708 },
            { 234, 713 },
            { 235, 718 },
            { 235, 723 },
            { 236, 728 },
            { 236, 733 },
            { 237, 738 }
    };

    public void fillVotingSpace(int gameIndex) {
        World world = Bukkit.getWorld("build");
        for(int x = voteSpaceLocations[gameIndex][0]; x <= voteSpaceLocations[gameIndex][0] + 4; x++) {
            for(int y = 140; y <= 144; y++){
                for(int z = voteSpaceLocations[gameIndex][1]; z <= voteSpaceLocations[gameIndex][1] + 4; z++) {
                    world.getBlockAt(x, y, z).setType(Material.TINTED_GLASS);
                }
            }
        }
    }

    public void changeMultiplier(double multiplierValue){
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 321;
            double t;
            String formatted;
            double eased;
            double progress;
            @Override
            public void run() {
                if(runningTimers.containsKey("multiplierchange")) {
                    if (!pausedTimers.contains("multiplierchange")) {
                        timeLeft--;
                        runningTimers.get("multiplierchange").setValue(timeLeft);
                        if(timeLeft <= 280 && timeLeft > 140){
                            t = (280 - timeLeft) / 140.0;

                            eased = 1 - Math.pow(1 - t, 2);

                            progress = multiplier + eased * (multiplierValue - multiplier);

                            formatted = String.format("%.2f", progress);

                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle("§f§lᴍᴜʟᴛɪᴘʟɪᴇʀ", "§e§l" + formatted + "x", 0, 20, 0);
                            }
                        }
                        switch (timeLeft) {
                            case 320:
                                formatted = String.format("%.2f", multiplier);
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle("§f§lᴍᴜʟᴛɪᴘʟɪᴇʀ", "§e§l" + formatted + "x", 0, 60, 0);
                                }
                                break;
                            case 140:
                                formatted = String.format("%.2f", multiplierValue);
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle("§f§lᴍᴜʟᴛɪᴘʟɪᴇʀ", "§e§l" + formatted + "x", 0, 20, 0);
                                }
                                multiplier = multiplierValue;
                                runningTimers.remove("multiplierchange");
                                cancel();
                                break;
                            default:
                                break;
                        }
                    }
                } else {
                    cancel();
                }
            }

        }.runTaskTimer(this, 0L, 1L);

        runningTimers.put("multiplierchange", new AbstractMap.SimpleEntry<>(task, 320));
    }

    public void startVoting(){
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 106;
            @Override
            public void run() {
                if(runningTimers.containsKey("voting")) {
                    if (!pausedTimers.contains("voting")) {
                        timeLeft--;
                        bossBarBgTest();
                        runningTimers.get("voting").setValue(timeLeft);
                        switch (timeLeft) {
                            case 105:
                                switch(GameOrderConfig.get().getStringList("order").size()){
                                    case 0:
                                        changeMultiplier(1.0);
                                    case 2,4,6:
                                        changeMultiplier(multiplier+0.5);
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case 90:
                                currentMode = "Voting";
                                playSoundAll(Sound.ITEM_GOAT_HORN_SOUND_0, 1);
                                for (int i = 187; i <= 230; i++) {
                                    for (int j = 713; j <= 756; j++) {
                                        Bukkit.getServer().getWorld("build").getBlockAt(i, 139, j).setType(Material.BLACK_WOOL);
                                    }
                                }
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendTitle("§e§lVoting Time!", "", 0, 60, 40);
                                }
                                break;
                            case 87:
                                if (leastVotes) {
                                    playSoundAll(Sound.ITEM_GOAT_HORN_SOUND_0, 1);
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        player.sendTitle("§7Modifier:", "§c§lʟᴇᴀsᴛ ᴠᴏᴛᴇs ᴡɪɴs", 0, 60, 40);
                                        messagePlayer(player, """
                                                §8
                                                §8
                                                §c§lLeast Votes Wins!
                                                §fThe mode with the least amount of votes will be played, strategise to make sure the modes you don't want played aren't played!
                                                §8
                                                """);
                                    }
                                }
                                if (audienceVote) {
                                    playSoundAll(Sound.ITEM_GOAT_HORN_SOUND_0, 1);
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        player.sendTitle("§f§l☁ AUDIENCE VOTE ☁", "§b§lX §f| §a@MCShowdownTeam", 0, 7200, 0);
                                        messagePlayer(player, """
                                                §8
                                                §8
                                                §f§lAudience Votes!
                                                §fHead over to Twitter/X and vote using the poll in our latest post for which mode you want played!
                                                §8
                                                """);
                                    }
                                    timeLeft = 1;
                                }
                                break;
                            case 75:
                                teleportPlayers(TeleportConfig.get().getLocation("players.votearena"), 0);
                                teleportSpectators(TeleportConfig.get().getLocation("spectators.votearena"), 0);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "usc setScoreboard " + player.getName() + " Voting");
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
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
                                if(voteParty) {
                                    summonPowerUp(25);
                                    summonPowerUp(25);
                                    summonPowerUp(25);
                                    summonPowerUp(25);
                                }
                                summonPowerUp(25);
                                break;
                            case 50:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
                                if(voteParty) {
                                    summonPowerUp(20);
                                    summonPowerUp(20);
                                    summonPowerUp(20);
                                    summonPowerUp(20);
                                }
                                summonPowerUp(20);
                                break;
                            case 40:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
                                if(voteParty) {
                                    summonPowerUp(10);
                                    summonPowerUp(10);
                                    summonPowerUp(10);
                                    summonPowerUp(10);
                                }
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
                                for (Entity entity : Bukkit.getWorld("build").getEntities()) {
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
                                leastVotes = false;
                                audienceVote = false;
                                currentMode = "Lobby";
                                runningTimers.remove("voting");
                                bossBarBgTest();
                                cancel();
                                break;
                            default:
                                break;
                        }
                    }
                } else {
                    cancel();
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("voting", new AbstractMap.SimpleEntry<>(task, 91));
    }

    public void startWishBook(){
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 21;
            @Override
            public void run() {
                if(runningTimers.containsKey("wishes")) {
                    if (!pausedTimers.contains("wishes")) {
                        timeLeft--;
                        bossBarBgTest();
                        runningTimers.get("wishes").setValue(timeLeft);
                        switch (timeLeft) {
                            case 20:
                                ItemStack book = new ItemStack(Material.WRITABLE_BOOK, 1);
                                playSoundAll(Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.getInventory().addItem(book);
                                    player.sendTitle("§a§lʙᴏᴏᴋ ᴏғ ᴡɪsʜᴇs", "", 0, 60, 40);
                                }
                                break;
                            case 15:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 2);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §e§lThe book of wishes has been granted!
                                            §fSpread some festive cheer by writing a message in the book!
                                            §8
                                            """);
                                }
                                break;
                            case 10:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 2);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §e§lAll festive messages will be recorded!
                                            §fThese messages will be used to spread cheer to the community!
                                            §8
                                            """);
                                }
                                break;
                            case 5:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 2);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §e§lOnce you're done sign the book and it will be sent to Aldo!
                                            §fYou can write your message to a specific person or just the community! All messages will be made public so keep it family friendly!
                                            §8
                                            """);
                                }
                                break;
                            case 0:
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §e§lHappy Holidays!
                                            §fThank you all for the charity support!
                                            §8
                                            """);
                                }
                                currentMode = "Lobby";
                                runningTimers.remove("wishes");
                                bossBarBgTest();
                                cancel();
                                break;
                            default:
                                break;
                        }
                    }
                } else {
                    cancel();
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("wishes", new AbstractMap.SimpleEntry<>(task, 31));
    }


    public void startPresentHunt(int round){

        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 136;
            int index = (4 * round) - 4;
            @Override
            public void run() {
                if(runningTimers.containsKey("presenthunt")) {
                    if (!pausedTimers.contains("presenthunt")) {
                        timeLeft--;
                        bossBarBgTest();
                        runningTimers.get("presenthunt").setValue(timeLeft);
                        switch (timeLeft) {
                            case 135:
                                currentMode = "Presents";
                                playSoundAll(Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendTitle("§f§lᴘʀᴇsᴇɴᴛ §e§lʜᴜɴᴛ", "", 0, 60, 40);
                                }
                                break;
                            case 132:
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    if(round == 1) {
                                        player.playSound(player.getLocation(), phStartSound[0], SoundCategory.VOICE, 1F, 1F);
                                        messagePlayer(player, """
                                                §8
                                                §8
                                                §e§lHo Ho Ho!
                                                §fI have a special task for you all!
                                                §8
                                                """);
                                    } else if (round == 2){
                                        player.playSound(player.getLocation(), phRoundStartSound[1], SoundCategory.VOICE, 1F, 1F);
                                        messagePlayer(player, """
                                                §8
                                                §8
                                                §e§lHo Ho Ho!
                                                §fI seem to have misplaced the presents you found me!
                                                §8
                                                """);
                                    } else {
                                        player.playSound(player.getLocation(), phRoundStartSound[0], SoundCategory.VOICE, 1F, 1F);
                                        messagePlayer(player, """
                                                §8
                                                §8
                                                §e§lI'm getting to old for this!
                                                §fWill you lend me a hand?
                                                §8
                                                """);
                                    }
                                }
                                break;
                            case 120:
                                if(round == 1) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        player.playSound(player.getLocation(), phStartSound[1], SoundCategory.VOICE, 1F, 1F);
                                        messagePlayer(player, """
                                                §8
                                                §8
                                                §e§lI seem to have lost some of my gifts!
                                                §fWill you help me find them?
                                                §8
                                                """);
                                    }
                                } else {
                                    timeLeft = 106;
                                }
                                break;
                            case 108:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 1F);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendTitle("§c§l3", "", 0, 60, 40);
                                }
                                break;
                            case 107:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 1.2F);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendTitle("§c§l2", "", 0, 60, 40);
                                }
                                break;
                            case 106:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 1.4F);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendTitle("§c§l1", "", 0, 60, 40);
                                }
                                break;
                            case 105:
                                BlockDisplay tempBD;

                                List<BlockDisplay> allPresents = new ArrayList<>();
                                Random rand = new Random();

                                allPresents.addAll(lobbyPresents.keySet());

                                for(int i = 1; i <= PresentsConfig.get().getConfigurationSection("presents").getKeys(false).size(); i++){
                                    Location loc = PresentsConfig.get().getLocation("presents.loc" + i).clone();
                                    loc.setY(loc.getY()+0.5);
                                    tempBD = Bukkit.getWorld("build").spawn(loc, BlockDisplay.class);

                                    tempBD.setBlock(Material.ENDER_CHEST.createBlockData());
                                    Vector3f translation = new Vector3f(-0.5F, -0.5F, -0.5F);
                                    Quaternionf leftRotation = new Quaternionf();
                                    Quaternionf rightRotation = new Quaternionf();
                                    Vector3f scaleVector = new Vector3f(1F, 1F, 1F);

                                    Transformation transformation = new Transformation(translation, leftRotation, scaleVector, rightRotation);
                                    tempBD.setTransformation(transformation);
                                    lobbyPresents.put(tempBD, false);
                                }
                                allPresents.addAll(lobbyPresents.keySet());
                                int randNum;
                                for(int i = 0; i < 60; i++){
                                    randNum = rand.nextInt(allPresents.size());
                                    allPresents.get(randNum).remove();
                                    allPresents.remove(randNum);
                                }

                                playSoundAll(Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 1F);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendTitle("§a§lGO!", "", 0, 60, 40);
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §e§l§oGO GO GO!.
                                            §fPresents will be counted in 90 seconds!
                                            §8
                                            """);
                                }
                                break;
                            case 90:
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.playSound(player.getLocation(), phDialogue[index], SoundCategory.VOICE, 1F, 1F);
                                }
                                break;
                            case 75:
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.playSound(player.getLocation(), phDialogue[1+index], SoundCategory.VOICE, 1F, 1F);
                                }
                                break;
                            case 60:
                                playSoundAll(Sound.ITEM_GOAT_HORN_SOUND_1, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §a§l§oGlow Enabled!.
                                            §fPresents are now glowing, just in the nick of time!
                                            §8
                                            """);
                                }
                                for(BlockDisplay bd : lobbyPresents.keySet()){
                                    if(lobbyPresents.containsKey(bd)) {
                                        if (lobbyPresents.get(bd)) {
                                            for (Player player : Bukkit.getOnlinePlayers()) {
                                                bd.setGlowing(true);
                                                try {
                                                    glowingEntities.setGlowing(bd, player);
                                                } catch (ReflectiveOperationException e) {
                                                    throw new RuntimeException(e);
                                                }
                                            }
                                        }
                                    }
                                }
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.playSound(player.getLocation(), phDialogue[2+index], SoundCategory.VOICE, 1F, 1F);
                                }
                                break;
                            case 45:
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.playSound(player.getLocation(), phDialogue[3+index], SoundCategory.VOICE, 1F, 1F);
                                }
                                break;
                            case 30:
                                if(round == 3){
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        player.playSound(player.getLocation(), phDialogue[4+index], SoundCategory.VOICE, 1F, 1F);
                                    }
                                }
                                break;
                            case 15:
                                for(BlockDisplay present : lobbyPresents.keySet()){
                                    present.remove();
                                }
                                playSoundAll(Sound.BLOCK_FIRE_EXTINGUISH, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §c§lPresent Hunt Over.
                                            §fWell Done!
                                            §8
                                            """);
                                }
                                break;
                            case 10:
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    if(round == 1) {
                                        player.playSound(player.getLocation(), phRoundEnd[0], SoundCategory.VOICE, 1F, 1F);
                                        messagePlayer(player, """
                                                §8
                                                §8
                                                §e§lHo Ho Ho, thank you!
                                                §fI hope those points help you in your competition!
                                                §8
                                                """);
                                    } else if (round == 2){
                                        player.playSound(player.getLocation(), phRoundEnd[1], SoundCategory.VOICE, 1F, 1F);
                                        messagePlayer(player, """
                                                §8
                                                §8
                                                §e§lFantastic! All is well..
                                                §fI'll be sure to grant all your wishes!
                                                §8
                                                """);
                                    } else {
                                        player.playSound(player.getLocation(), phRoundEnd[2], SoundCategory.VOICE, 1F, 1F);
                                        messagePlayer(player, """
                                                §8
                                                §8
                                                §e§lMarvelous! I'll.. take the coal out of your stocking..
                                                §fHo Ho Ho! Not sure how it got there.. Ho Ho Ho!
                                                §8
                                                """);
                                    }
                                }
                                break;
                            case 0:
                                currentMode = "Lobby";
                                runningTimers.remove("presenthunt");
                                bossBarBgTest();
                                cancel();
                                break;
                            default:
                                break;
                        }
                    }
                } else {
                    cancel();
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("presenthunt", new AbstractMap.SimpleEntry<>(task, 91));
    }

    public void summonPowerUp(int time){
        Random random = new Random();
        int x = 187+random.nextInt(43);
        int z = 713+random.nextInt(43);
        ItemStack tntItemStack = new ItemStack(Material.TNT);
        Location coords = new Location(Bukkit.getWorld("build"), x,139,z);
        Item powerUp = Bukkit.getWorld("build").dropItemNaturally(coords, tntItemStack);
        powerUp.setGlowing(true);
        coords.setY(142);
        for(Player player : Bukkit.getOnlinePlayers()) {
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

                            Bukkit.getWorld("build").spawnParticle(Particle.DUST, coords, 40, 0.0, 1, 0.0, 1, dustOptions, false);
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
        Location coords = new Location(Bukkit.getWorld("build"), 208, 149, 734);
        chickenBall = (LivingEntity) Bukkit.getWorld("build").spawnEntity(coords, EntityType.CHICKEN);
        messageConsole("Chicken spawned hopefully.");

        for(Player player : Bukkit.getOnlinePlayers()){
            glowingEntities.setGlowing(chickenBall, player, ChatColor.WHITE);
        }

        for(Player player : Bukkit.getOnlinePlayers()) {
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

                                Bukkit.getWorld("build").spawnParticle(Particle.DUST, chickenBall.getLocation(), 300, 1.5, 0.0, 1.5, 1, dustOptions, false);
                                playSoundAll(Sound.ENTITY_CHICKEN_DEATH, 1);
                                chickenBall.remove();
                                cancel();
                                for (Player player : Bukkit.getOnlinePlayers()) {
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
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.stopAllSounds();
        }
        BukkitTask task = new BukkitRunnable() {
            int bridgeCourseId = 0;
            int timeLeft = 71;
            @Override
            public void run() {
                if(!pausedTimers.contains("backtolobby")) {
                    timeLeft--;
                    runningTimers.get("backtolobby").setValue(timeLeft);
                    switch (timeLeft) {
                        case 70:
                            pvpEnabled = false;
                            doubleJumpEnabled = false;
                            currentBorderRadius = 236;
                            newBorderRadius = 236;
                            killRecord.clear();
                            if(currentMode.equals("Slime Golf")){
                                for(Slime slime : slimeGolfSlime){
                                    slime.remove();
                                }
                            }
                            if(currentMode.equals("Survival Games")){
                                Bukkit.getWorld("build").getBlockAt(-200, 32, -730).setType(Material.AIR);
                                Bukkit.getWorld("build").getBlockAt(-199, 32, -730).setType(Material.REDSTONE_BLOCK);
                            }
                            try {
                                unGlowTeams();
                            } catch (ReflectiveOperationException e) {
                                throw new RuntimeException(e);
                            }
                            for (Player player : getPlayers()) {
                                for (PotionEffect effect : player.getActivePotionEffects()) {
                                    player.removePotionEffect(effect.getType());
                                }
                                bossBars.get(player.getName()).removePlayer(player);
                                bossBars.put(player.getName(), null);
                                player.setGameMode(GameMode.SPECTATOR);
                                Bukkit.getScheduler().runTaskLater(plugin, () -> player.setFlying(true), 1L);
                                healFeedPlayer(player);

                            }
                            if(currentMode.equals("Bridge Builders")){
                                Set<Integer> allCourseIds = new HashSet<>();
                                for (Map<Integer, Integer> courseMap : bridgeCourseTimes.values()) {
                                    allCourseIds.addAll(courseMap.keySet());
                                }

                                for (int courseId : allCourseIds) {
                                    List<Map.Entry<String, Integer>> sortedList = bridgeCourseTimes.entrySet().stream()
                                            .filter(e -> e.getValue().containsKey(courseId))
                                            .map(e -> Map.entry(e.getKey(), e.getValue().get(courseId)))
                                            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()) // Sort highest first
                                            .collect(Collectors.toList());

                                        sortedTimesPerCourse.put(courseId, sortedList);
                                }
                                for(Player player : Bukkit.getOnlinePlayers()){
                                    player.sendTitle("💎", "§7Build times:", 0, 60, 40);
                                }
                            } else {
                                if(((currentMode.equals("Slime Golf") || currentMode.equals("Colour Dash") || currentMode.equals("Zoomo Go")) && currentRound == 1) || ((currentMode.equals("Zoomo Go") || currentMode.equals("Slime Golf")) && currentRound == 2)){
                                    if(currentMode.equals("Zoomo Go")){
                                        for(Player player : Bukkit.getOnlinePlayers()){
                                            player.sendTitle("🦑", "§e★ " + plugin.getTeamDisplayName(winningTeam), 0, 60, 40);
                                        }
                                    } else {
                                        for (Player player : Bukkit.getOnlinePlayers()) {
                                            player.sendTitle("🦑", "", 0, 60, 40);
                                        }
                                    }
                                } else if (currentMode.equals("Survival Games")) {
                                    for(Player player : Bukkit.getOnlinePlayers()){
                                        player.sendTitle("💎", "§e★ " + plugin.getTeamDisplayName(winningTeam), 0, 60, 40);
                                    }
                                } else {
                                    if(currentMode.equals("Zoomo Go")){
                                        for(Player player : Bukkit.getOnlinePlayers()){
                                            player.sendTitle("💎", "§e★ " + plugin.getTeamDisplayName(winningTeam), 0, 60, 40);
                                        }
                                    } else {
                                        for (Player player : Bukkit.getOnlinePlayers()) {
                                            player.sendTitle("💎", "", 0, 60, 40);
                                        }
                                    }
                                }
                                timeLeft = 55;
                            }
                            break;
                        case 66, 64, 62, 60, 58, 56:
                            bridgeCourseId++;
                            bridgeBuildTimeHandling(bridgeCourseId);
                            break;
                        case 54:
                            if(currentMode.equals("Zoomo Go") && currentRound < 3){
                                teleportPlayers(TeleportConfig.get().getLocation("players.zoomointermission"), 0);
                                teleportSpectators(TeleportConfig.get().getLocation("players.zoomointermission"), 0);
                                for(Player p : getPlayers()){
                                    p.setGameMode(GameMode.ADVENTURE);
                                    p.getInventory().clear();
                                }
                            }
                            if(currentMode.equals("Slime Golf") && currentRound == 1){
                                teleportPlayers(TeleportConfig.get().getLocation("players.slimeintermission"), 0);
                                teleportSpectators(TeleportConfig.get().getLocation("players.slimeintermission"), 0);
                                for(Player p : getPlayers()){
                                    p.setGameMode(GameMode.ADVENTURE);
                                    p.getInventory().clear();
                                }
                            }
                            if(currentMode.equals("Slime Golf") && currentRound > 1){
                                teleportPlayers(TeleportConfig.get().getLocation("players.slimeintermission2"), 0);
                                teleportSpectators(TeleportConfig.get().getLocation("players.slimeintermission2"), 0);
                                for(Player p : getPlayers()){
                                    p.setGameMode(GameMode.ADVENTURE);
                                    p.getInventory().clear();
                                }
                            }
                            if(currentMode.equals("Colour Dash") && currentRound < 2){
                                teleportPlayers(TeleportConfig.get().getLocation("players.cdintermission"), 0);
                                teleportSpectators(TeleportConfig.get().getLocation("players.cdintermission"), 0);
                                for(Player p : getPlayers()){
                                    p.setGameMode(GameMode.ADVENTURE);
                                    p.getInventory().clear();
                                }
                            }
                            break;
                        case 50:
                            for(Entity entities : Bukkit.getWorld("build").getEntities()){
                                if(entities instanceof BlockDisplay){
                                    entities.remove();
                                }
                            }
                            winningTeam = "";
                            if(currentMode.equals("Survival Games")){
                                Bukkit.getWorld("build").getBlockAt(-199, 32, -730).setType(Material.AIR);
                            }
                            if(currentMode.equals("Slime Golf")) {
                                slimeGolfTimes();
                            } else {
                                for(boolean truefalse : plugin.teamShown){
                                    if(truefalse){
                                        getPlayerModePoints();
                                        break;
                                    }
                                }
                            }
                            break;
                        case 45:
                            for(boolean truefalse : plugin.teamShown){
                                if(truefalse){
                                    getTeamModePoints();
                                    break;
                                }
                            }
                            break;
                        case 40:
                            if(currentMode.equals("Zoomo Go") && currentRound < 3){
                                currentRound++;
                                startZoomoGo();
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                        §8
                                        §8
                                        §8[§c§l🛫§8] §cStarting Round §c""" + currentRound + """
                                        ..
                                        §8
                                        """);
                                }
                                runningTimers.remove("backtolobby");
                                cancel();
                            } else if(currentMode.equals("Slime Golf") && currentRound < 3){
                                currentRound++;
                                startSlimeGolf();
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                        §8
                                        §8
                                        §8[§c§l🛫§8] §cStarting Round §c""" + currentRound + """
                                        ..
                                        §8
                                        """);
                                }
                                runningTimers.remove("backtolobby");
                                cancel();
                            } else if(currentMode.equals("Colour Dash") && currentRound < 2){
                                currentRound++;
                                startColourDash();
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                        §8
                                        §8
                                        §8[§c§l🛫§8] §cStarting round """ + currentRound + """
                                        ..
                                        §8
                                        """);
                                }
                                World world2 = Bukkit.getWorld("build");
                                for(int i = 0; i <= 1; i++) {
                                    world2.getBlockAt(plugin.cdWallCoords[i][0], plugin.cdWallCoords[i][1] - 1, plugin.cdWallCoords[i][2]).setType(Material.REDSTONE_BLOCK);
                                    world2.getBlockAt(plugin.cdWallCoords[i][0], plugin.cdWallCoords[i][1] - 1, plugin.cdWallCoords[i][2]).setType(Material.DIRT);
                                }
                                for(int i = 2; i <= 4; i++) {
                                    world2.getBlockAt(plugin.cdWallCoords[i][0], plugin.cdWallCoords[i][1], plugin.cdWallCoords[i][2]).setType(Material.REDSTONE_BLOCK);
                                    world2.getBlockAt(plugin.cdWallCoords[i][0], plugin.cdWallCoords[i][1], plugin.cdWallCoords[i][2]).setType(Material.DIRT);
                                }
                                world2.getBlockAt(78, 138, 1235).setType(Material.REDSTONE_BLOCK);
                                world2.getBlockAt(78, 138, 1235).setType(Material.AIR);
                                runningTimers.remove("backtolobby");
                                cancel();
                            } else {
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §8[§c§l🛫§8] §cTeleporting back to the lobby..
                                            §8
                                            """);
                                }
                                teleportPlayers(TeleportConfig.get().getLocation("players.lobby"), 5);
                                teleportSpectators(TeleportConfig.get().getLocation("spectators.lobby"), 5);
                                plugin.shopAllowed = true;
                                setCurrentPlacements();
                                List<String> modeLeaderName = new ArrayList<>(plugin.sortMap(plugin.modeFullPoints).keySet());
                                for(Player player : getPlayers()){
                                    if(PlayerInfoConfig.get().getConfigurationSection("players").getKeys(false).contains(player.getName())){
                                        int placement = Integer.parseInt(PlaceholderAPI.setPlaceholders(player, "%mce24_eventplacement%"));
                                        if(placement < PlayerInfoConfig.get().getInt("players." + player.getName() + ".highestplacement")){
                                            PlayerInfoConfig.get().set("players." + player.getName() + ".highestplacement", placement);
                                            PlayerInfoConfig.save();
                                        }
                                        for(int i = 0; i < modeLeaderName.size(); i++) {
                                            if(modeLeaderName.get(i).equalsIgnoreCase(player.getName())){
                                                if(i < PlayerInfoConfig.get().getInt("players." + player.getName() + ".bestgameplacement")){
                                                    PlayerInfoConfig.get().set("players." + player.getName() + ".bestgameplacement", i + 1);
                                                    PlayerInfoConfig.get().set("players." + player.getName() + ".bestgame", currentMode);
                                                    PlayerInfoConfig.save();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        case 35:
                            currentMode = "Lobby";
                            for (Player player : getPlayers()) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "usc setScoreboard " + player.getName() + " Lobby");
                                player.setGameMode(GameMode.ADVENTURE);
                                player.getInventory().clear();
                            }
                            break;
                        case 34:
                            for (Player player : getPlayers()) {
                                player.setAllowFlight(false);
                            }
                            runAldo();
                            deadPlayers.clear();
                            runningTimers.remove("backtolobby");
                            startLobbyInterval(61);
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

    public String[] aldoLines1 = {
            "Wow first place that's incredible!",
            "Second place?? So close that's amazing!",
            "Now that was a performance, good job.",
            "Fourth! Are you kidding me?",
            "Top 5 that game, you are up there!",
            "I'd say that was smashing!!",
            "7th? That's my favourite number!",
            "8th! 8th! 8th!!! Wooo!!",
            "And number 9 goes to... You!!",
            "Just the top 10, that warrants a high five!",
            "Okay, now we're talking, that was great!",
            "So proud of you! What a performance.",
            "I just know you feel great about that. I do!",
            "Lucky number 14! Spread the luck!",
            "Round of applause to you.",
            "You cannot make that up, well done!",
            "You're soaring! Just like me!!",
            "18th! Nice work out there.",
            "We love to see it, good effort.",
            "Can't stress enough, you did well!",
            "I wonder if you can beat that! Nice!",
            "Oooo that was a close one!",
            "Wow, that game was intense.",
            "That sent shivers down my wings.",
            "I was cheering you on the whole way.",
            "I kept a close eye on you, nice one!",
            "You really tried, and that's amazing!",
            "Vibes are high, I believe in you.",
            "29th place! A good effort!",
            "You'll get em' next time!",
            "Hey, stop going easy on everyone champ!",
            "You're first! From the back. And that's okay!!"
    };

    public String[] aldoLines2 = {
            "I can't wait to see what you do next.",
            "We love the good vibes, keep it up!",
            "I believe in you!",
            "I'm so excited for what you do next.",
            "You're looking very stylish yknow.",
            "Sir Craftalot has nothing on you trust me.",
            "I've got a good feeling about this.",
            "I could never be as good as you!",
            "No matter where you place, I will be supportive of you!",
            "I wonder what that Chazzagram guy thinks..",
            "I'm grabbing my popcorn for this.",
            "Work as a team, I'm rooting for you.",
            "You're number 1 in my eyes!",
            "Lyla and Philip are proud of you too.",
            "Can we follow this with an even better performance? I think so!",
            "I'm gonna go grab some snacks from Craftalot castle. Brb!",
            "This calls for a trampoline session I think!",
            "They call me Aldo.",
            "My favourite game has to be Bridge Builders.",
            "I hope my game explanation helped!",
            "I'll be watching you even more now.",
            "Don't let the other teams bring your awesomeness down.",
            "This is so cool!",
            "I'm just buzzing with excitement!!",
            "This has been so fun.",
            "Do you know where Edguard went? He owes me money.",
            "Skill, charm, vibes, you've got it all!",
            "Do you want to share a choccy bar?",
            "Can you tell me what an inventory jam is?"
    };


    public void runAldo(){
        BukkitTask task = new BukkitRunnable() {
            Location aldoSpawn = new Location(Bukkit.getWorld("build"), 180, 146, 696);
            LivingEntity aldo = (LivingEntity) Bukkit.getWorld("build").spawnEntity(aldoSpawn, EntityType.ALLAY);
            Vector velocity = new Vector(0, 0, 0.3);


            List<String> modeplayertop = new ArrayList<>(plugin.sortMap(plugin.modePoints).keySet());
            int timeLeft = 15;
            Random r = new Random();
            int index = 0;
            @Override
            public void run() {
                if(!pausedTimers.contains("aldo")) {
                    timeLeft--;
                    runningTimers.get("aldo").setValue(timeLeft);
                    switch (timeLeft) {
                        case 14:
                            Bukkit.getWorld("build").spawnParticle(Particle.CLOUD, aldo.getLocation(), 3, 0.0, 0.0, 0.0, 0);
                            aldo.setVelocity(velocity);
                            PotionEffect glowing = new PotionEffect(PotionEffectType.GLOWING, 280, 0, false, false);
                            aldo.addPotionEffect(glowing);
                            break;
                        case 13:
                            for(String player2 : modeplayertop){
                                if(Bukkit.getPlayer(player2) != null){
                                    Player player = Bukkit.getPlayer(player2);
                                    float random = 1F + r.nextFloat() * 0.3F;
                                    if(player.getName().equals("Pers0nified")){
                                        player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_7, 1F, random);
                                    } else {
                                        player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_6, 1F, random);
                                    }
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("⏳ §f§l" + aldoLines1[index]));
                                }
                                index++;
                            }

                            break;
                        case 7:
                            for(String player2 : modeplayertop){
                                if(Bukkit.getPlayer(player2) != null){
                                    float random = 1F + r.nextFloat() * 0.3F;
                                    Player player = Bukkit.getPlayer(player2);
                                    if(player.getName().equals("Pers0nified")){
                                        player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_7, 1F, random);
                                    } else {
                                        player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_6, 1F, random);
                                    }
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("⏳ §f§l" + aldoLines2[r.nextInt(aldoLines2.length)]));
                                }
                            }
                            break;
                        case 0:
                            Bukkit.getWorld("build").spawnParticle(Particle.CLOUD, aldo.getLocation(), 3, 0.0, 0.0, 0.0, 0);
                            aldo.remove();
                            runningTimers.remove("aldo");
                            cancel();
                            break;
                        default:
                            break;
                    }
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("aldo", new AbstractMap.SimpleEntry<>(task, 15));
    }



    public void bridgeBuildTimeHandling(int courseId) {
        int position = 1;
        playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 0.3F * courseId);

        List<Map.Entry<String, Integer>> sortedCourseList = sortedTimesPerCourse.get(courseId);

        ConfigurationSection teamsSection = TeamsConfig.get().getConfigurationSection("teams");
        if (teamsSection == null) return; // No teams defined

        if (sortedCourseList != null) {
            for (Map.Entry<String, Integer> entry : sortedCourseList) {
                for (String player : TeamsConfig.get().getStringList("teams." + entry.getKey() + ".players")) {
                    Player p = Bukkit.getPlayer(player);
                    if (p != null) {
                        p.sendTitle(
                                positionsFormatted[position - 1],
                                "§a[\uD83D\uDDFB-" + courseId + "] §e§l💰" + ((106 - ((position - 1) * 6)) / 4),
                                0, 60, 20
                        );
                        earnPoints(player, (106 - ((position - 1) * 6)) / 4, true);
                    }
                }
                position++;
            }

            for (String team : teamsSection.getKeys(false)) {
                boolean isRanked = sortedCourseList.stream()
                        .anyMatch(entry -> entry.getKey().equals(team));
                if (!isRanked) {
                    for (String player : TeamsConfig.get().getStringList("teams." + team + ".players")) {
                        Player p = Bukkit.getPlayer(player);
                        if (p != null) {
                            p.sendTitle("§7§lᴅɴғ", "§a[\uD83D\uDDFB-" + courseId + "]", 0, 60, 20);
                        }
                    }
                }
            }
        } else {
            for (String team : teamsSection.getKeys(false)) {
                for (String player : TeamsConfig.get().getStringList("teams." + team + ".players")) {
                    Player p = Bukkit.getPlayer(player);
                    if (p != null) {
                        p.sendTitle("§7§lᴅɴғ", "§a[\uD83D\uDDFB-" + courseId + "]", 0, 60, 20);
                    }
                }
            }
        }
    }

    public String[] positionsFormatted = { "§e§l1sᴛ", "§7§l2ɴᴅ", "§6§l3ʀᴅ", "§l4ᴛʜ", "§l5ᴛʜ", "§l6ᴛʜ", "§l7ᴛʜ", "§l8ᴛʜ" };


    public void slimeGolfTimes(){
        for(Player p : Bukkit.getOnlinePlayers()) {
            messagePlayer(p, " §f-  §e§lʜᴏʟᴇ ᴛɪᴍᴇs  §f-");
            for (String team : slimeFinishers.keySet()) {
                messagePlayer(p, "§e§l⏱§e" + getTimerValue(slimeFinishers.get(team)) + " §f- " + getTeamDisplayName(team));
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
        ItemMeta swordMeta = sword.getItemMeta();
        swordMeta.addEnchant(Enchantment.LOOTING, 3, true);
        sword.setItemMeta(swordMeta);

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

    public void playMusicAll(Sound sound) {
        for(Player player : Bukkit.getOnlinePlayers()){
            player.playSound(player.getLocation(),sound, SoundCategory.VOICE, 1, 1);
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
                            String readyTypeString = "";
                            switch(readyType){
                                case "jump":
                                    readyTypeString = "Spam Jump!";
                                    break;
                                case "sneak":
                                    readyTypeString = "Spam Crouch!";
                                    break;
                                case "punch":
                                    readyTypeString = "Spam Punch!";
                                    break;
                                case "sneakbomb":
                                    readyTypeString = "Hold Crouch!";
                                    break;
                            }
                            for (Player player : getPlayers()) {
                                player.sendTitle("§b§lReady to play?", readyTypeString, 0, 560, 40);
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
        readyPlayerCount = 0;
        readyPlayers.clear();
        for(Player player : getPlayers()){
            readyPlayers.put(player.getName(), 1);
        }
    }

    public static ItemDisplay[] findItemDisplay() {
        int index = 0;
        ItemDisplay[] items = new ItemDisplay[2];
        World buildWorld = Bukkit.getWorld("build");
        for (Entity entity : buildWorld.getEntities()) {
            if (entity instanceof ItemDisplay itemDisplay) {
                items[index] = itemDisplay;
                index++;
            }
        }
        return items;
    }

    public void endEvent(){
        int z = 828;
        Bukkit.getWorld("build").getBlockAt(52, 189, z).setType(Material.REDSTONE_BLOCK);
        Bukkit.getWorld("build").getBlockAt(52, 189, z).setType(Material.AIR);
        int[][] lakeFireworks = {
                { 133, 143, 678 },
                { 110, 143, 671 },
                { 90, 143, 689 },
                { 96, 143, 705 },
                { 130, 143, 708 }
        };
        World world = Bukkit.getWorld("build");

        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 3600;
            int z2 = 828;
            double pitch = 1.0;
            LivingEntity aldo;
            Random r = new Random();
            List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());
            List<Integer> leaderteampoints = new ArrayList<>(plugin.sortByValue().values());
            @Override
            public void run() {
                if(!pausedTimers.contains("endevent")) {
                    timeLeft--;
                    runningTimers.get("endevent").setValue(timeLeft);
                    switch (timeLeft) {
                        case 3599:
                            playMusicAll(Sound.MUSIC_DISC_STRAD);
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle("§a§lʀᴇsᴜʟᴛs ᴛɪᴍᴇ", "", 0, 60, 20);
                            }
                            break;
                        case 3499, 3399, 3299, 3199:
                            for(int[] firework : lakeFireworks) {
                                world.getBlockAt(firework[0], firework[1]-6, firework[2]).setType(Material.REDSTONE_BLOCK);
                                world.getBlockAt(firework[0], firework[1]-6, firework[2]).setType(Material.AIR);
                            }
                            world.getBlockAt(172, 138, 747).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(172, 138, 747).setType(Material.AIR);
                            world.getBlockAt(151, 138, 753).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(151, 138, 753).setType(Material.AIR);
                            world.getBlockAt(173, 138, 773).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(173, 138, 773).setType(Material.AIR);
                            world.getBlockAt(156, 138, 773).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(156, 138, 773).setType(Material.AIR);
                            world.getBlockAt(154, 138, 785).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(154, 138, 785).setType(Material.AIR);
                            world.getBlockAt(172, 138, 785).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(172, 138, 785).setType(Material.AIR);
                            world.getBlockAt(187, 138, 783).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(187, 138, 783).setType(Material.AIR);
                            world.getBlockAt(139, 138, 783).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(139, 138, 783).setType(Material.AIR);
                            break;
                        case 3500:
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle(getTeamDisplayName(leaderteams.get(7)), "§f§l8ᴛʜ §7| §e\uD83D\uDCB0"  + leaderteampoints.get(7), 0, 120, 0);
                            }
                            teamShown[7] = true;
                            break;
                        case 3400:
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle(getTeamDisplayName(leaderteams.get(6)), "§f§l7ᴛʜ §7| §e\uD83D\uDCB0"  + leaderteampoints.get(6), 0, 120, 0);
                            }
                            teamShown[6] = true;
                            break;
                        case 3300:
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle(getTeamDisplayName(leaderteams.get(5)), "§f§l6ᴛʜ §7| §e\uD83D\uDCB0"  + leaderteampoints.get(5), 0, 120, 0);
                            }
                            teamShown[5] = true;
                            break;
                        case 3200:
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle(getTeamDisplayName(leaderteams.get(4)), "§f§l5ᴛʜ §7| §e\uD83D\uDCB0"  + leaderteampoints.get(4), 0, 120, 0);
                            }
                            teamShown[4] = true;
                            break;
                        case 3100:
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle(getTeamDisplayName(leaderteams.get(3)), "§f§l4ᴛʜ §7| §e\uD83D\uDCB0"  + leaderteampoints.get(3), 0, 120, 0);
                            }
                            teamShown[3] = true;
                            break;

                        case 3000:
                            PotionEffect PotionEffect = new PotionEffect(PotionEffectType.LEVITATION, 100, 1, false, false);
                            for(Player player : getPlayers()) {
                                player.addPotionEffect(PotionEffect);
                            }
                            teleportPlayers(TeleportConfig.get().getLocation("players.finish"), 5);
                            teleportSpectators(TeleportConfig.get().getLocation("spectators.finish") , 5);
                            break;
                        case 2900:
                            plugin.emotesEnabled = true;
                            for(Player p : getPlayers()){
                                messagePlayer(p, """
                                        §8
                                        §8
                                        §e§l[!] §6Emotes are now enabled. Use §a/emote §6to emote!
                                        §8
                                        §8
                                        """);
                            }
                            PotionEffect PotionEffect2 = new PotionEffect(PotionEffectType.SLOW_FALLING, 100, 1, false, false);
                            for(Player player : getPlayers()) {
                                player.addPotionEffect(PotionEffect2);
                            }
                            Location aldoSpawn = new Location(Bukkit.getWorld("build"), 57.5, 196, 827.5);
                            aldo = (LivingEntity) Bukkit.getWorld("build").spawnEntity(aldoSpawn, EntityType.ALLAY);
                            Vector velocity = new Vector(-0.3, 0, 0);
                            Bukkit.getWorld("build").spawnParticle(Particle.CLOUD, aldo.getLocation(), 3, 0.0, 0.0, 0.0, 0);
                            aldo.setVelocity(velocity);
                            PotionEffect glowing = new PotionEffect(PotionEffectType.GLOWING, 20000, 0, false, false);
                            aldo.addPotionEffect(glowing);
                            float random = 1F + r.nextFloat() * 0.3F;
                            for(Player player : Bukkit.getOnlinePlayers()) {
                                if (player.getName().equals("Pers0nified")) {
                                    player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_7, 1F, random);
                                } else {
                                    player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_6, 1F, random);
                                }
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("⏳ §f§lAre we ready? I'm ready."));
                            }
                            break;
                        case 2801:
                            random = 1F + r.nextFloat() * 0.3F;
                            for(Player player : Bukkit.getOnlinePlayers()) {
                                if (player.getName().equals("Pers0nified")) {
                                    player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_7, 1F, random);
                                } else {
                                    player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_6, 1F, random);
                                }
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("⏳ §f§lThis will prove who is the one and only lad..."));
                            }
                            break;
                        case 2880, 2870, 2860, 2850:
                            z2++;
                            pitch+=0.1;
                            Bukkit.getWorld("build").getBlockAt(52, 189, z2).setType(Material.REDSTONE_BLOCK);
                            Bukkit.getWorld("build").getBlockAt(52, 189, z2).setType(Material.AIR);
                            playSoundAll(Sound.BLOCK_GRASS_BREAK, (float) pitch);
                            break;
                        case 2700:
                            Location thirdLoc = new Location(world, 63.5, 194, 838, 90, 0);
                            for(String player : TeamsConfig.get().getStringList("teams." + leaderteams.get(2) + ".players")){
                                if(Bukkit.getPlayer(player) != null){
                                    Player p = Bukkit.getPlayer(player);
                                    p.teleport(thirdLoc);
                                    thirdLoc.setZ(thirdLoc.getZ()-1);
                                }
                            }
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle(getTeamDisplayName(leaderteams.get(2)), "§f§l3ʀᴅ §7| §e\uD83D\uDCB0"  + leaderteampoints.get(2), 0, 120, 0);
                            }
                            teamShown[2] = true;
                            break;
                        case 2600:
                            Location secondLoc = new Location(world, 63.5, 195, 822, 90, 0);
                            for(String player : TeamsConfig.get().getStringList("teams." + leaderteams.get(1) + ".players")){
                                if(Bukkit.getPlayer(player) != null){
                                    Player p = Bukkit.getPlayer(player);
                                    p.teleport(secondLoc);
                                    secondLoc.setZ(secondLoc.getZ()-1);
                                }
                            }
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle(getTeamDisplayName(leaderteams.get(1)), "§e§l2ɴᴅ §7| §e\uD83D\uDCB0"  + leaderteampoints.get(1), 0, 120, 20);
                            }
                            teamShown[1] = true;
                            break;
                        case 2370:
                            Location firstLoc = new Location(world, 63.5, 196, 830, 90, 0);
                            for(String player : TeamsConfig.get().getStringList("teams." + leaderteams.getFirst() + ".players")){
                                if(Bukkit.getPlayer(player) != null){
                                    Player p = Bukkit.getPlayer(player);
                                    p.teleport(firstLoc);
                                    firstLoc.setZ(firstLoc.getZ()-1);
                                }
                            }
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle(getTeamDisplayName(leaderteams.getFirst()), "§b§l1sᴛ §7| §e\uD83D\uDCB0"  + leaderteampoints.getFirst(), 0, 120, 20);
                            }
                            teamShown[0] = true;
                            break;
                        case 2310:
                            getTeamModePoints();
                            break;
                        case 2360, 2340, 2320, 2300, 2280, 2260, 2240, 2220, 2200:
                            world.getBlockAt(66, 188, 832).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(66, 188, 832).setType(Material.AIR);
                            world.getBlockAt(66, 188, 824).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(66, 188, 824).setType(Material.AIR);
                            break;
                        case 2100:
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle("§e§lᴛʜᴀɴᴋs ғᴏʀ ᴘʟᴀʏɪɴɢ", "", 0, 60, 20);
                            }
                            break;
                        case 2060:
                            random = 1F + r.nextFloat() * 0.3F;
                            for(Player player : Bukkit.getOnlinePlayers()) {
                                if (player.getName().equals("Pers0nified")) {
                                    player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_7, 1F, random);
                                } else {
                                    player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_6, 1F, random);
                                }
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("⏳ §f§lThank you so much for coming! Bye for now!"));
                            }
                            break;
                        case 2000:
                            Bukkit.getWorld("build").spawnParticle(Particle.CLOUD, aldo.getLocation(), 3, 0.0, 0.0, 0.0, 0);
                            aldo.remove();
                            teleportPlayers(TeleportConfig.get().getLocation("players.lobby"), 0);
                            teleportSpectators(TeleportConfig.get().getLocation("spectators.lobby") , 0);
                        case 1140:
                            runningTimers.remove("endevent");
                            cancel();
                            break;
                        default:
                            break;
                    }
                }
            }

        }.runTaskTimer(this, 0L, 1L);

        runningTimers.put("endevent", new AbstractMap.SimpleEntry<>(task, 91));

    }

    public void addToGameOrder(String game){
        List<String> gameOrder = GameOrderConfig.get().getStringList("order");
        if(!gameOrder.contains(game)) {
            gameOrder.add(game);
        }
        GameOrderConfig.get().set("order", gameOrder);
        GameOrderConfig.save();
    }

        public void startTeamsPan(){
        String name = "teamPan";
        World world = Bukkit.getWorld("build");
        Location start = new Location(world, 160.5F, 152, 782.3F, -15.4F, 9.7F);
        ArmorStand camera = (ArmorStand) world.spawnEntity(start, EntityType.ARMOR_STAND);
        camera.setInvisible(true);
        camera.setMarker(true);
        camera.setGravity(false);
        camera.setInvulnerable(true);
        for(Player player : getPlayers()){
            player.setGameMode(GameMode.SPECTATOR);
            player.setSpectatorTarget(camera);
        }
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 1601;
            Location loc;
            @Override
            public void run() {
                if (runningTimers.containsKey(name)) {
                    if (!pausedTimers.contains(name)) {
                        timeLeft--;
                        runningTimers.get(name).setValue(timeLeft);
                        loc = camera.getLocation();
                        loc.add(0.00375, 0, 0);
                        loc.setYaw(loc.getYaw() + 0.01925f);
                        camera.teleport(loc);
                        for (Player player : getPlayers()) {
                            if (player.getGameMode() == GameMode.SPECTATOR && player.getSpectatorTarget() != camera) {
                                player.setSpectatorTarget(camera);
                            }
                        }
                        if (timeLeft == 0) {
                            camera.remove();
                            messageConsole("Pan finished.");
                            runningTimers.remove(name);
                            cancel();
                        }
                    }
                } else {
                    messageConsole("Timer removed by external factor.");
                    cancel();
                }
            }

        }.runTaskTimer(this, 0L, 1L);

        runningTimers.put(name, new AbstractMap.SimpleEntry<>(task, 1601));
    }


    public void startEvent(){

        List<String> gameOrder = new ArrayList<>();

        GameOrderConfig.get().set("order", gameOrder);

        GameOrderConfig.save();

        int[][] lakeFireworks = {
                { 133, 143, 678 },
                { 110, 143, 671 },
                { 90, 143, 689 },
                { 96, 143, 705 },
                { 130, 143, 708 }
        };

//        172 144 733;
//        151 144 740;
//
//        172 144 747;
//        151 144 753;
//
//        173 144 773;
//        156 144 773;
//        154 144 785;
//        172 144 785;
//        187 144 783;
//        139 144 783;
//        133 144 793;
//        193 144 793;
//
//        158 151 786;
//        168 151 786;





        ItemDisplay[] bobAndJim = findItemDisplay();

        StringBuilder teamList = new StringBuilder();

        for(Player p : Bukkit.getOnlinePlayers()){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "usc setScoreboard " + p.getName() + " none");
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "blockclock start lobby");

        World world = Bukkit.getWorld("build");

        world.setTime(6000);

        Location stage1 = new Location(Bukkit.getWorld("build"), 177, 144, 793, 90, 0);
        Location stage2 = new Location(Bukkit.getWorld("build"), 149, 144, 793, -90, 0);
        Location stagemain = new Location(Bukkit.getWorld("build"), 163.5, 149, 778.5, 0, 0);

        BukkitTask task = new BukkitRunnable() {
            Random r = new Random();
            LivingEntity aldo;
            TextDisplay playersDisplay;
            TextDisplay teamNameDisplay;
            float currentXrotation = 0F;
            int direction = 1;
            int timeLeft = 3600;
            @Override
            public void run() {
                if(!pausedTimers.contains("startevent")) {

                    if(currentXrotation >= 20F){
                        direction = 0;
                    }
                    if(currentXrotation <= -20F){
                        direction = 1;
                    }
                    if(direction == 1) currentXrotation += 0.3f;
                    if(direction == 0) currentXrotation -= 0.3f;

                    Transformation current = bobAndJim[0].getTransformation();

                    float radX = (float) Math.toRadians(currentXrotation);
                    float radZ = 0f;
                    float radY = 0f;

                    Quaternionf rotation = new Quaternionf().rotateXYZ(radX, radY, radZ);

                    Transformation newTransformation = new Transformation(
                            current.getTranslation(),
                            rotation,
                            current.getScale(),
                            current.getLeftRotation()
                    );

                    bobAndJim[0].setTransformation(newTransformation);

                    bobAndJim[1].setTransformation(newTransformation);

                    if(timeLeft < 3400 && timeLeft > 3200) {
                        world.setTime(world.getTime() + 60);
                    }
                    if(timeLeft < 550 && timeLeft > 350) {
                        world.setTime(world.getTime() - 60);
                    }
                    timeLeft--;
                    runningTimers.get("startevent").setValue(timeLeft);
                    switch (timeLeft) {
                        case 3599:
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle("§9§lsᴘᴇᴄɪᴀʟ ᴛʜᴀɴᴋs", "ʙᴜɪʟᴅᴇʀ - ᴍᴀʟᴠᴀʀᴇ & ᴅᴇʀᴘᴍᴀsᴋ", 0, 50, 0);
                            }
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 2);
                            break;
                        case 3560:
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle("§9§lsᴘᴇᴄɪᴀʟ ᴛʜᴀɴᴋs", "ʙᴜɪʟᴅᴇʀ - ʀʜᴇɴᴇʏᴇ & ɢᴏᴏsᴇ", 0, 50, 0);
                            }
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 2);
                            break;
                        case 3520:
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle("§d§lsᴘᴇᴄɪᴀʟ ᴛʜᴀɴᴋs", "ᴍᴜsɪᴄ - ᴇᴀɢʟᴇᴀɢʟᴇ & ᴢᴏɪᴅʏ", 0, 50, 20);
                            }
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 2);
                            break;
                        case 3480:
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle("§a§lsᴘᴇᴄɪᴀʟ ᴛʜᴀɴᴋs", "sᴜᴘᴘᴏʀᴛ - ᴡᴇᴄʜᴏᴋᴇᴏɴᴍɪʟᴋ & ʟᴇᴍᴏɴᴘʜʀᴏɢɢɢ", 0, 50, 0);
                            }
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 2);
                            break;
                        case 3440:
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle("§a§lsᴘᴇᴄɪᴀʟ ᴛʜᴀɴᴋs", "sᴜᴘᴘᴏʀᴛ - ᴀᴊx & ᴊᴜʟᴇs & ᴘɪɢɢʟᴇs", 0, 50, 0);
                            }
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 2);
                            break;
                        case 3400:
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle("§b§lsᴘᴇᴄɪᴀʟ ᴛʜᴀɴᴋs", "ᴛʜᴇ ɢʟᴏʀɪᴏᴜs ᴛᴇsᴛᴇʀs", 0, 50, 20);
                            }
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 2);
                            break;
                        case 3360:
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle("§6§lsᴘᴇᴄɪᴀʟ ᴛʜᴀɴᴋs", "ᴛʜᴇ ᴘᴀᴛʀᴇᴏɴs", 0, 50, 20);
                                messagePlayer(p, """
                                                §8
                                                §8
                                                §6§lThe Patreons!
                                                §fProfPie
                                                §fZombreyy
                                                §fStarOun
                                                §fLoveFromNyx
                                                §fMrkvaMan
                                                §fNURSEGUY
                                                §fPepsiTrain26
                                                §8
                                                """);
                            }
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 2);
                            break;
                        case 3320:
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle("§e§lʙʀᴏᴜɢʜᴛ ᴛᴏ ʏᴏᴜ ʙʏ", "ᴄʜᴀᴢᴢᴀɢʀᴀᴍ", 0, 50, 20);
                            }
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 1.6F);
                            break;
                        case 3280:
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle("§e§lʙʀᴏᴜɢʜᴛ ᴛᴏ ʏᴏᴜ ʙʏ", "ᴄʜᴀᴢᴢᴀɢʀᴀᴍ &", 0, 50, 20);
                            }
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 1.8F);
                            break;
                        case 3240:
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle("§e§lʙʀᴏᴜɢʜᴛ ᴛᴏ ʏᴏᴜ ʙʏ", "ᴄʜᴀᴢᴢᴀɢʀᴀᴍ & ᴘɪɢɢʟᴇs", 0, 50, 20);
                            }
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 2.0F);
                            break;
                        case 3208:
                            Location aldoSpawn = new Location(Bukkit.getWorld("build"), 163.5, 143, 783.5);
                            aldo = (LivingEntity) Bukkit.getWorld("build").spawnEntity(aldoSpawn, EntityType.ALLAY);
                            Vector velocity = new Vector(0, 0, -0.3);
                            Bukkit.getWorld("build").spawnParticle(Particle.CLOUD, aldo.getLocation(), 3, 0.0, 0.0, 0.0, 0);
                            aldo.setVelocity(velocity);
                            PotionEffect glowing = new PotionEffect(PotionEffectType.GLOWING, 20000, 0, false, false);
                            aldo.addPotionEffect(glowing);
                            break;
                        case 3108:
                            playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1F);
                            for(Player player : Bukkit.getOnlinePlayers()){
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("⏳ §f§lHello Lads.."));
                            }
                            break;
                        case 3008:
                            float random = 1F + r.nextFloat() * 0.3F;
                            for(Player player : Bukkit.getOnlinePlayers()){
                                if (player.getName().equals("Pers0nified")) {
                                    player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_7, 1F, random);
                                } else {
                                    player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_6, 1F, random);
                                }
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("⏳ §f§lThe years coming to an end, so let's celebrate!"));
                            }
                            break;
                        case 2908:
                            playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1F);
                            for(Player player : Bukkit.getOnlinePlayers()) {
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("⏳ §f§lI hope you're ready to Showdown it!"));
                            }
                            break;
                        case 2808:
                            playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1F);
                            for(Player player : Bukkit.getOnlinePlayers()){
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("⏳ §f§lSo glad to see Pers0nified is the same height as me!"));
                            }
                            break;
                        case 2708:
                            playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1F);
                            for(Player player : Bukkit.getOnlinePlayers()){
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("⏳ §f§lBut hey, let's not waste time.. it's team reveals time!"));
                            }
                            break;
                        case 2608:
                            Bukkit.getWorld("build").spawnParticle(Particle.CLOUD, aldo.getLocation(), 3, 0.0, 0.0, 0.0, 0);
                            aldo.remove();
                            random = 1F + r.nextFloat() * 0.3F;
                            for(Player player : Bukkit.getOnlinePlayers()){
                                if (player.getName().equals("Pers0nified")) {
                                    player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_7, 1F, random);
                                } else {
                                    player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_6, 1F, random);
                                }
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("⏳ §f§lLet's shake things up, make it more cinematic!"));
                            }
                            break;
                        case 3199, 3100, 3000, 2900, 2800, 2700, 2600:
                            for(int[] firework : lakeFireworks) {
                                world.getBlockAt(firework[0], firework[1]-6, firework[2]).setType(Material.REDSTONE_BLOCK);
                                world.getBlockAt(firework[0], firework[1]-6, firework[2]).setType(Material.AIR);
                            }
                            break;


                        case 3190, 3090, 2990, 2890, 2790, 2690, 2590:
                            world.getBlockAt(172, 138, 733).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(172, 138, 733).setType(Material.AIR);
                            world.getBlockAt(151, 138, 740).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(151, 138, 740).setType(Material.AIR);
                            break;

                        case 3180, 3080, 2980, 2880, 2780, 2680, 2580:
                            world.getBlockAt(172, 138, 747).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(172, 138, 747).setType(Material.AIR);
                            world.getBlockAt(151, 138, 753).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(151, 138, 753).setType(Material.AIR);
                            break;

                        case 3170, 3070, 2970, 2870, 2770, 2670, 2570:
                            world.getBlockAt(173, 138, 773).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(173, 138, 773).setType(Material.AIR);
                            world.getBlockAt(156, 138, 773).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(156, 138, 773).setType(Material.AIR);
                            break;

                        case 3160, 3060, 2960, 2860, 2760, 2660, 2560:
                            world.getBlockAt(154, 138, 785).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(154, 138, 785).setType(Material.AIR);
                            world.getBlockAt(172, 138, 785).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(172, 138, 785).setType(Material.AIR);
                            break;

                        case 3150, 3050, 2950, 2850, 2750, 2650, 2550:
                            world.getBlockAt(187, 138, 783).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(187, 138, 783).setType(Material.AIR);
                            world.getBlockAt(139, 138, 783).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(139, 138, 783).setType(Material.AIR);
                            break;

                        case 3140, 3040, 2940, 2840, 2740, 2640, 2540:
                            world.getBlockAt(133, 138, 793).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(133, 138, 793).setType(Material.AIR);
                            world.getBlockAt(193, 138, 793).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(193, 138, 793).setType(Material.AIR);
                            break;

                        case 2460:
                            currentMode = "Start";
                            playMusicAll(Sound.MUSIC_DISC_STRAD);
                            PotionEffect PotionEffect = new PotionEffect(PotionEffectType.LEVITATION, 100, 1, false, false);
                            for(Player player : getPlayers()) {
                                player.addPotionEffect(PotionEffect);
                            }
                            teleportPlayers(TeleportConfig.get().getLocation("players.stage"), 5);
                            teleportSpectators(TeleportConfig.get().getLocation("spectators.stage") , 5);
                            break;
                        case 2360:
                            PotionEffect PotionEffect2 = new PotionEffect(PotionEffectType.SLOW_FALLING, 200, 1, false, false);
                            for(Player player : getPlayers()) {
                                player.addPotionEffect(PotionEffect2);
                                for(Player playerHide : getPlayers()){
                                    if(player != playerHide) {
                                        player.hidePlayer(plugin, playerHide);
                                    }
                                }
                            }
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle("§lɪɴᴛʀᴏᴅᴜᴄɪɴɢ..", "§lᴛʜᴇ ʀᴏsᴛᴇʀ!", 20, 60, 20);
                            }
                            break;
                        case 2220:
                            for(Player p : getPlayers()){
                                p.sendTitle("\uE023", "", 20, 60, 20);
                            }
                            break;
                        case 2160:
                            plugin.emotesEnabled = true;
                            for(Player p : getPlayers()){
                                messagePlayer(p, """
                                        §8
                                        §8
                                        §e§l[!] §6Emotes are now enabled. Use §a/emote §6to emote!
                                        §8
                                        §8
                                        """);
                            }
                            startTeamsPan();
                            teamList.setLength(0);
                            summonTeamFirework(Color.RED);
                            for(String player : TeamsConfig.get().getStringList("teams.RubyRaiders.players")){
                                teamList.append(player).append(" ");
                                if(Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.setGameMode(GameMode.ADVENTURE);
                                    for (Player playerShow : getPlayers()) {
                                        playerShow.showPlayer(plugin, p);
                                    }
                                    p.teleport(stage1);
                                }
                            }
                            teamList.setLength(teamList.length()-1);

                            Location theLocation = new Location(world, 163.5, 154, 788.5);

                            teamNameDisplay = world.spawn(theLocation, TextDisplay.class);
                            teamNameDisplay.setSeeThrough(true);
                            teamNameDisplay.setText(getTeamDisplayName("RubyRaiders"));
                            teamNameDisplay.setRotation(-180, 0);
                            teamNameDisplay.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                            Quaternionf quat = new Quaternionf();
                            Transformation transform = new Transformation(
                                    new Vector3f(0, 0, 0),
                                    quat,
                                    new Vector3f(5.0f, 5.0f, 5.0f),
                                    quat
                            );

                            teamNameDisplay.setTransformation(transform);

                            theLocation.setY(153);

                            playersDisplay = world.spawn(theLocation, TextDisplay.class);
                            playersDisplay.setSeeThrough(true);
                            playersDisplay.setText(teamList.toString());
                            playersDisplay.setRotation(-180, 0);
                            playersDisplay.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                            Quaternionf quat2 = new Quaternionf();
                            Transformation transform2 = new Transformation(
                                    new Vector3f(0, 0, 0),
                                    quat2,
                                    new Vector3f(2.0f, 2.0f, 2.0f),
                                    quat2
                            );

                            playersDisplay.setTransformation(transform2);
                            for(String player : TeamsConfig.get().getStringList("teams.AmberAmbushers.players")){
                                if(Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.sendTitle("", "§eYou're up next!", 0, 200, 0);
                                }
                            }

                            break;
                        case 1960:
                            teamList.setLength(0);
                            summonTeamFirework(Color.ORANGE);
                            for(String player : TeamsConfig.get().getStringList("teams.RubyRaiders.players")){
                                if(Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.setGameMode(GameMode.SPECTATOR);
                                    for (Player playerHide : getPlayers()) {
                                        playerHide.hidePlayer(plugin, p);
                                    }
                                    p.teleport(stagemain);
                                }
                            }
                            for(String player : TeamsConfig.get().getStringList("teams.AmberAmbushers.players")){
                                teamList.append(player).append(" ");
                                if(Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.setGameMode(GameMode.ADVENTURE);
                                    for (Player playerShow : getPlayers()) {
                                        playerShow.showPlayer(plugin, p);
                                    }
                                    p.teleport(stage2);
                                }
                            }
                            teamNameDisplay.setText(getTeamDisplayName("AmberAmbushers"));
                            playersDisplay.setText("");
                            if(!teamList.isEmpty()) {
                                teamList.setLength(teamList.length() - 1);
                                playersDisplay.setText(teamList.toString());
                            }
                            for(String player : TeamsConfig.get().getStringList("teams.TopazTroopers.players")){
                                if(Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.sendTitle("", "§eYou're up next!", 0, 200, 0);
                                }
                            }
                            break;
                        case 1760:
                            teamList.setLength(0);
                            summonTeamFirework(Color.YELLOW);
                            for(String player : TeamsConfig.get().getStringList("teams.AmberAmbushers.players")){
                                if(Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.setGameMode(GameMode.SPECTATOR);
                                    for (Player playerHide : getPlayers()) {
                                        playerHide.hidePlayer(plugin, p);
                                    }
                                    p.teleport(stagemain);
                                }
                            }
                            for(String player : TeamsConfig.get().getStringList("teams.TopazTroopers.players")){
                                teamList.append(player).append(" ");
                                if(Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.setGameMode(GameMode.ADVENTURE);
                                    for (Player playerShow : getPlayers()) {
                                        playerShow.showPlayer(plugin, p);
                                    }
                                    p.teleport(stage1);
                                }
                            }
                            teamNameDisplay.setText(getTeamDisplayName("TopazTroopers"));
                            playersDisplay.setText("");
                            if(!teamList.isEmpty()) {
                                teamList.setLength(teamList.length() - 1);
                                playersDisplay.setText(teamList.toString());
                            }
                            for(String player : TeamsConfig.get().getStringList("teams.KyaniteKillers.players")){
                                if(Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.sendTitle("", "§eYou're up next!", 0, 200, 0);
                                }
                            }
                            break;
                        case 1560:
                            teamList.setLength(0);
                            summonTeamFirework(Color.LIME);
                            for(String player : TeamsConfig.get().getStringList("teams.TopazTroopers.players")){
                                if(Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.setGameMode(GameMode.SPECTATOR);
                                    for (Player playerHide : getPlayers()) {
                                        playerHide.hidePlayer(plugin, p);
                                    }
                                    p.teleport(stagemain);
                                }
                            }
                            for(String player : TeamsConfig.get().getStringList("teams.KyaniteKillers.players")){
                                teamList.append(player).append(" ");
                                if(Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.setGameMode(GameMode.ADVENTURE);
                                    for (Player playerShow : getPlayers()) {
                                        playerShow.showPlayer(plugin, p);
                                    }
                                    p.teleport(stage2);
                                }
                            }
                            teamNameDisplay.setText(getTeamDisplayName("KyaniteKillers"));
                            playersDisplay.setText("");
                            if(!teamList.isEmpty()) {
                                teamList.setLength(teamList.length() - 1);
                                playersDisplay.setText(teamList.toString());
                            }
                            for(String player : TeamsConfig.get().getStringList("teams.DiamondDestroyers.players")){
                                if(Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.sendTitle("", "§eYou're up next!", 0, 200, 0);
                                }
                            }
                            break;
                        case 1360:
                            teamList.setLength(0);
                            summonTeamFirework(Color.AQUA);
                            for(String player : TeamsConfig.get().getStringList("teams.KyaniteKillers.players")){
                                if(Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.setGameMode(GameMode.SPECTATOR);
                                    for (Player playerHide : getPlayers()) {
                                        playerHide.hidePlayer(plugin, p);
                                    }
                                    p.teleport(stagemain);
                                }
                            }
                            for(String player : TeamsConfig.get().getStringList("teams.DiamondDestroyers.players")){
                                teamList.append(player).append(" ");
                                if(Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.setGameMode(GameMode.ADVENTURE);
                                    for (Player playerShow : getPlayers()) {
                                        playerShow.showPlayer(plugin, p);
                                    }
                                    p.teleport(stage1);
                                }
                            }
                            teamNameDisplay.setText(getTeamDisplayName("DiamondDestroyers"));
                            playersDisplay.setText("");
                            if(!teamList.isEmpty()) {
                                teamList.setLength(teamList.length() - 1);
                                playersDisplay.setText(teamList.toString());
                            }
                            for(String player : TeamsConfig.get().getStringList("teams.SapphireSoldiers.players")){
                                if(Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.sendTitle("", "§eYou're up next!", 0, 200, 0);
                                }
                            }
                            break;
                        case 1160:
                            teamList.setLength(0);
                            summonTeamFirework(Color.BLUE);
                            for(String player : TeamsConfig.get().getStringList("teams.DiamondDestroyers.players")){
                                if(Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.setGameMode(GameMode.SPECTATOR);
                                    for (Player playerHide : getPlayers()) {
                                        playerHide.hidePlayer(plugin, p);
                                    }
                                    p.teleport(stagemain);
                                }
                            }
                            for(String player : TeamsConfig.get().getStringList("teams.SapphireSoldiers.players")){
                                teamList.append(player).append(" ");
                                if(Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.setGameMode(GameMode.ADVENTURE);
                                    for (Player playerShow : getPlayers()) {
                                        playerShow.showPlayer(plugin, p);
                                    }
                                    p.teleport(stage2);
                                }
                            }
                            teamNameDisplay.setText(getTeamDisplayName("SapphireSoldiers"));
                            playersDisplay.setText("");
                            if(!teamList.isEmpty()) {
                                teamList.setLength(teamList.length() - 1);
                                playersDisplay.setText(teamList.toString());
                            }
                            for(String player : TeamsConfig.get().getStringList("teams.SmithsoniteSlayers.players")){
                                if(Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.sendTitle("", "§eYou're up next!", 0, 200, 0);
                                }
                            }
                            break;
                        case 960:
                            teamList.setLength(0);
                            summonTeamFirework(Color.FUCHSIA);
                            for(String player : TeamsConfig.get().getStringList("teams.SapphireSoldiers.players")){
                                if(Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.setGameMode(GameMode.SPECTATOR);
                                    for (Player playerHide : getPlayers()) {
                                        playerHide.hidePlayer(plugin, p);
                                    }
                                    p.teleport(stagemain);
                                }
                            }
                            for(String player : TeamsConfig.get().getStringList("teams.SmithsoniteSlayers.players")){
                                teamList.append(player).append(" ");
                                if(Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.setGameMode(GameMode.ADVENTURE);
                                    for (Player playerShow : getPlayers()) {
                                        playerShow.showPlayer(plugin, p);
                                    }
                                    p.teleport(stage1);
                                }
                            }
                            teamNameDisplay.setText(getTeamDisplayName("SmithsoniteSlayers"));
                            playersDisplay.setText("");
                            if(!teamList.isEmpty()) {
                                teamList.setLength(teamList.length() - 1);
                                playersDisplay.setText(teamList.toString());
                            }
                            for(String player : TeamsConfig.get().getStringList("teams.CrystalCrashers.players")){
                                if(Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.sendTitle("", "§eYou're up next!", 0, 200, 0);
                                }
                            }
                            break;
                        case 760:
                            teamList.setLength(0);
                            summonTeamFirework(Color.WHITE);
                            for(String player : TeamsConfig.get().getStringList("teams.SmithsoniteSlayers.players")){
                                if(Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.setGameMode(GameMode.SPECTATOR);
                                    for (Player playerHide : getPlayers()) {
                                        playerHide.hidePlayer(plugin, p);
                                    }
                                    p.teleport(stagemain);
                                }
                            }
                            for(String player : TeamsConfig.get().getStringList("teams.CrystalCrashers.players")){
                                teamList.append(player).append(" ");
                                if(Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.setGameMode(GameMode.ADVENTURE);
                                    for (Player playerShow : getPlayers()) {
                                        playerShow.showPlayer(plugin, p);
                                    }
                                    p.teleport(stage2);
                                }
                            }
                            teamNameDisplay.setText(getTeamDisplayName("CrystalCrashers"));
                            playersDisplay.setText("");
                            if(!teamList.isEmpty()) {
                                teamList.setLength(teamList.length() - 1);
                                playersDisplay.setText(teamList.toString());
                            }
                            break;
                        case 559:
                            playersDisplay.remove();
                            teamNameDisplay.remove();
                            for(Player player : getPlayers()) {
                                player.setGameMode(GameMode.ADVENTURE);
                                for(Player playerShow : getPlayers()){
                                    if(player != playerShow) {
                                        player.showPlayer(plugin, playerShow);
                                    }
                                }
                            }
                            teleportPlayers(TeleportConfig.get().getLocation("players.lobby"), 0);
                            break;

                        case 360:
                            for(int[] firework : lakeFireworks) {
                                world.getBlockAt(firework[0], firework[1]-6, firework[2]).setType(Material.REDSTONE_BLOCK);
                                world.getBlockAt(firework[0], firework[1]-6, firework[2]).setType(Material.AIR);
                            }

                            world.getBlockAt(172, 138, 733).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(172, 138, 733).setType(Material.AIR);
                            world.getBlockAt(151, 138, 740).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(151, 138, 740).setType(Material.AIR);

                            world.getBlockAt(172, 138, 747).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(172, 138, 747).setType(Material.AIR);
                            world.getBlockAt(151, 138, 753).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(151, 138, 753).setType(Material.AIR);

                            world.getBlockAt(173, 138, 773).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(173, 138, 773).setType(Material.AIR);
                            world.getBlockAt(156, 138, 773).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(156, 138, 773).setType(Material.AIR);

                            world.getBlockAt(154, 138, 785).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(154, 138, 785).setType(Material.AIR);
                            world.getBlockAt(172, 138, 785).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(172, 138, 785).setType(Material.AIR);

                            world.getBlockAt(187, 138, 783).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(187, 138, 783).setType(Material.AIR);
                            world.getBlockAt(139, 138, 783).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(139, 138, 783).setType(Material.AIR);

                            world.getBlockAt(133, 138, 793).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(133, 138, 793).setType(Material.AIR);
                            world.getBlockAt(193, 138, 793).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(193, 138, 793).setType(Material.AIR);
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle("\uD83D\uDC9B", "§l  ʟᴀᴅsᴍᴜs", 0, 100, 40);
                            }
                            break;
                        case 320:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 2);
                            for (Player player : getPlayers()) {
                                messagePlayer(player, """
                                            §8
                                            §f❶ ❷ ❸ ❹ ❺ ❻ ❼ ❽
                                            §f§lCAN WE GET SOME HYPE FROM THE TEAMS?
                                            §8
                                            """);
                            }
                            break;
                        case 0:
                            plugin.emotesEnabled = false;
                            runningTimers.remove("startevent");
                            cancel();
                            break;
                        default:
                            break;
                    }
                }
            }

        }.runTaskTimer(this, 0L, 1L);

        runningTimers.put("startevent", new AbstractMap.SimpleEntry<>(task, 91));
    }


    public void summonTeamFirework(Color colour){
        double[][] fireworks = new double[][]{
                {171.5, 150.5, 788.5},
                {169.5, 154.5, 788.5},
                {166.5, 156.5, 788.5},
                {163.5, 157.5, 788.5},
                {160.5, 156.5, 788.5},
                {157.5, 154.5, 788.5},
                {155.5, 150.5, 788.5}
        };

        String name = "teamfirework";

        BukkitTask task2 = new BukkitRunnable() {
            boolean forward = true;
            int timeLeft = 89;
            int index = 0;
            World world = Bukkit.getWorld("build");
            Location location = new Location(world, 171.5, 150.5, 788.5);
            @Override
            public void run() {
                if(plugin.runningTimers.containsKey(name)) {
                    if (!plugin.pausedTimers.contains(name)) {
                        timeLeft--;
                        plugin.runningTimers.get(name).setValue(timeLeft);

                        if(timeLeft % 2 == 0 && timeLeft < 29 && index >= 0) {
                            location.setX(fireworks[index][0]);
                            location.setY(fireworks[index][1]);
                            location.setZ(fireworks[index][2]);

                            Firework firework = world.spawn(location, Firework.class);
                            FireworkMeta meta = firework.getFireworkMeta();

                            FireworkEffect effect = FireworkEffect.builder()
                                    .with(FireworkEffect.Type.BURST)
                                    .withTrail()
                                    .flicker(false)
                                    .withColor(colour)
                                    .withFade(Color.WHITE)
                                    .build();

                            meta.addEffect(effect);
                            meta.setPower(1);
                            firework.setFireworkMeta(meta);

                            if(index == 6){
                                forward = false;
                            }
                            if(forward){
                                index++;
                            } else {
                                index--;
                            }
                            firework.detonate();

                        }
                        if (timeLeft == 0) {
                            plugin.runningTimers.remove(name);
                            cancel();
                        }
                    }
                } else {
                    cancel();
                }
            }

        }.runTaskTimer(plugin, 0L, 2L);

        plugin.runningTimers.put(name, new AbstractMap.SimpleEntry<>(task2, 89));
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
                        glowingEntities.setGlowing(Bukkit.getPlayer(player), Bukkit.getPlayer(player2));
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

    public void bossBarBgTest() {
        StringBuilder output = new StringBuilder();
        int width;
        int blocks;
        for(Player player : Bukkit.getOnlinePlayers()) {
            output.setLength(0);
            if (bossBars.get(player.getName()) == null) {
                BossBar boss = Bukkit.createBossBar("", BarColor.GREEN, BarStyle.SOLID);
                boss.addPlayer(player);
                bossBars.put(player.getName(), boss);
            }
            String text = "";
            switch (currentMode) {
                case "Voting":
                    if(plugin.runningTimers.containsKey("voting")){
                        text = getTimer("voting");
                    } else {
                        text = "00:00";
                    }
                    width = FontUtils.getStringWidth("\uD83D\uDCB0" + PlaceholderAPI.setPlaceholders(player, "%mce24_points%"));
                    blocks = Math.round((float) width / 6);
                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("§e§l\uD83D\uDCB0").append(PlaceholderAPI.setPlaceholders(player, "%mce24_points%"));
                    output.append(" ".repeat(5)).append("§r");

                    width = FontUtils.getStringWidth("ᴛʜᴇ ᴠᴏᴛɪɴɢ ᴘᴀʟᴇᴛᴛᴇ");
                    blocks = Math.round((float) width / 6);
                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("§rᴛʜᴇ ᴠᴏᴛɪɴɢ ᴘᴀʟᴇᴛᴛᴇ");
                    output.append(" ".repeat(6)).append("§r");

                    width = FontUtils.getStringWidth("⏱ " + text);
                    blocks = Math.round((float) width / 6);
                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("§a§l⏱ §a").append(text);
                    break;
                case "Lobby":
                    if(plugin.runningTimers.containsKey("break")){
                        text = getTimer("break");
                    } else {
                        text = "00:00";
                    }
                    width = FontUtils.getStringWidth("\uD83D\uDCB0" + PlaceholderAPI.setPlaceholders(player, "%mce24_points%"));
                    blocks = Math.round((float) width / 6);
                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("§e§l\uD83D\uDCB0").append(PlaceholderAPI.setPlaceholders(player, "%mce24_points%"));
                    output.append(" ".repeat(5)).append("§r");

                    width = FontUtils.getStringWidth("sʜᴏᴡᴅᴏᴡɴ ᴘᴀʀᴋ");
                    blocks = Math.round((float) width / 6);
                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("§rsʜᴏᴡᴅᴏᴡɴ ᴘᴀʀᴋ");
                    output.append(" ".repeat(6)).append("§r");

                    width = FontUtils.getStringWidth("⏱ Interval: " + text);
                    blocks = Math.round((float) width / 6);
                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("§a§l⏱ §aInterval: ").append(text);
                    break;
                case "Slime Golf":
                    width = FontUtils.getStringWidth("\uD83D\uDCB0" + PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"));
                    blocks = Math.round((float) width / 6);
                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("§e§l\uD83D\uDCB0").append(PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"));
                    output.append(" ".repeat(5)).append("§r");

                    width = FontUtils.getStringWidth("sʟɪᴍᴇ ɢᴏʟғ Slimey Slipway");
                    blocks = Math.round((float) width / 6);
                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("§rsʟɪᴍᴇ ɢᴏʟғ §7§oSlimey Slipway");
                    output.append(" ".repeat(6)).append("§r");

                    width = FontUtils.getStringWidth("⏱ " + PlaceholderAPI.setPlaceholders(player, "%mce24_timer_slimegolf%"));
                    blocks = Math.round((float) width / 6);
                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("§a§l⏱ §a").append(PlaceholderAPI.setPlaceholders(player, "%mce24_timer_slimegolf%"));
                    break;
                case "Bridge Builders":
                    width = FontUtils.getStringWidth("\uD83D\uDCB0" + PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"));
                    blocks = Math.round((float) width / 6);
                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("§e§l\uD83D\uDCB0").append(PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"));
                    output.append(" ".repeat(5)).append("§r");

                    width = FontUtils.getStringWidth("ʙʀɪᴅɢᴇ ʙᴜɪʟᴅᴇʀs Abandoned Mineshaft");
                    blocks = Math.round((float) width / 6);
                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("§rʙʀɪᴅɢᴇ ʙᴜɪʟᴅᴇʀs §7§oAbandoned Mineshaft");
                    output.append(" ".repeat(6)).append("§r");

                    width = FontUtils.getStringWidth("⏱ " + PlaceholderAPI.setPlaceholders(player, "%mce24_timer_bridgebuilders%"));
                    blocks = Math.round((float) width / 6);
                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("§a§l⏱ §a").append(PlaceholderAPI.setPlaceholders(player, "%mce24_timer_bridgebuilders%"));
                    break;
                case "Gub Game":
                    width = FontUtils.getStringWidth("\uD83D\uDCB0" + PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"));
                    blocks = Math.round((float) width / 6);
                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("§e§l\uD83D\uDCB0").append(PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"));
                    output.append(" ".repeat(5)).append("§r");

                    width = FontUtils.getStringWidth("ɢᴜʙ ɢᴀᴍᴇ The Courtyard ");
                    blocks = Math.round((float) width / 6);
                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("§rɢᴜʙ ɢᴀᴍᴇ §7§oThe Courtyard");
                    output.append(" ".repeat(6)).append("§r");

                    width = FontUtils.getStringWidth("⏱ " + PlaceholderAPI.setPlaceholders(player, "%mce24_timer_gubgame%"));
                    blocks = Math.round((float) width / 6);
                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("§a§l⏱ §a").append(PlaceholderAPI.setPlaceholders(player, "%mce24_timer_gubgame%"));
                    break;
                case "Craftalot":
                    width = FontUtils.getStringWidth("\uD83D\uDCB0" + PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"));
                    blocks = Math.round((float) width / 6);
                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("§e§l\uD83D\uDCB0").append(PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"));
                    output.append(" ".repeat(5)).append("§r");

                    width = FontUtils.getStringWidth("ᴄʀᴀғᴛᴀʟᴏᴛ Sir Craftalot's Castle");
                    blocks = Math.round((float) width / 6);
                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("§rᴄʀᴀғᴛᴀʟᴏᴛ §7§oSir Craftalot's Castle");
                    output.append(" ".repeat(6)).append("§r");

                    width = FontUtils.getStringWidth("⏱ " + PlaceholderAPI.setPlaceholders(player, "%mce24_timer_craftalot%"));
                    blocks = Math.round((float) width / 6);
                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("§a§l⏱ §a").append(PlaceholderAPI.setPlaceholders(player, "%mce24_timer_craftalot%"));
                    break;
                case "Zoomo Go":
                    width = FontUtils.getStringWidth("\uD83D\uDCB0" + PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"));
                    blocks = Math.round((float) width / 6);
                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("§e§l\uD83D\uDCB0").append(PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"));
                    output.append(" ".repeat(5)).append("§r");

                    width = FontUtils.getStringWidth("ᴢᴏᴏᴍᴏ ɢᴏ Adrenaline Ravine");
                    blocks = Math.round((float) width / 6);
                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("§rᴢᴏᴏᴍᴏ ɢᴏ §7§oAdrenaline Ravine");
                    output.append(" ".repeat(6)).append("§r");

                    width = FontUtils.getStringWidth("⏱ " + PlaceholderAPI.setPlaceholders(player, "%mce24_timer_zoomogo%"));
                    blocks = Math.round((float) width / 6);
                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("§a§l⏱ §a").append(PlaceholderAPI.setPlaceholders(player, "%mce24_timer_zoomogo%"));
                    break;
                case "Colour Dash":
                    width = FontUtils.getStringWidth("\uD83D\uDCB0" + PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"));
                    blocks = Math.round((float) width / 6);
                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("§e§l\uD83D\uDCB0").append(PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"));
                    output.append(" ".repeat(5)).append("§r");

                    width = FontUtils.getStringWidth("ᴄᴏʟᴏᴜʀ ᴅᴀsʜᴀᴛʜᴏɴ The Journey");
                    blocks = Math.round((float) width / 6);
                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("§rᴄᴏʟᴏᴜʀ ᴅᴀsʜᴀᴛʜᴏɴ §7§oThe Journey");
                    output.append(" ".repeat(6)).append("§r");

                    width = FontUtils.getStringWidth("⏱ " + PlaceholderAPI.setPlaceholders(player, "%mce24_timer_colourdash%"));
                    blocks = Math.round((float) width / 6);
                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("§a§l⏱ §a").append(PlaceholderAPI.setPlaceholders(player, "%mce24_timer_colourdash%"));
                    break;
                case "Survival Games":
                    width = FontUtils.getStringWidth("\uD83D\uDCB0" + PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"));
                    blocks = Math.round((float) width / 6);
                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("§e§l\uD83D\uDCB0").append(PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"));
                    output.append(" ".repeat(5)).append("§r");

                    width = FontUtils.getStringWidth("sᴜʀᴠɪᴠᴀʟ ɢᴀᴍᴇs Kondas by Kikzo");
                    blocks = Math.round((float) width / 6);
                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("§rsᴜʀᴠɪᴠᴀʟ ɢᴀᴍᴇs §7§oKondas §aby Kikzo");
                    output.append(" ".repeat(6)).append("§r");

                    width = FontUtils.getStringWidth("⏱ " + PlaceholderAPI.setPlaceholders(player, "%mce24_timer_survivalgames%"));
                    blocks = Math.round((float) width / 6);
                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("§a§l⏱ §a").append(PlaceholderAPI.setPlaceholders(player, "%mce24_timer_survivalgames%"));
                    break;
                default:
                    break;
            }
            bossBars.get(player.getName()).setTitle(output.toString());
        }
    }

    public void summonFirework(Location location, String team){
        World world = location.getWorld();

        Firework firework = (Firework) world.spawnEntity(location, EntityType.FIREWORK_ROCKET);

        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        FireworkEffect effect = FireworkEffect.builder()
                .withColor(plugin.teamColors.get(team))
                .withFade(plugin.teamColors.get(team))
                .with(FireworkEffect.Type.BURST)
                .build();

        fireworkMeta.addEffect(effect);
        fireworkMeta.setPower(0);
        firework.setFireworkMeta(fireworkMeta);

        firework.detonate();
    }

    public List<Material> getBridgeBlocks(int level, String team) {

        int xMultiplier = 35;

        switch(team){
            case "RubyRaiders": xMultiplier *= 0; break;
            case "AmberAmbushers": break;
            case "TopazTroopers": xMultiplier *= 2; break;
            case "KyaniteKillers": xMultiplier *= 3; break;
            case "DiamondDestroyers": xMultiplier *= 4; break;
            case "SapphireSoldiers": xMultiplier *= 5; break;
            case "SmithsoniteSlayers": xMultiplier *= 6; break;
            case "CrystalCrashers": xMultiplier *= 7; break;
        }

        List<Material> bridgeBlocks = new ArrayList<>();

        for (int i = 254 + xMultiplier; i <= 260 + xMultiplier; i++) {
            for (int j = 677 - (level * 38); j >= 663 - (level * 38); j--) {
                for(int k = -21; k <= -12; k++) {
                    if (!bridgeBlocks.contains(Bukkit.getServer().getWorld("build").getBlockAt(i, k, j).getType())) {
                        bridgeBlocks.add(Bukkit.getServer().getWorld("build").getBlockAt(i, k, j).getType());
                    }
                }
            }
        }

        return bridgeBlocks;
    }

    public LinkedHashMap<String, Integer> getSortedIndivs(){
        List<Map.Entry<String, Integer>> playerList = new ArrayList<>();

        for (String playerName : PlayerConfig.get().getConfigurationSection("players").getKeys(false)) {
            int points = PlayerConfig.get().getInt("players." + playerName + ".points");
            playerList.add(new AbstractMap.SimpleEntry<>(playerName, points));
        }

        playerList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : playerList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public boolean checkWithinBuildArea(Block block, String player) {

        String team = PlayerConfig.get().getString("players." + player + ".team");

        int level = plugin.teamCheckpoints.get(team);

        int teamIndex = 35;

        switch(team){
            case "RubyRaiders": teamIndex *= 0; break;
            case "AmberAmbushers": break;
            case "TopazTroopers": teamIndex *= 2; break;
            case "KyaniteKillers": teamIndex *= 3; break;
            case "DiamondDestroyers": teamIndex *= 4; break;
            case "SapphireSoldiers": teamIndex *= 5; break;
            case "SmithsoniteSlayers": teamIndex *= 6; break;
            case "CrystalCrashers": teamIndex *= 7; break;
        }

        return block.getX() >= 234 + teamIndex && block.getX() <= 240 + teamIndex &&
                block.getY() >= -21 && block.getY() <= -12 &&
                block.getZ() >= 663 - (level * 38) && block.getZ() <= 677 - (level * 38);
    }





    public void startBridgeJump(String team, int jump) {
        World world = Bukkit.getServer().getWorld("build");
        Location theLocation = new Location(world, 0, 0, 0);
        theLocation.setX((teamJump.get(team)[0]));
        theLocation.setY((teamJump.get(team)[1]));
        theLocation.setZ((teamJump.get(team)[2])-(38*(jump-1)));

        theLocation.setX(theLocation.getX()+0.5);

        TextDisplay newDisplay = world.spawn(theLocation, TextDisplay.class);
        newDisplay.setText("");
        newDisplay.setRotation(0, 0);
        newDisplay.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
        Quaternionf quat = new Quaternionf();
        Transformation transform = new Transformation(
                new Vector3f(0, 0, 0),
                quat,
                new Vector3f(10.0f, 10.0f, 10.0f),
                quat
        );

        newDisplay.setTransformation(transform);

        String name = team + jump;

        int seconds = 4;

        for(int x = teamJump.get(team)[0]-3; x <= teamJump.get(team)[0] + 3; x++){
            for(int y = teamJump.get(team)[1]-2; y <= teamJump.get(team)[1]+2; y++){
                Bukkit.getWorld("build").getBlockAt(x, y, (plugin.teamJump.get(team)[2]-(38*(jump-1)))).setType(Material.BARRIER);
            }
        }

        BukkitTask task = new BukkitRunnable() {
            boolean jumpStarted = false;
            int timeLeft = seconds;
            @Override
            public void run() {
                if(runningTimers.containsKey(name)) {
                    if (!pausedTimers.contains(name)) {
                        runningTimers.get(name).setValue(timeLeft);
                        bossBarBgTest();
                        timeLeft--;
                        int base = 0xE005 + (30-timeLeft);
                        String character = new String(Character.toChars(base));
                        newDisplay.setText(character);
                        if(timeLeft <= 5) {
                            for(String player : TeamsConfig.get().getStringList("teams." + team + ".players")){
                                if(Bukkit.getPlayer(player) != null){
                                    Player playerSound = Bukkit.getPlayer(player);
                                    playerSound.playSound(playerSound.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 10, 0.3F*timeLeft);
                                }
                            }
                        }
                        if (timeLeft == 0 && jumpStarted) {
                            plugin.bridgeTally.put(team, plugin.bridgeTally.get(team) + 1);
                            if(jump < 6) {
                                theLocation.setZ(theLocation.getZ() - 2);
                                theLocation.setYaw(180);
                                theLocation.setYaw(0);
                                for (String player : TeamsConfig.get().getStringList("teams." + team + ".players")) {
                                    if (Bukkit.getPlayer(player) != null) {
                                        Player p = Bukkit.getPlayer(player);
                                        p.teleport(theLocation);
                                        if (!bridgeJumpRegister.get(jump).contains(player)) {
                                            messagePlayer(p, "§c✖ §7| §fYou failed to complete the jump in time!");
                                        }
                                        p.playSound(p.getLocation(), Sound.ENTITY_BAT_DEATH, 10, 1);
                                        messagePlayer(p, "§4✖ §7| §cYour entire team was not able to complete the jump in time.");
                                        p.sendTitle("§a[✔] \uD83C\uDF09-" + jump, "§7Now build!", 0, 40, 0);
                                        messagePlayer(p, "§a[\uD83D\uDDFB-" + jump + "] Jump Complete!");
                                        messagePlayer(p, "§cBuild mode attained, get building!");
                                        p.setGameMode(GameMode.SURVIVAL);
                                        p.setAllowFlight(true);

                                        List<Material> blocks = plugin.getBridgeBlocks(jump, PlayerConfig.get().getString("players." + player + ".team"));
                                        for (Material block : blocks) {
                                            p.getInventory().addItem(new ItemStack(block, 64));
                                        }
                                    }
                                }
                                for (int x = (teamJump.get(team)[0]) - 3; x <= (teamJump.get(team)[0]) + 3; x++) {
                                    for (int y = teamJump.get(team)[1] - 2; y <= teamJump.get(team)[1] + 9; y++) {
                                        Bukkit.getWorld("build").getBlockAt(x, y, (plugin.teamJump.get(team)[2] - (38 * (jump - 1))) - 16).setType(Material.BARRIER);
                                    }
                                }
                            } else {
                                theLocation.setZ(theLocation.getZ() - 2);
                                theLocation.setYaw(180);
                                theLocation.setYaw(0);
                                for (String player : TeamsConfig.get().getStringList("teams." + team + ".players")) {
                                    if (Bukkit.getPlayer(player) != null) {
                                        plugin.bridgeJumpRegister.get(plugin.bridgeJumpRegister.size()).add(player);
                                        Player p = Bukkit.getPlayer(player);
                                        if (!bridgeJumpRegister.get(jump).contains(player)) {
                                            theLocation.setYaw(180);
                                            p.teleport(theLocation);
                                            theLocation.setYaw(0);
                                            messagePlayer(p, "§c✖ §7| §fYou failed to complete the jump in time!");
                                        }
                                        p.playSound(p.getLocation(), Sound.ENTITY_BAT_DEATH, 10, 1);
                                        messagePlayer(p, "§4✖ §7| §cYour entire team was not able to complete the jump in time.");
                                        p.setGameMode(GameMode.SPECTATOR);
                                        p.sendTitle("§aFINISH", "", 0, 100, 5);
                                        plugin.messagePlayer(p, "§e\uD83D\uDCB030 §8| §a§lCourse Completed!");
                                    }
                                }
                                Integer placement = plugin.bridgeJumpCheckpoints.get(plugin.bridgeJumpCheckpoints.size());
                                switch (placement) {
                                    case 1:
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            plugin.messagePlayer(p, "§8| §e\uD83D\uDC51 §8| " + plugin.getTeamDisplayName(team) + "§e was §e§l1st §eto finish!");
                                        }
                                        break;
                                    case 2:
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            plugin.messagePlayer(p, "§8| §7\uD83D\uDC51 §8| " + plugin.getTeamDisplayName(team) + "§e was §7§l2nd §eto finish!");
                                        }
                                        break;
                                    case 3:
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            plugin.messagePlayer(p, "§8| §6\uD83D\uDC51 §8| " + plugin.getTeamDisplayName(team) + "§e was §6§l3rd §eto finish!");
                                        }
                                        break;
                                    default:
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            plugin.messagePlayer(p, "§8| §f\uD83D\uDC51 §8| " + plugin.getTeamDisplayName(team) + "§e was §f§l" + placement + "th §eto finish!");
                                        }
                                        break;
                                }
                                plugin.bridgeJumpCheckpoints.replace(plugin.bridgeJumpCheckpoints.size(), placement + 1);
                            }
                            plugin.buildTimeStamps.put(team, plugin.runningTimers.get("bridgebuilders").getValue());
                            newDisplay.remove();
                            messageConsole("Timer finished.");
                            runningTimers.remove(name);
                            cancel();
                        }
                        if (timeLeft == 0 && !jumpStarted) {
                            theLocation.setZ(theLocation.getZ() - 16);
                            newDisplay.teleport(theLocation);
                            timeLeft = 30;
                            base = 0xE005;
                            character = new String(Character.toChars(base));
                            newDisplay.setText(character);
                            jumpStarted = true;
                            for(int x = teamJump.get(team)[0]-3; x <= teamJump.get(team)[0] + 3; x++){
                                for(int y = teamJump.get(team)[1]-2; y <= teamJump.get(team)[1]+2; y++){
                                    Bukkit.getWorld("build").getBlockAt(x, y, (plugin.teamJump.get(team)[2]-(38*(jump-1)))).setType(Material.AIR);
                                }
                            }
                        }
                    }
                } else {
                    messageConsole("Timer removed by external factor.");
                    newDisplay.remove();
                    cancel();
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put(name, new AbstractMap.SimpleEntry<>(task, seconds));
    }


    public void thunderStormEvent() {
        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendTitle("§7Modifier:", "§6§lᴛʜᴜɴᴅᴇʀsᴛᴏʀᴍ", 0, 40, 20);
        }
        playSoundAll(Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1);
        World world = Bukkit.getWorld("build");
        world.setStorm(true);
        world.setThundering(true);
        world.setWeatherDuration(1200);
        Random rand = new Random();
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 51;
            @Override
            public void run() {
                if(runningTimers.containsKey("thunderstorm")) {
                    if (!pausedTimers.contains("thunderstorm")) {
                        runningTimers.get("thunderstorm").setValue(timeLeft);
                        timeLeft--;
                        if(timeLeft % 5 == 0 && timeLeft <= 40 && timeLeft >= 10){
                            for(Slime slime : slimeGolfSlime) {
                                world.strikeLightning(slime.getLocation());
                                Vector velocity = new Vector(-1 + (rand.nextDouble() * 2), 1.5, -1 + (rand.nextDouble() * 2));
                                slime.setVelocity(velocity);
                            }
                            for(Player player : getPlayers()) {
                                Vector velocity = new Vector(0, 0.8, 0);
                                player.setVelocity(velocity);
                            }
                        }
                        if (timeLeft == 0) {
                            world.setStorm(false);
                            world.setThundering(false);
                            messageConsole("Timer finished.");
                            runningTimers.remove("thunderstorm");
                            cancel();
                        }
                    }
                } else {
                    messageConsole("Timer removed by external factor.");
                    cancel();
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("thunderstorm", new AbstractMap.SimpleEntry<>(task, 26));
    }


    public void honeyIShrunkTheRosterEvent() {
        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendTitle("§7Modifier:", "§b§lʜᴏɴᴇʏ ɪ sʜʀᴜɴᴋ ᴛʜᴇ ʀᴏsᴛᴇʀ", 0, 40, 20);
        }
        playSoundAll(Sound.BLOCK_BREWING_STAND_BREW, 1.5F);
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 36;
            @Override
            public void run() {
                if(runningTimers.containsKey("HISTR")) {
                    if (!pausedTimers.contains("HISTR")) {
                        runningTimers.get("HISTR").setValue(timeLeft);
                        timeLeft--;
                        if(timeLeft == 30){
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 2.0F);
                            PotionEffect effect = new PotionEffect(PotionEffectType.HASTE, 600, 0, true, true);
                            for(Player player : getPlayers()) {
                                player.addPotionEffect(effect);
                                player.getAttribute(Attribute.SCALE).setBaseValue(0.5F);
                            }
                        }
                        if (timeLeft == 0) {
                            for(Player player : getPlayers()) {
                                player.getAttribute(Attribute.SCALE).setBaseValue(1.0F);
                            }
                            messageConsole("Timer finished.");
                            runningTimers.remove("HISTR");
                            cancel();
                        }
                    }
                } else {
                    messageConsole("Timer removed by external factor.");
                    cancel();
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("HISTR", new AbstractMap.SimpleEntry<>(task, 26));
    }

    public void setPreviousPlacements(){
        previousPlacements.clear();
        List<String> indivNames = new ArrayList<>(plugin.getSortedIndivs().keySet());
        int index = 0;
        for(String player : indivNames){
            index++;
            previousPlacements.put(player, index);
        }
    }

    public void setCurrentPlacements() {
        currentPlacements.clear();
        List<String> indivNames = new ArrayList<>(plugin.getSortedIndivs().keySet());
        int index = 0;
        for(String player : indivNames){
            index++;
            currentPlacements.put(player, index);
        }
    }


    public void summonVotingPiece(int index, BlockData colour) {

        World world = Bukkit.getWorld("build");

        int x = 187 + index;
        int y = 139;
        int z = 713;

        List<BlockDisplay> blockList = new ArrayList<>();
        BlockDisplay tempBD;

        for(int i = z; i <= z + 43; i++){
            tempBD = (BlockDisplay) world.spawnEntity(new Location(world, x, y, i), EntityType.BLOCK_DISPLAY);
            BlockData sourceData = colour;
            tempBD.setBlock(sourceData);
            blockList.add(tempBD);
        }

        Vector3f translation = new Vector3f(0.5F, 0.5F, 0.5F);
        Quaternionf leftRotation = new Quaternionf();
        Quaternionf rightRotation = new Quaternionf();
        Vector3f scaleVector = new Vector3f(0.0F, 0.0F, 0.0F);

        Transformation transformation = new Transformation(translation, leftRotation, scaleVector, rightRotation);
        for(BlockDisplay bd : blockList){
            bd.setTransformation(transformation);
        }

        String name = "build" + index;

        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 20;
            @Override
            public void run() {
                if(plugin.runningTimers.containsKey(name)) {
                    if (!plugin.pausedTimers.contains(name)) {
                        timeLeft--;
                        plugin.runningTimers.get(name).setValue(timeLeft);

                        transformation.getScale().add(0.05F,0.05F,0.05F);
                        transformation.getTranslation().sub(0.025F, 0.025F, 0.025F);

                        if (timeLeft == 1) {
                            for (int i = z; i <= z + 43; i++) {
                                world.getBlockAt(x, y, i).setType(colour.getMaterial());
                            }
                        } else if(timeLeft == 0){
                            for(BlockDisplay bd : blockList){
                                bd.remove();
                            }
                            plugin.runningTimers.remove(name);
                            cancel();
                        } else {
                            for(BlockDisplay bd : blockList){
                                bd.setTransformation(transformation);
                            }
                        }
                    }
                } else {
                    cancel();
                }
            }

        }.runTaskTimer(plugin, 0L, 1L);

        plugin.runningTimers.put(name, new AbstractMap.SimpleEntry<>(task, 21));
    }

    public void removeVotingPiece(int index) {

        World world = Bukkit.getWorld("build");

        int x = 187 + index;
        int y = 139;
        int z = 713;

        List<BlockDisplay> blockList = new ArrayList<>();
        BlockDisplay tempBD;

        for(int i = z; i <= z + 43; i++){
            tempBD = (BlockDisplay) world.spawnEntity(new Location(world, x, y, i), EntityType.BLOCK_DISPLAY);
            BlockData sourceData = world.getBlockAt(x, y, i).getBlockData();
            tempBD.setBlock(sourceData);
            blockList.add(tempBD);
        }

        Vector3f translation = new Vector3f(0.0F, 0.0F, 0.0F);
        Quaternionf leftRotation = new Quaternionf();
        Quaternionf rightRotation = new Quaternionf();
        Vector3f scaleVector = new Vector3f(1.0F, 1.0F, 1.0F);

        Transformation transformation = new Transformation(translation, leftRotation, scaleVector, rightRotation);
        for(BlockDisplay bd : blockList){
            bd.setTransformation(transformation);
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for(int i = z; i <= z + 43; i++) {
                world.getBlockAt(x, y, i).setType(Material.BARRIER);
            }
        }, 1L);

        String name = "buildremove" + index;

        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 20;
            @Override
            public void run() {
                if(plugin.runningTimers.containsKey(name)) {
                    if (!plugin.pausedTimers.contains(name)) {
                        timeLeft--;
                        plugin.runningTimers.get(name).setValue(timeLeft);

                        transformation.getScale().sub(0.05F,0.05F,0.05F);
                        transformation.getTranslation().add(0.025F, 0.025F, 0.025F);

                        for(BlockDisplay bd : blockList){
                            bd.setTransformation(transformation);
                        }

                        if (timeLeft == 0) {
                            for(BlockDisplay bd : blockList){
                                bd.remove();
                            }
                            plugin.runningTimers.remove(name);
                            cancel();
                        }
                    }
                } else {
                    cancel();
                }
            }

        }.runTaskTimer(plugin, 0L, 1L);

        plugin.runningTimers.put(name, new AbstractMap.SimpleEntry<>(task, 21));
    }


    public void startFinale(){
        int[][] modeTitles = {
                {1861, 167, 889},
                {1860, 167, 902},
                {1852, 167, 913},
                {1838, 167, 918},
                {1824, 167, 913},
                {1816, 167, 902},
                {1815, 167, 889}
        };

        int[][] modeSlots = {
                {1864, 154, 885},
                {1863, 154, 898},
                {1855, 154, 909},
                {1841, 154, 914},
                {1827, 154, 909},
                {1819, 154, 898},
                {1818, 154, 885}
        };

        HashMap<String, Integer> modeIndexes = new HashMap<>();

        modeIndexes.put("Craftalot", 1);
        modeIndexes.put("Colour Dash", 2);
        modeIndexes.put("Bridge Builders", 3);
        modeIndexes.put("Survival Games", 4);
        modeIndexes.put("Gub Game", 5);
        modeIndexes.put("Zoomo Go", 6);
        modeIndexes.put("Slime Golf", 7);

        TextDisplay[] displays = new TextDisplay[modeTitles.length];


        finaleRound = 1;
        String name = "finalestart";
        List<String> gameOrder = new ArrayList<>();
        gameOrder.addAll(GameOrderConfig.get().getStringList("order"));

        BukkitTask task = new BukkitRunnable() {
            List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());
            String firstTeam = leaderteams.getFirst();
            String secondTeam = leaderteams.get(1);
            List<String> firstPlayers = TeamsConfig.get().getStringList("teams." + firstTeam + ".players");
            List<String> secondPlayers = TeamsConfig.get().getStringList("teams." + secondTeam + ".players");


            World world = Bukkit.getWorld("build");
            int index1 = 0;
            int index2 = 0;
            int gameIndex = 0;
            int timeLeft = 101;
            @Override
            public void run() {
                if(plugin.runningTimers.containsKey(name)) {
                    if (!plugin.pausedTimers.contains(name)) {
                        timeLeft--;
                        plugin.runningTimers.get(name).setValue(timeLeft);

                        switch(timeLeft){
                            case 100:
                                finaleScores.put(firstTeam, 0);
                                finaleScores.put(secondTeam, 0);

                                for(int[] modeSlot : modeSlots) {
                                    world.getBlockAt(modeSlot[0], modeSlot[1], modeSlot[2]).setType(Material.REDSTONE_BLOCK);
                                    world.getBlockAt(modeSlot[0], modeSlot[1], modeSlot[2]).setType(Material.AIR);
                                }

                                PotionEffect lev = new PotionEffect(PotionEffectType.LEVITATION, 200, 1);
                                PotionEffect slowfall = new PotionEffect(PotionEffectType.SLOW_FALLING, 300, 1);
                                for(Player p : getPlayers()){
                                    p.addPotionEffect(lev);
                                    p.addPotionEffect(slowfall);
                                }
                                teleportPlayers(TeleportConfig.get().getLocation("players.finalepodium"), 10);
                                teleportSpectators(TeleportConfig.get().getLocation("spectators.finalepodium"), 10);
                                break;
                            case 90:
                                playSoundAll(Sound.BLOCK_END_PORTAL_SPAWN, 1F);
                                break;

                            case 85, 84, 83, 82:
                                if(Bukkit.getPlayer(secondPlayers.get(index1)) != null){
                                    Player p = Bukkit.getPlayer(secondPlayers.get(index1));
                                    p.teleport(new Location(world, 1832 + index1, 157, 895, 180, -15));
                                }
                                for(int x = 1824; x <= 1837; x++){
                                    for(int y = 151; y <= 153 + (index1*2); y++){
                                        for(int z = 891; z <= 899; z++){
                                            if(world.getBlockAt(x,y,z).getType().equals(Material.WHITE_CONCRETE)){
                                                world.getBlockAt(x,y,z).setType(teamConcrete.get(secondTeam));
                                            }
                                            if(world.getBlockAt(x,y,z).getType().equals(Material.WHITE_WOOL)){
                                                world.getBlockAt(x,y,z).setType(teamWool.get(secondTeam));
                                            }

                                        }

                                    }
                                }
                                index1++;
                                if(index1 == 4){
                                    for(Player p : Bukkit.getOnlinePlayers()){
                                        messagePlayer(p, """
                                            §8
                                            §8
                                            §r""" + getTeamDisplayName(secondTeam) + """
                                            §f""" + secondPlayers.getFirst() + ", " + secondPlayers.get(1) + ", " + secondPlayers.get(2) + ", " + secondPlayers.get(3) + """
                                            §8
                                            """);
                                    }
                                }
                                break;
                            case 80, 79, 78, 77:
                                if(Bukkit.getPlayer(firstPlayers.get(index2)) != null){
                                    Player p = Bukkit.getPlayer(firstPlayers.get(index2));
                                    p.teleport(new Location(world, 1842 + index2, 157, 895, 180, -15));
                                }
                                for(int x = 1839; x <= 1852; x++){
                                    for(int y = 151; y <= 153 + (index2*2); y++){
                                        for(int z = 891; z <= 899; z++){
                                            if(world.getBlockAt(x,y,z).getType().equals(Material.WHITE_CONCRETE)){
                                                world.getBlockAt(x,y,z).setType(teamConcrete.get(firstTeam));
                                            }
                                            if(world.getBlockAt(x,y,z).getType().equals(Material.WHITE_WOOL)){
                                                world.getBlockAt(x,y,z).setType(teamWool.get(firstTeam));
                                            }

                                        }

                                    }
                                }
                                index2++;
                                if(index2 == 4){
                                    for(Player p : Bukkit.getOnlinePlayers()){
                                        messagePlayer(p, """
                                            §8
                                            §8
                                            §r""" + getTeamDisplayName(firstTeam) + """
                                            §f""" + firstPlayers.getFirst() + ", " + firstPlayers.get(1) + ", " + firstPlayers.get(2) + ", " + firstPlayers.get(3) + """
                                            §8
                                            """);
                                    }
                                }
                                break;
                            case 74:
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle("", getTeamDisplayName(firstTeam) + " §evs " + getTeamDisplayName(secondTeam), 0, 60, 20);
                                }
                                break;
                            case 70:
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle("§f§lɢᴀᴍᴇ ᴏʀᴅᴇʀ:", "§7§oLoading..", 0, 80, 20);
                                }
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1F);
                                for(Player player : Bukkit.getOnlinePlayers()){
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("⏳ §f§lHere's the finale game order!"));
                                }
                                break;
                            case 66, 63, 60, 57, 54, 51, 48:
                                String game = gameOrder.get(gameIndex);
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle("§f§lɢᴀᴍᴇ ᴏʀᴅᴇʀ:", "§e§l" + gameOrder.get(gameIndex), 0, 80, 20);
                                }
                                world.getBlockAt(modeSlots[gameIndex][0], modeSlots[gameIndex][1] - modeIndexes.get(game), modeSlots[gameIndex][2]).setType(Material.REDSTONE_BLOCK);
                                world.getBlockAt(modeSlots[gameIndex][0], modeSlots[gameIndex][1] - modeIndexes.get(game), modeSlots[gameIndex][2]).setType(Material.AIR);

                                displays[gameIndex] = world.spawn(new Location(world, modeTitles[gameIndex][0], modeTitles[gameIndex][1], modeTitles[gameIndex][2]), TextDisplay.class);
                                displays[gameIndex].setSeeThrough(true);
                                displays[gameIndex].setText(plugin.modeLogos.get(game));
                                displays[gameIndex].setBillboard(Display.Billboard.CENTER);
                                displays[gameIndex].setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                                Quaternionf quat = new Quaternionf();
                                Transformation transform = new Transformation(
                                        new Vector3f(0, 0, 0),
                                        quat,
                                        new Vector3f(5.0f, 5.0f, 5.0f),
                                        quat
                                );

                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1F);
                                for(Player player : Bukkit.getOnlinePlayers()){
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("⏳ §f§l" + game));
                                }

                                displays[gameIndex].setTransformation(transform);

                                gameIndex++;
                                break;

                            case 30:
                                String firstGame = gameOrder.getFirst();
                                switch(firstGame){
                                    case "Slime Golf":
                                        startSlimeGolfFinale();
                                        break;
                                    case "Survival Games":

                                        break;
                                    case "Gub Game":

                                        break;
                                    case "Colour Dash":

                                        break;
                                    case "Craftalot":

                                        break;
                                    case "Bridge Builders":

                                        break;
                                    case "Zoomo Go":

                                        break;
                                }
                        }


                        if (timeLeft == 25) {
                            plugin.runningTimers.remove(name);
                            cancel();
                        }
                    }
                } else {
                    cancel();
                }
            }

        }.runTaskTimer(plugin, 0L, 20L);

        plugin.runningTimers.put(name, new AbstractMap.SimpleEntry<>(task, 21));


//        teamNameDisplay = world.spawn(theLocation, TextDisplay.class);
//        teamNameDisplay.setSeeThrough(true);
//        teamNameDisplay.setText(getTeamDisplayName("RubyRaiders"));
//        teamNameDisplay.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
//        Quaternionf quat = new Quaternionf();
//        Transformation transform = new Transformation(
//                new Vector3f(0, 0, 0),
//                quat,
//                new Vector3f(5.0f, 5.0f, 5.0f),
//                quat
//        );
//
//        teamNameDisplay.setTransformation(transform);

    }

    public void finaleRoundOver(String winningTeam){
        List<String> gameOrder = new ArrayList<>();
        gameOrder.addAll(GameOrderConfig.get().getStringList("order"));
        List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());
        String firstTeam = leaderteams.getFirst();
        String secondTeam = leaderteams.get(1);
        String losingTeam;
        if(Objects.equals(winningTeam, firstTeam)){
            losingTeam = secondTeam;
        } else {
            losingTeam = firstTeam;
        }
        String winningTeamPrefix = TeamsConfig.get().getString("teams." + winningTeam + ".colour") + "§l";
        String losingTeamPrefix = TeamsConfig.get().getString("teams." + losingTeam + ".colour") + "§l";

        finaleScores.put(winningTeam, finaleScores.get(winningTeam)+1);
        if(finaleScores.get(winningTeam) == 4){
            for(Player p : Bukkit.getOnlinePlayers()){
                p.sendTitle(getTeamDisplayName(winningTeam), "§f§lᴡɪɴ sʜᴏᴡᴅᴏᴡɴ", 0, 120, 40);
            }
        } else {
            for(Player p : Bukkit.getOnlinePlayers()){
                p.sendTitle("§c§lROUND OVER", winningTeamPrefix + finaleScores.get(winningTeam) + "§f - " + losingTeamPrefix + finaleScores.get(losingTeam) , 0, 60, 20);
            }
        }
        pvpEnabled = false;
        doubleJumpEnabled = false;
        currentBorderRadius = 236;
        newBorderRadius = 236;
        killRecord.clear();
        if(currentMode.equals("Slime Golf")){
            for(Slime slime : slimeGolfSlime){
                slime.remove();
            }
        }
        try {
            unGlowTeams();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
        for (Player player : getPlayers()) {
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }
            bossBars.get(player.getName()).removePlayer(player);
            bossBars.put(player.getName(), null);
            player.setGameMode(GameMode.SPECTATOR);
            Bukkit.getScheduler().runTaskLater(plugin, () -> player.setFlying(true), 1L);
            healFeedPlayer(player);

        }
        String name = "finaleroundover";
        BukkitTask task = new BukkitRunnable() {
            List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());
            String firstTeam = leaderteams.getFirst();
            String secondTeam = leaderteams.get(1);
            List<String> firstPlayers = TeamsConfig.get().getStringList("teams." + firstTeam + ".players");
            List<String> secondPlayers = TeamsConfig.get().getStringList("teams." + secondTeam + ".players");


            World world = Bukkit.getWorld("build");
            int timeLeft = 51;
            @Override
            public void run() {
                if(plugin.runningTimers.containsKey(name)) {
                    if (!plugin.pausedTimers.contains(name)) {
                        timeLeft--;
                        plugin.runningTimers.get(name).setValue(timeLeft);

                        switch(timeLeft){
                            case 50:
                                PotionEffect lev = new PotionEffect(PotionEffectType.LEVITATION, 200, 1);
                                PotionEffect slowfall = new PotionEffect(PotionEffectType.SLOW_FALLING, 300, 1);
                                for(Player p : getPlayers()){
                                    p.addPotionEffect(lev);
                                    p.addPotionEffect(slowfall);
                                }
                                teleportPlayers(TeleportConfig.get().getLocation("players.finalepodium"), 10);
                                teleportSpectators(TeleportConfig.get().getLocation("spectators.finalepodium"), 10);
                                break;
                            case 40:
                                for(String player : secondPlayers) {
                                    if (Bukkit.getPlayer(player) != null) {
                                        Player p = Bukkit.getPlayer(player);
                                        p.teleport(new Location(world, 1834, 157, 895, 180, -15));
                                    }
                                }
                                for(String player : secondPlayers) {
                                    if (Bukkit.getPlayer(player) != null) {
                                        Player p = Bukkit.getPlayer(player);
                                        p.teleport(new Location(world, 1844, 157, 895, 180, -15));
                                    }
                                }
                                break;
                            case 30:
                                finaleRound++;
                                String firstGame = gameOrder.get(finaleRound);
                                switch(firstGame){
                                    case "Slime Golf":
                                        startSlimeGolfFinale();
                                        break;
                                    case "Survival Games":

                                        break;
                                    case "Gub Game":

                                        break;
                                    case "Colour Dash":

                                        break;
                                    case "Craftalot":

                                        break;
                                    case "Bridge Builders":

                                        break;
                                    case "Zoomo Go":

                                        break;
                                }
                                break;
                        }
                        if (timeLeft == 25) {
                            plugin.runningTimers.remove(name);
                            cancel();
                        }
                    }
                } else {
                    cancel();
                }
            }

        }.runTaskTimer(plugin, 0L, 20L);

        plugin.runningTimers.put(name, new AbstractMap.SimpleEntry<>(task, 21));
    }
}


