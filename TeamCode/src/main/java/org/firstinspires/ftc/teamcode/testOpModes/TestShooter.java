package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

public class TestShooter extends OpMode {

    Robot robot;
    double setPoint;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);
        setPoint = 0.5;
    }

    @Override
    public void loop() {
        robot.shooter.update(setPoint);
        robot.shooter.getTelemetry(true);
    }
}
