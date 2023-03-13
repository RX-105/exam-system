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

package io.n0sense.examsystem.controller;

import io.n0sense.examsystem.commons.constants.Status;
import io.n0sense.examsystem.entity.R;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommonRestController {
    private final HttpServletRequest request;
    @PostMapping("/logout")
    public R logout() {
        request.getSession().removeAttribute("username");
        request.getSession().removeAttribute("groupName");
        request.getSession().removeAttribute("role");
        request.getSession().setAttribute("remember", false);
        return R.builder()
                .status(Status.OK)
                .message("成功退出帐号。")
                .data(Map.of("location", "/index"))
                .build();
    }
}
