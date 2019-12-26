package com.webapp.systemtest; // Generated by Selenium IDE

import com.webapp.config.MvcConfig;
import com.webapp.model.Building;
import com.webapp.model.user.Admin;
import com.webapp.model.user.User;
import com.webapp.service.database.dao.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MvcConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebAppConfiguration
public class AdminST {

    JavascriptExecutor js;
    private WebDriver driver;

    @Autowired
    private RecordDao recordDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private BuildingDao buildingDao;

    @Autowired
    private NewsDao newsDao;

    @Autowired
    private CommentDao commentDao;

    private final String adminName = "admin1";
    private final String adminPassword = "passwordpassword1";

    private Admin admin;
    private User user;
    private Building building;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
        User user1 = new User("usernameusername1", "passwordpassword1", "name1", "MALE", "13000000000");
        Admin admin1 = new Admin("admin1", "passwordpassword1", "name1", "MALE", "13000000000");
        this.userDao.addUser(user1);
        this.userDao.addUser(admin1);
        for (User user : this.userDao.queryAllUsers()) {
            if (user instanceof Admin) {
                this.admin = (Admin) user;
            } else {
                this.user = user;
            }
        }
        Building building1 = new Building("buildingname1", "buildingdescription1", "400");
        this.buildingDao.addBuilding(building1);
        for (Building building : this.buildingDao.listBuilding(20)) {
            this.building = building;
        }
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        for (User user : this.userDao.queryAllUsers()) {
            this.userDao.deleteUser(user.getId());
        }
        for (Building building : this.buildingDao.listBuilding(20)) {
            this.buildingDao.deleteBuilding(building.getId());
        }
    }

    private void adminLogin() {
        // Step # | name | target | value | comment
        // 1 | open | http://localhost:8080/MeetHere_war/ |  |
        driver.get("http://localhost:8080/MeetHere_war/");
        // 2 | setWindowSize | 1052x554 |  |
        driver.manage().window().setSize(new Dimension(1052, 554));
        // 3 | click | id=username |  |
        driver.findElement(By.id("username")).click();
        // 4 | type | id=username | root |
        driver.findElement(By.id("username")).sendKeys(adminName);
        // 5 | type | id=password | root |
        driver.findElement(By.id("password")).sendKeys(adminPassword);
        // 6 | click | css=.btn:nth-child(1) |  |
        driver.findElement(By.cssSelector(".btn:nth-child(1)")).click();
    }

    @Test
    @Order(1)
    public void deleteUser() throws Throwable {
        adminLogin();
        // Test name: DeleteUser
        // Step # | name | target | value | comment
        // 1 | open | http://localhost:8080/MeetHere_war/user?action=list |  |
        driver.get("http://localhost:8080/MeetHere_war/user?action=list");
        // 2 | setWindowSize | 1052x554 |  |
        driver.manage().window().setSize(new Dimension(1052, 554));
        // 3 | chooseOkOnNextConfirmation |  |  |
        // 4 | click | css=tr:nth-child(1) .btn-danger |  |
        driver.findElement(By.id("#id_delete_button_" + this.user.getId())).click();
        // 5 | assertConfirmation | 您确定要删除这个用户吗？ |  |
        assertEquals("您确定要删除这个用户吗？", driver.switchTo().alert().getText());
        // 6 | webdriverChooseOkOnVisibleConfirmation |  |  |
        driver.switchTo().alert().accept();
        TimeUnit.MILLISECONDS.sleep(200);
        User deletedUser = this.userDao.queryUserById(this.user.getId());
        assertNull(deletedUser);
    }

    static Stream<Arguments> newUserProvider() {
        return Stream.of(
                Arguments.of("newusername", "newpassword", "newname", "MALE", "newtel"),
                Arguments.of("newusername", "newpassword", "newname", "FEMALE", "newtel")
        );
    }
    @ParameterizedTest
    @MethodSource("newUserProvider")
    @Order(2)
    public void modifyUser(String username, String password, String name, String sex, String tel) {
        adminLogin();
        // Test name: ModifyUser
        // Step # | name | target | value | comment
        // 1 | open | http://localhost:8080/MeetHere_war/user?action=list |  |
        driver.get("http://localhost:8080/MeetHere_war/user?action=list");
        // 2 | setWindowSize | 1052x554 |  |
        driver.manage().window().setSize(new Dimension(1052, 554));
        // 3 | click | css=tr:nth-child(1) .btn-info |  |
        driver.findElement(By.id("#id_modify_button_"+this.user.getId())).click();
        // 4 | click | css=.row-fluid |  |
        driver.findElement(By.cssSelector(".row-fluid")).click();
        // 5 | type | id=username | 123 |
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys(username);
        // 6 | click | css=.data_form |  |
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("rPassword")).clear();
        driver.findElement(By.id("rPassword")).sendKeys(password);
        // 9 | click | css=.data_form |  |
        // 10 | type | id=name | 123 |
        driver.findElement(By.id("name")).clear();
        driver.findElement(By.id("name")).sendKeys(name);
        // 11 | click | id=sex |  |
        driver.findElement(By.id("sex")).click();
        // 12 | select | id=sex | label=女 |
        if ("FEMALE".equals(sex)) {
            WebElement dropdown = driver.findElement(By.id("sex"));
            dropdown.findElement(By.xpath("//option[. = '女']")).click();
        } else if ("MALE".equals(sex)) {
            WebElement dropdown = driver.findElement(By.id("sex"));
            dropdown.findElement(By.xpath("//option[. = '男']")).click();
        }
        // 13 | click | id=sex |  |
        driver.findElement(By.id("sex")).click();
        // 14 | click | id=tel |  |
        driver.findElement(By.id("tel")).clear();
        // 15 | type | id=tel | 123 |
        driver.findElement(By.id("tel")).sendKeys(tel);
        // 16 | click | css=.btn:nth-child(1) |  |
        driver.findElement(By.cssSelector(".btn:nth-child(1)")).click();
        User modifiedUser = this.userDao.queryUserById(this.user.getId());
        assertAll(
                () -> assertEquals(username, modifiedUser.getUsername()),
                () -> assertEquals(password, modifiedUser.getPassword()),
                () -> assertEquals(name, modifiedUser.getName()),
                () -> assertEquals(sex, modifiedUser.getSex().toString()),
                () -> assertEquals(tel, modifiedUser.getTel())
        );
    }

    @Test
    @Order(3)
    public void homePage() {
        adminLogin();
        // Test name: HomePage
        // Step # | name | target | value | comment
        // 1 | open | http://localhost:8080/MeetHere_war/blank |  |
        driver.get("http://localhost:8080/MeetHere_war/blank");
        // 2 | setWindowSize | 1052x554 |  |
        driver.manage().window().setSize(new Dimension(1052, 554));
        // 3 | click | linkText=首页 |  |
        driver.findElement(By.linkText("首页")).click();
        assertEquals("欢迎您,系统管理员!", driver.findElement(By.id("welcome")).getText());
    }

    static Stream<Arguments> buildingProvider() {
        return Stream.of(
                Arguments.of("addbuildingname1", "300", "addbuildingdescription1"),
                Arguments.of("addbuildingname1", "500", "addbuildingdescription2")
        );
    }

    @ParameterizedTest
    @MethodSource("buildingProvider")
    @Order(4)
    public void addBuilding(String buildingName, String buildingPrice, String buildingDescription) {
        adminLogin();
        // Test name: AddBuilding
        // Step # | name | target | value | comment
        // 1 | open | http://localhost:8080/MeetHere_war/building?action=list |  |
        driver.get("http://localhost:8080/MeetHere_war/building?action=list");
        // 2 | setWindowSize | 1052x554 |  |
        driver.manage().window().setSize(new Dimension(1920, 1080));
        // 3 | click | css=.btn |  |
        driver.findElement(By.id("#id_add_button")).click();
        // 4 | click | id=buildingName |  |
        driver.findElement(By.id("buildingName")).click();
        // 5 | type | id=buildingName | 123 |
        driver.findElement(By.id("buildingName")).sendKeys(buildingName);
        // 6 | type | id=buildingPrice | 123 |
        driver.findElement(By.id("buildingPrice")).sendKeys(buildingPrice);
        // 7 | type | id=buildingDescription | 123 |
        driver.findElement(By.id("buildingDescription")).sendKeys(buildingDescription);
        // 8 | click | css=.btn:nth-child(1) |  |
        driver.findElement(By.cssSelector(".btn:nth-child(1)")).click();
        List<Building> queryList = this.buildingDao.listBuilding(10);
        Building addedBuilding = queryList.get(0);
        assertAll(
                () -> assertEquals(buildingName, addedBuilding.getName()),
                () -> assertEquals(buildingPrice, addedBuilding.getPrice()),
                () -> assertEquals(buildingDescription, addedBuilding.getDescription())
        );
    }

    @Test
    @Order(5)
    public void deleteBuilding() throws Throwable {
        adminLogin();
        // Test name: DeleteBuilding
        // Step # | name | target | value | comment
        // 1 | open | http://localhost:8080/MeetHere_war/building?action=list |  |
        driver.get("http://localhost:8080/MeetHere_war/building?action=list");
        // 2 | setWindowSize | 1052x554 |  |
        driver.manage().window().setSize(new Dimension(1052, 554));
        // 3 | chooseOkOnNextConfirmation |  |  |
        // 4 | click | css=.btn-danger |  |
        driver.findElement(By.id("#id_delete_button_"+this.building.getId())).click();
        // 5 | assertConfirmation | 您确定要删除这个楼吗？ |  |
        assertEquals("您确定要删除这个楼吗？", driver.switchTo().alert().getText());
        // 6 | webdriverChooseOkOnVisibleConfirmation |  |  |
        driver.switchTo().alert().accept();
        TimeUnit.MILLISECONDS.sleep(200);
        Building deletedBuilding = this.buildingDao.queryBuildingById(this.building.getId());
        assertNull(deletedBuilding);
    }

    static Stream<Arguments> newBuildingProvider() {
        return Stream.of(
                Arguments.of("newbuildingname1", "300", "newbuildingdescription1"),
                Arguments.of("newbuildingname1", "500", "newbuildingdescription2")
        );
    }

    @ParameterizedTest
    @MethodSource("newBuildingProvider")
    @Order(6)
    public void modifyBuilding(String newBuildingName, String newBuildingPrice, String newBuildingDescription) {
        adminLogin();
        // Test name: ModifyBuilding
        // Step # | name | target | value | comment
        // 1 | open | http://localhost:8080/MeetHere_war/building?action=list |  |
        driver.get("http://localhost:8080/MeetHere_war/building?action=list");
        // 2 | setWindowSize | 1052x554 |  |
        driver.manage().window().setSize(new Dimension(1052, 554));
        // 3 | click | css=td > .btn-info |  |
        driver.findElement(By.id("#id_modify_button_"+this.building.getId())).click();
        // 5 | type | id=buildingName | 456 |
        driver.findElement(By.id("buildingName")).clear();
        driver.findElement(By.id("buildingName")).sendKeys(newBuildingName);
        // 6 | type | id=buildingPrice | 456 |
        driver.findElement(By.id("buildingPrice")).clear();
        driver.findElement(By.id("buildingPrice")).sendKeys(newBuildingPrice);
        // 7 | type | id=buildingDescription | 456 |
        driver.findElement(By.id("buildingDescription")).clear();
        driver.findElement(By.id("buildingDescription")).sendKeys(newBuildingDescription);
        // 8 | click | css=.btn:nth-child(1) |  |
        driver.findElement(By.cssSelector(".btn:nth-child(1)")).click();
        List<Building> queryList = this.buildingDao.listBuilding(10);
        Building addedBuilding = queryList.get(0);
        assertAll(
                () -> assertEquals(newBuildingName, addedBuilding.getName()),
                () -> assertEquals(newBuildingPrice, addedBuilding.getPrice()),
                () -> assertEquals(newBuildingDescription, addedBuilding.getDescription())
        );
    }

    @Test
    @Order(7)
    public void clickMenu() {
        adminLogin();
        // Test name: ClickMenu
        // Step # | name | target | value | comment
        // 1 | open | http://localhost:8080/MeetHere_war/blank |  |
        driver.get("http://localhost:8080/MeetHere_war/blank");
        // 2 | setWindowSize | 1052x554 |  |
        driver.manage().window().setSize(new Dimension(1052, 554));
        // 3 | click | linkText=首页 |  |
        driver.findElement(By.linkText("首页")).click();
        assertEquals("欢迎您,系统管理员!", driver.findElement(By.id("welcome")).getText());
        // 4 | click | linkText=用户管理 |  |
        driver.findElement(By.linkText("用户管理")).click();
        assertEquals("用户管理", driver.findElement(By.id("#id_title")).getText());
        // 5 | click | linkText=新闻管理 |  |
        driver.findElement(By.linkText("新闻管理")).click();
        assertEquals("新闻管理", driver.findElement(By.id("#id_title")).getText());
        // 6 | click | linkText=留言管理 |  |
        driver.findElement(By.linkText("留言管理")).click();
        assertEquals("已审核留言", driver.findElement(By.id("#id_title")).getText());
        // 7 | click | linkText=场地管理 |  |
        driver.findElement(By.linkText("场地管理")).click();
        assertEquals("场地管理", driver.findElement(By.id("#id_title")).getText());
        // 8 | click | linkText=预约管理 |  |
        driver.findElement(By.linkText("预约管理")).click();
        assertEquals("已审核记录", driver.findElement(By.id("#id_title")).getText());
        // 9 | click | linkText=修改密码 |  |
        driver.findElement(By.linkText("修改密码")).click();
        assertEquals("修改密码", driver.findElement(By.id("#id_title")).getText());
        // 10 | click | linkText=退出系统 |  |
        driver.findElement(By.linkText("退出系统")).click();
        assertEquals("注册", driver.findElement(By.id("#id_signup")).getText());
    }

    @Disabled
    @Test
    public void modifyNews() {
        // Test name: ModifyNews
        // Step # | name | target | value | comment
        // 1 | open | http://localhost:8080/MeetHere_war/news?action=list |  |
        driver.get("http://localhost:8080/MeetHere_war/news?action=list");
        // 2 | setWindowSize | 1052x554 |  |
        driver.manage().window().setSize(new Dimension(1052, 554));
        // 3 | click | css=td > .btn-info |  |
        driver.findElement(By.cssSelector("td > .btn-info")).click();
        // 4 | click | css=.data_form |  |
        driver.findElement(By.cssSelector(".data_form")).click();
        // 5 | type | id=title | 456 |
        driver.findElement(By.id("title")).sendKeys("456");
        // 6 | type | id=author | 456 |
        driver.findElement(By.id("author")).sendKeys("456");
        // 7 | type | id=detail | 456 |
        driver.findElement(By.id("detail")).sendKeys("456");
        // 8 | click | css=.btn:nth-child(1) |  |
        driver.findElement(By.cssSelector(".btn:nth-child(1)")).click();
    }

    @Disabled
    @Test
    //TODO
    public void deleteNews() {
        // Test name: DeleteNews
        // Step # | name | target | value | comment
        // 1 | open | http://localhost:8080/MeetHere_war/news?action=list |  |
        driver.get("http://localhost:8080/MeetHere_war/news?action=list");
        // 2 | setWindowSize | 1052x554 |  |
        driver.manage().window().setSize(new Dimension(1052, 554));
        // 3 | chooseOkOnNextConfirmation |  |  |
        // 4 | click | css=.btn-danger |  |
        driver.findElement(By.cssSelector(".btn-danger")).click();
        // 5 | assertConfirmation | 您确定要删除这条新闻吗？ |  |
        assertEquals("您确定要删除这条新闻吗？", driver.switchTo().alert().getText());
        // 6 | webdriverChooseOkOnVisibleConfirmation |  |  |
        driver.switchTo().alert().accept();
    }

    @Disabled
    @Test
    //TODO
    public void createNews() {
        // Test name: CreateNews
        // Step # | name | target | value | comment
        // 1 | open | http://localhost:8080/MeetHere_war/news?action=list |  |
        driver.get("http://localhost:8080/MeetHere_war/news?action=list");
        // 2 | setWindowSize | 1052x554 |  |
        driver.manage().window().setSize(new Dimension(1052, 554));
        // 3 | click | css=.btn |  |
        driver.findElement(By.cssSelector(".btn")).click();
        // 4 | click | id=title |  |
        driver.findElement(By.id("title")).click();
        // 5 | type | id=title | 123 |
        driver.findElement(By.id("title")).sendKeys("123");
        // 6 | type | id=author | 123 |
        driver.findElement(By.id("author")).sendKeys("123");
        // 7 | click | id=detail |  |
        driver.findElement(By.id("detail")).click();
        // 8 | type | id=detail | 123 |
        driver.findElement(By.id("detail")).sendKeys("123");
        // 9 | click | css=.btn:nth-child(1) |  |
        driver.findElement(By.cssSelector(".btn:nth-child(1)")).click();
    }

    @Disabled
    @Test
    //TODO
    public void deleteUnverifiedComment() {
        // Test name: DeleteUnverifiedComment
        // Step # | name | target | value | comment
        // 1 | open | http://localhost:8080/MeetHere_war/comment?action=list |  |
        driver.get("http://localhost:8080/MeetHere_war/comment?action=list");
        // 2 | setWindowSize | 1052x554 |  |
        driver.manage().window().setSize(new Dimension(1052, 554));
        // 3 | chooseOkOnNextConfirmation |  |  |
        // 4 | click | css=.btn:nth-child(2) |  |
        driver.findElement(By.cssSelector(".btn:nth-child(2)")).click();
        // 5 | assertConfirmation | 您确定要删除这条留言吗？ |  |
        assertEquals("您确定要删除这条留言吗？", driver.switchTo().alert().getText());
        // 6 | webdriverChooseOkOnVisibleConfirmation |  |  |
        driver.switchTo().alert().accept();
    }

    @Disabled
    @Test
    //TODO
    public void deleteVerifiedComment() {
        // Test name: DeleteVerifiedComment
        // Step # | name | target | value | comment
        // 1 | open | http://localhost:8080/MeetHere_war/comment?action=list |  |
        driver.get("http://localhost:8080/MeetHere_war/comment?action=list");
        // 2 | setWindowSize | 1052x554 |  |
        driver.manage().window().setSize(new Dimension(1052, 554));
        // 3 | chooseOkOnNextConfirmation |  |  |
        // 4 | click | css=div:nth-child(2) > .table .btn |  |
        driver.findElement(By.cssSelector("div:nth-child(2) > .table .btn")).click();
        // 5 | assertConfirmation | 您确定要删除这条留言吗？ |  |
        assertEquals("您确定要删除这条留言吗？", driver.switchTo().alert().getText());
        // 6 | webdriverChooseOkOnVisibleConfirmation |  |  |
        driver.switchTo().alert().accept();
    }

    @Disabled
    @Test
    //TODO
    public void verifyComment() {
        // Test name: VerifyComment
        // Step # | name | target | value | comment
        // 1 | open | http://localhost:8080/MeetHere_war/comment?action=list |  |
        driver.get("http://localhost:8080/MeetHere_war/comment?action=list");
        // 2 | setWindowSize | 1052x554 |  |
        driver.manage().window().setSize(new Dimension(1052, 554));
        // 3 | chooseOkOnNextConfirmation |  |  |
        // 4 | click | css=tr:nth-child(1) .btn:nth-child(1) |  |
        driver.findElement(By.cssSelector("tr:nth-child(1) .btn:nth-child(1)")).click();
        // 5 | assertConfirmation | 您确定要通过这条留言吗？ |  |
        assertEquals("您确定要通过这条留言吗？", driver.switchTo().alert().getText());
        // 6 | webdriverChooseOkOnVisibleConfirmation |  |  |
        driver.switchTo().alert().accept();
    }

    @Disabled
    @Test
    //TODO
    public void deleteUnverifiedRecord() {
        // Test name: DeleteUnverifiedRecord
        // Step # | name | target | value | comment
        // 1 | open | http://localhost:8080/MeetHere_war/record?action=list |  |
        driver.get("http://localhost:8080/MeetHere_war/record?action=list");
        // 2 | setWindowSize | 1052x554 |  |
        driver.manage().window().setSize(new Dimension(1052, 554));
        // 3 | chooseOkOnNextConfirmation |  |  |
        // 4 | click | css=.btn:nth-child(2) |  |
        driver.findElement(By.cssSelector(".btn:nth-child(2)")).click();
        // 5 | assertConfirmation | 您确定要删除这条记录吗？ |  |
        assertEquals("您确定要删除这条记录吗？", driver.switchTo().alert().getText());
        // 6 | webdriverChooseOkOnVisibleConfirmation |  |  |
        driver.switchTo().alert().accept();
    }

    @Disabled
    @Test
    //TODO
    public void verifyRecord() {
        // Test name: VerifyRecord
        // Step # | name | target | value | comment
        // 1 | open | http://localhost:8080/MeetHere_war/record?action=list |  |
        driver.get("http://localhost:8080/MeetHere_war/record?action=list");
        // 2 | setWindowSize | 1052x554 |  |
        driver.manage().window().setSize(new Dimension(1052, 554));
        // 3 | chooseOkOnNextConfirmation |  |  |
        // 4 | click | css=tr:nth-child(1) .btn:nth-child(1) |  |
        driver.findElement(By.cssSelector("tr:nth-child(1) .btn:nth-child(1)")).click();
        // 5 | assertConfirmation | 您确定审核通过这条记录吗？ |  |
        assertEquals("您确定审核通过这条记录吗？", driver.switchTo().alert().getText());
        // 6 | webdriverChooseOkOnVisibleConfirmation |  |  |
        driver.switchTo().alert().accept();
    }

    @Disabled
    @Test
    //TODO
    public void changePassword() {
        // Test name: ChangePassword
        // Step # | name | target | value | comment
        // 1 | open | http://localhost:8080/MeetHere_war/password?action=change |  |
        driver.get("http://localhost:8080/MeetHere_war/password?action=change");
        // 2 | setWindowSize | 1052x554 |  |
        driver.manage().window().setSize(new Dimension(1052, 554));
        // 3 | click | id=oldPassword |  |
        driver.findElement(By.id("oldPassword")).click();
        // 4 | type | id=oldPassword | root |
        driver.findElement(By.id("oldPassword")).sendKeys("root");
        // 5 | type | id=newPassword | root |
        driver.findElement(By.id("newPassword")).sendKeys("root");
        // 6 | type | id=rPassword | root |
        driver.findElement(By.id("rPassword")).sendKeys("root");
        // 7 | sendKeys | id=rPassword | ${KEY_ENTER} |
        driver.findElement(By.id("rPassword")).sendKeys(Keys.ENTER);
    }


}
