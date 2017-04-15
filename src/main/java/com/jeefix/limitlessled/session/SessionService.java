package com.jeefix.limitlessled.session;

import com.jeefix.iot.milight.common.HexUtils;
import com.jeefix.iot.milight.common.MilightCommand;
import com.jeefix.limitlessled.transport.TransportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TODO write class description here
 * <p>
 * Created by Maciej Iskra (emacisk) on 2017-04-12.
 */
public class SessionService {

    private static final Logger log = LoggerFactory.getLogger(SessionService.class);
    public static final int SESSION_REFRESH_TIME = 5000;
    private Lock lock = new ReentrantLock();

    private SessionState sessionState;

    private TransportService transportService;

    public SessionService(TransportService transportService) {
        this.transportService = transportService;
        createSession();
    }

    protected void createSession() {
        byte[] createSessionRequest = HexUtils.getStringAsHex(MilightCommand.CREATE_SESSION.getHexCommand());
        transportService.sendPackage(createSessionRequest, (response) -> {
            sessionState = new SessionState(response[19], response[20]);
            log.info("Successfully Created new session: {}", sessionState);
        });
    }


    public SessionState getSessionState() {
        createSession();
        return sessionState;

    }
}
