package com.application.upstox.model;

/**
 * Created by harsh.jain on 5/6/18.
 */

public class Data {

    private long timestamp;
    private float open;
    private float high;
    private float low;
    private float close;
    private float volume;

    public Data(String response) {

        String data[] = response.split(",");

        this.timestamp = Long.parseLong(data[0])/1000L;
        this.open = Float.parseFloat(data[1]);
        this.high = Float.parseFloat(data[2]);;
        this.low = Float.parseFloat(data[3]);;
        this.close = Float.parseFloat(data[4]);;
        this.volume = Float.parseFloat(data[5]);;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public float getOpen() {
        return open;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public float getClose() {
        return close;
    }

    public void setClose(float close) {
        this.close = close;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }
}
