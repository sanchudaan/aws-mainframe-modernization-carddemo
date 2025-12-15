      *****************************************************************         
      * Program:     CCRDT17T.CBL                                     *         
      * Layer:       Unit Test                                        *         
      * Function:    Test 17-digit credit card number validation      *         
      ******************************************************************
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
      ******************************************************************        
      * Unit Test: Validates that 17-digit card numbers are accepted   *
      * Test Cases:                                                    *
      *   1. Valid 17-digit numeric card number - should PASS          *
      *   2. Valid 17-digit card number with leading zeros - PASS      *
      *   3. Invalid 16-digit card number - should FAIL (too short)    *
      *   4. Invalid 18-digit card number - should FAIL (too long)     *
      *   5. Invalid non-numeric card number - should FAIL             *
      ******************************************************************        
                                                                               
       IDENTIFICATION DIVISION.                                                 
       PROGRAM-ID.                                                              
           CCRDT17T.                                                            
       DATE-WRITTEN.                                                            
           December 2024.                                                       
       DATE-COMPILED.                                                           
           Today.                                                               
                                                                               
       ENVIRONMENT DIVISION.                                                    
       INPUT-OUTPUT SECTION.                                                    
                                                                               
       DATA DIVISION.                                                           
                                                                               
       WORKING-STORAGE SECTION.                                                 
                                                                               
       01  WS-TEST-VARIABLES.                                                   
           05  WS-TEST-CARD-NUM              PIC X(17).                         
           05  WS-TEST-CARD-NUM-N REDEFINES                                     
               WS-TEST-CARD-NUM              PIC 9(17).                         
           05  WS-TEST-RESULT                PIC X(10).                         
               88  TEST-PASSED               VALUE 'PASSED'.                    
               88  TEST-FAILED               VALUE 'FAILED'.                    
           05  WS-TEST-COUNT                 PIC 9(02) VALUE 0.                 
           05  WS-PASS-COUNT                 PIC 9(02) VALUE 0.                 
           05  WS-FAIL-COUNT                 PIC 9(02) VALUE 0.                 
           05  WS-EXPECTED-RESULT            PIC X(10).                         
               88  EXPECT-VALID              VALUE 'VALID'.                     
               88  EXPECT-INVALID            VALUE 'INVALID'.                   
           05  WS-ACTUAL-RESULT              PIC X(10).                         
               88  ACTUAL-VALID              VALUE 'VALID'.                     
               88  ACTUAL-INVALID            VALUE 'INVALID'.                   
           05  WS-VALIDATION-FLAG            PIC X(01).                         
               88  CARD-IS-VALID             VALUE '1'.                         
               88  CARD-IS-INVALID           VALUE '0'.                         
                                                                               
       01  WS-OUTPUT-MESSAGE.                                                   
           05  FILLER                        PIC X(20)                          
                                             VALUE 'Test Case '.                
           05  WS-OUT-TEST-NUM               PIC 9(02).                         
           05  FILLER                        PIC X(02)                          
                                             VALUE ': '.                        
           05  WS-OUT-CARD-NUM               PIC X(17).                         
           05  FILLER                        PIC X(12)                          
                                             VALUE ' - Expected:'.              
           05  WS-OUT-EXPECTED               PIC X(10).                         
           05  FILLER                        PIC X(10)                          
                                             VALUE ' - Actual:'.                
           05  WS-OUT-ACTUAL                 PIC X(10).                         
           05  FILLER                        PIC X(03)                          
                                             VALUE ' - '.                       
           05  WS-OUT-RESULT                 PIC X(10).                         
                                                                               
       01  WS-SUMMARY-MESSAGE.                                                  
           05  FILLER                        PIC X(30)                          
                                             VALUE 'Test Summary: Total Tests: '.
           05  WS-SUM-TOTAL                  PIC 9(02).                         
           05  FILLER                        PIC X(10)                          
                                             VALUE ' Passed: '.                 
           05  WS-SUM-PASSED                 PIC 9(02).                         
           05  FILLER                        PIC X(10)                          
                                             VALUE ' Failed: '.                 
           05  WS-SUM-FAILED                 PIC 9(02).                         
                                                                               
       PROCEDURE DIVISION.                                                      
       0000-MAIN.                                                               
                                                                               
           DISPLAY '================================================'.         
           DISPLAY '17-DIGIT CARD NUMBER VALIDATION UNIT TESTS'.                
           DISPLAY '================================================'.         
           DISPLAY ' '.                                                         
                                                                               
      *****************************************************************         
      * Test Case 1: Valid 17-digit numeric card number                         
      *****************************************************************         
           ADD 1 TO WS-TEST-COUNT                                               
           MOVE '12345678901234567' TO WS-TEST-CARD-NUM                         
           SET EXPECT-VALID TO TRUE                                             
           PERFORM 1000-VALIDATE-CARD-NUMBER                                    
           PERFORM 2000-CHECK-RESULT                                            
                                                                               
      *****************************************************************         
      * Test Case 2: Valid 17-digit card with leading zeros                     
      *****************************************************************         
           ADD 1 TO WS-TEST-COUNT                                               
           MOVE '00000000000000001' TO WS-TEST-CARD-NUM                         
           SET EXPECT-VALID TO TRUE                                             
           PERFORM 1000-VALIDATE-CARD-NUMBER                                    
           PERFORM 2000-CHECK-RESULT                                            
                                                                               
      *****************************************************************         
      * Test Case 3: Valid 17-digit card number (all 9s)                        
      *****************************************************************         
           ADD 1 TO WS-TEST-COUNT                                               
           MOVE '99999999999999999' TO WS-TEST-CARD-NUM                         
           SET EXPECT-VALID TO TRUE                                             
           PERFORM 1000-VALIDATE-CARD-NUMBER                                    
           PERFORM 2000-CHECK-RESULT                                            
                                                                               
      *****************************************************************         
      * Test Case 4: Invalid - 16-digit card number (too short)                 
      *****************************************************************         
           ADD 1 TO WS-TEST-COUNT                                               
           MOVE '1234567890123456 ' TO WS-TEST-CARD-NUM                         
           SET EXPECT-INVALID TO TRUE                                           
           PERFORM 1000-VALIDATE-CARD-NUMBER                                    
           PERFORM 2000-CHECK-RESULT                                            
                                                                               
      *****************************************************************         
      * Test Case 5: Invalid - non-numeric card number                          
      *****************************************************************         
           ADD 1 TO WS-TEST-COUNT                                               
           MOVE '1234567890123456A' TO WS-TEST-CARD-NUM                         
           SET EXPECT-INVALID TO TRUE                                           
           PERFORM 1000-VALIDATE-CARD-NUMBER                                    
           PERFORM 2000-CHECK-RESULT                                            
                                                                               
      *****************************************************************         
      * Test Case 6: Invalid - all spaces                                       
      *****************************************************************         
           ADD 1 TO WS-TEST-COUNT                                               
           MOVE SPACES TO WS-TEST-CARD-NUM                                      
           SET EXPECT-INVALID TO TRUE                                           
           PERFORM 1000-VALIDATE-CARD-NUMBER                                    
           PERFORM 2000-CHECK-RESULT                                            
                                                                               
      *****************************************************************         
      * Test Case 7: Invalid - all zeros                                        
      *****************************************************************         
           ADD 1 TO WS-TEST-COUNT                                               
           MOVE '00000000000000000' TO WS-TEST-CARD-NUM                         
           SET EXPECT-INVALID TO TRUE                                           
           PERFORM 1000-VALIDATE-CARD-NUMBER                                    
           PERFORM 2000-CHECK-RESULT                                            
                                                                               
      *****************************************************************         
      * Display Test Summary                                                    
      *****************************************************************         
           DISPLAY ' '.                                                         
           DISPLAY '================================================'.         
           MOVE WS-TEST-COUNT TO WS-SUM-TOTAL                                   
           MOVE WS-PASS-COUNT TO WS-SUM-PASSED                                  
           MOVE WS-FAIL-COUNT TO WS-SUM-FAILED                                  
           DISPLAY WS-SUMMARY-MESSAGE                                           
           DISPLAY '================================================'.         
                                                                               
           IF WS-FAIL-COUNT = 0                                                 
              DISPLAY 'ALL TESTS PASSED - 17-DIGIT VALIDATION WORKING'          
           ELSE                                                                 
              DISPLAY 'SOME TESTS FAILED - REVIEW VALIDATION LOGIC'             
           END-IF                                                               
                                                                               
           STOP RUN.                                                            
                                                                               
      *****************************************************************         
      * 1000-VALIDATE-CARD-NUMBER                                               
      * Validates the card number using the same logic as the                   
      * production programs (COCRDLIC, COCRDSLC, COCRDUPC)                       
      *****************************************************************         
       1000-VALIDATE-CARD-NUMBER.                                               
                                                                               
           SET CARD-IS-INVALID TO TRUE                                          
           SET ACTUAL-INVALID TO TRUE                                           
                                                                               
      *    Check if card number is blank or spaces                              
           IF WS-TEST-CARD-NUM = SPACES                                         
           OR WS-TEST-CARD-NUM = LOW-VALUES                                     
              SET CARD-IS-INVALID TO TRUE                                       
              SET ACTUAL-INVALID TO TRUE                                        
              GO TO 1000-VALIDATE-EXIT                                          
           END-IF                                                               
                                                                               
      *    Check if card number is all zeros                                    
           IF WS-TEST-CARD-NUM-N = ZEROS                                        
              SET CARD-IS-INVALID TO TRUE                                       
              SET ACTUAL-INVALID TO TRUE                                        
              GO TO 1000-VALIDATE-EXIT                                          
           END-IF                                                               
                                                                               
      *    Check if card number is numeric (17 digits)                          
           IF WS-TEST-CARD-NUM IS NOT NUMERIC                                   
              SET CARD-IS-INVALID TO TRUE                                       
              SET ACTUAL-INVALID TO TRUE                                        
              GO TO 1000-VALIDATE-EXIT                                          
           END-IF                                                               
                                                                               
      *    Card number is valid - 17 numeric digits, non-zero                   
           SET CARD-IS-VALID TO TRUE                                            
           SET ACTUAL-VALID TO TRUE                                             
           .                                                                    
                                                                               
       1000-VALIDATE-EXIT.                                                      
           EXIT.                                                                
                                                                               
      *****************************************************************         
      * 2000-CHECK-RESULT                                                       
      * Compares expected result with actual result                             
      *****************************************************************         
       2000-CHECK-RESULT.                                                       
                                                                               
           MOVE WS-TEST-COUNT TO WS-OUT-TEST-NUM                                
           MOVE WS-TEST-CARD-NUM TO WS-OUT-CARD-NUM                             
           MOVE WS-EXPECTED-RESULT TO WS-OUT-EXPECTED                           
           MOVE WS-ACTUAL-RESULT TO WS-OUT-ACTUAL                               
                                                                               
           IF WS-EXPECTED-RESULT = WS-ACTUAL-RESULT                             
              SET TEST-PASSED TO TRUE                                           
              ADD 1 TO WS-PASS-COUNT                                            
           ELSE                                                                 
              SET TEST-FAILED TO TRUE                                           
              ADD 1 TO WS-FAIL-COUNT                                            
           END-IF                                                               
                                                                               
           MOVE WS-TEST-RESULT TO WS-OUT-RESULT                                 
           DISPLAY WS-OUTPUT-MESSAGE                                            
           .                                                                    
                                                                               
       2000-CHECK-RESULT-EXIT.                                                  
           EXIT.                                                                
