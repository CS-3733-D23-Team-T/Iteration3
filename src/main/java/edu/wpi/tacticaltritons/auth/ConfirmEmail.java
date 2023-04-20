package edu.wpi.tacticaltritons.auth;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import edu.wpi.tacticaltritons.App;
import org.apache.commons.codec.binary.Base64;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class ConfirmEmail {
    private static final String EMAIL_ADDRESS = "email-service@cs3733-383908.iam.gserviceaccount.com";
    private static final String CREDENTIAL_PATH = "keys/email_api_credentials.json";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_SEND);
    public static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = App.class.getResourceAsStream(CREDENTIAL_PATH);
        if(in == null){
            throw new FileNotFoundException("Resource Not Found: " + CREDENTIAL_PATH);
        }
        GoogleClientSecrets secrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));


        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT,
                JSON_FACTORY,
                secrets,
                SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
                .setAccessType("offline")
                .build();


        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static String sendConfirmationEmail(String address){
        String code = String.valueOf(Math.abs(new Random().nextInt() % 1000000));

        try {
            final NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
            Gmail service = new Gmail.Builder(transport, JSON_FACTORY, getCredentials(transport))
                    .setApplicationName("CS3733").build();


            Properties properties = new Properties();
            Session session = Session.getDefaultInstance(properties, null);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_ADDRESS));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(address));
            message.setSubject("Confirmation Email");
            message.setText("Your Confirmation Code is: " + code);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            message.writeTo(buffer);
            byte[] rawMessage = buffer.toByteArray();
            String encodedMessage = Base64.encodeBase64URLSafeString(rawMessage);
            com.google.api.services.gmail.model.Message msg = new com.google.api.services.gmail.model.Message();
            msg.setRaw(encodedMessage);

            msg = service.users().messages().send("me", msg).execute();
//            System.out.println("Message ID: " + msg.getId());
//            System.out.println(msg.toPrettyString());
        } catch (IOException | MessagingException | GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        return code;
    }
}
