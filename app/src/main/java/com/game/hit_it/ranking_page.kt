package com.game.hit_it

import MyListAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_register_score.*


class ranking_page : AppCompatActivity() {

    internal lateinit var next_page: ImageView
    internal lateinit var ranking_list: ListView
    internal lateinit var title_name: Array<String>
    internal lateinit var score_description: Array<Int>
    internal lateinit var user_imgid: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_score)

        val ref = FirebaseDatabase.getInstance().getReference("/users/")

        ref
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Log.e("data", dataSnapshot.toString())
                    val list_X = mutableListOf<Ranking_User>()
                    for (ds in dataSnapshot.children) {
                        val p: Ranking_User? = dataSnapshot.getValue(Ranking_User::class.java)
                        p?.let {
                            list_X.add(it)
                            title_name = arrayOf(it.username)
                            Log.e("TAG", "onDataChange: $title_name")
                            score_description = arrayOf(it.score)
                            Log.e("TAG", "onDataChange: $score_description")
                            user_imgid = arrayOf(it.profileImageUrl)
                            Log.e("TAG", "onDataChange: $user_imgid")
                        }
                    }
                    Log.e("List", "DATA: $list_X ")
                    val acceessAdapter =
                        MyListAdapter(this@ranking_page, title_name, score_description, user_imgid)
                    ranking_list.setAdapter(acceessAdapter)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("data", databaseError.toString())
                }
            })


        next_page = findViewById(R.id.play_again_image)
        ranking_list = findViewById(R.id.ranking_list)

        next_page.setOnClickListener {
            val intent = Intent(this, play_page::class.java)
            startActivity(intent)
            finish()
        }
        log_out.setOnClickListener {
            val intent = Intent(this, login::class.java)
            startActivity(intent)
            finish()
        }
        about_app.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            //set title for alert dialog
            builder.setTitle(R.string.dialogTitle)
            //set message for alert dialog
            builder.setMessage(R.string.dialogMessage)
            builder.setIcon(R.drawable.icon_display)
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }

        ranking_list.setOnItemClickListener() { adapterView, view, position, id ->
            val itemAtPos = adapterView.getItemAtPosition(position)
            val itemIdAtPos = adapterView.getItemIdAtPosition(position)
            Toast.makeText(
                this,
                "Click on item at $itemAtPos its item id $itemIdAtPos",
                Toast.LENGTH_LONG
            ).show()
        }

    }
}