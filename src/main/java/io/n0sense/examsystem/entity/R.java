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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * R=Response<br>
 * 用于Web请求中使用的响应实体类，这个类包含三个成员。<br>
 * status - 用于表示本次响应的状态信息。在正常情况下，统一使用0作为状态值；非正常情况返回的状态值由使用这个类的方法定义，
 *      详细参考这个方法的JavaDoc。<br>
 * message - 用于表示本次响应的结果信息。<br>
 * data - 用于存储具体数据的键值对对象。<br>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class R {
    private Integer status;
    private String message;
    private Map<String, Object> data;
}
