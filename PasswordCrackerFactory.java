import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;


public class PasswordCrackerFactory {
    
    public static PasswordCracker getPasswordCracker(String type) {
        if (type.equalsIgnoreCase("BruteForce")) {
            return new BruteForcePasswordCracker();
        } else if (type.equalsIgnoreCase("Dictionary")) {
            return new DictionaryPasswordCracker();
        } else {
            throw new IllegalArgumentException("Type d'attaque non valide : " + type);
        }
    }

    public interface PasswordCracker {
        void crack(String hash);
        void checkPassword(String hash, String password);
        String algoHash(String password);
    }

    public static class BruteForcePasswordCracker implements PasswordCracker {
        static long startTime=System.currentTimeMillis();
        public void crack(String hash) {
            int maxLength=1000000000;
            String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            StringBuilder password = new StringBuilder();
            for (int targetLength = 1; targetLength <= maxLength; targetLength++) {
                crackLength(hash, password, chars, 0, targetLength);
            }
        }
        
        public void crackLength(String hash, StringBuilder password, String chars, int currentLength, int targetLength) {
            if (currentLength == targetLength) {
                checkPassword(hash, password.toString());
            } else {
                for (int i = 0; i < chars.length(); i++) {
                    password.append(chars.charAt(i));
                    crackLength(hash, password, chars, currentLength + 1, targetLength);
                    password.deleteCharAt(password.length() - 1);
                }
            }
        }

        public void checkPassword(String hash, String password) {
            String hashedPassword = algoHash(password);
            if (hashedPassword != null && hashedPassword.equals(hash)) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                System.out.println("PASSWORD CRACKED: " + password);
                System.out.println("Time to crack: " + elapsedTime + " milliseconds.");
                System.exit(0);
            }
        }
        
        
        public String algoHash(String password) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hashBytes = md.digest(password.getBytes("UTF-8")); 
                StringBuilder hexString = new StringBuilder();
                for (byte b : hashBytes) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) hexString.append('0');
                    hexString.append(hex);
                }
             return hexString.toString();
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                System.err.println("Erreur : Algorithme de hachage non trouvé.");
                return null;
            }
        }
    }

    public static class DictionaryPasswordCracker implements PasswordCracker {
        static long startTime=System.currentTimeMillis();
        @Override
        public void crack(String hash) {
            String dictFilename = "dictionary.txt";
            boolean passwordFound = false;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dictFilename), StandardCharsets.UTF_8))) {
                String password;
                while ((password = br.readLine()) != null) {
                    checkPassword(hash, password);
                }
            } catch (IOException e) {
                    System.err.println("Erreur lors de la lecture du fichier de dictionnaire : " + e.getMessage());
            }
            if (!passwordFound) {
                    System.out.println("PASSWORD CRACKED NOT FOUND");
            }
        }

        public void checkPassword(String hash, String password) {
            String hashedPassword = algoHash(password);
            if (hashedPassword != null && hashedPassword.equals(hash)) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                System.out.println("PASSWORD CRACKED: " + password);
                System.out.println("Time to crack: " + elapsedTime + " milliseconds.");
                System.exit(0);
            }
        }

         

        public String algoHash(String password) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8)); 
                StringBuilder hexString = new StringBuilder();
                for (byte b : hashBytes) {
                   String hex = Integer.toHexString(0xff & b);
                   if (hex.length() == 1) hexString.append('0');
                   hexString.append(hex);
                }
             return hexString.toString();
            }catch (NoSuchAlgorithmException e) {
                System.err.println("Erreur : Algorithme de hachage non trouvé.");
                return null;
            }
       }
    }
}
