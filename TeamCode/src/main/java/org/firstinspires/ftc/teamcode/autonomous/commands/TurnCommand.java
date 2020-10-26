package org.firstinspires.ftc.teamcode.autonomous.commands;

public class TurnCommand extends Command {

    //IN DEGREES
    double desiredTurn;
    double currentTurn = drivetrain.getCurrentTurn();

    //Takes desired turn IN DEGREES
    public TurnCommand(double desiredTurn) {
        this.desiredTurn = desiredTurn;
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
