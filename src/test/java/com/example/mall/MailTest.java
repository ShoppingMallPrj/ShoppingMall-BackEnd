package com.example.mall;

import com.example.mall.Repository.OrderRepository;
import com.example.mall.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

////
@SpringBootTest
public class MailTest {

    @Autowired
    MailService mailService;

    @Autowired
    OrderRepository orderRepository;
//
//    @Test
//    public void sendTestMail()  {
//
//        try {
//            mailService.sendTestEmail("test Mail", "sasa5680@naver.com");
//        } catch (MessagingException e) {
//            e.printStackTrace();
//            System.out.println("mail error");
//        }
//    }
//
//    @Test
//    public void sendConfirmEmail()   {
//
//        String code = "123456";
//        String email = "sasa5680@naver.com";
//
//        try {
//            mailService.sendConfirmEmail(code, email);
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//    }



}
