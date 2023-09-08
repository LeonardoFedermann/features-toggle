package com.project.featurestoggle.controllers;

import com.project.featurestoggle.dtos.*;
import com.project.featurestoggle.entities.User;
import com.project.featurestoggle.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public Page<UserListData> list(@PageableDefault(size = 10, sort = {"name"}) Pageable pageable) {
        return userRepository.findAllByIsActiveTrue(pageable).map(UserListData::new);
    }

    @GetMapping("/{id}")
    public ResponseEntity detail(@PathVariable Long id) {
        User user = userRepository.getReferenceById(id);
        return ResponseEntity.ok(new UserDetailData(user));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<UserDetailData> create(
            @RequestBody UserCreateData userCreateData,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        User user = new User(userCreateData);
        userRepository.save(user);

        URI uri = uriComponentsBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(new UserDetailData(user));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity update(
            @PathVariable Long id,
            @RequestBody UserUpdateData userUpdateData
    ) {
        User user = userRepository.getReferenceById(id);
        //TODO: update "updatedBy" property with the ID of the logged user who sent the request
        user.update(userUpdateData);
        return ResponseEntity.ok(new UserDetailData(user));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity toggle(@PathVariable Long id) {
        User user = userRepository.getReferenceById(id);
        user.toggle();
        return ResponseEntity.ok(new UserToggleData(user));
    }
}
