package com.aliyayman.kisilerAppRetrofit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private Toolbar toolbar;
    private RecyclerView rv;
    private FloatingActionButton fab;
    private ArrayList<Kisiler> kisilerList;
    private KisilerAdapter adapter;
    private KisilerDaoInterface kisilerDaoInterface;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.toolbar);
        rv=findViewById(R.id.rv);
        fab=findViewById(R.id.fab);



        toolbar.setTitle("Kişiler Uygulması");
        setSupportActionBar(toolbar);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        kisilerDaoInterface=ApiUtils.getKisilerDaoInterface();

        tumKisiler();




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertGoster();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        MenuItem menuItem=menu.findItem(R.id.action_ara);
        SearchView searchView=(SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.e("aranan kelime",query);
        kisiAra(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        rv.setAdapter(adapter);

        kisiAra(newText);
        Log.e("anlık kelime",newText);
        return false;
    }
    public void alertGoster(){
        LayoutInflater layout=LayoutInflater.from(this);
        View view=layout.inflate(R.layout.alert_tasarim,null);

        EditText edtAd=view.findViewById(R.id.edtAd);
        EditText edtTel=view.findViewById(R.id.edtTel);

        AlertDialog.Builder ad=new AlertDialog.Builder(this);
        ad.setTitle("Kişi ekle");
        ad.setView(view);
        ad.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String kisi_ad=edtAd.getText().toString().trim();
                String kisi_tel=edtTel.getText().toString().trim();


                kisilerDaoInterface.kisiEkle(kisi_ad,kisi_tel).enqueue(new Callback<CRUDCevap>() {
                    @Override
                    public void onResponse(Call<CRUDCevap> call, Response<CRUDCevap> response) {
                       tumKisiler();
                    }

                    @Override
                    public void onFailure(Call<CRUDCevap> call, Throwable t) {

                    }
                });

                rv.setAdapter(adapter);

                //Toast.makeText(getApplicationContext(),kisi_ad+"-"+kisi_tel,Toast.LENGTH_SHORT).show();


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

                List<Kisiler> kisiler=response.body().getKisiler();


                adapter=new KisilerAdapter(MainActivity.this,kisiler,kisilerDaoInterface);
                rv.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<KisilerCevap> call, Throwable t) {

            }
        });
    }
    public void kisiAra(String kisi_ad){

        kisilerDaoInterface.kisiAra(kisi_ad).enqueue(new Callback<KisilerCevap>() {
            @Override
            public void onResponse(Call<KisilerCevap> call, Response<KisilerCevap> response) {
                List<Kisiler> kisiler=response.body().getKisiler();


                adapter=new KisilerAdapter(MainActivity.this,kisiler,kisilerDaoInterface);
                rv.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<KisilerCevap> call, Throwable t) {

            }
        });

    }



}