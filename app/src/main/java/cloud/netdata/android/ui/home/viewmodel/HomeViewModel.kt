package cloud.netdata.android.ui.home.viewmodel

import cloud.netdata.android.data.repository.HomeRepository
import cloud.netdata.android.ui.base.BaseViewModel
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) : BaseViewModel() {

}