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
public class SimpleMessageImageDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer simpleMessageImageID;
    private String url;
    private String secureUrl;
    private Long dateAdded;
    private Integer simpleMessageID;

    public SimpleMessageImageDTO() {
    }

    public Integer getSimpleMessageImageID() {
        return simpleMessageImageID;
    }

    public void setSimpleMessageImageID(Integer simpleMessageImageID) {
        this.simpleMessageImageID = simpleMessageImageID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSecureUrl() {
        return secureUrl;
    }

    public void setSecureUrl(String secureUrl) {
        this.secureUrl = secureUrl;
    }

    public Long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Integer getSimpleMessageID() {
        return simpleMessageID;
    }

    public void setSimpleMessageID(Integer simpleMessageID) {
        this.simpleMessageID = simpleMessageID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (simpleMessageImageID != null ? simpleMessageImageID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SimpleMessageImageDTO)) {
            return false;
        }
        SimpleMessageImageDTO other = (SimpleMessageImageDTO) object;
        if ((this.simpleMessageImageID == null && other.simpleMessageImageID != null) || (this.simpleMessageImageID != null && !this.simpleMessageImageID.equals(other.simpleMessageImageID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.monitor.data.SimpleMessageImage[ simpleMessageImageID=" + simpleMessageImageID + " ]";
    }
    
}
