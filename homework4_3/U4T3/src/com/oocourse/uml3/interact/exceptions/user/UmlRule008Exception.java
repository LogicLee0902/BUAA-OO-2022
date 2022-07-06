package com.oocourse.uml3.interact.exceptions.user;

/**
 * UML033 规则异常
 */
public class UmlRule008Exception extends PreCheckRuleException {
    /**
     * 构造函数
     */
    public UmlRule008Exception() {
        super("Failed when check R008, a final vertices has at least one outgoing transition.");
    }
}
