package com.example.onboardingservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Transaction;
import org.jooq.TransactionContext;
import org.jooq.TransactionProvider;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import static org.springframework.transaction.TransactionDefinition.PROPAGATION_NESTED;

@Slf4j
@Component
@RequiredArgsConstructor
public class JooqTransactionProvider implements TransactionProvider {

    record SpringTransaction(TransactionStatus tx) implements Transaction {
    }

    private final DataSourceTransactionManager txMgr;

    @Override
    public void begin(TransactionContext ctx) {
        log.trace("Begin transaction");
        TransactionStatus tx = txMgr.getTransaction(new DefaultTransactionDefinition(PROPAGATION_NESTED));
        ctx.transaction(new SpringTransaction(tx));
    }

    @Override
    public void commit(TransactionContext ctx) {
        log.trace("commit transaction");
        txMgr.commit(((SpringTransaction) ctx.transaction()).tx);
    }

    @Override
    public void rollback(TransactionContext ctx) {
        log.trace("rollback transaction");
        txMgr.rollback(((SpringTransaction) ctx.transaction()).tx);
    }
}