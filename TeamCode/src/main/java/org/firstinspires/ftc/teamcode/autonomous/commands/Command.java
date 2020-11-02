package org.firstinspires.ftc.teamcode.autonomous.commands;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.subsystems.StarterStackDetector;

public abstract class Command {

    //Subsystems to be accessed by commands
    protected Drivetrain drivetrain;
    protected StarterStackDetector starterStackDetector;
    protected Intake intake;

    //MUST be called by a command scheduler and runner
    public void setSubsystems(Drivetrain drivetrain, StarterStackDetector starterStackDetector, Intake intake) {
        this.drivetrain = drivetrain;
        this.starterStackDetector = starterStackDetector;
        this.intake = intake;
    }

    //Methods that need to be implemented by each command
    public abstract void start();
    public abstract void loop();
    public abstract boolean isFinished();
}
