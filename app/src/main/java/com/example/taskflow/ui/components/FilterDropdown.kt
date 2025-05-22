package com.example.taskflow.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.taskflow.ui.screens.home.HomeViewModel.FilterDone
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDropdown(
    selected: FilterDone,
    onSelected: (FilterDone) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val filterText = when (selected) {
        FilterDone.ALL -> "Барлығы"
        FilterDone.DONE -> "Аяқталғандар"
        FilterDone.NOT_DONE -> "Аяқталмағандар"
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        TextField(
            value = filterText,
            onValueChange = {},
            readOnly = true,
            label = { Text("Фильтр") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Барлығы") },
                onClick = {
                    onSelected(FilterDone.ALL)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Аяқталғандар") },
                onClick = {
                    onSelected(FilterDone.DONE)
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Аяқталмағандар") },
                onClick = {
                    onSelected(FilterDone.NOT_DONE)
                    expanded = false
                }
            )
        }
    }
}
