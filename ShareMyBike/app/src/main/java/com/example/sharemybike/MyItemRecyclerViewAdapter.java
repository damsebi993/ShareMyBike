package com.example.sharemybike;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sharemybike.placeholder.PlaceholderContent;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderContent.PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private List<Bike> mValues;

    public MyItemRecyclerViewAdapter(List<Bike> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.mItem = mValues.get(position);
        holder.mOwnerView.setText(mValues.get(position).getOwner());
        holder.mDescriptionView.setText(mValues.get(position).getDescription());
        // holder.mCountryView.setText(mValues.get(position).getCountry());
        holder.mCityView.setText(mValues.get(position).getCity());
        holder.mLocationView.setText(mValues.get(position).getLocation());
        // holder.mEmailView.setText(mValues.get(position).getEmail());
        holder.downloadPhoto(mValues.get(position));

        holder.mImageView.setImageBitmap(mValues.get(position).getPhoto());

        holder.mEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ownerEmail = mValues.get(position).getEmail();
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + ownerEmail));

                String subject = "Reserva de Bicicleta";
                String body = "Dear Mr/Mrs " + mValues.get(position).getOwner() + ":\n" +
                        "I'd like to use your bike at " + mValues.get(position).getLocation() + " (" + mValues.get(position).getCity() + ")\n" +
                        "for the following date: " + BikesContent.selectedDate + "\n" +
                        "Can you confirm its availability?\n" +
                        "Kindest regards";

                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, body);
                view.getContext().startActivity(intent);



            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        // public final TextView mCountryView;
        public final TextView mCityView;
        public final TextView mOwnerView;
        public final TextView mLocationView;
        public final TextView mDescriptionView;
        public final ImageButton mEmailButton;
        // public final TextView mEmailView;

        public Bike mItem;
        public StorageReference mStorageReference;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = view.findViewById(R.id.imgPhoto);
            // mCountryView = view.findViewById(R.id.txt);
            mOwnerView = view.findViewById(R.id.txtOwner);
            // mEmailView = view.findViewById(R.id.txt);
            mLocationView = view.findViewById(R.id.txtLocation);
            mCityView = view.findViewById(R.id.txtCity);
            mDescriptionView = view.findViewById(R.id.txtDescription);
            mEmailButton = view.findViewById(R.id.btnMail);
            mStorageReference = FirebaseStorage.getInstance().getReference(); // Inicializa el StorageReference
        }

        private void downloadPhoto(Bike c){
            mStorageReference= FirebaseStorage.getInstance().getReferenceFromUrl(c.getImage());
            try{
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                final File localFile=File.createTempFile("PNG_"+timeStamp,".png");
                mStorageReference.getFile(localFile).addOnSuccessListener(
                        new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                //Insert the downloaded image in its right position at the ArrayList
                                String url="gs://"+taskSnapshot.getStorage().getBucket()+"/"+
                                        taskSnapshot.getStorage().getName();;
                                Log.d(TAG,"Loaded "+url);
                                hacerAlgoConFoto(BitmapFactory.decodeFile(localFile.getAbsolutePath()));
                            }
                        });
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        private void hacerAlgoConFoto(Bitmap bitmap) {

            mImageView.setImageBitmap(bitmap);
        }
    }
}
