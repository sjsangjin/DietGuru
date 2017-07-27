package com.example.pc.dietapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import org.springframework.util.comparator.BooleanComparator;
import org.springframework.web.client.RestTemplate;


//자동로그인 구현해야함!

public class LoginActivity extends AppCompatActivity {

    private EditText mEdtUserId, mEdtUserPw;
    private ProgressBar mProgressBar;
    private CheckBox mAutoLogin;
    String loginId, loginPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEdtUserId = (EditText)findViewById(R.id.edtUserId);
        mEdtUserPw = (EditText)findViewById(R.id.edtUserPw);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        mAutoLogin = (CheckBox)findViewById(R.id.chkAutoLogin);



        findViewById(R.id.btnJoin).setOnClickListener(btnJoinClick);
        findViewById(R.id.btnLogin).setOnClickListener(btnLoginClick);
    }

    private View.OnClickListener btnJoinClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(LoginActivity.this, JoinActivity.class);
            startActivity(i);
        }
    };  //end btnJoinClick

    private View.OnClickListener btnLoginClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new LoginProcTask().execute();
        }
    };  //end btnLoginClick

    //로그인처리
    private class LoginProcTask extends AsyncTask<String, Void, String> {

        public static final String URL_LOGIN_PROC= Constants.BASE_URL+"/rest/loginProc.do";
        private String userId, userPw;

        @Override
        protected void onPreExecute() {
            //프로그래스 다이얼로그 표시
            mProgressBar.setVisibility(View.VISIBLE);

            userId = mEdtUserId.getText().toString();
            userPw = mEdtUserPw.getText().toString();
        }

        @Override
        protected String doInBackground(String... params) {

            try{
                RestTemplate restTemplate = new RestTemplate();
                //restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

                MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
                map.add("userId",userId);
                map.add("userPw",userPw);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.ALL.APPLICATION_FORM_URLENCODED);
                HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);

                return restTemplate.postForObject(URL_LOGIN_PROC, request, String.class);
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }//end doInBackgroud

        @Override
        protected void onPostExecute(String s) {
            mProgressBar.setVisibility(View.INVISIBLE);
            Gson gson = new Gson();
            try {
                JoinBean bean = gson.fromJson(s, JoinBean.class);
                if(bean!=null){
                    if(bean.getResult().equals("ok")) {
                        //로그인성공
                        Intent i = new Intent(LoginActivity.this, JoinActivity.class);
                        i.putExtra("joinBean", bean.getJoinBean());
                        startActivity(i);
                    }else {
                        //로그인실패
                        Toast.makeText(LoginActivity.this, "로그인실패", Toast.LENGTH_SHORT).show();
                    }
                }
            }catch (Exception e){
                Toast.makeText(LoginActivity.this, "파싱실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }//end onPostExecute
    }//end class
}
