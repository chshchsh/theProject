package com.jct.bd.theproject.controller;

        import android.app.Activity;
        import android.os.Bundle;
        import android.widget.ProgressBar;
        import android.widget.TextView;

        import com.jct.bd.theproject.R;

public class MainActivity extends Activity {
    ProgressBar progressBar;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progress_bar);
        textView = findViewById(R.id.text_view);
        ProgressBarAnimation anim = new ProgressBarAnimation(this, progressBar, textView,0f,100f);
        anim.setDuration(4000);
        progressBar.setAnimation(anim);
    }
}
