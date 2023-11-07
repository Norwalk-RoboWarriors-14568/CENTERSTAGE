package org.firstinspires.ftc.teamcode.opmodes;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "POV Drive")

public class TeleOpPOV extends OpMode {
    private DcMotor fl, bl, fr, br, armAngle, armHeight, lift, intakeLeft;
    private Servo swing, rotateBucket;
    private boolean mecanumDriveMode = true;
    private float mecanumStrafe = 0, dominantXJoystick = 0;
    boolean drivePOV = true;

    @Override
    public void init() {
        fl = hardwareMap.dcMotor.get("leftFront");
        bl = hardwareMap.dcMotor.get("leftRear");
        fr = hardwareMap.dcMotor.get("rightFront");
        br = hardwareMap.dcMotor.get("rightRear");

        armAngle = hardwareMap.dcMotor.get("armAngle");
        armHeight = hardwareMap.dcMotor.get("armHeight");

        lift = hardwareMap.dcMotor.get("lift");
        intakeLeft = hardwareMap.dcMotor.get("intakeLeft");

        swing = hardwareMap.servo.get("swing");
        rotateBucket = hardwareMap.servo.get("bucket");

        fr.setDirection(DcMotor.Direction.REVERSE);
        br.setDirection(DcMotor.Direction.REVERSE);

        setBehavior(fl, FLOAT);
        setBehavior(bl, FLOAT);
        setBehavior(fr, FLOAT);
        setBehavior(br, FLOAT);

        setBehavior(armAngle, BRAKE  );
        setBehavior(armHeight, BRAKE);
        setBehavior(lift, BRAKE);

        telemetry.addLine("Init Opmode");
        /*
        armAngle.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armAngle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        */
         armAngle.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        float armTicksZero = armAngle.getCurrentPosition();//sets the floor position to 0
    }

    @Override
    public void loop(){
        mecanumStrafe = gamepad1.left_stick_x;

        if (gamepad1.a && drivePOV){
            drivePOV = false;
        } else if (gamepad1.a && !drivePOV){
            drivePOV = true;
        }
        mecanumStrafe = gamepad1.left_stick_x;

        if (drivePOV) {
            if ((gamepad1.left_stick_x) > 0.15){
                drive(-gamepad1.left_stick_x / 2, gamepad1.left_stick_x / 2, gamepad1.left_stick_x / 2, -gamepad1.left_stick_x / 2);
            } else {
                drive((gamepad1.left_stick_y + -mecanumStrafe) / 2, (gamepad1.left_stick_y + mecanumStrafe) / 2, (gamepad1.left_stick_y + mecanumStrafe) / 2, (gamepad1.left_stick_y + -mecanumStrafe) / 2);
            }

            if ((gamepad1.right_stick_x) > 0.2){
                drive(gamepad1.right_stick_x *-1,gamepad1.right_stick_x  *-1, gamepad1.right_stick_x *   1, gamepad1.right_stick_x  *1);
            } else if (gamepad1.right_stick_x < -0.2){
                drive(gamepad1.right_stick_x *-1,gamepad1.right_stick_x  *-1, gamepad1.right_stick_x * 1, gamepad1.right_stick_x);
                telemetry.addLine("DRIVE POV");

            }
        } else {
            if (abs(gamepad1.left_stick_x) > 0.15 || abs(gamepad1.right_stick_x) > 0.15) {
                //removes negatives from joystick values, to set variable to +/- for determing stick farther from zero
                dominantXJoystick = (abs(gamepad1.left_stick_x) - abs(gamepad1.right_stick_x));
                mecanumDriveMode = true;
            } else {
                mecanumDriveMode = false;
            }

            if (mecanumDriveMode) {     //when enabled, motors will only hit 100% when strafing and driving

                if (dominantXJoystick > 0) {
                    mecanumStrafe = gamepad1.left_stick_x;
                } else if (dominantXJoystick < 0) {
                    mecanumStrafe = gamepad1.right_stick_x;
                }

                if (gamepad1.left_bumper) {
                    fl.setPower((gamepad1.left_stick_y + -mecanumStrafe) / 3.0); // previously 2
                    bl.setPower((gamepad1.left_stick_y + mecanumStrafe) / 3.0);
                    fr.setPower((gamepad1.right_stick_y + mecanumStrafe) / 3.0);
                    br.setPower((gamepad1.right_stick_y + -mecanumStrafe) / 3.0);
                } else if (gamepad1.right_bumper) {
                    fl.setPower((gamepad1.left_stick_y + -mecanumStrafe) / 1.5); // previously 2 * .5
                    bl.setPower((gamepad1.left_stick_y + mecanumStrafe) / 1.5);
                    fr.setPower((gamepad1.right_stick_y + mecanumStrafe) / 1.5);
                    br.setPower((gamepad1.right_stick_y + -mecanumStrafe) / 1.5);
                } else {
                    fl.setPower((gamepad1.left_stick_y + -mecanumStrafe) / 2.25); // previously 2 * .75
                    bl.setPower((gamepad1.left_stick_y + mecanumStrafe) / 2.25);
                    fr.setPower((gamepad1.right_stick_y + mecanumStrafe) / 2.25);
                    br.setPower((gamepad1.right_stick_y + -mecanumStrafe) / 2.25 );
                }
            } else if (!mecanumDriveMode ) {
                if (gamepad1.left_bumper) {
                    drive(gamepad1.left_stick_y * 0.8, gamepad1.right_stick_y * 0.8);
                } else if (gamepad1.right_bumper) {
                    drive(gamepad1.left_stick_y * 0.25, gamepad1.right_stick_y * 0.25);
                } else {
                    drive(gamepad1.left_stick_y * 0.5, gamepad1.right_stick_y * 0.5);
                }
            }
            telemetry.addLine("DRIVE TANK");

        }

        if (gamepad2.b){
            rotateBucket.setPosition(0 );

            swing.setPosition(0.53);//rest

        } else if (gamepad2.y){
            swing.setPosition(0);
            rotateBucket.setPosition(1);

        } else if (gamepad2.x) {
            swing.setPosition(-1);
            rotateBucket.setPosition(-1);

        }

        if (gamepad2.dpad_left){
            rotateBucket.setPosition(1);
        } else if (gamepad2.dpad_right){
            rotateBucket.setPosition(0);
        }


        if (gamepad2.right_stick_y > 0.1){
            armHeight.setPower(gamepad2.right_stick_y * 0.7);//arm slow
        } else if (gamepad2.right_stick_y < -0.1) {
            armHeight.setPower(gamepad2.right_stick_y * 0.7);
        } else {
            armHeight.setPower(0);
        }

        if (gamepad2.left_stick_y > 0.1) {
            //raiseArm(0.1,233);
            armAngle.setPower(gamepad2.left_stick_y * -0.5);
        } else if (gamepad2.left_stick_y < -0.1) {
           // raiseArm(0.1,0);
            armAngle.setPower(gamepad2.left_stick_y * -0.5);
        } else {
            armAngle.setPower(0);
        }

        if (gamepad2.right_bumper) {
            //intakeRight.setPower(1);
            intakeLeft.setPower(1);
        } else if (gamepad2.left_bumper) {
           // intakeRight.setPower(-1);
            intakeLeft.setPower(-1);
        } else {
            //intakeRight.setPower(0);
            intakeLeft.setPower(0);
        }
        if (gamepad2.dpad_up){
            lift.setPower(-1);
        } else if (gamepad2.dpad_down){
            lift.setPower(1);
        } else{
            lift.setPower(0);
        }
        telemetry.update();
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
        public void drive(double left, double right) {

            fl.setPower(left);
            bl.setPower(left);
            fr.setPower(right);
            br.setPower(right);

        }


        public void setBehavior(DcMotor motor, DcMotor.ZeroPowerBehavior Behavior){
            motor.setZeroPowerBehavior(Behavior);
        }
        public void raiseArm(double armSpeed, int newTargetAngle){
            armAngle.setTargetPosition(newTargetAngle);
            armAngle.setPower(armSpeed);
            armAngle.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

    }