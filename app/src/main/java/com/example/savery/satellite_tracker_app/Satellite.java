package com.example.savery.satellite_tracker_app;

public class Satellite {
    private String name;
    private String noradId;

    public Satellite(String name, String noradId) {
        this.name = name;
        this.noradId = noradId;
    }

    public String getName() {
        return name;
    }

    public String getNoradId() {
        return noradId;
    }

}
