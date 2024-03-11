package com.chavan.automessagereplier.presentation.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.chavan.automessagereplier.core.utils.ScreenState
import com.chavan.automessagereplier.domain.model.CustomMessage

data class HomeScreenState(
  val result: ScreenState<List<CustomMessage>> = ScreenState.Success(emptyList()),
  val activeAutoMessage : MutableState<Boolean> = mutableStateOf(true)
)