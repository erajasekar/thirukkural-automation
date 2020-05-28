package thirukkural.automation

import khttp.responses.Response
import org.json.JSONObject
import org.json.JSONArray
import java.io.BufferedWriter
import kotlin.system.exitProcess
import java.io.File


fun writeKuralsToFile(adhigaramNo: Int, adhigaramName: String) {

    val kurals = getKurals(adhigaramNo);

    var outFile = File("downloads/${adhigaramName}.md");

    outFile.bufferedWriter().use { out ->
        out.write("# ${adhigaramName} \n\n")
        writeSummary(adhigaramNo, kurals, out);
        writeMeaning(adhigaramNo, adhigaramName, kurals, out);
    }
    println("Written to ${outFile}")
}

fun writeSummary(adhigaramNo: Int, kurals: JSONArray, out: BufferedWriter){
    out.write("## Summary \n\n")
    var i = 1;
    for (kural in kurals) {
        val data : JSONObject = kural as JSONObject;
        val kuralNo = (adhigaramNo - 1) * 10 + (i++)
        out.write("### குறள் / Kural ${kuralNo} - 00:00 \n")
        writeKural(data, "Tamil", out);
        writeKural(data, "TamilTransliteration", out);
    }
}

fun writeMeaning(adhigaramNo: Int, adhigaramName: String, kurals: JSONArray, out: BufferedWriter){
    out.write("## Meaning \n\n")
    var i = 1;
    for (kural in kurals) {
        val data : JSONObject = kural as JSONObject;
        val kuralNo = (adhigaramNo - 1) * 10 + (i++)
        val kuralNoStr = "( குறள் - ${kuralNo})"

        out.write("---?include=includes/${adhigaramName}.md \n\n")
        out.write("```\n")
        writeKural(data, "Tamil", out, kuralNoStr);
        out.write("```\n")

        out.write("@snap[span-100 text-gray text-left text-04]\n")
        out.write("தொடரமைப்பு:  TODO\n")
        out.write("@snapend\n\n")

        out.write("@snap[span-100 text-06 text-left]\n")
        out.write("> பொருள்:  TODO\n\n")
        out.write("@snapend\n\n\n")
    }
}

fun writeKural(data: JSONObject, field: String, out: BufferedWriter, kuralNo : String = "\n"){
    val value = convertHtmlBreaksToNewLines(data[field])
    out.write(value)
    out.write("\t\t${kuralNo}")
    out.newLine();
}

fun getKurals(adhigaramNo: Int): JSONArray {
    val response : Response = khttp.get("https://api.gokulnath.com/thirukkuralchapters/${adhigaramNo}/thirukkurals")
    val json : JSONObject = response.jsonObject

    return json["Data"] as JSONArray
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

