/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.limitlessled.handler;

import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link LimitlessRgbLedHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Maciej Iskra - Initial contribution
 */
public class LimitlessRgbLedHandler extends BaseThingHandler {

    private Logger logger = LoggerFactory.getLogger(LimitlessRgbLedHandler.class);


    public LimitlessRgbLedHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {

        logger.debug("Attempting to handle command {} on channel {}", command, channelUID);

//        refreshCommandService();
//
//        if (command instanceof RefreshType) {
//            logger.warn("NOT IMPLEMENTED");
//            return;
//        }
//
//        if (channelUID.getId().equals(CHANNEL_RGB_LED_COLOR)) {
//            if (command instanceof HSBType == false) {
//                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
//                        String.format("Received non HSBType command on channel", channelUID));
//                return;
//            }
//            HSBType hsbCommand = (HSBType) command;
//            int brightness = hsbCommand.getBrightness().intValue();
//            int saturation = hsbCommand.getSaturation().intValue();
//            int hue = hsbCommand.getHue().intValue();
//
//            if (brightness != thingState.getBrightness()) {
//                if (thingState.isWhiteModeOn()) {
//                    thingState.setWhiteModeBrightness(brightness);
//                } else {
//                    thingState.setBrightness(brightness);
//                }
//                commandService.setBrightness(brightness);
//
//            }
//
//            if (saturation != thingState.getSaturation()) {
//                thingState.setSaturation(saturation);
//                commandService.setSaturation(saturation);
//            }
//
//            if (hue != thingState.getHue()) {
//                thingState.setHue(hue);
//                commandService.setHue(hue);
//                if (thingState.isWhiteModeOn()) {
//                    thingState.setWhiteModeOn(false);
//                    updateState(thing.getChannel(CHANNEL_RGB_LED_WHITE_ON).getUID(), OnOffType.OFF);
//                }
//            }
//
//        }
//
//        if (channelUID.getId().equals(CHANNEL_RGB_LED_WHITE_ON)) {
//            if (command instanceof OnOffType == false) {
//                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
//                        String.format("Received non ON/OFF command on channel", channelUID));
//                return;
//            }
//
//            switch ((OnOffType) command) {
//                case ON: {
//                    if (thingState.isWhiteModeOn() == false) {
//                        thingState.setWhiteModeOn(true);
//                        commandService.whiteOn();
//                        HSBType state = new HSBType(new DecimalType(thingState.getHue()),
//                                new PercentType(thingState.getSaturation()),
//                                new PercentType(thingState.getWhiteModeBrightness()));
//
//                        updateState(thing.getChannel(CHANNEL_RGB_LED_COLOR).getUID(), state);
//
//                    }
//
//                    break;
//                }
//                case OFF: {
//                    if (thingState.isWhiteModeOn()) {
//                        thingState.setWhiteModeOn(false);
//                        // set previous values
//                        commandService.setHue(thingState.getHue());
//                        // commandService.setBrightness(thingState.getBrightness());
//                        // commandService.setSaturation(thingState.getSaturation());
//                        HSBType state = new HSBType(new DecimalType(thingState.getHue()),
//                                new PercentType(thingState.getSaturation()),
//                                new PercentType(thingState.getBrightness()));
//
//                        updateState(thing.getChannel(CHANNEL_RGB_LED_COLOR).getUID(), state);
//
//                    }
//                }
//            }
//        }
//
//        if (channelUID.getId().equals(CHANNEL_RGB_LED_ONOFF)) {
//
//            if (command instanceof OnOffType == false) {
//                updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
//                        String.format("Received non ON/OFF command on channel", channelUID));
//                return;
//            }
//            // TODO: handle command
//
//            // Note: if communication with thing fails for some reason,
//            // indicate that by setting the status with detail information
//            // updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
//            // "Could not control device at IP address x.x.x.x");
//
//            switch ((OnOffType) command) {
//                case ON: {
//                    if (thingState.isOn() == false) {
//                        thingState.setOn(true);
//                        commandService.turnOn();
//                    }
//                    break;
//                }
//                case OFF: {
//                    if (thingState.isOn()) {
//                        thingState.setOn(false);
//                        commandService.turnOff();
//                    }
//                    commandService.turnOff();
//                }
//            }
//
//        }
        logger.info("Handled command {} on channel {}", command, channelUID);
    }

    /**
     * Re-creates Limitless session if current hasn't been used for more than given period of time
     */
    protected void refreshCommandService() {


    }



    @Override
    public void initialize() {


        updateStatus(ThingStatus.ONLINE);

    }
}
