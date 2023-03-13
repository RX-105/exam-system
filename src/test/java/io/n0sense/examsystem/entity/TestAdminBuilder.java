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

package io.n0sense.examsystem.entity;

import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;

@Log
public class TestAdminBuilder {
    @Test
    void testBuilder(){
        Admin admin = Admin.builder()
                .name("demo")
                .password("1234")
                .groupName("sudoers")
                .schoolId(100000L)
                .build();
        log.info("admin.name = " + admin.getName());
    }
}
