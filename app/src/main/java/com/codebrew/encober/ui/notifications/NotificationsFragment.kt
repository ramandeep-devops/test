package com.codebrew.encober.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codebrew.encober.R
import kotlinx.android.synthetic.main.fragment_main_notifications.*

class NotificationsFragment : Fragment() {

    companion object{
        const val TAG = "com.codebrew.vipcarts.ui.main.notifications.NotificationsFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_main_notifications, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        rvNotifications.layoutManager = LinearLayoutManager(requireActivity(),RecyclerView.VERTICAL,false)
        rvNotifications.adapter = NotificationsAdapter()

        vfNotifications.displayedChild = 1

    }

}