package me.chazzagram.showdown2;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import fr.skytasul.glowingentities.GlowingBlocks;
import fr.skytasul.glowingentities.GlowingEntities;
import me.chazzagram.showdown2.commands.*;
import me.chazzagram.showdown2.expansions.MongoManager;
import me.chazzagram.showdown2.expansions.SpigotExpansion;
import me.chazzagram.showdown2.files.*;
import me.chazzagram.showdown2.listeners.*;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.data.*;
import org.bukkit.block.data.type.Fire;
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
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;



import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.bukkit.util.NumberConversions.round;

public final class Showdown2 extends JavaPlugin implements Listener {

    public HashMap<String, EventPlayer> eventPlayers = new HashMap<>();

    public boolean eventOver = false;

    public List<String> ddFinishers = new ArrayList<>();

    public HashMap<String, SulfurGolfQueue> golfQueues = new HashMap<>();

    BukkitTask crumblePearlTask;

    public LobbyPvPManager pvpArenaManager;

    // Crumble Clash Border
    public final int ccMIN_X = 21,  ccMAX_X = 112;
    public final int ccMIN_Y = 175, ccMAX_Y = 230;
    public final int ccMIN_Z = 455, ccMAX_Z = 546;

    List<SulfurCube> votingCubes = new ArrayList<>();

    BukkitTask votingTask;

    public long slimeGolfStartTime = 0;

    public List<String> gameOrderTab = new ArrayList<>();

    public HashMap<String, Integer> ddFinaleTeamCompletions = new HashMap<>();

    public Team slimeTeam;

    public Map<Material, String> concreteConvertTeam = new HashMap<>();

    public List<String> firstJoinList = new ArrayList<>();

    public boolean finaleFirstTeamRevealed = true;
    public boolean finaleSecondTeamRevealed = true;

    public BukkitTask ddTimer;

    public BukkitTask craftFinaleTimer;

    public List<Player> unfinishedPlayers = new ArrayList<>();

    public HashMap<String, DashTimeData> dashLapData = new HashMap<>();

    public long ddStartTime = 0;

    public boolean suddenDeath = false;

    public boolean ppActive = false;

    private static Showdown2 plugin;

    public HashMap<Player, List<Snowball>> thrownSnowballs = new HashMap<>();

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

    public HashMap<String, CrumbleKillData> crumbleKillTracker = new HashMap<>();

    // Layer, Blocks Broken
    public HashMap<String, List<CrumbleBlockRecord>> crumbleBlockRecords = new HashMap<>();

    public HashMap<String, Integer> modeCompletions = new HashMap<>();

    public HashMap<String, Integer> modeTeamPoints = new HashMap<>();

    public HashMap<String, Integer> modePoints = new HashMap<>();

    public HashMap<String, Integer> modeFullPoints = new HashMap<>();

    public HashMap<String, Integer> modeTeamFullPoints = new HashMap<>();

    public HashMap<String, String> itemToCraft = new HashMap<>();

    public HashMap<String, List<String>> itemsToCraft = new HashMap<>();

    public HashMap<Integer, List<String>> craftDifficultyLists = new HashMap<>();

    public List<String> finaleCraftList = new ArrayList<>();

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

    public Boolean lifeCap = false;
    BossBar bossBar = Bukkit.createBossBar("Initial Title", BarColor.BLUE, BarStyle.SOLID);

    public HashMap<String, List<BossBar>> bossBars = new HashMap<>();

    public HashMap<String, List<String>> craftLists = new HashMap<>();

    public Location sgCenter = new Location(Bukkit.getWorld("build"), -180, 0, -709);

    public int newBorderRadius = 236;

    public int currentBorderRadius = 236;

    public HashMap<String, Integer> currentPlacements = new HashMap<>();
    public HashMap<String, Integer> previousPlacements = new HashMap<>();

    public int[][] slimeCmdCoords = {
            {880, 41, 1362},
            {934, 40, 1317},
            {1007, 38, 1414},
            {1132, 36, 1356},
            {1221, 27, 1339},
            {1302, 23, 1348}

    };

    public int[][] slimeCmdCoords2 = {
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

    List<SulfurCube> slimeGolfSlime = new ArrayList<>();

    public HashMap<String, SulfurCube> golfTeamCubes = new HashMap<>();

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

    // Push Point
    public HashMap<ItemDisplay, String> mapWalls = new HashMap<>();

    public Map<ItemDisplay, Map<String, List<String>>> playersNearWalls = new HashMap<>();

    public HashMap<ItemDisplay, TextDisplay> wallTexts = new HashMap<>();

    public HashMap<ItemDisplay, TextDisplay> wallPushersTexts = new HashMap<>();

    public HashMap<String, Block[]> mapSides = new HashMap<>();

    public HashMap<Material, String> concreteChatColors = new HashMap<>();

    public static final int PP_PATH_MIN = -420;
    public static final int PP_PATH_MAX = -482;

    public HashMap<String, List<Player>> ppEscapedPlayers = new HashMap<>();

    public Map<String, Map<ItemDisplay, Double>> finalPushMovements = new HashMap<>();

    public Boolean finalPush = false;

    public HashMap<String, Integer> zoomoLives = new HashMap<>();

    public HashMap<String, String> teamChatColours = new HashMap<>();

    public HashMap<Integer, String> crumbleMaps = new HashMap<>();

    public HashMap<Integer, List<String>> crumbleMapModifiers = new HashMap<>();

    public List<Integer> crumbleMapList = new ArrayList<>();

    public List<Integer> currentCrumbleList = new ArrayList<>();

    public Boolean copperDecay = false;


    Location txtLeftLoc = new Location(Bukkit.getWorld("build"), 88.5, 201.5, 500.5);
    Location txtMiddleLoc = new Location(Bukkit.getWorld("build"), 66.5, 201.5, 500.5);
    Location txtRightLoc = new Location(Bukkit.getWorld("build"), 44.5, 201.5, 500.5);

    List<TextDisplay> mapTitles = new ArrayList<>();

    HashMap<Integer, Integer> ccMapVotes = new HashMap<>();

    private final Map<Block, BukkitTask> scheduledDecay = new HashMap<>();

    public String currentSpleef = "&fWaiting..";

    public String zoomoMap;

    public List<ItemBox> itemBoxes = new ArrayList<>();

    public HashMap<Integer, Integer> itemBoxBoundaries = new HashMap<>();

    public HashMap<Integer, Integer> itemBoxCount = new HashMap<>();

    final double MIN_DISTANCE = 4.0;

    Random ranX = new Random();
    Random ranZ = new Random();

    public List<int[]> activeIslands = new ArrayList<>();

    final int SLOT_A = 36;
    final int GAP = 33;
    final int SLOT_B = 159;
    final int SLOT_C = 36;

    final int SLOT2_A = 0;
    final int GAP2 = 0;
    final int SLOT2_B = 227;
    final int SLOT2_C = 0;

    public HashMap<Fireball, Player> fireballSenders = new HashMap<>();

    Interaction goal;

    Slime slime1, slime2;

    private MongoManager mongoManager;

    private MongoDatabase database;

    public HashMap<String, String> teamNamesFormatted = new HashMap<>();

    public HashMap<Item, Player> voteBlasterBlasts = new HashMap<>();

    public String votingMode = "walk";

    public HashMap<Player, Location> playerSelectedTeleport = new HashMap<>();

    public HashMap<String, Map<Player, String>> ppTeamSelectedKits = new HashMap<>();

    public HashMap<String, Inventory> ppTeamKitInventories = new HashMap<>();

    public HashMap<String, List<Integer>> ppTeamStandings = new HashMap<>();

    public HashMap<String, String> ppTeamMatchups = new HashMap<>();

    public VoteFrozenManager voteFrozenManager;

    public HashMap<String, List<Material>> ddMapBlocks = new HashMap<>();

    public HashMap<Material, String> ddMapWalkableBlock = new HashMap<>();

    public List<String> ddSelectedMaps = new ArrayList<>();

    public List<TextDisplay> ddPortalTitles = new ArrayList<>();

    public String ddChosenMap = "";

    public HashMap<String, Location> ddTeleportLocations = new HashMap<>();

    public HashMap<String, String> ddMapVotes = new HashMap<>();

    public GhostManager ghostManager;

    public HashMap<SulfurCube, String> finaleSlimes = new HashMap<>();

    public HashMap<String, Integer> teamCrafts = new HashMap<>();

    public HashMap<String, List<Integer>> playerCrafts = new HashMap<>();

    public boolean finaleActive = false;

    public Map<Block, BlockDisplay> blockToDisplay = new HashMap<>();

    public LobbyMusicManager musicManager;

    public String timerLabel = "Starting Soon..";

    public int targetTime = 0;
    
    public boolean ccRoundStarted = false;

    public boolean playersShown = true;

    public List<TextDisplay> ddMapTiles = new ArrayList<>();

    public List<Material> concreteList = new ArrayList<>();


    // Spawn the particle

    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;

        musicManager = new LobbyMusicManager(plugin);

        voteFrozenManager = new VoteFrozenManager(plugin);

        ghostManager = new GhostManager(plugin);

        mongoManager = new MongoManager();

        pvpArenaManager = new LobbyPvPManager(plugin);


        for(int i = 0; i <= 3; i++){
            Block[] blocks = new Block[2];
            World world = Bukkit.getWorld("build");
            blocks[0] = world.getBlockAt(1073 + (101*i), -61, -403);
            blocks[1] = world.getBlockAt(1073 + (101*i), -61, -499);
            mapSides.put("map" + (i+1), blocks);
        }

        Block[] blocks = new Block[2];
        World world = Bukkit.getWorld("build");
        blocks[0] = world.getBlockAt(1506, -61, -403);
        blocks[1] = world.getBlockAt(1506, -61, -499);
        mapSides.put("map5finale", blocks);

        emotes.put("§e§l§oHYPE!", "\uE024");
        emotes.put("§e§l§oFIRE!", "\uE025");
        emotes.put("§e§l§o...", "\uE026");
        emotes.put("§e§l§o:O", "\uE027");

        ddMapBlocks.put("Ski Resort", Arrays.asList(Material.BLUE_ICE, Material.SNOW_BLOCK));
        ddMapBlocks.put("2023", Arrays.asList(Material.BIRCH_PLANKS, Material.STRIPPED_BIRCH_WOOD));
        ddMapBlocks.put("Chaos Canyon", Arrays.asList(Material.RED_SANDSTONE, Material.ORANGE_TERRACOTTA));

        ddMapWalkableBlock.put(Material.SNOW_BLOCK, "Ski Resort");
        ddMapWalkableBlock.put(Material.STRIPPED_BIRCH_WOOD, "2023");
        ddMapWalkableBlock.put(Material.ORANGE_TERRACOTTA, "Chaos Canyon");

        ddTeleportLocations.put("Ski Resort", new Location(Bukkit.getWorld("build"), -979, 144, 1104, 0 , 0));
        ddTeleportLocations.put("2023", new Location(Bukkit.getWorld("build"), -529, 144, 1104, 0, 0));
        ddTeleportLocations.put("Chaos Canyon", new Location(Bukkit.getWorld("build"), -1261, -10, 79, 0, 0));

        superAdmins.add("Chazzagram");

        glowingEntities = new GlowingEntities(plugin);
        glowingBlocks = new GlowingBlocks(plugin);

        this.getCommand("mcevent").setExecutor(new MainCommand(this));
        this.getCommand("mcevent").setTabCompleter(new TabCompleterCMD());
        this.getCommand("pvparena").setExecutor(new PvPArenaCommand(this));
        this.getCommand("emote").setExecutor(new EmoteCommand(this));
        this.getCommand("music").setExecutor(new LobbyMusicCommand(this));
        this.getCommand("indiv").setExecutor(new IndivLeaderCommand(this));
        this.getCommand("modeindiv").setExecutor(new ModeIndivCommand(this));
        this.getCommand("leaderboard").setExecutor(new TeamLeaderCommand(this));


        crumbleMaps.put(1,"§b§lSlippery Spleef");
        crumbleMaps.put(2,"§f§lClassic Spleef");
        crumbleMaps.put(3,"§c§lNether Spleef");
        crumbleMaps.put(4,"§a§lEnder Spleef");
        crumbleMaps.put(5,"§6§lCopper Spleef");

        crumbleMapModifiers.put(1, new ArrayList<>(List.of("Pickaxe", "Fishing Rods")));
        crumbleMapModifiers.put(2, new ArrayList<>(List.of("Shovel", "Snowballs")));
        crumbleMapModifiers.put(3, new ArrayList<>(List.of("Pickaxe", "Shovel", "Fireballs", "Soul Speed")));
        crumbleMapModifiers.put(4, new ArrayList<>(List.of("Pickaxe", "Enderpearls")));
        crumbleMapModifiers.put(5, new ArrayList<>(List.of("Decay Boots", "Mace", "Wind Charges")));

        teamColors.put("RubyRaiders", Color.RED);
        teamColors.put("AmberAmbushers", Color.ORANGE);
        teamColors.put("TopazTroopers", Color.YELLOW);
        teamColors.put("KyaniteKillers", Color.LIME);
        teamColors.put("DiamondDestroyers", Color.AQUA);
        teamColors.put("SapphireSoldiers", Color.BLUE);
        teamColors.put("SmithsoniteSlayers", Color.FUCHSIA);
        teamColors.put("CrystalCrashers", Color.WHITE);

        teamChatColours.put("RubyRaiders", "FDABAB");
        teamChatColours.put("AmberAmbushers", "FED37C");
        teamChatColours.put("TopazTroopers", "FFFF92");
        teamChatColours.put("KyaniteKillers", "B5FFB5");
        teamChatColours.put("DiamondDestroyers", "ADFBFB");
        teamChatColours.put("SapphireSoldiers", "9E9EFF");
        teamChatColours.put("SmithsoniteSlayers", "FFA8FF");
        teamChatColours.put("CrystalCrashers", "E2E2D9");

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

        teamConcrete.forEach((team, material) -> concreteConvertTeam.put(material, team));

        concreteList = List.of(Material.RED_CONCRETE, Material.ORANGE_CONCRETE, Material.YELLOW_CONCRETE, Material.LIME_CONCRETE, Material.LIGHT_BLUE_CONCRETE, Material.BLUE_CONCRETE, Material.MAGENTA_CONCRETE, Material.WHITE_CONCRETE);

        woolModes.put(Material.WHITE_WOOL, "Crumble Clash");
        woolModes.put(Material.PURPLE_WOOL, "Push Point");
        woolModes.put(Material.LIME_WOOL, "Sulfur Golf");
        woolModes.put(Material.ORANGE_WOOL, "Zoomo Go!");
        woolModes.put(Material.RED_WOOL, "Bridge Builders");
        woolModes.put(Material.LIGHT_BLUE_WOOL, "Dimension Dash");
        woolModes.put(Material.YELLOW_WOOL, "Craftalot");

        concreteChatColors.put(Material.RED_CONCRETE, "§c");
        concreteChatColors.put(Material.ORANGE_CONCRETE, "§6");
        concreteChatColors.put(Material.YELLOW_CONCRETE, "§e");
        concreteChatColors.put(Material.LIME_CONCRETE, "§a");
        concreteChatColors.put(Material.LIGHT_BLUE_CONCRETE, "§b");
        concreteChatColors.put(Material.BLUE_CONCRETE, "§9");
        concreteChatColors.put(Material.MAGENTA_CONCRETE, "§d");
        concreteChatColors.put(Material.WHITE_CONCRETE, "§f");

        woolColors.put(Material.WHITE_WOOL, Color.WHITE);
        woolColors.put(Material.PURPLE_WOOL, Color.PURPLE);
        woolColors.put(Material.LIME_WOOL, Color.LIME);
        woolColors.put(Material.ORANGE_WOOL, Color.ORANGE);
        woolColors.put(Material.RED_WOOL, Color.RED);
        woolColors.put(Material.LIGHT_BLUE_WOOL, Color.AQUA);
        woolColors.put(Material.YELLOW_WOOL, Color.YELLOW);

        modeColors.put("Crumble Clash", ChatColor.WHITE);
        modeColors.put("Push Point", ChatColor.LIGHT_PURPLE);
        modeColors.put("Sulfur Golf", ChatColor.GREEN);
        modeColors.put("Zoomo Go!", ChatColor.GOLD);
        modeColors.put("Bridge Builders", ChatColor.RED);
        modeColors.put("Dimension Dash", ChatColor.AQUA);
        modeColors.put("Craftalot", ChatColor.YELLOW);

        woolLogos.put(Material.WHITE_WOOL, "\uD83E\uDD6C");
        woolLogos.put(Material.PURPLE_WOOL, "\uD83E\uDED0");
        woolLogos.put(Material.LIME_WOOL, "\uE172");
        woolLogos.put(Material.ORANGE_WOOL, "\uD83E\uDD55");
        woolLogos.put(Material.RED_WOOL, "\uD83C\uDF45");
        woolLogos.put(Material.LIGHT_BLUE_WOOL, "\uD83E\uDD68");
        woolLogos.put(Material.YELLOW_WOOL, "\ue238");

        modeLogos.put("Crumble Clash", "\uD83E\uDD6C");
        modeLogos.put("Push Point", "\uD83E\uDED0");
        modeLogos.put("Slime Golf", "\uE172");
        modeLogos.put("Zoomo Go", "\uD83E\uDD55");
        modeLogos.put("Bridge Builders", "\uD83C\uDF45");
        modeLogos.put("Dimension Dash", "\uD83E\uDD68");
        modeLogos.put("Craftalot", "\ue238");

        teamJump.put("RubyRaiders", new int[] { 247, -19, 659 } );
        teamJump.put("AmberAmbushers", new int[] { 282, -19, 659 });
        teamJump.put("TopazTroopers", new int[] { 317, -19, 659 });
        teamJump.put("KyaniteKillers", new int[] { 352, -19, 659 });
        teamJump.put("DiamondDestroyers", new int[] { 387, -19, 659 });
        teamJump.put("SapphireSoldiers", new int[] { 422, -19, 659 });
        teamJump.put("SmithsoniteSlayers", new int[] { 457, -19, 659 });
        teamJump.put("CrystalCrashers", new int[] { 492, -19, 659 });

        teamNamesFormatted.put("RubyRaiders", "Ruby Raiders");
        teamNamesFormatted.put("AmberAmbushers", "Amber Ambushers");
        teamNamesFormatted.put("TopazTroopers", "Topaz Troopers");
        teamNamesFormatted.put("KyaniteKillers", "Kyanite Killers");
        teamNamesFormatted.put("DiamondDestroyers", "Diamond Destroyers");
        teamNamesFormatted.put("SapphireSoldiers", "Sapphire Soldiers");
        teamNamesFormatted.put("SmithsoniteSlayers", "Smithsonite Slayers");
        teamNamesFormatted.put("CrystalCrashers", "Crystal Crashers");

        getServer().getPluginManager().registerEvents(new DismountEvent(this), this);
        getServer().getPluginManager().registerEvents(new SlimeSplittingEvent(this), this);
        getServer().getPluginManager().registerEvents(new CustomFurnace(this), this);
        getServer().getPluginManager().registerEvents(musicManager, this);
        getServer().getPluginManager().registerEvents(voteFrozenManager, this);
        getServer().getPluginManager().registerEvents(new FireballEvent(this), this);
        getServer().getPluginManager().registerEvents(new ProjectileEvent(this), this);
        getServer().getPluginManager().registerEvents(new PlayerChatEvent(this), this);
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

        saveDefaultConfig();

        String uri = getConfig().getString("mongo.uri");
        String dbName = getConfig().getString("mongo.database");

        mongoManager = new MongoManager();

        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            mongoManager.connect(uri, dbName);
        });

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

        MapsPlayedConfig.setup();
        MapsPlayedConfig.get().options().copyDefaults(true);
        MapsPlayedConfig.save();

        PanConfig.setup();
        PanConfig.get().options().copyDefaults(true);
        PanConfig.save();

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

    public MongoManager getMongoManager() {
        return mongoManager;
    }

    @Override
    public void onDisable() {
        glowingEntities.disable();
        glowingBlocks.disable();

        if (mongoManager != null) {
            mongoManager.disconnect();
        }

        for (BlockDisplay display : blockToDisplay.values()) {
            display.remove();
        }

        for(ItemDisplay wall : mapWalls.keySet()){
            wall.remove();
        }

        for(TextDisplay txt : wallTexts.values()){
            txt.remove();
        }

        for(TextDisplay txt : wallPushersTexts.values()){
            txt.remove();
        }

        for(ItemBox ib : itemBoxes){
            ib.despawn();
        }

        mapWalls.clear();
        wallTexts.clear();
        wallPushersTexts.clear();

        blockToDisplay.clear();

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
        String name = player.getName();

        Player p = e.getPlayer();
        EventPlayer ep = plugin.eventPlayers.get(p.getName());
        if (ep != null) {
            ep.setPlayer(null);
        }

        // Remove frozen state
        voteFrozenManager.getFrozenPlayers().remove(name);

        // Remove display safely
        voteFrozenManager.despawnFrozenBlock(name);

        List<BossBar> bars = bossBars.remove(e.getPlayer().getName());
        if (bars != null) bars.forEach(BossBar::removeAll);

        if(currentMode.equals("Slime Golf")){
            List<Entity> passengers = new ArrayList<>(player.getPassengers());

            for (Entity passenger : passengers) {
                if (passenger instanceof SulfurCube cube) {
                    player.removePassenger(cube);
                }
            }
        }

        // OLD CHAOTIC SULFUR GOLF
//        if (currentMode.equals("Slime Golf")) {
//            String leavingPlayerName = e.getPlayer().getName();
//            String team = PlayerConfig.get().getString("players." + leavingPlayerName + ".team");
//
//            if (!golfTeamCubes.isEmpty() && golfTeamCubes.containsKey(team)) {
//                Entity cube = golfTeamCubes.get(team);
//
//                List<String> teamPlayers = TeamsConfig.get().getStringList("teams." + team + ".players");
//                List<Player> remaining = new ArrayList<>();
//                for (String playerName : teamPlayers) {
//                    if (playerName.equals(leavingPlayerName)) continue;
//                    Player p = Bukkit.getPlayer(playerName);
//                    if (p != null) remaining.add(p);
//                }
//
//                cube.eject();
//                for (Player p : remaining) {
//                    p.leaveVehicle();
//                }
//
//                Entity lastMounted = cube;
//                for (Player p : remaining) {
//                    lastMounted.addPassenger(p);
//                    lastMounted = p;
//                }
//            }
//
//            String leavingPlayerTeam = PlayerConfig.get().getString("players." + leavingPlayerName + ".team");
//
//            if(golfQueues.get(leavingPlayerTeam).getCurrentPlayerName().equals(leavingPlayerName)){
//                golfQueues.get(leavingPlayerTeam).updateQueuePosition();
//            }
//
//        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.getPlayer().sendTitle("§7§oLoading...", "", 0, 10, 0);

        Player joinedPlayer = e.getPlayer();
        EventPlayer ep = plugin.eventPlayers.get(joinedPlayer.getUniqueId());
        if (ep != null) {
            ep.setPlayer(joinedPlayer);
            ep.setPlayerName(joinedPlayer.getName());
        }

        String name = e.getPlayer().getName();

        if(name.equals("Chazzagram")){
            messagePlayer(e.getPlayer(), "DB Connection: " + mongoManager.isDbConnected());
        }



        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "noxesiumapi:noxesium component game " + name + " set noxesium:client_authoritative_riptide_trident");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "noxesiumapi:noxesium component game " + name + " set noxesium:disable_spin_attack_collisions");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "noxesiumapi:noxesium component game " + name + " set noxesium:riptide_pre_charging");
        }, 200L);

        bossBarBgTest();

        if(!firstJoinList.contains(name)){
            Location spawnLocation = new Location(Bukkit.getWorld("build"), 154.5, 142, 586.5, 12, 0);
            e.getPlayer().teleport(spawnLocation);
            firstJoinList.add(name);
        }

        switch(currentMode){
            case "Craftalot":
            case "Bridge Builders":
            case "Colour Dash":
                e.getPlayer().setGameMode(GameMode.SURVIVAL);
            default:
                e.getPlayer().setGameMode(GameMode.ADVENTURE);
        }

        if(plugin.ghostManager.getGhostPlayers().contains(name)){
            e.getPlayer().setGameMode(GameMode.ADVENTURE);
            e.getPlayer().setAllowFlight(true);
            e.getPlayer().setFlying(true);
        }

        if(Objects.equals(currentMode, "Lobby")) {
            Player p = e.getPlayer();
            for (Player p2 : Bukkit.getOnlinePlayers()) {
                p.showPlayer(plugin, p2);
                p2.showPlayer(plugin, p);
            }
        }

        // OLD CHAOTIC SULFUR GOLF CODE
//        if(Objects.equals(currentMode, "Slime Golf")) {
//            String team = PlayerConfig.get().getString("players." + e.getPlayer().getName() + ".team");
//
//            SulfurCube cube = plugin.golfTeamCubes.get(team);
//
//            boolean passengers = false;
//
//            if(cube.getPassengers().isEmpty()){
//                cube.addPassenger(e.getPlayer());
//                return;
//            }
//
//            Entity passenger = cube.getPassengers().getFirst();
//
//            while(!passengers){
//                List<Entity> subPassengers = passenger.getPassengers();
//                if(!subPassengers.isEmpty()){
//                    passenger = subPassengers.getFirst();
//                } else {
//                    passenger.addPassenger(e.getPlayer());
//                    passengers = true;
//                }
//            }
//        }

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

    public void sendAnnouncement(String message){
        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendMessage("""
                                🔗
                                §8
                                §e §e""" + message + """
                                §8
                                """);
        }
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
            modeTeamFullPoints.put(playerTeam, modeTeamFullPoints.get(playerTeam) + multiplyPoints(points));
        }

        if(individual) {
            int currentPoints = PlayerConfig.get().getInt("players." + player + ".points");
            PlayerConfig.get().set("players." + player + ".points", currentPoints+points);
            PlayerConfig.save();
            if(!Objects.equals(currentMode, "Presents")) {
                modePoints.put(player, modePoints.get(player) + points);
                modeFullPoints.put(player, modeFullPoints.get(player) + points);
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
        modeTeamFullPoints.clear();

        for(String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)){
            for(String player : TeamsConfig.get().getStringList("teams." + team + ".players")){
                modeFullPoints.put(player, 0);
            }
            modeTeamFullPoints.put(team, 0);
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

//                        if(name.equals("slimegolftimer")){
//                            for(SulfurGolfQueue golfQueue : golfQueues.values()){
//                                golfQueue.displayQueue();
//                            }
//                        }

                        if((name.equals("craftalot"))){
                            for(Player p : getPlayers()){
                                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(getPlayerCraftlist(p.getName())));
                            }
                        }
                        if(currentMode.equals("Push Point")){
                            if(timeLeft <= 15 && timeLeft > 5){
                                for(Player player : Bukkit.getOnlinePlayers()){
                                    player.sendMessage("§c§lFACTORY OVERLOAD §fin §c" + (timeLeft - 5) + " §fseconds..");
                                }
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 1F);
                            }
//                            if(timeLeft <= 35 && timeLeft > 5){
//                                for(Player player : Bukkit.getOnlinePlayers()){
//                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("§6⚠ Head back to the §6§uSafe Zone§6 at base! ⚠"));
//                                }
//                            }
                            switch(timeLeft){
                                case 35:
                                    for(Player player : Bukkit.getOnlinePlayers()){
                                        player.sendTitle("§c§lFACTORY OVERLOAD", "in 30 seconds..", 0, 20, 0);
                                        messagePlayer(player, """
                                            §8
                                            §8
                                            §7[§a!§7] §a§lFINAL PUSH ACTIVATED!
                                            §fWall speed is increased!
                                            §8
                                            """);
                                    }
                                    finalPush = true;
                                    targetTime = 5;
                                    timerLabel = "Factory Overload:";
//                                    playSoundAll(Sound.ENTITY_ENDER_DRAGON_DEATH, 1F);
                                    break;
                                case 20:
                                    for(Player player : Bukkit.getOnlinePlayers()){
                                        player.sendTitle("§c§lFACTORY OVERLOAD", "in 15 seconds..", 0, 40, 0);
                                    }
                                    playSoundAll(Sound.BLOCK_ANVIL_FALL, 1F);
                                    break;
                                case 5:
                                    playSoundAll(Sound.BLOCK_ANVIL_FALL, 1F);
                                    List<Player> escapedPlayers = new ArrayList<>();
                                    for(List<Player> players : ppEscapedPlayers.values()) {
                                        escapedPlayers.addAll(players);
                                    }
                                    for(Player p : getPlayers()){
                                        p.setGameMode(GameMode.SPECTATOR);
                                        p.closeInventory();
                                        p.sendTitle("§c§lFACTORY OVERLOADED", "§f§oCalculating Percentages!", 0, 40, 20);
                                    }
                                    targetTime = 0;
                                    timerLabel = "Game End:";
                                    break;
                                default:
                                    break;
                            }
                        }
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
                                    targetTime = 180;
                                    thunderStormEvent();
                                    break;
                                case 180:
                                    targetTime = 0;
                                    timerLabel = "Game End:";
                                    honeyIShrunkTheRosterEvent();
                                    break;
                                default:
                                    break;
                            }
                        }
                        if(currentMode.equals("Push Point") && finaleActive && timeLeft == 0){

                            finalePushPointOver();

                            runningTimers.remove(name);
                            cancel();
                        } else {
                            if (timeLeft == 30 && !currentMode.equals("Push Point")) {
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
            int timeElapsed = 0;
            @Override
            public void run() {
                if (runningTimers.containsKey(name)) {
                    if (!pausedTimers.contains(name)) {
                        runningTimers.get(name).setValue(timeElapsed);
                        if (name.equals("slimegolf")) {
                            textDisplay.setText(getFullTimer("slimegolf"));
                        }
                        if (timeElapsed == seconds) {
                            messageConsole("Timer finished.");
                            runningTimers.remove(name);
                            cancel();
                        }
                        timeElapsed++;
                    }
                } else {
                    messageConsole("Stopwatch removed by external factor.");
                    cancel();
                }
            }
        }.runTaskTimer(this, 1L, 20L);

        runningTimers.put(name, new AbstractMap.SimpleEntry<>(task, 0));
    }

    public String getTimer(String timer) {
        return LocalTime.of(0, (runningTimers.get(timer).getValue() - targetTime) / 60, (runningTimers.get(timer).getValue() - targetTime) % 60).format(DateTimeFormatter.ofPattern("mm:ss"));
    }

    public String getFullTimer(String timer) {
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
                            if(location.equals("pushpoint")){
                                teleportTeamsToCurrentRound();
                            } else {
                                for (Player player : getPlayers()) {
                                    Location tplocation = TeleportConfig.get().getLocation("teams." + PlayerConfig.get().getString("players." + player.getName() + ".team") + "." + location);
                                    player.teleport(tplocation);
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
                                Location tplocation = TeleportConfig.get().getLocation("players." + location);
                                if(Bukkit.getPlayer(player) != null){
                                    Player p = Bukkit.getPlayer(player);
                                    p.teleport(tplocation);
                                }
                            }
                            for(String player : secondPlayers){
                                Location tplocation = TeleportConfig.get().getLocation("players." + location);
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

    public void finaleTeamTeleportSeparate(String location, int countdown){
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
                                Location tplocation = TeleportConfig.get().getLocation("players." + location + "1");
                                if(Bukkit.getPlayer(player) != null){
                                    Player p = Bukkit.getPlayer(player);
                                    p.teleport(tplocation);
                                }
                            }
                            for(String player : secondPlayers){
                                Location tplocation = TeleportConfig.get().getLocation("players." + location + "2");
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

        return "§r" + TeamsConfig.get().getString("teams." + team + ".icon") + TeamsConfig.get().getString("teams." + team + ".colour") + "§l" + team;
    }

    public String getTeamNameFromDisplay(String displayName) {

        return switch (displayName) {
            case "§r❶§c§lRubyRaiders" -> "RubyRaiders";
            case "§r❷§6§lAmberAmbushers" -> "AmberAmbushers";
            case "§r❸§e§lTopazTroopers" -> "TopazTroopers";
            case "§r❹§a§lKyaniteKillers" -> "KyaniteKillers";
            case "§r❺§b§lDiamondDestroyers" -> "DiamondDestroyers";
            case "§r❻§9§lSapphireSoldiers" -> "SapphireSoldiers";
            case "§r❼§d§lSmithsoniteSlayers" -> "SmithsoniteSlayers";
            case "§r❽§f§lCrystalCrashers" -> "CrystalCrashers";
            default -> "";
        };

    }

    public String getPlayerDisplayName(String player) {
        if (PlayerConfig.get().getConfigurationSection("players").contains(player)) {
            String team = PlayerConfig.get().getString("players." + player + ".team");
            return "§r" + TeamsConfig.get().getString("teams." + team + ".icon") + TeamsConfig.get().getString("teams." + team + ".colour") + player;
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

    public void replaceSlimeCommands(int[][] slimeCmdCoords, int[][] slimeCmdCoords2, String find, String replace) {
        World world = Bukkit.getWorld("build");
        int updatedCount = 0;

        // Round 1 coords
        for (int[] coord : slimeCmdCoords) {
            for (int i = 0; i <= 1540; i += 220) {
                int cmdX = coord[0];
                int cmdY = coord[1];
                int cmdZ = coord[2] + i + 1; // +1 to get the actual command block

                Block block = world.getBlockAt(cmdX, cmdY, cmdZ);
                BlockState state = block.getState();

                if (state instanceof CommandBlock cmdBlock) {
                    String command = cmdBlock.getCommand();

                    if (command.contains(find)) {
                        cmdBlock.setCommand(command.replace(find, replace));
                        cmdBlock.update();
                        updatedCount++;
                    }
                }
            }
        }

        for (int[] coord : slimeCmdCoords2) {
            for (int i = 0; i <= 1092; i += 156) {
                int cmdX = coord[0];
                int cmdY = coord[1];
                int cmdZ = coord[2] + i + 1;

                Block block = world.getBlockAt(cmdX, cmdY, cmdZ);
                BlockState state = block.getState();

                if (state instanceof CommandBlock cmdBlock) {
                    String command = cmdBlock.getCommand();

                    if (command.contains(find)) {
                        cmdBlock.setCommand(command.replace(find, replace));
                        cmdBlock.update();
                        updatedCount++;
                    }
                }
            }
        }

        getLogger().info("Updated " + updatedCount + " command block(s).");
    }


    public void startSlimeGolf(){
        pvpArenaManager.disablePvPArena();
        multiplier = GameOrderConfig.get().getDouble("multiplier");
        fillVotingSpace(3);
        musicManager.stopMusicAll();
        plugin.shopAllowed = false;
        clearInventories();
        if(currentRound == 1){
            setPreviousPlacements();
            resetModeFullPoints();
        }
        if(currentRound == 1) {
            targetTime = 0;
            timerLabel = "Game Explanation:";
        } else {
            targetTime = 10;
            timerLabel = "Exploration Phase:";
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
                                if(currentRound == 1) {
                                    ItemStack pumpkin = new ItemStack(Material.CARVED_PUMPKIN);
                                    player.getInventory().clear();
                                    player.getInventory().setHelmet(pumpkin);
                                }
                                ghostManager.removeGhostPlayer(player.getName());
                            }
                            if(currentRound > 1){
                                timeLeft = 26;
                            }
                            if(currentRound == 1) {
                                startCustomPan("slime1");
                            }
                            break;
                        case 52:
                            if(currentRound == 1) {
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendTitle("\uE172", "", 20, 60, 20);
                                }
                                playSoundAll(Sound.ENTITY_ARMADILLO_LAND, 1F);
                            }
                            break;
                        case 49:
                            playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §r⏳ §eWelcome to §7§lSulfur Golf§e! The aim of the game is to hit your bouncy ball into the hole at the end of the course as quickly as possible!
                                        §8
                                        """);
                            }
                            break;
                        case 41:
                            if(currentRound == 1) {
                                startCustomPan("slime2");
                            }
                            break;
                        case 36:
                            playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                messagePlayer(player, """
                                        §8
                                        §8
                                        §r⏳ §eUse your §aPutter §eto fling your sulfur cube across the course! Each of your team will be given a putter! §cRight click §ethe Sulfur Cube to pick it up. Hold §cright-click §eto charge your putter, the longer you hold the farther you go!
                                        §8
                                        """);
//                                                                        §r⏳ §eUse your §aknockback stick §eand work as a team, jump ahead and plan out your strategy, player-sized shortcuts will help you get ahead of the slime for strategic putting strategies!
                            }
                            break;
                        case 27:
                            if(currentRound == 1) {
                                startCustomPan("slime3");
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
                                            §r⏳ §a§lSlime Golf§e! This is the §b§lMODIFIER ROUND§e, so watch out because anything can happen! Get your sulfur cube in the hole as fast as you can...
                                            §8
                                            """);
                                }
                            }
                            break;
                        case 13:
                            if(currentRound == 1) {
                                teamTeleport("slimegolf", 0);
                                teleportSpectators(TeleportConfig.get().getLocation("spectators.slimegolf"), 0);
                                for(Player player : getPlayers()){
                                    ItemStack air = new ItemStack(Material.AIR);
                                    player.getInventory().clear();
                                    player.getInventory().setHelmet(air);
                                    player.setGameMode(GameMode.ADVENTURE);
                                }
                            } else {
                                teamTeleport("slimegolf2", 0);
                            }
//                            ItemStack knockbackStick = new ItemStack(Material.STICK);
//                            knockbackStick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);
//
//                            ItemStack fishingRod = new ItemStack(Material.FISHING_ROD);
//                            fishingRod.addUnsafeEnchantment(Enchantment.UNBREAKING, 3);
//                            for (Player player : getPlayers()) {
//                                player.getInventory().addItem(knockbackStick);
//                                player.getInventory().addItem(fishingRod);
//                            }
                            ItemStack stack = new ItemStack(Material.BOW);
                            ItemMeta meta = stack.getItemMeta();
                            meta.setItemModel(new NamespacedKey("amongus", "slingshot"));
                            meta.setDisplayName("§a§lPutter!");
                            meta.addEnchant(Enchantment.INFINITY, 1, true);
                            stack.setItemMeta(meta);

                            ItemStack arrow = new ItemStack(Material.ARROW);
                            ItemMeta arrowmeta = arrow.getItemMeta();
                            arrowmeta.setDisplayName("§f§lARROW!");
                            arrow.setItemMeta(arrowmeta);
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.getInventory().setItem(35, arrow);
                                player.getInventory().addItem(stack);
                            }
                            break;
                        case 10:
                            if(currentRound > 1){
                                targetTime = 0;
                                timerLabel = "Game Starting:";
                            }
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
                            for(Player p : Bukkit.getOnlinePlayers()){
                                messagePlayer(p, """
                                        §8
                                        §8
                                        §8[§c§l!§8] §7Game Starting in §c§l10 seconds...
                                        §8
                                        """);
                            }
//                            for(String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
//                                SulfurGolfQueue golfQueue = new SulfurGolfQueue(plugin, team);
//                                if (!TeamsConfig.get().getStringList("teams." + team + ".players").isEmpty()){
//                                    for (String teamPlayer : TeamsConfig.get().getStringList("teams." + team + ".players")) {
//                                        golfQueue.addPlayer(teamPlayer);
//                                    }
//                                    golfQueues.put(team, golfQueue);
//                                }
//                            }
                            break;
                        case 5, 4, 3, 2, 1:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 0.3F*(6-timeLeft));
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.sendTitle("§c§l▶ " + timeLeft + " ◀", "", 0, 20, 20);
                            }
                            break;
                        case 0:
                            for(SulfurGolfQueue golfQueue : golfQueues.values()){
                                golfQueue.startQueue();
                            }
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
                            int index = 0;
                            if(currentRound == 1) {
                                Location slimeCoords = new Location(Bukkit.getWorld("build"), 809, 59, 1369);
                                for(int z = 1369; z <= 2909; z+=220){
                                    slimeCoords.setZ(z);
                                    SulfurCube newSlime = (SulfurCube) Bukkit.getWorld("build").spawnEntity(slimeCoords, EntityType.SULFUR_CUBE);
//                                    Slime newSlime = (Slime) Bukkit.getWorld("build").spawnEntity(slimeCoords, EntityType.SLIME);
                                    newSlime.addPotionEffect(new PotionEffect(
                                            PotionEffectType.RESISTANCE,
                                            Integer.MAX_VALUE,
                                            255,
                                            false,
                                            false,
                                            false
                                    ));
                                    newSlime.setSize(3);
                                    newSlime.setRemoveWhenFarAway(true);
                                    newSlime.setPersistent(true);

                                    EntityEquipment equipment = newSlime.getEquipment();
                                    if (equipment != null) {
                                        equipment.setItem(EquipmentSlot.BODY, new ItemStack(concreteList.get(index)));
                                    }

                                    slimeGolfSlime.add(newSlime);
                                    index++;
                                }

                            } else {
                                Location slimeCoords = new Location(Bukkit.getWorld("build"), 847, 77, 3180);
                                for(int z = 3180; z <= 4272; z+=156){
                                    slimeCoords.setZ(z);
                                    SulfurCube newSlime = (SulfurCube) Bukkit.getWorld("build").spawnEntity(slimeCoords, EntityType.SULFUR_CUBE);
//                                    Slime newSlime = (Slime) Bukkit.getWorld("build").spawnEntity(slimeCoords, EntityType.SLIME);
                                    newSlime.addPotionEffect(new PotionEffect(
                                            PotionEffectType.RESISTANCE,
                                            Integer.MAX_VALUE,
                                            255,
                                            false,
                                            false,
                                            false
                                    ));
                                    newSlime.setSize(3);
                                    newSlime.setRemoveWhenFarAway(true);
                                    newSlime.setPersistent(true);

                                    EntityEquipment equipment = newSlime.getEquipment();
                                    if (equipment != null) {
                                        equipment.setItem(EquipmentSlot.BODY, new ItemStack(concreteList.get(index)));
                                    }

                                    slimeGolfSlime.add(newSlime);
                                    index++;
                                }
                            }

                            int teamIndex = 0;
                            for(String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)){
                                golfTeamCubes.put(team, slimeGolfSlime.get(teamIndex));
//                                if(TeamsConfig.get().getStringList("teams." + team + ".players").isEmpty()) continue;
//                                List<String> teamPlayers = TeamsConfig.get().getStringList("teams." + team + ".players");
//                                for(int i = 0; i < TeamsConfig.get().getStringList("teams." + team + ".players").size(); i++){
//                                    Player p = Bukkit.getPlayer(teamPlayers.get(i));
//                                    if(i == 0) {
//                                        if (p != null) {
//                                            slimeGolfSlime.get(teamIndex).addPassenger(p);
//                                        }
//                                    } else {
//                                        Player p2 = Bukkit.getPlayer(teamPlayers.get(i-1));
//                                        if (p != null && p2 != null) {
//                                            p2.addPassenger(p);
//                                        }
//                                    }
//                                }
                                teamIndex++;
                            }

                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 2);
                            playMusicAll(Sound.MUSIC_DISC_CAT);
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                PotionEffect PotionEffect = new PotionEffect(PotionEffectType.RESISTANCE, 12000, 1, false, false);
                                player.addPotionEffect(PotionEffect);
                                player.sendTitle("§a§l▶ GO! ◀", "", 0, 40, 0);
                            }
                            if(currentRound == 3){
                                targetTime = 270;
                                timerLabel = "Next Modifier:";
                            } else {
                                targetTime = 0;
                                timerLabel = "Game End:";
                            }

                            slimeGolfStartTime = System.currentTimeMillis();

                            startTimer(300, "slimegolftimer");
                            startStopwatch(300, "slimegolf");
//                            pvpEnabled = true;
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

    private void setupSlimeTeam() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getMainScoreboard();

        slimeTeam = scoreboard.getTeam("slime_noclip");
        if (slimeTeam == null) {
            slimeTeam = scoreboard.registerNewTeam("slime_noclip");
        }

        slimeTeam.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
    }

    private void addToNoCollideTeam(SulfurCube slime) {
        slime.setCollidable(false);
    }


    public void startSlimeGolfFinale(){

        setupSlimeTeam();
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

                            for(Player p : getPlayers()){
                                if(!secondPlayers.contains(p.getName()) && !firstPlayers.contains(p.getName())){
                                    ghostManager.addGhostPlayer(p.getName());
                                }
                            }

                            for (Player player : getPlayers()) {
                                if(firstPlayers.contains(player.getName()) || secondPlayers.contains(player.getName())) {
                                    PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 90, 1, false, false);
                                    player.addPotionEffect(PotionEffect);
                                    player.setGameMode(GameMode.SURVIVAL);
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
                                player.sendTitle("§a§lFirst to", "§a§lFinish.", 0, 40, 20);
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
//                            ItemStack knockbackStick = new ItemStack(Material.STICK);
//                            knockbackStick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);
//
//                            finaleTeamTeleport("slimegolffinale", 0);
//
//                            ItemStack fishingRod = new ItemStack(Material.FISHING_ROD);
//                            fishingRod.addUnsafeEnchantment(Enchantment.UNBREAKING, 3);
//                            for (Player player : getPlayers()) {
//                                if(secondPlayers.contains(player.getName()) || firstPlayers.contains(player.getName())) {
//                                    player.getInventory().addItem(knockbackStick);
//                                    player.getInventory().addItem(fishingRod);
//                                }
//                            }
                            ItemStack stack = new ItemStack(Material.BOW);
                            ItemMeta meta = stack.getItemMeta();
                            meta.setItemModel(new NamespacedKey("amongus", "slingshot"));
                            meta.setDisplayName("§a§lPutter!");
                            meta.addEnchant(Enchantment.INFINITY, 1, true);
                            stack.setItemMeta(meta);

                            ItemStack arrow = new ItemStack(Material.ARROW);
                            ItemMeta arrowmeta = arrow.getItemMeta();
                            arrowmeta.setDisplayName("§f§lARROW!");
                            arrow.setItemMeta(arrowmeta);
                            for (Player player : getPlayers()) {
                                if(secondPlayers.contains(player.getName()) || firstPlayers.contains(player.getName())) {
                                    player.getInventory().setItem(35, arrow);
                                    player.getInventory().addItem(stack);
                                }
                            }
                            for(Player p : Bukkit.getOnlinePlayers()){
                                messagePlayer(p, """
                                        §8
                                        §8
                                        §8[§c§l!§8] §7Game Starting in §c§l10 seconds...
                                        §8
                                        """);
                            }

                            Player playerTeam1, playerTeam2;
                            for(String player : firstPlayers){
                                for(String player2 : secondPlayers){
                                    if(Bukkit.getPlayer(player) != null && Bukkit.getPlayer(player2) != null){
                                        playerTeam1 = Bukkit.getPlayer(player);
                                        playerTeam2 = Bukkit.getPlayer(player2);
                                        playerTeam1.hidePlayer(plugin, playerTeam2);
                                        playerTeam2.hidePlayer(plugin, playerTeam1);
                                    }
                                }
                            }
                            break;
                        case 5, 4, 3, 2, 1:
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 0.3F*(6-timeLeft));
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.sendTitle("§c§l▶ " + timeLeft + " ◀", "", 0, 20, 20);
                            }
                            break;
                        case 0:

                            Location slimeCoords = new Location(Bukkit.getWorld("build"), 2300, 80, 458);
                            SulfurCube slime1 = (SulfurCube) Bukkit.getWorld("build").spawnEntity(slimeCoords, EntityType.SULFUR_CUBE);

//                            Slime slime1 = (Slime) Bukkit.getWorld("build").spawnEntity(slimeCoords, EntityType.SLIME);
                            slime1.addPotionEffect(new PotionEffect(
                                        PotionEffectType.RESISTANCE,
                                        Integer.MAX_VALUE,
                                        255,
                                        false,
                                        false,
                                        false
                                ));
                            slime1.setCustomName(getTeamDisplayName(firstTeam));
                            slime1.setCustomNameVisible(true);
                            slime1.setSize(3);
                            slime1.setRemoveWhenFarAway(true);
                            slime1.setPersistent(true);
                            slime1.addScoreboardTag(firstTeam + "_slime");

                            EntityEquipment equipment = slime1.getEquipment();
                            if (equipment != null) {
                                equipment.setItem(EquipmentSlot.BODY, new ItemStack(teamConcrete.get(firstTeam)));
                            }

                            slimeGolfSlime.add(slime1);


                            for(String player : secondPlayers){
                                if(Bukkit.getPlayer(player) != null){
                                    Player p = Bukkit.getPlayer(player);
                                    p.hideEntity(plugin, slime1);
                                }
                            }

                            for(Player player : Bukkit.getOnlinePlayers()){
                                if(!secondPlayers.contains(player.getName())){
                                    try {
                                        glowingEntities.setGlowing(slime1, player, teamGlowColors.get(firstTeam));
                                    } catch (ReflectiveOperationException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                            SulfurCube slime2 = (SulfurCube) Bukkit.getWorld("build").spawnEntity(slimeCoords, EntityType.SULFUR_CUBE);

//                            Slime slime2 = (Slime) Bukkit.getWorld("build").spawnEntity(slimeCoords, EntityType.SLIME);
                            slime2.addPotionEffect(new PotionEffect(
                                    PotionEffectType.RESISTANCE,
                                    Integer.MAX_VALUE,
                                    255,
                                    false,
                                    false,
                                    false
                            ));
                            slime2.setCustomName(getTeamDisplayName(secondTeam));
                            slime2.setCustomNameVisible(true);
                            slime2.setSize(3);
                            slime2.setRemoveWhenFarAway(true);
                            slime2.setPersistent(true);
                            slime2.addScoreboardTag(secondTeam + "_slime");

                            EntityEquipment equipment2 = slime2.getEquipment();
                            if (equipment2 != null) {
                                equipment2.setItem(EquipmentSlot.BODY, new ItemStack(teamConcrete.get(secondTeam)));
                            }

                            slimeGolfSlime.add(slime2);

                            for(String player : firstPlayers){
                                if(Bukkit.getPlayer(player) != null){
                                    Player p = Bukkit.getPlayer(player);
                                    p.hideEntity(plugin, slime2);
                                }
                            }

                            for(Player player : Bukkit.getOnlinePlayers()){
                                if(!firstPlayers.contains(player.getName())){
                                    try {
                                        glowingEntities.setGlowing(slime2, player, teamGlowColors.get(secondTeam));
                                    } catch (ReflectiveOperationException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }

                            finaleSlimes.put(slime1, firstTeam);
                            finaleSlimes.put(slime2, secondTeam);

                            addToNoCollideTeam(slime1);
                            addToNoCollideTeam(slime2);

                            Location goalCoords = new Location(Bukkit.getWorld("build"), 2678.5, 48, 448);
                            goal = Bukkit.getWorld("build").spawn(goalCoords, Interaction.class);

                            goal.setInteractionWidth(15);
                            goal.setInteractionHeight(2);

                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 2);
                            playMusicAll(Sound.MUSIC_DISC_CAT);
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                PotionEffect PotionEffect = new PotionEffect(PotionEffectType.RESISTANCE, 12000, 1, false, false);
                                player.addPotionEffect(PotionEffect);
                                player.sendTitle("§a§l▶ GO! ◀", "", 0, 40, 0);
                            }
//                            startSlimeFinaleCheck();

                            for (int y = 40; y <= 47; y++) {
                                Bukkit.getWorld("build").getBlockAt(2671, y, 440).setType(Material.REDSTONE_BLOCK);
                            }

                            ghostManager.giveCompasses();

                            slimeGolfStartTime = System.currentTimeMillis();

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
        slimeGolfSlime.clear();
        golfTeamCubes.clear();
        golfQueues.clear();
        for (int i = 1; i <= 6; i++) {
            slimeCheckpoints.put(i, 1);
        }
    }

    public void resetColourDash(){
        colourDashCheckpoints.clear();

        for(String player : PlayerConfig.get().getConfigurationSection("players").getKeys(false)){
            colourDashCheckpoints.put(player, 0);
        }
        cdCompletions = 0;

        unfinishedPlayers.clear();

        unfinishedPlayers.addAll(Bukkit.getOnlinePlayers());
    }

//    public void startColourDash(){
//        pvpArenaManager.disablePvPArena();
//        fillVotingSpace(4);
//        plugin.shopAllowed = false;
//        clearInventories();
//        if(currentRound == 1){
//            setPreviousPlacements();
//            resetModeFullPoints();
//        }
//        World world = Bukkit.getWorld("build");
//        Block block;
//        for(int x = 137; x >= -27; x--){
//            for(int y = 195; y >= 127; y--){
//                for(int z = 804; z <= 1345; z++){
//                    block = world.getBlockAt(x,y,z);
//                    if(colourDashBlocks.contains(block.getType())){
//                        block.setType(Material.AIR);
//                    }
//                }
//            }
//        }
//        BukkitTask task = new BukkitRunnable() {
//            int timeLeft = 61;
//            @Override
//            public void run() {
//                if(runningTimers.containsKey("colourdashstart")) {
//                    if (!pausedTimers.contains("colourdashstart")) {
//                        timeLeft--;
//                        runningTimers.get("colourdashstart").setValue(timeLeft);
//                        bossBarBgTest();
//                        switch (timeLeft) {
//                            case 60:
//                                if(plugin.currentRound == 1) {
//                                    Bukkit.getWorld("build").getBlockAt(121, 139, 790).setType(Material.REDSTONE_BLOCK);
//                                    Bukkit.getWorld("build").getBlockAt(121, 139, 790).setType(Material.AIR);
//                                    teleportPlayers(TeleportConfig.get().getLocation("players.colourdash"), 5);
//                                    teleportSpectators(TeleportConfig.get().getLocation("spectators.colourdash"), 5);
//                                }
//                                if(plugin.currentRound == 2){
//                                    Bukkit.getWorld("build").getBlockAt(75, 175, 1331).setType(Material.REDSTONE_BLOCK);
//                                    Bukkit.getWorld("build").getBlockAt(75, 175, 1331).setType(Material.AIR);
//                                    teleportPlayers(TeleportConfig.get().getLocation("players.colourdash2"), 5);
//                                    teleportSpectators(TeleportConfig.get().getLocation("spectators.colourdash2"), 5);
//                                }
//                                resetColourDash();
//                                resetTeamCompletions();
//                                resetModePoints();
//                                break;
//                            case 55:
//                                try {
//                                    glowTeams();
//                                } catch (ReflectiveOperationException e) {
//                                    throw new RuntimeException(e);
//                                }
//                                currentMode = "Colour Dash";
//                                addToGameOrder(currentMode);
//                                for(Player p : Bukkit.getOnlinePlayers()){
//                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "usc setScoreboard " + p.getName() + " ColourDash");
//                                }
//                                for (Player player : getPlayers()) {
//                                    PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 90, 1, false, false);
//                                    player.addPotionEffect(PotionEffect);
//                                    player.setGameMode(GameMode.SURVIVAL);
//                                }
//                                break;
//                            case 50:
//                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
//                                if(plugin.currentRound == 1) {
//                                    for (Player player : Bukkit.getOnlinePlayers()) {
//                                        messagePlayer(player, """
//                                                §8
//                                                §8
//                                                §r⏳ §eWelcome to §a§lColour Dash§e! This is a race to the finish, the map is bigger, and there's multiple routes for your team to take so make the right choice!
//                                                §8
//                                                """);
//                                    }
//                                }
//                                if(plugin.currentRound == 2){
//                                    for (Player player : Bukkit.getOnlinePlayers()) {
//                                        messagePlayer(player, """
//                                            §8
//                                            §8
//                                            §r⏳ §eWelcome to §a§lColour Dash §c§lʀᴏᴜɴᴅ ᴛᴡᴏ§e! Now you know the course.. or do you? This time we're going backwards on a different route! Good luck!
//                                            §8
//                                            """);
//                                    }
//                                    timeLeft = 16;
//                                }
//                                break;
//                            case 30:
//                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
//                                for (Player player : Bukkit.getOnlinePlayers()) {
//                                    messagePlayer(player, """
//                                            §8
//                                            §8
//                                            §r⏳ §eIn this mode speed is the most important factor! The faster you reach each checkpoint, the more points you earn, so get dashing!
//                                            §8
//                                            """);
//                                }
//                                break;
//                            case 10:
//                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
//                                for (Player player : Bukkit.getOnlinePlayers()) {
//                                    messagePlayer(player, """
//                                            §8
//                                            §8
//                                            §8[§c§l!§8] §7Game Starting in §c§l10 seconds...
//                                            §8
//                                            """);
//                                }
//                                break;
//                            case 5, 4, 3, 2, 1:
//                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 0.3F * (6 - timeLeft));
//                                for (Player player : Bukkit.getOnlinePlayers()) {
//                                    player.sendTitle("§c§l▶ " + timeLeft + " ◀", "", 0, 20, 20);
//                                }
//                                break;
//                            case 0:
//                                if(plugin.currentRound == 1) {
//                                    Bukkit.getWorld("build").getBlockAt(121, 139, 791).setType(Material.REDSTONE_BLOCK);
//                                    Bukkit.getWorld("build").getBlockAt(121, 139, 791).setType(Material.AIR);
//                                }
//                                if(plugin.currentRound == 2){
//                                    Bukkit.getWorld("build").getBlockAt(75, 174, 1331).setType(Material.REDSTONE_BLOCK);
//                                    Bukkit.getWorld("build").getBlockAt(75, 174, 1331).setType(Material.AIR);
//                                }
//                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 2);
//                                playMusicAll(Sound.MUSIC_DISC_BLOCKS);
//                                pvpEnabled = true;
//                                for (Player player : getPlayers()) {
//                                    PotionEffect PotionEffect = new PotionEffect(PotionEffectType.RESISTANCE, 12000, 1, false, false);
//                                    player.addPotionEffect(PotionEffect);
//                                    ItemStack infiniteBlocks = new ItemStack(Material.getMaterial(TeamsConfig.get().getString("teams." + PlayerConfig.get().getString("players." + player.getName() + ".team") + ".colourname") + "_CONCRETE"));
//                                    infiniteBlocks.setAmount(64);
//                                    player.getInventory().addItem(infiniteBlocks);
//
//                                    ItemStack pickaxe = new ItemStack(Material.IRON_PICKAXE);
//                                    pickaxe.addEnchantment(Enchantment.UNBREAKING, 3);
//                                    pickaxe.addEnchantment(Enchantment.EFFICIENCY, 1);
//                                    player.getInventory().addItem(pickaxe);
//                                    player.sendTitle("§a§l▶ DASH! ◀", "", 0, 40, 0);
//                                }
//                                startTimer(540, "colourdash");
//                                startStopwatch(540  , "colourdashwatch");
//                                runningTimers.remove("colourdashstart");
//                                cancel();
//                                break;
//                            default:
//                                break;
//                        }
//                    }
//                } else {
//                    cancel();
//                }
//            }
//
//        }.runTaskTimer(this, 0L, 20L);
//
//        runningTimers.put("colourdashstart", new AbstractMap.SimpleEntry<>(task, 61));
//    }

    public List<Integer> ddPoints() {
        return Arrays.asList(
                306, 260, 240, 234, 228, 212, 199, 186,
                173, 163, 147, 143, 140, 137, 130, 124,
                117, 111, 104, 98, 85, 78, 72, 65,
                59, 52, 46, 39, 36, 33, 30, 26
        );
    }

    public List<Integer> slimePoints() {
        return Arrays.asList(
                568, 472, 400, 352, 328, 304, 280, 256
        );
    }

    public void resetDDLapTimes() {
        ddStartTime = 0;

        dashLapData.clear();

        for(Player player : getPlayers()){
            DashTimeData playerData = new DashTimeData(player.getName(), plugin);
            dashLapData.put(player.getName(), playerData);
        }
    }

    public void startDimensionDash(){
        pvpArenaManager.disablePvPArena();
        multiplier = GameOrderConfig.get().getDouble("multiplier");
        fillVotingSpace(4);
        musicManager.stopMusicAll();
        plugin.shopAllowed = false;
        clearInventories();
        if(currentRound == 1){
            resetDDMapsConfig();
            setPreviousPlacements();
            resetModeFullPoints();

            MapsPlayedConfig.get().set("maps.Dimension Dash", new ArrayList<>());
            MapsPlayedConfig.save();
        }
        World world = Bukkit.getWorld("build");

        killTextDisplaysInArea(new Location(world, 37, 144, 1090), new Location(world, 84, 165, 1160));

        world.getBlockAt(90, 149, 920).setType(Material.REDSTONE_BLOCK);
        world.getBlockAt(90, 149, 920).setType(Material.AIR);

        if(currentRound == 2){
            world.getBlockAt(60, 156, 1251).setType(Material.REDSTONE_BLOCK);
            world.getBlockAt(60, 156, 1251).setType(Material.AIR);

            // Castle Elytra Barriers
            world.getBlockAt(39, 156, 1214).setType(Material.REDSTONE_BLOCK);
            world.getBlockAt(39, 156, 1214).setType(Material.AIR);

            world.getBlockAt(94, 159, 1214).setType(Material.REDSTONE_BLOCK);
            world.getBlockAt(94, 159, 1214).setType(Material.AIR);
        }

        world.getBlockAt(60, 137, 1077).setType(Material.REDSTONE_BLOCK);
        world.getBlockAt(60, 137, 1077).setType(Material.AIR);

        world.getBlockAt(60, 137, 1174).setType(Material.REDSTONE_BLOCK);
        world.getBlockAt(60, 137, 1174).setType(Material.AIR);
        ddSelectedMaps.clear();
        ddSelectedMaps.addAll(ddMapBlocks.keySet());
        if(currentRound == 2){
            ddSelectedMaps.remove(ddChosenMap);
        }
        ddChosenMap = "";
        Random rand = new Random();
        String map1 = ddSelectedMaps.get(rand.nextInt(ddSelectedMaps.size()));
        ddSelectedMaps.remove(map1);
        String map2 = ddSelectedMaps.get(rand.nextInt(ddSelectedMaps.size()));
        targetTime = 0;
        if(currentRound == 1) {
            timerLabel = "Game Explanation:";
        } else {
            timerLabel = "Starting Round:";
        }
        resetDDLapTimes();
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 61;
            @Override
            public void run() {
                if(runningTimers.containsKey("dimensiondashstart")) {
                    if (!pausedTimers.contains("dimensiondashstart")) {
                        timeLeft--;
                        runningTimers.get("dimensiondashstart").setValue(timeLeft);
                        bossBarBgTest();
                        switch (timeLeft) {
                            case 60:
                                if(plugin.currentRound == 1) {
                                    Bukkit.getWorld("build").getBlockAt(62, 186, 875).setType(Material.REDSTONE_BLOCK);
                                    Bukkit.getWorld("build").getBlockAt(62, 186, 875).setType(Material.AIR);
                                    teleportPlayers(TeleportConfig.get().getLocation("players.dimensiondash"), 5);
                                    teleportSpectators(TeleportConfig.get().getLocation("spectators.dimensiondash"), 5);
                                }
                                if(plugin.currentRound == 2){
                                    Bukkit.getWorld("build").getBlockAt(75, 175, 1331).setType(Material.REDSTONE_BLOCK);
                                    Bukkit.getWorld("build").getBlockAt(75, 175, 1331).setType(Material.AIR);
                                    teleportPlayers(TeleportConfig.get().getLocation("players.dimensiondash2"), 5);
                                    teleportSpectators(TeleportConfig.get().getLocation("spectators.dimensiondash2"), 5);
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
                                currentMode = "Dimension Dash";
                                addToGameOrder(currentMode);
                                for (Player player : getPlayers()) {
                                    if(currentRound == 1) {
                                        ItemStack pumpkin = new ItemStack(Material.CARVED_PUMPKIN);
                                        player.getInventory().clear();
                                        player.getInventory().setHelmet(pumpkin);
                                    }
                                    ghostManager.removeGhostPlayer(player.getName());
                                }
                                if(currentRound == 1) {
                                    startCustomPan("dd1");
                                }
                                break;
                            case 52:
                                if(currentRound == 1) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        player.sendTitle("\uD83E\uDD68", "", 20, 60, 20);
                                    }
                                    playSoundAll(Sound.ENTITY_ARMADILLO_LAND, 1F);
                                }
                                break;
                            case 49:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                if(currentRound == 1){
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        messagePlayer(player, """
                                                §8
                                                §8
                                                §r⏳ §eWelcome to §d§l§oDimension Dash§e! This is a race to the finish, travel across dimensions and race to the finish line.
                                                §8
                                                """);
                                    }
                                } else if (currentRound == 2){
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §eWelcome to §d§l§oDimension Dash §c§lʀᴏᴜɴᴅ ᴛᴡᴏ§e! Now you know the course.. or do you? This time we're going backwards, what dimension will you end up in next? Good luck!
                                            §8
                                            """);
                                    }
                                }
                                break;
                            case 46:
                                if(currentRound == 2) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        player.sendTitle("\uE023", "", 20, 40, 20);
                                    }
                                }
                                break;
                            case 45:
                                if(currentRound == 2) {
                                    timeLeft = 28;
                                }
                                break;
                            case 41:
                                if(currentRound == 1) {
                                    startCustomPan("dd2");
                                }
                                break;
                            case 36:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                                §8
                                                §8
                                                §r⏳ §eIn this mode speed, parkour, and various movement skills will be neccessary! The faster you travel between dimensions, the more points you earn, so get dashing!
                                                §8
                                                """);
                                }
                                break;
                            case 27:
                                if(currentRound == 1){
                                    startDDPan1();
                                } else if (currentRound == 2){
                                    startDDPan2();
                                }
                                break;
                            case 22:
                                if(currentRound == 1){
                                    TextDisplay mapTitle1 = world.spawn(new Location(world, 53.5f, 149.5f, 1099.5f), TextDisplay.class);

                                    mapTitle1.setSeeThrough(true);
                                    mapTitle1.setText("§l" + map1);
                                    mapTitle1.setRotation(-180, 0);
                                    mapTitle1.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                                    Quaternionf quat = new Quaternionf();
                                    Transformation transform = new Transformation(
                                            new Vector3f(0, 0, 0),
                                            quat,
                                            new Vector3f(5.0f, 5.0f, 5.0f),
                                            quat
                                    );

                                    mapTitle1.setTransformation(transform);

                                    ddPortalTitles.add(mapTitle1);
                                    Block block;
                                    for(int x = 36; x <= 83; x++){
                                        for(int y = 138; y <= 152; y++){
                                            for(int z = 1071; z <= 1109; z++){
                                                block = Bukkit.getWorld("build").getBlockAt(x, y, z);
                                                if(block.getType().equals(Material.GRAY_CONCRETE_POWDER)){
                                                    block.setType(ddMapBlocks.get(map1).getFirst());
                                                }
                                                if(block.getType().equals(Material.GRAY_CONCRETE)){
                                                    block.setType(ddMapBlocks.get(map1).get(1));
                                                }
                                            }
                                        }
                                    }

                                    ddMapTiles.add(mapTitle1);
                                }
                                if(currentRound == 2){
                                    TextDisplay mapTitle1 = world.spawn(new Location(world, 67.5f, 149.5f, 1152.5f), TextDisplay.class);
                                    Block block;

                                    mapTitle1.setSeeThrough(true);
                                    mapTitle1.setText("§l" + map1);
                                    mapTitle1.setRotation(0, 0);
                                    mapTitle1.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                                    Quaternionf quat = new Quaternionf();
                                    Transformation transform = new Transformation(
                                            new Vector3f(0, 0, 0),
                                            quat,
                                            new Vector3f(5.0f, 5.0f, 5.0f),
                                            quat
                                    );

                                    mapTitle1.setTransformation(transform);

                                    ddPortalTitles.add(mapTitle1);
                                    for(int x = 36; x <= 83; x++){
                                        for(int y = 138; y <= 155; y++){
                                            for(int z = 1143; z <= 1179; z++){
                                                block = Bukkit.getWorld("build").getBlockAt(x, y, z);
                                                if(block.getType().equals(Material.GRAY_CONCRETE_POWDER)){
                                                    block.setType(ddMapBlocks.get(map1).getFirst());
                                                }
                                                if(block.getType().equals(Material.GRAY_CONCRETE)){
                                                    block.setType(ddMapBlocks.get(map1).get(1));
                                                }
                                            }
                                        }
                                    }

                                    ddMapTiles.add(mapTitle1);
                                }
                                break;
                            case 18:
                                if(currentRound == 1){
                                    TextDisplay mapTitle2 = world.spawn(new Location(world, 67.5f, 149.5f, 1099.5f), TextDisplay.class);
                                    Block block;

                                    mapTitle2.setSeeThrough(true);
                                    mapTitle2.setText("§l" + map2);
                                    mapTitle2.setRotation(-180, 0);
                                    mapTitle2.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                                    Quaternionf quat = new Quaternionf();
                                    Transformation transform = new Transformation(
                                            new Vector3f(0, 0, 0),
                                            quat,
                                            new Vector3f(5.0f, 5.0f, 5.0f),
                                            quat
                                    );

                                    mapTitle2.setTransformation(transform);

                                    ddPortalTitles.add(mapTitle2);
                                    for(int x = 36; x <= 83; x++){
                                        for(int y = 138; y <= 152; y++){
                                            for(int z = 1071; z <= 1109; z++){
                                                block = Bukkit.getWorld("build").getBlockAt(x, y, z);
                                                if(block.getType().equals(Material.LIGHT_GRAY_CONCRETE_POWDER)){
                                                    block.setType(ddMapBlocks.get(map2).getFirst());
                                                }
                                                if(block.getType().equals(Material.LIGHT_GRAY_CONCRETE)){
                                                    block.setType(ddMapBlocks.get(map2).get(1));
                                                }
                                            }
                                        }
                                    }

                                    ddMapTiles.add(mapTitle2);
                                }
                                if(currentRound == 2){
                                    TextDisplay mapTitle2 = world.spawn(new Location(world, 53.5f, 149.5f, 1152.5f), TextDisplay.class);
                                    Block block;

                                    mapTitle2.setSeeThrough(true);
                                    mapTitle2.setText("§l" + map2);
                                    mapTitle2.setRotation(0, 0);
                                    mapTitle2.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                                    Quaternionf quat = new Quaternionf();
                                    Transformation transform = new Transformation(
                                            new Vector3f(0, 0, 0),
                                            quat,
                                            new Vector3f(5.0f, 5.0f, 5.0f),
                                            quat
                                    );

                                    mapTitle2.setTransformation(transform);

                                    ddPortalTitles.add(mapTitle2);
                                    for(int x = 36; x <= 83; x++){
                                        for(int y = 138; y <= 155; y++){
                                            for(int z = 1143; z <= 1179; z++){
                                                block = Bukkit.getWorld("build").getBlockAt(x, y, z);
                                                if(block.getType().equals(Material.LIGHT_GRAY_CONCRETE_POWDER)){
                                                    block.setType(ddMapBlocks.get(map2).getFirst());
                                                }
                                                if(block.getType().equals(Material.LIGHT_GRAY_CONCRETE)){
                                                    block.setType(ddMapBlocks.get(map2).get(1));
                                                }
                                            }
                                        }
                                    }

                                    ddMapTiles.add(mapTitle2);
                                }
                                break;
                            case 11:
                                if(plugin.currentRound == 1) {
                                    teleportPlayers(TeleportConfig.get().getLocation("players.dimensiondash"), 2);
                                    teleportSpectators(TeleportConfig.get().getLocation("spectators.dimensiondash"), 2);
                                }
                                if(plugin.currentRound == 2){
                                    teleportPlayers(TeleportConfig.get().getLocation("players.dimensiondash2"), 2);
                                    teleportSpectators(TeleportConfig.get().getLocation("spectators.dimensiondash2"), 2);
                                }
                                break;
                            case 10:
                                for (Player player : getPlayers()) {
                                    player.setGameMode(GameMode.SURVIVAL);
                                    ItemStack air = new ItemStack(Material.AIR);
                                    player.getInventory().clear();
                                    player.getInventory().setHelmet(air);
                                }
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
                                ddStartTime = System.currentTimeMillis();
                                if(plugin.currentRound == 1) {
                                    Bukkit.getWorld("build").getBlockAt(62, 186, 874).setType(Material.REDSTONE_BLOCK);
                                    Bukkit.getWorld("build").getBlockAt(62, 186, 874).setType(Material.AIR);

                                    // Start Wall
                                    world.getBlockAt(90, 149, 919).setType(Material.REDSTONE_BLOCK);
                                    world.getBlockAt(90, 149, 919).setType(Material.AIR);

                                    Bukkit.getWorld("build").getBlockAt(75, 137, 923).setType(Material.REDSTONE_BLOCK);
                                    Bukkit.getWorld("build").getBlockAt(75, 137, 923).setType(Material.AIR);

                                    Bukkit.getWorld("build").getBlockAt(62, 136, 1064).setType(Material.REDSTONE_BLOCK);
                                    Bukkit.getWorld("build").getBlockAt(62, 136, 1064).setType(Material.AIR);
                                }
                                if(plugin.currentRound == 2){
                                    Bukkit.getWorld("build").getBlockAt(75, 174, 1331).setType(Material.REDSTONE_BLOCK);
                                    Bukkit.getWorld("build").getBlockAt(75, 174, 1331).setType(Material.AIR);

                                    Bukkit.getWorld("build").getBlockAt(76, 137, 923).setType(Material.REDSTONE_BLOCK);
                                    Bukkit.getWorld("build").getBlockAt(76, 137, 923).setType(Material.AIR);

                                    Bukkit.getWorld("build").getBlockAt(61, 136, 1064).setType(Material.REDSTONE_BLOCK);
                                    Bukkit.getWorld("build").getBlockAt(61, 136, 1064).setType(Material.AIR);
                                }
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 2);
                                playMusicAll(Sound.MUSIC_DISC_BLOCKS);
                                pvpEnabled = true;
                                for (Player player : getPlayers()) {
                                    PotionEffect PotionEffect = new PotionEffect(PotionEffectType.RESISTANCE, 12000, 1, false, false);
                                    PotionEffect PotionEffect2 = new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 12000, 5, false, false);
                                    PotionEffect PotionEffect3 = new PotionEffect(PotionEffectType.NIGHT_VISION, 12000, 5, false, false);
                                    player.addPotionEffect(PotionEffect);
                                    player.addPotionEffect(PotionEffect2);
                                    player.addPotionEffect(PotionEffect3);
                                    player.sendTitle("§a§l▶ DASH! ◀", "", 0, 40, 0);
                                }

                                // Hide everyone from eachother.
                                for (Player viewer : getPlayers()) {
                                    for (Player target : getPlayers()) {
                                        if (!viewer.equals(target)) {
                                            viewer.hidePlayer(plugin, target);
                                        }
                                    }
                                }

                                targetTime = 0;
                                timerLabel = "Game End:";
                                startTimer(540, "dimensiondash");
                                startStopwatch(540  , "dimensiondashwatch");
                                runningTimers.remove("dimensiondashstart");
                                startDDTimer();
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

        runningTimers.put("dimensiondashstart", new AbstractMap.SimpleEntry<>(task, 61));
    }

    public void startDDTimer() {

        ddTimer = Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            long now = System.currentTimeMillis();

            for (Player player : unfinishedPlayers) {

                if (!player.isOnline()) continue;

                DashTimeData data = plugin.dashLapData.get(player.getName());
                if (data == null) continue;

                long overall = now - plugin.ddStartTime;

                long lapTimer;

                if (data.getLap1Timestamp() == 0) {
                    lapTimer = now - plugin.ddStartTime;

                } else if (data.getLap2Timestamp() == 0) {
                    lapTimer = now - data.getLap1Timestamp();

                } else if (data.getLap3Timestamp() == 0) {
                    lapTimer = now - data.getLap2Timestamp();

                } else {
                    lapTimer = data.getLap3Timestamp() - data.getLap2Timestamp();
                }

                if(getPlayers().contains(player) && !ghostManager.getGhostPlayers().contains(player.getName())) {
                    player.spigot().sendMessage(
                            ChatMessageType.ACTION_BAR,
                            TextComponent.fromLegacy(
                                    "§8[§a" + plugin.formatTime(overall) +
                                            "§8] §8[§c" + plugin.formatTime(lapTimer) +
                                            "§8]"
                            )
                    );
                } else {
                    if(getPlayers().contains(player)) {
                        player.spigot().sendMessage(
                                ChatMessageType.ACTION_BAR,
                                TextComponent.fromLegacy(
                                        "§8[§a" + plugin.formatTime(overall) +
                                                "§8]"
                                )
                        );
                    }
                }
            }

        }, 0L, 1L);

        runningTimers.put("ddTimer", new AbstractMap.SimpleEntry<>(ddTimer, 0));
    }

    public String formatTime(long millis) {

        long minutes = millis / 60000;
        long seconds = (millis % 60000) / 1000;
        long milliseconds = millis % 1000;

        return String.format("%02d:%02d:%03d",
                minutes,
                seconds,
                milliseconds
        );
    }

    public void revealOtherPlayers(Player target) {
        List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());
        String firstTeam = leaderteams.getFirst();
        String secondTeam = leaderteams.get(1);

        List<String> firstPlayers = TeamsConfig.get().getStringList("teams." + firstTeam + ".players");
        List<String> secondPlayers = TeamsConfig.get().getStringList("teams." + secondTeam + ".players");

        List<Player> allTeamPlayers = new ArrayList<>();

        for (String name : firstPlayers) {
            Player p = Bukkit.getPlayer(name);
            if (p != null) allTeamPlayers.add(p);
        }

        for (String name : secondPlayers) {
            Player p = Bukkit.getPlayer(name);
            if (p != null) allTeamPlayers.add(p);
        }

        for(Player reveal : allTeamPlayers){
            target.showPlayer(plugin, reveal);
        }
    }

    public void revealAllOtherPlayers(Player target) {
        for(Player reveal : getPlayers()){
            target.showPlayer(plugin, reveal);
        }
    }

    public void startDimensionDashFinale(){
        ddFinishers.clear();
        musicManager.stopMusicAll();
        plugin.shopAllowed = false;
        clearInventories();
        World world = Bukkit.getWorld("build");

        world.getBlockAt(60, 137, 1077).setType(Material.REDSTONE_BLOCK);
        world.getBlockAt(60, 137, 1077).setType(Material.AIR);

        world.getBlockAt(60, 137, 1174).setType(Material.REDSTONE_BLOCK);
        world.getBlockAt(60, 137, 1174).setType(Material.AIR);

        world.getBlockAt(60, 156, 1250).setType(Material.REDSTONE_BLOCK);
        world.getBlockAt(60, 156, 1250).setType(Material.AIR);

        // Castle elytra barriers
        world.getBlockAt(39, 156, 1213).setType(Material.REDSTONE_BLOCK);
        world.getBlockAt(39, 156, 1213).setType(Material.AIR);

        world.getBlockAt(94, 159, 1213).setType(Material.REDSTONE_BLOCK);
        world.getBlockAt(94, 159, 1213).setType(Material.AIR);
        // -------

        // Start Wall
        world.getBlockAt(88, 149, 920).setType(Material.REDSTONE_BLOCK);
        world.getBlockAt(88, 149, 920).setType(Material.AIR);
        // -------

        // Castle Exit Barrier
        world.getBlockAt(60, 156, 1250).setType(Material.REDSTONE_BLOCK);
        world.getBlockAt(60, 156, 1250).setType(Material.AIR);
        // --------


        World world2 = Bukkit.getWorld("build");
        for(int i = 2; i <= 4; i++) {
            world2.getBlockAt(plugin.cdWallCoords[i][0], plugin.cdWallCoords[i][1] - 1, plugin.cdWallCoords[i][2]).setType(Material.REDSTONE_BLOCK);
            world2.getBlockAt(plugin.cdWallCoords[i][0], plugin.cdWallCoords[i][1] - 1, plugin.cdWallCoords[i][2]).setType(Material.DIRT);
        }
        for(int i = 0; i <= 1; i++) {
            world2.getBlockAt(plugin.cdWallCoords[i][0], plugin.cdWallCoords[i][1], plugin.cdWallCoords[i][2]).setType(Material.REDSTONE_BLOCK);
            world2.getBlockAt(plugin.cdWallCoords[i][0], plugin.cdWallCoords[i][1], plugin.cdWallCoords[i][2]).setType(Material.DIRT);
        }

        List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());
        String firstTeam = leaderteams.getFirst();
        String secondTeam = leaderteams.get(1);

        ddFinaleTeamCompletions.clear();
        ddFinaleTeamCompletions.put(firstTeam, 0);
        ddFinaleTeamCompletions.put(secondTeam, 0);

        List<String> firstPlayers = TeamsConfig.get().getStringList("teams." + firstTeam + ".players");
        List<String> secondPlayers = TeamsConfig.get().getStringList("teams." + secondTeam + ".players");

        for(String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)){
            if(TeamsConfig.get().getStringList("teams." + team + ".players").isEmpty()) continue;
            if(!Objects.equals(team, firstTeam) && !Objects.equals(team, secondTeam)){
                deadTeams.add(team);
                deadPlayers.addAll(TeamsConfig.get().getStringList("teams." + team + ".players"));
            }
        }
        List<String> mapsPlayed = MapsPlayedConfig.get().getStringList("maps.Dimension Dash");

        List<String> mapList = new ArrayList<>(ddMapBlocks.keySet());

        mapList.removeAll(mapsPlayed);

        Random r = new Random();

        ddChosenMap = mapList.get(r.nextInt(mapList.size()));
        resetDDLapTimes();
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 41;
            @Override
            public void run() {
                if(runningTimers.containsKey("dimensiondashstart")) {
                    if (!pausedTimers.contains("dimensiondashstart")) {
                        timeLeft--;
                        runningTimers.get("dimensiondashstart").setValue(timeLeft);
                        bossBarBgTest();
                        switch (timeLeft) {
                            case 40:
                                Bukkit.getWorld("build").getBlockAt(62, 186, 875).setType(Material.REDSTONE_BLOCK);
                                Bukkit.getWorld("build").getBlockAt(62, 186, 875).setType(Material.AIR);
                                finaleTeamTeleport("dimensiondash", 5);
                                teleportSpectators(TeleportConfig.get().getLocation("spectators.dimensiondash"), 5);

                                resetColourDash();
                                resetTeamCompletions();
                                break;
                            case 35:
                                try {
                                    glowTeams();
                                } catch (ReflectiveOperationException e) {
                                    throw new RuntimeException(e);
                                }
                                currentMode = "Dimension Dash";

                                for(Player p : getPlayers()){
                                    if(!secondPlayers.contains(p.getName()) && !firstPlayers.contains(p.getName())){
                                        ghostManager.addGhostPlayer(p.getName());
                                    }
                                }

                                for(Player player : getPlayers()) {
                                    if(!firstPlayers.contains(player.getName()) && !secondPlayers.contains(player.getName())){
                                        ghostManager.addGhostPlayer(player.getName());
                                    } else {
                                        ghostManager.removeGhostPlayer(player.getName());
                                    }
                                }
                                break;
                            case 30:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §eWelcome to §d§l§oDimension Dash §cFinale§e! Race to the finish, only one way! This time in an undiscovered dimension.
                                            §8
                                            """);
                                    player.sendTitle("§a§lPlacement Points", "§a§lDecides.", 0, 40, 20);
                                }
                                break;
                            case 20:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                                §8
                                                §8
                                                §r⏳ §eIn this mode speed, parkour, and various movement skills will be neccessary! If both teams tie for points, the player in 1st will take the victory!
                                                §8
                                                """);
                                }
                                TextDisplay mapTitle1 = world.spawn(new Location(world, 60.5f, 149.5f, 1099.5f), TextDisplay.class);

                                mapTitle1.setSeeThrough(true);
                                mapTitle1.setText("§l" + ddChosenMap);
                                mapTitle1.setRotation(-180, 0);
                                mapTitle1.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
                                Quaternionf quat = new Quaternionf();
                                Transformation transform = new Transformation(
                                        new Vector3f(0, 0, 0),
                                        quat,
                                        new Vector3f(5.0f, 5.0f, 5.0f),
                                        quat
                                );

                                mapTitle1.setTransformation(transform);

                                ddPortalTitles.add(mapTitle1);
                                Block block;
                                for(int x = 36; x <= 83; x++){
                                    for(int y = 138; y <= 152; y++){
                                        for(int z = 1071; z <= 1109; z++){
                                            block = Bukkit.getWorld("build").getBlockAt(x, y, z);
                                            if(block.getType().equals(Material.GRAY_CONCRETE_POWDER) || block.getType().equals(Material.LIGHT_GRAY_CONCRETE_POWDER)){
                                                block.setType(ddMapBlocks.get(ddChosenMap).getFirst());
                                            }
                                            if(block.getType().equals(Material.GRAY_CONCRETE) || block.getType().equals(Material.LIGHT_GRAY_CONCRETE)){
                                                block.setType(ddMapBlocks.get(ddChosenMap).get(1));
                                            }
                                        }
                                    }
                                }
                                ddMapTiles.add(mapTitle1);
                                break;
                            case 10:
                                for (Player player : getPlayers()) {
                                    if(!ghostManager.getGhostPlayers().contains(player.getName())) {
                                        player.setGameMode(GameMode.ADVENTURE);
                                    }
                                }
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
                                ddStartTime = System.currentTimeMillis();
                                Bukkit.getWorld("build").getBlockAt(62, 186, 874).setType(Material.REDSTONE_BLOCK);
                                Bukkit.getWorld("build").getBlockAt(62, 186, 874).setType(Material.AIR);

                                Bukkit.getWorld("build").getBlockAt(75, 137, 923).setType(Material.REDSTONE_BLOCK);
                                Bukkit.getWorld("build").getBlockAt(75, 137, 923).setType(Material.AIR);

                                Bukkit.getWorld("build").getBlockAt(62, 136, 1064).setType(Material.REDSTONE_BLOCK);
                                Bukkit.getWorld("build").getBlockAt(62, 136, 1064).setType(Material.AIR);

                                // Start Wall
                                world.getBlockAt(90, 149, 919).setType(Material.REDSTONE_BLOCK);
                                world.getBlockAt(90, 149, 919).setType(Material.AIR);
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 2);
                                playMusicAll(Sound.MUSIC_DISC_BLOCKS);
                                for (Player player : getPlayers()) {
                                    PotionEffect PotionEffect = new PotionEffect(PotionEffectType.RESISTANCE, 12000, 1, false, false);
                                    PotionEffect PotionEffect2 = new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 12000, 5, false, false);
                                    PotionEffect PotionEffect3 = new PotionEffect(PotionEffectType.NIGHT_VISION, 12000, 5, false, false);
                                    player.addPotionEffect(PotionEffect);
                                    player.addPotionEffect(PotionEffect2);
                                    player.addPotionEffect(PotionEffect3);
                                    player.sendTitle("§a§l▶ DASH! ◀", "", 0, 40, 0);
                                }

                                List<Player> allTeamPlayers = new ArrayList<>();

                                for (String name : firstPlayers) {
                                    Player p = Bukkit.getPlayer(name);
                                    if (p != null) allTeamPlayers.add(p);
                                }

                                for (String name : secondPlayers) {
                                    Player p = Bukkit.getPlayer(name);
                                    if (p != null) allTeamPlayers.add(p);
                                }

                                for (Player viewer : allTeamPlayers) {
                                    for (Player target : allTeamPlayers) {
                                        if (!viewer.equals(target)) {
                                            viewer.hidePlayer(plugin, target);
                                        }
                                    }
                                }


                                ghostManager.giveCompasses();

                                startDDTimer();
                                startTimer(540, "dimensiondash");
                                startStopwatch(540  , "dimensiondashwatch");
                                runningTimers.remove("dimensiondashstart");
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

        runningTimers.put("dimensiondashstart", new AbstractMap.SimpleEntry<>(task, 61));
    }

    public void startColourDashFinale(){
        pvpArenaManager.disablePvPArena();
        fillVotingSpace(4);
        plugin.shopAllowed = false;
        clearInventories();
        setPreviousPlacements();
        resetModeFullPoints();
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
            int timeLeft = 41;
            @Override
            public void run() {
                if(runningTimers.containsKey("colourdashstart")) {
                    if (!pausedTimers.contains("colourdashstart")) {
                        timeLeft--;
                        runningTimers.get("colourdashstart").setValue(timeLeft);
                        bossBarBgTest();
                        switch (timeLeft) {
                            case 40:
                                if(plugin.currentRound == 1) {
                                    Bukkit.getWorld("build").getBlockAt(121, 139, 790).setType(Material.REDSTONE_BLOCK);
                                    Bukkit.getWorld("build").getBlockAt(121, 139, 790).setType(Material.AIR);
                                    teleportPlayers(TeleportConfig.get().getLocation("players.colourdash"), 5);
                                    teleportSpectators(TeleportConfig.get().getLocation("spectators.colourdash"), 5);
                                }
                                resetColourDash();
                                resetTeamCompletions();
                                resetModePoints();
                                break;
                            case 35:
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
                            case 30:
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
                                break;
                            case 20:
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

        runningTimers.put("colourdashstart", new AbstractMap.SimpleEntry<>(task, 41));
    }

    public String buildBar(int barsA, int barsB, String teamA, String teamB) {
        StringBuilder sb = new StringBuilder();

        sb.append(teamGlowColors.get(teamA));
        for (int i = 0; i < barsA; i++) sb.append("|");

        sb.append(teamGlowColors.get(teamB));
        for (int i = 0; i < barsB; i++) sb.append("|");

        return sb.toString();
    }

    public void getCurrentPPStandings(){
        for(Player p : Bukkit.getOnlinePlayers()) {
            messagePlayer(p, " §f-  §e§l ᴄᴜʀʀᴇɴᴛ ᴘᴜsʜ sᴛᴀɴᴅɪɴɢs  §f-");
            messagePlayer(p, " §f-  §e§l  (ᴡɪɴs - ʟᴏssᴇs)  §f-");
            for (String team : ppTeamStandings.keySet()) {
                messagePlayer(p, getTeamDisplayName(team) + "§7 | §a" + ppTeamStandings.get(team).getFirst() + "§f - §c" + ppTeamStandings.get(team).get(1));
            }
            messagePlayer(p, "§f--------------------------");
        }
    }

    public void pushPointPercentages(){
        Material leftBlock;
        Material rightBlock;
        World world = Bukkit.getWorld("build");

        Map<Material, String> concreteToTeam = new HashMap<>();

        for (Map.Entry<String, Material> entry : teamConcrete.entrySet()) {
            concreteToTeam.put(entry.getValue(), entry.getKey());
        }

        for (int map = 1; map <= 4; map++) {

            leftBlock = world.getBlockAt(972 + (map * 101), -61, -403).getType();
            rightBlock = world.getBlockAt(972 + (map * 101), -61, -499).getType();

            final String leftTeam = concreteToTeam.get(leftBlock);
            final String rightTeam = concreteToTeam.get(rightBlock);

            final List<Double> leftTeamScores = new ArrayList<>();

            for (ItemDisplay wall : mapWalls.keySet()) {
                if (mapWalls.get(wall).equals("map" + map)) {
                    String rawText = wallTexts.get(wall).getText().replaceAll("§.", "");
                    String[] parts = rawText.split("\\|");
                    leftTeamScores.add(Double.parseDouble(parts[0].replace("%", "").trim()));
                }
            }

            if (leftTeamScores.isEmpty()) continue;

            final List<String> leftPlayers = TeamsConfig.get().getStringList("teams." + leftTeam + ".players");
            final List<String> rightPlayers = TeamsConfig.get().getStringList("teams." + rightTeam + ".players");

            final List<String> mapPlayers = new ArrayList<>();
            mapPlayers.addAll(leftPlayers);
            mapPlayers.addAll(rightPlayers);

            // ✅ CALCULATE FINAL PERCENT → MULTIPLE OF 4 POINTS
            double total = 0;
            for (double d : leftTeamScores) {
                total += d;
            }

            double finalPercentA = total / leftTeamScores.size();

            int rawPointsA = (int) Math.round((finalPercentA / 100.0) * 440.0);

            // Round to nearest multiple of 4
            final int pointsA = Math.round(rawPointsA / 4.0f) * 4;
            final int pointsB = 440 - pointsA;

            BukkitTask task = new BukkitRunnable() {

                int timeLeft = 201;
                double t;
                double eased;

                double startPercentA = 50.0;
                int nextIterationStart = 180;
                int index = 1;

                double sum = leftTeamScores.get(0);
                double endPercentA = sum;

                @Override
                public void run() {

                    if (!runningTimers.containsKey("percentages" + leftTeam)) {
                        cancel();
                        return;
                    }

                    if (pausedTimers.contains("percentages" + leftTeam)) {
                        return;
                    }

                    timeLeft--;
                    runningTimers.get("percentages" + leftTeam).setValue(timeLeft);

                    // Animation window
                    if (timeLeft <= nextIterationStart && timeLeft > nextIterationStart - 20) {

                        t = (nextIterationStart - timeLeft) / 20.0;
                        t = Math.min(1.0, Math.max(0.0, t));
                        eased = 1 - Math.pow(1 - t, 2);

                        double currentA = startPercentA + eased * (endPercentA - startPercentA);
                        int barsA = (int) Math.round((currentA / 100.0) * 20);
                        int barsB = 20 - barsA;

                        String bar = buildBar(barsA, barsB, leftTeam, rightTeam);

                        for (String player : mapPlayers) {
                            Player p = Bukkit.getPlayer(player);
                            if (p != null) {
                                p.sendTitle(
                                        teamGlowColors.get(leftTeam) + String.format("%.2f", currentA) + "%" +
                                                ChatColor.WHITE + " | " +
                                                teamGlowColors.get(rightTeam) + String.format("%.2f", 100 - currentA) + "%",
                                        bar,
                                        0, 60, 20
                                );
                            }
                        }
                    }

                    switch(timeLeft){

                        case 200:
                            String bar = buildBar(10, 10, leftTeam, rightTeam);
                            for (String player : mapPlayers) {
                                Player p = Bukkit.getPlayer(player);
                                if (p != null) {
                                    p.sendTitle(
                                            teamGlowColors.get(leftTeam) + "50.00%" +
                                                    ChatColor.WHITE + " | " +
                                                    teamGlowColors.get(rightTeam) + "50.00%",
                                            bar,
                                            0, 60, 20
                                    );
                                }
                            }
                            break;

                        case 150, 110:
                            startPercentA = endPercentA;

                            if (index < leftTeamScores.size()) {
                                sum += leftTeamScores.get(index);
                                endPercentA = sum / (index + 1);
                                index++;
                            }

                            nextIterationStart -= 40;
                            break;
                    }

                    if (timeLeft <= 60) {

                        int perPlayerA = pointsA / 4;
                        int perPlayerB = pointsB / 4;

                        for (String player : leftPlayers) {
                            earnPoints(player, perPlayerA, true);
                        }

                        for (String player : rightPlayers) {
                            earnPoints(player, perPlayerB, true);
                        }

                        for (String player : mapPlayers) {
                            Player p = Bukkit.getPlayer(player);
                            if (p != null) {
                                p.sendTitle(
                                        ChatColor.GOLD + "§lғɪɴᴀʟ ʀᴇsᴜʟᴛ",
                                        teamGlowColors.get(leftTeam) + "§l\uD83D\uDCB0" + pointsA +
                                                ChatColor.WHITE + " | " +
                                                teamGlowColors.get(rightTeam) + "§l\uD83D\uDCB0" + pointsB,
                                        10, 80, 20
                                );
                                if(Objects.equals(PlayerConfig.get().getString("players." + player + ".team"), leftTeam)){
                                    messagePlayer(p, "§e§l\uD83D\uDCB0" + pointsA + " §7| §ePoints earned for the percentage of land gained by your team on average.");
                                }
                                if(Objects.equals(PlayerConfig.get().getString("players." + player + ".team"), rightTeam)){
                                    messagePlayer(p, "§e§l\uD83D\uDCB0" + pointsB + " §7| §ePoints earned for the percentage of land gained by your team on average.");
                                }

                            }
                        }

                        if(pointsA > pointsB){
                            addRoundPoints(leftTeam, rightTeam);
                            for (String player : mapPlayers) {
                                Player p = Bukkit.getPlayer(player);
                                if (p != null) {
                                    if(Objects.equals(PlayerConfig.get().getString("players." + player + ".team"), leftTeam)){
                                        messagePlayer(p, "§e§l\uD83D\uDCB040 §7| §aPoints earned for winning the round!");
                                    }
                                }
                            }
                        } else {
                            addRoundPoints(rightTeam, leftTeam);

                            for (String player : mapPlayers) {
                                Player p = Bukkit.getPlayer(player);
                                if (p != null) {
                                    if(Objects.equals(PlayerConfig.get().getString("players." + player + ".team"), rightTeam)){
                                        messagePlayer(p, "§e§l\uD83D\uDCB040 §7| §aPoints earned for winning the round!");
                                    }
                                }
                            }
                        }


                        runningTimers.remove("percentages" + leftTeam);
                        cancel();
                    }
                }

            }.runTaskTimer(this, 0L, 1L);

            runningTimers.put("percentages" + leftTeam, new AbstractMap.SimpleEntry<>(task, 201));
        }
    }

    public void addRoundPoints(String winningTeam, String losingTeam) {
        ppTeamStandings.putIfAbsent(winningTeam, new ArrayList<>(Arrays.asList(0, 0)));
        List<Integer> winningScores = ppTeamStandings.get(winningTeam);
        winningScores.set(0, winningScores.getFirst() + 1);

        ppTeamStandings.putIfAbsent(losingTeam, new ArrayList<>(Arrays.asList(0, 0)));
        List<Integer> losingScores = ppTeamStandings.get(losingTeam);
        losingScores.set(1, losingScores.get(1) + 1);
    }

    public Integer[][] ppWallSummoners() {
        return new Integer[][]{
                {1073, -405},
                {1174, -405},
                {1275, -405},
                {1376, -405},
                {1506, -405},

                {1073, -497},
                {1174, -497},
                {1275, -497},
                {1376, -497},
                {1506, -497}
        };
    }

    public Integer[][] ppWallDestroyers() {
        return new Integer[][]{
                {1072, -405},
                {1173, -405},
                {1274, -405},
                {1375, -405},
                {1505, -405},

                {1074, -497},
                {1175, -497},
                {1276, -497},
                {1377, -497},
                {1507, -497}
        };
    }

    public List<Material> woolColours() {
            return List.of(
                    Material.RED_CONCRETE,
                    Material.ORANGE_CONCRETE,
                    Material.YELLOW_CONCRETE,
                    Material.LIME_CONCRETE,
                    Material.LIGHT_BLUE_CONCRETE,
                    Material.BLUE_CONCRETE,
                    Material.MAGENTA_CONCRETE,
                    Material.WHITE_CONCRETE,
                    Material.GRAY_CONCRETE
                    );
    }

    public List<Material> woolBlocks() {
        return List.of(
                Material.RED_WOOL,
                Material.ORANGE_WOOL,
                Material.YELLOW_WOOL,
                Material.LIME_WOOL,
                Material.LIGHT_BLUE_WOOL,
                Material.BLUE_WOOL,
                Material.MAGENTA_WOOL,
                Material.WHITE_WOOL,
                Material.GRAY_WOOL
        );
    }

    private Location getTeleportLocation(int mapIndex, int sideIndex) {
        double x = 1073.5 + (101 * mapIndex);
        double y = -51;
        double z = sideIndex == 0 ? -403.5 : -499.5;
        float yaw = sideIndex == 0 ? 180f : 0f;

        return new Location(Bukkit.getWorld("build"), x, y, z, yaw, 0f);
    }

    public void teleportTeamsToCurrentRound() {
        for (int mapIndex = 0; mapIndex < 4; mapIndex++) {
            Block[] blocks = mapSides.get("map" + (mapIndex + 1));

            if (blocks == null) continue;

            for (int sideIndex = 0; sideIndex < blocks.length; sideIndex++) {
                Block block = blocks[sideIndex];
                Material concrete = block.getType();

                String teamName = concreteConvertTeam.get(concrete);
                if (teamName == null) {
                    plugin.getLogger().warning("No team found for concrete: " + concrete);
                    continue;
                }

                Location dest = getTeleportLocation(mapIndex, sideIndex);
                List<String> teamPlayerNames = TeamsConfig.get().getStringList("teams." + teamName + ".players");
                List<Player> teamPlayers = teamPlayerNames.stream()
                        .map(Bukkit::getPlayerExact)
                        .filter(Objects::nonNull)
                        .toList();
                for (Player p : teamPlayers) {
                    p.teleport(dest);
                }
            }
        }
    }

    public void startPushPoint(){
        pvpArenaManager.disablePvPArena();
        multiplier = GameOrderConfig.get().getDouble("multiplier");
        fillVotingSpace(5);
        musicManager.stopMusicAll();
        plugin.shopAllowed = false;
        finalPush = false;
        clearInventories();
        ppTeamMatchups.clear();
        if(currentRound == 1){
            setPreviousPlacements();
            resetModeFullPoints();
            ppTeamStandings.clear();
        }
        ppEscapedPlayers.clear();

        List<Block> allBlocks = mapSides.values().stream()
                .flatMap(Arrays::stream)
                .toList();

        for (Block b : allBlocks) {
            b.setType(Material.GRAY_CONCRETE);
        }

        for(Integer[] blocks : ppWallSummoners()){
            Bukkit.getWorld("build").getBlockAt(blocks[0], -62, blocks[1]).setType(Material.REDSTONE_BLOCK);
            Bukkit.getWorld("build").getBlockAt(blocks[0], -62, blocks[1]).setType(Material.AIR);
        }

//        World world = Bukkit.getWorld("build");
//        Block block;
//        for(int x = 137; x >= -27; x--){
//            for(int y = 195; y >= 127; y--){
//                for(int z = 804; z <= 1345; z++){
//                    block = world.getBlockAt(x,y,z);
//                    if(colourDashBlocks.contains(block.getType())){
//                        block.setType(Material.AIR);
//                    }
//                }
//            }
//        }
        targetTime = 12;
        if(currentRound == 1) {
            timerLabel = "Game Explanation:";
        } else {
            timerLabel = "Starting Round:";
        }
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 61;
            @Override
            public void run() {
                if(runningTimers.containsKey("pushpointstart")) {
                    if (!pausedTimers.contains("pushpointstart")) {
                        timeLeft--;
                        runningTimers.get("pushpointstart").setValue(timeLeft);
                        bossBarBgTest();
                        switch (timeLeft) {
                            case 60:
                                World world = Bukkit.getWorld("build");
                                for(int i = 0; i <= 3; i++) {

                                    mapSides.get("map" + (i + 1))[0].setType(world.getBlockAt(872 + ((i + 1) * 111) + (currentRound - 1), -61, -392).getType());
                                    mapSides.get("map" + (i + 1))[1].setType(world.getBlockAt(872 + ((i + 1) * 111) + (currentRound - 1), -61, -527).getType());
                                }
                                teamTeleport("pushpoint", 5);
                                teleportSpectators(TeleportConfig.get().getLocation("spectators.pushpoint"), 5);
                                break;
                            case 55:
                                try {
                                    glowTeams();
                                } catch (ReflectiveOperationException e) {
                                    throw new RuntimeException(e);
                                }
                                resetModePoints();
                                currentMode = "Push Point";
                                addToGameOrder(currentMode);
                                for (Player player : getPlayers()) {
                                    if(currentRound == 1) {
                                        ItemStack pumpkin = new ItemStack(Material.CARVED_PUMPKIN);
                                        player.getInventory().clear();
                                        player.getInventory().setHelmet(pumpkin);
                                    }
                                    ghostManager.removeGhostPlayer(player.getName());
                                }
                                World world2 = Bukkit.getWorld("build");
                                for(int i = 0; i <= 3; i++) {
                                    Block block;
                                    for (int x = (1025 + (i * 101)); x <= (1121 + (i * 101)); x++) {
                                        for (int y = -57; y <= -39; y++) {
                                            for (int z = -417; z <= -402; z++) {
                                                block = world2.getBlockAt(x, y, z);
                                                if (woolColours().contains(block.getType())) {
                                                    block.setType(world2.getBlockAt(872 + ((i + 1) * 111) + (currentRound - 1), -61, -392).getType());
                                                }
                                            }
                                        }
                                    }

                                    for (int x = (1025 + (i * 101)); x <= (1121 + (i * 101)); x++) {
                                        for (int y = -57; y <= -39; y++) {
                                            for (int z = -500; z <= -489; z++) {
                                                block = world2.getBlockAt(x, y, z);
                                                if (woolColours().contains(block.getType())) {
                                                    block.setType(world2.getBlockAt(872 + ((i + 1) * 111) + (currentRound - 1), -61, -527).getType());
                                                }
                                            }
                                        }
                                    }
                                }

                                if(currentRound == 1){
                                    startCustomPan("push1");
                                }
                                if(currentRound >= 2){
                                    timeLeft = 51;
                                }
                                break;
                            case 52:
//                                resetPushPointRound();
                                if(currentRound == 1) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        player.sendTitle("\uD83E\uDED0", "", 20, 60, 20);
                                    }
                                    playSoundAll(Sound.ENTITY_ARMADILLO_LAND, 1F);
                                }
                                break;
                            case 49:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                if(plugin.currentRound == 1) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        messagePlayer(player, """
                                                §8
                                                §8
                                                §r⏳ §eWelcome to §c§lPush §9§lPoint§e! This is a fight for possession! Work as a team to push the push points to gain a greater portion of land than your opponents!
                                                §8
                                                """);
                                    }
                                }
                                if (plugin.currentRound >= 2) {
                                    World world3 = Bukkit.getWorld("build");
                                    Map<Material, String> concreteToTeam = new HashMap<>();
                                    for (Map.Entry<String, Material> entry : teamConcrete.entrySet()) {
                                        concreteToTeam.put(entry.getValue(), entry.getKey());
                                    }

                                    ppTeamMatchups.clear();

                                    for (int x = -1 + currentRound; x <= 332 + currentRound; x += 111) {
                                        Material leftBlock = world3.getBlockAt(983 + x, -61, -392).getType();
                                        Material rightBlock = world3.getBlockAt(983 + x, -61, -527).getType();

                                        String leftTeam = concreteToTeam.get(leftBlock);
                                        String rightTeam = concreteToTeam.get(rightBlock);

                                        if (leftTeam == null || rightTeam == null) {
                                            plugin.getLogger().warning("[matchup] unrecognized block at x=" + x
                                                    + " left=" + leftBlock + " right=" + rightBlock);
                                            continue;
                                        }
                                        ppTeamMatchups.put(leftTeam, rightTeam);
                                    }

                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        String team = PlayerConfig.get().getString("players." + player.getName() + ".team");
                                        String opponent = null;

                                        if (ppTeamMatchups.containsKey(team)) {
                                            opponent = ppTeamMatchups.get(team);
                                        } else {
                                            for (Map.Entry<String, String> entry : ppTeamMatchups.entrySet()) {
                                                if (entry.getValue().equals(team)) {
                                                    opponent = entry.getKey();
                                                    break;
                                                }
                                            }
                                        }

                                        String matchupText;
                                        if (opponent != null) {
                                            matchupText = getTeamDisplayName(team) + " §f§lᴠs " + getTeamDisplayName(opponent);
                                            player.sendTitle("§f§lɴᴇxᴛ ᴍᴀᴛᴄʜᴜᴘ", getTeamDisplayName(opponent));
                                        } else {
                                            matchupText = "Waiting..";
                                        }

                                        messagePlayer(player, """
            §8
            §8
            §f§lɴᴇxᴛ ᴍᴀᴛᴄʜᴜᴘ
            §r""" + matchupText + """
            §8
            """);
                                    }
                                    timeLeft = 14;
                                }
                                break;
                            case 41:
                                if(currentRound == 1){
                                    startCustomPan("push2");
                                }
                                break;
                            case 36:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §eChoose your kit, and battle it out! You will have to fight for possession, balance pushing the point and fighting your opponent to win!
                                            §8
                                            """);
                                }
                                break;
                            case 27:
                                if(currentRound == 1){
                                    startCustomPan("push3");
                                }
                                break;
                            case 22:
                                World world3 = Bukkit.getWorld("build");
                                StringBuilder matchup = new StringBuilder();

                                Map<Material, String> concreteToTeam = new HashMap<>();
                                for (Map.Entry<String, Material> entry : teamConcrete.entrySet()) {
                                    concreteToTeam.put(entry.getValue(), entry.getKey());
                                }

                                ppTeamMatchups.clear();

                                for (int x = -1 + currentRound; x <= 332 + currentRound; x += 111) {
                                    Material leftBlock = world3.getBlockAt(983 + x, -61, -392).getType();
                                    Material rightBlock = world3.getBlockAt(983 + x, -61, -527).getType();

                                    String leftTeam = concreteToTeam.get(leftBlock);
                                    String rightTeam = concreteToTeam.get(rightBlock);

                                    if (leftTeam == null || rightTeam == null) {
                                        plugin.getLogger().warning("[matchup] unrecognized block at x=" + x
                                                + " left=" + leftBlock + " right=" + rightBlock);
                                        continue;
                                    }
                                    ppTeamMatchups.put(leftTeam, rightTeam);
                                }

                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    String team = PlayerConfig.get().getString("players." + player.getName() + ".team");
                                    String opponent = null;

                                    if (team != null) {
                                        if (ppTeamMatchups.containsKey(team)) {
                                            opponent = ppTeamMatchups.get(team);
                                        } else {
                                            for (Map.Entry<String, String> entry : ppTeamMatchups.entrySet()) {
                                                if (entry.getValue().equals(team)) {
                                                    opponent = entry.getKey();
                                                    break;
                                                }
                                            }
                                        }
                                    }

                                    if (opponent != null) {
                                        matchup.append(getTeamDisplayName(team)).append(" §f§lᴠs ").append(getTeamDisplayName(opponent));
                                    }

                                    messagePlayer(player, """
            §8
            §8
            §f§lғɪʀsᴛ ᴍᴀᴛᴄʜᴜᴘ
            §r""" + matchup + """
            §8
            """);
                                    matchup.setLength(0);
                                }
                                break;
                            case 13:
                                if(currentRound == 1){
                                    for(Player player : getPlayers()){
                                        ItemStack pumpkin = new ItemStack(Material.CARVED_PUMPKIN);
                                        player.getInventory().clear();
                                        player.getInventory().setHelmet(pumpkin);
                                        player.setGameMode(GameMode.ADVENTURE);
                                    }
                                    teamTeleport("pushpoint", 0);
                                    teleportSpectators(TeleportConfig.get().getLocation("spectators.pushpoint"), 0);
                                }
                                break;
                            case 12:
                                resetPushPointRound();
                                ItemStack tankKit = new ItemStack(Material.IRON_CHESTPLATE);
                                ItemMeta tankMeta = tankKit.getItemMeta();
                                tankMeta.setDisplayName("§c§lTank Kit");
                                tankMeta.setLore(Arrays.asList(
                                        "§eClick to select.",
                                        "§7- Iron Chestplate",
                                        "§7- Iron Boots",
                                        "§7- Wooden Sword",
                                        "§7- Crossbow (8 Arrows)"
                                ));
                                tankKit.setItemMeta(tankMeta);

                                ItemStack archerKit = new ItemStack(Material.CROSSBOW);
                                ItemMeta archerMeta = archerKit.getItemMeta();
                                archerMeta.setDisplayName("§a§lArcher Kit");
                                archerMeta.setLore(Arrays.asList(
                                        "§eClick to select.",
                                        "§7- Chainmail Chestplate",
                                        "§7- Iron Boots",
                                        "§7- Wooden Sword",
                                        "§7- Crossbow (18 Arrows)"
                                ));
                                archerKit.setItemMeta(archerMeta);

                                ItemStack duelistKit = new ItemStack(Material.STONE_SWORD);
                                ItemMeta duelistMeta = duelistKit.getItemMeta();
                                duelistMeta.setDisplayName("§6§lDuelist Kit");
                                duelistMeta.setLore(Arrays.asList(
                                        "§eClick to select.",
                                        "§7- Chainmail Chestplate",
                                        "§7- Iron Boots",
                                        "§7- Stone Sword",
                                        "§7- Bow (12 Arrows)"
                                ));
                                duelistKit.setItemMeta(duelistMeta);

                                ItemStack healerKit = new ItemStack(Material.SPLASH_POTION);
                                PotionMeta healerMeta = (PotionMeta) healerKit.getItemMeta();
                                healerMeta.setBasePotionType(PotionType.HEALING);
                                healerMeta.setDisplayName("§d§lHealer Kit");
                                healerMeta.setLore(Arrays.asList(
                                        "§eClick to select.",
                                        "§7- Chainmail Chestplate",
                                        "§7- Iron Boots",
                                        "§7- Wooden Sword",
                                        "§7- Bow (12 Arrows)",
                                        "§7- §cSplash Healing Potion",
                                        "§7- §dSplash Regeneration Potion",
                                        "§7- §eGolden Apple"
                                ));
                                healerKit.setItemMeta(healerMeta);

                                ItemStack flankerKit = new ItemStack(Material.POTION);
                                PotionMeta flankerMeta = (PotionMeta) flankerKit.getItemMeta();
                                flankerMeta.setBasePotionType(PotionType.SWIFTNESS);
                                flankerMeta.setDisplayName("§b§lFlanker Kit");
                                flankerMeta.setLore(Arrays.asList(
                                        "§eClick to select.",
                                        "§7- Leather Chestplate",
                                        "§7- Iron Boots",
                                        "§7- Stone Sword",
                                        "§7- Bow (12 Arrows)",
                                        "§7- §bSwiftness Potion",
                                        "§7- §eGolden Apple"
                                ));
                                flankerKit.setItemMeta(flankerMeta);

                                ItemStack unselected = new ItemStack(Material.GRAY_STAINED_GLASS);
                                ItemMeta unselectedMeta = unselected.getItemMeta();
                                unselectedMeta.setDisplayName("§7§lNo Player.");
                                unselected.setItemMeta(unselectedMeta);

                                for(String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)){
                                    Inventory inventory = Bukkit.createInventory(null, 36, "§f" + getTeamDisplayName(team) + "§f - §eSelect a kit!");
                                    inventory.setItem(11, tankKit);
                                    inventory.setItem(12, archerKit);
                                    inventory.setItem(13, duelistKit);
                                    inventory.setItem(14, healerKit);
                                    inventory.setItem(15, flankerKit);
                                    for(int i = 20; i <= 24; i++){
                                        inventory.setItem(i, unselected);
                                    }
                                    ppTeamKitInventories.put(team, inventory);
                                }
                                String team;
                                for(Player p : getPlayers()){
                                    team = PlayerConfig.get().getString("players." + p.getName() + ".team");
                                    p.openInventory(ppTeamKitInventories.get(team));
                                }
                                targetTime = 0;
                                timerLabel = "Kit Selection:";
                                break;
                            case 10:
                                ppActive = true;
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
                            case 9:
                                playMusicAll(Sound.MUSIC_DISC_WAIT);
                                break;
                            case 5, 4, 3, 2, 1:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 0.3F * (6 - timeLeft));
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendTitle("§c§l▶ " + timeLeft + " ◀", "", 0, 20, 20);
                                }
                                break;
                            case 0:
                                for(Player p : getPlayers()){
                                    p.closeInventory();
                                }
                                for(Integer[] blocks : ppWallDestroyers()){
                                    Bukkit.getWorld("build").getBlockAt(blocks[0], -62, blocks[1]).setType(Material.REDSTONE_BLOCK);
                                    Bukkit.getWorld("build").getBlockAt(blocks[0], -62, blocks[1]).setType(Material.AIR);
                                }
                                List<Player> result = getPlayers().stream()
                                        .filter(player -> ppTeamSelectedKits.values().stream()
                                                .filter(Objects::nonNull)
                                                .noneMatch(map -> map.containsKey(player)))
                                        .toList();

                                for(Player p : result){
                                    giveRandomKit(p.getName());
                                }
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 2);
                                pvpEnabled = true;
                                for (Player player : getPlayers()) {
                                    player.sendTitle("§a§l▶ PUSH! ◀", "", 0, 40, 0);
                                    healFeedPlayer(player);
                                }
                                targetTime = 35;
                                timerLabel = "Final Push:";
                                startTimer(105, "pushpoint");
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if(runningTimers.containsKey("pushpoint")) {
                                            if(!pausedTimers.contains("pushpoint")) {
                                                collectPlayersNearWalls(getPlayers());
                                                try {
                                                    updateWallPositions();
                                                } catch (ReflectiveOperationException e) {
                                                    throw new RuntimeException(e);
                                                }
                                            }
                                        } else {
                                            cancel();
                                        }
                                    }
                                }.runTaskTimer(plugin, 0L, 10L);
                                runningTimers.remove("pushpointstart");
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

        runningTimers.put("pushpointstart", new AbstractMap.SimpleEntry<>(task, 61));
    }

    public void giveRandomKit(String player){
        String team = PlayerConfig.get().getString("players." + player + ".team");
        List<String> kits = new ArrayList<>(List.of("Tank", "Flanker", "Healer", "Archer", "Duelist"));

        plugin.ppTeamSelectedKits.computeIfAbsent(team, k -> new HashMap<Player, String>());

        for (String kit : plugin.ppTeamSelectedKits.get(team).values()) {
            kits.remove(kit);
        }

        Player p = Bukkit.getPlayer(player);
        if(p != null) {
            Random r = new Random();
            String randomKit = kits.get(r.nextInt(kits.size()));
            plugin.ppTeamSelectedKits.get(team).put(p, randomKit);
            givePPKits(p);
        }
    }

    public void givePPKits(Player player) {
        player.getInventory().clear();
        ItemStack arrows = new ItemStack(Material.ARROW, 12);
        ItemStack food = new ItemStack(Material.COOKED_BEEF, 16);
        ItemStack goldenapple = new ItemStack(Material.GOLDEN_APPLE);
        String team = PlayerConfig.get().getString("players." + player.getName() + ".team");
        switch(plugin.ppTeamSelectedKits.get(team).get(player)) {
            case "Tank":
                ItemStack tankChest = new ItemStack(Material.IRON_CHESTPLATE);
                ItemStack tankBoots = new ItemStack(Material.IRON_BOOTS);
                makeUnbreakable(tankChest);
                makeUnbreakable(tankBoots);
                player.getInventory().setChestplate(tankChest);
                player.getInventory().setBoots(tankBoots);

                ItemStack tanksword = new ItemStack(Material.WOODEN_SWORD);
                ItemStack tankcrossbow = new ItemStack(Material.CROSSBOW);
                tankcrossbow.addEnchantment(Enchantment.QUICK_CHARGE, 2);
                ItemStack tankarrows = new ItemStack(Material.ARROW, 8);
                makeUnbreakable(tanksword);
                makeUnbreakable(tankcrossbow);

                player.getInventory().addItem(tanksword, tankcrossbow, food, tankarrows);
                player.sendMessage("§8[§e!§8] §eYou have been allocated the §f§lTanker §ekit!");

                break;

            case "Archer":
                ItemStack archerChest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
                ItemStack archerBoots = new ItemStack(Material.IRON_BOOTS);
                makeUnbreakable(archerChest);
                makeUnbreakable(archerBoots);
                player.getInventory().setChestplate(archerChest);
                player.getInventory().setBoots(archerBoots);

                ItemStack archersword = new ItemStack(Material.WOODEN_SWORD);
                ItemStack archercrossbow = new ItemStack(Material.CROSSBOW);
                archercrossbow.addEnchantment(Enchantment.QUICK_CHARGE, 2);
                ItemStack archerarrows = new ItemStack(Material.ARROW, 6);
                makeUnbreakable(archersword);
                makeUnbreakable(archercrossbow);

                player.getInventory().addItem(archersword, archercrossbow, food, arrows, archerarrows);
                player.sendMessage("§8[§e!§8] §eYou have been allocated the §f§lArcher §ekit!");

                break;

            case "Duelist":
                ItemStack duelistChest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
                ItemStack duelistBoots = new ItemStack(Material.IRON_BOOTS);
                makeUnbreakable(duelistChest);
                makeUnbreakable(duelistBoots);
                player.getInventory().setChestplate(duelistChest);
                player.getInventory().setBoots(duelistBoots);

                ItemStack duelistsword = new ItemStack(Material.STONE_SWORD);
                ItemStack duelistbow = new ItemStack(Material.BOW);
                makeUnbreakable(duelistsword);
                makeUnbreakable(duelistbow);

                player.getInventory().addItem(duelistsword, duelistbow, food, arrows);
                player.sendMessage("§8[§e!§8] §eYou have been allocated the §f§lDuelist §ekit!");
                break;

            case "Healer":
                ItemStack healerChest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
                ItemStack healerBoots = new ItemStack(Material.IRON_BOOTS);
                makeUnbreakable(healerChest);
                makeUnbreakable(healerBoots);
                player.getInventory().setChestplate(healerChest);
                player.getInventory().setBoots(healerBoots);

                ItemStack healersword = new ItemStack(Material.WOODEN_SWORD);
                ItemStack healerbow = new ItemStack(Material.BOW);
                makeUnbreakable(healersword);
                makeUnbreakable(healerbow);

                ItemStack healingpotion = new ItemStack(Material.SPLASH_POTION);
                PotionMeta healingpotionsmeta = (PotionMeta) healingpotion.getItemMeta();
                healingpotionsmeta.setBasePotionType(PotionType.HEALING);
                healingpotion.setItemMeta(healingpotionsmeta);

                ItemStack regenpotion = new ItemStack(Material.SPLASH_POTION);
                PotionMeta regenpotionmeta = (PotionMeta) regenpotion.getItemMeta();
                regenpotionmeta.setBasePotionType(PotionType.WATER);
                regenpotionmeta.setDisplayName("§fSplash Regeneration Potion");
                PotionEffect effect = new PotionEffect(PotionEffectType.REGENERATION, 5 * 20, 1);
                regenpotionmeta.addCustomEffect(effect, true);
                regenpotion.setItemMeta(regenpotionmeta);

                player.getInventory().addItem(healersword, healerbow, food, goldenapple, healingpotion, regenpotion, arrows);
                player.sendMessage("§8[§e!§8] §eYou have been allocated the §f§lHealer §ekit!");
                break;

            case "Flanker":
                ItemStack flankerChest = new ItemStack(Material.LEATHER_CHESTPLATE);
                ItemStack flankerBoots = new ItemStack(Material.IRON_BOOTS);
                makeUnbreakable(flankerChest);
                makeUnbreakable(flankerBoots);
                player.getInventory().setChestplate(flankerChest);
                player.getInventory().setBoots(flankerBoots);

                ItemStack flankersword = new ItemStack(Material.STONE_SWORD);
                ItemStack flankerbow = new ItemStack(Material.BOW);
                makeUnbreakable(flankersword);
                makeUnbreakable(flankerbow);

                ItemStack speedpotions = new ItemStack(Material.POTION, 2);
                PotionMeta speedpotionsmeta = (PotionMeta) speedpotions.getItemMeta();
                speedpotionsmeta.setBasePotionType(PotionType.SWIFTNESS);
                speedpotions.setItemMeta(speedpotionsmeta);

                player.getInventory().addItem(flankersword, flankerbow, food, speedpotions, goldenapple, arrows);
                player.sendMessage("§8[§e!§8] §eYou have been allocated the §f§lFlanker §ekit!");
                break;

            default:
                player.sendMessage("No kit selected!");
                break;
        }
    }

    public void makeUnbreakable(ItemStack item) {
        if (item == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        meta.setUnbreakable(true);
        item.setItemMeta(meta);
    }

    public void startPushPointFinale(){
        fillVotingSpace(5);
        plugin.shopAllowed = false;
        finalPush = false;
        clearInventories();
        setPreviousPlacements();
        resetModeFullPoints();

        World world2 = Bukkit.getWorld("build");

        List<Block> allBlocks = mapSides.values().stream()
                .flatMap(Arrays::stream)
                .toList();

        for (Block b : allBlocks) {
            b.setType(Material.GRAY_CONCRETE);
        }

        for(Integer[] blocks : ppWallSummoners()){
            Bukkit.getWorld("build").getBlockAt(blocks[0], -62, blocks[1]).setType(Material.REDSTONE_BLOCK);
            Bukkit.getWorld("build").getBlockAt(blocks[0], -62, blocks[1]).setType(Material.AIR);
        }

        List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());
        String firstTeam = leaderteams.getFirst();
        String secondTeam = leaderteams.get(1);
        List<String> firstPlayers = TeamsConfig.get().getStringList("teams." + firstTeam + ".players");
        List<String> secondPlayers = TeamsConfig.get().getStringList("teams." + secondTeam + ".players");
//        World world = Bukkit.getWorld("build");
//        Block block;
//        for(int x = 137; x >= -27; x--){
//            for(int y = 195; y >= 127; y--){
//                for(int z = 804; z <= 1345; z++){
//                    block = world.getBlockAt(x,y,z);
//                    if(colourDashBlocks.contains(block.getType())){
//                        block.setType(Material.AIR);
//                    }
//                }
//            }
//        }
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 41;
            @Override
            public void run() {
                if(runningTimers.containsKey("pushpointstart")) {
                    if (!pausedTimers.contains("pushpointstart")) {
                        timeLeft--;
                        runningTimers.get("pushpointstart").setValue(timeLeft);
                        bossBarBgTest();
                        switch (timeLeft) {
                            case 40:
                                finaleTeamTeleportSeparate("pushpointfinale", 5);
                                teleportSpectators(TeleportConfig.get().getLocation("spectators.pushpointfinale"), 5);
                                break;
                            case 35:
                                mapSides.get("map5finale")[0].setType(teamConcrete.get(firstTeam));
                                mapSides.get("map5finale")[1].setType(teamConcrete.get(secondTeam));

                                for(Player p : getPlayers()){
                                    if(!secondPlayers.contains(p.getName()) && !firstPlayers.contains(p.getName())){
                                        ghostManager.addGhostPlayer(p.getName());
                                    }
                                }

                                Block block;
                                for(int x = 1458; x <= 1554; x++){
                                    for(int y = -57; y <= -39; y++){
                                        for(int z = -415; z <= -402; z++){
                                            block = world2.getBlockAt(x,y,z);
                                            if(woolColours().contains(block.getType())){
                                                block.setType(teamConcrete.get(firstTeam));
                                            }
                                        }
                                    }
                                }

                                for(int x = 1458; x <= 1554; x++){
                                    for(int y = -57; y <= -39; y++){
                                        for(int z = -500; z <= -489; z++){
                                            block = world2.getBlockAt(x,y,z);
                                            if(woolColours().contains(block.getType())){
                                                block.setType(teamConcrete.get(secondTeam));
                                            }
                                        }
                                    }
                                }
                                try {
                                    glowTeams();
                                } catch (ReflectiveOperationException e) {
                                    throw new RuntimeException(e);
                                }
                                currentMode = "Push Point";
                                for (Player player : getPlayers()) {
                                    if(!ghostManager.getGhostPlayers().contains(player.getName())) {
                                        PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 90, 1, false, false);
                                        player.addPotionEffect(PotionEffect);
                                        player.setGameMode(GameMode.SURVIVAL);
                                    }
                                }
                                break;
                            case 30:
                                resetPushPointFinale();
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §ePush Point Finale! Dominate possession and be resilient as a team to push the walls furthest!
                                            §8
                                            """);
                                    player.sendTitle("§a§lGreatest Average", "§a§lPush %.", 0, 40, 20);
                                }
                                break;
                            case 20:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §eChoose your kit, only one round to work together to gain the most possession!
                                            §8
                                            """);
                                }
                                break;
                            case 15:
                                String leftTeam;
                                String rightTeam;
                                World world = Bukkit.getWorld("build");
                                StringBuilder matchup = new StringBuilder();

                                Map<Material, String> concreteToTeam = new HashMap<>();

                                for (Map.Entry<String, Material> entry : teamConcrete.entrySet()) {
                                    concreteToTeam.put(entry.getValue(), entry.getKey());
                                }

                                world.getBlockAt(1500, -61, -403).setType(teamConcrete.get(firstTeam));
                                world.getBlockAt(1500, -61, -515).setType(teamConcrete.get(secondTeam));

                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    String team = PlayerConfig.get().getString("players." + player.getName() + ".team");

                                    leftTeam = firstTeam;
                                    rightTeam = secondTeam;

                                    if (team.equals(leftTeam) && rightTeam != null) {
                                        matchup.append(getTeamDisplayName(leftTeam)).append(" §f§lᴠs ").append(getTeamDisplayName(rightTeam));
                                        break;
                                    }

                                    if (team.equals(rightTeam) && leftTeam != null) {
                                        matchup.append(getTeamDisplayName(rightTeam)).append(" §f§lᴠs ").append(getTeamDisplayName(leftTeam));
                                        break;
                                    }
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §f§lᴍᴀᴛᴄʜᴜᴘ
                                            §r""" + matchup + """
                                            §8
                                            """);
                                    matchup.setLength(0);

                                    ppTeamMatchups.put(firstTeam, secondTeam);
                                }
                                break;
                            case 13:
                                ItemStack tankKit = new ItemStack(Material.IRON_CHESTPLATE);
                                ItemMeta tankMeta = tankKit.getItemMeta();
                                tankMeta.setDisplayName("§c§lTank Kit");
                                tankMeta.setLore(Arrays.asList(
                                        "§eClick to select.",
                                        "§7- Iron Chestplate",
                                        "§7- Iron Boots",
                                        "§7- Wooden Sword",
                                        "§7- Crossbow (8 Arrows)"
                                ));
                                tankKit.setItemMeta(tankMeta);

                                ItemStack archerKit = new ItemStack(Material.CROSSBOW);
                                ItemMeta archerMeta = archerKit.getItemMeta();
                                archerMeta.setDisplayName("§a§lArcher Kit");
                                archerMeta.setLore(Arrays.asList(
                                        "§eClick to select.",
                                        "§7- Chainmail Chestplate",
                                        "§7- Iron Boots",
                                        "§7- Wooden Sword",
                                        "§7- Crossbow (18 Arrows)"
                                ));
                                archerKit.setItemMeta(archerMeta);

                                ItemStack duelistKit = new ItemStack(Material.STONE_SWORD);
                                ItemMeta duelistMeta = duelistKit.getItemMeta();
                                duelistMeta.setDisplayName("§6§lDuelist Kit");
                                duelistMeta.setLore(Arrays.asList(
                                        "§eClick to select.",
                                        "§7- Chainmail Chestplate",
                                        "§7- Iron Boots",
                                        "§7- Stone Sword",
                                        "§7- Bow (12 Arrows)"
                                ));
                                duelistKit.setItemMeta(duelistMeta);

                                ItemStack healerKit = new ItemStack(Material.SPLASH_POTION);
                                PotionMeta healerMeta = (PotionMeta) healerKit.getItemMeta();
                                healerMeta.setBasePotionType(PotionType.HEALING);
                                healerMeta.setDisplayName("§d§lHealer Kit");
                                healerMeta.setLore(Arrays.asList(
                                        "§eClick to select.",
                                        "§7- Chainmail Chestplate",
                                        "§7- Iron Boots",
                                        "§7- Wooden Sword",
                                        "§7- Bow (12 Arrows)",
                                        "§7- §cSplash Healing Potion",
                                        "§7- §dSplash Regeneration Potion",
                                        "§7- §eGolden Apple"
                                ));
                                healerKit.setItemMeta(healerMeta);

                                ItemStack flankerKit = new ItemStack(Material.POTION);
                                PotionMeta flankerMeta = (PotionMeta) flankerKit.getItemMeta();
                                flankerMeta.setBasePotionType(PotionType.SWIFTNESS);
                                flankerMeta.setDisplayName("§b§lFlanker Kit");
                                flankerMeta.setLore(Arrays.asList(
                                        "§eClick to select.",
                                        "§7- Leather Chestplate",
                                        "§7- Iron Boots",
                                        "§7- Stone Sword",
                                        "§7- Bow (12 Arrows)",
                                        "§7- §bSwiftness Potion",
                                        "§7- §eGolden Apple"
                                ));
                                flankerKit.setItemMeta(flankerMeta);

                                ItemStack unselected = new ItemStack(Material.GRAY_STAINED_GLASS);
                                ItemMeta unselectedMeta = unselected.getItemMeta();
                                unselectedMeta.setDisplayName("§7§lNo Player.");
                                unselected.setItemMeta(unselectedMeta);

                                for(String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)){
                                    Inventory inventory = Bukkit.createInventory(null, 36, "§f" + getTeamDisplayName(team) + "§f - §eSelect a kit!");
                                    inventory.setItem(11, tankKit);
                                    inventory.setItem(12, archerKit);
                                    inventory.setItem(13, duelistKit);
                                    inventory.setItem(14, healerKit);
                                    inventory.setItem(15, flankerKit);
                                    for(int i = 20; i <= 24; i++){
                                        inventory.setItem(i, unselected);
                                    }
                                    ppTeamKitInventories.put(team, inventory);
                                }
                                String team;
                                for(String player : firstPlayers){
                                    Player p = Bukkit.getPlayer(player);
                                    if(p != null) {
                                    team = PlayerConfig.get().getString("players." + p.getName() + ".team");
                                    p.openInventory(ppTeamKitInventories.get(team));
                                    }
                                }

                                for(String player : secondPlayers){
                                    Player p = Bukkit.getPlayer(player);
                                    if(p != null) {
                                        team = PlayerConfig.get().getString("players." + p.getName() + ".team");
                                        p.openInventory(ppTeamKitInventories.get(team));
                                    }
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
                                ppActive = true;
                                break;
                            case 9:
                                playMusicAll(Sound.MUSIC_DISC_WAIT);
                                break;
                            case 5, 4, 3, 2, 1:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 0.3F * (6 - timeLeft));
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendTitle("§c§l▶ " + timeLeft + " ◀", "", 0, 20, 20);
                                }
                                break;
                            case 0:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 2);
                                for(Integer[] blocks : ppWallDestroyers()){
                                    Bukkit.getWorld("build").getBlockAt(blocks[0], -62, blocks[1]).setType(Material.REDSTONE_BLOCK);
                                    Bukkit.getWorld("build").getBlockAt(blocks[0], -62, blocks[1]).setType(Material.AIR);
                                }
                                List<Player> result = getPlayers().stream()
                                        .filter(player -> ppTeamSelectedKits.values().stream()
                                                .filter(Objects::nonNull)
                                                .noneMatch(map -> map.containsKey(player)))
                                        .filter(player -> firstPlayers.contains(player.getName())
                                                || secondPlayers.contains(player.getName()))
                                        .toList();

                                for(Player p : result){
                                    giveRandomKit(p.getName());
                                }
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 2);
                                pvpEnabled = true;
                                for (Player player : getPlayers()) {
                                    player.sendTitle("§a§l▶ PUSH! ◀", "", 0, 40, 0);
                                    healFeedPlayer(player);
                                }
                                startTimer(105, "pushpoint");
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if(runningTimers.containsKey("pushpoint")) {
                                            if(!pausedTimers.contains("pushpoint")) {
                                                collectPlayersNearWalls(getPlayers());
                                                try {
                                                    updateWallPositions();
                                                } catch (ReflectiveOperationException e) {
                                                    throw new RuntimeException(e);
                                                }
                                            }
                                        } else {
                                            cancel();
                                        }
                                    }
                                }.runTaskTimer(plugin, 0L, 10L);

                                ghostManager.giveCompasses();

                                runningTimers.remove("pushpointstart");
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

        runningTimers.put("pushpointstart", new AbstractMap.SimpleEntry<>(task, 41));
    }

    public void resetPushPointRound(){
        World world = Bukkit.getWorld("build");
        if(currentRound == 1) {
            playerKillCount.clear();
            for (Player p : getPlayers()) {
                playerKillCount.put(p.getName(), 0);
            }
        }
        playerSelectedTeleport.clear();
        ppTeamSelectedKits.clear();
        for(String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)){
            ppTeamSelectedKits.put(team, new HashMap<>());
        }
        ppTeamKitInventories.clear();
        Location loc;
        ItemStack stack = new ItemStack(Material.STICK);
        ItemMeta meta = stack.getItemMeta();
        meta.setItemModel(new NamespacedKey("amongus", "wall2"));
        stack.setItemMeta(meta);
        for(int i = 0; i <= 3; i++){
            loc = new Location(Bukkit.getWorld("build"), 1042.5 + (101*i),-52.65, -450.5);

            Chunk chunk = loc.getChunk();
            if (!chunk.isLoaded()) {
                chunk.load();
            }

            ItemDisplay wall = world.spawn(loc, ItemDisplay.class);
            wall.setItemStack(stack);

            Transformation current = wall.getTransformation();

            Vector3f newScale = new Vector3f(
                    current.getScale().x,
                    current.getScale().y,
                    current.getScale().z
            );

            Vector3f currentTranslation = current.getTranslation();
            Vector3f newTranslation = new Vector3f(
                    currentTranslation.x,
                    currentTranslation.y + (newScale.y - current.getScale().y) / 2f,
                    currentTranslation.z
            );

            float yaw = (float) Math.toRadians(-90);
            Quaternionf rotation = new Quaternionf().rotateY(yaw);

            Transformation newTransformation = new Transformation(
                    newTranslation,
                    rotation,
                    newScale,
                    current.getRightRotation()
            );

            Quaternionf quat = new Quaternionf();
            Transformation transform = new Transformation(
                    new Vector3f(0, 0, 0),
                    quat,
                    new Vector3f(2.0f, 2.0f, 2.0f),
                    quat
            );



//            mapSides.get("map" + (i+1))[0].setType(world.getBlockAt(872 + ((i+1) * 111) + (currentRound-1), -61, -392).getType());
//            mapSides.get("map" + (i+1))[1].setType(world.getBlockAt(872 + ((i+1) * 111) + (currentRound-1), -61, -527).getType());
//
//            Block block;
//            for(int x = (984 + (i * 111)); x <= (1090 + (i * 111)); x++){
//                for(int y = -57; y <= -39; y++){
//                    for(int z = -417; z <= -402; z++){
//                        block = world.getBlockAt(x,y,z);
//                        if(woolColours().contains(block.getType())){
//                            block.setType(world.getBlockAt(872 + ((i+1) * 111) + (currentRound-1), -61, -392).getType());
//                        }
//                    }
//                }
//            }
//
//            for(int x = (984 + (i * 111)); x <= (1090 + (i * 111)); x++){
//                for(int y = -57; y <= -39; y++){
//                    for(int z = -516; z <= -505; z++){
//                        block = world.getBlockAt(x,y,z);
//                        if(woolColours().contains(block.getType())){
//                            block.setType(world.getBlockAt(872 + ((i+1) * 111) + (currentRound-1), -61, -527).getType());
//                        }
//                    }
//                }
//            }



            wall.setTransformation(newTransformation);

            mapWalls.put(wall, "map" + (i+1));

            chunk = loc.getChunk();
            if (!chunk.isLoaded()) {
                chunk.load();
            }

            TextDisplay text = world.spawn(loc.clone().add(0, 2, 0), TextDisplay.class);
            text.setText(concreteChatColors.get(mapSides.get("map" + (i+1))[0].getType()) + "50.00% §f| " + concreteChatColors.get(mapSides.get("map" + (i+1))[1].getType()) + "50.00%");
            text.setBillboard(Display.Billboard.VERTICAL);
            text.setTransformation(transform);
            TextDisplay textPushers = world.spawn(loc.clone().add(0, 1.5, 0), TextDisplay.class);
            textPushers.setText("");
            textPushers.setBillboard(Display.Billboard.VERTICAL);
            textPushers.setTransformation(transform);
            wallTexts.put(wall, text);
            wallPushersTexts.put(wall, textPushers);

            loc = loc.clone().add(31, -6, 0);

            chunk = loc.getChunk();
            if (!chunk.isLoaded()) {
                chunk.load();
            }

            ItemDisplay wall2 = world.spawn(loc, ItemDisplay.class);
            wall2.setItemStack(stack);
            wall2.setTransformation(newTransformation);
            mapWalls.put(wall2, "map" + (i+1));
            TextDisplay text2 = world.spawn(loc.clone().add(0, 2, 0), TextDisplay.class);
            text2.setText(concreteChatColors.get(mapSides.get("map" + (i+1))[0].getType()) + "50.00% §f| " + concreteChatColors.get(mapSides.get("map" + (i+1))[1].getType()) + "50.00%");
            text2.setBillboard(Display.Billboard.VERTICAL);
            text2.setTransformation(transform);
            TextDisplay textPushers2 = world.spawn(loc.clone().add(0, 1.5, 0), TextDisplay.class);
            textPushers2.setText("");
            textPushers2.setBillboard(Display.Billboard.VERTICAL);
            textPushers2.setTransformation(transform);
            wallTexts.put(wall2, text2);
            wallPushersTexts.put(wall2, textPushers2);

            loc = loc.clone().add(31, 6, 0);

            chunk = loc.getChunk();
            if (!chunk.isLoaded()) {
                chunk.load();
            }

            ItemDisplay wall3 = world.spawn(loc, ItemDisplay.class);
            wall3.setItemStack(stack);
            wall3.setTransformation(newTransformation);
            mapWalls.put(wall3, "map" + (i+1));
            TextDisplay text3 = world.spawn(loc.clone().add(0, 2, 0), TextDisplay.class);
            text3.setText(concreteChatColors.get(mapSides.get("map" + (i+1))[0].getType()) + "50.00% §f| " + concreteChatColors.get(mapSides.get("map" + (i+1))[1].getType()) + "50.00%");
            text3.setBillboard(Display.Billboard.VERTICAL);
            text3.setTransformation(transform);
            TextDisplay textPushers3 = world.spawn(loc.clone().add(0, 1.5, 0), TextDisplay.class);
            textPushers3.setText("");
            textPushers3.setBillboard(Display.Billboard.VERTICAL);
            textPushers3.setTransformation(transform);
            wallTexts.put(wall3, text3);
            wallPushersTexts.put(wall3, textPushers3);
        }
    }

    public void resetPushPointFinale(){

        List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());
//        String firstTeam = leaderteams.getFirst();
//        String secondTeam = leaderteams.get(1);

        playerKillCount.clear();
        for(Player p : getPlayers()) {
            playerKillCount.put(p.getName(), 0);
        }
        playerSelectedTeleport.clear();
        ppTeamSelectedKits.clear();
        ppTeamKitInventories.clear();
        Location loc;
        ItemStack stack = new ItemStack(Material.STICK);
        ItemMeta meta = stack.getItemMeta();
        meta.setItemModel(new NamespacedKey("amongus", "wall2"));
        stack.setItemMeta(meta);

        loc = new Location(Bukkit.getWorld("build"), 1475.5, -52.65, -450.5);

        Chunk chunk = loc.getChunk();
        if (!chunk.isLoaded()) {
            chunk.load();
        }

        ItemDisplay wall = loc.getWorld().spawn(loc, ItemDisplay.class);
        wall.setItemStack(stack);

        Transformation current = wall.getTransformation();

        Vector3f newScale = new Vector3f(
                current.getScale().x * 1f,
                current.getScale().y * 1f,
                current.getScale().z * 1f
        );

        Vector3f currentTranslation = current.getTranslation();
        Vector3f newTranslation = new Vector3f(
                currentTranslation.x,
                currentTranslation.y + (newScale.y - current.getScale().y) / 2f,
                currentTranslation.z
        );

        float yaw = (float) Math.toRadians(-90);
        Quaternionf rotation = new Quaternionf().rotateY(yaw);

        Transformation newTransformation = new Transformation(
                newTranslation,
                rotation,
                newScale,
                current.getRightRotation()
        );

        Quaternionf quat = new Quaternionf();
        Transformation transform = new Transformation(
                new Vector3f(0, 0, 0),
                quat,
                new Vector3f(2.0f, 2.0f, 2.0f),
                quat
        );

        World world = Bukkit.getWorld("build");


//        mapSides.get("map5finale")[0].setType(world.getBlockAt(1500, -61, -403).getType());
//        mapSides.get("map5finale")[1].setType(world.getBlockAt(1500, -61, -515).getType());

//        mapSides.get("map5finale")[0].setType(teamConcrete.get(firstTeam));
//        mapSides.get("map5finale")[1].setType(teamConcrete.get(secondTeam));
//
//        Block block;
//        for(int x = 1447; x <= 1540; x++){
//            for(int y = -57; y <= -39; y++){
//                for(int z = -415; z <= -402; z++){
//                    block = world.getBlockAt(x,y,z);
//                    if(woolColours().contains(block.getType())){
//                        block.setType(teamConcrete.get(firstTeam));
//                    }
//                }
//            }
//        }
//
//        for(int x = 1447; x <= 1540; x++){
//            for(int y = -57; y <= -39; y++){
//                for(int z = -516; z <= -505; z++){
//                    block = world.getBlockAt(x,y,z);
//                    if(woolColours().contains(block.getType())){
//                        block.setType(teamConcrete.get(secondTeam));
//                    }
//                }
//            }
//        }


        wall.setTransformation(newTransformation);

        mapWalls.put(wall, "map5finale");
        TextDisplay text = loc.getWorld().spawn(loc.clone().add(0, 2, 0), TextDisplay.class);
        text.setText(concreteChatColors.get(mapSides.get("map5finale")[0].getType()) + "50.00% §f| " + concreteChatColors.get(mapSides.get("map5finale")[1].getType()) + "50.00%");
        text.setBillboard(Display.Billboard.VERTICAL);
        text.setTransformation(transform);
        TextDisplay textPushers = world.spawn(loc.clone().add(0, 1.5, 0), TextDisplay.class);
        textPushers.setText("");
        textPushers.setBillboard(Display.Billboard.VERTICAL);
        textPushers.setTransformation(transform);
        wallTexts.put(wall, text);
        wallPushersTexts.put(wall, textPushers);

        loc = loc.clone().add(31, -6, 0);

        chunk = loc.getChunk();
        if (!chunk.isLoaded()) {
            chunk.load();
        }

        ItemDisplay wall2 = loc.getWorld().spawn(loc, ItemDisplay.class);
        wall2.setItemStack(stack);
        wall2.setTransformation(newTransformation);
        mapWalls.put(wall2, "map5finale");
        TextDisplay text2 = loc.getWorld().spawn(loc.clone().add(0, 2, 0), TextDisplay.class);
        text2.setText(concreteChatColors.get(mapSides.get("map5finale")[0].getType()) + "50.00% §f| " + concreteChatColors.get(mapSides.get("map5finale")[1].getType()) + "50.00%");
        text2.setBillboard(Display.Billboard.VERTICAL);
        text2.setTransformation(transform);
        TextDisplay textPushers2 = world.spawn(loc.clone().add(0, 1.5, 0), TextDisplay.class);
        textPushers2.setText("");
        textPushers2.setBillboard(Display.Billboard.VERTICAL);
        textPushers2.setTransformation(transform);
        wallTexts.put(wall2, text2);
        wallPushersTexts.put(wall2, textPushers2);

        loc = loc.clone().add(31, 6, 0);

        chunk = loc.getChunk();
        if (!chunk.isLoaded()) {
            chunk.load();
        }

        ItemDisplay wall3 = loc.getWorld().spawn(loc, ItemDisplay.class);
        wall3.setItemStack(stack);
        wall3.setTransformation(newTransformation);
        mapWalls.put(wall3, "map5finale");
        TextDisplay text3 = loc.getWorld().spawn(loc.clone().add(0, 2, 0), TextDisplay.class);
        text3.setText(concreteChatColors.get(mapSides.get("map5finale")[0].getType()) + "50.00% §f| " + concreteChatColors.get(mapSides.get("map5finale")[1].getType()) + "50.00%");
        text3.setBillboard(Display.Billboard.VERTICAL);
        text3.setTransformation(transform);
        TextDisplay textPushers3 = world.spawn(loc.clone().add(0, 1.5, 0), TextDisplay.class);
        textPushers3.setText("");
        textPushers3.setBillboard(Display.Billboard.VERTICAL);
        textPushers3.setTransformation(transform);
        wallTexts.put(wall3, text3);
        wallPushersTexts.put(wall3, textPushers3);

    }

//    public void undoFinalPush(String player) {
//        Player p = Bukkit.getPlayer(player);
//        if (p == null) return; // player not online
//
//        String team = PlayerConfig.get().getString("players." + player + ".team");
//
//        Map<ItemDisplay, Double> playerWalls = finalPushMovements.get(player);
//        if (playerWalls == null || playerWalls.isEmpty()) {
//            // No recorded movement for this player
//            messagePlayer(p, """
//            §8
//            §8
//            §f§lScore Lost: §c0.00%
//            §8
//            §8
//            """);
//            return;
//        }
//
//        double totalMovement = 0.0;
//
//        // Undo movements for each wall the player contributed to
//        for (Map.Entry<ItemDisplay, Double> entry : playerWalls.entrySet()) {
//            ItemDisplay wall = entry.getKey();
//            double movement = entry.getValue();
//            TextDisplay text = wallTexts.get(wall);
//            TextDisplay textPusher = wallPushersTexts.get(wall);
//            Block[] mapSideBlocks = mapSides.get(mapWalls.get(wall));
//
//            // Determine which side the player is on
//            Material teamMaterial = teamConcrete.get(team);
//            Location wallLoc = wall.getLocation().clone();
//            Location textLoc = text.getLocation().clone();
//            Location textPusherLoc = textPusher.getLocation().clone();
//
//            if (teamMaterial.equals(mapSideBlocks[0].getType()) && (wallLoc.getZ() + movement) <= PP_PATH_MIN) {
//                wall.teleport(wallLoc.add(0, 0, movement));
//                text.teleport(textLoc.add(0, 0, movement));
//                textPusher.teleport(textPusherLoc.add(0, 0, movement));
//            } else if (teamMaterial.equals(mapSideBlocks[1].getType()) && (wallLoc.getZ() - movement) >= PP_PATH_MAX) {
//                wall.teleport(wallLoc.subtract(0, 0, movement));
//                text.teleport(textLoc.subtract(0, 0, movement));
//                textPusher.teleport(textPusherLoc.subtract(0, 0, movement));
//            }
//
//            // Update wall text
//            double progress = (PP_PATH_MAX - wall.getLocation().getZ()) / (PP_PATH_MAX - PP_PATH_MIN) * 100.0;
//            double opposing = 100.0 - progress;
//            if (Math.abs(progress) < 0.005) progress = 0.0;
//
//            text.setText(
//                    concreteChatColors.get(mapSideBlocks[0].getType())
//                            + String.format("%.2f", opposing)
//                            + "% §f| "
//                            + concreteChatColors.get(mapSideBlocks[1].getType())
//                            + String.format("%.2f", progress)
//                            + "%"
//            );
//
//            totalMovement += movement;
//        }
//
//        // Remove this player's movements now that they've been undone
//        finalPushMovements.remove(player);
//
//        // Show the score lost to the player
//        double progressPercent = totalMovement / (PP_PATH_MAX - PP_PATH_MIN) * 100.0;
//        messagePlayer(p, """
//            §8
//            §8
//            §f§lScore Lost: §c""" + String.format("%.2f", progressPercent) + """
//            §c%
//            §8
//            """);
//    }

    public void collectPlayersNearWalls(Collection<Player> players) {
        playersNearWalls.clear();

        for (Player player : players) {
            if (player.getGameMode().equals(GameMode.SPECTATOR)) continue;

            Location pLoc = player.getLocation();
            String playerName = player.getName();
            if (!PlayerConfig.get().getConfigurationSection("players").getKeys(false).contains(playerName)) continue;

            String team = PlayerConfig.get().getString("players." + playerName + ".team");

            for (ItemDisplay wall : mapWalls.keySet()) {
                if (wall.getWorld() != pLoc.getWorld()) continue;

                if (wall.getLocation().clone().subtract(0, 3.5F, 0).distanceSquared(pLoc) <= 30.25F) {
                    // initialize map for wall if missing
                    playersNearWalls.computeIfAbsent(wall, k -> new HashMap<>());

                    // get list of players for this team at this wall
                    Map<String, List<String>> teamPlayers = playersNearWalls.get(wall);
                    teamPlayers.computeIfAbsent(team, k -> new ArrayList<>()).add(playerName);
                }
            }
        }
    }

    void updateWallPositions() throws ReflectiveOperationException {
        for (ItemDisplay wall : mapWalls.keySet()) {

            Map<String, List<String>> teamPlayers = playersNearWalls.get(wall);
            Block[] mapSideBlocks = mapSides.get(mapWalls.get(wall));
            TextDisplay text = wallTexts.get(wall);
            TextDisplay textPusher = wallPushersTexts.get(wall);

            String teamSide1 = null;
            String teamSide2 = null;
            for (Map.Entry<String, Material> entry : teamConcrete.entrySet()) {
                if (entry.getValue().equals(mapSideBlocks[0].getType())) teamSide1 = entry.getKey();
                if (entry.getValue().equals(mapSideBlocks[1].getType())) teamSide2 = entry.getKey();
            }
            if (teamSide1 == null || teamSide2 == null) continue;

            List<String> allPlayers = new ArrayList<>(TeamsConfig.get().getStringList("teams." + teamSide1 + ".players"));
            allPlayers.addAll(TeamsConfig.get().getStringList("teams." + teamSide2 + ".players"));

            Player p;

            if (!playersNearWalls.containsKey(wall)) {
                for(String player : allPlayers) {
                    if(Bukkit.getPlayer(player) != null) {
                        p = Bukkit.getPlayer(player);
                        glowingEntities.setGlowing(wall, p, ChatColor.GRAY);
                        wallPushersTexts.get(wall).setText("");
                    }
                }
                continue;
            }

            int pushers = teamPlayers.getOrDefault(teamSide1, Collections.emptyList()).size();
            int opponents = teamPlayers.getOrDefault(teamSide2, Collections.emptyList()).size();
            int diff = pushers - opponents;

            wallPushersTexts.get(wall).setText(teamGlowColors.get(teamSide1).toString() + "⬤".repeat(pushers) + teamGlowColors.get(teamSide2).toString() + "⬤".repeat(opponents));

            for(String player : allPlayers){
                if(Bukkit.getPlayer(player) != null){
                    p = Bukkit.getPlayer(player);
                    if(pushers > opponents) {
                        glowingEntities.setGlowing(wall, p, teamGlowColors.get(teamSide1));
                    }
                    if (opponents > pushers){
                        glowingEntities.setGlowing(wall, p, teamGlowColors.get(teamSide2));
                    }
                    if(diff == 0){
                        glowingEntities.setGlowing(wall, p, ChatColor.GRAY);
                    }
                }
            }

            if (diff != 0) {
                int advantage = Math.abs(diff);
                double multiplier = calculateMovementMultiplier(advantage);
                double movement = 0.125 * multiplier;

                if (diff > 0 && (wall.getLocation().getZ() - movement) >= PP_PATH_MAX) {
                    wall.teleport(wall.getLocation().clone().subtract(0, 0, movement));
                    text.teleport(text.getLocation().clone().subtract(0, 0, movement));
                    textPusher.teleport(textPusher.getLocation().clone().subtract(0, 0, movement));


                    Location loc = wall.getLocation();

                    loc.setYaw(0);

                    wall.teleport(loc);

                    List<String> pushersList = teamPlayers.get(teamSide1);

                    if (finalPush) {
                        if (pushersList != null && !pushersList.isEmpty()) {
                            double perPlayerMovement = movement / pushersList.size();
                            for (String playerName : pushersList) {
                                finalPushMovements
                                        .computeIfAbsent(playerName, k -> new HashMap<>())
                                        .merge(wall, perPlayerMovement, Double::sum);
                            }
                        }
                    }
                    if (pushersList != null && !pushersList.isEmpty()) {
                        Player player;
                        for (String playerName : pushersList) {
                            player = Bukkit.getPlayer(playerName);
                            if(player != null){
                                player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_HURT_CLOSED, 1F, 1F);
                            }
                        }
                    }

                } else if (diff < 0 && (wall.getLocation().getZ() + movement) <= PP_PATH_MIN) {
                    wall.teleport(wall.getLocation().clone().add(0, 0, movement));
                    text.teleport(text.getLocation().clone().add(0, 0, movement));
                    textPusher.teleport(textPusher.getLocation().clone().add(0, 0, movement));
                    List<String> pushersList = teamPlayers.get(teamSide2);

                    Location loc = wall.getLocation();

                    loc.setYaw(180);

                    wall.teleport(loc);

                    if (finalPush) {
                        if (pushersList != null && !pushersList.isEmpty()) {
                            double perPlayerMovement = movement / pushersList.size();
                            for (String playerName : pushersList) {
                                finalPushMovements
                                        .computeIfAbsent(playerName, k -> new HashMap<>())
                                        .merge(wall, perPlayerMovement, Double::sum);
                            }
                        }
                    }
                    if (pushersList != null && !pushersList.isEmpty()) {
                        Player player;
                        for (String playerName : pushersList) {
                            player = Bukkit.getPlayer(playerName);
                            if(player != null){
                                player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_HURT_CLOSED, 1F, 1F);
                            }
                        }
                    }
                }
            }

            double progress = (PP_PATH_MAX - wall.getLocation().getZ()) / (PP_PATH_MAX - PP_PATH_MIN) * 100.0;
            double opposing = 100.0 - progress;
            if (Math.abs(progress) < 0.01) progress = 0.00;

            text.setText(
                    concreteChatColors.get(mapSideBlocks[0].getType())
                            + String.format("%.2f", opposing)
                            + "% §f| "
                            + concreteChatColors.get(mapSideBlocks[1].getType())
                            + String.format("%.2f", progress)
                            + "%"
            );
        }
    }

    private double calculateMovementMultiplier(int advantage) {
        return switch (advantage) {
            case 1 -> finalPush ? 1.2 : 1.0;
            case 2 -> finalPush ? 1.9 : 1.7;
            case 3 -> finalPush ? 2.4 : 2.2;
            default -> finalPush ? 2.8 : 2.5;
        };
    }

    public String getPlayerCraftlist(String player) {
        List<String> playerCraftlist = plugin.itemsToCraft.get(player);

        if (playerCraftlist == null || playerCraftlist.isEmpty()) {
            return "§eHead to Edguard the Villager!";
        }

        StringBuilder craftlist = new StringBuilder();

        List<String> easy = CraftalotConfig.get().getStringList("craftlist.0");
        List<String> medium = CraftalotConfig.get().getStringList("craftlist.1");
        List<String> hard = CraftalotConfig.get().getStringList("craftlist.2");

        for (int index = 0; index < playerCraftlist.size(); index++) {
            String craftable = playerCraftlist.get(index);
            String text = craftable.replace("_", " ");

            if (easy.contains(craftable)) {
                craftlist.append("§a").append(text);
            } else if (medium.contains(craftable)) {
                craftlist.append("§6").append(text);
            } else if (hard.contains(craftable)) {
                craftlist.append("§c").append(text);
            } else {
                craftlist.append("§7").append(text); // fallback
            }

            if (index < playerCraftlist.size() - 1) {
                craftlist.append(" ");
            }
        }

        return craftlist.toString();
    }


    public void startCraftalot(){
        currentRound = 1;
        pvpArenaManager.disablePvPArena();
        multiplier = GameOrderConfig.get().getDouble("multiplier");
        fillVotingSpace(2);
        musicManager.stopMusicAll();
        setPreviousPlacements();
        plugin.shopAllowed = false;
        clearInventories();
        resetModeFullPoints();
        targetTime = 0;
        timerLabel = "Game Explanation:";
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
                                List<NamespacedKey> keys = new ArrayList<>();
                                plugin.getServer().recipeIterator().forEachRemaining(recipe -> {
                                    if (recipe instanceof Keyed keyed) {
                                        keys.add(keyed.getKey());
                                    }
                                });
                                for (Player player : getPlayers()) {
                                    ItemStack pumpkin = new ItemStack(Material.CARVED_PUMPKIN);
                                    player.getInventory().clear();
                                    player.getInventory().setHelmet(pumpkin);
                                    ghostManager.removeGhostPlayer(player.getName());
                                    player.discoverRecipes(keys);
                                }
                                startCustomPan("craft1");
                                break;
                            case 52:
                                if(currentRound == 1) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        player.sendTitle("\ue238", "", 20, 60, 20);
                                    }
                                    playSoundAll(Sound.ENTITY_ARMADILLO_LAND, 1F);
                                }
                                break;
                            case 49:
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
                            case 41:
                                startCustomPan("craft2");
                                break;
                            case 36:
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
                            case 27:
                                startCustomPan("craft3");
                                break;
                            case 22:
                                setupCraftlist();
                                break;
                            case 13:
                                teleportPlayers(TeleportConfig.get().getLocation("players.craftalot"), 0);
                                teleportSpectators(TeleportConfig.get().getLocation("spectators.craftalot"), 0);
                                for(Player player : getPlayers()){
                                    player.setGameMode(GameMode.SURVIVAL);
                                    ItemStack air = new ItemStack(Material.AIR);
                                    player.getInventory().clear();
                                    player.getInventory().setHelmet(air);
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
                                targetTime = 0;
                                timerLabel = "Game End:";
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

    public void startCraftalotFinale(){
        fillVotingSpace(2);
        setPreviousPlacements();
        List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());
        String firstTeam = leaderteams.getFirst();
        String secondTeam = leaderteams.get(1);
        List<String> firstPlayers = TeamsConfig.get().getStringList("teams." + firstTeam + ".players");
        List<String> secondPlayers = TeamsConfig.get().getStringList("teams." + secondTeam + ".players");
        teamCrafts.put(firstTeam, 0);
        teamCrafts.put(secondTeam, 0);
        shopAllowed = false;
        clearInventories();
        resetModeFullPoints();
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 41;
            @Override
            public void run() {
                if(runningTimers.containsKey("craftalotstart")) {
                    if (!pausedTimers.contains("craftalotstart")) {
                        timeLeft--;
                        runningTimers.get("craftalotstart").setValue(timeLeft);
                        bossBarBgTest();
                        switch (timeLeft){
                            case 40:
                                for(int z = 714; z <= 724; z++){
                                    for(int y = 134; y <= 137; y++){
                                        Bukkit.getWorld("build").getBlockAt(1360, y, z).setType(Material.BARRIER);
                                    }
                                }
                                finaleTeamTeleport("craftalotfinale", 5);
                                teleportSpectators(TeleportConfig.get().getLocation("spectators.craftalotfinale"), 5);
                                resetCraftalot();
                                resetModePoints();
                                break;
                            case 35:
                                try {
                                    glowTeams();
                                } catch (ReflectiveOperationException e) {
                                    throw new RuntimeException(e);
                                }

                                for(Player p : getPlayers()){
                                    if(!secondPlayers.contains(p.getName()) && !firstPlayers.contains(p.getName())){
                                        ghostManager.addGhostPlayer(p.getName());
                                    }
                                }

                                currentMode = "Craftalot";
                                for (Player player : getPlayers()) {
                                    if(!ghostManager.getGhostPlayers().contains(player.getName())) {
                                        PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 90, 1, false, false);
                                        player.addPotionEffect(PotionEffect);
                                        player.setGameMode(GameMode.SURVIVAL);
                                    }
                                }
                                break;

                            case 30:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §eCraftalot Finale! Everyone has the same craft list, first team to 12 crafts wins!
                                            §8
                                            """);
                                    player.sendTitle("§a§lFirst Team to", "§e§l12 §a§lCrafts.", 0, 40, 20);
                                }
                                break;
                            case 20:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §eRun to Edguard and right-click him to get your orders, but check the map as the tunnels have changed!
                                            §8
                                            """);
                                }
                                break;
                            case 17:
                                setupFinaleCraftlist();
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
                                Player playerTeam1, playerTeam2;
                                for(String player : firstPlayers){
                                    for(String player2 : secondPlayers){
                                        if(Bukkit.getPlayer(player) != null && Bukkit.getPlayer(player2) != null){
                                            playerTeam1 = Bukkit.getPlayer(player);
                                            playerTeam2 = Bukkit.getPlayer(player2);
                                            playerTeam1.hidePlayer(plugin, playerTeam2);
                                            playerTeam2.hidePlayer(plugin, playerTeam1);
                                        }
                                    }
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
                                    if(!ghostManager.getGhostPlayers().contains(player.getName())) {
                                        player.getInventory().clear();
                                        for (int i = 0; i <= 3; i++) {
                                            player.getInventory().addItem(craftalotKit()[i]);
                                        }
                                    }
                                    player.getInventory().setItemInOffHand(craftalotKit()[4]);
                                    PotionEffect PotionEffect = new PotionEffect(PotionEffectType.WATER_BREATHING, 12000, 5, false, false);
                                    player.addPotionEffect(PotionEffect);
                                    PotionEffect = new PotionEffect(PotionEffectType.NIGHT_VISION, 12000, 1, false, false);
                                    player.addPotionEffect(PotionEffect);
                                }
                                for(int z = 714; z <= 724; z++){
                                    for(int y = 134; y <= 137; y++){
                                        Bukkit.getWorld("build").getBlockAt(1360, y, z).setType(Material.AIR);
                                    }
                                }

                                ghostManager.giveCompasses();

                                startCraftalotFinaleTimer();

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

    public void startCraftalotFinaleTimer() {

        List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());
        String firstTeam = leaderteams.getFirst();
        String secondTeam = leaderteams.get(1);
        List<String> firstPlayers = TeamsConfig.get().getStringList("teams." + firstTeam + ".players");
        List<String> secondPlayers = TeamsConfig.get().getStringList("teams." + secondTeam + ".players");

        List<String> allFinalePlayers = new ArrayList<>();

        allFinalePlayers.addAll(firstPlayers);
        allFinalePlayers.addAll(secondPlayers);

        craftFinaleTimer = Bukkit.getScheduler().runTaskTimer(plugin, () -> {

            for (String player : allFinalePlayers) {

                Player p = Bukkit.getPlayer(player);

                if (p == null) continue;

                String itemname = "§e§l" + plugin.itemToCraft.get(player).replaceAll("_", " ");

                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(itemname));

            }

        }, 0L, 1L);

        runningTimers.put("craftalotFinaleTimer", new AbstractMap.SimpleEntry<>(craftFinaleTimer, 0));
    }


    public void setupCraftlist(){
        craftList.clear();
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
                                itemToCraft = CraftalotConfig.get().getStringList("craftlist.0").get(r.nextInt(CraftalotConfig.get().getStringList("craftlist.0").size()));
                            } while (craftList.contains(itemToCraft));
                            craftList.add(itemToCraft);
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.sendTitle("§f§lsʜᴜғғʟɪɴɢ ᴄʀᴀғᴛs", "§b§l" + itemToCraft.replaceAll("_", " "), 0, 40, 0);
                            }
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 1F);
                        }
                        switch (timeLeft) {
                            case 310:
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendTitle("§f§lsʜᴜғғʟɪɴɢ ᴄʀᴀғᴛs", "§7§k0000000", 0, 40, 0);
                                }
                                playSoundAll(Sound.ENTITY_CREEPER_PRIMED, 1F);
                                break;
                            case 289, 279, 269, 266, 263:
                                do {
                                    itemToCraft = CraftalotConfig.get().getStringList("craftlist.1").get(r.nextInt(CraftalotConfig.get().getStringList("craftlist.1").size()));
                                } while (craftList.contains(itemToCraft));
                                craftList.add(itemToCraft);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendTitle("§f§lsʜᴜғғʟɪɴɢ ᴄʀᴀғᴛs", "§b§l" + itemToCraft.replaceAll("_", " "), 0, 20, 0);
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
                            List<String> easyCrafts = CraftalotConfig.get().getStringList("craftlist.0");
                            Collections.shuffle(easyCrafts);
                            easyCrafts.add("[COMPLETE]");
                            craftDifficultyLists.put(0, easyCrafts);
                            List<String> mediumCrafts = CraftalotConfig.get().getStringList("craftlist.1");
                            Collections.shuffle(mediumCrafts);
                            mediumCrafts.add("[COMPLETE]");
                            craftDifficultyLists.put(1, mediumCrafts);
                            List<String> hardCrafts = CraftalotConfig.get().getStringList("craftlist.2");
                            Collections.shuffle(hardCrafts);
                            hardCrafts.add("[COMPLETE]");
                            craftDifficultyLists.put(2, hardCrafts);
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

    public void setupFinaleCraftlist(){
        finaleCraftList.clear();
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
                                itemToCraft = CraftalotConfig.get().getStringList("craftlist.0").get(r.nextInt(CraftalotConfig.get().getStringList("craftlist.0").size()));
                            } while (finaleCraftList.contains(itemToCraft));
                            finaleCraftList.add(itemToCraft);
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.sendTitle("§f§lsʜᴜғғʟɪɴɢ ᴄʀᴀғᴛs", "§b§l" + itemToCraft.replaceAll("_", " "), 0, 40, 0);
                            }
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 1F);
                        }
                        switch (timeLeft) {
                            case 310:
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendTitle("§f§lsʜᴜғғʟɪɴɢ ᴄʀᴀғᴛs", "§7§k0000000", 0, 40, 0);
                                }
                                playSoundAll(Sound.ENTITY_CREEPER_PRIMED, 1F);
                                break;
                            case 289, 279, 269, 266, 263:
                                do {
                                    itemToCraft = CraftalotConfig.get().getStringList("craftlist.1").get(r.nextInt(CraftalotConfig.get().getStringList("craftlist.1").size()));
                                } while (finaleCraftList.contains(itemToCraft));
                                finaleCraftList.add(itemToCraft);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendTitle("§f§lsʜᴜғғʟɪɴɢ ᴄʀᴀғᴛs", "§b§l" + itemToCraft.replaceAll("_", " "), 0, 20, 0);
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
        craftDifficultyLists.clear();
        itemsToCraft.clear();
        itemToCraft.clear();
        craftLists.clear();
        playerCrafts.clear();
        for(Player player : getPlayers()) {
            playerCrafts.put(player.getName(), Arrays.asList(0, 0, 0));
            itemToCraft.put(player.getName(), "");
            itemsToCraft.put(player.getName(), new ArrayList<>());
            craftLists.put(player.getName(), new ArrayList<>());
        }
        craftTop.clear();
    }

    public String toPrettyCase(String input) {
        String[] words = input.toLowerCase().split("_");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            result.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }

        return result.toString().trim();
    }

    public void startBridgeBuilders(){
        currentRound = 1;
        pvpArenaManager.disablePvPArena();
        multiplier = GameOrderConfig.get().getDouble("multiplier");
        fillVotingSpace(0);
        musicManager.stopMusicAll();
        setPreviousPlacements();
        plugin.shopAllowed = false;
        clearInventories();
        resetModeFullPoints();
        targetTime = 0;
        timerLabel = "Game Explanation:";
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
                                    ItemStack pumpkin = new ItemStack(Material.CARVED_PUMPKIN);
                                    player.getInventory().clear();
                                    player.getInventory().setHelmet(pumpkin);
                                    ghostManager.removeGhostPlayer(player.getName());
                                }
                                startCustomPan("bridge1");
                                break;
                            case 54:
                                for (int i = 661; i >= 471; i -= 38) {
                                    for (int j = 244; j <= 489; j += 35) {
                                        Bukkit.getWorld("build").getBlockAt(j, -23, i).setType(Material.REDSTONE_BLOCK);
                                        Bukkit.getWorld("build").getBlockAt(j, -23, i).setType(Material.STONE);
                                    }
                                }
                                break;
                            case 52:
                                if(currentRound == 1) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        player.sendTitle("\uD83C\uDF45", "", 20, 60, 20);
                                    }
                                    playSoundAll(Sound.ENTITY_ARMADILLO_LAND, 1F);
                                }
                                break;
                            case 49:
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
                            case 41:
                                startCustomPan("bridge2");
                                break;
                            case 36:
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
                            case 27:
                                startCustomPan("bridge3");
                                break;
                            case 22:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §eStuck on a build? Incorrect block placements will be marked with a red glow to help you speedily build those jumps!
                                            §8
                                            """);
                                }
                                break;
                            case 13:
                                teamTeleport("bridgebuilders", 0);
                                teleportSpectators(TeleportConfig.get().getLocation("spectators.bridgebuilders"), 0);
                                for(Player player : getPlayers()){
                                    player.setGameMode(GameMode.SURVIVAL);
                                    ItemStack air = new ItemStack(Material.AIR);
                                    player.getInventory().clear();
                                    player.getInventory().setHelmet(air);
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
                                targetTime = 0;
                                timerLabel = "Game End:";
                                startTimer(450, "bridgebuilders");
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

    public void startBridgeBuildersFinale(){
        fillVotingSpace(0);
        setPreviousPlacements();
        List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());
        String firstTeam = leaderteams.getFirst();
        String secondTeam = leaderteams.get(1);
        List<String> firstPlayers = TeamsConfig.get().getStringList("teams." + firstTeam + ".players");
        List<String> secondPlayers = TeamsConfig.get().getStringList("teams." + secondTeam + ".players");
        plugin.shopAllowed = false;
        clearInventories();
        resetModeFullPoints();
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 41;
            @Override
            public void run() {
                if(runningTimers.containsKey("bridgebuildersstart")) {
                    if (!pausedTimers.contains("bridgebuildersstart")) {
                        timeLeft--;
                        runningTimers.get("bridgebuildersstart").setValue(timeLeft);
                        bossBarBgTest();
                        switch (timeLeft) {
                            case 40:
                                finaleTeamTeleportSeparate("bridgebuildersfinale", 5);
                                teleportSpectators(TeleportConfig.get().getLocation("spectators.bridgebuildersfinale"), 5);
                                resetBridgeBuilders();
                                resetModePoints();
                                break;
                            case 35:
                                try {
                                    glowTeams();
                                } catch (ReflectiveOperationException e) {
                                    throw new RuntimeException(e);
                                }

                                for(Player p : getPlayers()){
                                    if(!secondPlayers.contains(p.getName()) && !firstPlayers.contains(p.getName())){
                                        ghostManager.addGhostPlayer(p.getName());
                                    }
                                }

                                currentMode = "Bridge Builders";
                                for (Player player : getPlayers()) {
                                    if(!ghostManager.getGhostPlayers().contains(player.getName())) {
                                        PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 90, 1, false, false);
                                        player.addPotionEffect(PotionEffect);
                                        player.setGameMode(GameMode.ADVENTURE);
                                    }
                                }
                                break;
                            case 34:
                                for (int i = 900; i >= 824; i -= 38) {
                                    for (int j = 2050; j <= 2085; j += 35) {
                                        Bukkit.getWorld("build").getBlockAt(j, -39, i).setType(Material.REDSTONE_BLOCK);
                                        Bukkit.getWorld("build").getBlockAt(j, -39, i).setType(Material.STONE);
                                    }
                                }
                                break;
                            case 30:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §eBridge Builders Finale! New courses, no time limit.. it's a race to the finish!
                                            §8
                                            """);
                                    player.sendTitle("§a§lFirst Team to", "§a§lComplete §e§l3 §a§lSections.", 0, 40, 20);
                                }

                                World world = Bukkit.getWorld("build");
                                for(int x = 0; x < 2; x++){
                                    for(int z = 0; z < 3; z++){

                                        for(int x2 = 2040+(35*x); x2 <= 2046+(35*x); x2++){
                                            for(int y2 = -37; y2 <= -28; y2++){
                                                for(int z2 = 903-(38*z); z2 <= 916 - (38*z); z2++){
                                                    world.getBlockAt(x2, y2, z2).setType(Material.AIR);
                                                }
                                            }
                                        }
                                    }
                                }
                                break;
                            case 20:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §eBe the quickest team to complete the builds and the jumps!
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
                                Player playerTeam1, playerTeam2;
                                for(String player : firstPlayers){
                                    for(String player2 : secondPlayers){
                                        if(Bukkit.getPlayer(player) != null && Bukkit.getPlayer(player2) != null){
                                            playerTeam1 = Bukkit.getPlayer(player);
                                            playerTeam2 = Bukkit.getPlayer(player2);
                                            playerTeam1.hidePlayer(plugin, playerTeam2);
                                            playerTeam2.hidePlayer(plugin, playerTeam1);
                                        }
                                    }
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
                                    if(!ghostManager.getGhostPlayers().contains(player.getName())) {
                                        player.setGameMode(GameMode.SURVIVAL);
                                    }
                                    player.setAllowFlight(true);
                                    player.sendTitle("§a§l▶ BUILD! ◀", "", 0, 40, 0);
                                    if(firstPlayers.contains(player.getName()) || secondPlayers.contains(player.getName())) {
                                        for(Material block : getBridgeBlocksFinale(0, PlayerConfig.get().getString("players." + player.getName() + ".team"))) {
                                            player.getInventory().addItem(new ItemStack(block, 64));
                                        }
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

                                ghostManager.giveCompasses();

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
        currentSpleef = "&fWaiting..";
        deadPlayers.clear();
        deadTeams.clear();
        crumbleBlockRecords.clear();
        if(currentRound == 1) {
            playerKillCount.clear();
            for (Player player : getPlayers()) {
                playerKillCount.put(player.getName(), 0);
            }
        }
        crumbleKillTracker.clear();
        // Reset Stuff
    }

    public List<ItemStack> getCrumbleKits(int map){
        List<ItemStack> kit = new ArrayList<>();


//        crumbleMapModifiers.put(1, new ArrayList<>(List.of("Pickaxe", "Fishing Rods")));
//        crumbleMapModifiers.put(2, new ArrayList<>(List.of("Shovel", "Snowballs")));
//        crumbleMapModifiers.put(3, new ArrayList<>(List.of("Pickaxe", "Shovel", "Fireballs", "Soul Speed")));
//        crumbleMapModifiers.put(4, new ArrayList<>(List.of("Pickaxe", "Enderpearls")));
//        crumbleMapModifiers.put(5, new ArrayList<>(List.of("Decay Boots", "Mace", "Wind Charges")));
        ItemStack item;
        ItemMeta meta;

        switch (map){
            case 1:
                item = new ItemStack(Material.DIAMOND_PICKAXE);
                meta = item.getItemMeta();
                meta.setUnbreakable(true);
                meta.addEnchant(Enchantment.EFFICIENCY, 100, true);
                meta.setDisplayName("§bDiamond Pickaxe");
                item.setItemMeta(meta);

                kit.add(item);

                item = new ItemStack(Material.FISHING_ROD);
                meta = item.getItemMeta();
                meta.setUnbreakable(true);
                meta.setDisplayName("§bFishing Rod");
                item.setItemMeta(meta);

                kit.add(item);

                item = new ItemStack(Material.WIND_CHARGE, 3);
                meta = item.getItemMeta();
                meta.setDisplayName("§fWind Charge");
                item.setItemMeta(meta);

                kit.add(item);
                break;
            case 2:
                item = new ItemStack(Material.DIAMOND_SHOVEL);
                meta = item.getItemMeta();
                meta.setUnbreakable(true);
                meta.addEnchant(Enchantment.EFFICIENCY, 100, true);
                meta.setDisplayName("§fDiamond Shovel");
                item.setItemMeta(meta);

                kit.add(item);

                item = new ItemStack(Material.WIND_CHARGE, 3);
                meta = item.getItemMeta();
                meta.setDisplayName("§fWind Charge");
                item.setItemMeta(meta);

                kit.add(item);
                break;
            case 3:
                item = new ItemStack(Material.DIAMOND_SHOVEL);
                meta = item.getItemMeta();
                meta.setUnbreakable(true);
                meta.addEnchant(Enchantment.EFFICIENCY, 100, true);
                meta.setDisplayName("§cDiamond Shovel");
                item.setItemMeta(meta);

                kit.add(item);

                item = new ItemStack(Material.DIAMOND_PICKAXE);
                meta = item.getItemMeta();
                meta.setUnbreakable(true);
                meta.addEnchant(Enchantment.EFFICIENCY, 100, true);
                meta.setDisplayName("§cDiamond Pickaxe");
                item.setItemMeta(meta);

                kit.add(item);

                item = new ItemStack(Material.GOLDEN_BOOTS);
                meta = item.getItemMeta();
                meta.setUnbreakable(true);
                meta.addEnchant(Enchantment.SOUL_SPEED, 2, true);
                meta.setDisplayName("§cSoul Speed Boots");
                item.setItemMeta(meta);

                kit.add(item);

                item = new ItemStack(Material.WIND_CHARGE, 3);
                meta = item.getItemMeta();
                meta.setDisplayName("§fWind Charge");
                item.setItemMeta(meta);

                kit.add(item);
                break;
            case 4:
                item = new ItemStack(Material.DIAMOND_PICKAXE);
                meta = item.getItemMeta();
                meta.setUnbreakable(true);
                meta.addEnchant(Enchantment.EFFICIENCY, 100, true);
                meta.setDisplayName("§aDiamond Pickaxe");
                item.setItemMeta(meta);

                kit.add(item);

                item = new ItemStack(Material.ENDER_PEARL, 3);
                meta = item.getItemMeta();
                meta.setDisplayName("§aEnder Pearl");
                item.setItemMeta(meta);

                kit.add(item);
                break;
            case 5:
                item = new ItemStack(Material.MACE);
                meta = item.getItemMeta();
                meta.setUnbreakable(true);
                meta.addEnchant(Enchantment.WIND_BURST, 1, true);
                meta.setDisplayName("§6Mace");
                item.setItemMeta(meta);

                kit.add(item);

                item = new ItemStack(Material.COPPER_BOOTS);
                meta = item.getItemMeta();
                meta.setUnbreakable(true);
                meta.setDisplayName("§6Copper Decay Boots");
                item.setItemMeta(meta);

                kit.add(item);

                item = new ItemStack(Material.WIND_CHARGE, 1);
                meta = item.getItemMeta();
                meta.setDisplayName("§fWind Charge");
                item.setItemMeta(meta);

                kit.add(item);
                break;
            default:
                break;
        }

        return kit;
    }

    private Map<UUID, Map<Block, Long>> lastDecayTime = new HashMap<>();

    public Set<Block> getSupportingBlocks(Player player) {
        Set<Block> supportingBlocks = new HashSet<>();
        Location loc = player.getLocation();
        World world = loc.getWorld();
        if (world == null) return supportingBlocks;

        double y = loc.getY() - 0.5; // just below feet
        double[] offsets = {-0.3, 0.0, 0.3}; // sample points around feet

        for (double xOff : offsets) {
            for (double zOff : offsets) {
                int bx = (int) Math.floor(loc.getX() + xOff);
                int by = (int) Math.floor(y);
                int bz = (int) Math.floor(loc.getZ() + zOff);

                Block block = world.getBlockAt(bx, by, bz);
                if (block.getType().isSolid()) supportingBlocks.add(block);
            }
        }

        return supportingBlocks;
    }

    public void decayBlock(Block block) {

        Material type = block.getType();

        switch (type) {
            case WAXED_COPPER_BLOCK:
                block.setType(Material.WAXED_EXPOSED_COPPER);
                break;
            case WAXED_EXPOSED_COPPER:
                block.setType(Material.WAXED_WEATHERED_COPPER);
                break;
            case WAXED_WEATHERED_COPPER:
                block.setType(Material.WAXED_OXIDIZED_COPPER);
                break;
            case WAXED_OXIDIZED_COPPER:
                block.setType(Material.AIR);
                break;
            default:
                break;
        }
    }

    public void handleDecay(Player player) {
        UUID uuid = player.getUniqueId();
        Set<Block> currentBlocks = getSupportingBlocks(player);

        if (currentBlocks.isEmpty()) return;

        if(plugin.ghostManager.getGhostPlayers().contains(player.getName())) { return; }

        Map<Block, Long> playerLastDecay = lastDecayTime.computeIfAbsent(uuid, k -> new HashMap<>());

        for (Block block : currentBlocks) {

            if (block.getType() == Material.AIR) continue;

            // Skip if a decay task is already scheduled for this block
            if (scheduledDecay.containsKey(block)) continue;

            // Check last decay time (actual decay)
            Long lastTime = playerLastDecay.get(block);
            Long now = System.currentTimeMillis();
            if (lastTime != null && now - lastTime < 500) continue;

            // Schedule decay
            BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, () -> {
                decayBlock(block);
                playerLastDecay.put(block, System.currentTimeMillis()); // update actual decay time
                scheduledDecay.remove(block); // allow rescheduling after decay
            }, 10L);

            // Mark block as scheduled
            scheduledDecay.put(block, task);
        }
    }

    public void giveCrumbleKits(int map){
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 0;
            ItemStack[] armour;
            ItemStack item;
            @Override
            public void run() {
                if(runningTimers.containsKey("crumblekit")) {
                    if (!pausedTimers.contains("crumblekit")) {
                        runningTimers.get("crumblekit").setValue(timeLeft);
                        if(timeLeft < getCrumbleKits(map).size()){
                            for(Player p : getPlayers()) {
                                p.setCooldown(Material.SNOWBALL, 1);
                                if (!ghostManager.getGhostPlayers().contains(p.getName())) {
                                    item = getCrumbleKits(map).get(timeLeft);
                                    String name = item.getType().name();
                                    if (name.endsWith("_BOOTS")) {
                                        armour = new ItemStack[]{
                                                item,
                                                p.getInventory().getArmorContents()[1],
                                                p.getInventory().getArmorContents()[2],
                                                p.getInventory().getArmorContents()[3]
                                        };
                                        p.getInventory().setArmorContents(armour);
                                    } else if (item.getType().equals(Material.WIND_CHARGE) && Objects.equals(currentSpleef, "§6§lCopper Spleef")) {
                                        p.getInventory().setItemInOffHand(item);
                                    } else {
                                        p.getInventory().addItem(item);
                                    }
                                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("§7[§a+§7] §r" + item.getItemMeta().getDisplayName()));
                                    p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1F, 1F);
                                }
                            }
                        }
                        if(timeLeft == 5){
                            runningTimers.remove("crumblekit");
                            cancel();
                        }
                        timeLeft++;
                    }
                } else {
                    cancel();
                }
            }

        }.runTaskTimer(this, 0L, 10L);

        runningTimers.put("crumblekit", new AbstractMap.SimpleEntry<>(task, 0));
    }

    public List<ItemStack> getVotingItemBoxItems(){

        List<ItemStack> items = new ArrayList<>();

        ItemStack item;
        PotionMeta pMeta;
        ItemMeta meta;

        item = new ItemStack(Material.SNOWBALL);
        meta = item.getItemMeta();
        meta.setDisplayName("§b§lIce Ball");
        meta.setLore(Arrays.asList("§fThrow this at a player or the floor", "§fto freeze the surrounding floor."));

        item.setItemMeta(meta);

        items.add(item);

        if(votingMode.equals("walk")) {

            item = new ItemStack(Material.POTION);
            pMeta = (PotionMeta) item.getItemMeta();
            pMeta.setDisplayName("§bSpeed Boost §7§o(5s)");
            pMeta.setBasePotionType(PotionType.SWIFTNESS);
            item.setItemMeta(pMeta);

            items.add(item);


            item = new ItemStack(Material.TNT);

            items.add(item);

        }


        return items;
    }

    public List<ItemStack> getItemBoxItems(int index){

        List<ItemStack> items = new ArrayList<>();

        ItemStack item;
        PotionMeta pMeta;
        ItemMeta meta;

        switch(index){
            case 0, 1, 4:
                item = new ItemStack(Material.POTION);
                pMeta = (PotionMeta) item.getItemMeta();
                pMeta.setDisplayName("§aJump Boost §7§o(10s)");
                pMeta.setBasePotionType(PotionType.LEAPING);
                item.setItemMeta(pMeta);

                items.add(item);

                item = new ItemStack(Material.POTION);
                pMeta = (PotionMeta) item.getItemMeta();
                pMeta.setDisplayName("§bSpeed Boost §7§o(10s)");
                pMeta.setBasePotionType(PotionType.SWIFTNESS);
                item.setItemMeta(pMeta);

                items.add(item);
                break;
            case 2:
                item = new ItemStack(Material.POTION);
                pMeta = (PotionMeta) item.getItemMeta();
                pMeta.setDisplayName("§aJump Boost §7§o(10s)");
                pMeta.setBasePotionType(PotionType.LEAPING);
                item.setItemMeta(pMeta);

                items.add(item);

                item = new ItemStack(Material.POTION);
                pMeta = (PotionMeta) item.getItemMeta();
                pMeta.setDisplayName("§bSpeed Boost §7§o(10s)");
                pMeta.setBasePotionType(PotionType.SWIFTNESS);
                item.setItemMeta(pMeta);

                items.add(item);

                item = new ItemStack(Material.FIRE_CHARGE);
                meta = item.getItemMeta();
                meta.setDisplayName("§cFireball Projectile §7§o(Right Click)");
                item.setItemMeta(meta);

                items.add(item);
                break;
            case 3:
                item = new ItemStack(Material.POTION);
                pMeta = (PotionMeta) item.getItemMeta();
                pMeta.setDisplayName("§aJump Boost §7§o(10s)");
                pMeta.setBasePotionType(PotionType.LEAPING);
                item.setItemMeta(pMeta);

                items.add(item);

                item = new ItemStack(Material.POTION);
                pMeta = (PotionMeta) item.getItemMeta();
                pMeta.setDisplayName("§bSpeed Boost §7§o(10s)");
                pMeta.setBasePotionType(PotionType.SWIFTNESS);
                item.setItemMeta(pMeta);

                items.add(item);

                item = new ItemStack(Material.ENDER_PEARL);
                meta = item.getItemMeta();
                meta.setDisplayName("§aEnder Pearl");
                item.setItemMeta(meta);

                items.add(item);
                break;
            default:
                break;
        }

        return items;
    }

    public void summonItemBox(Location location) {
        Random rand = new Random();
        ItemStack item = null;

        if(currentMode.equals("Crumble Clash")) {
            List<String> maps = new ArrayList<>(crumbleMaps.values());

            for (int index = 0; index < maps.size(); index++) {
                if (maps.get(index).equals(currentSpleef)) {

                    List<ItemStack> items = getItemBoxItems(index);
                    if (items == null || items.isEmpty()) {
                        return;
                    }

                    item = items.get(rand.nextInt(items.size()));
                    break;
                }
            }
        }
        if(votingEnabled){
            List<ItemStack> items = getVotingItemBoxItems();
            if (items == null || items.isEmpty()) {
                return;
            }

            item = items.get(rand.nextInt(items.size()));
        }

        if (item == null) {
            return;
        }

        ItemBox itembox = new ItemBox(plugin, location, item);
        itembox.spawn();
        itemBoxes.add(itembox);
    }

    public void runPearlCheck() {
        crumblePearlTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!plugin.currentMode.equals("Crumble Clash")) return;

                for (Entity entity : Bukkit.getWorld("build").getEntities()) {
                    if (!(entity instanceof EnderPearl pearl)) continue;

                    Location loc = pearl.getLocation();

                    boolean outOfBounds = loc.getX() < ccMIN_X || loc.getX() > ccMAX_X
                            || loc.getY() < ccMIN_Y || loc.getY() > ccMAX_Y
                            || loc.getZ() < ccMIN_Z || loc.getZ() > ccMAX_Z;

                    if (outOfBounds) {
                        ProjectileSource shooter = pearl.getShooter();
                        pearl.remove();

                        if (shooter instanceof Player player) {
                            player.sendMessage("§cYour ender pearl did not land safely.");
                            returnPearl(player);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    private void returnPearl(Player player) {
        int pearlCount = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == Material.ENDER_PEARL) {
                pearlCount += item.getAmount();
            }
        }

        if (pearlCount < 3) {
            ItemStack pearl = new ItemStack(Material.ENDER_PEARL);
            ItemMeta meta = pearl.getItemMeta();
            meta.setDisplayName("§aEnder Pearl");
            pearl.setItemMeta(meta);
            player.getInventory().addItem(pearl);

            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1F, 1F);
            player.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText("§7[§a+§7] §r" + pearl.getItemMeta().getDisplayName())
            );
            player.sendMessage("§aA pearl has been returned to your inventory.");
        } else {
            player.sendMessage("§cYou have reached the maximum pearl capacity.");
        }
    }


    public void startCrumbleClash(){
        pvpArenaManager.disablePvPArena();
        ccRoundStarted = false;
        multiplier = GameOrderConfig.get().getDouble("multiplier");
        fillVotingSpace(6);
        musicManager.stopMusicAll();
        setPreviousPlacements();
        plugin.shopAllowed = false;
        clearInventories();
        if(currentRound == 1){
            resetCrumbleMapsConfig();
            resetModeFullPoints();
            MapsPlayedConfig.get().set("maps.Crumble Clash", new ArrayList<>());
            MapsPlayedConfig.save();
        }

        targetTime = 11;
        if(currentRound == 1) {
            timerLabel = "Game Explanation:";
        } else {
            timerLabel = "Starting Round:";
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
                                for(int x = 0; x < 85; x++){
                                    for(int z = 0; z <= 84; z++){
                                        Bukkit.getWorld("build").getBlockAt(108-x, 194, 458+z).setType(Material.BARRIER);
                                    }
                                }
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
                                    if(currentRound == 1) {
                                        ItemStack pumpkin = new ItemStack(Material.CARVED_PUMPKIN);
                                        player.getInventory().clear();
                                        player.getInventory().setHelmet(pumpkin);
                                    } else {
                                        player.setGameMode(GameMode.SURVIVAL);
                                    }
                                    ghostManager.removeGhostPlayer(player.getName());
                                }
                                if(currentRound == 1){
                                    startCustomPan("clash1");
                                }
                                break;
                            case 52:
                                if(currentRound == 1) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        player.sendTitle("\uD83E\uDD6C", "", 20, 40, 20);
                                    }
                                    playSoundAll(Sound.ENTITY_ARMADILLO_LAND, 1F);
                                }
                                break;
                            case 44:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                if(currentRound == 2) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        messagePlayer(player, """
                                                §8
                                                §8
                                                §r⏳ §eWelcome to §b§lCrumble Clash §c§lROUND §c§l""" + currentRound + """
                                                §e!
                                                §eNow you understand it. Let's do it again!
                                                §8
                                                """);
                                    }
                                    timeLeft = 17;
                                } else if(currentRound == 3) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        messagePlayer(player, """
                                                §8
                                                §8
                                                §r⏳ §eWelcome to §b§lCrumble Clash §c§lROUND §c§l""" + currentRound + """
                                                §e!
                                                §eFinal round.. §ulock in.
                                                §8
                                                """);
                                    }
                                    timeLeft = 17;
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
                            case 41:
                                if(currentRound == 1){
                                    startCustomPan("clash2");
                                }
                                break;
                            case 36:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §eThere are many ways to play! Think up strategies and vote for the mode you wish to play each round. Each mode with different a spin on the classic game of spleef.
                                            §8
                                            """);
                                }
                                break;
                            case 27:
                                if(currentRound == 1){
                                    startCustomPan("clash3");
                                }
                                break;
                            case 22:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §eYour individual placement will determine the game, its last team standing wins! Good luck.
                                            §8
                                            """);
                                }
                                break;
                            case 13:
                                if(currentRound == 1){
                                    for (Player player : getPlayers()) {
                                        ItemStack air = new ItemStack(Material.AIR);
                                        player.getInventory().clear();
                                        player.getInventory().setHelmet(air);
                                        player.setGameMode(GameMode.SURVIVAL);
                                    }
                                    teamTeleport("crumbleclash", 0);
                                    teleportSpectators(TeleportConfig.get().getLocation("spectators.crumbleclash"), 0);
                                }
                                break;
                            case 11:
                                crumbleClashModeIterator();
                                runningTimers.remove("crumbleclashstart");
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

    // Get nearest block broken which is within 0.8 blocks of a players location, and was broken within a 5 second timeframe of running this code.
    public CrumbleBlockRecord getClosestBlock(Player player, String layer) {
        long now = System.currentTimeMillis();
        long windowMillis = 5000; // 5 seconds
        double maxDist = 0.8;

        Location playerLoc = player.getLocation();

        List<CrumbleBlockRecord> layerRecords = crumbleBlockRecords.get(layer);
        if (layerRecords == null) return null;

        return layerRecords.stream()
                .filter(r -> now - r.time <= windowMillis)
                .filter(r -> distanceSquaredXZ(r, playerLoc) <= maxDist * maxDist)
                .min(Comparator.comparingDouble(r -> distanceSquaredXZ(r, playerLoc)))
                .orElse(null);
    }

    private double distanceSquaredXZ(CrumbleBlockRecord record, Location loc) {
        double dx = record.blockX - loc.getX();
        double dz = record.blockZ - loc.getZ();
        return dx * dx + dz * dz;
    }

    public void onPlayerFallThrough(Player player, String layer) {
        CrumbleBlockRecord closest = getClosestBlock(player, layer);
        if (closest == null) return;

        CrumbleKillData existing = crumbleKillTracker.get(player.getName());

        if (Objects.equals(PlayerConfig.get().getString("players." + player.getName() + ".team"), PlayerConfig.get().getString("players." + closest.attacker + ".team"))) return;

        if (existing == null || closest.time > existing.time) {
            crumbleKillTracker.put(player.getName(), new CrumbleKillData(closest.attacker, closest.time));
        }
    }

    public void startCrumbleClashFinale(){

        List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());
        String firstTeam = leaderteams.getFirst();
        String secondTeam = leaderteams.get(1);
        List<String> firstPlayers = TeamsConfig.get().getStringList("teams." + firstTeam + ".players");
        List<String> secondPlayers = TeamsConfig.get().getStringList("teams." + secondTeam + ".players");

        fillVotingSpace(1);
        setPreviousPlacements();
        plugin.shopAllowed = false;
        clearInventories();
        resetModeFullPoints();
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 41;
            @Override
            public void run() {
                if(runningTimers.containsKey("crumbleclashstart")) {
                    if (!pausedTimers.contains("crumbleclashstart")) {
                        timeLeft--;
                        runningTimers.get("crumbleclashstart").setValue(timeLeft);
                        bossBarBgTest();
                        switch (timeLeft) {
                            case 40:
                                for(int x = 0; x < 85; x++){
                                    for(int z = 0; z <= 84; z++){
                                        Bukkit.getWorld("build").getBlockAt(108-x, 194, 458+z).setType(Material.BARRIER);
                                    }
                                }
                                for (Player p : getPlayers()) {
                                    lastHitPlayer.put(p.getName(), "");
                                }
                                finaleTeamTeleport("crumbleclashfinale", 5);
                                teleportSpectators(TeleportConfig.get().getLocation("spectators.crumbleclashfinale"), 5);
                                resetCrumbleClash();
                                resetModePoints();
                                break;
                            case 35:
                                currentMode = "Crumble Clash";
                                try {
                                    glowTeams();
                                } catch (ReflectiveOperationException e) {
                                    throw new RuntimeException(e);
                                }

                                for(Player p : getPlayers()){
                                    if(!secondPlayers.contains(p.getName()) && !firstPlayers.contains(p.getName())){
                                        ghostManager.addGhostPlayer(p.getName());
                                    }
                                }

                                for (Player player : getPlayers()) {
                                    if(!ghostManager.getGhostPlayers().contains(player.getName())) {
                                        PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 120, 1, false, false);
                                        player.addPotionEffect(PotionEffect);
                                        player.setGameMode(GameMode.SURVIVAL);
                                    }
                                }
                                break;

                            case 30:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                        §8
                                        §8
                                        §r⏳ §eCrumble Clash Finale! Outwit your opponent and be the last team on the platform.
                                        §8
                                        """);
                                    player.sendTitle("§a§lLast Team", "§a§lStanding.", 0, 40, 20);
                                }
                                break;
                            case 20:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §eIn this round you will play a random spleef mode which hasn't been played yet!
                                            §8
                                            """);
                                }
                                break;
                            case 11:
                                crumbleClashFinaleIterator();
                                runningTimers.remove("crumbleclashstart");
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

    List<Location> generatePositions(World world, int y, int radius, int amount) {
        List<Location> positions = new ArrayList<>();

        int attempts = 0;
        int maxAttempts = amount * 10; // prevent infinite loop

        while (positions.size() < amount && attempts < maxAttempts) {
            double x = 0;
            double z = 0;
            if(currentMode.equals("Crumble Clash")){
                x = 66 + (ranX.nextDouble() * 2 * radius - radius);
                z = 500 + (ranZ.nextDouble() * 2 * radius - radius);
            }
            if(votingEnabled){
                x = 208 + (ranX.nextDouble() * 2 * radius - radius);
                z = 734 + (ranZ.nextDouble() * 2 * radius - radius);
            }
            Location loc = new Location(world, x, y, z);

            boolean tooClose = false;
            for (Location existing : positions) {
                if (existing.distance(loc) < MIN_DISTANCE) {
                    tooClose = true;
                    break;
                }
            }

            if (!tooClose) {
                positions.add(loc);
            }

            attempts++;
        }

        return positions;
    }

    public void crumbleClashModeIterator(){
        List<Integer> ccMaps = new ArrayList<>();


        int y = 194;
        int mapX = 285;
        int mapY = 122;
        int mapZ = -10;
        World world = Bukkit.getWorld("build");
        Location loc = txtLeftLoc;
        for(int i = 0; i < 5; i+=2) {
            if(i == 2){
                loc = txtMiddleLoc;
            }
            if(i == 4){
                loc = txtRightLoc;
            }
            mapTitles.add(world.spawn(loc, TextDisplay.class));
            mapTitles.get(i).setSeeThrough(true);
            mapTitles.get(i).setText("§7§l???");
            mapTitles.get(i).setBillboard(Display.Billboard.VERTICAL);
            Quaternionf quat = new Quaternionf();
            Transformation transform = new Transformation(
                    new Vector3f(0, 0, 0),
                    quat,
                    new Vector3f(5.0f, 5.0f, 5.0f),
                    quat
            );

            mapTitles.get(i).setTransformation(transform);

            loc = loc.clone().subtract(0,1,0);

            mapTitles.add(world.spawn(loc, TextDisplay.class));
            mapTitles.get(i+1).setSeeThrough(true);
            mapTitles.get(i+1).setText("§7§l???");
            mapTitles.get(i+1).setBillboard(Display.Billboard.VERTICAL);
            Quaternionf quat2 = new Quaternionf();
            Transformation transform2 = new Transformation(
                    new Vector3f(0, 0, 0),
                    quat2,
                    new Vector3f(2.0f, 2.0f, 2.0f),
                    quat2
            );

            mapTitles.get(i+1).setTransformation(transform2);
        }
        if(currentRound == 1){
            currentCrumbleList.clear();
            currentCrumbleList.addAll(crumbleMaps.keySet());
        }

        crumbleMapList.clear();
        crumbleMapList.addAll(currentCrumbleList);

        Random rand = new Random();
        for (int i = 0; i < 3; i++) {
            int random = rand.nextInt(crumbleMapList.size());
            ccMaps.add(crumbleMapList.get(random));
            crumbleMapList.remove(random);
        }

        timerLabel = "Vote for next map:";
        targetTime = 330;

        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 331;
            @Override
            public void run() {
                if(runningTimers.containsKey("crumbleclash")) {
                    if (!pausedTimers.contains("crumbleclash")) {
                        timeLeft--;
                        runningTimers.get("crumbleclash").setValue(timeLeft);
                        bossBarBgTest();
                        if (timeLeft <= 280 && timeLeft % 10 == 0) {

                            int radius = itemBoxBoundaries.get(0) - 8;
                            int amount = radius / 2 - itemBoxCount.getOrDefault(0, 0);
                            if (amount > 0) {
                                List<Location> positions = generatePositions(world, 198, radius, amount);
                                for (Location loc : positions) {
                                    summonItemBox(loc);
                                    itemBoxCount.put(0, itemBoxCount.getOrDefault(0, 0) + 1);
                                }
                            }

                            radius = itemBoxBoundaries.get(1) - 8;
                            amount = radius / 2 - itemBoxCount.getOrDefault(1, 0);
                            if (amount > 0) {
                                List<Location> positions = generatePositions(world, 191, radius, amount);
                                for (Location loc : positions) {
                                    summonItemBox(loc);
                                    itemBoxCount.put(1, itemBoxCount.getOrDefault(1, 0) + 1);
                                }
                            }

                            radius = itemBoxBoundaries.get(2) - 8;
                            amount = radius / 2 - itemBoxCount.getOrDefault(2, 0);
                            if (amount > 0) {
                                List<Location> positions = generatePositions(world, 183, radius, amount);
                                for (Location loc : positions) {
                                    summonItemBox(loc);
                                    itemBoxCount.put(2, itemBoxCount.getOrDefault(2, 0) + 1);
                                }
                            }
                        }
                        switch (timeLeft) {
                            case 330:
                                itemBoxCount.put(0, 0);
                                itemBoxCount.put(1, 0);
                                itemBoxCount.put(2, 0);
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle("§a§lVoting Time!", "§fVote for the next map.", 0, 40, 20);
                                }
                                break;
                            case 325:
                                for(int x = 0; x < 32; x++){
                                    for(int z = 0; z <= 84; z++){
                                        world.getBlockAt(108-x, y, 458+z).setType(world.getBlockAt(mapX-(86*ccMaps.getFirst())-x, mapY, mapZ+z).getType());
                                    }
                                }
                                StringBuilder modifiers = new StringBuilder();
                                int index = 1;
                                for(String modifier : crumbleMapModifiers.get(ccMaps.getFirst())){
                                    if(index == crumbleMapModifiers.get(ccMaps.getFirst()).size()){
                                        modifiers.append(modifier);
                                    } else {
                                        modifiers.append(modifier).append(", ");
                                    }
                                    index++;
                                }
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle(crumbleMaps.get(ccMaps.getFirst()), modifiers.toString(), 0, 40, 20);
                                    messagePlayer(p, """
                                            §8
                                            §8
                                            §8""" + crumbleMaps.get(ccMaps.getFirst()) + """
                                            §8
                                            §f""" + modifiers + """
                                            §8
                                            """);
                                }
                                mapTitles.getFirst().setText(crumbleMaps.get(ccMaps.getFirst()));
                                mapTitles.get(1).setText(modifiers.toString());
                                break;
                            case 323:
                                for(int x = 0; x < 21; x++){
                                    for(int z = 0; z <= 84; z++){
                                        world.getBlockAt(76-x, y, 458+z).setType(world.getBlockAt(((mapX-(86*ccMaps.get(1)))-32)-x, mapY, mapZ+z).getType());
                                    }
                                }
                                StringBuilder modifiers2 = new StringBuilder();
                                int index2 = 1;
                                for(String modifier : crumbleMapModifiers.get(ccMaps.get(1))){
                                    if(index2 == crumbleMapModifiers.get(ccMaps.get(1)).size()){
                                        modifiers2.append(modifier);
                                    } else {
                                        modifiers2.append(modifier).append(", ");
                                    }
                                    index2++;
                                }
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle(crumbleMaps.get(ccMaps.get(1)), modifiers2.toString(), 0, 40, 20);
                                    messagePlayer(p, """
                                            §8
                                            §8
                                            §8""" + crumbleMaps.get(ccMaps.get(1)) + """
                                            §8
                                            §f""" + modifiers2 + """
                                            §8
                                            """);
                                }
                                mapTitles.get(2).setText(crumbleMaps.get(ccMaps.get(1)));
                                mapTitles.get(3).setText(modifiers2.toString());
                                break;
                            case 321:
                                for(int x = 0; x < 32; x++){
                                    for(int z = 0; z <= 84; z++){
                                        world.getBlockAt(55-x, y, 458+z).setType(world.getBlockAt(((mapX-(86*ccMaps.get(2)))-53)-x, mapY, mapZ+z).getType());
                                    }
                                }
                                StringBuilder modifiers3 = new StringBuilder();
                                int index3 = 1;
                                for(String modifier : crumbleMapModifiers.get(ccMaps.get(2))){
                                    if(index3 == crumbleMapModifiers.get(ccMaps.get(2)).size()){
                                        modifiers3.append(modifier);
                                    } else {
                                        modifiers3.append(modifier).append(", ");
                                    }
                                    index3++;
                                }
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle(crumbleMaps.get(ccMaps.get(2)), modifiers3.toString(), 0, 40, 20);
                                    messagePlayer(p, """
                                            §8
                                            §8
                                            §8""" + crumbleMaps.get(ccMaps.get(2)) + """
                                            §8
                                            §f""" + modifiers3 + """
                                            §8
                                            """);
                                }
                                mapTitles.get(4).setText(crumbleMaps.get(ccMaps.get(2)));
                                mapTitles.get(5).setText(modifiers3.toString());
                                break;
                            case 315:
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle("§a§lVOTE!", "§fVoting ends in 15 seconds...", 0, 40, 20);
                                }
                                break;
                            case 300:
                                targetTime = 285;
                                timerLabel = "Round starting in:";
                                for(TextDisplay td : mapTitles){
                                    td.remove();
                                }
                                mapTitles.clear();
                                ccMapVotes.put(ccMaps.getFirst(), countPlayersInArea(world, new Location(world, 92.5, 194, 500), 15.5, 10, 42));
                                ccMapVotes.put(ccMaps.get(1), countPlayersInArea(world, new Location(world, 66, 194, 500), 10, 10, 42));
                                ccMapVotes.put(ccMaps.get(2), countPlayersInArea(world, new Location(world, 39.5, 194, 500), 15.5, 10, 42));

                                List<Integer> leaderMap = new ArrayList<>(sortIntMap(ccMapVotes).keySet());

                                for(Player p : Bukkit.getOnlinePlayers()){
                                    messagePlayer(p, " §f-  §e§lᴍᴀᴘ ᴠᴏᴛᴇ ʟᴇᴀᴅᴇʀs  §f-");
                                    int indexLeaders = 1;
                                    for(Integer leader : leaderMap){
                                        messagePlayer(p, indexLeaders + ". " + crumbleMaps.get(leader) + "§f - §7" + ccMapVotes.get(leader) + " votes.");
                                        indexLeaders++;
                                    }
                                }

                                if(leaderMap.getFirst() == ccMaps.get(2)) {
                                    for(Player p : Bukkit.getOnlinePlayers()){
                                        p.sendTitle(crumbleMaps.get(ccMaps.get(2)), "§fLet's do this!", 0, 40, 20);
                                    }
                                    BukkitTask task2 = new BukkitRunnable() {
                                        int timeLeft = 52;

                                        @Override
                                        public void run() {
                                            if (runningTimers.containsKey("crumbleclashbuild")) {
                                                if (!pausedTimers.contains("crumbleclashbuild")) {
                                                    for(int z = 0; z <= 84; z++){
                                                        world.getBlockAt(56+(52-timeLeft), y, 458+z).setType(world.getBlockAt(((mapX-(86*ccMaps.get(2)))-timeLeft), mapY, mapZ+z).getType());
                                                    }
                                                    timeLeft--;
                                                    runningTimers.get("crumbleclashbuild").setValue(timeLeft);
                                                    if(timeLeft == 0){
                                                        runningTimers.remove("crumbleclashbuild");
                                                        cancel();
                                                    }
                                                }
                                            } else {
                                                messageConsole("Timer removed by external factor.");
                                                cancel();
                                            }
                                        }
                                    }.runTaskTimer(plugin, 0L, 2L);
                                    runningTimers.put("crumbleclashbuild", new AbstractMap.SimpleEntry<>(task2, 52));
                                } else if (leaderMap.getFirst() == ccMaps.get(1)) {
                                    for(Player p : Bukkit.getOnlinePlayers()){
                                        p.sendTitle(crumbleMaps.get(ccMaps.get(1)), "§fLet's do this!", 0, 40, 20);
                                    }
                                    BukkitTask task2 = new BukkitRunnable() {
                                        int timeLeft = 31;

                                        @Override
                                        public void run() {
                                            if (runningTimers.containsKey("crumbleclashbuild")) {
                                                if (!pausedTimers.contains("crumbleclashbuild")) {
                                                    for(int z = 0; z <= 84; z++){
                                                        world.getBlockAt(77+(31-timeLeft), y, 458+z).setType(world.getBlockAt(((mapX-(86*ccMaps.get(1)))-31+(31-timeLeft)), mapY, mapZ+z).getType());
                                                        world.getBlockAt(55-(31-timeLeft), y, 458+z).setType(world.getBlockAt(((mapX-(86*ccMaps.get(1)))-53-(31-timeLeft)), mapY, mapZ+z).getType());
                                                    }
                                                    timeLeft--;
                                                    runningTimers.get("crumbleclashbuild").setValue(timeLeft);
                                                    if(timeLeft == 0){
                                                        runningTimers.remove("crumbleclashbuild");
                                                        cancel();
                                                    }
                                                }
                                            } else {
                                                messageConsole("Timer removed by external factor.");
                                                cancel();
                                            }
                                        }
                                    }.runTaskTimer(plugin, 0L, 2L);
                                    runningTimers.put("crumbleclashbuild", new AbstractMap.SimpleEntry<>(task2, 31));
                                } else if (leaderMap.getFirst() == ccMaps.getFirst()) {
                                    for(Player p : Bukkit.getOnlinePlayers()){
                                        p.sendTitle(crumbleMaps.get(ccMaps.getFirst()), "§fLet's do this!", 0, 40, 20);
                                    }
                                    BukkitTask task2 = new BukkitRunnable() {
                                        int timeLeft = 52;

                                        @Override
                                        public void run() {
                                            if (runningTimers.containsKey("crumbleclashbuild")) {
                                                if (!pausedTimers.contains("crumbleclashbuild")) {
                                                    for(int z = 0; z <= 84; z++){
                                                        world.getBlockAt(76-(52-timeLeft), y, 458+z).setType(world.getBlockAt(((mapX-(86*ccMaps.getFirst()))-32-(52-timeLeft)), mapY, mapZ+z).getType());
                                                    }
                                                    timeLeft--;
                                                    runningTimers.get("crumbleclashbuild").setValue(timeLeft);
                                                    if(timeLeft == 0){
                                                        runningTimers.remove("crumbleclashbuild");
                                                        cancel();
                                                    }
                                                }
                                            } else {
                                                messageConsole("Timer removed by external factor.");
                                                cancel();
                                            }
                                        }
                                    }.runTaskTimer(plugin, 0L, 2L);
                                    runningTimers.put("crumbleclashbuild", new AbstractMap.SimpleEntry<>(task2, 52));
                                }
                                for(int z = 0; z <= 84; z++){
                                    for(int x = 0; x <= 84; x++) {
                                        world.getBlockAt(108-x, 187, 458 + z).setType(world.getBlockAt(((mapX - (86 * leaderMap.getFirst()))-x), 115, mapZ + z).getType());
                                        world.getBlockAt(108-x, 179, 458 + z).setType(world.getBlockAt(((mapX - (86 * leaderMap.getFirst()))-x), 107, mapZ + z).getType());
                                    }
                                }
                                ccMapVotes.clear();
                                giveCrumbleKits(leaderMap.getFirst());
                                currentSpleef = crumbleMaps.get(leaderMap.getFirst());
                                currentCrumbleList.remove(leaderMap.getFirst());
                                addPlayedMap("Crumble Clash", currentSpleef);
                                break;
                            case 295:
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    messagePlayer(p, """
                                            §8
                                            §8
                                            §8[§c§l!§8] §7Game Starting in §c§l10 seconds...
                                            §8
                                            """);
                                }
                                break;
                            case 294:
                                playMusicAll(Sound.MUSIC_DISC_MALL);
                                break;
                            case 290, 289, 288, 287, 286:
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendTitle("§c§l▶ " + (timeLeft - 285) + " ◀", "", 0, 20, 20);
                                }
                                break;
                            case 285:
                                ccRoundStarted = true;
                                targetTime = 235;
                                timerLabel = "Layer 1 decays in:";
                                for(int x = 0; x < 85; x++){
                                    for(int z = 0; z <= 84; z++){
                                        if(world.getBlockAt(108-x, y, 458+z).getType() == Material.BARRIER) {
                                            world.getBlockAt(108 - x, y, 458 + z).setType(Material.AIR);
                                        }
                                    }
                                }
                                for (Player player : getPlayers()) {
                                    PotionEffect PotionEffect = new PotionEffect(PotionEffectType.RESISTANCE, 12000, 1, false, false);
                                    player.addPotionEffect(PotionEffect);
                                    player.sendTitle("§a§l▶ CLASH! ◀", "", 0, 40, 0);
                                }
                                if(Objects.equals(currentSpleef, "§6§lCopper Spleef")){
                                    copperDecay = true;
                                }
                                if(Objects.equals(currentSpleef, "§a§lEnder Spleef")){
                                    runPearlCheck();
                                }
                                if(copperDecay){
                                    startCopperDecay();
                                }
                                blockBreak = true;
                                setItemBoxBoundaries(new Location(Bukkit.getWorld("build"), 66, 194, 500));
                                setItemBoxBoundaries(new Location(Bukkit.getWorld("build"), 66, 187, 500));
                                setItemBoxBoundaries(new Location(Bukkit.getWorld("build"), 66, 179, 500));
                                break;
                            case 284:
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    messagePlayer(p, "Timer done.");
                                }
                                break;
                            case 235:
                                targetTime = 175;
                                timerLabel = "Layer 2 decays in:";
                                decayCrumbleLayer(new Location(Bukkit.getWorld("build"), 66, 194, 500), 43, 0);
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    messagePlayer(p, "Layer 1 decaying.");
                                    p.sendTitle("", "§6⚠ Layer 1 decaying. ⚠", 0, 20, 20);
                                }
                                playSoundAll(Sound.BLOCK_POWDER_SNOW_BREAK, 1F);
                                break;
                            case 175:
                                targetTime = 115;
                                timerLabel = "Layer 3 decays in:";
                                decayCrumbleLayer(new Location(Bukkit.getWorld("build"), 66, 187, 500), 43, 0);
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    messagePlayer(p, "Layer 2 decaying.");
                                    p.sendTitle("", "§6⚠ Layer 2 decaying. ⚠", 0, 20, 20);
                                }
                                playSoundAll(Sound.BLOCK_POWDER_SNOW_BREAK, 1F);
                                break;
                            case 115:
                                targetTime = 1;
                                timerLabel = "Final Decay:";
                                decayCrumbleLayer(new Location(Bukkit.getWorld("build"), 66, 179, 500), 43, 5);
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    messagePlayer(p, "Layer 3 decaying..");
                                    p.sendTitle("", "§6⚠ Layer 3 decaying. ⚠", 0, 20, 20);
                                }
                                playSoundAll(Sound.BLOCK_POWDER_SNOW_BREAK, 1F);
                                break;

                            case 1:
                                decayCrumbleLayer(new Location(Bukkit.getWorld("build"), 66, 179, 500), 5, 0);
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

        runningTimers.put("crumbleclash", new AbstractMap.SimpleEntry<>(task, 331));
    }

    public void randomiseCCMapVisuals(List<Integer> ccMaps, TextDisplay textMiddle, TextDisplay txtMiddleItems, Integer chosenMap){
        playSoundAll(Sound.ENTITY_CREEPER_PRIMED, 1F);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle("§7§k000000000", "", 0, 160, 40);
        }
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 81;
            int index = 0;
            @Override
            public void run() {
                if (runningTimers.containsKey("crumbleclashmappicker")) {
                    if (!pausedTimers.contains("crumbleclashmappicker")) {
                        timeLeft--;
                        runningTimers.get("crumbleclashmappicker").setValue(timeLeft);
                        bossBarBgTest();

                        switch (timeLeft) {
                            case 80, 76, 72, 68, 64, 60, 56, 52, 48, 44, 40, 35, 30, 20, 10:
                                index++;
                                if((index & 2) == 0){
                                    textMiddle.setText(crumbleMaps.get(ccMaps.getFirst()));
                                } else {
                                    textMiddle.setText(crumbleMaps.get(ccMaps.get(1)));
                                }
                                break;
                            case 0:
                                StringBuilder modifiers3 = new StringBuilder();
                                playSoundAll(Sound.ENTITY_GENERIC_EXPLODE, 1F);
                                int index = 0;
                                for (String modifier : crumbleMapModifiers.get(chosenMap)) {
                                    if (index == crumbleMapModifiers.get(chosenMap).size()) {
                                        modifiers3.append(modifier);
                                    } else {
                                        modifiers3.append(modifier).append(", ");
                                    }
                                    index++;
                                }
                                for (Player p : Bukkit.getOnlinePlayers()) {
                                    p.sendTitle(crumbleMaps.get(chosenMap), modifiers3.toString(), 0, 40, 20);
                                    messagePlayer(p, """
                                            §8
                                            §8
                                            §8""" + crumbleMaps.get(chosenMap) + """
                                            §8
                                            §f""" + modifiers3 + """
                                            §8
                                            """);
                                }
                                textMiddle.setText(crumbleMaps.get(chosenMap));
                                txtMiddleItems.setText(modifiers3.toString());
                                break;
                        }
                    } else {
                        messageConsole("Timer removed by external factor.");
                        cancel();
                    }
                }
            }

        }.runTaskTimer(this, 0L, 1L);

        runningTimers.put("crumbleclashmappicker", new AbstractMap.SimpleEntry<>(task, 81));
    }

    public void reduceToRadius(int innerRadius, int outerRadius, int cx, int cy, int cz) {
        World world = Bukkit.getWorld("build");

        Material replaceTo = Material.AIR;

        if (cy == 194) {
            replaceTo = Material.BARRIER;
        }

        int innerSq = innerRadius * innerRadius;

        for (int x = -outerRadius; x <= outerRadius; x++) {
            for (int z = -outerRadius; z <= outerRadius; z++) {

                int distSq = x * x + z * z;

                if (distSq >= innerSq) {

                    Block block = world.getBlockAt(cx + x, cy, cz + z);

                    if (block.getType() != replaceTo) {
                        block.setType(replaceTo, false);
                    }
                }
            }
        }
    }

    public void crumbleClashFinaleIterator(){


        List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());
        String firstTeam = leaderteams.getFirst();
        String secondTeam = leaderteams.get(1);

        for(String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)){
            if(TeamsConfig.get().getStringList("teams." + team + ".players").isEmpty()) continue;
            if(!Objects.equals(team, firstTeam) && !Objects.equals(team, secondTeam)){
                deadTeams.add(team);
                deadPlayers.addAll(TeamsConfig.get().getStringList("teams." + team + ".players"));
            }
        }


        int y = 194;
        int mapX = 285;
        int mapY = 122;
        int mapZ = -10;
        World world = Bukkit.getWorld("build");
        Location loc = txtMiddleLoc;
        TextDisplay textMiddle = world.spawn(loc, TextDisplay.class);
        textMiddle.setSeeThrough(true);
        textMiddle.setText("§7§l???");
        textMiddle.setBillboard(Display.Billboard.VERTICAL);
        Quaternionf quat = new Quaternionf();
        Transformation transform = new Transformation(
                new Vector3f(0, 0, 0),
                quat,
                new Vector3f(5.0f, 5.0f, 5.0f),
                quat
        );

        textMiddle.setTransformation(transform);

        loc = loc.clone().subtract(0,1,0);

        TextDisplay txtMiddleItems = world.spawn(loc, TextDisplay.class);
        txtMiddleItems.setSeeThrough(true);
        txtMiddleItems.setText("§7§l???");
        txtMiddleItems.setBillboard(Display.Billboard.VERTICAL);
        Quaternionf quat2 = new Quaternionf();
        Transformation transform2 = new Transformation(
                new Vector3f(0, 0, 0),
                quat2,
                new Vector3f(2.0f, 2.0f, 2.0f),
                quat2
        );

        txtMiddleItems.setTransformation(transform2);

        crumbleMapList.clear();

        List<String> mapsPlayed = MapsPlayedConfig.get().getStringList("maps.Crumble Clash");
        for(Integer map : crumbleMaps.keySet()){
            if(!mapsPlayed.contains(crumbleMaps.get(map))){
                crumbleMapList.add(map);
            }
        }

        Random rand = new Random();
        Integer chosenMap = crumbleMapList.get(rand.nextInt(crumbleMapList.size()));

        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 301;
            @Override
            public void run() {
                if(runningTimers.containsKey("finale")) {
                    if (!pausedTimers.contains("finale")) {
                        timeLeft--;
                        runningTimers.get("finale").setValue(timeLeft);
                        bossBarBgTest();
                        if (timeLeft <= 265 && timeLeft % 10 == 0) {

                            int radius = itemBoxBoundaries.get(0) - 8;
                            int amount = radius / 2 - itemBoxCount.getOrDefault(0, 0);
                            if (amount > 0) {
                                List<Location> positions = generatePositions(world, 198, radius, amount);
                                for (Location loc : positions) {
                                    summonItemBox(loc);
                                    itemBoxCount.put(0, itemBoxCount.getOrDefault(0, 0) + 1);
                                }
                            }

                            radius = itemBoxBoundaries.get(1) - 8;
                            amount = radius / 2 - itemBoxCount.getOrDefault(1, 0);
                            if (amount > 0) {
                                List<Location> positions = generatePositions(world, 191, radius, amount);
                                for (Location loc : positions) {
                                    summonItemBox(loc);
                                    itemBoxCount.put(1, itemBoxCount.getOrDefault(1, 0) + 1);
                                }
                            }

                            radius = itemBoxBoundaries.get(2) - 8;
                            amount = radius / 2 - itemBoxCount.getOrDefault(2, 0);
                            if (amount > 0) {
                                List<Location> positions = generatePositions(world, 183, radius, amount);
                                for (Location loc : positions) {
                                    summonItemBox(loc);
                                    itemBoxCount.put(2, itemBoxCount.getOrDefault(2, 0) + 1);
                                }
                            }
                        }
                        switch (timeLeft) {
                            case 300:
                                itemBoxCount.put(0, 0);
                                itemBoxCount.put(1, 0);
                                itemBoxCount.put(2, 0);
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle("§a§lChoosing mode..", "", 0, 40, 20);
                                }
                                break;
                            case 295:
                                randomiseCCMapVisuals(crumbleMapList, textMiddle, txtMiddleItems, chosenMap);
                                break;
                            case 285:
                                textMiddle.remove();
                                txtMiddleItems.remove();
                                for(int z = 0; z <= 84; z++){
                                    for(int x = 0; x <= 84; x++) {
                                        world.getBlockAt(108-x, 194, 458 + z).setType(world.getBlockAt(((mapX - (86 * chosenMap))-x), 122, mapZ + z).getType());
                                        world.getBlockAt(108-x, 187, 458 + z).setType(world.getBlockAt(((mapX - (86 * chosenMap))-x), 115, mapZ + z).getType());
                                        world.getBlockAt(108-x, 179, 458 + z).setType(world.getBlockAt(((mapX - (86 * chosenMap))-x), 107, mapZ + z).getType());
                                    }
                                }
                                giveCrumbleKits(chosenMap);
                                currentSpleef = crumbleMaps.get(chosenMap);
                                break;
                            case 280:
                                reduceToRadius(15, 43, 66, 194, 500);
                                reduceToRadius(18, 43, 66, 187, 500);
                                reduceToRadius(21, 43, 66, 179, 500);
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    messagePlayer(p, """
                                            §8
                                            §8
                                            §8[§c§l!§8] §7Game Starting in §c§l10 seconds...
                                            §8
                                            """);
                                }
                                break;
                            case 279:
                                playMusicAll(Sound.MUSIC_DISC_MALL);
                                break;
                            case 275, 274, 273, 272, 271:
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendTitle("§c§l▶ " + (timeLeft - 270) + " ◀", "", 0, 20, 20);
                                }
                                break;
                            case 270:
                                for(int x = 0; x < 87; x++){
                                    for(int z = 0; z <= 86; z++){
                                        if(world.getBlockAt(109-x, y, 457+z).getType() == Material.BARRIER) {
                                            world.getBlockAt(109 - x, y, 457 + z).setType(Material.AIR);
                                        }
                                    }
                                }
                                for (Player player : getPlayers()) {
                                    PotionEffect PotionEffect = new PotionEffect(PotionEffectType.RESISTANCE, 12000, 1, false, false);
                                    player.addPotionEffect(PotionEffect);
                                    player.sendTitle("§a§l▶ CLASH! ◀", "", 0, 40, 0);
                                }
                                if(Objects.equals(currentSpleef, "§6§lCopper Spleef")){
                                    copperDecay = true;
                                }
                                if(copperDecay){
                                    startCopperDecay();
                                }
                                blockBreak = true;
                                ccRoundStarted = true;

                                ghostManager.giveCompasses();

                                break;
                            case 269:
                                setItemBoxBoundaries(new Location(Bukkit.getWorld("build"), 66, 194, 500));
                                setItemBoxBoundaries(new Location(Bukkit.getWorld("build"), 66, 187, 500));
                                setItemBoxBoundaries(new Location(Bukkit.getWorld("build"), 66, 179, 500));
                                break;
                            case 245:
                                decayCrumbleLayer(new Location(Bukkit.getWorld("build"), 66, 194, 500), 43, 0);
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    messagePlayer(p, "Layer 1 decaying.");
                                    p.sendTitle("", "§6⚠ Layer 1 decaying. ⚠", 0, 20, 20);
                                }
                                playSoundAll(Sound.BLOCK_POWDER_SNOW_BREAK, 1F);
                                break;
                            case 215:
                                decayCrumbleLayer(new Location(Bukkit.getWorld("build"), 66, 187, 500), 43, 0);
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    messagePlayer(p, "Layer 2 decaying.");
                                    p.sendTitle("", "§6⚠ Layer 2 decaying. ⚠", 0, 20, 20);
                                }
                                playSoundAll(Sound.BLOCK_POWDER_SNOW_BREAK, 1F);
                                break;
                            case 185:
                                decayCrumbleLayer(new Location(Bukkit.getWorld("build"), 66, 179, 500), 43, 5);
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    messagePlayer(p, "Layer 3 decaying..");
                                    p.sendTitle("", "§6⚠ Layer 3 decaying. ⚠", 0, 20, 20);
                                }
                                playSoundAll(Sound.BLOCK_POWDER_SNOW_BREAK, 1F);
                                break;
                            case 105:
                                decayCrumbleLayer(new Location(Bukkit.getWorld("build"), 66, 179, 500), 5, 0);
                                break;
                            case 0:
                                runningTimers.remove("finale");
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

        runningTimers.put("finale", new AbstractMap.SimpleEntry<>(task, 301));
    }

    public void startCopperDecay(){
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if(runningTimers.containsKey("crumbleclashdecay")) {
                    if (!pausedTimers.contains("crumbleclashdecay")) {
                        for (Player player : getPlayers()) {
                            handleDecay(player);
                        }
                    }
                } else {
                    plugin.runningTimers.remove("crumbleclashdecay");
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 20L, 4L);

        runningTimers.put("crumbleclashdecay", new AbstractMap.SimpleEntry<>(task, 0));
    }

    public void setItemBoxBoundaries(Location center) {
        World world = Bukkit.getWorld("build");
        int cx = center.getBlockX();
        int cy = center.getBlockY();
        int cz = center.getBlockZ();
        int index = 0;

        switch(cy){
            case 187:
                index = 1;
                break;
            case 179:
                index = 2;
                break;
            default:
                break;
        }

        int radius = 43;
        boolean beginCrumble = false;

        while (!beginCrumble) {
            for (int x = -radius; x <= radius; x++) {
                if(!world.getBlockAt(cx + x, cy, cz + radius).getType().equals(Material.AIR)) {
                    beginCrumble = true;
                    break;
                }
                if(!world.getBlockAt(cx + x, cy, cz - radius).getType().equals(Material.AIR)) {
                    beginCrumble = true;
                    break;
                }
            }

            // Left & right edges (skip corners to avoid double-setting)
            for (int z = -radius + 1; z <= radius - 1; z++) {
                if (!world.getBlockAt(cx + radius, cy, cz + z).getType().equals(Material.AIR)) {
                    beginCrumble = true;
                    break;
                }
                if (!world.getBlockAt(cx - radius, cy, cz + z).getType().equals(Material.AIR)) {
                    beginCrumble = true;
                    break;
                }
            }
            if (!beginCrumble) {
                radius--;
            }
        }
        itemBoxBoundaries.put(index, radius);
    }

    public void decayCrumbleLayer(Location center, int startRadius, int endRadius) {
        World world = Bukkit.getWorld("build");
        int cx = center.getBlockX();
        int cy = center.getBlockY();
        int cz = center.getBlockZ();

        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 0;
            int radius = startRadius;
            boolean beginCrumble = false;
            @Override
            public void run() {
                if(runningTimers.containsKey("crumbleblocks" + cy)) {
                    if (!pausedTimers.contains("crumbleblocks" + cy)) {
                        if(timeLeft == 0) {
                            while (!beginCrumble) {
                                for (int x = -radius; x <= radius; x++) {
                                    if(!world.getBlockAt(cx + x, cy, cz + radius).getType().equals(Material.AIR)) {
                                        beginCrumble = true;
                                        break;
                                    }
                                    if(!world.getBlockAt(cx + x, cy, cz - radius).getType().equals(Material.AIR)) {
                                        beginCrumble = true;
                                        break;
                                    }
                                }

                                // Left & right edges (skip corners to avoid double-setting)
                                for (int z = -radius + 1; z <= radius - 1; z++) {
                                    if (!world.getBlockAt(cx + radius, cy, cz + z).getType().equals(Material.AIR)) {
                                        beginCrumble = true;
                                        break;
                                    }
                                    if (!world.getBlockAt(cx - radius, cy, cz + z).getType().equals(Material.AIR)) {
                                        beginCrumble = true;
                                        break;
                                    }
                                }
                                if (!beginCrumble) {
                                    radius--;
                                }
                            }
                        }
                        timeLeft++;
                        runningTimers.get("crumbleblocks" + cy).setValue(timeLeft);
                        if(timeLeft == 2){
                            for (int x = -radius; x <= radius; x++) {
                                if(!world.getBlockAt(cx + x, cy, cz + radius).getType().equals(Material.AIR)) {
                                    world.getBlockAt(cx + x, cy, cz + radius)
                                            .setType(Material.RED_CONCRETE, false);
                                }
                                if(!world.getBlockAt(cx + x, cy, cz - radius).getType().equals(Material.AIR)) {
                                    world.getBlockAt(cx + x, cy, cz - radius)
                                            .setType(Material.RED_CONCRETE, false);
                                }
                            }

                            // Left & right edges (skip corners to avoid double-setting)
                            for (int z = -radius + 1; z <= radius - 1; z++) {
                                if(!world.getBlockAt(cx + radius, cy, cz + z).getType().equals(Material.AIR)) {
                                    world.getBlockAt(cx + radius, cy, cz + z)
                                            .setType(Material.RED_CONCRETE, false);
                                }
                                if(!world.getBlockAt(cx - radius, cy, cz + z).getType().equals(Material.AIR)) {
                                    world.getBlockAt(cx - radius, cy, cz + z)
                                            .setType(Material.RED_CONCRETE, false);
                                }
                            }
                        }
                        if(timeLeft == 4){
                            for (int x = -radius; x <= radius; x++) {
                                if(world.getBlockAt(cx + x, cy, cz + radius).getType().equals(Material.RED_CONCRETE)) {
                                    world.getBlockAt(cx + x, cy, cz + radius)
                                            .setType(Material.AIR, false);
                                }
                                if(world.getBlockAt(cx + x, cy, cz - radius).getType().equals(Material.RED_CONCRETE)) {
                                    world.getBlockAt(cx + x, cy, cz - radius)
                                            .setType(Material.AIR, false);
                                }
                            }

                            // Left & right edges (skip corners to avoid double-setting)
                            for (int z = -radius + 1; z <= radius - 1; z++) {
                                if(world.getBlockAt(cx + radius, cy, cz + z).getType().equals(Material.RED_CONCRETE)) {
                                    world.getBlockAt(cx + radius, cy, cz + z)
                                            .setType(Material.AIR, false);
                                }
                                if(world.getBlockAt(cx - radius, cy, cz + z).getType().equals(Material.RED_CONCRETE)) {
                                    world.getBlockAt(cx - radius, cy, cz + z)
                                            .setType(Material.AIR, false);
                                }
                            }
                            Iterator<ItemBox> iterator = plugin.itemBoxes.iterator();

                            while (iterator.hasNext()) {
                                ItemBox ib = iterator.next();
                                Location loc = ib.getInteraction().getLocation();

                                int x = loc.getBlockX();
                                int y = loc.getBlockY();
                                int z = loc.getBlockZ();

                                int centerX = 66;
                                int centerZ = 500;

                                int radius0 = itemBoxBoundaries.get(0);

                                if (y >= 197 && y <= 199 &&
                                        (x < centerX - radius0 || x > centerX + radius0 ||
                                                z < centerZ - radius0 || z > centerZ + radius0)) {

                                    ib.despawn();
                                    iterator.remove();
                                    itemBoxCount.replace(0, itemBoxCount.get(0) - 1);
                                    continue;
                                }

                                int radius1 = itemBoxBoundaries.get(1);

                                if (y >= 190 && y <= 192 &&
                                        (x < centerX - radius1 || x > centerX + radius1 ||
                                                z < centerZ - radius1 || z > centerZ + radius1)) {

                                    ib.despawn();
                                    iterator.remove();
                                    itemBoxCount.replace(1, itemBoxCount.get(1) - 1);
                                    continue;
                                }

                                int radius2 = itemBoxBoundaries.get(2);

                                if (y >= 182 && y <= 184 &&
                                        (x < centerX - radius2 || x > centerX + radius2 ||
                                                z < centerZ - radius2 || z > centerZ + radius2)) {

                                    ib.despawn();
                                    iterator.remove();
                                    itemBoxCount.replace(2, itemBoxCount.get(2) - 1);
                                }
                            }
                            if(radius == endRadius){
                                runningTimers.remove("crumbleblocks" + cy);
                                cancel();
                            } else {
                                radius--;
                                switch(cy){
                                    case 194:
                                        itemBoxBoundaries.replace(0, itemBoxBoundaries.get(0)-1);
                                        break;
                                    case 187:
                                        itemBoxBoundaries.replace(1, itemBoxBoundaries.get(1)-1);
                                        break;
                                    case 179:
                                        itemBoxBoundaries.replace(2, itemBoxBoundaries.get(2)-1);
                                        break;
                                    default:
                                        break;
                                }
                                timeLeft = 1;
                            }
                        }
                    }
                } else {
                    cancel();
                }
            }

        }.runTaskTimer(this, 0L, 20L);

        runningTimers.put("crumbleblocks" + cy, new AbstractMap.SimpleEntry<>(task, 0));
    }

    public int countPlayersInArea(World world, Location center, double radiusX, double radiusY, double radiusZ) {
        int count = 0;

        for (Entity entity : world.getNearbyEntities(center, radiusX, radiusY, radiusZ)) {
            if (entity instanceof Player) {
                count++;
            }
        }

        return count;
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
        pvpArenaManager.disablePvPArena();
        multiplier = GameOrderConfig.get().getDouble("multiplier");
        fillVotingSpace(1);
        musicManager.stopMusicAll();
        plugin.shopAllowed = false;
        clearInventories();
        if(currentRound == 1){
            setPreviousPlacements();
            resetModeFullPoints();
            zoomoMap = "§a§lAdrenaline Ravine";
        } else {
            zoomoMap = "§6§lDesert";
        }
        targetTime = 0;
        if(currentRound == 1) {
            timerLabel = "Game Explanation:";
        } else {
            timerLabel = "Starting Round:";
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
                                if(currentRound == 1) {
                                    teamTeleport("zoomogo", 5);
                                    teleportSpectators(TeleportConfig.get().getLocation("spectators.zoomogo"), 5);
                                } else {
                                    teamTeleport("zoomo2", 5);
                                    teleportSpectators(TeleportConfig.get().getLocation("spectators.zoomo2"), 5);
                                }
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
                                    ghostManager.removeGhostPlayer(player.getName());
                                    if(currentRound == 1) {
                                        ItemStack pumpkin = new ItemStack(Material.CARVED_PUMPKIN);
                                        player.getInventory().clear();
                                        player.getInventory().setHelmet(pumpkin);
                                    }
                                }
                                if(currentRound == 1) {
                                    for (int i = 1; i <= 26; i++) {
                                        summonIsland(zoomoIslands(i));
                                        if (i > 6) {
                                            destroyIsland(zoomoIslands(i));
                                        }
                                    }
                                    summonIsland(zoomoIslands(27));
                                } else {
                                    for (int i = 1; i <= 19; i++) {
                                        summonIsland(zoomoDesertIslands(i));
                                        if (i != 1 && i != 2 && i != 17) {
                                            destroyIsland(zoomoDesertIslands(i));
                                        }
                                    }
                                    summonIsland(zoomoDesertIslands(17));
                                    summonIsland(zoomoDesertIslands(1));
                                    summonIsland(zoomoDesertIslands(2));
                                }
                                if(currentRound == 1) {
                                    startCustomPan("zoomo1");
                                }
                                break;
                            case 52:
                                if(currentRound == 1) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        player.sendTitle("\uD83E\uDD55", "", 20, 60, 20);
                                    }
                                    playSoundAll(Sound.ENTITY_ARMADILLO_LAND, 1F);
                                }
                                break;
                            case 49:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                if(currentRound == 2){
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §eWelcome to §a§lZoomo Go §c§lROUND §c""" + currentRound + """
                                            §e! Welcome to the §6§lDesert§e!
                                            §8
                                            """);
                                    }
                                } else if (currentRound == 3){
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §eWelcome to §a§lZoomo Go §c§lROUND §c""" + currentRound + """
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
                            case 41:
                                if(currentRound == 1) {
                                    startCustomPan("zoomo2");
                                }
                                break;
                            case 36:
                                if(currentRound > 1) {
                                    timeLeft = 16;
                                    break;
                                }
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
                            case 30:
                                if(zoomoSpeed){
                                    playSoundAll(Sound.BLOCK_BREWING_STAND_BREW, 2);
                                    PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SPEED, 360, 2, false, false);
                                    for (Player player : getPlayers()) {
                                        player.addPotionEffect(PotionEffect);
                                    }
                                }
                                break;
                            case 27:
                                if(currentRound == 1) {
                                    startCustomPan("zoomo3");
                                }
                                break;
                            case 22:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §r⏳ §eYou are limited to two §c❤ §elives! If you fall you will be teleported to a random active platform and will be invulnerable to attacks and unable to attack for 5s, be careful!
                                            §8
                                            """);
                                }
                                break;
                            case 13:
                                if(currentRound == 1) {
                                    teamTeleport("zoomogo", 0);
                                    teleportSpectators(TeleportConfig.get().getLocation("spectators.zoomogo"), 0);
                                }
                                for(Player player : getPlayers()){
                                    player.setGameMode(GameMode.ADVENTURE);
                                    ItemStack air = new ItemStack(Material.AIR);
                                    player.getInventory().clear();
                                    player.getInventory().setHelmet(air);
                                }
                                break;
                            case 12:
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
                                if(currentRound == 1) {
                                    initiateZoomoIslands();
                                } else {
                                    initiateZoomoDesertIslands();
                                }
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

    public void startZoomoGoFinale(){
        fillVotingSpace(1);
        plugin.shopAllowed = false;
        clearInventories();
        setPreviousPlacements();
        resetModeFullPoints();
        List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());
        String firstTeam = leaderteams.getFirst();
        String secondTeam = leaderteams.get(1);
        List<String> firstPlayers = TeamsConfig.get().getStringList("teams." + firstTeam + ".players");
        List<String> secondPlayers = TeamsConfig.get().getStringList("teams." + secondTeam + ".players");

        zoomoMap = "§b§lFury Tide";
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 41;
            @Override
            public void run() {
                if(runningTimers.containsKey("zoomogostart")) {
                    if (!pausedTimers.contains("zoomogostart")) {
                        timeLeft--;
                        runningTimers.get("zoomogostart").setValue(timeLeft);
                        bossBarBgTest();
                        switch (timeLeft) {
                            case 40:
                                for (Player p : getPlayers()) {
                                    lastHitPlayer.put(p.getName(), "");
                                }
                                finaleTeamTeleportSeparate("zoomofinale", 5);
                                teleportSpectators(TeleportConfig.get().getLocation("spectators.zoomofinale"), 5);
                                resetZoomoGo();
                                resetModePoints();
                                break;
                            case 35:
                                for(String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)){
                                    if(TeamsConfig.get().getStringList("teams." + team + ".players").isEmpty()) continue;
                                    if(!Objects.equals(team, firstTeam) && !Objects.equals(team, secondTeam)){
                                        deadTeams.add(team);
                                        deadPlayers.addAll(TeamsConfig.get().getStringList("teams." + team + ".players"));
                                    }
                                }

                                for(Player p : getPlayers()){
                                    if(!secondPlayers.contains(p.getName()) && !firstPlayers.contains(p.getName())){
                                        ghostManager.addGhostPlayer(p.getName());
                                    }
                                }

                                currentMode = "Zoomo Go";
                                try {
                                    glowTeams();
                                } catch (ReflectiveOperationException e) {
                                    throw new RuntimeException(e);
                                }
                                for (Player player : getPlayers()) {
                                    if(!ghostManager.getGhostPlayers().contains(player.getName())) {
                                        PotionEffect PotionEffect = new PotionEffect(PotionEffectType.SLOW_FALLING, 120, 1, false, false);
                                        player.addPotionEffect(PotionEffect);
                                        player.setGameMode(GameMode.ADVENTURE);
                                    }
                                }
                                for (int i = 1; i <= 15; i++) {
                                    summonIsland(zoomoFinaleIslands(i));
                                    if (i > 3) {
                                        destroyIsland(zoomoFinaleIslands(i));
                                    }
                                }
                                break;
                            case 30:
                                playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    messagePlayer(player, """
                                        §8
                                        §8
                                        §r⏳ §eWelcome to §a§lZoomo Go Finale§e! Last team standing.. eliminate the opposing team before the islands crumble.
                                        §8
                                        """);
                                    player.sendTitle("§a§lLast Team", "§a§lStanding.", 0, 40, 20);
                                }
                                break;
                            case 20:
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
                                break;
                            case 10:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
                                ItemStack knockbackStick = new ItemStack(Material.STICK);
                                knockbackStick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);
                                for (Player player : getPlayers()) {
                                    if(firstPlayers.contains(player.getName()) || secondPlayers.contains(player.getName())) {
                                        player.getInventory().addItem(knockbackStick);
                                    }
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
                                    if(firstPlayers.contains(player.getName()) || secondPlayers.contains(player.getName())) {
                                        player.addPotionEffect(PotionEffect);
                                    }
                                    player.sendTitle("§a§l▶ ZOOMO GO! ◀", "", 0, 40, 0);
                                }

                                ghostManager.giveCompasses();

                                runningTimers.remove("zoomogostart");
                                cancel();
                                initiateZoomoFinaleIslands();
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

        runningTimers.put("zoomogostart", new AbstractMap.SimpleEntry<>(task, 41));
    }

    public void initiateZoomoIslands(){
        targetTime = 100;
        timerLabel = "Life Cap:";
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
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle("", "§e⚠ §f§lLIFE CAP BEGINS IN 5 SECONDS.. §e⚠", 0, 20, 0);
                                }
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 1);
                                summonIsland(zoomoIslands(15));
                                break;
                            case 104:
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendMessage("§e⚠ §f§lLIFE CAP BEGINS IN 4 SECONDS.. §e⚠");
                                }
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 1);
                                destroyIsland(zoomoIslands(8));
                                break;
                            case 103:
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendMessage("§e⚠ §f§lLIFE CAP BEGINS IN 3 SECONDS.. §e⚠");
                                }
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 1);
                                destroyIsland(zoomoIslands(11));
                                break;
                            case 102:
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendMessage("§e⚠ §f§lLIFE CAP BEGINS IN 2 SECONDS.. §e⚠");
                                }
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 1);
                                destroyIsland(zoomoIslands(12));
                                break;
                            case 101:
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendMessage("§e⚠ §f§lLIFE CAP BEGINS IN 1 SECONDS.. §e⚠");
                                }
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 1);
                                destroyIsland(zoomoIslands(9));
                                break;
                            case 100:
                                lifeCap = true;
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle("", "§e⚠ §6§lLIFE CAP ENABLED §e⚠", 0, 20, 0);
                                }
                                for(Player p : getPlayers()){
                                    if(!plugin.deadPlayers.contains(p.getName())){
                                        Block block = p.getLocation().subtract(0, 0.1, 0).getBlock();
                                        if (block.getType() != Material.AIR && plugin.zoomoLives.get(p.getName()) == 2) {
                                            zoomoLives.replace(p.getName(), 1);
                                            p.sendTitle("", "§e⚠ §6§lLIVES CAPPED: §f1§c❤ §e⚠", 0, 20, 0);
                                            plugin.earnPoints(p.getName(), 20, true);
                                            messagePlayer(p, "§e\uD83D\uDCB020 §7| §a§lBonus points awarded for keeping both lives.");
                                        }
                                    }
                                }
                                targetTime = 37;
                                timerLabel = "Final Island:";
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 2);
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
                                targetTime = 0;
                                timerLabel = "Sudden Death...";
                                suddenDeath = true;
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

    public void initiateZoomoDesertIslands(){
        targetTime = 93;
        timerLabel = "Life Cap:";
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
                            case 134:
                                summonIsland(zoomoDesertIslands(3));
                                break;
                            case 133:
                                summonIsland(zoomoDesertIslands(4));
                                break;
                            case 132:
                                summonIsland(zoomoDesertIslands(5));
                                break;
                            case 131:
                                summonIsland(zoomoDesertIslands(6));
                                break;
                            case 130:
                                destroyIsland(zoomoDesertIslands(17));
                                break;
                            case 129:
                                destroyIsland(zoomoDesertIslands(1));
                                break;
                            case 128:
                                destroyIsland(zoomoDesertIslands(2));
                                break;
                            case 117:
                                summonIsland(zoomoDesertIslands(7));
                                break;
                            case 116:
                                summonIsland(zoomoDesertIslands(8));
                                break;
                            case 115:
                                summonIsland(zoomoDesertIslands(9));
                                break;
                            case 114:
                                summonIsland(zoomoDesertIslands(10));
                                break;
                            case 113:
                                destroyIsland(zoomoDesertIslands(3));
                                break;
                            case 112:
                                destroyIsland(zoomoDesertIslands(4));
                                break;
                            case 111:
                                destroyIsland(zoomoDesertIslands(5));
                                break;
                            case 110:
                                destroyIsland(zoomoDesertIslands(6));
                                break;
                            case 99:
                                summonIsland(zoomoDesertIslands(11));
                                break;
                            case 98:
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle("", "§e⚠ §f§lLIFE CAP BEGINS IN 5 SECONDS.. §e⚠", 0, 20, 0);
                                }
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 1);
                                summonIsland(zoomoDesertIslands(12));
                                break;
                            case 97:
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendMessage("§e⚠ §f§lLIFE CAP BEGINS IN 4 SECONDS.. §e⚠");
                                }
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 1);
                                summonIsland(zoomoDesertIslands(13));
                                break;
                            case 96:
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendMessage("§e⚠ §f§lLIFE CAP BEGINS IN 3 SECONDS.. §e⚠");
                                }
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 1);
                                summonIsland(zoomoDesertIslands(14));
                                break;
                            case 95:
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendMessage("§e⚠ §f§lLIFE CAP BEGINS IN 2 SECONDS.. §e⚠");
                                }
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 1);
                                destroyIsland(zoomoDesertIslands(7));
                                break;
                            case 94:
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendMessage("§e⚠ §f§lLIFE CAP BEGINS IN 1 SECONDS.. §e⚠");
                                }
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 1);
                                destroyIsland(zoomoDesertIslands(8));
                                break;
                            case 93:
                                lifeCap = true;
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle("", "§e⚠ §6§lLIFE CAP ENABLED §e⚠", 0, 20, 0);
                                }
                                for(Player p : getPlayers()){
                                    if(!plugin.deadPlayers.contains(p.getName())){
                                        Block block = p.getLocation().subtract(0, 0.1, 0).getBlock();
                                        if (block.getType() != Material.AIR && plugin.zoomoLives.get(p.getName()) == 2) {
                                            zoomoLives.replace(p.getName(), 1);
                                            p.sendTitle("", "§e⚠ §6§lLIVES CAPPED: §f1§c❤ §e⚠", 0, 20, 0);
                                            plugin.earnPoints(p.getName(), 20, true);
                                            messagePlayer(p, "§e\uD83D\uDCB020 §7| §a§lBonus points awarded for keeping both lives.");
                                        }
                                    }
                                }
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 2);
                                destroyIsland(zoomoDesertIslands(9));
                                targetTime = 37;
                                timerLabel = "Final Island:";
                                break;
                            case 92:
                                destroyIsland(zoomoDesertIslands(10));
                                break;
                            case 81:
                                summonIsland(zoomoDesertIslands(15));
                                break;
                            case 80:
                                summonIsland(zoomoDesertIslands(16));
                                break;
                            case 79:
                                summonIsland(zoomoDesertIslands(18));
                                break;
                            case 78:
                                destroyIsland(zoomoDesertIslands(11));
                                break;
                            case 77:
                                destroyIsland(zoomoDesertIslands(12));
                                break;
                            case 76:
                                destroyIsland(zoomoDesertIslands(13));
                                break;
                            case 75:
                                destroyIsland(zoomoDesertIslands(14));
                                break;
                            case 64:
                                summonIsland(zoomoDesertIslands(19));
                                targetTime = 0;
                                timerLabel = "Sudden Death...";
                                suddenDeath = true;
                                break;
                            case 59:
                                destroyIsland(zoomoDesertIslands(16));
                                break;
                            case 54:
                                destroyIsland(zoomoDesertIslands(15));
                                break;
                            case 49:
                                destroyIsland(zoomoDesertIslands(18));
                                break;
                            case 30:
                                destroyIsland(zoomoDesertIslands(19));
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


    public void initiateZoomoFinaleIslands(){
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
                            case 129:
                                destroyIsland(zoomoFinaleIslands(1));
                                break;
                            case 120:
                                summonIsland(zoomoFinaleIslands(4));
                                break;
                            case 115:
                                summonIsland(zoomoFinaleIslands(5));
                                summonIsland(zoomoFinaleIslands(6));
                                break;
                            case 110:
                                destroyIsland(zoomoFinaleIslands(2));
                                destroyIsland(zoomoFinaleIslands(3));
                                break;
                            case 105:
                                destroyIsland(zoomoFinaleIslands(4));
                                break;
                            case 95:
                                summonIsland(zoomoFinaleIslands(7));
                                break;
                            case 90:
                                destroyIsland(zoomoFinaleIslands(5));
                                destroyIsland(zoomoFinaleIslands(6));
                                break;
                            case 80:
                                summonIsland(zoomoFinaleIslands(10));
                                break;
                            case 70:
                                summonIsland(zoomoFinaleIslands(8));
                                summonIsland(zoomoFinaleIslands(9));
                                break;
                            case 65:
                                destroyIsland(zoomoFinaleIslands(7));
                                break;
                            case 60:
                                summonIsland(zoomoFinaleIslands(11));
                                break;
                            case 58:
                                destroyIsland(zoomoFinaleIslands(8));
                                destroyIsland(zoomoFinaleIslands(9));
                                break;
                            case 50:
                                destroyIsland(zoomoFinaleIslands(10));
                                break;
                            case 45:
                                summonIsland(zoomoFinaleIslands(12));
                                summonIsland(zoomoFinaleIslands(13));
                                break;
                            case 43:
                                destroyIsland(zoomoFinaleIslands(11));
                                break;
                            case 35:
                                summonIsland(zoomoFinaleIslands(14));
                                break;
                            case 25:
                                destroyIsland(zoomoFinaleIslands(12));
                                destroyIsland(zoomoFinaleIslands(13));
                                break;
                            case 15:
                                summonIsland(zoomoFinaleIslands(15));
                                break;
                            case 5:
                                destroyIsland(zoomoFinaleIslands(14));
                                break;
                            case 0:
                                destroyIsland(zoomoFinaleIslands(15));
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

    public int[] zoomoFinaleIslands(int index){

        int[][] coords = {
                {1918, 1479},
                {1934, 1503},
                {1898, 1504},
                {1915, 1524},
                {1947, 1534},
                {1890, 1534},
                {1919, 1546},
                {1938, 1570},
                {1899, 1570},
                {1920, 1568},
                {1920, 1591},
                {1933, 1616},
                {1908, 1615},
                {1920, 1643},
                {1921, 1667}
        };


        return coords[index-1];
    }

    public int[] zoomoDesertIslands(int index){

        int[][] coords = {
                {-164, 698},
                {-166, 732},
                {-195, 725},
                {-200, 744},
                {-219, 723},
                {-228, 740},
                {-242, 720},
                {-250, 738},
                {-261, 715},
                {-240, 696},
                {-262, 680},
                {-256, 661},
                {-242, 671},
                {-227, 652},
                {-221, 673},
                {-194, 652},
                {-166, 663},
                {-195, 676},
                {-206, 698}
        };


        return coords[index-1];
    }

    public void zoomoDesertRespawn(Player player) {
        int targetY = 160;

        World world = Bukkit.getWorld("build");

        Location corner1 = new Location(world,-273, 160, 752);
        Location corner2 = new Location(world,-147, 160, 639);

        int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
        int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
        int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
        int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());

        List<Block> validGrassBlocks = new ArrayList<>();

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                Block block = world.getBlockAt(x, targetY, z);

                if (block.getType() == Material.SAND || block.getType() == Material.BLUE_ICE || block.getType() == Material.PACKED_ICE) {
                    boolean isSurroundedByGrass = true;

                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dz = -1; dz <= 1; dz++) {
                            if (dx == 0 && dz == 0) continue;

                            Block adjacentBlock = world.getBlockAt(x + dx, targetY, z + dz);
                            if (adjacentBlock.getType() != Material.SAND && adjacentBlock.getType() != Material.BLUE_ICE && adjacentBlock.getType() != Material.PACKED_ICE) {
                                isSurroundedByGrass = false;
                                break;
                            }
                        }
                        if (!isSurroundedByGrass) break;
                    }

                    if (isSurroundedByGrass) {
                        validGrassBlocks.add(block);
                    }
                }
            }
        }

        if (!validGrassBlocks.isEmpty()) {
            Random random = new Random();
            Block selectedBlock = validGrassBlocks.get(random.nextInt(validGrassBlocks.size()));

            player.teleport(new Location(world, selectedBlock.getX() + 0.5, targetY + 1, selectedBlock.getZ() + 0.5));
        }
    }


    public void zoomoRespawn(Player player) {
        int targetY = 157;

        World world = Bukkit.getWorld("build");

        Location corner1 = new Location(world,-101, 157, 539);
        Location corner2 = new Location(world,12, 157, 870);

        int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
        int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
        int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
        int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());

        List<Block> validGrassBlocks = new ArrayList<>();

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                Block block = world.getBlockAt(x, targetY, z);

                if (block.getType() == Material.GRASS_BLOCK || block.getType() == Material.BLUE_ICE || block.getType() == Material.PACKED_ICE) {
                    boolean isSurroundedByGrass = true;

                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dz = -1; dz <= 1; dz++) {
                            if (dx == 0 && dz == 0) continue;

                            Block adjacentBlock = world.getBlockAt(x + dx, targetY, z + dz);
                            if (adjacentBlock.getType() != Material.GRASS_BLOCK && adjacentBlock.getType() != Material.BLUE_ICE && adjacentBlock.getType() != Material.PACKED_ICE) {
                                isSurroundedByGrass = false;
                                break;
                            }
                        }
                        if (!isSurroundedByGrass) break;
                    }

                    if (isSurroundedByGrass) {
                        validGrassBlocks.add(block);
                    }
                }
            }
        }

        if (!validGrassBlocks.isEmpty()) {
            Random random = new Random();
            Block selectedBlock = validGrassBlocks.get(random.nextInt(validGrassBlocks.size()));

            player.teleport(new Location(world, selectedBlock.getX() + 0.5, targetY + 1, selectedBlock.getZ() + 0.5));
        }
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
        activeIslands.add(new int[]{islandCoords[0], islandCoords[1]});
        if(Objects.equals(zoomoMap, "§6§lDesert")){
            y = 130;
        }
        if(currentRound == 3 && Objects.equals(zoomoMap, "§a§lAdrenaline Ravine")){
            y = 137;
        }
        if(currentRound == 3 && Objects.equals(zoomoMap, "§6§lDesert")){
            y = 129;
        }
        if(finaleActive){
            y = -54;
        }
        Bukkit.getWorld("build").getBlockAt(islandCoords[0], y, islandCoords[1]).setType(Material.REDSTONE_BLOCK);
        Bukkit.getWorld("build").getBlockAt(islandCoords[0], y, islandCoords[1]).setType(Material.STONE);
    }

    public void destroyIsland(int[] islandCoords){
        int y = 135;
        Iterator<int[]> it = activeIslands.iterator();
        while (it.hasNext()) {
            if (Arrays.equals(it.next(), islandCoords)) {
                it.remove();
                break;
            }
        }
        if(Objects.equals(zoomoMap, "§6§lDesert")){
            y = 128;
        }
        if(finaleActive){
            y = -55;
        }
        final String name = ("islandtimer" + UUID.randomUUID());
        Bukkit.getWorld("build").getBlockAt(islandCoords[0], y, islandCoords[1]).setType(Material.REDSTONE_BLOCK);
        Bukkit.getWorld("build").getBlockAt(islandCoords[0], y, islandCoords[1]).setType(Material.STONE);
        int finalY = y;
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 6;
            @Override
            public void run() {
                if(runningTimers.containsKey(name)) {
                    if (!pausedTimers.contains(name)) {
                        runningTimers.get(name).setValue(timeLeft);
                        timeLeft--;
                        if (timeLeft == 0) {
                            Bukkit.getWorld("build").getBlockAt(islandCoords[0], finalY -1, islandCoords[1]).setType(Material.REDSTONE_BLOCK);
                            Bukkit.getWorld("build").getBlockAt(islandCoords[0], finalY -1, islandCoords[1]).setType(Material.STONE);
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
        lifeCap = false;
        deadPlayers.clear();
        deadTeams.clear();
        if(currentRound == 1) {
            playerKillCount.clear();
        }
        zoomoLives.clear();
        activeIslands.clear();
        for(Player player : getPlayers()){
            if(currentRound == 1) {
                playerKillCount.put(player.getName(), 0);
            }
            zoomoLives.put(player.getName(), 2);
        }
    }



    public void startGubGame(){
        pvpArenaManager.disablePvPArena();
        multiplier = GameOrderConfig.get().getDouble("multiplier");
        fillVotingSpace(5);
        musicManager.stopMusicAll();
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
                                ghostManager.removeGhostPlayer(player.getName());
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
        pvpArenaManager.disablePvPArena();
        multiplier = GameOrderConfig.get().getDouble("multiplier");
        fillVotingSpace(6);
        musicManager.stopMusicAll();
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
                                ghostManager.removeGhostPlayer(player.getName());
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

    public static HashMap<String, Integer> getTeamPointsFromConfig() {
        HashMap<String, Integer> teamPoints = new HashMap<>();

        if (TeamsConfig.get().contains("teams")) {
            for (String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
                int points = TeamsConfig.get().getInt("teams." + team + ".points", 0); // default 0
                teamPoints.put(team, points);
            }
        }

        return teamPoints;
    }

    public static HashMap<String, Integer> getPointsFromConfig() {
        HashMap<String, Integer> playerPoints = new HashMap<>();

        if (PlayerConfig.get().contains("players")) {
            for (String name : PlayerConfig.get().getConfigurationSection("players").getKeys(false)) {
                int points = PlayerConfig.get().getInt("players." + name + ".points", 0); // default 0
                playerPoints.put(name, points);
            }
        }

        return playerPoints;
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

    public LinkedHashMap<Integer, Integer> sortIntMap(HashMap map) {

        List<Map.Entry<Integer, Integer>> list = new LinkedList<>(map.entrySet());

        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        LinkedHashMap<Integer, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, Integer> entry : list) {
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
//                messagePlayer(player, String.format("%-15s%15s", placement + ". " + getTeamDisplayName(key), "§e§l\uD83D\uDCB0" + sortMap(modeTeamPoints).get(key) + " §7(\uD83D\uDCB0" + Math.floor(sortMap(modeTeamPoints).get(key) / multiplier)) + " §7§ox" + String.format("%.2f", multiplier) + ")");
                messagePlayer(player, formatLine(placement + ". " + getTeamDisplayName(key), "§e§l\uD83D\uDCB0" + sortMap(modeTeamPoints).get(key) + " §7(\uD83D\uDCB0" + Math.floor(sortMap(modeTeamPoints).get(key) / multiplier) + " §7§ox" + String.format("%.2f", multiplier) + ")", 250));
            }
            messagePlayer(player, "§f--------------------------");
        }
    }

    public void getTeamModeFullPoints(){
        for(Player player : Bukkit.getOnlinePlayers()) {
            messagePlayer(player, " §f-  §e§l   ᴍᴏᴅᴇ ᴛᴇᴀᴍ ʟᴇᴀᴅᴇʀs  §f-");
            int placement = 0;
            for (String key : sortMap(modeTeamFullPoints).keySet()) {
                placement++;
//                messagePlayer(player, String.format("%-15s%15s", placement + ". " + getTeamDisplayName(key), "§e§l\uD83D\uDCB0" + sortMap(modeTeamPoints).get(key) + " §7(\uD83D\uDCB0" + Math.floor(sortMap(modeTeamPoints).get(key) / multiplier)) + " §7§ox" + String.format("%.2f", multiplier) + ")");
                messagePlayer(player, formatLine(placement + ". " + getTeamDisplayName(key), "§e§l\uD83D\uDCB0" + sortMap(modeTeamPoints).get(key) + " §7(\uD83D\uDCB0" + Math.floor(sortMap(modeTeamPoints).get(key) / multiplier) + " §7§ox" + String.format("%.2f", multiplier) + ")", 250));
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
//                        messagePlayer(player, String.format("%-15s%15s", i+1 + ". " + getPlayerDisplayName(players.get(i)), "§e§l\uD83D\uDCB0" + points.get(i)));
                        messagePlayer(player, formatLine(i+1 + ". " + getPlayerDisplayName(players.get(i)), "§e§l\uD83D\uDCB0" + points.get(i), 210));
                    }
                }
            }
            messagePlayer(player, "§f--------------------------");
        }
        for(Player p : getPlayers()) {
            index = 1;
            for (String player2 : players) {
                if (p.getName().equals(player2)){
//                    messagePlayer(p, String.format("%-15s%15s", index + ". " + getPlayerDisplayName(players.get(index-1)), "§e§l\uD83D\uDCB0" + points.get(index-1)));
                    messagePlayer(p, formatLine(index + ". " + getPlayerDisplayName(players.get(index-1)), "§e§l\uD83D\uDCB0" + points.get(index-1), 210));
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
                            removeVotingPiece(indexRemove, data);
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
                messagePlayer(player, formatLine(placement + ". " + plugin.modeColors.get(key) + key, "§e§l" + leaderModeVotes.get(placement-1) + " spaces §e§o(" + percentage + "%)", 210));
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
                                GameOrderConfig.get().set("multiplier", multiplierValue);
                                GameOrderConfig.save();
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

        pvpArenaManager.disablePvPArena();

        if(GameOrderConfig.get().getStringList("order").isEmpty()){
            multiplier = 0.0;
            GameOrderConfig.get().set("multiplier", 0.0);
            GameOrderConfig.save();
        }
        clearInventories();

        thrownSnowballs.clear();
        for(Player p : Bukkit.getOnlinePlayers()){
            thrownSnowballs.put(p, new ArrayList<>());
        }
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
                                        break;
                                    case 2,4,6:
                                        changeMultiplier(multiplier+0.5);
                                        playSoundAll(Sound.BLOCK_BEACON_ACTIVATE, (float) ((multiplier+0.5)/2));
                                        break;
                                    default:
                                        timeLeft = 91;
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
                                    messagePlayer(player, """
                                            §8
                                            §8
                                            §e§lVoting Time!
                                            §fWalk into your mode of choice and paint the floor with as many blocks as possible! If you change your mind simply run back to the mode selection and get painting!
                                            §8
                                            """);
                                }
                                break;
                            case 74:
                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
                                break;
                            case 67:
                                switch(GameOrderConfig.get().getStringList("order").size()){
//                                    case 0,3,6:
//                                        votingMode = "guns";
//                                        break;
//                                    case 2,5:
//                                        votingMode = "bounce";
//                                        break;
                                    default:
                                        votingMode = "bounce";
                                        break;
                                }
                                if(Objects.equals(votingMode, "guns")){
                                    ItemStack stack = new ItemStack(Material.BOW);
                                    ItemMeta meta = stack.getItemMeta();
                                    meta.setItemModel(new NamespacedKey("amongus", "voteblaster"));
                                    meta.setDisplayName("§a§lVote Blaster!");
                                    meta.addEnchant(Enchantment.INFINITY, 1, true);
                                    stack.setItemMeta(meta);

                                    ItemStack arrow = new ItemStack(Material.ARROW);
                                    ItemMeta arrowmeta = stack.getItemMeta();
                                    arrowmeta.setDisplayName("§f§lARROW!");
                                    arrow.setItemMeta(arrowmeta);
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        player.sendTitle("§f§lᴠᴏᴛᴇ ᴍᴏᴅᴇ", "§a§lBlasters", 0, 40, 20);
                                        messagePlayer(player, """
                                            §8
                                            §8
                                            §f§lᴠᴏᴛᴇ ᴍᴏᴅᴇ: §a§lBlasters!
                                            §fGrab your blaster, charge it up using §aRight Click§f, then release and paint!
                                            §8
                                            """);
                                        player.getInventory().addItem(stack);
                                        player.getInventory().setItem(35, arrow);
                                    }
                                }
                                if(Objects.equals(votingMode, "bounce")){
                                    ItemStack stack = new ItemStack(Material.BOW);
                                    ItemMeta meta = stack.getItemMeta();
                                    meta.setItemModel(new NamespacedKey("amongus", "slingshot"));
                                    meta.setDisplayName("§a§lSling Shot!");
                                    meta.addEnchant(Enchantment.INFINITY, 1, true);
                                    stack.setItemMeta(meta);

                                    ItemStack arrow = new ItemStack(Material.ARROW);
                                    ItemMeta arrowmeta = stack.getItemMeta();
                                    arrowmeta.setDisplayName("§f§lARROW!");
                                    arrow.setItemMeta(arrowmeta);

                                    for(Player p : getPlayers()){
                                        SulfurCube cube = (SulfurCube) p.getWorld().spawnEntity(p.getLocation(), EntityType.SULFUR_CUBE);

                                        EntityEquipment equipment = cube.getEquipment();
                                        if (equipment != null) {
                                            equipment.setItem(EquipmentSlot.BODY, new ItemStack(Material.WHITE_CONCRETE));
                                        }

                                        cube.addPassenger(p);

                                        votingCubes.add(cube);
                                    }

                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        player.sendTitle("§f§lᴠᴏᴛᴇ ᴍᴏᴅᴇ", "§7§lBounce", 0, 40, 20);
                                        messagePlayer(player, """
                                            §8
                                            §8
                                            §f§lᴠᴏᴛᴇ ᴍᴏᴅᴇ: §7§lBounce!
                                            §fGrab your slingshot, charge it up using §aRight Click§f, then release and get sent flying on your sulfur companion!
                                            §8
                                            """);
                                        player.getInventory().addItem(stack);
                                        player.getInventory().setItem(35, arrow);
                                    }
                                }
                                if(Objects.equals(votingMode, "walk")){
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        player.sendTitle("§f§lᴠᴏᴛᴇ ᴍᴏᴅᴇ", "§e§lWalk!", 0, 40, 20);
                                        messagePlayer(player, """
                                            §8
                                            §8
                                            §f§lᴠᴏᴛᴇ ᴍᴏᴅᴇ: §e§lWalk!
                                            §fRun as fast as you can to turn the floor below you into your voted mode, your boots are your paintbrush!
                                            §8
                                            """);
                                    }
                                }
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

                                if(votingMode.equals("bounce")){
                                    votingTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {

                                        if (!plugin.votingEnabled || !plugin.votingMode.equals("bounce")) {
                                            return;
                                        }

                                        for (World world : Bukkit.getWorlds()) {
                                            for (SulfurCube entity : world.getEntitiesByClass(SulfurCube.class)) {

                                                Player rider = null;
                                                for (Entity passenger : entity.getPassengers()) {
                                                    if (passenger instanceof Player player) {
                                                        rider = player;
                                                        break;
                                                    }
                                                }

                                                if (rider == null) continue;

                                                Block below = entity.getLocation().getBlock().getRelative(BlockFace.DOWN);

                                                for (Material concrete : getConcreteColours()) {
                                                    if (below.getType().equals(concrete)) {
                                                        String colour = concrete.toString().toUpperCase().replace("CONCRETE", "WOOL");
                                                        Material wool = Material.getMaterial(colour);
                                                        if (!wool.equals(plugin.playerVote.get(rider))) {
                                                            rider.sendTitle(plugin.woolLogos.get(wool), "", 0, 20, 10);
                                                            rider.playSound(rider.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, 1.0F);
                                                        }
                                                        plugin.playerVote.put(rider, wool);
                                                    }
                                                }

                                                for (Material wool : getWoolColors()) {
                                                    if (below.getType().equals(wool)) {
                                                        if (plugin.playerVote.containsKey(rider)) {
                                                            below.setType(plugin.playerVote.get(rider));
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }, 0L, 1L);
                                }

                                voteFrozenManager.startPlayerCheck();
                                break;
                            case 57:
                                messageConsole("case 57 reached.");
                                try {
                                    summonSlimeBall(27);
                                } catch (ReflectiveOperationException e) {
                                    throw new RuntimeException(e);
                                }
                            case 55:
//                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
//                                if(voteParty) {
//                                    summonPowerUp(25);
//                                    summonPowerUp(25);
//                                    summonPowerUp(25);
//                                    summonPowerUp(25);
//                                }
//                                summonPowerUp(25);
                                int radius = 18;
                                int amount = 5 - itemBoxCount.getOrDefault(0, 0);
                                if (amount > 0) {
                                    List<Location> positions = generatePositions(Bukkit.getWorld("build"), 144, radius, amount);
                                    for (Location loc : positions) {
                                        summonItemBox(loc);
                                        itemBoxCount.put(0, itemBoxCount.getOrDefault(0, 0) + 1);
                                    }
                                }
                                break;
                            case 50:
//                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
//                                if(voteParty) {
//                                    summonPowerUp(20);
//                                    summonPowerUp(20);
//                                    summonPowerUp(20);
//                                    summonPowerUp(20);
//                                }
//                                summonPowerUp(20);
                                int radius2 = 18;
                                int amount2 = 5 - itemBoxCount.getOrDefault(0, 0);
                                if (amount2 > 0) {
                                    List<Location> positions = generatePositions(Bukkit.getWorld("build"), 144, radius2, amount2);
                                    for (Location loc : positions) {
                                        summonItemBox(loc);
                                        itemBoxCount.put(0, itemBoxCount.getOrDefault(0, 0) + 1);
                                    }
                                }
                                break;
                            case 40:
//                                playSoundAll(Sound.BLOCK_NOTE_BLOCK_BIT, 1);
//                                if(voteParty) {
//                                    summonPowerUp(10);
//                                    summonPowerUp(10);
//                                    summonPowerUp(10);
//                                    summonPowerUp(10);
//                                }
//                                summonPowerUp(10);
                                int radius3 = 18;
                                int amount3 = 5 - itemBoxCount.getOrDefault(0, 0);
                                if (amount3 > 0) {
                                    List<Location> positions = generatePositions(Bukkit.getWorld("build"), 144, radius3, amount3);
                                    for (Location loc : positions) {
                                        summonItemBox(loc);
                                        itemBoxCount.put(0, itemBoxCount.getOrDefault(0, 0) + 1);
                                    }
                                }
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
                                    player.getInventory().clear();
                                }

                                if(votingMode.equals("bounce") && votingTask != null) {
                                    votingTask.cancel();

                                    Iterator<SulfurCube> iterator = plugin.votingCubes.iterator();

                                    while (iterator.hasNext()) {
                                        SulfurCube cube = iterator.next();
                                        cube.remove();
                                        iterator.remove();
                                    }
                                }

                                Iterator<ItemBox> iterator = plugin.itemBoxes.iterator();

                                while (iterator.hasNext()) {
                                    ItemBox ib = iterator.next();
                                    ib.despawn();
                                    iterator.remove();
                                }

                                voteFrozenManager.stopPlayerCheck();
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

    private Material[] getConcreteColours() {
        return new Material[]{
                Material.RED_CONCRETE, Material.WHITE_CONCRETE, Material.ORANGE_CONCRETE, Material.LIGHT_BLUE_CONCRETE, Material.LIME_CONCRETE,
                Material.YELLOW_CONCRETE, Material.PURPLE_CONCRETE

        };
    }

    public List<Integer> teamPlacementPoints(){
        return List.of(25, 20, 15, 10, 8, 5, 2, 1);
    }

    public void gameEnd(){
        runningTimers.clear();
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.stopAllSounds();
            Objects.requireNonNull(player.getAttribute(Attribute.MOVEMENT_SPEED)).setBaseValue(0.1);
        }

        if(crumblePearlTask != null) {
            crumblePearlTask.cancel();
            crumblePearlTask = null;
        }

        if(currentMode.equals("Dimension Dash")) {
            ddTimer.cancel();
            for(TextDisplay td : ddMapTiles){
                td.remove();
            }
            ddMapTiles.clear();
        }

        playSoundAll(Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1F);

        suddenDeath = false;

//        List<String> modepoints = new ArrayList<>(plugin.sortMap(plugin.modePoints).keySet());
//            List<String> modeteampoints = new ArrayList<>(plugin.sortMap(plugin.modeTeamPoints).keySet());
//
//        int points = 32;
//        int teamIndex = 0;

        if(currentMode.equals("Zoomo Go") || currentMode.equals("Crumble Clash")){

            // ---------- INDIVIDUAL PLACEMENT ----------
            List<String> survivingPlayers = getPlayers().stream()
                    .map(Player::getName)
                    .collect(Collectors.toList());
            survivingPlayers.removeAll(deadPlayers);

            // Survivors all tie for 1st and get the max score
            for(String player : survivingPlayers){
                earnPoints(player, 32, true);
                Player p = Bukkit.getPlayer(player);
                if(p != null){
                    messagePlayer(p, "§6You have earned §e§u\uD83D\uDCB0" + 32 + "§6 for your individual placement.");
                }
            }

            // Dead players: reverse death order (last to die = best placement of the dead)
            List<String> deathOrderReversed = new ArrayList<>(deadPlayers);
            Collections.reverse(deathOrderReversed);

            int points2 = 32 - survivingPlayers.size();
            for(String player : deathOrderReversed){
                if(points2 < 1) break;
                earnPoints(player, points2, true);
                Player p = Bukkit.getPlayer(player);
                if(p != null){
                    messagePlayer(p, "§6You have earned §e§u\uD83D\uDCB0" + points2 + "§6 for your individual placement.");
                }
                points2--;
            }

            // ---------- TEAM PLACEMENT ----------
            List<String> allTeams = TeamsConfig.get().getConfigurationSection("teams").getKeys(false).stream()
                    .filter(team -> !TeamsConfig.get().getStringList("teams." + team + ".players").isEmpty())
                    .toList();

            String winningTeam = allTeams.stream()
                    .filter(team -> !deadTeams.contains(team))
                    .findFirst()
                    .orElse(null);

            if(winningTeam != null){
                int firstPlacePts = teamPlacementPoints().getFirst();
                List<String> teamPlayers = TeamsConfig.get().getStringList("teams." + winningTeam + ".players");
                for(String player : teamPlayers){
                    earnPoints(player, firstPlacePts, true);
                    Player p = Bukkit.getPlayer(player);
                    if(p != null){
                        messagePlayer(p, "§6You have earned §e§u\uD83D\uDCB0" + firstPlacePts + "§6 for your teams placement.");
                    }
                }
            }

            // Eliminated teams: reverse elimination order (last team eliminated = 2nd place, etc.)
            List<String> teamDeathOrderReversed = new ArrayList<>(deadTeams);
            Collections.reverse(teamDeathOrderReversed);

            int teamIndex2 = 1; // slot 0 (1st place) is taken by the winning team
            for(String team2 : teamDeathOrderReversed){
                if(teamIndex2 >= teamPlacementPoints().size()) break;
                int pts = teamPlacementPoints().get(teamIndex2);

                List<String> teamPlayers = TeamsConfig.get().getStringList("teams." + team2 + ".players");
                for(String player : teamPlayers){
                    earnPoints(player, pts, true);
                    Player p = Bukkit.getPlayer(player);
                    if(p != null){
                        messagePlayer(p, "§6You have earned §e§u\uD83D\uDCB0" + pts + "§6 for your teams placement.");
                    }
                }
                teamIndex2++;
            }
        }

        blockBreak = false;
        targetTime = 0;
        copperDecay = false;
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
                            ccRoundStarted = false;
                            timerLabel = "Returning to Lobby:";
                            pvpEnabled = false;
                            doubleJumpEnabled = false;
                            currentBorderRadius = 236;
                            newBorderRadius = 236;
                            killRecord.clear();
                            if(currentMode.equals("Dimension Dash")){
                                for(TextDisplay text : ddPortalTitles){
                                    text.remove();
                                }
                                ddPortalTitles.clear();
                            }
                            if(currentMode.equals("Crumble Clash")){
                                Iterator<ItemBox> iterator = plugin.itemBoxes.iterator();

                                while (iterator.hasNext()) {
                                    ItemBox ib = iterator.next();
                                    ib.despawn();
                                    iterator.remove();
                                }
                            }
                            if(currentMode.equals("Slime Golf")){
                                for(SulfurCube slime : slimeGolfSlime){
                                    slime.remove();
                                }
                                for (SulfurCube cube : Bukkit.getWorld("build").getEntitiesByClass(SulfurCube.class)) {
                                    cube.remove();
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
                                bossBars.get(player.getName()).forEach(bar -> bar.removePlayer(player));
                                bossBars.put(player.getName(), null);
                                plugin.ghostManager.addGhostPlayer(player.getName());
                                Bukkit.getScheduler().runTaskLater(plugin, () -> player.setFlying(true), 1L);
                                healFeedPlayer(player);

                            }
                            if(currentMode.equals("Bridge Builders")){

                                for (BlockDisplay display : blockToDisplay.values()) {
                                    display.remove();
                                }

                                blockToDisplay.clear();

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
                                    ItemStack pumpkin = new ItemStack(Material.CARVED_PUMPKIN);
                                    player.getInventory().clear();
                                    player.getInventory().setHelmet(pumpkin);
                                }
                            } else {
                                if(((currentMode.equals("Slime Golf") || currentMode.equals("Dimension Dash") || currentMode.equals("Zoomo Go")) && currentRound == 1) || ((currentMode.equals("Zoomo Go") || currentMode.equals("Slime Golf")) && currentRound == 2) || (currentMode.equals("Push Point") && currentRound < 7) || (currentMode.equals("Crumble Clash") && currentRound < 3)){
                                    timerLabel = "Round Over:";
                                    if(currentMode.equals("Zoomo Go") || currentMode.equals("Crumble Clash")){
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
                                        ItemStack pumpkin = new ItemStack(Material.CARVED_PUMPKIN);
                                        player.getInventory().clear();
                                        player.getInventory().setHelmet(pumpkin);
                                    }
                                } else {
                                    if(currentMode.equals("Zoomo Go")){
                                        for(Player player : Bukkit.getOnlinePlayers()){
                                            player.sendTitle("💎", "§e★ " + plugin.getTeamDisplayName(winningTeam), 0, 60, 40);
                                            ItemStack pumpkin = new ItemStack(Material.CARVED_PUMPKIN);
                                            player.getInventory().clear();
                                            player.getInventory().setHelmet(pumpkin);
                                        }
                                    } else {
                                        for (Player player : Bukkit.getOnlinePlayers()) {
                                            player.sendTitle("💎", "", 0, 60, 40);
                                            ItemStack pumpkin = new ItemStack(Material.CARVED_PUMPKIN);
                                            player.getInventory().clear();
                                            player.getInventory().setHelmet(pumpkin);
                                        }
                                    }
                                }
                                timeLeft = 51;
                            }
                            break;
                        case 66, 64, 62, 60, 58, 56:
                            bridgeCourseId++;
                            bridgeBuildTimeHandling(bridgeCourseId);
                            break;
                        case 54:
                            if(currentMode.equals("Zoomo Go") && currentRound < 3){
                                if(currentRound == 1) {
                                    teleportPlayers(TeleportConfig.get().getLocation("players.zoomointermission"), 0);
                                    teleportSpectators(TeleportConfig.get().getLocation("players.zoomointermission"), 0);
                                }
                                if(currentRound == 2){
                                    teleportPlayers(TeleportConfig.get().getLocation("players.zoomointermission2"), 0);
                                    teleportSpectators(TeleportConfig.get().getLocation("players.zoomointermission2"), 0);
                                }
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
                                if(!currentMode.equals("Push Point")) {
                                    for (boolean truefalse : plugin.teamShown) {
                                        if (truefalse) {
                                            getPlayerModePoints();
                                            break;
                                        }
                                    }
                                }
                            }
                            if(currentMode.equals("Push Point")){
                                pushPointPercentages();
                            }
                            break;
                        case 42:
                            if(!currentMode.equals("Push Point")) {
                                for (boolean truefalse : plugin.teamShown) {
                                    if (truefalse) {
                                        getTeamModePoints();
                                        break;
                                    }
                                }
                            }

                            if(currentMode.equals("Push Point")){
                                getCurrentPPStandings();
                            }
                            ppActive = false;
                            break;
                        case 40:
                            if(currentMode.equals("Push Point")){
                                for(TextDisplay text : new ArrayList<>(wallTexts.values())){
                                    text.remove();
                                }
                                for(TextDisplay text : new ArrayList<>(wallPushersTexts.values())){
                                    text.remove();
                                }
                                for(ItemDisplay wall : new ArrayList<>(mapWalls.keySet())){
                                    wall.remove();
                                }

                                mapWalls.clear();
                                wallTexts.clear();
                                wallPushersTexts.clear();
                                finalPushMovements.clear();
                            }
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
                            } else if(currentMode.equals("Dimension Dash") && currentRound < 2){
                                currentRound++;
                                startDimensionDash();
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
                            } else if(currentMode.equals("Push Point") && currentRound < 7) {
                                currentRound++;
                                startPushPoint();
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
                            } else if(currentMode.equals("Crumble Clash") && currentRound < 3){
                                currentRound++;
                                startCrumbleClash();
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
                        case 37:
                            mongoManager.updateLeaderboardAsync(plugin, getPointsFromConfig(), getTeamPointsFromConfig(),
                                    modeFullPoints, modeTeamFullPoints, currentMode);
                            break;
                        case 35:

                            musicManager.restartMusicAll();


                            currentMode = "Lobby";
                            for (Player player : getPlayers()) {
                                plugin.ghostManager.removeGhostPlayer(player.getName());
                                player.getInventory().clear();
                                ItemStack air = new ItemStack(Material.AIR);
                                player.getInventory().clear();
                                player.getInventory().setHelmet(air);
                            }
                            break;
                        case 34:
                            for (Player player : getPlayers()) {
                                player.setAllowFlight(false);
                            }
                            if(GameOrderConfig.get().getStringList("order").size() < 6) {
                                runAldo();
                            }
                            deadPlayers.clear();
                            deadTeams.clear();
                            startLobbyInterval(61);
                            pvpArenaManager.enablePvPArena();
                            break;
                        case 30:
                            if(GameOrderConfig.get().getStringList("order").size() == 6){
                                changeMultiplier(2.5);
                                for(int i = 0; i <= 7; i++){
                                    plugin.teamShown[i] = false;
                                }
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    messagePlayer(p, """
                                        §8
                                        §8
                                        §e§l[!] §6Points are now hidden!
                                        §8
                                        §8
                                        """);
                                }
                            }
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
            "Yip dip skiperoo!",
            "Whoopy doo-da day!",
            "{Positive dialogue}",
            "Hello chat!",
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
        boolean shown = true;
        for(int i = 0; i <= 7; i++){
            if(!plugin.teamShown[i]){
                shown = false;
                break;
            }
        }
        if(shown) {
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
                    if (!pausedTimers.contains("aldo")) {
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
                                for (String player2 : modeplayertop) {
                                    if (Bukkit.getPlayer(player2) != null) {
                                        Player player = Bukkit.getPlayer(player2);
                                        float random = 1F + r.nextFloat() * 0.3F;
                                        if (player.getName().equals("Kaelan_")) {
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
                                for (String player2 : modeplayertop) {
                                    if (Bukkit.getPlayer(player2) != null) {
                                        float random = 1F + r.nextFloat() * 0.3F;
                                        Player player = Bukkit.getPlayer(player2);
                                        if (player.getName().equals("Kaelan_")) {
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
            SequencedMap<String, Integer> orderedSlimeFinishes = sortMap(slimeFinishers).reversed();
            for (String team : orderedSlimeFinishes.keySet()) {
                messagePlayer(p, formatLine(getTeamDisplayName(team), "§e§l⏱§e" + getTimerValue(orderedSlimeFinishes.get(team)), 150));
            }
            for(String team : TeamsConfig.get().getConfigurationSection("teams").getKeys(false)) {
                if(!orderedSlimeFinishes.containsKey(team)) {
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
        ItemMeta meta = pickaxe.getItemMeta();
        meta.addEnchant(Enchantment.EFFICIENCY, 3, true);
        meta.setUnbreakable(true);
        pickaxe.setItemMeta(meta);
        ItemStack axe = new ItemStack(Material.IRON_AXE);
        meta = axe.getItemMeta();
        meta.addEnchant(Enchantment.EFFICIENCY, 3, true);
        meta.setUnbreakable(true);
        axe.setItemMeta(meta);
        ItemStack shovel = new ItemStack(Material.IRON_SHOVEL);
        meta = shovel.getItemMeta();
        meta.addEnchant(Enchantment.EFFICIENCY, 2, true);
        meta.setUnbreakable(true);
        shovel.setItemMeta(meta);
        ItemStack trident = new ItemStack(Material.TRIDENT);
        ItemMeta tridentMeta = trident.getItemMeta();
        tridentMeta.addEnchant(Enchantment.RIPTIDE, 3, true);
        tridentMeta.setUnbreakable(true);
        trident.setItemMeta(tridentMeta);
        ItemMeta swordMeta = sword.getItemMeta();
        swordMeta.addEnchant(Enchantment.LOOTING, 3, true);
        swordMeta.setUnbreakable(true);
        sword.setItemMeta(swordMeta);


        return new ItemStack[]{sword, pickaxe, axe, shovel, trident};
    }

    public void healFeedPlayer(Player p) {
        p.setHealth(20);
        p.setFoodLevel(20);
        p.setSaturation(20);
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

    public int countBelowTen() {
        int count = 0;
        for (int value : readyPlayers.values()) {
            if (value < 10) {
                count++;
            }
        }
        return count;
    }

    public void getReadyPlayers(){
        resetReady();
        pvpArenaManager.disablePvPArena();
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 31;
            @Override
            public void run() {
                if(!pausedTimers.contains("readytimer")) {
                    timeLeft--;
                    runningTimers.get("readytimer").setValue(timeLeft);
                    if(countBelowTen() == 0){
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            messagePlayer(player, "Everyone is ready!");
                            player.sendTitle("§a§lReady!", "Hooray!", 0, 60, 40);
                        }
                        runningTimers.remove("readytimer");
                        pvpArenaManager.enablePvPArena();
                        cancel();
                    } else {
                        switch (timeLeft) {
                            case 30:
                                String readyTypeString = "";
                                switch (readyType) {
                                    case "jump":
                                        readyTypeString = "Spam Jump!";
                                        break;
                                    case "snowballs":
                                        readyTypeString = "Throw Snowballs!";
                                        ItemStack snowballs = new ItemStack(Material.SNOWBALL, 15);
                                        for (Player player : getPlayers()) {
                                            player.getInventory().addItem(snowballs);
                                        }
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
                                for (Player p : getPlayers()) {
                                    p.getInventory().clear();
                                }
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
                                if (!notReady.isEmpty()) {
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
                                pvpArenaManager.enablePvPArena();
                            case 0:
                                runningTimers.remove("readytimer");
                                cancel();
                                break;
                            default:
                                break;
                        }
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
        musicManager.stopMusicAll();
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
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.sendTitle("§a§lʀᴇsᴜʟᴛs ᴛɪᴍᴇ", "", 0, 60, 20);
                            }
                            break;
                        case 3499, 3399, 3299, 3199:
                            for (int[] firework : lakeFireworks) {
                                world.getBlockAt(firework[0], firework[1] - 6, firework[2]).setType(Material.REDSTONE_BLOCK);
                                world.getBlockAt(firework[0], firework[1] - 6, firework[2]).setType(Material.AIR);
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
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.sendTitle(getTeamDisplayName(leaderteams.get(7)), "§f§l8ᴛʜ §7| §e\uD83D\uDCB0" + leaderteampoints.get(7), 0, 120, 0);
                            }
                            teamShown[7] = true;
                            break;
                        case 3400:
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.sendTitle(getTeamDisplayName(leaderteams.get(6)), "§f§l7ᴛʜ §7| §e\uD83D\uDCB0" + leaderteampoints.get(6), 0, 120, 0);
                            }
                            teamShown[6] = true;
                            break;
                        case 3300:
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.sendTitle(getTeamDisplayName(leaderteams.get(5)), "§f§l6ᴛʜ §7| §e\uD83D\uDCB0" + leaderteampoints.get(5), 0, 120, 0);
                            }
                            teamShown[5] = true;
                            break;
                        case 3200:
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.sendTitle(getTeamDisplayName(leaderteams.get(4)), "§f§l5ᴛʜ §7| §e\uD83D\uDCB0" + leaderteampoints.get(4), 0, 120, 0);
                            }
                            teamShown[4] = true;
                            break;
                        case 3100:
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.sendTitle(getTeamDisplayName(leaderteams.get(3)), "§f§l4ᴛʜ §7| §e\uD83D\uDCB0" + leaderteampoints.get(3), 0, 120, 0);
                            }
                            teamShown[3] = true;
                            break;

                        case 3000:
                            PotionEffect PotionEffect = new PotionEffect(PotionEffectType.LEVITATION, 100, 1, false, false);
                            for (Player player : getPlayers()) {
                                player.addPotionEffect(PotionEffect);
                            }
                            teleportPlayers(TeleportConfig.get().getLocation("players.finish"), 5);
                            teleportSpectators(TeleportConfig.get().getLocation("spectators.finish"), 5);
                            break;
                        case 2900:
                            PotionEffect PotionEffect2 = new PotionEffect(PotionEffectType.SLOW_FALLING, 100, 1, false, false);
                            for (Player player : getPlayers()) {
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
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                if (player.getName().equals("Kaelan_")) {
                                    player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_7, 1F, random);
                                } else {
                                    player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_6, 1F, random);
                                }
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("⏳ §f§lAre we ready? I'm ready."));
                            }
                            break;
                        case 2801:
                            random = 1F + r.nextFloat() * 0.3F;
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                if (player.getName().equals("Kaelan_")) {
                                    player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_7, 1F, random);
                                } else {
                                    player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_6, 1F, random);
                                }
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("⏳ §f§lThis will prove who is the one and only lad..."));
                            }
                            break;
                        case 2840:
                            startFinale();
                            Bukkit.getWorld("build").spawnParticle(Particle.CLOUD, aldo.getLocation(), 3, 0.0, 0.0, 0.0, 0);
                            aldo.remove();
                            runningTimers.remove("endevent");
                            cancel();
                            break;
                    }
                }
            }

        }.runTaskTimer(this, 0L, 1L);

        runningTimers.put("endevent", new AbstractMap.SimpleEntry<>(task, 91));

    }

    public void endEventFully(String winningTeam, String secondPlaceTeam){
        for(int i = 0; i <= 7; i++){
            teamShown[i] = true;
        }
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 3200;
            int z2 = 828;
            double pitch = 1.0;
            LivingEntity aldo;
            World world = Bukkit.getWorld("build");
            float random;
            Random r = new Random();
            List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());

            @Override
            public void run() {
                if (!pausedTimers.contains("endevent")) {
                    timeLeft--;
                    runningTimers.get("endevent").setValue(timeLeft);
                    switch (timeLeft) {
                        case 3179:
                            playMusicAll(Sound.MUSIC_DISC_TEARS);
                            break;
                        case 3100:
                            PotionEffect slowfall = new PotionEffect(PotionEffectType.SLOW_FALLING, 200, 1);
                            for(Player p : getPlayers()){
                                p.addPotionEffect(slowfall);
                            }
                            teleportPlayers(TeleportConfig.get().getLocation("players.ending"), 10);
                            teleportSpectators(TeleportConfig.get().getLocation("spectators.ending"), 10);
                            break;
                        case 2900:
                            Location aldoSpawn = new Location(Bukkit.getWorld("build"), 57.5, 196, 827.5);
                            aldo = (LivingEntity) Bukkit.getWorld("build").spawnEntity(aldoSpawn, EntityType.ALLAY);
                            Vector velocity = new Vector(-0.3, 0, 0);
                            Bukkit.getWorld("build").spawnParticle(Particle.CLOUD, aldo.getLocation(), 3, 0.0, 0.0, 0.0, 0);
                            aldo.setVelocity(velocity);
                            PotionEffect glowing = new PotionEffect(PotionEffectType.GLOWING, 20000, 0, false, false);
                            aldo.addPotionEffect(glowing);
                            break;
                        case 2880, 2870, 2860, 2850:
                            z2++;
                            pitch += 0.1;
                            Bukkit.getWorld("build").getBlockAt(52, 189, z2).setType(Material.REDSTONE_BLOCK);
                            Bukkit.getWorld("build").getBlockAt(52, 189, z2).setType(Material.AIR);
                            playSoundAll(Sound.BLOCK_GRASS_BREAK, (float) pitch);
                            break;
                        case 2800:
                            random = 1F + r.nextFloat() * 0.3F;
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                if (player.getName().equals("Kaelan_")) {
                                    player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_7, 1F, random);
                                } else {
                                    player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_6, 1F, random);
                                }
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("⏳ §f§lWhat a show! Let's see the final results."));
                            }
                            break;
                        case 2720:
                            Location thirdLoc = new Location(world, 63.5, 194, 838, 90, 0);
                            for (String player : TeamsConfig.get().getStringList("teams." + leaderteams.get(2) + ".players")) {
                                if (Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.teleport(thirdLoc);
                                    thirdLoc.setZ(thirdLoc.getZ() - 1);
                                }
                            }
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.sendTitle(getTeamDisplayName(leaderteams.get(2)), "§f§l3ʀᴅ", 0, 120, 0);
                            }
                            break;
                        case 2620:
                            Location secondLoc = new Location(world, 63.5, 195, 822, 90, 0);
                            for (String player : TeamsConfig.get().getStringList("teams." + secondPlaceTeam + ".players")) {
                                if (Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.teleport(secondLoc);
                                    secondLoc.setZ(secondLoc.getZ() - 1);
                                }
                            }
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.sendTitle(getTeamDisplayName(secondPlaceTeam), "§e§l2ɴᴅ", 0, 120, 20);
                            }
                            break;
                        case 2390:
                            Location firstLoc = new Location(world, 63.5, 196, 830, 90, 0);
                            for (String player : TeamsConfig.get().getStringList("teams." + winningTeam + ".players")) {
                                if (Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.teleport(firstLoc);
                                    firstLoc.setZ(firstLoc.getZ() - 1);
                                }
                            }
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.sendTitle(getTeamDisplayName(winningTeam), "§b§l1sᴛ", 0, 120, 20);
                            }
                            break;
                        case 2360, 2340, 2320, 2300, 2280, 2260, 2240, 2220, 2200:
                            world.getBlockAt(66, 188, 832).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(66, 188, 832).setType(Material.AIR);
                            world.getBlockAt(66, 188, 824).setType(Material.REDSTONE_BLOCK);
                            world.getBlockAt(66, 188, 824).setType(Material.AIR);
                            break;
                        case 2100:
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.sendTitle("§e§lᴛʜᴀɴᴋs ғᴏʀ ᴘʟᴀʏɪɴɢ", "", 0, 60, 20);
                            }
                            break;
                        case 2060:
                            random = 1F + r.nextFloat() * 0.3F;
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                if (player.getName().equals("Kaelan_")) {
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
                            teleportSpectators(TeleportConfig.get().getLocation("spectators.lobby"), 0);
                        case 1140:
                            pvpArenaManager.enablePvPArena();
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

    public void resetGameOrder() {
        GameOrderConfig.get().set("order", new ArrayList<>());
        GameOrderConfig.save();
    }

    public void resetCrumbleMapsConfig(){
        MapsPlayedConfig.get().set("maps.Crumble Clash", new ArrayList<>());
        MapsPlayedConfig.save();
    }

    public void resetDDMapsConfig(){
        MapsPlayedConfig.get().set("maps.Dimension Dash", new ArrayList<>());
        MapsPlayedConfig.save();
    }

    public void addPlayedMap(String game, String map) {
        List<String> gameMaps = MapsPlayedConfig.get().getStringList("maps." + game);

        gameMaps.add(map);

        MapsPlayedConfig.get().set("maps." + game, gameMaps);
        MapsPlayedConfig.save();
    }

    public void startTeamsPan(){
        String name = "teamPan";
        World world = Bukkit.getWorld("build");
        Location start = new Location(world, 160.4F, 152, 780.7F, -15.4F, 9.7F);
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
                        loc.add(0.004, 0, 0);
                        loc.setYaw(loc.getYaw() + 0.01975f);
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

    public void startCustomPan(String name){
        World world = Bukkit.getWorld("build");
        List<?> rawList = PanConfig.get().getList("pans." + name);

        List<Location> locs = new ArrayList<>();
        if (rawList != null) {
            for (Object obj : rawList) {
                if (obj instanceof Location) {
                    locs.add((Location) obj);
                }
            }
        }
        Location start = locs.getFirst();

        Chunk startChunk = start.getChunk();
        startChunk.load();
        for(Player p : Bukkit.getOnlinePlayers()) {
            p.setGameMode(GameMode.SPECTATOR);
            p.teleport(start);
        }
        BukkitTask task = new BukkitRunnable() {
            ArmorStand camera = null;
            int timeLeft = 261;
            int index = 0;
            Location loc;

            double distance;
            @Override
            public void run() {
                if (plugin.runningTimers.containsKey(name)) {
                    if (!plugin.pausedTimers.contains(name)) {
                        timeLeft--;
                        plugin.runningTimers.get(name).setValue(timeLeft);
                        if(camera != null && timeLeft < 241) {
                            camera.teleport(locs.get(index));
                        }
                        if(timeLeft == 250){
                            for(Player p : Bukkit.getOnlinePlayers()) {
                                p.teleport(start);
                            }
                        }
                        if(timeLeft == 245){
                            camera = (ArmorStand) world.spawnEntity(start, EntityType.ARMOR_STAND);
                            camera.setInvisible(true);
                            camera.setMarker(true);
                            camera.setGravity(false);
                            camera.setInvulnerable(true);
                            for(Player p : Bukkit.getOnlinePlayers()) {
                                if (p.getGameMode() == GameMode.SPECTATOR) {
                                    p.setSpectatorTarget(camera);
                                }
                            }
                        }
                        if(timeLeft < 241) {
                            index++;
                            for(Player p : Bukkit.getOnlinePlayers()) {
                                distance = p.getLocation().distance(camera.getLocation());

                                if (distance > 2) {
                                    p.setSpectatorTarget(null);

                                    p.setSpectatorTarget(camera);
                                }
                                if (p.getGameMode() == GameMode.SPECTATOR) {
                                    p.setSpectatorTarget(camera);
                                }
                            }
                            if(timeLeft == 20){
                                if(!Objects.equals(name, "logo")) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        player.sendTitle("\uE023", "", 20, 40, 20);
                                    }
                                }
                            }
                            if (timeLeft == 0) {
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.setSpectatorTarget(null);
                                }
                                camera.remove();
                                plugin.messageConsole("Pan finished.");
                                plugin.runningTimers.remove(name);
                                cancel();
                            }
                        }
                    }
                } else {
                    plugin.messageConsole("Timer removed by external factor.");
                    cancel();
                }
            }

        }.runTaskTimer(plugin, 0L, 1L);

        plugin.runningTimers.put(name, new AbstractMap.SimpleEntry<>(task, 241));
    }

    public void startDDPan1(){
        String name = "teamPan";
        World world = Bukkit.getWorld("build");
        Location start = new Location(world, 47.5F, 156, 1075.5F, 0, 34F);
        ArmorStand camera = (ArmorStand) world.spawnEntity(start, EntityType.ARMOR_STAND);
        camera.setInvisible(true);
        camera.setMarker(true);
        camera.setGravity(false);
        camera.setInvulnerable(true);
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 381;
            Location loc;
            @Override
            public void run() {
                if (runningTimers.containsKey(name)) {
                    if (!pausedTimers.contains(name)) {
                        timeLeft--;
                        runningTimers.get(name).setValue(timeLeft);
                        if(timeLeft > 360){
                            if(timeLeft == 380){
                                for(Player player : getPlayers()){
                                    player.teleport(start);
                                    player.setGameMode(GameMode.SPECTATOR);
                                }
                            }
                        } else {
                            if (timeLeft == 360) {
                                camera.teleport(start);
                            }
                            loc = camera.getLocation();
                            loc.add(0.0625, 0, 0);
                            camera.teleport(loc);
                            for (Player player : getPlayers()) {
                                if (player.getGameMode() == GameMode.SPECTATOR) {
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
                    }
                } else {
                    messageConsole("Timer removed by external factor.");
                    cancel();
                }
            }

        }.runTaskTimer(this, 0L, 1L);

        runningTimers.put(name, new AbstractMap.SimpleEntry<>(task, 401));
    }

    public void startDDPan2(){
        String name = "teamPan";
        World world = Bukkit.getWorld("build");
        Location start = new Location(world, 47.5F, 156, 1176.5F, 180F, 34F);
        ArmorStand camera = (ArmorStand) world.spawnEntity(start, EntityType.ARMOR_STAND);
        camera.setInvisible(true);
        camera.setMarker(true);
        camera.setGravity(false);
        camera.setInvulnerable(true);
        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 381;
            Location loc;
            @Override
            public void run() {
                if (runningTimers.containsKey(name)) {
                    if (!pausedTimers.contains(name)) {
                        timeLeft--;
                        runningTimers.get(name).setValue(timeLeft);
                        if(timeLeft > 360){
                            if(timeLeft == 380){
                                for(Player player : getPlayers()){
                                    player.teleport(start);
                                    player.setGameMode(GameMode.SPECTATOR);
                                }
                            }
                        } else {
                            if (timeLeft == 360) {
                                camera.teleport(start);
                            }
                            loc = camera.getLocation();
                            loc.add(0.0625, 0, 0);
                            camera.teleport(loc);
                            for (Player player : getPlayers()) {
                                if (player.getGameMode() == GameMode.SPECTATOR) {
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
                    }
                } else {
                    messageConsole("Timer removed by external factor.");
                    cancel();
                }
            }

        }.runTaskTimer(this, 0L, 1L);

        runningTimers.put(name, new AbstractMap.SimpleEntry<>(task, 401));
    }

    public void openDoors(ItemDisplay leftDoor, ItemDisplay rightDoor) {

        final int duration = 40;
        final float maxOffset = 3f;

        // Store original transforms ONCE
        Transformation leftBase = leftDoor.getTransformation();
        Transformation rightBase = rightDoor.getTransformation();

        new BukkitRunnable() {

            int tick = 0;

            @Override
            public void run() {

                if (tick >= duration) {
                    // Force exact final position
                    leftDoor.setTransformation(new Transformation(
                            new Vector3f(maxOffset, 0f, 0f),
                            leftBase.getLeftRotation(),
                            leftBase.getScale(),
                            leftBase.getRightRotation()
                    ));

                    rightDoor.setTransformation(new Transformation(
                            new Vector3f(-maxOffset, 0f, 0f),
                            rightBase.getLeftRotation(),
                            rightBase.getScale(),
                            rightBase.getRightRotation()
                    ));

                    cancel();
                    return;
                }

                float progress = (float) tick / duration;
                float offset = maxOffset * progress;

                leftDoor.setTransformation(new Transformation(
                        new Vector3f(offset, 0f, 0f),
                        leftBase.getLeftRotation(),
                        leftBase.getScale(),
                        leftBase.getRightRotation()
                ));

                rightDoor.setTransformation(new Transformation(
                        new Vector3f(-offset, 0f, 0f),
                        rightBase.getLeftRotation(),
                        rightBase.getScale(),
                        rightBase.getRightRotation()
                ));

                tick++;
            }

        }.runTaskTimer(plugin, 0L, 1L);
    }

    public void closeDoors(ItemDisplay leftDoor, ItemDisplay rightDoor) {

        final int duration = 40;
        final float maxOffset = 3f;

        Transformation leftBase = leftDoor.getTransformation();
        Transformation rightBase = rightDoor.getTransformation();

        new BukkitRunnable() {

            int tick = 0;

            @Override
            public void run() {

                if (tick >= duration) {
                    // Force exact closed position
                    leftDoor.setTransformation(new Transformation(
                            new Vector3f(0f, 0f, 0f),
                            leftBase.getLeftRotation(),
                            leftBase.getScale(),
                            leftBase.getRightRotation()
                    ));

                    rightDoor.setTransformation(new Transformation(
                            new Vector3f(0f, 0f, 0f),
                            rightBase.getLeftRotation(),
                            rightBase.getScale(),
                            rightBase.getRightRotation()
                    ));

                    cancel();
                    return;
                }

                float progress = (float) tick / duration;
                float offset = maxOffset * (1f - progress);

                leftDoor.setTransformation(new Transformation(
                        new Vector3f(offset, 0f, 0f),
                        leftBase.getLeftRotation(),
                        leftBase.getScale(),
                        leftBase.getRightRotation()
                ));

                rightDoor.setTransformation(new Transformation(
                        new Vector3f(-offset, 0f, 0f),
                        rightBase.getLeftRotation(),
                        rightBase.getScale(),
                        rightBase.getRightRotation()
                ));

                tick++;
            }

        }.runTaskTimer(plugin, 0L, 1L);
    }


    public void startEvent(){

        pvpArenaManager.disablePvPArena();

        shopAllowed = false;
        musicManager.stopMusicAll();

        resetGameOrder();

        World worldDef = Bukkit.getWorld("build");

        // Remove tinted glass from voting arena.
        for(int x = 233; x <= 241; x++){
            for(int y = 139; x <= 144; x++) {
                for (int z = 708; x <= 742; x++) {
                    if(worldDef.getBlockAt(x, y, z).getType().equals(Material.TINTED_GLASS)){
                        worldDef.getBlockAt(x, y, z).setType(Material.AIR);
                    }
                }
            }
        }

        multiplier = 0.0f;

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

        List<ItemDisplay> doors = new ArrayList<>();

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "blockclock start lobby");

        World world = Bukkit.getWorld("build");

        Location location = new Location(world, 161.5, 151.0, 791.9);

        ItemDisplay display = (ItemDisplay) world.spawnEntity(location, EntityType.ITEM_DISPLAY);

        // Set item
        display.setItemStack(new ItemStack(Material.SPRUCE_SHELF));

        // Set transformation
        Transformation transformation = new Transformation(
                new Vector3f(0f, 0f, 0f),                      // translation
                new AxisAngle4f(0f, 0f, 0f, 1f),                // left rotation
                new Vector3f(8f, 8f, 8f),                       // scale
                new AxisAngle4f((float) Math.toRadians(180), 0f, 1f, 0f) // right rotation
        );

        display.setTransformation(transformation);

        // Set display type (fixed)
        display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.FIXED);

        // Custom name
        display.setCustomName("test1");
        display.setCustomNameVisible(false);

        doors.add(display);

        Location location2 = new Location(world, 165.5, 151.0, 791.9);

        ItemDisplay display2 = (ItemDisplay) world.spawnEntity(location2, EntityType.ITEM_DISPLAY);

        display2.setItemStack(new ItemStack(Material.SPRUCE_SHELF));

        // Set transformation
        Transformation transformation2 = new Transformation(
                new Vector3f(0f, 0f, 0f),                      // translation
                new AxisAngle4f(0f, 0f, 0f, 1f),                // left rotation
                new Vector3f(8f, 8f, 8f),                       // scale
                new AxisAngle4f((float) Math.toRadians(180), 0f, 1f, 0f) // right rotation
        );

        display2.setTransformation(transformation2);

        // Set display type (fixed)
        display2.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.FIXED);

        // Custom name
        display2.setCustomName("test2");
        display2.setCustomNameVisible(false);

        doors.add(display2);

        world.setTime(6000);

        Location stage1 = new Location(Bukkit.getWorld("build"), 177, 144, 793, 90, 0);
        Location stage2 = new Location(Bukkit.getWorld("build"), 149, 144, 793, -90, 0);
        Location stagemain = new Location(Bukkit.getWorld("build"), 163.5, 149, 778.5, 0, 0);

        BukkitTask task1 = new BukkitRunnable() {
            int timeLeft = 60;

            @Override
            public void run() {
                if (plugin.runningTimers.containsKey("eventstart")) {
                    if (!plugin.pausedTimers.contains("eventstart")) {
                        timeLeft--;
                        plugin.runningTimers.get("eventstart").setValue(timeLeft);
                        bossBarBgTest();
                        if (timeLeft == 0) {
                            plugin.runningTimers.remove("eventstart");
                            cancel();
                        }
                    }
                } else {
                    plugin.messageConsole("Timer removed by external factor.");
                    cancel();
                }
            }

        }.runTaskTimer(plugin, 0L, 20L);

        plugin.runningTimers.put("eventstart", new AbstractMap.SimpleEntry<>(task1, 54));
        currentMode = "Start";

        BukkitTask task = new BukkitRunnable() {
            Random r = new Random();
            LivingEntity aldo;
            TextDisplay playersDisplay;
            TextDisplay teamNameDisplay;
            float currentXrotation = 0F;
            int direction = 1;
            int timeLeft = 3660;
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
                        case 3659:
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                player.sendTitle("\uE023", "", 20, 40, 20);
                            }
                            break;
                        case 3639:
                            startCustomPan("firstpath");
                            for(Player p : Bukkit.getOnlinePlayers()) {
                                ItemStack pumpkin = new ItemStack(Material.CARVED_PUMPKIN);
                                p.getInventory().clear();
                                p.getInventory().setHelmet(pumpkin);
                            }
                            break;
                        case 3599:
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle("§9§lsᴘᴇᴄɪᴀʟ ᴛʜᴀɴᴋs", "ᴛʜᴇ ʙᴜɪʟᴅ ᴛᴇᴀᴍ", 0, 50, 0);
                                messagePlayer(p, """
                                                §8
                                                §8
                                                §9§lThe Build Team!
                                                §fMalvare
                                                §fDerpMask
                                                §frheneye
                                                §fBeeA_Friend
                                                §fGusttafff
                                                §fjested
                                                §fCaptainZac
                                                §8
                                                """);
                            }
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 2);
                            break;
                        case 3562:
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle("§d§lsᴘᴇᴄɪᴀʟ ᴛʜᴀɴᴋs", "ᴍᴜsɪᴄ - ᴇᴀɢʟᴇᴀɢʟᴇ & ᴢᴏɪᴅʏ", 0, 50, 20);
                            }
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 2);
                            break;
                        case 3525:
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle("§a§lsᴘᴇᴄɪᴀʟ ᴛʜᴀɴᴋs", "sᴜᴘᴘᴏʀᴛᴇʀs", 0, 50, 0);
                                messagePlayer(p, """
                                                §8
                                                §8
                                                §9§lThe Supporters!
                                                §fAJX (Team Balancer)
                                                §fWeChokeOnMilk (Team Balancer)
                                                §fwhereismyjuless (Dev Assistant)
                                                §fLemonPhroggg (Voice Actor)
                                                §fItzIgglesPiggles (Voice Actor)
                                                §8
                                                """);
                            }
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 2);
                            break;
                        case 3488:
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle("§b§lsᴘᴇᴄɪᴀʟ ᴛʜᴀɴᴋs", "ᴛʜᴇ ɢʟᴏʀɪᴏᴜs ᴛᴇsᴛᴇʀs", 0, 50, 20);
                            }
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 2);
                            break;
                        case 3451:
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle("§6§lsᴘᴇᴄɪᴀʟ ᴛʜᴀɴᴋs", "ᴛʜᴇ ᴘᴀᴛʀᴇᴏɴs", 0, 50, 20);
                                messagePlayer(p, """
                                                §8
                                                §8
                                                §6§lThe Patreons!
                                                §fLoveFromNyx
                                                §fMrkvaMan
                                                §8
                                                """);
                            }
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 2);
                            break;
                        case 3374:
                            startCustomPan("lobbypath");
                            break;
                        case 3320:
                            for(Player p : Bukkit.getOnlinePlayers()){
                                p.sendTitle("§e§lʙʀᴏᴜɢʜᴛ ᴛᴏ ʏᴏᴜ ʙʏ", "ᴄʜᴀᴢᴢᴀɢʀᴀᴍ", 0, 50, 20);
                            }
                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 1.6F);
                            break;
//                        case 3280:
//                            for(Player p : Bukkit.getOnlinePlayers()){
//                                p.sendTitle("§e§lʙʀᴏᴜɢʜᴛ ᴛᴏ ʏᴏᴜ ʙʏ", "ᴄʜᴀᴢᴢᴀɢʀᴀᴍ", 0, 50, 20);
//                            }
//                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 1.8F);
//                            break;
//                        case 3240:
//                            for(Player p : Bukkit.getOnlinePlayers()){
//                                p.sendTitle("§e§lʙʀᴏᴜɢʜᴛ ᴛᴏ ʏᴏᴜ ʙʏ", "ᴄʜᴀᴢᴢᴀɢʀᴀᴍ", 0, 50, 20);
//                            }
//                            playSoundAll(Sound.BLOCK_NOTE_BLOCK_PLING, 2.0F);
//                            break;
                        case 3208:
                            Location aldoSpawn = new Location(Bukkit.getWorld("build"), 163.5, 143, 783.5);
                            aldo = (LivingEntity) Bukkit.getWorld("build").spawnEntity(aldoSpawn, EntityType.ALLAY);
                            Vector velocity = new Vector(0, 0, -0.3);
                            Bukkit.getWorld("build").spawnParticle(Particle.CLOUD, aldo.getLocation(), 3, 0.0, 0.0, 0.0, 0);
                            aldo.setVelocity(velocity);
                            PotionEffect glowing = new PotionEffect(PotionEffectType.GLOWING, 20000, 0, false, false);
                            aldo.addPotionEffect(glowing);
                            break;
                        case 3129:
                            startCustomPan("voting");
                            break;
                        case 3088:
                            playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1F);
                            for(Player player : Bukkit.getOnlinePlayers()){
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("⏳ §f§lHoly moly, it's Showdown time!"));
                            }
                            break;
                        case 3008:
                            float random = 1F + r.nextFloat() * 0.3F;
                            for(Player player : Bukkit.getOnlinePlayers()){
                                if (player.getName().equals("Kaelan_")) {
                                    player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_7, 1F, random);
                                } else {
                                    player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_6, 1F, random);
                                }
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("⏳ §f§lWe've had some friends invade the park..."));
                            }
                            break;
                        case 2935:
                            playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1F);
                            for(Player player : Bukkit.getOnlinePlayers()) {
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("⏳ §f§lWe had to wave goodbye to our slimy friends :("));
                            }
                            break;
                        case 2884:
                            startCustomPan("golf");
                            break;
                        case 2808:
                            playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1F);
                            for(Player player : Bukkit.getOnlinePlayers()){
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("⏳ §f§lBut some sulfury fellows have joined in their place!"));
                            }
                            break;
                        case 2688:
                            playSoundAll(Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1F);
                            for(Player player : Bukkit.getOnlinePlayers()){
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("⏳ §f§lWhy don't we get this show on the road!"));
                            }
                            break;
                        case 2639:
                            startCustomPan("logo");
                            break;
                        case 2568:
                            Bukkit.getWorld("build").spawnParticle(Particle.CLOUD, aldo.getLocation(), 3, 0.0, 0.0, 0.0, 0);
                            aldo.remove();
                            random = 1F + r.nextFloat() * 0.3F;
                            for(Player player : Bukkit.getOnlinePlayers()){
                                if (player.getName().equals("Kaelan_")) {
                                    player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_7, 1F, random);
                                } else {
                                    player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_6, 1F, random);
                                }
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("⏳ §f§lIT'S TEAM REVEALS TIME!"));
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
                            playMusicAll(Sound.MUSIC_DISC_STRAD);
                            bossBarBgTest();
                            PotionEffect PotionEffect = new PotionEffect(PotionEffectType.LEVITATION, 100, 1, false, false);
                            for(Player player : getPlayers()) {
                                player.addPotionEffect(PotionEffect);
                            }
                            teleportPlayers(TeleportConfig.get().getLocation("players.stage"), 5);
                            teleportSpectators(TeleportConfig.get().getLocation("spectators.stage") , 5);
                            break;
                        case 2361:
                            for(String pan : PanConfig.get().getConfigurationSection("pans").getKeys(false)){
                                plugin.runningTimers.get(pan).getKey().cancel();
                                plugin.runningTimers.remove(pan);
                            }
                            break;
                        case 2360:
                            PotionEffect PotionEffect2 = new PotionEffect(PotionEffectType.SLOW_FALLING, 200, 1, false, false);
                            for(Player player : getPlayers()) {
                                player.setGameMode(GameMode.ADVENTURE);
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
                                p.sendTitle("\uE023", "", 20, 60, 10);
                            }
                            break;
                        case 2160:
                            openDoors(doors.get(1), doors.getFirst());
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
                                    ItemStack air = new ItemStack(Material.AIR);
                                    p.getInventory().clear();
                                    p.getInventory().setHelmet(air);
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
                        case 2020:
                            closeDoors(doors.get(1), doors.getFirst());
                            break;
                        case 1960:
                            openDoors(doors.get(1), doors.getFirst());
                            teamList.setLength(0);
                            summonTeamFirework(Color.ORANGE);
                            for(String player : TeamsConfig.get().getStringList("teams.RubyRaiders.players")){
                                if(Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.setGameMode(GameMode.SPECTATOR);
                                    ItemStack pumpkin = new ItemStack(Material.CARVED_PUMPKIN);
                                    p.getInventory().clear();
                                    p.getInventory().setHelmet(pumpkin);
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
                                    ItemStack air = new ItemStack(Material.AIR);
                                    p.getInventory().clear();
                                    p.getInventory().setHelmet(air);
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
                        case 1820:
                            closeDoors(doors.get(1), doors.getFirst());
                            break;
                        case 1760:
                            openDoors(doors.get(1), doors.getFirst());
                            teamList.setLength(0);
                            summonTeamFirework(Color.YELLOW);
                            for(String player : TeamsConfig.get().getStringList("teams.AmberAmbushers.players")){
                                if(Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.setGameMode(GameMode.SPECTATOR);
                                    ItemStack pumpkin = new ItemStack(Material.CARVED_PUMPKIN);
                                    p.getInventory().clear();
                                    p.getInventory().setHelmet(pumpkin);
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
                                    ItemStack air = new ItemStack(Material.AIR);
                                    p.getInventory().clear();
                                    p.getInventory().setHelmet(air);
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
                        case 1620:
                            closeDoors(doors.get(1), doors.getFirst());
                            break;
                        case 1560:
                            openDoors(doors.get(1), doors.getFirst());
                            teamList.setLength(0);
                            summonTeamFirework(Color.LIME);
                            for(String player : TeamsConfig.get().getStringList("teams.TopazTroopers.players")){
                                if(Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.setGameMode(GameMode.SPECTATOR);
                                    ItemStack pumpkin = new ItemStack(Material.CARVED_PUMPKIN);
                                    p.getInventory().clear();
                                    p.getInventory().setHelmet(pumpkin);
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
                                    ItemStack air = new ItemStack(Material.AIR);
                                    p.getInventory().clear();
                                    p.getInventory().setHelmet(air);
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
                        case 1420:
                            closeDoors(doors.get(1), doors.getFirst());
                            break;
                        case 1360:
                            openDoors(doors.get(1), doors.getFirst());
                            teamList.setLength(0);
                            summonTeamFirework(Color.AQUA);
                            for(String player : TeamsConfig.get().getStringList("teams.KyaniteKillers.players")){
                                if(Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.setGameMode(GameMode.SPECTATOR);
                                    ItemStack pumpkin = new ItemStack(Material.CARVED_PUMPKIN);
                                    p.getInventory().clear();
                                    p.getInventory().setHelmet(pumpkin);
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
                                    ItemStack air = new ItemStack(Material.AIR);
                                    p.getInventory().clear();
                                    p.getInventory().setHelmet(air);
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
                        case 1220:
                            closeDoors(doors.get(1), doors.getFirst());
                            break;
                        case 1160:
                            openDoors(doors.get(1), doors.getFirst());
                            teamList.setLength(0);
                            summonTeamFirework(Color.BLUE);
                            for(String player : TeamsConfig.get().getStringList("teams.DiamondDestroyers.players")){
                                if(Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.setGameMode(GameMode.SPECTATOR);
                                    ItemStack pumpkin = new ItemStack(Material.CARVED_PUMPKIN);
                                    p.getInventory().clear();
                                    p.getInventory().setHelmet(pumpkin);
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
                                    ItemStack air = new ItemStack(Material.AIR);
                                    p.getInventory().clear();
                                    p.getInventory().setHelmet(air);
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
                        case 1020:
                            closeDoors(doors.get(1), doors.getFirst());
                            break;
                        case 960:
                            openDoors(doors.get(1), doors.getFirst());
                            teamList.setLength(0);
                            summonTeamFirework(Color.FUCHSIA);
                            for(String player : TeamsConfig.get().getStringList("teams.SapphireSoldiers.players")){
                                if(Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.setGameMode(GameMode.SPECTATOR);
                                    ItemStack pumpkin = new ItemStack(Material.CARVED_PUMPKIN);
                                    p.getInventory().clear();
                                    p.getInventory().setHelmet(pumpkin);
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
                                    ItemStack air = new ItemStack(Material.AIR);
                                    p.getInventory().clear();
                                    p.getInventory().setHelmet(air);
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
                        case 820:
                            closeDoors(doors.get(1), doors.getFirst());
                            break;
                        case 760:
                            openDoors(doors.get(1), doors.getFirst());
                            teamList.setLength(0);
                            summonTeamFirework(Color.WHITE);
                            for(String player : TeamsConfig.get().getStringList("teams.SmithsoniteSlayers.players")){
                                if(Bukkit.getPlayer(player) != null) {
                                    Player p = Bukkit.getPlayer(player);
                                    p.setGameMode(GameMode.SPECTATOR);
                                    ItemStack pumpkin = new ItemStack(Material.CARVED_PUMPKIN);
                                    p.getInventory().clear();
                                    p.getInventory().setHelmet(pumpkin);
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
                                    ItemStack air = new ItemStack(Material.AIR);
                                    p.getInventory().clear();
                                    p.getInventory().setHelmet(air);
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
                            for(ItemDisplay door : doors){
                                door.remove();
                            }
                            doors.clear();
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
                            plugin.shopAllowed = true;
                            for(Player p : Bukkit.getOnlinePlayers()){
                                ItemStack air = new ItemStack(Material.AIR);
                                p.getInventory().clear();
                                p.getInventory().setHelmet(air);
                            }
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
                                p.sendTitle("\uD83D\uDC9B", "§lsᴇᴀsᴏɴ ᴛʜʀᴇᴇ", 0, 100, 40);
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
                            currentMode = "Lobby";
                            bossBarBgTest();
                            plugin.emotesEnabled = false;
                            for(Player p : getPlayers()){
                                messagePlayer(p, "§c§l[!] §cEmotes are now disabled.");
                            }
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
        StringBuilder output2 = new StringBuilder();
        int width;
        int blocks;
        List<BossBar> bossBarList;
        for(Player player : Bukkit.getOnlinePlayers()) {
            bossBarList = new ArrayList<>();
            output.setLength(0);
            output2.setLength(0);
            if (bossBars.get(player.getName()) == null) {
                for(int i = 0; i < 2; i++) {
                    BossBar boss = Bukkit.createBossBar("", BarColor.GREEN, BarStyle.SOLID);
                    boss.addPlayer(player);
                    bossBarList.add(boss);
                }
                bossBars.put(player.getName(), bossBarList);
            }
            String text = "";
            switch (currentMode) {
                case "Start":
                    if(runningTimers.containsKey("eventstart")) {
                        output.append("\uD83E\uDD13").append("\uDAFF\uDFFF".repeat(307)).append(formatABC(" ", "§rᴇᴠᴇɴᴛ sᴛᴀʀᴛɪɴɢ ɪɴ " + plugin.runningTimers.get("eventstart").getValue() + " sᴇᴄᴏɴᴅs...", " ", 1));
                    } else {
                        output.append("\uD83E\uDD13").append("\uDAFF\uDFFF".repeat(307)).append(formatABC(" ", "§rᴛᴇᴀᴍ ʀᴇᴠᴇᴀʟs", " ", 1));
                    }
                    break;
                case "Voting":
                    if(plugin.runningTimers.containsKey("voting")){
                        text = getTimer("voting");
                    } else {
                        text = "00:00";
                    }
//                    width = FontUtils.getStringWidth("\uD83D\uDCB0" + PlaceholderAPI.setPlaceholders(player, "%mce24_points%"));
//                    blocks = Math.round((float) width / 6);
//                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
//                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");
//
//                    output.append("§e§l\uD83D\uDCB0").append(PlaceholderAPI.setPlaceholders(player, "%mce24_points%"));
//                    output.append(" ".repeat(5)).append("§r");
//
//                    width = FontUtils.getStringWidth("ᴛʜᴇ ᴠᴏᴛɪɴɢ ᴘᴀʟᴇᴛᴛᴇ");
//                    blocks = Math.round((float) width / 6);
//                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
//                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");
//
//                    output.append("§rᴛʜᴇ ᴠᴏᴛɪɴɢ ᴘᴀʟᴇᴛᴛᴇ");
//                    output.append(" ".repeat(6)).append("§r");
//
//                    width = FontUtils.getStringWidth("⏱ " + text);
//                    blocks = Math.round((float) width / 6);
//                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
//                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");
//
//                    output.append("§a§l⏱ §a").append(text);
                    output.append("\uD83E\uDD13").append("\uDAFF\uDFFF".repeat(307)).append(formatABC("§e§l\uD83D\uDCB0" + PlaceholderAPI.setPlaceholders(player, "%mce24_points%"), "§rᴛʜᴇ ᴠᴏᴛɪɴɢ ᴘᴀʟᴇᴛᴛᴇ", "§a⏱ " + text, 1));

                    break;
                case "Lobby":
                    if(plugin.runningTimers.containsKey("break")){
                        text = getTimer("break");
                    } else {
                        text = "00:00";
                    }
                    if(finaleActive) output2.append(getFinaleScoreboard());
//                    width = FontUtils.getStringWidth("\uD83D\uDCB0" + PlaceholderAPI.setPlaceholders(player, "%mce24_points%"));
//                    blocks = Math.round((float) width / 6);
//                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
//                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");
//
//                    output.append("§e§l\uD83D\uDCB0").append(PlaceholderAPI.setPlaceholders(player, "%mce24_points%"));
//                    output.append(" ".repeat(5)).append("§r");
//
//                    width = FontUtils.getStringWidth("sʜᴏᴡᴅᴏᴡɴ ᴘᴀʀᴋ");
//                    blocks = Math.round((float) width / 6);
//                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
//                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");
//
//                    output.append("§rsʜᴏᴡᴅᴏᴡɴ ᴘᴀʀᴋ");
//                    output.append(" ".repeat(6)).append("§r");
//
//                    width = FontUtils.getStringWidth("⏱: " + text);
//                    blocks = Math.round((float) width / 6);
//                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
//                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");
//
//                    output.append("§a§l⏱: ").append(text);
                    output.append("\uD83E\uDD13").append("\uDAFF\uDFFF".repeat(307)).append(formatABC("§e§l\uD83D\uDCB0" + PlaceholderAPI.setPlaceholders(player, "%mce24_points%"), "§rsʜᴏᴡᴅᴏᴡɴ ᴘᴀʀᴋ", "§a⏱ " + text, 1));
                    break;
                case "Slime Golf":
//                    width = FontUtils.getStringWidth("\uD83D\uDCB0" + PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"));
//                    blocks = Math.round((float) width / 6);
//                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
//                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");
//
//                    output.append("§e§l\uD83D\uDCB0").append(PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"));
//                    output.append(" ".repeat(5)).append("§r");
//
//                    width = FontUtils.getStringWidth("sʟɪᴍᴇ ɢᴏʟғ Slimey Slipway");
//                    blocks = Math.round((float) width / 6);
//                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
//                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");
//
//                    output.append("§rsʟɪᴍᴇ ɢᴏʟғ §7§oSlimey Slipway");
//                    output.append(" ".repeat(6)).append("§r");
//
//                    width = FontUtils.getStringWidth("⏱ " + PlaceholderAPI.setPlaceholders(player, "%mce24_timerfull_slimegolf%"));
//                    blocks = Math.round((float) width / 6);
//                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
//                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");
//
//                    output.append("§a§l⏱ §a").append(PlaceholderAPI.setPlaceholders(player, "%mce24_timerfull_slimegolf%"));
                    output.append("\uD83E\uDD13").append("\uDAFF\uDFFF".repeat(307)).append(formatABC("§e§l\uD83D\uDCB0" + PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"), "§rsʟɪᴍᴇ ɢᴏʟғ §7§oSlimey Slipway", "§a⏱ §a" + PlaceholderAPI.setPlaceholders(player, "%mce24_timerfull_slimegolf%"), 1));
                    if(finaleActive) output2.append(getFinaleScoreboard());
                    break;
                case "Crumble Clash":
//                    width = FontUtils.getStringWidth("\uD83D\uDCB0" + PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"));
//                    blocks = Math.round((float) width / 6);
//                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
//                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");
//
//                    output.append("§e§l\uD83D\uDCB0").append(PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"));
//                    output.append(" ".repeat(5)).append("§r");
//
//                    width = FontUtils.getStringWidth("ᴄʀᴜᴍʙʟᴇ ᴄʟᴀsʜ Crumble Colosseum");
//                    blocks = Math.round((float) width / 6);
//                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
//                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");
//
//                    output.append("§rᴄʀᴜᴍʙʟᴇ ᴄʟᴀsʜ §7§oCrumble Colosseum");
//                    output.append(" ".repeat(6)).append("§r");
//
//                    width = FontUtils.getStringWidth("⏱ " + PlaceholderAPI.setPlaceholders(player, "%mce24_timerfull_crumbleclash%"));
//                    blocks = Math.round((float) width / 6);
//                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
//                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("\uD83E\uDD13").append("\uDAFF\uDFFF".repeat(307)).append(formatABC("§e§l\uD83D\uDCB0" + PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"), "§rᴄʀᴜᴍʙʟᴇ ᴄʟᴀsʜ §7§oColosseum", "§a⏱ §a" + PlaceholderAPI.setPlaceholders(player, "%mce24_timerfull_crumbleclash%"), 1));
                    if(finaleActive) output2.append(getFinaleScoreboard());
                    break;
                case "Bridge Builders":
//                    width = FontUtils.getStringWidth("\uD83D\uDCB0" + PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"));
//                    blocks = Math.round((float) width / 6);
//                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
//                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");
//
//                    output.append("§e§l\uD83D\uDCB0").append(PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"));
//                    output.append(" ".repeat(5)).append("§r");
//
//                    width = FontUtils.getStringWidth("ʙʀɪᴅɢᴇ ʙᴜɪʟᴅᴇʀs Abandoned Mineshaft");
//                    blocks = Math.round((float) width / 6);
//                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
//                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");
//
//                    output.append("§rʙʀɪᴅɢᴇ ʙᴜɪʟᴅᴇʀs §7§oAbandoned Mineshaft");
//                    output.append(" ".repeat(6)).append("§r");
//
//                    width = FontUtils.getStringWidth("⏱ " + PlaceholderAPI.setPlaceholders(player, "%mce24_timerfull_bridgebuilders%"));
//                    blocks = Math.round((float) width / 6);
//                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
//                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");
//
//                    output.append("§a§l⏱ §a").append(PlaceholderAPI.setPlaceholders(player, "%mce24_timerfull_bridgebuilders%"));
                    output.append("\uD83E\uDD13").append("\uDAFF\uDFFF".repeat(307)).append(formatABC("§e§l\uD83D\uDCB0" + PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"), "§rʙʀɪᴅɢᴇ ʙᴜɪʟᴅᴇʀs §7§oMineshaft", "§a⏱ §a" + PlaceholderAPI.setPlaceholders(player, "%mce24_timerfull_bridgebuilders%"), 1));
                    if(finaleActive) output2.append(getFinaleScoreboard());
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

                    width = FontUtils.getStringWidth("⏱ " + PlaceholderAPI.setPlaceholders(player, "%mce24_timerfull_gubgame%"));
                    blocks = Math.round((float) width / 6);
                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("§a§l⏱ §a").append(PlaceholderAPI.setPlaceholders(player, "%mce24_timerfull_gubgame%"));
                    break;
                case "Craftalot":
//                    width = FontUtils.getStringWidth("\uD83D\uDCB0" + PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"));
//                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(width)).append("\uD83C\uDF59");
//                    output.append("\uDAFF\uDFFF".repeat(width)).append("\uDAFF\uDFFA");
//
//                    output.append("§e§l\uD83D\uDCB0").append(PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"));
//                    output.append(" ".repeat(5)).append("§r");
//
//                    width = FontUtils.getStringWidth("ᴄʀᴀғᴛᴀʟᴏᴛ Sir Craftalot's Castle");
//                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(width)).append("\uD83C\uDF59");
//                    output.append("\uDAFF\uDFFF".repeat(width)).append("\uDAFF\uDFFA");
//
//                    output.append("§rᴄʀᴀғᴛᴀʟᴏᴛ §7§oSir Craftalot's Castle");
//                    output.append(" ".repeat(6)).append("§r");
//
//                    width = FontUtils.getStringWidth("⏱ " + PlaceholderAPI.setPlaceholders(player, "%mce24_timerfull_craftalot%"));
//                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(width)).append("\uD83C\uDF59");
//                    output.append("\uDAFF\uDFFF".repeat(width));

                    output.append("\uD83E\uDD13").append("\uDAFF\uDFFF".repeat(307)).append(formatABC("§e§l\uD83D\uDCB0" + PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"), "§rᴄʀᴀғᴛᴀʟᴏᴛ §7§oSir Craftalot's Castle", "§a⏱ " + PlaceholderAPI.setPlaceholders(player, "%mce24_timerfull_craftalot%"), 1));
                    if(finaleActive) output2.append(getFinaleScoreboard());
//                    output.append("§a§l⏱ §a").append(PlaceholderAPI.setPlaceholders(player, "%mce24_timerfull_craftalot%"));
                    break;
                case "Zoomo Go":
//                    width = FontUtils.getStringWidth("\uD83D\uDCB0" + PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"));
//                    blocks = Math.round((float) width / 6);
//                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
//                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");
//
//                    output.append("§e§l\uD83D\uDCB0").append(PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"));
//                    output.append(" ".repeat(5)).append("§r");
//
//                    width = FontUtils.getStringWidth("ᴢᴏᴏᴍᴏ ɢᴏ Adrenaline Ravine");
//                    blocks = Math.round((float) width / 6);
//                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
//                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");
//
//                    output.append("§rᴢᴏᴏᴍᴏ ɢᴏ §7§oAdrenaline Ravine");
//                    output.append(" ".repeat(6)).append("§r");
//
//                    width = FontUtils.getStringWidth("⏱ " + PlaceholderAPI.setPlaceholders(player, "%mce24_timerfull_zoomogo%"));
//                    blocks = Math.round((float) width / 6);
//                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
//                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");
//
//                    output.append("§a§l⏱ §a").append(PlaceholderAPI.setPlaceholders(player, "%mce24_timerfull_zoomogo%"));

                    output.append("\uD83E\uDD13").append("\uDAFF\uDFFF".repeat(307)).append(formatABC("§e§l\uD83D\uDCB0" + PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"), "§rᴢᴏᴏᴍᴏ ɢᴏ §7§oAdrenaline Ravine", "§a⏱ §a" + PlaceholderAPI.setPlaceholders(player, "%mce24_timerfull_zoomogo%"), 1));
                    if(finaleActive) output2.append(getFinaleScoreboard());
                    break;
                case "Colour Dash":
//                    width = FontUtils.getStringWidth("\uD83D\uDCB0" + PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"));
//                    blocks = Math.round((float) width / 6);
//                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
//                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");
//
//                    output.append("§e§l\uD83D\uDCB0").append(PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"));
//                    output.append(" ".repeat(5)).append("§r");
//
//                    width = FontUtils.getStringWidth("ᴄᴏʟᴏᴜʀ ᴅᴀsʜᴀᴛʜᴏɴ The Journey");
//                    blocks = Math.round((float) width / 6);
//                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
//                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");
//
//                    output.append("§rᴄᴏʟᴏᴜʀ ᴅᴀsʜᴀᴛʜᴏɴ §7§oThe Journey");
//                    output.append(" ".repeat(6)).append("§r");
//
//                    width = FontUtils.getStringWidth("⏱ " + PlaceholderAPI.setPlaceholders(player, "%mce24_timerfull_colourdash%"));
//                    blocks = Math.round((float) width / 6);
//                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
//                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");
//
//                    output.append("§a§l⏱ §a").append(PlaceholderAPI.setPlaceholders(player, "%mce24_timerfull_colourdash%"));

                    output.append("\uD83E\uDD13").append("\uDAFF\uDFFF".repeat(307)).append(formatABC("§e§l\uD83D\uDCB0" + PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"), "§rᴄᴏʟᴏᴜʀ ᴅᴀsʜᴀᴛʜᴏɴ §7§oThe Journey", "§a⏱ §a" + PlaceholderAPI.setPlaceholders(player, "%mce24_timerfull_colourdash%"), 1));

                    break;
                case "Dimension Dash":
                    output.append("\uD83E\uDD13").append("\uDAFF\uDFFF".repeat(307)).append(formatABC("§e§l\uD83D\uDCB0" + PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"), "§rᴅɪᴍᴇɴsɪᴏɴ ᴅᴀsʜ §7§oEverywhere.", "§a⏱ §a" + PlaceholderAPI.setPlaceholders(player, "%mce24_timerfull_dimensiondash%"), 1));
                    if(finaleActive) output2.append(getFinaleScoreboard());
                    break;
                case "Push Point":
                    output.append("\uD83E\uDD13").append("\uDAFF\uDFFF".repeat(307)).append(formatABC("§e§l\uD83D\uDCB0" + PlaceholderAPI.setPlaceholders(player, "%mce24_teammodepoints%"), "§rᴘᴜsʜ ᴘᴏɪɴᴛ §7§oFailsafe Factory.", "§a⏱ §a" + PlaceholderAPI.setPlaceholders(player, "%mce24_timerfull_pushpoint%"), 1));
                    if(ppActive) {
                        if (PlayerConfig.get().getConfigurationSection("players").getKeys(false).contains(player.getName())) {
                            String team = PlayerConfig.get().getString("players." + player.getName() + ".team");
                            Map<String, Block[]> sortedMap = new TreeMap<>(plugin.mapSides);
                            List<ItemDisplay> keysWithMap = new ArrayList<>();
                            if (plugin.ppTeamMatchups.containsKey(team) || plugin.ppTeamMatchups.containsValue(team)) {
                                if (plugin.finaleActive) {

                                    Block[] mapBlocks = plugin.mapSides.get("map5finale");

                                    for (Map.Entry<ItemDisplay, String> entry2 : plugin.mapWalls.entrySet()) {
                                        if (("map5finale").equals(entry2.getValue())) {
                                            keysWithMap.add(entry2.getKey());
                                        }
                                    }

                                    keysWithMap.sort(Comparator.comparingDouble(item ->
                                            item.getLocation().getX()
                                    ));

                                    if (Objects.equals(plugin.teamConcrete.get(team), mapBlocks[1].getType())) {
                                        Collections.reverse(keysWithMap);
                                    }

                                    output2.append("\uD83E\uDEE1")
                                            .append("\uDAFF\uDFFF".repeat(255))
                                            .append(plugin.formatABC("", plugin.wallTexts.get(keysWithMap.get(0)).getText() + "   " + plugin.wallTexts.get(keysWithMap.get(1)).getText() + "   " + plugin.wallTexts.get(keysWithMap.get(2)).getText(), "", 2));

                                } else {
                                    int count2 = 0;

                                    for (Map.Entry<String, String> entry : plugin.ppTeamMatchups.entrySet()) {

                                        count2++;


                                        Block[] mapBlocks = sortedMap.get("map" + count2);

                                        if (!Objects.equals(plugin.teamConcrete.get(team), mapBlocks[0].getType()) && !Objects.equals(plugin.teamConcrete.get(team), mapBlocks[1].getType()))
                                            continue;

                                        for (Map.Entry<ItemDisplay, String> entry2 : plugin.mapWalls.entrySet()) {
                                            if (("map" + count2).equals(entry2.getValue())) {
                                                keysWithMap.add(entry2.getKey());
                                            }
                                        }

                                        keysWithMap.sort(Comparator.comparingDouble(item ->
                                                item.getLocation().getX()
                                        ));

                                        if (Objects.equals(plugin.teamConcrete.get(team), mapBlocks[1].getType())) {
                                            Collections.reverse(keysWithMap);
                                        }

                                        output2.append("\uD83E\uDEE1")
                                                .append("\uDAFF\uDFFF".repeat(255))
                                                .append(plugin.formatABC("", plugin.wallTexts.get(keysWithMap.get(0)).getText() + "   " + plugin.wallTexts.get(keysWithMap.get(1)).getText() + "   " + plugin.wallTexts.get(keysWithMap.get(2)).getText(), "", 2));

                                    }
                                }
                            }
                        }
                    }
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

                    width = FontUtils.getStringWidth("⏱ " + PlaceholderAPI.setPlaceholders(player, "%mce24_timerfull_survivalgames%"));
                    blocks = Math.round((float) width / 6);
                    output.append("\uD83D\uDE2D\uDAFF\uDFFF").append("\uD83E\uDD13\uDAFF\uDFFF".repeat(blocks)).append("\uD83C\uDF59");
                    output.append("\uDAFF\uDFFF".repeat((blocks * 6) - Math.round((float) ((blocks * 6) - width) / 2))).append("\uDAFF\uDFFA");

                    output.append("§a§l⏱ §a").append(PlaceholderAPI.setPlaceholders(player, "%mce24_timerfull_survivalgames%"));
                    break;
                default:
                    break;
            }
            bossBars.get(player.getName()).getFirst().setTitle(output.toString());
            bossBars.get(player.getName()).get(1).setTitle(output2.toString());
        }
    }

    public String centerTextInSlot(String text, int slotWidth) {
        int textWidth = FontUtils.getStringWidth(text);
        if (textWidth >= slotWidth) return text; // truncate if needed

        int remaining = slotWidth - textWidth;   // leftover space
        int leftPadding = remaining / 2;
        int rightPadding = remaining - leftPadding;

        return getExactSpace(leftPadding) + text + getExactSpace(rightPadding);
    }

    public String formatABC(String textA, String textB, String textC, int slot) {
        StringBuilder out = new StringBuilder();

        if(slot == 1) {
            // A (46px)
            out.append(centerTextInSlot(textA, SLOT_A));

            // gap (6px)
            out.append(getExactSpace(GAP));

            // B (145px)
            out.append(centerTextInSlot(textB, SLOT_B));

            // gap (6px)
            out.append(getExactSpace(GAP));

            // C (46px)
            out.append(centerTextInSlot(textC, SLOT_C));
        }
        if(slot == 2) {
            // A (46px)
            out.append(centerTextInSlot(textA, SLOT2_A));

            // gap (6px)
            out.append(getExactSpace(GAP2));

            // B (145px)
            out.append(centerTextInSlot(textB, SLOT2_B));

            // gap (6px)
            out.append(getExactSpace(GAP2));

            // C (46px)
            out.append(centerTextInSlot(textC, SLOT2_C));
        }

        return out.toString();
    }



    public String getExactSpace(double width) {
        // Use floor for positive, ceil for negative to always stay smaller
        int intWidth = width > 0 ? (int) Math.floor(width) : (int) Math.ceil(width);

        // Integer width space
        if (intWidth >= -8192 && intWidth <= 8192) {
            int charCode = 0xD0000 + intWidth;
            return new String(Character.toChars(charCode));
        }

        // Fractional widths (-1.0 to 1.0)
        if (width >= -1.0 && width <= 1.0) {
            int fracCode = 0x50000 + (int) Math.floor(width * 4800); // use floor to stay smaller
            return new String(Character.toChars(fracCode));
        }

        // Fallback: split into multiple integer-width spaces
        StringBuilder builder = new StringBuilder();
        int remaining = intWidth;
        while (remaining != 0) {
            int part = Math.max(-8192, Math.min(8192, remaining));
            builder.append(getExactSpace(part));
            remaining -= part;
        }
        return builder.toString();
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

    public List<Material> getBridgeBlocksFinale(int level, String team) {

        List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());

        int xMultiplier = 35;

        if(Objects.equals(team, leaderteams.getFirst())){
            xMultiplier *= 0;
        }

        List<Material> bridgeBlocks = new ArrayList<>();

        for (int i = 2060 + xMultiplier; i <= 2066 + xMultiplier; i++) {
            for (int j = 916 - (level * 38); j >= 902 - (level * 38); j--) {
                for(int k = -37; k <= -28; k++) {
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

        List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());

        int teamIndex = 35;

        if (finaleActive) {
            if(Objects.equals(team, leaderteams.getFirst())){
                teamIndex *= 0;
            }
            return block.getX() >= 2040 + teamIndex && block.getX() <= 2046 + teamIndex &&
                    block.getY() >= -37 && block.getY() <= -28 &&
                    block.getZ() >= 902 - (level * 38) && block.getZ() <= 916 - (level * 38);
        } else {
            switch (team) {
                case "RubyRaiders":
                    teamIndex *= 0;
                    break;
                case "AmberAmbushers":
                    break;
                case "TopazTroopers":
                    teamIndex *= 2;
                    break;
                case "KyaniteKillers":
                    teamIndex *= 3;
                    break;
                case "DiamondDestroyers":
                    teamIndex *= 4;
                    break;
                case "SapphireSoldiers":
                    teamIndex *= 5;
                    break;
                case "SmithsoniteSlayers":
                    teamIndex *= 6;
                    break;
                case "CrystalCrashers":
                    teamIndex *= 7;
                    break;
            }


            return block.getX() >= 234 + teamIndex && block.getX() <= 240 + teamIndex &&
                    block.getY() >= -21 && block.getY() <= -12 &&
                    block.getZ() >= 663 - (level * 38) && block.getZ() <= 677 - (level * 38);
        }
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

                            for(String teamPlayer : TeamsConfig.get().getStringList("teams." + team + ".players")){
                                for(String teamPlayer2 : TeamsConfig.get().getStringList("teams." + team + ".players")) {
                                    if (teamPlayer.equals(teamPlayer2)) continue;
                                    if (Bukkit.getServer().getPlayer(teamPlayer) != null && Bukkit.getServer().getPlayer(teamPlayer2) != null) {
                                        Player p = Bukkit.getServer().getPlayer(teamPlayer);
                                        Player p2 = Bukkit.getServer().getPlayer(teamPlayer2);
                                        p.showPlayer(p2);
                                        p2.showPlayer(p);
                                    }
                                }
                            }

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
                    for(String teamPlayer : TeamsConfig.get().getStringList("teams." + team + ".players")){
                        for(String teamPlayer2 : TeamsConfig.get().getStringList("teams." + team + ".players")) {
                            if (teamPlayer.equals(teamPlayer2)) continue;
                            if (Bukkit.getServer().getPlayer(teamPlayer) != null && Bukkit.getServer().getPlayer(teamPlayer2) != null) {
                                Player p = Bukkit.getServer().getPlayer(teamPlayer);
                                Player p2 = Bukkit.getServer().getPlayer(teamPlayer2);
                                p.showPlayer(p2);
                                p2.showPlayer(p);
                            }
                        }
                    }
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
                            for(SulfurCube slime : slimeGolfSlime) {
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
            if(!world.getBlockAt(x,y,i).getBlockData().getMaterial().equals(colour.getMaterial())) {
                tempBD = (BlockDisplay) world.spawnEntity(new Location(world, x, y, i), EntityType.BLOCK_DISPLAY);
                BlockData sourceData = colour;
                tempBD.setBlock(sourceData);
                blockList.add(tempBD);
            }
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
                if (plugin.runningTimers.containsKey(name)) {
                    if (!plugin.pausedTimers.contains(name)) {
                        timeLeft--;
                        plugin.runningTimers.get(name).setValue(timeLeft);

                        if (blockList.isEmpty()) { cancel(); return; }

                        // Read the current transformation
                        BlockDisplay first = blockList.get(0);
                        Transformation old = first.getTransformation();

                        // Calculate new scale & translation
                        Vector3f newScale = new Vector3f(
                                old.getScale().x() + 0.05F,
                                old.getScale().y() + 0.05F,
                                old.getScale().z() + 0.05F
                        );

                        Vector3f newTranslation = new Vector3f(
                                old.getTranslation().x() - 0.025F,
                                old.getTranslation().y() - 0.025F,
                                old.getTranslation().z() - 0.025F
                        );

                        // Construct a fresh transformation
                        Transformation newTransformation = new Transformation(
                                newTranslation,
                                old.getLeftRotation(),   // keep existing rotation
                                newScale,
                                old.getRightRotation()   // keep existing rotation
                        );

                        if (timeLeft == 1) {
                            for (int i = z; i <= z + 43; i++) {
                                world.getBlockAt(x, y, i).setType(colour.getMaterial());
                            }
                        } else if (timeLeft == 0) {
                            for (BlockDisplay bd : blockList) {
                                bd.remove();
                            }
                            plugin.runningTimers.remove(name);
                            cancel();
                        } else {
                            for (BlockDisplay bd : blockList) {
                                bd.setTransformation(newTransformation);
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

    public void removeVotingPiece(int index, BlockData colour) {

        World world = Bukkit.getWorld("build");

        int x = 187 + index;
        int y = 139;
        int z = 713;

        List<BlockDisplay> blockList = new ArrayList<>();
        BlockDisplay tempBD;

        for(int i = z; i <= z + 43; i++){
            if(!world.getBlockAt(x,y,i).getBlockData().getMaterial().equals(colour.getMaterial())) {
                tempBD = (BlockDisplay) world.spawnEntity(new Location(world, x, y, i), EntityType.BLOCK_DISPLAY);
                BlockData sourceData = world.getBlockAt(x, y, i).getBlockData();
                tempBD.setBlock(sourceData);
                blockList.add(tempBD);
            }
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
                if(!world.getBlockAt(x,y,i).getBlockData().getMaterial().equals(colour.getMaterial())) {
                    world.getBlockAt(x, y, i).setType(Material.BARRIER);
                }
            }
        }, 1L);

        String name = "buildremove" + index;

        BukkitTask task = new BukkitRunnable() {
            int timeLeft = 20;

            @Override
            public void run() {
                if (plugin.runningTimers.containsKey(name)) {
                    if (!plugin.pausedTimers.contains(name)) {
                        timeLeft--;
                        plugin.runningTimers.get(name).setValue(timeLeft);

                        if (blockList.isEmpty()) { cancel(); return; }

                        // Read the current transformation from the first BlockDisplay
                        BlockDisplay first = blockList.get(0);
                        Transformation old = first.getTransformation();

                        // Calculate new scale & translation
                        Vector3f newScale = new Vector3f(
                                old.getScale().x() - 0.05F,
                                old.getScale().y() - 0.05F,
                                old.getScale().z() - 0.05F
                        );

                        Vector3f newTranslation = new Vector3f(
                                old.getTranslation().x() + 0.025F,
                                old.getTranslation().y() + 0.025F,
                                old.getTranslation().z() + 0.025F
                        );

                        // Construct a fresh Transformation
                        Transformation newTransformation = new Transformation(
                                newTranslation,
                                old.getLeftRotation(),   // keep existing rotation
                                newScale,
                                old.getRightRotation()   // keep existing rotation
                        );

                        // Apply new transformation to all BlockDisplays
                        for (BlockDisplay bd : blockList) {
                            bd.setTransformation(newTransformation);
                        }

                        // Remove BlockDisplays when timer reaches 0
                        if (timeLeft == 0) {
                            for (BlockDisplay bd : blockList) {
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


    public void killTextDisplaysInArea(Location corner1, Location corner2) {
        World world = corner1.getWorld();
        if (world == null) return;

        double minX = Math.min(corner1.getX(), corner2.getX());
        double minY = Math.min(corner1.getY(), corner2.getY());
        double minZ = Math.min(corner1.getZ(), corner2.getZ());
        double maxX = Math.max(corner1.getX(), corner2.getX());
        double maxY = Math.max(corner1.getY(), corner2.getY());
        double maxZ = Math.max(corner1.getZ(), corner2.getZ());

        int minChunkX = (int) Math.floor(minX) >> 4;
        int minChunkZ = (int) Math.floor(minZ) >> 4;
        int maxChunkX = (int) Math.floor(maxX) >> 4;
        int maxChunkZ = (int) Math.floor(maxZ) >> 4;

        for (int cx = minChunkX; cx <= maxChunkX; cx++) {
            for (int cz = minChunkZ; cz <= maxChunkZ; cz++) {
                world.getChunkAt(cx, cz);
            }
        }

        world.getNearbyEntities(new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ)).stream()
                .filter(entity -> entity instanceof TextDisplay)
                .forEach(Entity::remove);
    }


    public void startFinale(){
        pvpArenaManager.disablePvPArena();
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
        modeIndexes.put("Dimension Dash", 8);
        modeIndexes.put("Crumble Clash", 9);
        modeIndexes.put("Push Point", 10);

        musicManager.stopMusicAll();

        World world = Bukkit.getWorld("build");

        killTextDisplaysInArea(new Location(world, 1869, 173, 926), new Location(world, 1803, 158, 874));

        for(int x = 1824; x <= 1852; x++){
            for(int y = 151; y <= 156; y++){
                for (int z = 891; z <= 899; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    Material type = block.getType();

                    if (woolColours().contains(type)) {
                        block.setType(Material.WHITE_CONCRETE);
                    } else if (woolBlocks().contains(type)) {
                        block.setType(Material.WHITE_WOOL);
                    }
                }
            }
        }

        gameOrderTab.clear();

        deadPlayers.clear();
        deadTeams.clear();

        TextDisplay[] displays = new TextDisplay[modeTitles.length];

        currentRound = 1;

        finaleFirstTeamRevealed = false;
        finaleSecondTeamRevealed = false;

        finaleRound = 1;
        plugin.finaleActive = true;
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
            int timeLeft = 121;
            @Override
            public void run() {
                if(plugin.runningTimers.containsKey(name)) {
                    if (!plugin.pausedTimers.contains(name)) {
                        timeLeft--;
                        plugin.runningTimers.get(name).setValue(timeLeft);

                        switch(timeLeft){
                            case 120:
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
                            case 110:
                                plugin.emotesEnabled = true;
                                for(Player p : getPlayers()){
                                    messagePlayer(p, """
                                        §8
                                        §8
                                        §e§l[!] §6Emotes are now enabled. Use §a/emote §6to emote!
                                        §fEmotes will be available between games.
                                        §8
                                        §8
                                        """);
                                }
                                break;
                            case 107:
                                playSoundAll(Sound.BLOCK_END_PORTAL_SPAWN, 1F);
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle("\uD83D\uDC7A", "", 20, 60, 20);
                                }
                                break;
                            case 100:
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle("§f§lɪɴᴛʀᴏᴅᴜᴄɪɴɢ..", "§f§lᴛʜᴇ ғɪɴᴀʟɪsᴛs!", 0, 60, 20);
                                }
                                break;
                            case 95, 94, 93, 92:
                                finaleFirstTeamRevealed = true;
                                if(index1 < firstPlayers.size()){
                                    Player p = Bukkit.getPlayer(firstPlayers.get(index1));
                                    if(p != null){
                                        p.teleport(new Location(world, 1832 + index1, 157, 895, 180, -15));
                                    }
                                }

                                for(int x = 1824; x <= 1837; x++){
                                    for(int y = 151; y <= 153 + (index1 * 2); y++){
                                        for(int z = 891; z <= 899; z++){

                                            if(world.getBlockAt(x,y,z).getType() == Material.WHITE_CONCRETE){
                                                world.getBlockAt(x,y,z).setType(teamConcrete.get(firstTeam));
                                            }

                                            if(world.getBlockAt(x,y,z).getType() == Material.WHITE_WOOL){
                                                world.getBlockAt(x,y,z).setType(teamWool.get(firstTeam));
                                            }
                                        }
                                    }
                                }

                                index1++;

                                if(index1 > firstPlayers.size()) break;

                                if(index1 == firstPlayers.size()){

                                    String playerList = String.join(", ", firstPlayers);

                                    for(Player p : Bukkit.getOnlinePlayers()){
                                        messagePlayer(p, """
                §8
                §8
                §r""" + getTeamDisplayName(firstTeam) + """
                §8
                §f""" + playerList + """
                §8
                """);
                                    }
                                }
                                break;
                            case 88:
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    messagePlayer(p, """
                                        §8
                                        §8
                                        §f§l  Against..
                                        §8
                                        """);
                                }
                                break;
                            case 80, 79, 78, 77:
                                finaleSecondTeamRevealed = true;
                                if(index2 < secondPlayers.size()){
                                    Player p = Bukkit.getPlayer(secondPlayers.get(index2));
                                    if(p != null){
                                        p.teleport(new Location(world, 1842 + index2, 157, 895, 180, -15));
                                    }
                                }

                                for(int x = 1839; x <= 1852; x++){
                                    for(int y = 151; y <= 153 + (index2 * 2); y++){
                                        for(int z = 891; z <= 899; z++){

                                            if(world.getBlockAt(x,y,z).getType() == Material.WHITE_CONCRETE){
                                                world.getBlockAt(x,y,z).setType(teamConcrete.get(secondTeam));
                                            }

                                            if(world.getBlockAt(x,y,z).getType() == Material.WHITE_WOOL){
                                                world.getBlockAt(x,y,z).setType(teamWool.get(secondTeam));
                                            }
                                        }
                                    }
                                }

                                index2++;

                                if(index2 > secondPlayers.size()) break;

                                if(index2 == secondPlayers.size()){

                                    String playerList = String.join(", ", secondPlayers);

                                    for(Player p : Bukkit.getOnlinePlayers()){
                                        messagePlayer(p, """
                §8
                §8
                §r""" + getTeamDisplayName(secondTeam) + """
                §8
                §f""" + playerList + """
                §8
                """);
                                    }
                                }
                                break;
                            case 76:
                                List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());
                                List<Integer> leaderteampoints = new ArrayList<>(plugin.sortByValue().values());
                                for(Player p : Bukkit.getOnlinePlayers()) {
                                    plugin.messagePlayer(p, "§6§lᴛᴇᴀᴍ ʟᴇᴀᴅᴇʀʙᴏᴀʀᴅ");
                                    int index = 1;
                                    for (String name : leaderteams) {
                                        plugin.messagePlayer(p, index + ". " + plugin.getTeamDisplayName(name) + " §7| §e§l💰" + leaderteampoints.get(index - 1));
                                        index++;
                                    }
                                }
                                break;
                            case 74:
                                for(Player p : Bukkit.getOnlinePlayers()){
                                    p.sendTitle("", getTeamDisplayName(firstTeam) + " §evs " + getTeamDisplayName(secondTeam), 0, 60, 20);
                                }
                                if (gameOrder.getLast().equals("Push Point")) {
                                    getCurrentPPStandings();
                                }
                                break;
                            case 72:
                                getTeamModeFullPoints();
                                break;
                            case 71:
                                for(int i = 0; i <= 7; i++){
                                    plugin.teamShown[i] = true;
                                }
                                for(Player p : Bukkit.getOnlinePlayers()) {
                                    p.sendMessage("§e[!] Leaderboard commands are now enabled! (/leaderboard, /indiv, /modeindiv)");
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
                                gameOrderTab.add(game);
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
                                    if(gameIndex == 7){
                                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("⏳ §f§l.. and " + game + "!"));
                                    } else {
                                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy("⏳ §f§l" + game + ".."));
                                    }
                                }

                                displays[gameIndex].setTransformation(transform);

                                gameIndex++;
                                break;

                            case 40:
                                playSoundAll(Sound.ENTITY_ARMADILLO_LAND, 1F);
                                for(Player p : Bukkit.getOnlinePlayers()) {
                                    switch (gameOrder.getFirst()) {
                                        case "Slime Golf":
                                            p.sendTitle("\uE172", "", 20, 60, 20);
                                            break;
                                        case "Dimension Dash":
                                            p.sendTitle("\uD83E\uDD68", "", 20, 60, 20);
                                            break;
                                        case "Craftalot":
                                            p.sendTitle("\ue238", "", 20, 60, 20);
                                            break;
                                        case "Bridge Builders":
                                            p.sendTitle("\uD83C\uDF45", "", 20, 60, 20);
                                            break;
                                        case "Zoomo Go":
                                            p.sendTitle("\uD83E\uDD55", "", 20, 60, 20);
                                            break;
                                        case "Crumble Clash":
                                            p.sendTitle("\uD83E\uDD6C", "", 20, 60, 20);
                                            break;
                                        case "Push Point":
                                            p.sendTitle("\uD83E\uDED0", "", 20, 60, 20);
                                            break;
                                    }
                                }
                                break;
                            case 30:
                                String firstGame = gameOrder.getFirst();

                                for(Player player : getPlayers()){
                                    if(!firstPlayers.contains(player.getName()) && !secondPlayers.contains(player.getName())) {
                                        ghostManager.addGhostPlayer(player.getName());
                                    }
                                }

                                switch(firstGame){
                                    case "Slime Golf":
                                        startSlimeGolfFinale();
                                        break;
//                                    case "Survival Games":
//
//                                        break;
//                                    case "Gub Game":
//
//                                        break;
//                                    case "Colour Dash":
//                                        startColourDashFinale();
//                                        break;
                                    case "Dimension Dash":
                                        startDimensionDashFinale();
                                        break;
                                    case "Craftalot":
                                        startCraftalotFinale();
                                        break;
                                    case "Bridge Builders":
                                        startBridgeBuildersFinale();
                                        break;
                                    case "Zoomo Go":
                                        startZoomoGoFinale();
                                        break;
                                    case "Crumble Clash":
                                        startCrumbleClashFinale();
                                        break;
                                    case "Push Point":
                                        startPushPointFinale();
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

    public void startSlimeFinaleCheck() {
        World world = Bukkit.getWorld("build");

        Location corner1 = new Location(world, 2685, 48, 441);
        Location corner2 = new Location(world, 2671, 49, 445);

        BoundingBox holeBox = BoundingBox.of(corner1, corner2);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Iterator<Map.Entry<SulfurCube, String>> it = finaleSlimes.entrySet().iterator();
                     it.hasNext();) {

                    Map.Entry<SulfurCube, String> entry = it.next();

                    SulfurCube slime = entry.getKey();

                    if (!slime.isValid()) {
                        it.remove();
                        continue;
                    }

                    if (holeBox.overlaps(slime.getBoundingBox())) {

                        String winningTeam = entry.getValue();

                        for (Map.Entry<SulfurCube, String> slimeEntry : finaleSlimes.entrySet()) {
                            SulfurCube slime2 = slimeEntry.getKey();
                            String team = slimeEntry.getValue();

                            if (team.equals(winningTeam)) {
                                slime2.remove();
                            } else {
                                slime2.setHealth(0.0);
                            }
                        }

                        finaleSlimes.clear();

                        finaleRoundOver(winningTeam);
                        plugin.runningTimers.remove("slimegolffinale");

                        cancel();
                        return;
                    }
                }
            }
        }.runTaskTimer(this, 0L, 2L);
    }

    public void finalePushPointOver(){
        Material leftBlock;
        Material rightBlock;
        World world = Bukkit.getWorld("build");

        Map<Material, String> concreteToTeam = new HashMap<>();

        for (Map.Entry<String, Material> entry : teamConcrete.entrySet()) {
            concreteToTeam.put(entry.getValue(), entry.getKey());
        }


        leftBlock = world.getBlockAt(1500, -61, -403).getType();
        rightBlock = world.getBlockAt(1500, -61, -515).getType();

        final String leftTeam = concreteToTeam.get(leftBlock);
        final String rightTeam = concreteToTeam.get(rightBlock);

        final List<Double> leftTeamScores = new ArrayList<>();

        for (ItemDisplay wall : mapWalls.keySet()) {
            if (mapWalls.get(wall).equals("map5finale")) {
                String rawText = wallTexts.get(wall).getText().replaceAll("§.", "");
                String[] parts = rawText.split("\\|");
                leftTeamScores.add(Double.parseDouble(parts[0].replace("%", "").trim()));
            }
        }

        final List<String> leftPlayers = TeamsConfig.get().getStringList("teams." + leftTeam + ".players");
        final List<String> rightPlayers = TeamsConfig.get().getStringList("teams." + rightTeam + ".players");

        final List<String> mapPlayers = new ArrayList<>();
        mapPlayers.addAll(leftPlayers);
        mapPlayers.addAll(rightPlayers);

        double total = 0;
        for (double d : leftTeamScores) {
            total += d;
        }

        double finalPercentA = total / leftTeamScores.size();

        int rawPointsA = (int) Math.round((finalPercentA / 100.0) * 440.0);

        // Round to nearest multiple of 4
        final int pointsA = Math.round(rawPointsA / 4.0f) * 4;
        final int pointsB = 440 - pointsA;

        BukkitTask task = new BukkitRunnable() {

            int timeLeft = 201;
            double t;
            double eased;

            double startPercentA = 50.0;
            int nextIterationStart = 180;
            int index = 1;

            double sum = leftTeamScores.getFirst();
            double endPercentA = sum;

            @Override
            public void run() {

                if (!runningTimers.containsKey("percentages" + leftTeam)) {
                    cancel();
                    return;
                }

                if (pausedTimers.contains("percentages" + leftTeam)) {
                    return;
                }

                timeLeft--;
                runningTimers.get("percentages" + leftTeam).setValue(timeLeft);

                // Animation window
                if (timeLeft <= nextIterationStart && timeLeft > nextIterationStart - 20) {

                    t = (nextIterationStart - timeLeft) / 20.0;
                    t = Math.min(1.0, Math.max(0.0, t));
                    eased = 1 - Math.pow(1 - t, 2);

                    double currentA = startPercentA + eased * (endPercentA - startPercentA);
                    int barsA = (int) Math.round((currentA / 100.0) * 20);
                    int barsB = 20 - barsA;

                    String bar = buildBar(barsA, barsB, leftTeam, rightTeam);

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        if (p != null) {
                            p.sendTitle(
                                    teamGlowColors.get(leftTeam) + String.format("%.2f", currentA) + "%" +
                                            ChatColor.WHITE + " | " +
                                            teamGlowColors.get(rightTeam) + String.format("%.2f", 100 - currentA) + "%",
                                    bar,
                                    0, 60, 20
                            );
                        }
                    }
                }

                switch(timeLeft){

                    case 200:
                        String bar = buildBar(10, 10, leftTeam, rightTeam);
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (p != null) {
                                p.sendTitle(
                                        teamGlowColors.get(leftTeam) + "50.00%" +
                                                ChatColor.WHITE + " | " +
                                                teamGlowColors.get(rightTeam) + "50.00%",
                                        bar,
                                        0, 60, 20
                                );
                            }
                        }
                        break;

                    case 150, 110:
                        startPercentA = endPercentA;

                        if (index < leftTeamScores.size()) {
                            sum += leftTeamScores.get(index);
                            endPercentA = sum / (index + 1);
                            index++;
                        }

                        nextIterationStart -= 40;
                        break;
                }

                if (timeLeft <= 60) {


                    if(finalPercentA > 50){
                        finaleRoundOver(leftTeam);
                    } else {
                        finaleRoundOver(rightTeam);
                    }
                    runningTimers.remove("percentages" + leftTeam);
                    cancel();
                }
            }

        }.runTaskTimer(this, 0L, 1L);

        runningTimers.put("percentages" + leftTeam, new AbstractMap.SimpleEntry<>(task, 201));
    }

    public String getFinaleScoreboard() {

        List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());
        String firstTeam = leaderteams.getFirst();
        String secondTeam = leaderteams.get(1);

        int firstScore = finaleScores.getOrDefault(firstTeam, 0);
        int secondScore = finaleScores.getOrDefault(secondTeam, 0);

        ChatColor firstColor = teamGlowColors.get(firstTeam);
        ChatColor secondColor = teamGlowColors.get(secondTeam);

        StringBuilder board = new StringBuilder();

        board.append(firstScore >= 1 ? firstColor + "🫓 " : ChatColor.GRAY + "🫓 ");
        board.append(firstScore >= 2 ? firstColor + "🥗 " : ChatColor.GRAY + "🥗 ");
        board.append(firstScore >= 3 ? firstColor + "🥗 " : ChatColor.GRAY + "🥗 ");

        if (firstScore >= 4) {
            board.append(firstColor).append("🍳 ");
        } else if (secondScore >= 4) {
            board.append(secondColor).append("🍳 ");
        } else {
            board.append(ChatColor.GRAY).append("🍳 ");
        }

        board.append(secondScore >= 3 ? secondColor + "🥗 " : ChatColor.GRAY + "🥗 ");
        board.append(secondScore >= 2 ? secondColor + "🥗 " : ChatColor.GRAY + "🥗 ");
        board.append(secondScore >= 1 ? secondColor + "🫓" : ChatColor.GRAY + "🫓");

        return board.toString();
    }

    public void finaleRoundOver(String winningTeam){

        if(currentMode.equals("Craftalot")){
            craftFinaleTimer.cancel();
        }

        ppActive = false;

        for (Player p2 : Bukkit.getOnlinePlayers()) {
            Objects.requireNonNull(p2.getAttribute(Attribute.MOVEMENT_SPEED)).setBaseValue(0.1);
            for (Player p3 : Bukkit.getOnlinePlayers()) {
                p3.showPlayer(plugin, p2);
                p2.showPlayer(plugin, p3);
            }
        }

        if(currentMode.equals("Dimension Dash")) {
            ddTimer.cancel();
        }

        if(currentMode.equals("Crumble Clash")){
            Iterator<ItemBox> iterator = plugin.itemBoxes.iterator();

            while (iterator.hasNext()) {
                ItemBox ib = iterator.next();
                ib.despawn();
                iterator.remove();
            }
        }

        ccRoundStarted = false;
        runningTimers.clear();
        deadPlayers.clear();
        deadTeams.clear();
        for(Player p : Bukkit.getOnlinePlayers()){
            p.stopAllSounds();
            p.getInventory().clear();
        }
        playSoundAll(Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1F);
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
        if (currentMode.equals("Slime Golf")) {
            Iterator<SulfurCube> iterator = slimeGolfSlime.iterator();
            while (iterator.hasNext()) {
                SulfurCube slime = iterator.next();
                slime.remove();
                iterator.remove();
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
            bossBars.get(player.getName()).forEach(bar -> bar.removePlayer(player));
            bossBars.put(player.getName(), null);
            healFeedPlayer(player);

        }
        if(finaleScores.get(winningTeam) == 4){
            finaleActive = false;
            for(Player player : getPlayers()){
                ghostManager.removeGhostPlayer(player.getName());
                player.setGameMode(GameMode.ADVENTURE);
            }
            endEventFully(winningTeam, losingTeam);
            eventOver = true;
        } else {
            String name = "finaleroundover";
            BukkitTask task = new BukkitRunnable() {
                List<String> leaderteams = new ArrayList<>(plugin.sortByValue().keySet());
                String firstTeam = leaderteams.getFirst();
                String secondTeam = leaderteams.get(1);
                List<String> firstPlayers = TeamsConfig.get().getStringList("teams." + firstTeam + ".players");
                List<String> secondPlayers = TeamsConfig.get().getStringList("teams." + secondTeam + ".players");


                World world = Bukkit.getWorld("build");
                int timeLeft = 56;

                @Override
                public void run() {
                    if (plugin.runningTimers.containsKey(name)) {
                        if (!plugin.pausedTimers.contains(name)) {
                            timeLeft--;
                            plugin.runningTimers.get(name).setValue(timeLeft);
                            bossBarBgTest();

                            switch (timeLeft) {
                                case 50:

                                    // Push Point remove walls.
                                    if(currentMode.equals("Push Point")){
                                        for(TextDisplay text : new ArrayList<>(wallTexts.values())){
                                            text.remove();
                                        }
                                        for(TextDisplay text : new ArrayList<>(wallPushersTexts.values())){
                                            text.remove();
                                        }
                                        for(ItemDisplay wall : new ArrayList<>(mapWalls.keySet())){
                                            wall.remove();
                                        }

                                        mapWalls.clear();
                                        wallTexts.clear();
                                        wallPushersTexts.clear();
                                        finalPushMovements.clear();
                                    }

                                    PotionEffect lev = new PotionEffect(PotionEffectType.LEVITATION, 100, 1);
                                    PotionEffect slowfall = new PotionEffect(PotionEffectType.SLOW_FALLING, 100, 1);
                                    for (Player p : getPlayers()) {
                                        p.addPotionEffect(lev);
                                        p.addPotionEffect(slowfall);
                                    }
                                    teleportPlayers(TeleportConfig.get().getLocation("players.finalepodium"), 5);
                                    teleportSpectators(TeleportConfig.get().getLocation("spectators.finalepodium"), 5);
                                    break;
                                case 45:
                                    for (Player p2 : Bukkit.getOnlinePlayers()) {
                                        for (Player p3 : Bukkit.getOnlinePlayers()) {
                                            p3.showPlayer(plugin, p2);
                                            p2.showPlayer(plugin, p3);
                                        }
                                    }
                                    for (String player : new ArrayList<>(ghostManager.getGhostPlayers())) {
                                        ghostManager.removeGhostPlayer(player);
                                    }
                                    for (String player : secondPlayers) {
                                        if (Bukkit.getPlayer(player) != null) {
                                            Player p = Bukkit.getPlayer(player);
                                            p.teleport(new Location(world, 1834, 157, 895, 180, -15));
                                        }
                                    }
                                    for (String player : firstPlayers) {
                                        if (Bukkit.getPlayer(player) != null) {
                                            Player p = Bukkit.getPlayer(player);
                                            p.teleport(new Location(world, 1844, 157, 895, 180, -15));
                                        }
                                    }
                                    for (Player p : getPlayers()) {
                                        p.setAllowFlight(false);
                                        p.setFlying(false);
                                        p.setGameMode(GameMode.ADVENTURE);
                                    }
                                    currentMode = "Lobby";
                                    break;
                                case 40:
                                    if(finaleScores.get(winningTeam) == 3 && finaleScores.get(losingTeam) == 3){
                                        for(Player p : Bukkit.getOnlinePlayers()){
                                            p.sendTitle("\uD83E\uDD6F", "", 0, 40, 20);
                                        }
                                        playSoundAll(Sound.ENTITY_ARMADILLO_LAND, 0.8F);
                                    } else if (finaleScores.containsValue(3)){
                                        for(Player p : Bukkit.getOnlinePlayers()){
                                            p.sendTitle("\uD83E\uDD50", "", 0, 40, 20);
                                            playSoundAll(Sound.ENTITY_ARMADILLO_LAND, 0.2F);
                                        }
                                    }
                                    break;
                                case 35:
                                    playSoundAll(Sound.ENTITY_ARMADILLO_LAND, 1F);
                                    for(Player p : Bukkit.getOnlinePlayers()) {
                                        switch (gameOrder.get(finaleRound)) {
                                            case "Slime Golf":
                                                p.sendTitle("\uE172", "", 20, 60, 20);
                                                break;
                                            case "Dimension Dash":
                                                p.sendTitle("\uD83E\uDD68", "", 20, 60, 20);
                                                break;
                                            case "Craftalot":
                                                p.sendTitle("\ue238", "", 20, 60, 20);
                                                break;
                                            case "Bridge Builders":
                                                p.sendTitle("\uD83C\uDF45", "", 20, 60, 20);
                                                break;
                                            case "Zoomo Go":
                                                p.sendTitle("\uD83E\uDD55", "", 20, 60, 20);
                                                break;
                                            case "Crumble Clash":
                                                p.sendTitle("\uD83E\uDD6C", "", 20, 60, 20);
                                                break;
                                            case "Push Point":
                                                p.sendTitle("\uD83E\uDED0", "", 20, 60, 20);
                                                break;
                                        }
                                    }
                                    break;
                                case 30:
                                    finaleRound++;
                                    String game = gameOrder.get(finaleRound - 1);
                                    switch (game) {
                                        case "Slime Golf":
                                            startSlimeGolfFinale();
                                            break;
//                                    case "Survival Games":
//
//                                        break;
//                                    case "Gub Game":
//
//                                        break;
//                                    case "Colour Dash":
//                                        startColourDashFinale();
//                                        break;
                                        case "Dimension Dash":
                                            startDimensionDashFinale();
                                            break;
                                        case "Craftalot":
                                            startCraftalotFinale();
                                            break;
                                        case "Bridge Builders":
                                            startBridgeBuildersFinale();
                                            break;
                                        case "Zoomo Go":
                                            startZoomoGoFinale();
                                            break;
                                        case "Crumble Clash":
                                            startCrumbleClashFinale();
                                            break;
                                        case "Push Point":
                                            startPushPointFinale();
                                            break;
                                    }
                                    break;
                                default:
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

            plugin.runningTimers.put(name, new AbstractMap.SimpleEntry<>(task, 51));
        }
    }

    public String formatLine(String leftText, String rightText, int targetWidth) {
        // Regex for emojis and circled numbers
        String regex = "\uD83D\uDCB0|[❶-❽]";

        // Strip formatting and special characters for width calculation
        String strippedLeft = leftText.replaceAll(regex, "");
        String strippedRight = rightText.replaceAll(regex, "");

        // Measure widths using your font utility
        int leftWidth = FontUtils.getStringWidth(strippedLeft);
        int rightWidth = FontUtils.getStringWidth(strippedRight);

        // Remaining width to fill
        int remainingWidth = targetWidth - leftWidth - rightWidth;

        // If no space remains, just concatenate
        if (remainingWidth <= 0) {
            return leftText + rightText;
        }

        // Generate the exact-width space character(s)
        String space = getExactSpace(remainingWidth);

        return leftText + space + rightText;
    }
}

