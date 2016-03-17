/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.golfpractice.library.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author aubreyM
 */
public class SimpleMessageDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer simpleMessageID;
    private String message, monitorName, staffName;
    private Long messageDate;
    private Boolean locationRequest;
    private Integer monitorID;
    private Integer companyID;
    private Integer staffID;
    private Integer projectID;
   
    private List<SimpleMessageDestinationDTO> simpleMessageDestinationList;
    private List<SimpleMessageImageDTO> simpleMessageImageList;

    public SimpleMessageDTO() {
    }

    public Integer getSimpleMessageID() {
        return simpleMessageID;
    }

    public void setSimpleMessageID(Integer simpleMessageID) {
        this.simpleMessageID = simpleMessageID;
    }

    public String getMonitorName() {
        return monitorName;
    }

    public void setMonitorName(String monitorName) {
        this.monitorName = monitorName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Integer getMonitorID() {
        return monitorID;
    }

    public void setMonitorID(Integer monitorID) {
        this.monitorID = monitorID;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public Integer getStaffID() {
        return staffID;
    }

    public void setStaffID(Integer staffID) {
        this.staffID = staffID;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Long messageDate) {
        this.messageDate = messageDate;
    }

    public Boolean getLocationRequest() {
        return locationRequest;
    }

    public void setLocationRequest(Boolean locationRequest) {
        this.locationRequest = locationRequest;
    }

    public List<SimpleMessageDestinationDTO> getSimpleMessageDestinationList() {
        return simpleMessageDestinationList;
    }

    public void setSimpleMessageDestinationList(List<SimpleMessageDestinationDTO> simpleMessageDestinationList) {
        this.simpleMessageDestinationList = simpleMessageDestinationList;
    }

    public List<SimpleMessageImageDTO> getSimpleMessageImageList() {
        return simpleMessageImageList;
    }

    public void setSimpleMessageImageList(List<SimpleMessageImageDTO> simpleMessageImageList) {
        this.simpleMessageImageList = simpleMessageImageList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (simpleMessageID != null ? simpleMessageID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SimpleMessageDTO)) {
            return false;
        }
        SimpleMessageDTO other = (SimpleMessageDTO) object;
        if ((this.simpleMessageID == null && other.simpleMessageID != null) || (this.simpleMessageID != null && !this.simpleMessageID.equals(other.simpleMessageID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.monitor.data.SimpleMessage[ simpleMessageID=" + simpleMessageID + " ]";
    }
    
}
