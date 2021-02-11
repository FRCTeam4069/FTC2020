package org.firstinspires.ftc.teamcode.autonomous.commands.wobble;

import org.firstinspires.ftc.teamcode.autonomous.commands.Command;

public class WobbleIntakeOff extends Command {
    @Override
    public void start() {
        robot.clamp.update(false, false, 0);
    }

    @Override
    public void loop() {
        robot.clamp.update(false, false, 0);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
