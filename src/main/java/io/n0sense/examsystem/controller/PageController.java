package io.n0sense.examsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PageController {
    @GetMapping("/{path:^.*(?!static)}")
    public String page(@PathVariable String path) {
        return path;
    }

    @GetMapping("/{path1:^.*(?!static)}/{path2}")
    public String page(@PathVariable String path1, @PathVariable String path2) {
        return path1 + "/" + path2;
    }

    @GetMapping({"/{path1:^.*(?!static)}/{path2}/{path3}"})
    public String page(@PathVariable String path1, @PathVariable String path2, @PathVariable String path3) {
        return path1 + "/" + path2 + "/" + path3;
    }

    @GetMapping("/sessionAttr")
    @ResponseBody
    public String getSessionAttribute(String attribute, HttpServletRequest request) {
        return (String) request.getSession().getAttribute(attribute);
    }
}
