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

import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(UsersController.class)
@AutoConfigureJsonTesters
class UsersControllerTest extends BasicControllerTest {
    @MockBean
    UserService userService;

    @Autowired
    private UsersController usersController;

    @Autowired
    private JacksonTester<UserDetailData> userResponseTester;

    @Autowired
    private JacksonTester<UserActivateAndDeactivatieData> userActivateAndDeactivateTester;

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

    @Test
    void shouldSuccessfullyRunDetailEndpoint() {
        Long mockId = (long) 1;
        UserDetailData mockUserDetailData = new UserDetailData(this.mockUserA);

        when(this.userService.detail(mockId)).thenReturn(mockUserDetailData);

        UserDetailData userDetailData = usersController.detail(mockId);
        assertEquals(userDetailData.name(), mockUserDetailData.name());
        assertEquals(mockUserDetailData.isActive(), userDetailData.isActive());
        assertEquals(mockUserDetailData.email(), userDetailData.email());
    }

    @Test
    void shouldThrowNotFoundErrorForDetailEndpoint() throws Exception {
        Long mockId = (long) 1;
        when(this.userService.detail(mockId))
                .thenThrow(new NotFoundException(Constants.USER_NOT_FOUND_MESSAGE));

        assertThrows(NotFoundException.class, () -> usersController.detail(mockId));
    }

    @Test
    void shouldSuccessfullyListMockUsers() {
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
    }

    @Test
    void shouldSuccessfullyRunCreateEndpointForMockUser() throws Exception {
        UserDetailData mockUserADetailData = new UserDetailData(this.mockUserA);
        when(userService.create(this.mockUserACreateData)).thenReturn(mockUserADetailData);
        var expectedJson = this.userResponseTester.write(mockUserADetailData).getJson();

        UserDetailData userDetailData = usersController.create(this.mockUserACreateData);
        var receivedJson = this.userResponseTester.write(userDetailData).getJson();

        assertEquals(expectedJson, receivedJson);
    }

    @Test
    void shouldSuccessfullyRunUpdateEndpointForMockUser() throws Exception {
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
        }
    }

    @Test
    void shouldSuccessfullyRunDeleteEndpointForMockUser() {
        Long mockId = (long) 1;

        doNothing().when(this.userService).delete(mockId);
        this.usersController.delete(mockId);
        verify(this.userService, times(1)).delete(mockId);
    }

    @Test
    void shouldThrowNotFoundErrorForDeleteEndpoint() {
        Long mockId = (long) 1;

        doThrow(NotFoundException.class).when(this.userService).delete(mockId);
        assertThrows(NotFoundException.class, () -> {
            this.usersController.delete(mockId);
        });
    }

    @Test
    void shouldSuccessfullyRunActivateEndpointForMockUser() throws IOException {
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
    void shouldThrowNotFoundErrorForActivateAndDeactivateEndpoints() {
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