package com.example.admin.model

class Food(
    private var idFood : String,
    private var nameFood : String,
    private var cost : Double,
    private var salePrice : Double,
    private var images : String,
    private var detailFood : String,
    private var intro : String,
    private var status : Boolean
){

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

    fun getCost() : Double {
        return cost
    }

    fun setCost(cost: Double) {
        this.cost = cost
    }

    fun getSalePrice() : Double {
        return salePrice
    }

    fun setSalePrice(salePrice: Double) {
        this.salePrice = salePrice
    }

    fun getImages(): String {
        return images
    }

    fun setImages(images: String) {
        this.images = images
    }


    fun getDetailFood(): String {
        return detailFood
    }

    fun setDetailFood(detailFood: String) {
        this.detailFood = detailFood
    }

    fun getIntro(): String {
        return intro
    }

    fun setIntro(intro: String) {
        this.intro = intro
    }

    fun getStatus(): Boolean {
        return status
    }

    fun setStatus(status: Boolean) {
        this.status = status
    }

}