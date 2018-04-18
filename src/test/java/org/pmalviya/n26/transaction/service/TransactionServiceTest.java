package org.pmalviya.n26.transaction.service;

import org.junit.Before;
import org.junit.Test;
import org.pmalviya.n26.transaction.config.AppProperties;
import org.pmalviya.n26.transaction.model.Transaction;
import org.pmalviya.n26.transaction.model.TransactionStatistics;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TransactionServiceTest {
    private TransactionService transactionService;

    @Before
    public void setUp() {
        transactionService = new TransactionService();
    }

    @Test
    public final void noTransactionsResultInZeroValuedStats() {
        TransactionStatistics transactionStatistics = transactionService.getTransactionStats();
        assertThat(transactionStatistics.getSum(), is(0.0));
        assertThat(transactionStatistics.getAvg(), is(0.0));
        assertThat(transactionStatistics.getMax(), is(Double.NEGATIVE_INFINITY));
        assertThat(transactionStatistics.getMin(), is(Double.POSITIVE_INFINITY));
        assertThat(transactionStatistics.getCount(), is(0L));
    }

    @Test
    public final void transactionsLessThanConfiguredTimeAreIgnored() {
        addTransactionBefore60Sec();
        addTransactionBefore60Sec();
        addTransactionBefore60Sec();
        TransactionStatistics transactionStatistics = transactionService.getTransactionStats();
        assertThat(transactionStatistics.getSum(), is(0.0));
        assertThat(transactionStatistics.getAvg(), is(0.0));
        assertThat(transactionStatistics.getMax(), is(Double.NEGATIVE_INFINITY));
        assertThat(transactionStatistics.getMin(), is(Double.POSITIVE_INFINITY));
        assertThat(transactionStatistics.getCount(), is(0L));
    }

    @Test
    public final void transactionsWithinConfiguredTimeAreIncludedInStatsCalculation() {
        addTransactionWithin60Sec();
        addTransactionWithin60Sec();
        addTransactionWithin60Sec();
        TransactionStatistics transactionStatistics = transactionService.getTransactionStats();
        assertThat(transactionStatistics.getSum(), is(3.0));
        assertThat(transactionStatistics.getAvg(), is(1.0));
        assertThat(transactionStatistics.getMax(), is(1.0));
        assertThat(transactionStatistics.getMin(), is(1.0));
        assertThat(transactionStatistics.getCount(), is(3L));
    }

    @Test
    public final void mixedTransactionsWithinAndOutOfLimitAreHandled() {
        addTransactionWithin60Sec();
        addTransactionWithin60Sec();
        addTransactionBefore60Sec();
        addTransactionBefore60Sec();
        TransactionStatistics transactionStatistics = transactionService.getTransactionStats();
        assertThat(transactionStatistics.getSum(), is(2.0));
        assertThat(transactionStatistics.getAvg(), is(1.0));
        assertThat(transactionStatistics.getMax(), is(1.0));
        assertThat(transactionStatistics.getMin(), is(1.0));
        assertThat(transactionStatistics.getCount(), is(2L));
    }

    private void addTransactionWithin60Sec() {
        transactionService.addTransaction(Transaction.builder()
                .amount(1D)
                .timestamp(System.currentTimeMillis())
                .build());
    }

    private void addTransactionBefore60Sec() {
        transactionService.addTransaction(Transaction.builder()
                .amount(1D)
                .timestamp(System.currentTimeMillis() - 61000)
                .build());
    }
}