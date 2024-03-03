/*
 * This file is part of exam-system.
 *
 * exam-system is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * exam-system is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 *  along with exam-system. If not, see <https://www.gnu.org/licenses/>.
 */

package io.n0sense.examsystem.service.impl;

import io.n0sense.examsystem.commons.constants.Identities;
import io.n0sense.examsystem.commons.constants.Status;
import io.n0sense.examsystem.dto.BasicUserDTO;
import io.n0sense.examsystem.dto.ExamUserDTO;
import io.n0sense.examsystem.entity.*;
import io.n0sense.examsystem.repository.*;
import io.n0sense.examsystem.service.IAdminService;
import io.n0sense.examsystem.util.IpUtil;
import io.n0sense.examsystem.util.PasswordEncoder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 实现管理员用户服务的类。
 * @author kazuha
 * @since 2022/11/23
 */
@Service
@RequiredArgsConstructor
public class AdminService implements IAdminService {
    private final AdminRepository adminRepository;
    private final RegistryRepository registryRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final HttpServletRequest request;

    /**
     * 添加一个新用户，并添加注册表项。
     * @param username 用户名
     * @param password 用户密码
     * @param groupName 组名
     * @return <code>CommonStatus</code>下定义的状态值，可能取值有<code>OK, ERR_USERNAME_IN_USE</code>。
     */
    @Override
    public int register(String username, String password, String groupName, Long schoolId){
        if ((username.equals(Identities.GROUP_SYSTEM_ADMIN.getKey()) && schoolId != 100000L) ||
                (!username.equals(Identities.GROUP_SYSTEM_ADMIN.getKey()) && schoolId == 100000L)) {
            return Status.ERR_PARAMETER_NOT_MATCH;
        }
        else if (adminRepository.existsAdminByName(username)){
            return Status.ERR_USERNAME_IN_USE;
        } else {
            Admin admin = adminRepository.save(
                    Admin.builder()
                    .name(username)
                    .password(PasswordEncoder.SHA256Encode(password))
                    .groupName(groupName)
                    .schoolId(schoolId)
                    .build()
            );
            Registry registry = new Registry(
                    admin.getAdminId(),
                    admin.getName(),
                    admin.getPassword(),
                    IpUtil.getIpAddress(request),
                    LocalDateTime.now()
            );
            registryRepository.save(registry);
            return Status.OK;
        }
    }

    /**
     * 检查给定的信息是否匹配。
     * @param username 用户名
     * @param password 用户密码
     * @return <code>CommonStatus</code>下定义的状态值，可能取值有<code>OK, ERR_INCORRECT_PASSWORD, ERR_USER_NOT_FOUNT</code>。
     */
    @Override
    public int login(String username, String password){
        Optional<Admin> admin = adminRepository.findAdminByName(username);
        if (admin.isPresent()){
            String encodedPassword = PasswordEncoder.SHA256Encode(password);
            if (encodedPassword.equals(admin.get().getPassword())){
                return Status.OK;
            } else {
                return Status.ERR_INCORRECT_PASSWORD;
            }
        } else {
            return Status.ERR_USER_NOT_FOUND;
        }
    }

    @Override
    public Optional<Admin> findByName(String name) {
        return this.adminRepository.findByName(name);
    }

    @Override
    public Optional<Admin> findById(Long id) {
        return adminRepository.findById(id);
    }

    @Override
    public Page<Admin> getAllAdmins(int page, int size) {
        return adminRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Page<Admin> getAllAdmins(String excludeGroupName, int page, int size) {
        return adminRepository.findAllByGroupNameIsNotContaining(excludeGroupName, PageRequest.of(page, size));
    }

    @Override
    public boolean resetPassword(Long id, HttpServletRequest request) {
        Optional<Admin> optionalAdmin = adminRepository.findById(id);
        if (optionalAdmin.isPresent()){
            Admin admin = optionalAdmin.get();
            admin.setPassword(PasswordEncoder.SHA256Encode("1234"));
            adminRepository.save(admin);
            Registry adminRegistry = registryRepository.findById(admin.getAdminId()).orElse(
                    Registry.builder()
                            .userId(admin.getAdminId())
                            .username(admin.getName())
                            .password(admin.getPassword())
                            .ip(IpUtil.getIpAddress(request))
                            .time(LocalDateTime.now())
                            .build()
            );
            adminRegistry.setPassword(admin.getPassword());
            registryRepository.save(adminRegistry);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void dropUser(Long id) {
        adminRepository.deleteById(id);
    }

    @Override
    public boolean checkExistence(String username) {
        return adminRepository.existsAdminByName(username);
    }

    @Override
    public void studentConfirm(Long uid) {
        Optional<User> user = userRepository.findById(uid);
        if (user.isPresent()) {
            User u = user.get();
            u.setIsConfirmed(true);
            userRepository.save(u);
        }
    }

    /**
     * 获取指定学校中现场确认和没有现场确认的人数。
     * @param schoolId 指定学校的ID
     * @return long数组，包含两个元素，下标为0的元素是已现场确认的人数，下标为1的元素是未确认的人数。
     */
    @Override
    public long[] getConfirmData(Long schoolId) {
        long[] data = new long[2];
        long confirmed = userRepository.countBySchoolAndIsConfirmedTrue(School.builder().schoolId(schoolId).build());
        long total = userRepository.countBySchool(School.builder().schoolId(schoolId).build());
        data[0] = confirmed;
        data[1] = total - confirmed;
        return data;
    }

    @Override
    public void assignTickets(Long schoolId) {
        List<BasicUserDTO> userList = userRepository.findBySchoolAndIsConfirmedTrue(School.builder().schoolId(schoolId).build());
        Random random = new Random();
        for (BasicUserDTO user : userList) {
            Long room = random.longs(0,100).findFirst().getAsLong();
            Long seat = random.longs(0,60).findFirst().getAsLong();
            userRepository.updateSeatAndRoom(seat, room, user.getUserId());
        }
    }

    @Override
    public Page<ExamUserDTO> getExamUserInfo(Long schoolId, int page, int size) {
        return userRepository.findAllBySchoolAndIsConfirmedTrue(new School(schoolId), PageRequest.of(page, size));
    }

    /**
     * 将学校ID指定的学校报考学生考场座位信息导出为Excel表格。
     * @param schoolId 学校ID
     * @return 使用Optional包裹的Resource对象。如果正常获得文件，则Optional内的对象不为空。
     * 为空的情况有：当前学校没有已确认的考生；无法创建临时文件；发生IO异常。
     * @throws IOException 如果无法处理IO操作，则会抛出这个异常。
     */
    @Override
    public Optional<Resource> exportExamUserInfo(Long schoolId) throws IOException {
        List<ExamUserDTO> userDTOList = userRepository.findAllBySchoolAndIsConfirmedTrue(
                new School(schoolId), Pageable.unpaged()).toList();
        if (userDTOList.isEmpty()) {
            return Optional.empty();
        }
        String schoolName = userDTOList.get(0).getSchool().getName();
        String fileName = "";
        Optional<FileInputStream> optional = Optional.empty();
        int counter = 0;
        while (optional.isEmpty()) {
            // 创建文件尝试次数不能超过100次，否则直接返回empty
            counter++;
            if (counter > 100) {
                return Optional.empty();
            }
            LocalDate seed = LocalDate.now().plusDays(counter);
            String suffix = PasswordEncoder.MD5Encode(seed.toString()).substring(0, 6);
            fileName = seed.getYear() + "年" + schoolName + "考试座位安排表-" + suffix + ".xlsx";
            optional = fileService.readTempFile(fileName);
        }

        // 创建工作簿和工作表
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("座位安排表");

        // 添加表头
        Row head = sheet.createRow(0);
        head.createCell(0).setCellValue("准考证编号");
        head.createCell(1).setCellValue("姓名");
        head.createCell(2).setCellValue("证件号码");
        head.createCell(3).setCellValue("报考学校");
        head.createCell(4).setCellValue("报考专业");
        head.createCell(5).setCellValue("考场号");
        head.createCell(6).setCellValue("座位号");

        // 添加表体数据
        for (int i = 0; i < userDTOList.size(); i++) {
            Row row = sheet.createRow(i+1);
            row.createCell(0).setCellValue(userDTOList.get(i).getUserId());
            row.createCell(1).setCellValue(userDTOList.get(i).getRealname());
            row.createCell(2).setCellValue(userDTOList.get(i).getIdentityId());
            row.createCell(3).setCellValue(userDTOList.get(i).getSchool().getName());
            row.createCell(4).setCellValue(userDTOList.get(i).getMajor().getName());
            row.createCell(5).setCellValue(userDTOList.get(i).getRoomId());
            row.createCell(6).setCellValue(userDTOList.get(i).getSeatId());
        }

        // 根据之前的文件名拉取FileOutputStream，并保存工作簿
        FileOutputStream os;
        try {
            Optional<FileOutputStream> opOs = fileService.writeTempFile(fileName);
            if (opOs.isEmpty()){
                return Optional.empty();
            }
            os = opOs.get();
        } catch (FileNotFoundException e) {
            return Optional.empty();
        }
        workbook.write(os);
        workbook.close();

        try {
            return Optional.of(fileService.getTempFile(fileName));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
