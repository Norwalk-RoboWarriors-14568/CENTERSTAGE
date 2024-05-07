package org.firstinspires.ftc.teamcode.opmodes;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;
import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "ESCAPETHEMATRIX \uD83E\uDD2B \uD83E\uDDCF")
public class TeleOpZTD extends OpMode {
    DcMotor fl, bl, fr, br, leftArmMotor, rightArmMotor, lift, intakeLeft;
    private Servo gun;
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
        gun = hardwareMap.servo.get("gun");


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
        leftArmMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightArmMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftArmMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightArmMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        float armTicksZero = leftArmMotor.getCurrentPosition();//sets the floor position to 0
        // bucket.setPosition(0);
        //bucket.setBehavior();
    }

    @Override
    public void loop(){
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
            if (gamepad1.right_bumper) {
                drive(gamepad1.left_stick_y * 0.8, gamepad1.right_stick_y * 0.8);
            } else if (gamepad1.left_bumper) {
                drive(gamepad1.left_stick_y * 0.25, gamepad1.right_stick_y * 0.25);
            } else {
                drive(gamepad1.left_stick_y * 0.5, gamepad1.right_stick_y * 0.5);
            }
        }
        telemetry.addLine("DRIVE TANK");
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

            // bucket.setPosition(0.15);//Can only use these values nothing beyond this point
            lift.setPower(0.30);
        } else if (gamepad2.left_bumper){
            lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

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
        if (gamepad2.dpad_left){
            setBehavior(lift, BRAKE);

            armDrive(1,7);
            bucketDrive(0.4,180);

        } else if (gamepad2.dpad_right){
            setBehavior(lift, BRAKE);
            bucketDrive(0.9,0);

            armDrive(1,0);
        }
    }
    @Override
    public void stop() {
        telemetry.clearAll();
        telemetry.addLine("Stopped");
    }
        public void drive(double left,  double right) {
            fl.setPower(left);
            bl.setPower(left);
            fr.setPower(right);
            br.setPower(right);

        }
        public void setBehavior(DcMotor motor, DcMotor.ZeroPowerBehavior Behavior){
            motor.setZeroPowerBehavior(Behavior);
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