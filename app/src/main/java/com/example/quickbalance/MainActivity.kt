package com.example.quickbalance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.quickbalance.fragments.CreditiFragment
import com.example.quickbalance.fragments.DebitiFragment
import com.example.quickbalance.fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment = HomeFragment();
        val creditiFragment = CreditiFragment();
        val debitiFragment = DebitiFragment()
        creaFragmentCorrente(homeFragment)
        /*Gestione bottom navigation menu*/
        bottom_navigation.setOnNavigationItemSelectedListener{
            when(it.itemId){
                /*Cambio fragment*/
                R.id.ic_home->{creaFragmentCorrente(homeFragment); true }
                R.id.ic_crediti->{creaFragmentCorrente(creditiFragment); true }
                R.id.ic_debiti->{creaFragmentCorrente(debitiFragment); true }
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