package com.codeclinic.yakrm.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codeclinic.yakrm.Activities.EditCardActivity;
import com.codeclinic.yakrm.Models.GetCardListItemModel;
import com.codeclinic.yakrm.Models.RemoveCardModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoredCardListAdapter extends RecyclerView.Adapter<StoredCardListAdapter.Holder> {
    List<GetCardListItemModel> arrayList;
    Context context;
    ProgressDialog progressDialog;
    API apiService;
    SessionManager sessionManager;

    public StoredCardListAdapter(List<GetCardListItemModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public StoredCardListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custoom_stored_list_view, null);
        return new Holder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull StoredCardListAdapter.Holder holder, final int i) {
        holder.tv_card_no.setText("**** **** **** " + arrayList.get(i).getCardNumber().substring(arrayList.get(i).getCardNumber().length() - 4));
        progressDialog = new ProgressDialog(context);
        sessionManager = new SessionManager(context);
        apiService = RestClass.getClient().create(API.class);
        holder.main_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditCardActivity.class);
                intent.putExtra("card_id", arrayList.get(i).getId());
                intent.putExtra("card_no", arrayList.get(i).getCardNumber());
                intent.putExtra("exp_date", arrayList.get(i).getExpiryDate());
                intent.putExtra("name", arrayList.get(i).getHolderName());
                intent.putExtra("security_no", arrayList.get(i).getSecurityNumber());
                intent.putExtra("payment_method", arrayList.get(i).getPaymentMethod());
                context.startActivity(intent);
            }
        });

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.CustomDialogFragment);
                alert.setMessage("Are you Sure?");
                alert.setCancelable(false);
                alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog = new ProgressDialog(context);
                        progressDialog.setMessage("Please Wait");
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("card_id", arrayList.get(i).getId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Call<RemoveCardModel> removeCardModelCall = apiService.REMOVE_CARD_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject.toString());
                        removeCardModelCall.enqueue(new Callback<RemoveCardModel>() {
                            @Override
                            public void onResponse(Call<RemoveCardModel> call, Response<RemoveCardModel> response) {
                                progressDialog.dismiss();
                                String status = response.body().getStatus();
                                if (status.equals("1")) {
                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    arrayList.remove(i);
                                    notifyDataSetChanged();
                                } else {
                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<RemoveCardModel> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tv_card_no, tv_user_name;
        ImageView img_edit, img_delete;
        RelativeLayout main_rl;

        public Holder(@NonNull View itemView) {
            super(itemView);
            main_rl = itemView.findViewById(R.id.main_rl);
            tv_user_name = itemView.findViewById(R.id.tv_user_name);
            tv_card_no = itemView.findViewById(R.id.tv_card_no);
            img_edit = itemView.findViewById(R.id.img_edit);
            img_delete = itemView.findViewById(R.id.img_delete);
        }
    }
}
