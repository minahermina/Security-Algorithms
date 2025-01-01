import java.util.Scanner;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;

public class DES {
    public static final int KEY_LEN = 8;
    public static final int BLOCK_LEN = 64;

    public static final byte[] PC1 = {
            57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4
    };

    public static final byte[] PC2 = {
            14, 17, 11, 24, 1, 5,
            3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32
    };

    public static final byte[] IP = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };

    public static final byte[] IP1 = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25
    };

    public static final byte[] E = {
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1
    };

    public static final byte[] SHIFTS = {
            0, 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
    };

    public static final int[][][] S = {
            {
                    { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7 },
                    { 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8 },
                    { 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0 },
                    { 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 }
            },
            {
                    { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10 },
                    { 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5 },
                    { 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15 },
                    { 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 }
            },
            {
                    { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8 },
                    { 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1 },
                    { 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7 },
                    { 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 }
            },
            {
                    { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15 },
                    { 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9 },
                    { 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4 },
                    { 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 },
            },
            {
                    { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9 },
                    { 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6 },
                    { 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14 },
                    { 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 },
            },
            {
                    { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11 },
                    { 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8 },
                    { 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6 },
                    { 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 },
            },
            {
                    { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1 },
                    { 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6 },
                    { 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2 },
                    { 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 },
            },
            {
                    { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7 },
                    { 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2 },
                    { 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8 },
                    { 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 },
            }
    };

    public static final byte[] P = {
            16, 7, 20, 21,
            29, 12, 28, 17,
            1, 15, 23, 26,
            5, 18, 31, 10,
            2, 8, 24, 14,
            32, 27, 3, 9,
            19, 13, 30, 6,
            22, 11, 4, 25
    };

    public static int cnt = 1;

    public static long[] KEYS = {};

    public static String bin2str(String bin_str) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < bin_str.length(); i += 8) {
            String by = bin_str.substring(i, Math.min(i + 8, bin_str.length()));
            str.append((char) Integer.parseInt(by, 2));
        }
        return str.toString();
    }

    public static String add_special_chars(String str, int minLength) {
        StringBuilder result = new StringBuilder(str);
        while (result.length() < minLength)
            result.append("@");
        return result.toString();
    }

    public static String bin2hex(String bin) {
        BigInteger b = new BigInteger(bin, 2);
        String ciphertext = b.toString(16);
        return ciphertext;
    }

    public static String fromStringToBin(String input) {
        StringBuilder binary = new StringBuilder();
        for (char c : input.toCharArray()) {
            String bin = Integer.toBinaryString(c);
            while (bin.length() < 8) {
                bin = "0" + bin;
            }
            binary.append(bin);
        }
        return binary.toString();
    }

    public static String hex2bin(String hex) {
        BigInteger b = new BigInteger(hex, 16);
        String bin = b.toString(2);
        return bin;
    }

    static void KeySchedule(String key) {
        cnt++;
        while (key.length() < 64)
            key = "0" + key;

        int i, j;
        // Apply Permuted Choice 1 (64 -> 56 bit)
        String key_PC1 = "";
        for (i = 0; i < PC1.length; i++) {
            key_PC1 = key_PC1 + key.charAt(PC1[i] - 1);
        }

        int C = Integer.parseInt(key_PC1.substring(0, 28), 2);
        int D = Integer.parseInt(key_PC1.substring(28), 2);

        for (i = 1; i < KEYS.length; i++) {

            C = Integer.rotateLeft(C, SHIFTS[i]);
            D = Integer.rotateLeft(D, SHIFTS[i]);

            long merged = ((long) C << 28) + D;

            // 56-bit merged
            String key_rotated = Long.toBinaryString(merged);

            while (key_rotated.length() < 56)
                key_rotated = "0" + key_rotated;

            String key_PC2 = "";

            // Apply Permuted Choice 2 (56 -> 48 bit)
            for (j = 0; j < PC2.length; j++) {
                key_PC2 = key_PC2 + key_rotated.charAt(PC2[j] - 1);
            }
            // Setting the actual keys
            KEYS[i] = Long.parseLong(key_PC2, 2);
        }
    }

    static String F(String R, String key) {
        // Expansion
        String exp_R = "";
        for (int i = 0; i < E.length; i++) {
            exp_R = exp_R + R.charAt(E[i] - 1);
        }

        long exp_res = Long.parseLong(exp_R, 2);
        long k = Long.parseLong(key, 2);

        // Xor (key, result of expansion)
        Long result = exp_res ^ k;
        String bin_res = Long.toBinaryString(result);

        while (bin_res.length() < 48) {
            bin_res = "0" + bin_res;
        }

        // Split to 8 6-bit strings
        String[] s_in = new String[8];
        for (int i = 0; i < 8; i++) {
            s_in[i] = bin_res.substring(0, 6);
            bin_res = bin_res.substring(6);
        }

        // Do S Box stuff
        String[] s_out = new String[8];
        for (int i = 0; i < 8; i++) {
            int[][] current_s = S[i];
            String cur = s_in[i];

            String row_bits = cur.charAt(0) + "" + cur.charAt(5);
            String col_bits = cur.substring(1, 5);

            int row = Integer.parseInt(row_bits, 2); // first & last bits
            int col = Integer.parseInt(col_bits, 2); // middle 4 bits

            // S Box lookup
            s_out[i] = Integer.toBinaryString(current_s[row][col]);

            // Make sure the string is 4 bits
            while (s_out[i].length() < 4) {
                s_out[i] = "0" + s_out[i];
            }

        }

        // Merge S-Boxes outputs in single 32-bit string
        String sboxes_out = "";
        for (int i = 0; i < 8; i++) {
            sboxes_out = sboxes_out + s_out[i];
        }

        // Apply P permutation
        String sboxes_out_per = "";
        for (int i = 0; i < P.length; i++) {
            sboxes_out_per = sboxes_out_per + sboxes_out.charAt(P[i] - 1);
        }

        return sboxes_out_per;
    }

    static String perform_DES(String block) {
        int i;
        String out = "";

        // Initial Permutation
        for (i = 0; i < block.length(); i++) {
            out = out + block.charAt(IP[i] - 1);
        }

        // Split block into L & R
        String L = out.substring(0, 32);
        String R = out.substring(32);

        // Rounds
        for (i = 0; i < 16; i++) {
            String k = Long.toBinaryString(KEYS[i + 1]);
            while (k.length() < 48) {
                k = "0" + k;
            }

            // Applying F function
            String F_result = F(R, k);

            // Xor L & F
            long f = Long.parseLong(F_result, 2);
            long L_num = Long.parseLong(L, 2);

            long r1 = L_num ^ f;
            String R1 = Long.toBinaryString(r1);

            while (R1.length() < 32) {
                R1 = "0" + R1;
            }

            L = R;
            R = R1;
        }

        String in = R + L;
        out = "";
        // Inverse IP
        for (i = 0; i < IP1.length; i++) 
            out = out + in.charAt(IP1[i] - 1);

        return out;
    }

    static String UTF82bin(String message) {
        byte[] bytes = null;

        bytes = message.getBytes(StandardCharsets.UTF_8);

        StringBuilder bin = new StringBuilder();
        for (byte b : bytes) {
            int value = b & 0xFF;
            for (int j = 0; j < 8; j++) {
                bin.append((value & 128) == 0 ? 0 : 1);
                value <<= 1;
            }
        }
        return bin.toString();
    }

    static public String reverse_DES(String block) {
        int length = block.length();

        // IP
        String out = "";
        for (int i = 0; i < IP.length; i++) {
            out = out + block.charAt(IP[i] - 1);
        }

        String L = out.substring(0, 32);
        String R = out.substring(32);

        for (int i = 16; i > 0; i--) {

            String k = Long.toBinaryString(KEYS[i]);
            // Ensure key is 48 bit
            while (k.length() < 48)
                k = "0" + k;

            // Xor R & current_key
            String F_result = F(R, k);

            // XOR L and f
            long f = Long.parseLong(F_result, 2);
            long L_num = Long.parseLong(L, 2);

            long r1 = L_num ^ f;
            String R1 = Long.toBinaryString(r1);

            while (R1.length() < 32)
                R1 = "0" + R1;

            L = R;
            R = R1;
        }

        String in = R + L;
        String output = "";
        for (int i = 0; i < IP1.length; i++) {
            output = output + in.charAt(IP1[i] - 1);
        }

        return output;
    }

    static public String decrypt(String message, String key) {

        KEYS = new long[17];
        KeySchedule(key);

        String message_bin = message;

        // Pad binary message
        int remainder = message_bin.length() % BLOCK_LEN;
        if (remainder != 0) {
            for (int i = 0; i < (BLOCK_LEN - remainder); i++)
                message_bin = "0" + message_bin;
        }

        // Separate binary plaintext into blocks
        String[] blocks = new String[message_bin.length() / BLOCK_LEN];
        int offset = 0;
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = message_bin.substring(offset, offset + BLOCK_LEN);
            offset += BLOCK_LEN;
        }

        String[] decrypted_blocks = new String[message_bin.length() / BLOCK_LEN];

        // decrypt the blocks
        for (int i = 0; i < decrypted_blocks.length; i++) {
            decrypted_blocks[i] = reverse_DES(blocks[i]);
        }

        // Build the plain binary string
        String bin_plaintext = "";
        for (int i = 0; i < decrypted_blocks.length; i++)
            bin_plaintext += decrypted_blocks[i];

        return bin_plaintext;
    }

    static String encrypt(String message, String key) {
        // Create the subkeys
        KEYS = new long[17];
        KeySchedule(key);

        String message_bin = message;
        int len = message_bin.length();
        int i, offset = 0;

        // Pad binary message
        int remainder = len % BLOCK_LEN;
        if (remainder != 0) {
            for (i = 0; i < (BLOCK_LEN - remainder); i++)
                message_bin = "0" + message_bin;
        }

        len = message_bin.length();

        // Each BLOCK_LEN bit to be encrypted
        String[] blocks = new String[len / BLOCK_LEN];
        for (i = 0; i < blocks.length; i++) {
            blocks[i] = message_bin.substring(offset, offset + BLOCK_LEN);
            offset += BLOCK_LEN;
        }
        String[] encrypted_blocks = new String[len / BLOCK_LEN];

        for (i = 0; i < encrypted_blocks.length; i++) {
            encrypted_blocks[i] = perform_DES(blocks[i]);
        }

        String ciphertext = "";
        for (i = 0; i < encrypted_blocks.length; i++) {
            ciphertext += encrypted_blocks[i];
        }

        return ciphertext;

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("ZBY: ");
        int choice = scanner.nextInt();
        String plaintext = "";
        String bin_plaintext = "";

        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Enter the plaintext in hexadecimal: ");
                plaintext = scanner.nextLine();
                while (plaintext.length() < 16) {
                    System.out.print("The plaintext must be 16 Hex characters!!");
                    System.out.print("Enter the plaintext as 16 Hex characters: ");
                    plaintext = scanner.nextLine();
                }
                bin_plaintext = hex2bin(plaintext);
                break;

            case 2: {
                System.out.print("Enter the plaintext as 8 characters: ");
                plaintext = scanner.nextLine();
                if (plaintext.length() < 8) {
                    System.out.println("The plaintext is less than 8 characters. Adding special characters...\n");
                    plaintext = add_special_chars(plaintext, 8);
                }

                bin_plaintext = UTF82bin(plaintext);
                break;
            }
            default:
                System.out.println("Invalid choice.");
                return;
        }

        System.out.println("1) Enter key as Hexadecimal");
        System.out.println("2) Enter key as Characters");
        System.out.print("Enter Ur choice: ");
        choice = scanner.nextInt();
        String key_bin = "";
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Enter the key in hexadecimal: ");
                String hex_key = scanner.nextLine();
                key_bin = hex2bin(hex_key);
                while (hex_key.length() != 16) {
                    System.out.print("The key must be 16 Hex characters!!");
                    System.out.print("Enter the key as 16 Hex characters: ");
                    hex_key = scanner.nextLine();
                }
                break;

            case 2: {
                System.out.print("Enter the key as 8 characters: ");
                String char_key = scanner.nextLine();
                while (char_key.length() < 8) {
                    System.out.print("The key must be 8 characters !!");
                    System.out.print("Enter the key as 8 characters: ");
                    char_key = scanner.nextLine();
                }

                key_bin = UTF82bin(char_key);
                break;
            }
            default:
                System.out.println("Invalid choice.");
                return;
        }

        /*
         * bin_plaintext stores binary represenation of plaintext
         * whether it is hexa or normal UTF-8
         */
        String encrypted_message = encrypt(bin_plaintext, (key_bin));
        String decrypted_message = decrypt(encrypted_message, (key_bin));
        System.out.println("\nEncrypted:   " + bin2hex(encrypted_message));
        System.out.println("Original Text in bin: " + bin2hex(bin_plaintext));
        System.out.println("Decrypted Text in bin:  " + bin2hex(decrypted_message));

        scanner.close();
    }
}

/*
 * M : 0123456789ABCDEF
 * K : 133457799BBCDFF1
 * 
 * K : 0E329232EA6D0D73
 * M : 8787878787878787
 * 
 * 
 * K : 0E329232EA6D0D73
 * M :
 * 596F7572206C6970732061726520736D6F6F74686572207468616E20766173656C696E650D0A
 */
