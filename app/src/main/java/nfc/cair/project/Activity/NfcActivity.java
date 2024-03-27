package nfc.cair.project.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import nfc.cair.project.Database.DbConnect;
import nfc.cair.project.R;

public class NfcActivity extends AppCompatActivity implements View.OnClickListener {


    @Nullable
    ArrayList<String> Edittext = new ArrayList<>();

    @Nullable
    public ArrayList<String> getEdittext() {
        return Edittext;
    }


    private FloatingActionButton buttonDown;
    private FloatingActionButton buttonSave;
    private FloatingActionButton buttonExit;
    private EditText editMedicine;
    private EditText editNameDisease;
    private EditText editSymptoms;
    private EditText editFirstAid;
    private EditText editDoctoname;
    private EditText editDoctorNumber;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        InitializationActivityObj();


    }

    @Override
    public void onClick(View v) {


        Intent intent = new Intent(this, MainActivity.class);

        switch (v.getId()) {
            case R.id.buttonExit:
                startActivity(intent);
                overridePendingTransition(R.anim.slidein, R.anim.slideout);
                finish();
                break;
            case R.id.buttonSave:

                RunSelectThread runSelectThread = new RunSelectThread();
                RunInsertThread runInsertThread = new RunInsertThread();

                Thread RunSelectThreadT1 = new Thread(runSelectThread);
                Thread RunInsertThreadT1 = new Thread(runInsertThread);
                RunSelectThreadT1.start();
                System.out.println("1Thread");
                try {
                    RunSelectThreadT1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("2Thread");
                arrayListAdd(Edittext);

                RunInsertThreadT1.start();
                try {
                    RunInsertThreadT1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                MainActivity.instance.getInstance().addCardView(getEdittext().get(0), editDoctorNumber.getText().toString(), editNameDisease.getText().toString());
                MainActivity.instance.getInstance().saveData();

                startActivity(intent);
                overridePendingTransition(R.anim.slidein, R.anim.slideout);
                finish();

                break;
        }
    }

    public class RunSelectThread implements Runnable {
        protected String id;

        @Override
        public void run() {

            DbConnect con = new DbConnect();
            con.run();
            String sqlSelect = "SELECT COUNT(*)  from `Diseases`";
            id = con.SelectQuery(sqlSelect).get(0);
            getEdittext().add(0, id);

        }
    }

    public class RunInsertThread extends RunSelectThread implements Runnable {


        @Override
        public void run() {
            DbConnect con = new DbConnect();
            con.run();
            if (getEdittext() != null) {
                String sqlInsert = " INSERT INTO `Diseases` (`Risk`, `Medicine`,`Name_of_the_disease`, `Symptoms`, `First_aid_for_symptoms`, `Number_Doctor`, `FIO_Doctor`, `Patient`)";
                getEdittext().add(sharedPref.getString("id", "400"));
                con.InsertQuery(sqlInsert, getEdittext());
            }

        }
    }


    private void InitializationActivityObj() {
        editMedicine = (EditText) findViewById(R.id.medicine);
        editNameDisease = (EditText) findViewById(R.id.nameDisease);
        editSymptoms = (EditText) findViewById(R.id.symptoms);
        editFirstAid = (EditText) findViewById(R.id.firstAid);
        editDoctoname = (EditText) findViewById(R.id.doctoname);
        editDoctorNumber = (EditText) findViewById(R.id.doctorNumber);


        buttonSave = (FloatingActionButton) findViewById(R.id.buttonSave);
        buttonExit = (FloatingActionButton) findViewById(R.id.buttonExit);
        buttonSave.setOnClickListener(this);
        buttonExit.setOnClickListener(this);

        buttonDown = findViewById(R.id.buttonDLoad);
        buttonDown.setVisibility(View.INVISIBLE);
        buttonSave.setVisibility(View.VISIBLE);
        buttonExit.setVisibility(View.VISIBLE);
        sharedPref= getSharedPreferences("my_pref", Context.MODE_PRIVATE);
    }


    private void arrayListAdd(ArrayList<String> edittext) {

        edittext.add(editMedicine.getText().toString());
        edittext.add(editNameDisease.getText().toString());
        edittext.add(editSymptoms.getText().toString());
        edittext.add(editFirstAid.getText().toString());
        edittext.add(editDoctoname.getText().toString());
        edittext.add(editDoctorNumber.getText().toString());


    }

}
