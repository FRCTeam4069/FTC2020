package org.firstinspires.ftc.teamcode.testOpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.autonomous.Scheduler;
import org.firstinspires.ftc.teamcode.autonomous.commands.DriveForward;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.StarterStackDetector;

@Autonomous
public class testAuto extends OpMode {

    //Create scheduler for beginning motion, one for each stack case
    Scheduler startingScheduler;
    Scheduler fourRingScheduler;
    Scheduler oneRingScheduler;
    Scheduler zeroRingScheduler;

    //Subsystems
    StarterStackDetector stackDetector;
    Drivetrain drivetrain;
    
    //Call to set commands to the scheduler called at the beginning of auto
    private void setStartingScheduler() {
        startingScheduler.addCommand(new DriveForward(1000));
    }

    //Call to set commands to scheduler called if there are 4 rings in stack
    private void setFourRingScheduler() {
        fourRingScheduler.addCommand(new DriveForward(5000));
    }

    //Call to set commands to scheduler called if there is 1 ring in stack
    private void setOneRingScheduler() {
        oneRingScheduler.addCommand(new DriveForward(4000));
    }

    //Call to set commands to scheduler called if there are no rings
    private void setZeroRingScheduler() {
        zeroRingScheduler.addCommand(new DriveForward(3000));
    }

    //Ensure all subsystems and schedulers are initialized
    @Override
    public void init() {
        stackDetector = new StarterStackDetector(hardwareMap, telemetry);
        drivetrain = new Drivetrain(hardwareMap, telemetry);
        startingScheduler = new Scheduler(telemetry, drivetrain, stackDetector);
        fourRingScheduler = new Scheduler(telemetry, drivetrain, stackDetector);
        oneRingScheduler = new Scheduler(telemetry, drivetrain, stackDetector);
        zeroRingScheduler = new Scheduler(telemetry, drivetrain, stackDetector);

        setStartingScheduler();
    }


    @Override
    public void loop() {

        //Run first scheduler to approach stack
        startingScheduler.run();

        //Get stack size (should have already been initialized and begun to run
        double stackSize = stackDetector.getStarterStackSize(170);

        //Call four stack scheduler if stack is four high
        if(stackSize == 4) {
            setFourRingScheduler();
            fourRingScheduler.run();
        }

        //Call one stack scheduler if stack is one high
        else if(stackSize == 1) {
            setOneRingScheduler();
            oneRingScheduler.run();
        }

        //Call empty stack scheduler if stack is empty
        else {
           setZeroRingScheduler();
           zeroRingScheduler.run();
        }
    }
}
