package easv.oe.mfriends

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.Toast
import com.easv.oe.friends.Model.Friends
import kotlinx.android.synthetic.main.activity_friendlist.*

class ActivityFriendlist1 : AppCompatActivity() {

    private val REQUEST_CODE_ANSWER = 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friendlist)

        val mFriendNames: Array<String> = Friends().getAllNames()

        val adapter : ListAdapter  = ArrayAdapter<String>(this,
                                                          android.R.layout.simple_list_item_1,
                                                          mFriendNames)

        lvFriends.adapter = adapter

        lvFriends.setOnItemClickListener { _,_,pos, _ -> onListItemClick(pos) }
    }

    fun onListItemClick( position: Int ) {

        val name = Friends().getAll()[position].name

        Intent(this, CreateFriendActivity::class.java).also {
            it.putExtra("EXTRA_FRIEND", position)
            startActivityForResult(it, REQUEST_CODE_ANSWER)
        }


    }
}