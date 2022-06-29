package com.insigma.exception;

public enum FailureCode {
    CODE("-1", "system error", "xitongcuowu"),
    ;
    private String code;
    private String msg;
    private String chnMsg;

    FailureCode(String code, String msg, String chnMsg) {
        this.msg = msg;
        this.code = code;
        this.chnMsg = chnMsg;
    }

    public String getMsg() {
        return msg;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return this.code + "_" + this.msg + "_" + this.chnMsg;
    }
}
