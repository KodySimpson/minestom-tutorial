package me.kodysimpson;

import me.kodysimpson.commands.FartCommand;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.item.PickupItemEvent;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.permission.Permission;
import net.minestom.server.utils.time.TimeUnit;

import java.time.Duration;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        //Initialize the server
        MinecraftServer server = MinecraftServer.init();

        // Create the instance
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer(new AnvilLoader("test"));

        MinecraftServer.getSchedulerManager().buildShutdownTask(() -> {
            System.out.println("Server is shutting down!");
            instanceManager.getInstances().forEach(Instance::saveChunksToStorage);
        });

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            System.out.println("This is a scheduled task that runs every 5 seconds!");
            instanceManager.getInstances().forEach(Instance::saveChunksToStorage);
        }).repeat(30, TimeUnit.SECOND).schedule();

        // Set the ChunkGenerator
        instanceContainer.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK));

        // Set the lighting system
        instanceContainer.setChunkSupplier(LightingChunk::new);

        // Add an event callback to specify the spawning instance (and the spawn position)
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 42, 0));

            player.addPermission(new Permission("fart.permission"));
        });

        globalEventHandler.addListener(PlayerBlockBreakEvent.class, event -> {
            var material = event.getBlock().registry().material();
            if (material != null){
                var itemStack = ItemStack.of(material);
                ItemEntity itemEntity = new ItemEntity(itemStack);
                itemEntity.setInstance(event.getInstance(), event.getBlockPosition().add(0.5, 0.5, 0.5));
                itemEntity.setPickupDelay(Duration.ofMillis(500));
            }
        });

        globalEventHandler.addListener(ItemDropEvent.class, event -> {
            System.out.println("Item Drop Event");
            ItemEntity itemEntity = new ItemEntity(event.getItemStack());
            itemEntity.setInstance(event.getPlayer().getInstance(), event.getPlayer().getPosition());
            itemEntity.setVelocity(event.getPlayer().getPosition().direction().mul(6));
            itemEntity.setPickupDelay(Duration.ofSeconds(5));
        });

        globalEventHandler.addListener(PickupItemEvent.class, event -> {
            System.out.println("Pickup Item Event");
            var itemStack = event.getItemStack();
            System.out.println(itemStack.material());
            if (event.getLivingEntity() instanceof Player player){
                player.getInventory().addItemStack(itemStack);
            }
        });

        //register commands
        MinecraftServer.getCommandManager().register(new FartCommand());

        MojangAuth.init();

        //start the server
        server.start("0.0.0.0", 25565);
    }
}