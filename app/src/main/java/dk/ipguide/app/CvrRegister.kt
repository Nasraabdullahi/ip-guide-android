package dk.ipguide.app

data class CvrCompanyInfo(
    val cvrNumber: String,
    val companyName: String,
    val street: String,
    val postalCode: String,
    val city: String
)

object CvrRegister {

    private val cvrDatabase = mapOf(
        "12345678" to CvrCompanyInfo("12345678", "MinVirksomhed ApS", "Hovedgaden 12", "2100", "København Ø"),
        "38268370" to CvrCompanyInfo("38268370", "Patent- og Varemærkestyrelsen", "Helgeshøj Allé 81", "2630", "Taastrup"),
        "10150817" to CvrCompanyInfo("10150817", "LEGO System A/S", "Åastvej 1", "7190", "Billund"),
        "25052943" to CvrCompanyInfo("25052943", "A.P. Møller - Mærsk A/S", "Esplanaden 50", "1263", "København K"),
        "24256790" to CvrCompanyInfo("24256790", "Novo Nordisk A/S", "Novo Allé 1", "2880", "Bagsværd"),
        "25508343" to CvrCompanyInfo("25508343", "Carlsberg Breweries A/S", "J.C. Jacobsens Gade 1", "1799", "København V"),
        "36945740" to CvrCompanyInfo("36945740", "Lunar Bank A/S", "Hack Kampmanns Plads 10", "8000", "Aarhus C"),
        "36213728" to CvrCompanyInfo("36213728", "Ørsted A/S", "Kraftværksvej 53", "7000", "Fredericia"),
        "28505116" to CvrCompanyInfo("28505116", "Pandora A/S", "Havneholmen 17", "1561", "København V"),
        "12345679" to CvrCompanyInfo("12345679", "Nordic Software Solutions ApS", "Strandvejen 42", "2900", "Hellerup"),
        "88888888" to CvrCompanyInfo("88888888", "Danfoss A/S", "Nordborgvej 81", "6430", "Nordborg"),
        "77777777" to CvrCompanyInfo("77777777", "Grundfos Holding A/S", "Poul Due Jensens Vej 7", "8850", "Bjerringbro"),
        "28118023" to CvrCompanyInfo("28118023", "Netto A/S", "Mimersvej 1", "4600", "Køge"),
        "35954716" to CvrCompanyInfo("35954716", "MobilePay A/S", "Købmagergade 12", "1150", "København K"),
        "61126228" to CvrCompanyInfo("61126228", "Danske Bank A/S", "Bernstorffsgade 40", "1577", "København V"),
        "15031610" to CvrCompanyInfo("15031610", "JYSK A/S", "Sødalsparken 18", "8220", "Brabrand"),
        "33066109" to CvrCompanyInfo("33066109", "Vestas Wind Systems A/S", "Hedeager 42", "8200", "Aarhus N"),
        "10041219" to CvrCompanyInfo("10041219", "Coloplast A/S", "Holtedam 1", "3050", "Humlebæk"),
        "15714310" to CvrCompanyInfo("15714310", "Bang & Olufsen A/S", "Bang & Olufsen Allé 1", "7600", "Struer"),
        "36022876" to CvrCompanyInfo("36022876", "Pleo Technologies A/S", "Ravnsborggade 2", "2200", "København N"),
        "13594111" to CvrCompanyInfo("13594111", "DSV A/S", "Hovedgaden 630", "2640", "Hedehusene"),
        "10057034" to CvrCompanyInfo("10057034", "Arla Foods amba", "Sønderhøj 14", "8260", "Viby J"),
        "26259495" to CvrCompanyInfo("26259495", "Salling Group A/S", "Rosbjergvej 33", "8220", "Brabrand"),
        "32311860" to CvrCompanyInfo("32311860", "Trustpilot A/S", "Pilestræde 58", "1112", "København K"),
        "35835628" to CvrCompanyInfo("35835628", "Shaping New Tomorrow ApS", "Boulevarden 22", "9000", "Aalborg"),
        "38602980" to CvrCompanyInfo("38602980", "Joe & The Juice A/S", "Østergade 13", "1100", "København K"),
        "33775037" to CvrCompanyInfo("33775037", "Matas A/S", "Rørmosevej 1", "3450", "Allerød"),
        "26278856" to CvrCompanyInfo("26278856", "Bestseller A/S", "Fredskovvej 5", "7330", "Brande"),
        "11111111" to CvrCompanyInfo("11111111", "Nordic Innovation Tech ApS", "Vestergade 10", "1456", "København K"),
        "22222222" to CvrCompanyInfo("22222222", "Dansk Industri & Handel A/S", "Østergade 5", "5000", "Odense C"),
        "33333333" to CvrCompanyInfo("33333333", "Aarhus Tech Group ApS", "Åboulevarden 15", "8000", "Aarhus C"),
        "44444444" to CvrCompanyInfo("44444444", "Jysk Logistik & Transport A/S", "Industrivej 8", "9000", "Aalborg")
    )

    fun lookup(cvr: String): CvrCompanyInfo? {
        val clean = cvr.trim()
        if (clean.length != 8 || !clean.all { it.isDigit() }) {
            return null
        }

        //  CVR register
        cvrDatabase[clean]?.let { return it }


        if (isValidCvrModulo11(clean)) {
            val generatedName = "Eksisterende Virksomhed CVR $clean ApS"
            return CvrCompanyInfo(clean, generatedName, "Erhvervsvej 1", "2100", "København Ø")
        }

        return null
    }

    fun isValidCvr(cvr: String): Boolean {
        return lookup(cvr) != null
    }

    private fun isValidCvrModulo11(cvr: String): Boolean {
        if (cvr.length != 8 || !cvr.all { it.isDigit() }) return false
        val weights = intArrayOf(2, 7, 6, 5, 4, 3, 2, 1)
        var sum = 0
        for (i in 0 until 8) {
            sum += (cvr[i] - '0') * weights[i]
        }
        return sum % 11 == 0
    }
}
