package com.example.admin.model

import java.time.LocalDateTime

class Rate(
    private var comment : String,
    private var createAt : LocalDateTime,
    private var idFood : String,
    private var nameFood : String,
    private var valueRate : Int
) {

    fun getComment() : String {
        return comment
    }

    fun setComment(comment : String) {
        this.comment = comment
    }

    fun getCreateAt() : LocalDateTime {
        return createAt
    }

    fun setCreateAt(createAt: LocalDateTime) {
        this.createAt = createAt
    }

    fun getIdFood() : String {
        return idFood
    }

    fun setIdFood(idFood: String) {
        this.idFood = idFood
    }

    fun getNameFood() : String {
        return nameFood
    }

    fun setNameFood(nameFood: String) {
        this.nameFood = nameFood
    }

    fun getValueRate() : Int {
        return valueRate
    }

    fun setValueRate(valueRate: Int) {
        this.valueRate = valueRate
    }

}