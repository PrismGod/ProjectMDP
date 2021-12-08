package com.example.projectandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.MahasiswaViewHolder> {

    private ArrayList<Mahasiswa> listMhs;
    TextView tname,tpass,tstatus;
    Button b1,b2;

    public MahasiswaAdapter(ArrayList<Mahasiswa> listMhs) {
        this.listMhs = listMhs;
    }

    @NonNull
    @Override

    public MahasiswaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mhs_item, parent, false);
        MahasiswaViewHolder viewHolder = new MahasiswaViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MahasiswaViewHolder holder, int position) {
        Mahasiswa mhs = listMhs.get(position);
        holder.bind(mhs);
    }

    @Override
    public int getItemCount() {
        return listMhs.size();
    }

    public class MahasiswaViewHolder extends RecyclerView.ViewHolder {
        public MahasiswaViewHolder(@NonNull View itemView) {
            super(itemView);
            tname =itemView.findViewById(R.id.data_username);
            tpass =itemView.findViewById(R.id.data_password);
            tstatus=itemView.findViewById(R.id.data_status);
            b1=itemView.findViewById(R.id.item_btn_ban);
            b2=itemView.findViewById(R.id.item_btn_unban);

            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        public void bind(Mahasiswa mhs) {
            tname.setText("Username : "+mhs.Username);
            tpass.setText("Password : "+mhs.Password);


            if(mhs.banned.equals("1"))
            {
                tstatus.setText("Status : Banned");
            }
            else
            {
                tstatus.setText("Status : Active");
            }
        }
    }
}
