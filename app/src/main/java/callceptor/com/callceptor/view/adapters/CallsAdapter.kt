package callceptor.com.callceptor.view.adapters

import android.content.Context
import android.graphics.BitmapFactory
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
import java.io.File

/**
 * Created by Tom on 22.8.2018..
 */
class CallsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    lateinit var context: Context
    lateinit var calls: ArrayList<Call>
//    private var isLoadingAdded: Boolean = false
//    private lateinit var onListItemClicked: OnResultItemClicked

    constructor()

    constructor(context: Context, calls: ArrayList<Call>) : super() { // , onListItemClicked: OnResultItemClicked
        this.context = context
        this.calls = calls
//        this.onListItemClicked = onListItemClicked
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
//        return if (viewType == ITEM) {
        return ResultItemHolder(LayoutInflater.from(context).inflate(R.layout.item_calls, parent, false))
//        } else {
//            LoadingViewHolder(LayoutInflater.from(context).inflate(R.layout.item_git_result_loading, parent, false))
//        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        var viewType = getItemViewType(position)
//
//        if (viewType == ITEM) {
        holder as ResultItemHolder


        holder.itemCallsTvName.text = calls[position].name
        holder.itemCallsTvNumber.text = calls[position].number
        holder.itemCallsTvTime.text = calls[position].date

        when(calls[position].type){

            1 -> holder.itemCallsType.setImageResource(R.mipmap.ic_call_incoming)
            3 -> holder.itemCallsType.setImageResource(R.mipmap.ic_call_missed)
            4,5,6 -> holder.itemCallsType.setImageResource(R.mipmap.ic_call_blocked)


        }

//            holder.callsItem.setOnClickListener { onListItemClicked.onItemClicked(position) }


        if (calls[position].photo_uri != null)
            Picasso.get()
                    .load(Uri.parse(calls[position].photo_uri))
                    .placeholder(R.mipmap.ic_contact_placeholder)
                    .tag(context)
                    .resize(200, 200)
//                    .fit()
                    .centerCrop()
                    .into(holder.itemCallsImageView)
        else
            holder.itemCallsImageView.setImageResource(R.mipmap.ic_contact_placeholder)

    }

//    override fun getItemViewType(position: Int): Int {
//        return if (repos[position].createdAt != null) {
//            ITEM
//        } else {
//            LOADING
//        }
//    }

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

//    protected inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
//
//    fun add(repo: RepoObject) {
//        repos.add(repo)
//        notifyItemInserted(repos.size - 1)
//    }
//
//    fun addAll(repoList: List<RepoObject>) {
//        for (rl in repoList) {
//            add(rl)
//        }
//    }
//
//    fun remove(repo: RepoObject) {
//        val position = repos.indexOf(repo)
//        if (position > -1) {
//            repos.removeAt(position)
//            notifyItemRemoved(position)
//        }
//    }
//
//    fun clear() {
//        isLoadingAdded = false
//        while (itemCount > 0) {
//            remove(getItem(0))
//        }
//    }
//
//    fun isEmpty(): Boolean {
//        return itemCount == 0
//    }
//
//    fun addLoadingFooter() {
//        isLoadingAdded = true
//        add(RepoObject())
//    }
//
//    fun removeLoadingFooter() {
//        isLoadingAdded = false
//
//        val position = if (repos.size > 0) repos.size - 1 else 0
//
//        repos.removeAt(position)
//        notifyItemRemoved(position)
//
//    }
//
//    fun getItem(position: Int): RepoObject {
//        return repos[position]
//    }

}