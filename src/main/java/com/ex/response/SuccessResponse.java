package com.ex.response;

public final class SuccessResponse extends ApiResponse {
    private final int code = 200;
    private final Object data;

    public SuccessResponse(Object data) {
        this.data = data;
    }

    // getter（仅data需要，code固定）
    public int getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }
}
