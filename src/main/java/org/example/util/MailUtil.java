package org.example.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;

/**
 * 发送邮件工具类
 * @author 沉晓
 */
public class MailUtil {

    public static  Properties p;
    public static  Session session;
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM-dd");

    static {
        p = new Properties();
        try {
            p.load(JdbcUtil.class.getClassLoader().getResourceAsStream("mail.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendEmailSuccess(String emailAddress) throws Exception{
        session = Session.getInstance(p, new Authenticator() {
            // 设置认证账户信息
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(p.getProperty("my.email.account"), p.getProperty("my.email.password"));
            }
        });

        MimeMessage message = new MimeMessage(session);
        // 发件人
        message.setFrom(new InternetAddress(p.getProperty("my.email.account")));
        // 收件人和抄送人
        message.setRecipients(Message.RecipientType.TO, emailAddress);

        // 内容(这个内容还不能乱写,有可能会被SMTP拒绝掉)
        //时分
        String subject = "【" + FORMATTER.format(LocalDate.now()) + "】 疫情打卡成功";
        message.setSubject(subject);
        message.setContent("同学你好呀，你的打卡已经完成~（系统提示）", "text/html;charset=UTF-8");
        message.setSentDate(new Date());
        message.saveChanges();
        Transport.send(message);
    }

    public static void sendEmailFailed(String emailAddress) throws Exception{
        session = Session.getInstance(p, new Authenticator() {
            // 设置认证账户信息
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(p.getProperty("my.email.account"), p.getProperty("my.email.password"));
            }
        });

        MimeMessage message = new MimeMessage(session);
        // 发件人
        message.setFrom(new InternetAddress(p.getProperty("my.email.account")));
        // 收件人和抄送人
        message.setRecipients(Message.RecipientType.TO, emailAddress);

        // 内容(这个内容还不能乱写,有可能会被SMTP拒绝掉)
        String subject = "【" + FORMATTER.format(LocalDate.now()) + "】 疫情打卡失败提醒";
        message.setSubject(subject);
        message.setContent("同学你好呀，你的打卡失败了，呜呜呜~（系统提示）", "text/html;charset=UTF-8");
        message.setSentDate(new Date());
        message.saveChanges();
        Transport.send(message);
    }
}