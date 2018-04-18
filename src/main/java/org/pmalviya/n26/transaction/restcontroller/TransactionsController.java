package org.pmalviya.n26.transaction.restcontroller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pmalviya.n26.transaction.config.AppProperties;
import org.pmalviya.n26.transaction.service.TransactionService;
import org.pmalviya.n26.transaction.exception.TransTimeLimitException;
import org.pmalviya.n26.transaction.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@Slf4j
@AllArgsConstructor
public class TransactionsController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AppProperties properties;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveTransaction(@RequestBody Transaction transaction) {
        log.info("Timestamp of received transaction {} minimum allowed time {} ", transaction.getTimestamp(), getMinAllowedTime());

        if (transaction.getTimestamp() < getMinAllowedTime()) {
            log.warn("{} is older than {}sec the record will not be processed",
                    transaction, properties.getSecondsToUseForStats());
            throw new TransTimeLimitException();
        } else {
            transactionService.addTransaction(transaction);
        }
    }

    private Long getMinAllowedTime() {
        return System.currentTimeMillis() - properties.getSecondsToUseForStats() * 1000;
    }
}
