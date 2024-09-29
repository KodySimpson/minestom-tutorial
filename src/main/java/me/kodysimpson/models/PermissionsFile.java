package me.kodysimpson.models;

import java.util.HashMap;
import java.util.Map;

public class PermissionsFile {
    private Map<String, PlayerPermissions> playerPermissions;
    private Map<String, PermissionGroup> permissionGroups;

    public PermissionsFile() {
        this.playerPermissions = new HashMap<>();
        this.permissionGroups = new HashMap<>();
    }

    public Map<String, PlayerPermissions> getPlayerPermissions() {
        return playerPermissions;
    }

    public void setPlayerPermissions(Map<String, PlayerPermissions> playerPermissions) {
        this.playerPermissions = playerPermissions;
    }

    public Map<String, PermissionGroup> getPermissionGroups() {
        return permissionGroups;
    }

    public void setPermissionGroups(Map<String, PermissionGroup> permissionGroups) {
        this.permissionGroups = permissionGroups;
    }
}
