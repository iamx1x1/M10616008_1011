package com.example.mandyyang.m10616008_1011;
//import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.util.Arrays;
//OpenCV
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.MatOfFloat;
import org.opencv.core.Size;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Point;
import org.opencv.core.MatOfInt;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Core;



public class MainActivity extends AppCompatActivity {

    //ScaleBright,RGB2Gray,ColorGammaContrast,Image Sub
    private Button b1;
    private ImageView inputImg, inputImg1,outputImg1,outputImg, iv1, iv2, iv3, iv4;

    private Bitmap inputBmp, bmp1, bmp2;
    private Bitmap operation;

    //OpenCV
    private Size mSize0;
    private Mat mIntermediateMat;
    private Mat mMat0;
    private MatOfInt mChannels[];
    private MatOfInt mHistSize;
    private int mHistSizeNum = 25;
    private MatOfFloat mRanges;
    private Scalar mColorsRGB[];
    private Scalar mColorsHue[];
    private Scalar mWhilte;
    private Point mP1;
    private Point mP2;
    private float mBuff[];

    private static final String TAG = "Image SegmentationＯpenCV3";

    private BaseLoaderCallback getmLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            super.onManagerConnected(status);
        }
    };


    //private static final String TAG = "Image SegmentationＯpenCV3";
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    //onResume
    @Override
    public void onResume(){
        super.onResume();

        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_3_0,this,mLoaderCallback);
    }


//b2_Click
public void b2_Click(View view){
    iv1 = (ImageView) findViewById(R.id.inputImg);
    BitmapDrawable abmp = (BitmapDrawable) iv1.getDrawable();
    bmp1=abmp.getBitmap();
    iv3 = (ImageView) findViewById(R.id.outputImg);
    Button b2 =(Button) findViewById(R.id.button5);


    HistogramVariableInitialization();
    Histogram(bmp1,iv3);

}

//b3_Click

    public void b3_Click(View view){
        iv2 = (ImageView) findViewById(R.id.inputImg1);
        BitmapDrawable abmp = (BitmapDrawable) iv2.getDrawable();
        bmp2=abmp.getBitmap();

        iv4 = (ImageView) findViewById(R.id.outputImg1);
        Button b2 =(Button) findViewById(R.id.button6);


        HistogramVariableInitialization();
        Histogram(bmp2,iv4);

    }


    ////b4_Click

    public void b4_Click(View view){
        iv2 = (ImageView) findViewById(R.id.inputImg1);
        BitmapDrawable abmp = (BitmapDrawable) iv2.getDrawable();
        bmp2=abmp.getBitmap();

        iv3 = (ImageView) findViewById(R.id.outputImg);
        iv4 = (ImageView) findViewById(R.id.outputImg1);

        Mat sImage=new Mat();
        Utils.bitmapToMat(bmp2,sImage);

        Mat grayImage=new Mat();
        Imgproc.cvtColor(sImage,grayImage,Imgproc.COLOR_RGB2GRAY);
        displayMatImage(grayImage,iv3);

        Mat eqGS=new Mat();
        Imgproc.equalizeHist(grayImage,eqGS);
        displayMatImage(grayImage,iv4);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button) findViewById(R.id.button4);
        //b2 = (Button) findViewById(R.id.button2);
        // b3 = (Button) findViewById(R.id.button3);
        inputImg = (ImageView) findViewById(R.id.inputImg);

        BitmapDrawable abmp = (BitmapDrawable) inputImg.getDrawable();
        inputBmp = abmp.getBitmap();

        outputImg = (ImageView) findViewById(R.id.outputImg);

    }

    public void RGB2Gray(View view) {
        operation = Bitmap.createBitmap(inputBmp.getWidth(), inputBmp.getHeight(), inputBmp.getConfig());
        // constant factors
        final double GS_RED = 0.299;
        final double GS_GREEN = 0.587;
        final double GS_BLUE = 0.114;

        // pixel information
        int A, R, G, B;
        int pixel;

        // get image size
        int width = inputBmp.getWidth();
        int height = inputBmp.getHeight();

        // scan through every single pixel
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get one pixel color
                pixel = inputBmp.getPixel(x, y);
                // retrieve color of all channels
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                // take conversion up to one single value
                R = G = B = (int) (GS_RED * R + GS_GREEN * G + GS_BLUE * B);
                // set new pixel color to output bitmap
                operation.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        outputImg.setImageBitmap(operation);
    } //灰階 RGB2Gray

    public void ScaleBright(View view) {
        operation = Bitmap.createBitmap(inputBmp.getWidth(), inputBmp.getHeight(), inputBmp.getConfig());
        double scale = 1.2;
        for (int x = 0; x < inputBmp.getWidth(); x++) {
            for (int y = 0; y < inputBmp.getHeight(); y++) {
                int p = inputBmp.getPixel(x, y);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);

                r = (int) (scale * r);
                if (r > 255) {
                    r = 255;
                } else if (r < 0) {
                    r = 0;
                }
                g = (int) (scale * g);
                if (g > 255) {
                    g = 255;
                } else if (g < 0) {
                    g = 0;
                }
                b = (int) (scale * b);
                if (b > 255) {
                    b = 255;
                } else if (b < 0) {
                    b = 0;
                }
                operation.setPixel(x, y, Color.argb(Color.alpha(p), r, g, b));
            }
        }
        outputImg.setImageBitmap(operation);
    } //變亮 ScaleBright

    public void ColorGammaContrast(View view) {
        operation = Bitmap.createBitmap(inputBmp.getWidth(), inputBmp.getHeight(), inputBmp.getConfig());
        double gamma = 0.6;
        // get image size
        int width = inputBmp.getWidth();
        int height = inputBmp.getHeight();
        // color information
        int A, R, G, B;
        int pixel;
        // constant value curve
        final int MAX_SIZE = 256;
        final double MAX_VALUE_DBL = 255.0;
        final int MAX_VALUE_INT = 255;
        final double REVERSE = 1.0;

        // gamma arrays
        int[] gammaR = new int[MAX_SIZE];
        int[] gammaG = new int[MAX_SIZE];
        int[] gammaB = new int[MAX_SIZE];

        // setting values for every gamma channels
        for (int i = 0; i < MAX_SIZE; ++i) {
            gammaR[i] = (int) Math.min(MAX_VALUE_INT,
                    (int) ((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / gamma)) + 0.5));
            gammaG[i] = (int) Math.min(MAX_VALUE_INT,
                    (int) ((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / gamma)) + 0.5));
            gammaB[i] = (int) Math.min(MAX_VALUE_INT,
                    (int) ((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / gamma)) + 0.5));
        }

        // apply gamma table
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = inputBmp.getPixel(x, y);
                A = Color.alpha(pixel);
                // look up gamma
                R = gammaR[Color.red(pixel)];
                G = gammaG[Color.green(pixel)];
                B = gammaB[Color.blue(pixel)];
                // set new color to output bitmap
                operation.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        outputImg.setImageBitmap(operation);
    }  //ColorGammaContrast

    public void b1_Click(View view) {

        iv1 = (ImageView) findViewById(R.id.inputImg);
        BitmapDrawable abmp = (BitmapDrawable) iv1.getDrawable();
        bmp1 = abmp.getBitmap();

        iv2 = (ImageView) findViewById(R.id.inputImg1);
        abmp = (BitmapDrawable) iv2.getDrawable();
        bmp2 = abmp.getBitmap();

        iv3 = (ImageView) findViewById(R.id.outputImg);
        Button b1 = (Button) findViewById(R.id.button4);
        Image_Subtraction();

    } //btn4 雜訊Image Sub

    private void Image_Subtraction() {
        operation = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        for (int i = 0; i < bmp1.getWidth(); i++) {
            for (int j = 0; j < bmp1.getHeight(); j++) {
                int p1 = bmp1.getPixel(i, j);
                int r1 = Color.red(p1);
                int g1 = Color.green(p1);
                int b1 = Color.blue(p1);
                int gray1 = (int) (0.3 * r1 + 0.59 * g1 + 0.11 * b1);

                int p2 = bmp2.getPixel(i, j);
                int r2 = Color.red(p2);
                int g2 = Color.green(p2);
                int b2 = Color.blue(p2);
                int gray2 = (int) (0.3 * r2 + 0.59 * g2 + 0.11 * b2);

                //int diff = Math.abs(r1 - r2);
                int diff = Math.abs(gray1 - gray2);
                if (diff > 30) {
                    operation.setPixel(i, j, Color.argb(Color.alpha(p1), 255, 255, 255));
                } else {
                    operation.setPixel(i, j, Color.argb(Color.alpha(p1), 0, 0, 0));
                }
                //operation.setPixel(i, j, Color.argb(Color.alpha(p1), diff, diff, diff));
            }
        }
        iv3.setImageBitmap(operation);
    } //雜訊 Image Sub

    //HistogramVariableInitialization
    private void HistogramVariableInitialization() {
        mIntermediateMat=new Mat();
        mSize0 = new Size();
        mChannels = new MatOfInt[]{new MatOfInt(0), new MatOfInt(1), new MatOfInt(2)};
        mBuff = new float[mHistSizeNum];
        mHistSize = new MatOfInt(mHistSizeNum);
        mRanges = new MatOfFloat(0f, 256f);
        mMat0 = new Mat();
        mColorsRGB = new Scalar[]{new Scalar(200, 0, 0, 255), new Scalar(0, 200, 0, 255), new Scalar(0, 0, 200, 255)};
        mColorsHue = new Scalar[]{
                new Scalar(255, 0, 0, 255), new Scalar(255, 60, 0, 255), new Scalar(255, 180, 0, 255), new Scalar(255, 180, 0, 255), new Scalar(255, 240, 0, 255),
                new Scalar(215, 213, 0, 255), new Scalar(85, 255, 0, 255), new Scalar(20, 255, 0, 255), new Scalar(20, 255, 0, 255), new Scalar(0, 255, 30, 255),
                new Scalar(0, 255, 85, 255), new Scalar(0, 255, 150, 255), new Scalar(0, 234, 255, 255), new Scalar(0, 234, 255, 255), new Scalar(0, 170, 255, 255),
                new Scalar(0, 120, 255, 255), new Scalar(0, 60, 255, 255), new Scalar(64, 0, 255, 255), new Scalar(64, 0, 255, 255), new Scalar(120, 0, 255, 255),
                new Scalar(180, 0, 255, 255), new Scalar(255, 0, 255, 255), new Scalar(255, 0, 85, 255), new Scalar(255, 0, 85, 255), new Scalar(255, 0, 0, 255)
        };
        mWhilte = Scalar.all(255);
        mP1 = new Point();
        mP2 = new Point();
    }


    //---Histogram_btn3

    private void Histogram(Bitmap bmp, ImageView iv) {
        Bitmap bmp3 = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        int imgH = bmp3.getHeight();
        int imgW = bmp3.getWidth();
        Mat rgba = new Mat(imgH, imgW, CvType.CV_8UC1);
        Utils.bitmapToMat(bmp, rgba);


//do histogram
        Size sizeRgba = rgba.size();
        Mat rgbaInnerWindow;
        int rows = (int) sizeRgba.height;
        int cols = (int) sizeRgba.width;

        int left = cols / 8;
        int top = rows / 8;

        int width = cols * 3 / 4;
        int height = rows * 3 / 4;

        Mat hist = new Mat();
        int thikness = (int) (sizeRgba.width / (mHistSizeNum + 10) / 5);
        if (thikness > 5) thikness = 5;
        int offset = (int) ((sizeRgba.width - (5 * mHistSizeNum + 4 * 10) * thikness) / 2);
        // RGB
        for (int c = 0; c < 3; c++) {
            Imgproc.calcHist(Arrays.asList(rgba), mChannels[c], mMat0, hist, mHistSize, mRanges);
            Core.normalize(hist, hist, sizeRgba.height / 2, 0, Core.NORM_INF);
            hist.get(0, 0, mBuff);
            for (int h = 0; h < mHistSizeNum; h++) {
                mP1.x = mP2.x = offset + (c * (mHistSizeNum + 10) + h) * thikness;
                mP1.y = sizeRgba.height - 1;
                mP2.y = mP1.y - 2 - (int) mBuff[h];
                Imgproc.line(rgba, mP1, mP2, mColorsRGB[c], thikness); //Core.line OpenCV sdk3 not support
            }
        }
        // Value and Hue
        Imgproc.cvtColor(rgba, mIntermediateMat, Imgproc.COLOR_RGB2HSV_FULL);
        // Value
        Imgproc.calcHist(Arrays.asList(mIntermediateMat), mChannels[2], mMat0, hist, mHistSize, mRanges);
        Core.normalize(hist, hist, sizeRgba.height / 2, 0, Core.NORM_INF);
        hist.get(0, 0, mBuff);
        for (int h = 0; h < mHistSizeNum; h++) {
            mP1.x = mP2.x = offset + (3 * (mHistSizeNum + 10) + h) * thikness;
            mP1.y = sizeRgba.height - 1;
            mP2.y = mP1.y - 2 - (int) mBuff[h];
            Imgproc.line(rgba, mP1, mP2, mWhilte, thikness); //Core.line OpenCV sdk3 not support
        }
        // Hue
        Imgproc.calcHist(Arrays.asList(mIntermediateMat), mChannels[0], mMat0, hist, mHistSize, mRanges);
        Core.normalize(hist, hist, sizeRgba.height / 2, 0, Core.NORM_INF);
        hist.get(0, 0, mBuff);
        for (int h = 0; h < mHistSizeNum; h++) {
            mP1.x = mP2.x = offset + (4 * (mHistSizeNum + 10) + h) * thikness;
            mP1.y = sizeRgba.height - 1;
            mP2.y = mP1.y - 2 - (int) mBuff[h];
            Imgproc.line(rgba, mP1, mP2, mColorsHue[h], thikness); //Core.line OpenCV sdk3 not support
        }

        try {
            bmp3 = Bitmap.createBitmap(rgba.cols(), rgba.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(rgba, bmp3);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        iv.setImageBitmap(bmp3);
        iv.invalidate();
    }


    //displayMatImage_btn4
    private void displayMatImage(Mat image, ImageView iv){

        Bitmap bitMap = Bitmap.createBitmap(image.cols(),image.rows(),Bitmap.Config.RGB_565);
        Utils.matToBitmap(image,bitMap);
        iv.setImageBitmap(bitMap);

    }

}
