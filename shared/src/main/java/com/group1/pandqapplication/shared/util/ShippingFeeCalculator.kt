package com.group1.pandqapplication.shared.util

/**
 * Tính phí vận chuyển theo vùng địa lý (Zonal Pricing)
 * Logic: Khách trả phí cố định theo vùng, không phụ thuộc ĐVVC
 */
object ShippingFeeCalculator {
    
    // Danh sách tỉnh/thành miền Nam lân cận HCM
    private val nearbyProvinces = listOf(
        "Bình Dương", "Đồng Nai", "Long An", "Bà Rịa - Vũng Tàu", "Bà Rịa-Vũng Tàu",
        "Tây Ninh", "Bình Phước", "Tiền Giang"
    )
    
    // Danh sách tỉnh/thành miền Nam còn lại
    private val southernProvinces = listOf(
        "Cần Thơ", "An Giang", "Bến Tre", "Cà Mau", "Đồng Tháp", "Hậu Giang",
        "Kiên Giang", "Sóc Trăng", "Trà Vinh", "Vĩnh Long", "Bạc Liêu",
        "Ninh Thuận", "Bình Thuận", "Lâm Đồng", "Đắk Lắk", "Đắk Nông", "Gia Lai", "Kon Tum"
    )
    
    // Danh sách tỉnh/thành miền Bắc
    private val northernProvinces = listOf(
        "Hà Nội", "Hải Phòng", "Quảng Ninh", "Hải Dương", "Hưng Yên", "Thái Bình",
        "Nam Định", "Ninh Bình", "Bắc Ninh", "Bắc Giang", "Vĩnh Phúc", "Phú Thọ",
        "Thái Nguyên", "Lạng Sơn", "Cao Bằng", "Bắc Kạn", "Tuyên Quang", "Hà Giang",
        "Lào Cai", "Yên Bái", "Điện Biên", "Lai Châu", "Sơn La", "Hòa Bình"
    )
    
    // Danh sách tỉnh miền Trung
    private val centralProvinces = listOf(
        "Đà Nẵng", "Thừa Thiên Huế", "Quảng Nam", "Quảng Ngãi", "Bình Định",
        "Phú Yên", "Khánh Hòa", "Quảng Bình", "Quảng Trị", "Hà Tĩnh",
        "Nghệ An", "Thanh Hóa"
    )
    
    /**
     * Tính phí vận chuyển dựa trên thành phố/tỉnh
     * @param city Tên thành phố/tỉnh từ địa chỉ giao hàng
     * @return Phí vận chuyển (VND)
     */
    fun calculateShippingFee(city: String?): Long {
        if (city.isNullOrBlank()) return 30000 // Default fee
        
        val normalizedCity = city.trim()
        
        return when {
            // TP.HCM
            isHoChiMinhCity(normalizedCity) -> 20000
            
            // Tỉnh lân cận HCM
            nearbyProvinces.any { normalizedCity.contains(it, ignoreCase = true) } -> 30000
            
            // Miền Nam còn lại
            southernProvinces.any { normalizedCity.contains(it, ignoreCase = true) } -> 35000
            
            // Hà Nội
            isHaNoi(normalizedCity) -> 45000
            
            // Miền Bắc
            northernProvinces.any { normalizedCity.contains(it, ignoreCase = true) } -> 45000
            
            // Miền Trung
            centralProvinces.any { normalizedCity.contains(it, ignoreCase = true) } -> 40000
            
            // Default - Các tỉnh khác
            else -> 35000
        }
    }
    
    private fun isHoChiMinhCity(city: String): Boolean {
        val hcmVariants = listOf(
            "Hồ Chí Minh", "Ho Chi Minh", "HCM", "TPHCM", "TP.HCM", 
            "Thành phố Hồ Chí Minh", "Sài Gòn", "Saigon"
        )
        return hcmVariants.any { city.contains(it, ignoreCase = true) }
    }
    
    private fun isHaNoi(city: String): Boolean {
        val hanoiVariants = listOf(
            "Hà Nội", "Ha Noi", "Hanoi", "HN", "Thủ đô Hà Nội"
        )
        return hanoiVariants.any { city.contains(it, ignoreCase = true) }
    }
    
    /**
     * Lấy tên vùng để hiển thị
     */
    fun getZoneName(city: String?): String {
        if (city.isNullOrBlank()) return "Tiêu chuẩn"
        
        val normalizedCity = city.trim()
        
        return when {
            isHoChiMinhCity(normalizedCity) -> "Nội thành HCM"
            nearbyProvinces.any { normalizedCity.contains(it, ignoreCase = true) } -> "Vùng lân cận"
            isHaNoi(normalizedCity) || northernProvinces.any { normalizedCity.contains(it, ignoreCase = true) } -> "Miền Bắc"
            centralProvinces.any { normalizedCity.contains(it, ignoreCase = true) } -> "Miền Trung"
            else -> "Tiêu chuẩn"
        }
    }
}
