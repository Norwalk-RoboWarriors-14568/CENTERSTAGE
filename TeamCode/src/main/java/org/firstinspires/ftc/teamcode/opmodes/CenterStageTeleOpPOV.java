package org.firstinspires.ftc.teamcode.opmodes;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp

public class CenterStageTeleOpPOV extends OpMode {
    private Servo bucket;
    private boolean mecanumDriveMode = true;

    @Override
    public void init() {

        bucket = hardwareMap.servo.get("bucket");
        bucket.resetDeviceConfigurationForOpMode();
        telemetry.addLine(bucket.getDirection().toString());





        telemetry.addLine("Init Opmode");
    }

    @Override
    public void loop(){


        if (gamepad2.right_bumper) {
            bucket.setPosition(0.15);
        } else if (gamepad2.left_bumper) {
            bucket.setPosition(0.85);
        }

        telemetry.addLine("bucket" +bucket.getPosition());

    }
    @Override
    public void stop() {
        telemetry.clearAll();
        telemetry.addLine("Stopped");
    }
        public void drive(double frontLeft, double backLeft, double frontRight, double backRight) {

        }
        public void setBehavior(DcMotor motor, DcMotor.ZeroPowerBehavior Behavior){
            motor.setZeroPowerBehavior(Behavior);
        }

    }