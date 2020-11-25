package org.firstinspires.ftc.teamcode.tutorials.mecanumTutorials.MecanumThree;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.tutorials.mecanumTutorials.MecanumThree.MecanumSubsystem;

@Disabled
@Autonomous
public class MecanumSubsystemAuto extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        MecanumSubsystem drivetrain = new MecanumSubsystem(hardwareMap, telemetry);

        waitForStart();

        drivetrain.update(0.5, 0, 0);
        sleep(1000);

        drivetrain.update(0,0,0);
        sleep(500);

        drivetrain.update(0, 0.5, 0);
        sleep(1000);

        drivetrain.update(0,0,0);
        sleep(500);

        drivetrain.update(0, 0, 0.5);
        sleep(1000);

        drivetrain.update(0,0,0);
        sleep(500);

        drivetrain.update(0.5, 0.5, 0);
        sleep(1000);

        drivetrain.update(0,0,0);
        sleep(500);
    }
}
