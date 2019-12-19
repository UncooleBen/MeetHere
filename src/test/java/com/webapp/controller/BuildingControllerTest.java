package com.webapp.controller;

import com.webapp.filter.LoginFilter;
import com.webapp.model.Building;
import com.webapp.model.Record;
import com.webapp.model.user.User;
import com.webapp.service.database.dao.BuildingDao;
import com.webapp.service.database.dao.RecordDao;
import org.junit.jupiter.api.Test;

import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BuildingControllerTest {

        private BuildingDao buildingDao = mock(BuildingDao.class);
        private RecordDao recordDao=mock(RecordDao.class);
        private HttpServletRequest request=mock(HttpServletRequest.class);
        private HttpSession session=mock(HttpSession.class);

        private BuildingController buildingController =new BuildingController(buildingDao,recordDao);


       @Test
        public void service_WhenIsAuthorized()
        {
            String action="";
            String currentUserType="";
            when(session.getAttribute("currentUserType")).thenReturn(currentUserType);
            ModelAndView result=buildingController.service(action,request,session);
            assertNotNull(result);
        }

        @Test
        public void service_WhenActionIsNotListAndBook()
        {
                String action="sds";
                String currentUserType="user";

                when(session.getAttribute("currentUserType")).thenReturn(currentUserType);
                when(request.getParameter("id")).thenReturn("305");


                ModelAndView result=buildingController.service(action,request,session);
                assertNotNull(result);
        }

        @Test
        public void service_WhenActionIsDelete_UserIsAdmin()
        {
                String action="delete";
                String currentUserType="admin";

                List<Building> buildingList=new LinkedList<>();
                buildingList.add(new Building());

                when(session.getAttribute("currentUserType")).thenReturn(currentUserType);
                when(request.getParameter("id")).thenReturn("305");
                when(buildingDao.listBuilding(20)).thenReturn(buildingList);


                ModelAndView result=buildingController.service(action,request,session);
                verify(buildingDao).deleteBuilding(305);

                assertAll(
                        ()->assertEquals(result.getViewName(),"mainAdmin"),
                        ()->assertEquals(result.getModelMap().get("mainPage"),"admin/building.jsp"),
                        ()->assertEquals(result.getModelMap().get("buildingList"),buildingList)
                );
        }

        @Test
        public void service_WhenActionIsModify_UserIsAdmin()
        {
                String action="modify";
                String currentUserType="admin";

                Building building=new Building();

                when(session.getAttribute("currentUserType")).thenReturn(currentUserType);
                when(request.getParameter("id")).thenReturn("305");
                when(buildingDao.queryBuildingById(305)).thenReturn(building);


                ModelAndView result=buildingController.service(action,request,session);

                assertAll(
                        ()->assertEquals(result.getViewName(),"mainAdmin"),
                        ()->assertEquals(result.getModelMap().get("mainPage"),"admin/buildingModify.jsp"),
                        ()->assertEquals(result.getModelMap().get("id"),"305"),
                        ()->assertEquals(result.getModelMap().get("building"),building)
                );
        }

        @Test
        public void service_WhenActionIsAdd_UserIsAdmin()
        {
                String action="add";
                String currentUserType="admin";


                when(session.getAttribute("currentUserType")).thenReturn(currentUserType);
                when(request.getParameter("id")).thenReturn("305");



                ModelAndView result=buildingController.service(action,request,session);

                assertAll(
                        ()->assertEquals(result.getViewName(),"mainAdmin"),
                        ()->assertEquals(result.getModelMap().get("mainPage"),"admin/buildingModify.jsp"),
                        ()->assertEquals(result.getModelMap().get("id"),null),
                        ()->assertEquals(result.getModelMap().get("building"),null)
                );
        }


        @Test
        public void service_WhenActionIsSave_UserIsAdmin_IDStrIsNotNull()
        {
                String action="save";
                String currentUserType="admin";

                List<Building> buildingList=new LinkedList<>();
                buildingList.add(new Building());

                Building building=new Building("ECNU","none","999");
                building.setId(305);

                when(session.getAttribute("currentUserType")).thenReturn(currentUserType);
                when(request.getParameter("id")).thenReturn("305");

                when(request.getParameter("buildingName")).thenReturn("ECNU");
                when(request.getParameter("buildingDescription")).thenReturn("none");
                when(request.getParameter("buildingPrice")).thenReturn("999");
                when(request.getParameter("buildingId")).thenReturn("305");

                when(buildingDao.listBuilding(20)).thenReturn(buildingList);


                ModelAndView result=buildingController.service(action,request,session);
                verify(buildingDao).updateBuilding(building);

                assertAll(
                        ()->assertEquals(result.getViewName(),"mainAdmin"),
                        ()->assertEquals(result.getModelMap().get("mainPage"),"admin/building.jsp"),
                        ()->assertEquals(result.getModelMap().get("buildingList"),buildingList)
                );
        }

        @Test
        public void service_WhenActionIsSave_UserIsAdmin_IDStrIsNull()
        {
                String action="save";
                String currentUserType="admin";

                List<Building> buildingList=new LinkedList<>();
                buildingList.add(new Building());

                Building building=new Building("ECNU","none","999");

                when(session.getAttribute("currentUserType")).thenReturn(currentUserType);
                when(request.getParameter("id")).thenReturn("305");

                when(request.getParameter("buildingName")).thenReturn("ECNU");
                when(request.getParameter("buildingDescription")).thenReturn("none");
                when(request.getParameter("buildingPrice")).thenReturn("999");
                when(request.getParameter("buildingId")).thenReturn("");

                when(buildingDao.listBuilding(20)).thenReturn(buildingList);


                ModelAndView result=buildingController.service(action,request,session);
                verify(buildingDao).addBuilding(building);

                assertAll(
                        ()->assertEquals(result.getViewName(),"mainAdmin"),
                        ()->assertEquals(result.getModelMap().get("mainPage"),"admin/building.jsp"),
                        ()->assertEquals(result.getModelMap().get("buildingList"),buildingList)
                );
        }

        @Test
        public void service_WhenActionIsList_UserIsAdmin()
        {
                String action="list";
                String currentUserType="admin";
                List<Building> buildingList=new LinkedList<>();
                buildingList.add(new Building());



                when(session.getAttribute("currentUserType")).thenReturn(currentUserType);
                when(buildingDao.listBuilding(20)).thenReturn(buildingList);


                ModelAndView result=buildingController.service(action,request,session);

                assertAll(
                        ()->assertEquals(result.getViewName(),"mainAdmin"),
                        ()->assertEquals(result.getModelMap().get("mainPage"),"admin/building.jsp"),
                        ()->assertEquals(result.getModelMap().get("buildingList"),buildingList)
                );
        }

        @Test
        public void service_WhenActionIsList_UserIsUser()
        {
                String action="list";
                String currentUserType="user";
                List<Building> buildingList=new LinkedList<>();
                buildingList.add(new Building());



                when(session.getAttribute("currentUserType")).thenReturn(currentUserType);
                when(buildingDao.listBuilding(20)).thenReturn(buildingList);


                ModelAndView result=buildingController.service(action,request,session);

                assertAll(
                        ()->assertEquals(result.getViewName(),"mainUser"),
                        ()->assertEquals(result.getModelMap().get("mainPage"),"user/building.jsp"),
                        ()->assertEquals(result.getModelMap().get("buildingList"),buildingList)
                );
        }
        @Test
        public void service_WhenActionIsOther_UserIsAdmin()
        {
                String action="other";
                String currentUserType="admin";
                List<Building> buildingList=new LinkedList<>();
                buildingList.add(new Building());



                when(session.getAttribute("currentUserType")).thenReturn(currentUserType);
                when(buildingDao.listBuilding(20)).thenReturn(buildingList);


                ModelAndView result=buildingController.service(action,request,session);

                assertAll(
                        ()->assertEquals(result.getViewName(),"mainAdmin"),
                        ()->assertEquals(result.getModelMap().get("mainPage"),"admin/building.jsp"),
                        ()->assertEquals(result.getModelMap().get("buildingList"),buildingList)
                );
        }

        @Test
        public void service_WhenActionIsBook_UserIsUser()
        {
                //TODO 测不了
                String action="book";
                String currentUserType="user";

                List<Building> buildingList=new LinkedList<>();
                buildingList.add(new Building());

                User user =new User();

                Record record=new Record();

                when(session.getAttribute("currentUserType")).thenReturn(currentUserType);
                when(buildingDao.listBuilding(20)).thenReturn(buildingList);

                when(session.getAttribute("currentUser")).thenReturn(user);
                when(request.getParameter("buildingId")).thenReturn("305");
                when(request.getParameter("startDate")).thenReturn("");
                when(request.getParameter("duration")).thenReturn("");


                ModelAndView result=buildingController.service(action,request,session);
                //TODO
                verify(recordDao).addRecord(record);

                assertAll(
                        ()->assertEquals(result.getViewName(),"mainUser"),
                        ()->assertEquals(result.getModelMap().get("mainPage"),"user/building.jsp"),
                        ()->assertEquals(result.getModelMap().get("buildingList"),buildingList)
                );
        }

        @Test
        public void service_WhenActionIsBook_UserIsAdmin()
        {
                //TODO 测不了
                String action="book";
                String currentUserType="admin";

                List<Building> buildingList=new LinkedList<>();
                buildingList.add(new Building());

                User user =new User();

                Record record=new Record();

                when(session.getAttribute("currentUserType")).thenReturn(currentUserType);
                when(buildingDao.listBuilding(20)).thenReturn(buildingList);

                when(session.getAttribute("currentUser")).thenReturn(user);
                when(request.getParameter("buildingId")).thenReturn("305");
                when(request.getParameter("startDate")).thenReturn("");
                when(request.getParameter("duration")).thenReturn("");


                ModelAndView result=buildingController.service(action,request,session);
                //TODO
                verify(recordDao).addRecord(record);

                assertAll(
                        ()->assertEquals(result.getViewName(),"mainAdmin"),
                        ()->assertEquals(result.getModelMap().get("mainPage"),"user/building.jsp"),
                        ()->assertEquals(result.getModelMap().get("buildingList"),buildingList)
                );
        }

}