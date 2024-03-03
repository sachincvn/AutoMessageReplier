package com.chavan.automessagereplier.presentation.todo_upsert

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.chavan.automessagereplier.core.common.NewUpdateStrings
import com.chavan.automessagereplier.core.common.StringResources
import com.chavan.automessagereplier.core.presentation.componenets.ArchiveButton
import com.chavan.automessagereplier.core.presentation.componenets.CompleteButton
import com.chavan.automessagereplier.core.presentation.componenets.DeleteButton
import com.chavan.automessagereplier.core.presentation.componenets.getTodoColors
import com.chavan.automessagereplier.presentation.todo_upsert.components.HintTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoUpsertScreen(
    navController: NavController,
    viewModel: TodoUpsertViewModel = hiltViewModel()
){
    val state = viewModel.state.value
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val scope = rememberCoroutineScope()
    val todoColors = getTodoColors(todoItem = state.todoItem)

    val configuration = LocalConfiguration.current
    val isPortrait =
        configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT

    val topBarHeight = if(isPortrait) 64.dp else 0.dp
    val horizontalPadding = 16.dp
    val verticalPadding = if(isPortrait) 16.dp else 2.dp

    LaunchedEffect(key1 = true ){
        viewModel.eventFlow.collectLatest {event ->
            when(event){
                TodoUpsertViewModel.UIEvent.Back -> {
                    navController.navigateUp()
                }
                TodoUpsertViewModel.UIEvent.SaveTodo -> {
                    navController.navigateUp()
                }
                is TodoUpsertViewModel.UIEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            if (!isPortrait) {
                FloatingActionButton(
                    onClick = {
                        viewModel.onEvent(TodoUpsertEvent.SaveTodo)
                    },
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Save Todo",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        topBar = {
            if (isPortrait) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = StringResources.todoList,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                        scrolledContainerColor = MaterialTheme.colorScheme.primary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                viewModel.onEvent(TodoUpsertEvent.Back)
                            },
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    actions = {},
                    scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
                )

            }
        },
        bottomBar = {
            if (isPortrait) {
                BottomAppBar(
                    actions = {
                        CompleteButton(
                            onCompleteClick = {
                                viewModel.onEvent(TodoUpsertEvent.ToggleCompleted)
                            },
                            color = todoColors.checkColor,
                            completed = state.todoItem.isCompleted
                        )
                        ArchiveButton(
                            onArchiveClick = {
                                viewModel.onEvent(TodoUpsertEvent.ToggleArchived)
                            }
                        )
                        DeleteButton(
                            onDeleteClick = {
                                scope.launch {
                                    val confirm = snackbarHostState.showSnackbar(
                                        message = NewUpdateStrings.CONFIRM_DELETE,
                                        actionLabel = NewUpdateStrings.YES
                                    )
                                    if (confirm == SnackbarResult.ActionPerformed) {
                                        viewModel.onEvent(TodoUpsertEvent.Delete)
                                    }
                                }
                            }
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                viewModel.onEvent(TodoUpsertEvent.SaveTodo)
                            },
                            shape = CircleShape,
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = "Save Todo",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.5f)
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)}
    ) {
        Box(
            contentAlignment = Alignment.TopStart,
            modifier = Modifier
                .fillMaxSize()
                .background(color = todoColors.backgroundColor)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = topBarHeight)
                    .fillMaxSize()
            ){
                HintTextField(
                    text = state.todoItem.title,
                    hint = NewUpdateStrings.TITLE_HINT,
                    textColor = todoColors.textColor,
                    onValueChange = {
                        viewModel.onEvent(TodoUpsertEvent.EnteredTitle(it))
                    },
                    onFocusChange = {
                        viewModel.onEvent(TodoUpsertEvent.ChangedTitleFocus(it))
                    },
                    isHintVisible = state.isTitleHintVisible,
                    singleLine = true,
                    fontSize = 40.sp,
                    modifier = Modifier.padding(
                        horizontal = horizontalPadding,
                        vertical = verticalPadding
                    )
                )
                Spacer(modifier = Modifier.height(verticalPadding))
                HintTextField(
                    text = state.todoItem.description,
                    hint = NewUpdateStrings.DESCRIPTION_HINT,
                    textColor = todoColors.textColor,
                    onValueChange = {
                        viewModel.onEvent(TodoUpsertEvent.EnteredDescription(it))
                    },
                    onFocusChange = {
                        viewModel.onEvent(TodoUpsertEvent.ChangedDescriptionFocus(it))
                    },
                    isHintVisible = state.isDescriptionHintVisible,
                    singleLine = false,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(
                            horizontal = horizontalPadding,
                            vertical = verticalPadding
                        )
                )
            }
        }

    }
}