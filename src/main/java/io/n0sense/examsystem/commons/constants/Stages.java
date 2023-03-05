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
