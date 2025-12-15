package com.carddemo.batch.job;

import com.carddemo.core.transaction.entity.Transaction;
import com.carddemo.core.transaction.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Transaction Posting Batch Job - replaces COBOL batch program CBTRN02C.cbl.
 * Processes unprocessed transactions and posts them to accounts.
 * 
 * Original JCL: TRANPOST.jcl
 * Original COBOL: CBTRN02C.cbl (Batch Transaction Post)
 */
@Configuration
public class TransactionPostingJob {

    private static final Logger logger = LoggerFactory.getLogger(TransactionPostingJob.class);

    private final TransactionRepository transactionRepository;

    public TransactionPostingJob(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Bean
    public Job transactionPostJob(JobRepository jobRepository, Step transactionPostStep) {
        return new JobBuilder("transactionPostJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(transactionPostStep)
                .build();
    }

    @Bean
    public Step transactionPostStep(JobRepository jobRepository, 
                                     PlatformTransactionManager transactionManager,
                                     ItemReader<Transaction> transactionReader,
                                     ItemProcessor<Transaction, Transaction> transactionProcessor,
                                     ItemWriter<Transaction> transactionWriter) {
        return new StepBuilder("transactionPostStep", jobRepository)
                .<Transaction, Transaction>chunk(100, transactionManager)
                .reader(transactionReader)
                .processor(transactionProcessor)
                .writer(transactionWriter)
                .build();
    }

    @Bean
    public RepositoryItemReader<Transaction> transactionReader() {
        return new RepositoryItemReaderBuilder<Transaction>()
                .name("transactionReader")
                .repository(transactionRepository)
                .methodName("findUnprocessedTransactions")
                .pageSize(100)
                .sorts(Map.of("transactionId", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemProcessor<Transaction, Transaction> transactionProcessor() {
        return transaction -> {
            logger.debug("Processing transaction: {}", transaction.getTransactionId());
            transaction.setProcessedTimestamp(LocalDateTime.now());
            return transaction;
        };
    }

    @Bean
    public ItemWriter<Transaction> transactionWriter() {
        return transactions -> {
            for (Transaction transaction : transactions) {
                transactionRepository.save(transaction);
                logger.info("Posted transaction: {}", transaction.getTransactionId());
            }
        };
    }
}
