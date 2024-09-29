package me.kodysimpson.commands.permissions;

import me.kodysimpson.managers.PermissionManager;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.minestom.server.permission.Permission;

public class AddPermissionCommand extends Command {
    public AddPermissionCommand(PermissionManager permissionManager) {
        super("addpermission");

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Usage: /addpermission <permission>");
        });

        var permissionArg = ArgumentType.String("permission");
        addSyntax((sender, context) -> {
            if (sender instanceof Player player) {
                var permissionName = context.get(permissionArg);
                player.addPermission(new Permission(permissionName));
                permissionManager.addPermissionToPlayer(player.getUuid(), permissionName);
                player.sendMessage("Permission added successfully!");
            }else{
                sender.sendMessage("You must be a player to execute this command.");
            }
        }, permissionArg);
    }
}
