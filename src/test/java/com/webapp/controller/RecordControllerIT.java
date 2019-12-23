package com.webapp.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.webapp.config.MvcConfig;
import com.webapp.model.Record;
import com.webapp.model.user.User;
import com.webapp.service.database.dao.RecordDao;
import com.webapp.service.database.dao.UserDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Shangzhen Li
 */
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = MvcConfig.class)
public class RecordControllerIT {

  final static int totalCount = 6;
  final String urlPrefix = "/WEB-INF/jsp/";
  final int verifiedCount = 3;
  final long[] testTime = {1000, 2000, 3000, 4000, 5000, 6000};
  final long[] testStartDate = {10000, 20000, 30000, 40000, 50000, 60000};
  final long[] testEndDate = {20000, 30000, 40000, 50000, 60000, 70000};
  final int[] testUserId = {10, 20, 30, 40, 50, 60};
  final int[] testBuildingId = {100, 200, 300, 400, 500, 600};
  @Autowired
  RecordDao recordDao;
  @Autowired
  UserDao userDao;
  @Autowired
  WebApplicationContext wac;
  MockMvc mockMvc;
  User testUser;
  int[] idList;

  static Stream<Integer> idIndexProvider() {
    return Stream.of(0, totalCount / 2, totalCount - 1);
  }

  @BeforeEach
  void init() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    //Add a test user
    final String TEST_NAME = "testName";
    assertTrue(
        userDao.addUser(new User("testUsername", "testPassword", TEST_NAME, "MALE", "testTel")));
    List<User> userList = userDao.queryUserByName(TEST_NAME);
    assertEquals(1, userList.size());
    testUser = userList.get(0);
    // Add 3 unverified records and 3 verified records
    Record[] records = new Record[6];
    for (int i = 0; i < totalCount; i++) {
      records[i] = new Record(testTime[i], testStartDate[i], testEndDate[i], testUserId[i],
          testBuildingId[i], false);
    }
    //Set first verifiedCount records to verified
    for (int i = 0; i < verifiedCount; i++) {
      records[i].setVerified(true);
    }
    records[totalCount - 1].setUserId(testUser.getId());
    for (int i = 0; i < totalCount; i++) {
      assertTrue(this.recordDao.addRecord(records[i]));
    }
    //Save the added records' id
    this.idList = new int[totalCount];
    List<Record> verifiedRecordList = recordDao.listRecord(verifiedCount, true);
    List<Record> unverifiedRecordList = recordDao.listRecord(totalCount - verifiedCount, false);
    assertAll(
        () -> assertEquals(verifiedCount, verifiedRecordList.size()),
        () -> assertEquals(totalCount - verifiedCount, unverifiedRecordList.size())
    );
    for (int i = 0; i < verifiedCount; i++) {
      idList[i] = verifiedRecordList.get(i).getId();
    }
    for (int i = verifiedCount; i < totalCount; i++) {
      idList[i] = unverifiedRecordList.get(i - verifiedCount).getId();
    }
  }

  @AfterEach
  void restore() {
    for (int i = 0; i < totalCount; i++) {
      assertTrue(recordDao.deleteRecord(idList[i]));
    }
    assertTrue(userDao.deleteUser(testUser.getId()));
  }

  @ParameterizedTest
  @ValueSource(strings = {"other", "verify", "list"})
  void givenUserWhenActionIsIllegalOrListThenList(String action) throws Exception {
    Map<String, Object> sessionAttrs = new HashMap<>();
    sessionAttrs.put("currentUserType", "user");
    sessionAttrs.put("currentUser", testUser);
    MvcResult result = this.mockMvc
        .perform(get("/record?action=" + action).sessionAttrs(sessionAttrs))
        .andExpect(status().isOk())
        .andExpect(view().name("mainUser"))
        .andExpect(forwardedUrl(this.urlPrefix + "mainUser.jsp"))
        .andExpect(model().attribute("mainPage", "user/record.jsp"))
        .andReturn();
    List<Record> verifiedRecordList = (List<Record>) result.getModelAndView().getModelMap()
        .get("verifiedRecordList");
    List<Record> unverifiedRecordList = (List<Record>) result.getModelAndView().getModelMap()
        .get("unverifiedRecordList");
    assertAll(
        () -> assertEquals(verifiedCount, verifiedRecordList.size()),
        () -> assertEquals(1, unverifiedRecordList.size()));
    Record temp = unverifiedRecordList.get(0);
    assertAll(
        () -> assertEquals(testTime[totalCount - 1], temp.getTime()),
        () -> assertEquals(testStartDate[totalCount - 1], temp.getStartDate()),
        () -> assertEquals(testEndDate[totalCount - 1], temp.getEndDate()),
        () -> assertEquals(testUser.getId(), temp.getUserId()),
        () -> assertEquals(testBuildingId[totalCount - 1], temp.getBuildingId())
    );
    for (int i = 0; i < verifiedCount; i++) {
      Record finalTemp = verifiedRecordList.get(i);
      int finalI = verifiedCount - 1
          - i;/*For lambda expression only. Records are listed in decreasing time order*/
      assertAll(
          () -> assertEquals(testTime[finalI], finalTemp.getTime()),
          () -> assertEquals(testStartDate[finalI], finalTemp.getStartDate()),
          () -> assertEquals(testEndDate[finalI], finalTemp.getEndDate()),
          () -> assertEquals(testUserId[finalI], finalTemp.getUserId()),
          () -> assertEquals(testBuildingId[finalI], finalTemp.getBuildingId())
      );
    }
  }

  @ParameterizedTest
  @ValueSource(strings = {"add", "save", "other", "list"})
  void givenAdminWhenActionIsIllegalOrListThenList(String action) throws Exception {
    Map<String, Object> sessionAttrs = new HashMap<>();
    sessionAttrs.put("currentUserType", "admin");
    MvcResult result = this.mockMvc
        .perform(get("/record?action=" + action).sessionAttrs(sessionAttrs))
        .andExpect(status().isOk())
        .andExpect(view().name("mainAdmin"))
        .andExpect(forwardedUrl(this.urlPrefix + "mainAdmin.jsp"))
        .andExpect(model().attribute("mainPage", "admin/record.jsp"))
        .andReturn();
    List<Record> verifiedRecordList = (List<Record>) result.getModelAndView().getModelMap()
        .get("verifiedRecordList");
    List<Record> unverifiedRecordList = (List<Record>) result.getModelAndView().getModelMap()
        .get("unverifiedRecordList");
    assertAll(
        () -> assertEquals(verifiedCount, verifiedRecordList.size()),
        () -> assertEquals(totalCount - verifiedCount, unverifiedRecordList.size()));
    for (int i = 0; i < verifiedCount; i++) {
      Record temp = verifiedRecordList.get(i);
      int finalI = verifiedCount - 1
          - i;/*For lambda expression only. Records are listed in decreasing time order*/
      assertAll(
          () -> assertEquals(testTime[finalI], temp.getTime()),
          () -> assertEquals(testStartDate[finalI], temp.getStartDate()),
          () -> assertEquals(testEndDate[finalI], temp.getEndDate()),
          () -> assertEquals(testUserId[finalI], temp.getUserId()),
          () -> assertEquals(testBuildingId[finalI], temp.getBuildingId())
      );
    }
    for (int i = verifiedCount; i < totalCount; i++) {
      Record temp = unverifiedRecordList.get(i - verifiedCount);
      int finalI = totalCount - 1 - i
          + verifiedCount;/*For lambda expression only. Records are listed in decreasing time order*/
      assertAll(
          () -> assertEquals(testTime[finalI], temp.getTime()),
          () -> assertEquals(testStartDate[finalI], temp.getStartDate()),
          () -> assertEquals(testEndDate[finalI], temp.getEndDate()),
          () -> {
            if (totalCount - 1 != finalI) {
              assertEquals(testUserId[finalI], temp.getUserId());
            } else {
              assertEquals(testUser.getId(), temp.getUserId());
            }
          },
          () -> assertEquals(testBuildingId[finalI], temp.getBuildingId())
      );
    }
  }

  @ParameterizedTest
  @MethodSource("idIndexProvider")
  void givenAdminWhenActionIsDeleteThenDelete(int idIndex) throws Exception {
    Map<String, Object> sessionAttrs = new HashMap<>();
    sessionAttrs.put("currentUserType", "admin");
    MvcResult result =
        this.mockMvc
            .perform(
                get("/record?action=delete&recordId=" + this.idList[idIndex])
                    .sessionAttrs(sessionAttrs))
            .andExpect(status().isOk())
            .andExpect(view().name("mainAdmin"))
            .andExpect(forwardedUrl(urlPrefix + "mainAdmin.jsp"))
            .andExpect(model().attribute("mainPage", "admin/record.jsp"))
            .andReturn();
    List<Record> verifiedRecordList =
        (List<Record>) result.getModelAndView().getModelMap().get("verifiedRecordList");
    List<Record> unverifiedRecordList =
        (List<Record>) result.getModelAndView().getModelMap().get("unverifiedRecordList");
    /*No record's id can equals to the deleted one*/
    for (Record record : verifiedRecordList) {
      assertNotEquals(this.idList[idIndex], record.getId());
    }
    for (Record record : unverifiedRecordList) {
      assertNotEquals(this.idList[idIndex], record.getId());
    }
  }

  @ParameterizedTest
  @MethodSource("idIndexProvider")
  void givenAdminWhenActionIsVerifyThenVerify(int idIndex) throws Exception {
    Map<String, Object> sessionAttrs = new HashMap<>();
    sessionAttrs.put("currentUserType", "admin");
    MvcResult result =
        this.mockMvc
            .perform(
                get("/record?action=verify&recordId=" + this.idList[idIndex])
                    .sessionAttrs(sessionAttrs))
            .andExpect(status().isOk())
            .andExpect(view().name("mainAdmin"))
            .andExpect(forwardedUrl(urlPrefix + "mainAdmin.jsp"))
            .andExpect(model().attribute("mainPage", "admin/record.jsp"))
            .andReturn();
    Record verifiedRecord = recordDao.queryRecordById(idList[idIndex]);
    assertTrue(verifiedRecord.isVerified());
  }

  @Test
  void givenUserWhenActionIsDeleteThenDelete() throws Exception {
    Map<String, Object> sessionAttrs = new HashMap<>();
    sessionAttrs.put("currentUserType", "user");
    sessionAttrs.put("currentUser", testUser);
    MvcResult result =
        this.mockMvc
            .perform(
                get("/record?action=delete&recordId=" + this.idList[totalCount - 1])
                    .sessionAttrs(sessionAttrs))
            .andExpect(status().isOk())
            .andExpect(view().name("mainUser"))
            .andExpect(forwardedUrl(urlPrefix + "mainUser.jsp"))
            .andExpect(model().attribute("mainPage", "user/record.jsp"))
            .andReturn();
    List<Record> verifiedRecordList =
        (List<Record>) result.getModelAndView().getModelMap().get("verifiedRecordList");
    List<Record> unverifiedRecordList =
        (List<Record>) result.getModelAndView().getModelMap().get("unverifiedRecordList");
    /*No record's id can equals to the deleted one*/
    for (Record record : verifiedRecordList) {
      assertNotEquals(this.idList[totalCount - 1], record.getId());
    }
    for (Record record : unverifiedRecordList) {
      assertNotEquals(this.idList[totalCount - 1], record.getId());
    }
  }

  @Test
  void givenUserWhenActionIsSaveThenSave() throws Exception {
    Map<String, Object> sessionAttrs = new HashMap<>();
    sessionAttrs.put("currentUserType", "user");
    final String TEST_NAME = "newName";
    assertTrue(
        userDao.addUser(new User("newUsername", "newPassword", TEST_NAME, "MALE", "newTel")));
    List<User> userList = userDao.queryUserByName(TEST_NAME);
    assertEquals(1, userList.size());
    User tempUser = userList.get(0);
    sessionAttrs.put("currentUser", tempUser);
    final long TEST_START_DATE = 80000;
    final long TEST_END_DATE = 90000;
    final long TEST_BUILDING_ID = 900;
    MvcResult result = this.mockMvc
        .perform(
            get("/record?action=save")
                .sessionAttrs(sessionAttrs)
                .param("startDate", String.valueOf(TEST_START_DATE))
                .param("endDate", String.valueOf(TEST_END_DATE))
                .param("buildingId", String.valueOf(TEST_BUILDING_ID)))
        .andExpect(status().isOk())
        .andExpect(view().name("mainUser"))
        .andExpect(forwardedUrl(urlPrefix + "mainUser.jsp"))
        .andExpect(model().attribute("mainPage", "user/record.jsp"))
        .andReturn();
    List<Record> verifiedRecordList = (List<Record>) result.getModelAndView().getModelMap()
        .get("verifiedRecordList");
    List<Record> unverifiedRecordList = (List<Record>) result.getModelAndView().getModelMap()
        .get("unverifiedRecordList");
    //The newly added record should not appear in the verified list
    for (Record record : verifiedRecordList) {
      assertNotEquals(tempUser.getId(), record.getUserId());
    }
    assertAll(
        () -> assertEquals(1, unverifiedRecordList.size()),
        () -> assertEquals(TEST_START_DATE, unverifiedRecordList.get(0).getStartDate()),
        () -> assertEquals(TEST_END_DATE, unverifiedRecordList.get(0).getEndDate()),
        () -> assertEquals(TEST_BUILDING_ID, unverifiedRecordList.get(0).getBuildingId()),
        //Delete test data from database
        () -> assertTrue(userDao.deleteUser(tempUser.getId())),
        () -> assertTrue(recordDao.deleteRecord(unverifiedRecordList.get(0).getId()))
    );
  }

  @Test
  void givenUserWhenActionIsAddThenRedirectToRecordAdd() throws Exception {
    Map<String, Object> sessionAttrs = new HashMap<>();
    sessionAttrs.put("currentUserType", "user");
    sessionAttrs.put("currentUser", testUser);
    this.mockMvc.perform(get("/record?action=add").sessionAttrs(sessionAttrs))
        .andExpect(status().isOk()).andExpect(view().name("mainUser"))
        .andExpect(forwardedUrl(urlPrefix + "mainUser.jsp"))
        .andExpect(model().attribute("mainPage", "user/recordAdd.jsp")).andReturn();
  }

}
