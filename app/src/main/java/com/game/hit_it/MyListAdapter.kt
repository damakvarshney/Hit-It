import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.game.hit_it.R
import com.game.hit_it.Ranking_User

class MyListAdapter (var mCtx: Context, var resource:Int, var item:List<Ranking_User>)
    : ArrayAdapter<Ranking_User>( mCtx , resource , item) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)

        val view: View = layoutInflater.inflate(resource,null)
        var user_image: ImageView = view.findViewById(R.id.user_image)
        var user_name : TextView = view.findViewById(R.id.user_name)
        var score : TextView = view.findViewById(R.id.scores)

        var Ranking_User: Ranking_User = item[position]
        user_name.text = Ranking_User.username
        score.text = Ranking_User.score.toString()
        Glide.with(context)
            .load(user_image)
            .placeholder(R.drawable.icon_display)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    user_image.setImageResource(R.drawable.icon_display)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .into(user_image)

        return view
    }
}