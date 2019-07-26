package com.springer.hack.exambuddy.ui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/")
public class UiController {

    @GetMapping
    public RedirectView index(){
        return new RedirectView("/swagger-ui.html");
    }

    @GetMapping("ui")
    public String graph(@RequestParam(required = false) String title, @RequestParam(required = false) Integer limit, Model model) {
        return "booksgraph";
    }
}
