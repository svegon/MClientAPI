package com.github.svegon.utils.multithreading;

import com.google.common.base.Preconditions;

import java.util.concurrent.atomic.AtomicInteger;

public class EventClock implements Runnable {
    private static final AtomicInteger THREAD_ID = new AtomicInteger();

    private final Object lock;
    private int speed; // in ticks per second
    private long tickNano; // approximate length of each tick in nanoseconds
    private Runnable event;
    private boolean running;
    private int realSpeed;
    private long lastTickStart;
    private long lastTickEnd;
    private long ticksBehind;

    public EventClock() {
        this.lock = this;
    }

    public EventClock(Object lock) {
        this.lock = Preconditions.checkNotNull(lock);
    }

    public Runnable setEvent(Runnable r) {
        synchronized (lock) {
            Runnable prev = event;
            this.event = Preconditions.checkNotNull(r);
            return prev;
        }
    }

    public void setSpeed(int speed) {
        synchronized (lock) {
            this.speed = speed;
            this.tickNano = 1000000000L / speed;
        }
    }

    public double getSpeed() {
        return speed;
    }

    public boolean isRunning() {
        return running;
    }

    public long getLastTickStart() {
        return lastTickStart;
    }

    public long getLastTickEnd() {
        return lastTickEnd;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        synchronized (lock) {
            Preconditions.checkArgument(speed > 0, "positive event clock speed required: %f", speed);
            Preconditions.checkNotNull(event, "no event set");

            running = true;

            new Thread("Event Clock Thread #" + THREAD_ID.getAndIncrement()) {
                @Override
                public void run() {
                    while (running) {
                        int l = realSpeed;
                        realSpeed = 0;
                        ticksBehind += speed - l;

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }.start();

            while (running) {
                while (ticksBehind-- > 0) {
                    lastTickStart = System.nanoTime();

                    event.run();

                    lastTickEnd = System.nanoTime();
                }

                realSpeed++;
                ticksBehind++;
                long sleepTime = tickNano - lastTickEnd + lastTickStart;

                try {
                    Thread.sleep(sleepTime / 1000000, (int) (sleepTime % 1000000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
