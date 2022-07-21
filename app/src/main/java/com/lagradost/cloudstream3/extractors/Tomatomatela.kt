package com.lagradost.cloudstream3.extractors

import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.ExtractorApi
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.Qualities
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class Cinestart : Tomatomatela() {
    override var name = "Cinestart"
    override var mainUrl = "https://cinestart.net"
    override val details = "vr.php?v="
}

open class Tomatomatela : ExtractorApi() {
    override var name = "Tomatomatela"
    override var mainUrl = "https://tomatomatela.com"
    override val requiresReferer = false

    @Serializable
    private data class Tomato(
        @SerialName("status") val status: Int,
        @SerialName("file") val file: String
    )

    open val details = "details.php?v="
    override suspend fun getUrl(url: String, referer: String?): List<ExtractorLink>? {
        val link = url.replace("$mainUrl/embed.html#", "$mainUrl/$details")
        val server = app.get(link, allowRedirects = false).text
        val json = parseJson<Tomato>(server)
        if (json.status == 200) return listOf(
            ExtractorLink(
                name,
                name,
                json.file,
                "",
                Qualities.Unknown.value,
                isM3u8 = false
            )
        )
        return null
    }
}
