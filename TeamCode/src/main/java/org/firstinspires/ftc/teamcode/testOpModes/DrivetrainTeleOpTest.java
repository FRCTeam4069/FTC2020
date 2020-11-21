package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.Robot;

@TeleOp
public class DrivetrainTeleOpTest extends OpMode {

    Robot robot;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        robot.drivetrain.update(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
//        robot.drivetrain.displayPIDCoeffs(false);
//        telemetry.addData("Average Velocity", robot.drivetrain.getAvgVelocity());
//        robot.drivetrain.pidTelemetry(true);
        robot.intake.update(gamepad1.a, gamepad1.b);

    }
}
