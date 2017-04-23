package com.jeefix.limitlessled.session;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO write class description here
 * <p>
 * Created by Maciej Iskra (emacisk) on 2017-04-12.
 */
public class SessionState {

    protected byte sessionId1;
    protected byte sessionId2;
    private LocalDateTime lastRefresh;
    private AtomicInteger sequenceNumber = new AtomicInteger();


    public SessionState(byte sessionId1, byte sessionId2) {
        this.sessionId1 = sessionId1;
        this.sessionId2 = sessionId2;
        lastRefresh = LocalDateTime.now();
    }

    public byte getSessionId1() {
        return sessionId1;
    }

    public byte getSessionId2() {
        return sessionId2;
    }

    public LocalDateTime getLastRefresh() {
        return lastRefresh;
    }

    public byte getSequenceNumber() {
        if (sequenceNumber.get() >= 255) {
            sequenceNumber.set(0);
        }
        return (byte) sequenceNumber.getAndIncrement();
    }

    @Override
    public String toString() {
        return "SessionState{" +
                "sessionId1=" + sessionId1 +
                ", sessionId2=" + sessionId2 +
                ", lastRefresh=" + lastRefresh +
                '}';
    }
}
