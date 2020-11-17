package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous
public class TestMecanumAuto extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        MecanumSubsystem drivebase = new MecanumSubsystem(hardwareMap, telemetry);

        waitForStart();

        drivebase.update(0.5, 0, 0);
        sleep(1000);

        drivebase.update(0,0,0);
        sleep(500);

        drivebase.update(0, 0.5, 0);
        sleep(1000);

        drivebase.update(0,0,0);
        sleep(500);

        drivebase.update(0, 0, 0.5);
        sleep(1000);

        drivebase.update(0,0,0);
        sleep(500);

        drivebase.update(0.5, 0.5, 0);
        sleep(1000);

        drivebase.update(0,0,0);
        sleep(500);
    }
}
