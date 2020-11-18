package com.shreehariji.servenotifire.data;

public class Server {
    private String address;
    private Integer checkFailCount;
    private Integer checkFailThreshold;
    private Integer checkInterval;
    private Integer checkStatus;
    private Long checkTimestamp;
    private Boolean disabled;

    /* renamed from: id */
    private Integer f25id;
    private String name;

    public static class STATUS {
        public static final int BUSY = 2;

        /* renamed from: KO */
        public static final int f26KO = 0;
        public static final int NO_CONNECTION = 3;

        /* renamed from: OK */
        public static final int f27OK = 1;
    }

    public Server(Integer _id, String _name, String _address, Integer _checkInterval, Integer _checkNumber) {
        this(_id, _name, _address, _checkInterval, _checkNumber, 0, 2, System.currentTimeMillis() / 1000, Boolean.FALSE);
    }

    public Server(Integer _id, String _name, String _address, Integer _checkInterval, Integer _checkFailThreshold, Integer _checkFailCount, Integer _checkStatus, Long _checkTimestamp, Boolean _disabled) {
        this.f25id = _id;
        this.name = _name;
        this.address = _address;
        this.checkInterval = _checkInterval;
        this.checkFailThreshold = _checkFailThreshold;
        this.checkFailCount = _checkFailCount;
        this.checkStatus = _checkStatus;
        this.checkTimestamp = _checkTimestamp;
        this.disabled = _disabled;
    }

    public Integer getId() {
        return this.f25id;
    }

    public void setId(Integer _id) {
        this.f25id = _id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String _name) {
        this.name = _name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String _address) {
        this.address = _address;
    }

    public Integer getCheckInterval() {
        return this.checkInterval;
    }

    public void setCheckInterval(Integer _checkInterval) {
        this.checkInterval = _checkInterval;
    }

    public Integer getCheckFailThreshold() {
        return this.checkFailThreshold;
    }

    public void setCheckFailThreshold(Integer _checkFailThreshold) {
        this.checkFailThreshold = _checkFailThreshold;
    }

    public Integer getCheckFailCount() {
        return this.checkFailCount;
    }

    public void setCheckFailCount(Integer _checkFailCount) {
        this.checkFailCount = _checkFailCount;
    }

    public Integer getCheckStatus() {
        return this.checkStatus;
    }

    public void setCheckStatus(Integer _checkStatus) {
        this.checkStatus = _checkStatus;
    }

    public Long getCheckTimestamp() {
        return this.checkTimestamp;
    }

    public void setCheckTimestamp(Long _checkTimestamp) {
        this.checkTimestamp = _checkTimestamp;
    }

    public Boolean getDisabled() {
        return this.disabled;
    }

    public void setDisabled(Boolean _disabled) {
        this.disabled = _disabled;
    }
}
