package app.jw.mapable.gm.Info

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.jw.mapable.gm.R
import kotlinx.android.synthetic.main.item_container_onboarding.view.*

class OnBoardingAdapter(onboardingItems: List<OnboardingItem>) : RecyclerView.Adapter<OnBoardingAdapter.OnboardingViewHolder>(){


    var onboardingItems : List<OnboardingItem> = onboardingItems


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        return OnboardingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_container_onboarding, parent, false))
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.setOnBoardingData(onboardingItems[position])
    }

    override fun getItemCount(): Int {
        return onboardingItems.size
    }

    class OnboardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setOnBoardingData(onboardingItem: OnboardingItem)
        {
            itemView.imageOnboarding.setImageResource(onboardingItem.getImage())
        }



    }


}