import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;

public class Credentials {
    // Define the characters to be used in the password
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]{}|;:,.<>?";
    private static final String ALL_CHARACTERS = UPPERCASE + LOWERCASE + DIGITS + SPECIAL_CHARACTERS;

    // SecureRandom instance
    private static final SecureRandom random = new SecureRandom();

    /**
     * Generates a secure random password of the given length.
     *
     * @param length the length of the password to generate
     * @return a secure random password
     */
    public static String generateSecurePassword(int length) {
        if (length < 4) {
            throw new IllegalArgumentException("Password length should be at least 4 characters.");
        }

        // StringBuilder for constructing the password
        StringBuilder password = new StringBuilder(length);

        // Ensure password has at least one character from each category
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));

        // Fill the remaining characters randomly from all characters
        for (int i = 4; i < length; i++) {
            password.append(ALL_CHARACTERS.charAt(random.nextInt(ALL_CHARACTERS.length())));
        }

        // Shuffle the characters to avoid predictable patterns
        return shuffleString(password.toString());
    }

    /**
     * Shuffles the given string using Fisher-Yates shuffle.
     *
     * @param input the string to shuffle
     * @return the shuffled string
     */
    private static String shuffleString(String input) {
        char[] characters = input.toCharArray();
        for (int i = characters.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            // Swap characters
            char temp = characters[i];
            characters[i] = characters[index];
            characters[index] = temp;
        }
        return new String(characters);
    }

    /**
     * Generates a secure random JWT secret key.
     *
     * @param length the length of the JWT secret key in bytes
     * @return a secure random JWT secret key in Base64 format
     */
    public static String generateJwtSecret(int length) {
        byte[] secret = new byte[length];
        random.nextBytes(secret);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(secret);
    }

    public static void main(String[] args) {
        // Create a Scanner object to read input
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the desired password length (minimum 4 characters): ");
        int passwordLength = scanner.nextInt();

        try {
            String securePassword = generateSecurePassword(passwordLength);
            System.out.println("Generated Secure Password: " + securePassword);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.print("Enter the desired JWT secret length in bytes (e.g., 32 for 256-bit key): ");
        int jwtSecretLength = scanner.nextInt();

        if (jwtSecretLength < 16) {  // Ensures a reasonable minimum length for security
            System.out.println("Error: JWT secret length should be at least 16 bytes.");
        } else {
            String jwtSecret = generateJwtSecret(jwtSecretLength);
            System.out.println("Generated JWT Secret (Base64): " + jwtSecret);
        }

        // Close the scanner
        scanner.close();
    }
}
