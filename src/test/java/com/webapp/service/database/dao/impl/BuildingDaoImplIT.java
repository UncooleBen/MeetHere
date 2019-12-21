package com.webapp.service.database.dao.impl;

import com.webapp.model.Building;
import com.webapp.service.database.dao.BuildingDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BuildingDaoImplIT {

    private final String TEST_NAME_0 = "testName0";
    private final String TEST_NAME_1 = "testName1";
    private final String TEST_NAME_2 = "testName2";
    private final String TEST_DESCRIPTION_0 = "test description 0";
    private final String TEST_DESCRIPTION_1 = "test description 1";
    private final String TEST_DESCRIPTION_2 = "test description 2";
    private final String TEST_PRICE_0 = "100";
    private final String TEST_PRICE_1 = "200";
    private final String TEST_PRICE_2 = "300";
    private BuildingDao buildingDao;

    @BeforeEach
    void init() {
        buildingDao = new BuildingDaoImpl();
        String DB_URL =
                "jdbc:mysql://localhost:3306/meethere?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8";
        ReflectionTestUtils.setField(buildingDao, "dbUrl", DB_URL);
        String DB_USERNAME = "root";
        ReflectionTestUtils.setField(buildingDao, "dbUsername", DB_USERNAME);
        String DB_PASSWORD = "root";
        ReflectionTestUtils.setField(buildingDao, "dbPassword", DB_PASSWORD);
        String DB_CLASSNAME = "com.mysql.jdbc.Driver";
        ReflectionTestUtils.setField(buildingDao, "dbClassname", DB_CLASSNAME);
    }

    @Test
    void add3BuildingsAndGet5BuildingsShouldGet3Buildings() {
        // Add three records to database
        Building building0 = new Building(TEST_NAME_0, TEST_DESCRIPTION_0, TEST_PRICE_0);
        Building building1 = new Building(TEST_NAME_1, TEST_DESCRIPTION_1, TEST_PRICE_1);
        Building building2 = new Building(TEST_NAME_2, TEST_DESCRIPTION_2, TEST_PRICE_2);
        assertAll(
                () -> assertTrue(buildingDao.addBuilding(building0)),
                () -> assertTrue(buildingDao.addBuilding(building1)),
                () -> assertTrue(buildingDao.addBuilding(building2)));
        // Execute query
        List<Building> buildings = buildingDao.listBuilding(5);
        assertEquals(3, buildings.size());
        Building returnedBuilding0 = buildings.get(2);
        Building returnedBuilding1 = buildings.get(1);
        Building returnedBuilding2 = buildings.get(0);
        assertAll(
                () -> assertEquals(TEST_NAME_0, returnedBuilding0.getName()),
                () -> assertEquals(TEST_DESCRIPTION_0, returnedBuilding0.getDescription()),
                () -> assertEquals(TEST_PRICE_0, returnedBuilding0.getPrice()),
                () -> assertEquals(TEST_NAME_1, returnedBuilding1.getName()),
                () -> assertEquals(TEST_DESCRIPTION_1, returnedBuilding1.getDescription()),
                () -> assertEquals(TEST_PRICE_1, returnedBuilding1.getPrice()),
                () -> assertEquals(TEST_NAME_2, returnedBuilding2.getName()),
                () -> assertEquals(TEST_DESCRIPTION_2, returnedBuilding2.getDescription()),
                () -> assertEquals(TEST_PRICE_2, returnedBuilding2.getPrice()),
                // Remove test records from database
                () -> assertTrue(buildingDao.deleteBuilding(returnedBuilding0.getId())),
                () -> assertTrue(buildingDao.deleteBuilding(returnedBuilding1.getId())),
                () -> assertTrue(buildingDao.deleteBuilding(returnedBuilding2.getId())));
    }

    @Test
    void add3BuildingsAndGet2BuildingsShouldGet2Buildings() {
        // Add three records to database
        Building building0 = new Building(TEST_NAME_0, TEST_DESCRIPTION_0, TEST_PRICE_0);
        Building building1 = new Building(TEST_NAME_1, TEST_DESCRIPTION_1, TEST_PRICE_1);
        Building building2 = new Building(TEST_NAME_2, TEST_DESCRIPTION_2, TEST_PRICE_2);
        assertAll(
                () -> assertTrue(buildingDao.addBuilding(building0)),
                () -> assertTrue(buildingDao.addBuilding(building1)),
                () -> assertTrue(buildingDao.addBuilding(building2)));
        // Execute query
        List<Building> buildings = buildingDao.listBuilding(5);
        assertEquals(3, buildings.size());
        Building returnedBuilding0 = buildings.get(2);
        Building returnedBuilding1 = buildings.get(1);
        Building returnedBuilding2 = buildings.get(0);
        assertAll(
                () -> assertEquals(TEST_NAME_0, returnedBuilding0.getName()),
                () -> assertEquals(TEST_DESCRIPTION_0, returnedBuilding0.getDescription()),
                () -> assertEquals(TEST_PRICE_0, returnedBuilding0.getPrice()),
                () -> assertEquals(TEST_NAME_1, returnedBuilding1.getName()),
                () -> assertEquals(TEST_DESCRIPTION_1, returnedBuilding1.getDescription()),
                () -> assertEquals(TEST_PRICE_1, returnedBuilding1.getPrice()),
                () -> assertEquals(TEST_NAME_2, returnedBuilding2.getName()),
                () -> assertEquals(TEST_DESCRIPTION_2, returnedBuilding2.getDescription()),
                () -> assertEquals(TEST_PRICE_2, returnedBuilding2.getPrice()),
                // Remove test records from database
                () -> assertTrue(buildingDao.deleteBuilding(returnedBuilding0.getId())),
                () -> assertTrue(buildingDao.deleteBuilding(returnedBuilding1.getId())),
                () -> assertTrue(buildingDao.deleteBuilding(returnedBuilding1.getId() + 1)));
    }

    @Test
    void add1BuildingAndQueryById() {
        // Add three records to database
        Building building0 = new Building(TEST_NAME_0, TEST_DESCRIPTION_0, TEST_PRICE_0);
        assertAll(() -> assertTrue(buildingDao.addBuilding(building0)));
        // Execute query
        List<Building> buildings = buildingDao.listBuilding(1);
        assertEquals(1, buildings.size());
        Building returnedBuilding0 = buildings.get(0);
        int returnedId0 = returnedBuilding0.getId();
        Building building = buildingDao.queryBuildingById(returnedId0);
        assertAll(
                () -> assertEquals(TEST_NAME_0, building.getName()),
                () -> assertEquals(TEST_DESCRIPTION_0, building.getDescription()),
                () -> assertEquals(TEST_PRICE_0, building.getPrice()),
                // Remove test records from database
                () -> assertTrue(buildingDao.deleteBuilding(returnedId0)));
    }

    @Test
    void add1BuildingAndUpdateName() {
        // Add 1 record to database
        Building buildingIn = new Building(TEST_NAME_0, TEST_DESCRIPTION_0, TEST_PRICE_0);
        assertAll(() -> buildingDao.addBuilding(buildingIn));
        // Execute update
        List<Building> buildingList = buildingDao.listBuilding(1);
        assertAll(() -> assertEquals(1, buildingList.size()));
        Building building = buildingList.get(0);
        int id = building.getId();
        building.setName(TEST_NAME_1);
        building.setDescription(TEST_DESCRIPTION_1);
        building.setPrice(TEST_PRICE_1);
        assertAll(() -> assertTrue(buildingDao.updateBuilding(building)));
        Building returnedBuilding = buildingDao.queryBuildingById(id);
        assertAll(
                // Test updated data
                () -> assertEquals(TEST_NAME_1, returnedBuilding.getName()),
                () -> assertEquals(TEST_DESCRIPTION_1, returnedBuilding.getDescription()),
                () -> assertEquals(TEST_PRICE_1, returnedBuilding.getPrice()),
                // Remove test data from database
                () -> assertTrue(buildingDao.deleteBuilding(id)));
    }

    @AfterEach
    void restore() {
    }
}
