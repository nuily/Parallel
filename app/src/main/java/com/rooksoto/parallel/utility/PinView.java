package com.rooksoto.parallel.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.rooksoto.parallel.R;

import java.util.ArrayList;
import java.util.List;

public class PinView extends SubsamplingScaleImageView {

    private PointF sPin;
    private List<PointF> pointList = new ArrayList<>();
    private Bitmap pin;

    public PinView(Context context) {
        this(context, null);
    }

    public PinView(Context context, AttributeSet attr) {
        super(context, attr);
        initialise();
    }

    public void addPin(PointF point) {
        pointList.add(point);
        initialise();
        invalidate();
    }

    public void setPin(PointF sPin) {
        this.sPin = sPin;
        initialise();
        invalidate();
    }

    public PointF getPin() {
        return sPin;
    }

    private void initialise() {
        if (pin == null) {
            float density = getResources().getDisplayMetrics().densityDpi;
            pin = BitmapFactory.decodeResource(this.getResources(), R.drawable.pushpin_blue);
            float w = (density/420f) * pin.getWidth();
            float h = (density/420f) * pin.getHeight();
            pin = Bitmap.createScaledBitmap(pin, (int)w, (int)h, true);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Don't draw pin before image is ready so it doesn't move around during setup.
        if (!isReady()) {
            return;
        }
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        if (sPin != null && pin != null) {
            drawPin(canvas, paint, sPin);
        }

        for (PointF point:pointList) {
            drawPin(canvas, paint, point);
        }

    }

    private void drawPin(Canvas canvas, Paint paint, PointF sPin) {
        PointF vPin = sourceToViewCoord(sPin);
        float vX = vPin.x - (pin.getWidth()/2);
        float vY = vPin.y - pin.getHeight();
        canvas.drawBitmap(pin, vX, vY, paint);
    }

}
