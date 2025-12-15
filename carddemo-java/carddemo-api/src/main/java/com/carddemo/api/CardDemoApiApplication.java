package com.carddemo.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * CardDemo API Application - Main entry point for the REST API.
 * Migrated from IBM Enterprise COBOL 6.3 CICS online transactions.
 * 
 * This application replaces the following CICS transactions:
 * - CC00 (Sign On) -> POST /api/users/auth
 * - CM00 (Main Menu) -> GET /api/
 * - CAVW (Account View) -> GET /api/accounts/{id}
 * - CAUP (Account Update) -> PUT /api/accounts/{id}
 * - CCLI (Card List) -> GET /api/cards
 * - CTLI (Transaction List) -> GET /api/transactions
 * - And more...
 */
@SpringBootApplication(scanBasePackages = {"com.carddemo.api", "com.carddemo.core"})
@EntityScan(basePackages = "com.carddemo.core")
@EnableJpaRepositories(basePackages = "com.carddemo.core")
public class CardDemoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardDemoApiApplication.class, args);
    }
}
