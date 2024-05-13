package com.educacionit.gotorute.congrats_route.presenter

import com.educacionit.gotorute.congrats_route.model.ResultDBOperation
import com.educacionit.gotorute.congrats_route.model.ResultError
import com.educacionit.gotorute.congrats_route.model.ResultOk
import com.educacionit.gotorute.contract.CongratsContract
import com.educacionit.gotorute.home.model.maps.Place
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CongratsPresenter(private val congratsModel: CongratsContract.CongratsModel) :
    CongratsContract.ICongratsPresenter<CongratsContract.CongratsView> {
    private lateinit var congratsView: CongratsContract.CongratsView

    override fun attachView(view: CongratsContract.CongratsView) {
        congratsView = view
    }

    override fun saveFavoriteRoute(startPlace: Place?, destinationPlace: Place?) {
        CoroutineScope(Dispatchers.IO).launch {
            startPlace?.let { safeStartPlace ->
                destinationPlace?.let { safeDestinationPlace ->
                    congratsModel.saveFavoriteRoute(safeStartPlace, safeDestinationPlace, getCurrentDateFormatted()).let {
                        onFavoriteSaved(it)
                    }
                }
            } ?: withContext(Dispatchers.Main) {
                congratsView.showErrorMessage("No se pudo cargar los puntos correctamente")
            }

        }
    }

    override fun getCurrentDateFormatted(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    private suspend fun onFavoriteSaved(result: ResultDBOperation) {
        withContext(Dispatchers.Main) {
            when (result) {
                ResultOk -> congratsView.notifyFavoriteSaved()
                ResultError -> congratsView.showErrorMessage("No se pudo guardar la ruta como favorita")
            }
        }
    }
}