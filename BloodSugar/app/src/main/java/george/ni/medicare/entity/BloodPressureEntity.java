package george.ni.medicare.entity;

import george.ni.medicare.utils.DateUtils;

/**
 * Created by Thinkpad on 2018/4/19.
 */

public class BloodPressureEntity {
    private long id;
    private long testTimeStamp;
    private String lowResult;
    private String highResult;
    private int state;//0:正常；1：//删除；
    private int testYear;
    private int testMonth;
    private int testDay;

    public BloodPressureEntity() {
    }

    public BloodPressureEntity(String lowResult,String highResult, long timeMills) {
        this.lowResult = lowResult;
        this.highResult = highResult;
        this.state = 0;
        this.testTimeStamp = timeMills;
        this.testYear = DateUtils.getYearByTimeStamp(testTimeStamp);
        this.testMonth = DateUtils.getMonthByTimeStamp(testTimeStamp);
        this.testDay = DateUtils.getDayByTimeStamp(testTimeStamp);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTestTimeStamp() {
        return testTimeStamp;
    }

    public void setTestTimeStamp(long testTimeStamp) {
        this.testTimeStamp = testTimeStamp;
        this.testYear = DateUtils.getYearByTimeStamp(testTimeStamp);
        this.testMonth = DateUtils.getMonthByTimeStamp(testTimeStamp);
        this.testDay = DateUtils.getDayByTimeStamp(testTimeStamp);
    }

    public String getLowResult() {
        return lowResult;
    }

    public void setLowResult(String lowResult) {
        this.lowResult = lowResult;
    }

    public String getHighResult() {
        return highResult;
    }

    public void setHighResult(String highResult) {
        this.highResult = highResult;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getTestYear() {
        return testYear;
    }


    public int getTestMonth() {
        return testMonth;
    }

    public int getTestDay() {
        return testDay;
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BloodPressureEntity)) {
            return false;
        }
        if (this.getTestTimeStamp() == ((BloodPressureEntity) obj).getTestTimeStamp()) {
            return true;
        }
        return false;
    }
}
