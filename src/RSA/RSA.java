import java.math.BigInteger;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.security.SecureRandom;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RSA {
    private BigInteger p, q;
    private BigInteger xp, xq;
    private BigInteger yp, yq;
    private BigInteger dp, dq;
    private BigInteger n;
    private BigInteger phi;
    private BigInteger e;
    private BigInteger d;

    public RSA() {
    }

    // Extended Euclidean Algorithm (EEA)
    private BigInteger extended_euclidean(BigInteger r0, BigInteger r1) {
        BigInteger s0 = BigInteger.ONE;
        BigInteger s1 = BigInteger.ZERO;
        BigInteger t0 = BigInteger.ZERO;
        BigInteger t1 = BigInteger.ONE;
        BigInteger i = BigInteger.ONE;

        BigInteger q;
        BigInteger r;
        BigInteger s;
        BigInteger t;

        while (!r1.equals(BigInteger.ZERO)) {
            // q_{i-1} = (r_{i-2})/(r_{i-1})
            q = r0.divide(r1);

            // r_i = r_{i-2} mod r_{i-1}
            r = r0.mod(r1);

            // s_i = s_{i-2} - q_{i-1}·s_{i-1}
            s = s0.subtract(q.multiply(s1));

            // t_i = t_{i-2} - q_{i-1}·t_{i-1}
            t = t0.subtract(q.multiply(t1));

            r0 = r1;
            r1 = r;
            s0 = s1;
            s1 = s;
            t0 = t1;
            t1 = t;
            i = i.add(BigInteger.ONE);
        }

        // Return s_{i-1} which is s0
        // Make sure s0 is positive
        while (s0.compareTo(BigInteger.ZERO) < 0) {
            s0 = s0.add(phi);
        }
        return s0;
    }

    // Square-and-Multiply for Modular Exponentiation
    private BigInteger square_and_multiply(BigInteger x, BigInteger exp, BigInteger n) {
        if (n.equals(BigInteger.ONE)) {
            return BigInteger.ZERO;
        }

        // Initialization: r = x
        BigInteger r = x;

        // MSB (t-1)
        int t = exp.bitLength();

        // FOR i = t-1 DOWNTO 0
        for (int i = t-2; i >= 0; i--) {
            // r = r² mod n
            r = r.multiply(r).mod(n);

            // IF h_i = 1
            if (exp.testBit(i)) {
                // r = r·x mod n
                r = r.multiply(x).mod(n);
            }
        }

        // RETURN (r)
        return r;
    }

    private String fermat_primality_test(BigInteger p, int s) {
        SecureRandom random = new SecureRandom();

        // FOR i = 1 TO s
        for (int i = 1; i <= s; i++) {
            // Choose random a ∈ {2,3,...,p-2}
            BigInteger a;
            do {
                a = new BigInteger(p.bitLength(), random);
            } while (a.compareTo(BigInteger.TWO) < 0 || a.compareTo(p.subtract(BigInteger.TWO)) > 0);

            // IF a^(p-1) ≢ 1 (mod p)
            BigInteger p_minus_1 = p.subtract(BigInteger.ONE);
            if (!square_and_multiply(a, p_minus_1, p).equals(BigInteger.ONE)) {
                return "p is composite";
            }
        }
        return "p is likely prime";
    }

    // Generate prime number within specific range for a given ASCII character
    private BigInteger generate_prime(int asciiValue) {
        SecureRandom random = new SecureRandom();
        BigInteger prime;
        int s = 100;

        // Calculate range: X < p < 2^15-1 where X is ASCII value
        BigInteger low_bound = BigInteger.valueOf(asciiValue);
        BigInteger up_bound = BigInteger.valueOf((1 << 15) - 1); // 2^15 - 1

        System.out.println("Generating prime in range: " + low_bound + " < p < " + up_bound);

        do {
            // Generate random number within range
            BigInteger range = up_bound.subtract(low_bound);
            prime = new BigInteger(range.bitLength(), random);
            prime = prime.mod(range).add(low_bound);

            // Make it odd if it's even
            if (prime.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
                prime = prime.add(BigInteger.ONE);
            }

            // Check if still in range after making odd
            if (prime.compareTo(up_bound) >= 0) {
                continue;
            }

            // Verify it's greater than ASCII value
            if (prime.compareTo(low_bound) <= 0) {
                continue;
            }

        } while (fermat_primality_test(prime, s).equals("p is composite"));

        return prime;
    }

    private void generate_keys(String message) {
        if (message.isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty");
        }

        int asciiValue = (int) message.charAt(0);
        System.out.println("ASCII value of character: (" + message.charAt(0) +  ") "+ asciiValue);

        System.out.println("Generating p...");
        p = generate_prime(asciiValue);

        System.out.println("Generating q...");
        do {
            q = generate_prime(asciiValue);
        } while (p.equals(q)); 

        System.out.println("\nGenerated primes:");
        System.out.println("p = " + p + " (" + fermat_primality_test(p, 5) + ")");
        System.out.println("q = " + q + " (" + fermat_primality_test(q, 5) + ")");

        // n = p * q
        n = p.multiply(q);

        // φ(n) = (p-1)(q-1)
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        // e ∈ {1,2,...,φ(n)-1} s.t. gcd(e,φ(n))=1
        SecureRandom random = new SecureRandom();
        do {
            e = new BigInteger(phi.bitLength(), random);
            // Ensure e is in range [3, φ(n)-1]
            if (e.compareTo(BigInteger.valueOf(1)) < 0 || e.compareTo(phi) >= 0) {
                continue;
            }
        } while (e.gcd(phi).compareTo(BigInteger.ONE) != 0);

        System.out.println("Selected public exponent e = " + e);

        d = extended_euclidean(e, phi);

        // Verify d * e ≡ 1 (mod phi)
        BigInteger test = e.multiply(d).mod(phi);
        if (!test.equals(BigInteger.ONE)) {
            throw new IllegalStateException("Error: d*e != 1 (mod phi)");
        }
    }

    public BigInteger encrypt(BigInteger message) {
        if (message.compareTo(BigInteger.ZERO) < 0 || message.compareTo(n) >= 0) {
            throw new IllegalArgumentException("Message must be between 0 and n-1");
        }
        return square_and_multiply(message, e, n);
    }

    public BigInteger decrypt(BigInteger ciphertext) {
        // Transform to CRT Domain
        xp = ciphertext.mod(p);
        xq = ciphertext.mod(q);

        // Exponentiation in CRT Domain
        dp = d.mod(p.subtract(BigInteger.ONE));
        dq = d.mod(q.subtract(BigInteger.ONE));
        yp = square_and_multiply(xp, dp, p);
        yq = square_and_multiply(xq, dq, q);

        // Inverse Transformation and Final Result
        BigInteger cp = q.modInverse(p);  // q^(-1) mod p
        BigInteger cq = p.modInverse(q);  // p^(-1) mod q

        BigInteger term1 = yp.multiply(cp).multiply(q);
        BigInteger term2 = yq.multiply(cq).multiply(p);

        return term1.add(term2).mod(p.multiply(q));
    }

    /* public BigInteger decrypt(BigInteger ciphertext) {
        if (ciphertext.compareTo(BigInteger.ZERO) < 0 || ciphertext.compareTo(n) >= 0) {
            throw new IllegalArgumentException("Ciphertext must be between 0 and n-1");
        }
        return square_and_multiply(ciphertext, d, n);
    } */

    public void display_parameters() {
        System.out.println("\nRSA Parameters:");
        System.out.println("p = " + p + " (prime 1)");
        System.out.println("q = " + q + " (prime 2)");
        System.out.println("n = p*q = " + n + " (modulus)");
        System.out.println("phi(n) = " + phi);
        System.out.println("e = " + e + " (public key)");
        System.out.println("d = " + d + " (private key)");
    }

    public static void main(String[] args) {
        String message;
        try {
            message = new String(Files.readAllBytes(Paths.get("message.txt"))).trim();
        } catch (IOException e) {
            System.out.println("Error reading file 'message.txt': " + e.getMessage());
            return;
        }

        StringBuilder encryptedValues = new StringBuilder();
        StringBuilder decryptedMessage = new StringBuilder();

        int cnt = 0;
        // Process each character with its own keys
        for (char c : message.toCharArray()) {
            RSA rsa = new RSA();
            rsa.generate_keys(String.valueOf(c));

            BigInteger plaintext = BigInteger.valueOf((int) c);
            BigInteger ciphertext = rsa.encrypt(plaintext);

            if (encryptedValues.length() > 0) {
                encryptedValues.append('\n');
            }
            encryptedValues.append(ciphertext);

            BigInteger decryptedValue = rsa.decrypt(ciphertext);
            char decryptedChar = (char) decryptedValue.intValue();
            decryptedMessage.append(decryptedChar);

            // Display parameters for this character
            System.out.println(String.format("-------- cnt: %s ---------------------------", cnt));
            System.out.println("\nFor character '" + c + "' (ASCII: " + (int)c + "):");
            System.out.println("Encrypted: " + ciphertext);
            System.out.println("Decrypted: '" + decryptedChar + "' (ASCII: " + decryptedValue + ")");
            rsa.display_parameters();
            System.out.println("----------------------------------------");
            cnt++;
        }

        System.out.println("\n================== SUMMARY ==================");
        System.out.println("Original text: '" + message + "'");
        System.out.println("\nEncrypted (ASCII decimal):");
        String[] encValues = encryptedValues.toString().split("\n");
        for (String val : encValues) {
            System.out.println(val);
        }
        System.out.println("\nDecrypted text: '" + decryptedMessage.toString() + "'");
        System.out.println("============================================\n");

        try (FileWriter writer = new FileWriter("encrypted.txt")) {
            String[] values = encryptedValues.toString().split("\n");
            for (int i = 0; i < values.length; i++) {
                writer.write(values[i]);
                if (i < values.length - 1) {
                    writer.write('\n');
                }
            }
            System.out.println("\nEncrypted message saved to 'encrypted.txt'");
        } catch (IOException ex) {
            System.out.println("Error writing to file: " + ex.getMessage());
        }

        try (FileWriter writer = new FileWriter("decrypted.txt")) {
            writer.write(decryptedMessage.toString());
            System.out.println("Decrypted message saved to 'decrypted.txt'");
        } catch (IOException ex) {
            System.out.println("Error writing to file: " + ex.getMessage());
        }
    }
}
