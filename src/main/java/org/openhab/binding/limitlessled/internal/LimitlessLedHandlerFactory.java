/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.limitlessled.internal;

import static org.openhab.binding.limitlessled.LimitlessLedBindingConstants.*;

import java.util.Arrays;
import java.util.List;

import com.jeefix.iot.milight.common.MilightException;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.openhab.binding.limitlessled.handler.bridge.IBoxBridgeHandler;
import org.openhab.binding.limitlessled.handler.device.LimitlessIBoxLedHandler;
import org.openhab.binding.limitlessled.handler.device.LimitlessRgbLedHandler;


/**
 * The {@link LimitlessLedHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Maciej Iskra - Initial contribution
 */
public class LimitlessLedHandlerFactory extends BaseThingHandlerFactory {

    private final static List<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Arrays.asList(THING_TYPE_IBOX_BRIDGE,THING_TYPE_IBOX_LED,
            THING_TYPE_RGB_LED);

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected ThingHandler createHandler(Thing thing) {

        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (thingTypeUID.equals(THING_TYPE_RGB_LED)) {
            return new LimitlessRgbLedHandler(thing);
        }

        if (thingTypeUID.equals(THING_TYPE_IBOX_LED)) {
            return new LimitlessIBoxLedHandler(thing);
        }


        if (thingTypeUID.equals(THING_TYPE_IBOX_BRIDGE)) {
            return new IBoxBridgeHandler((Bridge) thing);
        }

       throw new MilightException(String.format("Attempting to create unsupported thing: %s",thing));
    }
}
