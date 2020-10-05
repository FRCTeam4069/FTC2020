package org.firstinspires.ftc.teamcode.vision;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@TeleOp
public class StarterStackDetectTest extends OpMode {

    public static final String TF_DECTECTOR_MODEL_ASSETS = "UltimateGoal.tflite";
    public static final String LABEL_FIRST_ELEMENT = "Quad";
    public static final String LABEL_SECOND_ELEMENT = "Single";

    private static final String VUFORIA_KEY = "AUdbl2P/////AAABmWYe3kyuhU8cm/rBVoVMzVotL3p0vfzGnir9dscLSA2K03xgWC6VBG08gzHvNXA4ac6axF+EINaVBUMwEcoY6tNQX8bSAY778bfzT6WQzilY8UNOo+Pa0oBZ6Ut5B0piq7wcLLUSMjXv2iX0yjcioMJ9Lqoo5tXAbaL2x3sn+zkioNVm1zl1a1fgzXmtxilRTrK4MN7kYFoO0yfIlq5YDKiNU5Hzrbr1wzdhSp2+FoQekvlCfMZzi0YjICtfooi5m70RuTJcn7aRNHHMyuKSSmqzxe4W4j0cLR+Kw4yFVxue75GDxMIh1Ot7zi1blRojFBZ1oY5s84aQvCZ5Xtsf+COCA0MP1GQwcw9utlH9Ln2o";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfDetector;

    @Override
    public void init() {
        initVuforia();
        initTFDetector();

        if(tfDetector != null && vuforia != null) {
            tfDetector.activate();

            //Set zoom here if necessary
        }
    }

    @Override
    public void loop() {

        int activeZone;

        //Ensure tf detector is alive
        if(tfDetector != null) {

            //This function returns a null if there is no new detction info
            List<Recognition> updatedRecognitions = tfDetector.getUpdatedRecognitions();

            if(updatedRecognitions != null) {

                //Check if there is a starter stack
                if(updatedRecognitions.size() == 0) {
                    activeZone = 0;
                    telemetry.addData("ActiveZone", activeZone);
                }

                telemetry.addData("Num Objects Detected:", updatedRecognitions.size());

                int i = 0;

                // Loop through detections and display data (should only be one in starter stack)
                 for (Recognition recognition : updatedRecognitions) {

                     // Check active target zone
                     if(recognition.getTop() > 239 && recognition.getTop() < 250) {
                         activeZone = 4;
                     }
                     else if(recognition.getTop() > 310 && recognition.getTop() < 320) {
                         activeZone = 1;
                     }
                     else {
                         activeZone = 1000;
                     }

                     telemetry.addData(String.format("Label # (%d)", i), recognition.getLabel());
                     telemetry.addData(String.format("Confidence of (%d)", i), recognition.getConfidence());
                     telemetry.addData(String.format("Bottom of (%d)", i), recognition.getBottom());
                     telemetry.addData(String.format("Top of (%d)", i), recognition.getTop());
                     telemetry.addData(String.format("Left of (%d)", i), recognition.getLeft());
                     telemetry.addData(String.format("Right of (%d)", i), recognition.getRight());
                     telemetry.addData("ActiveZone", activeZone);
                     telemetry.update();
                     i++;
                    }
                }
        }
    }



    // Function to initialize vuforia detector (called in init)
    public void initVuforia() {

        // Create necessary params for vuforia init
        VuforiaLocalizer.Parameters vuforiaParam = new VuforiaLocalizer.Parameters();
        vuforiaParam.vuforiaLicenseKey = VUFORIA_KEY;
        vuforiaParam.cameraName = hardwareMap.get(WebcamName.class, "webcam");

        // Create vuforia
        vuforia = ClassFactory.getInstance().createVuforia(vuforiaParam);
    }

    // Function to initialize tensorFlow lite detector
    public void initTFDetector() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        // Create necessary params for tf init
        TFObjectDetector.Parameters tfParam = new TFObjectDetector.Parameters();
        tfParam.minResultConfidence = 0.75f;

        // Create tf detector and add model assets
        tfDetector = ClassFactory.getInstance().createTFObjectDetector(tfParam, vuforia);
        tfDetector.loadModelFromAsset(TF_DECTECTOR_MODEL_ASSETS, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }
}
