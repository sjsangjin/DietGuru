package com.example.pc.dietapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MemUpActivity extends AppCompatActivity {

    private EditText mEdtUpId, mEdtUpPw, mEdtUpCm, mEdtUpKg, mEdtUpHkg, mEdtUpWord;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mem_up);

        mEdtUpId = (EditText)findViewById(R.id.edtUpId);
        mEdtUpPw = (EditText)findViewById(R.id.edtUpPw);
        mEdtUpCm = (EditText)findViewById(R.id.edtUpCm);
        mEdtUpKg = (EditText)findViewById(R.id.edtUpKg);
        mEdtUpHkg = (EditText)findViewById(R.id.edtUpHkg);
        mEdtUpWord = (EditText)findViewById(R.id.edtUpWord);

        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);

        findViewById(R.id.btnUpOk).setOnClickListener(btnUpClick);
    }//end OnCreate



    private View.OnClickListener btnUpClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MemUpActivity.this);

            builder.setTitle("회원정보 수정확인")
                    .setMessage("정보를 수정하시겠습니까?")
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new UpTask().execute();
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
    };  //end btnUpClick



    private class UpTask extends AsyncTask<String, Void, String> {
        public static final String URL_UP_PROC= Constants.BASE_URL+"rest/updateMember.do";
        private String userId, userPw, userCm, userKg, userHkg, userWord;

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);

            userId = mEdtUpId.getText().toString();
            userPw = mEdtUpPw.getText().toString();
            userCm = mEdtUpCm.getText().toString();
            userKg = mEdtUpKg.getText().toString();
            userHkg = mEdtUpHkg.getText().toString();
            userWord = mEdtUpWord.getText().toString();
        }

        @Override
        protected String doInBackground(String... params) {

            try{
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

                MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
                //map.add(" " <- 이부분은 memberBean의 이름과 같게 해주어야함!!!!! 꼭!!!!!!!
                map.add("userId", userId);
                map.add("userPw", userPw);
                map.add("cm", userCm);
                map.add("kg", userKg);
                map.add("h_kg", userHkg);
                map.add("word", userWord);


                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.ALL.APPLICATION_FORM_URLENCODED);
                HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);

                return restTemplate.postForObject(URL_UP_PROC, request, String.class);
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
                        finish();
                    }else {
                        Toast.makeText(MemUpActivity.this, bean.getResultMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }catch (Exception e){
                Toast.makeText(MemUpActivity.this, "파싱실패", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }//end onPostExecute

    }//end Task


}
