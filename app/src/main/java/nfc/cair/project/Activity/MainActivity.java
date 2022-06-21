package nfc.cair.project.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import java.util.List;

import nfc.cair.project.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    @Nullable
    static List<View> allEds = new ArrayList<>();
    LinearLayout layoutCardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        FloatingActionButton btn= findViewById(R.id.buttonAdd);
        Intent intent = new Intent(this, NfcActivity.class);
        layoutCardView = (LinearLayout) findViewById(R.id.LayoutCardView);


        if(!allEds.isEmpty()) {
            for (int i = 0; i < allEds.size(); i++) {
                layoutCardView.addView(allEds.get(i));
            }
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(intent);
                overridePendingTransition(R.anim.slidein, R.anim.slideout);



            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}