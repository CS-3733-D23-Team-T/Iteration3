package edu.wpi.tacticaltritons.auth;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import de.taimos.totp.TOTP;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import lombok.Getter;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.Objects;

public class ConfirmApp {
    public static StringProperty APP_CODE = new SimpleStringProperty("");
    private static boolean VERIFIED;
    private static final String QR_CODE_DIRECTORY = "src/main/resources/edu/wpi/tacticaltritons/qrCodes";
    @Getter private static Thread TOTPThread;
    public static String generateSecretKey(){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }
    public static String getTOTPCode(String secretKey){
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }
    public static String getGoogleAuthenticatorBarCode(String secret, String account, String issuer){
        return "otpauth://totp/"
                + URLEncoder.encode(issuer + ":" + account, StandardCharsets.UTF_8).replace("+", "%20")
                + "?secret=" + URLEncoder.encode(secret, StandardCharsets.UTF_8).replace("+","%20")
                + "&issuer=" + URLEncoder.encode(issuer, StandardCharsets.UTF_8).replace("+","%20");
    }
    public static String findQRPath(){
        File qrDirectory = Path.of(QR_CODE_DIRECTORY).toFile();
        String randomString = Authenticator.generateRandomHexString();
        if(!qrDirectory.exists()) {
            if(!qrDirectory.mkdir()) throw new RuntimeException("Unable to create QR Code Directory");
        }

        if(qrDirectory.listFiles() == null){
            return QR_CODE_DIRECTORY + "/" + randomString + ".png";
        }

        boolean contained = true;
        while(contained) {
            contained = false;
            for (File file : Objects.requireNonNull(qrDirectory.listFiles())) {
                if(file.getName().equals(randomString)){
                    contained = true;
                    break;
                }
            }
        }
        return  QR_CODE_DIRECTORY + "/" + randomString + ".png";
    }
    public static Image createQRCode(String barCodeData, String path, int width, int height) throws WriterException {
        BitMatrix matrix = new MultiFormatWriter().encode(barCodeData, BarcodeFormat.QR_CODE, width, height);

        WritableImage writableImage = new WritableImage(matrix.getWidth(), matrix.getHeight());
        Color dark = Color.BLACK;
        Color light = Color.WHITE;

        PixelWriter writer = writableImage.getPixelWriter();
        for(int x = 0; x < matrix.getWidth(); x++){
            for(int y = 0; y < matrix.getHeight(); y++){
                if(matrix.get(x, y)){
                    writer.setColor(x, y, dark);
                }
                else{
                    writer.setColor(x, y, light);
                }
            }
        }

        try(FileOutputStream out = new FileOutputStream(path)){
            MatrixToImageWriter.writeToStream(matrix, "png", out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ImageView(writableImage).getImage();
    }
    public static void generateTOTPThread(String secretKey){
        TOTPThread = new Thread(() -> {
            while(!VERIFIED){
                APP_CODE.set(getTOTPCode(secretKey));

                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        TOTPThread.setDaemon(true);
        TOTPThread.start();
    }
    public static boolean attemptVerify(String code){
        if(code.equals(APP_CODE.get())) VERIFIED = true;
        return VERIFIED;
    }
}
