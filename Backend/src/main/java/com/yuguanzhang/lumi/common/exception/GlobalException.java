package com.yuguanzhang.lumi.common.exception;

import com.yuguanzhang.lumi.common.exception.message.ExceptionMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@Getter
public class GlobalException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String type;
    private final HttpStatus status;

    public GlobalException(ExceptionMessage message) {
        super(message.getMessage());
        this.type = message.name();
        this.status = message.getStatus();
    }
}
