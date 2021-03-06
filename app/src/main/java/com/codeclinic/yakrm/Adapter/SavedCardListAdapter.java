package com.codeclinic.yakrm.Adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codeclinic.yakrm.Models.GetCardListItemModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Utils.SessionManager;

import java.util.List;

public class SavedCardListAdapter extends RecyclerView.Adapter<SavedCardListAdapter.Holder> {
    List<GetCardListItemModel> arrayList;
    Context context;
    API apiService;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    Double price;
    String card_holder_name, card_expiry_date, card_cvv, card_no;

    private final OnItemClickListener listener;

    public SavedCardListAdapter(List<GetCardListItemModel> arrayList, Context context, API apiService, ProgressDialog progressDialog, SessionManager sessionManager, Double price, OnItemClickListener listener) {
        this.arrayList = arrayList;
        this.context = context;
        this.progressDialog = progressDialog;
        this.apiService = apiService;
        this.sessionManager = sessionManager;
        this.price = price;
        this.listener = listener;

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SavedCardListAdapter.Holder holder, final int i) {

        if (arrayList.get(i).getPaymentMethod().equals("1")) {
            holder.img_visa.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_payment_csmada_icon));
        }
        holder.tv_card_no_visa.setText("**** **** **** " + arrayList.get(i).getCardNumber().substring(arrayList.get(i).getCardNumber().length() - 4));

   /*     holder.ll_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.CustomDialogFragment);
                alert.setMessage("Are you Sure?");
                alert.setCancelable(false);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        CompletingPurchasingActivity.card_ID = arrayList.get(i).getId();
                        String[] card_date = null;
                        card_cvv = arrayList.get(i).getSecurityNumber();
                        card_holder_name = arrayList.get(i).getHolderName();
                        card_expiry_date = arrayList.get(i).getExpiryDate();
                        card_date = card_expiry_date.split("/");
                        card_date[0] = card_date[0].trim();
                        card_no = arrayList.get(i).getCardNumber();

                        double main_price = price - Double.parseDouble(sessionManager.getUserDetails().get(SessionManager.Wallet));

                        progressDialog.setMessage("Please Wait");
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        Call<PrepareTransactionProcessModel> prepareTransactionProcessModelCall = apiService.prepareTransaction("ex2SHCqdgtJlrF2gp5fGCis3tUGh5EkjcmcTZD7g6RCxwEOWJ3Cml4qOY664KroXOBQNeY3lPFTlkHh4KUq6YQVXW22HtrFh2w4g", "ahmed@yakrm.com", main_price, "Paytabs", card_holder_name, "test", card_date[1] + card_date[0], card_cvv, "4000000000000002", "SDK", "SAR", "ahmed@yakrm.com", "1", "00966551432252", "123456", "Product 1, Product 2", "jaypokarjdp@gmail.com", "BHR", "Flat 1,Building 123, Road 2345", "Manama", "Manama", "00973", "BHR", "Flat 1,Building 123, Road 2345", "Manama", "Manama", "00973", "1", "TRUE", "no", null, null, null);
                        prepareTransactionProcessModelCall.enqueue(new Callback<PrepareTransactionProcessModel>() {
                            @Override
                            public void onResponse(Call<PrepareTransactionProcessModel> call, Response<PrepareTransactionProcessModel> response) {
                                progressDialog.dismiss();
                                Intent in = new Intent(context, SecurePaymentActivity.class);
                                JSONObject jsonObjectData = new JSONObject();

                                try {
                                    jsonObjectData.put("merchant_email", "ahmed@yakrm.com");
                                    jsonObjectData.put("merchant_secret", "ex2SHCqdgtJlrF2gp5fGCis3tUGh5EkjcmcTZD7g6RCxwEOWJ3Cml4qOY664KroXOBQNeY3lPFTlkHh4KUq6YQVXW22HtrFh2w4g");
                                    jsonObjectData.put("processor", "1");
                                    jsonObjectData.put("acsUrl", response.body().getPayerAuthEnrollReply().getAcsURL());
                                    jsonObjectData.put("paReq", response.body().getPayerAuthEnrollReply().getPaReq());
                                    jsonObjectData.put("xid", response.body().getPayerAuthEnrollReply().getXid());
                                    jsonObjectData.put("local_transaction_id", response.body().getLocalTransactionId());
                                    jsonObjectData.put("merchant_id", response.body().getMerchantId());
                                    jsonObjectData.put("rating", "3");
                                    jsonObjectData.put("signature", "test");
                                    jsonObjectData.put("amount", price - Double.parseDouble(sessionManager.getUserDetails().get(SessionManager.Wallet)));
                                    jsonObjectData.put("currency", "SAR");
                                    jsonObjectData.put("store_name", "Yakrm");
                                    jsonObjectData.put("cc_holder_name", card_holder_name);
                                    jsonObjectData.put("is_tokenization", "TRUE");
                                } catch (JSONException var19) {
                                    var19.printStackTrace();
                                }
                                in.putExtra("data", jsonObjectData.toString());
                                in.putExtra("language", "en");
                                ((Activity) context).startActivityForResult(in, 1001);
                            }

                            @Override
                            public void onFailure(Call<PrepareTransactionProcessModel> call, Throwable t) {
                                progressDialog.dismiss();
                                Log.i("response", t.toString());
                            }
                        });
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });*/

        holder.ll_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetCardListItemModel getCardListItemModel = arrayList.get(i);
                listener.onItemClick(getCardListItemModel);
            }
        });
    }

    @NonNull
    @Override
    public SavedCardListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_saved_card_list_view, null);
        return new Holder(view);
    }

    public interface OnItemClickListener {
        void onItemClick(GetCardListItemModel item);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tv_card_no_visa;
        ImageView img_visa;
        LinearLayout ll_card;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tv_card_no_visa = itemView.findViewById(R.id.tv_card_no_visa);
            img_visa = itemView.findViewById(R.id.img_visa);
            ll_card = itemView.findViewById(R.id.ll_card);
        }
    }
}
