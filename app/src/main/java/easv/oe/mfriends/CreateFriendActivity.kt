package easv.oe.mfriends

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.telephony.SmsManager
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.easv.oe.friends.Model.BEFriend
import com.easv.oe.friends.Model.Friends
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast

class CreateFriendActivity: AppCompatActivity() {

    val PHONE_NO = "12345678"
    val TAG = "xyz"
    var position = -1
    private val PERMISSION_REQUEST_CODE = 1
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE = 101
    var mFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        checkPermissions()

        val position = intent.getIntExtra("EXTRA_FRIEND", -1)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (position > -1){
            this.position = position
            val friend = Friends().getAll()[position]
            supportActionBar!!.title = "Edit friend"
            val nameInput = findViewById<EditText>(R.id.etName)
            val phoneInput = findViewById<EditText>(R.id.etPhone)
            val favoriteCheck = findViewById<CheckBox>(R.id.cbFavorite)
            nameInput.setText(friend.name)
            phoneInput.setText(friend.phone)
            favoriteCheck.isChecked = friend.isFavorite
        }else {
            supportActionBar!!.title = "Create new friend"
        }
    }

    override fun onBackPressed() {
        val intent = Intent()
        setResult(RESULT_CANCELED, intent)
        finish()
    }

    /**
     * SMS handling
     */
    fun onMessage(view: View) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("SMS Handling")
        alertDialogBuilder
            .setMessage("Click Direct if SMS should be send directly. Click Start to start SMS app...")
            .setCancelable(true)
            .setPositiveButton("Direct") { dialog, id -> sendSMSDirectly() }
            .setNegativeButton("Start", { dialog, id -> startSMSActivity() })
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun startSMSActivity() {
        val sendIntent = Intent(Intent.ACTION_VIEW)
        sendIntent.data = Uri.parse("sms:$PHONE_NO")
        sendIntent.putExtra("sms_body", "Hi, it goes well on the android course...")
        startActivity(sendIntent)
    }

    private fun sendSMSDirectly() {
        Toast.makeText(this, "An sms will be send", Toast.LENGTH_LONG)
            .show()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_DENIED) {
                Log.d(TAG, "permission denied to SEND_SMS - requesting it")
                val permissions = arrayOf(Manifest.permission.SEND_SMS)
                requestPermissions(permissions, PERMISSION_REQUEST_CODE)
                return
            } else Log.d(TAG, "permission to SEND_SMS granted!")
        } else Log.d(TAG, "Runtime permission not needed")
        sendMessage()
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "Permission: " + permissions[0] + " - grantResult: " + grantResults[0])
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendMessage()
        }
    }

    private fun sendMessage() {
        val m = SmsManager.getDefault()
        val text = "Hi, it goes well on the android course..."
        m.sendTextMessage(PHONE_NO, null, text, null, null)
    }

    /**
     * CALL
     */
    fun onCall(view: View) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$PHONE_NO")
        startActivity(intent)
    }

    /**
     * EMAIL
     */
    fun onEmail(view: View) {
        Log.d(TAG, "onClickEMAIL started")
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "plain/text"
        val receivers = arrayOf("oe@cs.au.dk")
        emailIntent.putExtra(Intent.EXTRA_EMAIL, receivers)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Test")
        emailIntent.putExtra(Intent.EXTRA_TEXT,
            "Hej, Hope that it is ok, Best Regards android...;-)")
        Log.d(TAG, "onClickEMAIL: intent preprared")

        startActivity(emailIntent)
        Log.d(TAG, "Email activity started")

    }

    /**
     * BROWSER
     */
    fun onWeb(view: View) {
        val url = "http://www.dr.dk"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    fun onDelete(view: View){
        Friends.mFriends.removeAt(position)
        val s = Friends.mFriends.size
        Log.d("xy", "$s")
        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }

    fun onSave(view: View){
        val nameInput = findViewById<EditText>(R.id.etName)
        val phoneInput = findViewById<EditText>(R.id.etPhone)
        val favoriteCheck = findViewById<CheckBox>(R.id.cbFavorite)
        if (position > -1){
            Friends().getAll()[position].name  = nameInput.text.toString()
            Friends().getAll()[position].phone = phoneInput.text.toString()
            Friends().getAll()[position].isFavorite = favoriteCheck.isChecked
        }
        else {
            val friend = BEFriend(nameInput.text.toString(), phoneInput.text.toString(), favoriteCheck.isChecked, "Random Address")
            Friends.mFriends.plus(friend)
        }

        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }

    fun onCamera(view: View) {
        mFile = getOutputMediaFile("Camera01") // create a file to save the image

        if (mFile == null) {
            Toast.makeText(this, "Could not create file...", Toast.LENGTH_LONG).show()
            return
        }

        // create Intent to take a picture
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // Add extra to inform the app where to put the image.
        val applicationId = "easv.oe.mfriends"
        intent.putExtra(
            MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
            this,
            "${applicationId}.provider",  //use your app signature + ".provider"
            mFile!!))

        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE)
    }

    //Checks if the app has the required permissions, and prompts the user with the ones missing.
    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        val permissions = mutableListOf<String>()
        if ( ! isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) ) permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if ( ! isGranted(Manifest.permission.CAMERA) ) permissions.add(Manifest.permission.CAMERA)
        if (permissions.size > 0)
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), PERMISSION_REQUEST_CODE)
    }


    private fun isGranted(permission: String): Boolean =
        ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED


    // return a new file with a timestamp name in a folder named [folder] in
    // the external directory for pictures.
    // Return null if the file cannot be created
    private fun getOutputMediaFile(folder: String): File? {
        // in an emulated device you can see the external files in /sdcard/Android/data/<your app>.
        val mediaStorageDir = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), folder)
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "failed to create directory")
                return null
            }
        }

        // Create a media file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val postfix = "jpg"
        val prefix = "IMG"
        return File(mediaStorageDir.path +
                File.separator + prefix +
                "_" + timeStamp + "." + postfix)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val mImage = findViewById<ImageView>(R.id.imgView)
        when (requestCode) {
            CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE ->
                if (resultCode == RESULT_OK)
                    showImageFromFile(mImage, mFile!!)
                else handleOther(resultCode)
        }
    }

    private fun handleOther(resultCode: Int) {
        if (resultCode == RESULT_CANCELED)
            Toast.makeText(this, "Canceled...", Toast.LENGTH_LONG).show()
        else Toast.makeText(this, "Picture NOT taken - unknown error...", Toast.LENGTH_LONG).show()
    }


    // show the image allocated in [f] in imageview [img]. Show meta data in [txt]
    private fun showImageFromFile(img: ImageView, f: File) {
        img.setImageURI(Uri.fromFile(f))
        img.setBackgroundColor(Color.RED)
        //mImage.setRotation(90);
    }
}