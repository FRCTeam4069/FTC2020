package org.firstinspires.ftc.teamcode.subsystems.odometry;

import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.subsystems.robot.RobotHardware;

public class Odometry extends RobotHardware {

    Telemetry telemetry;
    public Encoder yLeft;
    public Encoder yRight;
    public Encoder x;

    public Odometry(HardwareMap hardwareMap, Telemetry telemetry) {
        super(hardwareMap);

        this.telemetry = telemetry;
        yLeft = new Encoder(telemetry, shooterSlave, Encoder.State.NEGATIVE);
        yRight = new Encoder(telemetry, yEncoderRight, Encoder.State.POSITIVE);
        x = new Encoder(telemetry, intakeMotor, Encoder.State.POSITIVE);
    }

    public double getYAvgPos() {
        return (yRight.getPosition() + yLeft.getPosition()) / 2.0;
    }

    public double getCurrentHeading() {
        return navx.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;
    }

    public NavxMicroNavigationSensor getNavx() {
        return navx;
    }

    public void addTelemetry(boolean update) {
        telemetry.addData("yLeft Pos", yLeft.getPosition());
        telemetry.addData("yRight Pos", yRight.getPosition());
        telemetry.addData("x pos", x.getPosition());
        telemetry.addData("Avg Pos y", getYAvgPos());
        telemetry.addData("Heading", getCurrentHeading());
        if(update) telemetry.update();
    }

    @Override
    public void disable() {
        yLeft.reset();
        yRight.reset();
        x.reset();
    }
}
