package org.pmalviya.n26.transaction.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class TransactionStatistics {
    private Double sum;
    private Double avg;
    private Double max;
    private Double min;
    private Long count;
}
