package com.example.pizzaorderingapp.Utils;

import android.os.AsyncTask;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender extends AsyncTask<Void, Void, Void> {

    private String email;
    private String subject;
    private String message;

    public MailSender(String email, String subject, String message) {
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        // Creating properties
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        // Creating a new session
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    // Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("farmresort2023@gmail.com", "povropxzrijledgr");
                    }
                });

        try {
            // Creating MimeMessage object
            MimeMessage mm = new MimeMessage(session);

            // Setting sender address
            mm.setFrom(new InternetAddress("farmresort2023@gmail.com"));
            // Adding recipient
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            // Adding subject
            mm.setSubject(subject);
            // Adding message
            mm.setText(message);

            // Sending email
            Transport.send(mm);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
