package com.labinot.bajrami.fitnessapp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.labinot.bajrami.fitness.R
import com.labinot.bajrami.fitnessapp.ui.theme.FitnessAppTheme

@Composable
fun FitnessAppDialog(
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    title: String,
    confirmButtonText: String = "Yes",
    dismissButtonText: String = "Cancel",
    body:  @Composable (() -> Unit)? = null,
    onDialogDismiss: () -> Unit,
    onConfirmButtonClick: () -> Unit,
) {
    if (isOpen) {
        AlertDialog(
            modifier = modifier,
            title = { Text(text = title) },
            text = body,
            icon = {

                Image(
                    painter = painterResource(R.drawable.app_logo),
                    modifier = Modifier.size(50.dp),
                    contentDescription = ""
                )

            },
            onDismissRequest = { onDialogDismiss() },
            confirmButton = {
                TextButton(onClick = { onConfirmButtonClick() }) {
                    Text(text = confirmButtonText)
                }
            },
            dismissButton = {
                TextButton(onClick = { onDialogDismiss() }) {
                    Text(text = dismissButtonText)
                }
            }
        )
    }
}

@Preview
@Composable
private fun MeasureMateDialogPreview() {

    FitnessAppTheme {
        FitnessAppDialog(
            isOpen = true,
            title = "Login anonymously?",
            body = {
                Text(
                    text = "By logging in anonymously, you will not be able to synchronize the data " +
                            "across devices or after uninstalling the app. \nAre you sure you want to proceed?"
                )
            },
            onDialogDismiss = {},
            onConfirmButtonClick = {}
        )


    }

}