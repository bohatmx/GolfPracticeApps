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
public class CoachPlayerDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer coachPlayerID;
    private Long dateRegistered;
    private Integer coachID;
    private PlayerDTO player;

    public CoachPlayerDTO() {
    }


    public Integer getCoachPlayerID() {
        return coachPlayerID;
    }

    public void setCoachPlayerID(Integer coachPlayerID) {
        this.coachPlayerID = coachPlayerID;
    }

   

    public Long getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Long dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public Integer getCoachID() {
        return coachID;
    }

    public void setCoachID(Integer coachID) {
        this.coachID = coachID;
    }

    public PlayerDTO getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDTO player) {
        this.player = player;
    }

   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (coachPlayerID != null ? coachPlayerID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CoachPlayerDTO)) {
            return false;
        }
        CoachPlayerDTO other = (CoachPlayerDTO) object;
        if ((this.coachPlayerID == null && other.coachPlayerID != null) || (this.coachPlayerID != null && !this.coachPlayerID.equals(other.coachPlayerID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.golfpractice.data.CoachPlayer[ coachPlayerID=" + coachPlayerID + " ]";
    }
    
}
