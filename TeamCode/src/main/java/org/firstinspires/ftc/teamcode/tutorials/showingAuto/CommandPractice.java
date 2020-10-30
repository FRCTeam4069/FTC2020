package org.firstinspires.ftc.teamcode.tutorials.showingAuto;

import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.StarterStackDetector;

public abstract class CommandPractice {

    protected Drivetrain drivetrain;
    protected StarterStackDetector detector;

    public void setSubsystems(Drivetrain drivetrain, StarterStackDetector detector) {
        this.drivetrain = drivetrain;
        this.detector = detector;
    }

    public abstract void init();
    public abstract void loop();
    public abstract boolean isFinished();
}
