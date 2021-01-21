package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

@TeleOp
public class WobbleGoalTest extends OpMode {

    Robot robot;
    double position = 0;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        if(gamepad1.dpad_up) position = 1;
        else if(gamepad1.dpad_down) position = 0;
        robot.clamp.update(gamepad1.left_bumper, gamepad1.right_bumper, position);
    }
}
