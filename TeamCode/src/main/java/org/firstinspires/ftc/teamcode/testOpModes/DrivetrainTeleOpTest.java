package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.autonomous.Scheduler;
import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

@TeleOp
public class DrivetrainTeleOpTest extends OpMode {

    Robot robot;
    Scheduler scheduler;
    double position = 1;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);
        scheduler = new Scheduler(telemetry, robot);
    }

    @Override
    public void loop() {
        robot.drivetrain.update(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        robot.drivetrain.addBaseTelemetry(false);
        telemetry.addData("Strafing encoder ticks/sec", robot.odometry.x.getCurrentVel());
        robot.odometry.addTelemetry(true);
        robot.intake.update(gamepad1.a, gamepad1.b);
        if(gamepad1.dpad_down) position = 0;
        else if(gamepad1.dpad_up) position = 1;
        robot.clamp.update(gamepad1.left_bumper, gamepad1.right_bumper, position);

    }
}
