package me.zkingofkill.armazem.utils.extra

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*


fun Double.formatMoney(): String {
    var Local = Locale("pt", "BR")
    var formatter = DecimalFormat("#,##0.00", DecimalFormatSymbols(Local))
    return formatter.format(this)
}
