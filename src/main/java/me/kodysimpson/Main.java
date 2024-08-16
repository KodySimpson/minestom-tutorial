package me.kodysimpson;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.*;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.item.PickupItemEvent;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerStartSneakingEvent;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.time.Duration;

public class Main {
    public static void main(String[] args) {
        MinecraftServer server = MinecraftServer.init();

        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer(new AnvilLoader("test2"));

        instanceContainer.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK));
        instanceContainer.setChunkSupplier(LightingChunk::new);

        // Add an event callback to specify the spawning instance (and the spawn position)
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 42, 0));
        });

        //add a listener for block breaking
        globalEventHandler.addListener(PlayerBlockBreakEvent.class, event -> {
            System.out.println("Player broke a block!");
            //get the material of the broken block
            var material = event.getBlock().registry().material();
            if (material != null) {
                //create a new itemstack
                var itemStack = ItemStack.of(material);
                //create an entity for the item that we will spawn on the ground
                ItemEntity itemEntity = new ItemEntity(itemStack);
                //where the entity will spawn: instance(world) and x,y,z
                itemEntity.setInstance(event.getInstance(), event.getBlockPosition().add(0.5, 0.5, 0.5));
                //the amount of time after being dropped before the item can be picked up
                itemEntity.setPickupDelay(Duration.ofMillis(500));
            }
        });

        //Nodes: a way to organize listeners and create conditions
        EventNode<Event> node = EventNode.all("all"); //accepts all events
        node.addListener(PickupItemEvent.class, event -> {
            System.out.println("Player picked up an item!");
            var itemStack = event.getItemStack(); //get the itemstack that was picked up
            //make sure the livingentiy is a player
            if (event.getLivingEntity() instanceof Player player) {
                //add the item to the player's inventory
                player.getInventory().addItemStack(itemStack);
            }
        });

        //Accepts only player events
        EventNode<PlayerEvent> playerNode = EventNode.type("players", EventFilter.PLAYER);
        playerNode.addListener(ItemDropEvent.class, event -> {
            System.out.println("Player dropped an item!");
            ItemEntity itemEntity = new ItemEntity(event.getItemStack());
            itemEntity.setInstance(event.getPlayer().getInstance(), event.getPlayer().getPosition());
            itemEntity.setVelocity(event.getPlayer().getPosition().add(0, 1, 0).direction().mul(16));
            itemEntity.setPickupDelay(Duration.ofMillis(500));
        });
        node.addChild(playerNode);

        //Accepts only player events where the player is invisible(wow)
        var groundedPlayersNode = EventNode.value("grounded-players", EventFilter.PLAYER, Player::isOnGround);
        groundedPlayersNode.addListener(EventListener.builder(PlayerStartSneakingEvent.class)
                        .expireCount(3)
                        .expireWhen(event -> {
                            if (event.getPlayer().getInventory().getItemInMainHand().material() == Material.GRASS_BLOCK){
                                event.getPlayer().sendMessage("You found a grass block, good job.");
                                return true;
                            }
                            return false;
                        })
                        .handler(event -> {
                            System.out.println("Player is sneaking!");
                        })
                .build());
        playerNode.addChild(groundedPlayersNode);

        //Add the nodes to the global event handler
        globalEventHandler.addChild(node);

        MojangAuth.init();
        server.start("0.0.0.0", 25565);
    }
}