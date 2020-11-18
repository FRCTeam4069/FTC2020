package org.firstinspires.ftc.teamcode.testOpModes.tuning;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.autonomous.Scheduler;
import org.firstinspires.ftc.teamcode.autonomous.commands.TurnCommand;
import org.firstinspires.ftc.teamcode.subsystems.Robot;

@Autonomous
public class TurnToAngle extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Robot robot = new Robot(hardwareMap, telemetry);
        double[] angles = {90, 270, 45, 180};
        Scheduler scheduler = new Scheduler(telemetry, robot);

        waitForStart();

        for(double angle : angles) {
            scheduler.addCommand(new TurnCommand(angle));
            scheduler.run();
            telemetry.addData("Current Turn", robot.drivetrain.getCurrentTurn());
            sleep(2000);
        }
    }
}
