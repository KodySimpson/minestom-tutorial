package me.kodysimpson.models;

import java.util.HashSet;
import java.util.Set;

public class PermissionGroup {
    private Set<String> children;
    private Set<String> permissions;

    public PermissionGroup() {
        this.children = new HashSet<>();
        this.permissions = new HashSet<>();
    }

    public PermissionGroup(Set<String> children, Set<String> permissions) {
        this.children = children;
        this.permissions = permissions;
    }

    public Set<String> getChildren() {
        return children;
    }

    public void setChildren(Set<String> children) {
        this.children = children;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }
}
