package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

@TeleOp
public class WobbleGoalTest extends OpMode {

    Robot robot;
    double speed = 0;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        if(gamepad1.dpad_up) speed = 1;
        else if(gamepad1.dpad_down) speed = -1;
        else speed = 0;

        if(robot.clamp.bottomSensor() || robot.clamp.topSensor()) speed = 0;
        robot.clamp.update(gamepad1.left_bumper, gamepad1.right_bumper, speed);

    }
}
