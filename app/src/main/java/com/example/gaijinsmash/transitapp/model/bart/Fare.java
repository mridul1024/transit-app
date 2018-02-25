package com.example.gaijinsmash.transitapp.model.bart;

public class Fare {
    private String fareAmount, fareClass, fareName;

    public void setFareAmount(String fareAmount) { this.fareAmount = fareAmount; }
    public void setFareClass(String fareClass) { this.fareClass = fareClass; }
    public void setFareName(String fareName) { this.fareName = fareName; }
    public String getFareAmount() { return fareAmount; }
    public String getFareClass() { return fareClass; }
    public String getFareName() { return fareName; }
}
