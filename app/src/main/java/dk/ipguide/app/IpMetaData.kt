package dk.ipguide.app

enum class IpCategory {
    PATENT,
    VAREMAERKE,
    DESIGN
}

data class IpRightMetadata(
    val category: IpCategory,
    val title: String,
    val subtitle: String,
    val description: String
)

val defaultIpRights = listOf(
    IpRightMetadata(
        category = IpCategory.PATENT,
        title = "Patent",
        subtitle = "Opfindelser & Teknik",
        description = "Beskytter tekniske opfindelser, produkter og fremgangsmåder i op til 20 år."
    ),
    IpRightMetadata(
        category = IpCategory.VAREMAERKE,
        title = "Varemærke",
        subtitle = "Kendetegn & Logo",
        description = "Beskytter firmanavne, logoer, slogans og særprægede kendetegn."
    ),
    IpRightMetadata(
        category = IpCategory.DESIGN,
        title = "Design",
        subtitle = "Udseende & Form",
        description = "Beskytter et produkts visuelle udseende, form, mønster og farvesammensætning."
    )
)
