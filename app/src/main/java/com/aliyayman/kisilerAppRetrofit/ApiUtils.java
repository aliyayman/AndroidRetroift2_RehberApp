package com.aliyayman.kisilerAppRetrofit;

public class ApiUtils {

    public static final String BASE_URL="http://www.byrmkus.tk/";

    public static KisilerDaoInterface getKisilerDaoInterface(){
        return RetrofitClient.getClient(BASE_URL).create(KisilerDaoInterface.class);


    }
}
