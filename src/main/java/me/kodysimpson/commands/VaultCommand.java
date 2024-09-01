package me.kodysimpson.commands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class VaultCommand extends Command {
    public VaultCommand() {
        super("vault");

        setDefaultExecutor((sender, context) -> {
            var inventory = new Inventory(InventoryType.WINDOW_3X3, "Item Vault");
            inventory.addItemStack(ItemStack.of(Material.GOLD_INGOT));
            if (sender instanceof Player player){

                //Add the inventory to an eventnode so that we can handle events
                var testNode = EventNode.type("vault-inventory", EventFilter.INVENTORY, (inventoryEvent, inventory1) -> {
                    return inventory1 == inventory;
                });
                testNode.addListener(InventoryClickEvent.class, event -> {
                    sender.sendMessage("Inventory clicked");
                    event.getPlayer().sendMessage(event.getClickedItem().material().name());
                });
                testNode.addListener(InventoryCloseEvent.class, event -> {
                    sender.sendMessage("Inventory closed");
                    MinecraftServer.getGlobalEventHandler().removeChild(testNode);
                });
                MinecraftServer.getGlobalEventHandler().addChild(testNode);

                player.openInventory(inventory);
            }
        });
    }
}
