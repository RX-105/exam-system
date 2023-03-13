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

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PageController {
    @Value("${application.version}")
    private String version;

    @ModelAttribute
    public void addCommonAttributes(Model model) {
        model.addAttribute("version", version);
    }
    // 不应该使用通配符把所有URL都拦截到这里来，因为这样会误杀静态资源
    // 只能把用到的页面映射一下
    @GetMapping("/sessionAttr")
    @ResponseBody
    public String getSessionAttribute(String attribute, HttpServletRequest request) {
        return (String) request.getSession().getAttribute(attribute);
    }
    @GetMapping({"/admin/404", "/student/404"})
    public String get404View(){
        return "/404";
    }

    @GetMapping("/index")
    public String getIndexView() {
        return "/index";
    }
}
