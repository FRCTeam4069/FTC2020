package org.firstinspires.ftc.teamcode.autonomous.commands;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.robot.Robot;

public abstract class Command {

    //Subsystems to be accessed by commands
    protected Robot robot;
    protected Telemetry telemetry;

    //MUST be called by a command scheduler and runner
    public void setSubsystems(Robot robot, Telemetry telemetry) {
        this.robot = robot;
        this.telemetry = telemetry;
    }

    //Methods that need to be implemented by each command
    public abstract void start();
    public abstract void loop();
    public abstract boolean isFinished();
}
