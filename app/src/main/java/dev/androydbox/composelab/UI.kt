package dev.androydbox.composelab

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@Composable
fun TextFieldwithScrollBar(){
    var text by remember {
        mutableStateOf("")
    }
Row(modifier = Modifier.fillMaxWidth()) {


        Column(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth(0.9f)
                .background(Color.LightGray)
                .border(width = 2.dp, color = Color.Black)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
            ) {


                BasicTextField(
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp)
                        .onGloballyPositioned {

                        }
                )
                Text(
                    text = "Querry",
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.CenterStart),
                    color = Color.Gray
                )


            }
        }

    Icon(Icons.Rounded.Send,contentDescription = "Send", modifier = Modifier
        .requiredWidth(50.dp)
        .fillMaxWidth(0.1f))
}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomLayout(textStyle: TextStyle= LocalTextStyle.current) {
    val BasicTextFieldWidthRatio = 9 //6 to 9 in whole numbers
    val MinIconSize = 48.dp//change as required
    //text is the text entered in the textfield
    var text by remember {
        mutableStateOf("")
    }


    //used to control the scrollbar
    var scrollState = rememberScrollState()


    var MessageList = remember {
        mutableStateListOf<String>()
    }

    //customlayout
    //Column
    //{
    //BasicTextField
    //}
    //
    val Locdensity = LocalDensity.current
//    AnimatedVisibility(visible = true,
//
//        enter = slideInVertically {
//            // Slide in from 40 dp from the top.
//            with(Locdensity) { -100.dp.roundToPx() }
//        } + expandVertically(
//            // Expand from the top.
//            expandFrom = Alignment.Top
//        ) + fadeIn(
//            // Fade in with the initial alpha of 0.3f.
//            initialAlpha = 0.3f
//        ),
//        exit = slideOutVertically() + shrinkVertically() + fadeOut()) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Green),
            verticalArrangement = Arrangement.Bottom
        ) {


            MessageList.forEach {
                Box(
                    modifier = Modifier

                        .fillMaxWidth()
                        .background(Color.Gray)
                        .animateContentSize()
                        .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(3.dp))
                ) {
                    Text(text = it, modifier = Modifier.padding(4.dp))
                }


                Spacer(modifier = Modifier.size(4.dp))
            }


            //custom layout
            lateinit var TextFieldPlaceable: Placeable
            lateinit var IconPlaceable: Placeable

            var enabled by remember {
                mutableStateOf(false)
            }

            var size by remember {
                mutableStateOf(80)
            }
            var lineCount by remember {
                mutableStateOf(80)
            }

            var layoutHeight by remember {
                mutableStateOf(0)
            }
            val scope = rememberCoroutineScope()
            var textFieldValue by remember {
                mutableStateOf(
                    TextFieldValue(
                        "", selection = TextRange.Zero
                    )

                )
            }
            val alpha: Float by animateFloatAsState(
                if (enabled) 1f else 0f, animationSpec = spring(
                    stiffness = Spring.StiffnessHigh,
                    dampingRatio = 5f
                )
            )
            ModalNavigationDrawer(drawerContent = {
                ModalDrawerSheet {
                        Text("Menu")
                        Divider()

                }
            }) {

            }
            Layout(content = {
                Column(
                    modifier = Modifier
                        .drawWithContent {
                            drawContent()
                            val ColumnHeight = this.size.height
                            var ScrollBarheight = (ColumnHeight / size * 1f) * ColumnHeight
                            Log.d(
                                "Scrollbar Height:",
                                ScrollBarheight.toString()
                            )//size is the textfield height
                            Log.d("Denisty", density.toString())
                            Log.d("Size.height", size.toString())
                            Log.d("this.size", this.size.height.toString())
                            Log.d(
                                "Remaining height",
                                (ColumnHeight * density - ScrollBarheight).toString()
                            )
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
                        .background(Color.Black)
                        .layoutId("Column")
                        .verticalScroll(scrollState)
                )
                {
                    BasicTextField(
                        value = textFieldValue,
                        textStyle = textStyle,
                        //interactionSource = remember{ MutableInteractionSource()},
                        onValueChange = {
                            textFieldValue =
                                TextFieldValue(text = it.text, selection = it.selection)


                        },
                        onTextLayout = {
                            Log.d("TextLayout", "typing")
                            Log.d("Scrolling value", scrollState.value.toString())
                            size = it.size.height
                            if (lineCount != it.lineCount) {
                                lineCount = it.lineCount
                                if (lineCount == 0) {
                                    lineCount = 1
                                }

                            }
                            Log.d(
                                "Scrolling text line",
                                it.getLineForOffset(textFieldValue.selection.end).toString()
                            )
                            scope.launch {
                                var currentPos =
                                    it.getLineForOffset(textFieldValue.selection.end) * 1f / it.lineCount * it.size.height
                                scrollState.scrollTo(currentPos.toInt())

                                Log.d("Height", it.size.height.toString())
                                Log.d("ScrollValue", scrollState.value.toString())
                                Log.d("Current pos", (it.lineCount).toString())
                            }
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray)

                    )
                }

                IconButton(onClick = { /*TODO*/
                    MessageList.add(textFieldValue.text)
                    var TextString:String=textFieldValue.text
                    Log.e("promp as entered",TextString)

                    GlobalScope.launch {

                        MessageList.add(generateText(TextString))

                    }
                    textFieldValue = TextFieldValue(text = "")

                }, modifier = Modifier.layoutId("SendButton")) {
                    Icon(imageVector = Icons.Rounded.Send, "Send")
                }


            },
                measurePolicy = { measurables, constraints ->
                    if (lineCount < 0) {
                        lineCount = 1

                    }


                    if (lineCount > 3) {
                        lineCount = 3
                    }
                    if (lineCount == 1) {
                        layoutHeight = (lineCount * density * 48).toInt()
                    } else {
                        layoutHeight =
                            (1 * density * 48).toInt() + ((lineCount - 1) * density * textStyle.fontSize.value).toInt()
                    }

                    //checking the size of the icon
                    var IconWidth = constraints.maxWidth * (10 - BasicTextFieldWidthRatio) / 10
                    var TextFieldHeight = textStyle.fontSize.value * density
                    Log.d("TextField Height", textStyle.fontSize.value.toString())
                    if (IconWidth < 48 * density) {
                        IconWidth = 48 * density.toInt()
                    }
                    if (textStyle.fontSize.value * density < 48 * density) {
                        TextFieldHeight = 48 * density
                    }
                    if (IconWidth < TextFieldHeight) {
                        TextFieldHeight = IconWidth.toFloat() //this is a mistake
                    }
                    if (IconWidth > TextFieldHeight) {
                        IconWidth = TextFieldHeight.toInt()
                    }

                    val TextFieldConstraints = constraints.copy(
                        minWidth = constraints.maxWidth - IconWidth,
                        maxWidth = constraints.maxWidth - IconWidth,
                        minHeight = TextFieldHeight.toInt() * 1,
                        maxHeight = TextFieldHeight.toInt() * 3
                    )

                    val IconConstraints = constraints.copy(
                        minWidth = IconWidth,
                        maxWidth = IconWidth,
                        minHeight = TextFieldHeight.toInt(),
                        maxHeight = TextFieldHeight.toInt()
                    )
                    measurables.forEach {
                        Log.d("Measurables", it.layoutId.toString())
                        if (it.layoutId == "Column") {
                            TextFieldPlaceable = it.measure(TextFieldConstraints)

                        }
                        if (it.layoutId == "SendButton") {
                            IconPlaceable = it.measure(IconConstraints)
                            Log.d("Icon Placeable", "Icons")
                        }
                    }

                    layout(width = constraints.maxWidth, height = layoutHeight) {

                        TextFieldPlaceable.placeRelative(
                            x = 0,
                            y = layoutHeight - TextFieldPlaceable.height
                        )
                        IconPlaceable.placeRelative(
                            x = TextFieldConstraints.maxWidth,
                            y = layoutHeight - TextFieldPlaceable.height
                        )
                    }
                })
        }
    }


    suspend fun generateText(prompt: String): String {
        val apiKey = "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"
        //val prompt = "Translate the following English text to French: 'Hello, world!'"
    Log.e("Prompt inside generate text", prompt)
        val openAIApiClient = OpenAIAPIClient(apiKey)
        var generatedText =""
        runBlocking {

         //   val job1 = launch {
             generatedText= async {
//                    try {
                       // Log.e("Main", "$prompt")
                        openAIApiClient.generateText(prompt)
//                        withContext(Dispatchers.Main) {
//                            // Handle the generated text on the main thread
//                            Log.e("Generated Text:", generatedText)
//
//                        }
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }

                }.await()
           // }
            //job1.join()
        }

        //delay(4000L)

        return generatedText.toString()
    }
