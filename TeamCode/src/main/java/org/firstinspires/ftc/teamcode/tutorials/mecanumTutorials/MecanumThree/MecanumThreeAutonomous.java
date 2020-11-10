package org.firstinspires.ftc.teamcode.tutorials.mecanumTutorials.MecanumThree;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "MecanumThreeAutonomous", group = "Mecanum tutorials")
public class MecanumThreeAutonomous extends LinearOpMode {

    //Autonomously control our subsystem

    @Override
    public void runOpMode() throws InterruptedException {

        //Declare and initialize subsystem
        MecanumThreeSubsystem drivetrain = new MecanumThreeSubsystem(hardwareMap, telemetry);

        waitForStart();

        //Drive forward
        drivetrain.update(0.5,0,0);
        sleep(1000);

        drivetrain.update(0,0,0);
        sleep(500);

        //Drive right
        drivetrain.update(0, 0.5, 0);
        sleep(1000);

        drivetrain.update(0,0,0);
        sleep(500);

        //Drive backLeft(sw)
        drivetrain.update(-0.5, -0.5, 0);
        sleep(1000);

        drivetrain.update(0,0,0);
        sleep(500);

        //Drive forwardLeft
        drivetrain.update(0.5, -0.5, 0);
        sleep(1000);

        drivetrain.update(0,0,0);
        sleep(500);

        //Turn clockwise
        drivetrain.update(0,0,0.5);
        sleep(1000);

        drivetrain.update(0,0,0);
    }
}
