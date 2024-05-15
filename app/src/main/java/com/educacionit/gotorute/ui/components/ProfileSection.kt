package com.educacionit.gotorute.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.educacionit.gotorute.R

class ProfileSection(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    init {
        LayoutInflater.from(context).inflate(R.layout.profile_settings_item, this, true)

       val imageView = findViewById<ImageView>(R.id.profile_image)


        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.ProfileSection)
            val title = typedArray.getString(R.styleable.ProfileSection_itemTitle)
            val details = typedArray.getString(R.styleable.ProfileSection_details)
            val iconResource = typedArray.getResourceId(R.styleable.ProfileSection_iconResource, 0)
            typedArray.recycle()

            if (iconResource != 0) {
                imageView.setImageResource(iconResource)
            }

            findViewById<TextView>(R.id.item_title).text = title
            findViewById<TextView>(R.id.item_details).text = details

        }
    }
}

