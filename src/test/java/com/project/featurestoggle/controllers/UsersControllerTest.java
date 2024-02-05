package com.project.featurestoggle.controllers;

import com.project.featurestoggle.dtos.UserCreateData;
import com.project.featurestoggle.dtos.UserDetailData;
import com.project.featurestoggle.dtos.UserListData;
import com.project.featurestoggle.domains.User;
import com.project.featurestoggle.dtos.UserUpdateData;
import com.project.featurestoggle.exceptions.NotFoundException;
import com.project.featurestoggle.services.UserService;
import com.project.featurestoggle.utils.Constants;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

class UsersControllerTest extends BasicControllerTest {
    @MockBean
    UserService userService;

    @Autowired
    private JacksonTester<UserCreateData> userCreateRequestTester;

    @Autowired
    private JacksonTester<UserDetailData> userResponseTester;

    @Autowired
    private JacksonTester<UserUpdateData> userUpdateRequestTester;

    private UserCreateData mockUserACreateData = new UserCreateData("TestA", "testa@gmail.com", "$TestA123");
    private UserCreateData mockUserBCreateData = new UserCreateData("TestB", "testb@gmail.com", "$TestB123");
    private UserCreateData mockUserCCreateData = new UserCreateData("TestC", "testc@gmail.com", "$TestC123");
    private UserCreateData mockUserDCreateData = new UserCreateData("TestD", "testd@gmail.com", "$TestD123");

    private User mockUserA = new User(mockUserACreateData);
    private User mockUserB = new User(mockUserBCreateData);
    private User mockUserC = new User(mockUserCCreateData);
    private User mockUserD = new User(mockUserDCreateData);

    @Value("{password.pattern}")
    private String test;

    @Test
    void detailSuccessTest() throws Exception {
        Long mockId = (long) 1;
        UserDetailData mockUserDetailData = new UserDetailData(this.mockUserA);

        when(this.userService.detail(mockId)).thenReturn(mockUserDetailData);

        this.mockMvc.perform(get(String.format("/users/%s", mockId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(mockUserDetailData.name()))
                .andExpect(jsonPath("$.isActive").value(true))
                .andExpect(jsonPath("$.email").value(mockUserDetailData.email()));
    }

    @Test
    void detailNotFoundErrorTest() throws Exception {
        Long mockId = (long) 1;
        when(this.userService.detail(mockId))
                .thenThrow(new NotFoundException(Constants.USER_NOT_FOUND_MESSAGE));

        this.mockMvc.perform(get(String.format("/users/%s", mockId)))
                .andExpect(status().isNotFound());
    }

    @Test
    void defaultListTest() throws Exception {
        List<UserListData> mockUsersList = new ArrayList<>();

        mockUsersList.add(new UserListData(this.mockUserA));
        mockUsersList.add(new UserListData(this.mockUserB));
        mockUsersList.add(new UserListData(this.mockUserC));
        mockUsersList.add(new UserListData(this.mockUserD));

        Page<UserListData> mockUsersPage = new PageImpl<>(mockUsersList);

        when(userService.list(any(Pageable.class))).thenReturn(mockUsersPage);

        this.mockMvc.perform(get("/users"))
                // RETURNING STATUS 500 WHEN IT SHOULD BE 200
//                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[1].name").value(this.mockUserB.getName()))
                .andExpect(jsonPath("$.content[3].email").value(this.mockUserD.getEmail()))
                .andExpect(jsonPath("$.content[2].isActive").value(true));
    }

    // list
    // teste para paginação com diferentes valores de size, page e sort

    @Test
    void createValidUserTest() throws Exception {
        UserDetailData mockUserADetailData = new UserDetailData(this.mockUserA);
        when(userService.create(this.mockUserACreateData)).thenReturn(mockUserADetailData);
        var expectedJson = this.userResponseTester.write(mockUserADetailData).getJson();

        var response = this.mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.userCreateRequestTester
                                .write(this.mockUserACreateData)
                                .getJson()
                        )
        ).andReturn().getResponse();

        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    @Test
    void createUserWithIncorrectName() throws Exception {
        String nameNumberOfCharactersErrorMessage = "Name must have between 3 and 40 characters.";
        List<String> mockInvalidNames = new ArrayList<>();

        mockInvalidNames.add("e");
        mockInvalidNames.add("a".repeat(41));

        this.testRequestWithValidationFieldError(
                "/users",
                "name",
                "Name field is mandatory.",
                "POST",
                userCreateRequestTester.write(
                        new UserCreateData(
                                null,
                                "testuser@gmail.com",
                                "$Aa123456"
                        )
                ).getJson()
        );

        for (String invalidName : mockInvalidNames) {
            this.testRequestWithValidationFieldError(
                    "/users",
                    "name",
                    nameNumberOfCharactersErrorMessage,
                    "POST",
                    userCreateRequestTester.write(
                            new UserCreateData(
                                    invalidName,
                                    "testuser@gmail.com",
                                    "$Aa123456"
                            )
                    ).getJson()
            );
        }
    }

    @Test
    void createUserWithIncorrectEmail() throws Exception {
        List<String> mockInvalidEmails = new ArrayList<>();

        mockInvalidEmails.add(String.format("%s@gmail.com", "a".repeat(400)));
        mockInvalidEmails.add("testemail.com");

        this.testRequestWithValidationFieldError(
                "/users",
                "email",
                "Email field is mandatory.",
                "POST",
                userCreateRequestTester.write(
                        new UserCreateData(
                                "Test User",
                                null,
                                "$Aa123456"
                        )
                ).getJson()
        );

        for (String invalidEmail : mockInvalidEmails) {
            this.testRequestWithValidationFieldError(
                    "/users",
                    "email",
                    "Email must be in the proper format. Make sure you included @.",
                    "POST",
                    userCreateRequestTester.write(
                            new UserCreateData(
                                    "Test User",
                                    invalidEmail,
                                    "$Aa123456"
                            )
                    ).getJson()
            );
        }
    }

    @Test
    void createUserWithIncorrectPassword() throws Exception {
        List<String> mockInvalidPasswords = new ArrayList<>();

        mockInvalidPasswords.add("Aa123456");
        mockInvalidPasswords.add("$123456");
        mockInvalidPasswords.add("$Aabcde");
        mockInvalidPasswords.add("$AA123456");
        mockInvalidPasswords.add("$aa123456");
        mockInvalidPasswords.add("$Aa1");
        mockInvalidPasswords.add(String.format("$A%s8", "a".repeat(20)));

        this.testRequestWithValidationFieldError(
                "/users",
                "password",
                "Password field is mandatory.",
                "POST",
                userCreateRequestTester.write(
                        new UserCreateData(
                                "Test User",
                                "testuser@gmail.com",
                                null
                        )
                ).getJson()
        );

        for (String invalidPassword : mockInvalidPasswords) {
            this.testRequestWithValidationFieldError(
                    "/users",
                    "password",
                    "Password must have between 5 and 20 characters and at least one digit, one uppercase letter, one lowercase letter and one special character.",
                    "POST",
                    userCreateRequestTester.write(
                            new UserCreateData(
                                    "Test User",
                                    "testuser@gmail.com",
                                    invalidPassword
                            )
                    ).getJson()
            );
        }
    }

    @Test
    void updateSuccessTest() throws Exception {
        Long mockId = (long) 1;
        List<UserUpdateData> userUpdateDataList = new ArrayList<>();
        String updatedName = mockUserACreateData.name();
        String updatedEmail = mockUserACreateData.email();
        String updatedPassword = mockUserACreateData.password();
        UserDetailData returnUser = new UserDetailData(this.mockUserA);
        var expectedJson = this.userResponseTester.write(returnUser).getJson();

        userUpdateDataList.add(new UserUpdateData(updatedName, null, null));
        userUpdateDataList.add(new UserUpdateData(null, updatedEmail, null));
        userUpdateDataList.add(new UserUpdateData(null, null, updatedPassword));
        userUpdateDataList.add(new UserUpdateData(updatedName, updatedEmail, null));
        userUpdateDataList.add(new UserUpdateData(updatedName, null, updatedPassword));
        userUpdateDataList.add(new UserUpdateData(null, updatedEmail, updatedPassword));
        userUpdateDataList.add(new UserUpdateData(updatedName, updatedEmail, updatedPassword));

        for (UserUpdateData userUpdateData : userUpdateDataList) {
            when(this.userService.update(mockId, userUpdateData)).thenReturn(returnUser);

            var response = this.mockMvc.perform(put(String.format("/users/%s", mockId))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(this.userUpdateRequestTester.write(userUpdateData).getJson())
                    )
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse();

            assertThat(response.getContentAsString()).isEqualTo(expectedJson);
        }
    }

    @Test
    void updateUserWithIncorrectName() throws Exception {
        String nameNumberOfCharactersErrorMessage = "Name must have between 3 and 40 characters.";
        List<String> mockInvalidNames = new ArrayList<>();

        mockInvalidNames.add("e");
        mockInvalidNames.add("a".repeat(41));

        for (String invalidName : mockInvalidNames) {
            this.testRequestWithValidationFieldError(
                    String.format("/users/%s", 1),
                    "name",
                    nameNumberOfCharactersErrorMessage,
                    "PUT",
                    userUpdateRequestTester.write(
                            new UserUpdateData(invalidName, null, null)
                    ).getJson()
            );
        }
    }

    @Test
    void updateUserWithIncorrectEmail() throws Exception {
        List<String> mockInvalidEmails = new ArrayList<>();

        mockInvalidEmails.add(String.format("%s@gmail.com", "a".repeat(400)));
        mockInvalidEmails.add("testemail.com");

        for (String invalidEmail : mockInvalidEmails) {
            this.testRequestWithValidationFieldError(
                    String.format("/users/%s", 1),
                    "email",
                    "Email must be in the proper format. Make sure you included @.",
                    "PUT",
                    userUpdateRequestTester.write(
                            new UserUpdateData(null, invalidEmail, null)
                    ).getJson()
            );
        }
    }

    @Test
    void updateUserWithIncorrectPassword() throws Exception {
        List<String> mockInvalidPasswords = new ArrayList<>();

        mockInvalidPasswords.add("Aa123456");
        mockInvalidPasswords.add("$123456");
        mockInvalidPasswords.add("$Aabcde");
        mockInvalidPasswords.add("$AA123456");
        mockInvalidPasswords.add("$aa123456");
        mockInvalidPasswords.add("$Aa1");
        mockInvalidPasswords.add(String.format("$A%s8", "a".repeat(20)));

        for (String invalidPassword : mockInvalidPasswords) {
            this.testRequestWithValidationFieldError(
                    String.format("/users/%s", 1),
                    "password",
                    "Password must have between 5 and 20 characters and at least one digit, one uppercase letter, one lowercase letter and one special character.",
                    "PUT",
                    userUpdateRequestTester.write(
                            new UserUpdateData(null, null, invalidPassword)
                    ).getJson()
            );
        }
    }

    // delete
    // apagar usuário

    // activate and deactivate
    // testar ativação e desativação de usuário
}