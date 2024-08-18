package me.kodysimpson;

import me.kodysimpson.commands.FartCommand;
import me.kodysimpson.commands.SetHealthCommand;
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
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.anvil.AnvilLoader;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.utils.time.TimeUnit;

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
            var material = event.getBlock().registry().material();
            if (material != null) {
                var itemStack = ItemStack.of(material);
                ItemEntity itemEntity = new ItemEntity(itemStack);
                itemEntity.setInstance(event.getInstance(), event.getBlockPosition().add(0.5, 0.5, 0.5));
                itemEntity.setPickupDelay(Duration.ofMillis(500));
            }
        });

        //Register our commands
        MinecraftServer.getCommandManager().register(new FartCommand());
        MinecraftServer.getCommandManager().register(new SetHealthCommand());

        //Using the Scheduler to do world saving
        MinecraftServer.getSchedulerManager().buildShutdownTask(() -> {
            System.out.println("Server shutting down, saving all instances.");
            instanceManager.getInstances().forEach(Instance::saveChunksToStorage);
        });

        //A repeating task that can save the instances
        MinecraftServer.getSchedulerManager().buildTask(() -> {
                    System.out.println("Saving all instances...");
                    instanceManager.getInstances().forEach(Instance::saveChunksToStorage);
                })
                .repeat(30, TimeUnit.SECOND) //Repeat every 30 seconds
                .delay(1, TimeUnit.MINUTE) //Start after 1 minute
                .schedule();

        //Another way to create a task, this one repeats every second(20 ticks per second)
        var task = MinecraftServer.getSchedulerManager().submitTask(() -> {
            System.out.println("Peanut Butter");
            return TaskSchedule.duration(20, TimeUnit.SERVER_TICK);
        });
        //You can cancel the task
        //task.cancel();

        //You can also schedule a task to run on the next tick(20 ticks per second)
        MinecraftServer.getSchedulerManager().scheduleNextTick(() -> {
            System.out.println("This will run next tick!");
        });

        MojangAuth.init();
        server.start("0.0.0.0", 25565);
    }
}