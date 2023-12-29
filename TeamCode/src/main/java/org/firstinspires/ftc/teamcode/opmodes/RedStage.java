package org.firstinspires.ftc.teamcode.opmodes;


import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.MecanumDrive;
@Autonomous(group = "RedStage")
public class RedStage extends LinearOpMode{

    MecanumDrive drive;
    // MONKERYSEEMONRYDOO openCv;
    private DcMotor armLeft, armRight, intakeLeft;
    final double pulleyDiameter = 0;
    ElapsedTime waitTimer = new ElapsedTime();
    ElapsedTime matchTimer = new ElapsedTime();

    /*        enum State {
                START,
                FIRST_JUNCTION,
                WAIT_1,
                STACK,
                WAIT_2,
                POLE_RETURN,
                DROP_CONE,
                IDLE,
                SMALL_POLE,
                PARK
            }
            State currentState = State.START; */
    Pose2d startPose = new Pose2d(0, 0, 0);

    Pose2d Middle = new Pose2d(50,-25, Math.toRadians(90));
    Pose2d Left = new Pose2d(52,-3,Math.toRadians(90));
    Pose2d Right = new Pose2d(52,28,Math.toRadians(180));
    Pose2d park;

    @Override
    public void runOpMode() throws InterruptedException {

        drive = new MecanumDrive(hardwareMap, startPose);
        armLeft = hardwareMap.dcMotor.get("armLeft");
        armRight = hardwareMap.dcMotor.get("armRight");
        intakeLeft = hardwareMap.dcMotor.get("intakeLeft");
        motorSetModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorSetModes(DcMotor.RunMode.RUN_USING_ENCODER);

        // openCv = new MONKERYSEEMONRYDOO();
        // openCv.OpenCv(hardwareMap, telemetry);
/*
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
                    park = Right;
                    break;
                }
                case 2://YELLOW
                {
                    park = Middle;
                    break;
                }
                default://RED
                {
                    park = Left;
                    break;
                }
            }

*/

        Action BlueFront = drive.actionBuilder(new Pose2d(-36.33, 61.08, Math.toRadians(-89)))
                .lineToX(-36.18).lineToY(47.98)
                .lineToXSplineHeading(-36.33,Math.toRadians(180.00)).lineToYSplineHeading(36.18, Math.toRadians(180.00))
                .lineToX(10.99).lineToY(35.89)
                .lineToX(46.96).lineToY(35.75)
                .afterTime(-0.1,raise(1,20))
                .afterTime(0,(telemetryPacket) -> { // Run some action
                    armLeft.setPower(0);
                    return false;
                })
                .afterTime(0,(telemetryPacket) -> { // Run some action
                    armRight.setPower(0);
                    return false;
                })
                .build();

        /*
        Action BlueBored = drive.actionBuilder(new Pose2d(11.43, 59.77, Math.toRadians(-90.00)))
                .splineTo(new Vector2d(49.14, 36.33), Math.toRadians(-0.20))
                //.afterTime(-0.1,usARMy(1,20))
                .lineToXConstantHeading(49.29).lineToYConstantHeading( 61.23)
                .build();
        Action RedFront = drive.actionBuilder(new Pose2d(-36.33, -61.08, Math.toRadians(90.00)))
                .lineToX(-36.18).lineToY(-47.98)
                .lineToXSplineHeading(36.33,Math.toRadians(180.00)).lineToYSplineHeading(-36.18, Math.toRadians(-180.00))
                .lineToX(10.99).lineToY(-35.89)
                .lineToX(46.96).lineToY(-35.75)
                //.afterTime(-0.1,usARMy(1,20))
                .build();
        Action RedBored = drive.actionBuilder(new Pose2d(11.43, -59.77, Math.toRadians(90.00)))
                .splineTo(new Vector2d(49.14, -36.33), Math.toRadians(0.20))
                //.afterTime(-0.1,usARMy(1,20))
                .lineToXConstantHeading(49.29).lineToYConstantHeading( -61.23)
                .build();

*/
        while(!isStopRequested() && !opModeIsActive()) {
        }

        waitForStart();

        if (isStopRequested()) return;

        //
        Actions.runBlocking(new SequentialAction(
                BlueFront,
                (telemetryPacket) -> {
                    telemetry.addLine("Action!");
                    return false; // Returning true causes the action to run again, returning false causes it to cease
                })
        );
    }

    public void driveArm(double speed){
        armLeft.setPower(speed);
        armRight.setPower(speed);
    }
    public boolean IsInRange(double inches, double target){
        final float DEAD_RANGE = 20;
        if(Math.abs(target - inches) <= DEAD_RANGE){
            return true;
        }
        return false;
    }
    public void motorSetModes(DcMotor.RunMode modeName) {
        armLeft.setMode(modeName);
        armRight.setMode(modeName);
    }
    public Action raise(double armSpeed, double armDistance) {
        return new Action() {
            private boolean initialized = false;
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (!initialized) {
                    driveArm(armSpeed);
                    initialized = true;
                }
                int leftArmTarget = armLeft.getCurrentPosition() + (int) (pulleyDiameter * armDistance);
                int rightArmTarget = armRight.getCurrentPosition() + (int) (pulleyDiameter * armDistance);
                armLeft.setTargetPosition(leftArmTarget);
                armRight.setTargetPosition(rightArmTarget);
                packet.put("Position Left tar", leftArmTarget);
                packet.put("Position right tar", rightArmTarget);
                packet.put("Position Left", armLeft.getCurrentPosition());
                packet.put("Position right", armRight.getCurrentPosition());

                return !IsInRange(armLeft.getCurrentPosition(), leftArmTarget) && !IsInRange(armRight.getCurrentPosition(), rightArmTarget);
            }
        };
    }


    /*
   public void usARMy(double armSpeed, double armDistance){
       motorSetModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
       motorSetModes(DcMotor.RunMode.RUN_USING_ENCODER);

       int leftArmTarget = armLeft.getCurrentPosition() + (int) (pulleyDiameter * armDistance);
       int rightArmTarget = armRight.getCurrentPosition() + (int) (pulleyDiameter * armDistance);

       armLeft.setTargetPosition(leftArmTarget);
       armRight.setTargetPosition(rightArmTarget);
       driveArm(armSpeed);
       while (opModeIsActive() && !IsInRange(armLeft.getCurrentPosition(), leftArmTarget) && !IsInRange(armRight.getCurrentPosition(), rightArmTarget)){
           telemetry.addData("T-ArmLeft: ", armLeft.getTargetPosition());
           telemetry.addData("A-ArmLeft: ", armLeft.getCurrentPosition());
           telemetry.addData("T-ArmRight: ", armRight.getTargetPosition());
           telemetry.addData("A-ArmRight: ", armRight.getTargetPosition());
           telemetry.update();
       }
       driveArm(0);

   } */
}
