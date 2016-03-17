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
public class VideoUploadDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer videoUploadID;
    private String youTubeID;
    private Long dateTaken;
    private String url;
    private Integer playerID;
    private Integer practiceSessionID;

    public VideoUploadDTO() {
    }

    public VideoUploadDTO(Integer videoUploadID) {
        this.videoUploadID = videoUploadID;
    }


    public Long getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(Long dateTaken) {
        this.dateTaken = dateTaken;
    }

    public Integer getPlayerID() {
        return playerID;
    }

    public void setPlayerID(Integer playerID) {
        this.playerID = playerID;
    }

    public Integer getPracticeSessionID() {
        return practiceSessionID;
    }

    public void setPracticeSessionID(Integer practiceSessionID) {
        this.practiceSessionID = practiceSessionID;
    }

    public Integer getVideoUploadID() {
        return videoUploadID;
    }

    public void setVideoUploadID(Integer videoUploadID) {
        this.videoUploadID = videoUploadID;
    }

    public String getYouTubeID() {
        return youTubeID;
    }

    public void setYouTubeID(String youTubeID) {
        this.youTubeID = youTubeID;
    }

    

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (videoUploadID != null ? videoUploadID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VideoUploadDTO)) {
            return false;
        }
        VideoUploadDTO other = (VideoUploadDTO) object;
        if ((this.videoUploadID == null && other.videoUploadID != null) || (this.videoUploadID != null && !this.videoUploadID.equals(other.videoUploadID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.golfpractice.data.VideoUpload[ videoUploadID=" + videoUploadID + " ]";
    }
    
}
