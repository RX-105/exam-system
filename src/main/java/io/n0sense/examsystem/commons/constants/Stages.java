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

public interface Stages {
    Map.Entry<String, String> RELEASE =
            new AbstractMap.SimpleEntry<>("release", "招考信息发布");
    Map.Entry<String, String> REGISTER =
            new AbstractMap.SimpleEntry<>("register", "在线报名");
    Map.Entry<String, String> CONFIRM =
            new AbstractMap.SimpleEntry<>("confirm", "现场确认");
    Map.Entry<String, String> ASSIGN =
            new AbstractMap.SimpleEntry<>("assign", "考号与考场分配");
    Map.Entry<String, String> PREPARE_EXAM =
            new AbstractMap.SimpleEntry<>("prepare_exam", "准考证打印与考试");
    Map.Entry<String, String> ENTER =
            new AbstractMap.SimpleEntry<>("enter", "成绩录入");
    Map.Entry<String, String> SET_SCORE =
            new AbstractMap.SimpleEntry<>("set_score", "设置录取分数线");
    Map.Entry<String, String> QUERY =
            new AbstractMap.SimpleEntry<>("query", "成绩与录取查询");
    Map<String, String> stages =
            Map.ofEntries(RELEASE, REGISTER, CONFIRM, ASSIGN, PREPARE_EXAM, ENTER, SET_SCORE, QUERY);
}
