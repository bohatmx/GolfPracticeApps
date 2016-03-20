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
public class ResponseDTO implements Serializable {
    private int statusCode;
    private String message, GCMRegistrationID;
    private double elapsedSeconds;
    
    private List<PlayerDTO> playerList;
    private List<CoachDTO> coachList;
    private List<GolfCourseDTO> golfCourseList;
    private List<HoleStatDTO> holeStatList;
    private List<PracticeSessionDTO> practiceSessionList;
    private List<VideoUploadDTO> videoUploadList;
    private List<ClubDTO> clubList;
    private List<HoleDTO> holeList;
    private List<ShotShapeDTO> shotShapeList;

    public List<ShotShapeDTO> getShotShapeList() {
        if (shotShapeList == null) {
            shotShapeList = new ArrayList<>();
        }
        return shotShapeList;
    }

    public void setShotShapeList(List<ShotShapeDTO> shotShapeList) {
        this.shotShapeList = shotShapeList;
    }

    public double getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void setElapsedSeconds(double elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
    }

    public String getGCMRegistrationID() {
        return GCMRegistrationID;
    }

    public void setGCMRegistrationID(String GCMRegistrationID) {
        this.GCMRegistrationID = GCMRegistrationID;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PlayerDTO> getPlayerList() {
        if (playerList == null) {
            playerList = new ArrayList<>();
        }
        return playerList;
    }

    public void setPlayerList(List<PlayerDTO> playerList) {
        this.playerList = playerList;
    }

    public List<CoachDTO> getCoachList() {
        if (coachList == null) {
            coachList = new ArrayList<>();
        }
        return coachList;
    }

    public void setCoachList(List<CoachDTO> coachList) {
        this.coachList = coachList;
    }

    public List<GolfCourseDTO> getGolfCourseList() {
        if (golfCourseList == null) {
            golfCourseList = new ArrayList<>();
        }
        return golfCourseList;
    }

    public List<HoleDTO> getHoleList() {
        if (holeList == null) {
            holeList = new ArrayList<>();
        }
        return holeList;
    }

    public void setHoleList(List<HoleDTO> holeList) {
        this.holeList = holeList;
    }

    public void setGolfCourseList(List<GolfCourseDTO> golfCourseList) {
        this.golfCourseList = golfCourseList;
    }

    public List<HoleStatDTO> getHoleStatList() {
        if (holeStatList == null) {
            holeStatList = new ArrayList<>();
        }
        return holeStatList;
    }

    public void setHoleStatList(List<HoleStatDTO> holeStatList) {
        this.holeStatList = holeStatList;
    }

    public List<PracticeSessionDTO> getPracticeSessionList() {
        if (practiceSessionList == null) {
            practiceSessionList = new ArrayList<>();
        }
        return practiceSessionList;
    }

    public void setPracticeSessionList(List<PracticeSessionDTO> practiceSessionList) {
        this.practiceSessionList = practiceSessionList;
    }

    public List<VideoUploadDTO> getVideoUploadList() {
        if (videoUploadList == null) {
            videoUploadList = new ArrayList<>();
        }
        return videoUploadList;
    }

    public void setVideoUploadList(List<VideoUploadDTO> videoUploadList) {
        
        this.videoUploadList = videoUploadList;
    }

    public List<ClubDTO> getClubList() {
        if (clubList == null) {
            clubList = new ArrayList<>();
        }
        return clubList;
    }

    public void setClubList(List<ClubDTO> clubList) {
        this.clubList = clubList;
    }
    
    
}
