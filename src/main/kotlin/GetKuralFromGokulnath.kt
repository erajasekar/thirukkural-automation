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
    var websiteOutFile = File("downloads/${adhigaramName}-website.md");

    outFile.bufferedWriter().use { out ->
        out.write("# ${adhigaramName} \n\n")
        writeYoutubeInfo(adhigaramNo, adhigaramName,  kurals, out);
        writeMeaning(adhigaramNo, adhigaramName, kurals, out);
    }

    websiteOutFile.bufferedWriter().use { out ->
        writeParagragh("# திருக்குறள் - <ADHIGARAM>" , out);
        writeParagragh("இந்த காணொளியில் உள்ள தகவல்கள்" , out);
        writeKuralsForWebsite(adhigaramNo, kurals, out);
    }
    println("Written to ${outFile}")
}

fun writeKuralsForWebsite( adhigaramNo: Int, kurals: JSONArray, out: BufferedWriter ) {
    var i = 1;
    for (kural in kurals) {
        val kuralNo = (adhigaramNo - 1) * 10 + (i++)
        out.write("***குறள் : ${kuralNo}***\n\n")
        out.write("```\n")
        val data: JSONObject = kural as JSONObject;
        writeKural(data, "Tamil", out);
        writeKural(data, "TamilTransliteration", out,"");
        out.write("```\n\n")
    }
}

fun writeYoutubeInfo(adhigaramNo: Int, adhigaramName: String, kurals: JSONArray, out: BufferedWriter){
    val adhigaramRange = "${(adhigaramNo - 1) * 10 + 1} - ${adhigaramNo * 10}"
    val adhigaramNameWords = convertCamelCaseToWords(adhigaramName)
    writeParagragh("## YouTube", out)
    writeTitle(adhigaramNo, adhigaramNameWords, adhigaramRange, out);
    writeParagragh("### Description", out)
    writeParagragh("உயர் வாழ்க்கைக்கு திருக்குறள் கற்போம் - அதிகாரம் <ADHIGARAM> - குறள்கள் ${adhigaramRange} ", out)
    writeParagragh("Learn Thirrukural for Extraordinary Life - Adhigaram ${adhigaramNameWords} - Kurals ${adhigaramRange} #Thirukkural #Thiruvalluvar #Kural ", out)
    writeParagragh("இந்த காணொளியில் உள்ள தகவல்கள்" , out);
    writeParagragh("<THUMBNAIL POINTS>", out)
    writeParagragh("Please see below for Thirukkurals  and their timestamps of explanation in this video." , out)
    writeParagragh("Introduction - 00:00", out);

    var i = 1;
    for (kural in kurals) {
        val data : JSONObject = kural as JSONObject;
        val kuralNo = (adhigaramNo - 1) * 10 + (i++)
        out.write("குறள் / Kural ${kuralNo} - 00:00 \n\n")
        writeKural(data, "Tamil", out);
        writeKural(data, "TamilTransliteration", out);
      //  out.write("English Meaning: \n\n")
       // writeKural(data, "EnglishMeaning", out);
        out.write("Keywords : \n\n")
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
    val tamilPart = "உயர் வாழ்க்கைக்கு திருக்குறள் - <ADHIGARAM>"
    val engPart = "Thirukkural for Extraordinary Life"
    writeParagragh("${tamilPart} | ${engPart} ", out)
}

fun writeMeaning(adhigaramNo: Int, adhigaramName: String, kurals: JSONArray, out: BufferedWriter){
    out.write("## Meaning \n\n")
    var i = 1;

    out.write("---?include=includes/${adhigaramName}.md \n\n")
    out.write("### <ADHIGHARAM INTRO> \n\n")

    for (kural in kurals) {
        val data : JSONObject = kural as JSONObject;
        val kuralNo = (adhigaramNo - 1) * 10 + (i++)
        val kuralNoStr = "( குறள் - ${kuralNo})"

        out.write("---?include=includes/${adhigaramName}.md \n\n")
        out.write("### <KURAL ${kuralNo} INTRO> \n\n")

        out.write("---?include=includes/${adhigaramName}.md \n\n")
        out.write("```\n")
        writeKural(data, "Tamil", out, kuralNoStr);
        out.write("```\n")

        /*out.write("@snap[span-100 text-left text-04]\n")
        out.write("**தொடரமைப்பு:**  <TODO> \n")
        out.write("@snapend\n\n")

        out.write("@snap[span-100 text-08 text-left]\n")
        out.write("> <பொருள்:>\n\n")
        out.write("@snapend\n\n\n")*/
    }

    out.write("@snap[span-100 text-08 text-left]\n")
    out.write("<div class=\"conclusion\" >\n")
    out.write("<CONCLUSION>\n\n")
    out.write("</div>\n\n")
    out.write("@snapend\n\n\n")
    writeParagragh("---?include=includes/${adhigaramName}.md", out)
    writeParagragh("`@img[width=200, height=200](assets/img/comment-button.png)`", out)
    writeParagragh("###### உங்களுக்கு இந்த அதிகாரத்துலே எந்த குறள் பிடிச்சிருக்குனு கமெண்ட்-ல சொல்லுங்க.", out)
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

