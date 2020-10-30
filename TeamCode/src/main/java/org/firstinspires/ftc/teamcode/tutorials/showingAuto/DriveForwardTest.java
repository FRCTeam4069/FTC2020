package org.firstinspires.ftc.teamcode.tutorials.showingAuto;

public class DriveForwardTest extends CommandPractice {

    double desiredPosition;
    double startingPosition;

    public DriveForwardTest(double desiredPosition) {
        this.desiredPosition = desiredPosition;
    }

    @Override
    public void init() {
        startingPosition = drivetrain.getCurrentPos();
        desiredPosition += startingPosition;
    }

    @Override
    public void loop() {
        drivetrain.update(0.75, 0, 0);
    }

    @Override
    public boolean isFinished() {
        return desiredPosition - 5 <= drivetrain.getCurrentPos();
    }
}
