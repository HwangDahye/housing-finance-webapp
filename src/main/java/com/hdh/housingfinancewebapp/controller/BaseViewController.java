package com.hdh.housingfinancewebapp.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(path = {"", "/", "/login"})
public class BaseViewController {
  @RequestMapping(method = RequestMethod.GET)
  public String index(HttpServletRequest request, ModelMap modelMap) {
    modelMap.put("title", "Housing Finance Service");
    return "index";
  }
}
