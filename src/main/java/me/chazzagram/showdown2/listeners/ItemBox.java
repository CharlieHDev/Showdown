package me.chazzagram.showdown2.listeners;

import me.chazzagram.showdown2.Showdown2;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ItemBox {

    private static Showdown2 plugin;

    public ItemBox(Showdown2 plugin, Location loc, ItemStack item) {
        this.plugin = plugin;
        this.loc = loc;
        this.type = item;
    }

    private Interaction interaction;

    private ItemDisplay box;

    private final ItemStack type;

    private ItemDisplay powerup;

    private final Location loc;

    public Interaction getInteraction() {
        return interaction;
    }

    public ItemStack getItem() {
        return type;
    }


    public void spawn(){
        World world = Bukkit.getWorld("build");

        interaction = world.spawn(loc.clone().subtract(0,0.5,0), Interaction.class);

        interaction.setInteractionHeight(1.8f);
        interaction.setInteractionWidth(1.8f);


        box = world.spawn(loc, ItemDisplay.class);

        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();

        meta.setItemModel(NamespacedKey.fromString("amongus:itembox"));

        item.setItemMeta(meta);
        box.setItemStack(item);

        box.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GROUND);

        Transformation transformation = new Transformation(
                new Vector3f(0f, 0f, 0f),
                new Quaternionf(0f, 0f, 0f, 1f),
                new Vector3f(1.5f, 1.5f, 1.5f),
                new Quaternionf(0f, 0f, 0f, 1f)
        );

        box.setTransformation(transformation);


        powerup = world.spawn(loc, ItemDisplay.class);

        powerup.setItemStack(type);

        powerup.setBillboard(ItemDisplay.Billboard.CENTER);

        Transformation transformation2 = new Transformation(
                new Vector3f(0f, 0f, 0f),
                new Quaternionf(0f, 0f, 0f, 1f),
                new Vector3f(0.8f, 0.8f, 0.8f),
                new Quaternionf(0f, 0f, 0f, 1f)
        );

        powerup.setTransformation(transformation2);
    }

    public void despawn(){
        interaction.remove();
        box.remove();
        powerup.remove();

        Firework firework = Bukkit.getWorld("build").spawn(loc, Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();

        FireworkEffect effect = FireworkEffect.builder()
                .with(FireworkEffect.Type.BALL)
                .withTrail()
                .flicker(false)
                .withColor(Color.AQUA)
                .withFade(Color.WHITE)
                .build();

        meta.addEffect(effect);
        meta.setPower(1);
        firework.setFireworkMeta(meta);

        firework.detonate();
    }

}
