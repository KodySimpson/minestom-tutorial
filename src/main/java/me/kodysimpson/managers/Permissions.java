package me.kodysimpson.managers;

import net.minestom.server.permission.Permission;

//A static list of all of our server's permissions
public class Permissions {
    public static final Permission GAMEMODE = new Permission("gamemode");
    public static final Permission BREAK_BLOCKS = new Permission("block.break");
    public static final Permission BLOCKS_ALL = new Permission("block.*");
}
