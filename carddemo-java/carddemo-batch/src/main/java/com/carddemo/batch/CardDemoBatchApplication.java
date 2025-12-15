package com.carddemo.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * CardDemo Batch Application - Main entry point for batch processing.
 * Migrated from IBM Enterprise COBOL 6.3 batch programs.
 * 
 * This application replaces the following JCL/COBOL batch jobs:
 * - CBACT01C.cbl (Account Processing) -> accountReportJob
 * - CBTRN02C.cbl (Transaction Post) -> transactionPostJob
 * - CBTRN03C.cbl (Interest Calculation) -> interestCalculationJob
 * - CBCUS01C.cbl (Customer Processing) -> customerReportJob
 */
@SpringBootApplication(scanBasePackages = {"com.carddemo.batch", "com.carddemo.core"})
@EntityScan(basePackages = "com.carddemo.core")
@EnableJpaRepositories(basePackages = "com.carddemo.core")
public class CardDemoBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardDemoBatchApplication.class, args);
    }
}
