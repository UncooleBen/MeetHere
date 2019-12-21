package com.webapp.controller;

import com.webapp.config.MvcConfig;
import com.webapp.model.Building;
import com.webapp.model.Record;
import com.webapp.model.user.User;
import com.webapp.service.database.dao.BuildingDao;
import com.webapp.service.database.dao.RecordDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Juntao Peng
 */
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = MvcConfig.class)
public class BuildingControllerIT {
    @Autowired
    BuildingDao buildingDao;
    @Autowired
    RecordDao recordDao;
    @Autowired
    WebApplicationContext wac;
    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        this.buildingDao.addBuilding(new Building("Sather Gate", "sg", "999"));
        this.buildingDao.addBuilding(new Building("Allston Way", "aw", "999"));
        this.buildingDao.addBuilding(new Building("Golden Gate Park", "ggp", "999"));
    }

    @AfterEach
    void tearDown() {
        for (Building building : this.buildingDao.listBuilding(20)) {
            this.buildingDao.deleteBuilding(building.getId());
        }
    }

    @Test
    void shouldReturnList_whenUserActionIsList() throws Throwable {
        List<String> buildingNames = Arrays.asList("Golden Gate Park", "Allston Way", "Sather Gate");
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("currentUserType", "user");
        MvcResult result = this.mockMvc.perform(get("/building?action=list").sessionAttrs(sessionAttrs))
                .andExpect(status().isOk())
                .andExpect(view().name("mainUser"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mainUser.jsp"))
                .andExpect(model().attribute("mainPage", "user/building.jsp"))
                .andReturn();
        List<Building> resultBuildings = (List<Building>) result.getModelAndView().getModelMap().get("buildingList");
        for (int i = 0; i < buildingNames.size(); i++) {
            String expected = buildingNames.get(i);
            String actual = resultBuildings.get(i).getName();
            assertEquals(expected, actual);
        }
    }

    @Test
    void shouldReturnList_whenAdminActionIsList() throws Throwable {
        List<String> buildingNames = Arrays.asList("Golden Gate Park", "Allston Way", "Sather Gate");
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("currentUserType", "admin");
        MvcResult result = this.mockMvc.perform(get("/building?action=list").sessionAttrs(sessionAttrs))
                .andExpect(status().isOk())
                .andExpect(view().name("mainAdmin"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mainAdmin.jsp"))
                .andExpect(model().attribute("mainPage", "admin/building.jsp"))
                .andReturn();
        List<Building> resultBuildings = (List<Building>) result.getModelAndView().getModelMap().get("buildingList");
        for (int i = 0; i < buildingNames.size(); i++) {
            String expected = buildingNames.get(i);
            String actual = resultBuildings.get(i).getName();
            assertEquals(expected, actual);
        }
    }

    @Test
    void shouldReturnList_whenAdminActionIsNull() throws Throwable {
        List<String> buildingNames = Arrays.asList("Golden Gate Park", "Allston Way", "Sather Gate");
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("currentUserType", "admin");
        MvcResult result = this.mockMvc.perform(get("/building?action=").sessionAttrs(sessionAttrs))
                .andExpect(status().isOk())
                .andExpect(view().name("mainAdmin"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mainAdmin.jsp"))
                .andExpect(model().attribute("mainPage", "admin/building.jsp"))
                .andReturn();
        List<Building> resultBuildings = (List<Building>) result.getModelAndView().getModelMap().get("buildingList");
        for (int i = 0; i < buildingNames.size(); i++) {
            String expected = buildingNames.get(i);
            String actual = resultBuildings.get(i).getName();
            assertEquals(expected, actual);
        }
    }

    static Stream<Arguments> buildingProvider() {
        return Stream.of(
                Arguments.of("ECNU", "East China Normal University", "1"),
                Arguments.of("SJTU", "Shanghai Jiao Tong University", "2"),
                Arguments.of("UCB", "University of California, Berkeley", "3"),
                Arguments.of("MIT", "Massachusetts Institute of Technology", "4")
        );
    }

    @Test
    void shouldGoToMainUser_whenCurrentUserIsUser_andActionIsDelete() throws Throwable {
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("currentUserType", "user");
        this.mockMvc.perform(get("/building?action=delete")
                .sessionAttrs(sessionAttrs))
                .andExpect(status().isOk())
                .andExpect(view().name("mainUser"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mainUser.jsp"))
                .andExpect(model().attribute("mainPage", "user/blank.jsp"));
    }

    @ParameterizedTest
    @MethodSource("buildingProvider")
    void deleteUserAsAdmin(String name, String description, String price) throws Throwable {
        Building expectedBuilding = new Building(name, description, price);
        this.buildingDao.addBuilding(expectedBuilding);
        expectedBuilding = this.buildingDao.listBuilding(20).get(0);
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("currentUserType", "admin");
        MvcResult result = this.mockMvc.perform(get("/building?action=delete&id=" + expectedBuilding.getId()).sessionAttrs(sessionAttrs))
                .andExpect(status().isOk())
                .andExpect(view().name("mainAdmin"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mainAdmin.jsp"))
                .andExpect(model().attribute("mainPage", "admin/building.jsp"))
                .andReturn();
        List<Building> resultBuildings = (List<Building>) result.getModelAndView().getModelMap().get("buildingList");
        for (int i = 0; i < resultBuildings.size(); i++) {
            String expected = resultBuildings.get(i).getName();
            assertNotEquals(expected, name);
        }
    }

    @Test
    void shouldGoToMainUser_whenCurrentUserIsUser_andActionIsSave() throws Throwable {
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("currentUserType", "user");
        this.mockMvc.perform(get("/building?action=save")
                .sessionAttrs(sessionAttrs))
                .andExpect(status().isOk())
                .andExpect(view().name("mainUser"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mainUser.jsp"))
                .andExpect(model().attribute("mainPage", "user/blank.jsp"));
    }

    @ParameterizedTest
    @MethodSource("buildingProvider")
    void saveBuildingAsAdmin(String name, String description, String price) throws Throwable {
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("currentUserType", "admin");
        MvcResult result = this.mockMvc.perform(post("/building?action=save")
                .sessionAttrs(sessionAttrs)
                .param("buildingName", name)
                .param("buildingDescription", description)
                .param("buildingPrice", price))
                .andExpect(status().isOk())
                .andExpect(view().name("mainAdmin"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mainAdmin.jsp"))
                .andExpect(model().attribute("mainPage", "admin/building.jsp"))
                .andReturn();
        Building addedBuilding = this.buildingDao.listBuilding(20).get(0);
        assertAll(
                () -> assertEquals(name, addedBuilding.getName()),
                () -> assertEquals(description, addedBuilding.getDescription()),
                () -> assertEquals(price, addedBuilding.getPrice())
        );
        this.buildingDao.deleteBuilding(addedBuilding.getId());
    }

    static Stream<Arguments> existedBuildingProvider() {
        return Stream.of(
                Arguments.of("Sather Gate", "sg", "999"),
                Arguments.of("Allston Way", "aw", "999"),
                Arguments.of("Golden Gate Park", "ggp", "999")
        );
    }

    @ParameterizedTest
    @MethodSource("existedBuildingProvider")
    void updateBuildingThenRestore(String name, String description, String price) throws Throwable {
        String newBuildingName = "clandestine building name";
        int id = 0;
        for (Building building : this.buildingDao.listBuilding(20)) {
            if (building.getName().equals(name)) {
                id = building.getId();
                break;
            }
        }
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("currentUserType", "admin");
        this.mockMvc.perform(post("/building?action=save")
                .param("buildingName", newBuildingName)
                .param("buildingDescription", description)
                .param("buildingPrice", price)
                .param("buildingId", String.valueOf(id))
                .sessionAttrs(sessionAttrs))
                .andExpect(status().isOk())
                .andExpect(view().name("mainAdmin"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mainAdmin.jsp"))
                .andExpect(model().attribute("mainPage", "admin/building.jsp"));
        Building addedBuilding = this.buildingDao.queryBuildingById(id);
        assertAll(
                () -> assertEquals(newBuildingName, addedBuilding.getName()),
                () -> assertEquals(description, addedBuilding.getDescription()),
                () -> assertEquals(price, addedBuilding.getPrice())
        );
        // Restore building
        addedBuilding.setDescription(description);
        addedBuilding.setPrice(price);
        addedBuilding.setName(name);
        this.buildingDao.updateBuilding(addedBuilding);
    }

    @Test
    void shouldGoToMainUser_whenCurrentUserIsUser_andActionIsAdd() throws Throwable {
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("currentUserType", "user");
        this.mockMvc.perform(get("/building?action=add")
                .sessionAttrs(sessionAttrs))
                .andExpect(status().isOk())
                .andExpect(view().name("mainUser"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mainUser.jsp"))
                .andExpect(model().attribute("mainPage", "user/blank.jsp"));
    }

    @Test
    void shouldGoToModify_whenCurrentUserIsAdmin_andActionIsAdd() throws Throwable {
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("currentUserType", "admin");
        this.mockMvc.perform(get("/building?action=add")
                .sessionAttrs(sessionAttrs))
                .andExpect(status().isOk())
                .andExpect(view().name("mainAdmin"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mainAdmin.jsp"))
                .andExpect(model().attribute("mainPage", "admin/buildingModify.jsp"));
    }

    @Test
    void shouldGoToMainUser_whenCurrentUserIsUser_andActionIsModify() throws Throwable {
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("currentUserType", "user");
        this.mockMvc.perform(get("/building?action=modify")
                .sessionAttrs(sessionAttrs))
                .andExpect(status().isOk())
                .andExpect(view().name("mainUser"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mainUser.jsp"))
                .andExpect(model().attribute("mainPage", "user/blank.jsp"));
    }

    @Test
    void shouldGoToModify_whenCurrentUserIsAdmin_andActionIsModify() throws Throwable {
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("currentUserType", "admin");
        this.mockMvc.perform(get("/building?action=modify&id=5")
                .sessionAttrs(sessionAttrs))
                .andExpect(status().isOk())
                .andExpect(view().name("mainAdmin"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mainAdmin.jsp"))
                .andExpect(model().attribute("mainPage", "admin/buildingModify.jsp"))
                .andExpect(model().attribute("id", "5"));
    }

    static Stream<Arguments> bookingInfoProvider() {
        return Stream.of(
                Arguments.of(1, "2019-12-31", "5"),
                Arguments.of(2, "2020-1-31", "5"),
                Arguments.of(3, "2020-2-29", "5")
        );
    }
    @ParameterizedTest
    @MethodSource("bookingInfoProvider")
    void bookBuilding_whenUserIsUser(int buildingId, String startDate, String duration) throws Throwable {
        User currentUser = new User(10, "999", "999", "999", "MALE", "999");
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("currentUserType", "user");
        sessionAttrs.put("currentUser", currentUser);
        this.mockMvc.perform(post("/building?action=book")
                .sessionAttrs(sessionAttrs)
                .param("buildingId", String.valueOf(buildingId))
                .param("startDate", startDate)
                .param("duration", duration))
                .andExpect(status().isOk())
                .andExpect(view().name("mainUser"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/mainUser.jsp"))
                .andExpect(model().attribute("mainPage", "user/building.jsp"));
        Record bookRecord = this.recordDao.listRecord(20, false).get(0);
        assertEquals(buildingId, bookRecord.getBuildingId());
        this.recordDao.deleteRecord(bookRecord.getId());
    }

}
