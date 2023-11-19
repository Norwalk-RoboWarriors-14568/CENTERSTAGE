package org.firstinspires.ftc.teamcode.opmodes;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "POV Drive")

public class TeleOpPOV extends OpMode {
    private DcMotor fl, bl, fr, br, arm1, arm2, lift, intakeLeft;
    private CRServo gun;
    private Servo rotateBucket;
    private boolean mecanumDriveMode = true;
    private float mecanumStrafe = 0, dominantXJoystick = 0;
    boolean drivePOV = true;

    @Override
    public void init() {
        fl = hardwareMap.dcMotor.get("leftFront");
        bl = hardwareMap.dcMotor.get("leftRear");
        fr = hardwareMap.dcMotor.get("rightFront");
        br = hardwareMap.dcMotor.get("rightRear");

        arm1 = hardwareMap.dcMotor.get("armAngle");
        arm2 = hardwareMap.dcMotor.get("armHeight");

        lift = hardwareMap.dcMotor.get("lift");
        intakeLeft = hardwareMap.dcMotor.get("intakeLeft");

        gun = hardwareMap.crservo.get("swing");
        rotateBucket = hardwareMap.servo.get("bucket");

        fr.setDirection(DcMotor.Direction.REVERSE);
        br.setDirection(DcMotor.Direction.REVERSE);

        setBehavior(fl, FLOAT);
        setBehavior(bl, FLOAT);
        setBehavior(fr, FLOAT);
        setBehavior(br, FLOAT);

        setBehavior(arm1, BRAKE  );
        setBehavior(arm2, BRAKE);
        setBehavior(lift, BRAKE);

        telemetry.addLine("Init Opmode");
        /*
        armAngle.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armAngle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        */
         arm1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        float armTicksZero = arm1.getCurrentPosition();//sets the floor position to 0
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


            }
            telemetry.addLine("DRIVE POV");
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

        if (gamepad2.right_stick_y > 0.1){
            arm2.setPower(gamepad2.right_stick_y);//arm slow
            arm1.setPower(gamepad2.right_stick_y);
        } else if (gamepad2.right_stick_y < -0.1) {
            arm2.setPower(gamepad2.right_stick_y);
            arm1.setPower(gamepad2.right_stick_y);

        } else {
            arm2.setPower(0);
            arm1.setPower(0);
        }
        if (gamepad1.right_trigger > 0.25){
            intakeLeft.setPower(1);
        } else if (gamepad1.left_trigger > 0.25){
            intakeLeft.setPower(-1);
        } else{
            intakeLeft.setPower(0);
        }

        if(gamepad1.dpad_up){
            lift.setPower(1);
        } else if (gamepad1.dpad_down){
            lift.setPower(-1);
        } else{
            lift.setPower(0);
        }
        if (gamepad1.dpad_left) {
            gun.setPower(1);
        } else if (gamepad1.dpad_right){
            gun.setPower(0);
        } else {
            gun.setPower(-1);

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
            arm1.setTargetPosition(newTargetAngle);
            arm1.setPower(armSpeed);
            arm1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

    }