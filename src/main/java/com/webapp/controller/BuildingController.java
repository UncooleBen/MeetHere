package com.webapp.controller;

import com.webapp.filter.LoginFilter;
import com.webapp.model.Building;
import com.webapp.service.database.dao.BuildingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class BuildingController {

    BuildingDao buildingDao;

    @Autowired
    public BuildingController(BuildingDao buildingDao) {
        this.buildingDao = buildingDao;
    }

    @RequestMapping("/building")
    public ModelAndView service(@RequestParam("action") String action, HttpServletRequest request,
                                HttpSession session) {
        ModelAndView mv = new ModelAndView();
        String currentUserType = (String) session.getAttribute("currentUserType");
        boolean isAuthorized = LoginFilter.isAuthorized(currentUserType, "user", mv); /* Filter not login*/
        if (!isAuthorized) {
            return mv;
        }
        if (!"list".equals(action)) {
            isAuthorized = LoginFilter.isAuthorized(currentUserType, "admin", mv); /* Filter not login*/
            if (!isAuthorized) {
                return mv;
            }
        }
        String idStr = request.getParameter("id");
        int id = 0;
        if (idStr != null && idStr.length() != 0) {
            id = Integer.parseInt(idStr);
        }
        mv.setViewName("mainAdmin");
        switch (action) {
            case "delete":
                deleteBuilding(mv, id);
                listBuildings(mv, currentUserType);
                break;
            case "modify":
                modifyBuilding(mv, id);
                break;
            case "add":
                addBuilding(mv);
                break;
            case "save":
                saveBuilding(mv, request);
                listBuildings(mv, currentUserType);
                break;
            case "list":
            default:
                listBuildings(mv, currentUserType);
        }
        return mv;
    }

    public void listBuildings(ModelAndView mv, String currentUserType) {
        List<Building> buildingList = this.buildingDao.listBuilding(20);
        if ("admin".equals(currentUserType)) {
            mv.setViewName("mainAdmin");
            mv.addObject("mainPage", "admin/building.jsp");
        } else {
            mv.setViewName("mainUser");
            mv.addObject("mainPage", "user/building.jsp");
        }
        mv.addObject("buildingList", buildingList);
    }

    public void deleteBuilding(ModelAndView mv, int id) {
        this.buildingDao.deleteBuilding(id);
    }

    public void modifyBuilding(ModelAndView mv, int id) {
        Building building = this.buildingDao.queryBuildingById(id);
        mv.addObject("mainPage", "admin/buildingModify.jsp");
        mv.addObject("id", String.valueOf(id));
        mv.addObject("building", building);
    }

    public void addBuilding(ModelAndView mv) {
        mv.addObject("mainPage", "admin/buildingModify.jsp");
        mv.addObject("id", null);
        mv.addObject("building", null);
    }

    public void saveBuilding(ModelAndView mv, HttpServletRequest request) {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String price = request.getParameter("price");
        String idStr = request.getParameter("id");
        Building building = new Building(name, description, price);
        if (idStr != null && idStr.length() != 0) {
            int id = Integer.valueOf(idStr);
            building.setId(id);
            this.buildingDao.updateBuilding(building);
        } else {
            this.buildingDao.addBuilding(building);
        }
    }
}
