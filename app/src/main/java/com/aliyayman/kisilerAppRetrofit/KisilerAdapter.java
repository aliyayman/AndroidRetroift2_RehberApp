package com.aliyayman.kisilerAppRetrofit;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class  KisilerAdapter extends RecyclerView.Adapter<KisilerAdapter.CardNesneleriTutucu>{
    private Context mContext;
    private List<Kisiler> kisilerList;
    private KisilerDaoInterface kisilerDaoInterface;

    public KisilerAdapter(Context mContext, List<Kisiler> kisilerList, KisilerDaoInterface kisilerDaoInterface) {
        this.mContext = mContext;
        this.kisilerList = kisilerList;
        this.kisilerDaoInterface = kisilerDaoInterface;
    }

    @NonNull
    @Override
    public CardNesneleriTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.kisicard_tasarim,parent,false);

        return new CardNesneleriTutucu(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardNesneleriTutucu holder, int position) {
        Kisiler kisi=kisilerList.get(position);

        holder.textViewBilgi.setText(kisi.getKisiAd()+"-"+kisi.getKisiTel());

        holder.imageViewNokta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu=new PopupMenu(mContext,holder.imageViewNokta);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()){
                            case R.id.action_sil:
                                Snackbar.make(holder.imageViewNokta,"Kişi silinsin mi?",Snackbar.LENGTH_SHORT)
                                        .setAction("Evet", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                kisilerDaoInterface.kisiSil(Integer.parseInt(kisi.getKisiId())).enqueue(new Callback<CRUDCevap>() {
                                                    @Override
                                                    public void onResponse(Call<CRUDCevap> call, Response<CRUDCevap> response) {

                                                        tumKisiler();
                                                    }

                                                    @Override
                                                    public void onFailure(Call<CRUDCevap> call, Throwable t) {

                                                    }
                                                });




                                            }
                                        })
                                        .show();
                                return true;
                            case R.id.action_duzenle:
                                alertGoster(kisi);
                                return true;
                            default:
                                return false;

                        }
                    }
                });


                popupMenu.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return kisilerList.size();
    }

    public class CardNesneleriTutucu extends RecyclerView.ViewHolder{
        private TextView textViewBilgi;
        private ImageView imageViewNokta;

        public CardNesneleriTutucu(@NonNull View itemView) {
            super(itemView);
            textViewBilgi=itemView.findViewById(R.id.textViewBilgi);
            imageViewNokta=itemView.findViewById(R.id.imageViewNokta);

        }
    }
    public void alertGoster(Kisiler kisi){
        LayoutInflater layout=LayoutInflater.from(mContext);
        View view=layout.inflate(R.layout.alert_tasarim,null);

        EditText edtAd=view.findViewById(R.id.edtAd);
        EditText edtTel=view.findViewById(R.id.edtTel);

        edtAd.setText(kisi.getKisiAd());
        edtTel.setText(kisi.getKisiTel());

        AlertDialog.Builder ad=new AlertDialog.Builder(mContext);
        ad.setTitle("Kişi Güncelle");
        ad.setView(view);
        ad.setPositiveButton("Güncelle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String kisi_ad=edtAd.getText().toString().trim();
                String kisi_tel=edtTel.getText().toString().trim();
                int kisi_id=Integer.parseInt(kisi.getKisiId());


                kisilerDaoInterface.kisiGuncelle(kisi_id,kisi_ad,kisi_tel).enqueue(new Callback<CRUDCevap>() {
                    @Override
                    public void onResponse(Call<CRUDCevap> call, Response<CRUDCevap> response) {
                        tumKisiler();
                    }

                    @Override
                    public void onFailure(Call<CRUDCevap> call, Throwable t) {

                    }
                });

            }
        });
        ad.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        ad.create().show();

    }
    public void tumKisiler(){

        kisilerDaoInterface.tumKisiler().enqueue(new Callback<KisilerCevap>() {
            @Override
            public void onResponse(Call<KisilerCevap> call, Response<KisilerCevap> response) {

               kisilerList=response.body().getKisiler();
               notifyDataSetChanged();



            }

            @Override
            public void onFailure(Call<KisilerCevap> call, Throwable t) {

            }
        });
    }

}
