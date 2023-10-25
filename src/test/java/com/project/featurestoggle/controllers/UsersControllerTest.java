package com.project.featurestoggle.controllers;

import com.project.featurestoggle.data.UserRepository;
import com.project.featurestoggle.dtos.UserCreateData;
import com.project.featurestoggle.dtos.UserDetailData;
import com.project.featurestoggle.entities.User;
import com.project.featurestoggle.exceptions.NotFoundException;
import com.project.featurestoggle.services.UserService;
import com.project.featurestoggle.utils.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import static org.mockito.Mockito.when;

import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@ExtendWith(MockitoExtension.class)
class UsersControllerTest {
    @Mock
    private UserRepository usersRepository;

    @InjectMocks
    private UsersController usersController;

    @Test
    void list() {
    }

    @Test
    void detail() {
        Long mockId = (long) 1;
        UserCreateData mockUserCreateData = new UserCreateData(
                "John Due",
                "johndue@gmail.com",
                "$Aa123456"
        );
        User mockUser = new User(mockUserCreateData);
        UserDetailData expectedUser = new UserDetailData(mockUser);

        when(usersRepository.findById(mockId).orElseThrow(
                () -> new NotFoundException(Constants.USER_NOT_FOUND_MESSAGE)
        )).thenReturn(mockUser);

        UserDetailData receivedUser = usersController.detail(mockId);

        assertEquals(expectedUser.name(), receivedUser.name());
        assertEquals(expectedUser.email(), receivedUser.email());
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void activate() {
    }

    @Test
    void deactivate() {
    }
}