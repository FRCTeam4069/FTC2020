package org.firstinspires.ftc.teamcode.opModes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.autonomous.Scheduler;
import org.firstinspires.ftc.teamcode.autonomous.commands.drivetrain.DriveToPosition;
import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

@Autonomous
public class ParkOnLine extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(hardwareMap, telemetry);
        Scheduler scheduler = new Scheduler(telemetry, robot);

        scheduler.addCommand(new DriveToPosition(0, 110000));

        waitForStart();

        while(opModeIsActive()) {
            scheduler.run();
            telemetry.addData("X Position", robot.odometry.x.getPosition());
            telemetry.addData("Y Position", robot.odometry.yRight.getPosition());
            telemetry.addData("Heading", robot.odometry.getCurrentHeading());
        }
    }
}
