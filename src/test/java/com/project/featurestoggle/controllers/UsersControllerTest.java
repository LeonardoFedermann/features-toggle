package com.project.featurestoggle.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.featurestoggle.dtos.UserCreateData;
import com.project.featurestoggle.dtos.UserDetailData;
import com.project.featurestoggle.dtos.UserListData;
import com.project.featurestoggle.entities.User;
import com.project.featurestoggle.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.data.domain.Page;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(UsersController.class)
class UsersControllerTest {
    @MockBean
    UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();
    private User mockUser0;
    private User mockUser1;
    private User mockUser2;
    private User mockUser3;

    @BeforeEach
    void setMockUsers() {
        UserCreateData mockUserCreateData0 = new UserCreateData("Thor", "thor@gmail.com", "$Thor123");
        UserCreateData mockUserCreateData1 = new UserCreateData("Odin", "odin@gmail.com", "$Odin123");
        UserCreateData mockUserCreateData2 = new UserCreateData("Baldur", "baldur@gmail.com", "$Baldur123");
        UserCreateData mockUserCreateData3 = new UserCreateData("Frey", "frey@gmail.com", "$Frey123");

        this.mockUser0 = new User(mockUserCreateData0);
        this.mockUser1 = new User(mockUserCreateData1);
        this.mockUser2 = new User(mockUserCreateData2);
        this.mockUser3 = new User(mockUserCreateData3);
    }

    @Test
    void detailTest() throws Exception {
        Long mockId = (long) 1;
        UserDetailData mockUserDetailData = new UserDetailData(this.mockUser0);

        when(userService.detail(mockId)).thenReturn(mockUserDetailData);

        this.mockMvc.perform(get(String.format("/users/%s", mockId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(mockUserDetailData.name()))
                .andExpect(jsonPath("$.isActive").value(true))
                .andExpect(jsonPath("$.email").value(mockUserDetailData.email()));
    }

    @Test
    void testDefaultListRequest() throws Exception {
        List<UserListData> mockUsersList = new ArrayList<>();

        mockUsersList.add(new UserListData(this.mockUser0));
        mockUsersList.add(new UserListData(this.mockUser1));
        mockUsersList.add(new UserListData(this.mockUser2));
        mockUsersList.add(new UserListData(this.mockUser3));

        Page<UserListData> mockUsersPage = new PageImpl<>(mockUsersList);

        when(userService.list(any(Pageable.class))).thenReturn(mockUsersPage);

        this.mockMvc.perform(get("/users"))
                // RETURNING STATUS 500 WHEN IT SHOULD BE 200
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[1].name").value(this.mockUser1.getName()))
                .andExpect(jsonPath("$.content[3].email").value(this.mockUser3.getEmail()))
                .andExpect(jsonPath("$.content[2].isActive").value(true));
    }

//    @Test
//    void create() {
//    }
//
//    @Test
//    void update() {
//    }
//
//    @Test
//    void delete() {
//    }
//
//    @Test
//    void activate() {
//    }
//
//    @Test
//    void deactivate() {
//    }
}