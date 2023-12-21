package dev.androydbox.composelab

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*//import for remember, does not work automatically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.androydbox.composelab.ui.theme.ComposeLabTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeLabTheme {
                // A surface container using the 'background' color from the theme

                    //textField()
                //    Greeting(name = "Android")
                //TextFieldwithScrollBar()

                    //text is the text entered in the textfield

                        CustomLayout()





            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(
    name: String, modifier: Modifier = Modifier
        .background(color = Color.Green)
        .wrapContentHeight()
    // .graphicsLayer(rotationZ = 45.0f)

) {
    val scope= rememberCoroutineScope()
    var maxLines=3;
    var size by remember {
        mutableStateOf(80)
    }
    var textFieldValue by remember{
        mutableStateOf(
            TextFieldValue(""
                , selection = TextRange.Zero)

        )
    }
    var TextFieldSizeheight:Int=0
    var maxLineTextFieldSizeheight:Int=0
    var scrollState:ScrollState= rememberScrollState()
    var text by remember {
        mutableStateOf("")
    }
    var offset by remember {
        mutableStateOf(0f)
    }
    var enabled by remember {
        mutableStateOf(false)
    }
    val alpha:Float by animateFloatAsState(if (enabled) 1f else 0f, animationSpec = spring(stiffness = Spring.StiffnessHigh,
        dampingRatio = 5f))



    Column(
        modifier = Modifier
            .background(color = Color.LightGray)

            .drawWithContent {
                drawContent()
                val ColumnHeight = this.size.height
                var ScrollBarheight = (ColumnHeight / size * 1f) * ColumnHeight
                Log.d("Scrollbar Height:", ScrollBarheight.toString())//size is the textfield height
                Log.d("Denisty", density.toString())
                Log.d("Size.height", size.toString())
                Log.d("this.size", this.size.height.toString())
                Log.d("Remaining height", (ColumnHeight * density - ScrollBarheight).toString())
                if (scrollState.isScrollInProgress) {
                    enabled = true
                } else {
                    enabled = false
                }
                drawRect(
                    color = Color(red = 1f, green = 0f, blue = 0f, alpha = alpha),

                    topLeft = Offset(
                        x = this.size.width - 10f,
                        y = scrollState.value * 1f * ColumnHeight / size
                    ),

                    size = Size(width = 20f, height = ScrollBarheight)
                )
            }
            .fillMaxWidth()
            .height(80.dp)
            .padding(0.dp)

    ) {
        // Your scrollable content goes here
        Log.d("Scrolling",scrollState.value.toString())
        BasicTextField(
            modifier= Modifier
                .fillMaxHeight()

                .verticalScroll(scrollState)
                .background(color = Color.DarkGray)
//                        .onGloballyPositioned {
//                            Log.d("Cursor position", textFieldValue.selection.toString())
//                        }
            ,

            value =textFieldValue, onValueChange = {
                textFieldValue= TextFieldValue(text=it.text, selection = it.selection)

            },
            onTextLayout = {
                Log.d("TextLayout","typing")
                Log.d("Scrolling value",scrollState.value.toString())
                size=it.size.height

                Log.d("Scrolling text line",it.getLineForOffset(textFieldValue.selection.end).toString())
                scope.launch {
                    var currentPos=it.getLineForOffset(textFieldValue.selection.end)*1f/it.lineCount*it.size.height
                    scrollState.scrollTo(currentPos.toInt())
                    Log.d("Height",it.size.height.toString())
                    Log.d("ScrollValue",scrollState.value.toString())
                    Log.d("Current pos",(it.lineCount).toString())
                }
            },

            )
//                repeat(500) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(5.dp)
//                            .background(MaterialTheme.colorScheme.primary)
//                    )
//                    Spacer(modifier = Modifier.height(2.dp))
//
//                }

    }



}

/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun textField() {
    var OffsetY by remember{
        mutableStateOf(0.0f)
    }
    var text by remember {
        mutableStateOf("")
    }

    val maxLines=3//this is the max number of lines in the textfield
    var TextFieldSize=0//this is the size of the textfield used to keep track of the size of the textfielf
    var TextFieldMaxLineSize=0//this is the size of the textfield used to store the size of the textfield when number
    //of lines =maxline

    val Liststate = rememberLazyListState()
    Row(modifier = Modifier
        .drawWithContent {
            drawContent()
            if (TextFieldSize > 210) {
                Log.d("drawContent MaxLine Height", TextFieldMaxLineSize.toString())
                Log.d("drawContent Height", TextFieldSize.toString())

                drawRect(
                    color = Color.Red,
                    topLeft = Offset(x = this.size.width - 20f, y = OffsetY),

                    size = Size(
                        width = 40f,
                        height = 210f / TextFieldSize * 210f*density
                    )
                )
            }
        }
        .padding(3.dp)
        .border(
            width = 1.dp,
            color = Color.Black,
            shape = RoundedCornerShape(8.dp)
        )) {

        BasicTextField(value = text, onValueChange = {
            text=it
        },
            modifier = Modifier.scrollable(orientation = Orientation.Vertical,
                state = rememberScrollableState{ //find out what is rememberScrollableState
                    delta->
                    OffsetY+=delta*135f/TextFieldSize
                    OffsetY*=135f/TextFieldSize
                    if(OffsetY>135){
                        OffsetY=135f
                    }
                    else if(OffsetY<0f){
                        OffsetY=0f
                    }

                    delta
            }),
            onTextLayout = {
                if(it.lineCount==maxLines) { //find out how to do this better
                    TextFieldMaxLineSize=it.size.height
                }
                TextFieldSize=it.size.height
                Log.d("textlayout",TextFieldSize.toString())
                Log.d("textlayout max line",TextFieldMaxLineSize.toString())
                Log.d("textlayout line count",it.lineCount.toString())
            },
            maxLines = maxLines
            )

       /* TextField(
            
            value = text,

            onValueChange = {
                text = it
                 linecount=it.count { it == '\n' } + 1
                Log.d("MainActivity", linecount.toString())

            },


            modifier = Modifier
                .fillMaxWidth()
                .drawWithContent {
                    drawContent()
                    drawRect(
                        color = Color.Red,
                        size = Size(width = 10f, height = 80.0f),
                        topLeft = Offset(x = this.size.width - 10.0f, y = OffsetY)
                    )
                    Log.d("MainActivity", Liststate.layoutInfo.totalItemsCount.toString())
                }
                .draggable(orientation = Orientation.Vertical,
                    state =
                    rememberDraggableState { delta -> OffsetY += delta }
                ),
            shape = RoundedCornerShape(8.dp),
            colors=TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                containerColor = Color.Transparent

            ) //removing underline

        )*/


    }
}*/

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeLabTheme {

    }
}