package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

@TeleOp
public class TestShooter extends OpMode {

    Robot robot;
    double setPoint;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);
        setPoint = 0.0;
    }

    @Override
    public void loop() {
        if(gamepad1.a) setPoint = 0.5;
        else if(gamepad1.b) setPoint = 0.55;
        else if(gamepad1.y) setPoint = 0.6;
        else if(gamepad1.x) setPoint = 0.65;
        else if(gamepad1.dpad_down) setPoint = 0.7;
        else if(gamepad1.dpad_right) setPoint = 0.75;
        else if(gamepad1.dpad_up) setPoint = 0.8;
        else if(gamepad1.dpad_left) setPoint = 0.85;
        else if(gamepad1.left_bumper) setPoint = 0.9;
        else if(gamepad1.right_bumper) setPoint = 0.95;
        else setPoint = 0.0;

        robot.shooter.update(setPoint);
        robot.shooter.getTelemetry(true);
        robot.intake.update(gamepad1.start, gamepad1.back);
    }
}
