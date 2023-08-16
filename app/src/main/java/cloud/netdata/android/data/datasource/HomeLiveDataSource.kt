package cloud.netdata.android.data.datasource

import cloud.netdata.android.data.repository.HomeRepository
import cloud.netdata.android.data.service.HomeService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeLiveDataSource @Inject constructor(private val homeService: HomeService) : BaseDataSource(), HomeRepository {

}
