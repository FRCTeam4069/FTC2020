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

    boolean started = false;

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
        Command nextCommand = commandQueue.get(0);

        if(!started) {
            nextCommand.start();
            started = true;
        }

        nextCommand.loop();
        if(nextCommand.isFinished()) {
            commandQueue.remove(0);
            if(commandQueue.size() != 0) {
                commandQueue.get(0).start();
            }
        }
    }
}
