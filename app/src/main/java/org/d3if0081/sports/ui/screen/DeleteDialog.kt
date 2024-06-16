package org.d3if0081.sports.ui.screen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.d3if0081.sports.R
import org.d3if0081.sports.model.Sports


@Composable
fun DeleteDialog(sports: Sports, onDismissRequest: () -> Unit, onConfirmation: (Long) -> Unit, id: Long) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = {
            Text(text = "Want to delete this ${sports.nama} item ?")
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirmation(id)
            }) {
                Text(text = stringResource(id = R.string.hapus))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(text = stringResource(id = R.string.batal))
            }
        }
    )
}