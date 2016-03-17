/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.golfpractice.library.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author aubreymalabie
 */
public class GcmDeviceDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer gcmDeviceID;
    private String registrationID;
    private String manufacturer;
    private String model;
    private String product;
    private Date dateRegistered;
    private String serialNumber;
    private String androidVersion;
    private String app;
    private Integer playerID;
    private Integer coachID;

    public GcmDeviceDTO() {
    }

    public Integer getGcmDeviceID() {
        return gcmDeviceID;
    }

    public void setGcmDeviceID(Integer gcmDeviceID) {
        this.gcmDeviceID = gcmDeviceID;
    }

    public String getRegistrationID() {
        return registrationID;
    }

    public void setRegistrationID(String registrationID) {
        this.registrationID = registrationID;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public Integer getPlayerID() {
        return playerID;
    }

    public void setPlayerID(Integer playerID) {
        this.playerID = playerID;
    }

    public Integer getCoachID() {
        return coachID;
    }

    public void setCoachID(Integer coachID) {
        this.coachID = coachID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (gcmDeviceID != null ? gcmDeviceID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GcmDeviceDTO)) {
            return false;
        }
        GcmDeviceDTO other = (GcmDeviceDTO) object;
        if ((this.gcmDeviceID == null && other.gcmDeviceID != null) || (this.gcmDeviceID != null && !this.gcmDeviceID.equals(other.gcmDeviceID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.golfpractice.data.GcmDevice[ gcmDeviceID=" + gcmDeviceID + " ]";
    }
    
}
