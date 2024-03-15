package com.example.slshopping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {

    /**
     * HOME画面表示
     */
    @GetMapping("/home")
    public String viewHomePage() {
        return "index";
    }

    /**
     * ログイン画面表示
     */
    @GetMapping("/loginForm")
    public String viewLoginPage() {
        return "login";
    }

}
