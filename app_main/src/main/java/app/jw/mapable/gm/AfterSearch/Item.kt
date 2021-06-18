package app.jw.mapable.gm.AfterSearch

class Item {

    var trafficdistance : String = ""
    var totalwalk = ""
    var totaltime = ""
    var payment1 = ""
    var ways1 = ""


    private lateinit var timeValue: IntArray


    fun getTrafficDistance(): String {
        return trafficdistance
    }

    fun getTotalWalk(): String? {
        return totalwalk
    }

    fun getTotalTime(): String? {
        return totaltime
    }

    fun getPayment(): String? {
        return payment1
    }

    fun getWays(): String? {
        return ways1
    }

    fun getTimeValue(): IntArray {
        return timeValue
    }

    fun setTrafficDistance(trafficDistance: String) {
        trafficdistance = trafficDistance
    }

    fun setTotalWalk(totalWalk: String) {
        totalwalk = totalWalk
    }

    fun setTotalTime(totalTime: String) {
        totaltime = totalTime
    }

    fun setPayment(payment: String) {
        payment1 = payment
    }

    fun setWays(ways: String) {
        ways1 = ways
    }

    fun setTimeValue(timeValue: IntArray) {
        this.timeValue = timeValue
    }
}