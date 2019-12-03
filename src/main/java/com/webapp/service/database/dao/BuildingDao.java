package com.webapp.service.database.dao;

import com.webapp.model.Building;
import java.util.List;

public interface BuildingDao {
  List<Building> listBuilding(int size);

  Building queryBuildingById(int id);

  boolean addBuilding(Building building);

  boolean deleteBuilding(int id);

  boolean updateBuilding(Building building);
}
