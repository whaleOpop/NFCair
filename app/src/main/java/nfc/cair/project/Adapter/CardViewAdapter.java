package nfc.cair.project.Adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;


import nfc.cair.project.Activity.EditActivity;
import nfc.cair.project.Activity.MainActivity;
import nfc.cair.project.Activity.NfcActivity;
import nfc.cair.project.Database.DbConnect;
import nfc.cair.project.Model.CardView;

import nfc.cair.project.R;


public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {
    private ArrayList<CardView> mList;
    MainActivity main = MainActivity.instance;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public TextView name;
        public FloatingActionButton delete;
        public FloatingActionButton nfc;
        public FloatingActionButton edit;
        public LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.imageIcon);
            name = itemView.findViewById(R.id.textName);
            delete = itemView.findViewById(R.id.buttonDelete);
            edit = itemView.findViewById(R.id.buttonEdit);
            nfc = itemView.findViewById(R.id.buttonLoad);
            layout = itemView.findViewById(R.id.idGroup);
        }
    }

    public CardViewAdapter(ArrayList<CardView> List) {
        mList = List;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cardview, parent, false);
        ViewHolder evh = new ViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardView currentItem = mList.get(position);
        holder.name.setText(currentItem.getTextName());
        Picasso.get().load(currentItem.getIconLink()).resize(90, 90).into(holder.icon);
        holder.layout.setId(currentItem.getId());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCardView(holder);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 deleteCardView(holder);
                                             }
                                         }
        );
        holder.nfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nfcCardViewShowDialog(holder);
                main.setLink(encrypt(String.valueOf(holder.layout.getId()), "34987342"));
            }
        });
    }


    private void editCardView(ViewHolder holder) {
        main.setId(String.valueOf(holder.layout.getId()));
        main.changeForm();

    }

    public static String encrypt(String sourceString, String password) {
        char[] p = password.toCharArray(); // строка в массив символов
        int n = p.length; // Длина пароля
        char[] c = sourceString.toCharArray();
        int m = c.length; // длина строки
        for (int k = 0; k < m; k++) {
            int mima = c[k] + p[k / n]; // шифрование
            c[k] = (char) mima;
        }
        return new String(c);
    }


    private void nfcCardViewShowDialog(ViewHolder holder) {
        new AlertDialog.Builder(holder.layout.getContext())
                .setTitle("Тег выбран")
                .setMessage("Нажмите окей и приложите метку к экрану")
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.ok, null)
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setIcon(R.drawable.ic_baseline_nfc_24)
                .show();

    }

    private void deleteCardView(ViewHolder holder) {
        new AlertDialog.Builder(holder.layout.getContext())
                .setTitle("Удаление")
                .setMessage("ВЫ уверены что хотите удалить визитку : \"" + holder.name.getText().toString() + "\"?")
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        RunDeleteThread runDeleteThread = new RunDeleteThread();
                        runDeleteThread.setId(holder.layout.getId());
                        Thread RunDeleteThreadT1 = new Thread(runDeleteThread);
                        RunDeleteThreadT1.start();
                        try {
                            RunDeleteThreadT1.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        removeWallet(String.valueOf(holder.layout.getId()));
                        System.out.println();
                        holder.layout.removeAllViews();
                        main.saveData();

                    }

                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.drawable.ic_baseline_error_24)
                .show();

    }

    public Boolean removeWallet(String walletName) {

        for (CardView cardView : main.CardViewList) {
            if (cardView.getId().toString().equals(walletName)) {
                return main.CardViewList.remove(cardView);
            }
        }
        return false;
    }

    private class RunDeleteThread implements Runnable {

        Integer id;

        public void setId(Integer id) {
            this.id = id;
        }

        @Override
        public void run() {

            DbConnect con = new DbConnect();
            con.run();
            con.DeleteQuery(id);


        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}