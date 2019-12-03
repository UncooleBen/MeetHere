package com.webapp.service.database.dao;

import com.webapp.model.Buildng;
import java.util.List;

public interface BuildingDao {
  List<Buildng> listBuilding(int size);

  Buildng queryBuildingById(int id);

  boolean addBuilding(Buildng buildng);

  boolean deleteBuilding(int id);

  boolean updateBuilding(Buildng buildng);
}
