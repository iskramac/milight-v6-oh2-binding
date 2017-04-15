package com.jeefix.limitlessled.transport;

import com.jeefix.iot.milight.common.HexUtils;
import com.jeefix.iot.milight.common.MilightException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * TODO write class description here
 * <p>
 * Created by Maciej Iskra (emacisk) on 2017-04-12.
 */
public class SimpleTransportService implements TransportService {

    private static final Logger logger = LoggerFactory.getLogger(SimpleTransportService.class);

    public static final int SOCKET_TIMEOUT = 5000;
    public static final int COMMUNICATION_PORT = 5987;
    public static final int INTER_INVOCATION_DELAY_IN_MILIS = 100;

    private DatagramSocket clientSocket;
    private LocalDateTime lastPackageDate;
    private Lock lock = new ReentrantLock();

    private InetAddress ipAddress;

    /**
     * Creates new instance of service
     *
     * @param bridgeIp IP address of a bridge
     */
    public SimpleTransportService(String bridgeIp) {
        try {
            this.ipAddress = InetAddress.getByName(bridgeIp);
            this.clientSocket = new DatagramSocket();
            this.clientSocket.setBroadcast(true);
            this.lastPackageDate = LocalDateTime.now();
            logger.info("Created connection for bridge IP: {}", ipAddress.getHostAddress());
        } catch (UnknownHostException e) {
            throw new MilightException(String.format("Bridge IP address is malformed: ", e));
        } catch (SocketException e) {
            throw new MilightException(String.format("Unable to create UDP socket connection"));
        }
    }

    @Override
    public void sendPackage(byte[] message, Consumer<byte[]> consumer) {
        lock.lock();
        try {
            String requestString = HexUtils.getHexAsString(message);
            DatagramPacket sendPacket = new DatagramPacket(message, message.length, ipAddress, COMMUNICATION_PORT);
            logger.debug("Attempting to send request {} to address {}:{}", new Object[]{requestString, ipAddress, COMMUNICATION_PORT});
            invokeInLimitedThreshold(() -> {
                        try {
                            clientSocket.send(sendPacket);
                        } catch (Exception e) {
                            throw new MilightException(String.format("Unable to send request %s", requestString), e);
                        }
                    }
            );
            logger.info("Sent request {} to address {}:{}", new Object[]{requestString, ipAddress, COMMUNICATION_PORT});

            if (consumer != null) {
                try {
                    logger.debug("Waiting for response for request {}", requestString);
                    byte[] receiveData = new byte[256];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    clientSocket.setSoTimeout(SOCKET_TIMEOUT);
                    clientSocket.receive(receivePacket);
                    byte[] response = new byte[receivePacket.getLength()];
                    for (int i = 0; i < response.length; i++) {
                        response[i] = receivePacket.getData()[i];
                    }
                    String responseString = HexUtils.getHexAsString(response);
                    logger.info("Received response from {}:{} : {}", new Object[]{receivePacket.getAddress(), receivePacket.getPort(), responseString});
                    consumer.accept(response);
                } catch (Exception e) {
                    throw new MilightException(String.format("Unable to get response for command %s", requestString), e);
                }
            }
        } finally {
            lock.unlock();
        }

    }

    protected void invokeInLimitedThreshold(Runnable operation) {
        LocalDateTime currentTime = LocalDateTime.now();
        long milisFromLastInvocation = Duration.between(lastPackageDate, currentTime).toMillis();
        if (milisFromLastInvocation < INTER_INVOCATION_DELAY_IN_MILIS) {
            try {
                long milisToWait = INTER_INVOCATION_DELAY_IN_MILIS - milisFromLastInvocation;
                logger.info("Reached maximum threshold for controller, waiting {} miliseconds", milisToWait);
                Thread.sleep(milisToWait);
            } catch (InterruptedException e) {
                throw new MilightException("Thread interrupted", e);
            }
        }
        operation.run();
        lastPackageDate = currentTime;
    }
}
