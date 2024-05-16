package com.example.CBUdemo

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.example.CBUdemo.ui.theme.MyApplicationTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Firebase.database
        val ledReference = database.getReference("LED1")
        val led2Reference = database.getReference("LED2")
        val temperatureReference = database.getReference("Temperature")










        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    cbuDemo(led1Reference = ledReference, led2Reference = led2Reference , temperatureReference = temperatureReference)
                }
            }
        }
    }
}



@Composable
fun cbuDemo(modifier: Modifier = Modifier, led1Reference: DatabaseReference,  led2Reference: DatabaseReference, temperatureReference: DatabaseReference) {


    val temperatureData = remember {
        mutableStateOf(0f)

    }


    // Read from the database
    temperatureReference.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // This method is called once with the initial value and again
            // whenever data at this location is updated.
            temperatureData.value = dataSnapshot.getValue<Float>() ?: 0f
            Log.d("TEMPERATURE", "Value is: ${temperatureData.value}")
        }

        override fun onCancelled(error: DatabaseError) {
            // Failed to read value
            Log.w("TEMPERATURE", "Failed to read value.", error.toException())
        }
    })

    val flashState1  = remember {
        mutableStateOf(R.drawable.baseline_flashlight_off_24)
    }

    val flashState2  = remember {
        mutableStateOf(R.drawable.baseline_flashlight_off_24)
    }



    val initialState1 =  remember {

        mutableStateOf(false)

    }

    val initialState2 =  remember {

        mutableStateOf(false)

    }

    LaunchedEffect(initialState1.value,initialState2.value) {

        led1Reference.setValue(initialState1.value)
        led2Reference.setValue(initialState2.value)

    }



    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.CenterHorizontally) {


        Text(text = "${temperatureData.value} oC", fontSize = 75.sp)


        IconButton(
            onClick = {

                initialState1.value = !initialState1.value


                if(initialState1.value)
                    flashState1.value = R.drawable.flash_on

                else
                    flashState1.value = R.drawable.baseline_flashlight_off_24


            },

            modifier = Modifier
                .border(1.dp, Color.Black, CircleShape)
                .size(200.dp)

        ) {

            Column {
                Icon(
                    painterResource(id = flashState1.value) ,
                    contentDescription = "LED1",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(100.dp)

                )

                Text(
                    text = "LED1",
                    fontSize = 30.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontWeight = FontWeight.Bold
                )

            }


        }




        IconButton(
            onClick = {

                initialState2.value = !initialState2.value


                if(initialState2.value)
                    flashState2.value = R.drawable.flash_on

                else
                    flashState2.value = R.drawable.baseline_flashlight_off_24


            },

            modifier = Modifier
                .border(1.dp, Color.Black, CircleShape)
                .size(200.dp)


        ) {

            Column {
                Icon(
                    painterResource(id = flashState2.value) ,
                    contentDescription = "LED2",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(100.dp)

                )

                Text(
                    text = "LED2",
                    fontSize = 30.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontWeight = FontWeight.Bold
                )

            }


        }







    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {


    }
}