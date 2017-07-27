package com.example.pc.dietapp;

//1 날짜는 어떻게 받아와야하지?
//2 가장 최신 데이터는 어떻게 받아와야하지?
//3 서버에서 데이터를 15개 불러와서 어떻게 함수를 구현해야하지?


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pc.dietapp.Bean.JoinBean;
import com.example.pc.dietapp.Util.Constants;
import com.google.gson.Gson;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class MyWeightActivity extends AppCompatActivity {

    private EditText mEdtTodayWeight;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_weight);

        mEdtTodayWeight = (EditText)findViewById(R.id.edtTodayWeight);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);

        findViewById(R.id.btnAddWeight).setOnClickListener(btnAddOkClick);
        findViewById(R.id.btnDeleteWeight).setOnClickListener(btnDeleteClick);
    }

    private View.OnClickListener btnAddOkClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new WeightAddProc().execute();
        }
    }; //end btnAddOkClick

    private View.OnClickListener btnDeleteClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MyWeightActivity.this);

            builder.setTitle("")
                    .setMessage("오늘의 몸무게를 삭제하시겠습니까?")
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new WeightDeleteProc().execute();
                        }
                    }) //확인버튼 클릭시 이벤트
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }); //취소버튼 클릭시 설정
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };//end btn DeleteOkClick



    //오늘의 몸무게 삭제하는 함수
    private class WeightDeleteProc extends AsyncTask<String, Void, String>{

        public static final String URL_DELETE_WEIGHT_PROC= Constants.BASE_URL+"";
        private String mDate, mD_kg;

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);

            //날짜 어떻게 받아오지..?
            mD_kg = mEdtTodayWeight.getText().toString();

        }

        @Override
        protected String doInBackground(String... params) {

            try{
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

                MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
                //map.add(" " <- 이부분은 memberBean의 이름과 같게 해주어야함!!!!! 꼭!!!!!!!
//                map.add("date", mDate);
                map.add("d_kg", mD_kg);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.ALL.APPLICATION_FORM_URLENCODED);
                HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);

                return restTemplate.postForObject(URL_DELETE_WEIGHT_PROC, request, String.class);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }   //end doInBackground

        @Override
        protected void onPostExecute(String s) {
            mProgressBar.setVisibility(View.INVISIBLE);
            Gson gson = new Gson();
            try{
                JoinBean bean = gson.fromJson(s, JoinBean.class);
                if(bean!=null){
                    if(bean.getResult().equals("ok")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(MyWeightActivity.this);

                        builder.setTitle("")
                                .setMessage("어제보다 약" + (mD_kg) + "감량에 성공하였습니다.")
                                .setCancelable(false)
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                }); //확인버튼 클릭시 이벤트
                    }else {
                        Toast.makeText(MyWeightActivity.this, bean.getResultMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }catch (Exception e){
                Toast.makeText(MyWeightActivity.this, "파싱실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }//end onPostExecute
    }//end class WeightDeleteProc



    //오늘의 몸무게를 추가하는 함수
    private class WeightAddProc extends AsyncTask<String, Void, String>{

        public static final String URL_ADD_WEIGHT_PROC= Constants.BASE_URL+"";
        private String mDate, mD_kg;

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);

            //날짜 어떻게 받아오지..?
            mD_kg = mEdtTodayWeight.getText().toString();

        }

        @Override
        protected String doInBackground(String... params) {

            try{
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

                MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
                //map.add(" " <- 이부분은 memberBean의 이름과 같게 해주어야함!!!!! 꼭!!!!!!!
//                map.add("date", mDate);
                map.add("d_kg", mD_kg);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.ALL.APPLICATION_FORM_URLENCODED);
                HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);

                return restTemplate.postForObject(URL_ADD_WEIGHT_PROC, request, String.class);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }   //end doInBackground

        @Override
        protected void onPostExecute(String s) {
            mProgressBar.setVisibility(View.INVISIBLE);
            Gson gson = new Gson();
            try{
                JoinBean bean = gson.fromJson(s, JoinBean.class);
                if(bean!=null){
                    if(bean.getResult().equals("ok")){
                       Toast.makeText(MyWeightActivity.this, bean.getResultMsg(), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MyWeightActivity.this, bean.getResultMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }catch (Exception e){
                Toast.makeText(MyWeightActivity.this, "파싱실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }//end onPostExecute
    }//end class WeightAddProc
}
