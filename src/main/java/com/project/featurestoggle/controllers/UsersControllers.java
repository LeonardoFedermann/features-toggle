package com.project.featurestoggle.controllers;

import com.project.featurestoggle.dtos.UserCreateData;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersControllers {
    @PostMapping
    @Transactional
    public void create(@RequestBody UserCreateData userCreateData){
        System.out.println("AQUIIIIIIIIIIIII: " + userCreateData);
    }
}
