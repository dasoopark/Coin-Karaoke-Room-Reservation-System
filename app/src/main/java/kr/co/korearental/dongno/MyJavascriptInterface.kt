package kr.co.korearental.dongno

import android.webkit.JavascriptInterface
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MyJavascriptInterface {
    @JavascriptInterface
    fun getHtml(html: String?) { //위 자바스크립트가 호출되면 여기로 html이 반환됨
        Thread(){
            try {
                val doc = Jsoup.parse(html)
                val elements = doc.select("div[class=container] div shrinkable-layout search-layout search-list search-list-contents perfect-scrollbar div div div div div search-item-place")
                for(element in elements){
                    val title = element.select("span[class=search_title_text]").text()
                    var number = element.select("span[class=search_text phone ng-star-inserted]").text()
                    if(number == ""){
                        number = "null"
                    }
                    val address = element.select("span[class=search_text address]").text()
                    GlobalApplication.mapList.add(arrayListOf(title, number, address))
                    getLongLatDist(address)
                }
            }catch (e:Exception){
            }
        }.start()
    }

    fun getLongLatDist(value:String) {
        Thread() {
            var response = StringBuffer()
            try {
                val longitude = GlobalApplication.longitude
                val latitude = GlobalApplication.latitude
                val url = URL("https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=$value&coordinate=$longitude,$latitude")
                val con = url.openConnection() as HttpURLConnection
                con.requestMethod = "GET"
                con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", "4cbtm9g0q6")
                con.setRequestProperty("X-NCP-APIGW-API-KEY", "isgBHvU8rLoXBL9vqdLdhnxmX6ZJg47liwqztbNw")
                val responseCode = con.responseCode
                val br: BufferedReader
                if (responseCode == 200) { // 정상 호출
                    br = BufferedReader(InputStreamReader(con.inputStream))
                    println("ㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱㄱ")
                } else {  // 에러 발생
                    br = BufferedReader(InputStreamReader(con.errorStream))
                    println("ㄴㄴㄴㄴㄴㄴㄴㄴㄴㄴㄴㄴㄴㄴㄴㄴㄴㄴㄴㄴㄴㄴㄴㄴㄴㄴㄴㄴㄴㄴㄴ")
                }
                var inputLine: String?
                while (br.readLine().also { inputLine = it } != null) {
                    response.append(inputLine)
                }
                br.close()
            } catch (e: java.lang.Exception) {
                println("ㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹ")
            }

            val json = JSONObject(response.toString())
            val jsonCount = json.getJSONObject("meta").getInt("count")
            val jsonArray = json.getJSONArray("addresses")
            val x: Double
            val y: Double
            val dis: Double
            val distance: String
            if (jsonCount == 1) {
                x = jsonArray.getJSONObject(0).getDouble("x")
                y = jsonArray.getJSONObject(0).getDouble("y")
                dis = jsonArray.getJSONObject(0).getDouble("distance")
            } else {
                x = jsonArray.getJSONObject(1).getDouble("x")
                y = jsonArray.getJSONObject(1).getDouble("y")
                dis = jsonArray.getJSONObject(1).getDouble("distance")
            }

            val distanceArray = dis.toString().split(".")
            if (distanceArray[0].length > 3) {
                distance = distanceArray[0].substring(0, distanceArray[0].length - 3) + "." +
                        distanceArray[0].substring(distanceArray[0].length - 3, distanceArray[0].length - 1) + "km"
            } else {
                distance = dis.toString() + "m"
            }
            GlobalApplication.mapList_LonLatDist.add(arrayListOf(x.toString(), y.toString(), distance))
        }.start()
    }
}