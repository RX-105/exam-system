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
