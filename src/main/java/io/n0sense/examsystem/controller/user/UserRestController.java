package io.n0sense.examsystem.controller.user;

import io.n0sense.examsystem.annotation.RecordLog;
import io.n0sense.examsystem.commons.constants.Actions;
import io.n0sense.examsystem.commons.constants.Identities;
import io.n0sense.examsystem.commons.constants.Status;
import io.n0sense.examsystem.entity.R;
import io.n0sense.examsystem.service.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController()
@RequestMapping("/api/student")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Log
public class UserRestController {
    private final UserService userService;
    private final HttpServletRequest request;

    @ExceptionHandler(NullPointerException.class)
    public R nullParameterHandler(NullPointerException e) {
        return R.builder()
                .status(Status.ERR_PARAMETER_NOT_PRESENT)
                .message("参数不完整。")
                .data(Map.of("location", "/student/404", "exception", e.getMessage()))
                .build();
    }

    @PostMapping("/login")
    @RecordLog(action = Actions.LOGIN, message = "主动登录")
    public R login(@NonNull String username, @NonNull String password,
                   Boolean remember) {
        int result = userService.login(username, password);
        if (result == 0) {
            // 判断为信息正确
            request.getSession().setAttribute("username", username);
            request.getSession().setAttribute("role", Identities.ROLE_STUDENT.getKey());
            if (remember != null && remember) {
                request.getSession().setAttribute("remember", true);
            }

            Map<String, Object> data = new HashMap<>();
            data.put("location", "/" + Identities.ROLE_STUDENT.getKey() + "/home");
            return R.builder()
                    .status(result)
                    .message("登陆成功。")
                    .data(data)
                    .build();
        } else if (result == Status.ERR_INCORRECT_PASSWORD
                || result == Status.ERR_USER_NOT_FOUND) {
            // 判断为密码错误或用户不存在，但是只能提示密码错误
            return R.builder()
                    .status(Status.ERR_INCORRECT_PASSWORD)
                    .message("密码错误。")
                    .build();
        } else {
            // 其他错误
            log.warning("login: 预期外的错误  " + result);
            return R.builder()
                    .status(Status.ERR_UNSPECIFIED)
                    .message("发生未知错误。")
                    .build();
        }
    }

    @PostMapping("/register")
    @RecordLog(action = Actions.REGISTER, message = "主动注册")
    public R register(@NonNull String username, @NonNull String password){
        int result = userService.register(username, password);
        if (result == Status.OK) {
            // 检查判断为注册成功
            request.getSession().setAttribute("username", username);
            request.getSession().setAttribute("role", Identities.ROLE_STUDENT.getKey());

            Map<String, Object> data = new HashMap<>();
            data.put("location", "/" + Identities.ROLE_ADMIN.getKey() + "/home");
            return R.builder()
                    .status(result)
                    .message("注册成功。")
                    .data(data)
                    .build();
        } else if (result == Status.ERR_USERNAME_IN_USE) {
            // 用户密码已经存在
            return R.builder()
                    .status(result)
                    .message("用户名已占用。")
                    .build();
        } else {
            // 其他错误
            log.warning("register: 预期外的错误  " + result);
            return R.builder()
                    .status(Status.ERR_UNSPECIFIED)
                    .message("发生未知错误。")
                    .build();
        }
    }
}
