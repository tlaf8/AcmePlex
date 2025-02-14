package com.AcmePlex.Control;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

import com.AcmePlex.Database.DatabaseConnection;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailController {
    private final String email = "acmeplex.donotreply@gmail.com";
    private String email_password;
    private DatabaseConnection connection;
    Session session;

    public static String formatTicket(String movieName, String location, String date, String showtime, ArrayList<String> seats) {
        Random rand = new Random();
        int ticketId = rand.nextInt(2147483647);
        String ticketHtml = "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"><meta content=\"width=device-width,initial-scale=1\" name=\"viewport\"><meta content=\"IE=edge\" http-equiv=\"X-UA-Compatible\"><title>Table-Based Layout</title></head><body style=\"margin:0;padding:0;font-family:Arial,sans-serif;background-color:#f4f4f4;\"><table class=\"container\" style=\"width:100%;background-color:#f4f4f4;padding:20px 0;\"><tr><td><table class=\"email-body\" style=\"width:600px;margin:0 auto;background-color:#fff;border:1px solid #ddd;\"><tr><td class=\"title\" style=\"font-size:24px;font-weight:700;color:#333;text-align:center;\"><h2>AcmePlex</h2></td></tr><tr><td class=\"subtitle\" style=\"font-size:16px;color:#666;padding:10px 20px;text-align:center;\">You're all set! Your receipt and ticket are just below. See you there!</td></tr><tr><td class=\"info-box\" style=\"background-color:#f5f5dc;padding:20px;border-radius:10px;border:2px solid black;text-align:left;font-size:14px;color:#333;\"><p><strong>Movie:</strong> {{ movieName }}</p><p><strong>Location:</strong> {{ location }}</p><p><strong>Date:</strong> {{ ticketDate }}</p><p><strong>Showtime:</strong> {{ showtime }}</p><p><strong>Your seats:</strong></p><ul>{{ li_seats }}</ul><p><strong>Ticket ID:</strong> {{ ticketId }}</p><h3 class=\"warning\" style=\"text-align:center;color:red;\">This is a ticket! Do not discard.</h3></td></tr><tr><td class=\"info-box\" style=\"background-color:#eee;padding:20px;border-radius:10px;border:2px solid black;text-align:left;font-size:14px;color:#333;\"><p><strong>CARD:</strong> ****{{ cardDigits }}</p><p><strong>AMOUNT:</strong> {{ amount }}</p><p><strong>DATE:</strong> {{ receiptDate }}</p><p><strong>APPROVED</strong></p></td></tr></table></td></tr></table></body></html>"
                .replace("{{ movieName }}", movieName)
                .replace("{{ location }}", location)
                .replace("{{ ticketDate }}", date)
                .replace("{{ showtime }}", showtime)
                .replace("{{ ticketId }}", "[" + ticketId + "]");

        StringBuilder seatBuilder = new StringBuilder();
        for(String seat : seats) {
            seatBuilder.append("<li>").append(seat).append("</li>");
        }
        return ticketHtml.replace("{{ li_seats }}", seatBuilder.toString());
    }

    public static String formatReceipt(String ticketHtml, String cardDigits, String amount, String date) {
        return ticketHtml.replace("{{ cardDigits }}", cardDigits.substring(cardDigits.length() - 4)).replace("{{ amount }}", amount).replace("{{ receiptDate }}", date);
    }

    public EmailController() throws RuntimeException {
        read_credentials();
        Properties email_properties = new Properties();
        email_properties.put("mail.smtp.host", "smtp.gmail.com");
        email_properties.put("mail.smtp.port", "587");
        email_properties.put("mail.smtp.auth", "true");
        email_properties.put("mail.smtp.starttls.enable", "true");
        session = Session.getInstance(email_properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, email_password);
            }
        });
    }

    public String formatReceipt() {
        return "";
    }

    public void send_email(String to, String subject, String body) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(email));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        MimeMultipart multipart = new MimeMultipart("alternative");
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(body, "text/html; charset=utf-8");
        multipart.addBodyPart(htmlPart);
        message.setContent(multipart);
        Transport.send(message);
        System.out.printf("Email sent successfully to \033[94m%s\033[0m\n", to);
    }

    private void read_credentials() throws RuntimeException {
        try {
            File file = new File(System.getProperty("user.dir") + "/password");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                email_password = scanner.nextLine();
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not find password file for emailing.");
        }
    }
}