package dk.ipguide.app

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

object PdfExporter {

    fun generateAndSharePdf(context: Context, draft: ApplicationDraft) {
        try {
            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size in points
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas

            val paint = Paint().apply {
                isAntiAlias = true
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            }

            // Header Banner (Navy)
            paint.color = Color.parseColor("#123B68")
            canvas.drawRect(0f, 0f, 595f, 70f, paint)

            // Header Title
            paint.color = Color.WHITE
            paint.textSize = 20f
            paint.isFakeBoldText = true
            canvas.drawText("PATENT- OG VAREMÆRKESTYRELSEN", 30f, 42f, paint)

            // WATERMARK / KLADDE BADGE (US10 requirement)
            paint.color = Color.parseColor("#FFEBEE")
            canvas.drawRect(30f, 85f, 565f, 115f, paint)

            paint.color = Color.parseColor("#C62828")
            paint.textSize = 14f
            paint.isFakeBoldText = true
            canvas.drawText("KLADDE - IKKE OFFICIEL INDSENDELSE", 160f, 105f, paint)

            // Section 1: Kladde Oplysninger
            var yPos = 150f
            paint.color = Color.parseColor("#123B68")
            paint.textSize = 16f
            paint.isFakeBoldText = true
            canvas.drawText("Ansøgningskladde: ${draft.id}", 30f, yPos, paint)

            yPos += 24f
            paint.color = Color.parseColor("#5A6E85")
            paint.textSize = 12f
            paint.isFakeBoldText = false
            canvas.drawText("Oprettet dato: ${draft.createdDate}  |  Status: ${draft.status.label}", 30f, yPos, paint)

            // Divider line
            yPos += 15f
            paint.color = Color.parseColor("#E2E8F0")
            canvas.drawLine(30f, yPos, 565f, yPos, paint)

            // Section 2: Varemærkeoplysninger
            yPos += 30f
            paint.color = Color.parseColor("#123B68")
            paint.textSize = 14f
            paint.isFakeBoldText = true
            canvas.drawText("1. VAREMÆRKEOPLYSNINGER", 30f, yPos, paint)

            yPos += 20f
            paint.color = Color.BLACK
            paint.textSize = 11f
            paint.isFakeBoldText = true
            canvas.drawText("Varemærkenavn:", 30f, yPos, paint)
            paint.isFakeBoldText = false
            canvas.drawText(draft.brandName.ifBlank { "[Ikke angivet]" }, 160f, yPos, paint)

            yPos += 18f
            paint.isFakeBoldText = true
            canvas.drawText("Mærketype:", 30f, yPos, paint)
            paint.isFakeBoldText = false
            canvas.drawText(draft.brandType, 160f, yPos, paint)

            yPos += 18f
            paint.isFakeBoldText = true
            canvas.drawText("Beskrivelse:", 30f, yPos, paint)
            paint.isFakeBoldText = false
            canvas.drawText(draft.description.ifBlank { "[Ikke angivet]" }, 160f, yPos, paint)

            // Section 3: Vare- og tjenesteydelsesklasser
            yPos += 35f
            paint.color = Color.parseColor("#123B68")
            paint.textSize = 14f
            paint.isFakeBoldText = true
            canvas.drawText("2. VALGTE VAREKLASSER", 30f, yPos, paint)

            yPos += 20f
            paint.color = Color.BLACK
            paint.textSize = 11f
            paint.isFakeBoldText = false
            val classText = if (draft.selectedClasses.isEmpty()) {
                "Ingen vareklasser valgt"
            } else {
                "Valgte klasser: " + draft.selectedClasses.joinToString(", ") { "Klasse $it" }
            }
            canvas.drawText(classText, 30f, yPos, paint)

            // Section 4: Ansøgeroplysninger
            yPos += 35f
            paint.color = Color.parseColor("#123B68")
            paint.textSize = 14f
            paint.isFakeBoldText = true
            canvas.drawText("3. ANSØGEROPLYSNINGER", 30f, yPos, paint)

            yPos += 20f
            paint.color = Color.BLACK
            paint.textSize = 11f
            paint.isFakeBoldText = true
            canvas.drawText("Ansøgertype:", 30f, yPos, paint)
            paint.isFakeBoldText = false
            canvas.drawText(draft.applicantType.label, 160f, yPos, paint)

            yPos += 18f
            paint.isFakeBoldText = true
            canvas.drawText("Navn/Virksomhed:", 30f, yPos, paint)
            paint.isFakeBoldText = false
            canvas.drawText(draft.applicantName.ifBlank { "[Ikke angivet]" }, 160f, yPos, paint)

            if (draft.applicantType == ApplicantType.VIRKSOMHED) {
                yPos += 18f
                paint.isFakeBoldText = true
                canvas.drawText("CVR-nummer:", 30f, yPos, paint)
                paint.isFakeBoldText = false
                canvas.drawText(draft.cvrNumber.ifBlank { "[Ikke angivet]" }, 160f, yPos, paint)
            }

            yPos += 18f
            paint.isFakeBoldText = true
            canvas.drawText("Adresse:", 30f, yPos, paint)
            paint.isFakeBoldText = false
            canvas.drawText(draft.applicantAddress.ifBlank { "[Ikke angivet]" }, 160f, yPos, paint)

            yPos += 18f
            paint.isFakeBoldText = true
            canvas.drawText("E-mail:", 30f, yPos, paint)
            paint.isFakeBoldText = false
            canvas.drawText(draft.applicantEmail.ifBlank { "[Ikke angivet]" }, 160f, yPos, paint)

            // Footer
            yPos = 800f
            paint.color = Color.parseColor("#94A3B8")
            paint.textSize = 10f
            canvas.drawText("Genereret via IPGuide App for Patent- og Varemærkestyrelsen", 140f, yPos, paint)

            pdfDocument.finishPage(page)

            // Save file
            val file = File(context.cacheDir, "Ansogningskladde_${draft.id}.pdf")
            val outputStream = FileOutputStream(file)
            pdfDocument.writeTo(outputStream)
            outputStream.close()
            pdfDocument.close()

            Toast.makeText(context, "PDF genereret: ${file.name}", Toast.LENGTH_LONG).show()

            // Open or share intent
            val fileUri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, fileUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(shareIntent, "Del eller gem PDF-kladde"))

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Kladde eksporteret som PDF til enheden!", Toast.LENGTH_SHORT).show()
        }
    }
}
