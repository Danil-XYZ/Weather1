package com.example.weather1.ui.mainScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather1.R

@Composable
@Preview
fun MainViewScreen() {
    Scaffold() {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.img),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        modifier = Modifier
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(100)
                            )
                            .clip(RoundedCornerShape(100))
                            .clickable { },
                        painter = painterResource(id = R.drawable.ic_baseline_add_24),
                        contentDescription = null,
                        tint = Color.White
                    )

                    Text(
                        text = "Mocow",
                        fontSize = 24.sp,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                    )

                    Icon(
                        modifier = Modifier.clickable { },
                        painter = painterResource(id = R.drawable.ic_baseline_more_vert_24),
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(96.dp))

//                Text(
//                    text = "20",
//                    modifier = Modifier.fillMaxWidth(),
//                    textAlign = TextAlign.Center,
//                    fontSize = 96.sp
//                )
//
////                Spacer(modifier = Modifier.height(16.dp))
//
//                Text(
//                    text = "Clear 29/16",
//                    modifier = Modifier.fillMaxWidth(),
//                    textAlign = TextAlign.Center,
//                    fontSize = 24.sp
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Box(
//                    modifier = Modifier.fillMaxWidth(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Button(
//                        onClick = { /*TODO*/ },
//                        modifier = Modifier
//                            .clip(CircleShape)
//                            .background(color = Color.Transparent, shape = CircleShape)
//                    ) {
//                        Text(
//                            text = "AQL 27",
//                            textAlign = TextAlign.Center,
//                            fontSize = 16.sp
//                        )
//                    }
//                }

            }
        }
    }
}

@Composable
fun TopBarRow(){
    Row() {

        Icon(
            modifier = Modifier
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(100)
                )
                .clip(RoundedCornerShape(100))
                .clickable { },
            painter = painterResource(id = R.drawable.ic_baseline_add_24),
            contentDescription = null,
            tint = Color.White
        )

        Text(
            text = "Mocow",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )

        Icon(
            modifier = Modifier.clickable {  },
            painter = painterResource(id = R.drawable.ic_baseline_more_vert_24),
            contentDescription = null,
            tint = Color.White
        )

    }
}