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
