package org.firstinspires.ftc.teamcode.tutorials.showingAuto;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;

@Disabled
@Autonomous
public class GyroTurningNavx extends LinearOpMode {

    NavxMicroNavigationSensor navx;
    Drivetrain drivetrain;

    @Override
    public void runOpMode() throws InterruptedException {
        navx = hardwareMap.get(NavxMicroNavigationSensor.class, "navx");
        drivetrain = new Drivetrain(hardwareMap, telemetry);
        double desiredTurn = 90;

        waitForStart();

        while(navx.isCalibrating()) {
            wait(50);
            idle();
        }
        if(navx.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle < desiredTurn) {
            while (navx.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle < desiredTurn) {
                drivetrain.update(0, 0, 0.5);
                idle();
            }
        }
        else {
            while(navx.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle > desiredTurn) {
                drivetrain.update(0,0,0.5);
                idle();
            }
        }
    }
}
