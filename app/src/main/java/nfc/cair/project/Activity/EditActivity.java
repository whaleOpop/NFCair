package nfc.cair.project.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;

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
    private FloatingActionButton buttonDown;
    private FloatingActionButton buttonSave;
    private FloatingActionButton buttonExit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

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

        buttonExit= findViewById(R.id.buttonExit);
        buttonExit.setVisibility(View.INVISIBLE);


        buttonDown=findViewById(R.id.buttonDLoad);




        buttonSave=findViewById(R.id.buttonSave);
        buttonDown.setOnClickListener(this);
        buttonSave.setOnClickListener(this);

    }

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
                editFio.setText(textList.get(1));
                editPhone.setText(textList.get(2));
                editAboutMe.setText(textList.get(3));
                editCompany.setText(textList.get(4));
                editProfesia.setText(textList.get(5));
                editPhoto.setText(textList.get(6));
                editSiteCompany.setText(textList.get(7));
                editInst.setText(textList.get(8));
                editTel.setText(textList.get(9));
                editVk.setText(textList.get(10));
                break;
            case R.id.buttonSave:

                changeCardViewList(id.toString());
                main.CardViewList.add(new CardView(Integer.valueOf(id),editPhoto.getText().toString(),editCompany.getText().toString()));
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
    private class RunUpdateThread implements Runnable {
        @Override
        public void run() {
            Message msg = new Message();
            DbConnect con = new DbConnect();
            con.run();
            con.UpdateQuery(textList);
        }}

    private class RunSelectThread implements Runnable {
        @Override
        public void run() {
            Message msg = new Message();
            DbConnect con = new DbConnect();
            con.run();
            String sqlSelect = "SELECT * FROM `nfcinfo` where ID ="+id;
            textList = con.SelectQuery(sqlSelect);

        }
    }
}
