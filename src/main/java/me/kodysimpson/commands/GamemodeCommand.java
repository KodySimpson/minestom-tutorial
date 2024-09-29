package me.kodysimpson.commands;

import me.kodysimpson.managers.Permissions;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.permission.Permission;

public class GamemodeCommand extends Command {
    public GamemodeCommand() {
        super("gamemode");

        setCondition((sender, commandString) -> sender.hasPermission(Permissions.GAMEMODE));

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Usage: /gamemode <creative|survival|adventure|spectator>");
        });

        var gamemodeArg = ArgumentType.String("gamemode");
        addSyntax((sender, context) -> {
            var gamemode = context.get(gamemodeArg);
            if (sender instanceof Player player) {
                switch (gamemode) {
                    case "creative" -> player.setGameMode(GameMode.CREATIVE);
                    case "survival" -> player.setGameMode(GameMode.SURVIVAL);
                    case "adventure" -> player.setGameMode(GameMode.ADVENTURE);
                    case "spectator" -> player.setGameMode(GameMode.SPECTATOR);
                    default -> sender.sendMessage("Invalid gamemode");
                }
            } else {
                sender.sendMessage("You must be a player to execute this command.");
            }
        }, gamemodeArg);
    }
}
