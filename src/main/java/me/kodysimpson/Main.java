package me.kodysimpson;

import me.kodysimpson.commands.DisguiseCommand;
import me.kodysimpson.commands.GamemodeCommand;
import me.kodysimpson.commands.UndisguiseCommand;
import me.kodysimpson.commands.permissions.AddPermissionCommand;
import me.kodysimpson.commands.permissions.RemovePermissionCommand;
import me.kodysimpson.listeners.PlayerBlockListener;
import me.kodysimpson.listeners.PlayerEventListener;
import me.kodysimpson.managers.DisguiseManager;
import me.kodysimpson.managers.PermissionManager;
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
        PermissionManager permissionManager = new PermissionManager();

        //Add Commands
        MinecraftServer.getCommandManager().register(new DisguiseCommand(disguiseManager));
        MinecraftServer.getCommandManager().register(new UndisguiseCommand(disguiseManager));
        MinecraftServer.getCommandManager().register(new AddPermissionCommand(permissionManager));
        MinecraftServer.getCommandManager().register(new RemovePermissionCommand(permissionManager));
        MinecraftServer.getCommandManager().register(new GamemodeCommand());

        //Add Listeners
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addChild(PlayerEventListener.getEventNode(instanceContainer, disguiseManager, permissionManager));
        globalEventHandler.addChild(PlayerBlockListener.getEventNode());

        MojangAuth.init();
        server.start("0.0.0.0", 25565);
    }
}