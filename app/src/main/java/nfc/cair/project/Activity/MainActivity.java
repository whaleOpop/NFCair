package nfc.cair.project.Activity;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import nfc.cair.project.Adapter.CardViewAdapter;
import nfc.cair.project.Database.DbConnect;
import nfc.cair.project.Model.CardView;
import nfc.cair.project.R;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private ArrayList<String> res = new ArrayList<>();;

    public void setRes(ArrayList<String> res) {
        this.res = res;
    }

    public ArrayList<String> getRes() {
        return res;
    }

    public ArrayList<CardView> CardViewList;
    private RecyclerView mRecyclerView;
    private CardViewAdapter cAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String id;
    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    private NfcAdapter adapter;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static MainActivity instance;
    private Intent intent;

    public void changeForm() {

        Intent intentedit = new Intent(this, EditActivity.class);
        startActivity(intentedit);

    }

    public MainActivity getInstance() {
        if (instance == null) {
            setInstance(this);
        }
        return instance;
    }

    public static void setInstance(MainActivity instance) {
        MainActivity.instance = instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInstance(this);
        setContentView(R.layout.activity_main);
        sharedPref= getSharedPreferences("my_pref", Context.MODE_PRIVATE);
        FloatingActionButton btn = findViewById(R.id.buttonAdd);
        intent = new Intent(this, NfcActivity.class);
        loadData();
        buildRecyclerView();
        adapter = NfcAdapter.getDefaultAdapter(this);
        if (adapter != null && adapter.isEnabled()) {
        } else {
            finish();
        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(intent);
                overridePendingTransition(R.anim.slidein, R.anim.slideout);
                finish();
            }
        });
    }

    private void nfcShowDialogRecorded() {
        new AlertDialog.Builder(this)
                .setTitle("Тег записан")
                .setMessage("Вы можете выйти из приложения и проверить его")
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(R.drawable.ic_baseline_nfc_24)
                .show();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        //Toast.makeText(this, "Nfc receive", Toast.LENGTH_LONG).show();

        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            //Toast.makeText(this,"NfcIntent",Toast.LENGTH_LONG).show();

            RunSelectThread runInsertThread = new RunSelectThread();
            Thread RunSelectThreadT1 = new Thread(runInsertThread);

            RunSelectThreadT1.start();
            try {
                RunSelectThreadT1.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);


            Log.d("Potok", "onNewIntent: "+getRes().get(0));
            NdefRecord textRecord = NdefRecord.createTextRecord("en", getRes().get(0));
            // Используйте textRecord в своем NDEF сообщении
            NdefMessage message = new NdefMessage(textRecord);
            writeNdefMessage(tag, message);
            nfcShowDialogRecorded();

        }
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        IntentFilter[] intentFilters = new IntentFilter[]{};
        adapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
        super.onResume();
    }


    @Override
    protected void onPause() {
        adapter.disableForegroundDispatch(this);
        super.onPause();
    }


    private void formatTag(Tag tag, NdefMessage ndefMessage) {
        try {
            NdefFormatable ndefFormatable = NdefFormatable.get(tag);
            if (ndefFormatable == null) {
                Toast.makeText(this, "Tag is ndef formtable", Toast.LENGTH_LONG);
            }
            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();
            Toast.makeText(this, "Tag is not writble", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
        }
    }


    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage) {
        try {
            if (tag == null) {
                Toast.makeText(this, "Tag object be null", Toast.LENGTH_LONG).show();
                return;
            }
            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                formatTag(tag, ndefMessage);
            } else {
                ndef.connect();
                if (!ndef.isWritable()) {
                    Toast.makeText(this, "Tag is not writble", Toast.LENGTH_LONG).show();
                    ndef.close();
                    return;
                }
                ndef.writeNdefMessage(ndefMessage);
                ndef.close();
                Toast.makeText(this, "Tag writeble", Toast.LENGTH_LONG);
            }
        } catch (Exception e) {

        }
    }


    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        cAdapter = new CardViewAdapter(CardViewList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(cAdapter);


    }


    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(CardViewList);
        editor.putString("task list", json);
        editor.apply();

    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<CardView>>() {
        }.getType();
        CardViewList = gson.fromJson(json, type);

        if (CardViewList == null) {
            CardViewList = new ArrayList<>();
        }
    }

    public void addCardView(String id, String number, String disease) {
        CardViewList.add(new CardView(Integer.parseInt(id), number, disease));
        cAdapter.notifyItemInserted(CardViewList.size());
    }

    public class RunSelectThread implements Runnable {

        @Override
        public void run() {

            DbConnect con = new DbConnect();
            con.run();

            String value = sharedPref.getString("id", "0");
            String sqlSelect = "SELECT Medicine,Name_of_the_disease,Symptoms,First_aid_for_symptoms,Number_Doctor,FIO_Doctor From Diseases where Patient ="+value;

            StringBuilder ress = new StringBuilder();

            ress.append("Информация:");
            for(String item:con.SelectQuery(sqlSelect)){
                ress.append(item);
                ress.append(" ");
            }
            ress.append("\n");
            getRes().add(String.valueOf(ress));
            Log.d("Potok", "run: "+getRes().get(0));



        }


    }


}