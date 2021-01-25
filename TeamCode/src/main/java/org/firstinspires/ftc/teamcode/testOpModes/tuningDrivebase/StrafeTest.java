package org.firstinspires.ftc.teamcode.testOpModes.tuningDrivebase;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.autonomous.Scheduler;
import org.firstinspires.ftc.teamcode.autonomous.commands.drivetrain.DriveToPosition;
import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

@Autonomous
public class StrafeTest extends LinearOpMode {

    public void runOpMode() throws InterruptedException {

        Robot robot = new Robot(hardwareMap, telemetry);
        Scheduler scheduler = new Scheduler(telemetry, robot);

        double[] positions = {50000, 15000, 70000};

        waitForStart();
        for (double position : positions) {
            if(!opModeIsActive()) break;
            scheduler.addCommand(new DriveToPosition(position, 0));
            while (scheduler.getQueueSize() != 0 && opModeIsActive()) {
                scheduler.run();
                telemetry.addData("Position", robot.odometry.x.getPosition());
                telemetry.addData("Heading", robot.odometry.getCurrentHeading());
                telemetry.update();
                idle();
            }
            telemetry.addData("Position", robot.odometry.x.getPosition());
            sleep(2000);
        }
    }
}
