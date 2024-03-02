package com.chavan.automessagereplier.presentation.todo_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chavan.automessagereplier.core.presentation.componenets.ArchiveButton
import com.chavan.automessagereplier.core.presentation.componenets.CompleteButton
import com.chavan.automessagereplier.core.presentation.componenets.DeleteButton
import com.chavan.automessagereplier.core.presentation.componenets.getTodoColors
import com.chavan.automessagereplier.domain.model.TodoItem
import com.chavan.automessagereplier.ui.theme.AutoMessageReplierTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoItemCard(
    todoItem: TodoItem,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit,
    onCompleteClick: () -> Unit,
    onArchiveClick: () -> Unit,
    onCardClick: () -> Unit,
){
    val todoColors = getTodoColors(todoItem = todoItem)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        onClick = onCardClick,
        colors = CardDefaults.cardColors(containerColor = todoColors.backgroundColor)
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ){
            CompleteButton(onCompleteClick, todoColors.checkColor, todoItem.isCompleted)
            Text(
                text = todoItem.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = todoColors.textColor,
                fontSize = 32.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Row (
            verticalAlignment = Alignment.Top
        ){
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.Top
            ){
                Text(
                    text = todoItem.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = todoColors.textColor,
                    fontSize = 24.sp,
                    maxLines = 10,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.1f)
                    .padding(end = 4.dp)
            ) {
                ArchiveButton(onArchiveClick = onArchiveClick, color = todoColors.archiveIconColor)
                DeleteButton(onDeleteClick = onDeleteClick)
            }
        }
    }
}

@Preview
@Composable
fun TodoItemCardPreview(){
    AutoMessageReplierTheme {
        TodoItemCard(
            todoItem = TodoItem(
                title = "Hello World, Title",
                description = "Here the description comes",
                isCompleted = true,
                isArchived = false,
                timStamp = 112234564,
                id = 0,
            ),
            onDeleteClick = { /*TODO*/ },
            onCompleteClick = { /*TODO*/ },
            onArchiveClick = { /*TODO*/ }) {

        }
    }
}