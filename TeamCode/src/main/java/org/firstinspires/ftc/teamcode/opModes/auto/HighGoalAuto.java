package org.firstinspires.ftc.teamcode.opModes.auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.autonomous.Scheduler;
import org.firstinspires.ftc.teamcode.autonomous.commands.DriveToPosition;
import org.firstinspires.ftc.teamcode.autonomous.commands.IntakeFeed;
import org.firstinspires.ftc.teamcode.autonomous.commands.IntakeOff;
import org.firstinspires.ftc.teamcode.autonomous.commands.ResetEncoders;
import org.firstinspires.ftc.teamcode.autonomous.commands.ShooterOff;
import org.firstinspires.ftc.teamcode.autonomous.commands.ShooterOn;
import org.firstinspires.ftc.teamcode.autonomous.commands.TurnCommand;
import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

public class HighGoalAuto extends LinearOpMode {

    Robot robot;

    @Override
    public void runOpMode() throws InterruptedException {

        boolean isStack = false;

        robot = new Robot(hardwareMap, telemetry);
        Scheduler initialScheduler = new Scheduler(telemetry, robot);
        Scheduler secondScheduler = new Scheduler(telemetry, robot);

        initialScheduler.addCommand(new DriveToPosition(50000, 0));
        secondScheduler.addCommand(new DriveToPosition(75000, 30000));
        secondScheduler.addCommand(new TurnCommand(270));
        secondScheduler.addCommand(new ResetEncoders());
        secondScheduler.addCommand(new ShooterOn(3000));
        secondScheduler.addCommand(new IntakeFeed());
        secondScheduler.addCommand(new IntakeOff());
        secondScheduler.addCommand(new ShooterOff());

        waitForStart();

        while(opModeIsActive() && initialScheduler.getQueueSize() != 0) {
            initialScheduler.run();
            telemetry.update();
            idle();
        }

        if(opModeIsActive()) {
            double startTime = System.currentTimeMillis();
            while(System.currentTimeMillis() + 500 < startTime && opModeIsActive()) {
                robot.detector.updateRecognitions();
                if(robot.detector.getStarterStackSize(100) != 0) isStack = true;
                idle();
            }
        }

        while(opModeIsActive() && secondScheduler.getQueueSize() != 0) {
            secondScheduler.run();
            telemetry.update();
            idle();
        }
    }

    private Scheduler setScheduler(boolean isStack) {
        Scheduler scheduler = new Scheduler(telemetry, robot);
        if(isStack) {
            scheduler.addCommand(new IntakeFeed());
            scheduler.addCommand(new DriveToPosition(0, -50000));
            scheduler.addCommand(new DriveToPosition(0, 20000));
            scheduler.addCommand(new );
        }
        else scheduler.addCommand(new DriveToPosition(0, 30000));
    }
}
