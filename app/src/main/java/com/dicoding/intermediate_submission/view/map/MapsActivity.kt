package com.dicoding.intermediate_submission.view.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.dicoding.intermediate_submission.R
import com.dicoding.intermediate_submission.data.Result

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.dicoding.intermediate_submission.databinding.ActivityMapsBinding
import com.dicoding.intermediate_submission.di.Story
import com.google.android.gms.maps.model.LatLngBounds
import com.dicoding.intermediate_submission.data.StoryRepository
import com.dicoding.intermediate_submission.data.pref.UserPreference
import com.dicoding.intermediate_submission.di.ApiService
import com.dicoding.intermediate_submission.di.Injection
import com.dicoding.intermediate_submission.di.Injection.provideApiService
import com.dicoding.intermediate_submission.view.story.StoryViewModel
import com.dicoding.intermediate_submission.view.story.StoryViewModelFactory
import com.dicoding.intermediate_submission.view.map.MapsViewModel


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val viewModel: MapsViewModel by viewModels {
        MapsViewModelFactory(Injection.provideStoryRepository(this))
    }

    private val storyViewModel: StoryViewModel by viewModels {
        StoryViewModelFactory(Injection.provideStoryRepository(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)




        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.getStoriesWithLocation().observe(this,) { result ->
            when (result) {
                is Result.Success -> {
                     val stories = result.data
                    addMarkersToMap(stories)
                }
                is Result.Error -> {
                    }

                else -> {}
            }
        }



    }

    private fun addMarkersToMap(stories: List<Story>) {
        val boundsBuilder = LatLngBounds.Builder()
        for (story in stories) {
            val location = LatLng(story.lat?.toDouble() ?: 0.0, story.lon?.toDouble() ?: 0.0)
            mMap.addMarker(MarkerOptions().position(location).title(story.name))
            boundsBuilder.include(location)
        }

        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                30
            )
        )
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

    }
}