package org.firstinspires.ftc.teamcode.tutorials.showingAuto;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.autonomous.commands.Command;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.StarterStackDetector;

import java.util.ArrayList;

public class SchedulerPractice {

    ArrayList<CommandPractice> commandQueue;
    Telemetry telemetry;
    CommandPractice command;

    boolean isStarted = false;

    public SchedulerPractice(Telemetry telemetry, Drivetrain drivetrain, StarterStackDetector starterStackDetector) {
        this.telemetry = telemetry;
        command.setSubsystems(drivetrain,starterStackDetector);
        commandQueue = new ArrayList<>();
    }

    public void addCommand(CommandPractice command) {
        commandQueue.add(command);
    }

    public void run() {
        if(!commandQueue.isEmpty()) {
            CommandPractice nextCommand = commandQueue.get(0);

            if(!isStarted) {
                nextCommand.init();
                isStarted = true;
            }

            nextCommand.loop();
            if(nextCommand.isFinished()) {
                commandQueue.remove(0);
                if(!commandQueue.isEmpty()) {
                    commandQueue.get(0).init();
                }
            }
        }
    }
}
