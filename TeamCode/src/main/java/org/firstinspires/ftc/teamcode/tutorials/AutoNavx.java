package org.firstinspires.ftc.teamcode.tutorials;

import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.tutorials.MecanumDrive;

@Disabled
@Autonomous
public class AutoNavx extends LinearOpMode {

    NavxMicroNavigationSensor navx;
    MecanumDrive drivetrain;

    @Override
    public void runOpMode() throws InterruptedException {

        navx = hardwareMap.get(NavxMicroNavigationSensor.class, "navx");
        drivetrain = new MecanumDrive(hardwareMap, telemetry);

        double desiredTurnAngle = 270;

        waitForStart();

        while(navx.isCalibrating()) {
            wait(50);
            idle();
        }
        while(navx.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle < desiredTurnAngle - 2) {
            drivetrain.update(0, 0, 0.5);
            idle();
        }
        while(navx.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle > desiredTurnAngle + 2) {
            drivetrain.update(0, 0, -0.5);
            idle();
        }
    }
}
