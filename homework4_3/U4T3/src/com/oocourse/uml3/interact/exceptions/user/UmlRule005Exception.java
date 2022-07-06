package com.oocourse.uml3.interact.exceptions.user;

/**
 * UML011 规则异常
 */
public class UmlRule005Exception extends PreCheckRuleException {
    /**
     * 构造函数
     */
    public UmlRule005Exception() {
        super("Failed when check R005, All attributes and operations of interface must be public.");
    }
}
