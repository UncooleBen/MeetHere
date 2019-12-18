package com.webapp.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IndexControllerTest {

    private IndexController indexController=new IndexController();


    @Test
    public void service_test()
    {
        assertEquals(indexController.service(),"index");
    }

}