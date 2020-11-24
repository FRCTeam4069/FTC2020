package org.firstinspires.ftc.teamcode.testOpModes.tuningDrivebase;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.autonomous.Scheduler;
import org.firstinspires.ftc.teamcode.autonomous.commands.DriveForward;
import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

@Autonomous
public class DriveToPosition extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(hardwareMap, telemetry);
        Scheduler scheduler = new Scheduler(telemetry, robot);
        double[] positions = {1000, -500, 100, 2000};

        waitForStart();

        for(double position : positions) {
            scheduler.addCommand(new DriveForward(position));
            while(scheduler.getQueueSize() != 0) {
                scheduler.run();
                idle();
            }
            telemetry.addData("Position", robot.drivetrain.getCurrentPos());
            sleep(2000);
        }
    }
}
