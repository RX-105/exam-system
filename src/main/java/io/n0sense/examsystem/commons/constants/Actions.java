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

import java.util.List;

public interface Actions {
    String LOGIN = "login";
    String REGISTER = "register";
    String CREATE_BACKUP = "create-backup";
    String RESTORE_BACKUP = "restore-backup";
    String REMOVE_BACKUP = "remove-backup";
    String UPDATE_AVATAR = "update-avatar";
    String ADD_EXAM = "add-exam";
    List<String> actionList = List.of(
            LOGIN, REGISTER, CREATE_BACKUP, RESTORE_BACKUP, REMOVE_BACKUP,
            UPDATE_AVATAR, ADD_EXAM
    );
}
