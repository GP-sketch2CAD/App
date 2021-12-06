package gachon.termproject.finalproject;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.Interpreter.Options;

import kotlin.collections.ArraysKt;

public final class DigitClassifier {
    private Interpreter interpreter;
    private boolean isInitialized;
    MyView myView;
    private final ExecutorService executorService;
    private int inputImageWidth;
    private int inputImageHeight;
    private int modelInputSize;
    private ArrayList<Integer> finalresult= new ArrayList<Integer>();
    private static final String TAG = "DigitClassifier";
    private static final int FLOAT_TYPE_SIZE = 4;
    private static final int PIXEL_SIZE = 1;
    private static final int OUTPUT_CLASSES_COUNT = 10;

    public final boolean isInitialized() {
        return this.isInitialized;
    }

    public final Task initialize() {
        final TaskCompletionSource task = new TaskCompletionSource();
        this.executorService.execute((Runnable)(new Runnable() {
            public final void run() {
                try {
                    DigitClassifier.this.initializeInterpreter();
                    task.setResult((Object)null);
                } catch (IOException var2) {
                    task.setException((Exception)var2);
                }

            }
        }));
        Task task2 = task.getTask();
        return task2;
    }

    private final void initializeInterpreter() throws IOException {
        AssetManager assetManager = myView.getContext().getAssets();
        ByteBuffer model = this.loadModelFile(assetManager, "tntwkdlstlr.tflite");
        Options options = new Options();
        options.setUseNNAPI(true);
        Interpreter interpreter = new Interpreter(model, options);
        int[] inputShape = interpreter.getInputTensor(0).shape();
        this.inputImageWidth = inputShape[1];
        this.inputImageHeight = inputShape[2];
        this.modelInputSize = 4 * this.inputImageWidth * this.inputImageHeight * 1;
        this.interpreter = interpreter;
        this.isInitialized = true;
        Log.d("DigitClassifier", "Initialized TFLite interpreter.");
    }

    private final ByteBuffer loadModelFile(AssetManager assetManager, String filename) throws IOException {
        AssetFileDescriptor var10000 = assetManager.openFd(filename);
        AssetFileDescriptor fileDescriptor = var10000;
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        MappedByteBuffer var10 = fileChannel.map(MapMode.READ_ONLY, startOffset, declaredLength);
        return (ByteBuffer)var10;
    }

    public final String classify(Bitmap bitmap) throws Throwable {
        boolean var2 = this.isInitialized;
        if (!var2) {
            String var23 = "TF Lite Interpreter is not initialized yet.";
            throw (Throwable)(new IllegalStateException(var23.toString()));
        } else {
            System.out.println(bitmap);
            Bitmap resizedImage = Bitmap.createScaledBitmap(bitmap, this.inputImageWidth, this.inputImageHeight, true);
            ByteBuffer byteBuffer = this.convertBitmapToByteBuffer(resizedImage);
            System.out.println(byteBuffer);
            byte var5 = 1;
            float[][] var6 = new float[var5][];

            for(int var7 = 0; var7 < var5; ++var7) {
                float[] var19 = new float[10];
                var6[var7] = var19;
            }

            Interpreter var10000 = this.interpreter;
            if (var10000 != null) {
                var10000.run(byteBuffer, (float[][])var6);
            }

            float[] result = ((float[][])var6)[0];
            Iterable $this$maxBy$iv = (Iterable) ArraysKt.getIndices(result);
            Iterator iterator$iv$iv = $this$maxBy$iv.iterator();
            Object var34;
            if (!iterator$iv$iv.hasNext()) {
                var34 = null;
            } else {
                Object maxElem$iv$iv = iterator$iv$iv.next();
                if (!iterator$iv$iv.hasNext()) {
                    var34 = maxElem$iv$iv;
                } else {
                    int it = ((Number)maxElem$iv$iv).intValue();
                    float maxValue$iv$iv = result[it];

                    do {
                        Object e$iv$iv = iterator$iv$iv.next();
                        it = ((Number)e$iv$iv).intValue();
                        float v$iv$iv = result[it];
                        if (Float.compare(maxValue$iv$iv, v$iv$iv) < 0) {
                            maxElem$iv$iv = e$iv$iv;
                            maxValue$iv$iv = v$iv$iv;
                        }
                    } while(iterator$iv$iv.hasNext());

                    var34 = maxElem$iv$iv;
                }
            }

            int maxIndex = (Integer)var34 != null ? (Integer)var34 : -1;
            String var29 = "Predicion Result: %d\nConfidence: %2f";
            Object[] var30 = new Object[]{maxIndex, result[maxIndex]};
            String var35 = String.format(var29, Arrays.copyOf(var30, var30.length));
            String resultString = var35;
            System.out.println(var35);
            return resultString;
        }
    }

    public final Task classifyAsync( Bitmap bitmap) {
        final TaskCompletionSource task = new TaskCompletionSource();
        this.executorService.execute((Runnable)(new Runnable() {
            public final void run() {
                String result = null;
                try {
                    System.out.println(bitmap+"11");
                    result = DigitClassifier.this.classify(bitmap);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                task.setResult(result);
            }
        }));
        Task var10000 = task.getTask();
        return var10000;
    }

    public final void close() {
        this.executorService.execute((Runnable)(new Runnable() {
            public final void run() {
                Interpreter var10000 = DigitClassifier.this.interpreter;
                if (var10000 != null) {
                    var10000.close();
                }

                Log.d("DigitClassifier", "Closed TFLite interpreter.");
            }
        }));
    }

    private final ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        System.out.println(bitmap+"22");
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(this.modelInputSize);
        byteBuffer.order(ByteOrder.nativeOrder());
        int[] pixels = new int[this.inputImageWidth * this.inputImageHeight];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        int[] var6 = pixels;
        int var7 = pixels.length;

        for(int var5 = 0; var5 < var7; ++var5) {
            int pixelValue = var6[var5];
            int r = pixelValue >> 16 & 255;
            int g = pixelValue >> 8 & 255;
            int b = pixelValue & 255;
            float normalizedPixelValue = (float)(r + g + b) / 3.0F / 255.0F;
            byteBuffer.putFloat(normalizedPixelValue);
        }

        return byteBuffer;
    }

    public DigitClassifier(MyView myView) {
        super();
        this.myView = myView;
        ExecutorService var10001 = Executors.newCachedThreadPool();
        this.executorService = var10001;
    }

    // $FF: synthetic method
    public static final void access$setInterpreter$p(DigitClassifier $this, Interpreter var1) {
        $this.interpreter = var1;
    }


    public static final class Companion {
        private Companion() {
        }

    }
}
