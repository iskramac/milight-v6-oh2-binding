package org.openhab.binding.limitlessled.handler.bridge;

import com.google.common.collect.ImmutableSet;
import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.of;
import static org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder.create;
import static org.openhab.binding.limitlessled.LimitlessLedBindingConstants.*;

/**
 * TODO write class description here
 * <p>
 * Created by Maciej Iskra (emacisk) on 2017-04-14.
 */
public class ThingDiscoveryService extends AbstractDiscoveryService {

    private static final Logger log = LoggerFactory.getLogger(ThingDiscoveryService.class);

    private ThingUID bridgeUID;


    public ThingDiscoveryService() {
        super(ImmutableSet.of(THING_TYPE_IBOX_LED, THING_TYPE_RGB_LED), 100000);
    }


    public void discoverThings() {
        discoverThing(new ThingUID(THING_TYPE_IBOX_LED, bridgeUID, "0"), "iBox Led", of());
//        discoverThing(new ThingUID(THING_TYPE_RGB_LED, bridgeUID, "0"), "Led Strip (All Zones)", of("ZONE_ID", "0"));
        discoverThing(new ThingUID(THING_TYPE_RGB_LED, bridgeUID, "1"), "Led Strip (Zone 1)", of(CONFIG_LED_STRIP_ZONE, "1"));
        discoverThing(new ThingUID(THING_TYPE_RGB_LED, bridgeUID, "2"), "Led Strip (Zone 2)", of(CONFIG_LED_STRIP_ZONE, "2"));
        discoverThing(new ThingUID(THING_TYPE_RGB_LED, bridgeUID, "3"), "Led Strip (Zone 3)", of(CONFIG_LED_STRIP_ZONE, "3"));
        discoverThing(new ThingUID(THING_TYPE_RGB_LED, bridgeUID, "4"), "Led Strip (Zone 4)", of(CONFIG_LED_STRIP_ZONE, "4"));


    }

    public void discoverThing(ThingUID thing, String label, Map<String, Object> properties) {
        log.debug("Attempting to add thing {} with properties {}", thing, properties);
        DiscoveryResult discoveryResult = create(thing).withLabel(label).withBridge(bridgeUID).withProperties(properties).build();
        thingDiscovered(discoveryResult);
        log.info("Added thing {} with properties {}", thing, properties);
    }


    @Override
    protected void startScan() {
        // Automatic scanning will is not implemented since bridge doesn't support devices discovery ;(
    }

    public void setBridgeUID(ThingUID bridgeUID) {
        this.bridgeUID = bridgeUID;
    }
}
