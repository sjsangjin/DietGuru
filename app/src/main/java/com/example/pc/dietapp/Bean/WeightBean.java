package com.example.pc.dietapp.Bean;

import java.util.List;

/**
 * Created by pc on 2017-07-27.
 */

public class WeightBean extends CommonBean{

    private WeightBeanSub weightBean;
    private List<WeightBeanSub> weightList;

    class WeightBeanSub{
        private String date;
        private String d_kg;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getD_kg() {
            return d_kg;
        }

        public void setD_kg(String d_kg) {
            this.d_kg = d_kg;
        }
    }

    public WeightBeanSub getWeightBean() {
        return weightBean;
    }

    public void setWeightBean(WeightBeanSub weightBean) {
        this.weightBean = weightBean;
    }

    public List<WeightBeanSub> getWeightList() {
        return weightList;
    }

    public void setWeightList(List<WeightBeanSub> weightList) {
        this.weightList = weightList;
    }
}
