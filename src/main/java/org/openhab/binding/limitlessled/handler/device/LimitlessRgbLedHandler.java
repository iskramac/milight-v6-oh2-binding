/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.limitlessled.handler.device;

import com.jeefix.limitlessled.device.LedStripDevice;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.HSBType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.PercentType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.ThingStatusDetail;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.eclipse.smarthome.core.types.State;
import org.openhab.binding.limitlessled.handler.bridge.IBoxBridgeHandler;
import org.openhab.binding.limitlessled.internal.StateForceable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import static org.openhab.binding.limitlessled.LimitlessLedBindingConstants.*;

/**
 * The {@link LimitlessRgbLedHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Maciej Iskra - Initial contribution
 */
public class LimitlessRgbLedHandler extends BaseThingHandler implements StateForceable {

    private Logger logger = LoggerFactory.getLogger(LimitlessRgbLedHandler.class);

    private LedStripDevice device;

    public LimitlessRgbLedHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void initialize() {
        byte zoneId = Byte.valueOf(String.valueOf(editProperties().get(CONFIG_LED_STRIP_ZONE)));
        device = ((IBoxBridgeHandler) this.getBridge().getHandler()).getLedStrip(zoneId);

        forceState();

    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {

        logger.debug("Attempting to handle command {} on channel {}", command, channelUID);
        if (channelUID != null) {
            switch (channelUID.getId()) {
                case CHANNEL_RGB_LED_ONOFF: {
                    handleDeviceOnOff(command);
                    break;
                }
                case CHANNEL_RGB_LED_COLOR: {
                    handleDeviceColor(command);
                    break;
                }
                case CHANNEL_RGB_LED_WHITE_ON: {
                    handleDeviceWhiteOn(command);
                    break;
                }
                default: {
                    logger.warn("Received message on unsupported channel {}", channelUID.getId());
                }
            }
        }
        //refresh all channels if channel is null and command REFRESH or channel set and command is other than REFRESH
        if ((channelUID != null && command != RefreshType.REFRESH) || (channelUID == null && command == RefreshType.REFRESH)) {
            getDeviceChannelStateMap().forEach((channel, state) -> {
                updateState(channel, state);
            });
        }
        logger.info("Handled command {} on channel {}", command, channelUID);


    }

    protected void handleDeviceOnOff(Command command) {
        if (command == OnOffType.ON) {
            device.on();
        } else if ((command == OnOffType.OFF)) {
            device.off();
        } else if (command == RefreshType.REFRESH) {
            updateState(getThing().getChannel(CHANNEL_RGB_LED_ONOFF).getUID(), getDeviceChannelStateMap().get(CHANNEL_RGB_LED_ONOFF));
        } else {
            logger.warn("Received unsupported command on channel {}", command, CHANNEL_RGB_LED_ONOFF);
        }
    }

    protected void handleDeviceWhiteOn(Command command) {
        if (command == OnOffType.ON) {
            device.whiteOn();
        } else if ((command == OnOffType.OFF)) {
            device.whiteOff();
        } else if (command == RefreshType.REFRESH) {
            updateState(getThing().getChannel(CHANNEL_RGB_LED_WHITE_ON).getUID(), getDeviceChannelStateMap().get(CHANNEL_RGB_LED_WHITE_ON));
        } else {
            logger.warn("Received unsupported command on channel {}", command, CHANNEL_RGB_LED_ONOFF);
        }
    }

    protected void handleDeviceColor(Command command) {
        if (command instanceof HSBType) {

            HSBType commandHsb = (HSBType) command;
            if (device.getState().getHue() != commandHsb.getHue().intValue()) {
                device.hue(commandHsb.getHue().intValue());
                updateState(getThing().getChannel(CHANNEL_RGB_LED_WHITE_ON).getUID(), getDeviceChannelStateMap().get(CHANNEL_RGB_LED_WHITE_ON));
            }
            if (device.getState().getBrightness() != commandHsb.getBrightness().intValue()) {
                device.brightness(commandHsb.getBrightness().intValue());
            }
            if (device.getState().getSaturation() != commandHsb.getSaturation().intValue()) {
                device.saturation(commandHsb.getSaturation().intValue());
            }
        } else if (command == RefreshType.REFRESH) {
            updateState(getThing().getChannel(CHANNEL_RGB_LED_COLOR).getUID(), getDeviceChannelStateMap().get(CHANNEL_RGB_LED_COLOR));
        } else {
            logger.warn("Received unsupported command on channel {}", command, CHANNEL_RGB_LED_ONOFF);
        }
    }


    protected HashMap<String, State> getDeviceChannelStateMap() {
        HashMap<String, State> result = new HashMap<>();

        DecimalType hue = new DecimalType(device.getState().getHue());
        PercentType saturation = new PercentType(device.getState().getSaturation());
        PercentType brightness = new PercentType(device.getState().getBrightness());

        result.put(CHANNEL_RGB_LED_COLOR, new HSBType(hue, saturation, brightness));
        result.put(CHANNEL_RGB_LED_ONOFF, device.getState().isOn() ? OnOffType.ON : OnOffType.OFF);
        result.put(CHANNEL_RGB_LED_WHITE_ON, device.getState().isWhite() ? OnOffType.ON : OnOffType.OFF);
        return result;
    }

    @Override
    public void forceState() {
        try {
            logger.debug("Attempting to force state of thing: {}", this.getThing().getUID());
            device.synchronizeState();
            handleCommand(null, RefreshType.REFRESH);
            logger.info("Synchronized state of thing {} to: {}", this.getThing().getUID(), device.getState());
            updateStatus(ThingStatus.ONLINE);
        } catch (Exception e) {
            logger.error("Unable to force state", e);
            updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,e.getMessage());
        }
    }
}
