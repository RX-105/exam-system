package io.n0sense.examsystem.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.java.Log;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Log
public class GeneralErrorController implements ErrorController {
    public String getFullURL(HttpServletRequest request) {
        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
        String queryString = request.getQueryString();

        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }
    @GetMapping("/error")
    public ModelAndView resolveErrorView(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        HttpSession session = request.getSession();
        String path = getFullURL(request);
        String view;
        String username = (String) session.getAttribute("username"),
                role = (String) session.getAttribute("role");
        if (status != null) {
            int code = Integer.parseInt(status.toString());
            log.info("%s@%s访问%s时出现错误，代码%d".formatted(username, role, path, code));
            if (code == HttpStatus.NOT_FOUND.value()) {
                view = "/404";
            } else if (code == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                view = "/500";
            } else {
                view = "/error";
                model.addAttribute("status", code);
            }
        } else {
            log.info("%s@%s访问%s时出现错误，没有错误代码。".formatted(username, role, path));
            view = "/error";
        }
        return new ModelAndView(view);
    }
}
