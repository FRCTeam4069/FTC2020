package org.firstinspires.ftc.teamcode.tutorials;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.tutorials.MecanumDrive;

@Disabled
@Autonomous
public class AutoInternalIMU extends LinearOpMode {

    BNO055IMU imu;
    MecanumDrive drivetrain;

    @Override
    public void runOpMode() throws InterruptedException {

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.loggingEnabled = true;
        parameters.loggingTag = "imu";

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        drivetrain = new MecanumDrive(hardwareMap, telemetry);

        double desiredTurnAngle = 90;

        waitForStart();

        while(imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle < desiredTurnAngle - 2) {
            drivetrain.update(0, 0, 0.5);
            idle();
        }

        while(imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle > desiredTurnAngle + 2) {
            drivetrain.update(0, 0, -0.5);
            idle();
        }
    }
}
