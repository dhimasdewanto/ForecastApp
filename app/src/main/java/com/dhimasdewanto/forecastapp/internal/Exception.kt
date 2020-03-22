package com.dhimasdewanto.forecastapp.internal

import java.io.IOException
import java.lang.Exception

class NoConnectivityException : IOException()
class LocationPermissionNotGrantedException : Exception()