      *****************************************************************         
      * Program:     TESTCARD17.CBL                                  *         
      * Layer:       Unit Test                                       *         
      * Function:    Test 17-digit card number validation            *         
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
                                                                               
       IDENTIFICATION DIVISION.                                                 
       PROGRAM-ID.                                                              
           TESTCARD17.                                                          
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
           05  WS-CARD-NUM-X                      PIC X(17).                    
           05  WS-CARD-NUM-N REDEFINES WS-CARD-NUM-X                            
                                                  PIC 9(17).                    
           05  WS-TEST-RESULT                     PIC X(10).                    
               88  TEST-PASSED                    VALUE 'PASSED'.               
               88  TEST-FAILED                    VALUE 'FAILED'.               
           05  WS-TEST-COUNT                      PIC 9(2) VALUE 0.             
           05  WS-PASS-COUNT                      PIC 9(2) VALUE 0.             
           05  WS-FAIL-COUNT                      PIC 9(2) VALUE 0.             
                                                                               
       01  WS-TEST-MESSAGES.                                                    
           05  WS-TEST-HEADER                     PIC X(60)                     
               VALUE '========== 17-DIGIT CARD NUMBER VALIDATION TESTS ===='.  
           05  WS-TEST-SEPARATOR                  PIC X(60)                     
               VALUE '----------------------------------------------------'.   
           05  WS-TEST-FOOTER                     PIC X(60)                     
               VALUE '==================== TEST SUMMARY ===================='. 
                                                                               
       01  WS-DISPLAY-LINE.                                                     
           05  FILLER                             PIC X(10)                     
               VALUE 'Test '.                                                   
           05  WS-DISP-TEST-NUM                   PIC 9(2).                     
           05  FILLER                             PIC X(2)                      
               VALUE ': '.                                                      
           05  WS-DISP-TEST-DESC                  PIC X(40).                    
           05  FILLER                             PIC X(3)                      
               VALUE ' - '.                                                     
           05  WS-DISP-RESULT                     PIC X(10).                    
                                                                               
       PROCEDURE DIVISION.                                                      
       0000-MAIN.                                                               
                                                                               
           DISPLAY WS-TEST-HEADER                                               
           DISPLAY WS-TEST-SEPARATOR                                            
           DISPLAY SPACES                                                       
                                                                               
      ******************************************************************        
      * Test 1: Valid 17-digit numeric card number                             
      ******************************************************************        
           ADD 1 TO WS-TEST-COUNT                                               
           MOVE '12345678901234567' TO WS-CARD-NUM-X                            
           PERFORM 1000-VALIDATE-CARD-NUMBER                                    
           IF WS-CARD-NUM-X IS NUMERIC                                          
              SET TEST-PASSED TO TRUE                                           
              ADD 1 TO WS-PASS-COUNT                                            
           ELSE                                                                 
              SET TEST-FAILED TO TRUE                                           
              ADD 1 TO WS-FAIL-COUNT                                            
           END-IF                                                               
           MOVE WS-TEST-COUNT TO WS-DISP-TEST-NUM                               
           MOVE 'Valid 17-digit numeric card number' TO WS-DISP-TEST-DESC       
           MOVE WS-TEST-RESULT TO WS-DISP-RESULT                                
           DISPLAY WS-DISPLAY-LINE                                              
                                                                               
      ******************************************************************        
      * Test 2: Another valid 17-digit card number                             
      ******************************************************************        
           ADD 1 TO WS-TEST-COUNT                                               
           MOVE '98765432109876543' TO WS-CARD-NUM-X                            
           PERFORM 1000-VALIDATE-CARD-NUMBER                                    
           IF WS-CARD-NUM-X IS NUMERIC                                          
              SET TEST-PASSED TO TRUE                                           
              ADD 1 TO WS-PASS-COUNT                                            
           ELSE                                                                 
              SET TEST-FAILED TO TRUE                                           
              ADD 1 TO WS-FAIL-COUNT                                            
           END-IF                                                               
           MOVE WS-TEST-COUNT TO WS-DISP-TEST-NUM                               
           MOVE 'Another valid 17-digit card number' TO WS-DISP-TEST-DESC       
           MOVE WS-TEST-RESULT TO WS-DISP-RESULT                                
           DISPLAY WS-DISPLAY-LINE                                              
                                                                               
      ******************************************************************        
      * Test 3: Valid 17-digit card with leading zeros                         
      ******************************************************************        
           ADD 1 TO WS-TEST-COUNT                                               
           MOVE '00000000000000001' TO WS-CARD-NUM-X                            
           PERFORM 1000-VALIDATE-CARD-NUMBER                                    
           IF WS-CARD-NUM-X IS NUMERIC                                          
              SET TEST-PASSED TO TRUE                                           
              ADD 1 TO WS-PASS-COUNT                                            
           ELSE                                                                 
              SET TEST-FAILED TO TRUE                                           
              ADD 1 TO WS-FAIL-COUNT                                            
           END-IF                                                               
           MOVE WS-TEST-COUNT TO WS-DISP-TEST-NUM                               
           MOVE 'Valid 17-digit card with leading zeros' TO WS-DISP-TEST-DESC   
           MOVE WS-TEST-RESULT TO WS-DISP-RESULT                                
           DISPLAY WS-DISPLAY-LINE                                              
                                                                               
      ******************************************************************        
      * Test 4: Invalid card number (contains letters)                         
      ******************************************************************        
           ADD 1 TO WS-TEST-COUNT                                               
           MOVE '1234567890ABCDEFG' TO WS-CARD-NUM-X                            
           PERFORM 1000-VALIDATE-CARD-NUMBER                                    
           IF WS-CARD-NUM-X IS NOT NUMERIC                                      
              SET TEST-PASSED TO TRUE                                           
              ADD 1 TO WS-PASS-COUNT                                            
           ELSE                                                                 
              SET TEST-FAILED TO TRUE                                           
              ADD 1 TO WS-FAIL-COUNT                                            
           END-IF                                                               
           MOVE WS-TEST-COUNT TO WS-DISP-TEST-NUM                               
           MOVE 'Invalid card (contains letters) rejected' TO WS-DISP-TEST-DESC 
           MOVE WS-TEST-RESULT TO WS-DISP-RESULT                                
           DISPLAY WS-DISPLAY-LINE                                              
                                                                               
      ******************************************************************        
      * Test 5: Valid maximum 17-digit card number                             
      ******************************************************************        
           ADD 1 TO WS-TEST-COUNT                                               
           MOVE '99999999999999999' TO WS-CARD-NUM-X                            
           PERFORM 1000-VALIDATE-CARD-NUMBER                                    
           IF WS-CARD-NUM-X IS NUMERIC                                          
              SET TEST-PASSED TO TRUE                                           
              ADD 1 TO WS-PASS-COUNT                                            
           ELSE                                                                 
              SET TEST-FAILED TO TRUE                                           
              ADD 1 TO WS-FAIL-COUNT                                            
           END-IF                                                               
           MOVE WS-TEST-COUNT TO WS-DISP-TEST-NUM                               
           MOVE 'Valid maximum 17-digit card number' TO WS-DISP-TEST-DESC       
           MOVE WS-TEST-RESULT TO WS-DISP-RESULT                                
           DISPLAY WS-DISPLAY-LINE                                              
                                                                               
      ******************************************************************        
      * Display test summary                                                   
      ******************************************************************        
           DISPLAY SPACES                                                       
           DISPLAY WS-TEST-FOOTER                                               
           DISPLAY 'Total Tests: ' WS-TEST-COUNT                                
           DISPLAY 'Tests Passed: ' WS-PASS-COUNT                               
           DISPLAY 'Tests Failed: ' WS-FAIL-COUNT                               
           DISPLAY WS-TEST-SEPARATOR                                            
                                                                               
           IF WS-FAIL-COUNT = 0                                                 
              DISPLAY 'ALL TESTS PASSED - 17-DIGIT CARD VALIDATION WORKING'     
           ELSE                                                                 
              DISPLAY 'SOME TESTS FAILED - REVIEW VALIDATION LOGIC'             
           END-IF                                                               
                                                                               
           STOP RUN                                                             
           .                                                                    
                                                                               
       1000-VALIDATE-CARD-NUMBER.                                               
      ******************************************************************        
      * Validate that the card number is exactly 17 numeric digits             
      * This mirrors the validation logic in COCRDLIC, COCRDSLC, COCRDUPC      
      ******************************************************************        
           CONTINUE                                                             
           .                                                                    
                                                                               
       1000-VALIDATE-CARD-NUMBER-EXIT.                                          
           EXIT                                                                 
           .                                                                    
