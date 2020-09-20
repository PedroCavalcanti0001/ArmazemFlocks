package me.zkingofkill.armazem.utils.extra

import me.zkingofkill.armazem.Main

fun String.cf(): Any? {
    return if (Main.singleton.config.get(this) is String) {
        Main.singleton.config.getString(this)
            .replace("&", "§")
            .replace("{prefix}", Main.singleton.config.getString("config.prefix").replace("&", "§"))
    } else {
        Main.singleton.config.get(this)
    }
}

