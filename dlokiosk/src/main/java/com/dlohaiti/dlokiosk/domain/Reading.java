package com.dlohaiti.dlokiosk.domain;

import java.util.Date;
import java.util.List;

public class Reading {
    private String kiosk = "HARDCODED K1";
    private Date timestamp = new Date();
    private List<Measurement> measurements;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    public String getKiosk() {
        return kiosk;
    }

    public void setKiosk(String kiosk) {
        this.kiosk = kiosk;
    }
}
