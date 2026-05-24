package com.hairsalon.model;
public class Appointment {
    private int id;
    private String clientName;
    private String serviceName;
    private String stylistName;
    private String date;
    private String time;
    private String status;
    public Appointment(int id, String clientName, String serviceName, String stylistName,
                       String date, String time, String status) {
        this.id = id;
        this.clientName = clientName;
        this.serviceName = serviceName;
        this.stylistName = stylistName;
        this.date = date;
        this.time = time;
        this.status = status;
    }
    public int getId() {
        return id;
    }
    public String getClientName() {
        return clientName;
    }
    public String getServiceName() {
        return serviceName;
    }
}
