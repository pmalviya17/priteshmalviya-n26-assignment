package org.pmalviya.n26.transaction.restcontroller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pmalviya.n26.transaction.config.AppProperties;
import org.pmalviya.n26.transaction.exception.TransTimeLimitException;
import org.pmalviya.n26.transaction.model.Transaction;
import org.pmalviya.n26.transaction.service.TransactionService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TransactionsTest {
    @Mock
    private TransactionService transactionService;
    @Mock
    private AppProperties properties;

    @InjectMocks
    private TransactionsController transactionsController;

    @Test(expected = TransTimeLimitException.class)
    public final void oldTransactionLeadToException() {
        transactionsController.saveTransaction(oldTransaction());
    }

    @Test
    public final void transactionWithinAllowedTimeIsProcessed() {
        Transaction transaction = transactionWithinAllowedTime();
        transactionsController.saveTransaction(transaction);
        verify(properties, times(1)).getSecondsToUseForStats();
        verify(transactionService, times(1)).addTransaction(transaction);
    }

    private Transaction transactionWithinAllowedTime() {
        return Transaction.builder()
                .amount(1D)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    private Transaction oldTransaction() {
        return Transaction.builder()
                .amount(1D)
                .timestamp(1L)
                .build();
    }
}