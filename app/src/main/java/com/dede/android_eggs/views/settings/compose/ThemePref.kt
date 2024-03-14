package com.dede.android_eggs.views.settings.compose

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BrightnessAuto
import androidx.compose.material.icons.rounded.Brush
import androidx.compose.material.icons.rounded.Contrast
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dede.android_eggs.R
import com.dede.android_eggs.util.LocalEvent
import com.dede.android_eggs.util.ThemeUtils
import com.dede.android_eggs.util.pref
import com.dede.android_eggs.views.settings.compose.ThemePrefUtil.ACTION_NIGHT_MODE_CHANGED
import com.dede.android_eggs.views.settings.compose.ThemePrefUtil.AMOLED
import com.dede.android_eggs.views.settings.compose.ThemePrefUtil.DARK
import com.dede.android_eggs.views.settings.compose.ThemePrefUtil.FOLLOW_SYSTEM
import com.dede.android_eggs.views.settings.compose.ThemePrefUtil.KEY_NIGHT_MODE
import com.dede.android_eggs.views.settings.compose.ThemePrefUtil.LIGHT
import com.dede.android_eggs.views.theme.themeMode

object ThemePrefUtil {

    const val AMOLED = -2
    const val LIGHT = AppCompatDelegate.MODE_NIGHT_NO
    const val DARK = AppCompatDelegate.MODE_NIGHT_YES
    const val FOLLOW_SYSTEM = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM

    const val KEY_NIGHT_MODE = "pref_key_night_mode"

    const val ACTION_NIGHT_MODE_CHANGED = "action_night_mode_changed"
    fun isAmoledMode(context: Context): Boolean {
        return context.pref.getInt(KEY_NIGHT_MODE, FOLLOW_SYSTEM) == AMOLED
    }

    fun getThemeModeValue(context: Context): Int {
        return context.pref.getInt(KEY_NIGHT_MODE, FOLLOW_SYSTEM)
    }

    fun apply(context: Context) {
        var mode = getThemeModeValue(context)
        if (mode == AMOLED) {
            mode = DARK
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}

@Preview
@Composable
fun ThemePref() {
    val context = LocalContext.current
    var themeModeValue by rememberPrefIntState(KEY_NIGHT_MODE, FOLLOW_SYSTEM)
    val onOptionClick = click@{ mode: Int ->
        themeModeValue = mode
        themeMode = themeModeValue
        var appCompatMode = mode
        if (appCompatMode == AMOLED) {
            appCompatMode = DARK
        }
        if (appCompatMode == AppCompatDelegate.getDefaultNightMode()) {
            if ((mode == AMOLED) != ThemeUtils.isOLEDTheme(context)) {
                ThemeUtils.recreateActivityIfPossible(context)
                LocalEvent.poster(context).post(ACTION_NIGHT_MODE_CHANGED)
            }
            return@click
        }
        AppCompatDelegate.setDefaultNightMode(appCompatMode)
        LocalEvent.poster(context).post(ACTION_NIGHT_MODE_CHANGED)
    }

    ExpandOptionsPref(
        leadingIcon = Icons.Rounded.Brush,
        title = stringResource(R.string.pref_title_theme),
    ) {
        ValueOption(
            shape = OptionShapes.firstShape(),
            leadingIcon = imageVectorIconBlock(
                imageVector = Icons.Rounded.BrightnessAuto,
                contentDescription = stringResource(R.string.summary_system_default)
            ),
            title = stringResource(R.string.summary_system_default),
            trailingContent = radioButtonBlock(themeModeValue == FOLLOW_SYSTEM),
            value = FOLLOW_SYSTEM,
            onOptionClick = onOptionClick,
        )
        ValueOption(
            leadingIcon = imageVectorIconBlock(
                imageVector = Icons.Rounded.LightMode,
                contentDescription = stringResource(R.string.summary_theme_light_mode)
            ),
            title = stringResource(R.string.summary_theme_light_mode),
            trailingContent = radioButtonBlock(themeModeValue == LIGHT),
            value = LIGHT,
            onOptionClick = onOptionClick,
        )
        ValueOption(
            leadingIcon = imageVectorIconBlock(
                imageVector = Icons.Rounded.DarkMode,
                contentDescription = stringResource(R.string.summary_theme_dark_mode)
            ),
            title = stringResource(R.string.summary_theme_dark_mode),
            trailingContent = radioButtonBlock(themeModeValue == DARK),
            value = DARK,
            onOptionClick = onOptionClick,
        )
        ValueOption(
            shape = OptionShapes.lastShape(),
            leadingIcon = imageVectorIconBlock(
                imageVector = Icons.Rounded.Contrast,
                contentDescription = stringResource(id = R.string.summary_theme_amoled_mode)
            ),
            title = stringResource(id = R.string.summary_theme_amoled_mode),
            trailingContent = radioButtonBlock(themeModeValue == AMOLED),
            value = AMOLED,
            onOptionClick = onOptionClick,
        )

        if (DynamicColorPrefUtil.isSupported()) {
            Spacer(modifier = Modifier.height(1.dp))

            DynamicColorPref()
        }

    }
}
