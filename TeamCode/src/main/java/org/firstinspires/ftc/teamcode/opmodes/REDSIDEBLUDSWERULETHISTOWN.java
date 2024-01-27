package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;


public class REDSIDEBLUDSWERULETHISTOWN
{
    OpenCvWebcam webcam;
    SamplePipeline pipeline;
    static Telemetry telemetry;

    public void OpenCv(HardwareMap hardwareMap, Telemetry telemetryIn)
    {
        telemetry = telemetryIn;

        /*
         * Instantiate an OpenCvCamera object for the camera we'll be using.
         * In this sample, we're using a webcam. Note that you will need to
         * make sure you have added the webcam to your configuration file and
         * adjusted the name here to match what you named it in said config file.
         *
         * We pass it the view that we wish to use for camera monitor (on
         * the RC phone). If no camera monitor is desired, use the alternate
         * single-parameter constructor instead (commented out below)
         */
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        //webcam = OpenCvCameraFactory.getInstance().createWebcam(webcam, cameraMonitorViewId);
        // OR...  Do Not Activate the Camera Monitor View
        //webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"));

        /*
         * Specify the image processing pipeline we wish to invoke upon receipt
         * of a frame from the camera. Note that switching pipelines on-the-fly
         * (while a streaming session is in flight) *IS* supported.
         */
        pipeline = new SamplePipeline();
        // pipeline.setColor(thisColor);
        webcam.setPipeline(pipeline);

        /*
         * Open the connection to the camera device. New in v1.4.0 is the ability
         * to open the camera asynchronously, and this is now the recommended way
         * to do it. The benefits of opening async include faster init time, and
         * better behavior when pressing stop during init (i.e. less of a chance
         * of tripping the stuck watchdog)
         *
         * If you really want to open synchronously, the old method is still available.
         */
        webcam.setMillisecondsPermissionTimeout(2500); // Timeout for obtaining permission is configurable. Set before opening.
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()

            {

                webcam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {
                telemetry.addData("Theoretical max FPS", webcam.getCurrentPipelineMaxFps());
                telemetry.update();

                /*
                 * This will be called if the camera could not be opened
                 */
            }
        });

        telemetry.addLine("Waiting for start");
        telemetry.update();

        /*
         * Wait for the user to press start on the Driver Station
         */
        //waitForStart();

        //while (opModeIsActive())
        //{

        //}

    }
    public void runWhileActive()
    {
        /*
         * Send some stats to the telemetry
         */

        telemetry.addData("Frame Count", webcam.getFrameCount());
        telemetry.addData("FPS", String.format("%.2f", webcam.getFps()));
        telemetry.addData("Total frame time ms", webcam.getTotalFrameTimeMs());
        telemetry.addData("Pipeline time ms", webcam.getPipelineTimeMs());
        telemetry.addData("Overhead time ms", webcam.getOverheadTimeMs());
        telemetry.addData("Theoretical max FPS", webcam.getCurrentPipelineMaxFps());
        //  telemetry.addData("Analysis", pipeline.getAnalysis());
        telemetry.addData("Max", pipeline.getMax());
        telemetry.update();
        //linearOpMode.sleep(50);

        /*
         * NOTE: stopping the stream from the camera early (before the end of the OpMode
         * when it will be automatically stopped for you) *IS* supported. The "if" statement
         * below will stop streaming from the camera when the "A" button on gamepad 1 is pressed.
         */
        //if(gamepad1.a)
        //{
        /*
         * IMPORTANT NOTE: calling stopStreaming() will indeed stop the stream of images
         * from the camera (and, by extension, stop calling your vision pipeline). HOWEVER,
         * if the reason you wish to stop the stream early is to switch use of the camera
         * over to, say, Vuforia or TFOD, you will also need to call closeCameraDevice()
         * (commented out below), because according to the Android Camera API documentation:
         *         "Your application should only have one Camera object active at a time for
         *          a particular hardware camera."
         *
         * NB: calling closeCameraDevice() will internally call stopStreaming() if applicable,
         * but it doesn't hurt to call it anyway, if for no other reason than clarity.
         *
         * NB2: if you are stopping the camera stream to simply save some processing power
         * (or battery power) for a short while when you do not need your vision pipeline,
         * it is recommended to NOT call closeCameraDevice() as you will then need to re-open
         * it the next time you wish to activate your vision pipeline, which can take a bit of
         * time. Of course, this comment is irrelevant in light of the use case described in
         * the above "important note".
         */
        //webcam.stopStreaming();
        //webcam.closeCameraDevice();
        //}


        /*
         * For the purposes of this sample, throttle ourselves to 10Hz loop to avoid burning
         * excess CPU cycles for no reason. (By default, telemetry is only sent to the DS at 4Hz
         * anyway). Of course in a real OpMode you will likely not want to do this.
         */
        //linearOpMode.sleep(100);
    }


    public int analysis()
    {
        return pipeline.getAnalysis().ordinal();
    }
    public int getMax()
    {
        return pipeline.getMax();
    }

    public static class SamplePipeline extends OpenCvPipeline
    {
        public enum SkystonePosition
        {
            RIGHT,
            MIDDLE,
            LEFT
        }
        static final Scalar LEFT = new Scalar(0, 0, 255);
        static final Scalar RIGHT = new Scalar(255, 255, 51);
        static final Scalar MIDDLE = new Scalar(255, 0, 0);
        private volatile SkystonePosition position = SkystonePosition.LEFT;
        int cNum =0;
        int cNum1 =1;
        int cNum2 =2;
        static int posNum = 590;
        String colorLEFT = "LEFT";
        String colorMIDDLE = "RIGHT";
        String colorMIDDLENeg = "MIDDLE";
        Mat region2_Cb, region3_Cb,region4_Cb;
        Mat YCrCb = new Mat();
        Mat Cr = new Mat();
        Mat Cb = new Mat();
        Mat outPut = new Mat();
        int avg2, avg3, avg4;
        static final int REGION_WIDTH = 100;
        static final int REGION_HEIGHT = 30;
        static final Point REGION1_TOPLEFT_ANCHOR_POINT = new Point(20,260);
        static final Point REGION2_TOPLEFT_ANCHOR_POINT = new Point(230,260);
        static final Point REGION3_TOPLEFT_ANCHOR_POINT = new Point(539,260);
        void inputToCb(Mat input)
        {
            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
            Core.extractChannel(YCrCb, Cr, cNum1);
        }
        void inputToCbInvert(Mat input)
        {
            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
            Core.extractChannel(YCrCb, Cb, cNum2);
            Core.bitwise_not(Cb, outPut);
        }
        void inputToCr(Mat input)
        {
            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
            Core.extractChannel(YCrCb, Cr, cNum1);
        }
        Point region1_pointA = new Point(
                REGION1_TOPLEFT_ANCHOR_POINT.x,
                REGION1_TOPLEFT_ANCHOR_POINT.y);
        Point region1_pointB = new Point(
                REGION1_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
                REGION1_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);
        Point region2_pointA = new Point(
                REGION2_TOPLEFT_ANCHOR_POINT.x,
                REGION2_TOPLEFT_ANCHOR_POINT.y);
        Point region2_pointB = new Point(
                REGION2_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
                REGION2_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);
        Point region3_pointA = new Point(
                REGION3_TOPLEFT_ANCHOR_POINT.x,
                REGION3_TOPLEFT_ANCHOR_POINT.y);
        Point region3_pointB = new Point(
                REGION3_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
                REGION3_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);
        @Override
        public void init(Mat firstFrame)
        {
            inputToCb(firstFrame);
            region2_Cb = Cr.submat(new Rect(region2_pointA, region2_pointB));
            //inputToCbInvert(firstFrame);
            region4_Cb = Cr.submat(new Rect(region1_pointA, region1_pointB));
            //inputToCr(firstFrame);
            region3_Cb = Cr.submat(new Rect(region3_pointA, region3_pointB));
        }
        @Override
        public Mat processFrame(Mat input)
        {
            inputToCb(input);
            avg2 = (int) Core.mean(region2_Cb).val[0];
            //inputToCbInvert(input);
            avg4 = (int) Core.mean(region4_Cb).val[0];
           // inputToCr(input);
            avg3 = (int) Core.mean(region3_Cb).val[0];
            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region1_pointA, // First point which defines the rectangle
                    region1_pointB, // Second point which defines the rectangle
                    RIGHT, // The color the rectangle is drawn in
                    1); // Thickness of the rectangle lines
            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region2_pointA, // First point which defines the rectangle
                    region2_pointB, // Second point which defines the rectangle
                    RIGHT, // The color the rectangle is drawn in
                    1); // Thickness of the rectangle lines
            Imgproc.rectangle(
                    input, // Buffer to draw on
                    region3_pointA, // First point which defines the rectangle
                    region3_pointB, // Second point which defines the rectangle
                    RIGHT, // The color the rectangle is drawn in
                    1); // Thickness of the rectangle lines


            int maxTwoThree = Math.max(avg3, avg2);
            int max = Math.max(maxTwoThree, avg4);

            if (max <= 133){
                position = SkystonePosition.LEFT; // Record our analysis
                Imgproc.rectangle(
                        input, // Buffer to draw on
                        region1_pointA, // First point which defines the rectangle
                        region1_pointB, // Second point which defines the rectangle
                        RIGHT, // The color the rectangle is drawn in
                        -1); // Negative thickness means solid fill
            }
            else if( max == avg2) // Was it from region 2?
            {
                position = SkystonePosition.MIDDLE; // Record our analysis
                Imgproc.rectangle(
                        input, // Buffer to draw on
                        region2_pointA, // First point which defines the rectangle
                        region2_pointB, // Second point which defines the rectangle
                        LEFT, // The color the rectangle is drawn in
                        -1); // Negative thickness means solid fill
            }
            else if( max == avg3 ) // Was it from region 3?
            {
                position = SkystonePosition.RIGHT; // Record our analysis
                Imgproc.rectangle(
                        input, // Buffer to draw on
                        region3_pointA, // First point which defines the rectangle
                        region3_pointB, // Second point which defines the rectangle
                        MIDDLE, // The color the rectangle is drawn in
                        -1); // Negative thickness means solid fill
            }
            else if (max == avg4 ) // Was it from region 2?
            {
                position = SkystonePosition.LEFT; // Record our analysis
                Imgproc.rectangle(
                        input, // Buffer to draw on
                        region1_pointA, // First point which defines the rectangle
                        region1_pointB, // Second point which defines the rectangle
                        RIGHT, // The color the rectangle is drawn in
                        -1); // Negative thickness means solid fill
            }
            return input;
        }
        public SkystonePosition getAnalysis()
        {
            return position;
        }
        public int getMax(){
            int maxTwoThree = Math.max(avg3, avg2);
            int max = Math.max(maxTwoThree, avg4);
            return max;
        }
    }
}