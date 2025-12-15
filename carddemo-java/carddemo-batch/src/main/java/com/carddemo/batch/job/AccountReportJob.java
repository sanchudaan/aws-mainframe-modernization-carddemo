package com.carddemo.batch.job;

import com.carddemo.core.account.entity.Account;
import com.carddemo.core.account.repository.AccountRepository;
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

import java.util.Map;

/**
 * Account Report Batch Job - replaces COBOL batch program CBACT01C.cbl.
 * Reads account data and generates reports.
 * 
 * Original JCL: ACTRPT.jcl
 * Original COBOL: CBACT01C.cbl (Batch Account Processing)
 */
@Configuration
public class AccountReportJob {

    private static final Logger logger = LoggerFactory.getLogger(AccountReportJob.class);

    private final AccountRepository accountRepository;

    public AccountReportJob(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Bean
    public Job accountReportBatchJob(JobRepository jobRepository, Step accountReportStep) {
        return new JobBuilder("accountReportJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(accountReportStep)
                .build();
    }

    @Bean
    public Step accountReportStep(JobRepository jobRepository,
                                   PlatformTransactionManager transactionManager,
                                   ItemReader<Account> accountReader,
                                   ItemProcessor<Account, AccountReportRecord> accountReportProcessor,
                                   ItemWriter<AccountReportRecord> accountReportWriter) {
        return new StepBuilder("accountReportStep", jobRepository)
                .<Account, AccountReportRecord>chunk(100, transactionManager)
                .reader(accountReader)
                .processor(accountReportProcessor)
                .writer(accountReportWriter)
                .build();
    }

    @Bean
    public RepositoryItemReader<Account> accountReader() {
        return new RepositoryItemReaderBuilder<Account>()
                .name("accountReader")
                .repository(accountRepository)
                .methodName("findAll")
                .pageSize(100)
                .sorts(Map.of("accountId", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemProcessor<Account, AccountReportRecord> accountReportProcessor() {
        return account -> {
            logger.debug("Processing account for report: {}", account.getAccountId());
            return new AccountReportRecord(
                    account.getAccountId(),
                    account.getStatus().name(),
                    account.getCurrentBalance(),
                    account.getCreditLimit(),
                    account.getAvailableCredit(),
                    account.getCurrentCycleCredit(),
                    account.getCurrentCycleDebit()
            );
        };
    }

    @Bean
    public ItemWriter<AccountReportRecord> accountReportWriter() {
        return records -> {
            for (AccountReportRecord record : records) {
                logger.info("Account Report: ID={}, Status={}, Balance={}, Available={}",
                        record.accountId(), record.status(), record.currentBalance(), record.availableCredit());
            }
        };
    }

    public record AccountReportRecord(
            Long accountId,
            String status,
            java.math.BigDecimal currentBalance,
            java.math.BigDecimal creditLimit,
            java.math.BigDecimal availableCredit,
            java.math.BigDecimal currentCycleCredit,
            java.math.BigDecimal currentCycleDebit
    ) {}
}
