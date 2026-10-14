package me.chazzagram.showdown2.expansions;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.Locale;

public class TiltifyChecker {

    private static final String BASE = "https://v5api.tiltify.com";

    private final HttpClient http = HttpClient.newHttpClient();
    private final String clientId;
    private final String clientSecret;
    private final String campaignId;

    private String token;
    private long tokenExpiresAt = 0;

    private volatile double amountRaised = -1;
    private volatile double goal = -1;
    private volatile String currency = "GBP";

    public TiltifyChecker(String clientId, String clientSecret, String campaignId) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.campaignId = campaignId;
    }

    public void refresh() throws Exception {
        ensureToken();

        JsonObject data = get("/api/public/campaigns/" + campaignId).getAsJsonObject("data");

        JsonObject amt = data.getAsJsonObject("amount_raised");
        amountRaised = Double.parseDouble(amt.get("value").getAsString());
        currency = amt.get("currency").getAsString();

        if (data.has("goal") && data.get("goal").isJsonObject()) {
            goal = Double.parseDouble(data.getAsJsonObject("goal").get("value").getAsString());
        }
    }

    public String getProgressString() {
        if (amountRaised < 0) return "Total Amount Raised: loading...";

        String symbol = currencySymbol(currency);
        String raisedStr = symbol + formatDecimal(amountRaised);

        if (goal > 0) {
            String goalStr = symbol + formatWhole(goal);
            return "Total Amount Raised: " + raisedStr + " / " + goalStr;
        }
        return "Total Amount Raised: " + raisedStr;
    }

    private String formatWhole(double value) {
        NumberFormat nf = NumberFormat.getIntegerInstance(Locale.UK);
        return nf.format(Math.round(value));
    }

    private String formatDecimal(double value) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.UK);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        return nf.format(value);
    }

    private String currencySymbol(String code) {
        switch (code) {
            case "GBP": return "£";
            case "USD": return "$";
            case "EUR": return "€";
            default: return code + " ";
        }
    }

    private void ensureToken() throws Exception {
        if (token != null && System.currentTimeMillis() < tokenExpiresAt - 30_000) return;

        String body = "client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8)
                + "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8)
                + "&grant_type=client_credentials&scope=public";

        HttpRequest req = HttpRequest.newBuilder(URI.create(BASE + "/oauth/token"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() != 200) throw new RuntimeException("Auth failed: HTTP " + res.statusCode());

        JsonObject json = JsonParser.parseString(res.body()).getAsJsonObject();
        token = json.get("access_token").getAsString();
        tokenExpiresAt = System.currentTimeMillis() + json.get("expires_in").getAsLong() * 1000L;
    }

    private JsonObject get(String path) throws Exception {
        HttpRequest req = HttpRequest.newBuilder(URI.create(BASE + path))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();
        HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() != 200) throw new RuntimeException("HTTP " + res.statusCode() + " on " + path);
        return JsonParser.parseString(res.body()).getAsJsonObject();
    }
}
