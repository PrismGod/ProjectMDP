package com.example.projectandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.MahasiswaViewHolder> {

    private ArrayList<Mahasiswa> listMhs;
    TextView tname,tpass,tstatus;
    Button b1,b2;
    Context base;
    public String url;

    public MahasiswaAdapter(ArrayList<Mahasiswa> listMhs) {
        this.listMhs = listMhs;
    }

    @NonNull
    @Override

    public MahasiswaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mhs_item, parent, false);
        MahasiswaViewHolder viewHolder = new MahasiswaViewHolder(view);
        base = parent.getContext();
        url=AdminActivity.urldatabase;
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
                    AdminActivity.banMhsStatic(tname.getText().toString(),AdminActivity.urldatabase,base);
                }
            });

            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AdminActivity.unbanMhsStatic(tname.getText().toString(),AdminActivity.urldatabase,base);
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

//    private void banMhs(){
//        StringRequest _StringRequest = new StringRequest(
//                Request.Method.POST,
//                url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        System.out.println(response);
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            int code = jsonObject.getInt("code");
//                            String message = jsonObject.getString("message");
//                            toaster(message);
//                            System.out.println(message);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                //handle error
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        System.out.println(error);
//                    }
//                }
//        ){
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> params = new HashMap<>();
//                params.put("function","banmhs");
//                params.put("username",tname.getText().toString());
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(base);
//        requestQueue.add(_StringRequest);
//    }

//    private void unbanMhs(){
//        StringRequest _StringRequest = new StringRequest(
//                Request.Method.POST,
//                url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        System.out.println(response);
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            int code = jsonObject.getInt("code");
//                            String message = jsonObject.getString("message");
//                            toaster(message);
//                            System.out.println(message);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                //handle error
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        System.out.println(error);
//                    }
//                }
//        ){
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> params = new HashMap<>();
//                params.put("function","unbanmhs");
//                params.put("username",tname.getText().toString());
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(base);
//        requestQueue.add(_StringRequest);
//    }

//    public void toaster(String a)
//    {
//        Context context = base;
//        CharSequence text = a;
//        int duration = Toast.LENGTH_SHORT;
//        Toast toast = Toast.makeText(context, text, duration);
//        toast.show();
//    }
}
