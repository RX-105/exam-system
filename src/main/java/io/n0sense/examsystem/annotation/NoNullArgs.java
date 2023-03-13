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

package io.n0sense.examsystem.annotation;

import java.lang.annotation.*;

/**
 * 只能为方法添加该注解，且返回类型应该是{@link io.n0sense.examsystem.entity.R R}。<br>
 * 如果为一个方法添加这个注解，那么配套的AOP增强类将会在该方法被调用时检查实参列表中是否包含null对象。一旦存在，返回类型
 * {@link io.n0sense.examsystem.entity.R R}的返回代码将会修改为1001（参见{@link io.n0sense.examsystem.commons.constants.Status Status}）
 * ，且data中将会添加/覆盖属性"location"为400页面地址。
 * @see io.n0sense.examsystem.entity.R
 * @see io.n0sense.examsystem.commons.constants.Status
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoNullArgs {
    String[] excludedNames() default {};
    Class[] excludedTypes() default {};
}
