package org.pmalviya.n26.transaction.model;

import lombok.*;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    Double amount;
    Long timestamp;
}
