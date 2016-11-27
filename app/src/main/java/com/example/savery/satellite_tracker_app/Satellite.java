package com.example.savery.satellite_tracker_app;

public class Satellite {
    private String name;
    private String noradId;
    private String type;

    public Satellite(String noradId, String name) {
        this.name = name;
        this.noradId = noradId;
    }

    public Satellite(String noradId, String name, String type) {
        this.noradId = noradId;
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getNoradId() {
        return noradId;
    }

    public String getType() {
        return type;
    }
}
