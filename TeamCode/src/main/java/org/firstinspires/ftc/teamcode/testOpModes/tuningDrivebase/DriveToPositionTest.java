package org.firstinspires.ftc.teamcode.testOpModes.tuningDrivebase;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.autonomous.Scheduler;
import org.firstinspires.ftc.teamcode.autonomous.commands.DriveToPosition;
import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

@Autonomous
public class DriveToPositionTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(hardwareMap, telemetry);
        Scheduler scheduler = new Scheduler(telemetry, robot);

        double[] xPositions = {50000, 75000, 75000, 30000, -10000};
        double[] yPositions = {50000, 75000, 50000, 70000, 0};

        waitForStart();

        for(int i = 0; i < xPositions.length; i++) {
            if(!opModeIsActive()) break;
            DriveToPosition driveToPosition = new DriveToPosition(xPositions[i], yPositions[i]);
            scheduler.addCommand(driveToPosition);
            while(scheduler.getQueueSize() > 0) {
                if(!opModeIsActive()) break;
                scheduler.run();
                telemetry.addData("X position", robot.odometry.x.getPosition());
                telemetry.addData("Y position", robot.odometry.getYAvgPos());
                telemetry.addData("Turn", robot.odometry.getCurrentHeading());
                telemetry.addData("Starting turn", driveToPosition.startingTurn);
                telemetry.addData("Turn Error", driveToPosition.turnError);
                idle();
            }
            sleep(2000);
            idle();
        }
    }
}
