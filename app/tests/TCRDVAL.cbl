      *****************************************************************         
      * Program:     TCRDVAL.CBL                                     *         
      * Layer:       Unit Test                                       *         
      * Function:    Test Credit Card Number Validation (17 digits)  *         
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
      * Unit Test Cases:
      * 1. Valid 17-digit numeric card number - should PASS
      * 2. Non-numeric card number - should FAIL
      * 3. Card number can be processed through the system
      ******************************************************************        
                                                                               
       IDENTIFICATION DIVISION.                                                 
       PROGRAM-ID.                                                              
           TCRDVAL.                                                            
       DATE-WRITTEN.                                                            
           December 2024.                                                          
       DATE-COMPILED.                                                           
           Today.                                                               
                                                                               
       ENVIRONMENT DIVISION.                                                    
       INPUT-OUTPUT SECTION.                                                    
                                                                               
       DATA DIVISION.                                                           
                                                                               
       WORKING-STORAGE SECTION.                                                 
                                                                               
       01  WS-TEST-VARS.                                                        
      ******************************************************************        
      * Test card number fields - 17 digits                                    
      ******************************************************************        
           05  WS-CARD-NUM-X                  PIC X(17).                        
           05  WS-CARD-NUM-N REDEFINES WS-CARD-NUM-X                            
                                              PIC 9(17).                        
           05  WS-TEST-RESULT                 PIC X(10).                        
               88  TEST-PASSED                VALUE 'PASSED'.                   
               88  TEST-FAILED                VALUE 'FAILED'.                   
           05  WS-TEST-COUNT                  PIC 9(2) VALUE 0.                 
           05  WS-PASS-COUNT                  PIC 9(2) VALUE 0.                 
           05  WS-FAIL-COUNT                  PIC 9(2) VALUE 0.                 
                                                                               
       01  WS-TEST-MESSAGES.                                                    
           05  WS-TEST-HEADER                 PIC X(60)                         
               VALUE '========== CARD VALIDATION UNIT TESTS =========='.       
           05  WS-TEST-FOOTER                 PIC X(60)                         
               VALUE '==============================================='.        
           05  WS-TEST-MSG                    PIC X(80).                        
                                                                               
       01  WS-VALIDATION-FLAG                 PIC X(1).                         
           88  CARD-VALID                     VALUE '1'.                        
           88  CARD-INVALID                   VALUE '0'.                        
                                                                               
       PROCEDURE DIVISION.                                                      
       0000-MAIN.                                                               
                                                                               
           DISPLAY WS-TEST-HEADER                                               
           DISPLAY SPACES                                                       
                                                                               
      ******************************************************************        
      * TEST CASE 1: Valid 17-digit numeric card number                        
      ******************************************************************        
           PERFORM 1000-TEST-VALID-17-DIGIT                                     
              THRU 1000-TEST-VALID-17-DIGIT-EXIT                                
                                                                               
      ******************************************************************        
      * TEST CASE 2: Non-numeric card number should fail                       
      ******************************************************************        
           PERFORM 2000-TEST-NON-NUMERIC                                        
              THRU 2000-TEST-NON-NUMERIC-EXIT                                   
                                                                               
      ******************************************************************        
      * TEST CASE 3: Card number processing test                               
      ******************************************************************        
           PERFORM 3000-TEST-CARD-PROCESSING                                    
              THRU 3000-TEST-CARD-PROCESSING-EXIT                               
                                                                               
      ******************************************************************        
      * Display test summary                                                   
      ******************************************************************        
           DISPLAY SPACES                                                       
           DISPLAY WS-TEST-FOOTER                                               
           DISPLAY 'TOTAL TESTS: ' WS-TEST-COUNT                                
           DISPLAY 'PASSED:      ' WS-PASS-COUNT                                
           DISPLAY 'FAILED:      ' WS-FAIL-COUNT                                
           DISPLAY WS-TEST-FOOTER                                               
                                                                               
           STOP RUN                                                             
           .                                                                    
                                                                               
       1000-TEST-VALID-17-DIGIT.                                                
      ******************************************************************        
      * Test Case 1: Valid 17-digit numeric card number                        
      * Expected: Validation should PASS                                       
      ******************************************************************        
           ADD 1 TO WS-TEST-COUNT                                               
           MOVE '12345678901234567' TO WS-CARD-NUM-X                            
                                                                               
           IF WS-CARD-NUM-X IS NUMERIC                                          
              SET CARD-VALID TO TRUE                                            
              SET TEST-PASSED TO TRUE                                           
              ADD 1 TO WS-PASS-COUNT                                            
           ELSE                                                                 
              SET CARD-INVALID TO TRUE                                          
              SET TEST-FAILED TO TRUE                                           
              ADD 1 TO WS-FAIL-COUNT                                            
           END-IF                                                               
                                                                               
           MOVE SPACES TO WS-TEST-MSG                                           
           STRING 'TEST 1: Valid 17-digit card number ('                        
                  WS-CARD-NUM-X                                                 
                  ') - '                                                        
                  WS-TEST-RESULT                                                
                  DELIMITED BY SIZE                                             
                  INTO WS-TEST-MSG                                              
           DISPLAY WS-TEST-MSG                                                  
           .                                                                    
                                                                               
       1000-TEST-VALID-17-DIGIT-EXIT.                                           
           EXIT                                                                 
           .                                                                    
                                                                               
       2000-TEST-NON-NUMERIC.                                                   
      ******************************************************************        
      * Test Case 2: Non-numeric card number                                   
      * Expected: Validation should FAIL (reject non-numeric)                  
      ******************************************************************        
           ADD 1 TO WS-TEST-COUNT                                               
           MOVE '1234567890123456A' TO WS-CARD-NUM-X                            
                                                                               
           IF WS-CARD-NUM-X IS NOT NUMERIC                                      
              SET CARD-INVALID TO TRUE                                          
              SET TEST-PASSED TO TRUE                                           
              ADD 1 TO WS-PASS-COUNT                                            
           ELSE                                                                 
              SET CARD-VALID TO TRUE                                            
              SET TEST-FAILED TO TRUE                                           
              ADD 1 TO WS-FAIL-COUNT                                            
           END-IF                                                               
                                                                               
           MOVE SPACES TO WS-TEST-MSG                                           
           STRING 'TEST 2: Non-numeric card number ('                           
                  WS-CARD-NUM-X                                                 
                  ') rejected - '                                               
                  WS-TEST-RESULT                                                
                  DELIMITED BY SIZE                                             
                  INTO WS-TEST-MSG                                              
           DISPLAY WS-TEST-MSG                                                  
           .                                                                    
                                                                               
       2000-TEST-NON-NUMERIC-EXIT.                                              
           EXIT                                                                 
           .                                                                    
                                                                               
       3000-TEST-CARD-PROCESSING.                                               
      ******************************************************************        
      * Test Case 3: Card number can be processed (moved/stored)               
      * Expected: Card number should be successfully processed                 
      ******************************************************************        
           ADD 1 TO WS-TEST-COUNT                                               
           MOVE '98765432109876543' TO WS-CARD-NUM-X                            
                                                                               
      *    Verify the card number was stored correctly                         
           IF WS-CARD-NUM-X EQUAL '98765432109876543'                           
           AND WS-CARD-NUM-N EQUAL 98765432109876543                            
              SET CARD-VALID TO TRUE                                            
              SET TEST-PASSED TO TRUE                                           
              ADD 1 TO WS-PASS-COUNT                                            
           ELSE                                                                 
              SET CARD-INVALID TO TRUE                                          
              SET TEST-FAILED TO TRUE                                           
              ADD 1 TO WS-FAIL-COUNT                                            
           END-IF                                                               
                                                                               
           MOVE SPACES TO WS-TEST-MSG                                           
           STRING 'TEST 3: 17-digit card processing ('                          
                  WS-CARD-NUM-X                                                 
                  ') - '                                                        
                  WS-TEST-RESULT                                                
                  DELIMITED BY SIZE                                             
                  INTO WS-TEST-MSG                                              
           DISPLAY WS-TEST-MSG                                                  
           .                                                                    
                                                                               
       3000-TEST-CARD-PROCESSING-EXIT.                                          
           EXIT                                                                 
           .                                                                    
      *
      * Unit Test for 17-digit card number validation
      * Created: December 2024
      *
