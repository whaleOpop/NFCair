package nfc.cair.project.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import nfc.cair.project.Adapter.CardViewAdapter;
import nfc.cair.project.Database.DbConnect;
import nfc.cair.project.Model.CardView;
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
    private EditText editFio;
    private EditText editPhone;
    private EditText editAboutMe;
    private EditText editCompany;
    private EditText editProfesia;
    private EditText editPhoto;
    private EditText editSiteCompany;
    private EditText editInst;
    private EditText editTel;
    private EditText editVk;

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

                MainActivity.instance.getInstance().addCardView(getEdittext().get(0),editCompany.getText().toString(),editPhoto.getText().toString());
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
            String sqlSelect = "SELECT COUNT(*)  from `nfcinfo`";
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
                String sqlInsert = " INSERT INTO `nfcinfo`(`ID`, `FIO`,`NUMBERs`, `ABOUTME`, `COMPANY`, `PROFESION`, `LINKPHOTO`, `SITECOMPANY`, `INST`, `TELE`, `VKON`)";
                con.InsertQuery(sqlInsert, getEdittext());
            }

        }
    }


    private void InitializationActivityObj() {
        editFio = (EditText) findViewById(R.id.editFio);
        editPhone = (EditText) findViewById(R.id.editPhone);
        editAboutMe = (EditText) findViewById(R.id.editAboutMe);
        editCompany = (EditText) findViewById(R.id.editCompany);
        editProfesia = (EditText) findViewById(R.id.editProfesia);
        editPhoto = (EditText) findViewById(R.id.editPhoto);
        editSiteCompany = (EditText) findViewById(R.id.editSiteCompany);
        editInst = (EditText) findViewById(R.id.editInst);
        editTel = (EditText) findViewById(R.id.editTel);
        editVk = (EditText) findViewById(R.id.editVk);


        buttonSave = (FloatingActionButton) findViewById(R.id.buttonSave);
        buttonExit = (FloatingActionButton) findViewById(R.id.buttonExit);
        buttonSave.setOnClickListener(this);
        buttonExit.setOnClickListener(this);

        buttonDown=findViewById(R.id.buttonDLoad);
        buttonDown.setVisibility(View.INVISIBLE);
        buttonSave.setVisibility(View.VISIBLE);
        buttonExit.setVisibility(View.VISIBLE);
    }


    private void arrayListAdd(ArrayList<String> edittext) {

        edittext.add(editFio.getText().toString());
        edittext.add(editPhone.getText().toString());
        edittext.add(editAboutMe.getText().toString());
        edittext.add(editCompany.getText().toString());
        edittext.add(editProfesia.getText().toString());
        edittext.add(editPhoto.getText().toString());
        edittext.add(editSiteCompany.getText().toString());
        edittext.add(editInst.getText().toString());
        edittext.add(editTel.getText().toString());
        edittext.add(editVk.getText().toString());

    }

}
