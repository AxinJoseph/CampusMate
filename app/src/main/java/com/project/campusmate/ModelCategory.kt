package com.project.campusmate

class ModelCategory {
    var id:String = ""
    var category:String = ""
    var timestamp:Long = 0
    var uid:String = ""

    //empty constructor, need by firebase
    constructor()


    //parametrized constructor
    constructor(id: String, category: String, timestamp: Long, uid: String) {
        this.id = id
        this.category = category
        this.timestamp = timestamp
        this.uid = uid
    }

}