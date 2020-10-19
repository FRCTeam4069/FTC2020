package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.StarterStackDetector;

@TeleOp
public class TestOfStarterStackDetector extends OpMode {

    StarterStackDetector stackDetector;

    @Override
    public void init() {
        stackDetector = new StarterStackDetector(hardwareMap, telemetry);

    }

    @Override
    public void loop() {
        stackDetector.getStarterStackSize(170);
        telemetry.addData("starter stack size", stackDetector.getStarterStackSize(170));
        telemetry.update();
    }
}
