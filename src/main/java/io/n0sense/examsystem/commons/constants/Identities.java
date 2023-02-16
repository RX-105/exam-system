package io.n0sense.examsystem.commons.constants;

import java.util.AbstractMap;
import java.util.Map;

public interface Identities {
    Map.Entry<String, String> ROLE_STUDENT =
            new AbstractMap.SimpleEntry<>("student", "学生");
    Map.Entry<String, String> ROLE_ADMIN =
            new AbstractMap.SimpleEntry<>("admin", "管理员");

    Map.Entry<String, String> GROUP_SYSTEM_ADMIN =
            new AbstractMap.SimpleEntry<>("sudoers", "网站管理员");
    Map.Entry<String, String> GROUP_ACADEMIC_AFFAIRS =
            new AbstractMap.SimpleEntry<>("aca-aff", "教务管理员");
    Map.Entry<String, String> GROUP_RECRUIT_AFFAIRS =
            new AbstractMap.SimpleEntry<>("recruit", "招生管理员");
}