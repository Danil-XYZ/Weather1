package com.example.weather1.ui.components

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weather1.R

@Composable
fun DrawerPanel(menuItems: List<MenuItems>, activeItem: MenuItems, onClick: (MenuItems) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Spacer(modifier = Modifier.height(16.dp))
        menuItems.forEach {
            Log.e("Drawer", "$it ${it.title}")
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 64.dp)
                    .clickable { onClick(it) }
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(id = it.title), color = Color.Black)
            }
        }
    }
}

@Preview
@Composable
fun DemoDrawer(){
    DrawerPanel(listOf(MenuItems.Settings, MenuItems.Share), MenuItems.Settings, {})
}

enum class MenuItems(@StringRes val title: Int) {
    Settings(title = R.string.settings),
    Share(title = R.string.share)
}