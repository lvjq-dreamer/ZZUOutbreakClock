package org.example.job;

import org.example.util.JdbcUtil;
import org.example.util.MailUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 发送邮件定时任务类
 * @author 沉晓
 */
public class SendMailJob implements Job {

    private static final JdbcTemplate JDBC_TEMPLATE = new JdbcTemplate(JdbcUtil.getDataSource());

    private static final Logger log = LoggerFactory.getLogger(SendMailJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        log.info("发送邮件任务开始");
        String getUsersSql = "select * from user";
        List<Map<String,Object>> usersList = JDBC_TEMPLATE.queryForList(getUsersSql);
        for (Map<String, Object> userMap : usersList) {
            // 校验发送邮箱情况
            if("0".equals(userMap.get("send"))) {
                // 没有发送邮箱
                if("1".equals(userMap.get("record"))) {
                    log.info("-- {} -- 打卡成功，邮件开始发送",userMap.get("uid"));
                    // 打卡成功
                    CompletableFuture.runAsync(() -> {
                        try {
                            MailUtil.sendEmailSuccess((String)userMap.get("email"));
                            // 标记邮件
                            String updateSql = "update user set send = 1 where id = ? ";
                            JDBC_TEMPLATE.update(updateSql, userMap.get("id"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    // 打卡失败
                    log.info("-- {} -- 打卡失败，邮件开始发送",userMap.get("uid"));
                    CompletableFuture.runAsync(() -> {
                        try {
                            MailUtil.sendEmailFailed((String)userMap.get("email"));
                            // 标记邮件
                            String updateSql = "update user set send = 1 where id = ? ";
                            JDBC_TEMPLATE.update(updateSql, userMap.get("id"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        }
    }
}
