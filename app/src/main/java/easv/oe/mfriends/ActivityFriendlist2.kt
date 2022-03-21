package easv.oe.mfriends

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SimpleAdapter
import android.widget.Toast
import com.easv.oe.friends.Model.BEFriend
import com.easv.oe.friends.Model.Friends
import kotlinx.android.synthetic.main.activity_friendlist.*

class ActivityFriendlist2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friendlist)

        val adapter = SimpleAdapter(
            this,
            asListMap(Friends().getAll()),
            R.layout.cell,
            arrayOf("name", "phone", "address"),
            intArrayOf(R.id.name, R.id.phone, R.id.address)
        )

        lvFriends.adapter = adapter

        lvFriends.setOnItemClickListener { _,_,pos, _ -> onListItemClick(pos) }
    }

    private fun asListMap(src: Array<BEFriend>): List<Map<String, String?>> {
        return src.map{ person -> hashMapOf("name" to person.name, "phone" to person.phone, "address" to person.address) }
    }

    fun onListItemClick( position: Int ) {
        // position is in the list!
        // first get the name of the person clicked
        val name = Friends().getAll()[position].name
        // and a greeting
        Toast.makeText(
            this,
            "Hi $name! Have you done your homework?",
            Toast.LENGTH_LONG
        ).show()


    }
}