package com.likelion.catdogpia.domain.entity.mypage;

public enum PointStatus {
    SAVED("적립"),
    USED("사용");

    private final String status;

    PointStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}