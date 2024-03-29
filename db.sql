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

drop database if exists examsystem;
create database examsystem;
use examsystem;

create table exam(
		id int primary key auto_increment, # 考试科目ID
		name varchar(50) not null, # 考试科目名称
		major_id int, # 所属专业ID
		start_time datetime, # 考试开始时间
		end_time datetime # 考试结束时间
);

create table major(
	id int primary key auto_increment, # 专业ID
	name varchar(50) not null, # 专业名称
	school_id int, # 外键 学校ID
	applicant_count int, # 报考人数
	enrollment_count int, # 计划招生数
	accept_score double, # 录取分数线
	admission_count int, # 录取人数
	foreign key (school_id) references school(school_id)
);

create table score(
	id int primary key auto_increment, # 成绩信息ID
	admission_id int not null, # 准考证号
	exam_id int not null, # 考试科目ID
	score double not null, # 分数
	remark varchar(200), # 备注
	status varchar(10) # 状态
);

create table user(
	user_id int primary key auto_increment, # 用户ID
	table_id int, # 信息表ID
	name varchar(20) not null, # 用户名
	realname varchar(20), # 真实姓名
	identity_id int, # 身份证号
	gender varchar(5), # 性别
	political_status varchar(10), # 政治面貌
	phone int, # 手机号
	source varchar(50), # 生源地
	graduate_school varchar(50), # 毕业学校
	graduate_time datetime, # 毕业时间
	is_current tinyint, # 是否应届
	is_science tinyint, # 是否理科
	school_id int, # 报考学校
	major int, # 报考专业
	cet4 int, # 四级成绩
	cet6 int, # 六级成绩
	home_address varchar(100), # 家庭住址
	nationality varchar(10), # 民族
	birthday datetime, # 出生时间
	avatar_name varchar(100), # 头像文件名
	room_id int, # 考场ID
	seat_id int, # 座位号
	admission_id int, # 准考证ID
	is_confirmed tinyint, # 是否确认
	receiver varchar(10), # 接收人
	contact_address varchar(100), # 通讯地址
	zipcode int, # 邮编
	contact_number varchar(20), # 通讯电话
    foreign key (school_id) references school(school_id),
    foreign key (major) references major(id)
);

create table config(
	config_id int primary key auto_increment, # 配置信息ID
	username varchar(20) not null, # 用户名
	time datetime, # 更改时间
	status varchar(10) # 更改状态
);

create table stage(
	stage_id int primary key auto_increment, # 阶段ID
	name varchar(20) not null, # 阶段名称
	school_id int, # 学校ID
	start_time datetime, # 开始时间
	end_time datetime, # 结束时间
	remark varchar(200), # 描述
	definer varchar(20), # 定义者
	foreign key (school_id) references school(school_id)
);

create table admin(
	admin_id int primary key auto_increment, # 管理员ID
	name varchar(20) not null, # 名称
	password varchar(128), # 密码
	group_name varchar(20), # 所在组
    school_id int not null, # 外键学校ID
    foreign key (school_id) references school(school_id)
);

create table school(
	school_id int primary key auto_increment, # 学校ID
	code varchar(20) not null, # 学校代码
	name varchar(20) not null, # 学校名称
	address varchar(100), # 学校地址
	zipcode int, # 邮编
	contact_number varchar(20), # 联系电话
	exam_name varchar(20), # 考试名称
	recruit_years varchar(20) # 招生年份
);

create table registry(
	user_id int primary key, # 用户ID
	username varchar(20) not null, # 用户名
	password varchar(128) not null, # 密码
	ip varchar(40), # 注册ip
	time datetime # 注册时间
);

create table log(
	log_id int primary key auto_increment, # 日志ID
	username varchar(20), # 用户名
	group_name varchar(20), # 所属组
	time datetime not null, # 时间
	ip varchar(40), # 请求ip
	action varchar(32), # 行为
	extras varchar(255) # 附加信息
);

create table backup(
    backup_id int primary key auto_increment,
    time datetime,
    filename varchar(255),
    creator varchar(32)
);

alter table exam AUTO_INCREMENT=100000;
alter table major AUTO_INCREMENT=100000;
alter table score AUTO_INCREMENT=100000;
alter table user AUTO_INCREMENT=100000;
alter table config AUTO_INCREMENT=100000;
alter table stage AUTO_INCREMENT=100000;
alter table admin AUTO_INCREMENT=100000;
alter table school AUTO_INCREMENT=100000;
-- alter table registry AUTO_INCREMENT=100000;
alter table log AUTO_INCREMENT=100000;
alter table backup AUTO_INCREMENT=100000;
