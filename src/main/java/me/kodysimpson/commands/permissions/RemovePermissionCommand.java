package me.kodysimpson.commands.permissions;

import me.kodysimpson.managers.PermissionManager;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.minestom.server.permission.Permission;

public class RemovePermissionCommand extends Command {
    public RemovePermissionCommand(PermissionManager permissionManager) {
        super("removepermission");

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Usage: /removepermission <permission>");
        });

        var permissionArg = ArgumentType.String("permission");
        addSyntax((sender, context) -> {
            if (sender instanceof Player player) {
                var permissionName = context.get(permissionArg);
                player.removePermission(new Permission(permissionName));
                permissionManager.removePermissionFromPlayer(player.getUuid(), permissionName);
                player.sendMessage("Permission removed successfully!");
            }else{
                sender.sendMessage("You must be a player to execute this command.");
            }
        }, permissionArg);
    }
}
