package com.labinot.bajrami.fitnessapp.presentation.screens.addItem

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.measuremate.presentation.add_item.AddItemEvent
import com.example.measuremate.presentation.add_item.AddItemState
import com.labinot.bajrami.fitnessapp.presentation.components.FitnessAppDialog
import com.labinot.bajrami.fitnessapp.presentation.util.UiEvent
import kotlinx.coroutines.flow.Flow

@Composable
fun AddItemScreen(
    snackbarHostState: SnackbarHostState,
    state: AddItemState,
    uiEvent: Flow<UiEvent>,
    onEvent: (AddItemEvent) -> Unit,
    onBackIconClick: () -> Unit
){

    LaunchedEffect(key1 = Unit) {
        uiEvent.collect { event ->
            when (event) {
                is UiEvent.Snackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                UiEvent.HideBottomSheet -> {}
                UiEvent.Navigate -> {}
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        var isAddNewItemDialogOpen by rememberSaveable { mutableStateOf(false) }
        FitnessAppDialog(
            isOpen = isAddNewItemDialogOpen,
            title = "Add/Update New Item",
            confirmButtonText = "Save",
            body = {
                OutlinedTextField(
                    value = state.textFieldValue,
                    onValueChange = {
                        onEvent(AddItemEvent.OnTextFieldValueChange(it))
                    }
                )
            },
            onDialogDismiss = {
                isAddNewItemDialogOpen = false
                onEvent(AddItemEvent.OnAddItemDialogDismiss)
            },
            onConfirmButtonClick = {
                isAddNewItemDialogOpen = false
                onEvent(AddItemEvent.UpsertItem)
            }
        )

      AddItemTopBar(
            onAddIconClick = { isAddNewItemDialogOpen = true },
            onBackIconClick = onBackIconClick
        )
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Adaptive(minSize = 300.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            items(items = state.bodyParts) { bodyPart ->

                ItemCard(
                    name = bodyPart.name,
                    isChecked = bodyPart.isActive,
                    onCheckedChange = {
                        onEvent(AddItemEvent.OnItemIsActiveChange(bodyPart))
                                      },
                    onClick = {
                        isAddNewItemDialogOpen = true
                        onEvent(AddItemEvent.OnItemClick(bodyPart))
                    }
                )
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddItemTopBar(
    modifier: Modifier = Modifier,
    onAddIconClick: () -> Unit,
    onBackIconClick: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        windowInsets = WindowInsets(0, 0, 0, 0),
        title = { Text(text = "Add Item") },
        navigationIcon = {
            IconButton(onClick = { onBackIconClick() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Navigate Back"
                )
            }
        },
        actions = {
            IconButton(onClick = { onAddIconClick() }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add New Item")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

@Composable
private fun ItemCard(
    modifier: Modifier = Modifier,
    name: String,
    isChecked: Boolean,
    onCheckedChange: () -> Unit,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier.clickable { onClick() },
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.weight(8f),
                text = name,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = isChecked,
                onCheckedChange = { onCheckedChange() }
            )
        }
    }
}