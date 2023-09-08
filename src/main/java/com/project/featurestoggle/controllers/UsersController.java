package com.project.featurestoggle.controllers;

import com.project.featurestoggle.dtos.*;
import com.project.featurestoggle.data.UserRepository;
import com.project.featurestoggle.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Page<UserListData> list(@PageableDefault(size = 10, sort = {"name"}) Pageable pageable) {
        return userService.list(pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public UserDetailData detail(@PathVariable Long id) {
        return userService.detail(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserDetailData create(@RequestBody UserCreateData userCreateData) {
        return userService.create(userCreateData);
    }

    @PutMapping("/{id}")
    @Transactional
    @ResponseStatus(value = HttpStatus.OK)
    public UserDetailData update(
            @PathVariable Long id,
            @RequestBody UserUpdateData userUpdateData
    ) {
        return userService.update(id, userUpdateData);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @PatchMapping("/{id}")
    @Transactional
    @ResponseStatus(value = HttpStatus.OK)
    public UserToggleData toggle(@PathVariable Long id) {
        return userService.toggle(id);
    }
}
