package nfc.cair.project.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import nfc.cair.project.Database.DbConnect;
import nfc.cair.project.Model.CardView;
import nfc.cair.project.R;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    MainActivity main = MainActivity.instance;


    private String id=main.getId();
    private ArrayList<String> textList;

    SharedPreferences sharedPref;
    private EditText editMedicine;
    private EditText editNameDisease;
    private EditText editSymptoms;
    private EditText editFirstAid;
    private EditText editDoctoname;
    private EditText editDoctorNumber;
    private FloatingActionButton  buttonDown;
    private FloatingActionButton buttonSave;
    private FloatingActionButton buttonExit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        sharedPref= getSharedPreferences("my_pref", Context.MODE_PRIVATE);
        editMedicine = (EditText) findViewById(R.id.medicine);
        editNameDisease = (EditText) findViewById(R.id.nameDisease);
        editSymptoms = (EditText) findViewById(R.id.symptoms);
        editFirstAid = (EditText) findViewById(R.id.firstAid);
        editDoctoname = (EditText) findViewById(R.id.doctoname);
        editDoctorNumber = (EditText) findViewById(R.id.doctorNumber);


        buttonExit= findViewById(R.id.buttonExit);
        buttonExit.setVisibility(View.INVISIBLE);


        buttonDown=findViewById(R.id.buttonDLoad);




        buttonSave=findViewById(R.id.buttonSave);

        buttonDown.setOnClickListener(this);
        buttonSave.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        Intent intent = new Intent(this, MainActivity.class);
        switch (v.getId()) {
            case R.id.buttonDLoad:


                RunSelectThread runSelectThread = new RunSelectThread();
                Thread RunSelectThreadT1 = new Thread(runSelectThread);
                RunSelectThreadT1.start();
                try {
                    RunSelectThreadT1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                editMedicine.setText(textList.get(1));
                editNameDisease.setText(textList.get(2));
                editSymptoms.setText(textList.get(3));
                editFirstAid.setText(textList.get(4));
                editDoctoname.setText(textList.get(5));
                editDoctorNumber.setText(textList.get(6));
                break;
            case R.id.buttonSave:

                changeCardViewList(id.toString());
                main.CardViewList.add(new CardView(Integer.valueOf(id),editDoctorNumber.getText().toString(),editNameDisease.getText().toString()));
                textList=new ArrayList<String>();
                arrayListAdd(textList);
                RunUpdateThread runUpdateThread = new RunUpdateThread();
                Thread RunUpdateThreadT1 = new Thread(runUpdateThread);
                RunUpdateThreadT1.start();
                try {
                    RunUpdateThreadT1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                main.saveData();
                buttonExit.setVisibility(View.VISIBLE);
                buttonDown.setVisibility(View.INVISIBLE);
                startActivity(intent);

                overridePendingTransition(R.anim.slidein, R.anim.slideout);
                finish();

                break;
        }
    }




    private boolean changeCardViewList(String id){

        for (CardView cardView : main.CardViewList) {
            if (cardView.getId().toString().equals(id)) {
                main.CardViewList.remove(cardView);
                return true;
            }
        }

        return false;
    }
    private void arrayListAdd(ArrayList<String> edittext) {
        edittext.add(id);
        edittext.add(editMedicine.getText().toString());
        edittext.add(editNameDisease.getText().toString());
        edittext.add(editSymptoms.getText().toString());
        edittext.add(editFirstAid.getText().toString());
        edittext.add(editDoctoname.getText().toString());
        edittext.add(editDoctorNumber.getText().toString());

    }
    private class RunUpdateThread implements Runnable {
        @Override
        public void run() {
            Message msg = new Message();
            DbConnect con = new DbConnect();
            con.run();
            textList.add(sharedPref.getString("id", "400"));
            con.UpdateQuery(textList);
        }}

    private class RunSelectThread implements Runnable {
        @Override
        public void run() {
            Message msg = new Message();
            DbConnect con = new DbConnect();
            con.run();
            String sqlSelect = "SELECT * FROM `Diseases` where Risk ="+id;
            textList = con.SelectQuery(sqlSelect);

        }
    }
}
