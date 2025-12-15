      *****************************************************************
      * Program:     CARD-NUM-VALIDATION-TESTS.CBL
      * Layer:       Unit Tests
      * Function:    Test cases for 17-digit card number validation
      *****************************************************************
      * Copyright Amazon.com, Inc. or its affiliates.
      * All Rights Reserved.
      *
      * Licensed under the Apache License, Version 2.0 (the "License").
      * You may not use this file except in compliance with the License.
      * You may obtain a copy of the License at
      *
      *    http://www.apache.org/licenses/LICENSE-2.0
      *
      * Unless required by applicable law or agreed to in writing,
      * software distributed under the License is distributed on an
      * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
      * either express or implied. See the License for the specific
      * language governing permissions and limitations under the License
      *****************************************************************
      * TEST SPECIFICATION FOR 17-DIGIT CARD NUMBER VALIDATION
      *
      * This test specification defines unit tests for validating
      * the 17-digit card number validation logic in the following
      * programs:
      *   - COCRDSLC.cbl (Card Select/List)
      *   - COCRDUPC.cbl (Card Update)
      *   - COCRDLIC.cbl (Card List)
      *
      * TEST CASES:
      *****************************************************************

       IDENTIFICATION DIVISION.
       PROGRAM-ID.
           CARDNUMTESTS.
       DATE-WRITTEN.
           December 2024.

       DATA DIVISION.
       WORKING-STORAGE SECTION.

      *****************************************************************
      * Test Data Definitions
      *****************************************************************
       01  TEST-DATA.
           05  TEST-CASE-NUM              PIC 9(3) VALUE 0.
           05  TEST-DESCRIPTION           PIC X(60).
           05  TEST-INPUT-CARD-NUM        PIC X(17).
           05  TEST-EXPECTED-RESULT       PIC X(10).
           05  TEST-ACTUAL-RESULT         PIC X(10).
           05  TEST-PASSED                PIC X(1).
               88 TEST-PASS               VALUE 'Y'.
               88 TEST-FAIL               VALUE 'N'.

      *****************************************************************
      * Test Case 1: Valid 17-digit numeric card number
      * Input:  12345678901234567 (17 numeric digits)
      * Expected: VALID - Card number should be accepted
      *****************************************************************
       01  TC01-VALID-17-DIGIT.
           05  TC01-INPUT                 PIC X(17)
               VALUE '12345678901234567'.
           05  TC01-EXPECTED              PIC X(10)
               VALUE 'VALID'.
           05  TC01-DESC                  PIC X(60)
               VALUE 'Valid 17-digit numeric card number'.

      *****************************************************************
      * Test Case 2: Card number with fewer than 17 digits (16 digits)
      * Input:  1234567890123456 (16 digits, padded with space)
      * Expected: INVALID - Card number should be rejected
      *****************************************************************
       01  TC02-SHORT-16-DIGIT.
           05  TC02-INPUT                 PIC X(17)
               VALUE '1234567890123456 '.
           05  TC02-EXPECTED              PIC X(10)
               VALUE 'INVALID'.
           05  TC02-DESC                  PIC X(60)
               VALUE 'Card number with 16 digits (too short)'.

      *****************************************************************
      * Test Case 3: Card number with fewer than 17 digits (15 digits)
      * Input:  123456789012345 (15 digits, padded with spaces)
      * Expected: INVALID - Card number should be rejected
      *****************************************************************
       01  TC03-SHORT-15-DIGIT.
           05  TC03-INPUT                 PIC X(17)
               VALUE '123456789012345  '.
           05  TC03-EXPECTED              PIC X(10)
               VALUE 'INVALID'.
           05  TC03-DESC                  PIC X(60)
               VALUE 'Card number with 15 digits (too short)'.

      *****************************************************************
      * Test Case 4: Card number with more than 17 digits
      * Input:  123456789012345678 (18 digits - truncated to 17)
      * Expected: VALID - First 17 digits accepted (field truncation)
      * Note: Field is PIC X(17), so extra digits are truncated
      *****************************************************************
       01  TC04-LONG-18-DIGIT.
           05  TC04-INPUT                 PIC X(17)
               VALUE '12345678901234567'.
           05  TC04-EXPECTED              PIC X(10)
               VALUE 'VALID'.
           05  TC04-DESC                  PIC X(60)
               VALUE 'Card number truncated to 17 digits'.

      *****************************************************************
      * Test Case 5: Non-numeric card number (contains letters)
      * Input:  1234567890123456A (contains letter A)
      * Expected: INVALID - Card number should be rejected
      *****************************************************************
       01  TC05-NON-NUMERIC-ALPHA.
           05  TC05-INPUT                 PIC X(17)
               VALUE '1234567890123456A'.
           05  TC05-EXPECTED              PIC X(10)
               VALUE 'INVALID'.
           05  TC05-DESC                  PIC X(60)
               VALUE 'Non-numeric card number (contains letter)'.

      *****************************************************************
      * Test Case 6: Non-numeric card number (contains special chars)
      * Input:  1234567890123456! (contains special character)
      * Expected: INVALID - Card number should be rejected
      *****************************************************************
       01  TC06-NON-NUMERIC-SPECIAL.
           05  TC06-INPUT                 PIC X(17)
               VALUE '1234567890123456!'.
           05  TC06-EXPECTED              PIC X(10)
               VALUE 'INVALID'.
           05  TC06-DESC                  PIC X(60)
               VALUE 'Non-numeric card number (contains special char)'.

      *****************************************************************
      * Test Case 7: Empty card number (all spaces)
      * Input:  (17 spaces)
      * Expected: INVALID - Card number should be rejected
      *****************************************************************
       01  TC07-EMPTY-SPACES.
           05  TC07-INPUT                 PIC X(17)
               VALUE SPACES.
           05  TC07-EXPECTED              PIC X(10)
               VALUE 'INVALID'.
           05  TC07-DESC                  PIC X(60)
               VALUE 'Empty card number (all spaces)'.

      *****************************************************************
      * Test Case 8: Card number with all zeros
      * Input:  00000000000000000 (17 zeros)
      * Expected: INVALID - Card number should be rejected (zero check)
      *****************************************************************
       01  TC08-ALL-ZEROS.
           05  TC08-INPUT                 PIC X(17)
               VALUE '00000000000000000'.
           05  TC08-EXPECTED              PIC X(10)
               VALUE 'INVALID'.
           05  TC08-DESC                  PIC X(60)
               VALUE 'Card number with all zeros'.

      *****************************************************************
      * Test Case 9: Card number with leading zeros
      * Input:  00000000000000001 (leading zeros, valid)
      * Expected: VALID - Card number should be accepted
      *****************************************************************
       01  TC09-LEADING-ZEROS.
           05  TC09-INPUT                 PIC X(17)
               VALUE '00000000000000001'.
           05  TC09-EXPECTED              PIC X(10)
               VALUE 'VALID'.
           05  TC09-DESC                  PIC X(60)
               VALUE 'Card number with leading zeros (non-zero value)'.

      *****************************************************************
      * Test Case 10: Card number with LOW-VALUES
      * Input:  LOW-VALUES
      * Expected: INVALID - Card number should be rejected
      *****************************************************************
       01  TC10-LOW-VALUES.
           05  TC10-INPUT                 PIC X(17)
               VALUE LOW-VALUES.
           05  TC10-EXPECTED              PIC X(10)
               VALUE 'INVALID'.
           05  TC10-DESC                  PIC X(60)
               VALUE 'Card number with LOW-VALUES'.

      *****************************************************************
      * Test Case 11: Mixed numeric and spaces
      * Input:  12345678901234 67 (space in middle)
      * Expected: INVALID - Card number should be rejected
      *****************************************************************
       01  TC11-MIXED-SPACES.
           05  TC11-INPUT                 PIC X(17)
               VALUE '12345678901234 67'.
           05  TC11-EXPECTED              PIC X(10)
               VALUE 'INVALID'.
           05  TC11-DESC                  PIC X(60)
               VALUE 'Card number with embedded space'.

      *****************************************************************
      * Test Case 12: Valid card number at boundary (all 9s)
      * Input:  99999999999999999 (17 nines - max value)
      * Expected: VALID - Card number should be accepted
      *****************************************************************
       01  TC12-MAX-VALUE.
           05  TC12-INPUT                 PIC X(17)
               VALUE '99999999999999999'.
           05  TC12-EXPECTED              PIC X(10)
               VALUE 'VALID'.
           05  TC12-DESC                  PIC X(60)
               VALUE 'Card number at maximum value (all 9s)'.

       PROCEDURE DIVISION.
       0000-MAIN.
      *****************************************************************
      * Main test execution procedure
      * Note: This is a test specification. Actual execution requires
      * a COBOL unit testing framework such as:
      *   - IBM zUnit
      *   - Micro Focus Unit Testing Framework
      *   - Compuware Topaz for Total Test
      *****************************************************************
           DISPLAY '================================================='
           DISPLAY 'CARD NUMBER VALIDATION TEST SUITE'
           DISPLAY '17-DIGIT CARD NUMBER VALIDATION TESTS'
           DISPLAY '================================================='
           DISPLAY ' '

           PERFORM TEST-CASE-01
           PERFORM TEST-CASE-02
           PERFORM TEST-CASE-03
           PERFORM TEST-CASE-04
           PERFORM TEST-CASE-05
           PERFORM TEST-CASE-06
           PERFORM TEST-CASE-07
           PERFORM TEST-CASE-08
           PERFORM TEST-CASE-09
           PERFORM TEST-CASE-10
           PERFORM TEST-CASE-11
           PERFORM TEST-CASE-12

           DISPLAY ' '
           DISPLAY '================================================='
           DISPLAY 'TEST SUITE COMPLETE'
           DISPLAY '================================================='

           STOP RUN
           .

       TEST-CASE-01.
           MOVE 1 TO TEST-CASE-NUM
           MOVE TC01-DESC TO TEST-DESCRIPTION
           DISPLAY 'TC01: ' TEST-DESCRIPTION
           DISPLAY '  Input: ' TC01-INPUT
           DISPLAY '  Expected: ' TC01-EXPECTED
           .

       TEST-CASE-02.
           MOVE 2 TO TEST-CASE-NUM
           MOVE TC02-DESC TO TEST-DESCRIPTION
           DISPLAY 'TC02: ' TEST-DESCRIPTION
           DISPLAY '  Input: ' TC02-INPUT
           DISPLAY '  Expected: ' TC02-EXPECTED
           .

       TEST-CASE-03.
           MOVE 3 TO TEST-CASE-NUM
           MOVE TC03-DESC TO TEST-DESCRIPTION
           DISPLAY 'TC03: ' TEST-DESCRIPTION
           DISPLAY '  Input: ' TC03-INPUT
           DISPLAY '  Expected: ' TC03-EXPECTED
           .

       TEST-CASE-04.
           MOVE 4 TO TEST-CASE-NUM
           MOVE TC04-DESC TO TEST-DESCRIPTION
           DISPLAY 'TC04: ' TEST-DESCRIPTION
           DISPLAY '  Input: ' TC04-INPUT
           DISPLAY '  Expected: ' TC04-EXPECTED
           .

       TEST-CASE-05.
           MOVE 5 TO TEST-CASE-NUM
           MOVE TC05-DESC TO TEST-DESCRIPTION
           DISPLAY 'TC05: ' TEST-DESCRIPTION
           DISPLAY '  Input: ' TC05-INPUT
           DISPLAY '  Expected: ' TC05-EXPECTED
           .

       TEST-CASE-06.
           MOVE 6 TO TEST-CASE-NUM
           MOVE TC06-DESC TO TEST-DESCRIPTION
           DISPLAY 'TC06: ' TEST-DESCRIPTION
           DISPLAY '  Input: ' TC06-INPUT
           DISPLAY '  Expected: ' TC06-EXPECTED
           .

       TEST-CASE-07.
           MOVE 7 TO TEST-CASE-NUM
           MOVE TC07-DESC TO TEST-DESCRIPTION
           DISPLAY 'TC07: ' TEST-DESCRIPTION
           DISPLAY '  Input: ' TC07-INPUT
           DISPLAY '  Expected: ' TC07-EXPECTED
           .

       TEST-CASE-08.
           MOVE 8 TO TEST-CASE-NUM
           MOVE TC08-DESC TO TEST-DESCRIPTION
           DISPLAY 'TC08: ' TEST-DESCRIPTION
           DISPLAY '  Input: ' TC08-INPUT
           DISPLAY '  Expected: ' TC08-EXPECTED
           .

       TEST-CASE-09.
           MOVE 9 TO TEST-CASE-NUM
           MOVE TC09-DESC TO TEST-DESCRIPTION
           DISPLAY 'TC09: ' TEST-DESCRIPTION
           DISPLAY '  Input: ' TC09-INPUT
           DISPLAY '  Expected: ' TC09-EXPECTED
           .

       TEST-CASE-10.
           MOVE 10 TO TEST-CASE-NUM
           MOVE TC10-DESC TO TEST-DESCRIPTION
           DISPLAY 'TC10: ' TEST-DESCRIPTION
           DISPLAY '  Input: LOW-VALUES'
           DISPLAY '  Expected: ' TC10-EXPECTED
           .

       TEST-CASE-11.
           MOVE 11 TO TEST-CASE-NUM
           MOVE TC11-DESC TO TEST-DESCRIPTION
           DISPLAY 'TC11: ' TEST-DESCRIPTION
           DISPLAY '  Input: ' TC11-INPUT
           DISPLAY '  Expected: ' TC11-EXPECTED
           .

       TEST-CASE-12.
           MOVE 12 TO TEST-CASE-NUM
           MOVE TC12-DESC TO TEST-DESCRIPTION
           DISPLAY 'TC12: ' TEST-DESCRIPTION
           DISPLAY '  Input: ' TC12-INPUT
           DISPLAY '  Expected: ' TC12-EXPECTED
           .

      *
      * Ver: CardDemo_v1.0 Date: 2024-12-15
      *
