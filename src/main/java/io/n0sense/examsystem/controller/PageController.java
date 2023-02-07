package io.n0sense.examsystem.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PageController {
    // 不应该使用通配符把所有URL都拦截到这里来，因为这样会误杀静态资源
    // 只能把用到的页面映射一下
    @GetMapping("/sessionAttr")
    @ResponseBody
    public String getSessionAttribute(String attribute, HttpServletRequest request) {
        return (String) request.getSession().getAttribute(attribute);
    }
    @GetMapping({"/admin/404", "/student/404"})
    public String get404View(){
        return "/404";
    }
}
