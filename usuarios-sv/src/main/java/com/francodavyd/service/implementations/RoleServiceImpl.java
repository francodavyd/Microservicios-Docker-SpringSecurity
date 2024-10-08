package com.francodavyd.service.implementations;

import com.francodavyd.model.Role;
import com.francodavyd.repository.IRoleRepository;
import com.francodavyd.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private IRoleRepository repository;

    @Override
    public List<Role> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Role> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Role save(Role role) {
        return repository.save(role);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Role update(Long id, Role updatedRole) {
        Optional<Role> searchedRole = this.findById(id);
        if (searchedRole.isPresent()) {
            Role role = searchedRole.get();
            role.setRole(updatedRole.getRole());
            role.setPermissionsList(updatedRole.getPermissionsList());
            return repository.save(role);
        }
        return null;
    }

}