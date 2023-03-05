package io.n0sense.examsystem.controller.user;

import io.n0sense.examsystem.annotation.RecordLog;
import io.n0sense.examsystem.commons.constants.Actions;
import io.n0sense.examsystem.commons.constants.Identities;
import io.n0sense.examsystem.commons.constants.Stages;
import io.n0sense.examsystem.commons.constants.Status;
import io.n0sense.examsystem.entity.Major;
import io.n0sense.examsystem.entity.R;
import io.n0sense.examsystem.entity.Stage;
import io.n0sense.examsystem.entity.User;
import io.n0sense.examsystem.service.impl.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

@RestController()
@RequestMapping("/api/student")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Log
public class UserRestController {
    private final HttpServletRequest request;
    private final HttpSession session;
    private final UserService userService;
    private final MajorService majorService;
    private final StageService stageService;
    private final FileService fileService;
    private final LogService logService;

    @ExceptionHandler(NullPointerException.class)
    public R nullParameterHandler(NullPointerException e) {
        return R.builder()
                .status(Status.ERR_PARAMETER_NOT_PRESENT)
                .message("参数不完整。")
                .data(Map.of("location", "/student/404", "exception", e.getMessage()))
                .build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public R noElementFoundHandler() {
        return R.builder()
                .status(Status.ERR_NO_SUCH_ELEMENT)
                .message("请求查询的资源不存在。")
                .build();
    }

    @PostMapping("/checkUser/{username}")
    public R checkExistence(@PathVariable("username") String username) {
        if (userService.checkExistence(username)) {
            return R.builder()
                    .status(Status.OK)
                    .build();
        } else {
            return R.builder()
                    .status(Status.ERR_NO_SUCH_ELEMENT)
                    .build();
        }
    }

    @PostMapping("/login")
    @RecordLog(action = Actions.LOGIN, message = "主动登录")
    public R login(@NonNull String username, @NonNull String password,
                   Boolean remember) {
        int result = userService.login(username, password);
        if (result == 0) {
            // 判断为信息正确
            Long uid = userService.findByName(username).orElseThrow().getUserId();
            request.getSession().setAttribute("username", username);
            request.getSession().setAttribute("uid", uid);
            request.getSession().setAttribute("group", "student");
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
            request.getSession().setAttribute("group", "student");
            request.getSession().setAttribute("role", Identities.ROLE_STUDENT.getKey());

            Map<String, Object> data = new HashMap<>();
            data.put("location", "/" + Identities.ROLE_STUDENT.getKey() + "/home");
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

    @PostMapping("/school2major/{schoolId}")
    public R getMajorListBySchoolId(@PathVariable("schoolId") Long schoolId) {
        List<Major> majorList = majorService.findAllBySchoolId(schoolId);
        return R.builder()
                .status(Status.OK)
                .data(Map.of("schoolId", schoolId, "majors", majorList))
                .build();
    }

    @PostMapping("/registerExam")
    public R registerExam(Long schoolId, Long majorId, String realName, String identity,
                          String gender, String nationality, String politics, String source,
                          String home, String gradSchool, YearMonth gradTime, Boolean isCurrent,
                          Boolean isScience, String english, String mailName, String mailAddr,
                          Long zip, String contact, Long phone) {
        List<Stage> stages;
        Long uid = (Long) request.getSession().getAttribute("uid");
        if (uid == null) {
            return R.builder()
                    .status(Status.ERR_LOGIN_REQUIRED)
                    .message("需要登陆。")
                    .build();
        }
        Optional<User> opUser = userService.findById(uid);
        if (opUser.isEmpty()) {
            return R.builder()
                    .status(Status.ERR_NO_SUCH_ELEMENT)
                    .build();
        }
        User user = opUser.get();
        // 获取学校ID，判断是否在报名期间内
        // 先从参数获取，如果为null说明不更新id，然后再从用户信息中获取
        if (schoolId != null) {
            stages = stageService.findAllStageBySchoolIdAndName(
                    schoolId, Stages.REGISTER.getKey());
        } else if (user.getSchoolId() != null) {
            stages = stageService.findAllStageBySchoolIdAndName(
                    user.getSchoolId(), Stages.REGISTER.getKey());
        } else {
            return R.builder()
                    .status(Status.ERR_PARAMETER_NOT_PRESENT)
                    .message("没有设置学校参数。")
                    .data(Map.of("param", "schoolId"))
                    .build();
        }
        if (stages.size() == 0) {
            return R.builder()
                    .status(Status.ERR_TIME_NOT_FOUND)
                    .message("目标报考学校没有设置在线报名时间，报名失败。请联系报考学校。")
                    .build();
        } else {
            LocalDateTime now = LocalDateTime.now();
            boolean timeExceeded = true;
            for (Stage stage : stages) {
                if (now.isAfter(stage.getStartTime()) && now.isBefore(stage.getEndTime())) {
                    timeExceeded = false;
                    break;
                }
            }
            if (timeExceeded) {
                return R.builder()
                        .status(Status.ERR_TIME_NOT_ALLOWED)
                        .message("当前时间不在该学校在线报名时间范围内，不能报名。")
                        .build();
            }
        }

        if (schoolId != null) user.setSchoolId(schoolId);
        if (majorId != null) user.setMajor(majorId);
        if (StringUtils.hasLength(realName)) user.setRealname(realName);
        if (StringUtils.hasLength(identity)) user.setIdentityId(identity);
        if (StringUtils.hasLength(gender)) user.setGender(gender);
        if (StringUtils.hasLength(nationality)) user.setNationality(nationality);
        if (StringUtils.hasLength(politics)) user.setPoliticalStatus(politics);
        if (StringUtils.hasLength(source)) user.setSource(source);
        if (StringUtils.hasLength(home)) user.setHomeAddress(home);
        if (StringUtils.hasLength(gradSchool)) user.setGraduateSchool(gradSchool);
        if (gradTime != null) user.setGraduateTime(gradTime.atDay(1));
        if (isCurrent != null) user.setIsCurrent(isCurrent);
        if (isScience != null) user.setIsScience(isScience);
        if (StringUtils.hasLength(english)) user.setEnglishLevel(english);
        if (StringUtils.hasLength(mailName)) user.setReceiver(mailName);
        if (StringUtils.hasLength(mailAddr)) user.setContactAddress(mailAddr);
        if (zip != null) user.setZipcode(zip);
        if (StringUtils.hasLength(contact)) user.setContactNumber(contact);
        if (phone != null) user.setPhone(phone);

        userService.save(user);

        return R.builder()
                .status(Status.OK)
                .message("修改完成。")
                .build();
    }

    @PostMapping("/user/this")
    public R getMyInfo() {
        Long id = (Long) session.getAttribute("uid");
        if (id == null) {
            return R.builder()
                    .status(Status.ERR_LOGIN_REQUIRED)
                    .message("登录信息过期，请尝试重新登陆。")
                    .data(Map.of("location", "/"))
                    .build();
        }
        return R.builder()
                .status(Status.OK)
                .data(Map.of("user-info", userService.findById(id).orElseThrow()))
                .build();
    }

    @PostMapping("/user/{id}")
    public R getUserInfo(@PathVariable("id") @NonNull Long id) {
        return R.builder()
                .status(Status.OK)
                .data(Map.of("user-info", userService.findById(id).orElseThrow()))
                .build();
    }

    @PostMapping("/avatar")
    public R setAvatar(MultipartFile file) {
        String filename;
        try {
            filename = fileService.saveAvatar(file);
        } catch (IOException e) {
            return R.builder()
                    .status(Status.ERR_OPERATION_FAILED)
                    .message("文件保存失败。")
                    .build();
        }
        List<Stage> stages;
        Long uid = (Long) request.getSession().getAttribute("uid");
        if (uid == null) {
            return R.builder()
                    .status(Status.ERR_LOGIN_REQUIRED)
                    .message("需要登陆。")
                    .build();
        }
        Optional<User> opUser = userService.findById(uid);
        if (opUser.isEmpty()) {
            return R.builder()
                    .status(Status.ERR_NO_SUCH_ELEMENT)
                    .build();
        }
        User user = opUser.get();
        // 获取学校ID，判断是否在报名期间内
        if (user.getSchoolId() != null) {
            stages = stageService.findAllStageBySchoolIdAndName(
                    user.getSchoolId(), Stages.REGISTER.getKey());
        } else {
            return R.builder()
                    .status(Status.ERR_PARAMETER_NOT_PRESENT)
                    .message("请先填写报考学校。")
                    .data(Map.of("param", "schoolId"))
                    .build();
        }
        if (stages.size() == 0) {
            return R.builder()
                    .status(Status.ERR_TIME_NOT_FOUND)
                    .message("目标报考学校没有设置在线报名时间，不能修改信息。请联系报考学校。")
                    .build();
        } else {
            user.setAvatarName(filename);
            userService.save(user);
            logService.record(Actions.UPDATE_AVATAR, filename, request);
            return R.builder()
                    .status(Status.OK)
                    .message("个人照片修改成功")
                    .data(Map.of("filename", filename))
                    .build();
        }
    }

    @PostMapping("/avatar/{filename}")
    public ResponseEntity<Object> getAvatar(@PathVariable("filename") String filename) {
        Resource fileResource;
        try {
            fileResource = fileService.getAvatar(filename);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", String.format("attachment;filename=\"%s", filename));
            headers.add("Cache-Control", "no-cache,no-store,must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(fileResource.contentLength())
                    .contentType(MediaType.IMAGE_PNG)
                    .body(fileResource);
        } catch (Exception e) {
            return null;
        }
    }
}
