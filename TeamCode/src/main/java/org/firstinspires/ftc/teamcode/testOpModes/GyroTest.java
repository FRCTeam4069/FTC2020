package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

@TeleOp
public class GyroTest extends OpMode {

    Robot robot;
    boolean started = false;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        if(!started) {
            robot.odometry.imu().initialize(robot.odometry.parameters);
            started = true;
        }
        telemetry.addData("Navx Heading", robot.odometry.getNavxHeading());
        telemetry.addData("IMU Heading", robot.odometry.getIMUHeading());
    }
}
