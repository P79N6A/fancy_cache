package pers.fancy.tools.executor;

import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;


public interface ExecutorLoggerInner {

    String DEFAULT_GROUP = "default";

    Logger executorLogger = LoggerFactory.getLogger("com.github.tools.executor");

    Logger monitorLogger = LoggerFactory.getLogger("ExecutorMonitor");

    default String generatePolicyLoggerContent(String group, Object policy, BlockingQueue<?> queue,
                                               String taskInfo, int taskHash) {
        return MessageFormatter.arrayFormat(
            "executor group:[{}] triggered policy:[{}], RunQ remain:[{}], task: '{}', obj: {}",
            new Object[] {group, policy.getClass().getSimpleName(), queue.remainingCapacity(), taskInfo, taskHash})
            .getMessage();
    }
}
