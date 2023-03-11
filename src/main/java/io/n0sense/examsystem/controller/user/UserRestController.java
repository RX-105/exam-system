package io.n0sense.examsystem.controller.user;

import io.n0sense.examsystem.annotation.RecordLog;
import io.n0sense.examsystem.commons.constants.Actions;
import io.n0sense.examsystem.commons.constants.Identities;
import io.n0sense.examsystem.commons.constants.Stages;
import io.n0sense.examsystem.commons.constants.Status;
import io.n0sense.examsystem.config.properties.DevProperties;
import io.n0sense.examsystem.entity.*;
import io.n0sense.examsystem.service.impl.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
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
    private final DevProperties devProperties;
    private final UserService userService;
    private final MajorService majorService;
    private final StageService stageService;
    private final FileService fileService;
    private final LogService logService;
    private final ThreadLocal<User> localUser = ThreadLocal.withInitial(() -> null);

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

    /**
     * 检查当前时间是否在用户报名学校的指定阶段时间内，如果在时间范围内，则返回null，否则返回一个{@link R}对象，
     * 包含错误原因和错误代码，可以用于直接返回。<br>
     * 此外，配置文件中的配置也会导致本方法的返回结果发生变化，比如application.debug.validate-stage-time
     * 置为false时，本方法将不会检查阶段时间，而是直接返回null。<br>
     * 如果返回值是null，该方法将会设置线程局部对象localUser。
     * @param stage 指定检查时间的阶段，在{@link Stages}里有定义。
     * @param schoolId 如果已知学校ID，可以传入，如果没有，将会尝试从登录用户信息中读取，否则返回包含错误信息的{@link R}对象。
     * @return 一个R对象，包含检查时的错误信息，如果没有错误，将会返回null。
     */
    public R checkStageValidity(Map.Entry<String, String> stage, Long schoolId) {
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
        localUser.set(opUser.get());
        // 确认是否检查阶段时间合法性
        if (!devProperties.getValidateStageTime()) {
            return null;
        }
        // 获取学校ID，判断是否在报名期间内
        if (schoolId != null) {
            stages = stageService.findAllStageBySchoolIdAndName(
                    schoolId, stage.getKey());
        } else if (localUser.get().getSchool() != null) {
            stages = stageService.findAllStageBySchoolIdAndName(
                    localUser.get().getSchool().getSchoolId(), stage.getKey());
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
                    .message("目标报考学校没有设置阶段[" + stage.getValue() + "]的时间，无法进行该操作。请联系报考学校。")
                    .build();
        } else {
            LocalDateTime now = LocalDateTime.now();
            boolean timeExceeded = true;
            for (Stage s : stages) {
                if (now.isAfter(s.getStartTime()) && now.isBefore(s.getEndTime())) {
                    timeExceeded = false;
                    break;
                }
            }
            if (timeExceeded) {
                return R.builder()
                        .status(Status.ERR_TIME_NOT_ALLOWED)
                        .message("当前时间不在该学校[" + stage.getValue() + "]阶段的时间范围内，不能进行该操作。")
                        .build();
            } else {
                return null;
            }
        }
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
                          Long zip, String contact, Long phone, LocalDate birthday) {
        R r = checkStageValidity(Stages.REGISTER, schoolId);
        if (r != null) {
            return r;
        }

        User user = localUser.get();
        if (schoolId != null) user.setSchool(School.builder().schoolId(schoolId).build());
        if (majorId != null) user.setMajor(Major.builder().id(majorId).build());
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
        if (birthday != null) user.setBirthday(birthday);
        user = userService.save(user);
        localUser.remove();

        return R.builder()
                .status(Status.OK)
                .message("修改完成。")
                .data(Map.of("user-info", user))
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
        R r = checkStageValidity(Stages.REGISTER, null);
        if (r != null) {
            return r;
        } else {
            String filename;
            try {
                filename = fileService.saveAvatar(file);
            } catch (IOException e) {
                return R.builder()
                        .status(Status.ERR_OPERATION_FAILED)
                        .message("文件保存失败。")
                        .build();
            }
            localUser.get().setAvatarName(filename);
            userService.save(localUser.get());
            localUser.remove();

            logService.record(Actions.UPDATE_AVATAR, filename, request);
            return R.builder()
                    .status(Status.OK)
                    .message("个人照片修改成功")
                    .data(Map.of("filename", filename))
                    .build();
        }
    }

    @GetMapping("/avatar/{filename}")
    public ResponseEntity<Object> getAvatar(@PathVariable("filename") String filename) {
        try {
            return getFile(fileService.getAvatar(filename), MediaType.IMAGE_PNG);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/avatar")
    public ResponseEntity<Object> getMyAvatar() {
        try {
            Resource defaultAvatar = new ClassPathResource("/static/img/user.jpg");
            Long uid = (Long) session.getAttribute("uid");
            if (uid == null) {
                return getFile(defaultAvatar, MediaType.IMAGE_JPEG);
            }
            User user = userService.findById(uid).orElseThrow();
            if (!StringUtils.hasLength(user.getAvatarName())) {
                return getFile(defaultAvatar, MediaType.IMAGE_JPEG);
            }
            return getFile(fileService.getAvatar(user.getAvatarName()), MediaType.IMAGE_PNG);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResponseEntity<Object> getFile(Resource file, MediaType mediaType) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("attachment;filename=\"%s", file.getFilename()));
        headers.add("Cache-Control", "no-cache,no-store,must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(mediaType)
                .body(file);
    }
}
