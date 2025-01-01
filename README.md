# Security Algorithms

This repository contains implementations of various fundamental cryptographic and security algorithms in Java.

## Repository Structure

```
src/
├── AES/                    # Advanced Encryption Standard implementation
│   ├── AES.java           # Main AES implementation
│   └── MCTables.java      # Multiplication and substitution tables
├── DES/                    # Data Encryption Standard implementation
│   ├── DES.java           # Main DES implementation
│   └── Makefile           # Build configuration
├── RSA/                    # RSA-related files
│   └── message.txt        # Sample message for RSA
├── RSA.java               # RSA implementation
├── lfsr.java              # Linear Feedback Shift Register implementation
├── Makefile               # Main build configuration
└── Various text files     # Keys and test data
```

## Implemented Algorithms

- **RSA (RSA.java)**: Public-key cryptography implementation using BigInteger for large number operations
- **AES (AES/AES.java)**: Advanced Encryption Standard implementation with full block cipher operations
- **DES (DES/DES.java)**: Data Encryption Standard implementation
- **LFSR (lfsr.java)**: Linear Feedback Shift Register implementation

## Building and Running

Use the provided Makefiles to compile the Java source files. Each algorithm can be compiled and run independently.

## Note

This is an educational implementation of cryptographic algorithms. For production use, please use established cryptographic libraries and follow current security best practices.
