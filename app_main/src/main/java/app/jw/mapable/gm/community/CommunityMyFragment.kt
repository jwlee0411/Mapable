package app.jw.mapable.gm.community

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.jw.mapable.gm.databinding.FragmentCommunityMyBinding

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


        //TODO : 여기에 코드 입력



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}