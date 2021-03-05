package org.firstinspires.ftc.teamcode.autonomous.commands.drivetrain;

import org.firstinspires.ftc.teamcode.autonomous.commands.Command;

public class ParallelCommand extends Command {

    private Command firstCommand;
    private Command secondCommand;

    public ParallelCommand(Command firstCommand, Command secondCommand) {
        this.firstCommand = firstCommand;
        this.secondCommand = secondCommand;
    }

    @Override
    public void start() {
        firstCommand.setSubsystems(robot, telemetry);
        secondCommand.setSubsystems(robot, telemetry);
        firstCommand.start();
        secondCommand.start();
    }

    @Override
    public void loop() {
        if(!firstCommand.isFinished()) firstCommand.loop();
        if(!secondCommand.isFinished()) secondCommand.loop();
    }

    @Override
    public boolean isFinished() {
        return firstCommand.isFinished() && secondCommand.isFinished();
    }
}
