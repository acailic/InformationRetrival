package app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class IndexController {

	@RequestMapping("/welcome")
    public String index() {
        return "welcome";
    }
   
	@RequestMapping("/403")
    public String unauthorized() {
        return "unauthorized";
    }
	
	@RequestMapping("/")
    public String home() {
        return "welcome";
    }
}
