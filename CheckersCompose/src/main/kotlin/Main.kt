import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.*
import isel.leic.tds.checkers.model.*
import isel.leic.tds.checkers.storage.MongoDriver
import isel.leic.tds.checkers.ui.*

const val BOARD_DIM = 8

@Composable
@Preview
private fun FrameWindowScope.BoardApp(vm: AppViewModel) {
    MaterialTheme {
        Column {
            GameView(
                board = vm.board ,
                showTargets = vm.showTargets,
                selectedMove = vm.selectedMove,
                sidePlayer = vm.sidePlayer ?: Player.WHITE ,
                onClickSquare = { square: Square -> vm.selectSquare(square) }
            )
            StatusBarView(vm.clash)
        }
        if(vm.scoreView) ScoreDialog(vm.score, onClose = vm::hideScore  )
        vm.action?.let { NameEdit(it, vm::cancelAction, vm::doAction) }
        vm.message?.let { Message(it, vm::cancelMessage) }
    }
}

@Composable
fun FrameWindowScope.BoardMenu(vm: AppViewModel, onExit: ()->Unit){
    MenuBar {
        Menu("Game"){
            Item("New board", enabled = vm.isSideTurn, onClick = vm::newBoard)
            Item("Score",  enabled = vm.hasClash, onClick = vm::showScore)
            Item("Exit", onClick = onExit )
            Item("Refresh",  enabled = vm.hasClash && !vm.autoRefresh, onClick = vm::refresh)
        }
        Menu("Clash") {
            Item("Start", onClick = vm::start)
            Item("Join", onClick = vm::join)
        }
        Menu("Options"){
            CheckboxItem(
                text = "Shows targets",
                checked = vm.showTargets,
                onCheckedChange =  vm::changeShowTargets
            )
            CheckboxItem(
                text = "Auto Refresh",
                checked = vm.autoRefresh,
                onCheckedChange = vm::changeAutoRefresh
            )
        }
    }
}

fun main() {
    MongoDriver("test").use { driver ->
        application(exitProcessOnExit = false) {
            val scope = rememberCoroutineScope()
            val vm = remember { AppViewModel(scope,driver) }
            val onExit = { vm.exit(); exitApplication() }
            Window(
                onCloseRequest = onExit,
                state = WindowState(size = DpSize.Unspecified),
                title = "Checkers"
            ) {
                BoardMenu(vm, onExit)
                BoardApp(vm)
            }
        }
    }
}
