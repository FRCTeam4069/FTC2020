package org.firstinspires.ftc.teamcode.opModes.auto;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.autonomous.Scheduler;
import org.firstinspires.ftc.teamcode.autonomous.commands.drivetrain.DriveToPosition;
import org.firstinspires.ftc.teamcode.autonomous.commands.intake.IntakeFeed;
import org.firstinspires.ftc.teamcode.autonomous.commands.intake.IntakeOff;
import org.firstinspires.ftc.teamcode.autonomous.commands.drivetrain.ResetEncoders;
import org.firstinspires.ftc.teamcode.autonomous.commands.drivetrain.TurnCommand;
import org.firstinspires.ftc.teamcode.autonomous.commands.WaitCommand;
import org.firstinspires.ftc.teamcode.autonomous.commands.wobble.WobbleIntake;
import org.firstinspires.ftc.teamcode.autonomous.commands.wobble.WobbleIntakeOff;
import org.firstinspires.ftc.teamcode.autonomous.commands.wobble.WobbleSetPosition;
import org.firstinspires.ftc.teamcode.subsystems.StarterStackDetector;
import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;


//A = ZERO RINGS - CLOSEST TO LAUNCH LINE
//C = FOUR RINGS - CLOSEST TO GOAL

@Autonomous
public class WobbleGoalAuto extends LinearOpMode {

    Robot robot;
    StarterStackDetector.DropZone dropZone;

    public Scheduler setScheduler(StarterStackDetector.DropZone dropZone) {
        Scheduler scheduler = new Scheduler(telemetry, robot);
        if(dropZone == StarterStackDetector.DropZone.A) {
            scheduler.addCommand(new DriveToPosition(0, 40000));
            scheduler.addCommand(new WobbleSetPosition(0.15));
            scheduler.addCommand(new WobbleIntake(false));
            scheduler.addCommand(new WaitCommand(500));
            scheduler.addCommand(new WobbleIntakeOff());
            scheduler.addCommand(new DriveToPosition(29000, 60000));
        }
        else if(dropZone == StarterStackDetector.DropZone.B) {
            scheduler.addCommand(new DriveToPosition(15000, 80000));
            scheduler.addCommand(new WobbleSetPosition(0.15));
            scheduler.addCommand(new WobbleIntake(false));
            scheduler.addCommand(new WaitCommand(500));
            scheduler.addCommand(new WobbleIntakeOff());
            scheduler.addCommand(new DriveToPosition(29000, 60000));
        }
        else {
            scheduler.addCommand(new DriveToPosition(0, 110000));
            scheduler.addCommand(new WobbleSetPosition(0.15));
            scheduler.addCommand(new WobbleIntake(false));
            scheduler.addCommand(new WaitCommand(500));
            scheduler.addCommand(new WobbleIntakeOff());
            scheduler.addCommand(new DriveToPosition(29000, 60000));
        }
        return scheduler;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry dashboardTelemetry = dashboard.getTelemetry();

        robot = new Robot(hardwareMap, telemetry);
        Scheduler initialScheduler = new Scheduler(telemetry, robot);
        Scheduler secondScheduler = new Scheduler(telemetry, robot);
        Scheduler thirdScheduler;

        //Scheduler to drive to starter stack
        initialScheduler.addCommand(new WobbleSetPosition(0.85));
        initialScheduler.addCommand(new DriveToPosition(-62000, 0));

        //Drives to line, turns and fires
        secondScheduler.addCommand(new ResetEncoders());
        secondScheduler.addCommand(new TurnCommand(270));
        secondScheduler.addCommand(new ResetEncoders());
        secondScheduler.addCommand(new DriveToPosition(29000, 40000));
        secondScheduler.addCommand(new TurnCommand(270));
        //Start running shooter
        secondScheduler.addCommand(new IntakeFeed(false, 1));
        secondScheduler.addCommand(new WaitCommand(3000));
        secondScheduler.addCommand(new IntakeOff());

        waitForStart();

        //Drive to starter stack
        while(opModeIsActive() && initialScheduler.getQueueSize() != 0) {
            initialScheduler.run();
            telemetry.update();
            idle();
        }
        int stackSize = 0;
        //Check for stack
        if(opModeIsActive()) {
            double startTime = System.currentTimeMillis();
            while(System.currentTimeMillis() < startTime + 1000 && opModeIsActive()) {
                robot.detector.updateRecognitions();
                stackSize = robot.detector.getStarterStackSize(155);
                dashboardTelemetry.addData("Stack size?", stackSize);
                dashboardTelemetry.update();
                sleep(50);
                idle();
            }
        }
        if(stackSize == 4) dropZone = StarterStackDetector.DropZone.C;
        else if (stackSize == 1) dropZone = StarterStackDetector.DropZone.B;
        else dropZone = StarterStackDetector.DropZone.A;
        thirdScheduler = setScheduler(dropZone);

        //Drive to line and fire
        while(opModeIsActive() && secondScheduler.getQueueSize() != 0) {
            secondScheduler.run();
            robot.shooter.update(3000);
            dashboardTelemetry.addData("RPM", robot.shooter.speed);
            dashboardTelemetry.addData("Queue", secondScheduler.getQueueSize());
            dashboardTelemetry.update();
            idle();
        }
        //Position after this motion is (29k, 40k) relative to starter stack detection position

        while(opModeIsActive() && thirdScheduler.getQueueSize() != 0) {
            thirdScheduler.run();
            idle();
        }
        robot.deactivate();
    }
}
