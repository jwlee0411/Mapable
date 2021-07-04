package app.jw.mapable.gm.community

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.jw.mapable.gm.databinding.FragmentCommunitySearchBinding



class CommunitySearchFragment : Fragment() {

    private var _binding : FragmentCommunitySearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommunitySearchBinding.inflate(inflater, container, false)
        val root: View = binding.root






        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}