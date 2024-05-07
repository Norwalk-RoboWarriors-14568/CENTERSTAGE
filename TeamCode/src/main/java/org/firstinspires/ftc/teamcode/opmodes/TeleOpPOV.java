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
    DcMotor fl, bl, fr, br, leftArmMotor, rightArmMotor, lift, intakeLeft;
    private Servo droneLauncherServo;
    boolean jacob = false;
    private CRServo intake2;
    private Servo bucket;
    private boolean mecanumDriveMode = true;
    private float mecanumStrafe = 0, dominantXJoystick = 0;
    boolean drivePOV = true;
    final private double CPCM_MECC = 537.6/ ( 3.75 * Math.PI);
    final private double CPI_ARM = 537.6/(4.4);
    @Override
    public void init() {
        fl = hardwareMap.dcMotor.get("leftFront");
        bl = hardwareMap.dcMotor.get("leftBack");
        fr = hardwareMap.dcMotor.get("rightFront");
        br = hardwareMap.dcMotor.get("rightBack");

        leftArmMotor = hardwareMap.dcMotor.get("armLeft");
        rightArmMotor = hardwareMap.dcMotor.get("armRight");
        //bucket = hardwareMap.servo.get("bucket");

        lift = hardwareMap.dcMotor.get("Lift");
        intakeLeft = hardwareMap.dcMotor.get("intakeLeft");
       intake2 = hardwareMap.crservo.get("intake2");
        droneLauncherServo = hardwareMap.servo.get("gun");


        fr.setDirection(DcMotor.Direction.REVERSE);
        br.setDirection(DcMotor.Direction.REVERSE);
        leftArmMotor.setDirection(DcMotor.Direction.REVERSE);

        setBehavior(fl, FLOAT);
        setBehavior(bl, FLOAT);
        setBehavior(fr, FLOAT);
        setBehavior(br, FLOAT);

        setBehavior(leftArmMotor, BRAKE  );
        setBehavior(rightArmMotor, BRAKE);
        setBehavior(lift, BRAKE);
        telemetry.addLine("Init Opmode");
        /*
        armAngle.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armAngle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        */
        float armTicksZero = leftArmMotor.getCurrentPosition();//sets the floor position to 0
       // bucket.setPosition(0);
        //bucket.setBehavior();
        leftArmMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightArmMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftArmMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightArmMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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
                drive(-gamepad1.left_stick_x*0.66, gamepad1.left_stick_x *0.66,
                        gamepad1.left_stick_x*0.66, -gamepad1.left_stick_x *0.66);
            } else {
                drive((gamepad1.left_stick_y + -mecanumStrafe) *0.66, (gamepad1.left_stick_y + mecanumStrafe)*0.66,
                        (gamepad1.left_stick_y + mecanumStrafe)*0.66, (gamepad1.left_stick_y + -mecanumStrafe)*0.66);
            }

            if ((gamepad1.right_stick_x) > 0.2) {
                drive(gamepad1.right_stick_x * -0.66, gamepad1.right_stick_x * -0.66,
                        gamepad1.right_stick_x * 0.66, gamepad1.right_stick_x * 0.66);
            } else if (gamepad1.right_stick_x < -0.2) {
                drive(gamepad1.right_stick_x * -0.66, gamepad1.right_stick_x * -0.66,
                        gamepad1.right_stick_x * 0.66, gamepad1.right_stick_x * 0.66);


            }
        } else {
            if ((gamepad1.left_stick_x) > 0.15) {
                drive(-gamepad1.left_stick_x, gamepad1.left_stick_x, gamepad1.left_stick_x, -gamepad1.left_stick_x);
            } else {
                drive((gamepad1.left_stick_y + -mecanumStrafe), (gamepad1.left_stick_y + mecanumStrafe), (gamepad1.left_stick_y + mecanumStrafe), (gamepad1.left_stick_y + -mecanumStrafe));
            }

            if ((gamepad1.right_stick_x) > 0.2) {
                drive(-gamepad1.right_stick_x, -gamepad1.right_stick_x, gamepad1.right_stick_x, gamepad1.right_stick_x );
            } else if (gamepad1.right_stick_x < -0.2) {
                drive(-gamepad1.right_stick_x, -gamepad1.right_stick_x, gamepad1.right_stick_x, gamepad1.right_stick_x);


            }
        }
        telemetry.addLine("DRIVE POV");




        if (gamepad2.right_stick_y > 0.1){
            leftArmMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightArmMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            rightArmMotor.setPower(-gamepad2.right_stick_y);//arm slow
            leftArmMotor.setPower(-gamepad2.right_stick_y);
        } else if (gamepad2.right_stick_y < -0.1) {
            leftArmMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightArmMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightArmMotor.setPower(-gamepad2.right_stick_y);
            leftArmMotor.setPower(-gamepad2.right_stick_y);

        } else {
            rightArmMotor.setPower(0);
            leftArmMotor.setPower(0);
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
            lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            setBehavior(lift, BRAKE);

            // bucket.setPosition(0.15);//Can only use these values nothing beyond this point
            lift.setPower(0.30);
        } else if (gamepad2.left_bumper){
            setBehavior(lift, BRAKE);

            lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            // bucket.setPosition(0.85);//Can only use these values nothing beyond this point
            lift.setPower(-0.30);
        }else {
            lift.setPower(0);
        }
        //telemetry.addLine("Servo " + bucket.getPosition());
        if (gamepad2.y) {
            droneLauncherServo.setPosition(1);
        }
        if (gamepad2.x ){
            droneLauncherServo.setPosition(0.5);
        }
        if (gamepad2.dpad_left){
            setBehavior(lift, BRAKE);

            armDrive(1,7);
            bucketDrive(0.4,180);

        } else if (gamepad2.dpad_right){
            setBehavior(lift, BRAKE);
            bucketDrive(0.9,0);

            armDrive(1,0);
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
            leftArmMotor.setTargetPosition(newTargetAngle);
            leftArmMotor.setPower(armSpeed);
            leftArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    public void armDrive (double armSpeed, double armInches){
        leftArmMotor.setZeroPowerBehavior(BRAKE);
        rightArmMotor.setZeroPowerBehavior(BRAKE);
        int arm1Target = (int)(armInches * CPI_ARM);
        int arm2Target = (int)(armInches * CPI_ARM);
        leftArmMotor.setTargetPosition(arm1Target);
        rightArmMotor.setTargetPosition(arm2Target);
        leftArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftArmMotor.setPower(armSpeed);
        rightArmMotor.setPower(armSpeed);
        while(!IsInRange(leftArmMotor.getCurrentPosition(), leftArmMotor.getTargetPosition()) || !IsInRange(rightArmMotor.getCurrentPosition(), rightArmMotor.getTargetPosition())){
            telemetry.addData("T-Arm1: ", leftArmMotor.getTargetPosition());
            telemetry.addData("A-Arm1: ", leftArmMotor.getCurrentPosition());
            telemetry.addData("T-Arm2", rightArmMotor.getTargetPosition());
            telemetry.addData("A-Arm2: ", rightArmMotor.getCurrentPosition());
            telemetry.update();
        }
        leftArmMotor.setPower(0);
        rightArmMotor.setPower(0);
    }
    public void bucketDrive(double speed, double targetDegrees) {
        int liftTarget;
        lift.setZeroPowerBehavior(BRAKE);
        if (targetDegrees != 0) {
            liftTarget = (int) (targetDegrees * (700 / 360));
        } else {
            liftTarget = 0;
        }
        lift.setTargetPosition(liftTarget);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lift.setPower(speed);
        while (!IsInRange(lift.getCurrentPosition(), lift.getTargetPosition())) {
            telemetry.addData("T-Bucket: ", lift.getTargetPosition());
            telemetry.addData("A-Bucket: ", lift.getCurrentPosition());
            telemetry.update();
        }
        lift.setPower(0);
    }
    public boolean IsInRange(double inches, double target){
        final float DEAD_RANGE = 10;
        if(Math.abs(target - inches) <= DEAD_RANGE){
            return true;
        }
        return false;
    }
    }