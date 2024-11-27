package com.gmail.leejaewon264.accessdatabase;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String home() {
        return "download"; // templates/download.html 파일을 렌더링
    }
}

