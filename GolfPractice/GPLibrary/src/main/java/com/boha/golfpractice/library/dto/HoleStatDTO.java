/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.golfpractice.library.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aubreymalabie
 */
public class HoleStatDTO implements Serializable, Comparable<HoleStatDTO> {

    private static final long serialVersionUID = 1L;
    private Integer holeStatID, mistakes = 0;
    private Boolean fairwayHit = false;
    private Boolean fairwayBunkerHit = false;
    private Integer distanceToPin = 0;
    private Boolean greenInRegulation = false;
    private Integer numberOfPutts = 0;
    private Boolean greensideBunkerHit  = false;
    private Integer score = 0;
    private String remarks;
    private Boolean inRough = false;
    private Boolean inWater  = false;
    private Boolean outOfBounds = false;
    private Integer practiceSessionID;
    private HoleDTO hole;
    private Double lengthOfPutt = Double.valueOf(0);
    private List<ClubUsedDTO> clubUsedList;

    public HoleStatDTO() {
    }

    public Integer getMistakes() {
        return mistakes;
    }

    public void setMistakes(Integer mistakes) {
        this.mistakes = mistakes;
    }

    public Double getLengthOfPutt() {
        return lengthOfPutt;
    }

    public void setLengthOfPutt(Double lengthOfPutt) {
        this.lengthOfPutt = lengthOfPutt;
    }

    
    public Integer getHoleStatID() {
        return holeStatID;
    }

    public void setHoleStatID(Integer holeStatID) {
        this.holeStatID = holeStatID;
    }

    public Boolean getFairwayHit() {
        return fairwayHit;
    }

    public void setFairwayHit(Boolean fairwayHit) {
        this.fairwayHit = fairwayHit;
    }

    public Boolean getFairwayBunkerHit() {
        return fairwayBunkerHit;
    }

    public void setFairwayBunkerHit(Boolean fairwayBunkerHit) {
        this.fairwayBunkerHit = fairwayBunkerHit;
    }

    public Integer getDistanceToPin() {
        return distanceToPin;
    }

    public void setDistanceToPin(Integer distanceToPin) {
        this.distanceToPin = distanceToPin;
    }

    public Boolean getGreenInRegulation() {
        return greenInRegulation;
    }

    public void setGreenInRegulation(Boolean greenInRegulation) {
        this.greenInRegulation = greenInRegulation;
    }

    public Integer getNumberOfPutts() {
        return numberOfPutts;
    }

    public void setNumberOfPutts(Integer numberOfPutts) {
        this.numberOfPutts = numberOfPutts;
    }

    public Boolean getGreensideBunkerHit() {
        return greensideBunkerHit;
    }

    public void setGreensideBunkerHit(Boolean greensideBunkerHit) {
        this.greensideBunkerHit = greensideBunkerHit;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean getInRough() {
        return inRough;
    }

    public void setInRough(Boolean inRough) {
        this.inRough = inRough;
    }

    public Boolean getInWater() {
        return inWater;
    }

    public void setInWater(Boolean inWater) {
        this.inWater = inWater;
    }

    public Boolean getOutOfBounds() {
        return outOfBounds;
    }

    public void setOutOfBounds(Boolean outOfBounds) {
        this.outOfBounds = outOfBounds;
    }

    public Integer getPracticeSessionID() {
        return practiceSessionID;
    }

    public void setPracticeSessionID(Integer practiceSessionID) {
        this.practiceSessionID = practiceSessionID;
    }

    public HoleDTO getHole() {
        return hole;
    }

    public void setHole(HoleDTO hole) {
        this.hole = hole;
    }

    public List<ClubUsedDTO> getClubUsedList() {
        if (clubUsedList == null) {
            clubUsedList = new ArrayList<>();
        }
        return clubUsedList;
    }

    public void setClubUsedList(List<ClubUsedDTO> clubUsedList) {
        this.clubUsedList = clubUsedList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (holeStatID != null ? holeStatID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof HoleStatDTO)) {
            return false;
        }
        HoleStatDTO other = (HoleStatDTO) object;
        if ((this.holeStatID == null && other.holeStatID != null) || (this.holeStatID != null && !this.holeStatID.equals(other.holeStatID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.golfpractice.data.HoleStat[ holeStatID=" + holeStatID + " ]";
    }

    @Override
    public int compareTo(HoleStatDTO another) {
        try {
            if (this.hole.getHoleNumber() < another.hole.getHoleNumber()) {
                return -1;
            }
            if (this.hole.getHoleNumber() > another.hole.getHoleNumber()) {
                return 1;
            }
        } catch (Exception e){};
        return 0;
    }
}
