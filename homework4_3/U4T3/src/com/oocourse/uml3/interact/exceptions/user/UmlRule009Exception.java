package com.oocourse.uml3.interact.exceptions.user;

/**
 */
public class UmlRule009Exception extends PreCheckRuleException {
    /**
     * 构造函数
     */
    public UmlRule009Exception() {
        super("Failed when check R009, a state has ambiguous outgoing transition.");
    }
}
