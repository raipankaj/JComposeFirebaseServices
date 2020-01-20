package com.sample.firebasecompose

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.ui.core.Text
import androidx.ui.core.dp
import androidx.ui.core.px
import androidx.ui.core.setContent
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.shape.border.Border
import androidx.ui.foundation.shape.corner.CutCornerShape
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.graphics.Shadow
import androidx.ui.layout.*
import androidx.ui.material.MaterialTheme
import androidx.ui.material.surface.Card
import androidx.ui.material.surface.Surface
import androidx.ui.text.TextStyle
import com.google.firebase.firestore.DocumentSnapshot
import com.sample.firebasecompose.viewmodel.InfoViewModel

class MainActivity : AppCompatActivity() {

    private val mInfoViewModel by lazy {
        ViewModelProvider(this@MainActivity).get(InfoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(color = Color.White) {
                    FirebaseServices(liveData = mInfoViewModel.retrieveDataFromFirestore())
                }
            }
        }
    }
}

@Composable
fun FirebaseServices(liveData: LiveData<List<DocumentSnapshot>>) {
    val listOfDoc = +observe(liveData)
    var heading = ""
    Column {
        Surface(
            color = Color.DarkGray, modifier = Height(48.dp),
            shape = RoundedCornerShape(bottomLeft = 24.dp, bottomRight = 24.dp)
        ) {
            Center {
                Text(
                    "Firebase Services",
                    style = TextStyle(color = Color.White)
                )
            }
        }

        VerticalScroller(modifier = Spacing(12.dp)) {
            Column {
                listOfDoc?.forEach {
                    val type = it.data?.get("type").toString()
                    ListItem(it, heading)
                    heading = type
                }
            }
        }
    }
}

@Composable
fun ListItem(documentSnapshot: DocumentSnapshot, heading: String) {

    Column {
        if (heading != documentSnapshot.data?.get("type")?.toString()) {
            Padding(padding = EdgeInsets(top = 12.dp)) {
                Text(
                    text = documentSnapshot.data?.get("type")?.toString()?.capitalize() ?: "",
                    style = TextStyle(
                        shadow = Shadow(Color.Gray, blurRadius = 12.px),
                        color = Color.DarkGray
                    )
                )
            }
        }

        Card(
            modifier = Height(48.dp) wraps Expanded wraps Spacing(4.dp),
            shape = CutCornerShape(16.dp),
            border = Border(Color.Yellow, 1.dp)
        ) {
            Center {
                Text(text = documentSnapshot.data?.get("name")?.toString() ?: "")
            }
        }

    }
}

// general purpose observe effect. this will likely be provided by LiveData. effect API for
// compose will also simplify soon.
fun <T> observe(data: LiveData<T>) = effectOf<T?> {
    val result = +state<T?> { data.value }
    val observer = +memo { Observer<T> { result.value = it } }

    +onCommit(data) {
        data.observeForever(observer)
        onDispose { data.removeObserver(observer) }
    }

    result.value
}
