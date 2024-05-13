package com.educacionit.gotorute.home.presenter.favorites

import com.educacionit.gotorute.contract.FavoritesContract
import com.educacionit.gotorute.contract.HomeContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesPresenter(private val favoritesModel: FavoritesContract.FavoritesModel) :
    FavoritesContract.IFavoritesPresenter<FavoritesContract.FavoritesView<HomeContract.HomeView>> {
    private lateinit var favoritesView: FavoritesContract.FavoritesView<HomeContract.HomeView>

    override fun attachView(view: FavoritesContract.FavoritesView<HomeContract.HomeView>) {
        this.favoritesView = view
        getFavoriteRoutes()
    }

    override fun getFavoriteRoutes() {
        CoroutineScope(Dispatchers.IO).launch {
            val favoritesFromDb = favoritesModel.getFavorites()
            withContext(Dispatchers.Main) {
                favoritesView.setFavoritesItems(favoritesFromDb)
            }
        }
    }
}