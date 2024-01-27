package org.firstinspires.ftc.teamcode.opmodes;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class RedBoredCorner extends LinearOpMode {
    // Declare OpMode members.
    //Tages
    REDSIDEBLUDSWERULETHISTOWN openCv = new REDSIDEBLUDSWERULETHISTOWN();
    ElapsedTime t = new ElapsedTime();
    private ElapsedTime runtime = new ElapsedTime();
    DcMotor motorLeftFRONT, motorLeftBACK, motorRightFRONT, motorRightBACK, arm1, arm2, lift, intakeLeft;
    private Servo gun;
    private Servo bucket;


    final private double CPCM_MECC = 537.6/ ( 3.75 * Math.PI);
    final private double CPI_ARM = 537.6/(4.4);
    private ElapsedTime timer;

    private final int CPR_ODOMETRY = 8192;//counts per revolution for encoder, from website
    private final int ODOMETRY_WHEEL_DIAMETER = 4;
    private double cpiOdometry;
    private double leftPos = 0;
    private double rightPos = 0;
    private int timeOutCount = 0;
    enum park {
        Right,
        Middle,
        Left
    }
    park parkpos = park.Left;
    @Override
    public void runOpMode() {
        //telemetry.addData("T-FrontLeft: ", frontLeftTarget);

        cpiOdometry  = CPR_ODOMETRY / (ODOMETRY_WHEEL_DIAMETER * Math.PI);
        //CPI =     ticksPerRev / (circumerence);
        motorLeftBACK = hardwareMap.dcMotor.get("leftBack");
        motorRightBACK = hardwareMap.dcMotor.get("rightBack");
        motorLeftFRONT = hardwareMap.dcMotor.get( "leftFront");
        motorRightFRONT = hardwareMap.dcMotor.get("rightFront");
        intakeLeft = hardwareMap.dcMotor.get("intakeLeft");
        arm1 = hardwareMap.dcMotor.get("armLeft");
        arm2 = hardwareMap.dcMotor.get("armRight");
        //bucket = hardwareMap.servo.get("bucket");
        lift = hardwareMap.dcMotor.get("Lift");
        intakeLeft = hardwareMap.dcMotor.get("intakeLeft");
        gun = hardwareMap.servo.get("gun");
        //servoRight = hardwareMap.crservo.get("servo_0");
        //servoLeft = hardwareMap.crservo.get("servo_1");
        // vs  = this.hardwareMap.voltageSensor.iterator().next();
        timer = new ElapsedTime();//create a timer from the elapsed time class

        openCv.OpenCv(hardwareMap, telemetry);
        if (isStopRequested()) return;

        //arm1.setDirection(DcMotorSimple.Direction.REVERSE);
        brakeMotors();
        reverseMotors();
        telemetry.update();
        waitForStart();
        runtime.reset();

        int snapshotAnalysis = openCv.analysis();
        telemetry.addData("Snapshot post-START analysis : ", snapshotAnalysis);
        telemetry.addData("MAX : ", openCv.getMax());
        telemetry.update();
        //sleep(10000);
        switch (snapshotAnalysis) {
            case 0://BLUE
            {
                parkpos = park.Right;
                break;
            }
            case 1://YELLOW
            {
                parkpos = park.Middle;
                break;
            }
            default://RED
            {
                parkpos = park.Left;
                break;
            }
        }

        //run autonomous
        if (opModeIsActive()) {

            switch (parkpos){
                case Left: {
                    encoferDrive(0.4,0.4,26,26,false);

                    encoferDrive(0.4,0.4,-23.71,23.71,false);
                    encoferDrive(0.4,0.4,1,1,false);

                    intakeLeft.setPower(-0.4);
                    encoferDrive(0.4,0.4,-30,-30,true);
                    intakeLeft.setPower(0);

                    break;
                }
                case Middle: {
                    encoferDrive(0.4,0.4,27,27,false);
                    sleep(400);
                    intakeLeft.setPower(-0.4);

                    encoferDrive(0.4,0.4,-6,-6,false);
                    intakeLeft.setPower(0);

                    encoferDrive(0.4,0.4,-23.71,23.71,false);
                    encoferDrive(0.4,0.4,-24,-24,true);
                    break;

                } default:{
                    encoferDrive(0.4,0.4,26,26,false);

                    encoferDrive(0.4,0.4,23.71,-23.71,false);
                    sleep(200);
                    intakeLeft.setPower(-0.4);
                    sleep(200);
                    encoferDrive(0.4,0.4,-5,-5,false);
                    intakeLeft.setPower(0);


                    encoferDrive(0.4,0.4,-23.71,23.71,false);
                    encoferDrive(0.4,0.4,-23.71,23.71,false);
                    encoferDrive(0.4,0.4,-5,-5,false);
                    encoferDrive(0.4,0.4,-30,-30,true);
                    break;
                }
            }
            //encoferDrive(0.4,0.4,-2,-2,false);

            sleep(200);
            intakeLeft.setPower(0);
            encoferDrive(0.4,0.4,-79,-79,false);
            intakeLeft.setPower(-0.4);
            encoferDrive(0.4,0.4,22,22,true);
            intakeLeft.setPower(0);

            armDrive(0.5, 18);
            switch (parkpos){
                case Right:{//Middle
                    encoferDrive(0.4,0.4,-4,-4,true);
                    break;
                }
                case Middle:{ //Right
                    break;
                }
                default:{ //Left
                    encoferDrive(0.4,0.4,4,4,true);
                    break;
                }
            }


            bucketDrive(0.3, 370);
            sleep(500);
            bucketDrive(0.2, -350);

            armDrive(0.5, -13);
            encoferDrive(0.4,0.4,-5,-5,false);

            switch (parkpos) {
                case Right: {//Middle
                    encoferDrive(0.4,0.4,-19,-19,true);
                    break;
                }
                case Middle: { //Right
                    encoferDrive(0.4,0.4,-24,-24,true);
                    break;
                }
                default: { //Left
                    encoferDrive(0.4,0.4,-29,-29,true);
                    break;
                }
            }
            encoferDrive(0.4,0.4,-9,-9,false);
            //encoferDrive(0.4,0.4,-35,-35,true);

            //armDrive(0.4,50);

        }


    }

    public void encoferDrive(double leftSpeed, double rightSpeed, double left, double right, boolean edge){
        brakeMotors();
        motorSetModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int frontLeftTarget=0,frontRightTarget=0,backRightTarget=0,backLeftTarget=0;
        if (edge) {
            frontRightTarget = motorRightFRONT.getCurrentPosition() + (int) (CPCM_MECC * -right);
            backRightTarget = motorRightBACK.getCurrentPosition() + (int) (CPCM_MECC * right);
            frontLeftTarget = motorLeftFRONT.getCurrentPosition() + (int) (CPCM_MECC * left);
            backLeftTarget = motorLeftBACK.getCurrentPosition() + (int) (CPCM_MECC * -left);
        } else {
            frontRightTarget = motorRightFRONT.getCurrentPosition() + (int) (CPCM_MECC * right);
            backRightTarget = motorRightBACK.getCurrentPosition() + (int) (CPCM_MECC * right);
            frontLeftTarget = motorLeftFRONT.getCurrentPosition() + (int) (CPCM_MECC * left);
            backLeftTarget = motorLeftBACK.getCurrentPosition() + (int) (CPCM_MECC * left);
        }
        motorRightFRONT.setTargetPosition(frontRightTarget);
        motorRightBACK.setTargetPosition(backRightTarget);
        motorLeftFRONT.setTargetPosition(frontLeftTarget);
        motorLeftBACK.setTargetPosition(backLeftTarget);
        motorSetModes(DcMotor.RunMode.RUN_TO_POSITION);
        brakeMotors();
        drive(leftSpeed,rightSpeed, edge);
        while (!IsInRange(motorLeftBACK.getCurrentPosition(), backLeftTarget) || !IsInRange(motorRightBACK.getCurrentPosition(), backRightTarget)
                || !IsInRange(motorLeftFRONT.getCurrentPosition(), frontLeftTarget) || !IsInRange(motorRightFRONT.getCurrentPosition(), frontRightTarget)){
            telemetry.addData("T-FrontLeft: ", frontLeftTarget);
            telemetry.addData("A-FrontLeft: ", motorLeftFRONT.getCurrentPosition());
            telemetry.addData("T-FrontRight: ", frontRightTarget);
            telemetry.addData("A-FrontRight: ", motorRightFRONT.getCurrentPosition());
            telemetry.addData("T-BackLeft: ", backLeftTarget);
            telemetry.addData("A-BackLeft: ", motorLeftBACK.getCurrentPosition());
            telemetry.addData("T-BackRight: ", backRightTarget);
            telemetry.addData("A-BackRight: ", motorRightBACK.getCurrentPosition());
            telemetry.update();

        }
        drive(0, 0, edge);
    }
    public void armDrive (double armSpeed, double armInches){
        arm1.setZeroPowerBehavior(BRAKE);
        arm2.setZeroPowerBehavior(BRAKE);
        arm1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int arm1Target = arm1.getCurrentPosition() + (int)(armInches * CPI_ARM);
        int arm2Target = arm2.getCurrentPosition() + (int)(armInches * CPI_ARM);
        arm1.setTargetPosition(arm1Target);
        arm2.setTargetPosition(arm2Target);
        arm1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        arm1.setPower(armSpeed);
        arm2.setPower(armSpeed);
        while(!IsInRange(arm1.getCurrentPosition(), arm1.getTargetPosition()) || !IsInRange(arm2.getCurrentPosition(), arm2.getTargetPosition())){
            telemetry.addData("T-Arm1: ", arm1.getTargetPosition());
            telemetry.addData("A-Arm1: ", arm1.getCurrentPosition());
            telemetry.addData("T-Arm2", arm2.getTargetPosition());
            telemetry.addData("A-Arm2: ", arm2.getCurrentPosition());
            telemetry.update();
        }
        arm1.setPower(0);
        arm2.setPower(0);
    }
    public void bucketDrive(double speed, double targetDegrees){
        lift.setZeroPowerBehavior(BRAKE);
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        int liftTarget = lift.getCurrentPosition() + (int) (targetDegrees * (700/360));
        lift.setTargetPosition(liftTarget);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lift.setPower(speed);
        while(!IsInRange(lift.getCurrentPosition(), lift.getTargetPosition())){
            telemetry.addData("T-Bucket: ", lift.getTargetPosition());
            telemetry.addData("A-Bucket: ", lift.getCurrentPosition());
            telemetry.update();
        }
        lift.setPower(0);
    }
/*
    public void encoderDrive(double leftDTSpeed, double rightDTSpeed, double mtrLeftInches, double mtrRightInches) {
        motorSetModes(DcMotor.RunMode.RUN_USING_ENCODER);
        int newLeftTarget = motorLeftBACK.getCurrentPosition() + (int) (CPI_ATV_DT * mtrLeftInches);
        int newRightTarget = motorRightBACK.getCurrentPosition() + (int) (CPI_ATV_DT * mtrRightInches);
        drive(mtrLeftInches < 0 ? -leftDTSpeed : leftDTSpeed, mtrRightInches < 0 ? -rightDTSpeed : rightDTSpeed);
        while (opModeIsActive() && !IsInRange(motorLeftBACK.getCurrentPosition(), newLeftTarget)
                && !IsInRange(motorRightBACK.getCurrentPosition(), newRightTarget)) {
            telemetry.addData("Target Left: ", newLeftTarget);
            telemetry.addData("Target right: ", newRightTarget);
            telemetry.addData("Current Pos Left:", motorLeftBACK.getCurrentPosition());
            telemetry.addData("Current Pos Right:", motorRightBACK.getCurrentPosition());
            telemetry.addData("right power: ", motorRightBACK.getPower());
            telemetry.addData("left power: ", motorLeftBACK.getPower());

            telemetry.update();
        }
        // Stop all motion;
        drive(0, 0);
    }
*/

    public void motorSetModes(DcMotor.RunMode modeName) {
        motorLeftBACK.setMode(modeName);
        motorRightBACK.setMode(modeName);
        motorLeftFRONT.setMode(modeName);
        motorRightFRONT.setMode(modeName);
    }

    public void drive(double left, double right, boolean strafe) {
        if (strafe){
            motorLeftBACK.setPower(-left);
            motorRightBACK.setPower(right);
            motorRightFRONT.setPower(-right);
            motorLeftFRONT.setPower(left);
        } else {
            motorLeftBACK.setPower(left);
            motorRightBACK.setPower(right);
            motorRightFRONT.setPower(right);
            motorLeftFRONT.setPower(left);
        }
    }

    public void motorSetTargetPos(int targetLeft, int targetRight) {
        motorLeftBACK.setTargetPosition(targetLeft);
        motorRightBACK.setTargetPosition(targetRight);
    }

    public boolean IsInRange(double inches, double target){
        final float DEAD_RANGE = 10;
        if(Math.abs(target - inches) <= DEAD_RANGE){
            return true;
        }
        return false;
    }





    private void reverseMotors(){
        motorLeftBACK.setDirection(DcMotor.Direction.REVERSE);
        motorLeftFRONT.setDirection(DcMotor.Direction.REVERSE);
        arm1.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    private void brakeMotors(){
        motorLeftBACK.setZeroPowerBehavior(BRAKE);
        motorRightBACK.setZeroPowerBehavior(BRAKE);
        motorLeftFRONT.setZeroPowerBehavior(BRAKE);
        motorRightFRONT.setZeroPowerBehavior(BRAKE);



    }
}