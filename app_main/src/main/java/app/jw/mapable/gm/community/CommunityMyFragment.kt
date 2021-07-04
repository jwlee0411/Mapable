package app.jw.mapable.gm.community

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.jw.mapable.gm.databinding.FragmentCommunityMyBinding
import kotlinx.android.synthetic.main.fragment_community_my.view.*

class CommunityMyFragment : Fragment() {
    private var _binding : FragmentCommunityMyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommunityMyBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setonClick(root)




        return root
    }


    private fun setonClick(root:View)
    {
        val intent = Intent(root.context, CommunityMyMoreActivity::class.java)

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
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}