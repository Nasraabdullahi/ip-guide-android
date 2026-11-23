package dk.ipguide.app

enum class DraftStatus(val label: String) {
    UNDER_UDARBEJDELSE("Under udarbejdelse"),
    KLAR_TIL_EKSPORT("Klar til eksport")
}

enum class ApplicantType(val label: String) {
    PRIVATPERSON("Privatperson"),
    VIRKSOMHED("Virksomhed")
}

data class NiceClass(
    val classNumber: Int,
    val title: String,
    val description: String
)

val defaultNiceClasses = listOf(
    NiceClass(9, "Klasse 9: IT & Software", "Applikationer, software, computere og elektroniske apparater."),
    NiceClass(25, "Klasse 25: Beklædning", "Tøj, fodtøj og hovedbeklædning."),
    NiceClass(35, "Klasse 35: Annoncering & Business", "Markedsføring, forretningsledelse og online handel."),
    NiceClass(41, "Klasse 41: Uddannelse & Underholdning", "Kurser, undervisning, sport og underholdningsaktiviteter."),
    NiceClass(42, "Klasse 42: IT-tjenester & Udvikling", "Softwareudvikling, IT-rådgivning og tekniske analyser.")
)

data class ApplicationDraft(
    val id: String = "VM-2026-${(100..999).random()}",
    var category: IpCategory = IpCategory.VAREMAERKE,
    var status: DraftStatus = DraftStatus.UNDER_UDARBEJDELSE,
    var brandName: String = "",
    var brandType: String = "Ordmærke (kun tekst)",
    var hasLogoImage: Boolean = false,
    var logoFileName: String? = null,
    var description: String = "",
    var selectedClasses: MutableList<Int> = mutableListOf(),
    var customGoodsServices: String = "",
    var applicantType: ApplicantType = ApplicantType.PRIVATPERSON,
    var applicantName: String = "",
    var cvrNumber: String = "",
    var applicantAddress: String = "",
    var applicantEmail: String = "",
    var createdDate: String = "20. nov. 2026"
) {
    fun isEmailValid(): Boolean {
        val trimmed = applicantEmail.trim()
        if (trimmed.isBlank() || !trimmed.contains("@")) return false
        val parts = trimmed.split("@")
        if (parts.size != 2) return false
        val local = parts[0]
        val domain = parts[1]
        return local.isNotBlank() && domain.contains(".") && domain.substringAfterLast(".").length >= 2
    }

    fun isCvrValid(): Boolean {
        return CvrRegister.isValidCvr(cvrNumber)
    }

    fun validate(): DraftValidationResult {
        val errors = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        if (brandName.isBlank()) {
            errors.add("Varemærkenavn skal udfyldes")
        }
        if (description.isBlank() || description.length < 10) {
            errors.add("Beskrivelse skal indeholde mindst 10 tegn")
        }
        if (selectedClasses.isEmpty()) {
            errors.add("Der skal vælges mindst én vare- eller tjenesteydelsesklasse")
        }
        if (applicantName.isBlank()) {
            errors.add(if (applicantType == ApplicantType.VIRKSOMHED) "Virksomhedsnavn skal udfyldes" else "Ansøgers navn skal udfyldes")
        }
        if (applicantType == ApplicantType.VIRKSOMHED) {
            if (cvrNumber.isBlank()) {
                errors.add("CVR-nummer skal udfyldes for virksomheder")
            } else if (!isCvrValid()) {
                errors.add("CVR-nummeret blev ikke fundet i CVR-registeret (skal være et gyldigt 8-cifret CVR)")
            }
        }
        if (applicantAddress.isBlank()) {
            errors.add("Adresse skal udfyldes")
        }
        if (applicantEmail.isBlank()) {
            errors.add("Ansøgers e-mail skal udfyldes")
        } else if (!isEmailValid()) {
            errors.add("E-mailadressen har et ugyldigt format")
        }

        if (brandType.contains("Figur") && !hasLogoImage) {
            warnings.add("Et logo/billede bør uploades for figurmærker")
        }

        val isValid = errors.isEmpty()
        if (isValid) {
            status = DraftStatus.KLAR_TIL_EKSPORT
        } else {
            status = DraftStatus.UNDER_UDARBEJDELSE
        }

        return DraftValidationResult(
            isValid = isValid,
            errors = errors,
            warnings = warnings
        )
    }
}

data class DraftValidationResult(
    val isValid: Boolean,
    val errors: List<String>,
    val warnings: List<String>
)
