package me.kodysimpson;

import me.kodysimpson.commands.BroadcastCommand;
import me.kodysimpson.commands.TitleCommand;
import me.kodysimpson.handler.TestHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;

import java.time.Duration;

public class Main {
    public static void main(String[] args) {
        MinecraftServer server = MinecraftServer.init();

        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();

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
            var material = event.getBlock().registry().material();
            if (material != null) {
                var itemStack = ItemStack.of(material);
                ItemEntity itemEntity = new ItemEntity(itemStack);
                itemEntity.setInstance(event.getInstance(), event.getBlockPosition().add(0.5, 0.5, 0.5));
                itemEntity.setPickupDelay(Duration.ofMillis(500));
            }
        });

        //event listener for block interact
        globalEventHandler.addListener(PlayerBlockInteractEvent.class, event -> {
            if (event.getHand() == Player.Hand.MAIN) {

                event.getPlayer().sendMessage("You interacted with a block!");

                //Get the instance where the player is
                var instance = event.getPlayer().getInstance();

                //Set the block at the position the player interacted with to a diamond block
//                instance.setBlock(event.getBlockPosition(), Block.DARK_OAK_DOOR
//                        .withProperty("half", "upper"));

                var testBlock = Block.SWEET_BERRY_BUSH
                        .withProperty("age", "3")
                        .withHandler(new TestHandler()); //add custom block handler
                instance.setBlock(event.getBlockPosition(), testBlock);

                //Get information about the current block
                var material = event.getBlock().registry().material();
                event.getPlayer().sendMessage("Material: " + material);

            }
        });

        //Register our commands
        MinecraftServer.getCommandManager().register(new BroadcastCommand());
        MinecraftServer.getCommandManager().register(new TitleCommand());

        MojangAuth.init();
        server.start("0.0.0.0", 25565);
    }
}