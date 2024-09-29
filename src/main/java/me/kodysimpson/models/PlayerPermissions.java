package me.kodysimpson.models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerPermissions {
    private Set<String> permissions;
    private Set<String> groups;

    public PlayerPermissions() {
        this.permissions = new HashSet<>();
        this.groups = new HashSet<>();
    }

    public PlayerPermissions(Set<String> permissions, Set<String> groups) {
        this.permissions = permissions;
        this.groups = groups;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public Set<String> getGroups() {
        return groups;
    }

    public void setGroups(Set<String> groups) {
        this.groups = groups;
    }
}
