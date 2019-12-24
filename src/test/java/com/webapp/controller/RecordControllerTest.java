package com.webapp.controller;

import com.webapp.model.Record;
import com.webapp.model.user.User;
import com.webapp.service.database.dao.RecordDao;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RecordControllerTest {


    private RecordDao recordDao = mock(RecordDao.class);
    private HttpServletRequest request = mock(HttpServletRequest.class);
    private HttpSession session = mock(HttpSession.class);

    private RecordController recordController = new RecordController(recordDao);

    @Test
    public void service_WhenUserTypeIsUser_ActionIsAdd() {
        String action = "add";
        String usertype = "user";

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUserType")).thenReturn(usertype);


        ModelAndView result = recordController.service(request, action);

        assertAll(
                () -> assertEquals(result.getViewName(), "mainUser"),
                () -> assertEquals(result.getModelMap().get("mainPage"), "user/recordAdd.jsp")
        );
    }

    @Test
    public void service_WhenUserTypeIsUser_ActionIsSave() {
        String action = "save";
        String usertype = "user";
        User user = mock(User.class);


        Record record = new Record();
        record.setBuildingId(Integer.parseInt("13"));
        record.setUserId(Integer.parseInt("100"));
        long startDate = Long.parseLong("1");
        long endDate = Long.parseLong("1001");
        record.setStartDate(startDate);
        record.setEndDate(endDate);
        record.setTime(endDate - startDate);
        record.setVerified(false);
        when(user.getId()).thenReturn(100);
        when(session.getAttribute("currentUser")).thenReturn(user);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUserType")).thenReturn(usertype);

        when(request.getParameter("buildingId")).thenReturn("13");
        when(request.getParameter("userId")).thenReturn("305");
        when(request.getParameter("startDate")).thenReturn("1");
        when(request.getParameter("endDate")).thenReturn("1001");


        ModelAndView result = recordController.service(request, action);

        verify(recordDao).addRecord(record);
        assertAll(
                () -> assertEquals(result.getViewName(), "mainUser"),
                () -> assertEquals(result.getModelMap().get("mainPage"), "user/record.jsp")
        );
    }

    @Test
    public void service_WhenUserTypeIsUser_ActionIsDelete() {
        String action = "delete";
        String usertype = "user";

        User user = mock(User.class);
        when(user.getId()).thenReturn(100);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUserType")).thenReturn(usertype);
        when(session.getAttribute("currentUser")).thenReturn(user);
        when(request.getParameter("recordId")).thenReturn("305");


        ModelAndView result = recordController.service(request, action);

        verify(recordDao).deleteRecord(305);
        assertAll(
                () -> assertEquals(result.getViewName(), "mainUser"),
                () -> assertEquals(result.getModelMap().get("mainPage"), "user/record.jsp")
        );
    }

    @Test
    public void service_WhenUserTypeIsUser_ActionIsList() {
        String action = "list";
        String usertype = "user";
        User user = new User();
        user.setId(305);

        List<Record> verifiedRecordList = new LinkedList<Record>();
        List<Record> unverifiedRecordList = new LinkedList<Record>();
        verifiedRecordList.add(new Record());
        unverifiedRecordList.add(new Record());

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUserType")).thenReturn(usertype);
        when(session.getAttribute("currentUser")).thenReturn(user);


        when(recordDao.listRecord(20, true)).thenReturn(verifiedRecordList);
        when(recordDao.listRecordWithUserId(20, 305, false)).thenReturn(unverifiedRecordList);

        ModelAndView result = recordController.service(request, action);

        assertAll(
                () -> assertEquals(result.getViewName(), "mainUser"),
                () -> assertEquals(result.getModelMap().get("mainPage"), "user/record.jsp"),
                () -> assertEquals(result.getModelMap().get("verifiedRecordList"), verifiedRecordList),
                () -> assertEquals(result.getModelMap().get("unverifiedRecordList"), unverifiedRecordList)
        );
    }

    @Test
    public void service_WhenUserTypeIsUser_ActionIsDefault() {
        String action = "default";
        String usertype = "user";
        User user = new User();
        user.setId(305);

        List<Record> verifiedRecordList = new LinkedList<Record>();
        List<Record> unverifiedRecordList = new LinkedList<Record>();
        verifiedRecordList.add(new Record());
        unverifiedRecordList.add(new Record());


        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUserType")).thenReturn(usertype);
        when(session.getAttribute("currentUser")).thenReturn(user);

        when(recordDao.listRecord(20, true)).thenReturn(verifiedRecordList);
        when(recordDao.listRecordWithUserId(20, 305, false)).thenReturn(unverifiedRecordList);

        ModelAndView result = recordController.service(request, action);

        assertAll(
                () -> assertEquals(result.getViewName(), "mainUser"),
                () -> assertEquals(result.getModelMap().get("mainPage"), "user/record.jsp"),
                () -> assertEquals(result.getModelMap().get("verifiedRecordList"), verifiedRecordList),
                () -> assertEquals(result.getModelMap().get("unverifiedRecordList"), unverifiedRecordList)
        );
    }

    @Test
    public void service_WhenUserTypeIsAdmin_ActionIsDelete() {
        String action = "delete";
        String usertype = "admin";

        List<Record> verifiedRecordList = new LinkedList<Record>();
        List<Record> unverifiedRecordList = new LinkedList<Record>();
        verifiedRecordList.add(new Record());
        unverifiedRecordList.add(new Record());

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUserType")).thenReturn(usertype);

        when(request.getParameter("recordId")).thenReturn("305");
        when(recordDao.listRecord(20, true)).thenReturn(verifiedRecordList);
        when(recordDao.listRecord(20, false)).thenReturn(unverifiedRecordList);

        ModelAndView result = recordController.service(request, action);

        verify(recordDao).deleteRecord(305);
        assertAll(
                () -> assertEquals(result.getViewName(), "mainAdmin"),
                () -> assertEquals(result.getModelMap().get("mainPage"), "admin/record.jsp"),
                () -> assertEquals(result.getModelMap().get("verifiedRecordList"), verifiedRecordList),
                () -> assertEquals(result.getModelMap().get("unverifiedRecordList"), unverifiedRecordList)

        );
    }

    @Test
    public void service_WhenUserTypeIsAdmin_ActionIsVetify() {
        String action = "verify";
        String usertype = "admin";

        Record record = new Record();
        record.setVerified(true);


        List<Record> verifiedRecordList = new LinkedList<Record>();
        List<Record> unverifiedRecordList = new LinkedList<Record>();
        verifiedRecordList.add(new Record());
        unverifiedRecordList.add(new Record());

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUserType")).thenReturn(usertype);

        when(request.getParameter("recordId")).thenReturn("305");
        when(recordDao.queryRecordById(305)).thenReturn(record);

        when(recordDao.listRecord(20, true)).thenReturn(verifiedRecordList);
        when(recordDao.listRecord(20, false)).thenReturn(unverifiedRecordList);

        ModelAndView result = recordController.service(request, action);

        verify(recordDao).updateRecord(record);
        assertAll(
                () -> assertEquals(result.getViewName(), "mainAdmin"),
                () -> assertEquals(result.getModelMap().get("mainPage"), "admin/record.jsp"),
                () -> assertEquals(result.getModelMap().get("verifiedRecordList"), verifiedRecordList),
                () -> assertEquals(result.getModelMap().get("unverifiedRecordList"), unverifiedRecordList)

        );
    }

    @Test
    public void service_WhenUserTypeIsAdmin_ActionIsList() {
        String action = "list";
        String usertype = "admin";


        List<Record> verifiedRecordList = new LinkedList<Record>();
        List<Record> unverifiedRecordList = new LinkedList<Record>();
        verifiedRecordList.add(new Record());
        unverifiedRecordList.add(new Record());

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUserType")).thenReturn(usertype);


        when(recordDao.listRecord(20, true)).thenReturn(verifiedRecordList);
        when(recordDao.listRecord(20, false)).thenReturn(unverifiedRecordList);

        ModelAndView result = recordController.service(request, action);
        assertAll(
                () -> assertEquals(result.getViewName(), "mainAdmin"),
                () -> assertEquals(result.getModelMap().get("mainPage"), "admin/record.jsp"),
                () -> assertEquals(result.getModelMap().get("verifiedRecordList"), verifiedRecordList),
                () -> assertEquals(result.getModelMap().get("unverifiedRecordList"), unverifiedRecordList)

        );
    }


    @Test
    public void service_WhenUserTypeIsAdmin_ActionIsOther() {
        String action = "other";
        String usertype = "admin";


        List<Record> verifiedRecordList = new LinkedList<Record>();
        List<Record> unverifiedRecordList = new LinkedList<Record>();
        verifiedRecordList.add(new Record());
        unverifiedRecordList.add(new Record());

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUserType")).thenReturn(usertype);


        when(recordDao.listRecord(20, true)).thenReturn(verifiedRecordList);
        when(recordDao.listRecord(20, false)).thenReturn(unverifiedRecordList);

        ModelAndView result = recordController.service(request, action);

        assertAll(
                () -> assertEquals(result.getViewName(), "mainAdmin"),
                () -> assertEquals(result.getModelMap().get("mainPage"), "admin/record.jsp"),
                () -> assertEquals(result.getModelMap().get("verifiedRecordList"), verifiedRecordList),
                () -> assertEquals(result.getModelMap().get("unverifiedRecordList"), unverifiedRecordList)

        );
    }


}