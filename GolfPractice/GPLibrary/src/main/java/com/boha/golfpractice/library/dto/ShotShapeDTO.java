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
public class ShotShapeDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer shotShapeID;
    private String shape;

    public ShotShapeDTO() {
    }

    public Integer getShotShapeID() {
        return shotShapeID;
    }

    public void setShotShapeID(Integer shotShapeID) {
        this.shotShapeID = shotShapeID;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (shotShapeID != null ? shotShapeID.hashCode() : 0);
        return hash;
    }


    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ShotShapeDTO)) {
            return false;
        }
        ShotShapeDTO other = (ShotShapeDTO) object;
        if ((this.shotShapeID == null && other.shotShapeID != null) || (this.shotShapeID != null && !this.shotShapeID.equals(other.shotShapeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.golfpractice.data.ShotShape[ shotShapeID=" + shotShapeID + " ]";
    }
    
}
