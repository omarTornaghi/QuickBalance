package com.example.quickbalance

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.quickbalance.fragments.CreditiFragment
import com.example.quickbalance.fragments.DebitiFragment
import com.example.quickbalance.fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottom_navigation.selectedItemId = R.id.ic_home
        val homeFragment = HomeFragment();
        val creditiFragment = CreditiFragment();
        val debitiFragment = DebitiFragment()
        creaFragmentCorrente(homeFragment)
        /*Gestione bottom navigation menu*/

        bottom_navigation.setOnNavigationItemSelectedListener{
            when(it.itemId){
                /*Cambio fragment*/
                R.id.ic_home->{
                    creaFragmentCorrente(homeFragment);
                    //tv.text = getString(R.string.str_bnv_tab1) //"Tab 1"
                    //tv.setTextColor(ContextCompat.getColor(this, R.color.colorTab1))
                    bottom_navigation.itemIconTintList = ContextCompat.getColorStateList(this, R.color.nav_bottombar_item_orange)
                    bottom_navigation.itemTextColor = ContextCompat.getColorStateList(this, R.color.nav_bottombar_item_orange)
                    true
                }
                R.id.ic_crediti->{
                    creaFragmentCorrente(creditiFragment)
                    bottom_navigation.itemIconTintList = ContextCompat.getColorStateList(this, R.color.nav_bottombar_item_green)
                    bottom_navigation.itemTextColor = ContextCompat.getColorStateList(this, R.color.nav_bottombar_item_green)
                    true
                }
                R.id.ic_debiti->{
                    creaFragmentCorrente(debitiFragment)
                    bottom_navigation.itemIconTintList = ContextCompat.getColorStateList(this, R.color.nav_bottombar_item_red)
                    bottom_navigation.itemTextColor = ContextCompat.getColorStateList(this, R.color.nav_bottombar_item_red)
                    true
                }
                else -> false
            }
            true
        }
    }

    private fun creaFragmentCorrente(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }

}