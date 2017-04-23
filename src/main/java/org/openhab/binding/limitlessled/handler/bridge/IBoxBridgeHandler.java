/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.limitlessled.handler.bridge;

import com.jeefix.limitlessled.device.IBoxDevice;
import com.jeefix.limitlessled.device.LedStripDevice;
import com.jeefix.limitlessled.session.SessionService;
import com.jeefix.limitlessled.transport.SimpleTransportService;
import com.jeefix.limitlessled.transport.TransportService;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.*;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.limitlessled.LimitlessLedBindingConstants;
import org.openhab.binding.limitlessled.internal.StateForceable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Hashtable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author Maciej Iskra - Initial contribution
 */
public class IBoxBridgeHandler extends BaseBridgeHandler {

    private static Logger log = LoggerFactory.getLogger(IBoxBridgeHandler.class);

    private ThingDiscoveryService discoveryService;

    private SessionService sessionService;
    private TransportService transportService;

    private ScheduledFuture<?> forceStateTimerHandler;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();


    public IBoxBridgeHandler(Bridge bridge) {
        super(bridge);
        discoveryService = new ThingDiscoveryService();
        discoveryService.setBridgeUID(this.getThing().getUID());
        log.info("Created bridge instance");
    }


    @Override
    public void initialize() {
        log.debug("Attempting to initialize bridge {}", this.getThing().getUID());
        if (bundleContext == null) {
            log.error("Unable to initialize handler, missing OSGI context");
            return;
        }

        bundleContext.registerService(DiscoveryService.class.getName(), discoveryService, new Hashtable<>());
        String bridgeIP = String.valueOf(getConfig().get(LimitlessLedBindingConstants.CONFIG_BRIDGE_ADDRESS));
        int refreshPeriod = Integer.valueOf(String.valueOf(getConfig().get(LimitlessLedBindingConstants.CONFIG_STATE_SYNC_PERIOD)));
        transportService = new SimpleTransportService(bridgeIP);//new MockTransportService(bridgeIP);
        sessionService = new SessionService(transportService);

        updateStatus(ThingStatus.ONLINE);
        try {
            discoveryService.discoverThings();
            log.info("Initialized bridge {}", this.getThing().getUID());
        } catch (Exception e) {
            updateStatus(ThingStatus.UNINITIALIZED, ThingStatusDetail.HANDLER_INITIALIZING_ERROR, e.getMessage());
            log.error(String.format("Unable to initialize bridge %s", this.getThing().getUID()), e);
        }
        runStateSyncScheduler(refreshPeriod);

    }

    @Override
    public void dispose() {
        if (forceStateTimerHandler != null) {
            forceStateTimerHandler.cancel(true);
        }
        log.info("Disposing handler");
    }

    @Override
    public void childHandlerInitialized(ThingHandler childHandler, Thing childThing) {
        log.info("childHandlerInitialized");
    }

    @Override
    public void childHandlerDisposed(ThingHandler childHandler, Thing childThing) {
        log.info("childHandlerDisposed");
    }

    public LedStripDevice getLedStrip(byte zoneId) {
        LedStripDevice ledStripDevice = new LedStripDevice();
        ledStripDevice.setSessionService(sessionService);
        ledStripDevice.setTransportService(transportService);
        ledStripDevice.setZoneId(zoneId);
        return ledStripDevice;
    }

    public IBoxDevice getIBoxLed() {
        IBoxDevice iBoxDevice = new IBoxDevice();
        iBoxDevice.setSessionService(sessionService);
        iBoxDevice.setTransportService(transportService);

        return iBoxDevice;
    }

    @Override
    public void thingUpdated(Thing thing) {
        forceStateTimerHandler.cancel(true);
        //TODO check IP configuration etc.
    }


    protected void runStateSyncScheduler(int refreshPeriod) {
        if (forceStateTimerHandler != null) {
            forceStateTimerHandler.cancel(true);
        }
        forceStateTimerHandler = scheduler.scheduleWithFixedDelay(() -> {
            for (Thing thing : getThing().getThings()) {
                if (thing.getHandler() instanceof StateForceable) {
                    try {
                        ((StateForceable) thing.getHandler()).forceState();
                    } catch (Exception e) {
                        log.error(String.format("Unable to perform force state of thing: %s", thing), e);
                    }
                }
            }
        }, refreshPeriod, refreshPeriod, TimeUnit.MILLISECONDS);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        // this bridge doesn't support commands
    }
}
