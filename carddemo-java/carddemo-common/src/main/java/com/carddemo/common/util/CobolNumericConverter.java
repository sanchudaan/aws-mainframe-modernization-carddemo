package com.carddemo.common.util;

import java.math.BigDecimal;
import java.nio.charset.Charset;

/**
 * Utility class for converting COBOL numeric formats to Java types.
 * Handles packed decimal (COMP-3), zoned decimal, and binary (COMP) formats.
 */
public final class CobolNumericConverter {

    private static final Charset EBCDIC = Charset.forName("IBM037");

    private CobolNumericConverter() {
        // Utility class
    }

    /**
     * Converts a COBOL packed decimal (COMP-3) to BigDecimal.
     * 
     * @param packed the packed decimal bytes
     * @param scale the number of decimal places
     * @return the converted BigDecimal value
     */
    public static BigDecimal fromPackedDecimal(byte[] packed, int scale) {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < packed.length - 1; i++) {
            sb.append((packed[i] >> 4) & 0x0F);
            sb.append(packed[i] & 0x0F);
        }
        
        // Last byte: high nibble is digit, low nibble is sign
        sb.append((packed[packed.length - 1] >> 4) & 0x0F);
        int sign = packed[packed.length - 1] & 0x0F;
        
        BigDecimal value = new BigDecimal(sb.toString()).movePointLeft(scale);
        
        // Sign: 0x0D = negative, 0x0C or 0x0F = positive
        return (sign == 0x0D) ? value.negate() : value;
    }

    /**
     * Converts a BigDecimal to COBOL packed decimal (COMP-3) format.
     * 
     * @param value the BigDecimal value
     * @param totalDigits total number of digits (before and after decimal)
     * @param scale the number of decimal places
     * @return the packed decimal bytes
     */
    public static byte[] toPackedDecimal(BigDecimal value, int totalDigits, int scale) {
        boolean negative = value.signum() < 0;
        String digits = value.abs().movePointRight(scale).toBigInteger().toString();
        
        // Pad with leading zeros
        while (digits.length() < totalDigits) {
            digits = "0" + digits;
        }
        
        // Packed decimal: 2 digits per byte, last nibble is sign
        int byteLength = (totalDigits + 2) / 2;
        byte[] packed = new byte[byteLength];
        
        int digitIndex = 0;
        for (int i = 0; i < byteLength - 1; i++) {
            int high = digits.charAt(digitIndex++) - '0';
            int low = digits.charAt(digitIndex++) - '0';
            packed[i] = (byte) ((high << 4) | low);
        }
        
        // Last byte: one digit + sign
        int lastDigit = digits.charAt(digitIndex) - '0';
        int sign = negative ? 0x0D : 0x0C;
        packed[byteLength - 1] = (byte) ((lastDigit << 4) | sign);
        
        return packed;
    }

    /**
     * Converts a COBOL zoned decimal string to BigDecimal.
     * 
     * @param zoned the zoned decimal string (EBCDIC or ASCII)
     * @param scale the number of decimal places
     * @return the converted BigDecimal value
     */
    public static BigDecimal fromZonedDecimal(String zoned, int scale) {
        if (zoned == null || zoned.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        StringBuilder sb = new StringBuilder();
        boolean negative = false;
        
        for (int i = 0; i < zoned.length(); i++) {
            char c = zoned.charAt(i);
            if (Character.isDigit(c)) {
                sb.append(c);
            } else if (c == '-') {
                negative = true;
            } else if (c == '+' || c == ' ') {
                // Ignore
            } else {
                // Handle EBCDIC overpunch signs
                int digit = getOverpunchDigit(c);
                if (digit >= 0) {
                    sb.append(digit);
                    if (isNegativeOverpunch(c)) {
                        negative = true;
                    }
                }
            }
        }
        
        if (sb.length() == 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal value = new BigDecimal(sb.toString()).movePointLeft(scale);
        return negative ? value.negate() : value;
    }

    private static int getOverpunchDigit(char c) {
        // EBCDIC overpunch characters for positive: { A B C D E F G H I
        // EBCDIC overpunch characters for negative: } J K L M N O P Q R
        return switch (c) {
            case '{', '}' -> 0;
            case 'A', 'J' -> 1;
            case 'B', 'K' -> 2;
            case 'C', 'L' -> 3;
            case 'D', 'M' -> 4;
            case 'E', 'N' -> 5;
            case 'F', 'O' -> 6;
            case 'G', 'P' -> 7;
            case 'H', 'Q' -> 8;
            case 'I', 'R' -> 9;
            default -> -1;
        };
    }

    private static boolean isNegativeOverpunch(char c) {
        return c == '}' || (c >= 'J' && c <= 'R');
    }

    /**
     * Converts EBCDIC bytes to ASCII string.
     * 
     * @param ebcdic the EBCDIC bytes
     * @return the ASCII string
     */
    public static String ebcdicToAscii(byte[] ebcdic) {
        return new String(ebcdic, EBCDIC);
    }

    /**
     * Converts ASCII string to EBCDIC bytes.
     * 
     * @param ascii the ASCII string
     * @return the EBCDIC bytes
     */
    public static byte[] asciiToEbcdic(String ascii) {
        return ascii.getBytes(EBCDIC);
    }
}
