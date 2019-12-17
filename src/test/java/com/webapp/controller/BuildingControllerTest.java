package com.webapp.controller;

import com.webapp.filter.LoginFilter;
import com.webapp.model.Record;
import com.webapp.model.user.User;
import com.webapp.service.database.dao.BuildingDao;
import com.webapp.service.database.dao.RecordDao;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;

class BuildingControllerTest {

        private BuildingDao buildingDao = mock(BuildingDao.class);
        private RecordDao recordDao=mock(RecordDao.class);
        private HttpServletRequest request=mock(HttpServletRequest.class);
        private HttpSession session=mock(HttpSession.class);

        private BuildingController buildingController =new BuildingController(buildingDao,recordDao);


       //@Test
      /*  public void service_test()
        {
            String action="";
            when(session.getAttribute("currentUserType")).thenReturn(result);
          //  when(LoginFilter.isAuthorized("","",)).Then
            ModelAndView result=buildingController.service(action,request,session);
        }*/

      //  @Test
      /*  public void bookBuilding_Test()
        {
            ModelAndView input=new ModelAndView();

            int id=305;
            User user=new User();
            user.setId(id);
            when(session.getAttribute("currentUser")).thenReturn(user);
            when(request.getParameter("buildingId")).thenReturn("");
            when(request.getParameter("startDate")).thenReturn("");
            when(request.getParameter("duration")).thenReturn(new Object());
            long currentTime = System.currentTimeMillis();

            buildingController.bookBuilding(input,request,session);
            Record record=new Record(currentTime,currentTime,currentTime,userId,Integer.parseInt(buildingId),false);
            verify(recordDao).addRecord(record);

        }*/
}