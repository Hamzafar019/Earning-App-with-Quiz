package com.example.earningapp.model

class historymodelclass{
    var timeAndDate:String=""
    var coin:String=""
    var isWithdrawal:Boolean=false
    constructor()
    constructor(timeAndDate: String, coin: String, isWithdrawal: Boolean) {
        this.timeAndDate = timeAndDate
        this.coin = coin
        this.isWithdrawal = isWithdrawal
    }
}
