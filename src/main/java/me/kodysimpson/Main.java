package me.kodysimpson;

import me.kodysimpson.commands.DisguiseCommand;
import me.kodysimpson.commands.UndisguiseCommand;
import me.kodysimpson.listeners.PlayerEventListener;
import me.kodysimpson.managers.DisguiseManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;

public class Main {

    public static void main(String[] args) {
        MinecraftServer server = MinecraftServer.init();

        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
        instanceContainer.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK));
        instanceContainer.setChunkSupplier(LightingChunk::new);

        //Setup our managers
        DisguiseManager disguiseManager = new DisguiseManager();

        //Add Commands
        MinecraftServer.getCommandManager().register(new DisguiseCommand(disguiseManager));
        MinecraftServer.getCommandManager().register(new UndisguiseCommand(disguiseManager));

        //Add Listeners
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addChild(PlayerEventListener.getEventNode(instanceContainer, disguiseManager));

        MojangAuth.init();
        server.start("0.0.0.0", 25565);
    }
}