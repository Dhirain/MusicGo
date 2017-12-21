package murgency.customer.ui.base.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.dhirain.musicgo.R

/**
 * Created by Dhirain Jain on 20-11-2017.
 */
abstract class BaseActivity : AppCompatActivity() {

    /**
     * Setup the UI elements.
     */
    abstract fun initUI()

    /**
     * Set click listeners on the UI elements.
     */
    abstract fun clickListener()

    /**
     * Init the presenter.
     */
    abstract fun setupPresenter()

    fun setTitleWithBackPress(title: String) {
        val cd = ColorDrawable(resources.getColor(R.color.colorPrimary))
        val mActionBar = supportActionBar

        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.rgb(17, 17, 17)
        }

        if (mActionBar != null) {
            //mActionBar.setHomeAsUpIndicator(R.drawable.back);
            mActionBar.setBackgroundDrawable(cd)
            mActionBar.setTitle(title)
            mActionBar.setDisplayHomeAsUpEnabled(true) //to activate back pressed on home button press
            mActionBar.setDisplayShowHomeEnabled(false) //
        }

    }
}