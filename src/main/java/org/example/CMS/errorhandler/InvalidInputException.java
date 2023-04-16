package org.example.CMS.errorhandler;

public class InvalidInputException extends CMSException{

    public InvalidInputException(ErrorCode errorCode) {
        super(errorCode);
    }
}
