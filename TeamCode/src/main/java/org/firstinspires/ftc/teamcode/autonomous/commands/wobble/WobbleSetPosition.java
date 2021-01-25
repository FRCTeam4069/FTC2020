package org.firstinspires.ftc.teamcode.autonomous.commands.wobble;

import org.firstinspires.ftc.teamcode.autonomous.commands.Command;

public class WobbleSetPosition extends Command {

    double position;

    public WobbleSetPosition(double position) {
        this.position = position;
    }

    @Override
    public void start() {
        robot.clamp.update(false, false, position);
    }

    @Override
    public void loop() {
        robot.clamp.update(false, false, position);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
