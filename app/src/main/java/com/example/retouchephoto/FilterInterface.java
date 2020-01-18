package com.example.retouchephoto;

import android.graphics.Bitmap;

/**
 * This interface is used to dynamically change Filter instances apply method.
 * Each filter will call a different FilterFunctions static method and using this
 * interface, it is possible for MainActivity to change which one to use.
 *
 * @author Thomas Barillot
 * @version 1.0
 * @since   2019-01-08
 */
public interface FilterInterface {
    void apply(Bitmap bmp, int colorSeekHue, float seekBar, float seekBar2);
}
