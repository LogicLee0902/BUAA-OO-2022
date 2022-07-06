package com.oocourse.uml3.interact.exceptions.user;

/**
 */
public class UmlRule007Exception extends PreCheckRuleException {
    /**
     * 构造函数
     */
    public UmlRule007Exception() {
        super("Failed when check R007, a lifeline receives messages after receiving a delete message.");
    }
}
