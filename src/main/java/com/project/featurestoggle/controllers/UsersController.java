package com.project.featurestoggle.controllers;

import com.project.featurestoggle.dtos.UserCreateData;
import com.project.featurestoggle.entities.User;
import com.project.featurestoggle.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
