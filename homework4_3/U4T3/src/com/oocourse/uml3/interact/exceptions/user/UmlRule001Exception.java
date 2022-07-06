package com.oocourse.uml3.interact.exceptions.user;

/**
 * UML001 规则异常
 */
public class UmlRule001Exception extends PreCheckRuleException {
    /**
     * 构造函数
     */
    public UmlRule001Exception() {
        super("Failed when check R001, a certain element doesn't have name.");
    }
}
