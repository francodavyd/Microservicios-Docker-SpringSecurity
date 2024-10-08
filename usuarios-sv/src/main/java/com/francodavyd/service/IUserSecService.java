package com.francodavyd.service;

import com.francodavyd.model.UserSec;

import java.util.List;
import java.util.Optional;

public interface IUserSecService {
    public UserSec save(UserSec userSec);
    public List<UserSec> findAll();
    public Optional<UserSec> findById(Long id);
    public void deleteById(Long id);
    public UserSec update(Long id, UserSec updatedUserSec);
    public String encriptPassword(String password);
}