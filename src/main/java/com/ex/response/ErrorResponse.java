package com.ex.response;

public final class ErrorResponse extends ApiResponse {
    private final int code;
    private final String message;

    public ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    // getter
    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
