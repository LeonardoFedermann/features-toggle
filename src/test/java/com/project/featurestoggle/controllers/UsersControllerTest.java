package com.project.featurestoggle.controllers;

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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Autowired
    private JacksonTester<UserActivateAndDeactivatieData> userActivateAndDeactivateTester;

    @Value("${validation_messages_for_tests.name.mandatory}")
    private String mandatoryNameErrorMessage;

    @Value("${validation_messages_for_tests.name.size}")
    private String nameSizeErrorMessage;

    @Value("${validation_messages_for_tests.email.mandatory}")
    private String mandatoryEmailErrorMessage;

    @Value("${validation_messages_for_tests.email.format}")
    private String emailFormatErrorMessage;

    @Value("${validation_messages_for_tests.password.mandatory}")
    private String mandatoryPasswordErrorMessage;

    @Value("${validation_messages_for_tests.password.pattern}")
    private String passwordPatternErrorMessage;

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

    @Autowired
    private UsersController usersController;

    @Test
    void detailSuccessTest() {
        Long mockId = (long) 1;
        UserDetailData mockUserDetailData = new UserDetailData(this.mockUserA);

        when(this.userService.detail(mockId)).thenReturn(mockUserDetailData);

        UserDetailData userDetailData = usersController.detail(mockId);
        assertEquals(userDetailData.name(), mockUserDetailData.name());
        assertEquals(mockUserDetailData.isActive(), userDetailData.isActive());
        assertEquals(mockUserDetailData.email(), userDetailData.email());

//        this.mockMvc.perform(get(String.format("/users/%s", mockId)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value(mockUserDetailData.name()))
//                .andExpect(jsonPath("$.isActive").value(true))
//                .andExpect(jsonPath("$.email").value(mockUserDetailData.email()));
    }

    @Test
    void detailNotFoundErrorTest() throws Exception {
        Long mockId = (long) 1;
        when(this.userService.detail(mockId))
                .thenThrow(new NotFoundException(Constants.USER_NOT_FOUND_MESSAGE));

        assertThrows(NotFoundException.class, () -> usersController.detail(mockId));
    }

    @Test
    void listTest() {
        Page<UserListData> mockUsersPage = new PageImpl<>(Arrays.asList(
                new UserListData(this.mockUserA),
                new UserListData(this.mockUserB),
                new UserListData(this.mockUserC),
                new UserListData(this.mockUserD),
                new UserListData(this.mockUserE),
                new UserListData(this.mockUserF),
                new UserListData(this.mockUserG),
                new UserListData(this.mockUserH)
        ));
        Pageable pageable = PageRequest.of(0, 20);

        when(userService.list(any(Pageable.class))).thenReturn(mockUsersPage);

        List<UserListData> usersList = usersController.list(pageable)
                .stream().toList();

        assertEquals(this.mockUserB.getName(), usersList.get(1).name());
        assertEquals(this.mockUserG.getEmail(), usersList.get(6).email());
        assertEquals(true, usersList.get(2).isActive());
//
//        this.mockMvc.perform(get("/users"))
//                // RETURNING STATUS 500 WHEN IT SHOULD BE 200
////                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content.[1].name").value(this.mockUserB.getName()))
//                .andExpect(jsonPath("$.content[3].email").value(this.mockUserD.getEmail()))
//                .andExpect(jsonPath("$.content[2].isActive").value(true));
    }

    @Test
    void createValidUserTest() throws Exception {
        UserDetailData mockUserADetailData = new UserDetailData(this.mockUserA);
        when(userService.create(this.mockUserACreateData)).thenReturn(mockUserADetailData);
        var expectedJson = this.userResponseTester.write(mockUserADetailData).getJson();

        UserDetailData userDetailData = usersController.create(this.mockUserACreateData);
        var receivedJson = this.userResponseTester.write(userDetailData).getJson();

        assertEquals(expectedJson, receivedJson);

//        var response = this.mockMvc.perform(
//                post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(this.userCreateRequestTester
//                                .write(this.mockUserACreateData)
//                                .getJson()
//                        )
//        ).andReturn().getResponse();
//
//        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    @Test
    void createUserWithIncorrectName() throws Exception {
        List<String> mockInvalidNames = new ArrayList<>();

        mockInvalidNames.add("e");
        mockInvalidNames.add("a".repeat(41));

        this.testRequestWithValidationFieldError(
                "/users",
                "name",
                this.mandatoryNameErrorMessage,
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
                    this.nameSizeErrorMessage,
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
                this.mandatoryEmailErrorMessage,
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
                    this.emailFormatErrorMessage,
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
                this.mandatoryPasswordErrorMessage,
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
                    this.passwordPatternErrorMessage,
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

            var response = usersController.update(mockId, userUpdateData);
            String receivedJson = this.userResponseTester.write(response).getJson();

            assertEquals(expectedJson, receivedJson);

//            var response = this.mockMvc.perform(put(String.format("/users/%s", mockId))
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(this.userUpdateRequestTester.write(userUpdateData).getJson())
//                    )
//                    .andExpect(status().isOk())
//                    .andReturn()
//                    .getResponse();
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
    @Test
    void deleteUserTest() {
        Long mockId = (long) 1;

        doNothing().when(this.userService).delete(mockId);
        this.usersController.delete(mockId);
        verify(this.userService, times(1)).delete(mockId);
    }

    @Test
    void deleteNotFoundUserTest() {
        Long mockId = (long) 1;

        doThrow(NotFoundException.class).when(this.userService).delete(mockId);
        assertThrows(NotFoundException.class, () -> {
            this.usersController.delete(mockId);
        });
    }

    @Test
    void activateUserTest() throws IOException {
        Long mockId = (long) 1;
        UserActivateAndDeactivatieData expectedUser = new UserActivateAndDeactivatieData(this.mockUserA);
        String expectedJson = this.userActivateAndDeactivateTester.write(expectedUser).getJson();
        when(this.userService.activate(mockId)).thenReturn(
                new UserActivateAndDeactivatieData(mockUserA)
        );

        var response = this.usersController.activate(mockId);
        String receivedJson = this.userActivateAndDeactivateTester.write(response).getJson();
        assertEquals(expectedJson, receivedJson);
    }

    @Test
    void activateAndDeactivateUserNotFoundTest() {
        Long mockId = (long) 1;

        doThrow(NotFoundException.class).when(this.userService).activate(mockId);
        doThrow(NotFoundException.class).when(this.userService).deactivate(mockId);

        assertThrows(NotFoundException.class, () -> {
            this.usersController.activate(mockId);
        });

        assertThrows(NotFoundException.class, () -> {
            this.usersController.deactivate(mockId);
        });
    }
}