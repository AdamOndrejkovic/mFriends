package easv.oe.mfriends

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View


class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_ANSWER = 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun onClickLevel1(view: View) {
      val intent = Intent(this, ActivityFriendlist1::class.java)
        startActivity(intent)

    }
    fun onClickLevel2(view: View) {
        val intent = Intent(this, ActivityFriendlist2::class.java)
        startActivity(intent)
    }
    fun onClickLevel3(view: View) {
        val intent = Intent(this, ActivityFriendlist3::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.iCreate -> {
                Intent(this, CreateFriendActivity::class.java).also {
                    startActivityForResult(it, REQUEST_CODE_ANSWER)
                }
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ANSWER) {
            if (resultCode == RESULT_OK)
                Log.d("xyz", "ok")
        }
    }
}