package io.n0sense.examsystem.commons;

import io.n0sense.examsystem.commons.constants.Identities;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Beans {
    @Bean("groupMap")
    public Map<String, String> getGroupMap() {
        return Map.ofEntries(
                Identities.GROUP_SYSTEM_ADMIN,
                Identities.GROUP_ACADEMIC_AFFAIRS,
                Identities.GROUP_RECRUIT_AFFAIRS
        );
    }
}
