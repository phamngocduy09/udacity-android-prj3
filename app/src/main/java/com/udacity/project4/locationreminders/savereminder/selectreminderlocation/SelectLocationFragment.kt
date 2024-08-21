package com.udacity.project4.locationreminders.savereminder.selectreminderlocation

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.view.View.OnClickListener
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.udacity.project4.R
import com.udacity.project4.base.BaseFragment
import com.udacity.project4.databinding.FragmentSelectLocationBinding
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import com.udacity.project4.utils.setDisplayHomeAsUpEnabled
import org.koin.android.ext.android.inject
import java.util.Locale

class SelectLocationFragment : BaseFragment(), OnMapReadyCallback, OnClickListener {

    override val _viewModel: SaveReminderViewModel by inject()
    private lateinit var binding: FragmentSelectLocationBinding
    private lateinit var gMap: GoogleMap
    private var marker: Marker? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val layoutId = R.layout.fragment_select_location
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)

        binding.viewModel = _viewModel
        binding.lifecycleOwner = this

        setHasOptionsMenu(true)
        setDisplayHomeAsUpEnabled(true)

        val frgMap = childFragmentManager.findFragmentById(R.id.frgMap) as SupportMapFragment
        frgMap.getMapAsync(this)

        binding.btnSave.setOnClickListener(this)
        return binding.root
    }

    override fun onClick(v: View?) {
        if (v?.id == binding.btnSave.id) {
            onLocationSelected()
        }
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "inflater.inflate(R.menu.map_options, menu)",
            "com.udacity.project4.R"
        )
    )
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.map_options, menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.normal_map -> {
            gMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }

        R.id.hybrid_map -> {
            gMap.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }

        R.id.satellite_map -> {
            gMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }

        R.id.terrain_map -> {
            gMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    override fun onMapReady(p0: GoogleMap) {
        gMap = p0
        gMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                requireContext(),
                R.raw.map_style
            )
        )
        gMap.setOnMapLongClickListener {
            addMapLongMarker(it)
        }
        addMapPoiMarker()
        checkPermission()
    }

    private fun onLocationSelected() {
        marker?.let {
            _viewModel.reminderSelectedLocationStr.value = it.title
            _viewModel.latitude.value = it.position.latitude
            _viewModel.longitude.value = it.position.longitude
        }

        findNavController().popBackStack()
    }

    private fun addMapLongMarker(latLng: LatLng) {
        marker?.remove()
        val snippet = String.format(
            Locale.getDefault(),
            getString(R.string.lat_long_snippet),
            latLng.latitude,
            latLng.longitude
        )
        marker = gMap.addMarker(
            MarkerOptions()
                .title(getString(R.string.dropped_pin))
                .position(latLng)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        )
        gMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng,
                15f
            )
        )
        marker?.showInfoWindow()
    }

    private fun addMapPoiMarker() {
        gMap.setOnPoiClickListener {
            marker?.remove()
            marker = gMap.addMarker(
                MarkerOptions()
                    .position(it.latLng)
                    .title(it.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            )
            marker?.showInfoWindow()
        }

    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            enableMyLocation()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        gMap.isMyLocationEnabled = true
        val lastLocation = LocationServices
            .getFusedLocationProviderClient(requireContext())
            .lastLocation

        lastLocation.addOnCompleteListener(requireActivity()) {
            if (it.isSuccessful) {
                val latLng = LatLng(it.result.latitude, it.result.longitude)
                gMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        latLng,
                        15f
                    )
                )
                addMapLongMarker(latLng)
            }
        }
    }
}