package utils;

import entities.User;
import facades.UserFacade;

import java.util.Properties;

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

    public void resetPW(String mail, String keyForUser) {

        String recipient = mail;
        String key = keyForUser;

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

            message.setSubject("Reset password");

            message.setContent(
                    "<p>Please click: <p> <a href=\"www.ipwithme.com/sys-frontend/#/resetPW/" + recipient + "\">Here</a>" +
                            "<p> And input: </p> <p>" + key + "</p>" +
                            "<p> to reset your password</p><p>Kind regards, JJStocks</p>",
                    "text/html");

            System.out.println("sending...");

            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public void twoFactor(String username, String code) {

        String recipient = username;

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

            message.setSubject("Two factor authentication");

            message.setContent(
                    "<p>Your code: <p> " +
                            "<p>"+ code +"</p>",
                    "text/html");

            System.out.println("sending...");
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
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

        session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

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
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public void sendVerificationEmail(String username) {
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

        session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            message.setSubject("Please verify your email");

            message.setContent(
                    "<p>Warning, multiple attempts at logging in on your account from ip: "+
                            " <p> This ip, in combination with your account has been temporarily banned. If this was a mistake" +
                            "and it was you, trying to log in, please the following password along with your mail on: </p>" +
                            " <p> <a href=\"www.ipwithme.com/sys-frontend/#/unlock\">This site</a><p> to reactivate</p>" +
                            "<p>Password: " + uniqueKey + "</p>"+
                            "<p>Kind regards</p>"+
                            "<p>JJStocks</p>",
                    "text/html");

            System.out.println("sending...");
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public void verifyUsersEmail(String username, String key) {

        String recipient = username;

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

            message.setSubject("Please verify your email");

            message.setContent(
                    "<p>Your verification code: <p> " +
                            "<p>"+ key +"</p>" +
                            "<p>Please go to <p> " +
                            " <p> <a href=\"www.ipwithme.dk/sys-frontend/#/verify\">This site</a><p> to verify</p>" ,
                    "text/html");

            System.out.println("sending...");
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
