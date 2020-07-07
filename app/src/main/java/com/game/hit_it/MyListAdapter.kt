import android.app.Activity
import android.graphics.drawable.Drawable
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

class MyListAdapter(
    private var context: Activity,
    private var title: Array<String>,
    private var description: Array<Int>,
    private var imgid: Array<String>
) : ArrayAdapter<String>(context, R.layout.custom_list, title) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var inflater = context.layoutInflater
        var rowView = inflater.inflate(R.layout.custom_list, null, true)

        var titleText = rowView.findViewById(R.id.title) as TextView
        var imageView = rowView.findViewById(R.id.icon) as ImageView
        var subtitleText = rowView.findViewById(R.id.description) as TextView

        titleText.text = title[position]
        subtitleText.text = description[position].toString()
        Glide.with(context)
            .load(imgid)
            .placeholder(R.drawable.glide_default)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    imageView.setImageResource(R.drawable.glide_default)
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
            .into(imageView)

        return rowView
    }
}