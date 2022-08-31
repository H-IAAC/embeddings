package com.hiacc.embeddings.images;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import java.util.List;

public class Cmap {
    private static final int LOWER_BYTE_MASK = 0xFF;

    public static float[][] getEmbedding(float[][] data, int windowsSize) {

        int gyroensor = windowsSize * 3;

        float[][] embedding = new float[data.length][];
        for (int i = 0; i < data.length; i++) {
            int countacc = 0;
            float[][]rawData = new float[6][windowsSize];

            for (int w = 0; w < windowsSize; w++) {
                rawData[0][w] = data[i][countacc];
                rawData[1][w]= data[i][countacc + 1];
                rawData[2][w] = data[i][countacc + 2];
                rawData[3][w] = data[i][gyroensor + countacc];
                rawData[4][w] = data[i][gyroensor + countacc + 1];
                rawData[5][w]= data[i][gyroensor + countacc + 2];
                countacc = countacc + 3;

            }
        Bitmap image = getBitmap(rawData,6,windowsSize);

            embedding[i]=prepareImage(image);
        }

        return embedding;
    }

    public static Bitmap getBitmap(float[][] rows,int h, int w) {

        int[] rawData = new int[w * h];

        for (int y = 0; y < w; y++) {
            for (int x = 0; x < h; x++) {
                float hue =(rows[x][y] + 1) * 180;
                float hsv[] = new float[3];
                hsv[0] = hue;
                hsv[1] = 1f;
                hsv[2] = 1f;
                int color = Color.HSVToColor(hsv);
                rawData[y + x * w] = color;
            }
        }
        Log.d("d", String.valueOf(rawData.length));
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        bitmap.setPixels(rawData, 0, w, 0, 0, w, h);
        return bitmap;
    }



    private static float[] prepareImage(Bitmap bitmap)  {
        int modelImageSize = 32;
        Bitmap paddedBitmap = padToSquare(bitmap);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(
                paddedBitmap, modelImageSize, modelImageSize, true);
        float[] normalizedRgb = new float[modelImageSize * modelImageSize * 3];
        int nextIdx = 0;
        for (int y = 0; y < modelImageSize; y++) {
            for (int x = 0; x < modelImageSize; x++) {
                int rgb = scaledBitmap.getPixel(x, y);
                float r = ((rgb >> 16) & LOWER_BYTE_MASK) * (1 / 255.0f);
                float g = ((rgb >> 8) & LOWER_BYTE_MASK) * (1 / 255.0f);
                float b = (rgb & LOWER_BYTE_MASK) * (1 / 255.0f);
                normalizedRgb[nextIdx++] = r;
                normalizedRgb[nextIdx++] = g;
                normalizedRgb[nextIdx++] = b;
            }
        }

        return normalizedRgb;
    }

    private static Bitmap padToSquare(Bitmap source) {
        int width = source.getWidth();
        int height = source.getHeight();

        int paddingX = width < height ? (height - width) / 2 : 0;
        int paddingY = height < width ? (width - height) / 2 : 0;
        Bitmap paddedBitmap = Bitmap.createBitmap(
                width + 2 * paddingX, height + 2 * paddingY, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(paddedBitmap);
        canvas.drawARGB(0xFF, 0xFF, 0xFF, 0xFF);
        canvas.drawBitmap(source, paddingX, paddingY, null);
        return paddedBitmap;
    }


    public static Bitmap getBitmapObject(List<float[]> allSensors) {
        int w = 5;
        int h = 50;
        int[] rawData = new int[w * h];

        for (int y = 0; y < w; y++) {
            for (int x = 0; x < h; x++) {
                float hue = (allSensors.get(x)[y] + 1) * 180;
                float hsv[] = new float[3];
                hsv[0] = hue;
                hsv[1] = 1f;
                hsv[2] = 1f;
                int color = Color.HSVToColor(hsv);
                rawData[y + x * w] = color;
            }
        }
        Log.d("d", String.valueOf(rawData.length));
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        bitmap.setPixels(rawData, 0, w, 0, 0, w, h);
        return bitmap;
    }

}
