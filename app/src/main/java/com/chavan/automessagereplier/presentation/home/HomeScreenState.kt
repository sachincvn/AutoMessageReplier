package com.chavan.automessagereplier.presentation.home

import com.chavan.automessagereplier.data.local.custom_message.ReceivedPattern
import com.chavan.automessagereplier.data.local.custom_message.ReplyToOption
import com.chavan.automessagereplier.domain.model.custom_message.CustomMessage

data class HomeScreenState(
    val customMessages: List<CustomMessage> = emptyList(),
    val categories: List<Category> = listOf(
        Category("Custom", emptyList()),
        Category("Work", defaultWorkMessages),
        Category("Family", defaultFamilyMessages),
        Category("Friends", defaultFriendMessages),
        Category("Events", defaultEventMessages)
    ),
    val activeAutoMessage: Boolean = false,
    val isAutoMessagingActive: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class Category(
    val title: String,
    val messages: List<CustomMessage>
)

// Default templates
val defaultWorkMessages = listOf(
    CustomMessage("Meeting?", "Yes, I'll attend the meeting.",ReceivedPattern.ExactMatch,ReplyToOption.All),
    CustomMessage("Update?", "The project update has been shared.",ReceivedPattern.ExactMatch,ReplyToOption.All),
    CustomMessage("Deadline?", "The deadline is on Friday.",ReceivedPattern.ExactMatch,ReplyToOption.All)
)

val defaultFamilyMessages = listOf(
    CustomMessage("Dinner?", "Yes, I'll join for dinner.",ReceivedPattern.ExactMatch,ReplyToOption.All),
    CustomMessage("Call?", "I'll call you in 10 minutes.",ReceivedPattern.ExactMatch,ReplyToOption.All),
    CustomMessage("Home?", "I'll reach home by 7 PM.",ReceivedPattern.ExactMatch,ReplyToOption.All)
)

val defaultFriendMessages = listOf(
    CustomMessage("Plan?", "How about a game night this weekend?",ReceivedPattern.ExactMatch,ReplyToOption.All),
    CustomMessage("Movie?", "Let's decide on a movie to watch.",ReceivedPattern.ExactMatch,ReplyToOption.All),
    CustomMessage("Meetup?", "Let's catch up tomorrow evening.",ReceivedPattern.ExactMatch,ReplyToOption.All)
)

val defaultEventMessages = listOf(
    CustomMessage("Birthday?", "Happy Birthday! Have a great day!",ReceivedPattern.ExactMatch,ReplyToOption.All),
    CustomMessage("Anniversary?", "Happy Anniversary! Wishing you many more!",ReceivedPattern.ExactMatch,ReplyToOption.All),
    CustomMessage("Reminder?", "Don't forget the event at 5 PM.",ReceivedPattern.ExactMatch,ReplyToOption.All)
)
