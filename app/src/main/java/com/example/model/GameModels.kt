package com.example.model

data class GamePackage(
    val id: String,
    val name: String,
    val amount: Int,
    val price: Double,
    val unit: String // e.g., "Diamonds", "UC", "Tokens", "Wallet"
)

data class GameInfo(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val idFieldLabel: String = "User ID",
    val hasZoneId: Boolean = false,
    val zoneFieldLabel: String? = null,
    val iconName: String, // simple string identifier to draw or map custom vector graphics
    val packages: List<GamePackage>
)

data class PaymentMethod(
    val id: String,
    val name: String,
    val category: String, // "E-Wallet", "Instant QRIS", "Bank Transfer"
    val isAutoInstant: Boolean = true,
    val fee: Double = 0.0
)

object GameDataProvider {
    val games = listOf(
        GameInfo(
            id = "ml",
            name = "Mobile Legends",
            category = "MOBA",
            description = "Top up MLBB Diamonds secara instan dalam hitungan detik. Cukup masukkan User ID dan Zone ID Anda, pilih jumlah Diamond yang Anda inginkan, selesaikan pembayaran, dan Diamond akan langsung ditambahkan ke akun MLBB Anda.",
            idFieldLabel = "User ID",
            hasZoneId = true,
            zoneFieldLabel = "Zone ID",
            iconName = "ml",
            packages = listOf(
                GamePackage("ml_1", "86 Diamonds", 86, 21900.0, "Diamonds"),
                GamePackage("ml_2", "172 Diamonds", 172, 43500.0, "Diamonds"),
                GamePackage("ml_3", "257 Diamonds", 257, 65000.0, "Diamonds"),
                GamePackage("ml_4", "343 Diamonds", 343, 87000.0, "Diamonds"),
                GamePackage("ml_5", "706 Diamonds", 706, 174000.0, "Diamonds"),
                GamePackage("ml_6", "1412 Diamonds", 1412, 348000.0, "Diamonds"),
                GamePackage("ml_7", "2195 Diamonds", 2195, 525000.0, "Diamonds")
            )
        ),
        GameInfo(
            id = "ff",
            name = "Free Fire",
            category = "Battle Royale",
            description = "Top up Free Fire (FF) Diamonds instan langsung masuk. Masukkan Player ID Anda, pilih nominal Diamonds yang diinginkan, lakukan pembayaran otomatis, dan Diamonds akan langsung terkirim ke karakter Anda.",
            idFieldLabel = "Player ID",
            hasZoneId = false,
            iconName = "ff",
            packages = listOf(
                GamePackage("ff_1", "50 Diamonds", 50, 8000.0, "Diamonds"),
                GamePackage("ff_2", "140 Diamonds", 140, 20000.0, "Diamonds"),
                GamePackage("ff_3", "355 Diamonds", 355, 50000.0, "Diamonds"),
                GamePackage("ff_4", "720 Diamonds", 720, 100000.0, "Diamonds"),
                GamePackage("ff_5", "1440 Diamonds", 1440, 200000.0, "Diamonds"),
                GamePackage("ff_6", "2180 Diamonds", 2180, 300000.0, "Diamonds")
            )
        ),
        GameInfo(
            id = "pubg",
            name = "PUBG Mobile",
            category = "Battle Royale",
            description = "Dapatkan PUBG Mobile Unknown Cash (UC) dengan mudah. Masukkan ID Karakter Anda, pilih paket UC yang Anda butuhkan, bayar instan, dan UC akan langsung dikirim ke inbox PUBG Mobile Anda secara otomatis.",
            idFieldLabel = "Character ID",
            hasZoneId = false,
            iconName = "pubg",
            packages = listOf(
                GamePackage("pubg_1", "60 UC", 60, 14000.0, "UC"),
                GamePackage("pubg_2", "325 UC", 325, 70000.0, "UC"),
                GamePackage("pubg_3", "660 UC", 660, 140000.0, "UC"),
                GamePackage("pubg_4", "1800 UC", 1800, 350000.0, "UC"),
                GamePackage("pubg_5", "3850 UC", 3850, 700000.0, "UC"),
                GamePackage("pubg_6", "8100 UC", 8100, 1400000.0, "UC")
            )
        ),
        GameInfo(
            id = "hok",
            name = "Honor of Kings",
            category = "MOBA",
            description = "Beli Honor of Kings (HoK) Tokens secara aman dan cepat. Masukkan ID Pemain, pilih jumlah Token, selesaikan pembayaran melalui transfer otomatis, dan nikmati skin-skin keren favorit Anda seketika.",
            idFieldLabel = "Player ID",
            hasZoneId = false,
            iconName = "hok",
            packages = listOf(
                GamePackage("hok_1", "80 Tokens", 80, 16000.0, "Tokens"),
                GamePackage("hok_2", "240 Tokens", 240, 48000.0, "Tokens"),
                GamePackage("hok_3", "480 Tokens", 480, 96000.0, "Tokens"),
                GamePackage("hok_4", "1200 Tokens", 1200, 240000.0, "Tokens"),
                GamePackage("hok_5", "2400 Tokens", 2400, 480000.0, "Tokens"),
                GamePackage("hok_6", "4800 Tokens", 4800, 960000.0, "Tokens")
            )
        ),
        GameInfo(
            id = "dota",
            name = "Dota 2",
            category = "PC Game / MOBA",
            description = "Top up Steam Wallet IDR untuk game Dota 2 secara instan. Masukkan nomor HP/Steam ID Anda, pilih jumlah nominal Wallet, selesaikan pembayaran, dan saldo Steam Wallet otomatis ditambahkan.",
            idFieldLabel = "Steam ID / No HP",
            hasZoneId = false,
            iconName = "dota",
            packages = listOf(
                GamePackage("dota_1", "Steam Wallet IDR 45K", 45000, 55000.0, "Wallet"),
                GamePackage("dota_2", "Steam Wallet IDR 90K", 90000, 108000.0, "Wallet"),
                GamePackage("dota_3", "Steam Wallet IDR 225K", 225000, 265000.0, "Wallet"),
                GamePackage("dota_4", "Steam Wallet IDR 450K", 450000, 525000.0, "Wallet"),
                GamePackage("dota_5", "Steam Wallet IDR 600K", 600000, 695000.0, "Wallet")
            )
        )
    )

    val paymentMethods = listOf(
        PaymentMethod("qris", "QRIS Instant (GOPAY, OVO, ShopeePay)", "Instant QRIS", isAutoInstant = true, fee = 0.0),
        PaymentMethod("gopay", "GoPay", "E-Wallet", isAutoInstant = true, fee = 1000.0),
        PaymentMethod("shopeepay", "ShopeePay", "E-Wallet", isAutoInstant = true, fee = 1000.0),
        PaymentMethod("bca_va", "BCA Virtual Account", "Bank Transfer", isAutoInstant = true, fee = 2500.0),
        PaymentMethod("mandiri_va", "Mandiri Virtual Account", "Bank Transfer", isAutoInstant = true, fee = 2500.0),
        PaymentMethod("bni_va", "BNI Virtual Account", "Bank Transfer", isAutoInstant = true, fee = 2500.0)
    )
}
