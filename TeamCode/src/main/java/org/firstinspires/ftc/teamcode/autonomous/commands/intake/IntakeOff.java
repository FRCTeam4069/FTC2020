package org.firstinspires.ftc.teamcode.autonomous.commands.intake;

import org.firstinspires.ftc.teamcode.autonomous.commands.Command;

public class IntakeOff extends Command {

    @Override
    public void start() {
        robot.intake.update(false, false);
    }

    @Override
    public void loop() {
        robot.intake.update(false, false);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
