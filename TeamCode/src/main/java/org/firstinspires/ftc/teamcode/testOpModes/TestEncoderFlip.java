package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

@Disabled
@TeleOp
public class TestEncoderFlip extends OpMode {

    Robot robot;
    boolean press = false;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        robot.drivetrain.update(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        if(gamepad1.a && !press) {
            press = true;
            int x = robot.odometry.x.getPosition();
            int y = robot.odometry.yRight.getPosition();

            robot.odometry.disable();

            robot.odometry.x.setCurrentPositionTo(y);
            robot.odometry.yRight.setCurrentPositionTo(x);
        }
        else press = false;

        telemetry.addData("x", robot.odometry.x.getPosition());
        telemetry.addData("y", robot.odometry.yRight.getPosition());
        telemetry.update();
    }
}
