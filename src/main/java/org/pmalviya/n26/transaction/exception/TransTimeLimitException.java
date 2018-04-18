package org.pmalviya.n26.transaction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "This Transaction is failed as it's older than 60 sec")
public class TransTimeLimitException extends RuntimeException {
}
