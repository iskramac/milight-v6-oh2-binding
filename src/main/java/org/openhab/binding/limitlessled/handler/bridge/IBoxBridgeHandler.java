/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.limitlessled.handler.bridge;

import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.*;
import org.eclipse.smarthome.core.thing.binding.BaseBridgeHandler;
import org.eclipse.smarthome.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * TODO
 *
 * @author Maciej Iskra - Initial contribution
 */
public class IBoxBridgeHandler extends BaseBridgeHandler {

    private static Logger log = LoggerFactory.getLogger(IBoxBridgeHandler.class);

    private ThingDiscoveryService discoveryService;

    public IBoxBridgeHandler(Bridge bridge) {
        super(bridge);
        discoveryService = new ThingDiscoveryService();
        discoveryService.setBridgeUID(this.getThing().getUID());
        log.info("Created bridge instance");
    }


    @Override
    public void initialize() {
        log.debug("Attemping to initialize bridge {}", this.getThing().getUID());
        bundleContext.registerService(DiscoveryService.class.getName(), discoveryService, new Hashtable<>());
        updateStatus(ThingStatus.ONLINE);
        try {
            discoveryService.discoverThings();
            log.info("Initialized bridge {}", this.getThing().getUID());
        } catch (Exception e) {
            updateStatus(ThingStatus.UNINITIALIZED, ThingStatusDetail.HANDLER_INITIALIZING_ERROR, e.getMessage());
            log.error(String.format("Unable to initialize bridge %s", this.getThing().getUID()), e);
        }

    }

    @Override
    public void thingUpdated(Thing thing) {
        //TODO check IP configuration etc.
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        // this bridge doesn't support commands
    }
}
