package com.app.spellcheck;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WelcomeController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String welcome(@RequestParam(value="name", required=false, defaultValue="Welcome to Spell Check App") String name, Model model) {
        model.addAttribute("name", name);
        return "home";
    }
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String hello(@RequestParam(value="name", required=false, defaultValue="Welcome to Spell Check App") String name, Model model) {
        model.addAttribute("name", name);
        return "home";
    }
}
