package app.jw.mapable.gm.community

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.jw.mapable.gm.databinding.FragmentCommunityMyBinding
import app.jw.mapable.gm.setting.UserSettingActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_community_my.*
import kotlinx.android.synthetic.main.fragment_community_my.view.*
import kotlinx.android.synthetic.main.item_community_summary.view.*

class CommunityMyFragment : Fragment() {
    private var _binding : FragmentCommunityMyBinding? = null
    private val binding get() = _binding!!

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    var userName = ""
    var userMessage = ""
    var userPhoto = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommunityMyBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedPreferences = requireContext().getSharedPreferences("preferences", 0)
        editor = sharedPreferences.edit()

        setonClick(root)

        setView(root)




        return root
    }


    private fun setonClick(root:View)
    {
        var intent = Intent(root.context, CommunityMyMoreActivity::class.java)

        root.textLovedTextMore.setOnClickListener{
            intent.putExtra("click", "찜한 글")
            startActivity(intent)
        }

        root.textMyTextMore.setOnClickListener{
            intent.putExtra("click", "내가 쓴 글")
            startActivity(intent)
        }

        root.textMyReplyMore.setOnClickListener{
            intent.putExtra("click", "내가 쓴 댓글")
            startActivity(intent)
        }

        root.textViewMySetting.setOnClickListener{
            intent = Intent(root.context, UserSettingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setView(root: View)
    {
        userName = sharedPreferences.getString("userName", "")!!
        userMessage = sharedPreferences.getString("userMessage", "")!!
        userPhoto = sharedPreferences.getString("userPhoto", "")!!

        root.textMyName.text = userName
        root.textMyMessage.text = userMessage


        //TODO : 이미지 로딩 기능 적용 후 활성화
        //Glide.with(root.context).load(Uri.parse(userPhoto)).into(root.imageMyProfile)



    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}