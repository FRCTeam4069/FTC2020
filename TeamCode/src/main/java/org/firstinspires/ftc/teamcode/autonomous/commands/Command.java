package org.firstinspires.ftc.teamcode.autonomous.commands;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.StarterStackDetector;

public abstract class Command {

    //Subsystems to be accessed by commands
    protected Drivetrain drivetrain;
    protected StarterStackDetector starterStackDetector;

    //MUST be called by a command scheduler and runner
    public void setSubsystems(Drivetrain drivetrain, StarterStackDetector starterStackDetector) {
        this.drivetrain = drivetrain;
        this. starterStackDetector = starterStackDetector;
    }


    public abstract void start();
    public abstract void loop();
    public abstract boolean isFinished();
}
