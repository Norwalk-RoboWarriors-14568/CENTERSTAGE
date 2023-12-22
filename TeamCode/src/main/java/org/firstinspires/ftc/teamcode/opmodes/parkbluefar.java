package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

//@Autonomous(name = "parkfarfar")
public class parkbluefar extends LinearOpMode {
    // Declare OpMode members.
    //Tages

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor motorLeftBACK = null;
    private DcMotor motorRightBACK = null;
    private DcMotor motorLeftFRONT = null;
    private DcMotor motorRightFRONT = null;
    private DcMotor intakeLeft = null;
    final private double CPI_MECC = (537.6/ ( 3.93 * Math.PI));
    private ElapsedTime timer;

    private final int CPR_ODOMETRY = 8192;//counts per revolution for encoder, from website
    private final int ODOMETRY_WHEEL_DIAMETER = 4;
    private double cpiOdometry;
    private double leftPos = 0;
    private double rightPos = 0;
    private int timeOutCount = 0;
    // private VoltageSensor vs;
    private double gameTimeSnapShot = 0;

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
        motorSetModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        waitForStart();
        runtime.reset();


        //run autonomous
        if (opModeIsActive()) {

            encoferDrive(0.5,0.5,-30, 30, false);
            encoferDrive(0.5,0.5,-18, -18, false);
            encoferDrive(0.5,0.5,-80, 80, false);

            while (opModeIsActive()) {
                //  telemetry.addData("T-FrontLeft: ", frontLeftTarget);
                telemetry.addData("A-FrontLeft: ", motorLeftFRONT.getCurrentPosition());
                //   telemetry.addData("T-FrontRight: ", frontRightTarget);
                telemetry.addData("A-FrontRight: ", motorRightFRONT.getCurrentPosition());
                //   telemetry.addData("T-BackLeft: ", backLeftTarget);
                telemetry.addData("A-BackLeft: ", motorLeftBACK.getCurrentPosition());
                //  telemetry.addData("T-BackRight: ", backRightTarget);
                telemetry.addData("A-BackRight: ", motorRightBACK.getCurrentPosition());
                telemetry.update();
            }
        }

    }

    public void encoferDrive(double leftSpeed, double rightSpeed, double leftInches, double rightInches, boolean strafeywafey){
        motorSetModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorSetModes(DcMotor.RunMode.RUN_USING_ENCODER);

        int frontRightTarget = motorRightFRONT.getCurrentPosition() + (int) (CPI_MECC * rightInches);
        int backRightTarget = motorRightBACK.getCurrentPosition() + (int) (CPI_MECC * rightInches);
        int frontLeftTarget = motorLeftFRONT.getCurrentPosition() +  (int) (CPI_MECC * leftInches);
        int backLeftTarget = motorLeftBACK.getCurrentPosition() + (int) (CPI_MECC * leftInches);
        motorRightFRONT.setTargetPosition(frontRightTarget);
        motorRightBACK.setTargetPosition(backRightTarget);
        motorLeftFRONT.setTargetPosition(frontLeftTarget);
        motorLeftBACK.setTargetPosition(backLeftTarget);
        drive(leftSpeed,rightSpeed, strafeywafey);
        while (opModeIsActive() && !IsInRange(motorLeftBACK.getCurrentPosition(), backLeftTarget) && !IsInRange(motorRightBACK.getCurrentPosition(), backRightTarget)
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
        drive(0, 0, false);
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
}