package com.example.retouchephoto;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import static android.graphics.Bitmap.createBitmap;
import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.red;

class ImageTools {


    @SuppressWarnings("SameParameterValue")
    static Bitmap resizeAsContainInARectangle(Bitmap bmp, int maxSize) {
        if (bmp.getHeight() >  bmp.getWidth()) {
            return Bitmap.createScaledBitmap(bmp, bmp.getWidth() * maxSize / bmp.getHeight() , maxSize, true);
        } else {
            return Bitmap.createScaledBitmap(bmp, maxSize, bmp.getHeight() * maxSize / bmp.getWidth(), true);
        }
    }

    @SuppressWarnings({"SuspiciousNameCombination", "SameParameterValue"})
    static Bitmap toSquare(Bitmap bmp, int newSize) {

        int currentWidth = bmp.getWidth();
        int currentHeight =  bmp.getHeight();

        int newWidth = currentWidth;
        int newHeight = currentHeight;

        int newX = 0;
        int newY = 0;

        if (currentWidth > currentHeight) {
            newWidth = currentHeight;
            newX = (currentWidth - currentHeight) / 2;
        } else {
            newHeight = currentWidth;
            newY = (currentHeight - currentWidth) / 2;
        }

        bmp = Bitmap.createBitmap(bmp, newX, newY, newWidth, newHeight);

        // L'image est maintenant un carrée

        return Bitmap.createScaledBitmap(bmp, newSize, newSize, true);

    }

    /**
     *  Generates the histogram and displays it in "histogram"
     */
    static Bitmap generateHistogram(Bitmap bmp) {

        int[] pixels = new int[bmp.getWidth() * bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        int[] Rvalues = new int[256];
        int[] Gvalues = new int[256];
        int[] Bvalues = new int[256];

        // Stores how many occurrences of all color intensities, for each channel.
        for (int pixel : pixels) {
            Rvalues[(pixel >> 16) & 0x000000FF] += 1;
            Gvalues[(pixel >>8 ) & 0x000000FF] += 1;
            Bvalues[(pixel) & 0x000000FF] += 1;
        }

        int max = 0;

        // Finds the intensity (in all three channels) with the maximum number of occurrences.
        for (int i = 0; i < 256; i++) {
            max = Math.max(max, Rvalues[i]);
            max = Math.max(max, Gvalues[i]);
            max = Math.max(max, Bvalues[i]);
        }

        Bitmap hist = createBitmap(256, 200, Bitmap.Config.ARGB_8888);
        int histHeight = hist.getHeight() - 1;
        int histWidth = hist.getWidth();

        //ImageTools.fillWithColor(hist, Color.argb(Settings.HISTOGRAM_BACKGROUND_TRANSPARENCY, 0, 0, 0));

        // If the image is blank, return with a black histogram.
        if (max != 0) {

            int[] histPixels = new int[hist.getHeight() * hist.getWidth()];

            /*
            int rPos;
            int gPos;
            int bPos;
            for (int x = 0; x < 256; x++) {

                rPos = (int) Math.sqrt(Rvalues[x] * histHeight / max) * 14;
                gPos = (int) Math.sqrt(Gvalues[x] * histHeight / max) * 14;
                bPos = (int) Math.sqrt(Bvalues[x] * histHeight / max) * 14;

                for (int shift = - 5; shift <= 0; shift++) {
                    if (rPos + shift >= 0) histPixels[x + ((histHeight - rPos + shift) * histWidth)] = Color.rgb(255, 0, 0);
                    if (gPos + shift >= 0) histPixels[x + ((histHeight - gPos + shift) * histWidth)] = Color.rgb(0, 255, 0);
                    if (bPos + shift >= 0) histPixels[x + ((histHeight - bPos + shift) * histWidth)] = Color.rgb(0, 0, 255);
                }
            }

             */

            float ratio = (float) histHeight / max;

            for (int x = 0; x < histWidth; x++) {
                for (int y = 1; y < histHeight; y++) {

                    int colorR = 0;
                    int colorG = 0;
                    int colorB = 0;
                    int colorA = Settings.HISTOGRAM_BACKGROUND_TRANSPARENCY;

                    if (Math.sqrt(Rvalues[x] * ratio) * 14 >= y) {colorR = 255; colorA = Settings.HISTOGRAM_FOREGROUND_TRANSPARENCY;}
                    if (Math.sqrt(Gvalues[x] * ratio) * 14 >= y) {colorG = 255; colorA = Settings.HISTOGRAM_FOREGROUND_TRANSPARENCY;}
                    if (Math.sqrt(Bvalues[x] * ratio) * 14 >= y) {colorB = 255; colorA = Settings.HISTOGRAM_FOREGROUND_TRANSPARENCY;}

                    histPixels[x + ((histHeight - y) * histWidth)] = Color.argb(colorA, colorR, colorG, colorB);
                }
            }
            hist.setPixels(histPixels, 0, hist.getWidth(), 0, 0, hist.getWidth(), hist.getHeight());
        }
        return hist;
    }


    static void drawRectangle(final Bitmap bmp, Point a, Point b, int color) {
        drawRectangle(bmp, a, b, color, -1);
    }


    static void drawRectangle(final Bitmap bmp, Point a, Point b, int color, int thickness) {
        if (a != null && b!= null) {
            if (!a.isEquals(b)) {
                Canvas myCanvas = new Canvas(bmp);

                Paint paint = new Paint();
                if (thickness > 0) {
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(Settings.CROP_BORDER_SIZE);
                } else {
                    paint.setStyle(Paint.Style.FILL);
                }
                paint.setColor(color);
                myCanvas.drawRect(a.x, a.y, b.x, b.y, paint);
            }
        }
    }

    @SuppressWarnings("unused")
    static void fillWithColor(final Bitmap bmp, int color) {
        drawRectangle(bmp, new Point(0,0), new Point(bmp.getWidth(), bmp.getHeight()), color);
    }

    static Bitmap bitmapClone(Bitmap source) {
        if (source == null) return null;
        return source.copy(source.getConfig(), true);
    }

    static Bitmap bitmapCreate(int width, int height) {return Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);}

    static void drawCircle(final Bitmap bmp, Point center, int radius, int color) {
        if (center != null && radius > 0) {
            final Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(color);
            Canvas selection = new Canvas(bmp);
            selection.setBitmap(bmp);
            selection.drawCircle(center.x, center.y, radius, paint);
        }
    }

    //TODO: Not working well yet
    static void forceRectangleRatio(Bitmap bmp, Point a, Point b) {

        // Negative values are not acceptable.
        if (a.x < 0 || a.y < 0 ||b.x < 0 ||b.y < 0) return;

        int rectangleWidth = Math.abs(a.x - b.x);
        int rectangleHeight = Math.abs(a.y - b.y);

        float rectangleRatio = (float) rectangleWidth / rectangleHeight;
        float bmpRatio = (float) bmp.getWidth() / bmp.getHeight();

        boolean rectangleWiderThenBmp = rectangleRatio > bmpRatio;

        if (rectangleWiderThenBmp) {
            b.x =  a.x + (int) ((b.x - a.x) * bmpRatio);
        } else {
            b.y =  a.y + (int) ((b.x - a.x) / bmpRatio);
        }

    }

    static int getHueFromColor(int color){
        float[] hsv = new float[3];
        Color.RGBToHSV(red(color), green(color), blue(color), hsv);
        return (int) hsv[0];
    }



}
