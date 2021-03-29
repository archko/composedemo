package cn.archko.sunflower.ui.utils

import GankBean
import GankResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class JsonUtils {

    companion object {
        fun parseGankBean(content: String): GankBean {
            return fromJson(content, object : TypeToken<GankBean?>() {}.type)
        }

        fun parseGankResponse(content: String?): GankResponse<MutableList<GankBean>> {
            return fromJson(content, object : TypeToken<GankResponse<MutableList<GankBean?>?>>() {}.type)
        }

        fun <T> toJson(data: T): String {
            return Gson().toJson(data)
        }

        fun <T> fromJson(content: String?, type: Type): T {
            return Gson().fromJson(content, type)
        }
    }
}
