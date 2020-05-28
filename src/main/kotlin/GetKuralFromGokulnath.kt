package thirukkural.automation

import khttp.responses.Response
import org.json.JSONObject
import org.json.JSONArray
import kotlin.system.exitProcess
import java.io.File


fun writeKuralsToFile(adhigaramNo: Int, adhigaramName: String) {

    val response : Response = khttp.get("https://api.gokulnath.com/thirukkuralchapters/${adhigaramNo}/thirukkurals")
    val json : JSONObject = response.jsonObject

    val kurals : JSONArray = json["Data"] as JSONArray

    var outFile = File("downloads/${adhigaramName}.md");

    outFile.bufferedWriter().use { out ->

        for (kural in kurals) {
            var i = 1;
            val data : JSONObject = kural as JSONObject;
            val tamil = convertHtmlBreaksToNewLines(data["Tamil"])
            val english = convertHtmlBreaksToNewLines(data["TamilTransliteration"])
            val kuralNo = (adhigaramNo - 1) * 10 + i++

            out.write("### குறள் / Kural ${kuralNo} - 00:00")
            out.newLine();
            out.write(tamil)
            out.newLine();
            out.newLine();

            out.write(english)
            out.newLine();
            out.newLine();
        }
    }
    println("Written to ${outFile}")

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

