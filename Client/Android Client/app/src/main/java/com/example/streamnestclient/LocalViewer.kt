package com.example.streamnestclient

import android.os.Bundle
import android.util.Log
import android.content.Intent
import android.net.Uri
import android.content.Context

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.BackHandler

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.Box

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField

import androidx.compose.runtime.*

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch

import java.net.URL
import java.net.InetAddress



data class FileItem(
    val name: String,
    val type: String
)



enum class RequestType
{
    FILES,
    DOWNLOAD
}



fun buildRequestUrl(
    baseUrl: String,
    requestType: RequestType,
    currentPath: String
): String
{
    val action =
        when(requestType)
        {
            RequestType.FILES -> "Files"
            RequestType.DOWNLOAD -> "Download"
        }

    return "$baseUrl/$action$currentPath"
}



suspend fun downloadJsonText(
    requestUrl: String
): String
{
    return withContext(Dispatchers.IO)
    {
        URL(requestUrl).readText()
    }
}



fun parseFileItems(
    jsonText: String
): List<FileItem>
{
    val gson = Gson()

    val listType =
        object : TypeToken<List<FileItem>>() {}.type

    return gson.fromJson(
        jsonText,
        listType
    )
}



suspend fun loadFolderContents(
    baseUrl: String,
    requestType: RequestType,
    currentPath: String
): List<FileItem>
{
    val requestUrl =
        buildRequestUrl(
            baseUrl,
            requestType,
            currentPath
        )

    Log.d(
        "LOCAL_VIEWER",
        "REQUEST URL = $requestUrl"
    )

    val jsonText =
        downloadJsonText(requestUrl)

    return parseFileItems(jsonText)
}



fun openFileUrl(
    activity: ComponentActivity,
    url: String
)
{
    val mimeType =
        when
        {
            url.endsWith(".mp4") -> "video/mp4"
            url.endsWith(".mkv") -> "video/*"
            url.endsWith(".mov") -> "video/*"
            url.endsWith(".avi") -> "video/*"

            url.endsWith(".jpg") -> "image/*"
            url.endsWith(".jpeg") -> "image/*"
            url.endsWith(".png") -> "image/*"
            url.endsWith(".webp") -> "image/*"

            url.endsWith(".txt") -> "text/plain"

            else -> "*/*"
        }

    val intent = Intent(Intent.ACTION_VIEW)

    intent.setDataAndType(
        Uri.parse(url),
        mimeType
    )

    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    try
    {
        activity.startActivity(intent)
    }
    catch(e: Exception)
    {
        Log.d(
            "LOCAL_VIEWER",
            "FAILED TO OPEN FILE"
        )

        Log.d(
            "LOCAL_VIEWER",
            e.toString()
        )
    }
}



fun getParentPath(
    currentPath: String
): String
{
    if(currentPath.isEmpty())
    {
        return ""
    }

    val lastSlashIndex =
        currentPath.lastIndexOf('/')

    if(lastSlashIndex <= 0)
    {
        return ""
    }

    return currentPath.substring(
        0,
        lastSlashIndex
    )
}



fun saveServerConfig(
    activity: ComponentActivity,
    ipAddress: String,
    port: String
)
{
    val sharedPreferences =
        activity.getSharedPreferences(
            "LOCAL_VIEWER_PREFS",
            Context.MODE_PRIVATE
        )

    val editor =
        sharedPreferences.edit()

    editor.putString(
        "IP_ADDRESS",
        ipAddress
    )

    editor.putString(
        "PORT",
        port
    )

    editor.apply()
}



fun loadServerConfig(
    activity: ComponentActivity
): Pair<String?, String?>
{
    val sharedPreferences =
        activity.getSharedPreferences(
            "LOCAL_VIEWER_PREFS",
            Context.MODE_PRIVATE
        )

    val ipAddress =
        sharedPreferences.getString(
            "IP_ADDRESS",
            null
        )

    val port =
        sharedPreferences.getString(
            "PORT",
            null
        )

    return Pair(
        ipAddress,
        port
    )
}



fun isValidIpAddress(
    ipAddress: String
): Boolean
{
    return try
    {
        InetAddress.getByName(ipAddress)

        true
    }
    catch(e: Exception)
    {
        false
    }
}



fun isValidPort(
    port: String
): Boolean
{
    val portNumber =
        port.toIntOrNull()

    return portNumber != null
            &&
            portNumber in 1..65535
}



suspend fun tryConnectToServer(
    ipAddress: String,
    port: String,

    onConnectionSuccess: suspend (String) -> Unit,

    onConnectionFailure: (String) -> Unit
)
{
    if(
        !isValidIpAddress(ipAddress)
        ||
        !isValidPort(port)
    )
    {
        onConnectionFailure(
            "Invalid IP address or port format."
        )

        return
    }

    try
    {
        val tempBaseUrl =
            "http://$ipAddress:$port"

        loadFolderContents(
            tempBaseUrl,
            RequestType.FILES,
            ""
        )

        onConnectionSuccess(tempBaseUrl)
    }
    catch(e: Exception)
    {
        Log.d(
            "LOCAL_VIEWER",
            "SERVER CONNECTION FAILED"
        )

        Log.d(
            "LOCAL_VIEWER",
            e.toString()
        )

        onConnectionFailure(
            "Unable to connect to server. Check the IP address, port, and current network connection."
        )
    }
}

suspend fun onSubmitClicked(
    activity: ComponentActivity,

    ipAddressInput: String,
    portInput: String,

    onSuccess: (String) -> Unit,
    onFailure: (String) -> Unit,

    onFoldersLoaded: (List<FileItem>) -> Unit
){
    tryConnectToServer(
        ipAddressInput,
        portInput,

        onConnectionSuccess =
            {
                saveServerConfig(
                    activity,
                    ipAddressInput,
                    portInput
                )

                val initialFileItems =
                    loadFolderContents(
                        it,
                        RequestType.FILES,
                        ""
                    )

                onFoldersLoaded(initialFileItems)

                onSuccess(it)
            },

        onConnectionFailure =
            {
                onFailure(it)
            }
    )
}

fun clearServerConfig(
    activity: ComponentActivity
)
{
    val sharedPreferences =
        activity.getSharedPreferences(
            "LOCAL_VIEWER_PREFS",
            Context.MODE_PRIVATE
        )

    sharedPreferences
        .edit()
        .clear()
        .apply()
}

@Composable
fun FileListUI(
    fileItems: List<FileItem>,
    currentPath: String,

    onItemClick: (FileItem) -> Unit,

    onBackClick: () -> Unit,

    onDisconnectClick: () -> Unit
)
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    )
    {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        {
            if(currentPath.isNotEmpty())
            {
                Button(
                    onClick =
                        {
                            val parentPath =
                                getParentPath(currentPath)

                            Log.d(
                                "LOCAL_VIEWER",
                                "BACK BUTTON PRESSED"
                            )

                            Log.d(
                                "LOCAL_VIEWER",
                                "NEW PATH = $parentPath"
                            )

                            onBackClick()
                        }
                )
                {
                    Text("Back")
                }
            }

            Button(
                onClick =
                    {
                        onDisconnectClick()
                    },

                modifier = Modifier
                    .align(Alignment.CenterEnd)
            )
            {
                Text("Disconnect")
            }
        }

        fileItems.forEach {
            Button(
                onClick =
                    {
                        onItemClick(it)
                    },

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            {
                Text(
                    text = it.name + " - " + it.type
                )
            }
        }
    }
}

@Composable
fun ServerConfigurationUI(
    ipAddressInput: String,
    portInput: String,
    serverErrorMessage: String,

    onIpAddressChange: (String) -> Unit,
    onPortChange: (String) -> Unit,
    onSubmitClick: () -> Unit
)
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    )
    {
        Text(
            text = "IP Address"
        )

        OutlinedTextField(
            value = ipAddressInput,

            onValueChange =
                {
                    onIpAddressChange(it)
                },

            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Text(
            text = "Port"
        )

        OutlinedTextField(
            value = portInput,

            onValueChange =
                {
                    onPortChange(it)
                },

            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick =
                {
                    onSubmitClick()
                }
        )
        {
            Text("Submit")
        }

        if(serverErrorMessage.isNotEmpty())
        {
            Text(
                text = serverErrorMessage,

                color = Color.Red,

                modifier = Modifier
                    .padding(top = 16.dp)
            )
        }
    }
}



class LocalViewer : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setContent()
        {
            var fileItems by remember {
                mutableStateOf<List<FileItem>>(emptyList())
            }

            var currentPath by remember {
                mutableStateOf("")
            }

            var isServerConfigured by remember {
                mutableStateOf(false)
            }

            var ipAddressInput by remember {
                mutableStateOf("")
            }

            var portInput by remember  {
                mutableStateOf("")
            }

            var baseUrl by remember {
                mutableStateOf("")
            }

            var serverErrorMessage by remember {
                mutableStateOf("")
            }

            var hasFinishedStartup by remember {
                mutableStateOf(false)
            }

            val coroutineScope =
                rememberCoroutineScope()



            LaunchedEffect(Unit)
            {
                val serverConfig =
                    loadServerConfig(this@LocalViewer)

                val ipAddress = serverConfig.first
                val port = serverConfig.second

                if(
                    ipAddress != null
                    &&
                    port != null
                )
                {
                    ipAddressInput = ipAddress
                    portInput = port

                    tryConnectToServer(
                        ipAddress,
                        port,

                        onConnectionSuccess =
                            {
                                baseUrl = it

                                serverErrorMessage = ""

                                currentPath = ""

                                fileItems =
                                    loadFolderContents(
                                        it,
                                        RequestType.FILES,
                                        ""
                                    )

                                isServerConfigured = true

                                hasFinishedStartup = true
                            },

                        onConnectionFailure =
                            {
                                serverErrorMessage = it

                                isServerConfigured = false
                            }
                    )
                }
            }



            LaunchedEffect(currentPath)
            {
                if(
                    isServerConfigured
                    &&
                    hasFinishedStartup
                )
                {
                    try
                    {
                        fileItems =
                            loadFolderContents(
                                baseUrl,
                                RequestType.FILES,
                                currentPath
                            )

                        serverErrorMessage = ""
                    }
                    catch(e: Exception)
                    {
                        Log.d(
                            "LOCAL_VIEWER",
                            "SERVER CONNECTION FAILED"
                        )

                        Log.d(
                            "LOCAL_VIEWER",
                            e.toString()
                        )

                        fileItems = emptyList()

                        isServerConfigured = false

                        serverErrorMessage =
                            "Unable to connect to server. Check the IP address, port, and current network connection."
                    }
                }
            }



            BackHandler(
                enabled =
                    currentPath.isNotEmpty()
                            &&
                            isServerConfigured
            )
            {
                currentPath =
                    getParentPath(currentPath)
            }



            if(isServerConfigured)
            {
                FileListUI(
                    fileItems = fileItems,

                    currentPath = currentPath,

                    onItemClick =
                        {
                            when(it.type)
                            {
                                "Folder" ->
                                {
                                    currentPath += "/${it.name}"
                                }

                                "File" ->
                                {
                                    val downloadUrl =
                                        buildRequestUrl(
                                            baseUrl,
                                            RequestType.DOWNLOAD,
                                            currentPath + "/${it.name}"
                                        )

                                    Log.d(
                                        "LOCAL_VIEWER",
                                        "Opening URL = $downloadUrl"
                                    )

                                    openFileUrl(
                                        this@LocalViewer,
                                        downloadUrl
                                    )
                                }

                                else ->
                                {
                                    Log.d(
                                        "LOCAL_VIEWER",
                                        "Invalid File Type!"
                                    )
                                }
                            }
                        },

                    onBackClick =
                        {
                            currentPath =
                                getParentPath(currentPath)
                        },

                    onDisconnectClick =
                        {
                            clearServerConfig(this@LocalViewer)

                            fileItems = emptyList()

                            currentPath = ""

                            baseUrl = ""

                            ipAddressInput = ""

                            portInput = ""

                            serverErrorMessage = ""

                            isServerConfigured = false

                            hasFinishedStartup = false
                        }
                )

            }
            else
            {
                ServerConfigurationUI(
                    ipAddressInput = ipAddressInput,

                    portInput = portInput,

                    serverErrorMessage = serverErrorMessage,

                    onIpAddressChange =
                        {
                            ipAddressInput = it
                        },

                    onPortChange =
                        {
                            portInput = it
                        },

                    onSubmitClick =
                        {
                            coroutineScope.launch {

                                onSubmitClicked(
                                    this@LocalViewer,

                                    ipAddressInput,
                                    portInput,

                                    onSuccess =
                                        {
                                            baseUrl = it

                                            serverErrorMessage = ""

                                            currentPath = ""

                                            isServerConfigured = true
                                        },

                                    onFailure =
                                        {
                                            serverErrorMessage = it

                                            isServerConfigured = false
                                        },

                                    onFoldersLoaded =
                                        {
                                            fileItems = it
                                        }
                                )
                            }
                        }
                )
            }
        }
    }
}