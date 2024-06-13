package com.server.todoapp.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class EmailService {

    public static void sendEmail(String destinationEmail) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        final String WINDOWS_USERNAME = System.getProperty("user.name");
        BufferedReader reader = null;

        final String PATH = "C:\\Users\\" + WINDOWS_USERNAME + "\\Documents\\TodoApp\\email_config.cfg";
        try {
            reader = new BufferedReader(new FileReader(PATH));

            final String USERNAME = reader.readLine();
            final String PASSWORD = reader.readLine();

            final Session SESSION = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            });

            final Message MESSAGE = new MimeMessage(SESSION);
            MESSAGE.setFrom(new InternetAddress(USERNAME));
            MESSAGE.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinationEmail));
            MESSAGE.setSubject("Account created successfully");
            MESSAGE.setText("Your account for TodoApp has been successfully created!");

            Transport.send(MESSAGE);
        } catch (IOException e) {
            throw new RuntimeException("The file " + PATH + " doesn't exist!");
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
