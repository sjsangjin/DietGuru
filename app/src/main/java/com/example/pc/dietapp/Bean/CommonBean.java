package com.example.pc.dietapp.Bean;

import java.io.Serializable;

/**
 * Created by pc on 2017-07-21.
 */

public class CommonBean implements Serializable {

    private String result;
    private String resultMsg;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }
}
