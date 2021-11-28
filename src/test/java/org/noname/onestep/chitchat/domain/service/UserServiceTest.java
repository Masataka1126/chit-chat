package org.noname.onestep.chitchat.domain.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.junit.jupiter.api.Test;
import org.noname.onestep.chitchat.Application;
import org.noname.onestep.chitchat.common.CsvDataSetLoader;
import org.noname.onestep.chitchat.domain.entity.User;
import org.noname.onestep.chitchat.domain.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DbUnitConfiguration(dataSetLoader = CsvDataSetLoader.class,databaseConnection = {"databaseConnection"})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class UserServiceTest {

    private final UserService userService;

    @Autowired
    public UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    // 対象のメールアドレスがすでに登録されている場合、trueを返す。
    @Test
    @DatabaseSetup(value = "/db/testdata/setup/domain/service/users/")
    public void userService_userExists_returnTrue() {

        String mailAddress = "sakura@example.com";
        assertThat(this.userService.userExits(mailAddress))
                .isTrue();
    }

    // 対象のユーザが保存されるか
    @Test
    @DatabaseSetup(value = "/db/testdata/setup/domain/service/users/")
    @ExpectedDatabase(value = "/db/testdata/expecteddata/domain/service/users/create/",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void userService_saveUser_savedUser() {

        User user = new User();
        user.setName("ガルル");
        user.setEmailAddress("nogi@example.com");
        user.setPassword("password");

        userService.save(user);
    }

    @Test
    @DatabaseSetup(value = "/db/testdata/setup/domain/service/users/")
    @ExpectedDatabase(value = "/db/testdata/expecteddata/domain/service/users/update/",
            assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void userService_updateUser_updatedUser() {

        User user = userService.findUser("sakura@example.com");
        user.setName("流れ弾");

        userService.save(user);
        userService.findUser(user.getEmailAddress());
    }


}
