package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import java.lang.annotation.Target;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.configuration.WebcamConfiguration;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;
@Autonomous(name = "Blue Car Warehouse")
public class AUTO extends LinearOpMode {
    // Declare OpMode members.
    //Tages

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor motorLeftBACK = null;
    private DcMotor motorRightBACK = null;
    private DcMotor motorLeftFRONT = null;
    private DcMotor motorRightFRONT = null;
    private DcMotor intakeLeft = null;
    int hubLevel = 1;
    //OpenCvRed cvRed;
    //OpenCVBLUE CVBlue;
    //private CRServo servoLeft, servoRight;
    private double CPI_ATV_DT;
    final private double CPI_MECC_DT = 537.7/ ( 5 * Math.PI);
    // private Servo servomain;
    //private boolean buttonG2APressest = false;
    //private boolean buttonG2XPressedLast = false;
    private ElapsedTime timer;

    private final int CPR_ODOMETRY = 8192;//counts per revolution for encoder, from website
    private final int hexCoreCPR = 288;
    private final int ODOMETRY_WHEEL_DIAMETER = 4;
    private double CPI_DRIVE_TRAIN, CPI_CORE_HEX, CPI_GOBILDA26TO1;
    private double cpiOdometry;
    private double leftPos = 0;
    private double rightPos = 0;
    private int timeOutCount = 0;
    // private VoltageSensor vs;
    private double gameTimeSnapShot = 0;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        cpiOdometry  = CPR_ODOMETRY / (ODOMETRY_WHEEL_DIAMETER * Math.PI);
        //CPI =     ticksPerRev / (circumerence);
        //CPI_CORE_HEX = hexCoreCPR/4.4;
        CPI_ATV_DT = 537.7/ ( 5 * Math.PI);
        CPI_GOBILDA26TO1 = 180.81*2.6/2.5;//gear ratio adjustment
        motorLeftBACK = hardwareMap.dcMotor.get("leftRear");
        motorRightBACK = hardwareMap.dcMotor.get("rightRear");
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


        //run autonomous
        if (opModeIsActive()) {
            intakeLeft.setPower(-1);
            encoderDrive(.1, .1 , 45, 45);






        }
    }

    public void encoferDrive(double leftSpeed, double rightSpeed, double leftInches, double rightInches){
        motorSetModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorSetModes(DcMotor.RunMode.RUN_USING_ENCODER);

        int frontRightTarget = motorRightFRONT.getCurrentPosition() + (int) (CPI_MECC_DT * rightInches);
        int backRightTarget = motorRightBACK.getCurrentPosition() + (int) (CPI_MECC_DT * rightInches);
        int frontLeftTarget = motorLeftFRONT.getCurrentPosition() +  (int) (CPI_MECC_DT * leftInches);
        int backLeftTarget = motorLeftBACK.getCurrentPosition() + (int) (CPI_MECC_DT * leftInches);
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
}