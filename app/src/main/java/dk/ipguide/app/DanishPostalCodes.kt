package dk.ipguide.app

object DanishPostalCodes {

    private val postalCodeMap = mapOf(
        "1000" to "København K",
        "1500" to "København V",
        "2000" to "Frederiksberg",
        "2100" to "København Ø",
        "2200" to "København N",
        "2300" to "København S",
        "2400" to "København NV",
        "2450" to "København SV",
        "2500" to "Valby",
        "2600" to "Glostrup",
        "2610" to "Rødovre",
        "2620" to "Albertslund",
        "2630" to "Taastrup",
        "2650" to "Hvidovre",
        "2660" to "Brøndby Strand",
        "2670" to "Greve",
        "2680" to "Solrød Strand",
        "2700" to "Brønshøj",
        "2720" to "Vanløse",
        "2730" to "Herlev",
        "2740" to "Skovlunde",
        "2750" to "Ballerup",
        "2760" to "Måløv",
        "2770" to "Kastrup",
        "2791" to "Dragør",
        "2800" to "Kgs. Lyngby",
        "2820" to "Gentofte",
        "2830" to "Virum",
        "2840" to "Holte",
        "2860" to "Søborg",
        "2880" to "Bagsværd",
        "2900" to "Hellerup",
        "2920" to "Charlottenlund",
        "2930" to "Klampenborg",
        "2942" to "Skodsborg",
        "2950" to "Vedbæk",
        "2960" to "Rungsted Kyst",
        "2970" to "Hørsholm",
        "3000" to "Helsingør",
        "3050" to "Humlebæk",
        "3060" to "Espergærde",
        "3100" to "Hornbæk",
        "3200" to "Helsinge",
        "3300" to "Frederiksværk",
        "3400" to "Hillerød",
        "3450" to "Allerød",
        "3460" to "Birkerød",
        "3480" to "Fredensborg",
        "3500" to "Værløse",
        "3520" to "Farum",
        "3600" to "Frederikssund",
        "3700" to "Rønne",
        "4000" to "Roskilde",
        "4100" to "Ringsted",
        "4200" to "Slagelse",
        "4300" to "Holbæk",
        "4400" to "Kalundborg",
        "4500" to "Nykøbing Sj",
        "4600" to "Køge",
        "4700" to "Næstved",
        "4800" to "Nykøbing F",
        "4900" to "Nakskov",
        "5000" to "Odense C",
        "5200" to "Odense V",
        "5210" to "Odense NV",
        "5220" to "Odense SØ",
        "5230" to "Odense M",
        "5240" to "Odense NØ",
        "5250" to "Odense SV",
        "5260" to "Odense S",
        "5270" to "Odense N",
        "5300" to "Kerteminde",
        "5400" to "Bogense",
        "5500" to "Middelfart",
        "5600" to "Faaborg",
        "5700" to "Svendborg",
        "5800" to "Nyborg",
        "6000" to "Kolding",
        "6100" to "Haderslev",
        "6200" to "Aabenraa",
        "6300" to "Gråsten",
        "6400" to "Sønderborg",
        "6500" to "Vojens",
        "6600" to "Vejen",
        "6700" to "Esbjerg",
        "6705" to "Esbjerg Ø",
        "6710" to "Esbjerg V",
        "6800" to "Varde",
        "6900" to "Skjern",
        "7000" to "Fredericia",
        "7100" to "Vejle",
        "7200" to "Grindsted",
        "7300" to "Jelling",
        "7400" to "Herning",
        "7500" to "Holstebro",
        "7600" to "Struer",
        "7700" to "Thisted",
        "7800" to "Skive",
        "7900" to "Nykøbing M",
        "8000" to "Aarhus C",
        "8200" to "Aarhus N",
        "8210" to "Aarhus V",
        "8220" to "Brabrand",
        "8230" to "Åbyhøj",
        "8240" to "Risskov",
        "8250" to "Egå",
        "8260" to "Viby J",
        "8270" to "Højbjerg",
        "8300" to "Odder",
        "8370" to "Hadsten",
        "8382" to "Hinnerup",
        "8400" to "Ebeltoft",
        "8500" to "Grenaa",
        "8600" to "Silkeborg",
        "8660" to "Skanderborg",
        "8700" to "Horsens",
        "8800" to "Viborg",
        "8850" to "Bjerringbro",
        "8900" to "Randers C",
        "9000" to "Aalborg",
        "9200" to "Aalborg SV",
        "9210" to "Aalborg SØ",
        "9220" to "Aalborg Øst",
        "9300" to "Sæby",
        "9400" to "Nørresundby",
        "9500" to "Hobro",
        "9600" to "Aars",
        "9700" to "Brønderslev",
        "9800" to "Hjørring",
        "9900" to "Frederikshavn"
    )

    fun getCityName(postalCode: String): String? {
        val cleanCode = postalCode.trim()
        if (cleanCode.length != 4 || !cleanCode.all { it.isDigit() }) {
            return null
        }

        postalCodeMap[cleanCode]?.let { return it }

        // postnumre
        val codeInt = cleanCode.toIntOrNull() ?: return null
        return when (codeInt) {
            in 1000..1499 -> "København K"
            in 1500..1799 -> "København V"
            in 1800..2000 -> "Frederiksberg"
            in 2100..2199 -> "København Ø"
            in 2200..2299 -> "København N"
            in 2300..2399 -> "København S"
            in 2400..2499 -> "København NV"
            in 2500..2599 -> "Valby"
            in 2600..2699 -> "Glostrup / Vestegn"
            in 2700..2799 -> "Storkøbenhavn"
            in 2800..2999 -> "Nordsjælland"
            in 3000..3699 -> "Nordsjælland"
            in 3700..3799 -> "Bornholm"
            in 4000..4999 -> "Sjælland & Øerne"
            in 5000..5999 -> "Fyn"
            in 6000..6999 -> "Syd- & Sønderjylland"
            in 7000..7999 -> "Midt- & Vestjylland"
            in 8000..8999 -> "Østjylland"
            in 9000..9999 -> "Nordjylland"
            else -> null
        }
    }
}
