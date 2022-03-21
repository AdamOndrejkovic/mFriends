package easv.oe.mfriends

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.easv.oe.friends.Model.BEFriend
import com.easv.oe.friends.Model.Friends

class CreateFriendActivity: AppCompatActivity() {

    val PHONE_NO = "12345678"
    val TAG = "xyz"
    var position = -1

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

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

    val PERMISSION_REQUEST_CODE = 1

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
        Friends().getAll().drop(position)
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
            Friends().getAll().plus(friend)
        }
        
        Log.d("xyz", nameInput.text.toString())
        Log.d("xyz", phoneInput.text.toString())

        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }
}