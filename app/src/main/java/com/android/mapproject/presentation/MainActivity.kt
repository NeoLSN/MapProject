package com.android.mapproject.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.mapproject.R
import com.android.mapproject.di.androidx.HasAndroidXFragmentInjector
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasAndroidXFragmentInjector {

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        val fragmentList = supportFragmentManager.fragments
        if (!onFragmentListBackPressed(fragmentList)) super.onBackPressed()
    }

    private fun onFragmentBackPressed(fragment: Fragment): Boolean {
        val fragmentList = fragment.childFragmentManager.fragments
        return onFragmentListBackPressed(fragmentList)
    }

    private fun onFragmentListBackPressed(fragments: List<Fragment>) : Boolean {
        for (f in fragments) {
            if (onFragmentBackPressed(f)) return true
            if (f is OnBackPressed) {
                if (f.onBackPressed()) return true
            }
        }
        return false
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector
}
