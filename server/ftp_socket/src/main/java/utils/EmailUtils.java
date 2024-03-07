/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import ftp.NormalFilePermission;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import java.util.Properties;

/**
 *
 * @author lamanhhai
 */
public class EmailUtils {

    private final String username = "nguyenhoangbao799@gmail.com";
    private final String password = "ikpcwbkckfchkolg";

    public boolean sendEmail(String emailTo, String otp) {
        boolean isSended = false;
        String subject = "Xác thực tài khoản - Mã OTP của bạn";
        String body = "<p>Chào bạn,</p>\n"
                + "    <p>Chúng tôi đã nhận được yêu cầu xác thực tài khoản của bạn. Dưới đây là mã OTP của bạn:</p>\n"
                + "    <p><strong>Mã OTP:</strong> " + otp + "</p>\n"
                + "    <p>Mã OTP này sẽ có hiệu lực trong vòng 10 phút kể từ thời điểm bạn nhận được email này. Vui lòng không chia sẻ mã OTP này với bất kỳ ai khác.</p>\n"
                + "    <p>Xin cảm ơn.</p>";
        try {

            Properties prop = new Properties();
            prop.put("mail.smtp.auth", true);
            prop.put("mail.smtp.starttls.enable", "true");
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", "587");

            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(emailTo));
            message.setSubject(subject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(body, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);

            isSended = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isSended;
    }

    public boolean sendSharingFileNotification(String sender, String receiver, String permission) {
        boolean isSended = false;
        String subject = "Thông báo: Chia sẻ Quyền Truy Cập File/Folder qua FTP";
        String messagePermission = permission.equalsIgnoreCase(NormalFilePermission.FULL_PERMISSION) ? "Quyền đọc và ghi" : "Chỉ quyền đọc";
        String body = "<p>Xin chào,</p>\n"
                + "    \n"
                + "    <p>Chúng tôi gửi thông báo này để thông báo về việc " + sender + " chia sẻ quyền truy cập file thông qua dịch vụ FTP. Dưới đây là các chi tiết cần thiết:</p>\n"
                + "    \n"
                + "    <ul>\n"
                + "        <li>Người dùng nhận được quyền truy cập: " + receiver + "</li>\n"
                + "        <li>Quyền được cấp: " + messagePermission + "</li>\n"
                + "    </ul>\n"
                + "    \n"
                + "    <p>Vui lòng kiểm tra lại thông tin trên để đảm bảo rằng việc chia sẻ quyền truy cập diễn ra chính xác. Nếu có bất kỳ thắc mắc hay cần hỗ trợ thêm, xin vui lòng liên hệ với chúng tôi.</p>";
        try {

            Properties prop = new Properties();
            prop.put("mail.smtp.auth", true);
            prop.put("mail.smtp.starttls.enable", "true");
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", "587");

            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(receiver));
            message.setSubject(subject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(body, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);

            isSended = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isSended;
    }

    public boolean sendSharingDirectoryNotification(String sender, String receiver, boolean canModify, boolean uploadable, boolean downloadable) {
        boolean isSended = false;
        String subject = "Thông báo: Chia sẻ Quyền Truy Cập File/Folder qua FTP";
        String permisionModify = canModify ? "Được phép chỉnh sửa" : "Không được phép chỉnh sửa";
        String permissonUpload = uploadable ? "Được phép upload" : "Không được phép upload";
        String permissonDownload = downloadable ? "Được phép download" : "Không được phép download";
        String body = "<p>Xin chào,</p>\n"
                + "    \n"
                + "    <p>Chúng tôi gửi thông báo này để thông báo về việc " + sender + " chia sẻ quyền truy cập file thông qua dịch vụ FTP. Dưới đây là các chi tiết cần thiết:</p>\n"
                + "    \n"
                + "    <ul>\n"
                + "        <li>Người dùng nhận được quyền truy cập: " + receiver + "</li>\n"
                + "        <li>Quyền được cấp: " + permisionModify + "</li>\n"
                + "        <li>Quyền được upload: " + permissonUpload + "</li>\n"
                + "        <li>Quyền được download: " + permissonDownload + "</li>\n"
                + "    </ul>\n"
                + "    \n"
                + "    <p>Vui lòng kiểm tra lại thông tin trên để đảm bảo rằng việc chia sẻ quyền truy cập diễn ra chính xác. Nếu có bất kỳ thắc mắc hay cần hỗ trợ thêm, xin vui lòng liên hệ với chúng tôi.</p>";
        try {

            Properties prop = new Properties();
            prop.put("mail.smtp.auth", true);
            prop.put("mail.smtp.starttls.enable", "true");
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", "587");

            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(receiver));
            message.setSubject(subject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(body, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);

            isSended = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isSended;
    }
}
