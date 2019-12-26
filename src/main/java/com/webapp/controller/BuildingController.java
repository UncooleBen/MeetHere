package com.webapp.controller;

import com.webapp.filter.LoginFilter;
import com.webapp.model.Building;
import com.webapp.model.Record;
import com.webapp.model.user.User;
import com.webapp.service.database.dao.BuildingDao;
import com.webapp.service.database.dao.RecordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


/**
 * @author Juntao Peng
 */
@Controller
public class BuildingController {

    BuildingDao buildingDao;
    RecordDao recordDao;
    SimpleDateFormat sdf;

    @Autowired
    public BuildingController(BuildingDao buildingDao, RecordDao recordDao) {
        this.buildingDao = buildingDao;
        this.recordDao = recordDao;
        this.sdf = new SimpleDateFormat("yyyy-MM-dd");
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
        if (!"list".equals(action) && !"book".equals(action)) {
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
            case "book":
                bookBuilding(mv, request, session);
                listBuildings(mv, currentUserType);
                break;
            case "list":
            default:
                listBuildings(mv, currentUserType);
        }
        return mv;
    }

    private void bookBuilding(ModelAndView mv, HttpServletRequest request, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        String buildingId = request.getParameter("buildingId");
        String startDate = request.getParameter("startDate");
        String duration = request.getParameter("duration");
        int userId = currentUser.getId();
        long currentTime = System.currentTimeMillis();
        try {
            long startDateInLong = this.sdf.parse(startDate).getTime();
            long endDateInLong = (long) Double.parseDouble(duration) * 3600 * 1000 * 24 + startDateInLong;
            Record record = new Record(currentTime, startDateInLong, endDateInLong, userId,
                    Integer.parseInt(buildingId), false);
            this.recordDao.addRecord(record);
        } catch (ParseException pe) {
            pe.printStackTrace(System.err);
        }
    }

    private void listBuildings(ModelAndView mv, String currentUserType) {
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

    private void deleteBuilding(ModelAndView mv, int id) {
        this.buildingDao.deleteBuilding(id);
    }

    private void modifyBuilding(ModelAndView mv, int id) {
        Building building = this.buildingDao.queryBuildingById(id);
        mv.addObject("mainPage", "admin/buildingModify.jsp");
        mv.addObject("id", String.valueOf(id));
        mv.addObject("building", building);
    }

    private void addBuilding(ModelAndView mv) {
        mv.addObject("mainPage", "admin/buildingModify.jsp");
        mv.addObject("id", null);
        mv.addObject("building", null);
    }

    private void saveBuilding(ModelAndView mv, HttpServletRequest request) {
        String name = request.getParameter("buildingName");
        String description = request.getParameter("buildingDescription");
        String price = request.getParameter("buildingPrice");
        String idStr = request.getParameter("buildingId");
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
