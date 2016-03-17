package com.boha.golfpractice.library.dto;

import java.util.List;

/**
 * Created by aubreymalabie on 3/17/16.
 */
public class RequestList {
    private List<RequestDTO> requests;

    public List<RequestDTO> getRequests() {
        return requests;
    }

    public void setRequests(List<RequestDTO> requests) {
        this.requests = requests;
    }
}
