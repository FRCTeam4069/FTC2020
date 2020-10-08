package org.firstinspires.ftc.teamcode.vision;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class TestOfStarterStackDetector extends OpMode {

    StarterStackDetector stackDetector;

    @Override
    public void init() {
        stackDetector = new StarterStackDetector(hardwareMap, telemetry);

    }

    @Override
    public void loop() {
        stackDetector.getStarterStackSize();
        telemetry.addData("starter stack size", stackDetector.getStarterStackSize());
        telemetry.update();
    }
}
