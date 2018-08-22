package callceptor.com.callceptor.common.enums

/**
 * Created by Tom on 22.8.2018..
 */
enum class FragmentTag
constructor(fragmentTag: String) {

    CallsFragment("CallsFragment"),
    MessagesFragment("MessagesFragment"),
    SettingsFragment("SettingsFragment");

    private var fragmentTag: String = ""

    init {

        this.fragmentTag = fragmentTag

    }

    fun getTag(): String {
        return fragmentTag
    }

}