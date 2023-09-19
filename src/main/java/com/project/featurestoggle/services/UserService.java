package com.project.featurestoggle.services;

import com.project.featurestoggle.data.UserRepository;
import com.project.featurestoggle.dtos.*;
import com.project.featurestoggle.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Page<UserListData> list(Pageable pageable) {
        return userRepository.findAllByIsActiveTrue(pageable).map(UserListData::new);
    }

    public UserDetailData detail(Long id) {
        User user = userRepository.getReferenceById(id);
        return new UserDetailData(user);
    }

    public UserDetailData create(UserCreateData userCreateData) {
        User user = new User(userCreateData);
        userRepository.save(user);

        return new UserDetailData(user);
    }

    public UserDetailData update(Long id, UserUpdateData userUpdateData) {
        User user = userRepository.getReferenceById(id);
        // TODO: update "updatedBy" property with the ID of the logged user who sent the request
        // TODO: allow user update only if the current logged user is the same one or if it is a privileged one.
        user.update(userUpdateData);
        return new UserDetailData(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public UserActivatieAndDeactivatieData activate(Long id) {
        User user = userRepository.getReferenceById(id);
        user.activate();
        return new UserActivatieAndDeactivatieData(user);
    }

    public UserActivatieAndDeactivatieData deactivate(Long id) {
        User user = userRepository.getReferenceById(id);
        user.deactivate();
        return new UserActivatieAndDeactivatieData(user);
    }
}
