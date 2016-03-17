/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.golfpractice.library.dto;

import java.io.Serializable;

/**
 *
 * @author aubreymalabie
 */
public class ClubUsedDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer clubUsedID;
    private ClubDTO club;
    private Integer holeStatID;
    private ShotShapeDTO shotShape;

    public ClubUsedDTO() {
    }

    public ShotShapeDTO getShotShape() {
        return shotShape;
    }

    public void setShotShape(ShotShapeDTO shotShape) {
        this.shotShape = shotShape;
    }

    
    public Integer getHoleStatID() {
        return holeStatID;
    }

    public void setHoleStatID(Integer holeStatID) {
        this.holeStatID = holeStatID;
    }

    public Integer getClubUsedID() {
        return clubUsedID;
    }

    public void setClubUsedID(Integer clubUsedID) {
        this.clubUsedID = clubUsedID;
    }

    public ClubDTO getClub() {
        return club;
    }

    public void setClub(ClubDTO club) {
        this.club = club;
    }

  
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clubUsedID != null ? clubUsedID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClubUsedDTO)) {
            return false;
        }
        ClubUsedDTO other = (ClubUsedDTO) object;
        if ((this.clubUsedID == null && other.clubUsedID != null) || (this.clubUsedID != null && !this.clubUsedID.equals(other.clubUsedID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.golfpractice.data.ClubUsed[ clubUsedID=" + clubUsedID + " ]";
    }
    
}
