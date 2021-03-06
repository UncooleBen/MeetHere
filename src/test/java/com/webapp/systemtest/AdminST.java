package com.webapp.systemtest; // Generated by Selenium IDE

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.webapp.config.MvcConfig;
import com.webapp.model.Building;
import com.webapp.model.Comment;
import com.webapp.model.News;
import com.webapp.model.Record;
import com.webapp.model.user.Admin;
import com.webapp.model.user.User;
import com.webapp.service.database.dao.BuildingDao;
import com.webapp.service.database.dao.CommentDao;
import com.webapp.service.database.dao.NewsDao;
import com.webapp.service.database.dao.RecordDao;
import com.webapp.service.database.dao.UserDao;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MvcConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebAppConfiguration
public class AdminST {

  private static final String adminName = "admin1";
  private static final String adminPassword = "passwordpassword1";
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
  private Admin admin;
  private User user;
  private Building building;
  private Set<Integer> addedNewsIdSet;
  private Set<Integer> originNewsIdSet;
  private Set<Integer> addedUnverifiedCommentIdSet;
  private Set<Integer> addedVerifiedCommentIdSet;
  private Set<Integer> addedUnverifiedRecordIdSet;
  private Set<Integer> addedVerifiedRecordIdSet;

  static Stream<Arguments> newUserProvider() {
    return Stream.of(
        Arguments.of("newusername", "newpassword", "newname", "MALE", "newtel"),
        Arguments.of("newusername", "newpassword", "newname", "FEMALE", "newtel"));
  }

  static Stream<Arguments> buildingProvider() {
    return Stream.of(
        Arguments.of("addbuildingname1", "300", "addbuildingdescription1"),
        Arguments.of("addbuildingname1", "500", "addbuildingdescription2"));
  }

  static Stream<Arguments> newBuildingProvider() {
    return Stream.of(
        Arguments.of("newbuildingname1", "300", "newbuildingdescription1"),
        Arguments.of("newbuildingname1", "500", "newbuildingdescription2"));
  }

  static Stream<Arguments> newsProvider() {
    return Stream.of(
        arguments("correct title", "correct author", "correct detail", true, ""),
        arguments("", "correct author", "correct detail", false, "标题不能为空！"),
        arguments("correct title", "", "correct detail", false, "作者不能为空！"),
        arguments("correct title", "correct author", "", false, "内容不能为空！"),
        arguments("", "", "", false, "内容不能为空！"));
  }

  static Stream<Arguments> passwordProvider() {
    return Stream.of(
        arguments(adminPassword, "root", "root", true, ""),
        arguments("wrongOldPassword", "root", "root", false, "旧密码错误"),
        arguments(adminPassword, "root", "root1", false, "密码填写不一致！"),
        arguments("", "root", "root", false, "信息填写不完整！"),
        arguments(adminPassword, "", "root", false, "信息填写不完整！"),
        arguments(adminPassword, "root", "", false, "信息填写不完整！"),
        arguments("", "", "", false, "信息填写不完整！"));
  }

  @BeforeEach
  public void setUp() {
    // Protect original data in database
    List<User> originUserList = this.userDao.queryAllUsers();
    Set<Integer> originUserIdSet = new HashSet<>();
    for (User user : originUserList) {
      originUserIdSet.add(user.getId());
    }
    this.originNewsIdSet = new HashSet<>();
    List<News> originalNewsList = this.newsDao.listNews(20);
    for (News news : originalNewsList) {
      this.originNewsIdSet.add(news.getId());
    }
    List<Comment> originUnverifiedCommentList = this.commentDao.listComment(20, false);
    List<Comment> originVerifiedCommentList = this.commentDao.listComment(20, true);
    Set<Integer> originUnverifiedCommentIdSet = new HashSet<>();
    Set<Integer> originVerifiedCommentIdSet = new HashSet<>();
    for (Comment comment : originUnverifiedCommentList) {
      originUnverifiedCommentIdSet.add(comment.getId());
    }
    for (Comment comment : originVerifiedCommentList) {
      originVerifiedCommentIdSet.add(comment.getId());
    }
    List<Record> originUnverifiedRecordList = this.recordDao.listRecord(20, false);
    List<Record> originVerifiedRecordList = this.recordDao.listRecord(20, true);

    // Add new data to database
    // Add admin
    Admin admin1 = new Admin(adminName, adminPassword, "name1", "MALE", "13000000000");
    this.userDao.addUser(admin1);
    List<User> addedUserList = this.userDao.queryAllUsers();
    Set<Integer> addedUserIdSet = new HashSet<>();
    for (User user : addedUserList) {
      addedUserIdSet.add(user.getId());
    }
    addedUserIdSet.removeAll(originUserIdSet);
    assertEquals(1, addedUserIdSet.size());
    for (int userId : addedUserIdSet) {
      this.admin = (Admin) this.userDao.queryUserById(userId);
    }
    // Add news
    News news1 = new News("newsTitle1", 10000, 10000, "newsAuthor1", "newsDetail1");
    News news2 = new News("newsTitle2", 20000, 20000, "newsAuthor2", "newsDetail2");
    assertAll(() -> this.newsDao.insertNews(news1), () -> this.newsDao.insertNews(news2));
    this.addedNewsIdSet = new HashSet<>();
    List<News> addedNewsList = this.newsDao.listNews(20);
    for (News news : addedNewsList) {
      this.addedNewsIdSet.add(news.getId());
    }
    this.addedNewsIdSet.removeAll(originNewsIdSet);
    assertEquals(2, this.addedNewsIdSet.size());
    // Add comment
    Comment comment1 = new Comment(1, 10000, "comment1 content", false);
    Comment comment2 = new Comment(2, 20000, "comment2 content", true);
    assertAll(
        () -> this.commentDao.addComment(comment1), () -> this.commentDao.addComment(comment2));
    List<Comment> addedUnverifiedCommentList = this.commentDao.listComment(20, false);
    addedUnverifiedCommentList.removeAll(originUnverifiedCommentList);
    assertEquals(2, addedUnverifiedCommentList.size());
    Comment temp = addedUnverifiedCommentList.get(1);
    temp.setVerified(true);
    assertTrue(this.commentDao.updateComment(temp));
    addedUnverifiedCommentList.remove(temp);
    List<Comment> addedVerifiedCommentList = this.commentDao.listComment(20, true);
    addedVerifiedCommentList.removeAll(originVerifiedCommentList);
    this.addedVerifiedCommentIdSet = new HashSet<>();
    this.addedUnverifiedCommentIdSet = new HashSet<>();
    for (Comment comment : addedUnverifiedCommentList) {
      this.addedUnverifiedCommentIdSet.add(comment.getId());
    }
    for (Comment comment : addedVerifiedCommentList) {
      this.addedVerifiedCommentIdSet.add(comment.getId());
    }
    assertAll(
        () -> assertEquals(1, this.addedUnverifiedCommentIdSet.size()),
        () -> assertEquals(1, this.addedVerifiedCommentIdSet.size()));
    // Add record
    Record record1 = new Record(10000, 10000, 20000, 10, 100, false);
    Record record2 = new Record(10000, 20000, 30000, 20, 200, true);
    assertAll(
        () -> assertTrue(this.recordDao.addRecord(record1)),
        () -> assertTrue(this.recordDao.addRecord(record2)));
    List<Record> addedUnverifiedRecordList = this.recordDao.listRecord(20, false);
    List<Record> addedVerifiedRecordList = this.recordDao.listRecord(20, true);
    addedUnverifiedRecordList.removeAll(originUnverifiedRecordList);
    addedVerifiedRecordList.removeAll(originVerifiedRecordList);
    assertAll(
        () -> assertEquals(1, addedUnverifiedRecordList.size()),
        () -> assertEquals(1, addedVerifiedRecordList.size()));
    this.addedUnverifiedRecordIdSet = new HashSet<>();
    this.addedVerifiedRecordIdSet = new HashSet<>();
    for (Record record : addedUnverifiedRecordList) {
      this.addedUnverifiedRecordIdSet.add(record.getId());
    }
    for (Record record : addedVerifiedRecordList) {
      this.addedVerifiedRecordIdSet.add(record.getId());
    }

    driver = new ChromeDriver();
    js = (JavascriptExecutor) driver;
    User user1 = new User("usernameusername1", "passwordpassword1", "name1", "MALE", "13000000000");
    this.userDao.addUser(user1);
    for (User user : this.userDao.queryAllUsers()) {
      if (user instanceof Admin) {
        // admin = (Admin) user;
      } else {
        this.user = user;
      }
    }
    Building building1 = new Building("buildingname1", "buildingdescription1", "400");
    this.buildingDao.addBuilding(building1);
    for (Building building : this.buildingDao.listBuilding(20)) {
      this.building = building;
    }

    this.adminLogin();
  }

  @AfterEach
  public void tearDown() {
    driver.quit();
    assertTrue(this.userDao.deleteUser(this.admin.getId()));
    for (User user : this.userDao.queryAllUsers()) {
      this.userDao.deleteUser(user.getId());
    }
    for (Building building : this.buildingDao.listBuilding(20)) {
      this.buildingDao.deleteBuilding(building.getId());
    }
    for (int newsId : this.addedNewsIdSet) {
      assertTrue(this.newsDao.deleteNewsById(newsId));
    }
    for (int commentId : this.addedUnverifiedCommentIdSet) {
      assertTrue(this.commentDao.deleteComment(commentId));
    }
    for (int commentId : this.addedVerifiedCommentIdSet) {
      assertTrue(this.commentDao.deleteComment(commentId));
    }
    for (int recordId : this.addedUnverifiedRecordIdSet) {
      assertTrue(this.recordDao.deleteRecord(recordId));
    }
    for (int recordId : this.addedVerifiedRecordIdSet) {
      assertTrue(this.recordDao.deleteRecord(recordId));
    }
  }

  private void adminLogin() {
    // Step # | name | target | value | comment
    // 1 | open | http://localhost:8080/MeetHere_war/ |  |
    driver.get("http://localhost:8080/MeetHere_war/");
    // 2 | setWindowSize | 1052x554 |  |
    driver.manage().window().setSize(new Dimension(1920, 1080));
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
    // Test name: DeleteUser
    // Step # | name | target | value | comment
    // 1 | open | http://localhost:8080/MeetHere_war/user?action=list |  |
    driver.get("http://localhost:8080/MeetHere_war/user?action=list");
    // 2 | setWindowSize | 1052x554 |  |
    driver.manage().window().setSize(new Dimension(1920, 1080));
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

  @ParameterizedTest
  @MethodSource("newUserProvider")
  @Order(2)
  public void modifyUser(String username, String password, String name, String sex, String tel) {
    // Test name: ModifyUser
    // Step # | name | target | value | comment
    // 1 | open | http://localhost:8080/MeetHere_war/user?action=list |  |
    driver.get("http://localhost:8080/MeetHere_war/user?action=list");
    // 2 | setWindowSize | 1052x554 |  |
    driver.manage().window().setSize(new Dimension(1920, 1080));
    // 3 | click | css=tr:nth-child(1) .btn-info |  |
    driver.findElement(By.id("#id_modify_button_" + this.user.getId())).click();
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
        () -> assertEquals(tel, modifiedUser.getTel()));
  }

  @Test
  @Order(3)
  public void homePage() {
    // Test name: HomePage
    // Step # | name | target | value | comment
    // 1 | open | http://localhost:8080/MeetHere_war/blank |  |
    driver.get("http://localhost:8080/MeetHere_war/blank");
    // 2 | setWindowSize | 1052x554 |  |
    driver.manage().window().setSize(new Dimension(1920, 1080));
    // 3 | click | linkText=首页 |  |
    driver.findElement(By.linkText("首页")).click();
    assertEquals("欢迎您,系统管理员!", driver.findElement(By.id("welcome")).getText());
  }

  @ParameterizedTest
  @MethodSource("buildingProvider")
  @Order(4)
  public void addBuilding(String buildingName, String buildingPrice, String buildingDescription) {
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
        () -> assertEquals(buildingDescription, addedBuilding.getDescription()));
  }

  @Test
  @Order(5)
  public void deleteBuilding() throws Throwable {
    // Test name: DeleteBuilding
    // Step # | name | target | value | comment
    // 1 | open | http://localhost:8080/MeetHere_war/building?action=list |  |
    driver.get("http://localhost:8080/MeetHere_war/building?action=list");
    // 2 | setWindowSize | 1052x554 |  |
    driver.manage().window().setSize(new Dimension(1920, 1080));
    // 3 | chooseOkOnNextConfirmation |  |  |
    // 4 | click | css=.btn-danger |  |
    driver.findElement(By.id("#id_delete_button_" + this.building.getId())).click();
    // 5 | assertConfirmation | 您确定要删除这个楼吗？ |  |
    assertEquals("您确定要删除这个楼吗？", driver.switchTo().alert().getText());
    // 6 | webdriverChooseOkOnVisibleConfirmation |  |  |
    driver.switchTo().alert().accept();
    TimeUnit.MILLISECONDS.sleep(200);
    Building deletedBuilding = this.buildingDao.queryBuildingById(this.building.getId());
    assertNull(deletedBuilding);
  }

  @ParameterizedTest
  @MethodSource("newBuildingProvider")
  @Order(6)
  public void modifyBuilding(
      String newBuildingName, String newBuildingPrice, String newBuildingDescription) {
    // Test name: ModifyBuilding
    // Step # | name | target | value | comment
    // 1 | open | http://localhost:8080/MeetHere_war/building?action=list |  |
    driver.get("http://localhost:8080/MeetHere_war/building?action=list");
    // 2 | setWindowSize | 1052x554 |  |
    driver.manage().window().setSize(new Dimension(1920, 1080));
    // 3 | click | css=td > .btn-info |  |
    driver.findElement(By.id("#id_modify_button_" + this.building.getId())).click();
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
        () -> assertEquals(newBuildingDescription, addedBuilding.getDescription()));
  }

  @Test
  @Order(7)
  public void clickMenu() {
    // Test name: ClickMenu
    // Step # | name | target | value | comment
    // 1 | open | http://localhost:8080/MeetHere_war/blank |  |
    driver.get("http://localhost:8080/MeetHere_war/blank");
    // 2 | setWindowSize | 1052x554 |  |
    driver.manage().window().setSize(new Dimension(1920, 1080));
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

  @ParameterizedTest
  @MethodSource("newsProvider")
  public void modifyNews(
      String title, String author, String detail, boolean success, String errorMessage)
      throws Exception {
    driver.manage().window().setSize(new Dimension(1920, 1080));
    for (int newsId : this.addedNewsIdSet) {
      driver.get("http://localhost:8080/MeetHere_war/news?action=list");
      driver.findElement(By.id("modify" + newsId)).click();
      driver.findElement(By.cssSelector(".data_form")).click();
      driver.findElement(By.id("title")).clear();
      driver.findElement(By.id("title")).sendKeys(title);
      driver.findElement(By.id("author")).clear();
      driver.findElement(By.id("author")).sendKeys(author);
      driver.findElement(By.id("detail")).clear();
      driver.findElement(By.id("detail")).sendKeys(detail);
      driver.findElement(By.cssSelector(".btn:nth-child(1)")).click();
      if (success) {
        Thread.sleep(500);
        News modifiedNews = this.newsDao.queryNewsById(newsId);
        assertAll(
            () -> assertEquals(newsId, modifiedNews.getId()),
            () -> assertEquals(title, modifiedNews.getTitle()),
            () -> assertEquals(author, modifiedNews.getAuthor()),
            () -> assertEquals(detail, modifiedNews.getDetail()));
      } else {
        assertEquals(errorMessage, driver.findElement(By.id("error")).getText());
      }
    }
  }

  @Test
  public void deleteNews() throws Exception {
    // Test name: DeleteNews
    // Step # | name | target | value | comment
    // 1 | open | http://localhost:8080/MeetHere_war/news?action=list |  |

    // 2 | setWindowSize | 1052x554 |  |
    driver.manage().window().setSize(new Dimension(1920, 1080));

    // 3 | chooseOkOnNextConfirmation |  |  |
    // 4 | click | css=.btn-danger |  |
    for (int newsId : this.addedNewsIdSet) {
      driver.get("http://localhost:8080/MeetHere_war/news?action=list");
      driver.findElement(By.id("delete" + newsId)).click();
      // 5 | assertConfirmation | 您确定要删除这条新闻吗？ |  |
      assertEquals("您确定要删除这条新闻吗？", driver.switchTo().alert().getText());
      // 6 | webdriverChooseOkOnVisibleConfirmation |  |  |
      driver.switchTo().alert().accept();
      Thread.sleep(500);
      News deletedNews = this.newsDao.queryNewsById(newsId);
      assertNull(deletedNews);
    }
  }

  @ParameterizedTest
  @MethodSource("newsProvider")
  public void createNews(
      String title, String author, String detail, boolean success, String errorMessage)
      throws Exception {
    // Test name: CreateNews
    // Step # | name | target | value | comment
    // 1 | open | http://localhost:8080/MeetHere_war/news?action=list |  |
    driver.get("http://localhost:8080/MeetHere_war/news?action=list");
    // 2 | setWindowSize | 1052x554 |  |
    driver.manage().window().setSize(new Dimension(1600, 900));
    // 3 | click | css=.btn |  |
    driver.findElement(By.cssSelector(".btn")).click();
    // 4 | click | id=title |  |
    driver.findElement(By.id("title")).click();
    // 5 | type | id=title | 123 |
    driver.findElement(By.id("title")).sendKeys(title);
    // 6 | type | id=author | 123 |
    driver.findElement(By.id("author")).sendKeys(author);
    // 7 | click | id=detail |  |
    driver.findElement(By.id("detail")).click();
    // 8 | type | id=detail | 123 |
    driver.findElement(By.id("detail")).sendKeys(detail);
    // 9 | click | css=.btn:nth-child(1) |  |
    driver.findElement(By.cssSelector(".btn:nth-child(1)")).click();
    Thread.sleep(500);
    if (success) {
      List<News> currentNewsList = this.newsDao.listNews(20);
      Set<Integer> tempNewsIdSet = new HashSet<>();
      for (News news : currentNewsList) {
        tempNewsIdSet.add(news.getId());
      }
      tempNewsIdSet.removeAll(this.addedNewsIdSet);
      tempNewsIdSet.removeAll(this.originNewsIdSet);
      assertEquals(1, tempNewsIdSet.size());
      for (int newsId : tempNewsIdSet) {
        // Track added news
        this.addedNewsIdSet.add(newsId);
        News temp = this.newsDao.queryNewsById(newsId);
        assertAll(
            () -> assertEquals(newsId, temp.getId()),
            () -> assertEquals(title, temp.getTitle()),
            () -> assertEquals(author, temp.getAuthor()),
            () -> assertEquals(detail, temp.getDetail()));
      }
    } else {
      assertEquals(errorMessage, driver.findElement(By.id("error")).getText());
    }
  }

  @Test
  public void deleteUnverifiedComment() throws Exception {
    // Test name: DeleteUnverifiedComment
    // Step # | name | target | value | comment
    // 1 | open | http://localhost:8080/MeetHere_war/comment?action=list |  |

    // 2 | setWindowSize | 1052x554 |  |
    driver.manage().window().setSize(new Dimension(1920, 1080));
    // 3 | chooseOkOnNextConfirmation |  |  |
    // 4 | click | css=.btn:nth-child(2) |  |
    for (int id : this.addedUnverifiedCommentIdSet) {
      driver.get("http://localhost:8080/MeetHere_war/comment?action=list");
      this.js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
      driver.findElement(By.id("deleteUnverified" + id)).click();
      assertEquals("您确定要删除这条留言吗？", driver.switchTo().alert().getText());
      driver.switchTo().alert().accept();
      Thread.sleep(500);
      Comment deletedComment = this.commentDao.queryCommentById(id);
      assertNull(deletedComment);
    }

    // 5 | assertConfirmation | 您确定要删除这条留言吗？ |  |

    // 6 | webdriverChooseOkOnVisibleConfirmation |  |  |

  }

  @Test
  public void deleteVerifiedComment() throws Exception {
    // Test name: DeleteVerifiedComment
    // Step # | name | target | value | comment
    // 1 | open | http://localhost:8080/MeetHere_war/comment?action=list |  |

    // 2 | setWindowSize | 1052x554 |  |
    driver.manage().window().setSize(new Dimension(1920, 1080));
    // 3 | chooseOkOnNextConfirmation |  |  |
    // 4 | click | css=div:nth-child(2) > .table .btn |  |
    for (int id : this.addedVerifiedCommentIdSet) {
      driver.get("http://localhost:8080/MeetHere_war/comment?action=list");
      this.js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
      driver.findElement(By.id("deleteVerified" + id)).click();
      assertEquals("您确定要删除这条留言吗？", driver.switchTo().alert().getText());
      driver.switchTo().alert().accept();
      Thread.sleep(500);
      Comment deletedComment = this.commentDao.queryCommentById(id);
      assertNull(deletedComment);
    }

    // 5 | assertConfirmation | 您确定要删除这条留言吗？ |  |
    // 6 | webdriverChooseOkOnVisibleConfirmation |  |  |

  }

  @Test
  public void verifyComment() throws Exception {
    // Test name: VerifyComment
    // Step # | name | target | value | comment
    // 1 | open | http://localhost:8080/MeetHere_war/comment?action=list |  |

    // 2 | setWindowSize | 1052x554 |  |
    driver.manage().window().setSize(new Dimension(1920, 1080));
    // 3 | chooseOkOnNextConfirmation |  |  |
    // 4 | click | css=tr:nth-child(1) .btn:nth-child(1) |  |
    for (int id : this.addedUnverifiedCommentIdSet) {
      driver.get("http://localhost:8080/MeetHere_war/comment?action=list");
      this.js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
      driver.findElement(By.id("verify" + id)).click();
      assertEquals("您确定要通过这条留言吗？", driver.switchTo().alert().getText());
      driver.switchTo().alert().accept();
      Thread.sleep(500);
      Comment verifiedComment = this.commentDao.queryCommentById(id);
      assertTrue(verifiedComment.isVerified());
    }

    // 5 | assertConfirmation | 您确定要通过这条留言吗？ |  |

    // 6 | webdriverChooseOkOnVisibleConfirmation |  |  |

  }

  @Test
  public void deleteUnverifiedRecord() throws Exception {
    // Test name: DeleteUnverifiedRecord
    // Step # | name | target | value | comment
    // 1 | open | http://localhost:8080/MeetHere_war/record?action=list |  |
    driver.manage().window().setSize(new Dimension(1920, 1080));
    for (int id : this.addedUnverifiedRecordIdSet) {
      driver.get("http://localhost:8080/MeetHere_war/record?action=list");
      driver.findElement(By.id("delete" + id)).click();
      assertEquals("您确定要删除这条记录吗？", driver.switchTo().alert().getText());
      driver.switchTo().alert().accept();
      Thread.sleep(500);
      Record deletedRecord = this.recordDao.queryRecordById(id);
      assertNull(deletedRecord);
    }

    // 2 | setWindowSize | 1052x554 |  |

    // 3 | chooseOkOnNextConfirmation |  |  |
    // 4 | click | css=.btn:nth-child(2) |  |

    // 5 | assertConfirmation | 您确定要删除这条记录吗？ |  |

    // 6 | webdriverChooseOkOnVisibleConfirmation |  |  |

  }

  @Test
  public void verifyRecord() throws Exception {
    // Test name: VerifyRecord
    // Step # | name | target | value | comment
    // 1 | open | http://localhost:8080/MeetHere_war/record?action=list |  |

    // 2 | setWindowSize | 1052x554 |  |
    driver.manage().window().setSize(new Dimension(1920, 1080));
    for (int id : this.addedUnverifiedRecordIdSet) {
      driver.get("http://localhost:8080/MeetHere_war/record?action=list");
      driver.findElement(By.id("verify" + id)).click();
      assertEquals("您确定审核通过这条记录吗？", driver.switchTo().alert().getText());
      driver.switchTo().alert().accept();
      Thread.sleep(500);
      Record verifiedRecord = this.recordDao.queryRecordById(id);
      assertTrue(verifiedRecord.isVerified());
    }
    // 3 | chooseOkOnNextConfirmation |  |  |
    // 4 | click | css=tr:nth-child(1) .btn:nth-child(1) |  |

    // 5 | assertConfirmation | 您确定审核通过这条记录吗？ |  |

    // 6 | webdriverChooseOkOnVisibleConfirmation |  |  |

  }

  @ParameterizedTest
  @MethodSource("passwordProvider")
  public void changePassword(
      String oldPassword,
      String newPassword,
      String rPassword,
      boolean success,
      String errorMessage)
      throws Exception {
    // Test name: ChangePassword
    // Step # | name | target | value | comment
    // 1 | open | http://localhost:8080/MeetHere_war/password?action=change |  |
    driver.get("http://localhost:8080/MeetHere_war/password?action=change");
    // 2 | setWindowSize | 1052x554 |  |
    driver.manage().window().setSize(new Dimension(1920, 1080));
    // 3 | click | id=oldPassword |  |
    driver.findElement(By.id("oldPassword")).click();
    // 4 | type | id=oldPassword | root |
    driver.findElement(By.id("oldPassword")).sendKeys(oldPassword);
    // 5 | type | id=newPassword | root |
    driver.findElement(By.id("newPassword")).sendKeys(newPassword);
    // 6 | type | id=rPassword | root |
    driver.findElement(By.id("rPassword")).sendKeys(rPassword);
    // 7 | sendKeys | id=rPassword | ${KEY_ENTER} |
    driver.findElement(By.id("rPassword")).sendKeys(Keys.ENTER);
    Thread.sleep(500);
    if (success) {
      this.admin = (Admin) this.userDao.queryUserById(this.admin.getId());
      assertEquals(newPassword, this.admin.getPassword());
    } else {
      assertEquals(errorMessage, driver.findElement(By.id("error")).getText());
    }
  }
}
