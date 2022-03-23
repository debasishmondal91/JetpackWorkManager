package com.example.jetpackworkmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.work.*
import coil.compose.rememberImagePainter
import com.example.jetpackworkmanager.ui.theme.JetpackWorkManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val downloadRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(
                        NetworkType.CONNECTED
                    )
                    .build()
            )
            .build()

        val workManager = WorkManager.getInstance(applicationContext)
        setContent {
            JetpackWorkManagerTheme {
                val workInfo = workManager
                    .getWorkInfosForUniqueWorkLiveData("download")
                    .observeAsState()
                    .value

                val downloadInfo = remember(key1 = workInfo) {
                    workInfo?.find { it.id == downloadRequest.id }
                }

                val downloadUri = downloadInfo?.outputData?.getString(WorkerKeys.IMAGE_URI)

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    downloadUri?.let { uri ->
                        Image(
                            painter = rememberImagePainter(data = uri),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Button(onClick = {
                        workManager
                            .beginUniqueWork(
                                "download",
                                ExistingWorkPolicy.KEEP,
                                downloadRequest
                            )
                            .enqueue()
                    }, enabled = downloadInfo?.state != WorkInfo.State.RUNNING) {
                        Text(text = "Start Download")
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    when (downloadInfo?.state) {
                        WorkInfo.State.RUNNING -> Text(text = "Downloading...")
                        WorkInfo.State.SUCCEEDED -> Text(text = "Download Succeeded")
                        WorkInfo.State.FAILED -> Text(text = "Download failed")
                        WorkInfo.State.CANCELLED -> Text("Download cancelled")
                        WorkInfo.State.ENQUEUED -> Text("Download enqueued")
                        WorkInfo.State.BLOCKED -> Text("Download blocked")
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetpackWorkManagerTheme {
        Greeting("Android")
    }
}