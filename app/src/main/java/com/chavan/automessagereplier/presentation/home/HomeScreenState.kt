package com.chavan.automessagereplier.presentation.home

import com.chavan.automessagereplier.core.utils.ScreenState
import com.chavan.automessagereplier.domain.model.CustomMessage

data class HomeScreenState(
  val result: ScreenState<List<CustomMessage>> = ScreenState.Success(emptyList())
)