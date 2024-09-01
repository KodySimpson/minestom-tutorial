package me.kodysimpson.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class MenuCommand extends Command {

    public MenuCommand() {
        super("menu");
        setDefaultExecutor((sender, context) -> {
            if (sender instanceof Player player) {
                player.sendMessage("Opening menu...");
                var inventory = createInventory();
                player.openInventory(inventory);
            }
        });
    }

    private Inventory createInventory() {
        var inventory = new Inventory(InventoryType.CHEST_1_ROW, "Menu");

        //Add items to the inventory to represent buttons
        var fart = ItemStack.builder(Material.DIRT)
                .customName(Component.text("Fart")
                        .color(TextColor.color(0xFF9100))
                        .decorate(TextDecoration.BOLD))
                .build();
        var feed = ItemStack.builder(Material.BREAD)
                .customName(Component.text("Feed")
                        .color(TextColor.color(0x00FF00))
                        .decorate(TextDecoration.BOLD))
                .build();
        var kill = ItemStack.builder(Material.DIAMOND_SWORD)
                .customName(Component.text("Kill")
                        .color(TextColor.color(0xFF0000))
                        .decorate(TextDecoration.BOLD))
                .build();

        //Add the items to the inventory
        inventory.setItemStack(0, fart);
        inventory.setItemStack(4, feed);
        inventory.setItemStack(7, kill);

        //Add the inventory to an eventnode so that we can handle events
        var menuInventoryNode = EventNode.type("menuInventory", EventFilter.INVENTORY, (inventoryEvent, inventory1) -> inventory1 == inventory);
        menuInventoryNode.addListener(EventListener.builder(InventoryPreClickEvent.class)
                .filter(inventoryPreClickEvent -> inventoryPreClickEvent.getClickType() == ClickType.LEFT_CLICK)
                .handler(event -> {
                    event.setCancelled(true);
                    System.out.println("Slot: " + event.getSlot());
                    if (event.getClickedItem() == fart) {
                        event.getPlayer().sendMessage("You farted!");
                    } else if (event.getClickedItem() == feed) {
                        event.getPlayer().sendMessage("You fed yourself!");
                    } else if (event.getClickedItem() == kill) {
                        event.getPlayer().sendMessage("You killed yourself!");
                    }
                })
                .build());
        MinecraftServer.getGlobalEventHandler().addChild(menuInventoryNode);
        return inventory;
    }
}
