package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

@TeleOp
public class TestOfStarterStackDetector extends OpMode {

    Robot robot;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        robot.detector.getStarterStackSize();
        telemetry.update();
    }
}
