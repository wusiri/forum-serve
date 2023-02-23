package wlong.work.forumserve.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Slf4j
public class MailServiced {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSenderImpl mailSender;

    /**
     * 发送邮件
     */
    public void sendEmail(String email, String code) {
        int count = 1;//默认发送一次
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);
            while (count-- != 0) {
                //标题
                helper.setSubject("您的验证码为：" + code);
                //内容
                helper.setText("您好！，感谢您的使用。您的验证码为：" + "<h2>" + code + "</h2>" + "千万不能告诉别人哦！", true);
                //邮件接收者
                helper.setTo(email);
                //邮件发送者，必须和配置文件里的一样，不然授权码匹配不上
                helper.setFrom(from);
                log.info(email);
                mailSender.send(mimeMessage);
                log.info("邮件发送成功！");
            }
        }catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


}
