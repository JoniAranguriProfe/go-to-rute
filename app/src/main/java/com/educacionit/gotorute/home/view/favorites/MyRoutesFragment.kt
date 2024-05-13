package com.educacionit.gotorute.home.view.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.educacionit.gotorute.R
import com.educacionit.gotorute.congrats_route.model.db.entities.FavoriteRoute
import com.educacionit.gotorute.contract.BaseContract
import com.educacionit.gotorute.contract.FavoritesContract
import com.educacionit.gotorute.home.model.favorites.FavoritesRepository
import com.educacionit.gotorute.home.model.maps.NavigateRepository
import com.educacionit.gotorute.home.presenter.favorites.FavoritesPresenter
import com.educacionit.gotorute.home.presenter.maps.NavigatePresenter
import com.educacionit.gotorute.home.view.favorites.adapters.FavoritesAdapter

class MyRoutesFragment : Fragment(), FavoritesContract.FavoritesView<BaseContract.IBaseView> {
    private lateinit var favoritesRecyclerView: RecyclerView
    private lateinit var favoritesAdapter: FavoritesAdapter
    private lateinit var favoritesPresenter: FavoritesContract.IFavoritesPresenter<FavoritesContract.FavoritesView<BaseContract.IBaseView>>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val favoritesView = inflater.inflate(R.layout.fragment_my_routes, container, false)
        favoritesRecyclerView = favoritesView.findViewById(R.id.favorites_recyclerview)
        return favoritesView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoritesAdapter = FavoritesAdapter()
        favoritesRecyclerView.layoutManager = LinearLayoutManager(context)
        favoritesRecyclerView.adapter = favoritesAdapter
        initPresenter()
    }

    override fun setFavoritesItems(favorites: List<FavoriteRoute>) {
        favoritesAdapter.setData(favorites)
    }

    override fun getParentView(): BaseContract.IBaseView? {
        return activity as? BaseContract.IBaseView
    }

    override fun initPresenter() {
        context?.let {
            val favoritesModel = FavoritesRepository(it)
            favoritesPresenter = FavoritesPresenter(favoritesModel)
            favoritesPresenter.attachView(this)
        }
    }

}