package com.sample.rest.server.controllers.transferobjects;

public class BookingCommand {

    private Integer code;
    private String date;
    private Integer travelers;

    public BookingCommand(Integer code, String date, Integer travelers) {
        this.code = code;
        this.date = date;
        this.travelers = travelers;
    }

    public BookingCommand() {
        // Needed for JAX-RS
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getTravelers() {
        return travelers;
    }

    public void setTravelers(Integer travelers) {
        this.travelers = travelers;
    }
}
