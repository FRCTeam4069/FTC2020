package org.firstinspires.ftc.teamcode.opModes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.autonomous.Scheduler;
import org.firstinspires.ftc.teamcode.autonomous.commands.DriveToPosition;
import org.firstinspires.ftc.teamcode.autonomous.commands.TurnCommand;
import org.firstinspires.ftc.teamcode.subsystems.StarterStackDetector;
import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

@Autonomous
public class WobbleGoalAuto extends LinearOpMode {

    Robot robot;
    Scheduler scheduler;


    public void setScheduler(StarterStackDetector.DropZone dropZone) {
        if(dropZone == StarterStackDetector.DropZone.A)
            scheduler.addCommand(new DriveToPosition(50000, 75000));
        else if(dropZone == StarterStackDetector.DropZone.B)
            scheduler.addCommand(new DriveToPosition(25000, 100000));
        else scheduler.addCommand(new DriveToPosition(50000, 125000));
    }

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(hardwareMap, telemetry);
        Scheduler initialScheduler = new Scheduler(telemetry, robot);
        Scheduler secondScheduler = new Scheduler(telemetry, robot);
        scheduler = new Scheduler(telemetry, robot);

        initialScheduler.addCommand(new DriveToPosition(100000, 0));
        secondScheduler.addCommand(new DriveToPosition(50000, 100000));
        secondScheduler.addCommand(new TurnCommand(90));

        waitForStart();

        while(initialScheduler.getQueueSize() != 0 && opModeIsActive()) {
            initialScheduler.run();
            telemetry.update();
            idle();
        }

        double startingTime = System.currentTimeMillis();
        while(System.currentTimeMillis() < (startingTime + 1000) && opModeIsActive()) {
            robot.detector.updateRecognitions();
            telemetry.addData("Updating recognitions", true);
            idle();
        }

        while(secondScheduler.getQueueSize() != 0 && opModeIsActive()) {
            secondScheduler.run();
            idle();
        }

        int starterStack = robot.detector.getStarterStackSize(100);
        StarterStackDetector.DropZone dropZone;
        if(starterStack == 0) dropZone = StarterStackDetector.DropZone.A;
        else if(starterStack == 1) dropZone = StarterStackDetector.DropZone.B;
        else if(starterStack == 4) dropZone = StarterStackDetector.DropZone.C;
        else dropZone = StarterStackDetector.DropZone.BAD;

        setScheduler(dropZone);

        while(opModeIsActive()) {
            scheduler.run();
            telemetry.addData("Drop Zone", dropZone);
            telemetry.update();
            idle();
        }
    }
}
