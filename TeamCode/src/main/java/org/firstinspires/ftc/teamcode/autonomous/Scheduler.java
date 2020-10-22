package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.autonomous.commands.Command;
import org.firstinspires.ftc.teamcode.autonomous.commands.DriveForward;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.StarterStackDetector;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    Drivetrain drivetrain;
    StarterStackDetector starterStackDetector;
    Command command;
    List<Command> commandQueue = new ArrayList<>();
    Telemetry telemetry;

    //Take in subsystems and pass them to all commands
    public Scheduler(Telemetry telemetry, Drivetrain drivetrain, StarterStackDetector starterStackDetector) {
        this.telemetry = telemetry;
        this.drivetrain = drivetrain;
        this.starterStackDetector = starterStackDetector;
        command.setSubsystems(drivetrain, starterStackDetector);
    }

    //Add command to queue
    public void addCommand(Command command) {
        commandQueue.add(command);
    }

    //Method takes first command in queue, when it is finished, remove it and access next in line
    public void run() {
        if(commandQueue.size() == 0) {
            telemetry.addData("Queue is empty", true);
        }
        else {
            commandQueue.get(0).start();
            if (!commandQueue.get(0).isFinished()) {
                commandQueue.get(0).loop();
            }
            else commandQueue.remove(0);
        }
    }
}
