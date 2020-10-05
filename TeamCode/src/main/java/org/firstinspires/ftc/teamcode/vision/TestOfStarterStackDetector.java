package org.firstinspires.ftc.teamcode.vision;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp
public class TestOfStarterStackDetector extends OpMode {

    StarterStackDetector stackDetector;
    Telemetry telemetry;

    @Override
    public void init() {
        stackDetector = new StarterStackDetector(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        stackDetector.printCurrentVals();
    }
}
