package org.firstinspires.ftc.teamcode.testOpModes.tuningDrivebase;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.autonomous.Scheduler;
import org.firstinspires.ftc.teamcode.autonomous.commands.TurnCommand;
import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

@Disabled
@Autonomous
public class TurnToAngle extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Robot robot = new Robot(hardwareMap, telemetry);
        double[] angles = {90, 180, 5, 270};
        Scheduler scheduler = new Scheduler(telemetry, robot);

        waitForStart();
        for (double angle : angles) {
            if(!opModeIsActive()) break;
            scheduler.addCommand(new TurnCommand(angle));
            while (scheduler.getQueueSize() != 0 && opModeIsActive()) {
                scheduler.run();
                telemetry.addData("Current Turn", robot.odometry.getCurrentHeading());
                idle();
            }
            sleep(2000);
            idle();
        }
    }
}
