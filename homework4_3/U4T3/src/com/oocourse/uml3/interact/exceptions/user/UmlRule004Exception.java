package com.oocourse.uml3.interact.exceptions.user;

import com.oocourse.uml3.models.elements.UmlClassOrInterface;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * UML007 规则异常
 */
public class UmlRule004Exception extends PreCheckRuleException {
    private final Set<? extends UmlClassOrInterface> classes;

    /**
     * 构造函数
     *
     * @param classes 异常类或接口集合
     */
    public UmlRule004Exception(Set<? extends UmlClassOrInterface> classes) {
        super(String.format(
                "Failed when check R004, class/interface (%s) have duplicate generalizations.",
                classes.stream()
                        .sorted(Comparator.comparing(UmlClassOrInterface::getName))
                        .map(UmlClassOrInterface::getName)
                        .collect(Collectors.joining(", "))
        ));
        this.classes = classes;
    }

    /**
     * 获取异常类或接口集合
     *
     * @return 异常类或接口集合
     */
    public Set<? extends UmlClassOrInterface> getClasses() {
        return classes;
    }
}
