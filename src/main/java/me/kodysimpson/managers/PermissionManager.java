package me.kodysimpson.managers;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.kodysimpson.models.PermissionsFile;
import me.kodysimpson.models.PlayerPermissions;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PermissionManager {

    private PermissionsFile permissionsFile;
    private final String FILE_NAME = "permissions.json";

    public PermissionManager() {
        loadData();
    }

    public void loadData() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                this.permissionsFile = objectMapper.readValue(file, PermissionsFile.class);
                System.out.println("Data loaded successfully!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            this.createFile();
        }
    }

    public void saveData() {
        File file = new File(FILE_NAME);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(file, permissionsFile);
            System.out.println("Data saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPermissionToPlayer(UUID uuid, String permission) {
        var playerPermissions = permissionsFile.getPlayerPermissions().get(uuid.toString());
        if (playerPermissions == null) {
            playerPermissions = new PlayerPermissions();
            permissionsFile.getPlayerPermissions().put(uuid.toString(), playerPermissions);
        }
        playerPermissions.getPermissions().add(permission);
        saveData();
    }

    public void removePermissionFromPlayer(UUID uuid, String permission) {
        var playerPermissions = permissionsFile.getPlayerPermissions().get(uuid.toString());
        if (playerPermissions == null) {
            return;
        }
        playerPermissions.getPermissions().remove(permission);
        saveData();
    }

    public void addPlayerToGroup(UUID uuid, String group) {
        var playerPermissions = permissionsFile.getPlayerPermissions().get(uuid.toString());
        if (playerPermissions == null) {
            playerPermissions = new PlayerPermissions();
            permissionsFile.getPlayerPermissions().put(uuid.toString(), playerPermissions);
        }
        playerPermissions.getGroups().add(group);
        saveData();
    }

    public Set<String> getGroupPermissions(String name) {
        //Get all the group's permissions, and also recursively get the permissions of the group's children
        var group = permissionsFile.getPermissionGroups().get(name);
        if (group == null) {
            return new HashSet<>();
        }
        Set<String> permissions = new HashSet<>(group.getPermissions());
        for (String child : group.getChildren()) {
            permissions.addAll(getGroupPermissions(child));
        }
        return permissions;
    }

    public Set<String> getPlayerPermissions(UUID uuid){
        //get all the player's permissions, including those from their groups
        var playerPermissions = permissionsFile.getPlayerPermissions().get(uuid.toString());
        if (playerPermissions == null) {
            return new HashSet<>();
        }
        Set<String> permissions = new HashSet<>(playerPermissions.getPermissions());
        for (String group : playerPermissions.getGroups()) {
            permissions.addAll(getGroupPermissions(group));
        }
        return permissions;
    }

    private void createFile() {
        File file = new File(FILE_NAME);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            var permissionsFile = new PermissionsFile();
            objectMapper.writeValue(file, permissionsFile);
            this.permissionsFile = permissionsFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
