package com.group1.pandqapplication.admin.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.widget.Toast
import com.group1.pandqapplication.admin.data.remote.dto.CategorySaleItemDto
import com.group1.pandqapplication.admin.data.remote.dto.TopProductDto
import java.io.FileOutputStream
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

data class AnalyticsPdfData(
    val totalRevenue: BigDecimal,
    val totalOrders: Long,
    val reportType: String,
    val topProducts: List<TopProductDto> = emptyList(),
    val categories: List<CategorySaleItemDto> = emptyList(),
    val isCategoryReport: Boolean = false
)

object PdfUtils {

    fun createAnalyticsPdf(context: Context, outputUri: Uri, data: AnalyticsPdfData) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size in points (approx)
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()
        val titlePaint = Paint()

        // Define generic colors and fonts
        paint.color = Color.BLACK
        paint.textSize = 12f
        paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)

        titlePaint.color = Color.parseColor("#137fec")
        titlePaint.textSize = 24f
        titlePaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        titlePaint.textAlign = Paint.Align.CENTER

        // Draw Title
        canvas.drawText("BÁO CÁO DOANH THU PANDORA", pageInfo.pageWidth / 2f, 50f, titlePaint)

        // Draw Date
        paint.color = Color.GRAY
        paint.textSize = 10f
        paint.textAlign = Paint.Align.CENTER
        val dateStr = "Ngày xuất: ${LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}"
        canvas.drawText(dateStr, pageInfo.pageWidth / 2f, 75f, paint)

        // Draw Summary Box
        paint.textAlign = Paint.Align.LEFT
        paint.color = Color.BLACK
        paint.textSize = 14f
        paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        
        val summaryY = 120f
        canvas.drawText("TỔNG QUAN", 40f, summaryY, paint)
        
        paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        paint.textSize = 12f
        canvas.drawText("Tổng doanh thu: ${formatCurrency(data.totalRevenue)}", 40f, summaryY + 25f, paint)
        canvas.drawText("Tổng đơn hàng: ${data.totalOrders}", 300f, summaryY + 25f, paint)

        // Draw List Title
        val listY = summaryY + 70f
        paint.textSize = 14f
        paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        canvas.drawText(data.reportType.uppercase(), 40f, listY, paint)

        // Draw Table Header
        paint.textSize = 10f
        paint.color = Color.DKGRAY
        val headerY = listY + 30f
        canvas.drawText("TÊN", 40f, headerY, paint)
        canvas.drawText(if (data.isCategoryReport) "SỐ LƯỢNG" else "ĐÃ BÁN", 350f, headerY, paint)
        canvas.drawText("DOANH THU", 450f, headerY, paint)
        
        // Draw Divider
        paint.strokeWidth = 1f
        paint.color = Color.LTGRAY
        canvas.drawLine(40f, headerY + 10f, 555f, headerY + 10f, paint)

        // Draw Items
        var currentY = headerY + 30f
        paint.color = Color.BLACK
        paint.textSize = 11f
        paint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)

        if (data.isCategoryReport) {
            data.categories.forEach { item ->
                if (currentY > pageInfo.pageHeight - 50) return@forEach // Simple text clip prevent overflow
                val name = item.categoryName ?: "N/A"
                val truncatedName = if (name.length > 40) name.take(37) + "..." else name
                
                canvas.drawText(truncatedName, 40f, currentY, paint)
                canvas.drawText("${item.quantitySold}", 350f, currentY, paint)
                canvas.drawText(formatCurrency(item.revenue), 450f, currentY, paint)
                currentY += 25f
            }
        } else {
            data.topProducts.forEach { item ->
                if (currentY > pageInfo.pageHeight - 50) return@forEach
                val name = item.productName ?: "N/A"
                val truncatedName = if (name.length > 40) name.take(37) + "..." else name
                
                canvas.drawText(truncatedName, 40f, currentY, paint)
                canvas.drawText("${item.quantitySold}", 350f, currentY, paint)
                canvas.drawText(formatCurrency(item.totalRevenue), 450f, currentY, paint)
                currentY += 25f
            }
        }

        // Finish Page
        pdfDocument.finishPage(page)

        // Write to file
        try {
            context.contentResolver.openFileDescriptor(outputUri, "w")?.use { pfd ->
                FileOutputStream(pfd.fileDescriptor).use {
                    pdfDocument.writeTo(it)
                }
            }
            Toast.makeText(context, "Đã lưu file PDF thành công!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Lỗi khi lưu PDF: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            pdfDocument.close()
        }
    }

    private fun formatCurrency(value: BigDecimal?): String {
        if (value == null) return "0 VNĐ"
        return "${NumberFormat.getNumberInstance(Locale("vi", "VN")).format(value)} VNĐ"
    }
}
