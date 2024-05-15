package com.educacionit.gotorute.home.view.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.educacionit.gotorute.R

class ProfileFragment : Fragment() {
    private lateinit var profileImageView: ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val profileView = inflater.inflate(R.layout.fragment_profile, container, false)
        profileImageView = profileView.findViewById(R.id.profile_image)
        return profileView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(this)
            .load(HARCODED_PROFILE_IMAGE)
            .circleCrop()
            .into(profileImageView)
    }

    companion object {
        const val HARCODED_PROFILE_IMAGE = "https://randomuser.me/api/portraits/men/40.jpg"
    }
}