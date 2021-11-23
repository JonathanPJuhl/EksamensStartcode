package utils;

import entities.ResetPasswordDTO;
import entities.User;
import entities.UserDTO;
import facades.UserFacade;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManagerFactory;

public class MailSystem {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    public void resetPW(ResetPasswordDTO recipientAndSecurityAnswer) {

        String recipient = recipientAndSecurityAnswer.getEmail();
        String answerToSecurityQuestion = recipientAndSecurityAnswer.getAnswerToSecurityQuestion();

        UserFacade userF = UserFacade.getUserFacade(EMF);
        User user = userF.findUserByUsername(recipient);


        // Recipient's email ID needs to be mentioned.
        String to = recipient;

        // Sender's email ID needs to be mentioned
        String from = "itsikkerhedseksamen@gmail.com";

        // Assuming you are sending email from through gmails smtp
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("itsikkerhedseksamen@gmail.com", "Datamatik1");

            }

        });

        // Used to debug SMTP issues
        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Reset password");

            // Now set the actual message
            // message.setText("Please go to: localhost:3000/resetPW/"+recipient+" to reset your password");
            message.setContent(
                    "<p>Please click: <p> <a href=\"www.ipwithme.com/sys-frontend/#/resetPW/" + recipient + "\">Here</a><p> to reset your password</p><p>Kind regards, JJStocks</p>",
                    "text/html");

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

        System.out.println("Securityquestion answers don't match");

    }

    public void twoFactor(UserDTO username) {

        final int TOKEN_EXPIRE_TIME = 100 * 60 * 5;
        String recipient = username.getEmail();
        UserFacade userF = UserFacade.getUserFacade(EMF);
        User user = userF.findUserByUsername(recipient);

        String randomText = "";

        byte[] array = new byte[8];
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));

        randomText += generatedString;
        randomText += "";
        Date date = new Date();
        String dateString = new Date(date.getTime() + TOKEN_EXPIRE_TIME).toString();


        String to = recipient;

        String from = "itsikkerhedseksamen@gmail.com";

        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("itsikkerhedseksamen@gmail.com", "Datamatik1");

            }

        });

        session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            message.setSubject("Please authenticate");

            message.setContent(
                    "<p>Please click: <p> <a href=\"www.ipwithme.com/sys-frontend/#/resetPW/" + recipient + "\">Here</a><p> to reset your password</p><p>Kind regards, JJStocks</p>",
                    "text/html");

            System.out.println("sending...");
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }


        System.out.println("Securityquestion answers don't match");

    }

    public void sendWarningForUser(String username, String ip) {
        String recipient = username;
        UserFacade userF = UserFacade.getUserFacade(EMF);
        User user = userF.findUserByUsername(recipient);
        String uniqueKey = user.getKeyForUnlocking();
        String to = recipient;

        String from = "itsikkerhedseksamen@gmail.com";

        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("itsikkerhedseksamen@gmail.com", "Datamatik1");

            }

        });

        // Used to debug SMTP issues
        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Logon attempt from " + ip);

            message.setContent(
                    "<p>Warning, multiple attempts at logging in on your account from ip: "+ ip +
                            " <p> This ip, in combination with your account has been temporarily banned. If this was a mistake" +
                            "and it was you, trying to log in, please the following password along with your mail on: </p>" +
                            " <p> <a href=\"www.ipwithme.com/sys-frontend/#/unlock\">This site</a><p> to reactivate</p>" +
                                    "<p>Password: " + uniqueKey + "</p>"+
                                    "<p>Kind regards</p>"+
                                    "<p>JJStocks</p>",
                    "text/html");

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
