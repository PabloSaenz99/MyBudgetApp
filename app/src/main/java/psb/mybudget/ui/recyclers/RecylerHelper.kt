package psb.mybudget.ui.recyclers

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Clase estatica que crea y devuelve un recycler lineal (creada para no repetir codigo)
 * @param data lista con los elementos a mostrar en el recycler
 * @param viewHolderClass clase encargada de soportar los datos del recycler
 * @param recyclerId identificador del recycler en la vista donde se llama a esta funcion
 * @param layoutId identificador del layout donde se llama a esta funcion
 * @param view vista de la clase donde se llama a esta funcion
 * @param orientation orientacion del layout, vertical u horizontal
 * @param <T> ViewHolder encargado de gestionar los datos proporcionados
 * @param <ELEMENT> tipo de dato proporcionado
 * @return el adaptador creado
</ELEMENT></T> */
fun <ViewHolder, ELEMENT> createLinearRecycler(
    data: Array<ELEMENT>, viewHolderClass: Class<ViewHolder>,
    @IdRes recyclerId: Int, @LayoutRes layoutId: Int, view: View,
    @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL
): MyRecycler<ViewHolder, ELEMENT> where ViewHolder : MyViewHolder<ELEMENT> {

    val recyclerView: RecyclerView = view.findViewById(recyclerId)
    recyclerView.layoutManager = LinearLayoutManager(view.context, orientation, false)
    val adapter: MyRecycler<ViewHolder, ELEMENT> = MyRecycler(data, layoutId, viewHolderClass)
    recyclerView.adapter = adapter

    return adapter
}

/**
 * Clase estatica que crea y devuelve un recycler de cuadricula(creada para no repetir codigo)
 * @param data lista con los elementos a mostrar en el recycler
 * @param viewHolderClass clase encargada de soportar los datos del recycler
 * @param recyclerId identificador del recycler en la vista donde se llama a esta funcion
 * @param layoutId identificador del layout donde se llama a esta funcion
 * @param view vista de la clase donde se llama a esta funcion
 * @param nColumns numero de columnas que tendrá el layout
 * @param <T> ViewHolder encargado de gestionar los datos proporcionados
 * @param <ELEMENT> tipo de dato proporcionado
 * @return el adaptador creado
</ELEMENT></T> */
fun <ViewHolder, ELEMENT> createGridRecycler(
    data: Array<ELEMENT>, viewHolderClass: Class<ViewHolder>,
    @IdRes recyclerId: Int, @LayoutRes layoutId: Int,
    view: View, nColumns: Int = 3
): MyRecycler<ViewHolder, ELEMENT> where ViewHolder : MyViewHolder<ELEMENT> {

    val recyclerView: RecyclerView = view.findViewById(recyclerId)
    recyclerView.layoutManager = GridLayoutManager(view.context, nColumns)
    val my: MyRecycler<ViewHolder, ELEMENT> = MyRecycler(data, layoutId, viewHolderClass)
    recyclerView.adapter = my
    return my
}

fun getGradientColorFromRes(context: Context, @ColorRes colorId: Int, perc: Double = 0.0): Int {
    //Get alpha and transform into decimal
    val alpha = Integer.toHexString((255*perc).toInt()).toInt(16)
    //Get color from res
    val color = ContextCompat.getColor(context, colorId)
    //Return color argb
    return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color))
}
fun getGradientColor(context: Context, @ColorRes colorId: Int, perc: Double = 0.0): Int {
    //Get alpha and transform into decimal
    val alpha = Integer.toHexString((255*perc).toInt()).toInt(16)
    //Get color from res
    val color = ContextCompat.getColor(context, colorId)
    //Return color argb
    return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color))
}

