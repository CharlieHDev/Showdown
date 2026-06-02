package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.HashMap;
import java.util.Iterator;

public class CustomFurnace implements Listener {

    private static Showdown2 plugin;

    public CustomFurnace(Showdown2 plugin) {
        this.plugin = plugin;
    }

    public HashMap<Player, FurnaceInventory> playerFurnaces = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void openFurnace(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (plugin.ghostManager.getGhostPlayers().contains(player.getName())) return;

        if (!plugin.currentMode.equals("Craftalot")) return;

        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        Block block = event.getClickedBlock();
        if (block == null || block.getType() != Material.FURNACE) return;

        event.setCancelled(true);

        event.setUseInteractedBlock(org.bukkit.event.Event.Result.DENY);

        if (!playerFurnaces.containsKey(player)) {
            FurnaceInventory furnace = (FurnaceInventory) Bukkit.createInventory(null, InventoryType.FURNACE);
            furnace.setItem(1, new ItemStack(Material.COAL, 64));
            playerFurnaces.put(player, furnace);
        }

        player.openInventory(playerFurnaces.get(player));
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        if (plugin.ghostManager.getGhostPlayers().contains(player.getName())) return;

        if (event.getView().getTopInventory().getType() != InventoryType.FURNACE) return;

        FurnaceInventory furnace = (FurnaceInventory) event.getView().getTopInventory();

        if (!playerFurnaces.containsValue(furnace)) return;

        if (event.getRawSlot() == 1) {
            event.setCancelled(true);
            return;
        }

        if (event.isShiftClick()) {
            if (event.getClickedInventory() != null &&
                    event.getClickedInventory().getType() == InventoryType.PLAYER) {

                Bukkit.getScheduler().runTask(plugin, () -> handleSmelt(furnace));
            }
            return;
        }

        if (event.getRawSlot() == 0) {
            Bukkit.getScheduler().runTask(plugin, () -> handleSmelt(furnace));
        }
    }

    public void handleSmelt(FurnaceInventory furnace) {
        ItemStack input = furnace.getItem(0);
        if (input == null || input.getType() == Material.AIR) return;

        ItemStack result = getSmeltResult(input);
        if (result == null) return;

        ItemStack outputSlot = furnace.getItem(2);

        int inputAmount = input.getAmount();

        if (outputSlot != null && outputSlot.getType() != Material.AIR) {

            if (!outputSlot.isSimilar(result)) return;

            int max = outputSlot.getMaxStackSize();
            int total = outputSlot.getAmount() + inputAmount;

            if (total > max) return;

            outputSlot.setAmount(total);
            furnace.setItem(2, outputSlot);

        } else {
            ItemStack newOutput = result.clone();
            newOutput.setAmount(inputAmount);
            furnace.setItem(2, newOutput);
        }

        furnace.setItem(0, null);
    }

    public ItemStack getSmeltResult(ItemStack input) {
        Iterator<Recipe> it = Bukkit.recipeIterator();

        while (it.hasNext()) {
            Recipe recipe = it.next();
            if (recipe instanceof FurnaceRecipe furnaceRecipe) {
                if (furnaceRecipe.getInputChoice().test(input)) {
                    return furnaceRecipe.getResult();
                }
            }
        }
        return null;
    }
}
