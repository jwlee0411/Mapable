package app.jw.mapable.gm.community

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import app.jw.mapable.gm.databinding.FragmentCommunitySearchBinding
import kotlinx.android.synthetic.main.fragment_community_search.view.*


class CommunitySearchFragment : Fragment() {

    private var _binding : FragmentCommunitySearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommunitySearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        root.editCommunitySearch.requestFocus()
        val inputMethodManager = root.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

        root.editCommunitySearch.addTextChangedListener{

        }








        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}