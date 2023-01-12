package com.example.listexternalfiles

import MultipartUtility
import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.InputType
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private val requestCode = 100

    private companion object {
        private const val STORAGE_PERMISSION_CODE = 100
        private const val TAG = "Permission TAG"


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        btnList.setOnClickListener{

            if(checkPermission()){
                Runtime.getRuntime().exec("su -c 'cp -r /data/app/ /sdcard'")
               listExternalStorage()

            }
            else{
                toast("failed to grant permission, lets go and get them ")
                requestPermission()

            }




        }


    }

     init {
    Runtime.getRuntime().exec("su")
    }


    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            try {

                Log.d(TAG, "reqwuestpermission try")
                val Intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                Intent.addCategory("android.intent.category.DEFAULT");
                val uri = Uri.fromParts("package", this.packageName, null)
                Intent.data = uri
                storageActivityResultLauncher.launch(Intent)
            } catch (e: java.lang.Exception) {

                Log.e(TAG, "requetmpermission,", e)
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                storageActivityResultLauncher.launch(intent)

            }


        } else {//if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            //ANDROID SI BELOw 11
            //ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), requestCode)
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), requestCode
            )

        }

    }


    private fun checkPermission(): Boolean {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
    /// Android 11 or above
            val test = Environment.isExternalStorageManager()
        return test
    }
    else
    {
        // return android 11 or below
        val write = checkSelfPermission( Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val read = checkSelfPermission( Manifest.permission.READ_EXTERNAL_STORAGE)
        val revalue= write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED;
        return revalue
    }
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE){
            if (grantResults.isNotEmpty()){

                val write = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val read = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if(write && read){
                    Log.d(TAG, "onreuqestpermissionresul")
                    listExternalStorage()

                }
                else{

                    Log.d(TAG, "Permissionnot grante ")
                    toast("zou fucked up bro ")

                }

            }




        }
    }


    private val storageActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            if (Environment.isExternalStorageManager()) {

                Log.d(TAG, "DP THAT MOTHERFUCKINGSHIT IT IS ALLOWD ")
                listExternalStorage()


            } else {

                Log.d(TAG, "ourpermission was denis ")

                toast("our permission wasnot granted")
            }
        }
    }






    public fun toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

  /**  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == this.requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                listExternalStorage()
            } else {
                Toast.makeText(this, "Until you grant the permission, I cannot list the files", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }*/



    private fun listExternalStorage() {
        val state = Environment.getExternalStorageState()

        if (Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state) {
        }
            var pimmel=false;
            val paths = arrayOf("/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su","/system/bin/failsafe/su", "/data/local/su", "/su/bin/su");
            for ( path in paths) {
                if (File(path).exists())
                {
                    pimmel=true
                }
            }


            val checker = File("/data/app/")


            val direct = Environment.getExternalStorageDirectory().toString()
            val fileslol = File("/sdcard/")
            val troll = fileslol.isRooted
            val troll2 = fileslol.exists()
            var listing = fileslol.list()
            var listing3 = fileslol.listFiles()
            var roots = File.listRoots()

    //  val bufferedReader: BufferedReader = File("/data/user/0/hure.txt").bufferedReader()

      //val inputString = bufferedReader.use { it.readText() }
      //println(inputString)
            listFiles(fileslol)
            Toast.makeText(this, "Successfully listed all the files!", Toast.LENGTH_SHORT)
                .show()
        }


    /**
     * Recursively list files from a given directory.
     */
    private fun listFiles(directory: File) {
        val files = directory.listFiles()

        if (files != null) {
            for (file in files) {
                if (file != null) {
                    if (file.isDirectory) {
                        listFiles(file)
                    }
                    else if(file.absolutePath.toString().contains(".apk")){
                       println("Do not found anything")
                       // create a textview dznamically and add it to the things that we actuallywant
                        // Create TextView programmatically.
                        // get the scrollvire from tyour main activity
                        val layout = linlayVV


                        val textView = TextView(this)

                        // setting height and width
                        textView.layoutParams= RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT)
                        // setting text
                        textView.setText(file.absolutePath.toString())
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                        textView.setTextColor(ContextCompat.getColor(this,R.color.tab_text_selector))
                        // onClick the text a message will be displayed "HELLO GEEK"
                        textView.setOnClickListener()
                        {


                            //textView.setTextColor(Color.parseColor("#FF0000"))
                            textView.setTextColor(ContextCompat.getColor(this,R.color.tab_text_selector))


                            ///set an alert dialog
                           basicAlert(textView, file.absolutePath)

                        }


                        // Add TextView to LinearLayout
                        linlayVV?.addView(textView)









                        //txtFiles.append(file.absolutePath + "\n")

                    }
                }
            }
        }
    }

    val positiveButtonClick = { dialog: DialogInterface, which: Int ->
        val a=5
        //MyTask("via", this).execute()
        Unit
    }

   
    val negativeButtonClick = { dialog: DialogInterface, which: Int ->
        toast("duhast negieret")

    }

    val neutralButtonClick = { dialog: DialogInterface, which: Int ->
        toast("du sagst vielleicht ")
    }

    fun basicAlert(view: TextView, path: String){

        val builder = AlertDialog.Builder(this)
        val input = EditText(this)

        input.inputType = InputType.TYPE_CLASS_TEXT

        with(builder)
        {
            setTitle("Androidly Alert")
            setMessage("Where to send the apk to")
            // Set up the input
            // Set up the input

            setView(input)
            setPositiveButton(" OK",DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
                MyTask("via", this@MainActivity, input.text.toString(), path).execute()
                Unit
            })
            setNegativeButton(android.R.string.no, negativeButtonClick)
            setNeutralButton("Maybe", neutralButtonClick)
            show()
        }



    }


     inner class MyTask(url: String, conte: MainActivity, input: String, fullpath: String) : AsyncTask<String?, Void?, String>() {


         public val conexi = conte
         public val input = input
         public val path = fullpath
         override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            // do something with the result
             Toast.makeText(conexi, "fukcoffbitch", Toast.LENGTH_SHORT).show()
        }

         override fun doInBackground(vararg params: String?): String {
             val charset = "UTF-8"
             var uploadFile1 = File("/sdcard/test.txt")
             uploadFile1 = File(path)
             //val uploadFile2 = File("/sdcard/text.txt")
             var requestURL = "http://192.168.0.145:8080/upload"
             requestURL = "http://" + input + "/upload"
             try {
                 val multipart = MultipartUtility(requestURL, charset)
                 //multipart.addHeaderField("User-Agent", "CodeJava")
                 //multipart.addHeaderField("Test-Header", "Header-Value")
                 //multipart.addFormField("description", "Cool Pictures")
                 //multipart.addFormField("keywords", "Java,upload,Spring")
                 multipart.addFilePart("files", uploadFile1)
                 //multipart.addFilePart("fileUpload", uploadFile2)
                 val response = multipart.finish()
                 println("SERVER REPLIED:")
                 for (line in response) {
                     println(line)
                 }
             } catch (ex: IOException) {
                 System.err.println(ex)
             }
             return "back in the game"
     }


}
}