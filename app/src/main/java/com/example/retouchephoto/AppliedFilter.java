package com.example.retouchephoto;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

class AppliedFilter {
    private final Filter filter;
    private final Bitmap maskBmp;
    private final int colorSeekHue;
    private final float seekBar;
    private final float seekBar2;
    private final boolean switch1;
    private final Point touchDown;
    private final Point touchUp;

    AppliedFilter(Filter filter, Bitmap maskBmp, int colorSeekHue, float seekBar, float seekBar2, boolean switch1, Point touchDown, Point touchUp) {
        this.filter = filter;
        this.maskBmp = maskBmp;
        this.colorSeekHue = colorSeekHue;
        this.seekBar = seekBar;
        this.seekBar2 = seekBar2;
        this.switch1 = switch1;
        this.touchDown = touchDown;
        this.touchUp = touchUp;
    }

    AppliedFilter(Filter filter){
        this(filter, null, 0, filter.seekBar1Set, filter.seekBar2Set, filter.switch1Default,  new Point(0,0), new Point(0,0));
    }

    String getName() {return filter.getName();}

    Bitmap preview(final Bitmap bmp, Context context) {
        return filter.preview(bmp, maskBmp, context, colorSeekHue, seekBar, seekBar2, switch1, touchDown, touchUp);
    }

    Bitmap apply(final Bitmap bmp, Context context) {
        return filter.apply(bmp, maskBmp, context, colorSeekHue, seekBar, seekBar2, switch1, touchDown, touchUp);
    }


}
