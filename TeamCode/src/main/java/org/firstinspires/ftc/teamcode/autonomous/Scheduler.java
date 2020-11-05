package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.autonomous.commands.Command;
import org.firstinspires.ftc.teamcode.autonomous.commands.DriveForward;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.StarterStackDetector;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {

    List<Command> commandQueue = new ArrayList<>();
    Telemetry telemetry;
    private Drivetrain drivetrain;
    private StarterStackDetector starterStackDetector;
    private Intake intake;


    boolean started = false;

    //Take in subsystems and pass them to all commands
    public Scheduler(Telemetry telemetry, Drivetrain drivetrain, StarterStackDetector starterStackDetector, Intake intake) {
        this.telemetry = telemetry;
        this.drivetrain = drivetrain;
        this.starterStackDetector = starterStackDetector;
        this.intake = intake;
    }

    //Add command to queue
    public void addCommand(Command command) {
        commandQueue.add(command); //Queue of type ArrayList
        command.setSubsystems(drivetrain, starterStackDetector, intake);
    }

    public int getQueueSize() {
        return commandQueue.size();
    }

    //Method takes first command in queue, when it is finished, remove it and access next in line
    public void run() {

        telemetry.addData("command queue size", commandQueue.size());
        telemetry.update();
        if(commandQueue.size() != 0) {
            Command nextCommand = commandQueue.get(0);

            if(!started) {
                nextCommand.start();
                started = true;
            }

            nextCommand.loop();
            if(nextCommand.isFinished()) {
                commandQueue.remove(0);
                telemetry.addData("Command", "removed");
                telemetry.update();
                if(commandQueue.size() != 0) {
                    commandQueue.get(0).start();
                }
            }
        }
    }
}
