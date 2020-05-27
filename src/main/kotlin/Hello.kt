package hello

import khttp.responses.Response
import org.json.JSONObject
import org.json.JSONArray


fun getHelloString() {

    val response : Response = khttp.get("https://api.gokulnath.com/thirukkuralchapters/4/thirukkurals")
    val json : JSONObject = response.jsonObject

    val kurals : JSONArray = json["Data"] as JSONArray

    for (kural in kurals) {
        val data : JSONObject = kural as JSONObject;
        val tamil = data["Tamil"]
        val english = data["TamilTransliteration"]
        println(tamil)
        println(english)
    }


}

fun main(args : Array<String>) {
    getHelloString()
}

