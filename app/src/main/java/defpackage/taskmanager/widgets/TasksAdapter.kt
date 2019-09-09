/*
 * Copyright (c) 2019. Vlad Kalyuzhnyu <vladkalyuzhnyu@gmail.com>
 */

package defpackage.taskmanager.widgets

import android.widget.ArrayAdapter
import defpackage.taskmanager.data.models.Task

class AttractionsAdapter(items: ArrayList<Attraction>, ctx: Context) :
    ArrayAdapter<Task>(ctx, R.layout.attraction_item, items) {

    //view holder is used to prevent findViewById calls
    private class AttractionItemViewHolder {
        internal var image: ImageView? = null
        internal var title: TextView? = null
        internal var description: TextView? = null
        internal var hours: TextView? = null
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        var view = view

        val viewHolder: AttractionItemViewHolder

        if (view == null) {
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.attraction_item, viewGroup, false)

            viewHolder = AttractionItemViewHolder()
            viewHolder.title = view!!.findViewById<View>(R.id.title) as TextView
            viewHolder.description = view.findViewById<View>(R.id.description) as TextView
            viewHolder.hours = view.findViewById<View>(R.id.hours) as TextView
            //shows how to apply styles to views of item for specific items
            if (i == 3)
                viewHolder.hours!!.setTextColor(Color.DKGRAY)
            viewHolder.image = view.findViewById<View>(R.id.image) as ImageView
        } else {
            //no need to call findViewById, can use existing ones from saved view holder
            viewHolder = view.tag
        }

        val attraction = getItem(i)
        viewHolder.title!!.text = attraction!!.title
        viewHolder.description!!.text = attraction.description
        viewHolder.hours!!.text = attraction.hours
        viewHolder.image!!.setImageResource(attraction.image)

        //shows how to handle events of views of items
        viewHolder.image!!.setOnClickListener {
            Toast.makeText(
                context, "Clicked image of " + attraction.title,
                Toast.LENGTH_SHORT
            ).show()
        }

        view.tag = viewHolder

        return view
    }
}