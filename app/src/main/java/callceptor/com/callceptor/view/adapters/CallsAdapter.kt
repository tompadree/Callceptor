package callceptor.com.callceptor.view.adapters

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import callceptor.com.callceptor.data.models.Call
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso
import callceptor.com.callceptor.R
import callceptor.com.callceptor.utils.AppConstants.Companion.ITEM
import callceptor.com.callceptor.utils.AppConstants.Companion.LOADING

/**
 * Created by Tom on 22.8.2018..
 */
class CallsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    lateinit var context: Context
    lateinit var calls: ArrayList<Call>
    private var isLoadingAdded: Boolean = false

    constructor()

    constructor(context: Context, calls: ArrayList<Call>) : super() {
        this.context = context
        this.calls = calls
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM) {
            return ResultItemHolder(LayoutInflater.from(context).inflate(R.layout.item_calls, parent, false))
        } else {
            LoadingViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_loading, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var viewType = getItemViewType(position)

        if (viewType == ITEM) {
            holder as ResultItemHolder

            if (calls[position].name != null) {
                holder.itemCallsTvName.visibility = View.VISIBLE
                holder.itemCallsTvName.text = calls[position].name
            } else if (calls[position].callerID != null) {
                holder.itemCallsTvName.visibility = View.VISIBLE
                holder.itemCallsTvName.text = calls[position].callerID
            } else
                holder.itemCallsTvName.visibility = View.GONE

            holder.itemCallsTvNumber.text = calls[position].number
            holder.itemCallsTvTime.text = calls[position].date

            when (calls[position].type) {

                1 -> holder.itemCallsType.setImageResource(R.mipmap.ic_call_incoming)
                3 -> holder.itemCallsType.setImageResource(R.mipmap.ic_call_missed)
                4, 5, 6 -> holder.itemCallsType.setImageResource(R.mipmap.ic_call_blocked)

            }

            if (calls[position].photo_uri != null)
                Picasso.get()
                        .load(Uri.parse(calls[position].photo_uri))
                        .placeholder(R.mipmap.ic_contact_placeholder)
                        .tag(context)
                        .resize(200, 200)
                        .centerCrop()
                        .into(holder.itemCallsImageView)
            else
                holder.itemCallsImageView.setImageResource(R.mipmap.ic_contact_placeholder)

        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (calls[position].number != null) {
            ITEM
        } else {
            LOADING
        }
    }

    override fun getItemCount(): Int {
        return calls.size
    }

    class ResultItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.itemCallsMainRelativeLayout)
        lateinit var callsItem: RelativeLayout

        @BindView(R.id.itemCallsImageView)
        lateinit var itemCallsImageView: RoundedImageView

        @BindView(R.id.itemCallsTvName)
        lateinit var itemCallsTvName: TextView

        @BindView(R.id.itemCallsTvNumber)
        lateinit var itemCallsTvNumber: TextView

        @BindView(R.id.itemCallsTvTime)
        lateinit var itemCallsTvTime: TextView

        @BindView(R.id.itemCallsType)
        lateinit var itemCallsType: ImageView

        init {
            ButterKnife.bind(this, itemView)
        }

    }

    protected inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun add(call: Call) {
        calls.add(call)
        notifyItemInserted(calls.size - 1)
    }

    fun addAll(repoList: List<Call>) {
        for (rl in repoList) {
            add(rl)
        }
    }

    fun remove(call: Call) {
        val position = calls.indexOf(call)
        if (position > -1) {
            calls.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        isLoadingAdded = false
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    fun isEmpty(): Boolean {
        return itemCount == 0
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(Call())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

        val position = if (calls.size > 0) calls.size - 1 else 0

        calls.removeAt(position)
        notifyItemRemoved(position)

    }

    fun getItem(position: Int): Call {
        return calls[position]
    }
}
