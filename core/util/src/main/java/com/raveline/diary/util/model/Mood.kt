package com.raveline.diary.util.model

import androidx.compose.ui.graphics.Color
import com.raveline.diary.ui.theme.AngryColor
import com.raveline.diary.ui.theme.AnxiousColor
import com.raveline.diary.ui.theme.AwfulColor
import com.raveline.diary.ui.theme.BoredColor
import com.raveline.diary.ui.theme.CalmColor
import com.raveline.diary.ui.theme.DepressedColor
import com.raveline.diary.ui.theme.DisappointedColor
import com.raveline.diary.ui.theme.HappyColor
import com.raveline.diary.ui.theme.HumorousColor
import com.raveline.diary.ui.theme.LonelyColor
import com.raveline.diary.ui.theme.MysteriousColor
import com.raveline.diary.ui.theme.NeutralColor
import com.raveline.diary.ui.theme.RomanticColor
import com.raveline.diary.ui.theme.ShamefulColor
import com.raveline.diary.ui.theme.SurprisedColor
import com.raveline.diary.ui.theme.SuspiciousColor
import com.raveline.diary.ui.theme.TenseColor
import com.raveline.diary.util.R


enum class Mood(
    val icon: Int,
    val contentColor: Color,
    val containerColor: Color
) {
    Angry(
        icon = R.drawable.angry,
        contentColor = Color.White,
        containerColor = AngryColor
    ),
    Anxious(
        icon = R.drawable.depressed,
        contentColor = Color.White,
        containerColor = AnxiousColor
    ),
    Awful(
        icon = R.drawable.awful,
        contentColor = Color.Black,
        containerColor = AwfulColor
    ),
    Bored(
        icon = R.drawable.bored,
        contentColor = Color.Black,
        containerColor = BoredColor
    ),
    Calm(
        icon = R.drawable.calm,
        contentColor = Color.Black,
        containerColor = CalmColor
    ),
    Depressed(
        icon = R.drawable.depressed,
        contentColor = Color.Black,
        containerColor = DepressedColor
    ),
    Disappointed(
        icon = R.drawable.disappointed,
        contentColor = Color.White,
        containerColor = DisappointedColor
    ),
    Happy(
        icon = R.drawable.happy,
        contentColor = Color.Black,
        containerColor = HappyColor
    ),
    Humorous(
        icon = R.drawable.humorous,
        contentColor = Color.Black,
        containerColor = HumorousColor
    ),
    Lonely(
        icon = R.drawable.lonely,
        contentColor = Color.White,
        containerColor = LonelyColor
    ),
    Mysterious(
        icon = R.drawable.mysterious,
        contentColor = Color.Black,
        containerColor = MysteriousColor
    ),
    Neutral(
        icon = R.drawable.neutral,
        contentColor = Color.Black,
        containerColor = NeutralColor
    ),
    Romantic(
        icon = R.drawable.romantic,
        contentColor = Color.White,
        containerColor = RomanticColor
    ),
    Shameful(
        icon = R.drawable.shameful,
        contentColor = Color.White,
        containerColor = ShamefulColor
    ),

    Surprised(
        icon = R.drawable.surprised,
        contentColor = Color.Black,
        containerColor = SurprisedColor
    ),
    Suspicious(
        icon = R.drawable.suspicious,
        contentColor = Color.Black,
        containerColor = SuspiciousColor
    ),
    Tense(
        icon = R.drawable.tense,
        contentColor = Color.Black,
        containerColor = TenseColor
    )
}
