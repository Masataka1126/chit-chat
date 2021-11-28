package org.noname.onestep.chitchat.controller;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.hamcrest.beans.HasProperty;
import static org.hamcrest.CoreMatchers.is;
import org.junit.jupiter.api.Test;
import org.noname.onestep.chitchat.Application;
import org.noname.onestep.chitchat.annotation.WithMockCustomUser;
import org.noname.onestep.chitchat.common.CsvDataSetLoader;
import org.noname.onestep.chitchat.domain.entity.User;
import org.noname.onestep.chitchat.domain.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = Application.class)
@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class,databaseConnection = "databaseConnection")
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class
})
public class UserControllerTest {

    private final MockMvc mockMvc;

    private final UserService userService;

    @Autowired
    public UserControllerTest(MockMvc mockMvc,UserService userService) {
        this.mockMvc = mockMvc;
        this.userService = userService;
    }

    // ユーザー登録用ページにGETリクエストを送信した場合、ステータス「200」が返ってくること
    @Test
    public void userCreatePage_getRequest_returnHTTPStatus200() throws Exception {

        this.mockMvc.perform(get("/users/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("/view/user"));
    }

    // DBに存在するメールアドレスで登録処理を行った場合、登録が失敗すること
    @Test
    @DatabaseSetup(value = "/db/testdata/setup/controller/user/create/")
    @Transactional
    public void userCreatePage_postRequest_withRegisteredUser_failRegistration() throws Exception {

        this.mockMvc.perform(post("/users/create")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("emailAddress","testdata@example.com")
                        .param("password","password")
                        .param("name","無言の宇宙"))
                .andExpect(model().attribute("message","すでに登録されているメールアドレスです。"))
                .andExpect(view().name("/view/user"));
    }

    // ユーザー登録用リクエストを送信した場合、ユーザー登録が成功すること
    // ※DBに重複データが存在しない場合
    @Test
    @DatabaseSetup(value = "/db/testdata/setup/controller/user/create/")
    @ExpectedDatabase(value = "/db/testdata/expecteddata/controller/user/create/"
    ,assertionMode = DatabaseAssertionMode.NON_STRICT)
    @Transactional
    public void userCreatePage_postRequest_succeedRegistration() throws Exception {

        this.mockMvc.perform(post("/users/create")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("emailAddress","mydata@example.com")
                .param("password","password")
                .param("name","宇宙人"))
                .andExpect(model().attribute("message","ユーザー登録が完了しました。"))
                .andExpect(view().name("/view/login"));
    }

    // 更新用画面にリクエストした場合に、ユーザー画面が返ってくること
    @Test
    @WithMockCustomUser(emailAddress = "testdata@example.com",password = "password")
    public void userUpdatePage_getRequest_returnHTTPStatus200() throws Exception {
        this.mockMvc.perform(get("/users/update"))
                .andExpect(view().name("/view/user"));
    }

    // 更新用画面にリクエストした場合に、ログインユーザーのメールアドレスから取得した
    // モデルデータが格納されていること
    @Test
    @DatabaseSetup(value = "/db/testdata/setup/controller/user/update/")
    @WithMockCustomUser(emailAddress = "testdata@example.com",password = "password")
    @Transactional
    public void userUpdatePage_getRequest_containsCorrectUser() throws Exception {
        this.mockMvc.perform(get("/users/update"))
                .andDo(print())
                .andExpect(model().attribute("user", hasProperty(
                        "emailAddress", is("testdata@example.com")
                )))
                .andExpect(model().attribute("user", hasProperty(
                        "name", is("一般人")
                )));
    }

    // すでにデータが存在しているユーザー情報にPostリクエストを送信した場合、
    // 既存データが更新されること
    @Test
    @DatabaseSetup(value = "/db/testdata/setup/controller/user/update/")
    @ExpectedDatabase(value = "/db/testdata/expecteddata/controller/user/update/"
            ,assertionMode = DatabaseAssertionMode.NON_STRICT)
    @Transactional
    public void userCreatePage_postRequest_succeedUpdate() throws Exception {

        this.mockMvc.perform(post("/users/create")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id","1")
                        .param("emailAddress","testdata@example.com")
                        .param("password","password")
                        .param("name","芸能人"))
                .andExpect(model().attribute("message","ユーザー登録が完了しました。"))
                .andExpect(view().name("/view/login"));

        userService.findUser("testdata@example.com");

    }

}
