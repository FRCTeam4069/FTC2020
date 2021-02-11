package org.firstinspires.ftc.teamcode.autonomous.commands.wobble;

import org.firstinspires.ftc.teamcode.autonomous.commands.Command;

public class WobbleDown extends Command {
    @Override
    public void start() {

    }

    @Override
    public void loop() {
        robot.clamp.update(false, false, -0.5);
    }

    @Override
    public boolean isFinished() {
        return robot.clamp.bottomSensor();
    }
}
