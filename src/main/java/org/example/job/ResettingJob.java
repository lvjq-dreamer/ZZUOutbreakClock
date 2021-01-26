package org.example.job;

import org.example.util.JdbcUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;

/**
 * 数据重置定时任务类
 * @author 沉晓
 */
public class ResettingJob implements Job {

    private static final JdbcTemplate JDBC_TEMPLATE = new JdbcTemplate(JdbcUtil.getDataSource());

    private static final Logger log = LoggerFactory.getLogger(ResettingJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        log.info(LocalDateTime.now() + " 重置打卡开始");
        String updateSql = "update user set record = 0, send = 0 where record = 1 or send = 1";
        JDBC_TEMPLATE.update(updateSql);
        log.info(LocalDateTime.now() + " 重置打卡结束");
    }
}