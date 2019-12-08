package com.webapp.controller;

import com.webapp.model.Record;
import com.webapp.service.database.dao.impl.RecordDaoImpl;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Shangzhen Li
 */
@Controller
public class RecordController {
	// TODO 5. action = verify (admin), add (user)
	// list (both), delete (both)
	/*todo: need chcek*/

	private RecordDaoImpl recordDao;

	@RequestMapping("/record")
	public ModelAndView service(
			HttpServletRequest request, @RequestParam(value = "action") String action) {
		ModelAndView mv = new ModelAndView();
		HttpSession session = request.getSession();
		String userType = (String) session.getAttribute("currentUserType");
		if ("user".equals(userType)) {
			userRecordService(mv, action, request);
		} else if ("admin".equals(userType)) {
			adminRecordService(mv, action, request);
		}
		return mv;
	}

	private void adminRecordService(ModelAndView mv, String action, HttpServletRequest request) {
		switch (action) {
			case "verify":
				Record record = new Record();
				record.setId(Integer.parseInt(request.getParameter("recordId")));
				record.setBuildingId(Integer.parseInt(request.getParameter("buildingId")));
				record.setUserId(Integer.parseInt(request.getParameter("userId")));
				record.setStartDate(Long.parseLong(request.getParameter("startDate")));
				record.setEndDate(Long.parseLong(request.getParameter("endDate")));
				record.setTime(Long.parseLong(request.getParameter("time")));
				record.setVerified(true);
				recordDao.updateRecord(record);
				mv.setViewName("admin/record");
				break;
			case "delete":
				int recordId = Integer.parseInt(request.getParameter("recordId"));
				recordDao.deleteRecord(recordId);
				mv.setViewName("admin/record");
				break;
			case "list":
			default:
				listRecord(mv);
				mv.setViewName("admin/record");
				break;
		}
	}

	private void userRecordService(ModelAndView mv, String action, HttpServletRequest request) {
		switch (action) {
			case "add":
				mv.setViewName("user/recordAdd");
				break;
			case "save":
				Record record = new Record();
				record.setId(Integer.parseInt(request.getParameter("recordId")));
				record.setBuildingId(Integer.parseInt(request.getParameter("buildingId")));
				record.setUserId(Integer.parseInt(request.getParameter("userId")));
				record.setStartDate(Long.parseLong(request.getParameter("startDate")));
				record.setEndDate(Long.parseLong(request.getParameter("endDate")));
				record.setTime(Long.parseLong(request.getParameter("time")));
				record.setVerified(false);
				recordDao.updateRecord(record);
				mv.setViewName("admin/record");
				break;
			case "delete":
				int recordId = Integer.parseInt(request.getParameter("recordId"));
				recordDao.deleteRecord(recordId);
				mv.setViewName("user/record");
				break;
			case "list":
			default:
				listRecord(mv);
				mv.setViewName("user/record");
				break;
		}
	}

	private void listRecord(ModelAndView mv) {
		List<Record> recordList = recordDao.listRecord(20);
		if (recordList != null) {
			mv.addObject("recordList", recordList);
		}
	}
}
