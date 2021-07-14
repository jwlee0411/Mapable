package app.jw.mapable.gm.aftersearch;


public class Item2 {

    //테스트를 위해 도보 기준으로 작성함

    private String distance, sectionTime, trafficCount, name, startStation, endStation, trafficType;

    public String getDistance() {
        return distance;
    }

    public String getSectionTime() {
        return sectionTime;
    }

    public String getTrafficCount() {
        return trafficCount;
    }

    public String getName() {
        return name;
    }

    public String getTrafficType()
    {
        return trafficType;
    }



    public String getStartStation() {
        return startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setSectionTime(String sectionTime) {
        this.sectionTime = sectionTime;
    }

    public void setTrafficCount(String trafficCount) {
        this.trafficCount = trafficCount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTrafficType(String trafficType) {
        this.trafficType = trafficType;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }
}