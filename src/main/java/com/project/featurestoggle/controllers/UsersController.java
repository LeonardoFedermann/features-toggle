package com.project.featurestoggle.controllers;

import com.project.featurestoggle.dtos.UserCreateData;
import com.project.featurestoggle.dtos.UserListData;
import com.project.featurestoggle.entities.User;
import com.project.featurestoggle.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    @Transactional
    public void create(@RequestBody UserCreateData userCreateData) {
        userRepository.save(new User(userCreateData));
    }

    @GetMapping
    public Page<UserListData> list(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserListData::new);
    }
}
