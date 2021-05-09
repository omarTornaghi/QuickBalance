package com.example.quickbalance

import android.app.ActivityManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.quickbalance.Services.NotificationJobService
import com.example.quickbalance.Utils.ValutaUtils
import com.example.quickbalance.fragments.CreditiFragment
import com.example.quickbalance.fragments.DebitiFragment
import com.example.quickbalance.fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), Toolbar.OnMenuItemClickListener {
    private var selectedFragment:Int = 2
    private lateinit var homeFragment:HomeFragment
    private lateinit var creditiFragment:CreditiFragment
    private lateinit var debitiFragment:DebitiFragment

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )
        topAppBar.setOnMenuItemClickListener(this)
        //JOBSERVICE
        if(checkServiceRunning(NotificationJobService::class.java))
            Log.d("XXXX", "STA ANDANDO")
        else
            Log.d("XXXX", "NO")

        val builder = JobInfo.Builder(
            1, ComponentName(
                packageName,
                NotificationJobService::class.java.name
            )
        ) // funziona solo con reti WiFi
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
        builder.setPeriodic(24 * 60 * 60 * 1000L)
        var job: JobInfo =builder.build()
        // 2. otteniamo un riferimento al JobScheduler
        var scheduler:JobScheduler = this.getSystemService(
            JOB_SCHEDULER_SERVICE
        ) as JobScheduler
        // 3. scheduliamo il Job
        val result = scheduler.schedule(job)
        // 4. verifichiamo se schedulazione andata in porto
        if (result==JobScheduler.RESULT_SUCCESS)
            Log.d("XXXX", "JOB ATTIVATO")
        else
            Log.d("XXXX", "JOB NON ATTIVATO")

        //FINE JOB SERVICE

        /*setto se necessario valuta di default */
        var codiceValuta = ValutaUtils.getSelectedCurrencyCode(this)
        if(codiceValuta.isBlank())
            ValutaUtils.saveCurrencyCode(this, ValutaUtils.getCurrencyCodeLocale())
        if(savedInstanceState != null){
            selectedFragment = savedInstanceState.getInt("selected")
            var cf: CreditiFragment? = getSupportFragmentManager().getFragment(
                savedInstanceState,
                "creditiFragment"
            ) as CreditiFragment?
            creditiFragment = if(cf == null) CreditiFragment() else cf
            var hf: HomeFragment? = getSupportFragmentManager().getFragment(
                savedInstanceState,
                "homeFragment"
            ) as HomeFragment?
            homeFragment = if(hf == null) HomeFragment() else hf
            var df: DebitiFragment? = getSupportFragmentManager().getFragment(
                savedInstanceState,
                "debitiFragment"
            ) as DebitiFragment?
            debitiFragment = if(df == null) DebitiFragment() else df
        }
        else{
            homeFragment = HomeFragment()
            creditiFragment = CreditiFragment()
            debitiFragment = DebitiFragment()
        }
        when(selectedFragment){
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

        /*Gestione bottom navigation menu*/
        bottom_navigation.setOnNavigationItemSelectedListener{
            if(selectedFragment == getNumPagina(it.itemId)) return@setOnNavigationItemSelectedListener true
            var primaAnim:Int
            var secondaAnim:Int
            if(selectedFragment > getNumPagina(it.itemId)){
                primaAnim = R.anim.slide_in_left
                secondaAnim = R.anim.slide_out_right
            }
            else
            {
                primaAnim = R.anim.slide_in_right
                secondaAnim = R.anim.slide_out_left
            }
            when(it.itemId){
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

    private fun getNumPagina(res: Int):Int{
        when(res) {
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
        if(creditiFragment.isAdded())
            getSupportFragmentManager().putFragment(outState, "creditiFragment", creditiFragment)
        if(homeFragment.isAdded())
            getSupportFragmentManager().putFragment(outState, "homeFragment", homeFragment)
        if(debitiFragment.isAdded())
            getSupportFragmentManager().putFragment(outState, "debitiFragment", debitiFragment)
    }

    override fun onMenuItemClick(item: MenuItem):Boolean {
        when (item.itemId){
            R.id.search -> {
                val int = Intent(this, ImpostazioniActivity::class.java)
                startActivity(int)
                return true
            }
        }
        return false;
    }

    fun checkServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
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