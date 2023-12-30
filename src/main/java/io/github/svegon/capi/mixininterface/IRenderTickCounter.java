package io.github.svegon.capi.mixininterface;

public interface IRenderTickCounter {
    long getPrevTimeMillis();

    void setPrevTimeMillis(long prevTimeMillis);

    float getTickTime();

    void setTickTime(float tickTime);

    default float getTpS() {
        return 1000 / getTickTime();
    }

    default void setTpS(float tps) {
        setTickTime(1000 / tps);
    }
}
