package me.chazzagram.showdown2.expansions;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.PlayerConfig;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class MongoManager {

    private MongoClient mongoClient;
    private MongoDatabase database;

    private HashMap<String, String> teamNamesFormatted = new HashMap<>();

    private boolean dbConnected = false;

    // TODO: REPLACE EVENT NAME
    private String eventName = "Test";

    public void connect(String uri, String dbName) {
        try {
            mongoClient = MongoClients.create(uri);
            database = mongoClient.getDatabase(dbName);
            System.out.println("Connected to MongoDB!");
            dbConnected = true;
        } catch (Exception e) {
            System.err.println("Failed to connect to MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isDbConnected() {
        return dbConnected;
    }

    public void disconnect() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Disconnected from MongoDB.");
        }
    }



    public void updateLeaderboardAsync(JavaPlugin plugin,
                                       HashMap<String, Integer> overallPlayers,
                                       HashMap<String, Integer> overallTeams,
                                       HashMap<String, Integer> modePlayers,
                                       HashMap<String, Integer> modeTeams,
                                       String currentMode) {

        teamNamesFormatted.clear();
        teamNamesFormatted.put("RubyRaiders", "Ruby Raiders");
        teamNamesFormatted.put("AmberAmbushers", "Amber Ambushers");
        teamNamesFormatted.put("TopazTroopers", "Topaz Troopers");
        teamNamesFormatted.put("KyaniteKillers", "Kyanite Killers");
        teamNamesFormatted.put("DiamondDestroyers", "Diamond Destroyers");
        teamNamesFormatted.put("SapphireSoldiers", "Sapphire Soldiers");
        teamNamesFormatted.put("SmithsoniteSlayers", "Smithsonite Slayers");
        teamNamesFormatted.put("CrystalCrashers", "Crystal Crashers");

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            MongoCollection<Document> collection = database.getCollection("Event Stats");

            Document mainDoc = collection.find().first();
            if (mainDoc == null) {
                mainDoc = new Document("seasons", new ArrayList<>());
                collection.insertOne(mainDoc);
            }
            Object mainId = mainDoc.get("_id");

            collection.updateOne(
                    Filters.and(
                            Filters.eq("_id", mainId),
                            Filters.not(Filters.elemMatch("seasons", Filters.eq("season_name", eventName)))
                    ),
                    Updates.push("seasons",
                            new Document("season_name", eventName)
                                    .append("player_leaderboard", new ArrayList<>())
                                    .append("team_leaderboard", new ArrayList<>())
                    )
            );

            // ------------------- TEAMS -------------------
            for (String team : overallTeams.keySet()) {
                int overall = overallTeams.get(team);
                Integer modePoints = modeTeams.get(team);
                String teamName = teamNamesFormatted.get(team);

                collection.updateOne(
                        Filters.and(
                                Filters.eq("_id", mainId),
                                Filters.elemMatch("seasons", Filters.and(
                                        Filters.eq("season_name", eventName),
                                        Filters.not(Filters.elemMatch("team_leaderboard", Filters.eq("team_name", teamName)))
                                ))
                        ),
                        Updates.push("seasons.$.team_leaderboard",
                                new Document("team_name", teamName)
                                        .append("Overall", 0)
                                        .append(currentMode, 0)
                        )
                );

                Document updateFields = new Document("seasons.$[season].team_leaderboard.$[team].Overall", overall);
                if (modePoints != null) {
                    updateFields.append("seasons.$[season].team_leaderboard.$[team]." + currentMode, modePoints);
                }

                collection.updateOne(
                        Filters.eq("_id", mainId),
                        new Document("$set", updateFields),
                        new UpdateOptions().arrayFilters(
                                List.of(
                                        Filters.eq("season.season_name", eventName),
                                        Filters.eq("team.team_name", teamName)
                                )
                        )
                );
            }

            // ------------------- PLAYERS -------------------
            for (String player : overallPlayers.keySet()) {
                int overall = overallPlayers.get(player);
                Integer modePoints = modePlayers.get(player);

                collection.updateOne(
                        Filters.and(
                                Filters.eq("_id", mainId),
                                Filters.elemMatch("seasons", Filters.and(
                                        Filters.eq("season_name", eventName),
                                        Filters.not(Filters.elemMatch("player_leaderboard", Filters.eq("player_name", player)))
                                ))
                        ),
                        Updates.push("seasons.$.player_leaderboard",
                                new Document("player_name", player)
                                        .append("team", PlayerConfig.get().getString("players." + player + ".team"))
                                        .append("Overall", 0)
                                        .append(currentMode, 0)
                        )
                );

                Document updateFields = new Document("seasons.$[season].player_leaderboard.$[player].Overall", overall);
                if (modePoints != null) {
                    updateFields.append("seasons.$[season].player_leaderboard.$[player]." + currentMode, modePoints);
                }

                collection.updateOne(
                        Filters.eq("_id", mainId),
                        new Document("$set", updateFields),
                        new UpdateOptions().arrayFilters(
                                List.of(
                                        Filters.eq("season.season_name", eventName),
                                        Filters.eq("player.player_name", player)
                                )
                        )
                );
            }
        });
    }
}