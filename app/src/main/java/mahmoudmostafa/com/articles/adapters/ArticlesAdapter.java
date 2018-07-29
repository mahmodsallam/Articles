package mahmoudmostafa.com.articles.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import mahmoudmostafa.com.articles.R;
import mahmoudmostafa.com.articles.activities.DetailsActivity;
import mahmoudmostafa.com.articles.model.Article;

/**
 * Created by Mahmoud Sallam on 2/21/2018.
 */

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.MyViewHolder> {


    ArrayList<Article> articlesList;
    Context context;
    DatabaseReference mDatabase;

    public ArticlesAdapter(ArrayList<Article> articlesList, Context context) {
        this.articlesList = articlesList;
        this.context = context;

        //getting the reference to the databse
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_list_item, parent,
                false);
        return (new MyViewHolder(view));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Article article = articlesList.get(position);
        Glide.with(context).load(article.getUrlToImage()).into(holder.articleImage);
        holder.articleTitle.setText(article.getTitle());
        holder.articleDate.setText(article.getPublishedAt());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("article", article);
                context.startActivity(intent);
            }
        });

        holder.bookMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context , R.string.bookMarkAlert , Toast.LENGTH_LONG).show();
                mDatabase.child("article").push().setValue(article) ;
            }
        });

    }

    @Override
    public int getItemCount() {
        return articlesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView articleImage , bookMark;
        TextView articleTitle, articleDate;

        public MyViewHolder(View itemView) {
            super(itemView);

            articleImage = itemView.findViewById(R.id.article_image);
            articleTitle = itemView.findViewById(R.id.article_title);
            articleDate = itemView.findViewById(R.id.article_Date);
            bookMark = itemView.findViewById(R.id.bookMark) ;

        }
    }

}
