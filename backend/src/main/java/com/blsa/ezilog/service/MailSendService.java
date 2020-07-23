package com.blsa.ezilog.service;

import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MailSendService {

    @Value("${sendmail.email}")
    private String MyEmail;

    @Value("${sendmail.password}")
    private String MyPassword;

    private boolean lowerCheck;
    private int size;

    public String init() {
        Random ran = new Random();
        StringBuffer sb = new StringBuffer();
        int num = 0;

        do {
            num = ran.nextInt(75) + 48;
            if ((num >= 48 && num <= 57) || (num >= 65 && num <= 90) || (num >= 97 && num <= 122)) {
                sb.append((char) num);
            } else {
                continue;
            }

        } while (sb.length() < size);
        if (lowerCheck) {
            return sb.toString().toLowerCase();
        }
        return sb.toString();
    }

    public String getKey(boolean lowerCheck, int size) {
        this.lowerCheck = lowerCheck;
        this.size = size;
        return init();
    }

    private Session getSetting() {
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", 587);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(MyEmail, MyPassword);
            }
        });
        return session;
    }

    public void mailSendWithUserKey(String email, String nickname, String key, long uaid)
            throws AddressException, MessagingException {
        Session session = getSetting();
        String setfrom = MyEmail;
        String htmlStr = "<h2>안녕하세요 EZI LOG 입니다!</h2><br><br>" + "<h3>" + nickname + "님</h3>"
                + "<p>인증하기 버튼을 누르시면 인증이 완료되어 로그인이 가능해 집니다 : " + "<a href='http://localhost:3000/user/auth?uaid=" + uaid
                + "&userkey=" + key + "'>인증하기</a></p>" + "(혹시 잘못 전달된 메일이라면 이 이메일을 무시하셔도 됩니다)";

        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(setfrom));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
        message.setSubject("EZI LOG 인증 메일입니다.");
        message.setText(htmlStr, "utf-8", "html");

        Transport.send(message);
    }

    public void mailSendWithPassword(String email, String nickname, String password)
            throws AddressException, MessagingException {
        Session session = getSetting();
        String setfrom = MyEmail;
        String htmlStr = "<h2>안녕하세요 EZI LOG 입니다!</h2><br><br>" + "<h3>" + nickname + "님</h3>" + "현재 고객님의 비밀번호는 "
                + password + "입니다.";

        MimeMessage message = new MimeMessage(session);

        message.setFrom(new InternetAddress(setfrom));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
        message.setSubject("EZI LOG 비밀 번호 찾기 메일입니다.");
        message.setText(htmlStr, "utf-8", "html");

        Transport.send(message);
    }
}
