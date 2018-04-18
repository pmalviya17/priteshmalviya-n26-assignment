package org.pmalviya.n26.transaction.restcontroller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pmalviya.n26.transaction.model.TransactionStatistics;
import org.pmalviya.n26.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
@Slf4j
@AllArgsConstructor
public class StatisticsController {
    @Autowired
    private TransactionService transactionService;

    @RequestMapping(method = RequestMethod.GET)
    public TransactionStatistics getStatsForTransactions() {
        return transactionService.getTransactionStats();
    }


}
