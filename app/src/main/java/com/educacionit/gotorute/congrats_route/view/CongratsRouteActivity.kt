package com.educacionit.gotorute.congrats_route.view

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.educacionit.gotorute.R
import com.educacionit.gotorute.congrats_route.model.CongratsRepository
import com.educacionit.gotorute.congrats_route.presenter.CongratsPresenter
import com.educacionit.gotorute.contract.CongratsContract
import com.educacionit.gotorute.home.model.maps.Place
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class CongratsRouteActivity : AppCompatActivity(), CongratsContract.CongratsView {

    private lateinit var favoriteButton: ImageButton
    private lateinit var textDestinationRoute: TextView
    private lateinit var textOriginRoute: TextView
    private lateinit var textDate: TextView
    private var isFavorite = false
    private var startPlace: Place? = null
    private var destinationPlace: Place? = null
    private lateinit var congratsPresenter: CongratsContract.ICongratsPresenter<CongratsContract.CongratsView>
    private var startPointName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_congrats_route)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        favoriteButton = findViewById(R.id.save_favorite_button)
        textDestinationRoute = findViewById(R.id.text_destination)
        textOriginRoute = findViewById(R.id.text_origin)
        textDate = findViewById(R.id.text_date)

        initPresenter()
        getParamsFromIntent()
        configureView()
    }

    private fun getParamsFromIntent() {
        startPlace = intent?.extras?.getSerializable(EXTRA_START_PLACE) as? Place
        destinationPlace = intent?.extras?.getSerializable(EXTRA_DESTINATION_PLACE) as? Place
    }

    private fun configureView() {
        configureIconButton()
        favoriteButton.setOnClickListener {
            isFavorite = !isFavorite
            configureIconButton()

            if (isFavorite) {
                if (startPointName == "") {
                    showPlaceNameDialog()
                    return@setOnClickListener
                }
                congratsPresenter.saveFavoriteRoute(startPlace, destinationPlace)
            }
        }
        textOriginRoute.text = startPlace?.displayName
        textDestinationRoute.text = destinationPlace?.displayName?.split(",")?.take(3)?.joinToString(",")?:""
        textDate.text = congratsPresenter.getCurrentDateFormatted()
    }

    private fun showPlaceNameDialog() {
        val customView = LayoutInflater.from(this@CongratsRouteActivity)
            .inflate(R.layout.dialog_place_name, null)
        val editTextPlaceName = customView.findViewById<EditText>(R.id.editTextPlaceName)

        val dialog = MaterialAlertDialogBuilder(
            this@CongratsRouteActivity,
            R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog
        )
            .setTitle("Ingresa el nombre del punto inicial")
            .setView(customView)
            .setPositiveButton(
                "Ok"
            ) { dialog, _ ->
                startPointName = editTextPlaceName.text.toString()
                startPlace?.displayName = startPointName
                textOriginRoute.text = startPointName
                congratsPresenter.saveFavoriteRoute(startPlace, destinationPlace)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }

        dialog.show()
    }

    private fun configureIconButton() {
        val iconId = if (isFavorite) R.drawable.favorite_red else R.drawable.favorite_gray
        favoriteButton.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                iconId,
                null
            )
        )
    }

    override fun onStart() {
        super.onStart()
        playCongratsSound()
    }

    private fun playCongratsSound() {
        val mediaPlayer = MediaPlayer.create(this, R.raw.applause)
        mediaPlayer.start()
    }


    override fun notifyFavoriteSaved() {
        Toast.makeText(
            this,
            "Tu ruta se a guardado en favoritos correctamente!",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun getViewContext() = this

    override fun initPresenter() {
        val congratsModel = CongratsRepository(this)
        congratsPresenter = CongratsPresenter(congratsModel)
        congratsPresenter.attachView(this)
    }

    companion object {
        const val EXTRA_START_PLACE = "EXTRA_START_PLACE"
        const val EXTRA_DESTINATION_PLACE = "EXTRA_DESTINATION_PLACE"
    }
}