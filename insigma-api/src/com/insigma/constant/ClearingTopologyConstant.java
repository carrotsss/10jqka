package com.insigma.constant;

import kotlin.jvm.internal.MagicApiIntrinsics;
import org.apache.ibatis.javassist.expr.NewArray;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName ClearingTopologyConstant
 * @Description
 * @Author carrots
 * @Date 2022/6/28 15:40
 * @Version 1.0
 */
public class ClearingTopologyConstant {
    public static final Map<ClearingProcess, List<ClearingStep>> PROCESS_STEPS = new ConcurrentHashMap<Object, Object>() {{
        put(ClearingProcess.PROC_CLEAR_CALC, new CopyOnWriteArrayList<>() {{
            add(ClearingStep.CLEARING_START);
            add(ClearingStep.LOAD_CA);
            add(ClearingStep.PRICE_SYNC);
        }});
        put(clearingProcess.PROC_SOD_MAKE, new CopyOnWriteArrayList<>() {{
            add(ClearingStep.CLEARING_START);
        }});
    }};

    public static final Set<ClearingStep> CONSUMABLE_STEP_SET = new HashSet<>(){{
        add(ClearingStep.CLEARING_START);
        add(ClearingStep.CLEARING_END);
    }};

    public static final Map<ClearingStep, TopicProcessType> STEP_TOPIC_PROCESS_TYPE = new ConcurrentHashMap<>(){{
        put(ClearingStep.CLEARING_START, topicProcessType.MAIN);
//        put()
    }};

    public static TopicProcessType getTopicProcessTypeByStep(ClearingStep clearingStep) {
        TopicProcessType topicProcessType = STEP_TOPIC_PROCESS_TYPE.get(clearingStep);
        if (topicProcessType == null) {
            throw new RuntimeException();
        }
        return topicProcessType;
    }


}
