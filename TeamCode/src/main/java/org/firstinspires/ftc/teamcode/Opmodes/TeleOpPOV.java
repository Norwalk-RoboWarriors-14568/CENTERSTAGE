package org.firstinspires.ftc.teamcode.opmodes;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "POV Drive")

public class TeleOpPOV extends OpMode {
    private DcMotor fl, bl, fr, br, armAngle, armHeight, slide;

    private CRServo intakeLeft, intakeRight;
    private boolean mecanumDriveMode = true, slideOn = true;
    private float mecanumStrafe = 0;

    @Override
    public void init() {
        fl = hardwareMap.dcMotor.get("leftFront");
        bl = hardwareMap.dcMotor.get("leftRear");
        fr = hardwareMap.dcMotor.get("rightFront");
        br = hardwareMap.dcMotor.get("rightRear");


        armAngle = hardwareMap.dcMotor.get("armAngle");
        armHeight = hardwareMap.dcMotor.get("armHeight");

        slide = hardwareMap.dcMotor.get("slide");

        intakeLeft = hardwareMap.crservo.get("intakeLeft");
        intakeRight = hardwareMap.crservo.get("intakeRight");


        fr.setDirection(DcMotor.Direction.REVERSE);
        br.setDirection(DcMotor.Direction.REVERSE);
        intakeRight.setDirection(DcMotorSimple.Direction.REVERSE);

        setBehavior(fl, FLOAT);
        setBehavior(bl, FLOAT);
        setBehavior(fr, FLOAT);
        setBehavior(br, FLOAT);

        setBehavior(armAngle, BRAKE);
        setBehavior(armHeight, BRAKE);


        telemetry.addLine("Init Opmode");
    }

    @Override
    public void loop(){
        mecanumStrafe = gamepad1.left_stick_x;


        if ((gamepad1.left_stick_x) > 0.15){
            drive(-gamepad1.left_stick_x / 2, gamepad1.left_stick_x / 2, gamepad1.left_stick_x / 2, -gamepad1.left_stick_x / 2);
        } else {
            drive((gamepad1.left_stick_y + -mecanumStrafe) / 2, (gamepad1.left_stick_y + mecanumStrafe) / 2, (gamepad1.left_stick_y + mecanumStrafe) / 2, (gamepad1.left_stick_y + -mecanumStrafe) / 2);
        }

        if ((gamepad1.right_stick_x) > 0.2){
            drive(-gamepad1.right_stick_x *2,-gamepad1.right_stick_x  *2, gamepad1.right_stick_x * 2, gamepad1.right_stick_x  *2);
        }

        if (gamepad2.right_stick_y > 0.1){
            armHeight.setPower(0.4);
        } else if (gamepad2.right_stick_y < -0.1) {
            armHeight.setPower(-0.4);
        } else {
            armHeight.setPower(0);
        }

        if (gamepad2.left_stick_y > 0.1) {
            armAngle.setPower(0.4);
        } else if (gamepad2.left_stick_y < -1) {
            armAngle.setPower(-0.4);
        } else {
            armAngle.setPower(0);
        }

        if (gamepad2.right_bumper) {
            intakeRight.setPower(1);
            intakeLeft.setPower(1);
        } else if (gamepad2.left_bumper) {
            intakeRight.setPower(-1);
            intakeLeft.setPower(-1);
        } else {
            intakeRight.setPower(0);
            intakeLeft.setPower(0);
        }
        if (gamepad2.a){
            toggleSlide();
        }
    }
    @Override
    public void stop() {
        telemetry.clearAll();
        telemetry.addLine("Stopped");
    }
        public void drive(double frontLeft, double backLeft, double frontRight, double backRight) {
            fl.setPower(frontLeft);
            bl.setPower(backLeft);
            fr.setPower(frontRight);
            br.setPower(backRight);

        }
        public void toggleSlide(){
            if (!slideOn) {
                slide.setPower(1);
                slideOn = true;
            } else {
                slide.setPower(0);
                slideOn = false;
            }

        }

        public void setBehavior(DcMotor motor, DcMotor.ZeroPowerBehavior Behavior){
            motor.setZeroPowerBehavior(Behavior);
        }

    }