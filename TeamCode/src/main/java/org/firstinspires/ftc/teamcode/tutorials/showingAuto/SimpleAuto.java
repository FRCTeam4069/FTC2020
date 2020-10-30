package org.firstinspires.ftc.teamcode.tutorials.showingAuto;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.StarterStackDetector;

@Autonomous
public class SimpleAuto extends OpMode {

    Drivetrain drivetrain;
    StarterStackDetector detector;
    SchedulerPractice scheduler;
    BNO055IMU imu;

    @Override
    public void init() {
        drivetrain = new Drivetrain(hardwareMap, telemetry);
        detector = new StarterStackDetector(hardwareMap, telemetry);
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.

        scheduler = new SchedulerPractice(telemetry, drivetrain, detector);
        scheduler.addCommand(new DriveForwardTest(1000));

        DriveForwardTest driveForwardTest = new DriveForwardTest(1000);
        scheduler.addCommand(driveForwardTest);
    }

    @Override
    public void loop() {
        scheduler.run();
    }
}
