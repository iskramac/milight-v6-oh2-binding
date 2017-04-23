package com.jeefix.limitlessled.device;

import com.jeefix.iot.milight.common.MilightArgumentException;
import com.jeefix.iot.milight.common.MilightCommand;
import com.jeefix.limitlessled.device.state.IBoxState;
import com.jeefix.limitlessled.device.state.LedStripState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * TODO write class description here
 * <p>
 * Created by Maciej Iskra (emacisk) on 2017-04-12.
 */
public class IBoxDevice extends BaseDevice {

    private static final Logger log = LoggerFactory.getLogger(IBoxDevice.class);

    public static final int HUE_MAX_COLOR = 360;

    private IBoxState state;

    public IBoxDevice() {
        state = new IBoxState();
    }





    public IBoxDevice on() {
        log.debug("Attempting to turn on device {}", this);

        sendCommand(MilightCommand.IBOX_ON.getHexCommand());
        state.setOn(true);
        log.info("Turned on device {}", this);
        return this;
    }

    public IBoxDevice off() {
        log.debug("Attempting to turn off device {}", this);
        hue(getState().getHue());
        sendCommand(MilightCommand.IBOX_OFF.getHexCommand());
        state.setOn(false);
        log.info("Turned off device {}", this);
        return this;
    }

    public IBoxDevice brightness(int brightness) {
        log.debug("Attempting to set brightness level to {} of device {}", brightness, this);
        if (brightness < 0 || brightness > 100) {
            throw new MilightArgumentException(String.format("Brightness level should be in range 0-100. Received %d", brightness));
        }
        int normalizedValue = (int) Math.ceil((double) brightness * 64 / 100);
        ArrayList<String> commands = new ArrayList<>(2);
        if (state.isOn() == false) {
            state.setOn(true);
            commands.add(MilightCommand.IBOX_ON.getHexCommand());
        }
        commands.add(String.format(MilightCommand.IBOX_BRIGHTNESS.getHexCommand(), normalizedValue));
        state.setBrightness(brightness);
        sendCommandBatch(commands);
        log.info("Changed brightness level to {} of device {}", brightness, this);
        return this;
    }

    public IBoxDevice hue(int hue) {
        log.debug("Attempting to set hue level to {} of device {}", hue, this);
        int color = (int) (((float) hue / HUE_MAX_COLOR) * 255);

        ArrayList<String> commands = new ArrayList<>(2);
        if (state.isOn() == false) {
            state.setOn(true);
            commands.add(MilightCommand.IBOX_ON.getHexCommand());
        }
        commands.add(String.format(MilightCommand.IBOX_HUE.getHexCommand(), color, color, color, color));

        int normalizedValue = (int) Math.ceil((double) getState().getBrightness() * 64 / 100);
        commands.add(String.format(MilightCommand.IBOX_BRIGHTNESS.getHexCommand(), normalizedValue));

        sendCommandBatch(commands);
        state.setHue(hue);
        state.setWhite(false);
        log.info("Changed hue level to {} of device {}", hue, this);
        return this;
    }



    public IBoxDevice whiteOn() {
        log.debug("Attempting to turn on white mode of device {}", this);

        ArrayList<String> commands = new ArrayList<>(2);
        if (state.isOn() == false) {
            state.setOn(true);
            commands.add(MilightCommand.IBOX_ON.getHexCommand());
        }
        commands.add(MilightCommand.IBOX_WHITE_ON.getHexCommand());

        int normalizedValue = (int) Math.ceil((double) getState().getBrightness() * 64 / 100);
        commands.add(String.format(MilightCommand.IBOX_BRIGHTNESS.getHexCommand(), normalizedValue));

        state.setWhite(true);
        sendCommandBatch(commands);
        log.info("Turned on  white mode of device {}", this);
        return this;
    }

    public IBoxDevice whiteOff() {
        log.debug("Attempting to turn off white mode of device {}", this);
        hue(getState().getHue());
        log.info("Turned off white mode of device {}", this);
        return this;
    }

    public LedStripState getState() {
        return state;
    }

    @Override
    public void synchronizeState() {
        if(getState().isOn() == false){
            off();
            return;
        }else{
            on();
        }
        if(getState().isWhite()){
            whiteOn();
        }else{
            whiteOff();
        }
    }
}
