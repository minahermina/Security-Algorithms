import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;
import java.util.Arrays;

public class lfsr {

    public static List<Integer> lfsr_with_truth_table(int[] shift_registers, int[] coefficients, int length, int m) {
        List<Integer> lfsr_out = new ArrayList<>();

        // Truth Table Header
        System.out.printf("%-10s", "Clock");
        for (int i = 0; i < m; i++) {
            System.out.printf("FF%d ", i);
        }
        System.out.println("Output");

        int clock = 0, xor_out = 0;
        while (lfsr_out.size() < length) {
            xor_out = 0;

            // See lsrf.jpg
            for (int i = 0; i < m; i++) {
                xor_out ^= (coefficients[i] & shift_registers[i]);
            }
            lfsr_out.add(xor_out);

            // Table Data
            System.out.printf("%-10d", clock++);
            for (int bit : shift_registers) {
                System.out.printf("%d   ", bit);
            }
            System.out.println("   " + xor_out);

            // Shifting FFs values
            for (int i = m - 1; i > 0; i--) {
                shift_registers[i] = shift_registers[i - 1];
            }
            shift_registers[0] = xor_out;
        }

        System.out.println();
        return lfsr_out;
    }

    public static String string2binary(String text) {
        StringBuilder binary = new StringBuilder();
        for (char c : text.toCharArray()) {
            binary.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'));
        }
        return binary.toString();
    }

    public static String encrypt(String text, List<Integer> lfsr_out) {
        StringBuilder encrypted_binary = new StringBuilder();
        String binary_text = string2binary(text);

        for (int i = 0; i < binary_text.length(); i++) {
            int bit = Character.getNumericValue(binary_text.charAt(i));
            int xor_bit = bit ^ lfsr_out.get(i);
            encrypted_binary.append(xor_bit);
        }

        return encrypted_binary.toString();
    }

    public static String binary2string(String binary_text) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < binary_text.length(); i += 8) {
            int ch_code = Integer.parseInt(binary_text.substring(i, i + 8), 2);
            text.append((char) ch_code);
        }
        return text.toString();
    }

    public static void randomize_array(int[] array, int m) {
        Random rand = new Random();
        for (int i = 0; i < m; i++) {
            array[i] = rand.nextInt(2);
        }
    }

    public static void pretty_print(int[] array, String name) {
        System.out.print(name + ": ");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        final int m_max = 9;
        int m = 0;
        boolean invalid = true;
        int[] shift_registers;
        int[] coefficients;

        String message;

        // User Input
        System.out.print("Enter Ur name to be encrypted: ");
        message = scanner.nextLine();

        while (invalid) {
            System.out.print("Enter the m value: ");
            m = scanner.nextInt();
            invalid = (m > m_max) ? true : false;
        }

        shift_registers = new int[m];
        
          /* for (int i = 0; i <= m; i++) {
              shift_registers[i] = scanner.nextInt();
          } */
        

        randomize_array(shift_registers, m);

        coefficients = new int[m];
        // x^4 + x^3 + x^2 + 1
        if (m == 4) {
            coefficients[0] = 1;
            coefficients[1] = 0;
            coefficients[2] = 1;
            coefficients[3] = 1;
        }
        // Generate Pi
        randomize_array(coefficients, m);

        String binary_text = string2binary(message);

        List<Integer> lfsr_out = lfsr_with_truth_table(shift_registers, coefficients, message.length() * 8, m);
        System.out.printf("%-25s: %s%n", "--> Shift_registers",  Arrays.toString(shift_registers));
        System.out.printf("%-25s: %s%n", "--> Coefficients", Arrays.toString(coefficients));
        System.out.printf("%-25s: %s%n", "--> Original binary", binary_text);

        String encrypted_binary = encrypt(message, lfsr_out);
        System.out.printf("%-25s: %s%n", "--> Encrypted binary", encrypted_binary);

        String encrypted_text = binary2string(encrypted_binary);
        System.out.printf("%-25s: %s%n", "--> Encrypted text", encrypted_text);

        String decrypted_Binary = encrypt(encrypted_text, lfsr_out);
        System.out.printf("%-25s: %s%n", "--> Decrypted binary", decrypted_Binary);

        String decrypted_text = binary2string(decrypted_Binary);
        System.out.printf("%-25s: %s%n", "--> Decrypted text", decrypted_text);
        scanner.close();
    }
}
