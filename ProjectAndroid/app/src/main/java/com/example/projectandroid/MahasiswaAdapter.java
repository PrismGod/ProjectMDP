package com.example.projectandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.MahasiswaViewHolder> {

    private ArrayList<Mahasiswa> listMhs;
    TextView tname,tpass,tstatus;

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
