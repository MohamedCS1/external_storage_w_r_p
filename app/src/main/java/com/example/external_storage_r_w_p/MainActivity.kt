package com.example.external_storage_r_w_p

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import java.io.*
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    private var bu_insert: Button? = null
    private var bu_get: Button? = null
    private var tv_alltext: TextView? = null
    private var et_email: EditText? = null
    private var et_password: EditText? = null
    private val FILENAME = "Anyname"

    private val PERMISSION_REQUESTCODE = 12

    private var permissionaccepted:Boolean = false

    private var creat_file:File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bu_insert = findViewById(R.id.bu_insert)
        bu_get = findViewById(R.id.bu_get)

        et_password = findViewById(R.id.et_password)
        et_email = findViewById(R.id.et_email)
        tv_alltext = findViewById(R.id.tv_file_text)


        bu_insert!!.setOnClickListener {
            checkpermission()
            if (permissionaccepted) {
                try {
                    creat_file =
                        File(this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), FILENAME)
                    creat_file!!.createNewFile()
                    Log.d("path", creat_file!!.path)
                    val file = creat_file!!.outputStream()
                    val writer = PrintWriter(file)
                    writer.println("${et_email!!.text} ,${et_password!!.text}")
                    writer.close()
                    file.close()
                } catch (ex: Exception) {
                    Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }

        bu_get!!.setOnClickListener {
            checkpermission()
            if (permissionaccepted) {
                try {
                    val fileinputstream = FileInputStream(creat_file!!)
                    val streamreader = DataInputStream(fileinputstream)
                    val bfr = BufferedReader(InputStreamReader(streamreader))
                    var alltext = ""
                    var line = bfr.readLine()
                    while (line != null) {
                        Log.d("bfr", bfr.readText())
                        alltext += line
                        tv_alltext!!.text = alltext
                        line = bfr.readLine()

                    }
                    bfr.close()
                    streamreader.close()
                    fileinputstream.close()
                } catch (ex: Exception) {
                    Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUESTCODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionaccepted = true
                return
            } else if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun checkpermission() {
        if (ContextCompat.checkSelfPermission(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE).toString()) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUESTCODE)
        }
    }

}