package com.francodavyd.service;

import com.francodavyd.model.Role;

import java.util.List;
import java.util.Optional;

public interface IRoleService {
    public List<Role> findAll();
    public Optional<Role> findById(Long id);
    public Role save(Role role);
    public void deleteById(Long id);
    public Role update(Long id, Role updatedRole);
}