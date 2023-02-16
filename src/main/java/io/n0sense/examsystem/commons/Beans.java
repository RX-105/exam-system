package io.n0sense.examsystem.commons;

import io.n0sense.examsystem.commons.constants.Identities;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Beans {
    @Bean("groupMap")
    public Map<String, String> getGroupMap() {
        return Map.of(
                Identities.GROUP_SYSTEM_ADMIN.getKey(), "系统管理员",
                Identities.GROUP_ACADEMIC_AFFAIRS.getKey(), "教务管理员",
                Identities.GROUP_RECRUIT_AFFAIRS.getKey(), "招生管理员"
        );
    }
}
