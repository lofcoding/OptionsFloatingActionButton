package com.example.optionsfloatingactionbutton

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.optionsfloatingactionbutton.ui.theme.OptionsFloatingActionButtonTheme
import com.loc.options_fab.InteractiveFloatingActionButton
import com.loc.options_fab.ShaderOverlayScaffold

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OptionsFloatingActionButtonTheme {
                var showOptions by remember {
                    mutableStateOf(false)
                }
                ShaderOverlayScaffold(
                    floatingActionButton = {
                        InteractiveFloatingActionButton(
                            mainIcon = R.drawable.ic_add,
                            mainIconAlternative = R.drawable.ic_tweet,
                            mainText = "Tweet",
                            options = floatingActionButtonOptions,
                            onMainIconClick = {
                                showOptions = !showOptions
                            },
                            showOptions = showOptions,
                            onOptionClick = {

                            },
                            textStyle = TextStyle(color = Color.White)
                        )
                    },
                    enableShaderOverlay = showOptions,
                    bottomBar = {
                        BottomAppBarSample()
                    },
                    topBar = {
                        TopAppBarSample()
                    },
                    onShaderDismissRequest = {
                        showOptions = false
                    }
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Button(onClick = {}){
                            Text(text = "Content")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomAppBarSample() {
    BottomAppBar(modifier = Modifier.fillMaxWidth()) {
        TextButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .weight(1f)
        ) {
            Text(text = "Item")
        }
        TextButton(onClick = { /*TODO*/ }, modifier = Modifier.weight(1f)) {
            Text(text = "Item")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarSample() {
    TopAppBar(
        title = {
            Text(text = "Top App Bar")
        },
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}