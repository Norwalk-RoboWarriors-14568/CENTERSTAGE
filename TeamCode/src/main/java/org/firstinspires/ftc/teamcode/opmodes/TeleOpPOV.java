package org.firstinspires.ftc.teamcode.opmodes;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "POV Drive")

public class TeleOpPOV extends OpMode {
    DcMotor fl, bl, fr, br, arm1, arm2, lift, intakeLeft;
    private Servo gun;
    boolean jacob = false;
    private CRServo intake2;
    private Servo bucket;
    private boolean mecanumDriveMode = true;
    private float mecanumStrafe = 0, dominantXJoystick = 0;
    boolean drivePOV = true;

    @Override
    public void init() {
        fl = hardwareMap.dcMotor.get("leftFront");
        bl = hardwareMap.dcMotor.get("leftBack");
        fr = hardwareMap.dcMotor.get("rightFront");
        br = hardwareMap.dcMotor.get("rightBack");

        arm1 = hardwareMap.dcMotor.get("armLeft");
        arm2 = hardwareMap.dcMotor.get("armRight");
        //bucket = hardwareMap.servo.get("bucket");

        lift = hardwareMap.dcMotor.get("Lift");
        intakeLeft = hardwareMap.dcMotor.get("intakeLeft");
       intake2 = hardwareMap.crservo.get("intake2");
        gun = hardwareMap.servo.get("gun");


        fr.setDirection(DcMotor.Direction.REVERSE);
        br.setDirection(DcMotor.Direction.REVERSE);
        arm2.setDirection(DcMotor.Direction.REVERSE);

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
       // bucket.setPosition(0);
        //bucket.setBehavior();
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

        if (!gamepad1.right_bumper) {
            if ((gamepad1.left_stick_x) > 0.15) {
                drive(-gamepad1.left_stick_x / 1.5, gamepad1.left_stick_x / 1.5, gamepad1.left_stick_x / 1.5, -gamepad1.left_stick_x / 1.5);
            } else {
                drive((gamepad1.left_stick_y + -mecanumStrafe) / 1.5, (gamepad1.left_stick_y + mecanumStrafe) / 1.5, (gamepad1.left_stick_y + mecanumStrafe) / 1.5, (gamepad1.left_stick_y + -mecanumStrafe) / 1.5);
            }

            if ((gamepad1.right_stick_x) > 0.2) {
                drive(gamepad1.right_stick_x * -1, gamepad1.right_stick_x * -1, gamepad1.right_stick_x * 1, gamepad1.right_stick_x * 1);
            } else if (gamepad1.right_stick_x < -0.2) {
                drive(gamepad1.right_stick_x * -1, gamepad1.right_stick_x * -1, gamepad1.right_stick_x * 1, gamepad1.right_stick_x);


            }
        } else {
            if ((gamepad1.left_stick_x) > 0.15) {
                drive(-gamepad1.left_stick_x / 2, gamepad1.left_stick_x / 2, gamepad1.left_stick_x / 2, -gamepad1.left_stick_x / 2);
            } else {
                drive((gamepad1.left_stick_y + -mecanumStrafe) / 2, (gamepad1.left_stick_y + mecanumStrafe) / 2, (gamepad1.left_stick_y + mecanumStrafe) / 2, (gamepad1.left_stick_y + -mecanumStrafe) / 2);
            }

            if ((gamepad1.right_stick_x) > 0.2) {
                drive(gamepad1.right_stick_x * -1, gamepad1.right_stick_x * -1, gamepad1.right_stick_x * 1, gamepad1.right_stick_x * 1);
            } else if (gamepad1.right_stick_x < -0.2) {
                drive(gamepad1.right_stick_x * -1, gamepad1.right_stick_x * -1, gamepad1.right_stick_x * 1, gamepad1.right_stick_x);


            }
        }
        telemetry.addLine("DRIVE POV");




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

        if (gamepad2.right_trigger > 0.25){
            intakeLeft.setPower(1);
            intake2.setPower(-1);
        } else if (gamepad2.left_trigger > 0.25){
            intakeLeft.setPower(-0.5);
            intake2.setPower(0.5);
        } else{
            intakeLeft.setPower(0);
            intake2.setPower(0);

        }



        if(gamepad2.right_bumper){
           // bucket.setPosition(0.15);//Can only use these values nothing beyond this point
            lift.setPower(0.30);
        } else if (gamepad2.left_bumper){
            // bucket.setPosition(0.85);//Can only use these values nothing beyond this point
            lift.setPower(-0.30);
        }else {
            lift.setPower(0);
        }
        //telemetry.addLine("Servo " + bucket.getPosition());
        if (gamepad2.y) {
            gun.setPosition(1);
        }
        if (gamepad2.x ){
            gun.setPosition(0.5);
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