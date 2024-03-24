package com.hedmer.anibreak.common.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AnibreakInternetConnectivityChecker @Inject constructor(
  @ApplicationContext private val context: Context
): InternetConnectivityChecker {

  override fun hasInternet(): Boolean {
    val connectivityManager =
      context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
      connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    return hasAnyTransportCapabilities(capabilities)
  }

  private fun hasAnyTransportCapabilities(networkCapabilities: NetworkCapabilities?): Boolean {
    return networkCapabilities?.run {
      hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
          hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
          hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    } ?: false
  }
}

interface InternetConnectivityChecker {

  fun hasInternet(): Boolean
}