package com.webapp.controller;

import com.webapp.model.Building;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.webapp.service.database.dao.BuildingDao;
import com.webapp.service.database.dao.impl.RecordDaoImpl;
import com.webapp.model.Record;
import com.webapp.model.User;
import com.webapp.util.DbUtil;
import com.webapp.util.StringUtil;

@Controller
public class RecordController {

	@Value("${spring.datasource.url}")
	private String dbUrl;

	@Value("${spring.datasource.username}")
	private String dbUsername;

	@Value("${spring.datasource.password}")
	private String dbPassword;

	@Value("${spring.datasource.driver-class-name}")
	private String dbClassname;

	private DbUtil dbUtil;

	private RecordDaoImpl recordDao = new RecordDaoImpl();
	private com.webapp.service.database.dao.impl.RecordDaoImpl recordFinalDao = com.webapp.service.database.dao.impl.RecordDaoImpl;

	@RequestMapping("/record")
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();
		Object currentUserType = session.getAttribute("currentUserType");
		String s_userText = request.getParameter("s_userText");
		String buildId = request.getParameter("buildToSelect");
		String searchType = request.getParameter("searchType");
		String action = request.getParameter("action");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");

		Record record = new Record();
		if ("add".equals(action)) {
			recordPreSave(request, response);
			return;
		} else if ("preSave".equals(action)) {
			recordPreSave(request, response);
			return;
		} else if ("save".equals(action)) {
			recordSave(request, response);
			return;
		} else if ("delete".equals(action)) {
			recordDelete(request, response);
			return;
		} else if ("approve".equals(action)) {
			recordFinalSave(request, response);
			return;
		} else if ("list".equals(action)) {
			if (StringUtil.isNotEmpty(s_userText)) {
				if ("name".equals(searchType)) {
					record.setUserName(s_userText);
				} else if ("number".equals(searchType)) {
					record.setUserNumber(s_userText);
				} else if ("dorm".equals(searchType)) {
					record.setRoomName(s_userText);
				}
			}
			if (StringUtil.isNotEmpty(buildId)) {
				record.setBuildId(Integer.parseInt(buildId));
			}
			session.removeAttribute("s_userText");
			session.removeAttribute("searchType");
			session.removeAttribute("buildToSelect");
			request.setAttribute("s_userText", s_userText);
			request.setAttribute("searchType", searchType);
			request.setAttribute("buildToSelect", buildId);
		} else if ("search".equals(action)) {
			if (StringUtil.isNotEmpty(s_userText)) {
				if ("name".equals(searchType)) {
					record.setUserName(s_userText);
				} else if ("number".equals(searchType)) {
					record.setUserNumber(s_userText);
				} else if ("dorm".equals(searchType)) {
					record.setRoomName(s_userText);
				}
				session.setAttribute("s_userText", s_userText);
				session.setAttribute("searchType", searchType);
			} else {
				session.removeAttribute("s_userText");
				session.removeAttribute("searchType");
			}
			if (StringUtil.isNotEmpty(startDate)) {
				record.setStartDate(startDate);
				session.setAttribute("startDate", startDate);
			} else {
				session.removeAttribute("startDate");
			}
			if (StringUtil.isNotEmpty(endDate)) {
				record.setEndDate(endDate);
				session.setAttribute("endDate", endDate);
			} else {
				session.removeAttribute("endDate");
			}
			if (StringUtil.isNotEmpty(buildId)) {
				record.setBuildId(Integer.parseInt(buildId));
				session.setAttribute("buildToSelect", buildId);
			} else {
				session.removeAttribute("buildToSelect");
			}
		}
		Connection con = null;
		try {
			dbUtil = new DbUtil(dbUrl, dbClassname, dbUsername, dbPassword);
			con = dbUtil.getCon();
			if ("admin".equals(currentUserType)) {
				List<Record> recordList = recordDao.recordList(con, record);
				List<Record> recordFinalList = recordFinalDao.recordList(con, record);
				List<Building> buildingList = recordDao.buildList(con);
				List<Building> buildingFinalList = recordFinalDao.buildList(con);
				buildingList.removeAll(buildingFinalList);
				buildingList.addAll(buildingFinalList);
				request.setAttribute("buildingList", buildingList);
				request.setAttribute("recordList", recordList);
				request.setAttribute("recordFinalList", recordFinalList);
				request.setAttribute("mainPage", "admin/record.jsp");
				request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
			} else if ("user".equals(currentUserType)) {
				User user = (User) (session.getAttribute("currentUser"));
				List<Record> recordList = recordDao.recordListWithNumber(con, record, user.getUserNumber());
				List<Record> recordFinalList = recordFinalDao.recordListWithNumber(con, record, user.getUserNumber());
				request.setAttribute("recordList", recordList);
				request.setAttribute("recordFinalList", recordFinalList);
				request.setAttribute("mainPage", "user/record.jsp");
				request.getRequestDispatcher("mainUser.jsp").forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void recordDelete(HttpServletRequest request, HttpServletResponse response) {
		String recordId = request.getParameter("recordId");
		Connection con = null;
		try {
			con = dbUtil.getCon();
			recordDao.recordDelete(con, recordId);
			request.getRequestDispatcher("record?action=list").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void recordSave(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		BuildingDao buildingDao = new BuildingDao();
		String recordId = request.getParameter("recordId");
		User user1 = (User) (session.getAttribute("currentUser"));
		String userNumber = user1.getUserNumber();
		String date = request.getParameter("date");
		String buildId = request.getParameter("buildId");
		String roomName = request.getParameter("roomName");
		String detail = request.getParameter("detail");
		Record record = new Record(userNumber, date, detail);
		record.setBuildId(Integer.parseInt(buildId));
		if (StringUtil.isNotEmpty(recordId)) {
			if (Integer.parseInt(recordId) != 0) {
				record.setRecordId(Integer.parseInt(recordId));
			}
		}
		Connection con = null;
		try {
			con = dbUtil.getCon();
			int saveNum = 0;
			Statement stmt = con.createStatement();
			record.setBuildId(buildingDao.buildShow(con, buildId).getBuildId());
			record.setUserName(user1.getName());
			record.setRoomName(roomName);
			if (StringUtil.isNotEmpty(recordId) && Integer.parseInt(recordId) != 0) {
				saveNum = recordDao.recordUpdate(con, record);
			} else {
				saveNum = recordDao.recordAdd(con, record);
			}
			if (saveNum > 0) {
				request.getRequestDispatcher("record?action=list").forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void recordPreSave(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String recordId = request.getParameter("recordId");
		String userNumber = request.getParameter("userNumber");
		Connection con = null;
		BuildingDao buildingDao = new BuildingDao();
		try {
			con = dbUtil.getCon();
			Building building = new Building();
			List<Building> buildingList = buildingDao.buildAllList(con, building);
			request.setAttribute("buildingList", buildingList);
			if (StringUtil.isNotEmpty(recordId)) {
				Record record = recordDao.recordShow(con, recordId);
				request.setAttribute("record", record);
			} else {
				Calendar rightNow = Calendar.getInstance();
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
				String sysDatetime = fmt.format(rightNow.getTime());
				request.setAttribute("userNumber", userNumber);
				request.setAttribute("date", sysDatetime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		request.setAttribute("mainPage", "user/recordSave.jsp");
		request.getRequestDispatcher("mainUser.jsp").forward(request, response);
	}

	private void recordFinalSave(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String recordId = request.getParameter("recordId");
		Record record = new Record();
		if (StringUtil.isNotEmpty(recordId)) {
			if (Integer.parseInt(recordId) != 0) {
				record.setRecordId(Integer.parseInt(recordId));
			}
		}
		Connection con = null;
		try {
			con = dbUtil.getCon();
			int saveNum = 0;
			int buildId = 0;
			String name = "";
			Statement stmt = con.createStatement();
			ResultSet rs;
			rs = stmt.executeQuery(
					"select recordId, userNumber, userName, buildId, roomName, date, detail  from t_record");
			while (rs.next()) {
				if (rs.getInt(1) == (record.getRecordId())) {
					record.setUserNumber(rs.getString(2));
					record.setUserName(rs.getString(3));
					record.setBuildId(rs.getInt(4));
					record.setRoomName(rs.getString(5));
					record.setDate(rs.getString(6));
					record.setDetail(rs.getString(7));
					break;
				}
			}
			rs = stmt.executeQuery("select buildId, buildName from t_build");
			while (rs.next()) {
				if (rs.getString(1).equals(record.getBuildId())) {
					record.setBuildName(rs.getString(2));
					break;
				}
			}
			if (StringUtil.isNotEmpty(recordId) && Integer.parseInt(recordId) != 0) {
				saveNum = recordFinalDao.recordAdd(con, record);
				recordDao.recordDelete(con, recordId);
				String sql = "update t_user set buildId=?, roomName=? where name=?";
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setString(3, record.getUserName());
				pstmt.setString(2, record.getRoomName());
				pstmt.setInt(1, record.getBuildId());
				pstmt.executeUpdate();
			}
			if (saveNum > 0) {
				request.getRequestDispatcher("record?action=list").forward(request, response);
			} else {
				request.setAttribute("record", record);
				request.setAttribute("error", "����ʧ��");
				request.setAttribute("mainPage", "admin/record.jsp");
				request.getRequestDispatcher("record?action=list").forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}