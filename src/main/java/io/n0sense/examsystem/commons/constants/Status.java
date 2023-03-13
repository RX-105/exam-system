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

/**
 * 通用信息类。
 * @author kazuha
 * @since 2022/11/23
 */
public interface Status {
    int ERR_UNSPECIFIED = -1;
    int OK = 0;

    // 通用错误状态
    int ERR_OPERATION_FAILED = 100;
    int ERR_PARAMETER_NOT_PRESENT = 101;
    int ERR_PARAMETER_NOT_MATCH = 102;
    int ERR_NO_SUCH_ELEMENT = 102;
    int ERR_FILE_NOT_FOUND = 103;

    // 详细错误状态
    int ERR_USERNAME_IN_USE = 1001;
    int ERR_INCORRECT_PASSWORD = 1002;
    int ERR_USER_NOT_FOUND = 1003;
    int ERR_MAJOR_EXISTS = 1004;
    int ERR_MAJOR_NOT_FOUND = 1005;
    int ERR_EXAM_EXISTS = 1006;
    int ERR_EXAM_NOT_FOUND = 1007;
    int ERR_TIME_NOT_ALLOWED = 1008;
    int ERR_TIME_NOT_FOUND = 1009;
    int ERR_LOGIN_REQUIRED = 1010;
}
