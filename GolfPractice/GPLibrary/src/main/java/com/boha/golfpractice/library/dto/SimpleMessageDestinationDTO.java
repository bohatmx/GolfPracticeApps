/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.golfpractice.library.dto;

import java.io.Serializable;

/**
 *
 * @author aubreyM
 */
public class SimpleMessageDestinationDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer simpleMessageDestinationID;
    private Integer monitorID;
    private Integer simpleMessageID;
    private Integer staffID;

    public SimpleMessageDestinationDTO() {
    }

    public Integer getSimpleMessageDestinationID() {
        return simpleMessageDestinationID;
    }

    public void setSimpleMessageDestinationID(Integer simpleMessageDestinationID) {
        this.simpleMessageDestinationID = simpleMessageDestinationID;
    }

    public Integer getMonitorID() {
        return monitorID;
    }

    public void setMonitorID(Integer monitorID) {
        this.monitorID = monitorID;
    }

    public Integer getSimpleMessageID() {
        return simpleMessageID;
    }

    public void setSimpleMessageID(Integer simpleMessageID) {
        this.simpleMessageID = simpleMessageID;
    }

    public Integer getStaffID() {
        return staffID;
    }

    public void setStaffID(Integer staffID) {
        this.staffID = staffID;
    }

   
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (simpleMessageDestinationID != null ? simpleMessageDestinationID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SimpleMessageDestinationDTO)) {
            return false;
        }
        SimpleMessageDestinationDTO other = (SimpleMessageDestinationDTO) object;
        if ((this.simpleMessageDestinationID == null && other.simpleMessageDestinationID != null) || (this.simpleMessageDestinationID != null && !this.simpleMessageDestinationID.equals(other.simpleMessageDestinationID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.monitor.data.SimpleMessageDestination[ simpleMessageDestinationID=" + simpleMessageDestinationID + " ]";
    }
    
}
