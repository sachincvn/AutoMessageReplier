package com.chavan.automessagereplier.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CustomMessageEntity(
    val receivedMessage: String,
    val replyMessage: String,
    val isActive: Boolean = false,
    val receivedPattern: ReceivedPattern = ReceivedPattern.ExactMatch,
    val replyToOption: ReplyToOption = ReplyToOption.SavedContact,
    val selectedContacts: List<String> = emptyList(),
    val replyWithChatGptApi : Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)

enum class ReceivedPattern(val value: String? = null) {
    AnyMessage("Any message"),
    ExactMatch("Exact match"),
    Contains("Contains"),
    StartsWith("Starts with"),
    EndsWith("Ends with"),
    SimilarMatch("Similar match")
}

enum class ReplyToOption(val value: String? = null) {
    All("Everyone"),
    SavedContact("Saved contact"),
    UnknownContact("Unknown Contact"),
    Group("Groups only"),
    SpecificContacts("Specific Contacts"),
}