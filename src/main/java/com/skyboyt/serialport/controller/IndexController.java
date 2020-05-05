package com.skyboyt.serialport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Description: 首页调转接口
 * @Auther: tzx
 * @Date: 2020/4/30 17:37
 */
@Controller
public class IndexController {

    /**
     * 访问本地index页面
     *
     * @return
     */
    @GetMapping("/index")
    public String indexHtml() {
        return "/index.html";
    }
}
