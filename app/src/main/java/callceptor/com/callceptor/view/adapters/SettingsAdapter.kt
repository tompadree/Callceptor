package callceptor.com.callceptor.view.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import callceptor.com.callceptor.R
import callceptor.com.callceptor.domain.listeners.OnRemoveNumberClicked

/**
 * Created by Tom on 27.8.2018..
 */
class SettingsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    lateinit var context: Context
    lateinit var numbers: ArrayList<String>
    lateinit var onRemoveNumberClicked: OnRemoveNumberClicked
    private var isLoadingAdded: Boolean = false

    constructor()

    constructor(context: Context, numbers: ArrayList<String>, onRemoveNumberClicked: OnRemoveNumberClicked) : super() {
        this.context = context
        this.numbers = numbers
        this.onRemoveNumberClicked =  onRemoveNumberClicked
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return ResultItemHolder(LayoutInflater.from(context).inflate(R.layout.item_blocklist, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            holder as ResultItemHolder

            holder.itemBlockListTvNumber.text = numbers[position]
            holder.itemBlockListDeleteNumber.setOnClickListener { onRemoveNumberClicked.onRemoveNumberClicked(numbers[position]) }

    }


    override fun getItemCount(): Int {
        return numbers.size
    }

    class ResultItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.itemBlockListRelativeLayout)
        lateinit var itemBlockListRelativeLayout: RelativeLayout

        @BindView(R.id.itemBlockListTvNumber)
        lateinit var itemBlockListTvNumber: TextView

        @BindView(R.id.itemBlockListDeleteNumber)
        lateinit var itemBlockListDeleteNumber: ImageView

        init {
            ButterKnife.bind(this, itemView)
        }

    }

}
