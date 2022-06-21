package nfc.cair.project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import nfc.cair.project.Database.DbConnect;
import nfc.cair.project.R;

public class NfcActivity extends AppCompatActivity implements View.OnClickListener {
    @Nullable

    private int counter = 0;


    FloatingActionButton buttonSave;
    FloatingActionButton buttonExit;
    EditText editFio;
    EditText editPhone;
    EditText editAboutMe;
    EditText editCompany;
    EditText editProfesia;
    EditText editPhoto;
    EditText editSiteCompany;
    EditText editInst;
    EditText editTel;
    EditText editVk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);


        MainActivity.allEds = new ArrayList<View>();


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
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);

        switch (v.getId()) {
            case R.id.buttonExit:
                startActivity(intent);
                overridePendingTransition(R.anim.slidein, R.anim.slideout);
                break;
            case R.id.buttonSave:
                counter++;
                View view = getLayoutInflater().inflate(R.layout.activity_cardview, null);
                FloatingActionButton buttonDelete = view.findViewById(R.id.buttonDelete);
                FloatingActionButton buttonEdit = view.findViewById(R.id.buttonEdit);
                TextView textView = view.findViewById(R.id.textName);
                textView.setText("ЫЫ" + counter);
                MainActivity.allEds.add(view);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DbConnect con = new DbConnect();
                        con.run();
                        final String data = con.InsertData(editFio.getText().toString(), editAboutMe.getText().toString(), editCompany.getText().toString(),
                                editProfesia.getText().toString(), editPhoto.getText().toString(), editSiteCompany.getText().toString(),
                                editInst.getText().toString(), editTel.getText().toString(), editVk.getText().toString());
                        Log.v("OK", data);
                    }
                }).start();

                break;
        }

    }

}
