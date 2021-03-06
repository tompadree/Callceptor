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
import callceptor.com.callceptor.data.models.Call
import callceptor.com.callceptor.di.module.CallsModule
import callceptor.com.callceptor.presenter.CallsPresenter
import callceptor.com.callceptor.view.BaseFragment
import callceptor.com.callceptor.view.adapters.CallsAdapter
import callceptor.com.callceptor.view.views.CallsView
import kotlinx.android.synthetic.main.fragment_calls.*
import java.util.ArrayList
import javax.inject.Inject
import callceptor.com.callceptor.di.component.DaggerCallsComponent
import callceptor.com.callceptor.domain.interactors.CallsInteractor


class CallsFragment : BaseFragment(), CallsView {

    @Inject
    lateinit var callsPresenter: CallsPresenter

    @Inject
    lateinit var callsInteractor: CallsInteractor

    lateinit var localCalls: ArrayList<Call>
    var callsAdapter: CallsAdapter? = null

    companion object {

        @JvmStatic
        fun newInstance() = CallsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerCallsComponent.builder()
                .appComponent(getApplicationComponent())
                .callsModule(CallsModule(this))
                .build().inject(this)

        callsPresenter.destroy()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_calls, container, false)

        localCalls = ArrayList()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callsPresenter.fetchCallLogs("")
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun callLogsFetched(list: ArrayList<Call>) {

        if (localCalls.size == 0) {
            localCalls = list
            setupRecyclerView()
        } else {
            localCalls.addAll(list)
            fragmentCallsRv.adapter.notifyDataSetChanged()
        }
        hideLoading()

    }

    fun setupRecyclerView() {
        callsAdapter = CallsAdapter(context!!, localCalls)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        fragmentCallsRv.layoutManager = layoutManager
        fragmentCallsRv.adapter = callsAdapter

        fragmentCallsRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalItemCount = layoutManager.itemCount
                val myTotalCount = totalItemCount - 84
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (dy > 0) {
                    if ((firstVisibleItemPosition >= myTotalCount) && firstVisibleItemPosition > totalItemCount - 30
                            && myTotalCount > 0 && localCalls.size <= totalItemCount)
                        callsPresenter.fetchNextPage()
                }
            }
        })

        hideLoading()

    }

    override fun showLoading() {
        fragmentCallsProgressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        fragmentCallsProgressBar.visibility = View.GONE
    }

    override fun showLoadingFooter() {
        if (callsAdapter != null && localCalls.size > 0)
            callsAdapter?.addLoadingFooter()
    }

    override fun hideLoadingFooter() {
        if (callsAdapter != null && localCalls.size > 0)
            callsAdapter?.removeLoadingFooter()
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showRefreshLoading() {
    }

    override fun hideRefreshLoading() {
    }
}
