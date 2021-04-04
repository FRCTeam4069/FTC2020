package org.firstinspires.ftc.teamcode.autonomous;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.autonomous.commands.Command;
import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {

    List<Command> commandQueue = new ArrayList<>();
    Telemetry telemetry;
    private Robot robot;


    boolean started = false;

    //Take in subsystems and pass them to all commands
    public Scheduler(Telemetry telemetry, Robot robot) {
        this.telemetry = telemetry;
        this.robot = robot;
    }

    //Add command to queue
    public void addCommand(Command command) {
        commandQueue.add(command); //Queue of type ArrayList
        command.setSubsystems(robot, telemetry);
    }

    public void setCommand(int index, Command command) {
        commandQueue.add(index, command);
    }

    public int getQueueSize() {
        return commandQueue.size();
    }

    //Method takes first command in queue, when it is finished, remove it and access next in line
    public void run() {

        telemetry.addData("command queue size", commandQueue.size());
        if(commandQueue.size() != 0) {
            Command nextCommand = commandQueue.get(0);

            if(!started) {
                nextCommand.start();
                started = true;
            }

            nextCommand.loop();
            if(nextCommand.isFinished()) {
                robot.drivetrain.disable();
                commandQueue.remove(0);
                telemetry.addData("Command", "removed");
                telemetry.update();
                if(commandQueue.size() != 0) {
                    commandQueue.get(0).start();
                }
            }
        }
    }
    public void disableSubsystems() {
        robot.deactivate();
    }
}
