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
 * @author aubreymalabie
 */
public class HoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer holeID;
    private Integer holeNumber;
    private Integer lengthFromRed;
    private Integer lengthFromBlue;
    private Integer lengthFromWhite;
    private Integer lengthFromBlack;
    private Integer par;
    private Integer golfCourseID;
    private List<HoleStatDTO> holeStatList;

    public HoleDTO() {
    }

    public Integer getHoleID() {
        return holeID;
    }

    public void setHoleID(Integer holeID) {
        this.holeID = holeID;
    }

    public Integer getHoleNumber() {
        return holeNumber;
    }

    public void setHoleNumber(Integer holeNumber) {
        this.holeNumber = holeNumber;
    }

    public Integer getLengthFromRed() {
        return lengthFromRed;
    }

    public void setLengthFromRed(Integer lengthFromRed) {
        this.lengthFromRed = lengthFromRed;
    }

    public Integer getLengthFromBlue() {
        return lengthFromBlue;
    }

    public void setLengthFromBlue(Integer lengthFromBlue) {
        this.lengthFromBlue = lengthFromBlue;
    }

    public Integer getLengthFromWhite() {
        return lengthFromWhite;
    }

    public void setLengthFromWhite(Integer lengthFromWhite) {
        this.lengthFromWhite = lengthFromWhite;
    }

    public Integer getLengthFromBlack() {
        return lengthFromBlack;
    }

    public void setLengthFromBlack(Integer lengthFromBlack) {
        this.lengthFromBlack = lengthFromBlack;
    }

    public Integer getPar() {
        return par;
    }

    public void setPar(Integer par) {
        this.par = par;
    }

    public Integer getGolfCourseID() {
        return golfCourseID;
    }

    public void setGolfCourseID(Integer golfCourseID) {
        this.golfCourseID = golfCourseID;
    }
 

    public List<HoleStatDTO> getHoleStatList() {
        return holeStatList;
    }

    public void setHoleStatList(List<HoleStatDTO> holeStatList) {
        this.holeStatList = holeStatList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (holeID != null ? holeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HoleDTO)) {
            return false;
        }
        HoleDTO other = (HoleDTO) object;
        if ((this.holeID == null && other.holeID != null) || (this.holeID != null && !this.holeID.equals(other.holeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.golfpractice.data.Hole[ holeID=" + holeID + " ]";
    }
    
}
