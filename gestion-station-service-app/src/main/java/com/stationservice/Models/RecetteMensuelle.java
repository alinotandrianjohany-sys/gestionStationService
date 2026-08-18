/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package com.stationservice.Models;

public class RecetteMensuelle {
    private String moisAnnee; // Ex: "04/2026" ou "Avril 2026"
    private double totalMensuel;

    public RecetteMensuelle() {}

    public String getMoisAnnee() { return moisAnnee; }
    public void setMoisAnnee(String moisAnnee) { this.moisAnnee = moisAnnee; }

    public double getTotalMensuel() { return totalMensuel; }
    public void setTotalMensuel(double totalMensuel) { this.totalMensuel = totalMensuel; }
}