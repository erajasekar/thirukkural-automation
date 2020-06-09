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
        writeYoutubeInfo(adhigaramNo, adhigaramName,  kurals, out);
        writeMeaning(adhigaramNo, adhigaramName, kurals, out);
    }
    println("Written to ${outFile}")
}

fun writeYoutubeInfo(adhigaramNo: Int, adhigaramName: String, kurals: JSONArray, out: BufferedWriter){
    val adhigaramRange = "${(adhigaramNo - 1) * 10 + 1} - ${adhigaramNo * 10}"
    val adhigaramNameWords = convertCamelCaseToWords(adhigaramName)
    writeParagragh("## YouTube", out)
    writeTitle(adhigaramNo, adhigaramNameWords, adhigaramRange, out);
    writeParagragh("### Description", out)
    writeParagragh("திருக்குறள்  - <ADHIGARAM> அதிகாரத்தில் உள்ள (${adhigaramRange}) குறள்களின் எளிய  விளக்கம் ", out)
    writeParagragh("Simple Explanation of Thirukkurals ${adhigaramRange} in ${adhigaramNameWords} Adhigaram #Thirukkural #Thiruvalluvar #Kural ", out)
    writeParagragh("இந்த காணொளியில் உள்ள தகவல்கள்" , out);
    writeParagragh("<THUMBNAIL POINTS>", out)
    writeParagragh("Please see below for Thirukkurals  and their timestamps of explanation in this video." , out)

    var i = 1;
    for (kural in kurals) {
        val data : JSONObject = kural as JSONObject;
        val kuralNo = (adhigaramNo - 1) * 10 + (i++)
        out.write("குறள் / Kural ${kuralNo} - 00:00 \n\n")
        writeKural(data, "Tamil", out);
        writeKural(data, "TamilTransliteration", out);
        out.write("Meaning: \n\n")
        writeKural(data, "EnglishMeaning", out);
    }

    out.newLine()
    out.newLine()
    writeParagragh("Thank you for watching this video. Please comment on your favorite Thirukkural from this video below." , out)
    writeParagragh("Please like and share it with others if you like this video", out)
    writeParagragh("Please subscribe to our channel https://www.youtube.com/channel/UC4Xi_LjVkISzDW8-wpSAITA?sub_confirmation=1 and hit bell button to get notified when we post new videos.", out)
    writeParagragh("Thanks for your support.", out)
}

fun writeParagragh(text: String, out: BufferedWriter) {
    out.write("${text} \n\n\n")
}

fun writeTitle(adhigaramNo: Int, adhigaramName : String, adhigaramRange : String, out: BufferedWriter) {
    writeParagragh("### Title", out)
    val tamilPart = "திருக்குறள் ${adhigaramRange} <ADHIGARAM> விளக்கம் "
    val engPart = "Thirukkural Explanation - ${adhigaramName}"
    writeParagragh("${tamilPart} | ${engPart} ", out)
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

        out.write("@snap[span-100 text-left text-04]\n")
        out.write("தொடரமைப்பு:  <TODO> \n")
        out.write("@snapend\n\n")

        out.write("@snap[span-100 text-06 text-left]\n")
        out.write("> <பொருள்:>\n\n")
        out.write("@snapend\n\n\n")
    }

    writeParagragh("---", out)
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

fun convertCamelCaseToWords(str: String) : String {
    val result : StringBuilder = StringBuilder();
    for (i in 0 until str.length) {
        val ch = str[i]
        if (Character.isUpperCase(ch) && i!=0)
            result.append(" ").append(ch)
        else
            result.append(ch)
    }
    return result.toString()
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

