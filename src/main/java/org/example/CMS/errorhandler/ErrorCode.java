package org.example.CMS.errorhandler;

public enum ErrorCode {

    CMS100("100", "Email or phone number already exists"),
    CMS101("101", "Username does not exists"),
    CMS102("102", "Constraint Violation Exception");

    private final String code;
    private final String message;

    ErrorCode(String code,
              String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
