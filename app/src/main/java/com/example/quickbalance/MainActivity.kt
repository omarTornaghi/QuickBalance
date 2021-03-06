package com.example.quickbalance

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.MotionEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.quickbalance.Activities.ImpostazioniActivity
import com.example.quickbalance.Services.NotificationWorker
import com.example.quickbalance.Utils.ValutaUtils
import com.example.quickbalance.Fragments.CreditiFragment
import com.example.quickbalance.Fragments.DebitiFragment
import com.example.quickbalance.Fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), Toolbar.OnMenuItemClickListener {
    private var selectedFragment: Int = 2
    private lateinit var homeFragment: HomeFragment
    private lateinit var creditiFragment: CreditiFragment
    private lateinit var debitiFragment: DebitiFragment
    private var firstRun:Boolean = true

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )
        topAppBar.setOnMenuItemClickListener(this)

        /*setto se necessario valuta di default */
        var codiceValuta = ValutaUtils.getSelectedCurrencyCode(this)
        if (codiceValuta.isBlank())
            ValutaUtils.saveCurrencyCode(this, ValutaUtils.getCurrencyCodeLocale())

        /* Recupero stato */
        if (savedInstanceState != null) {
            firstRun = savedInstanceState.getBoolean("firstRun")
            selectedFragment = savedInstanceState.getInt("selected")
            var cf: CreditiFragment? = getSupportFragmentManager().getFragment(
                savedInstanceState,
                "creditiFragment"
            ) as CreditiFragment?
            creditiFragment = if (cf == null) CreditiFragment() else cf
            var hf: HomeFragment? = getSupportFragmentManager().getFragment(
                savedInstanceState,
                "homeFragment"
            ) as HomeFragment?
            homeFragment = if (hf == null) HomeFragment() else hf
            var df: DebitiFragment? = getSupportFragmentManager().getFragment(
                savedInstanceState,
                "debitiFragment"
            ) as DebitiFragment?
            debitiFragment = if (df == null) DebitiFragment() else df
        } else {
            homeFragment = HomeFragment()
            creditiFragment = CreditiFragment()
            debitiFragment = DebitiFragment()
        }
        when (selectedFragment) {
            null -> {
                bottom_navigation.selectedItemId = R.id.ic_home
                selezionaFHome(homeFragment, R.anim.fragment_fade_enter, R.anim.fragment_fade_exit)
            }
            1 -> {
                bottom_navigation.selectedItemId = R.id.ic_crediti
                selezionaFCrediti(
                    creditiFragment,
                    R.anim.fragment_fade_enter,
                    R.anim.fragment_fade_exit
                )
            }
            2 -> {
                bottom_navigation.selectedItemId = R.id.ic_home
                selezionaFHome(homeFragment, R.anim.fragment_fade_enter, R.anim.fragment_fade_exit)
            }
            3 -> {
                bottom_navigation.selectedItemId = R.id.ic_debiti
                selezionaFDebiti(
                    debitiFragment,
                    R.anim.fragment_fade_enter,
                    R.anim.fragment_fade_exit
                )
            }
        }

        //WORKER
        val notificationRequest =
            PeriodicWorkRequestBuilder<NotificationWorker>(24, TimeUnit.HOURS)
                // Additional configuration
                .build()
        val wm: WorkManager = WorkManager.getInstance(this)
        if(firstRun) {
            firstRun = false
            wm.enqueueUniquePeriodicWork(
                NotificationWorker.TAG,
                ExistingPeriodicWorkPolicy.REPLACE,
                notificationRequest
            )
        }
        else
            wm.enqueueUniquePeriodicWork(NotificationWorker.TAG, ExistingPeriodicWorkPolicy.KEEP, notificationRequest)

        /*Gestione bottom navigation menu*/
        bottom_navigation.setOnNavigationItemSelectedListener {
            if (selectedFragment == getNumPagina(it.itemId)) return@setOnNavigationItemSelectedListener true
            var primaAnim: Int
            var secondaAnim: Int
            if (selectedFragment > getNumPagina(it.itemId)) {
                primaAnim = R.anim.slide_in_left
                secondaAnim = R.anim.slide_out_right
            } else {
                primaAnim = R.anim.slide_in_right
                secondaAnim = R.anim.slide_out_left
            }
            when (it.itemId) {
                /*Cambio fragment*/
                R.id.ic_home -> {
                    homeFragment = HomeFragment()
                    selezionaFHome(homeFragment, primaAnim, secondaAnim)
                    true
                }
                R.id.ic_crediti -> {
                    creditiFragment = CreditiFragment()
                    selezionaFCrediti(creditiFragment, primaAnim, secondaAnim)
                    true
                }
                R.id.ic_debiti -> {
                    debitiFragment = DebitiFragment()
                    selezionaFDebiti(debitiFragment, primaAnim, secondaAnim)
                    true
                }
                else -> false
            }
            true
        }
    }

    private fun getNumPagina(res: Int): Int {
        when (res) {
            R.id.ic_crediti -> return 1
            R.id.ic_home -> return 2
            R.id.ic_debiti -> return 3
            else -> return -1
        }
    }

    private fun selezionaFDebiti(debitiFragment: DebitiFragment, primaAnim: Int, secondaAnim: Int) {
        creaFragmentCorrente(debitiFragment, primaAnim, secondaAnim)
        selectedFragment = 3
        bottom_navigation.itemIconTintList =
            ContextCompat.getColorStateList(this, R.color.nav_bottombar_item_red)
        bottom_navigation.itemTextColor =
            ContextCompat.getColorStateList(this, R.color.nav_bottombar_item_red)
    }

    private fun selezionaFHome(homeFragment: HomeFragment, primaAnim: Int, secondaAnim: Int) {
        creaFragmentCorrente(homeFragment, primaAnim, secondaAnim);
        selectedFragment = 2
        bottom_navigation.itemIconTintList =
            ContextCompat.getColorStateList(this, R.color.nav_bottombar_item_orange)
        bottom_navigation.itemTextColor =
            ContextCompat.getColorStateList(this, R.color.nav_bottombar_item_orange)
    }

    private fun selezionaFCrediti(
        creditiFragment: CreditiFragment,
        primaAnim: Int,
        secondaAnim: Int
    ) {
        selectedFragment = 1
        creaFragmentCorrente(creditiFragment, primaAnim, secondaAnim)
        bottom_navigation.itemIconTintList =
            ContextCompat.getColorStateList(this, R.color.nav_bottombar_item_green)
        bottom_navigation.itemTextColor =
            ContextCompat.getColorStateList(this, R.color.nav_bottombar_item_green)
    }

    private fun creaFragmentCorrente(fragment: Fragment, animPrima: Int, animSeconda: Int) =
        Handler(Looper.getMainLooper()).postDelayed({
            supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(animPrima, animSeconda)
                replace(R.id.fl_wrapper, fragment)
                commit()
            }
        }, 200)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("selected", selectedFragment)
        outState.putBoolean("firstRun", firstRun)
        if (creditiFragment.isAdded())
            getSupportFragmentManager().putFragment(outState, "creditiFragment", creditiFragment)
        if (homeFragment.isAdded())
            getSupportFragmentManager().putFragment(outState, "homeFragment", homeFragment)
        if (debitiFragment.isAdded())
            getSupportFragmentManager().putFragment(outState, "debitiFragment", debitiFragment)
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                val int = Intent(this, ImpostazioniActivity::class.java)
                startActivity(int)
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