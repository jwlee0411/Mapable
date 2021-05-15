package app.jw.mapable.gm;

public class Item {

    private String trafficDistance, totalWalk, totalTime, payment, ways;

    private int[] timeValue;




    public String getTrafficDistance(){
        return trafficDistance;
    }

    public String getTotalWalk() {
        return totalWalk;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public String getPayment() {
        return payment;
    }

    public String getWays() {
        return ways;
    }

    public int[] getTimeValue() {
        return timeValue;
    }

    public void setTrafficDistance(String trafficDistance)
    {
        this.trafficDistance = trafficDistance;
    }
    public void setTotalWalk(String totalWalk)
    {
        this.totalWalk = totalWalk;
    }
    public void setTotalTime(String totalTime)
    {
        this.totalTime = totalTime;
    }
    public void setPayment(String payment)
    {
        this.payment = payment;
    }

    public void setWays(String ways) {
        this.ways = ways;
    }

    public void setTimeValue(int[] timeValue) {
        this.timeValue = timeValue;
    }
}
