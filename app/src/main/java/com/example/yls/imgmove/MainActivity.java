package com.example.yls.imgmove;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {
    private ImageView Img1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Img1 = (ImageView) findViewById(R.id.img1);
        Img1.setOnTouchListener(new TouchListener());

    }

    private final class TouchListener implements View.OnTouchListener {
        private int mode = 0;
        private static final int MODE_DRAG = 1;
        private static final int MODE_ZOOM = 2;
        private PointF startPoint = new PointF();
        private Matrix matrix = new Matrix();
        private Matrix currrentMatrix = new Matrix();
        private float startDis;
        private PointF midPoint;


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    mode = MODE_DRAG;
                    currrentMatrix.set(Img1.getImageMatrix());
                    startPoint.set(event.getX(), event.getY());
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == MODE_DRAG) {
                        float dx = event.getX() - startPoint.x;
                        float dy = event.getY() - startPoint.y;
                        matrix.set(currrentMatrix);
                        matrix.postTranslate(dx, dy);
                    } else if (mode == MODE_ZOOM) {
                        float endDis = distance(event);
                        if (endDis > 10f) {
                            float scale=endDis/startDis;
                            matrix.set(currrentMatrix);
                            matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    mode=0;
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    mode=0;
                case MotionEvent.ACTION_POINTER_DOWN:
                    mode = MODE_ZOOM;
                    startDis = distance(event);
                    if (startDis > 10f) {
                        midPoint = mid(event);
                        currrentMatrix.set(Img1.getImageMatrix());
                    }
                    break;
            }
          Img1.setImageMatrix(matrix);
            return true;
        }

        private float distance(MotionEvent event) {
            float dx = event.getX(1) - event.getX(0);
            float dy = event.getY(1) - event.getY(0);
            return (float)Math.sqrt(dx * dx + dy * dy);
        }
        private PointF mid(MotionEvent event) {
            float midX = (event.getX(1) + event.getX(0)) / 2;
            float midY = (event.getY(1) + event.getY(0)) / 2;
            return new PointF(midX, midY);
        }


    }

}
