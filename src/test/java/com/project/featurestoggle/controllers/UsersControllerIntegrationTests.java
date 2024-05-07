package com.project.featurestoggle.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.featurestoggle.FeaturesToggleApplication;
import com.project.featurestoggle.data.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.project.featurestoggle.dtos.*;
import com.project.featurestoggle.domains.User;
import com.project.featurestoggle.exceptions.NotFoundException;
import com.project.featurestoggle.services.UserService;
import com.project.featurestoggle.utils.Constants;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = {FeaturesToggleApplication.class}
)
@AutoConfigureMockMvc
public class UsersControllerIntegrationTests extends BasicControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsersController usersController;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

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
    private UserCreateData mockUserBCreateData = new UserCreateData("TestB", "t", "$TestB123");
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

    private ObjectMapper objectMapper = new ObjectMapper();

    private Long insertUserToDatabaseAndGetId(UserCreateData userCreateData) {
        User user = new User(userCreateData);
        userRepository.save(user);

        return new UserDetailData(user).id();
    }

    @AfterEach
    void clearDatabase() {
        userRepository.deleteAll();
    }

    @Test
    void shouldSuccessfullyFetchSingleUser() throws Exception {
        Long userId = insertUserToDatabaseAndGetId(this.mockUserACreateData);
        UserDetailData mockUserDetailData = new UserDetailData(this.mockUserA);

        this.mockMvc.perform(get(String.format("/users/%s", userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(mockUserDetailData.name()))
                .andExpect(jsonPath("$.isActive").value(true))
                .andExpect(jsonPath("$.email").value(mockUserDetailData.email()));
    }

    @Test
    void shouldThrowNotFoundErrorWhenUserDoesNotExist() throws Exception {
        this.mockMvc.perform(get("/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldNotSaveNewUserWhenNameIsInvalid() throws Exception {
        List<String> mockInvalidNames = new ArrayList<>();

        mockInvalidNames.add("e");
        mockInvalidNames.add("a".repeat(41));

        this.testRequestWithValidationFieldError(
                "/users",
                "name",
                this.mandatoryNameErrorMessage,
                "POST",
                objectMapper.writeValueAsString(
                        new UserCreateData(
                                null,
                                "testuser@gmail.com",
                                "$Aa123456"
                        )
                )
        );

        for (String invalidName : mockInvalidNames) {
            this.testRequestWithValidationFieldError(
                    "/users",
                    "name",
                    this.nameSizeErrorMessage,
                    "POST",
                    objectMapper.writeValueAsString(
                            new UserCreateData(
                                    invalidName,
                                    "testuser@gmail.com",
                                    "$Aa123456"
                            )
                    )
            );
        }
    }

    @Test
    void shouldNotSaveNewUserWhenEmailIsInvalid() throws Exception {
        List<String> mockInvalidEmails = new ArrayList<>();

        mockInvalidEmails.add(String.format("%s@gmail.com", "a".repeat(400)));
        mockInvalidEmails.add("testemail.com");

        this.testRequestWithValidationFieldError(
                "/users",
                "email",
                this.mandatoryEmailErrorMessage,
                "POST",
                objectMapper.writeValueAsString(
                        new UserCreateData(
                                "Test User",
                                null,
                                "$Aa123456"
                        )
                )
        );

        for (String invalidEmail : mockInvalidEmails) {
            this.testRequestWithValidationFieldError(
                    "/users",
                    "email",
                    this.emailFormatErrorMessage,
                    "POST",
                    objectMapper.writeValueAsString(
                            new UserCreateData(
                                    "Test User",
                                    invalidEmail,
                                    "$Aa123456"
                            )
                    )
            );
        }
    }

    @Test
    void shouldNotSaveNewUserWhenPasswordIsInvalid() throws Exception {
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
                objectMapper.writeValueAsString(
                        new UserCreateData(
                                "Test User",
                                "testuser@gmail.com",
                                null
                        )
                )
        );

        for (String invalidPassword : mockInvalidPasswords) {
            this.testRequestWithValidationFieldError(
                    "/users",
                    "password",
                    this.passwordPatternErrorMessage,
                    "POST",
                    objectMapper.writeValueAsString(
                            new UserCreateData(
                                    "Test User",
                                    "testuser@gmail.com",
                                    invalidPassword
                            )
                    )
            );
        }
    }

    @Test
    void shouldNotUpdateUserWhenNameIsInvalid() throws Exception {
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
                    objectMapper.writeValueAsString(
                            new UserUpdateData(invalidName, null, null)
                    )
            );
        }
    }

    @Test
    void shouldNotUpdateUserWhenEmailIsInvalid() throws Exception {
        List<String> mockInvalidEmails = new ArrayList<>();

        mockInvalidEmails.add(String.format("%s@gmail.com", "a".repeat(400)));
        mockInvalidEmails.add("testemail.com");

        for (String invalidEmail : mockInvalidEmails) {
            this.testRequestWithValidationFieldError(
                    String.format("/users/%s", 1),
                    "email",
                    "Email must be in the proper format. Make sure you included @.",
                    "PUT",
                    objectMapper.writeValueAsString(
                            new UserUpdateData(null, invalidEmail, null)
                    )
            );
        }
    }

    @Test
    void shouldNotUpdateUserWhenPasswordIsInvalid() throws Exception {
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
                    objectMapper.writeValueAsString(
                            new UserUpdateData(null, null, invalidPassword)
                    )
            );
        }
    }

}
