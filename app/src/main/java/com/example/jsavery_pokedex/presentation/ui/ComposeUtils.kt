package com.example.jsavery_pokedex.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

@Composable
fun Modifier.dismissKeyboardOnTouch(): Modifier {
    val keyboardController = LocalSoftwareKeyboardController.current
    return pointerInput(Unit) {
        detectTouch { keyboardController?.hide() }
    }
}

suspend fun PointerInputScope.detectTouch(invokedAction: () -> Unit?) {
    awaitPointerEventScope {
        while (true) {
            val event = awaitPointerEvent()

            if (event.type == PointerEventType.Press) {
                invokedAction()
            }
        }
    }
}