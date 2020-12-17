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

        if(gamepad1.left_stick_button) {
            robot.shooter.rawControl(1);
            robot.shooter.getTelemetry(true);
        }
        else {
            if (gamepad1.a) setPoint = 1500;
            else if (gamepad1.b) setPoint = 1750;
            else if (gamepad1.y) setPoint = 2000;
            else if (gamepad1.x) setPoint = 2250;
            else if (gamepad1.dpad_down) setPoint = 2500;
            else if (gamepad1.dpad_right) setPoint = 2750;
            else if (gamepad1.dpad_up) setPoint = 3000;
            else if (gamepad1.dpad_left) setPoint = 3250;
            else if (gamepad1.left_bumper) setPoint = 3500;
            else if (gamepad1.right_bumper) setPoint = 3750;
            else setPoint = 0;

            robot.shooter.update(setPoint);
            robot.intake.update(gamepad1.start, gamepad1.back);
        }
    }
}
