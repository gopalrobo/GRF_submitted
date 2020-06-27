package com.example.grievanceregistrationform;

import java.io.Serializable;

public class Signbean implements Serializable {

    public String id;
    public String filePath;
    public String name;

    public Signbean(String filePath, String name) {
        this.filePath = filePath;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
