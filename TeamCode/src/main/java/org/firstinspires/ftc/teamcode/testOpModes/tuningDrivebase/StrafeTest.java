package org.firstinspires.ftc.teamcode.testOpModes.tuningDrivebase;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.autonomous.Scheduler;
import org.firstinspires.ftc.teamcode.autonomous.commands.StrafeCommand;
import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

public class StrafeTest extends LinearOpMode {

    public void runOpMode() throws InterruptedException {

        Robot robot = new Robot(hardwareMap, telemetry);
        Scheduler scheduler = new Scheduler(telemetry, robot);

        double[] positions = {5000, -9000, -200, 6000};

        waitForStart();

        while(opModeIsActive()) {
            for (double position : positions) {
                scheduler.addCommand(new StrafeCommand(position));
                while (scheduler.getQueueSize() != 0) {
                    scheduler.run();
                    idle();
                }
                telemetry.addData("Position", robot.odometry.x.getPosition());
                sleep(2000);
            }
        }
    }
}
