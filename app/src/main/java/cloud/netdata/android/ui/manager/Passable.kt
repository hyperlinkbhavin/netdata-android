package cloud.netdata.android.ui.manager

interface Passable<in T> {

    fun passData(t: T)

}
