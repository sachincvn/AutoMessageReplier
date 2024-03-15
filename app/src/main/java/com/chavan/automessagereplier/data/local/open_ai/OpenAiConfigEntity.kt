package com.chavan.automessagereplier.data.local.open_ai

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OpenAiConfigEntity(
    val openAiApiKey : String? = null,
    val openAiModel : OpenAiModelEnum? = OpenAiModelEnum.GPT_3_5_TURBO,
    val temperature : Double? = 0.7,
    val errorMessage : String? = null,
    @PrimaryKey
    val id : Int  = 0
)


enum class OpenAiModelEnum(val value: String? = null) {
    GPT_3_5_TURBO("gpt-3.5-turbo"),
    GPT_4("gpt-4"),
    GPT_3_5_TURBO_0301("gpt-3.5-turbo-0301"),
}
