package com.emranul.weatherupdate.core.base.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.emranul.weatherupdate.core.base.util.autoCleared

abstract class BaseFragment<T : ViewDataBinding> constructor(@LayoutRes private val mContentLayoutId: Int) :
    Fragment() {

    private var navigationHost: NavigationHost? = null
    var binding by autoCleared<T>()
    var mToolbar: Toolbar? = null
        private set


    override fun onAttach(newBase: Context) {
        navigationHost = newBase as NavigationHost
        super.onAttach(newBase)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            mContentLayoutId,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        val rootView = binding.root
        initToolbar(rootView)

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mToolbar = null

    }

    override fun onDetach() {
        super.onDetach()
        navigationHost = null
    }


    private fun initToolbar(view: View) {
        if (hasToolbar && resToolbarId != 0) {
            mToolbar = view.findViewById(resToolbarId)
            mToolbar?.apply { navigationHost?.registerToolbarWithNavigation(this) }
        }
    }

    protected open val resToolbarId: Int = 0

    protected open val hasToolbar: Boolean = false

    protected fun activityScreenSwitcher() = navigationHost?.activityScreenSwitcher()

}
