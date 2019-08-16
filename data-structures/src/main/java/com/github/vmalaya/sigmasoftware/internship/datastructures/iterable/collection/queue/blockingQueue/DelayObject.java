package com.github.vmalaya.sigmasoftware.internship.datastructures.iterable.collection.queue.blockingQueue;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 *  Objects should be acted upon after a given delay.
 */
public class DelayObject implements Delayed {
    private Object data;
    private long startTime;

    public DelayObject(Object data, long delayInMilliseconds) {
        this.data = data;
        this.startTime = System.currentTimeMillis() + delayInMilliseconds;;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long diff = startTime - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if (this.startTime < ((DelayObject)o).startTime) {
            return -1;
        }
        if (this.startTime > ((DelayObject)o).startTime) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "DelayObject{" +
                "data='" + data + '\'' +
                ", startTime=" + startTime +
                '}';
    }
}
