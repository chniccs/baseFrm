package site.chniccs.basefrm.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mzule.activityrouter.annotation.Router;

import site.chniccs.basefrm.R;
@Router("temp")
public class TempActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
    }
}
