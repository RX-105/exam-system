package io.n0sense.examsystem.controller;

import io.n0sense.examsystem.commons.constants.Status;
import io.n0sense.examsystem.entity.R;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommonRestController {
    private final HttpServletRequest request;
    @PostMapping("/logout")
    public R logout() {
        request.getSession().removeAttribute("username");
        request.getSession().removeAttribute("groupName");
        request.getSession().removeAttribute("role");
        request.getSession().setAttribute("remember", false);
        return R.builder()
                .status(Status.OK)
                .message("成功退出帐号。")
                .data(Map.of("location", "/index"))
                .build();
    }
}
