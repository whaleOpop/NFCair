package nfc.cair.project.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import nfc.cair.project.Database.DbConnect;
import nfc.cair.project.Model.CardView;

/**
 * This class implements loading during cold start of the program
 *
 * @author WhaleOpop
 * @version 1.0
 * @since 2022-06-13
 */

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    SharedPreferences sharedPref;
    @Nullable
    public static ArrayList<String> id;

    @Nullable
    public ArrayList<String> getEdittext() {
        return id;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref= getSharedPreferences("my_pref", Context.MODE_PRIVATE);
        boolean isFirstRun = sharedPref.getBoolean("firstRun", true);
        Intent intent = new Intent(this, MainActivity.class);
        if (isFirstRun) {
            // Если это первый запуск приложения, записываем значения в файл

            RunInsertThread runInsertThread = new RunInsertThread();
            Thread RunSelectThreadT1 = new Thread(runInsertThread);

            RunSelectThread runSelectThread = new RunSelectThread();
            Thread RunSelectThreadT2 = new Thread(runSelectThread);
            RunSelectThreadT1.start();
            RunSelectThreadT2.start();


        } else {
            // Если файл уже существует, получаем значение из него
            String value = sharedPref.getString("id", "0");
            Log.d("MyApp", "Значение из файла: " + value);

        }
        startActivity(intent);
        finish();
    }



    public class RunSelectThread implements Runnable {

        @Override
        public void run() {

            DbConnect con = new DbConnect();
            con.run();
            String sqlSelect = "SELECT id FROM Patient ORDER BY id DESC LIMIT 1;";
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("id",con.SelectQuery(sqlSelect).get(0));
            editor.putBoolean("firstRun", false);
            editor.apply();
        }
    }
    public static class RunInsertThread implements Runnable {
        private String id;

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        @Override
        public void run() {

            DbConnect con = new DbConnect();
            con.run();
            String sqlSelect = "INSERT INTO sql11664074.Patient VALUES ()";
            con.InsertQuery(sqlSelect);
        }
    }


}