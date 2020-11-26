package org.firstinspires.ftc.teamcode.testOpModes.tuningDrivebase;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.autonomous.Scheduler;
import org.firstinspires.ftc.teamcode.autonomous.commands.TurnCommand;
import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

@Autonomous
public class TurnToAngle extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Robot robot = new Robot(hardwareMap, telemetry);
        double[] angles = {90};
        Scheduler scheduler = new Scheduler(telemetry, robot);

        waitForStart();
        while (opModeIsActive()) {
            for (double angle : angles) {
                scheduler.addCommand(new TurnCommand(angle));
                while (scheduler.getQueueSize() != 0) {
                    scheduler.run();
                    idle();
                }
                telemetry.addData("Current Turn", robot.drivetrain.getCurrentTurn());
                sleep(2000);
            }
        }
    }
}
