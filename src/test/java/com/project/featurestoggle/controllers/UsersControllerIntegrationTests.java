package com.project.featurestoggle.controllers;

import com.project.featurestoggle.data.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import com.project.featurestoggle.dtos.*;
import com.project.featurestoggle.domains.User;
import com.project.featurestoggle.exceptions.NotFoundException;
import com.project.featurestoggle.services.UserService;
import com.project.featurestoggle.utils.Constants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = UsersController.class
)
@AutoConfigureMockMvc
//@TestPropertySource(locations = "../../resources/application.yml")
// ========
@EnableAutoConfiguration
// =========
public class UsersControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsersController usersController;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private UserCreateData mockUserACreateData = new UserCreateData("TestA", "testa@gmail.com", "$TestA123");
    private UserCreateData mockUserBCreateData = new UserCreateData("TestB", "testb@gmail.com", "$TestB123");
    private UserCreateData mockUserCCreateData = new UserCreateData("TestC", "testc@gmail.com", "$TestC123");
    private UserCreateData mockUserDCreateData = new UserCreateData("TestD", "testd@gmail.com", "$TestD123");
    private UserCreateData mockUserECreateData = new UserCreateData("TestE", "teste@gmail.com", "$TestE123");
    private UserCreateData mockUserFCreateData = new UserCreateData("TestF", "testf@gmail.com", "$TestF123");
    private UserCreateData mockUserGCreateData = new UserCreateData("TestG", "testg@gmail.com", "$TestG123");
    private UserCreateData mockUserHCreateData = new UserCreateData("TestH", "testh@gmail.com", "$TestH123");

    private User mockUserA = new User(mockUserACreateData);
    private User mockUserB = new User(mockUserBCreateData);
    private User mockUserC = new User(mockUserCCreateData);
    private User mockUserD = new User(mockUserDCreateData);
    private User mockUserE = new User(mockUserECreateData);
    private User mockUserF = new User(mockUserFCreateData);
    private User mockUserG = new User(mockUserGCreateData);
    private User mockUserH = new User(mockUserHCreateData);

    private Long insertUserToDatabaseAndGetId(UserCreateData userCreateData) {
        User user = new User(userCreateData);
        userRepository.save(user);

        return new UserDetailData(user).id();
    }

    private void deleteUserFromDatabase(Long userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(Constants.USER_NOT_FOUND_MESSAGE)
        );
    }

    @Test
    void detailsSuccessTest() throws Exception {
        Long userId = insertUserToDatabaseAndGetId(this.mockUserACreateData);
        UserDetailData mockUserDetailData = new UserDetailData(this.mockUserA);

        this.mockMvc.perform(get(String.format("/users/%s", userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(mockUserDetailData.name()))
                .andExpect(jsonPath("$.isActive").value(true))
                .andExpect(jsonPath("$.email").value(mockUserDetailData.email()));

        deleteUserFromDatabase(userId);
    }
}
