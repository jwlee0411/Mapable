package app.jw.mapable.gm.Info

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import app.jw.mapable.gm.R
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity() {

    lateinit var onBoardingAdapter : OnBoardingAdapter

    lateinit var item : OnboardingItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        setupOnboardingItems()

        onBoardingViewPager.adapter = onBoardingAdapter

        setupOnboardingIndicators()
        setCurrentOnboardingIndicator(0)

        onBoardingViewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentOnboardingIndicator(position)
            }
        })


        buttonOnBoardingAction.setOnClickListener {
            if(onBoardingViewPager.currentItem + 1 < onBoardingAdapter.itemCount)
            {
                onBoardingViewPager.setCurrentItem(onBoardingViewPager.currentItem+1)

            }
            else
            {
                finish()
            }
        }





    }

    fun setupOnboardingItems()
    {
        var onboardingItems = ArrayList<OnboardingItem>()

        item = OnboardingItem()
        item.setImage(R.drawable.fragment_01)
        onboardingItems.add(item)

        item = OnboardingItem()
        item.setImage(R.drawable.fragment_02)
        onboardingItems.add(item)

        item = OnboardingItem()
        item.setImage(R.drawable.fragment_03)
        onboardingItems.add(item)

        item = OnboardingItem()
        item.setImage(R.drawable.fragment_04)
        onboardingItems.add(item)

        item = OnboardingItem()
        item.setImage(R.drawable.fragment_05)
        onboardingItems.add(item)

        item = OnboardingItem()
        item.setImage(R.drawable.fragment_06)
        onboardingItems.add(item)

        item = OnboardingItem()
        item.setImage(R.drawable.fragment_07)
        onboardingItems.add(item)

        item = OnboardingItem()
        item.setImage(R.drawable.fragment_08)
        onboardingItems.add(item)

        item = OnboardingItem()
        item.setImage(R.drawable.fragment_09)
        onboardingItems.add(item)

        item = OnboardingItem()
        item.setImage(R.drawable.fragment_10)
        onboardingItems.add(item)

        item = OnboardingItem()
        item.setImage(R.drawable.fragment_11)
        onboardingItems.add(item)

        item = OnboardingItem()
        item.setImage(R.drawable.fragment_12)
        onboardingItems.add(item)

        item = OnboardingItem()
        item.setImage(R.drawable.fragment_13)
        onboardingItems.add(item)

        item = OnboardingItem()
        item.setImage(R.drawable.fragment_14)
        onboardingItems.add(item)

        item = OnboardingItem()
        item.setImage(R.drawable.fragment_15)
        onboardingItems.add(item)


        onBoardingAdapter = OnBoardingAdapter(onboardingItems)


    }

    private fun setupOnboardingIndicators()
    {
        val indicators = ArrayList<ImageView>()

        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        layoutParams.setMargins(8, 0, 8, 0)

        for(i in indicators.indices)
        {
            indicators[i] = ImageView(applicationContext)
            indicators[i].setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.fragment_01))
            indicators[i].layoutParams = layoutParams


        }


    }


    private fun setCurrentOnboardingIndicator(index : Int)
    {


        if(index == onBoardingAdapter.itemCount - 1)
        {
            buttonOnBoardingAction.visibility = View.VISIBLE
            buttonOnBoardingAction.text = "시작하기"
        }
        else
        {
            buttonOnBoardingAction.visibility = View.GONE
            buttonOnBoardingAction.text = "Next"
        }

    }


}