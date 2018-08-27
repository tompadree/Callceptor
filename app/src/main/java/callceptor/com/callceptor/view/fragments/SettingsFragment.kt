package callceptor.com.callceptor.view.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import java.lang.Class
import callceptor.com.callceptor.R
import callceptor.com.callceptor.domain.listeners.OnRemoveNumberClicked
import callceptor.com.callceptor.utils.AppConstants.Companion.BLOCK_LIST
import callceptor.com.callceptor.view.BaseFragment
import callceptor.com.callceptor.view.adapters.SettingsAdapter
import com.cinnamon.utils.storage.CinnamonPreferences
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment(), OnRemoveNumberClicked {

    lateinit var unbinder: Unbinder
    private var settingsAdapter: SettingsAdapter? = null
    private lateinit var blocklist: ArrayList<String>
    private var countryCode : String = ""

    companion object {

        @JvmStatic
        fun newInstance() = SettingsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()

        unbinder.unbind()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_settings, container, false)

        unbinder = ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        blocklist = (CinnamonPreferences.getInstance(context).getObject("blocklist", List::class.java, ArrayList<String>())) as ArrayList<String>
        setupRecyclerView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()
    }


    @OnClick(R.id.settingsFragmentAddNumberIv)
    fun enterTagClicked() {

        if (settingsFragmentAddNumberTv.text.toString() == "") {
            Toast.makeText(activity, R.string.enter_a_number, Toast.LENGTH_SHORT).show()
            return
        }

        blocklist .add(settingsFragmentAddPreNumberTv.text.toString() + settingsFragmentAddNumberTv.text.toString())
        CinnamonPreferences.getInstance(context).setObject(BLOCK_LIST, blocklist)

        settingsAdapter?.notifyDataSetChanged()
        settingsFragmentAddNumberTv.setText(getString(R.string.enter_a_number))
        settingsFragmentAddPreNumberTv.setText("+1")

        if (activity.currentFocus != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view!!.getWindowToken(), 0)
        }
    }

    override fun onRemoveNumberClicked(number: String) {
        blocklist.remove(number)
        settingsAdapter?.notifyDataSetChanged()
        CinnamonPreferences.getInstance(context).setObject(BLOCK_LIST, blocklist)
    }

    fun setupRecyclerView() {
        settingsAdapter = SettingsAdapter(context, blocklist as ArrayList<String>, this)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        settingsFragmentRv.layoutManager = layoutManager
        settingsFragmentRv.adapter = settingsAdapter


        hideLoading()
    }

    fun hideLoading() {
        settingsFragmentProgress.visibility = View.GONE
    }

    fun showLoading() {
        settingsFragmentProgress.visibility = View.VISIBLE
    }


}
