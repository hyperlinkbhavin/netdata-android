package com.netdata.app.ui.home.viewmodel

import com.netdata.app.data.repository.HomeRepository
import com.netdata.app.ui.base.BaseViewModel
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) : BaseViewModel() {

}