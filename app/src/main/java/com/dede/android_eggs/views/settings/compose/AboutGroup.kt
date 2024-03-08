package com.dede.android_eggs.views.settings.compose

import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Balance
import androidx.compose.material.icons.rounded.Coffee
import androidx.compose.material.icons.rounded.Feedback
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Policy
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.StarRate
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.dede.android_eggs.BuildConfig
import com.dede.android_eggs.R
import com.dede.android_eggs.util.CustomTabsBrowser
import com.dede.android_eggs.util.createChooser
import com.dede.basic.requireDrawable
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Preview
@Composable
fun AboutGroup() {
    val context = LocalContext.current

    fun launchUrl(@StringRes url: Int) {
        CustomTabsBrowser.launchUrl(context, context.getString(url).toUri())
    }

    ExpandOptionsPref(
        leadingIcon = Icons.Rounded.Info,
        title = stringResource(R.string.label_about)
    ) {
        Option(
            shape = OptionShapes.firstShape(),
            leadingIcon = {
                val drawable =
                    rememberDrawablePainter(context.requireDrawable(R.mipmap.ic_launcher_round))
                Image(
                    painter = drawable,
                    contentDescription = stringResource(
                        R.string.label_version,
                        BuildConfig.VERSION_NAME,
                        BuildConfig.VERSION_CODE
                    ),
                    modifier = Modifier.size(24.dp)
                )
            },
            title = stringResource(
                R.string.label_version,
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE
            ),
            desc = BuildConfig.GIT_HASH,
            onClick = {
                CustomTabsBrowser.launchUrl(
                    context,
                    context.getString(R.string.url_github_commit, BuildConfig.GIT_HASH).toUri()
                )
            }
        )
        Option(
            leadingIcon = {
                Box(
                    modifier = Modifier.size(24.dp),
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_pgyer_logo),
                        contentDescription = stringResource(R.string.label_beta),
                        modifier = Modifier.requiredSize(33.dp)
                    )
                }
            },
            title = stringResource(R.string.label_beta),
            desc = stringResource(R.string.url_beta),
            onClick = {
                launchUrl(R.string.url_beta)
            }
        )
        Option(
            leadingIcon = {
                Image(
                    painter = painterResource(R.drawable.ic_github_logo),
                    contentDescription = stringResource(R.string.label_github),
                    modifier = Modifier.size(24.dp)
                )
            },
            title = stringResource(R.string.label_github),
            desc = stringResource(R.string.url_github),
            onClick = {
                launchUrl(R.string.url_github)
            }
        )
        Option(
            leadingIcon = imageVectorIconBlock(
                imageVector = Icons.Rounded.Translate,
                contentDescription = stringResource(R.string.label_translation),
            ),
            title = stringResource(R.string.label_translation),
            desc = stringResource(R.string.url_translation),
            onClick = {
                launchUrl(R.string.url_translation)
            }
        )
        Option(
            leadingIcon = imageVectorIconBlock(
                imageVector = Icons.Rounded.Coffee,
                contentDescription = stringResource(R.string.label_donate),
            ),
            title = stringResource(R.string.label_donate),
            onClick = {
                launchUrl(R.string.url_sponsor)
            }
        )
        Option(
            leadingIcon = imageVectorIconBlock(
                imageVector = Icons.Rounded.Share,
                contentDescription = stringResource(id = R.string.label_share)
            ),
            title = stringResource(R.string.label_share),
            onClick = {
                val target = Intent(Intent.ACTION_SEND)
                    .putExtra(Intent.EXTRA_TEXT, context.getString(R.string.url_share))
                    .setType("text/plain")
                val intent = context.createChooser(target)
                context.startActivity(intent)
            }
        )
        Option(
            leadingIcon = imageVectorIconBlock(
                imageVector = Icons.Rounded.StarRate,
                contentDescription = stringResource(R.string.label_star),
            ),
            title = stringResource(R.string.label_star),
            onClick = {
                CustomTabsBrowser.launchUrlByBrowser(
                    context,
                    context.getString(R.string.url_market_detail).toUri()
                )
            }
        )

        Option(
            leadingIcon = imageVectorIconBlock(
                imageVector = Icons.Rounded.Policy,
                contentDescription = stringResource(R.string.label_privacy_policy),
            ),
            title = stringResource(R.string.label_privacy_policy),
            desc = stringResource(R.string.url_privacy),
            onClick = {
                launchUrl(R.string.url_privacy)
            }
        )
        Option(
            leadingIcon = imageVectorIconBlock(
                imageVector = Icons.Rounded.Balance,
                contentDescription = stringResource(R.string.label_license),
            ),
            title = stringResource(R.string.label_license),
            desc = "Apache-2.0 license",
            onClick = {
                launchUrl(R.string.url_license)
            }
        )
        Option(
            shape = OptionShapes.lastShape(),
            leadingIcon = imageVectorIconBlock(
                imageVector = Icons.Rounded.Feedback,
                contentDescription = stringResource(R.string.label_email),
            ),
            title = stringResource(R.string.label_email),
            desc = stringResource(R.string.url_github_issues),
            onClick = {
                launchUrl(R.string.url_github_issues)
            }
        )
    }
}