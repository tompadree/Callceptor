package callceptor.com.callceptor.view.fragments

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import callceptor.com.callceptor.R
import callceptor.com.callceptor.data.models.Message
import callceptor.com.callceptor.di.component.DaggerMessagesComponent
import callceptor.com.callceptor.di.module.MessageModule
import callceptor.com.callceptor.domain.interactors.MessageInteractor
import callceptor.com.callceptor.domain.listeners.OnMessagesItemClicked
import callceptor.com.callceptor.presenter.MessagesPresenter
import callceptor.com.callceptor.view.BaseFragment
import callceptor.com.callceptor.view.adapters.MessagesAdapter
import callceptor.com.callceptor.view.views.MessagesView
import kotlinx.android.synthetic.main.fragment_messages.*
import java.util.ArrayList
import javax.inject.Inject

class MessagesFragment : BaseFragment(), MessagesView, OnMessagesItemClicked {

    @Inject
    lateinit var messageInteractor: MessageInteractor

    @Inject
    lateinit var messagesPresenter: MessagesPresenter

    lateinit var localMessages: ArrayList<Message>
    var messagesAdapter: MessagesAdapter? = null

    companion object {
        @JvmStatic
        fun newInstance() = MessagesFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerMessagesComponent.builder()
                .appComponent(getApplicationComponent())
                .messageModule(MessageModule(this))
                .build().inject(this)

    }

//    override fun onSaveInstanceState(outState: Bundle?) {
//        super.onSaveInstanceState(outState)
//
//        fragmentManager.putFragment(outState, "myFragmentName", this);
//        outState?.putParcelableArrayList("list", localMessages)
//    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        localMessages = ArrayList()

        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messagesPresenter.fetchMessages("")

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()

    }

    override fun showLoading() {
        fragmentMessagesProgressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        fragmentMessagesProgressBar.visibility = View.GONE
    }

    override fun callMessagesFetched(list: ArrayList<Message>) {
        if (localMessages.size == 0) {
            localMessages = list
            setupRecyclerView()
        } else {
            localMessages.addAll(list)
            fragmentMessagesRv.adapter.notifyDataSetChanged()
        }
        hideLoading()
    }

    fun setupRecyclerView() {
        messagesAdapter = MessagesAdapter(context!!, localMessages, this)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        fragmentMessagesRv.layoutManager = layoutManager
        fragmentMessagesRv.adapter = messagesAdapter

        fragmentMessagesRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalItemCount = layoutManager.itemCount
                val myTotalCount = totalItemCount - 84
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (dy > 0) { //dy scrolling down
                    if ((firstVisibleItemPosition >= myTotalCount) && firstVisibleItemPosition > totalItemCount - 30
                            && myTotalCount > 0 && localMessages.size <= totalItemCount)
                        messagesPresenter.fetchNextPage()
                }
            }
        })

        hideLoading()
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onMessageClicked(pos: Int) {
        Toast.makeText(context, "Clicked: " + pos, Toast.LENGTH_SHORT).show()
    }

    override fun showLoadingFooter() {
        if (messagesAdapter != null && localMessages.size > 0)
            messagesAdapter?.addLoadingFooter()
    }

    override fun hideLoadingFooter() {
        if (messagesAdapter != null && localMessages.size > 0)
            messagesAdapter?.removeLoadingFooter()
    }

    override fun showRefreshLoading() {
    }

    override fun hideRefreshLoading() {
    }
}
