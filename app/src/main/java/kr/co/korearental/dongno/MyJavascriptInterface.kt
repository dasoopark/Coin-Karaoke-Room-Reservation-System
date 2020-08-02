package kr.co.korearental.dongno

import android.webkit.JavascriptInterface
import org.jsoup.Jsoup

class MyJavascriptInterface {
    @JavascriptInterface
    fun getHtml(html: String?) { //위 자바스크립트가 호출되면 여기로 html이 반환됨
        Thread(){
            val builder = StringBuilder()
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
                    builder.append("$title, $number, $address\n")
                }
            }catch (e:Exception){
                builder.append("e")
            }
            println(builder)
        }.start()
    }
}