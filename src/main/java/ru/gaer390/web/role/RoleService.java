package ru.gaer390.web.role;

public interface RoleService {
    void addNewRole(Role nameRole);
    Role findByNameRole(String nameRole);
}
