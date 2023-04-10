package com.dede.android_eggs.main.holders

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.view.View
import coil.dispose
import coil.load
import com.dede.android_eggs.databinding.ItemEasterEggLayoutBinding
import com.dede.android_eggs.main.EggActionHelp
import com.dede.android_eggs.main.entity.Egg
import com.dede.android_eggs.settings.IconShapePerf
import com.dede.android_eggs.ui.Icons
import com.dede.android_eggs.ui.adapter.VHType
import com.dede.android_eggs.ui.adapter.VHolder
import com.dede.android_eggs.ui.drawables.AlterableAdaptiveIconDrawable
import com.dede.android_eggs.ui.drawables.FontIconsDrawable
import com.dede.android_eggs.util.resolveColorStateList
import com.dede.android_eggs.util.updateCompoundDrawablesRelative
import kotlin.math.abs
import kotlin.math.max
import com.google.android.material.R as M3R

@VHType(viewType = Egg.VIEW_TYPE_EGG)
open class EggHolder(view: View) : VHolder<Egg>(view) {

    val binding: ItemEasterEggLayoutBinding = ItemEasterEggLayoutBinding.bind(view)
    val context: Context = itemView.context

    private val matrix = Matrix()
    private var lastXDegrees: Float = 0f
    private var lastYDegrees: Float = 0f
    private var animator: ValueAnimator? = null

    private fun Float.toRoundDegrees(): Float {
        return ((Math.toDegrees(toDouble())) % 90f).toFloat()
    }

    private fun calculateAnimDegrees(old: Float, new: Float, fraction: Float): Float {
        return old + (new - old) * fraction
    }

    fun updateOrientationAngles(xAngle: Float, yAngle: Float) {
        val iconDrawable = binding.ivIcon.drawable as? AlterableAdaptiveIconDrawable ?: return
        if (!iconDrawable.isAdaptiveIconDrawable) return

        val xDegrees = xAngle.toRoundDegrees()// 俯仰角
        val yDegrees = yAngle.toRoundDegrees()// 侧倾角
        if (max(abs(lastXDegrees - xDegrees), abs(lastYDegrees - yDegrees)) < 6f) return

        val bounds = iconDrawable.bounds
        val width = bounds.width() / 6f
        val height = bounds.height() / 6f

        val saveLastXDegrees = lastXDegrees
        val saveLastYDegrees = lastYDegrees
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = 100
        animator.addUpdateListener {
            val animX = calculateAnimDegrees(saveLastXDegrees, xDegrees, it.animatedFraction)
            val animY = calculateAnimDegrees(saveLastYDegrees, yDegrees, it.animatedFraction)
            val dx = animY / 90f * width * -1f
            val dy = animX / 90f * height
            matrix.reset()
            matrix.setTranslate(dx, dy)
            iconDrawable.setForegroundMatrix(matrix)
        }
        animator.start()
        this.animator?.cancel()
        this.animator = animator
        this.lastYDegrees = yDegrees
        this.lastXDegrees = xDegrees
    }

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun onBindViewHolder(egg: Egg) {
        binding.tvTitle.setText(egg.eggNameRes)
        binding.tvSummary.setText(egg.androidRes)
        binding.cardView.setOnClickListener { EggActionHelp.launchEgg(context, egg) }
        binding.background.tvBgMessage.setText(egg.versionCommentRes)
        binding.background.tvAddShortcut.isEnabled = EggActionHelp.supportShortcut(context, egg)

        binding.ivIcon.dispose()
        binding.background.ivBgIcon.dispose()
        if (egg.supportAdaptiveIcon) {
            val pathStr = IconShapePerf.getMaskPath(context)
            binding.ivIcon.setImageDrawable(
                AlterableAdaptiveIconDrawable(context, egg.iconRes, pathStr)
            )
            binding.background.ivBgIcon.setImageDrawable(
                AlterableAdaptiveIconDrawable(context, egg.iconRes, pathStr)
            )
        } else {
            binding.ivIcon.load(egg.iconRes)
            binding.background.ivBgIcon.load(egg.iconRes)
        }

        val color = context.resolveColorStateList(
            M3R.attr.textAppearanceLabelMedium, android.R.attr.textColor
        )
        val drawable = FontIconsDrawable(context, Icons.Outlined.app_shortcut, 22f).apply {
            setColorStateList(color)
        }
        binding.background.tvAddShortcut.updateCompoundDrawablesRelative(end = drawable)
    }

}