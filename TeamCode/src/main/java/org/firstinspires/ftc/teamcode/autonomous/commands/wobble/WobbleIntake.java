package org.firstinspires.ftc.teamcode.autonomous.commands.wobble;

import org.firstinspires.ftc.teamcode.autonomous.commands.Command;

public class WobbleIntake extends Command {

    boolean in;
    public WobbleIntake(boolean in) {

        this.in = in;
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        robot.clamp.update(in, !in, robot.clamp.position());
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
