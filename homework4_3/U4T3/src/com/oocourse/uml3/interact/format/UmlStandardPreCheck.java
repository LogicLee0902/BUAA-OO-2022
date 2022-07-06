package com.oocourse.uml3.interact.format;

import com.oocourse.uml3.interact.exceptions.user.PreCheckRuleException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule001Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule003Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule004Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule007Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule009Exception;

/**
 * UML基本标准预检查
 */
public interface UmlStandardPreCheck {
    /**
     * 检查全部规则
     *
     * @throws PreCheckRuleException 预检查异常
     */
    default void checkForAllRules() throws PreCheckRuleException {
        checkForUml001();
        checkForUml002();
        checkForUml003();
        checkForUml004();
        checkForUml005();
        checkForUml006();
        checkForUml007();
        checkForUml008();
        checkForUml009();
    }

    /**
     * 检查 R001 规则
     *
     * @throws UmlRule001Exception 规则异常
     */
    void checkForUml001() throws UmlRule001Exception;

    /**
     * 检查 R002 规则
     *
     * @throws UmlRule002Exception 规则异常
     */
    void checkForUml002() throws UmlRule002Exception;

    /**
     * 检查 R003 规则
     *
     * @throws UmlRule003Exception 规则异常
     */
    void checkForUml003() throws UmlRule003Exception;

    /**
     * 检查 R004 规则
     *
     * @throws UmlRule004Exception 规则异常
     */
    void checkForUml004() throws UmlRule004Exception;

    /**
     * 检查 R005 规则
     *
     * @throws UmlRule005Exception 规则异常
     */
    void checkForUml005() throws UmlRule005Exception;

    /**
     * 检查 R006 规则
     *
     * @throws UmlRule006Exception 规则异常
     */
    void checkForUml006() throws UmlRule006Exception;

    /**
     * 检查 R007 规则
     *
     * @throws UmlRule007Exception 规则异常
     */
    void checkForUml007() throws UmlRule007Exception;

    /**
     * 检查 R008 规则
     *
     * @throws UmlRule008Exception 规则异常
     */
    void checkForUml008() throws UmlRule008Exception;

    /**
     * 检查 R009 规则
     *
     * @throws UmlRule009Exception 规则异常
     */
    void checkForUml009() throws UmlRule009Exception;
}
