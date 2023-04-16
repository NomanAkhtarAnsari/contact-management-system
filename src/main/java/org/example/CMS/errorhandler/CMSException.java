package org.example.CMS.errorhandler;

import lombok.Data;

@Data
public abstract class CMSException extends RuntimeException{

    ErrorCode errorCode;

    public CMSException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
