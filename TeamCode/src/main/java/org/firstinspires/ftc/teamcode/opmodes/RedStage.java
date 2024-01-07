package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "RedStage")
public class RedStage extends LinearOpMode {
    // Declare OpMode members.
    //Tages
    MONKERYSEEMONRYDOO openCv;

    ElapsedTime t = new ElapsedTime();
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor motorLeftBACK = null;
    private DcMotor motorRightBACK = null;
    private DcMotor motorLeftFRONT = null;
    private DcMotor motorRightFRONT = null;
    private DcMotor intakeLeft = null;
    final private double CPCM_MECC = 537.6/ ( 10 * Math.PI);
    private ElapsedTime timer;

    private final int CPR_ODOMETRY = 8192;//counts per revolution for encoder, from website
    private final int ODOMETRY_WHEEL_DIAMETER = 4;
    private double cpiOdometry;
    private double leftPos = 0;
    private double rightPos = 0;
    private int timeOutCount = 0;
    // private VoltageSensor vs;
    private double gameTimeSnapShot = 0;
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
        //CPI_CORE_HEX = hexCoreCPR/4.4;
        motorLeftBACK = hardwareMap.dcMotor.get("leftBack");
        motorRightBACK = hardwareMap.dcMotor.get("rightBack");
        motorLeftFRONT = hardwareMap.dcMotor.get( "leftFront");
        motorRightFRONT = hardwareMap.dcMotor.get("rightFront");
        intakeLeft = hardwareMap.dcMotor.get("intakeLeft");
        //servoRight = hardwareMap.crservo.get("servo_0");
        //servoLeft = hardwareMap.crservo.get("servo_1");
        // vs  = this.hardwareMap.voltageSensor.iterator().next();
        timer = new ElapsedTime();//create a timer from the elapsed time class


        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery



        brakeMotors();
        reverseMotors();
        telemetry.update();
        waitForStart();
        runtime.reset();

        openCv = new MONKERYSEEMONRYDOO();
        openCv.OpenCv(hardwareMap, telemetry);

        while (!isStarted() && !isStopRequested())
        {
            telemetry.addData("Realtime analysis : ", openCv.pipeline.getAnalysis());
            telemetry.update();
            sleep(10);
        }
        int snapshotAnalysis = openCv.analysis();

        telemetry.addData("Snapshot post-START analysis : ", snapshotAnalysis);
        telemetry.update();

        if (isStopRequested()) return;

        switch (snapshotAnalysis)
        {
            case 1://BLUE
            {
                parkpos = park.Right;
                break;
            }
            case 2://YELLOW
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
        while (opModeIsActive()) {
            if (t.seconds() < 3 && t.seconds() > 2.5) {
                drive(-0.2, -0.2,true);
            }
            drive(0,0);
            if (t.seconds() > 3.5 && t.seconds() < 6) {
                drive(0.3, 0.3,false);
                intakeLeft.setPower(-1);
            }
            drive(0,0);
            intakeLeft.setPower(0);
        }

    }

    public void encoferDrive(double leftSpeed, double rightSpeed, double leftCM, double rightCM){
        motorSetModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorSetModes(DcMotor.RunMode.RUN_USING_ENCODER);

        int frontRightTarget = motorRightFRONT.getCurrentPosition() + (int) (CPCM_MECC * rightCM);
        int backRightTarget = motorRightBACK.getCurrentPosition() + (int) (CPCM_MECC * rightCM);
        int frontLeftTarget = motorLeftFRONT.getCurrentPosition() +  (int) (CPCM_MECC * leftCM);
        int backLeftTarget = motorLeftBACK.getCurrentPosition() + (int) (CPCM_MECC * leftCM);
        motorRightFRONT.setTargetPosition(frontRightTarget);
        motorRightBACK.setTargetPosition(backRightTarget);
        motorLeftFRONT.setTargetPosition(frontLeftTarget);
        motorLeftBACK.setTargetPosition(backLeftTarget);
        drive(leftSpeed,rightSpeed);
        if (opModeIsActive() && !IsInRange(motorLeftBACK.getCurrentPosition(), backLeftTarget) && !IsInRange(motorRightBACK.getCurrentPosition(), backRightTarget)
                && !IsInRange(motorLeftFRONT.getCurrentPosition(), frontLeftTarget) && !IsInRange(motorRightFRONT.getCurrentPosition(), frontRightTarget)){
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
        drive(0, 0);
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

    public void drive(double left, double right  ) {
        motorLeftBACK.setPower(left);
        motorRightBACK.setPower(right);
        motorRightFRONT.setPower(right);
        motorLeftFRONT.setPower(left);
    }

    public void motorSetTargetPos(int targetLeft, int targetRight) {
        motorLeftBACK.setTargetPosition(targetLeft);
        motorRightBACK.setTargetPosition(targetRight);
    }

    public boolean IsInRange(double inches, double target){
        final float DEAD_RANGE = 20;
        if(Math.abs(target - inches) <= DEAD_RANGE){
            return true;
        }
        return false;
    }





    private void reverseMotors(){
        motorLeftBACK.setDirection(DcMotor.Direction.REVERSE);
        motorLeftFRONT.setDirection(DcMotor.Direction.REVERSE);

    }

    private void brakeMotors(){
        motorLeftBACK.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRightBACK.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLeftFRONT.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRightFRONT.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    public void drive(double left, double right, boolean strafe) {

        if(strafe){
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
}