package com.netdata.app.data.datasource

import com.netdata.app.data.repository.HomeRepository
import com.netdata.app.data.service.HomeService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeLiveDataSource @Inject constructor(private val homeService: HomeService) : BaseDataSource(), HomeRepository {

}
