package com.example.admin.model

import java.time.LocalDateTime

class DetailBill(
    var idBill : String,
    var idFood : String,
    var nameFood : String,
    var amount : Int,
    var idCustomer : String,
    var createAt : LocalDateTime,
    var thanhTien : Float
) {
}