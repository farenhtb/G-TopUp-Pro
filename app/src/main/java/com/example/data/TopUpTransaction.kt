package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topup_transactions")
data class TopUpTransaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val gameName: String, // "Free Fire", "PUBG Mobile", "Mobile Legends", "Dota 2", "Honor of Kings"
    val userId: String,
    val zoneId: String? = null,
    val itemName: String, // e.g., "140 Diamonds"
    val price: Double,
    val paymentMethod: String, // "QRIS", "GoPay", "ShopeePay", "BCA Virtual Account", etc.
    val paymentStatus: String, // "PENDING", "SUCCESS", "CANCELLED"
    val transactionTime: Long,
    val paymentExpiryTime: Long,
    val invoiceNumber: String
)
