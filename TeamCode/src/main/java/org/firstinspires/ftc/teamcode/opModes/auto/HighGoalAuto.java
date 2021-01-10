package org.firstinspires.ftc.teamcode.opModes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.autonomous.Scheduler;
import org.firstinspires.ftc.teamcode.autonomous.commands.DriveToPosition;
import org.firstinspires.ftc.teamcode.autonomous.commands.IntakeFeed;
import org.firstinspires.ftc.teamcode.autonomous.commands.IntakeOff;
import org.firstinspires.ftc.teamcode.autonomous.commands.PassthroughFeed;
import org.firstinspires.ftc.teamcode.autonomous.commands.ResetEncoders;
import org.firstinspires.ftc.teamcode.autonomous.commands.ShooterOff;
import org.firstinspires.ftc.teamcode.autonomous.commands.ShooterOn;
import org.firstinspires.ftc.teamcode.autonomous.commands.TurnCommand;
import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

@Autonomous
public class HighGoalAuto extends LinearOpMode {

    Robot robot;

    @Override
    public void runOpMode() throws InterruptedException {

        boolean isStack = false;

        robot = new Robot(hardwareMap, telemetry);
        Scheduler initialScheduler = new Scheduler(telemetry, robot);
        Scheduler secondScheduler = new Scheduler(telemetry, robot);

        //Scheduler to drive to starter stack
        initialScheduler.addCommand(new DriveToPosition(-60000, 0));

        //Drives to line, turns and fires
        secondScheduler.addCommand(new ResetEncoders());
        secondScheduler.addCommand(new TurnCommand(270));
        secondScheduler.addCommand(new ResetEncoders());
        secondScheduler.addCommand(new DriveToPosition(24000, 40000));
        //Start running shooter
        secondScheduler.addCommand(new PassthroughFeed(3000, true));

        waitForStart();

        //Drive to starter stack
        while(opModeIsActive() && initialScheduler.getQueueSize() != 0) {
            initialScheduler.run();
            telemetry.update();
            idle();
        }


        //Check for stack
        if(opModeIsActive()) {
            double startTime = System.currentTimeMillis();
            while(System.currentTimeMillis() + 3000 < startTime && opModeIsActive()) {
                robot.detector.updateRecognitions();
                if(robot.detector.getStarterStackSize(100) != 0) isStack = true;
                sleep(50);
                idle();
            }
        }
        telemetry.addData("IS stack?", isStack);
        telemetry.update();
        //Set third scheduler based on stack (pick it up or no)
        Scheduler thirdScheduler = setScheduler(isStack);

        //Drive to line and fire
        while(opModeIsActive() && secondScheduler.getQueueSize() != 0) {
            double shooterSetpoint = 0;
            if(robot.shooter.isReady()) secondScheduler.run();
            if(secondScheduler.getQueueSize() < 2) shooterSetpoint = 3000;
            robot.shooter.update(shooterSetpoint);
            telemetry.addData("RPM", robot.shooter.speed);
            telemetry.addData("Queue", secondScheduler.getQueueSize());
            telemetry.update();
            idle();
        }

        while(!robot.shooter.isReady()) {
            robot.shooter.update(0);
        }

        //Pick up stack, fire and park on line or just park on line based on stack
        while(opModeIsActive() && thirdScheduler.getQueueSize() != 0) {
            if(robot.shooter.isReady()) thirdScheduler.run();
            if(thirdScheduler.getQueueSize() < 4 && thirdScheduler.getQueueSize() > 2)
                robot.shooter.update(3000);
            telemetry.update();
            idle();
        }
    }

    //Set final scheduler based on stack
    private Scheduler setScheduler(boolean isStack) {
        Scheduler scheduler = new Scheduler(telemetry, robot);
        if(isStack) {
            scheduler.addCommand(new IntakeFeed());
            scheduler.addCommand(new DriveToPosition(0, 0));
            scheduler.addCommand(new DriveToPosition(0, 60000));
            //Run shooter here
            scheduler.addCommand(new PassthroughFeed(3000, true));
            scheduler.addCommand(new ShooterOff());
            scheduler.addCommand(new DriveToPosition(0, 30000));
        }
        else scheduler.addCommand(new DriveToPosition(24000, 62000));

        return scheduler;
    }
}
