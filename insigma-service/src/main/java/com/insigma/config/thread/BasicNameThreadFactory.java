package com.insigma.config.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName BasicNameThreadFactory
 * @Description TODO
 * @Author carrots
 * @Date 2022/6/14 10:35
 * @Version 1.0
 **/
public class BasicNameThreadFactory {

    public void BasicNameThreadFactory() {

    }

    private static class DefaultThreadFactory implements ThreadFactory {

        private final ThreadGroup group;
        private final AtomicInteger thread_number = new AtomicInteger(1);
        private final String namePrefix;
        private final boolean deamon;

        private DefaultThreadFactory(String namePrefix, boolean deamon) {
            SecurityManager manager = System.getSecurityManager();
            this.group = manager != null ? manager.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.namePrefix = namePrefix;
            this.deamon = deamon;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(this.group, r, this.namePrefix + this.thread_number.incrementAndGet());
            thread.setDaemon(this.deamon);
            return thread;
        }
    }

    public static class Builder {
        private String namePattern;
        private boolean deamon;

        public Builder() {

        }

        public BasicNameThreadFactory.Builder namePattern(String namePattern) {
            this.namePattern = namePattern;
            return this;
        }

        public BasicNameThreadFactory.Builder deamon(boolean deamon) {
            this.deamon = deamon;
            return this;
        }

        public ThreadFactory builder() {
            return new BasicNameThreadFactory.DefaultThreadFactory(this.namePattern, this.deamon);
        }
    }
}
