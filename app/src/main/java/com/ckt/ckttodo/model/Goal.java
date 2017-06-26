package com.ckt.ckttodo.model;

import com.yalantis.beamazingtoday.interfaces.BatModel;


public class Goal implements BatModel {

    private String planId;

    private String name;

    private boolean isChecked;

    public Goal(String name) {
        this.name = name;
    }

    public Goal(String name, String planId) {
        this.name = name;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public String getText() {
        return getName();
    }

}
