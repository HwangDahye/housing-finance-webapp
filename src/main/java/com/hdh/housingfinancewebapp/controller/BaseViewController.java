package com.hdh.housingfinancewebapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = {"", "/", "/login"})
public class BaseViewController {
  @GetMapping
  public String index(ModelMap modelMap) {
    modelMap.put("title", "Housing Finance Service");
    return "index";
  }
}
