package com.example.quickbalance

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.quickbalance.fragments.CreditiFragment
import com.example.quickbalance.fragments.DebitiFragment
import com.example.quickbalance.fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), Toolbar.OnMenuItemClickListener {
    private var selectedFragment:Int = 2
    private var homeFragment:HomeFragment = HomeFragment()
    private  var creditiFragment: CreditiFragment = CreditiFragment()
    private  var debitiFragment: DebitiFragment = DebitiFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED)
        topAppBar.setOnMenuItemClickListener(this)
        if(savedInstanceState != null){
            selectedFragment = savedInstanceState.getInt("selected")
            var cf: CreditiFragment?
            cf = getSupportFragmentManager().getFragment(savedInstanceState, "creditiFragment") as CreditiFragment?
            creditiFragment = if(cf == null) CreditiFragment() else cf
        }

        when(selectedFragment){
            null-> {
                bottom_navigation.selectedItemId = R.id.ic_home
                selezionaFHome(homeFragment)
            }
            1-> {
                bottom_navigation.selectedItemId = R.id.ic_crediti
                selezionaFCrediti(creditiFragment)
            }
            2-> {
                bottom_navigation.selectedItemId = R.id.ic_home
                selezionaFHome(homeFragment)
            }
            3-> {
                bottom_navigation.selectedItemId = R.id.ic_debiti
                selezionaFDebiti(debitiFragment)
            }
        }

        /*Gestione bottom navigation menu*/

        bottom_navigation.setOnNavigationItemSelectedListener{
            when(it.itemId){
                /*Cambio fragment*/
                R.id.ic_home->{
                    homeFragment = HomeFragment()
                    selezionaFHome(homeFragment)
                    true
                }
                R.id.ic_crediti->{
                    creditiFragment = CreditiFragment()
                    selezionaFCrediti(creditiFragment)
                    true
                }
                R.id.ic_debiti->{
                    debitiFragment = DebitiFragment()
                    selezionaFDebiti(debitiFragment)
                    true
                }
                else -> false
            }
            true
        }
    }

    private fun selezionaFDebiti(debitiFragment: DebitiFragment) {
        creaFragmentCorrente(debitiFragment)
        selectedFragment = 3
        bottom_navigation.itemIconTintList =
            ContextCompat.getColorStateList(this, R.color.nav_bottombar_item_red)
        bottom_navigation.itemTextColor =
            ContextCompat.getColorStateList(this, R.color.nav_bottombar_item_red)
    }

    private fun selezionaFHome(homeFragment: HomeFragment) {
        creaFragmentCorrente(homeFragment);
        selectedFragment = 2
        bottom_navigation.itemIconTintList =
            ContextCompat.getColorStateList(this, R.color.nav_bottombar_item_orange)
        bottom_navigation.itemTextColor =
            ContextCompat.getColorStateList(this, R.color.nav_bottombar_item_orange)
    }

    private fun selezionaFCrediti(creditiFragment: CreditiFragment) {
        selectedFragment = 1
        creaFragmentCorrente(creditiFragment)
        bottom_navigation.itemIconTintList =
            ContextCompat.getColorStateList(this, R.color.nav_bottombar_item_green)
        bottom_navigation.itemTextColor =
            ContextCompat.getColorStateList(this, R.color.nav_bottombar_item_green)
    }

    private fun creaFragmentCorrente(fragment: Fragment) =
        Handler(Looper.getMainLooper()).postDelayed({
            supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                replace(R.id.fl_wrapper, fragment)
                commit()
            }
        }, 200)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selected", selectedFragment)
        if(creditiFragment.isAdded())
            getSupportFragmentManager().putFragment(outState, "creditiFragment", creditiFragment);
    }

    override fun onMenuItemClick(item: MenuItem):Boolean {
        when (item.itemId){
            R.id.search-> {
                //Start activity delle impostazioni
                return true
            }
        }
        return false;
    }

    //Nasconde keyboard quando non si clicca su editText
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }


}