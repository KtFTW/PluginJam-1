package net.stckoverflw.pluginjam.util

import net.kyori.adventure.text.Component

fun mini(string: String): Component {
    return string.deserializeMini()
}
