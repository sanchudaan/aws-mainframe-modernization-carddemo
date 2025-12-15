package com.carddemo.batch.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Batch configuration - replaces JCL batch job definitions.
 * Configures batch infrastructure for processing jobs.
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig {
}
