/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.limitlessled;

import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link LimitlessLedBinding} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Maciej Iskra - Initial contribution
 */
public class LimitlessLedBindingConstants {

    public static final String BINDING_ID = "limitlessled";

    // List of all Thing Type UIDs
    public final static ThingTypeUID THING_TYPE_IBOX_BRIDGE = new ThingTypeUID(BINDING_ID, "iBoxBridge");
    public final static ThingTypeUID THING_TYPE_IBOX_LED = new ThingTypeUID(BINDING_ID, "iBoxLed");
    public final static ThingTypeUID THING_TYPE_RGB_LED = new ThingTypeUID(BINDING_ID, "rgbLed");

    // List of all Channel ids
    public final static String CHANNEL_RGB_LED_ONOFF = "rgbLedOnOff";
    public final static String CHANNEL_RGB_LED_WHITE_ON = "rgbLedWhiteOn";
    public final static String CHANNEL_RGB_LED_COLOR = "rgbLedColor";

    // Things properties
    public final static String CONFIG_BRIDGE_ADDRESS = "BRIDGE_ADDRESS";
    public final static String CONFIG_STATE_SYNC_PERIOD = "STATE_SYNC_PERIOD";
    public final static String CONFIG_LED_STRIP_ZONE = "ZONE_ID";



}
