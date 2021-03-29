import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.iftekhar.ahmed.comicviewer.R


fun Context.toast(message: String?, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message?:"Unknown Error", length).show()
}

fun Activity.snack(
    message: String,
    view: View = findViewById(android.R.id.content),
    length: Int = Snackbar.LENGTH_SHORT
) {
    Snackbar.make(view, message, length).show()
}

fun Fragment.snack(message: String, view: View, length: Int = Snackbar.LENGTH_SHORT) {

    Snackbar.make(view, message, length).show()
}

fun Activity.snackRetry(
    message: String?,
    anchorView: View = findViewById(android.R.id.content),
    blk: (View) -> Unit
) {

    Snackbar.make(anchorView, message ?: "Unknown error try again", Snackbar.LENGTH_INDEFINITE)
        .setAction("Retry", blk).show()
}



fun Activity.glideLoad(imageLink:String?,progressBar:ProgressBar,imageView:ImageView){
    progressBar.visibility = View.VISIBLE
    Glide.with(this).load(imageLink).listener(object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            progressBar.visibility = View.GONE
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            progressBar.visibility = View.GONE
            return false
        }
    }).error(R.drawable.placeholder).into(imageView)


}