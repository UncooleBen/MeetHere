package com.webapp.controller;

import com.webapp.model.Record;
import com.webapp.model.user.User;
import com.webapp.service.database.dao.RecordDao;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Shangzhen Li
 */
@Controller
public class RecordController {

  RecordDao recordDao;

  @Autowired
  public RecordController(RecordDao recordDao) {
    this.recordDao = recordDao;
  }

  @RequestMapping("/record")
  public ModelAndView service(
      HttpServletRequest request, @RequestParam(value = "action") String action) {
    ModelAndView mv = new ModelAndView();
    HttpSession session = request.getSession();
    String userType = (String) session.getAttribute("currentUserType");
    if ("user".equals(userType)) {
      mv.setViewName("mainUser");
      userRecordService(mv, action, request);
    } else if ("admin".equals(userType)) {
      mv.setViewName("mainAdmin");
      adminRecordService(mv, action, request);
    }
    return mv;
  }

  private void adminRecordService(ModelAndView mv, String action, HttpServletRequest request) {
    switch (action) {
      case "verify":
        int id = Integer.parseInt(request.getParameter("recordId"));
        Record record = recordDao.queryRecordById(id);
        record.setVerified(true);
        recordDao.updateRecord(record);
        listRecord(mv, true, -1);
        mv.addObject("mainPage", "admin/record.jsp");
        break;
      case "delete":
        int recordId = Integer.parseInt(request.getParameter("recordId"));
        recordDao.deleteRecord(recordId);
        listRecord(mv, true, -1);
        mv.addObject("mainPage", "admin/record.jsp");
        break;
      case "list":
      default:
        listRecord(mv, true, -1);
        mv.addObject("mainPage", "admin/record.jsp");
        break;
    }
  }

  private void userRecordService(ModelAndView mv, String action, HttpServletRequest request) {

    switch (action) {
      case "add":
        mv.addObject("mainPage", "user/recordAdd.jsp");
        break;
      case "save": {
        Record record = new Record();
        record.setBuildingId(Integer.parseInt(request.getParameter("buildingId")));
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");
        int userId = user.getId();
        record.setUserId(userId);
        long startDate = Long.parseLong(request.getParameter("startDate"));
        long endDate = Long.parseLong(request.getParameter("endDate"));
        record.setStartDate(startDate);
        record.setEndDate(endDate);
        record.setTime(endDate - startDate);
        record.setVerified(false);
        recordDao.addRecord(record);
        listRecord(mv, false, userId);
        mv.addObject("mainPage", "user/record.jsp");
        break;
      }
      case "delete": {
        int recordId = Integer.parseInt(request.getParameter("recordId"));
        recordDao.deleteRecord(recordId);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");
        int userId = user.getId();
        listRecord(mv, false, userId);
        mv.addObject("mainPage", "user/record.jsp");
        break;
      }
      case "list":
      default: {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");
        int userId = user.getId();
        listRecord(mv, false, userId);
        mv.addObject("mainPage", "user/record.jsp");
      }
      break;
    }
  }

  private void listRecord(ModelAndView mv, boolean isAdmin, int userId) {
    List<Record> verifiedRecordList = recordDao.listRecord(20, true);
    if (isAdmin) {
      List<Record> unverifiedRecordList = recordDao.listRecord(20, false);
      mv.addObject("verifiedRecordList", verifiedRecordList);
      mv.addObject("unverifiedRecordList", unverifiedRecordList);
    } else {
      mv.addObject("verifiedRecordList", verifiedRecordList);
      if (userId >= 0) {
        List<Record> unverifiedRecordList = recordDao.listRecordWithUserId(20, userId, false);
        mv.addObject("unverifiedRecordList", unverifiedRecordList);
      }
    }
  }
}
