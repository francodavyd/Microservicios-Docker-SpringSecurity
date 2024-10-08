package com.francodavyd.service;

import com.francodavyd.model.Permission;

import java.util.List;
import java.util.Optional;

public interface IPermissionService {
    public Permission save(Permission permission);
    public List<Permission> findAll();
    public Optional<Permission> findById(Long id);
    public void deleteById(Long id);
    public Permission update(Long id, Permission updatedPermission);
}
