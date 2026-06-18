package com.campusconnect.campusconnect.model;

public class OfficeDesk {
    private String deskNumber;
    private String building;
    private int floor;

    public OfficeDesk() {}

    public OfficeDesk(String deskNumber, String building, int floor) {
        this.deskNumber = deskNumber;
        this.building = building;
        this.floor = floor;
    }

    public String getDeskNumber() { return deskNumber; }
    public void setDeskNumber(String deskNumber) { this.deskNumber = deskNumber; }

    public String getBuilding() { return building; }
    public void setBuilding(String building) { this.building = building; }

    public int getFloor() { return floor; }
    public void setFloor(int floor) { this.floor = floor; }

    @Override
    public String toString() {
        return "OfficeDesk{" +
                "deskNumber='" + deskNumber + '\'' +
                ", building='" + building + '\'' +
                ", floor=" + floor +
                '}';
    }

    public static OfficeDeskBuilder builder() {
        return new OfficeDeskBuilder();
    }

    public static class OfficeDeskBuilder {
        private String deskNumber;
        private String building;
        private int floor;

        public OfficeDeskBuilder deskNumber(String deskNumber) { this.deskNumber = deskNumber; return this; }
        public OfficeDeskBuilder building(String building) { this.building = building; return this; }
        public OfficeDeskBuilder floor(int floor) { this.floor = floor; return this; }

        public OfficeDesk build() {
            return new OfficeDesk(deskNumber, building, floor);
        }
    }
}
