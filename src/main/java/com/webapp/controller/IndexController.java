package com.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Juntao Peng
 */
@Controller
public class IndexController {

    @RequestMapping(value = {"/", "index"}, method = RequestMethod.GET)
    public String service() {
        return "index";
    }
}
