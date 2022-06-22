package nfc.cair.project.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;

import java.util.ArrayList;

import nfc.cair.project.Model.CardView;

/**
 * This class implements loading during cold start of the program
 * @author WhaleOpop
 * @version 1.0
 * @since 2022-06-13
 */

public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}