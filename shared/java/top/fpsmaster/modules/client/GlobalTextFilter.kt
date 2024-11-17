package top.fpsmaster.modules.client

import top.fpsmaster.features.impl.utility.IRC
import top.fpsmaster.features.impl.utility.NameProtect
import top.fpsmaster.wrapper.TextFormattingProvider

object GlobalTextFilter {
    @JvmStatic
    @Synchronized
    fun filter(text: String): String {
        if (!IRC.using || !IRC.showMates.value) {
            return NameProtect.filter(text)
        }

        var result = StringBuilder(text)
        result = StringBuilder(NameProtect.filter(result.toString()))
        return result.toString()
    }
}
