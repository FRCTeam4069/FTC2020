package org.firstinspires.ftc.teamcode.tutorials.showingAuto;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;

@Disabled
@Autonomous
public class GyroTurningInternalIMU extends LinearOpMode {

    BNO055IMU imu;
    Drivetrain drivetrain;

    @Override
    public void runOpMode() throws InterruptedException {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.temperatureUnit = BNO055IMU.TempUnit.CELSIUS;
        parameters.loggingEnabled = true;
        parameters.loggingTag = "imu";

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        drivetrain = new Drivetrain(hardwareMap, telemetry);

        double desiredTurn = 90;

        waitForStart();

        while(!imu.isGyroCalibrated()) {
            wait(50);
        }

        if(imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle < desiredTurn) {
            while (imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle < desiredTurn) {
                drivetrain.update(0, 0, 0.5);
                idle();
            }
        }
        else {
            while(imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle > desiredTurn) {
                drivetrain.update(0,0, -0.5);
                idle();
            }
        }
    }

}
