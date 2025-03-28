package isel.leic.tds.checkers.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import isel.leic.tds.checkers.model.Piece
import isel.leic.tds.checkers.model.Player
import isel.leic.tds.checkers.model.Score

@Composable
fun ScoreDialog(
    score: Score?,
    onClose: ()->Unit,
) = AlertDialog(
    onDismissRequest = onClose,
    confirmButton = {
        TextButton(onClick = onClose) { Text("Close") }
    },
    title = { Text("Score", style = MaterialTheme.typography.h3) },
    text = { ScoreContent(score) }
)

@Composable
private fun ScoreContent(score: Score?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Player.entries.forEach {
                Row {
                    Player(it, modifier = Modifier.size(32.dp))
                    Text(" - ${score?.get(it) ?: "0"}", style = MaterialTheme.typography.h4)
                }
            }
        }
    }
}

@Composable
@Preview
fun ScoreDialogPreview()  {
    ScoreContent(
        score = (Player.entries+null).associateWith { 1 },
    )
}
