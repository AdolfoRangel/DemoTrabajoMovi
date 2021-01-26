package com.kotlin.demotrabajo.main

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.demotrabajo.DetailMovie
import com.kotlin.demotrabajo.api.Movie
import com.kotlin.demotrabajo.databinding.MoviListItemBinding


class MoviAdaptar : ListAdapter<Movie, MoviAdaptar.MViewHolder>(
    DiffCallBack
) {
   private lateinit var context: Context

    private val TAG = MoviAdaptar::class.java.simpleName
    lateinit var onItemClickListener: (Movie) -> Unit

    companion object DiffCallBack : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewHolder {

        val binding: MoviListItemBinding =
            MoviListItemBinding.inflate(LayoutInflater.from(parent.context))
        return MViewHolder(binding)

        context = parent.context

    }

    override fun onBindViewHolder(holder: MViewHolder, position: Int) {
        val movie: Movie = getItem(position)
        holder.bind(movie)
    }

    inner class MViewHolder(private val binding: MoviListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movi: Movie) {

            val bmp :Bitmap = BitmapFactory.decodeByteArray(movi.image, 0, movi.image.size)

            binding.moviImage.setImageBitmap(bmp)

            binding.nameText.text = movi.name

            
            if(movi.adultos.equals("true")){
                binding.publico.text="Apto para todo el publico"
            }else{
                binding.publico.text="No apto para todo el publico"
            }

            binding.root.setOnClickListener {
                if (::onItemClickListener.isInitialized) {
                    onItemClickListener(movi)
                } else {
                    Log.e(TAG, "On itemClickListener not initialized")
                }
            }
            //con el binding tarda 16ml segundos ena ctualizar el item, con esta linea lo oblicamos a que sea inmediatamente
            binding.executePendingBindings()

        }
    }
}