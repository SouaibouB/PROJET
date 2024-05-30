import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

public class sha256 {
    public static void main(String[] args) {
        sha256 hasher = new sha256();
        System.out.println(hasher.algoHash("Anna"));
    }

    public String algoHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(b & 0xff );
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
               System.err.println("Erreur : Algorithme de hachage non trouvé ou encodage non supporté.");
               return null;
        }
    }
}

