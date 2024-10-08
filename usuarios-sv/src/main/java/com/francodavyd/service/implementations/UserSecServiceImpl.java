package com.francodavyd.service.implementations;

import com.francodavyd.model.UserSec;
import com.francodavyd.repository.IUserSecRepository;
import com.francodavyd.service.IUserSecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserSecServiceImpl implements IUserSecService {
    @Autowired
    private IUserSecRepository repository;

    @Override
    public List<UserSec> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<UserSec> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public UserSec save(UserSec userSec) {
        return repository.save(userSec);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public UserSec update(Long id, UserSec updatedUserSec) {
        Optional<UserSec> searchedUser = this.findById(id);
        if (searchedUser.isPresent()) {
            UserSec sec = searchedUser.get();
            sec.setUsername(updatedUserSec.getUsername());
            sec.setPassword(updatedUserSec.getPassword());
            sec.setEnabled(updatedUserSec.isEnabled());
            sec.setAccountNotExpired(updatedUserSec.isAccountNotExpired());
            sec.setAccountNotLocked(updatedUserSec.isAccountNotLocked());
            sec.setCredentialNotExpired(updatedUserSec.isCredentialNotExpired());
            return repository.save(sec);
        }
        return null;
    }

    @Override
    public String encriptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

}