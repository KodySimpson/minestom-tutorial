package me.kodysimpson.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.block.jukebox.JukeboxSong;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.JukeboxPlayable;

import java.util.List;

public class WeaponCommand extends Command {

    public WeaponCommand() {
        super("weapon");

        setDefaultExecutor((sender, context) -> {
            if (sender instanceof Player player) {
                //Obtaining an ItemStack
                //ItemStack sword = ItemStack.of(Material.DIAMOND_SWORD);

                ItemStack sword = ItemStack.builder(Material.DIAMOND_AXE)
                        .glowing()
                        .lore(List.of(Component.text("This sword has"), Component.text("magic powers!")))
                        .amount(2)
                        .customName(Component.text("Magic Sword").color(NamedTextColor.DARK_AQUA).decorate(TextDecoration.ITALIC))
                        .set(ItemComponent.DAMAGE, 100)
                        .set(ItemComponent.HIDE_TOOLTIP)
                        .set(ItemComponent.JUKEBOX_PLAYABLE, new JukeboxPlayable(JukeboxSong.CHIRP)) //not sure if this works lol
                        .build();

                player.getInventory().addItemStack(sword);
                player.sendMessage("Weapon added to your inventory");
            } else {
                sender.sendMessage("You must be a player to execute this command.");
            }
        });
    }

}
