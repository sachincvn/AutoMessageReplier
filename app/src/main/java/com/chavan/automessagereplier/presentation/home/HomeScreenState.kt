package com.chavan.automessagereplier.presentation.home

import com.chavan.automessagereplier.domain.model.CustomMessage

data class HomeScreenState(
  val customMessages: List<CustomMessage> = emptyList(),
  val activeAutoMessage : Boolean = false,
  val isAutoMessagingActive : Boolean = false,
  val isLoading: Boolean = false,
  val errorMessage: String? = null
)