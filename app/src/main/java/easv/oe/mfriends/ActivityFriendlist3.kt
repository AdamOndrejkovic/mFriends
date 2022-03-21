package easv.oe.mfriends

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.easv.oe.friends.Model.BEFriend
import com.easv.oe.friends.Model.Friends
import kotlinx.android.synthetic.main.activity_friendlist.*

class ActivityFriendlist3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friendlist)

        val layout = findViewById<LinearLayout>(R.id.vButton)
        val button = Button(this)
        button.text = "Filter Favorites"
        button.setOnClickListener{
            if (button.text === "Filter Favorites"){
                lvFriends.adapter = FriendAdapter(this, Friends().getFavorites())
                button.text = "Show All"
            }else{
                lvFriends.adapter = FriendAdapter(this, Friends().getAll())
                button.text = "Filter Favorites"
            }
        }
        layout.addView(button)

        val adapter = FriendAdapter(this, Friends().getAll())

        lvFriends.adapter = adapter
    }

    internal class FriendAdapter(context: Context,
                                 private val friends: Array<BEFriend>
    ) : ArrayAdapter<BEFriend>(context, 0, friends)
    {
        // these colors are used to toogle the background of the list items.
        private val colours = intArrayOf(
            Color.parseColor("#AAAAAA"),
            Color.parseColor("#CCCCCC")
        )

        override fun getView(position: Int, v: View?, parent: ViewGroup): View {
            var v1: View? = v
            if (v1 == null) {
                val mInflater = LayoutInflater.from(context)
                v1 = mInflater.inflate(R.layout.cell_extended, null)

            }
            val resView: View = v1!!
            resView.setBackgroundColor(colours[position % colours.size])
            val f = friends[position]
            val nameView = resView.findViewById<TextView>(R.id.tvNameExt)
            val phoneView = resView.findViewById<TextView>(R.id.tvPhoneExt)
            val favoriteView = resView.findViewById<ImageView>(R.id.imgFavoriteExt)
            val addressView = resView.findViewById<TextView>(R.id.tvAddressExt)
            nameView.text = f.name
            phoneView.text = f.phone
            addressView.text = f.address

            favoriteView.setImageResource(if (f.isFavorite) R.drawable.ok else R.drawable.notok)

            return resView
        }
    }
}