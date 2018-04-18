package org.pmalviya.n26.transaction.service;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.pmalviya.n26.transaction.config.AppProperties;
import org.pmalviya.n26.transaction.model.Transaction;
import org.pmalviya.n26.transaction.model.TransactionStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.DoubleSummaryStatistics;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private CacheManager cacheManager;

    @Cacheable("transactions")
    public void addTransaction(Transaction transaction) {
    }

    public TransactionStatistics getTransactionStats() {
        DoubleSummaryStatistics transactionStats = getTransactionsForTimeWindow(appProperties.getSecondsToUseForStats()).stream()
                .map(Transaction::getAmount)
                .collect(DoubleSummaryStatistics::new, DoubleSummaryStatistics::accept, DoubleSummaryStatistics::combine);

        return TransactionStatistics.builder()
                .sum(transactionStats.getSum())
                .avg(transactionStats.getAverage())
                .max(transactionStats.getMax())
                .min(transactionStats.getMin())
                .count(transactionStats.getCount())
                .build();

    }


    public List<Transaction> getTransactionsForTimeWindow(long timeSince) {
        Cache cache = cacheManager.getCache("transactions");
        List<Transaction> keys = cache.getKeys();
        long currentTime = System.currentTimeMillis();

        for (Transaction transaction : keys) {
            if (currentTime - transaction.getTimestamp() > timeSince * 1000) {
                cache.remove(transaction);
            }
        }

        return cache.getKeys();
    }

}
