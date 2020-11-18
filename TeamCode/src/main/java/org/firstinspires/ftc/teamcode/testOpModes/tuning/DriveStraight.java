package org.firstinspires.ftc.teamcode.testOpModes.tuning;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.autonomous.Scheduler;
import org.firstinspires.ftc.teamcode.autonomous.commands.DriveForward;
import org.firstinspires.ftc.teamcode.subsystems.Robot;

@Autonomous
public class DriveStraight extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(hardwareMap, telemetry);
        Scheduler scheduler = new Scheduler(telemetry, robot);

        double startingAngle = robot.drivetrain.getCurrentTurn();
        telemetry.addData("Starting angle", startingAngle);

        waitForStart();

        scheduler.addCommand(new DriveForward(2000));
        while(scheduler.getQueueSize() != 0) {
            scheduler.run();
            idle();
        }
        double finishingAngle = robot.drivetrain.getCurrentTurn();
        double totalTurnChange = finishingAngle - startingAngle;
        telemetry.addData("Finishing angle", finishingAngle);
        telemetry.addData("Turn change", totalTurnChange);
    }
}
