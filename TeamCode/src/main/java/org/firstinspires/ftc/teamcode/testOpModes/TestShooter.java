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
        if(gamepad1.a) setPoint = 3000;
        else if(gamepad1.b) setPoint = 3250;
        else if(gamepad1.y) setPoint = 3500;
        else if(gamepad1.x) setPoint = 3750;
        else if(gamepad1.dpad_down) setPoint = 4000;
        else if(gamepad1.dpad_right) setPoint = 4250;
        else if(gamepad1.dpad_up) setPoint = 4500;
        else if(gamepad1.dpad_left) setPoint = 4750;
        else if(gamepad1.left_bumper) setPoint = 5000;
        else if(gamepad1.right_bumper) setPoint = 5250;
        else setPoint = 0;

        robot.shooter.update(setPoint);
        robot.shooter.getTelemetry(true);
        robot.intake.update(gamepad1.start, gamepad1.back);
    }
}
