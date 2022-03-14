package webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import webapp.service.CategoryService;

@Controller
public class IndexController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/")
    public String main(){
        categoryService.categorysave();
        return "main";
    }
}
