package com.iftekhar.ahmed.comicviewer.ui

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.iftekhar.ahmed.comicviewer.R


class FullScreenImageActivity : AppCompatActivity() {
    var images: java.util.ArrayList<String> = java.util.ArrayList()
    private var defaultIndex = 0
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.apply { }
        setContentView(R.layout.activity_full_screen_image)
        viewPager = findViewById(R.id.view_pager)
        val bundle = intent.extras
        if (bundle != null) {
            images = bundle.getStringArrayList("images")!!
            defaultIndex = bundle.getInt("index")
        }
        viewPager.adapter = object :
            FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment {
                return FullScreenImageFragment.newInstance(images[position])
            }

            override fun getCount(): Int {
                return images.size
            }
        }
        if (images.size > defaultIndex) {
            viewPager.currentItem = defaultIndex
        }
    }

    companion object {
        fun start(activity: Activity, images: java.util.ArrayList<String>, index: Int) {
            val intent = Intent(activity, FullScreenImageActivity::class.java)
            intent.putStringArrayListExtra("images", images)
            intent.putExtra("index", index)
            activity.startActivity(intent)
        }
    }

    fun onBackClick(view: View) {
        finish()
    }
}


class FullScreenImageFragment : Fragment() {
    private lateinit var imageView: ImageView
    lateinit var progressBar: ProgressBar
    private var imageLink: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_full_screen_image, container, false)
        imageView = view.findViewById(R.id.image_full)
        progressBar = view.findViewById(R.id.progress_bar)

        val bundle = arguments
        if (bundle!!.containsKey(EXTRA_IMAGE)) {
            imageLink = bundle.getString(EXTRA_IMAGE)
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

        return view
    }

    companion object {
        const val EXTRA_IMAGE = "image"
        fun newInstance(imageLink: String): FullScreenImageFragment {
            val args = Bundle()
            args.putString(EXTRA_IMAGE, imageLink)
            val fragment = FullScreenImageFragment()
            fragment.arguments = args
            return fragment
        }
    }

}