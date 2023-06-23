package com.example.mini_projet_02.models;

public class Color {
    private String name;
    private String code;

    //region Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    //endregion


    public Color(String name, String value) {
        this.setName(name);
        this.setCode(value);
    }
}
