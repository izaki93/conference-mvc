package com.pluralsight.conference.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class GreetingController {

    @GetMapping("greeting")
    public String greeting(Map<String ,Object> model){
        model.put("message","Hello Ibrahim");
        return "greeting";
    }

    @GetMapping("thyme")
    public String thyme (@Valid Map<String, Object> model) {
        model.put("message", "Hello Thymeleaf");
        return "thyme";
    }
}
