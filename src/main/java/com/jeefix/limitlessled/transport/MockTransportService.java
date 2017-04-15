package com.jeefix.limitlessled.transport;

import com.jeefix.iot.milight.common.HexUtils;
import com.jeefix.iot.milight.common.MilightException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.function.Consumer;

/**
 * TODO write class description here
 * <p>
 * Created by Maciej Iskra (emacisk) on 2017-04-12.
 */
public class MockTransportService implements TransportService {

    private static final Logger logger = LoggerFactory.getLogger(MockTransportService.class);


    /**
     * Creates new instance of service
     *
     * @param bridgeIp IP address of a bridge
     */
    public MockTransportService(String bridgeIp) {
        logger.info("Created MOCK transport service on bridge with IP: {}", bridgeIp);
    }

    @Override
    public void sendPackage(byte[] message, Consumer<byte[]> consumer) {
        logger.info("Send message '{}' to mock endpoint", message);
        int responseLenght = 32;
        byte[] response = new byte[responseLenght];


        for (int i = 0; i < responseLenght; i++) {
            response[i] = 0;
        }
        response[0] = (byte) (message[0] + 8);
        response[4] = (byte) (response.length - 5);
        consumer.accept(response);
    }
}
