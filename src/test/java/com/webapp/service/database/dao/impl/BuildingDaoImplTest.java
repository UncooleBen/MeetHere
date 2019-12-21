package com.webapp.service.database.dao.impl;

import com.webapp.model.Building;
import com.webapp.service.database.dao.BuildingDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class BuildingDaoImplTest {
    private BuildingDao buildingDao;
    private Connection connection = mock(Connection.class);
    private PreparedStatement preparedStatement = mock(PreparedStatement.class);
    private SQLException test_sql_exception;
    private ResultSet rs = mock(ResultSet.class);
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    private PrintStream originalOut;
    private PrintStream originalErr;


    class TestableBuildingDaoImpl extends BuildingDaoImpl {
        @Override
        public Connection getConnection() {
            return connection;
        }
    }

    @BeforeEach
    void init() {
        this.buildingDao = new TestableBuildingDaoImpl();
        this.test_sql_exception = new SQLException();
        this.outContent = new ByteArrayOutputStream();
        this.errContent = new ByteArrayOutputStream();
        this.originalOut = System.out;
        this.originalErr = System.err;
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void tear_down() throws IOException {
        System.setErr(this.originalErr);
        System.setOut(this.originalOut);
        this.outContent.close();
        this.errContent.close();
    }

    @Test
    void test_throws_sql_exception_when_list_building() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        this.buildingDao.listBuilding(5);
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Test
    void test_when_list_building() throws SQLException {
        int id = 305;
        String name = "PengGe";
        String description = "PGNB";
        String price = "999";

        Building building = new Building();
        building.setId(id);
        building.setName(name);
        building.setDescription(description);
        building.setPrice(price);

        List<Building> buildingList = new ArrayList<>();
        buildingList.add(building);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        when(preparedStatement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, false); /* First call returns true, second call returns false */
        when(rs.getInt("id")).thenReturn(id);
        when(rs.getString("name")).thenReturn(name);
        when(rs.getString("description")).thenReturn(description);
        when(rs.getString("price")).thenReturn(price);


        assertEquals(buildingList, this.buildingDao.listBuilding(5));
        verify(preparedStatement).setInt(1, 5);

    }

    @Test
    void test_throws_sql_exception_query_Building_By_Id() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        this.buildingDao.queryBuildingById(5);
        assertTrue(errContent.toString().contains("java.sql.SQLException"));

    }


    @Test
    void test_query_Building_By_Id() throws SQLException {
        int id = 305;
        String name = "PengGe";
        String description = "PGNB";
        String price = "999";

        Building building = new Building();
        building.setId(id);
        building.setName(name);
        building.setDescription(description);
        building.setPrice(price);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, false); /* First call returns true, second call returns false */
        when(rs.getInt("id")).thenReturn(id);
        when(rs.getString("name")).thenReturn(name);
        when(rs.getString("description")).thenReturn(description);
        when(rs.getString("price")).thenReturn(price);


        assertEquals(building, this.buildingDao.queryBuildingById(5));
        verify(preparedStatement).setInt(1, 5);
    }

    @Test
    void test_throws_sql_exception_when_add_Building() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        this.buildingDao.addBuilding(new Building());
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Test
    void test_add_Building_When_Result_Is_True() throws SQLException {
        int id = 305;
        String name = "PengGe";
        String description = "PGNB";
        String price = "999";

        Building building = new Building();
        building.setId(id);
        building.setName(name);
        building.setDescription(description);
        building.setPrice(price);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = this.buildingDao.addBuilding(building);

        assertEquals(result, true);
        verify(preparedStatement).setString(1, name);
        verify(preparedStatement).setString(2, description);
        verify(preparedStatement).setString(3, price);


    }

    @Test
    void test_add_Building_When_Result_Is_False() throws SQLException {
        int id = 305;
        String name = "PengGe";
        String description = "PGNB";
        String price = "999";


        Building building = new Building();
        building.setId(id);
        building.setName(name);
        building.setDescription(description);
        building.setPrice(price);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        boolean result = this.buildingDao.addBuilding(building);

        assertEquals(result, false);
        verify(preparedStatement).setString(1, name);
        verify(preparedStatement).setString(2, description);
        verify(preparedStatement).setString(3, price);


    }

    @Test
    void test_delete_Building_When_Result_Is_True() throws SQLException {

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = this.buildingDao.deleteBuilding(5);

        assertEquals(result, true);
        verify(preparedStatement).setInt(1, 5);
    }

    @Test
    void test_delete_Building_When_Result_Is_False() throws SQLException {

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        boolean result = this.buildingDao.deleteBuilding(5);

        assertEquals(result, false);
        verify(preparedStatement).setInt(1, 5);
    }

    @Test
    void test_throws_sql_exception_when_deleteBuilding() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        this.buildingDao.deleteBuilding(5);
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Test
    void test_throws_sql_exception_when_updateBuilding() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(test_sql_exception);
        this.buildingDao.updateBuilding(new Building());
        assertTrue(errContent.toString().contains("java.sql.SQLException"));
    }

    @Test
    void test_update_Building_When_Result_Is_True() throws SQLException {
        int id = 305;
        String name = "PengGe";
        String description = "PGNB";
        String price = "999";

        Building building = new Building();
        building.setId(id);
        building.setName(name);
        building.setDescription(description);
        building.setPrice(price);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = this.buildingDao.updateBuilding(building);

        assertEquals(result, true);
        verify(preparedStatement).setString(1, name);
        verify(preparedStatement).setString(2, description);
        verify(preparedStatement).setString(3, price);
        verify(preparedStatement).setInt(4, id);

    }

    @Test
    void test_update_Building_When_Result_Is_False() throws SQLException {
        int id = 305;
        String name = "PengGe";
        String description = "PGNB";
        String price = "999";

        Building building = new Building();
        building.setId(id);
        building.setName(name);
        building.setDescription(description);
        building.setPrice(price);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        boolean result = this.buildingDao.updateBuilding(building);

        assertEquals(result, false);
        verify(preparedStatement).setString(1, name);
        verify(preparedStatement).setString(2, description);
        verify(preparedStatement).setString(3, price);
        verify(preparedStatement).setInt(4, id);

    }

}