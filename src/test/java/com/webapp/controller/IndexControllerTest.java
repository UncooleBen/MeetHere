package com.webapp.controller;

import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class IndexControllerTest {

    private IndexController indexController = new IndexController();


    @Test
    public void service_test() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        assertEquals(indexController.service(request), "index");
    }

}