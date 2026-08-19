/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stationservice.Models;

/**
 *
 * @author DELL
 */
public class Client {

    private String nom_client;
    private int total_paye;

    public Client() {
    }

    public Client(String nom_client, int total_paye) {
        this.nom_client = nom_client;
        this.total_paye = total_paye;
    }

    // --- GETTERS ET SETTERS ---

    public String getNom_client() {
        return nom_client;
    }

    public void setNom_client(String nom_client) {
        this.nom_client = nom_client;
    }

    public int getTotal_paye() {
        return total_paye;
    }

    public void setTotal_paye(int total_paye) {
        this.total_paye = total_paye;
    }


    @Override
    public String toString() {
        return "Client{" +
                "nom_client='" + nom_client + '\'' +
                ", total_paye=" + total_paye +
                '}';
    }
}