package com.example.quickbalance

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.quickbalance.fragments.CreditiFragment
import com.example.quickbalance.fragments.DebitiFragment
import com.example.quickbalance.fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var selectedFragment:Int = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment = HomeFragment();
        val creditiFragment = CreditiFragment();
        val debitiFragment = DebitiFragment()

        val stato: Int? = savedInstanceState?.getInt("selected")
        when(stato){
            null-> {
                //TODO Rimettere ic_home come primo fragment
                bottom_navigation.itemIconTintList = ContextCompat.getColorStateList(this, R.color.nav_bottombar_item_green)
                bottom_navigation.itemTextColor = ContextCompat.getColorStateList(this, R.color.nav_bottombar_item_green)
                bottom_navigation.selectedItemId = R.id.ic_crediti
                creaFragmentCorrente(creditiFragment)
                /*
                bottom_navigation.selectedItemId = R.id.ic_home
                creaFragmentCorrente(homeFragment)
                 */
            }
            1-> {
                bottom_navigation.selectedItemId = R.id.ic_crediti
                creaFragmentCorrente(creditiFragment)
            }
            2-> {
                bottom_navigation.selectedItemId = R.id.ic_home
                creaFragmentCorrente(homeFragment)
            }
            3-> {
                bottom_navigation.selectedItemId = R.id.ic_debiti
                creaFragmentCorrente(debitiFragment)
            }
        }



        /*Gestione bottom navigation menu*/

        bottom_navigation.setOnNavigationItemSelectedListener{
            when(it.itemId){
                /*Cambio fragment*/
                R.id.ic_home->{
                    creaFragmentCorrente(homeFragment);
                    bottom_navigation.itemIconTintList = ContextCompat.getColorStateList(this, R.color.nav_bottombar_item_orange)
                    bottom_navigation.itemTextColor = ContextCompat.getColorStateList(this, R.color.nav_bottombar_item_orange)
                    selectedFragment = 2;
                    true
                }
                R.id.ic_crediti->{
                    creaFragmentCorrente(creditiFragment)
                    bottom_navigation.itemIconTintList = ContextCompat.getColorStateList(this, R.color.nav_bottombar_item_green)
                    bottom_navigation.itemTextColor = ContextCompat.getColorStateList(this, R.color.nav_bottombar_item_green)
                    selectedFragment = 1;
                    true
                }
                R.id.ic_debiti->{
                    creaFragmentCorrente(debitiFragment)
                    bottom_navigation.itemIconTintList = ContextCompat.getColorStateList(this, R.color.nav_bottombar_item_red)
                    bottom_navigation.itemTextColor = ContextCompat.getColorStateList(this, R.color.nav_bottombar_item_red)
                    selectedFragment = 3;
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selected", selectedFragment)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        selectedFragment = savedInstanceState.getInt("selected")
    }
}