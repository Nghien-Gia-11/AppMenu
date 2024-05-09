package com.example.admin.model

import java.time.Instant
import java.time.LocalDateTime
import java.util.Date


class Bill(
    var createAt : LocalDateTime,
    var idBill : String,
    var idCustomer : String,
    var status : Boolean
) {
}