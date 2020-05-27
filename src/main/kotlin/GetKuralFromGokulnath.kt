package thirukkural.automation

import khttp.responses.Response
import org.json.JSONObject
import org.json.JSONArray
import kotlin.system.exitProcess


fun writeKuralsToFile(adhigaramNo: Int, adhigaramName: String) {

    val response : Response = khttp.get("https://api.gokulnath.com/thirukkuralchapters/${adhigaramNo}/thirukkurals")
    val json : JSONObject = response.jsonObject

    val kurals : JSONArray = json["Data"] as JSONArray

    for (kural in kurals) {
        val data : JSONObject = kural as JSONObject;
        val tamil = convertHtmlBreaksToNewLines(data["Tamil"])
        val english = convertHtmlBreaksToNewLines(data["TamilTransliteration"])
        println(tamil)
        println(english)
    }
}

fun convertHtmlBreaksToNewLines(value : Any) : String {
    return value.toString().replace("<br />", "\n")
}

fun main(args : Array<String>) {
    if (args.size < 2) {
        println("Usage GetKuralFromGokulnath <adhigaramNo> <adhigaramName>")
        exitProcess(-1);
    }
    writeKuralsToFile(args[0].toInt(), args[1])
}

