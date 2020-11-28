package org.firstinspires.ftc.teamcode.testOpModes.tuningDrivebase;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.autonomous.Scheduler;
import org.firstinspires.ftc.teamcode.autonomous.commands.DriveToPosition;
import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

public class DriveToPositionTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(hardwareMap, telemetry);
        Scheduler scheduler = new Scheduler(telemetry, robot);

        double[] xPositions = {50000};
        double[] yPositions = {50000};

        waitForStart();

        for(int i = 0; i < xPositions.length; i++) {
            if(!opModeIsActive()) break;
            scheduler.addCommand(new DriveToPosition(xPositions[i], yPositions[i]));
            while(scheduler.getQueueSize() > 0) {
                if(!opModeIsActive()) break;
                scheduler.run();
                telemetry.addData("X position", robot.odometry.x.getPosition());
                telemetry.addData("Y position", robot.odometry.getYAvgPos());
                idle();
            }
            sleep(2000);
            idle();
        }
    }
}
