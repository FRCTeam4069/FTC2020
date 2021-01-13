package org.firstinspires.ftc.teamcode.testOpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

@TeleOp
public class TestShooter extends OpMode {

    Robot robot;
    double setPoint;

    double startTime = 0;
    double endTime = 0;

    ColorSensor colourSensor;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);
        colourSensor = hardwareMap.get(ColorSensor.class, "colourSensor");
    }

    @Override
    public void loop() {

        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry dashboardTelemetry = dashboard.getTelemetry();

        startTime = System.currentTimeMillis() * 1000;
        dashboardTelemetry.addData("Elapsed time", startTime - endTime);
        endTime = System.currentTimeMillis() * 1000;

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

            dashboardTelemetry.addData("RPM", robot.shooter.speed);
            dashboardTelemetry.addData("Desired RPM", setPoint);
            dashboardTelemetry.addData("RPM1", robot.shooter.actualSpeed1);
            dashboardTelemetry.addData("RPM2", robot.shooter.actualSpeed2);
            dashboardTelemetry.addData("Time since shooter", robot.shooter.timeSinceLastUpdate);
            dashboardTelemetry.update();
        }
    }
}
