package org.firstinspires.ftc.teamcode.autonomous.commands;

import org.firstinspires.ftc.teamcode.subsystems.Robot;

public abstract class Command {

    //Subsystems to be accessed by commands
    protected Robot robot;
    //MUST be called by a command scheduler and runner
    public void setSubsystems(Robot robot) {
        this.robot = robot;
    }

    //Methods that need to be implemented by each command
    public abstract void start();
    public abstract void loop();
    public abstract boolean isFinished();
}
