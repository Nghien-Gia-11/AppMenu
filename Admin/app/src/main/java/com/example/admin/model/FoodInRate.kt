package com.example.admin.model

class FoodInRate(
    private var idFood : String,
    private var nameFood : String,
    private var value : Int
) {

    fun getIdFood() : String{
        return idFood
    }

    fun setIdFood(idFood: String) {
        this.idFood = idFood
    }

    fun getNameFood() : String{
        return nameFood
    }

    fun setNameFood(nameFood: String) {
        this.nameFood = nameFood
    }

    fun getValues() : Int{
        return value
    }

    fun setValues(value: Int) {
        this.value = value
    }

}