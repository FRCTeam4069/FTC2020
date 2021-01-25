package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class BlinkinTest extends OpMode {

    RevBlinkinLedDriver blinkin;

    @Override
    public void init() {
        blinkin = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");
    }

    @Override
    public void loop() {
        RevBlinkinLedDriver.BlinkinPattern pattern = RevBlinkinLedDriver.BlinkinPattern.VIOLET;
        blinkin.setPattern(pattern);
    }
}
