package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

@TeleOp
public class WobbleGoalTest extends OpMode {

    Robot robot;
    double position;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);
        position = robot.clamp.position();
    }

    @Override
    public void loop() {
        if(gamepad1.dpad_up) position += 0.01;
        else if(gamepad1.dpad_down) position -= 0.01;

        if(position < 0.05) position = 0.05;
        else if(position > 0.85) position = 0.85;
        robot.clamp.update(gamepad1.left_bumper, gamepad1.right_bumper, position);
        robot.clamp.positionTelemetry(true);

    }
}
