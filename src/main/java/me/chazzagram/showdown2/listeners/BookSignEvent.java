package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import me.chazzagram.showdown2.files.PresentsConfig;
import me.chazzagram.showdown2.files.WishesConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;

public class BookSignEvent implements Listener {

    private static Showdown2 plugin;

    public BookSignEvent(Showdown2 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBookSign(PlayerEditBookEvent event) {
        if (event.isSigning()) {

            BookMeta meta = event.getNewBookMeta();

            String title = meta.getTitle();
            List<String> pages = meta.getPages();

            plugin.messagePlayer(event.getPlayer(), "§a§l[!] Your book has been sent! Thank you!");

            // Example: print pages to console
            System.out.println("Book signed by: " + event.getPlayer().getName());
            System.out.println("Title: " + title);


            StringBuilder wish = new StringBuilder();
            wish.append(event.getPlayer().getName()).append(": ");
            for (int i = 0; i < pages.size(); i++) {
                System.out.println("Page " + (i+1) + ": " + pages.get(i));
                wish.append(pages.get(i)).append(", ");
            }

            if(WishesConfig.get().getConfigurationSection("wishes") != null) {
                int wishCount = WishesConfig.get().getConfigurationSection("wishes").getKeys(false).size() + 1;
                WishesConfig.get().set("wishes." + wishCount, wish.toString());
            } else {
                WishesConfig.get().set("wishes.1", wish.toString());
            }
            WishesConfig.save();

            // You can store/save the text however you want here
        }
    }
}
