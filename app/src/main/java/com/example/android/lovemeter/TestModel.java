package com.example.android.lovemeter;

class TestModel {
    String testerName;
    String testNumber;

    public TestModel(String testerName, String testNumber) {
        this.testerName = testerName;
        this.testNumber = testNumber;
    }

    public String getTesterName() {
        return testerName;
    }

    public void setTesterName(String testerName) {
        this.testerName = testerName;
    }

    public String getTestNumber() {
        return testNumber;
    }

    public void setTestNumber(String testNumber) {
        this.testNumber = testNumber;
    }
}
