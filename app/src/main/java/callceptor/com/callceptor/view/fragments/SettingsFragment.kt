package callceptor.com.callceptor.view.fragments

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import callceptor.com.callceptor.R
import callceptor.com.callceptor.domain.listeners.OnRemoveNumberClicked
import callceptor.com.callceptor.utils.AppConstants.Companion.BLOCK_LIST
import callceptor.com.callceptor.view.BaseFragment
import callceptor.com.callceptor.view.adapters.SettingsAdapter
import com.cinnamon.utils.storage.CinnamonPreferences
import kotlinx.android.synthetic.main.fragment_settings.*



@Suppress("UNCHECKED_CAST")
class SettingsFragment : BaseFragment(), OnRemoveNumberClicked {

    private var settingsAdapter: SettingsAdapter? = null
    private lateinit var blocklist: ArrayList<String>
    private var countryCode : String = "1"

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

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsFragmentCCP.setOnCountryChangeListener { country ->  countryCode = country.phoneCode}


        settingsFragmentAddNumberIv.setOnClickListener{
            if (settingsFragmentAddNumberTv.text.toString() == "") {
                Toast.makeText(activity, R.string.enter_a_number, Toast.LENGTH_SHORT).show()

            }else {

                blocklist.add("+" + countryCode + settingsFragmentAddNumberTv.text.toString())
                CinnamonPreferences.getInstance(context).setObject(BLOCK_LIST, blocklist)

                settingsAdapter?.notifyDataSetChanged()
                settingsFragmentAddNumberTv.hint = getString(R.string.enter_a_number)

                settingsFragmentCCP.setCountryForPhoneCode(1)
                countryCode = "1"
                settingsFragmentAddNumberTv.setText("")
            }


            if (activity?.currentFocus != null) {
                val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
            }
        }

        blocklist = (CinnamonPreferences.getInstance(context).getObject("blocklist", List::class.java, ArrayList<String>())) as ArrayList<String>
        setupRecyclerView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onRemoveNumberClicked(number: String) {
        blocklist.remove(number)
        settingsAdapter?.notifyDataSetChanged()
        CinnamonPreferences.getInstance(context).setObject(BLOCK_LIST, blocklist)
    }

    fun setupRecyclerView() {

        settingsAdapter = SettingsAdapter(context!!, blocklist as ArrayList<String>, this)

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
