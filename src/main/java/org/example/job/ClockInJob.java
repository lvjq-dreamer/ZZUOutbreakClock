package org.example.job;

import org.example.util.HttpClientUtil;
import org.example.util.JdbcUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 打卡定时任务类
 * @author 沉晓
 */
public class ClockInJob implements Job {

    private static final JdbcTemplate JDBC_TEMPLATE = new JdbcTemplate(JdbcUtil.getDataSource());
    private static final String LOGINURL = "https://jksb.v.zzu.edu.cn/vls6sss/zzujksb.dll/login";
    private static final String LOGREGEX = "location=\"(\\S+)\"";
    private static final String FILLINBYMYSELFURL = "https://jksb.v.zzu.edu.cn/vls6sss/zzujksb.dll/jksb";

    private static final Pattern LOGINPARRERN = Pattern.compile(LOGREGEX);
    private static final String CHECKSTR = "审核";

    private static final Logger log = LoggerFactory.getLogger(ClockInJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        String getUsersSql = "select * from user where record = 0";
        List<Map<String,Object>> usersList = JDBC_TEMPLATE.queryForList(getUsersSql);
        log.info("数据库解析，一共{}个用户",usersList.size());
        // 最大限度数为8
        for (Map<String, Object> userMap : usersList) {
            CompletableFuture.runAsync(() -> {
                log.info("-- {} 打卡开始",userMap.get("uid"));
                login(userMap);
            });
        }
    }

    /**
     * 登录打卡
     * @param userMap
     */
    public void login(Map<String, Object> userMap)  {
        // 通过账号密码登录
        Map<String,String> loginMap = new HashMap<>(2);
        loginMap.put("uid",(String) userMap.get("uid"));
        loginMap.put("upw",(String) userMap.get("upw"));
        log.info("-- {} -- 登录",userMap.get("uid"));
        String loginHtml =  HttpClientUtil.doPost(LOGINURL, loginMap);
        log.info("-- {} -- 登录页面解析开始",userMap.get("uid"));
        Matcher matcher = LOGINPARRERN.matcher(loginHtml);
        if(matcher.find()) {
            log.info("-- {} -- 登录页面解析成功",userMap.get("uid"));
            // 疫情打卡跳转页处理（信息获取）
            String str = matcher.group();
            // https://jksb.v.zzu.edu.cn/vls6sss/zzujksb.dll/first6?
            String newUrl = str.substring(10,str.length()-1);
            String paramsContent = newUrl.substring(53);
            String[] paramsStr = paramsContent.split("&");
            String ptopid = paramsStr[0].substring(7);
            String sid = paramsStr[1].substring(7);
            log.info("-- {} -- 疫情打卡跳转页解析",userMap.get("uid"));

            // 二、疫情打卡首页处理
            Map<String,String> clickMap = new HashMap<>(8);
            // 每日填报
            clickMap.put("day6","b");
            // 隐藏上送字段
            clickMap.put("did","1");
            clickMap.put("door","");
            clickMap.put("men6","a");
            clickMap.put("ptopid",ptopid);
            clickMap.put("sid",sid);
            HttpClientUtil.doPost(FILLINBYMYSELFURL, clickMap);
            log.info(LocalDateTime.now() + "-- {} -- 疫情打卡首页处理",userMap.get("uid"));

            // 三、疫情打卡提交页处理
            // 构造用户信息（上送字段）
            Map<String,String> importMap = new HashMap<>(32);
            // 1、您今天是否有发热症状
            importMap.put("myvs_1", (String) userMap.get("myvs_1"));
            // 2、您今天是否有咳嗽症状
            importMap.put("myvs_2", (String) userMap.get("myvs_2"));
            // 3、您今天是否有乏力或轻微乏力症状
            importMap.put("myvs_3", (String) userMap.get("myvs_3"));
            // 4、您今天是否有鼻塞、流涕、咽痛或者腹泻等症状
            importMap.put("myvs_4", (String) userMap.get("myvs_4"));
            // 5、您今天是否被所在地医疗机构确定为确诊病例?
            importMap.put("myvs_5",(String)userMap.get("myvs_5"));
            // 6、您今天是否被所在地医疗机构确定为疑似病例?
            importMap.put("myvs_6",(String)userMap.get("myvs_6"));
            // 7、您今天是否被所在地政府或医疗卫生部门确定为密切接触者?
            importMap.put("myvs_7",(String)userMap.get("myvs_7"));
            // 8、您今天是否被所在地医疗机构进行院内隔离观察治疗?
            importMap.put("myvs_8",(String)userMap.get("myvs_8"));
            // 9、您今天是否被要求在政府集中隔离点进行隔离观察?
            importMap.put("myvs_9",(String)userMap.get("myvs_9"));
            // 10、您今天是否被要求在政府集中隔离点进行隔离观察?
            importMap.put("myvs_10",(String)userMap.get("myvs_10"));
            // 11、所在小区（村）是否有确诊病例?(以当地政府公开信息为准)
            importMap.put("myvs_11",(String)userMap.get("myvs_11"));
            // 12、共同居住人是否有确诊病例?
            importMap.put("myvs_12",(String)userMap.get("myvs_12"));
            // 13、当前居住地：
            // 省份（自治区）
            importMap.put("myvs_13a", (String) userMap.get("myvs_13a"));
            // 地市
            importMap.put("myvs_13b", (String) userMap.get("myvs_13b"));
            // 具体地址
            importMap.put("myvs_13c", (String) userMap.get("myvs_13c"));
            // 14、您是否为当日返郑人员
            importMap.put("myvs_14", (String) userMap.get("myvs_14"));

            // 隐藏字段
            importMap.put("did","2");
            importMap.put("door","");
            importMap.put("men6","a");
            importMap.put("day6","b");
            importMap.put("sheng6","");
            importMap.put("shi6","");
            importMap.put("fun3","");
            importMap.put("jingdu","0.000000");
            importMap.put("weidu","0.000000");
            importMap.put("ptopid", ptopid);
            importMap.put("sid", sid);
            String s = HttpClientUtil.doPost(FILLINBYMYSELFURL, importMap);
            log.info("-- {} -- 疫情确认页处理",userMap.get("uid"));

            if(s.contains(CHECKSTR)) {
                log.info("-- {} -- 疫情打卡任务成功",userMap.get("uid"));
                String updateSql = "update user set record = 1 where id = ? ";
                JDBC_TEMPLATE.update(updateSql, userMap.get("id"));
            } else {
                log.info("-- {} -- 疫情打卡任务失败",userMap.get("uid"));
            }
        } else {
            // 解析失败，则打卡结束。
            log.error("-- {} -- 登录失败",userMap.get("uid"));
        }
    }
}