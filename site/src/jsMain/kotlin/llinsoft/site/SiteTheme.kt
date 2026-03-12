package llinsoft.site

import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.background
import com.varabyte.kobweb.silk.theme.colors.palette.color

class SitePalette(
    val background: Color,
    val surface: Color,
    val elevatedSurface: Color,
    val border: Color,
    val textPrimary: Color,
    val textMuted: Color,
    val shadow: Color,
    val cobweb: Color,
    val brand: Brand,
) {
    class Brand(
        val cyan: Color,
        val lime: Color,
        val gradientStart: Color,
        val gradientEnd: Color,
    )
}

object SitePalettes {
    val light = SitePalette(
        background = Color.rgb(0xF3F6FA),
        surface = Color.rgb(0xFFFFFF),
        elevatedSurface = Color.rgb(0xF7FAFF),
        border = Color.rgb(0xD1D9E6),
        textPrimary = Colors.Black,
        textMuted = Color.rgb(0x526072),
        shadow = Color.rgba(17, 24, 39, 0.16f),
        cobweb = Colors.LightGray,
        brand = SitePalette.Brand(
            cyan = Color.rgb(0x22D3EE),
            lime = Color.rgb(0x84CC16),
            gradientStart = Color.rgb(0x22D3EE),
            gradientEnd = Color.rgb(0x84CC16),
        )
    )

    val dark = SitePalette(
        background = Color.rgb(0x0F172A),
        surface = Color.rgb(0x1E1F22),
        elevatedSurface = Color.rgb(0x2B2D31),
        border = Color.rgba(255, 255, 255, 0.16f),
        textPrimary = Color.rgb(0xE5E7EB),
        textMuted = Color.rgb(0x9CA3AF),
        shadow = Color.rgba(2, 6, 23, 0.36f),
        cobweb = Colors.LightGray.inverted(),
        brand = SitePalette.Brand(
            cyan = Color.rgb(0x22D3EE),
            lime = Color.rgb(0x84CC16),
            gradientStart = Color.rgb(0x22D3EE),
            gradientEnd = Color.rgb(0x84CC16),
        )
    )
}

fun ColorMode.toSitePalette(): SitePalette {
    return when (this) {
        ColorMode.LIGHT -> SitePalettes.light
        ColorMode.DARK -> SitePalettes.dark
    }
}

@InitSilk
fun initTheme(ctx: InitSilkContext) {
    ctx.theme.palettes.light.background = SitePalettes.light.background
    ctx.theme.palettes.light.color = SitePalettes.light.textPrimary
    ctx.theme.palettes.dark.background = SitePalettes.dark.background
    ctx.theme.palettes.dark.color = SitePalettes.dark.textPrimary
}
