package coordemo.ly.com.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    private ScratchImageView scratch_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scratch_view = (ScratchImageView) findViewById(R.id.scratch_view);
        View view = View.inflate(this, R.layout.scratch_view_after, null);
        Bitmap viewBitmap = getViewBitmap(view);
        scratch_view.setImageBitmap(viewBitmap);
        scratch_view.setRevealListener(new ScratchImageView.IRevealListener() {
            @Override
            public void onRevealed(ScratchImageView tv) {
                // on reveal
            }

            @Override
            public void onRevealPercentChangedListener(ScratchImageView siv, float percent) {
                // on image percent reveal
                if (percent > 0.5f) {
                    scratch_view.reveal();
                }
                Log.d("percent", percent + "--");
            }
        });

        GuaGuaKa guaGuaKa = (GuaGuaKa) findViewById(R.id.gg);
//        guaGuaKa.setImageBitmap(viewBitmap);
        guaGuaKa.setLooklinstener(new GuaGuaKa.Looklinstener() {
            @Override
            public void onClick() {
                Toast.makeText(MainActivity.this, "nihao", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.tv_look).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "nihao", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private Bitmap getViewBitmap(View addViewContent) {

        addViewContent.setDrawingCacheEnabled(true);

        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0,
                addViewContent.getMeasuredWidth(),
                addViewContent.getMeasuredHeight());

        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        return bitmap;
    }


}
