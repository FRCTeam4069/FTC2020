package org.firstinspires.ftc.teamcode.subsystems.odometry;

import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.subsystems.robot.RobotHardware;

import java.util.HashMap;
import java.util.Map;

public class Odometry extends RobotHardware {

    //Create encoders
    Telemetry telemetry;
//    public Encoder yLeft;
    public Encoder yRight;
    public Encoder x;

    public Odometry(HardwareMap hardwareMap, Telemetry telemetry) {
        super(hardwareMap);

        //Initialize motors by passing in motors from RobotHardware
        this.telemetry = telemetry;
//        yLeft = new Encoder(telemetry, shooterSlave, Encoder.State.NEGATIVE);
        yRight = new Encoder(telemetry, passthroughMotor, Encoder.State.POSITIVE);
        x = new Encoder(telemetry, intakeMotor, Encoder.State.POSITIVE);
    }

    //Return forward/backward position
    public double getYAvgPos() {
        return (yRight.getPosition()); //+ yLeft.getPosition()) / 2.0;
    }

    //Return heading
    public double getCurrentHeading() {
        if(!navx.isCalibrating()) {
            double heading = navx.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;
            if(heading < 0) {
                heading = 180 + (180 + heading);
            }
            return heading;
        }
        else {
            telemetry.addData("Navx is calibrating?", navx.isCalibrating());
            return 0;
        }
    }

    //Return instance of navx
    public NavxMicroNavigationSensor getNavx() {
        return navx;
    }

    //Access to blinkin
    public RevBlinkinLedDriver blinkin() {
        return blinkin;
    }

    //Add odometry telemetry
    public void addTelemetry(boolean update) {
        //telemetry.addData("yLeft Pos", yLeft.getPosition());
        telemetry.addData("yRight Pos", yRight.getPosition());
        telemetry.addData("x pos", x.getPosition());
        telemetry.addData("Avg Pos y", getYAvgPos());
        telemetry.addData("Heading", getCurrentHeading());
        if(update) telemetry.update();
    }

    /////////////Colour sensor

    public void sensorTelemetry(boolean update) {
        telemetry.addData("R", colourSensor.red());
        telemetry.addData("G", colourSensor.green());
        telemetry.addData("B", colourSensor.blue());
        telemetry.addData("A", colourSensor.alpha());
        if(update) telemetry.update();
    }

    public ColorSensor colorSensor() {
        return colourSensor;
    }

    @Override
    public void disable() {
     //   yLeft.reset();
        yRight.reset();
        x.reset();
    }
}
